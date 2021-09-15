$(document).ready(function(){
	
//初始化校验方法
	$("#dicInfoname").validate({
        rules: {
		   code: 
		   {
			    required: true,
			    maxlength: 32
		   },
		   value: 
		   {
			    required: true,
			    maxlength: 32
		   }
	    }
	});


});