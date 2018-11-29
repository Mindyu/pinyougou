 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location,goodsService,uploadService,itemCatService,typeTemplateService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){
		var id = $location.search()['id'];
		if (id == null) {
			return;
		}
		goodsService.findOne(id).success(
			function(response){
				/**
				 * {"goods":
				 * 		{"auditStatus":"0","brandId":3,"caption":"性能发烧级手机","category1Id":558,"category2Id":559,"category3Id":560,
				 * 		"defaultItemId":null,"goodsName":"三星S9","id":149187842867969,"isDelete":null,"isEnableSpec":"1",
				 * 		"isMarketable":null,"price":5999,"sellerId":"xiaomi","smallPic":null,"typeTemplateId":35},
				 * "goodsDesc":
				 * 		{"customAttributeItems":"[{\"text\":\"内存大小\",\"value\":\"128G\"},{\"text\":\"颜色\",\"value\":\"黑色\"}]",
				 * 		"goodsId":149187842867969,"introduction":"性能贼厉害&nbsp;&nbsp;&nbsp;&nbsp;",
				 * 		"itemImages":"[{\"color\":\"黑色\",\"url\":\"http://192.168.25.133/group1/M00/00/00/wKgZhVv-dDSADF53AACKUM2lRlw932.jpg\"}]",
				 * 		"packageList":"精品","saleService":"一年包换、两年保修",
				 * 		"specificationItems":"[
				 * 			{\"attributeValue\":[\"移动4G\",\"电信4G\",\"双卡\"],\"attributeName\":\"网络\"},
				 * 			{\"attributeValue\":[\"128G\"],\"attributeName\":\"机身内存\"}]"
				 * 		},
				 * "itemList":[{},{},
				 * 		{"barcode":null,"brand":"三星","cartThumbnail":null,"category":"手机","categoryid":560,
				 * 		"costPirce":null,"createTime":"2018-11-28 18:57:12","goodsId":149187842867969,"id":1369293,
				 * 		"image":"url","isDefault":"0","itemSn":null,"marketPrice":null,"num":545,"price":6999,
				 * 		"sellPoint":null,"seller":"小米旗舰店","sellerId":"xiaomi",
				 * 		"spec":"{\"网络\":\"双卡\",\"机身内存\":\"128G\"}","status":"0","stockCount":null,
				 * 		"title":"三星S9 双卡 128G","updateTime":"2018-11-28 18:57:12"}	]
				 */
				$scope.entity= response;	
				editor.html($scope.entity.goodsDesc.introduction);	// 商品介绍富文本
				// 商品图片
				$scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);
				// 扩展属性
				$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
				// 规格选项
				$scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);
				
				// 转换SKU列表中的规格对象
				for (var i = 0; i < $scope.entity.itemList.length; i++) {
					$scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
				}
			}
		);				
	}
	
	//保存 
	$scope.save=function(){	
		$scope.entity.goodsDesc.introduction=editor.html();
		
		var serviceObject;					// 服务层对象  				
		if($scope.entity.goods.id!=null){	// 如果有ID
			serviceObject=goodsService.update( $scope.entity ); // 修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );	// 增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					alert('保存成功');
					location.href="goods.html";
					// $scope.init();
					// editor.html("");		// 清空富文本编辑器
				}else{
					alert(response.message);
				}
			}		
		);				
	}

	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
	//上传图片
	$scope.uploadFile=function(){
		uploadService.uploadFile().success(
			function(response){
				if(response.success){
					$scope.image_entity.url= response.message;
				}else{
					alert(response.message);					
				}
			}		
		);
	}
	
	// 初始化值
	$scope.init=function(){
		// 默认启用规格
		$scope.entity={goods:{isEnableSpec:'1'},goodsDesc:{itemImages:[],specificationItems:[]}};
		$scope.specList = [];	// 规格列表
	}
	
	//将当前上传的图片实体存入图片列表
	$scope.add_image_entity=function(){
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);			
	}
	
	//移除图片
	$scope.remove_image_entity=function(index){
		$scope.entity.goodsDesc.itemImages.splice(index,1);
	}
	
	// 查询一级商品分类列表
	$scope.selectItemCat1List=function(){
		itemCatService.findByParentId(0).success(
			function(response){
				$scope.itemCat1List = response;
			}
		);
	}
	
	// angularjs变量监控方法,查询二级分类信息
	$scope.$watch('entity.goods.category1Id',function(newValue, oldValue){
		if (newValue != undefined && newValue != "") {
			// alert("category1Id"+newValue);
			itemCatService.findByParentId(newValue).success(
					function(response){
						$scope.itemCat2List = response;
						if ($location.search()['id'] == null) $scope.entity.goods.category2Id = "";
					}
				);
		}
	});
	
	// angularjs变量监控方法,查询三级分类信息
	$scope.$watch('entity.goods.category2Id',function(newValue, oldValue){
		// alert("category2Id"+newValue);
		if(newValue == ""){
			$scope.entity.goods.category3Id = "";
		}else if (newValue != undefined) {
			itemCatService.findByParentId(newValue).success(
					function(response){
						$scope.itemCat3List = response;
						if ($location.search()['id'] == null) $scope.entity.goods.category3Id = "";
					}
				);
		}
	});
	
	// 读取模板ID
	$scope.$watch('entity.goods.category3Id',function(newValue, oldValue){
		// alert("category3Id"+newValue);
		if(newValue == ""){
			$scope.entity.goods.typeTemplateId = "";
		}else if (newValue != undefined) {
			itemCatService.findOne(newValue).success(
					function(response){
						$scope.entity.goods.typeTemplateId = response.typeId;
					}
			);
		}
	});
	
	// 读取模板ID对应的品牌列表、扩展属性、规格列表
	$scope.$watch('entity.goods.typeTemplateId',function(newValue, oldValue){
		// alert("typeTemplateId:" + newValue+"\n customAttributeItems:"+$scope.entity.goodsDesc.customAttributeItems);
		
		if (newValue == ""){
			$scope.typeTemplate = "";							// 类型模板对象
			$scope.typeTemplate.brandIds = "";					// 品牌转换为Json对象
			$scope.entity.goodsDesc.customAttributeItems = [];	// 扩展属性
			$scope.entity.goodsDesc.specificationItems = [];	// 规格项集合 [{"attributeName":"网络","attributeValue":["移动3G","联通3G"]},{"attributeName":"机身内存","attributeValue":["16G","64G"]}]
			$scope.specList = [];								// 规格列表
			$scope.entity.itemList = [];						
		}else if (newValue != undefined) {
			// alert("监控typeTemplateId信息变化："+newValue);
			typeTemplateService.findOne(newValue).success(
					function(response){
						$scope.typeTemplate = response;	// 类型模板对象
						$scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);	// 品牌转换为Json对象
						// 扩展属性
						if ($location.search()['id'] == null) {	// 如果为增加商品
							$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
						}
					}
			);
			// 读取规格
			typeTemplateService.findSpecList(newValue).success(
					function(response){
						$scope.specList = response;
					}
			);
		}
	});
	
	// {"attributeName":"网络","attributeValue":["移动2G","联通2G"]}
	$scope.updateSpecAttribute=function($event, name, value){
		
		var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems, 'attributeName', name);
		
		if (object != null) {
			if ($event.target.checked) {
				object.attributeValue.push(value);
			}else{
				object.attributeValue.splice(object.attributeValue.indexOf(value),1);	
				if (object.attributeValue.length == 0) 		// 当前规格选项全部清空时，将当前规格项也删除
					$scope.entity.goodsDesc.specificationItems.splice(
							$scope.entity.goodsDesc.specificationItems.indexOf(object),1);
			}
		}else{
			$scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
		}
	}
	
	// 创建SKU列表
	$scope.creatItemList=function(){
		// 列表初始化，规格对象、价格、库存量、状态、是否默认
		$scope.entity.itemList = [ {spec:{},price:0,num:9999,status:'0',isDefault:'0'} ];
		
		var items = $scope.entity.goodsDesc.specificationItems;
		
		for (var i = 0; i < items.length; i++) {
			$scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);
		}
	}
	
	/**
	 * $scope.entity.itemList:
	 * [{"spec":{"网络":"移动3G","机身内存":"16G"},"price":0,"num":9999,"status":"0","isDefault":"0"},
	 * {"spec":{"网络":"移动3G","机身内存":"32G"},"price":0,"num":9999,"status":"0","isDefault":"0"},
	 * {"spec":{"网络":"联通3G","机身内存":"16G"},"price":0,"num":9999,"status":"0","isDefault":"0"},
	 * {"spec":{"网络":"联通3G","机身内存":"32G"},"price":0,"num":9999,"status":"0","isDefault":"0"}]
	 */
	
	// 深克隆方法   原集合、列名、列值
	addColumn=function(list, columnName, columnValues){
		var newList = [];
		
		for (var i = 0; i < list.length; i++) {
			var oldRow = list[i];
			for (var j = 0; j < columnValues.length; j++) {
				var newRow = JSON.parse( JSON.stringify(oldRow) );
				newRow.spec[columnName] = columnValues[j];
				newList.push(newRow);
			}
		}
		return newList;
	}
	
	$scope.status=['未审核','已审核','审核未通过','已关闭'];
	
	$scope.itemCatList = [];
	// 全部商品分类查询，存储在itemList数组中，然后再前端页面通过数组下标直接将商品分类ID转换为商品分类名称，避免后端连接查询。
	$scope.findItemList = function(){
		itemCatService.findAll().success(
				function(response){
					for (var i = 0; i < response.length; i++) {
						$scope.itemCatList[response[i].id] = response[i].name;
					}
				}
		);
	}
	
	// 绑定规格选项框
	$scope.checkAttributeValue = function(specName, optionName){
		var items = $scope.entity.goodsDesc.specificationItems;
		var object = $scope.searchObjectByKey(items, 'attributeName', specName);
		
		if (object != null && object.attributeValue.indexOf(optionName)>=0) {	// 存在当前规格，且当前规格选项值中存在该选项
			return true;
 		}
		return false;
	}
});	
