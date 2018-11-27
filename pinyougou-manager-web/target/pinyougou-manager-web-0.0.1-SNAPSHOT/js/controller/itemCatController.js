 //控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService,typeTemplateService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	$scope.parentId = 0;
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update($scope.entity); 	//修改  
		}else{
			$scope.entity.parentId = $scope.parentId;
			serviceObject=itemCatService.add($scope.entity);		//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					$scope.findByParentId($scope.parentId);
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele($scope.selectIds).success(
			function(response){
				if(response.success){
					$scope.selectIds=[];
					$scope.findByParentId($scope.parentId);
				}else{
					alert(response.message);
				}					
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
	// 根据上级ID查询
	$scope.findByParentId=function(parentId){
		$scope.parentId = parentId;
		itemCatService.findByParentId(parentId).success(
			function(response){
				$scope.list = response;
			}
		);
	}
	
	// 当前面包屑等级
	$scope.grade = 1;
	$scope.setGrade=function(value){
		$scope.grade = value;
	}
	
	$scope.selectList=function(p_entity){
		if ($scope.grade == 1) {
			$scope.entity_1 = null;
			$scope.entity_2 = null;
		} else if ($scope.grade == 2){
			$scope.entity_1 = p_entity;
			$scope.entity_2 = null;
		} else {
			$scope.entity_2 = p_entity;
		}
		$scope.findByParentId(p_entity.id);
	}
	
	$scope.itemList={data:[]}; 	// 类型列表	[{"id":35,"text":"手机"},{"id":37,"text":"电视"}]
	
	// 读取品牌列表
	$scope.findItemList = function(){
		typeTemplateService.selectOptionList().success(
				function(response){
					$scope.itemList = {data:response};
				}
		);
	}
	
});	
