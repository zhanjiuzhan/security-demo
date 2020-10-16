# 前言
Spring Security 和 Shiro 是两个主流的安全框架。Shiro 是一个比较早期和简单灵活的框架，Spring Security 是在 Shiro 之后推出的，
可能 Spring Security 继承了前者的思想，对于核心模块它们具有很高的相似度。Shiro 的版本更新现在已经很慢了，而 Spring Security 却是当下比较流行的框架。
它社区活跃，资料丰富，功能也比 Shiro 丰富(比如安全防护等)，另外在web领域大多都是使用的 Spring 而它们是一个家族的可以无缝集成。
当然它也有缺点，那就是它的上手比较难。这也是本文的目的。

# 一、Spring Security简介

1. 认证 (Authentication)
对用户身份进行识别，认证失败401，用一句话说就是你是谁。
2. 授权 (Authorization)
对用户可以使用那些服务进行权限管理，没有权限403，简称: 你能干啥，啥你不能干。
3. 安全防护
对CSRF，XSS，等安全隐患进行防护的功能.
# 二、简单使用
## 1. Spring Security集成
### 1.1 引入依赖
<font color=#999AAA >创建一个Spring boot项目 在pom中引入Spring Security依赖
```xml
<!-- security的依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
* spring boot 最好用2.0以上版本
* 引入依赖后可见其版本在5.0以上
### 1.2 现象梳理
以上项目中就已经集成了Spring Security，当容器启动时Spring security会以默认的配置应用于服务中。 所以启动时会出现一个随机的密码。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201015110905829.png#pic_center)此时我们访问任意链接都会跳转到 /login 这个默认的表单登录界面
![](https://img-blog.csdnimg.cn/20201015111035226.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNzg5Njg4Mw==,size_16,color_FFFFFF,t_70#pic_left)
默认的用户名是: user  密码就是启动时随机给的那个(一个UUID)，登录后即可以访问到要访问的页面了
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201015111209592.png#pic_left)
可以看到我们只是添加了Spring Security依赖，其它的什么都没干就能够使用 Spring Security 的功能了，这正是它们可以无缝集成的表现。 spring boot有很多默认的配置，当Security被依赖后 ， 如SecurityAutoConfiguration 这类相关的默认配置就满足了条件，从而使得Spring Security应用于项目中。

## 2. 简单的配置
<font color=#999AAA >Spring security已经集成到项目中了(就加个依赖就好了Ö‿Ö)，默认的配置肯定不能满足我们的需求， 那么就需要自定义配置了。
###  2.1 自定义配置
#### 2.1.1 默认配置
spring boot 默认会通过 SecurityAutoConfiguration 来进行配置，它会创建一个DefaultConfigurerAdapter这样的Bean，它就是继承了WebSecurityConfigurerAdapter抽象类而已什么都没做，从抽象类中可以看到
```java
protected void configure(HttpSecurity http) throws Exception {
    this.logger.debug("Using default configure(HttpSecurity). If subclassed this will potentially override subclass configure(HttpSecurity).");
    ((HttpSecurity)((HttpSecurity)((AuthorizedUrl)http.authorizeRequests().anyRequest()).authenticated().and()).formLogin().and()).httpBasic();
}
```
这个方法的意思是对任意请求进行权限验证， 配置默认的登录页面为 /login 表单登录，并且支持httpBasic认证。
#### 2.1.2 自定义配置
自己写一个类并继承 WebSecurityConfigurerAdapter 抽象类，重写配置方法， 然后将该类作为Bean注入到容器中就可以应用自己的配置了。(当容器检测到有WebSecurityConfigurerAdapter 类型的Bean时将不再创建默认的配置Bean)
```java
@Component
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/welcome")
                    .permitAll()
                .antMatchers("/user/*")
                    .authenticated()
                .antMatchers("/admin/**")
                    .hasRole("ADMIN")
                .and()
            .formLogin().permitAll();
    }
}
```
一般情况下都是在该类上添加@EnableWebSecurity注解来启动Spring Security支持的，但是在spring boot 中 WebSecurityEnablerConfiguration 已经应用了该注解，所以也可以不加的。
+ 上面配置的意思是
    - /welcome  可以无权限访问
    - /user/* 需要登录之后才能访问
    - /admin/** 需要登录且是ADMIN用户才能访问
    - 默认的登录页还是 /login 表单登录
 + 星(*)号的用法
    - 一个星表示当前目录层级任意个字符
    - 两个星表示任意多个目录层级和字符
#### 2.1.3 详细的配置介绍
在以前的版本是通过xml的方式进行权限配置的
```xml
<http>
<intercept-url pattern="/secure/**" access="ROLE_USER" requires-channel="https"/>
<intercept-url pattern="/**" access="ROLE_USER" requires-channel="any"/>
...
</http>
```
现在都渐渐的使用代码来进行配置
**配置详细介绍**
HttpSecurity
| 方法 | 方法介绍  |  返回对象|
|--|--|--|
| authorizeRequests() |配置路径拦截，表明路径访问所对应的权限，角色，认证信息  |ExpressionUrlAuthorizationConfigurer |
| requestCache() | 允许配置请求缓存 | RequestCacheConfigurer|
|  csrf() | 添加 CSRF 支持，使用WebSecurityConfigurerAdapter时，默认启用 |CsrfConfigurer |
| securityContext() |在HttpServletRequests之间的SecurityContextHolder上设置SecurityContext的管理。 默认启用  |SecurityContextConfigurer |
|exceptionHandling()  | 允许配置错误处理 | ExceptionHandlingConfigurer|
| logout() | 添加退出登录支持。默认启用。默认情况是，访问URL"/logout"，使HTTP Session无效来清除用户，清除已配置的任何#rememberMe()身份验证，清除SecurityContextHolder，然后重定向到"/login?success" | LogoutConfigurer|
|formLogin()  |指定支持基于表单的身份验证。如果未指定FormLoginConfigurer#loginPage(String)，则将生成默认登录页面  |FormLoginConfigurer |
|anonymous()  | 允许配置匿名用户的表示方法，默认启用。默认情况下，匿名用户将使用AnonymousAuthenticationToken表示，并包含角色"ROLE_ANONYMOUS" |AnonymousConfigurer |
| servletApi() | 将HttpServletRequest方法与在其上找到的值集成到SecurityContext中。 默认启用 |ServletApiConfigurer |
| oauth2Login() | 根据外部OAuth 2.0或OpenID Connect 1.0提供程序配置身份验证 | OAuth2LoginConfigurer|
|requiresChannel()  |配置通道安全。为了使该配置有用，必须提供至少一个到所需信道的映射  | ChannelSecurityConfigurer|
| httpBasic() | 配置 Http Basic 验证 |HttpBasicConfigurer |
| rememberMe() | 允许配置"记住我"的验证 |RememberMeConfigurer |
| sessionManagement () | 会话管理配置 | SessionManagementConfigurer|
|cors()  | 配置跨域资源共享 (CORS ) |CorsConfigurer |
| openidLogin() |用于基于 OpenId 的验证  |OpenIDLoginConfigurer |
| headers() | 将安全标头添加到响应 |HeadersConfigurer |
| portMapper() | 用于在HTTP和HTTPS两种协议的端口之间做跳转 : 80–> 443，8080 --> 8443 | PortMapperConfigurer|
|jee()  |配置基于容器的预认证。 在这种情况下，认证由Servlet容器管理  |JeeConfigurer |
|x509()  | 配置基于x509的认证 |X509Configurer|
具体的详细介绍和应用， 用的时候自行查询了
### 2.2 基于内存的配置
<font color=#999AAA >其实默认情况下就是基于内存的配置，默认情况下用户名和密码是存储在内存中的，即用户名user，密码是启动时控制台打印的那个uuid，默认配置类为UserDetailsServiceAutoConfiguration。
#### 2.2.1 配置文件配置
配置文件对应于 SecurityProperties 它也是 UserDetailsServiceAutoConfiguration 使用的。  
现，在application.yml中添加如下配置:
```yml
spring:
  security:
    user:
      name: root
      password: 123456
      roles:
        - ADMIN
```
如上它只能配置一个用户，并且是基于内存的。 该用户的的用户名是root，密码是123456，身份是ADMIN，当我们配置用户信息后，控制台将不再打印随机生成的那个密码。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201015210448998.png#pic_left)
如上我们使用用户名密码登录后可以正常都访问 /admin/** 的链接
#### 2.2.2 自定义配置







