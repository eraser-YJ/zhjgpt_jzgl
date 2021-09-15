var warnViewJsForm = {};


/**
 * 操作窗口开关
 * @param operator
 */
warnViewJsForm.operatorModal = function (operator) {
    $('#form-modal-WarnView').modal(operator);
};

$(document).ready(function () {
    ie8StylePatch();
    $(".datepicker-input").each(function () {
        $(this).datepicker();
    });
    warnViewJsForm.operatorModal('show');
    $('#closeWarnViewBtn').click(function () {
        warnViewJsForm.operatorModal('hide');
    });
});
 