var projectChangeOrderJsForm = {};

projectChangeOrderJsForm.attach = new AttachControl.AttachListControl({
    renderElement: 'attachTable',
    businessTable: 'cm_project_change_order',
    titleWidth: 150
});

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
projectChangeOrderJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $.ajax({
        type : "GET", data : {id: config.id}, dataType : "json",
        url : getRootPath() + "/project/change/get.action",
        success : function(data) {
            if (data) {
                hideErrorMessage();
                $("#entityForm").fill(data);
                projectChangeOrderJsForm.attach.initAttachOnView(data.id);
                projectChangeOrderJsForm.attach.loadOpinionCtrl(data.piId, 'opinionCtrl', 'opinionTable');
                if (data.changeType == "1") {
                    projectChangePage.init({data: data, btnId: 'printBtn'});
                } else {
                    contractChangePage.init({data: data, btnId: 'printBtn'});
                }
                if (data.auditStatus != 'finish') { $('#fileBtn').hide(); }
                projectChangeOrderJsForm.operatorModal('show');
            }
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
projectChangeOrderJsForm.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#closeBtn') .click(function () { projectChangeOrderJsForm.operatorModal('hide'); });
});