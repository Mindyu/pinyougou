app.controller('brandController', function($scope, $http, brandService) {

		// 查询品牌列表
		$scope.findAll = function() {
			brandService.findAll().success(function(response) {
				$scope.list = response;
			});
		}
		
		// 分页控件配置 
		$scope.paginationConf = {
				 currentPage: 1,
				 totalItems: 10,
				 itemsPerPage: 10,
				 perPageOptions: [10, 20, 30, 40, 50],
				 onChange: function(){
				 	$scope.reloadList();//重新加载
				 }
		}
		
		// 分页查询
		$scope.findPage = function(page, size){
			brandService.findPage().success(function(response){
				$scope.list = response.rows;	// 显示当前页数据
				$scope.paginationConf.totalItems = response.total;
			});
		}

		// 刷新列表
		$scope.reloadList = function(){
			$scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
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
		
		// 存储当前选中复选框的id集合
		$scope.selectedIDs = [];
		
		$scope.updateSelectedIDS = function($event, id){
			if ($event.target.checked) {		// 当前为勾选状态
				$scope.selectedIDs.push(id); 	// 向selectedIDs集合中添加元素
			} else {
				var index = $scope.selectedIDs.indexOf(id); 	
				$scope.selectedIDs.splice(index, 1); 	// 参数1：移除的下标位置，参数2：需要移除的元素个数
			}
		}
		
		// 删除
		$scope.dele = function(){
			if (confirm('确定要删除吗？')) {
				brandService.dele($scope.selectedIDs).success(
						function(response){
							if (response.success) {
								$scope.reloadList();	//重新加载
							}else {
								alert(response.message);
							}
					});
			}
		}
		
		// 回车则执行保存
		$scope.enter = function($event){
			//IE 编码包含在window.event.keyCode中，Firefox或Safari 包含在event.which中
			var keycode = window.event?e.keyCode:e.which;
			if (keycode == 13) {
				$scope.save();
				// 关闭编辑框
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