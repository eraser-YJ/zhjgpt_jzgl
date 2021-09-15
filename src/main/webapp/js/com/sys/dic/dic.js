var dics = {};
var parentid;
//重复提交标识
dics.subState = false;
dics.dicTreeType = function(){//加载数据字典左侧树方法
	jQuery.ajax({
		url : getRootPath()+'/dic/getDicTreeTypes.action',
		type : 'POST',
		success : function(data) {
			$("#treeDemo").html(data);//要刷新的div
		}
	});
};

dics.loaddicinfo = function(code,parentId){//加载左侧菜单下包含子菜单方法
	jQuery.ajax({
		url : getRootPath()+"/dic/getDicTreeInfo.action?typeCode="+code+"&parentCode="+parentId,
		type : 'POST',
		success : function(data) {
			$("table tbody").html("");
			$("#dicinfolist").html(data);//要刷新的div
			dics.parentid = code;
			dics.parentType = parentId;
			dics.fnDrawCallback();
		}
	});
};


dics.fnDrawCallback=function() {
    if($("#treeDemo")[0]){
    	var content = $("#content").height();
        var headerHeight_1 = $('#header_1').height() || 0;
        var headerHeight_2 = $("#header_2").height() || 0;
        $(".tree-right").css("padding-left","215px");
		$("#LeftHeight").height(content-80-headerHeight_1-headerHeight_2);
        var lh = $("#LeftHeight").height(); 
        if($("#scrollable").scrollTop()>=113){
            $("#LeftHeight").addClass("fixedNav");
            $("#LeftHeight").height(lh + 113);
        }else{
            var a = $("#scrollable").scrollTop()>=113?113:$("#scrollable").scrollTop();
            $("#LeftHeight").height(lh + a);
            $("#LeftHeight").removeClass("fixedNav");
        }
    }
    
};

dics.autoTrInit = function(){
	//是否生成序号，表id，首次进行页面自动创建行数,自动增加行数,自定义列内容
	indexFun("dictable",1,"<td><span class='input-style'><input type='text' maxlength='20' name='value' class='valueValider' onblur=\"dics.clearTrim(this)\" id='value-{0}'> </span>@" +
			"<td><span class='input-style'><input type='text' maxlength='20' name='code' class='codeValider' onblur=\"dics.clearTrim(this)\" id='code-{0}'> </span>@" +
			"<td><span class='input-style'><input type='text' maxlength='20' name='dicType' disabled onblur=\"dics.clearTrim(this)\" id='dicType-{0}'> </span>@" +
			/*"<td><span class='input-style'><input type='text' maxlength='20' name='parentType' onblur=\"dics.clearTrim(this)\" id='parentType-{0}'> </span>@" +*/
			"<td><input type='checkbox' name='useFlag' id='useFlag-{0}' value='1'> @" +
			"<td><input type='checkbox' name='defaultValue' id='defaultValue-{0}' onclick='dics.defaultCheck(this)' value='1' > @" +
			"<td><input type='hidden' id='id-{0}' name='id'><input type='hidden' id='typeFlag-{0}' value='0' name='typeFlag'>" +
			"<a class=\"a-icon i-remove deleteClass\" href=\"#\" ><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a><input type='hidden' id='dicFlag-{0}' name='dicFlag' value='1' >" );
};

/**
 * 自定义动态加行-----start--
 */
var columnVal=0;//默认行号
var codeVal=0;
var tableId;//table id

//tableId:表id，autoAddValTemp:自动增加行数,autoContentTemp:自定义列内容
function indexFun(tableIdTemp,autoAddValTemp,autoContentTemp){
	if(tableIdTemp!=null && tableIdTemp!=''){
		tableId=tableIdTemp;
	}
	if(autoAddValTemp!=null && autoAddValTemp!=''){
		autoAddVal=autoAddValTemp;
	}
	if(autoContentTemp!=null && autoContentTemp!=''){
		autoContent=autoContentTemp;
	}
	autoCreateColumnCode();
	
}

function autoCreateColumnCode(){
	//参数1：绑定需要动态添加行的样式;参数2：创建事件;参数3：方法名称
	$('tbody').delegate('.input-style','blur',function cm(){
		var t=$(this).parent().parent().attr("id");
		codeVal = parseInt(t);
		columnVal = parseInt(t);
		//创建行(序号)
		creColumnCode(t);
	});
	$('tbody').delegate('.deleteClass','click',function(){
		var t=$(this).parent().parent().attr("id");
		deleteRowCode(t);
	});
}

function creColumnCode(temp){//创建行(序号)
	var id = $("#"+tableId).find("tr:last").attr("id");
	if(temp==id){
		for(var n=0;n<autoAddVal;n++){
			columnVal +=1;
			codeVal +=1;
			var rowHtml="";
			rowHtml +="<tr id='"+columnVal+"'>";
			rowHtml +="<td>"+codeVal+"</td>";
			rowHtml+=autoCreateRows(autoContent);
			rowHtml +="</tr>";
			$("#"+tableId).append(rowHtml);
		}
		$("[data-toggle=tooltip]").tooltip();
		$('tbody').delegate('.input-style','blur',function cm(){
			var t=$(this).parent().parent().attr("id");
			codeVal = parseInt(t);
			columnVal = parseInt(t);
	 		creColumnCode(t);
		});
		$('tbody').delegate('.deleteClass','click',function(){
			var t=$(this).parent().parent().attr("id");
			deleteRowCode(t);
		});
	}
}

function autoCreateRows(rowNames){//自动创建列
   	var roHtml="";
	   if(rowNames!=null && rowNames !=""){
	   		var arr=rowNames.split("@");
	   		if(arr !=null && arr.length >0){
	   			for(var m=0;m<arr.length;m++){
	   				roHtml+=arr[m]+" </span></td>";
	   			}
	   		}
	   }
	return roHtml;
}

function deleteRowCode(num,e){//删除行(序号)
	/*var evt = e||event;
	var eventTarget = evt.srcElement?evt.srcElement:event.target;
	$(eventTarget).tooltip("hide");//隐藏删除标签提示
	
	var targetTable = document.getElementById('dictable');
	if(eventTarget.parentNode != null){
		var rowCount = eventTarget.parentNode.parentNode.parentNode.rowIndex;
	    if(rowCount > 1){
	    	targetTable.deleteRow(eventTarget.parentNode.parentNode.parentNode.cells[0].innerHTML);
	    }
	}*/
	$("#"+num).remove();
	columnVal -=1;
	var le=$("#"+tableId+" tr").length;
	codeVal=le-1;
	for (var i = 0; i<le; i++) {
		if (i != 0){
			document.getElementById(tableId).rows[i].cells[0].innerHTML =i;
		}	
	}
}

dics.operAddrow = function(){
	var id = $("#"+tableId).find("tr:last").attr("id");
	if(typeof(id)!="undefined"){
		codeVal = parseInt(id);
		columnVal = parseInt(id);
	} else {
		codeVal = 0;
		columnVal = 0;
	}
		
	creColumnCode(id);
};

//------------------------------end-------------------
//保存方法
dics.dicSubmit = function(){
	if(dics.parentid == null){
		msgBox.info({content: "请选择要添加字典的类型", type:'fail'});
		return;
	}
	if (dics.subState)return;
		dics.subState = true;
	var spike=0;
	var strValue = "";
	var useFlag = 0;
	var defaultValue = 0;
	var typeFlag = 0;
	var dicFlag = 0;
	var id = null;

	//循环封装json数组
	$("input[name='value']").each(function () {
		if($(this).val() != ""){
			if($("input[name='useFlag']") != null && $("input[name='useFlag']").length > 0 && $("input[name='useFlag']")[spike].checked)
				useFlag = 1;
			else 
				useFlag = 0;
			if($("input[name='defaultValue']")[spike].checked)
				defaultValue = 1;
			if($("input[name='typeFlag']")[spike].value != ""){
				typeFlag = $("input[name='typeFlag']")[spike].value;
			}
			if($("input[name='id']")[spike].value != ""){
				id = $("input[name='id']")[spike].value;
			}
			if($("input[name='dicFlag']")[spike].value != ""){
				dicFlag = $("input[name='dicFlag']")[spike].value;
			}
			if(strValue == "")
				strValue = '{\"id\":'+id+',\"value\":\"'+$(this).val()+'\",\"code\":\"'+ $("input[name='code']")[spike].value+'\",\"parentId\":\"'+dics.parentid+
						'\",\"dicType\":\"'+$("input[name='dicType']")[spike].value+'\",\"parentType\":\"'+dics.parentid+"="+dics.parentType+
						'\",\"useFlag\":'+useFlag+',\"defaultValue\":'+defaultValue+',\"typeFlag\":'+typeFlag+',\"dicFlag\":'+dicFlag+',\"orderFlag\":1}';
			else
				strValue = '{\"id\":'+id+',\"value\":\"'+$(this).val()+'\",\"code\":\"'+ $("input[name='code']")[spike].value+'\",\"parentId\":\"'+dics.parentid+
						'\",\"dicType\":\"'+$("input[name='dicType']")[spike].value+'\",\"parentType\":\"'+dics.parentid+"="+dics.parentType+
						'\",\"useFlag\":'+useFlag+',\"defaultValue\":'+defaultValue+',\"typeFlag\":'+typeFlag+',\"dicFlag\":'+dicFlag+',\"orderFlag\":1},'+strValue;
	        spike +=1;
	        useFlag = 0;
	        defaultValue = 0;
	        id = null;
		}
    });
	//判断json数组中是否存在数据
	if(strValue == ""){
		msgBox.info({content: "您所选择的字典类型中不存在字典值，请进行添加", type:'fail'});
		return;
	}
	
	if(!dics.clearTrim(null,'value',1) || !dics.clearTrim(null,'code',1)){
		dics.subState = false;
		return;
	}
	if($("#dicsname").valid()){
    $.ajax({
        type: 'post',
        dataType : 'json',
        url: getRootPath()+"/dic/addNewDicList.action?parentid="+dics.parentid+"&token="+$("#token").val()+"&time="+(+new Date().getTime()),
        data : {'mydata':'['+strValue+']'},
        success: function (data) {
        	dics.subState = false;
        	$("#token").val(data.token);
			if(data.errorMessage!=null){
				alertx(data.errorMessage);
			}else{
				//alertx("保存成功");
				msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
				dics.loaddicinfo(dics.parentid,dics.parentType);
			}
        },
		error : function() {
			dics.subState = false;
			msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
			//alertx("保存失败");
		}
    });
	}else {
		dics.subState = false;
	}
};

dics.defaultCheck = function(obj){
	var check=obj.checked;
	$(':checkbox[name=defaultValue]').each(function(){ 
		this.checked=false;
	}); 
	obj.checked=check;
};

dics.clearTrim = function(obj,fieldname,valtype){//valtype 1验证空
	if(obj != null)
		obj.value =$.trim(obj.value);
	var str = '';
	var result=true;
	if(fieldname != null && fieldname != '')
	$("input[name='"+fieldname+"']").each(
		function(){ 
			if($(this).val() != ''){
				if(str.indexOf(','+$(this).val()+',') != -1){
					msgBox.tip({content:'数据已存在，请重新填写', type:'fail'});
					result = false;
					$(this).focus();
					return result;
				} else {
					str = str + ','+$(this).val()+',';
				}
			} else {
				if(valtype == 1){
					msgBox.tip({content:'数据不能为空，请进行填写', type:'fail'});
					result = false;
					$(this).focus();
					return result;
				}
			}
		}  
	);
	return result;
};


/**
 * 初始化方法
 */
jQuery(function($){
	$("#dicbtn").click(dics.dicSubmit);
	$("#operAddRow").click(dics.operAddrow);
	dics.dicTreeType();
	dics.autoTrInit();
	$(document).on('click','.dictionary-nav a',function(){
		$(".dictionary-nav a").removeClass("active");
		$(this).addClass("active");
	});
});