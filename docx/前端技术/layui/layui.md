# 表单

## 表单校验 

### lay-verify

- required ：必填项
- phone：手机号
- email：邮箱
- url：网址
- number：数字
- date：日期
- identity：身份证
- 自定义值

## 弹出层 

### 关闭 

### 关闭特定层

```js
var index = layer.open();
var index = layer.alert();
var index = layer.load();
var index = layer.tips();

layer.close(index);

//如果你想关闭最新弹出的层，直接获取layer.index即可
layer.close(layer.index); //它获取的始终是最新弹出的某个层，值是由layer内部动态递增计算的

//当你在iframe页面关闭自身时
var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
parent.layer.close(index); //再执行关闭 
```



### **关闭所有层** 

```js
layer.closeAll(); //疯狂模式，关闭所有层
layer.closeAll('dialog'); //关闭信息框
layer.closeAll('page'); //关闭所有页面层
layer.closeAll('iframe'); //关闭所有的iframe层
layer.closeAll('loading'); //关闭加载层
layer.closeAll('tips'); //关闭所有的tips层   
```



# 搜索下拉框

```html
<select name="modules" lay-search="" id="pname">
  <!-- <option value="1" selected="selected">全部</option> -->
  <!-- <option value="2">2</option> -->
</select>
```

```js
$('#pname').empty();//清空下拉框
$.each(data,function(index,item){
  $('#pname').append(new Option(item.name,item.id));// 下拉菜单里添加元素
  //$("#pname").append('<option value='+value+'>'+value+'</option>')
})
form.render();//下拉菜单渲染 把内容加载进去
//form.render('select');
```

# 模块化与非模块化用法 

- 模块化：最终使用的时候才会自动加载
- 非模块化：所有模块一次性加载

# 多级菜单 

- ```javascript
  layui.define(['layer'], function(exports) {
  	"use strict";

  	var $ = layui.jquery,
  		layer = layui.layer;

  	var common = {
  		/**
  		 * 抛出一个异常错误信息
  		 * @param {String} msg
  		 */
  		throwError: function(msg) {
  			throw new Error(msg);
  			return;
  		},
  		/**
  		 * 弹出一个错误提示
  		 * @param {String} msg
  		 */
  		msgError: function(msg) {
  			layer.msg(msg, {
  				icon: 5
  			});
  			return;
  		}
  	};

  	exports('common', common);
  });
  ```

- ```javascript
  /**
   * 导航栏
   * @param exports
   * @returns
   */
  layui.define(['element', 'common'], function (exports) {
      "use strict";
      var $ = layui.jquery,
          layer = parent.layer === undefined ? layui.layer : parent.layer,
          element = layui.element(),
          common = layui.common,
          cacheName = 'tb_navbar';

      var Navbar = function () {
  		/**
  		 *  默认配置 
  		 */
          this.config = {
              elem: undefined, //容器
              data: undefined, //数据源
              url: undefined, //数据源地址
              type: 'GET', //读取方式
              cached: false, //是否使用缓存
              spreadOne: false //设置是否只展开一个二级菜单
          };
          this.v = '1.0.0';
      };
      //渲染
      Navbar.prototype.render = function () {
          var _that = this;
          var _config = _that.config;
          if (typeof (_config.elem) !== 'string' && typeof (_config.elem) !== 'object') {
              common.throwError('Navbar error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
          }
          var $container;
          if (typeof (_config.elem) === 'string') {
              $container = $('' + _config.elem + '');
          }
          if (typeof (_config.elem) === 'object') {
              $container = _config.elem;
          }
          if ($container.length === 0) {
              common.throwError('Navbar error:找不到elem参数配置的容器，请检查.');
          }
          if (_config.data === undefined && _config.url === undefined) {
              common.throwError('Navbar error:请为Navbar配置数据源.')
          }
          if (_config.data !== undefined && typeof (_config.data) === 'object') {
              var html = getHtml(_config.data);
              $container.html(html);
              element.init();
              _that.config.elem = $container;
          } else {
              if (_config.cached) {
                  var cacheNavbar = layui.data(cacheName);
                  if (cacheNavbar.navbar === undefined) {
                      $.ajax({
                          type: _config.type,
                          url: _config.url,
                          async: false, //_config.async,
                          dataType: 'json',
                          success: function (result, status, xhr) {
                              //添加缓存
                              layui.data(cacheName, {
                                  key: 'navbar',
                                  value: result
                              });
                              var html = getHtml(result);
                              $container.html(html);
                              element.init();
                          },
                          error: function (xhr, status, error) {
                              common.msgError('Navbar error:' + error);
                          },
                          complete: function (xhr, status) {
                              _that.config.elem = $container;
                          }
                      });
                  } else {
                      var html = getHtml(cacheNavbar.navbar);
                      $container.html(html);
                      element.init();
                      _that.config.elem = $container;
                  }
              } else {
                  //清空缓存
                  layui.data(cacheName, null);
                  $.ajax({
                      type: _config.type,
                      url: _config.url,
                      async: false, //_config.async,
                      dataType: 'json',
                      success: function (result, status, xhr) {
                          var html = getHtml(result);
                          $container.html(html);
                          element.init();
                      },
                      error: function (xhr, status, error) {
                          common.msgError('Navbar error:' + error);
                      },
                      complete: function (xhr, status) {
                          _that.config.elem = $container;
                      }
                  });
              }
          }

          //只展开一个二级菜单
          if (_config.spreadOne) {
              var $ul = $container.children('ul');
              $ul.find('li.layui-nav-item').each(function () {
                  $(this).on('click', function () {
                      $(this).siblings().removeClass('layui-nav-itemed');
                  });
              });
          }
          return _that;
      };
  	/**
  	 * 配置Navbar
  	 * @param {Object} options
  	 */
      Navbar.prototype.set = function (options) {
          var that = this;
          that.config.data = undefined;
          $.extend(true, that.config, options);
          return that;
      };
  	/**
  	 * 绑定事件
  	 * @param {String} events
  	 * @param {Function} callback
  	 */
      Navbar.prototype.on = function (events, callback) {
          var that = this;
          var _con = that.config.elem;
          if (typeof (events) !== 'string') {
              common.throwError('Navbar error:事件名配置出错，请参考API文档.');
          }
          var lIndex = events.indexOf('(');
          var eventName = events.substr(0, lIndex);
          var filter = events.substring(lIndex + 1, events.indexOf(')'));
          if (eventName === 'click') {
              if (_con && _con.attr('lay-filter') !== undefined) {
                  _con.children('ul').find('li').each(function () {
                      var $this = $(this);
                      if ($this.find('dl').length > 0) {
                          var $dd = $this.find('dd').each(function () {
                              $(this).on('click', function () {
                                  var $a = $(this).children('a');
                                  var href = $a.data('url');
                                  var icon = $a.children('i:first').data('icon');
                                  var title = $a.children('cite').text();
                                  var data = {
                                      elem: $a,
                                      field: {
                                          href: href,
                                          icon: icon,
                                          title: title
                                      }
                                  }
                                  callback(data);
                              });
                          });
                      } else {
                          $this.on('click', function () {
                              var $a = $this.children('a');
                              var href = $a.data('url');
                              var icon = $a.children('i:first').data('icon');
                              var title = $a.children('cite').text();
                              var data = {
                                  elem: $a,
                                  field: {
                                      href: href,
                                      icon: icon,
                                      title: title
                                  }
                              }
                              callback(data);
                          });
                      }
                  });
              }
          }
      };
  	/**
  	 * 清除缓存
  	 */
      Navbar.prototype.cleanCached = function () {
          layui.data(cacheName, null);
      };
  	/**
  	 * 获取html字符串
  	 * @param {Object} data
  	 */
      function getHtml(data) {
          //debugger;
          var ulHtml = '<ul class="layui-nav layui-nav-tree beg-navbar">';
          // 遍历生成主菜单
          for( var i = 0; i <data.length; i++){
              // 判断是否存在子菜单
          	if(data[i].spread){
          		ulHtml+="<li class=\"layui-nav-item layui-nav-itemed\">";
          	}else{
          		ulHtml+="<li class=\"layui-nav-item\">";
          	}
              if(data[i].children!=null&&data[i].children.length>0){
                  ulHtml+="<a class=\"\" href=\"javascript:;\"><i class='layui-icon' data-icon='"+data[i].icon+"'>"+data[i].icon+"</i><cite>" + data[i].title + "</cite></a>\n"+
                              "<dl class=\"layui-nav-child\">\n";
                  // 遍历获取子菜单
                  for( var k = 0; k <data[i].children.length; k++){
                      ulHtml+=getChildMenu(data[i].children[k],0);
                  }
                  ulHtml+="</dl></li>";
              }else{
                  ulHtml+="<a class=\"\" data-url=\""+data[i].href+"\"><i class='layui-icon' data-icon='"+data[i].icon+"'>"+data[i].icon+"</i><cite>" + data[i].title + "</cite></a></li>";
              }
          };
          ulHtml += '</ul>';

          return ulHtml;
      }

      var navbar = new Navbar();

      exports('navbar', function (options) {
          return navbar.set(options);
      });
  });

  //递归生成子菜单
  function getChildMenu(subMenu,num) {
  	var menuCell = 5;
      num++;
      var subStr = "";
      if(subMenu.children!=null&&subMenu.children.length>0){
      	if(subMenu.spread){
      		subStr+="<li class=\"layui-nav-item layui-nav-itemed\">";
      	}else{
      		subStr+="<li class=\"layui-nav-item\">";
      	}
      	subStr+="<a class=\"\" href=\"javascript:;\"><i class='layui-icon' data-icon='"+subMenu.icon+"'> "+subMenu.icon+"</i><cite>" + subMenu.title + "</cite></a>\n"+
           "<dl class=\"layui-nav-child\">\n";
          for( var k = 0; k <subMenu.children.length; k++){
          	subStr+=getChildMenu(subMenu.children[k],0);
          }
          subStr+="</dl></li>";
      }else{
      	subStr+="<dd><a style=\"margin-Left:"+num*menuCell+"px\" data-url=\""+subMenu.href+"\"><i class='layui-icon'data-icon= '"+subMenu.icon+"'> "+subMenu.icon+"</i><cite>" + subMenu.title + "</cite></a></dd>";
      }
      return subStr;
  }
  ```

- ```javascript
  layui.config({
  	base: '../layui/extra/js/'
  }).use(['layer','element','navbar','tab'],function(){
  	var element = layui.element,
  	$ = layui.$;
  	layer = layui.layer,
  	navbar = layui.navbar(),
  	tab = layui.tab({
          elem: '.admin-nav-card' //设置选项卡容器
      });
  	
  	http.ajax({
  		url:"http://localhost:8030/microservice-provider-web/getAllMenu",
  		type:"GET",
  		beforeSend:function(xhr){
  			var authorication = localStorage.getItem("Authorication");
  			xhr.setRequestHeader("Authorication",authorication);
  		},
  		success:function(data){
  			if(data.code == "200"){
  				var menu = navbarData(data.data);
  				//设置navbar
  				navbar.set({
  					spreadOne: true,
  					elem: '#admin-navbar-side',
  					cached: true,
  					data: menu//navs
  				});
  				//渲染navbar
  				navbar.render();
  				//监听点击事件
  				navbar.on('click(side)', function (data) {
  					console.log(data);
  					tab.tabAdd(data.field);
  				});
  			}else if(data.code == "401"){
  				window.location.href="login.html"; 
  			}else{
  				layer.msg(data.msg);
  			}
  		}
  	});
  });
  ```

- 

# 树结构 

- layui.css
- layui.js
- front
- layui.atree

> spreadAll：设置CheckBox全部选中
>
> check：勾选风格，不写没有勾选框
>
> - props：设置key属性别名
>   - addBtnLabel：新增按钮标题
>   - deleteBtnLable：删除按钮标题
>   - name：树显示的标题
>   - id：主键对应的字段名
>   - children：子类对应的字段名
>   - checkbox：选中对应的字段名
>   - spread：是否展开对应的字段名
>
> - change：选中回调函数
>   - val：选中的对象数组
>
> - click：点击标题回调函数
>   - item：当前点击的对象
>
> - addClick：新增回调函数
>
>   - item：当前父节点的对象
>
>   - elem：当前节点的dom对象
>   - done：添加到dom节点的方法
>
> - deleteClick：删除回调函数
>   - item：当前父节点的对象
>   - elem：当前节点的dom对象
>   - done：删除dom节点的方法

# 大坑 

## 弹出层 

- 在iframe层关闭弹出层，file:index.html，在谷歌不可以，在火狐浏览器可用

  - ```js
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    					parent.layer.close(index); //再执行关闭
    ```

- [ImportUser(userId=null, birthAddress=null, birthday=null, bloodType=null, eduction=null, liveAddress=null, sex=null, userEmail=null, userLogo=null, userName=xiao, userPhone=13700013700.00, work=null, created=null)]