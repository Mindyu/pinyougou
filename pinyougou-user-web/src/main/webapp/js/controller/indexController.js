//首页控制器
app.controller('indexController',function($scope,loginService){
	
	$scope.showUserName=function(){
		loginService.showUserName().success(
			function(response){
				$scope.loginName=response.userName;
			}
		);
	} 
	
});