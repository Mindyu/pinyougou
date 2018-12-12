//购物车控制层
app.controller('cartController', function($scope, cartService) {
	// 查询购物车列表
	$scope.findCartList = function() {
		cartService.findCartList().success(function(response) {
			$scope.cartList = response;
			$scope.totalValue = cartService.sum($scope.cartList);	// 计算购物车总额
		});
	}
	

	// 添加商品到购物车
	$scope.addGoodsToCartList = function(itemId, num) {
		cartService.addGoodsToCartList(itemId, num).success(function(response) {
			if (response.success) {
				$scope.findCartList();		// 刷新列表
			} else {
				alert(response.message);	// 弹出错误提示
			}
		});
	}
	
	// 查询用户的收货地址信息
	$scope.findAddress=function(){
		cartService.findAddress().success(
				function(response){
					$scope.addressList = response;
					// 查找默认地址
					for(var i=0;i<$scope.addressList.length;i++){
						if($scope.addressList[i].isDefault=='1'){
							$scope.address=$scope.addressList[i];break;
						}
					}
				}
		);
	}
	
	// 选择地址
	$scope.selectAddress=function(address){
		$scope.address = address;
	}
	
	// 改地址是否被选
	$scope.isSelectedAddress=function(address){
		return ($scope.address == address);
	}
	
	$scope.order={paymentType:'1'};
	
	// 选择支付方式
	$scope.selectPayType=function(type){
		$scope.order.paymentType = type;
	}
	
	// 提交订单
	$scope.submitOrder=function(){
		$scope.order.receiverAreaName = $scope.address.address;
		$scope.order.receiverMobile = $scope.address.mobile;
		$scope.order.receiver = $scope.address.contact;
		
		cartService.submitOrder($scope.order).success(
				function(response){
					if (response.success) {
						// 微信支付 就跳转到支付页面
						if ($scope.order.paymentType == '1' ) {
							location.href="pay.html";
						}else {	// 货到付款，跳转到提示页面
							location.href="paysuccess.html";
						}
					}else {
						alert(response.message);
					}
				}
		);
	}
	
});