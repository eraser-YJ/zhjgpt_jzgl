//初始化方法
var dlhDatamodelItemModule = {};




//重复提交标识
dlhDatamodelItemModule.subState = false;

//显示表单弹出层
dlhDatamodelItemModule.show = function (){ 
	$("#id").val(0);
	dlhDatamodelItemModule.clearForm();
	$('#myModal-edit').modal('show');
	$('#modelId').val($("#query_modelId").val());
	dlhDatamodelItemModule.editCtrlFun(true);
};
//显示表单弹出层
dlhDatamodelItemModule.editCtrlFun = function (newMode){
	if(newMode){
		$("#itemName").removeAttr("readonly");
		$("#itemType").removeAttr("disabled");
		$("#itemLen").removeAttr("readonly");
		$("#dicCode").removeAttr("readonly");
	} else {
		$("#itemName").attr("readonly","readonly");
		$("#itemType").attr("disabled","disabled");
		$("#itemLen").attr("readonly","readonly");
		$("#dicCode").attr("readonly","readonly");
	}
};
//获取修改信息
dlhDatamodelItemModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/dlh/dlhDatamodelItem/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				dlhDatamodelItemModule.clearForm();
				$("#dlhDatamodelItemForm").fill(data);
				$('#myModal-edit').modal('show');
				dlhDatamodelItemModule.editCtrlFun(false);
			}
		}
	});
};

//添加修改公用方法
dlhDatamodelItemModule.saveOrModify = function (hide) {
	
	
	
	if(dlhDatamodelItemModule.subState)return;
		dlhDatamodelItemModule.subState = true;
	if ($("#dlhDatamodelItemForm").valid()) {
		var itemType = $("#itemType").val();
		if(itemType == 'dic'){
			var dicCode = $("#dicCode").val();
			if(dicCode == ''){
				msgBox.info({
					type:"fail",
					content: "请填写字典编码"
				});
				dlhDatamodelItemModule.subState = false;
				return ;
			}
		}
		
		var url = getRootPath()+"/dlh/dlhDatamodelItem/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/dlh/dlhDatamodelItem/update.action";
		} else {
			$('#modelId').val($("#query_modelId").val());
		}
		var formData = $("#dlhDatamodelItemForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				dlhDatamodelItemModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						dlhDatamodelItemModule.clearForm();
					}
					$("#token").val(data.token);
					dlhDatamodelItemList.dlhDatamodelItemList();
				} else {
					if(data.labelErrorMessage){
						showErrors("dlhDatamodelItemForm", data.labelErrorMessage);
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
				dlhDatamodelItemModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		dlhDatamodelItemModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
dlhDatamodelItemModule.clearForm = function(){
	var token = $("#dlhDatamodelItemForm #token").val();
	$('#dlhDatamodelItemForm')[0].reset();

	$("#dlhDatamodelItemForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});



	$("#saveAndClose").click(function(){dlhDatamodelItemModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){dlhDatamodelItemModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});