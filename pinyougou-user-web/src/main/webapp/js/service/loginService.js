//服务层
app.service('loginService',function($http){
	    	
	//返回登陆的用户信息
	this.showUserName=function(){
		return $http.get('../login/name.do');
	}
	
});
