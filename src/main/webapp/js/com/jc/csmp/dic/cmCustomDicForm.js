var cmCustomDicFormPanel = {};
cmCustomDicFormPanel.callback = null;
/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
cmCustomDicFormPanel.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    cmCustomDicFormPanel.callback = config.callback;
    if (config.operator == 'add') {
        $('#entityForm #dataType').val(config.dataType);
        $('#entityForm #parentId').val(config.parentId);
        cmCustomDicFormPanel.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/custom/dic/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    cmCustomDicFormPanel.operatorModal('show');
                }
            }
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
cmCustomDicFormPanel.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
cmCustomDicFormPanel.saveOrModify = function () {
	if (cmCustomDicFormPanel.subState) {
	    return;
    }
	cmCustomDicFormPanel.subState = true;
	if (!cmCustomDicFormPanel.formValidate()) {
	    cmCustomDicFormPanel.subState = false;
	    return;
    }
	var url = getRootPath() + "/custom/dic/" + ($("#entityForm #id").val() != '' ? 'update' : 'save') + '.action';
    jQuery.ajax({
        url : url, type : 'POST', cache: false, data : $("#entityForm").serializeArray(),
        success : function(data) {
            cmCustomDicFormPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmCustomDicFormPanel.callback(data.callbackData)
                cmCustomDicFormPanel.operatorModal('hide');
                cmCustomDicListPanel.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            cmCustomDicFormPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
cmCustomDicFormPanel.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn').click(function () { cmCustomDicFormPanel.saveOrModify(); });
    $('#closeBtn').click(function () { cmCustomDicFormPanel.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
$("#entityForm").validate({
	ignore:'ignore',
	rules: {
        code: {
            required: true,
            maxlength:255,
            remote : {
                url : getRootPath() + "/custom/dic/checkCode.action",
                type: "post", dataType: "json",
                data: {
                    code: function() {
                        return $("#entityForm #code").val();
                    },
                    id: function () {
                        return $("#entityForm #id").val();
                    },
                    dataType: function() {
                        return $('#entityForm #dataType').val();
                    }
                }
            }
        },
        name: { required: true, maxlength:255 },
        queue: { required: true },
        scope: { maxlength:500 }
    },
    messages:{
        code: {
            remote: "编码已存在"
        }
    }
});