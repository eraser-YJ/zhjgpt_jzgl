/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: CN
 */

$.i18n.properties({
	name:["sysMessage","commonMessage"],
    //language:"zh_CN",
    path:getRootPath()+"/system/i18n/",
    mode:"map"
});
String.prototype.trim=function(){return this.replace(/(^\s*)|(\s*$)/g,"")}
//alert($.i18n.prop("JC_VA_001"));
jQuery.extend(jQuery.validator.messages, {
        required: jQuery.validator.format($.i18n.prop("JC_SYS_010")),
		remote: "请修正该信息",
		email: jQuery.validator.format($.i18n.prop("JC_SYS_017")),
		url: jQuery.validator.format($.i18n.prop("JC_SYS_024")),
		date: jQuery.validator.format($.i18n.prop("JC_SYS_014")),
		dateISO: jQuery.validator.format($.i18n.prop("JC_SYS_014")),
		number: jQuery.validator.format($.i18n.prop("JC_SYS_013")),
		digits: jQuery.validator.format($.i18n.prop("JC_SYS_013")),
		creditcard: "请输入合法的卡号",
		equalTo: "请再次输入相同的值",
		accept: "请输入拥有合法后缀名的字符串",
		maxlength: jQuery.validator.format($.i18n.prop("JC_SYS_011")),
		minlength: jQuery.validator.format($.i18n.prop("JC_SYS_012")),
		rangelength: jQuery.validator.format($.i18n.prop("JC_SYS_027")),
		range: jQuery.validator.format($.i18n.prop("JC_SYS_053")),
		max: jQuery.validator.format($.i18n.prop("JC_SYS_052")),
		min: jQuery.validator.format($.i18n.prop("JC_SYS_051"))
});

jQuery.validator.addMethod("ip", function(value, element) {
	return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));   
}, "请输入合法的IP地址");

jQuery.validator.addMethod("abc",function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9_]*$/.test(value);
},"请输入字母数字或下划线");

jQuery.validator.addMethod("pwd",function(value, element) {
	return this.optional(element) || /^[\@A-Za-z0-9\!\@\#\$\%\^\&\*\.\(\)\~\_\,\;]{4,20}$/.test(value);
},"请输入合法的密码");

jQuery.validator.addMethod("username",function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9][a-zA-Z0-9_]{2,19}$/.test(value);
},"3-20位字母或数字开头，允许字母数字下划线");

jQuery.validator.addMethod("noEqualTo",function(value, element, param) {
	return value != $(param).val();
},"请再次输入不同的值");

//真实姓名验证
jQuery.validator.addMethod("realName", function(value, element) {
    return this.optional(element) || /^[\u4e00-\u9fa5]{2,30}$/.test(value);
}, "姓名只能为2-30个汉字");

jQuery.validator.addMethod("specialChar", function(value, element) {
    return this.optional(element) || !$.hasSpecialChar(value,"~`!#$%^&*()=+{}[]|\\\":;'?/><");
}, "输入中含有特殊字符");


// 字符验证
jQuery.validator.addMethod("userName", function(value, element) {
    return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);
}, "登录名只能包括中文字、英文字母、数字和下划线");

// 手机号码验证
jQuery.validator.addMethod("mobile", function(value, element) {
    var length = value.length;
    return this.optional(element) || (length == 11 && /^([+-]?)\d*\.?\d+$/.test(value));
    //return this.optional(element) || (length == 11 && /^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/.test(value));
}, "请输入有效的手机号码");

// 电话号码验证
jQuery.validator.addMethod("simplePhone", function(value, element) {
    var tel = /^(\d{3,4}-?)?\d{7,9}$/g;
    return this.optional(element) || (tel.test(value));
}, "请正确填写您的电话号码");

// 电话号码验证     
jQuery.validator.addMethod("phone", function(value, element) {     
	var tel = /(^0[1-9]{1}\d{9,10}$)|(^1[3,5,8]\d{9}$)/g;     
	return this.optional(element) || (tel.test(value));     
}, "格式为:固话为区号(3-4位)号码(7-9位),手机为:13,15,18号段");	

// 邮政编码验证
jQuery.validator.addMethod("zipCode", function(value, element) {
    var tel = /^[0-9]{6}$/;
    return this.optional(element) || (tel.test(value));
}, "请正确填写您的邮政编码");

//文件名
jQuery.validator.addMethod("fileName", function(value, element) {
    var reg = new RegExp('^[^\\\\\\/:*\'"?\\"<>|]+$');// 转义 \ 符号也不行
	//alert(reg.test("新建文件\\夹")); // 弹出 true
    return this.optional(element) || ( reg.test(value.trim()));
}, "名称中含有特殊字符");
//QQ号码验证
jQuery.validator.addMethod("qq", function(value, element) {
	var tel = /^[1-9][0-9]{4,}$/;
	return this.optional(element) || (tel.test(value));
}, "请正确填写您的QQ号码");
 
//校验身份证好
jQuery.validator.addMethod("card",function(value, element) {
	return this.optional(element) || checkIdcard(value);
},"请输入15位或18位的身份证号");

//路径不能含有中文  ----2014.4.28 系统参数应用
jQuery.validator.addMethod("isNotChinese", function(value, element) {    
	 var tel = /^[\u4e00-\u9fa5]+$/; 
     return !tel.test(value);       
}, "路径不能含有中文"); 

//只能输入[0-9]数字
jQuery.validator.addMethod("isDigits", function(value, element) {       
     return this.optional(element) || /^\d+$/.test(value);       
}, "只能输入0-9数字");

//正整数
jQuery.validator.addMethod("isPositive", function(value, element) {       
     return this.optional(element) || /^[1-9]\d*$/.test(value);       
}, "请输入大于0的整数");

//验证身份证函数
function checkIdcard(idcard){
	idcard = idcard.toString();
	//var Errors=new Array("验证通过!","身份证号码位数不对!","身份证号码出生日期超出范围或含有非法字符!","身份证号码校验错误!","身份证地区非法!");
	var Errors=new Array(true,false,false,false,false);
	var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}
	var idcard,Y,JYM;
	var S,M;
	var idcard_array = new Array();
	idcard_array = idcard.split("");
	//地区检验
	if(area[parseInt(idcard.substr(0,2))]==null) return Errors[4];
	//身份号码位数及格式检验
	switch(idcard.length){
		case 15:
			if ( (parseInt(idcard.substr(6,2))+1900) % 4 == 0 || ((parseInt(idcard.substr(6,2))+1900) % 100 == 0 && (parseInt(idcard.substr(6,2))+1900) % 4 == 0 )){
				ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//测试出生日期的合法性
			} else {
				ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//测试出生日期的合法性
			}
			if(ereg.test(idcard)) return Errors[0];
			else return Errors[2];
			break;
		case 18:
			//18 位身份号码检测
			//出生日期的合法性检查
			//闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
			//平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
			if ( parseInt(idcard.substr(6,4)) % 4 == 0 || (parseInt(idcard.substr(6,4)) % 100 == 0 && parseInt(idcard.substr(6,4))%4 == 0 )){
				ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
			} else {
				ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
			}
			if(ereg.test(idcard)) {//测试出生日期的合法性
				//计算校验位
				S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
					+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
					+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
					+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
					+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
					+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
					+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
					+ parseInt(idcard_array[7]) * 1
					+ parseInt(idcard_array[8]) * 6
					+ parseInt(idcard_array[9]) * 3 ;
				Y = S % 11;
				M = "F";
				JYM = "10X98765432";
				M = JYM.substr(Y,1);//判断校验位
				if(M == idcard_array[17]) return Errors[0]; //检测ID的校验位
				else return Errors[3];
			}
			else return Errors[2];
			break;
		default:
			return Errors[1];
			break;
	}
} 


//参数:param[0] 整数的范围，param[1] 小数的范围，param[2] 是否包含负数
jQuery.validator.addMethod("customNumber", function(value, element,param) {
	var reg = "";
	if(param[2] == false){
		if(param[0] == 0){
			reg = "//^[0]+(.[0-9]{1,"+param[1]+"})?$//"; //0.XX
		}else if(param[1] == 0){
			reg = "//^[0-9]{1,"+param[0]+"}$//";			//XX
		}else{
			reg = "//^[0-9]{1,"+param[0]+"}(\\.[0-9]{1,"+param[1]+"})?$//";		//xx.xx
		}
	}else{
		if(param[0] == 0){
			reg = "//^-?[0]+(.[0-9]{1,"+param[1]+"})?$//";		//(-)0.xx
		}else if(param[1] == 0){
			reg = "//^-?[0-9]{1,"+param[0]+"}$//";					//(-)xxx
		}else{
			reg = "//^-?[0-9]{1,"+param[0]+"}(\\.[0-9]{1,"+param[1]+"})?$//";	//(-)xx.xx
		}
	}
	reg = reg.replace(/\/\//g,"\/");
	reg = eval(reg);
     return this.optional(element) || reg.test(value);       
},$.i18n.prop("JC_SYS_013"));  

/**
 * 验证select2控件
 * 使用要验证的地方加上	validSelect2 : "传入DivId"
 */
jQuery.validator.addMethod("validSelect2", function(value, element, params){
	if(value.length == 0){
		$("#"+params+" .select2-container").addClass("error"); 
		$("#"+params+" .help-block").removeClass("hide");
		$("#"+params+" .help-block").html("<font color=red>"+$.i18n.prop("JC_SYS_010")+"</font>");
		return false;
	}else{
		$("#"+params+" .select2-container").removeClass("error"); 
		$("#"+params+" .help-block").addClass("hide");
		$("#"+params+" .help-block").html("");
		return true;
	}
}, "");