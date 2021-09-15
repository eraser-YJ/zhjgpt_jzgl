var contractPayModule = {};
contractPayModule.subState = false;
contractPayModule.saveOrUpdate = "save";
function insert(type, jumpFun) {
	if (contractPayModule.subState) return;
	contractPayModule.subState = true;
	var formData = $("#entityForm").serializeArray();
	formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
	formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
	jQuery.ajax({
		url: getRootPath() + "/contract/pay/saveWorkflow.action", type: 'POST', cache: false, data: formData,
		success: function(data) {
			contractPayModule.subState = false;
			if (data.success == "true") {
				setTimeout(function() {msgBox.tip({ type: "success", content: data.successMessage, callback: contractPayModule.gotoTodo });}, 500);
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
			contractPayModule.subState = false;
			msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
		}
	});
}

function update(type, jumpFun) {
	if (contractPayModule.subState) return;
	contractPayModule.subState = true;
	var formData = $("#entityForm").serializeArray();
	formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
	formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
	console.log(formData);
	jQuery.ajax({
		url: getRootPath() + "/contract/pay/updateWorkflow.action", async: false, type: 'POST', data: formData,
		success: function(data) {
			contractPayModule.saveOrUpdate = "update";
			contractPayModule.subState = false;
			if(data.success == "true"){
				setTimeout(function() {msgBox.tip({ type: "success", content: data.successMessage, callback: contractPayModule.gotoTodo });}, 500);
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
			contractPayModule.subState = false;
			msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
		}
	});
}

function validateForm(){
	return $("#entityForm").valid();
}

function validateFormFail(){
	contractPayModule.subState = false;
	msgBox.info({ content: $.i18n.prop("JC_SYS_067"), });
}

contractPayModule.gotoTodo = function(){
	if (contractPayModule.saveOrUpdate = "save") {
		JCFF.loadPage({url: "/contract/pay/paySearch.action?fn=myApply"});
	} else {
		JCFF.loadPage({url: "/contract/pay/todoList.action"});
	}
};

contractPayModule.attach = new AttachControl.AttachListControl({
	renderElement: 'attachTable',
	businessTable: 'cm_contract_pay',
	itemName: 'uploadAttachContainer',
	titleWidth: 150
});

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	$("#saveAndClose").click(function(){contractPayModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){contractPayModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
	var businessJson = $("#businessJson").val();
	if (businessJson.length > 0) {
		var businessObj = eval("("+businessJson+")");
		$("#entityForm").fill(businessObj);
		var contractMoney = businessObj.contractMoney == "" || businessObj.contractMoney == null ? 0 : businessObj.contractMoney;
		var totalPayment = businessObj.totalPayment == "" || businessObj.totalPayment == null ? 0 : businessObj.totalPayment;
		$('#entityForm #contractMoney').parent().html(contractMoney + $('#entityForm #contractMoney').parent().html());
		$('#entityForm #totalPayment').parent().html(totalPayment + $('#entityForm #totalPayment').parent().html());
		$('#entityForm #contractMoney').val(contractMoney);
		$('#entityForm #totalPayment').val(totalPayment);
		$('#entityForm #contractMoney').hide();
		$('#entityForm #totalPayment').hide();
		$('#payContainer').show();
		if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1') {
			contractPayModule.attach.initAttach($('#entityForm #id').val());
		} else {
			contractPayModule.attach.initAttachOnView($('#entityForm #id').val());
		}
	} else {
		contractPayModule.attach.initAttach(0);
	}
	//第一次提交申请时，所属合同注册点击事件
	if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1') {
		$('#entityForm #contractName').click(function() {
			chooseContractPanel.init(function(data) {
				$('#entityForm #contractId').val(data.id);
				$('#entityForm #contractName').val(data.contractName);
				$('#entityForm #contractMoney').val(data.contractMoney == "" || data.contractMoney == null ? 0 : data.contractMoney);
				$('#entityForm #totalPayment').val(data.totalPayment == "" || data.totalPayment == null ? 0 : data.totalPayment);
				$('#payContainer').show();
			});
		});
	}
	formPriv.doForm();
	$('#workflowButtongoBack').hide();
});

/**
 * jquery.validate
 */
/**
 * 验证申请金额
 */
jQuery.validator.addMethod("checkApplyMoney", function (value, element) {
	if (value == "") {
		$(element).data('error-msg', '请输入申请金额');
		return false;
	}
	if (value === '0') {
		$(element).data('error-msg', '请输入大于0的数字');
		return false;
	}
	if (value.indexOf('.') > -1) {
		if (value.split('.')[0].length > 13) {
			$(element).data('error-msg', '整数位长度不能大于13位');
			return false;
		}
	} else {
		if (value.length > 13) {
			$(element).data('error-msg', '整数位长度不能大于13位');
			return false;
		}
	}
	var json = new CommonToolsLib.CommonTools({}).validateNumber({value: value, format: '+#.##'});
	if (json.result) {
		var totalPayment = $('#entityForm #totalPayment').val();
		var contractMoney = $('#entityForm #contractMoney').val();
		totalPayment = (totalPayment == '') ? 0 : parseInt(totalPayment);
		contractMoney = (contractMoney == '') ? 0 : parseInt(contractMoney);
		value = parseInt(value);
		if (value > (contractMoney - totalPayment)) {
			$(element).data('error-msg', '申请金额+支付总额大于合同金额，请确认');
			return false;
		}
	}
	if(!json.result){
		$(element).data('error-msg', json.msg);
	}
	return json.result;
}, function(params, element) { return $(element).data('error-msg'); });

jQuery.validator.addMethod("checkReplyMoney", function (value, element) {
	if (value == "") {
		return true;
	}
	if (value === '0') {
		$(element).data('error-msg', '请输入大于0的数字');
		return false;
	}
	if (value.indexOf('.') > -1) {
		if (value.split('.')[0].length > 13) {
			$(element).data('error-msg', '整数位长度不能大于13位');
			return false;
		}
	} else {
		if (value.length > 13) {
			$(element).data('error-msg', '整数位长度不能大于13位');
			return false;
		}
	}
	var json = new CommonToolsLib.CommonTools({}).validateNumber({value: value, format: '+#.##'});
	if (json.result) {
		var totalPayment = $('#entityForm #totalPayment').val();
		var contractMoney = $('#entityForm #contractMoney').val();
		totalPayment = (totalPayment == '') ? 0 : parseInt(totalPayment);
		contractMoney = (contractMoney == '') ? 0 : parseInt(contractMoney);
		value = parseInt(value);
		if (value > (contractMoney - totalPayment)) {
			$(element).data('error-msg', '批复金额+支付总额大于合同金额，请确认');
			return false;
		}
	}
	if(!json.result){
		$(element).data('error-msg', json.msg);
	}
	return json.result;
}, function(params, element) { return $(element).data('error-msg'); });

$("#entityForm").validate({
	ignore:'ignore',
	rules: {
		contractId: { required: true, maxlength: 50 },
		payNo: { required: true, maxlength:50 },
		applyMoney: { required: true, checkApplyMoney: true, maxlength:50 },
		replyMoney: { checkReplyMoney: true, maxlength:50 }
	}
});