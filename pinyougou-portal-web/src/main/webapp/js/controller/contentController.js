app.controller('contentController',function($scope,contentService){
	
	$scope.contentList=[];	// 所有广告的集合
	
	$scope.findByCategoryId=function(categoryId){
		contentService.findByCategoryId(categoryId).success(
				function(response){
					$scope.contentList[categoryId] = response;
				}
		);
	}
	
	// 搜索（传递参数）
	$scope.search=function(keywords){
		location.href="http://localhost:9104/search.html#?keywords="+$scope.keywords;
	}
	
});