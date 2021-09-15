var cmProjectPlanStageFormPanel = {};
cmProjectPlanStageFormPanel.subState = false;
cmProjectPlanStageFormPanel.callback = null;

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
cmProjectPlanStageFormPanel.init = function(config) {
    $('#entityStageFormTitle').html(config.title);
    $("#entityStageForm #id").val('');
    $('#parentNameTr').show();
    cmProjectPlanStageFormPanel.callback = config.callback;
    if (config.operator == 'add') {
        $('#entityStageForm #parentId').val(config.parentId);
        $('#entityStageForm #parentName').val(config.parentName);
        $('#entityStageForm #projectId').val(config.projectId);
        cmProjectPlanStageFormPanel.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/project/plan/stage/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityStageForm").fill(data);
                    $('#parentNameTr').hide();
                    cmProjectPlanStageFormPanel.operatorModal('show');
                }
            }
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
cmProjectPlanStageFormPanel.formValidate = function() {
    if (!$("#entityStageForm").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
cmProjectPlanStageFormPanel.saveOrModify = function () {
	if (cmProjectPlanStageFormPanel.subState) {
	    return;
    }
	cmProjectPlanStageFormPanel.subState = true;
	if (!cmProjectPlanStageFormPanel.formValidate()) {
	    cmProjectPlanStageFormPanel.subState = false;
	    return;
    }
    var formData = $("#entityStageForm").serializeArray();
    var url = getRootPath() + "/project/plan/stage/" + ($("#entityStageForm #id").val() != '' ? 'update' : 'save') + ".action";
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: formData,
        success : function(data) {
            cmProjectPlanStageFormPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmProjectPlanStageFormPanel.operatorModal('hide');
                if (cmProjectPlanStageFormPanel.callback != null && cmProjectPlanStageFormPanel.callback != undefined) {
                    cmProjectPlanStageFormPanel.callback(data.callbackData);
                }
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityStageForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            cmProjectPlanStageFormPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
cmProjectPlanStageFormPanel.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn').click(function () { cmProjectPlanStageFormPanel.saveOrModify(); });
    $('#closeBtn').click(function () { cmProjectPlanStageFormPanel.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
$("#entityStageForm").validate({
	ignore:'ignore',
	rules: {
        stageName: { required: true, maxlength:255 },
        queue: { required: true, maxlength:11, number: true }
    },
    messages: {
        queue: {number: "请输入有效的数字"}
    }
});