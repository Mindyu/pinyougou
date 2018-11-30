## 品优购项目学习

​	  一个综合性的 B2B2C 的电商网站系统。网站采用商家入驻的模式，商家入驻平台提交申请，有平台进行资质审核，审核通过后，商家拥有独立的管理后台录入商品信息。商品经过平台审核后即可发布。 

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
​	致力于提供高性能和透明化的RPC远程服务调用方案，以及SOA服务治理方案。远程服务调用的分布式框架。

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

- ng-if 判断语句，条件不存在就不执行。

- ng-repeat 指令用于循环集合变量。

- $index 用于获取 ng-repeat 指令循环中的索引。

- $http 内置服务，用于访问后端数据。

- $location 服务，用于获取链接地址中的参数值。 $location.search()['id']可以获取地址栏中id对应的值。(注：地址中 ? 前需要添加 # )

  eg:  http://localhost:9102/admin/goods_edit.html#?id=149187842867969

*复选框的使用*

​	定义一个用于存储选中 ID 的数组，当我们点击复选框后判断是选择还是取消选择，如果是选择就加到数组中，如果是取消选择就从数组中移除。在后续点击删除按钮时需要用到这个存储了 ID 的数组。

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

​	运用 MVC 的思想，将 js 和 html 代码分离，提高程序的可维护性。

​	实现方式：自定义服务，同后端的 service 层，封装一些操作，比如请求后端数据。在不同控制器通过依赖注入相关服务，即可调用服务的方法。将代码分为前端页面、前端服务层、前端控制层。

*主键回填*

​	修改 Mapper.xml 文件

```xml
<selectKey resultType="java.lang.Long" order="AFTER" keyProperty="id">
	SELECT LAST_INSERT_ID() AS id
</selectKey>
```

​	对于规格与具体规格选项，可以创建一个组合实体类，包括 规格 和 规格选项的集合。在插入规格之后，通过主键回填，获取规格 ID ，然后将 ID 作为外键添加到规格选项中去。

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

​	将从后台获取的 json 字符串中的某个属性的值提取出来，用逗号分隔，更直观的显示。

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

​	为基于 Spring 的企业应用系统提供声明式的安全访问控制的解决方案。提供一组可以在 Spring 应用上下文中配置的 Bean。

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

​	CSRF（Cross-site request forgery）跨站请求伪造，也被称为“One Click Attack”或者Session Riding，通常缩写为CSRF或者XSRF，是一种对网站的恶意利用。

​	XSS(跨站脚本攻击)利用站点内的信任用户，往Web页面里插入恶意Script代码 。

​	CSRF通过伪装来自受信任用户的请求来利用受信任的网站。 

### 商家系统登录安全控制

*安全控制*

1. 自定义认证类，创建类 UserDetailsServiceImpl.java 实现 UserDetailsService 接口
2. 实现类中添加 SellerService 属性、和 setter 注入方法，修改 loadUserByUserName 方法。
3. 配置 spring-security.xml。认证管理器中 authentication-provider 引用userDetailService 的bean，同时通过 dobbo 去依赖一个 sellerService 对象。

*BCrypt 加密算法*

​	用户表的密码通常使用 MD5 等不可逆算法加密后存储，为防止彩虹表破解更会先使用
一个特定的字符串（如域名）加密，然后再使用一个随机的 salt（盐值）加密。 特定字符串是程序代码中固定的，salt 是每个密码单独随机，一般给用户表加一个字段单独存储，比较麻烦。 BCrypt 算法将 salt 随机并混入最终加密后的密码，验证时也无需单独提供之前的 salt，从而无需单独处理 salt 问题。

```java
/**
 *  认证类
 * @author YCQ
 *
 */
public class UserDetailsServiceImpl implements UserDetailsService{

	private SellerService sellerService;
	
	public void setSellerService(SellerService sellerService) {	// 通过配置的方式添加
		this.sellerService = sellerService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//		System.out.println("执行 UserDetailsServiceImpl 认证");
		// 构建角色列表
		List<GrantedAuthority> grantAuths = new ArrayList<>();
		grantAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
		
		TbSeller seller = sellerService.findOne(username);
		if (seller!=null && "1".equals(seller.getStatus())) {
			return new User(username, seller.getPassword(), grantAuths);
		}else {
			return null;
		}
	}

}
```

spring-security 配置
```xml
	<!-- 认证管理器 -->
	<authentication-manager>
		<authentication-provider user-service-ref="userDetailService">
			<password-encoder ref="bcryptEncoder"></password-encoder>
		</authentication-provider>	
	</authentication-manager>
	
	<!-- 认证类 -->
	<beans:bean id="userDetailService" class="com.pinyougou.service.UserDetailsServiceImpl">
		<beans:property name="sellerService" ref="mSellerService"></beans:property>
	</beans:bean>
	
	<!-- 引用dubbo 服务 -->
	<dubbo:application name="pinyougou-shop-web" />
	<dubbo:registry address="zookeeper://107.191.52.91:2181"/>
	<dubbo:reference id="mSellerService" interface="com.pinyougou.sellergoods.service.SellerService"></dubbo:reference>
	
	<beans:bean id="bcryptEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder"></beans:bean>
```

*注：浏览器控制台提示 [DOM] Input elements should have autocomplete attributes (suggested: "current-password") 为浏览器表单默认的记忆功能，可以在 input 标签中添加 autocomplete="off|on" 即可。*

### 商品分类管理

*多级分类列表*

​	将商品分类分为三级，进入页面首先显示所有一级分类（主分类），点击查询下级，可查看当前主分类下的次分类，再次点击进入三级分类。三级分类为最后一级，列表中不显示查询下级按钮，同时更新面包屑导航。直接点击面包屑导航，可以实现直接层级跳转。

*面包屑导航*

```javascript
	// 当前面包屑等级
	$scope.grade = 1;
	$scope.setGrade=function(value){
		$scope.grade = value;
	}
	
	$scope.selectList=function(p_entity){
		if ($scope.grade == 1) {
			$scope.entity_1 = null;
			$scope.entity_2 = null;
		} else if ($scope.grade == 2){
			$scope.entity_1 = p_entity;
			$scope.entity_2 = null;
		} else {
			$scope.entity_2 = p_entity;
		}
		$scope.findByParentId(p_entity.id);
	}
	
```

页面配置

```html
<li><a href="#" ng-click="grade=1;selectList({id:0})">顶级分类列表</a></li>
<li><a href="#" ng-click="grade=2;selectList(entity_1)">{{entity_1.name}}</a></li>
<li ng-if="entity_2!=null"><a href="#" ng-click="grade=3;selectList(entity_2)">{{entity_2.name}}</a></li>
```



*修改商品分类*

​	实现类型模板的下拉框，采用 select2 组件实现。

```html
<td>类型模板</td>
<td><input select2 ng-model="entity.typeId" config="itemList" placeholder="商品类型模板" class="form-control" type="text"/>
</td>
```

​	config 为数据来源

​	ng-model 绑定类型对象数据

​	itemList 的来源：itemCatController 中 findItemList() 方法 -> typeTemplateService 的 selectOptionList() 方法 -> 请求后端 /typeTemplate/selectOptionList -> TypeTemplateService 服务层 -> TypeTemplateMapper 层方法



*删除商品分类*

​	判断所选分类下是否存在子分类，存在则不能删除。

```java
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long[] ids){
		try {
			// 判断当前所有分类是否存在子分类
			boolean flag = false;	// 不存在
			for (Long id : ids) {
				if(itemCatService.findByParentId(id)!=null && itemCatService.findByParentId(id).size()!=0){
					flag = true;break;
				}
			}
			if (flag) return new Result(false, "当前所选分类存在子分类，切勿删除"); 
			
			itemCatService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
```



*SPU 与 SKU*

​	SPU （标准产品单位）为商品信息聚合的最小单位是一组可复用、易检索的标准化信息的集合,该集合描述了一个产品的特性。属性相同、特性相同的商品为一个SPU。

​	SKU （库存量单位） 为物理上不可分割的最小存货单元。不同的规格、颜色、款式为不同的SKU。



*分布式文件服务器 FastDFS*

​	FastDFS 是用 c 语言编写的一款开源的分布式文件系统。FastDFS 为互联网量身定制,充分考虑了**冗余备份、负载均衡、线性扩容**等机制,并注重**高可用、高性能**等指标,使用FastDFS 很容易搭建一套高性能的文件服务器集群提供文件上传、下载等服务。
	FastDFS 架构包括 Tracker server 和 Storage server。

- Tracker server （追踪服务器、调度服务器）作用为负载均衡和调度。

- Storage server （存储服务器）作用为文件存储。

   客户端请求 Tracker server 进行文件上传、下载,通过 Tracker server 调度最终由 Storage server 完成文件上传和下载。	

   服务端角色：

- Tracker : 管理集群，tracker也可以实现集群，每一个节点地位平等，一种备份的机制。tracker负责收集 storage 集群的存储状态。

- Stroage ：实际保存文件。分为多个组，组内文件相同，起到备份作用。组间文件不同，起到分布式存储。

![图片上传](https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E5%9B%BE%E7%89%87%E4%B8%8A%E4%BC%A0.png)



*商品分类级联刷新*

​	通过 Angular JS 变量监控方法，实现选择一级分类之后，初始化二级分类的列表信息。

```javascript
	// angularjs变量监控方法,查询二级分类信息
	$scope.$watch('entity.goods.category1Id',function(newValue, oldValue){
		if (newValue != undefined && newValue != "") {
			// alert("category1Id"+newValue);
			itemCatService.findByParentId(newValue).success(
					function(response){
						$scope.itemCat2List = response;
						$scope.entity.goods.category2Id = "";
					}
				);
		}
	});
```

![商品分类级联刷新](https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E7%BA%A7%E8%81%94%E5%88%B7%E6%96%B0.png)





*商品录入【SKU商品信息】* 

对于同一个产品分为多种不同的规格组合。根据选择的规格录入商品的 SKU 信息，当用户选择相应的规格，下面的 SKU 列表就会自动生成。

![规格管理](https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E5%95%86%E5%93%81%E8%A7%84%E6%A0%BC%E7%BB%84%E5%90%88.png)

实现思路：
（1）我们先定义一个初始的不带规格名称的集合，只有一条记录。
（2）循环用选择的规格，根据规格名称和已选择的规格选项对原集合进行扩充，添加规格名称和值，新增的记录数与选择的规格选项个数相同

```javascript
	// 创建SKU列表
	$scope.creatItemList=function(){
		// 列表初始化，规格对象、价格、库存量、状态、是否默认
		$scope.entity.itemList = [ {spec:{},price:0,num:9999,status:'0',isDefault:'0'} ];
		
		var items = $scope.entity.goodsDesc.specificationItems;
		
		for (var i = 0; i < items.length; i++) {
			$scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);
		}
	}
	
	/**
	 * $scope.entity.itemList:
	 * [{"spec":{"网络":"移动3G","机身内存":"16G"},"price":0,"num":9999,"status":"0","isDefault":"0"},
	 * {"spec":{"网络":"移动3G","机身内存":"32G"},"price":0,"num":9999,"status":"0","isDefault":"0"},
	 * {"spec":{"网络":"联通3G","机身内存":"16G"},"price":0,"num":9999,"status":"0","isDefault":"0"},
	 * {"spec":{"网络":"联通3G","机身内存":"32G"},"price":0,"num":9999,"status":"0","isDefault":"0"}]
	 */
	
	// 深克隆方法   原集合、列名、列值
	addColumn=function(list, columnName, columnValues){
		var newList = [];
		
		for (var i = 0; i < list.length; i++) {
			var oldRow = list[i];
			for (var j = 0; j < columnValues.length; j++) {
				var newRow = JSON.parse( JSON.stringify(oldRow) );
				newRow.spec[columnName] = columnValues[j];
				newList.push(newRow);
			}
		}
		return newList;
	}
```



*商家后台列表显示*

![商家商品管理](https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E5%95%86%E5%AE%B6%E5%95%86%E5%93%81%E7%AE%A1%E7%90%86.png)

状态显示：

​	商品信息表（goods）中状态子段为 audit_status 。存储的为数字，0表示未审核、1表示已审核、2表示审核未通过、3为已关闭。从后台获取的状态值，直接在前端进行修改。通过一个status数组存储：

​	$scope.status=['未审核','已审核','审核未通过','关闭'];//商品状态

​	然后列表中显示为 {{status[entity.auditStatus]}}。

分类信息显示：

​	商品分为三级分类。存储于 tb_item_cat 表中。包括 id、父级id、分类名称、对应绑定的类型id。但是为了避免商品查询时重复的关联查询，可以采用现将所有分类信息读取到本地，然后在前端进行分类id到分类名称的转换操作。

```javascript
	$scope.itemCatList = [];
	// 全部商品分类查询，存储在itemList数组中，然后再前端页面通过数组下标直接将商品分类ID转换为商品分类名称，避免后端连接查询。
	$scope.findItemList = function(){
		itemCatService.findAll().success(
				function(response){
					for (var i = 0; i < response.length; i++) {
						$scope.itemCatList[response[i].id] = response[i].name;
					}
				}
		);
	}
```

​	将分类结果 response 对象封装为数组类型，数组下标为商品分类id，数组值为商品分类的名称。然后在列表项中通过 {{itemCatList[entity.category1Id]}} 将id转换为名称。

*存在的问题*

​	pinyougou-shop-web 模块中分页插件提示 ClassNotFoundException。但是页面可以访问。

![](https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/error_page.png)

```xml
			<dependency>
			    <groupId>com.github.pagehelper</groupId>
			    <artifactId>pagehelper</artifactId>
			    <version>${pagehelper.version}</version>
			</dependency>	
```

​	如上配置之后，又出现下图错误，导致商品列表无法显示。（但是 manager-web 模块中也没有引入pagehelper,但是没有出现问题）

![](https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/error_RowBounds.png)



商品删除

​	逻辑删除，通过修改数据库表中的 is_delete 字段为1，然后过滤掉商品。然后查询时，在 findPage() 方法中添加 criteria.andIsDeleteIsNull() 条件。



*注解式事务配置*

​	创建  applicationContext-tx.xml 配置文件

```xml
<!-- 事务管理器 -->
<bean id="transactionManager"
class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
	<property name="dataSource" ref="dataSource" />
</bean>
<!-- 开启事务控制的注解支持 -->
<tx:annotation-driven transaction-manager="transactionManager"/>
```

​	然后在方法或服务实现类上添加 @Transactional 注解。



*网站前台广告服务*

​	设计为广告分类表（id、name）与广告内容表（id、categoryId、title、url、pic、status、order）。广告有首页轮播广告、今日推荐、各品类楼层广告等分类。



​	Redis 缓存数据库用于解决高访问量对后端数据库造成的很大的访问压力。（另一种解决方案为网页静态化）

​	Spring Data Redis 提供了在 srping 应用中通过简单的配置访问 redis 服务，对 reids 底层开发包(Jedis, JRedis, and RJC)进行了高度封装，RedisTemplate 提供了 redis 各种操作、异常处理及序列化，支持发布订阅，并对 spring 3.1 cache 进行了实现。

spring-data-redis 针对 jedis 提供了如下功能：
	1.连接池自动管理，提供了一个高度封装的“RedisTemplate”类。
	2.针对 jedis 客户端中大量 api 进行了归类封装,将同一类型操作封装为 operation 接口

操作样例：

key-value 键值对操作

​	插入：redisTemplate.boundValueOps("name").set("mindyu");

​	读取：redisTemplate.boundValueOps("name").get();

​	删除：redisTemplate.delete("name");

Set 类型操作（无序集合）

​	插入：redisTemplate.boundSetOps("nameset").add("曹操"); 

​	读取：redisTemplate.boundSetOps("nameset").members();

​	删除：redisTemplate.boundSetOps("nameset").remove("曹操");	// 单一元素

redisTemplate.delete("name");  // 整个集合

List 集合 （有序）

​	rightPush() 、leftPush()、读取：range(0,10)、index(1)、remove(1, "value") // 1 表示删除数据的个数

Hash 类型

​	put("key","value")、读取所有键：keys()、读取所有值：values()、get(“key”)、delete("key")

​	使用 Redis 缓存时，需要注意，当数据修改时需要清除缓存数据，使其达到一致性约束。必须修改广告时，如果修改了该广告所属的分类，那么需要同时清除原分类以及新分类的缓存信息。



出现的问题：

​	首页在加载广告模块时，出现 “Failed to load resource: net::ERR_BLOCKED_BY_CLIENT” 错误，是因为谷歌浏览器的广告插件，导致无法加载该图片。










[1]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E9%9D%A2%E5%90%91%E6%9C%8D%E5%8A%A1%E7%9A%84%E6%9E%B6%E6%9E%84.jpg
[2]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/Dubbox%E5%8E%9F%E7%90%86.jpg
[3]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E5%93%81%E7%89%8C%E7%AE%A1%E7%90%86.png
[4]: https://hexoblog-1253306922.cos.ap-guangzhou.myqcloud.com/photo2018/%E5%93%81%E4%BC%98%E8%B4%AD/%E5%93%81%E7%89%8C%E4%BF%AE%E6%94%B9%E4%B8%8E%E6%96%B0%E5%A2%9E.png