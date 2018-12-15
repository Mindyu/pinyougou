## 品优购项目学习笔记

一个综合性的 B2B2C 的电商网站系统。网站采用商家入驻的模式，商家入驻平台提交申请，有平台进行资质审核，审核通过后，商家拥有独立的管理后台录入商品信息。商品经过平台审核后即可发布。 

[Github地址](https://github.com/Mindyu/pinyougou)

### 项目简介

**系统模块**
 - 网站前台
 - 运营商平台
 - 商家管理平台



**框架组合**

​	前端 angularJS + Bootstrap 

​	后端 Spring + SpringMVC + mybatis + Dubbox



**系统架构**
​	面向服务的架构（SOA架构）。控制层与服务层分离，通过网络调用。
![面向服务的架构][1]



**Dubbox框架**
	致力于提供高性能和透明化的RPC远程服务调用方案，以及SOA服务治理方案。远程服务调用的分布式框架。

原理图
![Dubbox原理图][2]
	节点角色说明：

 - Provider: 暴露服务的服务提供方。 
 - Consumer: 调用远程服务的服务消费方。 
 - Registry: 服务注册与发现的注册中心。
 - Monitor: 统计服务的调用次调和调用时间的监控中心。 
 - Container: 服务运行容器。



模块关联关系图：![模块关联图](https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E7%B3%BB%E7%BB%9F%E6%A8%A1%E5%9D%97%E5%9B%BE.png)



[品优购项目笔记（上）](docs/品优购项目笔记（上）.md)

[品优购项目笔记（中）](docs/品优购项目笔记（中）.md)

[品优购项目笔记（下）](docs/品优购项目笔记（下）.md)



[1]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E9%9D%A2%E5%90%91%E6%9C%8D%E5%8A%A1%E7%9A%84%E6%9E%B6%E6%9E%84.jpg
[2]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/Dubbox%E5%8E%9F%E7%90%86.jpg

