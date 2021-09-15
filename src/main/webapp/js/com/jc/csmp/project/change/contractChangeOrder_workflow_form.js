var contractChangeOrderModule = {};
contractChangeOrderModule.subState = false;

contractChangeOrderModule.saveOrUpdate = "save";

function insert(type, jumpFun) {
	if ($('#entityForm #partaUnitLeaderId').val() == "") {
		msgBox.info({ type:"fail", content: "该项目建设单位未设置负责人，无法提交申请单" });
		return;
	}
	if (contractChangeOrderModule.subState) return;
	contractChangeOrderModule.subState = true;
	var formData = $("#entityForm").serializeArray();
	formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
	formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
	jQuery.ajax({
		url: getRootPath() + "/project/change/saveWorkflow.action", type: 'POST', cache: false, data: formData,
		success: function(data) {
			contractChangeOrderModule.subState = false;
			if (data.success == "true") {
				setTimeout(function() {msgBox.tip({ type: "success", content: data.successMessage, callback: contractChangeOrderModule.gotoTodo });}, 500);
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
			contractChangeOrderModule.subState = false;
			msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
		}
	});
}

function update(type, jumpFun) {
	if (contractChangeOrderModule.subState) return;
	contractChangeOrderModule.subState = true;
	var formData = $("#entityForm").serializeArray();
	formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
	formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
	jQuery.ajax({
		url: getRootPath() + "/project/change/updateWorkflow.action", async: false, type: 'POST', data: formData,
		success: function(data) {
			contractChangeOrderModule.saveOrUpdate = "update";
			contractChangeOrderModule.subState = false;
			if(data.success == "true"){
				setTimeout(function() {msgBox.tip({ type: "success", content: data.successMessage, callback: contractChangeOrderModule.gotoTodo });}, 500);
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
			contractChangeOrderModule.subState = false;
			msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
		}
	});
}

function validateForm(){
	return $("#entityForm").valid();
}

function validateFormFail(){
	contractChangeOrderModule.subState = false;
	msgBox.info({ content: $.i18n.prop("JC_SYS_067") });
}

contractChangeOrderModule.gotoTodo = function(){
	if (contractChangeOrderModule.saveOrUpdate == 'save') {
		JCFF.loadPage({url: "/project/change/myContractSearch.action"});
	} else {
		JCFF.loadPage({url: "/project/change/todoList.action"});
	}
};

/**
 * 加载合同
 * @param projectId: 项目
 * @param contractId: 合同
 */
contractChangeOrderModule.loadContract = function(projectId, contractId) {
	var contractObj = $('#entityForm #contractId');
	contractObj.empty();
	contractObj.append("<option value=''>-请选择-</option>");
	$.ajax({
		type: "GET", data: {projectId: projectId}, dataType: "json", async: false,
		url: getRootPath() + "/contract/info/contractList.action",
		success : function(data) {
			if (data != null && data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					contractObj.append("<option value='" + data[i].id + "'>" + data[i].contractName + "</option>");
					console.log(data[i].contractName);
				}
			}
			if (contractId != null) {
				contractObj.val(contractId);
			}
		}
	});
};

contractChangeOrderModule.attach = new AttachControl.AttachListControl({
	renderElement: 'attachTable',
	businessTable: 'cm_project_change_order',
	itemName: 'uploadAttachContainer',
	titleWidth: 150
});

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	$("#saveAndClose").click(function(){contractChangeOrderModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){contractChangeOrderModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
	var bandDeptNameChangeEvent = true;
	var businessJson = $("#businessJson").val();
	if (businessJson.length > 0) {
		var businessObj = eval("("+businessJson+")");
		$("#entityForm").fill(businessObj);
		if (businessObj.modifyType == '0') {
			document.getElementById("modifyType0").checked = true;
		} else if (businessObj.modifyType == '1') {
			document.getElementById("modifyType1").checked = true;
		}
		contractChangeOrderModule.loadContract(businessObj.projectId, businessObj.contractId);
		if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1') {
			contractChangeOrderModule.attach.initAttach($('#entityForm #id').val());
		} else {
			bandDeptNameChangeEvent = false;
			contractChangeOrderModule.attach.initAttachOnView($('#entityForm #id').val());
		}
	} else {
		contractChangeOrderModule.attach.initAttach(0);
		$('#entityForm #projectName').click(function() {
		    if ($('#entityForm #deptId').val() == null) {
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
				contractChangeOrderModule.loadContract(data.id, null);
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

/**
 * jquery.validate
 */
jQuery.validator.addMethod("checkChangeAmount", function (value, element) {
	if (value == "") {
		return true;
	}
	var json = new CommonToolsLib.CommonTools({}).validateNumber({value: value, format: '#.####'});
	if(!json.result){
		$(element).data('error-msg', json.msg);
	}
	return json.result;
}, function(params, element) { return $(element).data('error-msg'); });

$("#entityForm").validate({
	ignore:'ignore',
	rules: {
		deptId: { required: true, maxlength:50 },
		projectId: { required: true },
		code: { required: true, maxlength:50 },
		changeAmount: { checkChangeAmount: true },
		modifyType: { required: true },
		contractId: { required: true },
		changeDate: { required: true },
		handleUser: { required: true, maxlength: 50 },
		changeReason: { required: true, maxlength: 255 },
		changeContent: { required: true, maxlength: 10000 },
	}
});