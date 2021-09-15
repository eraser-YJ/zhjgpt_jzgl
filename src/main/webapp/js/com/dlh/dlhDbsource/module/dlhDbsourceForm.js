//初始化方法
var dlhDbsourceModule = {};

//重复提交标识
dlhDbsourceModule.subState = false;

//显示表单弹出层
dlhDbsourceModule.show = function (){
	$("#saveAndClose").attr("class","btn");
	$("#saveAndClose").html($.i18n.prop("JC_SYS_097"));
	$("#saveOrModify").show();
	$("#id").val(0);
	dlhDbsourceModule.clearForm();
	$('#myModal-edit').modal('show');
	dlhDbsourceModule.editCtrlFun(true)
};

dlhDbsourceModule.editCtrlFun = function (newMode){
	if(newMode){
		$("#dbCode").removeAttr("readonly");
		$("#dbType").removeAttr("disabled");
	} else {
		$("#dbCode").attr("readonly","readonly");
		$("#dbType").attr("disabled","disabled");
	}
};

//获取修改信息
dlhDbsourceModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/dlh/dlhDbsource/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				dlhDbsourceModule.clearForm();
				$("#dlhDbsourceForm").fill(data);
				$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
				$("#saveAndClose").html($.i18n.prop("JC_SYS_095"));
				$('#myModal-edit').modal('show');
				dlhDbsourceModule.editCtrlFun(false)
			}
		}
	});
};

//添加修改公用方法
dlhDbsourceModule.saveOrModify = function (hide) {
	if(dlhDbsourceModule.subState)return;
		dlhDbsourceModule.subState = true;
	if ($("#dlhDbsourceForm").valid()) {
		var url = getRootPath()+"/dlh/dlhDbsource/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/dlh/dlhDbsource/update.action";
		}
		var formData = $("#dlhDbsourceForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				dlhDbsourceModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						dlhDbsourceModule.clearForm();
					}
					$("#token").val(data.token);
					dlhDbsourceList.dlhDbsourceList();
				} else {
					if(data.labelErrorMessage){
						showErrors("dlhDbsourceForm", data.labelErrorMessage);
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
				dlhDbsourceModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		dlhDbsourceModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
dlhDbsourceModule.clearForm = function(){
	var token = $("#dlhDbsourceForm #token").val();
	$('#dlhDbsourceForm')[0].reset();

	$("#dlhDbsourceForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});



	$("#saveAndClose").click(function(){dlhDbsourceModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){dlhDbsourceModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});