var cmCompanyFormPanel = {};

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
cmCompanyFormPanel.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        cmCompanyFormPanel.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/company/info/get.action",
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
                    cmCompanyFormPanel.operatorModal('show');
                }
            }
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
cmCompanyFormPanel.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    }
    var companyTypeArray = [];
    $("[name='companyTypeCheckbox']:checked").each(function () {
        companyTypeArray.push($(this).val());
    });
    if (companyTypeArray.length == 0) {
        msgBox.tip({ type:"fail", content: "请选择企业类型" });
        return false;
    }
    $('#entityForm #companyType').val(companyTypeArray.join(','));
    return true;
};

/**
 * 保存或修改方法
 */
cmCompanyFormPanel.saveOrModify = function () {
	if (cmCompanyFormPanel.subState) {
	    return;
    }
	cmCompanyFormPanel.subState = true;
	if (!cmCompanyFormPanel.formValidate()) {
	    cmCompanyFormPanel.subState = false;
	    return;
    }
	var url = getRootPath() + "/company/info/" + ($("#id").val() != '' ? 'update' : 'save') + '.action';
    jQuery.ajax({
        url : url, type : 'POST', cache: false, data : $("#entityForm").serializeArray(),
        success : function(data) {
            cmCompanyFormPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmCompanyFormPanel.operatorModal('hide');
                cmCompanyListPanel.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            cmCompanyFormPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
cmCompanyFormPanel.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn').click(function () { cmCompanyFormPanel.saveOrModify(); });
    $('#closeBtn').click(function () { cmCompanyFormPanel.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
$("#entityForm").validate({
	ignore:'ignore',
	rules: {
        companyName: { required: true, maxlength:255 },
        creditCode: {
            required: true,
            maxlength:255,
            remote : {
                url : getRootPath() + "/company/info/checkCreditCode.action",
                type: "post", dataType: "json",
                data: {
                    creditCode: function() {
                        return $("#entityForm #creditCode").val();
                    },
                    id: function () {
                        return $("#entityForm #id").val();
                    }
                }
            }
        },
        legalPerson: { required: true, maxlength:255 },
        legalPhone: { required: true, maxlength:255 },
        liaisonMan: { required: true, maxlength:255 },
        liaisonPhone: { required: true, maxlength:255 },
        proxyMan: { maxlength:255 },
        proxyPhone: { maxlength:255 },
        zipcode: { maxlength:6, minlength: 6 },
        companyAddress: { maxlength:255 }
    },
    messages:{
        creditCode: {
            remote: "统一社会信用代码已存在"
        }
    }
});