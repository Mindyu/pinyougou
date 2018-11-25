## 品优购项目学习

  一个综合性的 B2B2C 的电商网站系统。网站采用商家入驻的模式，商家入驻平台提交申请，有平台进行资质审核，审核通过后，商家拥有独立的管理后台录入商品信息。商品经过平台审核后即可发布。 

[Github地址](https://github.com/Mindyu/pinyougou)

### 项目简介

**系统模块**
 - 网站前台
 - 运营商平台
 - 商家管理平台

**框架组合**

前端 angularJS + Bootstrap 

后端 Spring + SpringMVC + mybatis + Dubbox

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
  框架采用并扩展了传统HTML，通过双向的数据绑定来适应动态内容，双向的数据绑定允许模型和视图之间的自动同步。遵循声明式编程应该用于构建用户界面以及编写软件构建，而指令式编程非常适合来表示业务逻辑的理念。
  3. 依赖注入
  对象在创建的时候，其依赖对象由框架来自动创建并注入进来。即最少知道法则。
  4. 模块化设计
- 高内聚低耦合法则
  1)官方提供的模块   ng、ngRoute、ngAnimate
  2)用户自定义的模块     angular.module('模块名',[ ])

*常见指令*

- ng-app 定义 AngularJS 应用程序的根元素，表示以下的指令 angularJS 都会识别，且在页面加载完时会自动初始化。
- ng-model 指令用于绑定变量,将用户在文本框输入的内容绑定到变量上，而表达式可以实时地输出变量。
- ng-init 对变量初始化或调用某方法。
- ng-controller 用于指定所使用的控制器，在控制器中定义函数和变量，通过scope 对象来访问。
- ng-click 单击事件指令，点击时触发控制器的某个方法。
- ng-repeat 指令用于循环集合变量。
- $index 用于获取 ng-repeat 指令循环中的索引。
- $http 内置服务，用于访问后端数据。

*复选框的使用*

定义一个用于存储选中 ID 的数组，当我们点击复选框后判断是选择还是取消选择，如果是选择就加到数组中，如果是取消选择就从数组中移除。在后续点击删除按钮时需要用到这个存储了 ID 的数组。

```javascript
	// 存储当前选中复选框的id集合
	$scope.selectIds = [];
	$scope.updateSelection = function($event, id){
		if ($event.target.checked) {		// 当前为勾选状态
			$scope.selectIds.push(id); 		// 向selectIds集合中添加元素
		} else {
			var index = $scope.selectIds.indexOf(id); 	
			$scope.selectIds.splice(index, 1); 	// 参数1：移除的下标位置，参数2：需要移除的元素个数
		}
	}
```



### 规格及模板管理

*前端分层开发*

运用 MVC 的思想，将 js 和 html 代码分离，提高程序的可维护性。

实现方式：自定义服务，同后端的 service 层，封装一些操作，比如请求后端数据。在不同控制器通过依赖注入相关服务，即可调用服务的方法。将代码分为前端页面、前端服务层、前端控制层。

*主键回填*

修改 Mapper.xml 文件

```xml
<selectKey resultType="java.lang.Long" order="AFTER" keyProperty="id">
	SELECT LAST_INSERT_ID() AS id
</selectKey>
```

对于规格与具体规格选项，可以创建一个组合实体类，包括 规格 和 规格选项的集合。在插入规格之后，通过主键回填，获取规格 ID ，然后将 ID 作为外键添加到规格选项中去。

![规格管理](https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E8%A7%84%E6%A0%BC%E7%AE%A1%E7%90%86.png)

  *select2 组件-多选下拉列表*

1. 引入 select 2 相关的 js 和 css。

2. 设置数据源

   ```javascript
   $scope.brandList={data:[{id:1,text:'联想'},{id:2,text:'华为'},{id:3,text:'小米'}]};	// 品牌列表
   ```

3. 实现多选下拉框

   ```html
   <input  select2  select2-model="entity.brandIds"  config="brandList"  multiple placeholder=" 选择品牌（可多选）" class="form-control" type="text"/> 
   
   ```

   multiple 表示可多选
   Config 用于配置数据来源
   select2-model 用于指定用户选择后提交的变量

![select2多选下拉列表](https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E5%A4%9A%E9%80%89%E4%B8%8B%E6%8B%89%E6%A1%86.png)

*模板列表显示*

将从后台获取的 json 字符串中的某个属性的值提取出来，用逗号分隔，更直观的显示。

```javascript
// 提取 json 字符串数据中某个属性，返回拼接字符串逗号分隔
$scope.jsonToString = function(jsonString,key){
	var json=JSON.parse(jsonString); // 将 json 字符串转换为 json 对象
	var value="";
	for(var i=0;i<json.length;i++){ 
		if(i>0) value += ","；
		value += json[i][key];
	}
	return value;
}
```

![类型模板管理](https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E5%95%86%E5%93%81%E7%B1%BB%E5%9E%8B%E6%A8%A1%E6%9D%BF%E7%AE%A1%E7%90%86.png)


### Spring Security  安全框架

为基于 Spring 的企业应用系统提供声明式的安全访问控制的解决方案。提供一组可以在 Spring 应用上下文中配置的 Bean。

*使用步骤*

- 引入 jar 包

```xml
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-web</artifactId>
	<version>4.1.0.RELEASE</version>
</dependency>
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-config</artifactId>
	<version>4.1.0.RELEASE</version>
</dependency>
```

- web.xml 文件中引入 spring-security.xml 配置文件

```xml
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:spring/spring-security.xml</param-value>
	 </context-param>
	 <listener>
		<listener-class>
			org.springframework.web.context.ContextLoaderListener
		</listener-class>
	 </listener>
	
	 <filter>  
		<filter-name>springSecurityFilterChain</filter-name>  
		<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>  
	 </filter>  
	 <filter-mapping>  
		<filter-name>springSecurityFilterChain</filter-name>  
		<url-pattern>/*</url-pattern>  
	 </filter-mapping>
```



- spring-security.xml 配置文件设置页面拦截规则、认证管理器以及不拦截的资源（静态资源、登陆页面）

```xml
	<!-- 设置页面不登陆也可以访问 -->
	<http pattern="/*.html" security="none"></http>
	<http pattern="/css/**" security="none"></http>
	<http pattern="/img/**" security="none"></http>
	<http pattern="/js/**" security="none"></http>
	<http pattern="/plugins/**" security="none"></http>

	<!-- 页面的拦截规则    use-expressions:是否启动SPEL表达式 默认是true -->
	<http use-expressions="false">
		<!-- 当前用户必须有ROLE_USER的角色 才可以访问根目录及所属子目录的资源 -->
		<intercept-url pattern="/**" access="ROLE_ADMIN"/>
		<!-- 开启表单登陆功能 -->
		<form-login login-page="/login.html" default-target-url="/admin/index.html" authentication-failure-url="/login.html" always-use-default-target="true"/>
		<csrf disabled="true"/>
		<headers>
			<frame-options policy="SAMEORIGIN"/>
		</headers>
		<logout/>	<!-- 退出登录 -->
	</http>
	
	<!-- 认证管理器 -->
	<authentication-manager>
		<authentication-provider>
			<user-service>
				<user name="admin" password="123456" authorities="ROLE_ADMIN"/>
				<user name="yang" password="123456" authorities="ROLE_ADMIN"/>
			</user-service>
		</authentication-provider>	
	</authentication-manager>
```

CSRF（Cross-site request forgery）跨站请求伪造，也被称为“One Click Attack”或者Session Riding，通常缩写为CSRF或者XSRF，是一种对网站的恶意利用。

XSS(跨站脚本攻击)利用站点内的信任用户，往Web页面里插入恶意Script代码 。

CSRF通过伪装来自受信任用户的请求来利用受信任的网站。 
















[1]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E9%9D%A2%E5%90%91%E6%9C%8D%E5%8A%A1%E7%9A%84%E6%9E%B6%E6%9E%84.jpg
[2]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/Dubbox%E5%8E%9F%E7%90%86.jpg
[3]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E5%93%81%E7%89%8C%E7%AE%A1%E7%90%86.png
[4]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E5%93%81%E7%89%8C%E4%BF%AE%E6%94%B9%E4%B8%8E%E6%96%B0%E5%A2%9E.png