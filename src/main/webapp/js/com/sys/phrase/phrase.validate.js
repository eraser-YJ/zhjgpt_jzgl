$(document).ready(function(){
	
//初始化校验方法
	$("#phraseForm").validate({
        rules: {
		   content: 
		   {
			    required: true,
			    specialChar: true,
			    maxlength: 255
		   },
		   phraseType: 
		   {
			    required: false,
			    maxlength: 1
		   }
	    }
	});



});