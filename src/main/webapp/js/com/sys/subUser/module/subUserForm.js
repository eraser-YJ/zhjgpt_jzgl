//初始化方法
var subUserModule = {};

var displayNameJcTree = {};

var leaderIdJcTree = {};



//重复提交标识
subUserModule.subState = false;

//显示表单弹出层
subUserModule.show = function (){
	$("#saveAndClose").attr("class","btn");
	$("#saveAndClose").html($.i18n.prop("JC_SYS_097"));
	$("#saveOrModify").show();
	$("#id").val(0);
	subUserModule.clearForm();

	var deptId = $('#subUserQueryForm #deptId').val();
	if(deptId != '' ){
		$('#subUserForm #deptId').val(deptId);
	}
    $("#titleSubUserId").html("新增");
	$('#myModal-edit').modal('show');
	displayNameJcTree.unReadonly();
};

//获取修改信息
subUserModule.get = function (id,deptId) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/subUser/get.action",
		data : {"id": id,"deptId":deptId},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				subUserModule.clearForm();
				if(data.displayName){
					displayNameJcTree.setData({
						"id":data.id,
						"text":data.displayName
					});
				}
				displayNameJcTree.readonly();
				if(data.leaderId && data.leaderIdValue){
					leaderIdJcTree.setData({
						"id":data.leaderId,
						"text":data.leaderIdValue
					});
				}

				$("#subUserForm").fill(data);
							
				$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
				$("#saveAndClose").html($.i18n.prop("JC_SYS_095"));
                $("#titleSubUserId").html("编辑");
				$('#myModal-edit').modal('show');


			}
		}
	});
};

//添加修改公用方法
subUserModule.saveOrModify = function (hide) {
	if(subUserModule.subState)return;
		subUserModule.subState = true;
	if ($("#subUserForm").valid()) {
		var url = getRootPath()+"/sys/subUser/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/sys/subUser/update.action";
		}
		var formData = $("#subUserForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				subUserModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						subUserModule.clearForm();
					}
					$("#token").val(data.token);
					subUserList.subUserList();
				} else {
					if(data.labelErrorMessage){
						showErrors("subUserForm", data.labelErrorMessage);
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
				subUserModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		subUserModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
subUserModule.clearForm = function(){
	var token = $("#subUserForm #token").val();
	$('#subUserForm')[0].reset();
	displayNameJcTree.clearValue();
	leaderIdJcTree.clearValue();

	$("#subUserForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});

	displayNameJcTree = JCTree.init({
		container:"displayNameFormDiv",
		controlId:"displayNameForm-displayName",
		isCheckOrRadio:false
	});
	leaderIdJcTree = JCTree.init({
		container:"leaderIdFormDiv",
		controlId:"leaderIdForm-leaderId",
		isCheckOrRadio:false
	});


	$("#saveAndClose").click(function(){subUserModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){subUserModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});