//服务层
app.service('cartService',function($http){
	    	
	//查询购物车列表信息
	this.findCartList=function(){
		return $http.get('../cart/findCartList.do');
	}
	
	//添加商品到购物车
	this.addGoodsToCartList=function(itemId,num){
		return $http.get('cart/addGoodsToCartList.do?itemId='+itemId+'&num='+num);
	}
	
	// 购物车明细求和
	this.sum=function(cartList){
		var total = {totalNum:0, totalMoney:0};

		for(var i=0;i<cartList.length;i++){
			var cart = cartList[i];
			for(var j=0;j<cart.orderItemList.length;j++){
				var item=cart.orderItemList[j];		// 购物车明细
				total.totalNum +=item.num;
				total.totalMoney += item.totalFee;
			}
		}
		return total;
	}
	
	// 查询登录用户的收获地址信息
	this.findAddress=function(){
		return $http.get("address/findAddressByLoginUser.do");
	}
	
	// 提交订单
	this.submitOrder=function(order){
		return $http.post("order/add.do",order);
	}
});
