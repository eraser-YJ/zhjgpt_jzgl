var projectVisaOrderJsForm = {};

projectVisaOrderJsForm.attach = new AttachControl.AttachListControl({
    renderElement: 'attachTable',
    businessTable: 'cm_project_visa_order',
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
projectVisaOrderJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $.ajax({
        type : "GET", data : {id: config.id}, dataType : "json",
        url : getRootPath() + "/project/visa/get.action",
        success : function(data) {
            if (data) {
                hideErrorMessage();
                $("#entityForm").fill(data);
                projectVisaOrderJsForm.attach.initAttachOnView(data.id);
                projectVisaOrderJsForm.attach.loadOpinionCtrl(data.piId, 'opinionCtrl', 'opinionTable');
                visaPage.init({data: data, btnId: 'printBtn'});
                if (data.auditStatus != 'finish') { $('#fileBtn').hide(); }
                projectVisaOrderJsForm.operatorModal('show');
            }
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
projectVisaOrderJsForm.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#closeBtn') .click(function () { projectVisaOrderJsForm.operatorModal('hide'); });
});