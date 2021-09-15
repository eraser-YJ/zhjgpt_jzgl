var roleEdit = {};

roleEdit.subState = false;

//添加修改公用方法
roleEdit.saveOrModify = function (hide) {
	var dWeight = $("#roleForm #deptWeight").val();
	var rWeight = $("#roleForm #weight").val();
	var bWeight = $("#roleForm #bestrideWeight").val();
	if(parseInt(dWeight) < parseInt(rWeight)){
		msgBox.info({content:"角色权重不能大于所属部门权重", type:'fail'});
		return;
	}
	if(parseInt(dWeight) < parseInt(bWeight)){
		msgBox.info({content:"跨域权重不能大于所属部门权重", type:'fail'});
		return;
	}
	if (roleEdit.subState)return;
	roleEdit.subState = true;
	if ($("#roleForm").valid()) {
		var url = getRootPath()+"/sys/role/save.action?time=" + new Date().getTime();
		if ($("#roleForm #id").val() != 0) {
			url = getRootPath()+"/sys/role/update.action?time=" + new Date().getTime();
		}
		var formData = $("#roleForm").serializeArray();
		//添加其他表单信息
		jQuery.ajax({
			url : url,
			type : 'POST',
			data : formData,
			success : function(data) {
				roleEdit.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content:"保存成功"
					});
					if ($("#roleForm #id").val() == 0) {
						roleEdit.initForm();
					}
					if (hide) {
						$('#myModal-edit').modal('hide');
					}
					//getToken();
					$("#token").val(data.token);
					role.roleList();
				} else {
					if(data.labelErrorMessage){
						showErrors("roleForm", data.labelErrorMessage);
					} else{
						msgBox.info({
							type:"fail",
							content:data.errorMessage
						});
					}
					//getToken();
					$("#token").val(data.token);
				}
			},
			error : function() {
				roleEdit.subState = false;
				alertx("保存失败");
			}
		});
	}else {
		roleEdit.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//添加成功清空表单数据
roleEdit.initForm = function(){
	roleEdit.clearForm();
};

roleEdit.clearForm = function(){
	//$('#roleForm')[0].reset();
	$('#roleForm #name').val('');
	$('#roleForm #id').val('');
	$('#roleForm #weight').val('');
	$('#roleForm #bestrideWeight').val('');
	$('#roleForm #description').val('');
	hideErrorMessage();
}

jQuery(function($){
	$("#savaAndClose").click(function(){roleEdit.saveOrModify(true);});
	$("#savaAndKeep").click(function(){roleEdit.saveOrModify(false);});
	$("#roleDivClose").click(function(){
		$('#myModal-edit').modal('hide');
	});
});