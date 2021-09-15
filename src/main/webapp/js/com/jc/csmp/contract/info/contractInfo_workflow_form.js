var contractInfoModule = {};
contractInfoModule.subState = false;

contractInfoModule.saveOrUpdate = "save";

contractInfoModule.attach = new AttachControl.AttachListControl({
	renderElement: 'attachTable',
	itemName: 'uploadAttachContainer',
	businessTable: 'cm_contract_info'
});

function insert(type, jumpFun) {
	if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1' && !contractInfoModule.attach.validate()) {
		contractInfoModule.subState = false;
		return;
	}
	if (contractInfoModule.subState) return;
	contractInfoModule.subState = true;
	$('#workflowButtonId1').hide();
	var formData = $("#entityForm").serializeArray();
	formData.push({"name": "attachFile", "value": liuAttachPool.getAllFiles()});
	formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
	jQuery.ajax({
		url: getRootPath() + "/contract/info/saveWorkflow.action", type: 'POST', cache: false, data: formData,
		success: function(data) {
			if (data.success == "true") {
				setTimeout(function() {msgBox.tip({ type: "success", content: data.successMessage, callback: contractInfoModule.gotoTodo });}, 500);
				$("#token").val(data.token);
			} else {
				if(data.labelErrorMessage){
					showErrors("entityForm", data.labelErrorMessage);
				} else{
					msgBox.info({ type:"fail", content: data.errorMessage });
				}
				$("#token").val(data.token);
				contractInfoModule.subState = false;
				$('#workflowButtonId1').show();
			}
		},
		error: function() {
			contractInfoModule.subState = false;
			$('#workflowButtonId1').show();
			msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
		}
	});
}

function update(type, jumpFun) {
	if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1' && !contractInfoModule.attach.validate()) {
		contractInfoModule.subState = false;
		return;
	}
	if (contractInfoModule.subState) return;
	contractInfoModule.subState = true;
	$('#workflowButtonId1').hide();
	var formData = $("#entityForm").serializeArray();
	formData.push({"name": "attachFile", "value": liuAttachPool.getAllFiles()});
	formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
	jQuery.ajax({
		url: getRootPath() + "/contract/info/updateWorkflow.action", async: false, type: 'POST', data: formData,
		success: function(data) {
			contractInfoModule.saveOrUpdate = "update";
			contractInfoModule.subState = false;
			if(data.success == "true"){
				setTimeout(function() {msgBox.tip({ type: "success", content: data.successMessage, callback: contractInfoModule.gotoTodo });}, 500);
				$("#token").val(data.token);
			} else {
				if(data.labelErrorMessage){
					showErrors("entityForm", data.labelErrorMessage);
				} else{
					msgBox.info({ type: "fail", content: data.errorMessage });
				}
				$('#workflowButtonId1').show();
			}
		},
		error: function() {
			contractInfoModule.subState = false;
			msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
			$('#workflowButtonId1').show();
		}
	});
}

function validateForm(){
	return $("#entityForm").valid();
}

function validateFormFail(){
	contractInfoModule.subState = false;
	msgBox.info({ content: $.i18n.prop("JC_SYS_067"), });
}

contractInfoModule.gotoTodo = function(){
	if (contractInfoModule.saveOrUpdate = "save") {
		JCFF.loadPage({url: "/contract/info/search.action?fn=myApply"});
	} else {
		JCFF.loadPage({url: "/contract/info/todoList.action"});
	}
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	$("#saveAndClose").click(function(){contractInfoModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){contractInfoModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
	var businessJson = $("#businessJson").val();
	if (businessJson.length > 0) {
		var businessObj = eval("("+businessJson+")");
		$("#entityForm").fill(businessObj);
		$('#partyaUnitTdHtml').html(businessObj.partyaUnitValue);
		if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1') {
			contractInfoModule.attach.initAttach($('#entityForm #id').val());
		} else {
			contractInfoModule.attach.initAttachOnView($('#entityForm #id').val());
		}
	} else {
		$('#workflowButtongoBack').hide();
		contractInfoModule.attach.initAttach(0);
	}
	if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1') {
		$('#entityForm #projectName').click(function() {
			chooseProjectPanel.init(function(data) {
				$('#entityForm #projectId').val(data.id);
				$('#entityForm #projectName').val(data.projectName);
				$('#entityForm #partyaUnit').val(data.buildDeptId);
				$('#partyaUnitTdHtml').html(data.buildDeptIdValue);
			});
		});
		$('#entityForm #partybUnitValue').click(function() {
			new ChooseCompany.CompanyDataTable({
				renderElement: 'chooseCompanyModule',
				callback: function(data){
					$('#entityForm #partybUnitValue').val(data.name);
					$('#entityForm #partybUnit').val(data.id);
				}
			});
		});
	}
	formPriv.doForm();
});

/**
 * jquery.validate
 */
$("#entityForm").validate({
	ignore:'ignore',
	rules: {
		piId:{ required: false, maxlength:50 },
		projectId: { required: true, maxlength:50 },
		contractName: { required: true, maxlength:255 },
		contractType: { required: true, maxlength:50 },
		contractCode: { required: true, maxlength:255 },
		contractMoney: { required: true, maxlength:18 },
		partybUnit: { required: true, maxlength:50 },
		constructionPeriod: { common_check_positive_double_two: true, required: true, maxlength:255 },
		paymentMethod: { required: false, maxlength:50 },
		needAudit: { required: false, maxlength:50 },
		contractContent: { required: false, maxlength:2000 },
		startDate: { required: false, maxlength:19 },
		endDate: { required: false, maxlength:19 },
		signDate: { required: false, maxlength:19 },
		handleUser: { required: true, maxlength:50 },
		remark: { required: false, maxlength:16383 },
	}
});