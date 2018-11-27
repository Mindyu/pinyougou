app.service('uploadService',function($http){
	
	//上传文件
	this.uploadFile=function(){
		var formdata=new FormData();
		formdata.append('file',file.files[0]);	//file 文件上传框的name
		
		// headers 指定上传文件类型 multipart/form-data
		// transformRequest 二进制序列化
		return $http({
			url:'../upload.do',		
			method:'post',
			data:formdata,
			headers:{ 'Content-Type':undefined },
			transformRequest: angular.identity			
		});
	}
});