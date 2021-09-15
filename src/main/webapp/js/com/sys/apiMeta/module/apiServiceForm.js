//初始化方法
var apiServiceModule = {};




//重复提交标识
apiServiceModule.subState = false;

//显示表单弹出层
apiServiceModule.show = function (){
	$("#saveAndClose").attr("class","btn");
	$("#saveAndClose").html($.i18n.prop("JC_SYS_097"));
	$("#saveOrModify").show();
	$("#id").val(0);
	$("#titleApi").html("新增");
	apiServiceModule.clearForm();
	$('#myModal-edit').modal('show');
};

//获取修改信息
apiServiceModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/apiService/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				apiServiceModule.clearForm();

$("#apiServiceForm").fill(data);
							
				$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
				$("#saveAndClose").html($.i18n.prop("JC_SYS_095"));
				$("#titleApi").html("编辑");
				$('#myModal-edit').modal('show');
			}
		}
	});
};

//添加修改公用方法
apiServiceModule.saveOrModify = function (hide) {
	if(apiServiceModule.subState)return;
		apiServiceModule.subState = true;
	if ($("#apiServiceForm").valid()) {
		var url = getRootPath()+"/sys/apiService/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/sys/apiService/update.action";
		}
		var formData = $("#apiServiceForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				apiServiceModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						apiServiceModule.clearForm();
					}
					$("#token").val(data.token);
					apiServiceList.apiServiceList();
				} else {
					if(data.labelErrorMessage){
						showErrors("apiServiceForm", data.labelErrorMessage);
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
				apiServiceModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		apiServiceModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
apiServiceModule.clearForm = function(){
	var token = $("#apiServiceForm #token").val();
	$('#apiServiceForm')[0].reset();

	$("#apiServiceForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});



	$("#saveAndClose").click(function(){apiServiceModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){apiServiceModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});