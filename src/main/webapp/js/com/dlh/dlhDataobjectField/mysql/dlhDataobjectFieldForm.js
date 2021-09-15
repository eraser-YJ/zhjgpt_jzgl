//初始化方法
var dlhDataobjectFieldModule = {};




//重复提交标识
dlhDataobjectFieldModule.subState = false;

//显示表单弹出层
dlhDataobjectFieldModule.show = function (){
	$("#id").val(0);
	dlhDataobjectFieldModule.clearForm();
	$('#myModal-edit').modal('show');
	$('#modelId').val($('#query_modelId').val());
	$('#objectId').val($('#query_objectId').val());
	dlhDataobjectFieldModule.editCtrlFun(true)
};

//显示表单弹出层
dlhDataobjectFieldModule.editCtrlFun = function (newMode){
	if(newMode){ 
		$("#dlhDataobjectFieldModuleShowSel").show();
	} else { 
		$("#dlhDataobjectFieldModuleShowSel").hide();
	}
};

dlhDataobjectFieldModule.showItem = function () {
	$("#dlhDataobjectFieldModuleSelDiv").html("");
	var url = getRootPath()+"/dlh/dlhDatamodelItem/select.action?modelId="+$('#query_modelId').val()+"&n_="+(new Date().getTime());
	$("#dlhDataobjectFieldModuleSelDiv").load(url,null,function(){
		myDataModelItemModalFun.show(dlhDataobjectFieldModule.showItemCallback);
	});
}
dlhDataobjectFieldModule.showItemCallback = function (id,name) {
	$("#itemId").val(id);
	$("#itemName").val(name);
}

dlhDataobjectFieldModule.checkRuleSel = function () {
	var jsonStr = $("#fieldCheck").val();
	myCheckRuleModalFun.show(dlhDataobjectFieldModule.checkRuleSelCallback,jsonStr);
}
dlhDataobjectFieldModule.checkRuleSelCallback = function (jsonStr) {
	$("#fieldCheck").val(jsonStr);
}

//获取修改信息
dlhDataobjectFieldModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/dlh/dlhDataobjectField/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				dlhDataobjectFieldModule.clearForm();
				$("#dlhDataobjectFieldForm").fill(data);
				dlhDataobjectFieldModule.editCtrlFun(false)
				$('#myModal-edit').modal('show');
			}
		}
	});
};

//添加修改公用方法
dlhDataobjectFieldModule.saveOrModify = function (hide) {
	if(dlhDataobjectFieldModule.subState)return;
		dlhDataobjectFieldModule.subState = true;
	if ($("#dlhDataobjectFieldForm").valid()) {
		var url = getRootPath()+"/dlh/dlhDataobjectField/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/dlh/dlhDataobjectField/update.action";
		}
		var formData = $("#dlhDataobjectFieldForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				dlhDataobjectFieldModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						dlhDataobjectFieldModule.clearForm();
					}
					$("#token").val(data.token);
					dlhDataobjectFieldList.dlhDataobjectFieldList();
				} else {
					if(data.labelErrorMessage){
						showErrors("dlhDataobjectFieldForm", data.labelErrorMessage);
					} else{
						msgBox.info({
							type:"fail",
							content: data.errorMessage
						});
					}
					$("#token").val(data.token);
				}
			},
			error : function() {
				dlhDataobjectFieldModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		dlhDataobjectFieldModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
dlhDataobjectFieldModule.clearForm = function(){
	var token = $("#dlhDataobjectFieldForm #token").val();
	$('#dlhDataobjectFieldForm')[0].reset();

	$("#dlhDataobjectFieldForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});



	$("#saveAndClose").click(function(){dlhDataobjectFieldModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){dlhDataobjectFieldModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});