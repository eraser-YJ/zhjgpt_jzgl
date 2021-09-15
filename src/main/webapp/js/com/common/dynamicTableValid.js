/**
 * 表格动态行验证
 * @Auth zhanglg
 */

var dynamicTableValid={};

dynamicTableValid.valid=function(formId,tbodyId){
	var validator = $( "#"+formId ).validate();
	var valid=true;
	//循环处理行
	$.each($("#"+tbodyId).children("tr"),function(i,n){
		//循环处理单元格
		$.each($(n).children("td"),function(j,m){
			//找到待验证的域
			var field=$(m).find(":text,textarea");
			if(field!=undefined && field.length>0){
				//验证
				valid=validator.element(field);
				//验证结果非法：跳出
				if(valid==false){
					return false;
				}
			}
			if(valid==false){
				return false;
			}
		});
		
	});
	return valid;
};