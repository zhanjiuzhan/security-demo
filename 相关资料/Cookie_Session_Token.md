# Cookie, Session, Token, JWT
* 摘要
    > 由于http会话是无状态的，所以为了保持会话状态，就有了cookie, session, token的产生。
## Cookie
cookie是保存在客户端即浏览器中的一个简单的文本文件，这个文件与特定的web文档(web站点，域名等)关联在一起。当每次请求的时候都会携带着，从对应的web站点取得的所有保存的cookie信息。
### 作用
类似于会员卡,用于服务器识别客户身份。
### 特点
- cookie由服务端生成，以kv的形式保存在浏览器中。
- cookie用于识别用户身份(数据通常是加密的)
- 一段不超过4KB的小型文本数据
### Cookie 信息介绍
- (必选) Cookie名称，Cookie名称必须使用只能用在URL中的字符，一般用字母及数字，不能包含特殊字符，如有特殊字符想要转码。如js操作cookie的时候可以使用escape()对名称转码。
- (必选) Cookie值，Cookie值同理Cookie的名称，可以进行转码和加密。
- (可选) Expires，过期日期，一个GMT格式的时间，当过了这个日期之后，浏览器就会将这个Cookie删除掉，当不设置这个的时候，Cookie在浏览器关闭后消失。
会话cookie,若不设置过期时间，则表示这个cookie的生命期为浏览器会话期间，关闭浏览器窗口，cookie就消失。会话cookie一般不存储在硬盘上而是保存在内存里。
持久cookie，设置了过期时间，浏览器就会把cookie保存到硬盘上，关闭后再次打开浏览器，这些cookie仍然有效直到超过设定的过期时间。存储在硬盘上的cookie可以在浏览器的不同进程间共享。
- (可选) Path，一个路径，在这个路径下面的页面才可以访问该Cookie，一般设为“/”，以表示同一个站点的所有页面都可以访问这个Cookie。
- (可选) Domain，子域，指定在该子域下才可以访问Cookie，例如要让Cookie在a.test.com下可以访问，但在b.test.com下不能访问，则可将domain设置成a.test.com。
- (可选) Secure，安全性，指定Cookie是否只能通过https协议访问，一般的Cookie使用HTTP协议既可访问，如果设置了Secure（没有值），则只有当使用https协议连接时cookie才可以被页面访问。
- (可选) HttpOnly，如果在Cookie中设置了"HttpOnly"属性，那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息。
### 缺点
- cookie作为请求或者响应报文发送， 无形中增加了网络流量。
- cookie是明文传送的安全性差。
- cookie受限于浏览器， 用户可以随时清理等。
### 扩展知识
- Cookie捕获/重放
- 恶意Cookies
- 会话定位
- CSRF攻击
    + cookie类似于会员卡，可能会被串改其会员卡金额， 可能被偷， 可能被仿制。
        + Cookie被用户非法篡改，如篡改其中的expire项，可将Cookie的有效期延长；篡改path项可使用户能够访问服务器上不被授权的内容；或修改domain项，使用户能够访问不被授权的服务器从而获得合法用户的信息等; [加入MAC以进行完整性校验;]
        + 被非法用户非法截获，然后在有限期内重放，则非法用户将享有合法用户的合法权益，可能会损害网站方的利益;[防止非法用户非法截获后的重放，可以让用户对相关信息进行数字签名，加强有效性验证;]
        + 若Cookie被服务器加密，而非法用户通过强力攻击或其他手段获得了相应的加密密钥，则非法用户可以伪造任何合法Cookie，从而可以访问合法用户的所有个性化信息，甚至是账户信息等[对Cookie本身进行随机密钥加密，保证Cookie本身的信息安全]
    + cookie 安全相关 未完待续
    
## Session
session是服务器给访问客户端创建的一个"身份标志"，服务器使用session把用户信息临时保存在服务器上。  
session会话指的是浏览器打开，请求该服务到浏览器关闭的一段时间。因为默认cookie在浏览器关闭时销毁，所以该session即使存在也不再可用(sessionId已消失)，等待GC回收，所以认为是一次会话。
### 生命周期
- 创建: sessionid第一次产生是在直到某server端程序调用 HttpServletRequest.getSession(true)这样的语句时才被创建。所以session的创建是由服务器来做的。
- 删除: 超时(默认30min)；程序调用HttpSession.invalidate()；程序关闭；
### 扩展
- session存放在哪里：服务器端的内存中。不过session可以通过特殊的方式做持久化管理（memcache，redis）。
- session的id是从哪里来的，sessionID是如何使用的：当客户端第一次请求session对象时候，服务器会为客户端创建一个session，并将通过特殊算法算出一个session的ID，用来标识该session对象，一般将sessionId存储到cookie中。
- 若禁用cookie将使用URL重写，就是把sessionId直接附加到URL路径的后面。
- sessionId的保存：当在server中启用session的时候，sessionId将被服务器自动保存到cookie中。
### 缺点
- 每次认证用户发起请求的时候， 服务器需要去创建一个记录来存储信息。当越来越多的用户发起请求时，内存的开销也会不断的增加。
- 在服务端的内存中使用Session存储登录信息，伴随而来的是可扩展性问题。
- CORS(跨资源共享)
- CSRF(跨站请求伪造(Cross-site request forgery)
攻击者并不能通过CSRF攻击来直接获取用户的账户控制权，也不能直接窃取用户的任何信息。他们能做到的，是欺骗用户浏览器，让其以用户的名义运行操作  
防御措施:  1）检查Referer字段    2）添加校验token
## Token
Token 是在服务端产生(不用在服务端保存了)的。如果前端使用用户名/密码向服务端请求认证，服务端认证成功，那么在服务端会返回 Token 给前端。前端可以在每次请求的时候带上 Token 证明自己的合法地位
### JWT
Json Web token(JWT) 主要用于网络服务之间身份认证的，可以携带其它信息，可以被加密，它是一个基于json的开放标准，常用于单点登陆(SSO: 用户只需一次登录就可以访问所有相互信任的应用系统)场景。 json + token = jwt  
#### 构成
+ 头部(header)
    + 声明类型，这里是 JWT
    + 声明加密的算法 通常直接使用 HMAC SHA256
    ```json5
    {'typ': 'JWT', 'alg': 'HS256'}
    ```
    将其Base64编码: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9
+ 载荷(payload)
    + 标准中注册的声明
        - iss: jwt签发者
        - sub: jwt所面向的用户
        - aud: 接收jwt的一方
        - exp: jwt的过期时间，这个过期时间必须要大于签发时间
        - nbf: 定义在什么时间之前，该jwt都是不可用的.
        - iat: jwt的签发时间
        - jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
    + 公共的声明: 公共的声明可以添加任何的信息，一般添加用户的相关信息或其他业务需要的必要信息.但不建议添加敏感信息，因为该部分在客户端可解密.
    + 私有的声明: 私有声明是提供者和消费者所共同定义的声明，一般不建议存放敏感信息，因为base64是对称解密的，意味着该部分信息可以归类为明文信息。
    ```json5
    {
      "sub": "1234567890",
      "name": "John Doe",
      "admin": true
    }
    ```
    将其Base64编码: eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9
+ 签证(signature)


## 其它 Http 相关
### 跨域请求
域：域 = 协议名 + 主机名 + 端口号  
为了服务器便于管理和减轻服务器压力，不同的资源放在不同的服务器上，即需要跨域访问。ajax无法实现跨域访问。  
解决方法:  
1）使用JSONP进行请求  
2）通过CORS方式解决简单跨域请求  
3）...
### CSRF
### XSS
跨站脚本攻击, 利用网站漏洞执行脚本进行攻击。
### http头介绍
- Referer: 表示请求的来源。当一个请求并不是由链接触发产生的将携带这个参数来表示请求从哪里链接过来。  
    - 防盗链
    - 防止恶意的请求

## JSONP
Ajax直接请求普通文件存在跨域无权限访问的问题，甭管你是静态页面、动态网页、web服务、WCF，只要是跨域请求，一律不准。  
Web页面上调用js文件时则不受是否跨域的影响（不仅如此，我们还发现凡是拥有”src”这个属性的标签都拥有跨域的能力，比如<\script>、<\img>、<\iframe>）  
于是可以判断，当前阶段如果想通过纯web端（ActiveX控件、服务端代理、属于未来的HTML5之Websocket等方式不算）跨域访问数据就只有一种可能，那就是在远程服务器上设法把数据装进js格式的文件里，供客户端调用和进一步处理。  
恰巧我们已经知道有一种叫做JSON的纯字符数据格式可以简洁的描述复杂数据，更妙的是JSON还被js原生支持，所以在客户端几乎可以随心所欲的处理这种格式的数据。  
这样子解决方案就呼之欲出了，web客户端通过与调用脚本一模一样的方式，来调用跨域服务器上动态生成的js格式文件（一般以JSON为后缀），显而易见，服务器之所以要动态生成JSON文件，目的就在于把客户端需要的数据装入进去。  
客户端在对JSON文件调用成功之后，也就获得了自己所需的数据，剩下的就是按照自己需求进行处理和展现了，这种获取远程数据的方式看起来非常像AJAX，但其实并不一样。  
为了便于客户端使用数据，逐渐形成了一种非正式传输协议，人们把它称作JSONP，该协议的一个要点就是允许用户传递一个callback参数给服务端，然后服务端返回数据时会将这个callback参数作为函数名来包裹住JSON数据，这样客户端就可以随意定制自己的函数来自动处理返回数据了。  
代码实现:  
通过@ControllerAdvice来加强controller接口，该实现类继承AbstractJsonpResponseBodyAdvice类 并调用父构造，传递参数名。  
```java
@ControllerAdvice(annotations = {Jsonp.class})
public class MyControllerAdvice extends AbstractJsonpResponseBodyAdvice {
    public MyControllerAdvice() {
        super("callback", "jsonp");
    }
}
```
此时在url中携带callback或者jsonp参数 即认为是jsonp调用，若不带这两个参数即为正常访问。
