//初始化方法
var pinDepartmentModule = {};




//重复提交标识
pinDepartmentModule.subState = false;

//显示表单弹出层
pinDepartmentModule.show = function (){
	$("#saveAndClose").attr("class","btn");
	$("#saveAndClose").html($.i18n.prop("JC_SYS_097"));
	$("#saveOrModify").show();
	$("#id").val(0);
	pinDepartmentModule.clearForm();
	$('#myModal-edit').modal('show');
};

//获取修改信息
pinDepartmentModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/pinDepartment/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				pinDepartmentModule.clearForm();

$("#pinDepartmentForm").fill(data);
							
				$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
				$("#saveAndClose").html($.i18n.prop("JC_SYS_095"));
				$('#myModal-edit').modal('show');
			}
		}
	});
};

//添加修改公用方法
pinDepartmentModule.saveOrModify = function (hide) {
	if(pinDepartmentModule.subState)return;
		pinDepartmentModule.subState = true;
	if ($("#pinDepartmentForm").valid()) {
		var url = getRootPath()+"/sys/pinDepartment/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/sys/pinDepartment/update.action";
		}
		var formData = $("#pinDepartmentForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				pinDepartmentModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						pinDepartmentModule.clearForm();
					}
					$("#token").val(data.token);
					pinDepartmentList.pinDepartmentList();
				} else {
					if(data.labelErrorMessage){
						showErrors("pinDepartmentForm", data.labelErrorMessage);
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
				pinDepartmentModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		pinDepartmentModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
pinDepartmentModule.clearForm = function(){
	var token = $("#pinDepartmentForm #token").val();
	$('#pinDepartmentForm')[0].reset();

	$("#pinDepartmentForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});



	$("#saveAndClose").click(function(){pinDepartmentModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){pinDepartmentModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});