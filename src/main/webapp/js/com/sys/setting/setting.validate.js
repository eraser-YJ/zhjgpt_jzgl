$(document).ready(function(){
	
//初始化校验方法
	$("#settingForm").validate({
        rules: {
		   isMsgService: 
		   {
			    required: false,
			    maxlength: 1
		   },
		   msgPrefix: 
		   {
			    required: false,
			    maxlength: 20
		   },
		   suggestionType:
		   {
			    required: false,
			    maxlength: 1
		   },
		   showIdentifyingCode: 
		   {
			    required: false,
			    maxlength: 1
		   },
		   useIpBanding: 
		   {
			    required: false,
			    maxlength: 1
		   },
		   maxErrorCount: 
		   {
			    required: true,
			    maxlength: 1,
			    rangeDigits: [0,5]
		   },
		   lockTime: 
		   {
			    required: true,
			    rangeDigits : [4,100000]
		   },
		   worklogDay: 
		   {
			    required: true,
			    rangeDigits: [-1,31]
		   },
		   loginType: 
		   {
			    required: false,
			    maxlength: 1
		   },
		   netKey: 
		   {
			    required: false,
			    maxlength: 1
		   },
		   controlPrint: 
		   {
			    required: false,
			    maxlength: 1
		   },
		   signType: 
		   {
			    required: false,
			    maxlength: 1
		   },
		   filePath: 
		   {
			    required: false,
			    maxlength: 200,
			    isNotChinese : true
		   },
		   photoPatn: 
		   {
			    required: false,
			    maxlength: 200,
			    isNotChinese :true
		   },
		   emailSaveTime: 
		   {
			    required: true,
			    rangeDigits: [0,100]
		   }
	    },	
	    messages: {
	    	maxErrorCount: {rangeDigits: "只能输入[1-5]之间的整数"},
			emailSaveTime: {rangeDigits: "只能输入[1,99]之间的整数"},
			worklogDay: {rangeDigits: "只能输入[0,30]之间的整数"}
		}
	});
	//只能输入[5-99999]之间的数字
	jQuery.validator.addMethod("rangeDigits", function(value, element, param) { 
		var reg1 = true;
		var reg2 = true;
		if(param[0] >= value){
			reg1 = false;
			
		}
		if(param[1] <= value){
			reg2 = false;
		}
	    return (this.optional(element) || /^\d+$/.test(value)) && reg1 && reg2;       
	}, "只能输入[5-99999]之间的整数"); 


});