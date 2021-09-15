var warnRuleJsForm = {};

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
warnRuleJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        warnRuleJsForm.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/warn/rule/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    var companyType = ',' + data.companyType + ',';
                    $("[name='companyTypeCheckbox']").each(function () {
                        if (companyType.indexOf(',' + $(this).val() + ',') > -1) {
                            this.checked = true;
                        }
                    });
                    warnRuleJsForm.operatorModal('show');
                }
            }
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
warnRuleJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

/**
 * 保存或修改方法
 */
warnRuleJsForm.saveOrModify = function () {
	if (warnRuleJsForm.subState) {
	    return;
    }
	warnRuleJsForm.subState = true;
	if (!warnRuleJsForm.formValidate()) {
	    warnRuleJsForm.subState = false;
	    return;
    }
	var url = getRootPath() + "/warn/rule/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action'; 
    jQuery.ajax({
        url : url, type : 'POST', cache: false, data :  $("#entityForm").serializeArray(), 
        success : function(data) {
            warnRuleJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                warnRuleJsForm.operatorModal('hide');
                warnRuleJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            warnRuleJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
warnRuleJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
     $('#saveBtn') .click(function () { warnRuleJsForm.saveOrModify(); });
     $('#closeBtn') .click(function () { warnRuleJsForm.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		ruleCode: { required: true, maxlength:64 },
		ruleName: { required: true, maxlength:100 },
		ruleTxt: { required: true},
    }
});