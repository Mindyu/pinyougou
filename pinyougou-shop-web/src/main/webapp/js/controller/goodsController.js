 //控制层 
app.controller('goodsController' ,function($scope,$controller,goodsService,uploadService,itemCatService,typeTemplateService){	
	
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
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	// 增加商品
	$scope.add=function(){
		$scope.entity.goodsDesc.introduction=editor.html();
		goodsService.add($scope.entity).success(
				function(response){
					if(response.success){
						alert('保存成功');
						$scope.entity={};
						editor.html("");	//	清空富文本编辑器
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
	
	$scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};
	
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
						$scope.entity.goods.category2Id = "";
					}
				);
		}
	});
	
	// angularjs变量监控方法,查询三级分类信息
	$scope.$watch('entity.goods.category2Id',function(newValue, oldValue){
		if(newValue == ""){
			$scope.entity.goods.category3Id = "";
		}else if (newValue != undefined) {
			// alert("category2Id"+newValue);
			itemCatService.findByParentId(newValue).success(
					function(response){
						$scope.itemCat3List = response;
						$scope.entity.goods.category3Id = "";
					}
				);
		}
	});
	
	// 读取模板ID
	$scope.$watch('entity.goods.category3Id',function(newValue, oldValue){
		if(newValue == ""){
			$scope.entity.goods.typeTemplateId = "";
		}else if (newValue != undefined) {
			// alert("category3Id"+newValue);
			itemCatService.findOne(newValue).success(
					function(response){
						$scope.entity.goods.typeTemplateId = response.typeId;
					}
			);
		}
	});
	
	// 读取模板ID对应的品牌列表、扩展属性、规格列表
	$scope.$watch('entity.goods.typeTemplateId',function(newValue, oldValue){
		if (newValue == ""){
			$scope.typeTemplate = "";							// 类型模板对象
			$scope.typeTemplate.brandIds = "";					// 品牌转换为Json对象
			$scope.entity.goodsDesc.customAttributeItems = "";	// 扩展属性
			$scope.specList = "";								// 规格列表
		}else if (newValue != undefined) {
			// alert("监控typeTemplateId信息变化："+newValue);
			typeTemplateService.findOne(newValue).success(
					function(response){
						$scope.typeTemplate = response;	// 类型模板对象
						$scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);	// 品牌转换为Json对象
						// 扩展属性
						$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
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
	
});	
