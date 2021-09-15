var projectInfoJsForm = {};

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
projectInfoJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        projectInfoJsForm.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/doc/project/get.action",
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
                    projectInfoJsForm.operatorModal('show');
                }
            }
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
projectInfoJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

/**
 * 保存或修改方法
 */
projectInfoJsForm.saveOrModify = function () {
	if (projectInfoJsForm.subState) {
	    return;
    }
	projectInfoJsForm.subState = true;
	if (!projectInfoJsForm.formValidate()) {
	    projectInfoJsForm.subState = false;
	    return;
    }
	var url = getRootPath() + "/doc/project/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action'; 
    jQuery.ajax({
        url : url, type : 'POST', cache: false, data :  $("#entityForm").serializeArray(), 
        success : function(data) {
            projectInfoJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                projectInfoJsForm.operatorModal('hide');
                projectInfoJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            projectInfoJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
projectInfoJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
     $('#saveBtn') .click(function () { projectInfoJsForm.saveOrModify(); });
     $('#closeBtn') .click(function () { projectInfoJsForm.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		projectNumber: { required: false, maxlength:255 },
		projectName: { required: false, maxlength:255 },
		projectAddress: { required: false, maxlength:255 },
		projectTrade: { required: false, maxlength:50 },
		planAmount: { required: false, maxlength:14 },
		buildArea: { required: false, maxlength:14 },
		landNature: { required: false, maxlength:50 },
		landArea: { required: false, maxlength:14 },
		region: { required: false, maxlength:50 },
		investmentAmount: { required: false, maxlength:14 },
		planStartDate: { required: false, maxlength:19 },
		planEndDate: { required: false, maxlength:19 },
		projectContent: { required: false, maxlength:16383 },
		remark: { required: false, maxlength:16383 },
		superviseDeptId: { required: false, maxlength:50 },
		buildDeptId: { required: false, maxlength:50 },
    }
});