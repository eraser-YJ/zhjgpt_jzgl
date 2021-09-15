var subsystemModule = {};
subsystemModule.subState = false;

subsystemModule.show = function (){
	$("#saveAndClose").attr("class","btn");
	$("#saveAndClose").html($.i18n.prop("JC_SYS_097"));
	$("#saveOrModify").show();
	$("#id").val(0);
	$("#titleSubsystem").html("新增");
	subsystemModule.clearForm();
	$('#myModal-edit').modal('show');
};

subsystemModule.get = function (id) {
	$.ajax({
		type: "GET", url: getRootPath() + "/sys/subsystem/get.action", data: {"id": id}, dataType: "json",
		success: function(data) {
			if (data) {
				hideErrorMessage();
				subsystemModule.clearForm();
				$("#subsystemForm").fill(data);
				$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
				$("#saveAndClose").html($.i18n.prop("JC_SYS_095"));
				$("#titleSubsystem").html("编辑");
				$('#myModal-edit').modal('show');
			}
		}
	});
};

subsystemModule.saveOrModify = function (hide) {
	if(subsystemModule.subState)return;
		subsystemModule.subState = true;
	if ($("#subsystemForm").valid()) {
		var url = getRootPath() + "/sys/subsystem/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath() + "/sys/subsystem/update.action";
		}
		var formData = $("#subsystemForm").serializeArray();
		jQuery.ajax({
			url: url, type: 'POST', cache: false, data: formData,
			success: function(data) {
				subsystemModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({ type:"success", content: data.successMessage });
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						subsystemModule.clearForm();
					}
					$("#token").val(data.token);
					subsystemList.subsystemList();
				} else {
					if(data.labelErrorMessage){
						showErrors("subsystemForm", data.labelErrorMessage);
					} else{
						msgBox.info({ type:"fail", content: data.errorMessage });
					}
					$("#token").val(data.token);
				}
			},
			error: function() {
				subsystemModule.subState = false;
				msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
			}
		});
	} else {
		subsystemModule.subState = false;
		msgBox.info({ content:$.i18n.prop("JC_SYS_067") });
	}
};

subsystemModule.clearForm = function(){
	var token = $("#subsystemForm #token").val();
	$('#subsystemForm')[0].reset();
	$("#subsystemForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	$("#saveAndClose").click(function(){subsystemModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){subsystemModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});