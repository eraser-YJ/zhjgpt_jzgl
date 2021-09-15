var warnResultJsForm = {};

warnResultJsForm.init = function (config) {
    $('#entityWarnResultFormTitle').html(config.title);
    $("#entityWarnResultForm #id").val(config.id);
    warnResultJsForm.operatorModal('show');
};
warnResultJsForm.view = function (config) {
    $('#entityWarnResultFormTitle').html(config.title);
    $("#entityWarnResultForm #id").val(config.id);
    $("#entityWarnResultForm #processResult").attr("disabled",true);
    $("#saveWarnResultBtn").hide();
    $.ajax({
        type : "GET", data : {id: config.id}, dataType : "json",
        url : getRootPath() + "/warn/info/get.action",
        success : function(data) {
            if (data) {
                hideErrorMessage();
                $("#entityWarnResultForm").fill(data);
            }
            warnResultJsForm.operatorModal('show');
        }
    });

};

warnResultJsForm.formValidate = function () {
    if (!$("#entityWarnResultForm").valid()) {
        return false;
    }
    return true;
};


warnResultJsForm.saveOrModify = function () {
    if (warnResultJsForm.subState) {
        return;
    }
    warnResultJsForm.subState = true;
    if (!warnResultJsForm.formValidate()) {
        warnResultJsForm.subState = false;
        return;
    }
    var url = getRootPath() + "/warn/info/updateResult.action";
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: $("#entityWarnResultForm").serializeArray(),
        success: function (data) {
            warnResultJsForm.subState = false;
            if (data.success == "true") {
                msgBox.tip({type: "success", content: data.successMessage});
                warnResultJsForm.operatorModal('hide');
                if(warnInfoJsList){
                    warnInfoJsList.renderTable();
                }
                if(warnInfoJsList){
                    warnInfoJsList.renderTable();
                }
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityWarnResultForm", data.labelErrorMessage);
                } else {
                    msgBox.info({type: "fail", content: data.errorMessage});
                }
            }
        },
        error: function () {
            warnResultJsForm.subState = false;
            msgBox.tip({type: "fail", content: $.i18n.prop("JC_SYS_002")});
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
warnResultJsForm.operatorModal = function (operator) {
    $('#form-modal-WarnResult').modal(operator);
};

$(document).ready(function () {
    ie8StylePatch();
    $(".datepicker-input").each(function () {
        $(this).datepicker();
    });
    $('#saveWarnResultBtn').click(function () {
        warnResultJsForm.saveOrModify();
    });
    $('#closeWarnResultBtn').click(function () {
        warnResultJsForm.operatorModal('hide');
    });
});

/**
 * jquery.validate
 */
$("#entityWarnResultForm").validate({
    ignore: 'ignore',
    rules: {
        processResult: {required: true, maxlength: 500}
    }
});