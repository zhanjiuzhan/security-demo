# OAuth2

## 单点登录

多个站点(192.168.1.20X)共用一台认证授权服务器(192.168.1.110，用户数据库和认证授权模块共用)。用户经由其中任何一个站点(比如 192.168.1.201)登录后，可以免登录访问其他所有站点。而且，各站点间可以通过该登录状态直接交互。

![single-sign-on](single-sign-on.png)

## 是什么

用户认证和授权的开放的网络协议

## 能干什么

用来给第三方授权的, 就是第三方代表用户来访问服务资源

## 怎么用

例：用户->我，第三方->博客，服务资源->微信个人信息  
1. 我访问微信个人信息的话, 直接用户名密码登陆即可  
2. 博客访问的话，不能让它知道我的微信帐号所以就按照OAuth2来  
    1. 我登陆博客，博客让我找授权中心授于访问微信个人信息的权限 (授权中心是Oauth2添加的中间层)
    2. 我在授权中心上同意给予博客访问微信的权力
    3. 授权中心给博客一个token(据有时间，权限，范围等限制)
    4. 博客用token访问了微信，它在权限范围内取得了我的用户名，头像信息等

OAuth2的规范流程大抵上就是这样的，重要的是"我在授权中心上同意给予博客访问微信的权力" 即授权

**授权的参与者**
OAuth的参与者至少有如下三种：
resource owner(RO):资源所有者，对资源有授权能力的人，如用户

resource server(RS):资源服务器，存储资源，并处理资源的访问请求

client:第三方应用，获取了RO的授权就去RS上访问RO的资源

authorization server(AS):授权服务器，用于认证RO的身份，为RO提供授权审批流程，并颁发授权令牌，实际中RS与AS功能由同一个服务器提供

![Abstract Protocol Flow](OAuth2.0 relations.png)

开放授权的步骤：
1. Client 向RO发起请求，请求中一般包含，资源路径，Client信息，操作类型。
2. RO在AS提供的授权页面中进行审批流程，完成审批后返回“授权凭证”给Client。
3. Client向AS请求访问令牌，需要提供RO返回的“授权凭证”以及Client自身的“身份凭证”。
4. AS 返回令牌。
5. Client使用AS返回的令牌到RS上请求资源。
6. RS对令牌的有效性进行check，比如是否伪造，是否越权，是否过期，然后提供服务。

**授权模式**  

OAuth为了支持这些不同类型的第三方应用，提出了多种授权类型

> **授权码(Authorization Code Grant)**  

AS将授权码通过给RO的redirect_url给Client，然后Client再使用授权码去AS获取access_token

授权流程：

（1） Client初始化协议的执行流程。首先通过HTTP 302来重定向RO用户代理到AS。Client在redirect_uri中应包含如下参数：client_id, scope (描述被访问的资源), redirect_uri (即Client的URI), state (用于抵制CSRF攻击). 此外，请求中还可以包含access_type和approval_prompt参数。当approval_prompt=force时，AS将提供交互页面，要求RO必须显式地批准（或拒绝）Client的此次请求。如果没有approval_prompt参数，则默认为RO批准此次请求。当access_type=offline时，AS将在颁发access_token时，同时还会颁发一个refresh_token。因为access_token的有效期较短（如3600秒），为了优化协议执行流程，offline方式将允许Client直接持refresh_token来换取一个新的access_token。

（2） AS认证RO身份，并提供页面供RO决定是否批准或拒绝Client的此次请求。

（3） 若请求被批准，AS使用步骤(1)中Client提供的redirect_uri重定向RO用户代理到Client。redirect_uri须包含authorization_code，以及步骤1中Client提供的state。若请求被拒绝，AS将通过redirect_uri返回相应的错误信息。

（4） Client拿authorization_code去访问AS以交换所需的access_token。Client请求信息中应包含用于认证Client身份所需的认证数据，以及上一步请求authorization_code时所用的redirect_uri。

（5） AS在收到authorization_code时需要验证Client的身份，并验证收到的redirect_uri与第3步请求authorization_code时所使用的redirect_uri相匹配。如果验证通过，AS将返回access_token，以及refresh_token。

![An Instance of Authorization Code Flow](Authorization code flow.png)

> **隐式授权模式/简化模式(Implicit Grant)**  

和授权码模式类似，只不过少了获取code的步骤，是直接获取令牌token的，适用于公开的浏览器单页应用，令牌直接从授权服务器返回，不支持刷新令牌，且没有code安全保证，令牌容易因为被拦截窃听而泄露。

> **RO凭证授权(Resource Owner Password Credentials Grant)**  

使用用户名/密码作为授权方式从授权服务器上获取令牌，一般不支持刷新令牌。

> **Client凭证授权(Client Credentials Grant)**  

一般用于资源服务器是应用的一个后端模块，客户端向认证服务器验证身份来获取令牌。



## 参考文献
[OAuth2 原理详解](https://www.jianshu.com/p/a047176d9d65)
