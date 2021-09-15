//初始化方法
var dlhUserModule = {};




//重复提交标识
dlhUserModule.subState = false;

//显示表单弹出层
dlhUserModule.show = function (){
	$("#saveAndClose").attr("class","btn");
	$("#saveAndClose").html($.i18n.prop("JC_SYS_097"));
	$("#saveOrModify").show();
	$("#id").val(0);
	dlhUserModule.clearForm();
	$('#myModal-edit').modal('show');
};

//获取修改信息
dlhUserModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/dlh/dlhUser/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				dlhUserModule.clearForm();
				$("#dlhUserForm").fill(data);							
				$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
				$("#saveAndClose").html($.i18n.prop("JC_SYS_095"));
				$('#myModal-edit').modal('show');
			}
		}
	});
};

//获取修改信息
dlhUserModule.generateRSA = function () {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/dlh/dlhUser/generatorRSA.action",
		dataType : "json",
		success : function(resData) {
			if (resData&&resData.success) {
				$("#dlhPasswordPublic").val(resData.data.public);
				$("#dlhPasswordPrivate").val(resData.data.private);
			}
		}
	});
};

//添加修改公用方法
dlhUserModule.saveOrModify = function (hide) {
	if(dlhUserModule.subState)return;
		dlhUserModule.subState = true;
	if ($("#dlhUserForm").valid()) {
		var url = getRootPath()+"/dlh/dlhUser/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/dlh/dlhUser/update.action";
		}
		var formData = $("#dlhUserForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				dlhUserModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						dlhUserModule.clearForm();
					}
					$("#token").val(data.token);
					dlhUserList.dlhUserList();
				} else {
					if(data.labelErrorMessage){
						showErrors("dlhUserForm", data.labelErrorMessage);
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
				dlhUserModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		dlhUserModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
dlhUserModule.clearForm = function(){
	var token = $("#dlhUserForm #token").val();
	$('#dlhUserForm')[0].reset();

	$("#dlhUserForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});


	$("#generatorBtn").click(function(){dlhUserModule.generateRSA();});
	$("#saveAndClose").click(function(){dlhUserModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){dlhUserModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});