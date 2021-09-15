var warnInfoJsForm = {};

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
warnInfoJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        warnInfoJsForm.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/warn/info/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    warnInfoJsForm.operatorModal('show');
                }
            }
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
warnInfoJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

/**
 * 保存或修改方法
 */
warnInfoJsForm.saveOrModify = function () {
	if (warnInfoJsForm.subState) {
	    return;
    }
	warnInfoJsForm.subState = true;
	if (!warnInfoJsForm.formValidate()) {
	    warnInfoJsForm.subState = false;
	    return;
    }
	var url = getRootPath() + "/warn/info/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action'; 
    jQuery.ajax({
        url : url, type : 'POST', cache: false, data :  $("#entityForm").serializeArray(), 
        success : function(data) {
            warnInfoJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                warnInfoJsForm.operatorModal('hide');
                warnInfoJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            warnInfoJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
warnInfoJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
     $('#saveBtn') .click(function () { warnInfoJsForm.saveOrModify(); });
     $('#closeBtn') .click(function () { warnInfoJsForm.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		targetType: { required: false, maxlength:64 },
		targetId: { required: false, maxlength:64 },
		targetCode: { required: false, maxlength:64 },
		warnTime: { required: false, maxlength:64 },
		warnReasonCode: { required: false, maxlength:64 },
		warnReason: { required: false, maxlength:64 },
		num01: { required: false, maxlength:22 },
		num02: { required: false, maxlength:22 },
		num03: { required: false, maxlength:22 },
		num04: { required: false, maxlength:22 },
		num05: { required: false, maxlength:22 },
		num06: { required: false, maxlength:22 },
		num07: { required: false, maxlength:22 },
		num08: { required: false, maxlength:22 },
		num09: { required: false, maxlength:22 },
		num10: { required: false, maxlength:22 },
		num11: { required: false, maxlength:22 },
		num12: { required: false, maxlength:22 },
		num13: { required: false, maxlength:22 },
		num14: { required: false, maxlength:22 },
		num15: { required: false, maxlength:22 },
		num16: { required: false, maxlength:22 },
    }
});