 var equipmentExinfoJsForm = {};

/**
 * 操作窗口开关
 * @param operator
 */
equipmentExinfoJsForm.operatorModal = function (operator) {
    $('#form-modal-ExInfo').modal(operator);
};

$(document).ready(function () {
    ie8StylePatch();
    $(".datepicker-input").each(function () {
        $(this).datepicker();
    });
    equipmentExinfoJsForm.operatorModal('show');
    $('#closeExInfoBtn').click(function () {
        equipmentExinfoJsForm.operatorModal('hide');
    });
});
