## 品优购项目学习

  一个电商网站系统。

### 项目简介

**系统模块**
 - 网站前台
 - 运营商平台
 - 商家管理平台

**系统架构**
	面向服务的架构（SOA架构）。控制层与服务层分离，通过网络调用。
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


### 品牌管理模块
**功能实现**
 1. 运用AngularJS前端框架的常用指令
 2. 完成品牌管理的列表功能
![品牌管理][3]

 3. 完成品牌管理的分页列表功能
 4. 完成品牌管理的增加功能
 5. 完成品牌管理的修改功能
![品牌新增与修改][4]

 6. 完成品牌管理的删除功能
 7. 完成品牌管理的条件查询功能

**前端框架 AngularJS**
*四大特征*

 1. MVC 模式
- Model: 数据,其实就是angular变量($scope.XX);
- View: 数据的呈现,Html+Directive(指令);
- Controller: 操作数据,就是function,数据的增删改查;

 2. 双向绑定
框架采用并扩展了传统HTML，通过双向的数据绑定来适应动态内容，双向的数据绑定允许模型和视图之间的自动同步

 3. 依赖注入
最少知道法则

 4. 模块化设计
- 高内聚低耦合法则
1)官方提供的模块   ng、ngRoute、ngAnimate
2)用户自定义的模块     angular.module('模块名',[ ])


  [1]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E9%9D%A2%E5%90%91%E6%9C%8D%E5%8A%A1%E7%9A%84%E6%9E%B6%E6%9E%84.jpg
  [2]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/Dubbox%E5%8E%9F%E7%90%86.jpg
  [3]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E5%93%81%E7%89%8C%E7%AE%A1%E7%90%86.png
  [4]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E5%93%81%E7%89%8C%E4%BF%AE%E6%94%B9%E4%B8%8E%E6%96%B0%E5%A2%9E.png