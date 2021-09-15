var projectChangeOrderModule = {};
projectChangeOrderModule.subState = false;
projectChangeOrderModule.saveOrUpdate = "save";
function insert(type, jumpFun) {
	if ($('#entityForm #partaUnitLeaderId').val() == "") {
		msgBox.info({ type:"fail", content: "该项目建设单位未设置负责人，无法提交申请单" });
		return;
	}
	if (projectChangeOrderModule.subState) return;
	projectChangeOrderModule.subState = true;
	var formData = $("#entityForm").serializeArray();
	formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
	formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
	jQuery.ajax({
		url: getRootPath() + "/project/change/saveWorkflow.action", type: 'POST', cache: false, data: formData,
		success: function(data) {
			projectChangeOrderModule.subState = false;
			if (data.success == "true") {
				setTimeout(function() {msgBox.tip({ type: "success", content: data.successMessage, callback: projectChangeOrderModule.gotoTodo });}, 500);
				$("#token").val(data.token);
			} else {
				if(data.labelErrorMessage){
					showErrors("entityForm", data.labelErrorMessage);
				} else{
					msgBox.info({ type:"fail", content: data.errorMessage });
				}
				$("#token").val(data.token);
			}
		},
		error: function() {
			projectChangeOrderModule.subState = false;
			msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
		}
	});
}

function update(type, jumpFun) {
	if (projectChangeOrderModule.subState) return;
	projectChangeOrderModule.subState = true;
	var formData = $("#entityForm").serializeArray();
	formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
	formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
	jQuery.ajax({
		url: getRootPath() + "/project/change/updateWorkflow.action", async: false, type: 'POST', data: formData,
		success: function(data) {
			projectChangeOrderModule.saveOrUpdate = "update";
			projectChangeOrderModule.subState = false;
			if(data.success == "true"){
				setTimeout(function() {msgBox.tip({ type: "success", content: data.successMessage, callback: projectChangeOrderModule.gotoTodo });}, 500);
				$("#token").val(data.token);
			} else {
				if(data.labelErrorMessage){
					showErrors("entityForm", data.labelErrorMessage);
				} else{
					msgBox.info({ type: "fail", content: data.errorMessage });
				}
			}
		},
		error: function() {
			projectChangeOrderModule.subState = false;
			msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
		}
	});
}

function validateForm(){
	return $("#entityForm").valid();
}

function validateFormFail(){
	projectChangeOrderModule.subState = false;
	msgBox.info({ content: $.i18n.prop("JC_SYS_067") });
}

projectChangeOrderModule.gotoTodo = function(){
	if (projectChangeOrderModule.saveOrUpdate == 'save') {
		JCFF.loadPage({url: "/project/change/myProjectSearch.action"});
	} else {
		JCFF.loadPage({url: "/project/change/todoList.action"});
	}
};

projectChangeOrderModule.attach = new AttachControl.AttachListControl({
	renderElement: 'attachTable',
	businessTable: 'cm_project_change_order',
	itemName: 'uploadAttachContainer',
	titleWidth: 150
});

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	$("#saveAndClose").click(function(){projectChangeOrderModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){projectChangeOrderModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
	var businessJson = $("#businessJson").val();
	var bandDeptNameChangeEvent = true;
	if (businessJson.length > 0) {
		var businessObj = eval("("+businessJson+")");
		$("#entityForm").fill(businessObj);
		if (businessObj.modifyType == '0') {
			document.getElementById("modifyType0").checked = true;
		} else if (businessObj.modifyType == '1') {
			document.getElementById("modifyType1").checked = true;
		}
		if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1') {
			projectChangeOrderModule.attach.initAttach($('#entityForm #id').val());
		} else {
			bandDeptNameChangeEvent = false;
			projectChangeOrderModule.attach.initAttachOnView($('#entityForm #id').val());
		}
	} else {
		projectChangeOrderModule.attach.initAttach(0);
		$('#entityForm #projectName').click(function() {
		    if ($('#entityForm #deptId').val() == "") {
                msgBox.info({ content: "请选择申请单位" });
		        return;
            }
			chooseProjectPanel.init(function(data) {
				//选择项目名后回调函数
				$('#entityForm #projectId').val(data.id);
				$('#entityForm #projectName').val(data.projectName);
				if (data.buildDeptIdLeaderId != "") {
					$('#entityForm #partaUnitLeaderId').val(data.buildDeptIdLeaderId);
					$('#entityForm #buildDeptPersion').val(data.buildDeptPersion);
					$('#entityForm #buildDeptPersionMobile').val(data.buildDeptPersionMobile);
					$('#buildDeptTrContainer').show();
				} else {
					$('#entityForm #buildDeptPersion').val("");
					$('#entityForm #buildDeptPersionMobile').val("");
					$('#buildDeptTrContainer').hide();
					msgBox.info({ type:"fail", content: "该项目建设单位未设置负责人，无法提交申请单" });
					return;
				}
			}, $('#entityForm #deptId').val());
		});
		//绑定默认的申请单位
		var defaultApplyDept = $('#defaultApplyDept').val();
		if (defaultApplyDept.length > 0) {
			$('#entityForm #deptId').val(defaultApplyDept.split(',')[0]);
			$('#entityForm #deptName').val(defaultApplyDept.split(',')[1]);
		}
	}

	if (bandDeptNameChangeEvent) {
		$('#deptName').click(function() {
			new ChooseCompany.CompanyDataTable({
				renderElement: 'chooseCompanyModule',
				callback: function(data){
					$('#entityForm #projectId').val("");
					$('#entityForm #projectName').val("");
					$('#entityForm #partaUnitLeaderId').val("");
					$('#entityForm #deptName').val(data.name);
					$('#entityForm #deptId').val(data.id);
				}
			});
		});
	}
	formPriv.doForm();
	if (businessJson.length == 0) {
		$('#workflowButtongoBack').hide();
	}
});

$("#entityForm").validate({
	ignore:'ignore',
	rules: {
		deptId: { required: true, maxlength:50 },
		projectId: { required: true },
		modifyTypeContrainer: {required: true},
		changeDate: { required: true },
		code: { required: true, maxlength:50 },
		changeAmount: { common_check_positive_double_four: true },
		handleUser: { required: true, maxlength: 50 },
		changeReason: { required: true, maxlength: 255 },
		changeContent: { required: true, maxlength: 10000 },
	}
});