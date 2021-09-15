var projectQuestionFormPanel = {};
projectQuestionFormPanel.attach = new AttachControl.AttachListControl({
    renderElement: 'attachTable',
    businessTable: 'cm_project_question',
    titleWidth: 100
});

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
projectQuestionFormPanel.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $.ajax({
        type : "GET", data : {id: config.id}, dataType : "json",
        url : getRootPath() + "/project/question/get.action",
        success : function(data) {
            if (data) {
                hideErrorMessage();
                $("#entityForm").fill(data);
                projectQuestionFormPanel.attach.initAttachOnView(data.id);
                projectQuestionFormPanel.operatorModal('show');
            }
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
projectQuestionFormPanel.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#closeBtn') .click(function () { projectQuestionFormPanel.operatorModal('hide'); });
});