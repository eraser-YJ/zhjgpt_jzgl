//初始化方法
var tabTreeModule = {};




//重复提交标识
tabTreeModule.subState = false;

//显示表单弹出层
tabTreeModule.show = function (){
	$("#saveAndClose").attr("class","btn");
	$("#saveAndClose").html($.i18n.prop("JC_SYS_097"));
	$("#saveOrModify").show();
	$("#id").val(0);
	tabTreeModule.clearForm();
	$('#myModal-edit').modal('show');
};

//获取修改信息
tabTreeModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/tabTree/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				tabTreeModule.clearForm();

$("#tabTreeForm").fill(data);
							
				$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
				$("#saveAndClose").html($.i18n.prop("JC_SYS_095"));
				$('#myModal-edit').modal('show');
			}
		}
	});
};

//添加修改公用方法
tabTreeModule.saveOrModify = function (hide) {
	if(tabTreeModule.subState)return;
		tabTreeModule.subState = true;
	if ($("#tabTreeForm").valid()) {
		var url = getRootPath()+"/sys/tabTree/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/sys/tabTree/update.action";
		}
		var formData = $("#tabTreeForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				tabTreeModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						tabTreeModule.clearForm();
					}
					$("#token").val(data.token);
					tabTreeList.tabTreeList();
				} else {
					if(data.labelErrorMessage){
						showErrors("tabTreeForm", data.labelErrorMessage);
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
				tabTreeModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		tabTreeModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
tabTreeModule.clearForm = function(){
	var token = $("#tabTreeForm #token").val();
	$('#tabTreeForm')[0].reset();

	$("#tabTreeForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});



	$("#saveAndClose").click(function(){tabTreeModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){tabTreeModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});