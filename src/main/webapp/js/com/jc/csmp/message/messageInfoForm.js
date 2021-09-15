var messageInfoFormPanel = {};

messageInfoFormPanel.subState = false;

messageInfoFormPanel.init = function(config) {
    $.ajax({
        type: "GET", data: {id: config.id}, dataType : "json", url: getRootPath() + "/message/info/look.action",
        success : function(data) {
            if (data) {
                hideErrorMessage();
                $("#entityForm").fill(data);
                messageInfoFormPanel.operatorModal('show');
            }
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
messageInfoFormPanel.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#closeBtn') .click(function () { messageInfoFormPanel.operatorModal('hide'); });
});