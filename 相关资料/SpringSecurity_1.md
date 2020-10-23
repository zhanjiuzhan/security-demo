# Spring boot 自动配置
## 1. Spring Boot 项目启动
项目启动时会从spring-boot-autoconfigure/META-INF/spring.factories文件中加载默认的配置类
```factories
org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,\
org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration,\
org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration,\
org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration,\
org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration,\
org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration,\
```
## 2. SecurityAutoConfiguration
```java
@Configuration
// 当有 DefaultAuthenticationEventPublisher 这个class的时候 这个配置类才会生效, 所以当项目依赖了spring-security-core时自动配置生效了。
@ConditionalOnClass({DefaultAuthenticationEventPublisher.class})
// 配置类 也就是 application.properties 那里配置的 可以配置一个用户的用户名 密码 和角色 只能配置一个用户
@EnableConfigurationProperties({SecurityProperties.class})
// 引入了其它配置类
@Import({SpringBootWebSecurityConfiguration.class, WebSecurityEnablerConfiguration.class, SecurityDataConfiguration.class})
public class SecurityAutoConfiguration {
    public SecurityAutoConfiguration() {
    }
    
    // 这里这个Bean基本是会注入到容器中的 这个Bean是用来进行 成功 失败 Event 发送事件的
    @Bean
    @ConditionalOnMissingBean({AuthenticationEventPublisher.class})
    public DefaultAuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher publisher) {
        return new DefaultAuthenticationEventPublisher(publisher);
    }
}
```
### 2.1 引入配置 SpringBootWebSecurityConfiguration 
当没有 WebSecurityConfigurerAdapter 实现类的Bean时 像容器中注入一个 DefaultConfigurerAdapter (WebSecurityConfigurerAdapter 的实现类 啥都没做)

### 2.2 引入配置 WebSecurityEnablerConfiguration
@EnableWebSecurity 没有被应用那么，应用该注解@EnableWebSecurity(很重要), 所以这里 自己实现WebSecurityConfigurerAdapter 时可以不用写这个注解来启用Security的噢

### 2.3 引入配置 SecurityDataConfiguration
这个配置类 不懂 有用到再看喽  
1. 注入Bean SecurityEvaluationContextExtension  
2. 引入配置类 SecurityEvaluationContextExtension  
3. 引入配置类 EvaluationContextExtensionSupport  

## 3. UserDetailsServiceAutoConfiguration
AuthenticationManager, AuthenticationProvider, UserDetailsService 这三个Bean都没有, 并且
ObjectPostProcessor Bean有的时候这个配置类才生效, 当然这个Bean是在 @EnableWebSecurity 引入的，后面就知道了，主要用来配置Bean的 (AutowireCapableBeanFactory 了解一下)

### 3.1 注入 InMemoryUserDetailsManager extends UserDetailsService
注入这个Bean 的前提是没有 ClientRegistrationRepository(这个不知道干啥的) 这个Bean, InMemoryUserDetailsManager 配置了配置文件中的用户信息, 基于内存, passport 使用了编码。

## 4. SecurityFilterAutoConfiguration
这个配置是在 SecurityAutoConfiguration 之后才进行的, 其主要目的是当 springSecurityFilterChain 这个Bean 存在那么注入 DelegatingFilterProxyRegistrationBean
### 4.1 注入 DelegatingFilterProxyRegistrationBean
简单的看了下应该是吧 springSecurityFilterChain 这个Bean 弄到servlet 里面, 也就是让过滤链生效吧

## 5. ReactiveSecurityAutoConfiguration 和 ReactiveUserDetailsServiceAutoConfiguration
异步的, 流式的？ 不懂

## 6. OAuth2ClientAutoConfiguration
OAuth2 相关的吧

## 7. 总结
1. 条件配置 (默认配置让你使用默认功能)  
2. 默认或者自己使用 @EnableWebSecurity 启用security功能 所以主要配置就是 @EnableWebSecurity 了  
3. 这里注入的Bean 有(忽略条件)  
SecurityProperties, AuthenticationEventPublisher, DefaultConfigurerAdapter, InMemoryUserDetailsManager, DelegatingFilterProxyRegistrationBean, , SecurityEvaluationContextExtension

