 //控制层 
app.controller('userController' ,function($scope,$controller,userService){	
	
	// 注册
	$scope.register=function(){
		// 判断两次输入密码是否一致
		if ($scope.entity.password!=$scope.password) {
			alert("两次输入的密码不一致，请重新输入");
			$scope.entity.password = "";
			$scope.password = "";
			return ;
		}
		
		/**
		 * 短信验证码的实现过程：
		 * 1. 点击发送验证码的逻辑
		 * 	a.生成一个6位随机的数字作为验证码
		 * 	b.将验证码存入redis中，手机号作为key，验证码作为值（redis中的数据在集群中共享）
		 *  c.将短信内容发送给ActiveMQ
		 *  
		 * 2. 校验验证码是否正确
		 *  a.用户注册前进行校验（用户输入的验证码和redis中的验证码进行比较）
		 */
		
		// 新增
		userService.add($scope.entity,$scope.smsCode).success(
			function(response){
				alert(response.message);
			}
		);
	}
	
	// 生成验证码
	$scope.createSmsCode=function(){
		userService.createSmsCode($scope.entity.phone).success(
				function(response){
					alert(response.message);
				}
		);
	}
	
});	
