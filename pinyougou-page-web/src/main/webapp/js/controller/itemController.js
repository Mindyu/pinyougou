 //控制层 
app.controller('itemController' ,function($scope, $http){	
	 
	 $scope.specificationItems={};	// 存储用户选择的规格
	 
	 // 数量加减
	 $scope.addNum=function(x){
		 $scope.num+=x;
		 if ($scope.num<1) $scope.num=1;
	 }
	 
	 // 选择规格
	 $scope.selectSpecification=function(key,value){
		 $scope.specificationItems[key]=value;
		 searchSku();	// 查询sku
	 }
	 
	 // 判断规格是否被选中
	 $scope.isSelected=function(key,value){
		 if($scope.specificationItems[key]==value){
			 return true;
		 }return false;
	 }
	 
	 $scope.sku={};
	 // 加载默认的sku信息
	 $scope.loadSku=function(){
		 $scope.sku=skuList[0];
		 $scope.specificationItems=JSON.parse(JSON.stringify($scope.sku.spec)); // 深克隆
	 }
	 
	 // 判断两个对象是否匹配
	 isEqual=function(map1,map2){
		 
		 for(var k in map1){
			 if(map1[k]!=map2[k]){
				 return false;
			 }
		 }
		 for(var k in map2){
			 if(map2[k]!=map1[k]){
				 return false;
			 }
		 }
		 return true;
	 }
	 
	 // 根据规格查询sku信息
	 searchSku=function(){
		 
		 for(var i=0;i<skuList.length;i++){
			 if( isEqual($scope.specificationItems, skuList[i].spec) ){
				 $scope.sku=skuList[i];
				 return;
			 }
		 }
		 $scope.sku={id:0,title:'--------',price:0};
	 }
	 // 添加到购物车
	 $scope.addToCart=function(){
		 // alert('sku_id:'+ $scope.sku.id);
		 // 执行跨域请求
		 $http.get("http://localhost:9107/cart/addGoodsToCartList.do?itemId="+ $scope.sku.id +"&num=" 
				 + $scope.num, {'withCredentials':true}).success(
				 function(response){
					 if (response.success) {
						location.href="http://localhost:9107/cart.html";	// 跳转到购物车页面
					}else{
						alert(response.message);
					}
				 }
		 );
	 }
	 
});	
