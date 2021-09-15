$(document).ready(function(){
	
	ie8StylePatch();
	$("#saveAndClose").click(function(){userPhoneModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){userPhoneModule.saveOrModify(false);});
	$(".datepicker-input").each(function(){$(this).datepicker();});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
	



});
  	

	
//初始化方法
var userPhoneModule = {};

//重复提交标识
userPhoneModule.subState = false;

//显示表单弹出层
userPhoneModule.show = function (){
	$("#saveAndClose").attr("class","btn");
	$("#saveAndClose").html($.i18n.prop("JC_SYS_097"));
	$("#saveOrModify").show();
	$("#id").val(0);
	userPhoneModule.clearForm();
	$('#myModal-edit').modal('show');
};

//获取修改信息
userPhoneModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/userPhone/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				userPhoneModule.clearForm();
				$("#userPhoneForm").fill(data);
				$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
				$("#saveAndClose").html($.i18n.prop("JC_SYS_095"));
				$('#myModal-edit').modal('show');
			}
		}
	});
};

//添加修改公用方法
userPhoneModule.saveOrModify = function (hide) {
	if(userPhoneModule.subState)return;
		userPhoneModule.subState = true;
	if ($("#userPhoneForm").valid()) {
		var url = getRootPath()+"/sys/userPhone/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/sys/userPhone/update.action";
		}
		var formData = $("#userPhoneForm").serializeArray();
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				userPhoneModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						userPhoneModule.clearForm();
					}
					$("#token").val(data.token);
					userPhone.userPhoneList();
				} else {
					if(data.labelErrorMessage){
						showErrors("userPhoneForm", data.labelErrorMessage);
					} else{
						msgBox.info({
							content: data.errorMessage
						});
					}
					$("#token").val(data.token);
				}
			},
			error : function() {
				userPhoneModule.subState = false;
				msgBox.tip({
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		userPhoneModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
userPhoneModule.clearForm = function(){
	$('#userPhoneForm')[0].reset();
	hideErrorMessage();
};
  	


