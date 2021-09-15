var projectQuestionModifyPanel = {};
projectQuestionModifyPanel.subState = false;

projectQuestionModifyPanel.attach = new AttachControl.AttachListControl({
    renderElement: 'attachTable',
    businessTable: $('#paramBusinessTable').val(),
    titleWidth: 100
});

/**
 * 加载问题处置部门
 * @param projectId: 项目id
 * @param contractId
 */
projectQuestionModifyPanel.loadProblemDept = function(projectId, problemDept) {
    var problemDeptObj = $('#entityForm #problemDept');
    problemDeptObj.empty();
    problemDeptObj.append("<option value=''>-请选择-</option>");
    $.ajax({
        type: "GET", data: {projectId: projectId}, dataType: "json", async: false,
        url: getRootPath() + "/common/api/getSuperviseChildDeptByProjectId.action",
        success : function(data) {
            if (data != null && data.length > 0) {
                for (var i = 0; i < data.length; i++) {
                    problemDeptObj.append("<option value='" + data[i].id + "'>" + data[i].name + "</option>");
                }
            }
            if (problemDept != null) {
                problemDeptObj.val(problemDept);
            }
        }
    });
};

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
projectQuestionModifyPanel.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $.ajax({
        type : "GET", data : {id: config.id}, dataType : "json",
        url : getRootPath() + "/project/question/get.action",
        success : function(data) {
            if (data) {
                hideErrorMessage();
                $("#entityForm").fill(data);
                projectQuestionModifyPanel.loadProblemDept(data.projectId, data.problemDept);
                projectQuestionModifyPanel.attach.initAttach(data.id);
                projectQuestionModifyPanel.attach.loadOpinionCtrl(data.piId, 'opinionCtrl', 'opinionTable');
                projectQuestionModifyPanel.operatorModal('show');
            }
        }
    });
};

projectQuestionModifyPanel.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    }
    return true;
};

projectQuestionModifyPanel.saveOrModify = function() {
    if (projectQuestionModifyPanel.subState) {
        return;
    }
    projectQuestionModifyPanel.subState = true;
    if (!projectQuestionModifyPanel.formValidate()) {
        projectQuestionModifyPanel.subState = false;
        return;
    }
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
    var url = getRootPath() + "/project/question/update.action";
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: formData,
        success : function(data) {
            projectQuestionModifyPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                projectQuestionModifyPanel.operatorModal('hide');
                projectQuestionListPanel.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            projectQuestionModifyPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
projectQuestionModifyPanel.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#closeBtn') .click(function () { projectQuestionModifyPanel.operatorModal('hide'); });
    $('#saveBtn').click(function () { projectQuestionModifyPanel.saveOrModify(); });
});

/**
 * jquery.validate
 */
$("#entityForm").validate({
    ignore:'ignore',
    rules: {
        code: { required: true, maxlength:255 },
        projectId: { required: true, maxlength:50 },
        contractId: { required: true, maxlength:50 },
        title: { required: true, maxlength:500 },
        rectificationAsk: { required: false, maxlength:500 },
        rectificationResult: { required: false, maxlength:1000 },
        remark: { required: false, maxlength:16383 },
    }
});