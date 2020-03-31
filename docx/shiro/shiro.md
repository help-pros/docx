# shiro简介

Apache Shiro 是java的一个安全（权限）框架。

（spring security 过于庞大、复杂）

shiro可以完成：认证、授权、加密、会话管理、与web集成、缓存等。

![](images/QQ截图20180906164111.png)



![](images/QQ截图20180906164355.png)



![](images/QQ截图20180906165129.png)

![](images/QQ截图20180906165148.png)

![](images/QQ截图20180906165516.png)

# shiroFilter的工作原理

```mermaid
graph LR
F[shiroFilter的工作原理]
A[浏览器]-->B(filterChainDefinitions)
A-->G(loginUrl)
B-->C{经过认证或不被拦截的页面}
G-->H{没有经过认证}
```

细节：shiroFilter的id必须要和在web.xml文件中配置的DelegatingFilterProxy 的<filter-name> 一致。

若不一致，则会抛出NoSuchBeanDefinitionException。因为Shiro会来IOC容器中查找和<filter-name>名字对应的filter bean.

DelegatingFilterProxy实际上是Filter的一个代理对象。默认情况下，spring 会来IOC容器中查找和<filter-name>名字对应的filter bean.也可以通过targetBeanName 的初始化参数来配置filter bean 的id

 ![](images/QQ截图20180907103920.png)

# URL 匹配模式

![](images/QQ截图20180907104154.png)

URL 权限配置有顺序的：**URL权限采取第一次匹配优先的方式，即从头开始使用第一个匹配的url模式对应的拦截器链。** 

# 认证

即登录

1. 获取当前Subject。调用SecurityUtils.getSubject();

2. 测试当前的用户是否已经被认证。即是否已经登录。调用Subject的isAuthenticated()

3. 若没有被认证，则把用户名和密码封装为UsernamePasswordToken对象

   1）创建一个表单页面

   2）把请求提交到SpringMVC的Handler

   3）获取用户名和密码

4. 执行登录：调用Subject的login()方法（UsernamePasswordToken是Authentication的实现类）

5. 自定义Realm的方法，从数据库中获取对应的记录，返回给Shiro

   1）（Realm是个接口）实际上需要继承 org.apache.shiro.realm.AuthenticatingRealm类

   2）实现doGetAuthenticationInfo(AuthenticationToken)方法

6. 由shiro完成对密码的比对。

   密码的比对： 通过AuthenticatingRealm的creadentialsMathcher 属性来进行的密码的对比

   ​  如何把一个字符串加密为MD5

   替换当前Realm的credentialsMatcher属性。直接使用HashedCredent ialsMatcher对象，并设置加密算法即可。

   出现：同样的密码会出现同样的加密串，不安全，所以要达到同样密码，字符串也是不一样的，解决办法：

   MD5盐值加密（salt值的设置）

   1）在doGetAuthenticationInfo 方法返回值创建SimpleAuthenticationInfo 对象的时候，需要使用 SimpleAUthenticationInfo(principal,credentials,credentialsSalt,realName)构造器

   2）使用ByteSource.Util.bytes()来计算盐值

   3）盐值需要唯一：一般使用随机字符串或user id

   4）使用new SimpleHash（hashAlgorithmName,credentials,salt,hashIterations）;来计算盐值加密后的密码的值

   （shiro有缓存，所以做测试时，要有登出再登入）

 （shiro有缓存，所以做测试时，要有登出再登入）

![](images/QQ截图20180907111927.png)

![](images/QQ截图20180907111946.png)



![](images/QQ截图20180907112044.png)



 



# 多Realm验证

 配置认证器：ModularRealmAuthenticator 

认证策略：（多个realm时，怎样认证成功）：AuthenticationStrategy

![](images/QQ截图20180910145219.png)



# 授权

![](images/QQ截图20180910151344.png)



![](images/QQ截图20180910151655.png)



![](images/QQ截图20180910151806.png)

## shiro 自带过滤器

![](images/QQ截图20180910152127.png)



![QQ截图20180910152000.png](images/QQ截图20180910152000.png)

 

 认证的Realm继承AuthenticatingRealm，实现doGetAuthenticationInfo方法

授权继承AuthenticatingRealm子类AuthorizingRealm类，并实现其抽象方法doGetAuthorizationInfo，

但没有实现doGetAuthenticationInfo方法，所以认证和授权只需要继承AuthorizingRealm就可以了。同时实现他的两个抽象方法。

1）从PrincipalCollection中来获取登录用户的信息

2）利用登录的用户信息来获取当前用户的角色或权限（可能需要查询数据库）

3）创建SimpleAUthorizationInfo，并设置其reles属性

4）返回SimpleAuthorizationInfo对象

![](images/QQ截图20180910173049.png)



# Shiro标签

![](images/QQ截图20180910173233.png)



![](images/QQ截图20180910173252.png)



![](images/QQ截图20180910173445.png)



![](images/QQ截图20180910174859.png)

![](images/QQ截图20180910175434.png)

![](images/QQ截图20180910175354.png)



![](images/QQ截图20180910175336.png)







![](images/QQ截图20180910174949.png)

![](images/QQ截图20180910175013.png)

# 权限注解

![](images/QQ截图20180910175825.png)



# 从数据表中初始化资源和权限

Map集合：key:value形式

（配置一个bean，该bean实际上是一个Map。通过实例工厂方法的方式）

![](images/QQ截图20180910183015.png)

# 会话管理

（java web中的HttpSession一致）

shiro提供了完整的企业级会话管理功能，不依赖与底层容器（如web容器tomcat），不管javaSE还是javaEE环境都可以使用，提供了会话管理、会话事件监听、会话存储/持久化、容器无关的集群、失效/过期支持、对web的透明支持、SSO单点登录的支持等特性。

![](images/QQ截图20180910183621.png)



  ![](images/QQ截图20180910183725.png)



![](images/QQ截图20180910183748.png)



![](images/QQ截图20180910184317.png)

## SessionDao

![](images/QQ截图20180910184453.png)



![](images/QQ截图20180910184608.png)



## 会话验证

![](images/QQ截图20180910184953.png)

# shiro 缓存

  CacheManagerAware接口

![](images/QQ截图20180910185149.png)

# Remember Me

![](images/QQ截图20180910185546.png)

![](images/QQ截图20180910185718.png)

![](images/QQ截图20180910185814.png)

## 例子

![](images/QQ截图20180910190212.png)

![](images/QQ截图20180910190758.png)

## ShiroConfig 

- apache shiro 核心通过filter来实现，就好像spring mvc通过dispachServlet来主控制一样

- ```java
  package com.zhangbin.cloud.conf;

  import java.util.LinkedHashMap;
  import java.util.Map;

  import javax.servlet.Filter;

  import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
  import org.apache.shiro.mgt.DefaultSubjectDAO;
  import org.apache.shiro.mgt.SecurityManager;
  import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
  import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;

  /**
   * ShiroConfig
   * 
   * @author admin
   *
   */
  @Configuration
  public class ShiroConfig {

  	/**
  	 * 
  	 * @param securityManager
  	 *            SecurityManager：管理所有subject，SecurityManager是shiro架构的核心，配合内部安全组件共同组成安全伞
  	 *            Realms：用于进行权限信息的验证，自己实现。Realm本质上是一个特定的安全DAO，它封装与数据源连接的细节，得到shiro所需的相关的数据。在配置shiro的时候，你必须指定至少一个realm来实现认证
  	 * @return
  	 */
  	@Bean
  	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
  		System.out.println("ShiroConfiguration.shirFilter()");
  		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
  		shiroFilterFactoryBean.setSecurityManager(securityManager);
  		// 设置自定义的jwt过滤器
  		Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
  		filters.put("jwt", new JwtFilter());
  		shiroFilterFactoryBean.setFilters(filters);
  		// 拦截器
  		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
  		// 配置不会被拦截的链接，顺序判断
  		/**
  		 * anon：匿名拦截器，即不需要登录即可访问 logout：退出拦截器 authc:基于表单的拦截器，如果没有登录会跳转到相应的登录页面登录
  		 * roles:角色授权拦截器，验证用户是否拥有所有角色 perms:权限授权拦截器，验证用户是否拥有所有权限 port：端口拦截器
  		 */
  		filterChainDefinitionMap.put("/webjars/**", "anon");
  		filterChainDefinitionMap.put("/v2/api-docs", "anon");
  		filterChainDefinitionMap.put("/**/favicon.ico", "anon");
  		filterChainDefinitionMap.put("/swagger-resources/**", "anon");
  		filterChainDefinitionMap.put("/swagger-ui.html", "anon");
  		filterChainDefinitionMap.put("/doc.html", "anon");
  		filterChainDefinitionMap.put("/druid/**", "anon");
  		filterChainDefinitionMap.put("/login", "anon");
  		// 配置退出，过滤器，其中的具体的退出代码shiro已经替我们实现了
  		filterChainDefinitionMap.put("/logout", "logout");
  		/**
  		 * 过滤器定义，从上向下顺序执行，一般将/**放在最为下边 authc：所有url都必须认证通过才可以访问；anon：所有url都可以匿名访问
  		 */

  		filterChainDefinitionMap.put("/unauthorized/**", "anon");
  		filterChainDefinitionMap.put("/**", "jwt");
  		// filterChainDefinitionMap.put("/**", "authc");
  		// 如果不设置默认会自动寻找web工程根目录下的/login页面
  		// 前后端分离中，登录界面跳转应由前端路由控制，后台仅返回json数据
  		shiroFilterFactoryBean.setLoginUrl("/notLogin");
  		// 登录成功后要跳转的链接
  		// shiroFilterFactoryBean.setSuccessUrl("/index");
  		// 未授权界面,无权限时跳转的url
  		// shiroFilterFactoryBean.setUnauthorizedUrl("/notRole");
  		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

  		return shiroFilterFactoryBean;
  	}
  	/**
  	 * 
  	 * @return
  	 */
  	/*
  	 * @Bean public MyShiroRealm myShiroRealm() { MyShiroRealm myShiroRealm = new
  	 * MyShiroRealm(); return myShiroRealm; }
  	 */

  	/**
  	 * 
  	 * @return
  	 */
  	@Bean
  	public SecurityManager securityManager(MyShiroRealm myShiroRealm) {
  		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
  		securityManager.setRealm(myShiroRealm);

  		/**
  		 * 关闭shiro自带的session，用自定义的jwt token做无状态登录校验
  		 */
  		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
  		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
  		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
  		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
  		securityManager.setSubjectDAO(subjectDAO);

  		return securityManager;
  	}
  }

  ```

- ```java
  package com.zhangbin.cloud.conf;

  import org.apache.shiro.authc.AuthenticationException;
  import org.apache.shiro.authc.AuthenticationInfo;
  import org.apache.shiro.authc.AuthenticationToken;
  import org.apache.shiro.authc.SimpleAuthenticationInfo;
  import org.apache.shiro.authz.AuthorizationInfo;
  import org.apache.shiro.realm.AuthorizingRealm;
  import org.apache.shiro.subject.PrincipalCollection;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Component;

  import com.zhangbin.cloud.service.UserService;

  /**自定义权限匹配和账号密码匹配
   * @author admin
   *
   */
  @Component
  public class MyShiroRealm extends AuthorizingRealm {
  	
  	@Autowired
  	private UserService userService;
  	
  	
  	/**
       * 必须重写此方法，不然会报错
       */
      @Override
      public boolean supports(AuthenticationToken token) {
          return token instanceof JwtToken;
      }

  	
  	/**
  	 * Authorization(授权)：访问控制。比如某个用户是否具有某个操作的使用权限
  	 * Session Management（会话管理）：特定于用户的会话管理，甚至在非web或EJB应用程序
  	 * Cryptography（加密）：在对数据源使用加密算法加密的同时，保证易于使用
  	 * 授权访问控制，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件
  	 */
  	@Override
  	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
  		System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
  		return null;
  	}
  	/**
  	 * Authentication(认证)：用户身份识别，通常都被称为用户“登录”
  	 * 验证用户身份
  	 */
  	@Override
  	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
  		System.out.println("————身份认证方法————");
  		//获取用户的输入的账号
  		String jwtToken = (String) token.getPrincipal();
  		System.out.println(jwtToken);
  		Long userId = JwtUtil.getAppUID(jwtToken);
  		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
  				jwtToken,
  				jwtToken,
  				getName()
  				);
  		return authenticationInfo;
  		//获取用户的输入的账号
  		/*System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
  		//获取用户的输入的账号
  		String userName = (String) token.getPrincipal();
  		System.out.println(userName);
  		TbUser tbUser = userService.findByUserName(userName);
  		if(null == tbUser) {
  			return null;
  		}
  //		Long userId = JwtUtil.getAppUID(token.getCredentials());
  		String salt = "123";
  		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
  				tbUser,
  				tbUser.getUserPwd(),
  				ByteSource.Util.bytes(salt),
  				getName()
  				);
  		return authenticationInfo;*/
  	}

  }

  ```

- ```java
  package com.zhangbin.cloud.conf;

  import java.io.IOException;
  import java.net.URLEncoder;

  import javax.servlet.ServletRequest;
  import javax.servlet.ServletResponse;
  import javax.servlet.http.HttpServletRequest;
  import javax.servlet.http.HttpServletResponse;

  import org.apache.shiro.authz.UnauthorizedException;
  import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

  import lombok.extern.slf4j.Slf4j;

  /**
   * 自定义jwt过滤器来作为shiro的过滤器
   * 
   * @author Administrator
   *
   */
  @Slf4j
  public class JwtFilter extends BasicHttpAuthenticationFilter {

  	@Override
  	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
  			throws UnauthorizedException {
  		// 判断请求的请求头是否带上 "Authorication"
  		/*
  		 * if (isLoginAttempt(request, response)) { //如果存在，则进入 executeLogin 方法执行登入，检查
  		 * token 是否正确 try { executeLogin(request, response); return true; } catch
  		 * (Exception e) { //token 错误 throw new
  		 * RuntimeException("token校验失败:"+e.getMessage()); // responseError(response,
  		 * e.getMessage()); } } //如果请求头不存在 Authorication，则可能是执行登陆操作或者是游客状态访问，无需检查
  		 * token，直接返回 true return true;
  		 */
  		try {
  			executeLogin(request, response);
  		} catch (Exception e) {
  //			return false;
  			responseError(response,e.getMessage());
  		}
  		/**
  		 * UnsupportedTokenException:验证不通过，500 401，无Authorication头
  		 * 
  		 */
  		return true;
  	}

  	@Override
  	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
  		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
  		String token = httpServletRequest.getHeader("Authorication");
  		JwtToken jwtToken = new JwtToken(token);
  		// 提交给realm进行登入，如果错误他会抛出异常并被捕获
  		getSubject(request, response).login(jwtToken);
  		// 如果没有抛出异常则代表登入成功，返回true
  		return true;
  	}

  	@Override
  	protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
  		HttpServletRequest req = (HttpServletRequest) request;
  		String token = req.getHeader("Authorication");
  		return token != null;
  	}

  	/**
  	 * 将非法请求跳转到 /unauthorized/**
  	 */
  	private void responseError(ServletResponse response, String message) {
  		try {
  			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
  			// 设置编码，否则中文字符在重定向时会变为空字符串
  			message = URLEncoder.encode(message, "UTF-8");
  			httpServletResponse.sendRedirect("/unauthorized/" + message);
  		} catch (IOException e) {
  			log.error(e.getMessage());
  		}
  	}
  }

  ```

- ​

## shiro 异常 

> <!-- 身份认证异常 -->
> <!-- 身份令牌异常，不支持的身份令牌 -->
> org.apache.shiro.authc.pam.UnsupportedTokenException
>
> <!-- 未知账户/没找到帐号,登录失败 -->
> org.apache.shiro.authc.UnknownAccountException
> <!-- 帐号锁定 -->
> org.apache.shiro.authc.LockedAccountException
> <!-- 用户禁用 -->
> org.apache.shiro.authc.DisabledAccountException
> <!-- 登录重试次数，超限。只允许在一段时间内允许有一定数量的认证尝试 -->
> org.apache.shiro.authc.ExcessiveAttemptsException
> <!-- 一个用户多次登录异常：不允许多次登录，只能登录一次 。即不允许多处登录-->
> org.apache.shiro.authc.ConcurrentAccessException
> <!-- 账户异常 -->
> org.apache.shiro.authc.AccountException
>
> <!-- 过期的凭据异常 -->
> org.apache.shiro.authc.ExpiredCredentialsException
> <!-- 错误的凭据异常 -->
> org.apache.shiro.authc.IncorrectCredentialsException
> <!-- 凭据异常 -->
> org.apache.shiro.authc.CredentialsException
> org.apache.shiro.authc.AuthenticationException
>
> <!-- 权限异常 -->
> <!-- 没有访问权限，访问异常 -->
> org.apache.shiro.authz.HostUnauthorizedException
> org.apache.shiro.authz.UnauthorizedException
> <!-- 授权异常 -->
> org.apache.shiro.authz.UnauthenticatedException
> org.apache.shiro.authz.AuthorizationException
>
> <!-- shiro全局异常 -->
>
> org.apache.shiro.ShiroException