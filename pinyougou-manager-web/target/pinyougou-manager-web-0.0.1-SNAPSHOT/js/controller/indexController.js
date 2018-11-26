app.controller("indexController", function($scope, $controller, loginService){
	
	// 读取当前登录的用户名
	$scope.showLoginName = function(){
		loginService.loginName().success(
			function(response){
				$scope.userName = response.loginName;
			}
		);
	}
	
});