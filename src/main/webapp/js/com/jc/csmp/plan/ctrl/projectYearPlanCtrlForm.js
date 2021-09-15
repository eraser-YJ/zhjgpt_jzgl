var projectYearPlanCtrlJsForm = {};


//初始化
projectYearPlanCtrlJsForm.init = function (config) {
    $('#entityFormTitle').html(config.title);
    //年度初始化
    initSelectYear($("#entityForm #planYear"));
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        projectYearPlanCtrlJsForm.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type: "GET",
            data: {id: config.id},
            dataType: "json",
            url: getRootPath() + "/plan/ctrl/get.action",
            success: function (data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    projectYearPlanCtrlJsForm.operatorModal('show');
                }
            }
        });
    }
};

//验证表单
projectYearPlanCtrlJsForm.formValidate = function () {
    if (!$("#entityForm").valid()) {
        return false;
    }
    return true;
};

//保存或修改方法
projectYearPlanCtrlJsForm.saveOrModify = function () {
    if (projectYearPlanCtrlJsForm.subState) {
        return;
    }
    projectYearPlanCtrlJsForm.subState = true;
    if (!projectYearPlanCtrlJsForm.formValidate()) {
        projectYearPlanCtrlJsForm.subState = false;
        return;
    }
    var url = getRootPath() + "/plan/ctrl/" + ($("#id").val() != '' ? 'update' : 'save') + '.action';
    jQuery.ajax({
        url: url,
        type: 'POST',
        cache: false,
        data: $("#entityForm").serializeArray(),
        success: function (data) {
            projectYearPlanCtrlJsForm.subState = false;
            if (data.success == "true") {
                msgBox.tip({type: "success", content: data.successMessage});
                projectYearPlanCtrlJsForm.operatorModal('hide');
                projectYearPlanCtrlJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else {
                    msgBox.info({type: "fail", content: data.errorMessage});
                }
            }
        },
        error: function () {
            projectYearPlanCtrlJsForm.subState = false;
            msgBox.tip({type: "fail", content: $.i18n.prop("JC_SYS_002")});
        }
    });
};

//操作窗口开关
projectYearPlanCtrlJsForm.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function () {
    ie8StylePatch();
    $(".datepicker-input").each(function () {
        $(this).datepicker();
    });
    $('#saveBtn').click(function () {
        projectYearPlanCtrlJsForm.saveOrModify();
    });
    $('#closeBtn').click(function () {
        projectYearPlanCtrlJsForm.operatorModal('hide');
    });
});

//jquery.validate
$("#entityForm").validate({
    ignore: 'ignore',
    rules: {
        planYear: {required: true, maxlength: 10},
        requestStartDate: {required: true, maxlength: 10},
        requestEndDate: {required: true, maxlength: 10},
        seqno: {required: true, maxlength: 64},
        seqnoname: {required: false, maxlength: 200},
        extStr4: {required: true, maxlength: 200},
        remark: {required: false, maxlength: 100},
    }
});