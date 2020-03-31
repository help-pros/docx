<https://blog.csdn.net/ThinkWon/article/details/101020481>

# IDEA 永久激活

<https://segmentfault.com/a/1190000021220727?utm_source=tag-newest>

# 常用配置

## @Autowired提示问题

- ### IDEA报错Could not autowire. No beans of ‘xxxxMapper’ type found

- 解决办法是：降低Autowired检测的级别，将Severity的级别由之前的error改成warning

![](C:/Users/Administrator/Desktop/notes/%E4%B9%B1%E4%B8%83%E5%85%AB%E7%B3%9F/images/QQ%E6%88%AA%E5%9B%BE20191207164153.png)

## Auto import

![](C:/Users/Administrator/Desktop/notes/%E4%B9%B1%E4%B8%83%E5%85%AB%E7%B3%9F/images/QQ%E6%88%AA%E5%9B%BE20191207164329.png)

- 

## 配置JDK

在IDEA启动页面中，下拉Configure，选择Project Defaults – Project Structure，这样可以设置所有项目的默认的JDK版本，如下图

![](images/20190919134900196.png)

## 文件编码

![](images/aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL0pvdXJXb24vaW1hZ2UvbWFzdGVyL0lERUElRTUlQjglQjglRTclOTQlQTglRTklODUlOEQlRTclQkQlQUUlRTUlOTIlOEMlRTUlQjglQjglRTclOTQlQTglRTYlOEYlOTIlRTQlQkIlQjY.jpg)

## 文件和代码模板

![](images/20190919134940157.png)

## Maven配置

![](images/aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL0pvdXJXb24vaW1hZ2UvbWFzdGVyL0lERUElRTUlQjglQjglRTclOTQlQTglRTklODUlOEQlRTclQkQlQUUlRTUlOTIlOEMlRTUlQjglQjglRTclOTQlQTglRTYlOEYlOTIlRTQlQkI (1).jpg)

## 自动导入Maven依赖

![](images/20190919135012549.png)

## Java代码单行注释添加空格

![](images/2019121509061531.png)

## 优化导入和智能删除无关依赖

![](images/20190919135155737.png)

## 修改主题

![](images/20190919135213154.png)

## 修改字体

![](images/20190919135228539.png)

## 代码提示不区分大小写

![](images/20190919135244518.png)

## 显示行数和方法线

![](images/20190919135259168.png)

## 目录展示设置

![](images/aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL0pvdXJXb24vaW1hZ2UvbWFzdGVyL0lERUElRTUlQjglQjglRTclOTQlQTglRTklODUlOEQlRTclQkQlQUUlRTUlOTIlOEMlRTUlQjglQjglRTclOTQlQTglRTYlOEYlOTIlRTQlQkI (2).jpg)

## 打开IDEA项目选择

![](images/20190919135317919.png)

## 代码自动提示快捷键

![](images/20190919135333462.png)

设置Basic快捷键为Alt+斜杠

![](images/20190919135358583.png)

## 全局修改文件描述信息（推荐）

```java
/**
 * Description:
 *
 * @author zb
 * @date ${DATE} ${TIME}
 */
```



![](images/20190919135417548.png)

## 生成方法注释

1.打开Preferences

2.Editor -> Live Templates -> 点击右边加号为自己添加一个Templates Group -> 然后选中自己的Group再次点击加号添加Live Templates

![](images/20190919135512519.png)

3.然后设置自己喜欢的快捷键 在Abbreviation里面 记得在Applicable in 里面勾选Java

![](images/20190919135523250.png)

4.然后在Edit variables里面添加参数和返回值的自动取值

```java
*
 * Description: $Description$
 *
 * @author JourWon
 * @date $DATE$ $TIME$
$param$
 * @return $return$
 */
```



![](images/20190919135539209.png)

5.然后再你的方法上面直接输入`/*+*+Tab`

## IDEA生成序列号serialVersionUID

设置完成后，按Alt+Enter键，这个时候可以看到"Add serialVersionUID field"提示信息

![](images/20190919135641281.png)

# 安装的插件

- 阿里巴巴代码规范：Alibaba java coding guidelines 

- git提交详细信息查看：GitToolBox

- java类库：Lombok

- ### JSON转领域对象工具：GsonFormat

- ### 领域对象转JSON工具：POJO to JSON

- ### 代码作色工具：Rainbow Brackets

- ### 日志工具：Grep Console

- ### Redis可视化：Iedis

- ### K8s工具：Kubernetes

- ### 中英文翻译工具：Translation

- ### 一个对象的所有set方法：GenerateAllSetter

- ### mybatis代码自动生成：MyBatisCodeHelperPro

- <https://mp.weixin.qq.com/s/zyIKY0Bc7DXic7kQN-zuRA>

- **Free Mybatis plugin -Mybatis 辅助插件**