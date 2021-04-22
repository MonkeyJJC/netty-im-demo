### Information
* Netty是一个Java开源框架。Netty提供异步的、事件驱动的网络应用程序框架和工具，用以快速开发高性能、高可靠性的网络服务器和客户端程序。
* 也就是说，Netty是一个基于NIO的客户、服务器端编程框架，使用Netty可以确保你快速和简单的开发出一个网络应用，例如实现了某种协议的客户，服务端应用。
* Netty 相当简化和流线化了网络应用的编程开发过程，例如，TCP和UDP的Socket服务开发。
* 本项目基于Netty实现长连接，模拟一个简单im系统

### Structure
* api:Netty服务端
* client:Netty客户端
* service:业务逻辑，提供Netty的基础封装，api与client复用

### Start
* NettyImDemoApiApplicationContext：启动服务端
* NettyImDemoClientApplicationContext：启动客户端(注：模型单聊及群聊场景时，启动多个客户端实例即可，建议使用springboot2.x，可以直接使用示例代码更改port启动多实例即可)
* 测试类：MessageController，本地可以使用postman进行模拟客户端的上行数据

（1）模拟用户连接(长连接注册)
````
type ：AUTH_REQUEST
message ：{"accessToken":"from"}
````

（2） 模拟单聊消息发送
````
type ：SINGLE_CHAT_MESSAGE
message ： {"fromUser":"from","toUser":"to","msgId":1,"content":"测试发送"}
````

（2） 模拟群聊消息发送
````
type ：GROUP_CHAT_MESSAGE
message ： {"fromUser":"from","msgId":1,"content":"测试发送"}
````