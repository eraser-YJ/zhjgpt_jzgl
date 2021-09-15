//初始化方法
var dlhDataobjectModule = {};




//重复提交标识
dlhDataobjectModule.subState = false;

//显示表单弹出层
dlhDataobjectModule.show = function (){
	$("#id").val(0);
	dlhDataobjectModule.clearForm();
	$('#myModal-edit').modal('show');
	dlhDataobjectModule.editCtrlFun(true)
};

//显示表单弹出层
dlhDataobjectModule.editCtrlFun = function (newMode){
	if(newMode){
		$("#objUrl").removeAttr("readonly");
		$("#modelId").removeAttr("readonly");
	} else {
		$("#objUrl").attr("readonly","readonly");
		$("#modelId").attr("readonly","readonly");
	}
};

//获取修改信息
dlhDataobjectModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/dlh/dlhDataobject/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				dlhDataobjectModule.clearForm();
				$("#dlhDataobjectForm").fill(data);
				dlhDataobjectModule.editCtrlFun(false)			
				$("#saveOrModify").hide();
				$('#myModal-edit').modal('show');
			}
		}
	});
};

//添加修改公用方法
dlhDataobjectModule.saveOrModify = function (hide) {
	if(dlhDataobjectModule.subState)return;
		dlhDataobjectModule.subState = true;
	if ($("#dlhDataobjectForm").valid()) {
		var url = getRootPath()+"/dlh/dlhDataobject/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/dlh/dlhDataobject/update.action";
		}
		var formData = $("#dlhDataobjectForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				dlhDataQueryList.dlhDataQueryList();
				dlhDataobjectModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						dlhDataobjectModule.clearForm();
					}
					$("#token").val(data.token);
					
				} else {
					if(data.labelErrorMessage){
						showErrors("dlhDataobjectForm", data.labelErrorMessage);
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
				dlhDataobjectModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		dlhDataobjectModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
dlhDataobjectModule.clearForm = function(){
	var token = $("#dlhDataobjectForm #token").val();
	$('#dlhDataobjectForm')[0].reset();

	$("#dlhDataobjectForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});



	$("#saveAndClose").click(function(){dlhDataobjectModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){dlhDataobjectModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});