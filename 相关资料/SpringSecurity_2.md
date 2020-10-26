# Spring Security 启用 - @EnableWebSecurity
```java
@Documented
@Import({ WebSecurityConfiguration.class, SpringWebMvcImportSelector.class })
@EnableGlobalAuthentication
@Configuration
public @interface EnableWebSecurity {

	boolean debug() default false;
}
```
启用Security 主要就是引入了两个配置文件和 @EnableGlobalAuthentication 注解
## 1. WebSecurityConfiguration
基本配置类 WebSecurityConfiguration 也作为Bean放入了容器中, Bean加载的时候会执行 setFilterChainProxySecurityConfigurer 方法
```java
public class WebSecurityConfiguration implements ImportAware, BeanClassLoaderAware {
	@Autowired(required = false)
	public void setFilterChainProxySecurityConfigurer(
			ObjectPostProcessor<Object> objectPostProcessor,
			@Value("#{@autowiredWebSecurityConfigurersIgnoreParents.getWebSecurityConfigurers()}") List<SecurityConfigurer<Filter, WebSecurity>> webSecurityConfigurers)
			throws Exception {
		webSecurity = objectPostProcessor
				.postProcess(new WebSecurity(objectPostProcessor));
		if (debugEnabled != null) {
			webSecurity.debug(debugEnabled);
		}

		Collections.sort(webSecurityConfigurers, AnnotationAwareOrderComparator.INSTANCE);

		Integer previousOrder = null;
		Object previousConfig = null;
		for (SecurityConfigurer<Filter, WebSecurity> config : webSecurityConfigurers) {
			Integer order = AnnotationAwareOrderComparator.lookupOrder(config);
			if (previousOrder != null && previousOrder.equals(order)) {
				throw new IllegalStateException(
						"@Order on WebSecurityConfigurers must be unique. Order of "
								+ order + " was already used on " + previousConfig + ", so it cannot be used on "
								+ config + " too.");
			}
			previousOrder = order;
			previousConfig = config;
		}
		for (SecurityConfigurer<Filter, WebSecurity> webSecurityConfigurer : webSecurityConfigurers) {
			webSecurity.apply(webSecurityConfigurer);
		}
		this.webSecurityConfigurers = webSecurityConfigurers;
	}
}
```



### 1.1 注入 DelegatingApplicationListener
容器的监听器, 当有event来时, 该监听器轮询SmartApplicationListener实现类 来处理event
### 1.2 注入 springSecurityFilterChain 重要 
一个Filter 这个就是Security的过滤链了


## 2. SpringWebMvcImportSelector

## 3. @EnableGlobalAuthentication
```java
@Import(AuthenticationConfiguration.class)
@Configuration
public @interface EnableGlobalAuthentication {}
```
引入 AuthenticationConfiguration 配置类, 字面意思它是一个全局的配置
##
WebSecurityConfiguration, DelegatingApplicationListener
