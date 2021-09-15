$(document).ready(function(){
	
	//初始化校验方法 
	$.validator.addMethod("valueValider", function(value, element) {
			return !this.optional(element);
			//return true;
	}, "必填信息");

	$.validator.addMethod("codeValider", function(value, element) {
			return !this.optional(element);
			//return true;
	}, "必填信息");

	
	$("#dicsname").validate({
		ignore: ".ignore",
        rules: {code: 
		   {
		    required: false
		   }
        }
	});

	//初始化校验方法
	$("#dicForm").validate({
		ignore: ".ignore",
		rules: {
			value:
			{
				required: true,
				maxlength: 20,
				specialChar: true
			},
			code:
			{
				required: true,
				specialChar: true,
				remote:{
					url: getRootPath()+"/dic/checkDicCode.action",
					type: "get",
					dataType: 'json',
					data: {
						'codeOld': function(){return $("#codeOld").val();},
						'parentType': function(){return $("#parentType").val();},
						'id':function(){return $("#id").val();}
					}
				}

			},
			orderFlag:
			{
				required: true,
				isPositive: true
			}
		},
		messages: {
			code: {remote: $.i18n.prop("字典编码已经存在")}
		}
	});
});