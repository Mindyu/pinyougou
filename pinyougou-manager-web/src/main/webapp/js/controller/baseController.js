app.controller('baseController', function($scope) {	// 通用 Controller

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
	
	// 刷新列表
	$scope.reloadList = function(){
		$scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
	}
	
	// 存储当前选中复选框的id集合
	$scope.selectIds = [];
	$scope.updateSelection = function($event, id){
		if ($event.target.checked) {		// 当前为勾选状态
			$scope.selectIds.push(id); 	// 向selectIds集合中添加元素
		} else {
			var index = $scope.selectIds.indexOf(id); 	
			$scope.selectIds.splice(index, 1); 	// 参数1：移除的下标位置，参数2：需要移除的元素个数
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
	
	// 将 json 字符串转 特定简写格式
	$scope.jsonToString = function(jsonString, key){
		var json = JSON.parse(jsonString);
		var value = "";
		
		for (var i = 0; i < json.length; i++) {
			if(i > 0) value += ",";
			value += json[i][key];
		}
		return value;
	}
});