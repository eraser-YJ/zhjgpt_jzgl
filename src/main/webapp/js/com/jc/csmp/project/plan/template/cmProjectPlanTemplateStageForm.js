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
    $('#entityStageFormTitle').html(config.title);
    $("#entityStageForm #id").val('');
    $('#parentNameTr').show();
    cmProjectPlanTemplateStageFormPanel.callback = config.callback;
    if (config.operator == 'add') {
        $('#entityStageForm #parentId').val(config.parentId);
        $('#entityStageForm #parentName').val(config.parentName);
        $('#entityStageForm #templateId').val(config.templateId);
        cmProjectPlanTemplateStageFormPanel.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/project/plan/templatestage/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityStageForm").fill(data);
                    $('#parentNameTr').hide();
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
    if (!$("#entityStageForm").valid()) {
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
    var formData = $("#entityStageForm").serializeArray();
    var url = getRootPath() + "/project/plan/templatestage/" + ($("#id").val() != '' ? 'update' : 'save') + ".action";
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: formData,
        success : function(data) {
            cmProjectPlanTemplateStageFormPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmProjectPlanTemplateStageFormPanel.operatorModal('hide');
                if (cmProjectPlanTemplateStageFormPanel.callback != null && cmProjectPlanTemplateStageFormPanel.callback != undefined) {
                    cmProjectPlanTemplateStageFormPanel.callback(data.callbackData);
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

cmProjectPlanTemplateStageFormPanel.tree = function(){
    function onBeforeClick(id, node){
        if(node.isChecked == 0 && node.pId != null){
            return false;
        }else{
            return true;
        }
    }

    function onClick(event, treeId, treeNode) {
        $("#entityStageForm #parentId").val(treeNode.id);
    }

    JCTree.ztree({
        container : 'treeDemoForm',
        expand : true,
        rootNode : true,
        url: getRootPath() + "/project/plan/templatestage/treeList.action?templateId=" + $('#entityStageForm #templateId').val(),
        onClick: onClick,
        onBeforeClick : onBeforeClick,
    });
};

cmProjectPlanTemplateStageFormPanel.move = function() {
    if ($("#entityStageForm #parentId").val() == $("#entityStageForm #id").val()) {
        msgBox.tip({ type: "fail", content: "无法移动到本身节点" });
        return;
    }
    jQuery.ajax({
        url: getRootPath() + "/project/plan/templatestage/move.action", type: 'POST', cache: false,
        data: {id: $("#entityStageForm #id").val(), parentId: $("#entityStageForm #parentId").val()},
        success : function(data) {
            cmProjectPlanTemplateStageFormPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmProjectPlanTemplateStageFormPanel.operatorModal('hide');
                if (cmProjectPlanTemplateStageFormPanel.callback != null && cmProjectPlanTemplateStageFormPanel.callback != undefined) {
                    cmProjectPlanTemplateStageFormPanel.callback();
                }
            } else {
                msgBox.info({ type:"fail", content: data.errorMessage });
            }
        },
        error : function() {
            cmProjectPlanTemplateStageFormPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
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