//初始化方法
var pinUserModule = {};




//重复提交标识
pinUserModule.subState = false;

//显示表单弹出层
pinUserModule.show = function (){
	$("#saveAndClose").attr("class","btn");
	$("#saveAndClose").html($.i18n.prop("JC_SYS_097"));
	$("#saveOrModify").show();
	$("#id").val(0);
	pinUserModule.clearForm();
	$('#myModal-edit').modal('show');
};

//获取修改信息
pinUserModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/pinSubUser/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				pinUserModule.clearForm();

				$("#pinUserForm").fill(data);
							
				$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
				$("#saveAndClose").html($.i18n.prop("JC_SYS_095"));
				$('#myModal-edit').modal('show');
			}
		}
	});
};

//添加修改公用方法
pinUserModule.saveOrModify = function (hide) {
	if(pinUserModule.subState)return;
		pinUserModule.subState = true;
	if ($("#pinUserForm").valid()) {
		var url = getRootPath()+"/sys/pinSubUser/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/sys/pinSubUser/update.action";
		}
		var formData = $("#pinUserForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				pinUserModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						pinUserModule.clearForm();
					}
					$("#token").val(data.token);
					pinUserList.pinUserList();
				} else {
					if(data.labelErrorMessage){
						showErrors("pinUserForm", data.labelErrorMessage);
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
				pinUserModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		pinUserModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
pinUserModule.clearForm = function(){
	var token = $("#pinUserForm #token").val();
	$('#pinUserForm')[0].reset();

	$("#pinUserForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});



	$("#saveAndClose").click(function(){pinUserModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){pinUserModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});