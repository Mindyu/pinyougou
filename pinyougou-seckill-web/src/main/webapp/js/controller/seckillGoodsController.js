app.controller('seckillGoodsController', function($scope, $location, $interval, seckillGoodsService){ 
	
	// 读取列表数据绑定到表单中
	$scope.findList=function(){
		seckillGoodsService.findList().success(
			function(response){
				$scope.list=response;
			} 
		);
	}
	
	//查询实体
	$scope.findOne=function(){ 
		seckillGoodsService.findOne($location.search()['id']).success(
			function(response){
				$scope.entity = response;
				totalSecond = Math.floor((new Date($scope.entity.endTime).getTime() - (new Date().getTime()))/1000);
				time = $interval(function(){	
					if (totalSecond>0) {
						$scope.timeString = convertSecondToTime(totalSecond);	
						--totalSecond;
					}else{
						alert("秒杀已结束");
						$interval.cancel(time);
					}
				}, 1000);
			}
		);
	}
	
	convertSecondToTime=function(totalSecond){
		var sec = totalSecond%60;
		totalSecond = Math.floor(totalSecond/60);
		var min = totalSecond%60;
		totalSecond = Math.floor(totalSecond/60);
		var hour = totalSecond%24;
		totalSecond = Math.floor(totalSecond/24);
		var day = totalSecond;
		return day==0? hour+":"+min+":"+sec : day+"天 "+hour+":"+min+":"+sec;
	}
	
	$scope.submitOrder=function(){
		seckillGoodsService.submitOrder($scope.entity.id).success(
				function(response){
					if (response.success) {
						alert("抢购成功，请在五分钟内付款");
						location.href="pay.html";
					}else{
						alert(response.message);
					}
				}
		);
	}
	
});