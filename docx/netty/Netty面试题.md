##### **1、TCP、UDP的区别？**√

##### **2、TCP协议如何保证可靠传输？**√

##### **3、TCP的握手、挥手机制？**

##### **4、TCP的粘包/拆包原因及其解决方法是什么？**

##### **5、Netty的粘包/拆包是怎么处理的，有哪些实现？**

##### **6、同步与异步、阻塞与非阻塞的区别？**√

##### **7、说说网络IO模型？**

##### **8、BIO、NIO、AIO分别是什么？**√

##### **9、select、poll、epoll的机制及其区别？**

##### **10、说说你对Netty的了解？**

##### **11、Netty跟Java NIO有什么不同，为什么不直接使用JDK NIO类库？**

##### **12、Netty组件有哪些，分别有什么关联？**

##### **13、说说Netty的执行流程？**

##### **14、Netty高性能体现在哪些方面？**

##### **15、Netty的线程模型是怎么样的？**

##### **16、Netty的零拷贝提体现在哪里，与操作系统上的有什么区别？**

##### **17、Netty的内存池是怎么实现的？**

##### **18、Netty的对象池是怎么实现的？**

##### **19、在实际项目中，你们是怎么使用Netty的？**

##### **20、使用过Netty遇到过什么问题？**

## **Netty基础相关问题**

1. 讲讲Netty的特点？
2. BIO、NIO和AIO的区别？
3. NIO的组成是什么？
4. 如何使用 Java NIO 搭建简单的客户端与服务端实现网络通讯？
5. 如何使用 Netty 搭建简单的客户端与服务端实现网络通讯？
6. 讲讲Netty 底层操作与 Java NIO 操作对应关系？
7. Channel 与 Socket是什么关系，Channel 与 EventLoop是什么关系，Channel 与 ChannelPipeline是什么关系？
8. EventLoop与EventLoopGroup 是什么关系？
9. 说说Netty 中几个重要的对象是什么，它们之间的关系是什么？
10. Netty 的线程模型是什么？

## **粘包与半包和分隔符相关问题**

1. 什么是粘包与半包问题?
2. 粘包与半包为何会出现?
3. 如何避免粘包与半包问题？
4. 如何使用包定长 FixedLengthFrameDecoder 解决粘包与半包问题？原理是什么？
5. 如何使用包分隔符 DelimiterBasedFrameDecoder 解决粘包与半包问题？原理是什么？
6. Dubbo 在使用 Netty 作为网络通讯时候是如何避免粘包与半包问题？
7. Netty框架本身存在粘包半包问题？
8. 什么时候需要考虑粘包与半包问题？

## **WebSocket 协议开发相关问题**

讲讲如何实现 WebSocket 长连接？

讲讲WebSocket 帧结构的理解？

浏览器、服务器对 WebSocket 的支持情况

如何使用 WebSocket 接收和发送广本信息？

如何使用 WebSocket 接收和发送二进制信息？

## **Netty源码分析相关问题**

服务端如何进行初始化？

何时接受客户端请求？

何时注册接受 Socket 并注册到对应的 EventLoop 管理的 Selector ？

客户端如何进行初始化？

何时创建的 DefaultChannelPipeline ？

讲讲Netty的零拷贝？

<https://juejin.im/post/5c81b08f5188257a323f4cef#heading-0>