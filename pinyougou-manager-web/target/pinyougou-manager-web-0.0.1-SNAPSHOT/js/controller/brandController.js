app.controller('brandController', function($scope, $controller, brandService) {
	
	// 一种伪继承的方式，两个 controller 的 scope 等价
	$controller('baseController', {$scope:$scope});	
	
	// 查询品牌列表
	$scope.findAll = function() {
		brandService.findAll().success(function(response) {
			$scope.list = response;
		});
	}
	
	// 分页查询
	$scope.findPage = function(page, size){
		brandService.findPage().success(function(response){
			$scope.list = response.rows;	// 显示当前页数据
			$scope.paginationConf.totalItems = response.total;
		});
	}
		
	// 新增
	$scope.save = function(){
		var object = null;
		if ($scope.entity.id == null) {
			object = brandService.add($scope.entity);
		}else {
			object = brandService.update($scope.entity);
		}
		object.success(
			function(response){
				if (response.success) {
					$scope.reloadList();	//重新加载
				}else {
					alert(response.message);
				}
		});
	}
	
	// 查询实体
	$scope.findOne = function(id){
		brandService.findOne(id).success(
			function(response){
				$scope.entity = response; 
		});
	}
	
	// 删除
	$scope.dele = function(){
		if (confirm('确定要删除吗？')) {
			brandService.dele($scope.selectIds).success(
					function(response){
						if (response.success) {
							$scope.reloadList();	//重新加载
						}else {
							alert(response.message);
						}
				});
		}
	}
		
	$scope.searchEntity = {};	// 初始化对象
	$scope.search = function(page, size){
		brandService.search(page, size, $scope.searchEntity).success(function(response){
			$scope.list = response.rows;	// 显示当前页数据
			$scope.paginationConf.totalItems = response.total;
		});
	}
});