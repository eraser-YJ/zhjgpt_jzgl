/**
 * 流程字段的自定义属性相关操作
 */
var fieldCustomProperty = {};

/**
 * 自定义属性数组，key为自定义属性对象的id，value为自定义属性对象。
 * 自定义属性对象中有以下属性：
 * id：唯一标识，同时用于定义自定义属性对应的方法，方法命名规则为 fieldCustomProperty.[id]Function，“[id]”部分由该属性的实际id值替换，如：自定义属性的必填项校验方法命名为 fieldCustomProperty.requiredFunction
 * name：流程设计页面显示的名称
 * value：自定义属性的值，默认可以为空，然后在流程设计页面上维护
 * type：自定义属性支持的控件类型，具体每个值对应的类型含义详见 fieldCustomProperty.getType 方法中的说明
 */
fieldCustomProperty.todoCustomProperty = {};
fieldCustomProperty.todoCustomProperty['required'] = { id : 'required', name : '必填项', value : '', type : '[1,2,3,4,5,6,7,8,9,12]' };//必填项校验属性

/**
 * 处理流程当前节点各个字段的自定义属性
 */
fieldCustomProperty.handleFieldCustomProperty = function (returnResult) {
	returnResult['code'] = "1";//默认返回结果为成功
	returnResult['msg'] = "成功";//默认返回信息
	
	var type = returnResult["type"];//流程操作类型
	var formItemPrivJsonStr = $("#workflowBean\\.formItemPrivJsonStr").val();//流程当前节点的字段信息
    var formItemPrivJson;//流程当前节点的字段信息对象
    
    //将流程当前节点的字段信息转换成对象
    if ($.trim(formItemPrivJsonStr) != '') {
        formItemPrivJson = eval("(" + formItemPrivJsonStr + ")");
    }
    
    var openType = $("#workflowBean\\.openType_").val();//当前流程界面打开方式
    var todoFieldList = "";//待办相关字段信息
    
    //当前流程界面打开方式为“待办”
    if (openType == 'TODO') {
    	todoFieldList = formItemPrivJson.todo;//取出待办相关字段信息
    }
    
    //待办相关字段信息存在，则循环处理
    if (todoFieldList && todoFieldList.length > 0) {
    	for (var i = 0; i < todoFieldList.length; i++) {
        	var field = todoFieldList[i];//字段信息
        	var customPropertyMap = field.customProperty;//字段的自定义属性
        	
        	//字段的自定义属性存在，则循环处理
        	if (customPropertyMap) {
    			for (var key in customPropertyMap) {
    				var customProperty = customPropertyMap[key];//自定义属性对象
    				//自定义属性对应的方法，方法命名规则详见自定义属性数组（fieldCustomProperty.todoCustomProperty）定义的说明
    				var customPropertyFunctionName = "fieldCustomProperty." + customProperty.id + "Function";
            		
    				//如果当前自定义属性为必填项，并且流程操作类型为“提交”、“暂存”、“退回”、“转办”，则进行必填项校验
    				if (key == "required" && (type == "Submit" || type == "Save" || type == "Reject" || type == "Goto")) {
    					//如果自定义属性对应的方法存在，则执行该方法
    					if (eval("typeof " + customPropertyFunctionName) == 'function') {
                			var customPropertyFunction = eval(customPropertyFunctionName);//获取自定义属性对应的方法
                			returnResult = customPropertyFunction(field, customProperty, returnResult);//执行自定义属性对应的方法
                			
                			//如果返回失败，则跳出循环
                			if (returnResult["code"] != "1") {
                				break;
                			}
                		}
    				}
    			}
        	}
        	
        	//如果返回失败，则跳出循环
        	if (returnResult["code"] != "1") {
				break;
			}
        }
    }
    
    return returnResult;
}

/**
 * 自定义属性的必填项校验方法
 */
fieldCustomProperty.requiredFunction = function (field, customProperty, returnResult) {
	//如果当前字段状态为“编辑”或者“隐藏”，并且自定义属性的类型支持当前字段的控件类型，则对该字段进行必填项校验
	if ((field.privilege == "edit" || field.privilege == "hidden") && customProperty.type.indexOf(field.type) != -1) {
	    var formObj = $("form");
	    var fieldType = fieldCustomProperty.getType(field.type);//获取当前字段的控件类型
	    var fieldObj;//字段对象
	    
	    //当期字段的控件类型为意见域
	    if (field.type == "12") {
	    	fieldObj = $(formObj.find("[itemId='" + field.formName + "']"));//通过itemId查找字段对象
	    } else {//当期字段的控件类型为其他类型
	    	fieldObj = $(formObj.find("[itemName='" + field.formName + "']"));//通过itemName查找字段对象
	    }
	    
	    //字段对象和控件类型都存在
	    if (fieldObj && fieldObj.length > 0 && fieldType) {
	    	var fieldValue = fieldCustomProperty.type[fieldType].get(fieldObj, field, customProperty);//根据控件类型获取该字段对应的值
	    	
	    	//字段值不存在或者为空
		    if (typeof(fieldValue) != "undefined" && $.trim(fieldValue) == "") {
		    	returnResult['code'] = "0";//返回校验失败
		    	returnResult['msg'] = field.itemName + "不能为空！";//返回错误信息
		    	
		    	//失败信息弹出框
		    	msgBox.info({
		    		content: returnResult['msg']
		    	});
		    }
	    }
	}
	
	return returnResult;
}

/**
 * 自定义属性支持的字段控件类型
 */
fieldCustomProperty.getType = function (typeId) {
	var map = new Map();
    map.put("1", "textinput");//文本输入框
    map.put("2", "textinput");//文本输入域
    map.put("3", "textinput");//时间输入框
    map.put("4", "radio");//单选
    map.put("5", "checkbox");//多选
    map.put("6", "select");//下拉选择框
    map.put("7", "userSelect");//人员选择框
    map.put("8", "attach");//文件上传
    map.put("9", "editor");//富文本编辑器
    map.put("12", "container");//意见域
    
    return map.get(typeId);
}

//自定义属性支持的字段控件类型对象
fieldCustomProperty.type = {};

//文本输入框、文本输入域、时间输入框
fieldCustomProperty.type["textinput"] = {
	//获取控件的值
    get: function (obj, field, customProperty) {
    	var fieldValue = "";
    	
    	if (obj.is('input') || obj.is('textarea')) {
    		fieldValue = obj.val();
        } else if (obj.find('input').length > 0) {
        	fieldValue = obj.find('input').val();
        }
    	
        return fieldValue;
    }
}

//单选
fieldCustomProperty.type["radio"] = {
	//获取控件的值
    get: function (obj, field, customProperty) {
		var fieldValue = "";
    	
		if (obj.find("input:radio:checked").length > 0) {
			fieldValue = obj.find("input:radio:checked").val();
		}
        
        return fieldValue;
    }
}

//多选
fieldCustomProperty.type["checkbox"] = {
	//获取控件的值
    get: function (obj, field, customProperty) {
    	var fieldValue = "";
    	
        obj.find("input:checkbox:checked").each(function (index, item) {
        	fieldValue += $(item).val() + ",";
        });
        
        if (fieldValue.length > 0) {
        	fieldValue = fieldValue.substring(0, fieldValue.length - 1);
        }
        
        return fieldValue;
    }
}

//下拉选择框
fieldCustomProperty.type["select"] = {
	//获取控件的值
    get: function (obj, field, customProperty) {
    	var fieldValue = "";
    	
    	obj.find("select:not(.noneWorkflow) option:checked").each(function (index, item) {
            if ($(item).val() != null && $(item).val().length > 0) {
            	fieldValue = $(item).val();
            }
        });
    	
    	return fieldValue;
    }
}

//人员选择框
fieldCustomProperty.type["userSelect"] = {
	//获取控件的值
    get: function (obj, field, customProperty) {
    	var fieldValue = "";
    	
		var itemId = field.formName.split("!");
    	
    	if (itemId.length > 1) {
    		var controlId = itemId[1];
    		
    		if ($("#"+controlId).length > 0) {
    			fieldValue = $("#"+controlId).val();
    		}
    	}
    	
    	return fieldValue;
    }
}

//文件上传
fieldCustomProperty.type["attach"] = {
	//获取控件的值
    get: function (obj, field, customProperty) {
    	var fieldValue = "";
    	
    	var itemName = "";//文件上传的值对应的标签名称
    	
    	//如果自定义属性值存在，则取该值作为标签名称
    	if (customProperty.value != "") {
    		itemName = customProperty.value;
    	} else {//默认取控件名称，作为标签名称
    		itemName = field.formName;
    	}
    	
    	obj.parent().find("input[name='"+itemName+"']").each(function (index, item) {
            if ($(item).val() != null && $(item).val().length > 0) {
            	fieldValue = $(item).val();
            }
        });
    	
    	return fieldValue;
    }
}

//富文本编辑器
fieldCustomProperty.type["editor"] = {
	//获取控件的值
    get: function (obj, field, customProperty) {
		var fieldValue = "";
    	
		if (obj.find('textarea').length > 0) {
			fieldValue = obj.find('textarea').val();
		}
        
        return fieldValue;
    }
}

//意见域
fieldCustomProperty.type["container"] = {
	//获取控件的值
    get: function (obj, field, customProperty) {
		var fieldValue = "";

		var textObj = obj.find('textarea[id*="'+field.formName+'Textarea"]');//普通文本域
		
		if(textObj && textObj.length > 0) {
			fieldValue = textObj.val();
		}
		
		var webRion = handWritten.signObject;//签章
		
		if(webRion) {
			var isEmpty = webRion.DocEmpty;
			
			if(!isEmpty) {
				fieldValue = webRion.Value;
			}
		}
		
        return fieldValue;
    }
}
