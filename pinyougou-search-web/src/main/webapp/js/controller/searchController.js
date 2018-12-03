app.controller('searchController', function($scope, searchService) {
	
	// 定义搜索对象的结构   category：商品分类 brand：品牌  spec:规格
	$scope.searchMap={'keywords':'','category':'','brand':'',spec:{}};
	
	// 搜索
	$scope.search = function() {
		searchService.search($scope.searchMap).success(function(response) {
			$scope.resultMap = response;	// 搜索返回的结果
		});
	}
	
	// 添加查询搜索项
	$scope.addSearchItem=function(key,value){
		if (key == 'brand' || key == 'category') {	// 如果点击品牌和分类
			$scope.searchMap[key] = value;
		}else{
			$scope.searchMap.spec[key]=value;
		}
		$scope.search();
	}
	
	$scope.removeSearchItem=function(key){
		if (key == 'brand' || key == 'category') {	// 如果点击品牌和分类
			$scope.searchMap[key] = "";
		}else{
			delete $scope.searchMap.spec[key];
		}
		$scope.search();
	}
});