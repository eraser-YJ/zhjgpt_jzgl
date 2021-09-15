var cmProjectPlanTemplateFormPanel = {};
cmProjectPlanTemplateFormPanel.subState = false;

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
cmProjectPlanTemplateFormPanel.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    cmProjectPlanTemplateFormPanel.operatorModal('show');
};

/**
 * 验证表单
 * @returns {boolean}
 */
cmProjectPlanTemplateFormPanel.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
cmProjectPlanTemplateFormPanel.saveOrModify = function () {
	if (cmProjectPlanTemplateFormPanel.subState) {
	    return;
    }
	cmProjectPlanTemplateFormPanel.subState = true;
	if (!cmProjectPlanTemplateFormPanel.formValidate()) {
	    cmProjectPlanTemplateFormPanel.subState = false;
	    return;
    }
    var formData = $("#entityForm").serializeArray();
    var url = getRootPath() + "/project/plan/template/save.action";
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: formData,
        success : function(data) {
            cmProjectPlanTemplateFormPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmProjectPlanTemplateFormPanel.operatorModal('hide');
                cmProjectPlanTemplateListPanel.saveSuccess(data.callbackData);
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            cmProjectPlanTemplateFormPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
cmProjectPlanTemplateFormPanel.operatorModal = function (operator) {
    $('#entityFormform-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn').click(function () { cmProjectPlanTemplateFormPanel.saveOrModify(); });
    $('#closeBtn').click(function () { cmProjectPlanTemplateFormPanel.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
$("#entityForm").validate({
	ignore:'ignore',
	rules: {
        templateName: { required: true, maxlength:255 }
    }
});