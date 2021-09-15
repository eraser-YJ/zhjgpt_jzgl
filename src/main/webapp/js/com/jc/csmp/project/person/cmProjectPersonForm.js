var cmProjectPersonJsForm = {};

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
cmProjectPersonJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        $("#entityForm #projectId").val(config.projectId);
        cmProjectPersonJsForm.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/project/person/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    cmProjectPersonJsForm.operatorModal('show');
                }
            }
        });
    }
    $('#entityForm #companyName').click(function () {
        new ChooseCompany.CompanyDataTable({
            renderElement: 'chooseCompanyModule',
            callback: function(data){
                $('#entityForm #companyName').val(data.name);
                $('#entityForm #companyId').val(data.id);
            }
        });
    });
};

/**
 * 验证表单
 * @returns {boolean}
 */
cmProjectPersonJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
cmProjectPersonJsForm.saveOrModify = function () {
    if (cmProjectPersonJsForm.subState) {
        return;
    }
    cmProjectPersonJsForm.subState = true;
    if (!cmProjectPersonJsForm.formValidate()) {
        cmProjectPersonJsForm.subState = false;
        return;
    }
    var url = getRootPath() + "/project/person/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action';
    jQuery.ajax({
        url : url, type : 'POST', cache: false, data :  $("#entityForm").serializeArray(),
        success : function(data) {
            cmProjectPersonJsForm.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmProjectPersonJsForm.operatorModal('hide');
                cmProjectPersonJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            cmProjectPersonJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
cmProjectPersonJsForm.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn').click(function () { cmProjectPersonJsForm.saveOrModify(); });
    $('#closeBtn').click(function () { cmProjectPersonJsForm.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
$("#entityForm").validate({
    ignore:'ignore',
    rules: {
        partakeType: { required: true, maxlength:50 },
        companyId: { validSelect2: 'companyIdFormDiv', maxlength:50 },
        leader: { required: false, maxlength: 255 },
        phone: { required: false, maxlength: 255 }
    }
});