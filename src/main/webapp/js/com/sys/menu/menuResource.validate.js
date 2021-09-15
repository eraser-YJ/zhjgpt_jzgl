$(document).ready(function() {
	//初始化修改校验方法
	$("#menuEditForm").validate({
        rules: {
		   name: 
		   {
			    required: true,
			    specialChar: true,
			    maxlength: 64
		   },
		   icon: 
		   {
			    required: false,
			    maxlength: 64
		   },
		   actionName: 
		   {
			    required: false,
			    maxlength: 255
		   },
		   queue: 
		   {
			    required: true,
			    maxlength: 6,
			    digits: true
		   }
	    }
	});
	
	//初始化添加校验方法
	$("#menuAddForm").validate({
        rules: {
		   name: 
		   {
			    required: true,
			    specialChar: true,
			    maxlength: 64
		   },
		   icon: 
		   {
			    required: false,
			    maxlength: 64
		   },
		   actionName: 
		   {
			    required: false,
			    maxlength: 255
		   },
		   queue: 
		   {
			    required: true,
			    maxlength: 6,
			    digits: true
		   }
	    }
	});
	
	//初始化权限校验方法
	$("#menuPowerForm").validate({
        rules: {
		   name: 
		   {
			    required: true,
			    specialChar: true,
			    maxlength: 64
		   },
		   permission: 
		   {
			    required: false,
			    maxlength: 64
		   },
		   queue: 
		   {
			    required: true,
			    maxlength: 6,
			    digits: true
		   }
	    }
	});
	
	//初始化资源迁移校验方法
	$.validator.addMethod("moveMenuVal", function(value, element) {
		
		if($("#moveMenuForm #moveparentName").val() != '')
			return true;
		else
			return false;
		
	}, "此信息不能为空");
			
});