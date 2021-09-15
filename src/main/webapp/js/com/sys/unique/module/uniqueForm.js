//初始化方法
var uniqueModule = {};




//重复提交标识
uniqueModule.subState = false;

//显示表单弹出层
uniqueModule.show = function (){
	$("#saveAndClose").attr("class","btn dark");
	$("#saveOrModify").show();
	$("#id").val(0);
	uniqueModule.clearForm();
	$("#createCount").val("1");
	$('#myModal-edit').modal('show');
};

//获取修改信息
uniqueModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/unique/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				uniqueModule.clearForm();

$("#uniqueForm").fill(data);
							
				/*$("#saveOrModify").hide();*/
				$("#saveAndClose").attr("class","btn dark");
				$('#myModal-edit').modal('show');
			}
		}
	});
};

//添加修改公用方法
uniqueModule.saveOrModify = function (hide) {
	if(uniqueModule.subState)return;
		uniqueModule.subState = true;
	if ($("#uniqueForm").valid()) {
		var url = getRootPath()+"/sys/unique/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/sys/unique/update.action";
		}
		var formData = $("#uniqueForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				uniqueModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						uniqueModule.clearForm();
					}
					$("#token").val(data.token);
					uniqueList.uniqueList();
				} else {
					if(data.labelErrorMessage){
						showErrors("uniqueForm", data.labelErrorMessage);
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
				uniqueModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		uniqueModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
uniqueModule.clearForm = function(){
	var token = $("#uniqueForm #token").val();
	$('#uniqueForm')[0].reset();

	$("#uniqueForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});



	$("#saveAndClose").click(function(){uniqueModule.saveOrModify(true);});
	//$("#saveOrModify").click(function(){uniqueModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});