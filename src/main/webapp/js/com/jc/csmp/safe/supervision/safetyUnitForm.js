var safetyUnitJsForm = {};

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
safetyUnitJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        safetyUnitJsForm.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/csmp/safe/supervision/get.action",
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
                    safetyUnitJsForm.operatorModal('show');
                }
            }
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
safetyUnitJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

/**
 * 保存或修改方法
 */
safetyUnitJsForm.saveOrModify = function () {
	if (safetyUnitJsForm.subState) {
	    return;
    }
	safetyUnitJsForm.subState = true;
	if (!safetyUnitJsForm.formValidate()) {
	    safetyUnitJsForm.subState = false;
	    return;
    }
	var url = getRootPath() + "/csmp/safe/supervision/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action'; 
    jQuery.ajax({
        url : url, type : 'POST', cache: false, data :  $("#entityForm").serializeArray(), 
        success : function(data) {
            safetyUnitJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                safetyUnitJsForm.operatorModal('hide');
                safetyUnitJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            safetyUnitJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
safetyUnitJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
     $('#saveBtn') .click(function () { safetyUnitJsForm.saveOrModify(); });
     $('#closeBtn') .click(function () { safetyUnitJsForm.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		projectId: { required: false, maxlength:50 },
		unitName: { required: false, maxlength:255 },
		projectLeader: { required: false, maxlength:255 },
		phone: { required: false, maxlength:255 },
		partakeType: { required: false, maxlength:50 },
    }
});