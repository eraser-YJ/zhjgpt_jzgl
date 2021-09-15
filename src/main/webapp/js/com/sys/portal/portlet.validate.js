$(document).ready(function(){
	
	$.validator.addMethod("selectValider", function(value, element) {
		
		if($("#portletForm #optfunctions").find("option").text() != '')
			return true;
		else
			return false;
		
	}, "此信息不能为空");
	
	$.validator.addMethod("selectMaxValider", function(value, element) {
		var i = $("#portletForm #optfunctions").find("option").length;
		if(i < 5)
			return true;
		else
			return false;
	}, "最多不能超过4个");
	
//初始化校验方法
	$("#portletForm").validate({
        rules: {
           portalId: 
		   {
			    required: true
		   },
		   letTitle: 
		   {
			    required: true,
			    specialChar: true,
			    maxlength: 6
		   },
		   viewType: 
		   {
			    required: true
		   }
	    }
	});



});