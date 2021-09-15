$(document).ready(function(){
	
	
//初始化校验方法
	$("#userIpForm").validate({
        rules: {
		   userStartIp: 
		   {
			    required: true,
			    maxlength: 64,
			    ip: true
		   },
		   userEndIp: 
		   {
			    required: true,
			    maxlength: 64,
			    ip: true
		   },
		   userIpDesc: 
		   {
			    required: false,
			    specialChar: true,
			    maxlength: 255
		   }
	    }
	});



});