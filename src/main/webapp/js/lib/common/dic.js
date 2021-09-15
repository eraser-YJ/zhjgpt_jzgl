/**
 * 字典相关js
 */
var dic = {};

/**
 * @description 获得启动的字典，并放到固定的select中
 * @param elementId 字典对应select组件的id
 * @param typeCode 字典类型code
 * @param async为true时为同步请求
 * @param defaultType 1:请选择  2：全部 3：无
 */
dic.fillDics = function(elementId,typeCode,parentCode,async,defaultType){
	alert("bbb");
	if(typeCode==null||typeCode.length==0){
		//alert("字典类型为空");
		return;
	}
	var selectObj = $('#'+elementId);
	if(selectObj==null||selectObj.length==0){
		//alert("没有对应的的组件,id为"+elementId);
		return;
	}
	var ajaxData = {
		typeCode:typeCode,
		parentCode:parentCode,
		time:new Date()
	};
	//改为同步
//	$.ajaxSetup({ 
//	    async : false 
//	});
	$.ajax({
		url: getRootPath()+"/dic/getDics.action",
		type: "GET",
		async: false,
		success:function(data){
			dic.fillElement(selectObj,data,defaultType)},
		data:ajaxData
	});
/*	$.get(getRootPath()+"/dic/getDics.action",ajaxData,function(data){
		dic.fillElement(selectObj,data,defaultType);
	});*/
};

/**
 * @description 获得所有字典(包含未启动的)，并放到固定的select中
 * @param elementId 字典对应select组件的id
 * @param typeCode 字典类型code
 * @param defaultType 1:请选择  2：全部 3：无
 */
dic.fillAllDics = function(elementId,typeCode,parentCode,defaultType){
	if(typeCode==null||typeCode.length==0){
		//alert("字典类型为空");
		return;
	}
	var selectObj = $('#'+elementId);
	if(selectObj==null||selectObj.length==0){
		//alert("没有对应的的组件,id为"+elementId);
		return;
	}
	var ajaxData = {
		typeCode:typeCode,
		parentCode:parentCode,
		time:new Date()
	};
	$.get(getRootPath()+"/dic/getDicsAll.action",ajaxData,function(data){
		dic.fillElement(selectObj,data,defaultType);
	});
};

/**
 * @description 获得启动的字典，并放到固定的select中
 * @param elementId 字典对应select组件的id
 * @param data 字典项数据
 */
dic.fillElement = function(selectObj,data,defaultType){
	selectObj.html("");
	if(defaultType==null){
		defaultType == "1";
	}
	if(defaultType=="1"||defaultType==1){
		defaultType = "1";
		selectObj.html("<option value=''>-请选择-</option>");
	}else if(defaultType=="2"||defaultType==2){
		defaultType = "2";
		selectObj.html("<option value=''>-全部-</option>");
	}else if(defaultType=="3"||defaultType==3){
		defaultType = "3";
		selectObj.html("");
	}
	
	var selectValue = selectObj.attr("code");
	for(var i=0;i<data.length;i++){
		var selectStr = "";
		if(selectValue==null||selectValue.length==0){
			if(defaultType!="2"&&data[i].defaultValue == 1){
				selectStr = "selected";
			}
		}
		if(selectValue!=null&&selectValue.length>0){
			selectStr = "selected";
		}
		
		var optionStr = "<option value='"+data[i].code+"' "+selectStr+">"+data[i].value+"</option>";
		selectObj.append(optionStr);
	}
};

/**
 * @description 获得启动的字典，并返回toString
 * @param typeCode 字典类型code
 */
dic.toString = function(typeCode){
	if(typeCode){
		var ajaxData = {typeCode:typeCode,time:new Date()};
		var html = "<option value=''>请选择</option>";
		$.ajax({
			type : "GET",
			url : getRootPath()+"/dic/getDics.action",
			async:false,
			data : ajaxData,
			dataType : "json",
			success : function(data) {
				if (data) {
					var selectStr = "";
					for(var i=0;i<data.length;i++){
						if(data[i].defaultValue == 1){
							selectStr = "selected";
						}
						var optionStr = "<option value='"+data[i].code+"' "+selectStr+">"+data[i].value+"</option>";
						html = html.concat(optionStr);
					}
				}
			}
		});
		return html;
	}
};

/**
 * 多级联动下拉菜单方法
 * @param[*]代表必填项
 * @param[*] typeCode 字典码
 * @param[*] selectId 被替换select组件的id值
 * @param selectValue 被选中的下拉值
 */
dic.multistepDropDown = function(typeCode,selectId,selectValue){alert(11);
	$("#"+selectId).empty();
	$.ajax({
        type: 'post',
        async: false,
        dataType : 'json',
        url: getRootPath()+"/dic/getDicJSONInfo.action?typeCode="+typeCode+"&time="+(+new Date()),
        success: function (json) {
        	$(json).each(function(){
        		var opt = $("<option/>").text(this.value).attr("value", this.code); 
        		if(this.code == selectValue)
        			opt = $("<option/>").text(this.value).attr("value", this.code).attr('selected',true); 
        		$("#"+selectId).append(opt);
        	});
        }
    });
};

/**
 * 多级联动下拉菜单方法
 * @param[*]代表必填项
 * @param[*] typeCode 字典码
 * @param[*] selectId 被替换select组件的id值
 * @param[*] multistId 被替换的下拉菜单区域id值
 * @param selectValue 被选中的下拉值
 */
dic.multistepDropDownCache = function(typeCode,selectId,multistId,selectValue){
	$("#"+multistId).empty();
	if(selectValue != null && selectValue != '')
		$("#"+multistId).html("<dic:select name=\""+selectId+"\"id=\""+selectId+"\" defaultValue=\""+selectValue+"\" dictName=\""+typeCode+"\"/>");
	else
		$("#"+multistId).html("<dic:select name=\""+selectId+"\"id=\""+selectId+"\" dictName=\""+typeCode+"\"/>");
};