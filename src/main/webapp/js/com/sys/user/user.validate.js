$(document).ready(function(){
	
	//其他部门验证
	$.validator.addMethod("otherDeptNameCla", function(value, element) {
		if($("input[name='isOtherDept']:checked").val() == 1)
			return !this.optional(element);
		else
			return true;
	}, $.i18n.prop("JC_SYS_010"));

	$.validator.addMethod("otherDeptDutyCla", function(value, element) {
		if($("input[name='isOtherDept']:checked").val() == 1)
			return !this.optional(element);
		else
			return true;
	}, $.i18n.prop("JC_SYS_010"));

	$.validator.addMethod("otherDeptNoCla", function(value, element) {
		if($("input[name='isOtherDept']:checked").val() == 1)
			return !this.optional(element) && test(value);
		else
			return true;
	}, $.i18n.prop("JC_SYS_104"));
	
	function test(value){
		if(value == 0)
			return false;
		return /^\d{1,6}$/.test(value);
	}
	
//初始化校验方法
	$("#userForm").validate({
		ignore: ".ignore",
        rules: {
		   displayName: 
		   {
			    required: true,
			    maxlength: 20,
			    minlength: 2,
			    specialChar: true
		   },
		   loginName: 
		   {
			    required: true,
			    username: true,
			    specialChar: true,
			    remote:{
                    url: getRootPath()+"/sys/user/checkLoginName.action",
                    type: "get",
                    dataType: 'json',
                    data: {
                        'loginNameOld': function(){return $("#loginNameOld").val();}
                    }
			    }

		   },
		   adminSideV: 
		   {
			   required: function(element) {
			        return $("input[name='isAdmin']:checked").val() == 1;
			      }
		   },
		   entryDate: 
		   {
			    required: false,
			    dateISO: true
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
			    isPositive: true
		   },
		   mobile: 
		   {
			    required: false,
			    maxlength: 11
		   },
		   officeTel: 
		   {
			    required: false,
			    simplePhone: true,
			    maxlength: 20
		   },
		   groupTel: 
		   {
			    required: false,
			    number: true,
			    minlength: 6
		   },
		   email: 
		   {
			    required: false,
			    maxlength: 64,
			    email: true,
			    specialChar: false
		   },
		   entryPoliticalDate: 
		   {
			    required: false,
			    dateISO: true
		   },
		   birthday: 
		   {
			    required: false,
			    dateISO: true
		   },
		   hometown: 
		   {
			    required: false,
			    maxlength: 64,
			    specialChar: true
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
			    maxlength: 100,
			    specialChar: true
		   },
		   cardName: 
		   {
			    required: false,
			    maxlength: 20,
			    specialChar: true
		   },
		   
		   officeAddress: 
		   {
			    required: false,
			    maxlength: 100,
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
		   otherDeptNo: 
		   {
			    required: false,
			    maxlength: 6,
			    number: true
		   },
		   code: 
		   {
			    required: false,
			    maxlength: 64,
			    specialChar: true
		   }
	    },	
	    messages: {
			loginName: {remote: $.i18n.prop("JC_SYS_089")},
			adminSideV: {required: $.i18n.prop("JC_SYS_105")},
			officeTel: {simplePhone: $.i18n.prop("JC_SYS_106")},
			groupTel: {number: $.i18n.prop("JC_SYS_127")},
			payCardNo: {creditcard: $.i18n.prop("JC_SYS_107")},
			mobile:{remote: $.i18n.prop("JC_SYS_090")}
		}
	});

});