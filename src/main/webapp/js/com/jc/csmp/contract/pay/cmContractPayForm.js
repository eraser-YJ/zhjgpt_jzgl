var cmContractPayJsForm = {};

cmContractPayJsForm.attach = new AttachControl.AttachListControl({
    renderElement: 'attachTable',
    businessTable: 'cm_contract_pay',
    titleWidth: 150
});

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
cmContractPayJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    $('#saveBtn').show();
    if (config.operator == 'add') {
        cmContractPayJsForm.initAttach(0);
        cmContractPayJsForm.operatorModal('show');
    } else if (config.operator == 'modify' || config.operator == 'look') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/contract/pay/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    if (config.operator == 'modify') {
                        cmContractPayJsForm.initAttach(data.id);
                    } else if (config.operator == 'look') {
                        $('#saveBtn').hide();
                        cmContractPayJsForm.attach.initAttachOnView(data.id);
                        cmContractPayJsForm.attach.loadOpinionCtrl(data.piId, 'opinionCtrl', 'opinionTable');
                    }
                    cmContractPayJsForm.operatorModal('show');
                }
            }
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
cmContractPayJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

/**
 * 保存或修改方法
 */
cmContractPayJsForm.saveOrModify = function () {
	if (cmContractPayJsForm.subState) {
	    return;
    }
	cmContractPayJsForm.subState = true;
	if (!cmContractPayJsForm.formValidate()) {
	    cmContractPayJsForm.subState = false;
	    return;
    }
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
	var url = getRootPath() + "/contract/pay/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action';
    jQuery.ajax({
        url : url, type : 'POST', cache: false, data :  formData,
        success : function(data) {
            cmContractPayJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmContractPayJsForm.operatorModal('hide');
                cmContractInfoJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            cmContractPayJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
cmContractPayJsForm.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn') .click(function () { cmContractPayJsForm.saveOrModify(); });
    $('#closeBtn') .click(function () { cmContractPayJsForm.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
        payNo: { required: true, maxlength:50 },
        applyMoney: { required: true, common_check_positive_double_two: true, maxlength:50 },
        replyMoney: { required: true, common_check_positive_double_two: true, maxlength:50 }
    }
});