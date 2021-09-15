$(document).ready(function(){
	
//初始化校验方法
	$("#portalFriendlylinkForm").validate({
        rules: {
		   linkName: 
		   {
			    required: true,
			    maxlength: 20,
			    specialChar: true
		   },
		   linkUrl: 
		   {
			    required: true,
			    maxlength: 100,
			    url:true
		   },
		   portalId: 
		   {
			    required: true,
			    
		   },
		   portletId: 
		   {
			    required: true,
			    
		   },
		   sequence: 
		   {
			    required: true,
			    digits:true
		   },
	    }
	});



});