var cmProjectPlanTemplateStageFormPanel = {};
cmProjectPlanTemplateStageFormPanel.subState = false;
cmProjectPlanTemplateStageFormPanel.callback = null;

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
cmProjectPlanTemplateStageFormPanel.init = function(config) {
    $('#entityTaskFormTitle').html(config.title);
    $("#entityTaskForm #id").val('');
    cmProjectPlanTemplateStageFormPanel.callback = config.callback;
    if (config.operator == 'add') {
        $("#entityTaskForm #templateId").val(config.templateId);
        $("#entityTaskForm #stageId").val(config.stageId);
        cmProjectPlanTemplateStageFormPanel.operatorModal('show');
    } else {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/project/plan/templatetask/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityTaskForm").fill(data);
                    cmProjectPlanTemplateStageFormPanel.operatorModal('show');
                }
            }
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
cmProjectPlanTemplateStageFormPanel.formValidate = function() {
    if (!$("#entityTaskForm").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
cmProjectPlanTemplateStageFormPanel.saveOrModify = function () {
	if (cmProjectPlanTemplateStageFormPanel.subState) {
	    return;
    }
	cmProjectPlanTemplateStageFormPanel.subState = true;
	if (!cmProjectPlanTemplateStageFormPanel.formValidate()) {
	    cmProjectPlanTemplateStageFormPanel.subState = false;
	    return;
    }
    var formData = $("#entityTaskForm").serializeArray();
    var url = getRootPath() + "/project/plan/templatetask/" + ($("#id").val() != '' ? 'update' : 'save') + ".action";
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: formData,
        success : function(data) {
            cmProjectPlanTemplateStageFormPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmProjectPlanTemplateStageFormPanel.operatorModal('hide');
                if (cmProjectPlanTemplateStageFormPanel != undefined && cmProjectPlanTemplateStageFormPanel != null) {
                    cmProjectPlanTemplateStageFormPanel.callback();
                }
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityTaskForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            cmProjectPlanTemplateStageFormPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
cmProjectPlanTemplateStageFormPanel.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn').click(function () { cmProjectPlanTemplateStageFormPanel.saveOrModify(); });
    $('#closeBtn').click(function () { cmProjectPlanTemplateStageFormPanel.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
$("#entityTaskForm").validate({
	ignore:'ignore',
	rules: {
        taskName: { required: true, maxlength:255 },
        taskNumber: { required: true, maxlength:50 },
        planDay: { maxlength:11, number: true },
        planWorkDay: { maxlength:11, number: true },
        selfWeight: { maxlength:16, number: true },
        queue: { required: true, maxlength:11, number: true }
    },
    messages:{
        planDay: { number: "请输入有效的数字" },
        planWorkDay: { number: "请输入有效的数字" },
        selfWeight: { number: "请输入有效的数字" },
        queue: { number: "请输入有效的数字" },
    }
});