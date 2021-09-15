//初始化方法
var dlhDatamodelModule = {};

//重复提交标识
dlhDatamodelModule.subState = false;

//显示表单弹出层
dlhDatamodelModule.show = function (){
	$("#id").val(0);
	dlhDatamodelModule.clearForm();
	$('#myModal-edit').modal('show');
	$("#tableCode").removeAttr("readonly");
	$("#dbSource").removeAttr("readonly");
};

//获取修改信息
dlhDatamodelModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/dlh/dlhDatamodel/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				dlhDatamodelModule.clearForm();
				$("#tableCode").attr("readonly","readonly");
				$("#dbSource").attr("readonly","readonly");
				$("#dlhDatamodelForm").fill(data); 
				$('#myModal-edit').modal('show');
			}
		}
	});
};

//添加修改公用方法
dlhDatamodelModule.saveOrModify = function (hide) {
	if(dlhDatamodelModule.subState)return;
		dlhDatamodelModule.subState = true;
	if ($("#dlhDatamodelForm").valid()) {
		var url = getRootPath()+"/dlh/dlhDatamodel/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/dlh/dlhDatamodel/update.action";
		}
		var formData = $("#dlhDatamodelForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				dlhDatamodelModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						dlhDatamodelModule.clearForm();
					}
					$("#token").val(data.token);
					dlhDatamodelList.dlhDatamodelList();
				} else {
					if(data.labelErrorMessage){
						showErrors("dlhDatamodelForm", data.labelErrorMessage);
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
				dlhDatamodelModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		dlhDatamodelModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
dlhDatamodelModule.clearForm = function(){
	var token = $("#dlhDatamodelForm #token").val();
	$('#dlhDatamodelForm')[0].reset();

	$("#dlhDatamodelForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});



	$("#saveAndClose").click(function(){dlhDatamodelModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){dlhDatamodelModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});