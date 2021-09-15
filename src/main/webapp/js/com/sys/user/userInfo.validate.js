$(document).ready(function(){

//初始化校验方法
	$("#userInfoForm").validate({
        rules: {
        	birthday: 
 		   {
 			    required: false,
 			    dateISO: true
 		   },
		   mobile: 
		   {
			    required: false,
			    maxlength: 11,
			    mobile: true,
			    remote:{
                    url: getRootPath()+"/sys/user/checkMobile.action",
                    type: "get",
                    dataType: 'json',
                    data: {
                        'id': function(){return $("#id").val();}
                    }
			    }
		   },
		   email: 
		   {
			    required: false,
			    maxlength: 64,
			    email: true,
			    specialChar: false
		   },
		   officeAddress: 
		   {
			    required: false,
			    maxlength: 100,
			    specialChar: true
		   },
		   groupTel: 
		   {
			    required: false,
			    number: true,
			    minlength: 6,
			    maxlength: 6
		   },
		   officeTel: 
		   {
			    required: false,
			    simplePhone: true,
			    maxlength: 20
		   },
		   hometown: 
		   {
			    required: false,
			    maxlength: 64,
			    specialChar: true
		   },
		   cardNo: 
		   {
			    required: false,
			    card: true,
			    maxlength: 18
		   },
		   weight: 
		   {
			    required: true,
			    maxlength: 3,
			    digits: true
		   },
		   orderNo: 
		   {
			    required: true,
			    maxlength: 6,
			    digits: true
		   },
		   payCardNo: 
		   {
			    required: false,
			    creditcard: true,
			    maxlength: 20
		   },
		   cardBank: 
		   {
			    required: false,
			    specialChar: true,
			    maxlength: 100
		   },
		   cardName: 
		   {
			    required: false,
			    maxlength: 20,
			    specialChar: true
		   },
		   quession: 
		   {
			    required: false,
			    maxlength: 100
		   },
		   answer: 
		   {
			    required: false,
			    maxlength: 100,
			    specialChar: true
		   },
	    },	
	    messages: {
	    	mobile:{remote: $.i18n.prop("JC_SYS_090")},
			officeTel: {simplePhone: $.i18n.prop("JC_SYS_106")},
			payCardNo: {creditcard: $.i18n.prop("JC_SYS_107")}
		}
	});

});