$(document).ready(function(){
	
//初始化校验方法
	$("#userPhoneForm").validate({
        rules: {
		   userId: 
		   {
			    required: true,
			    
		   },
		   status: 
		   {
			    required: false,
			    maxlength: 1
		   },
		   imeiNo: 
		   {
			    required: false,
			    maxlength: 100
		   },
	    }
	});



});