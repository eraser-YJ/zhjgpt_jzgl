$(document).ready(function(){
	
//初始化校验方法
	$("#portalForm").validate({
        rules: {
		   portalName: 
		   {
			    required: true,
			    specialChar: true,
			    maxlength: 50
		   },
		   portalStatus: 
		   {
			    required: true
		   },
		   portalType: 
		   {
			    required: true
		   },
		   sequence: 
		   {
			    required: true,
			    digits: true
		   }
	    }
	});



});