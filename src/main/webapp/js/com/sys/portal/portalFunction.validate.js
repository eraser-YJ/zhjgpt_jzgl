$(document).ready(function(){
	
//初始化校验方法
	$("#pFunctionForm").validate({
        rules: {
		   funName: 
		   {
			    required: true,
			    specialChar: true,
			    maxlength: 20
		   },
		   funCode: 
		   {
			    required: true,
			    specialChar: false,
			    maxlength: 20
		   },
		   funUrl: 
		   {
			    required: true,
			    maxlength: 150
		   },
		   funParametertype: 
		   {
			    required: true
		   },
		   funUrlparameter: 
		   {
			    required: false,
			    maxlength: 150
		   },
		   funUrlmore: 
		   {
			    required: true,
			    maxlength: 150
		   },
		   funRows: 
		   {
			    required: true,
			    digits: true,
			    maxlength : 2
		   },
		   viewType: 
		   {
			    required: true
		   }
	    }
	});



});