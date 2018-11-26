//服务层
app.service('loginService',function($http){
	
	// 显示用户名
	this.loginName = function(){
		return $http.get("../login/name.do");
	}
	
});