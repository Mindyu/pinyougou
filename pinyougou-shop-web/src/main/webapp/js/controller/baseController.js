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
	
	// 在list集合中根据某key的值查询对象	
	/**
	 *  eg：list = [
	 *  {"attributeName":"网络","attributeValue":["移动3G","移动4G","联通3G"]},
	 *  {"attributeName":"机身内存","attributeValue":["128G","64G"]}	]
	 *  key = "attributeName", keyValue="网络"
	 */
	$scope.searchObjectByKey=function(list, key, keyValue){
		for (var i = 0; i < list.length; i++) {
			if (list[i][key] == keyValue) {		// list[i]['key']	表示集合中属性为‘key’对应的值为keyValue
				return list[i];
			}
		}
		return null;
	}
});