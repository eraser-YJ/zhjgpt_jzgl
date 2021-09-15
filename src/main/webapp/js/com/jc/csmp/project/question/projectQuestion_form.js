var projectQuestionJsForm = {};
projectQuestionJsForm.subState = false;

projectQuestionJsForm.saveOrUpdate = "save";

projectQuestionJsForm.attach = new AttachControl.AttachListControl({
    renderElement: 'attachTable',
    itemName: 'uploadAttachContainer',
    businessTable: $('#entityForm #attchBusinessTable').val()
});

function insert(type, jumpFun) {
    if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1' && !projectQuestionJsForm.attach.validate()) {
        projectQuestionJsForm.subState = false;
        return;
    }
    if ($('#entityForm #partaUnitLeaderId').val() == "") {
        msgBox.info({ type:"fail", content: "该项目建设单位未设置负责人，无法提交申请单" });
        return;
    }
    if ($("#entityForm #superviseLeaderId").val() == '' && $('#entityForm #questionType').val() == 'scene') {
        msgBox.info({ type:"fail", content: "该项目未设置监管负责人，无法提交申请单" });
        return;
    }
    if(projectQuestionJsForm.subState)  return;
        projectQuestionJsForm.subState = true;
    $('#workflowButtonId1').hide();
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getAllFiles()});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
    jQuery.ajax({
        url: getRootPath() + "/project/question/saveWorkflow.action", type: 'POST', cache: false, data: formData,
        success : function(data) {
            projectQuestionJsForm.subState = false;
            if(data.success == "true"){
                setTimeout(function() {msgBox.tip({ type: "success", content: data.successMessage, callback: projectQuestionJsForm.gotoTodo });}, 500);
                $("#token").val(data.token);
            } else {
                if(data.labelErrorMessage){
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
                $("#token").val(data.token);
                $('#workflowButtonId1').show();
            }
        },
        error : function() {
            projectQuestionJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
            $('#workflowButtonId1').show();
        }
    });
}

function update(type, jumpFun) {
    if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1' && !projectQuestionJsForm.attach.validate()) {
        projectQuestionJsForm.subState = false;
        return;
    }
    if ($('#entityForm #partaUnitLeaderId').val() == "") {
        msgBox.info({ type:"fail", content: "该项目建设单位未设置负责人，无法提交申请单" });
        return;
    }
    if ($("#entityForm #superviseLeaderId").val() == '' && $('#entityForm #questionType').val() == 'scene') {
        return;
    }
    if(projectQuestionJsForm.subState)  return;
        projectQuestionJsForm.subState = true;
    $('#workflowButtonId1').hide();
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getAllFiles()});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
    jQuery.ajax({
        url: getRootPath() + "/project/question/updateWorkflow.action", async: false, type: 'POST', data: formData,
        success : function(data) {
            projectQuestionJsForm.saveOrUpdate = "update";
            projectQuestionJsForm.subState = false;
            if(data.success == "true"){
                setTimeout(function() {msgBox.tip({ type: "success", content: data.successMessage, callback: projectQuestionJsForm.gotoTodo });}, 500);
                $("#token").val(data.token);
            } else {
                if(data.labelErrorMessage){
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type: "fail", content: data.errorMessage });
                }
                $('#workflowButtonId1').show();
            }
        },
        error : function() {
            projectQuestionJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
            $('#workflowButtonId1').show();
        }
    });
}

function validateForm(){
    return $("#entityForm").valid();
}

function validateFormFail(){
    projectQuestionJsForm.subState = false;
    msgBox.info({ content : $.i18n.prop("JC_SYS_067"), });
}

projectQuestionJsForm.gotoTodo = function(){
    if (projectQuestionJsForm.saveOrUpdate = "save") {
        JCFF.loadPage({url: "/project/question/search.action?questionType=" + $('#entityForm #questionType').val() + "&fn=myApply"});
    } else {
        JCFF.loadPage({url: "/project/question/todoList.action?questionType=" + $('#entityForm #questionType').val()});
    }
};

/**
 * 加载合同
 * @param projectId: 项目
 * @param contractId: 合同
 */
projectQuestionJsForm.loadContract = function(projectId, contractId) {
    var contractObj = $('#entityForm #contractId');
    contractObj.empty();
    contractObj.append("<option value=''>-请选择-</option>");
    $.ajax({
        type: "GET", data: {projectId: projectId}, dataType: "json", async: false,
        url: getRootPath() + "/contract/info/contractList.action",
        success : function(data) {
            if (data != null && data.length > 0) {
                for (var i = 0; i < data.length; i++) {
                    contractObj.append("<option value='" + data[i].id + "'>" + data[i].contractName + "</option>");
                }
            }
            if (contractId != null) {
                contractObj.val(contractId);
            }
        }
    });
};

/**
 * 加载问题处置部门
 * @param projectId: 项目id
 * @param contractId
 */
projectQuestionJsForm.loadProblemDept = function(projectId, problemDept) {
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

projectQuestionJsForm.contractChange = function() {
    var contractId = $('#entityForm #contractId').val();
    if (contractId == '') {
        $('#entityForm #partaUnitLeaderId').val("");
        return;
    }
    $.ajax({
        type: "GET", data: {id: contractId}, dataType: "json", async: false,
        url: getRootPath() + "/contract/info/getADeptLeaderByContractId.action",
        success : function(data) {
            $('#entityForm #partaUnitLeaderId').val("");
            $('#entityForm #buildDeptPersion').val("");
            $('#entityForm #buildDeptPersionMobile').val("");
            if (data.code != 0) {
                msgBox.info({ type:"fail", content: "该合同发包单位未设置负责人，无法提交申请单" });
                return;
            }
            $('#entityForm #partaUnitLeaderId').val(data.data.partyaUnitLeaderId);
            $('#buildDeptTrContainer').show();
            $('#entityForm #buildDeptPersion').val(data.data.leaderName);
            $('#entityForm #buildDeptPersionMobile').val(data.data.leaderMobile);
        }
    });
};

projectQuestionJsForm.problemDeptChange = function() {
    var problemDept = $('#entityForm #problemDept').val();
    if (problemDept != null) {
        $.ajax({
            type: "GET", data: {id: problemDept}, dataType: "json", async: false,
            url: getRootPath() + "/department/get.action",
            success : function(data) {
                if (data != null) {
                    if (data.leaderId == null || data.leaderId == '') {
                        msgBox.info({ type:"fail", content: "处置负责人下未设置相关部门负责人，无法进行审批" });
                        return;
                    }
                    $("#entityForm #superviseLeaderId").val(data.leaderId);
                }
            }
        });
    }
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#entityForm #contractId').change(projectQuestionJsForm.contractChange);
    $('#entityForm #problemDept').change(projectQuestionJsForm.problemDeptChange);
    var businessJson = $("#businessJson").val();
    if (businessJson.length > 0) {
        var businessObj = eval("("+businessJson+")");
        $("#entityForm").fill(businessObj);
        $('#entityForm #superviseLeaderId').val(businessObj.superviseLeaderId);
        $('#entityForm #partaUnitLeaderId').val(businessObj.partaUnitLeaderId);
        $('#rectificationCompanyHtml').html(businessObj.rectificationCompanyValue);
        projectQuestionJsForm.loadContract(businessObj.projectId, businessObj.contractId);
        projectQuestionJsForm.loadProblemDept(businessObj.projectId, businessObj.problemDept);
        if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1') {
            projectQuestionJsForm.attach.initAttach($('#entityForm #id').val());
        } else {
            projectQuestionJsForm.attach.initAttachOnView($('#entityForm #id').val());
        }
    } else {
        projectQuestionJsForm.attach.initAttach(0);
        $('#entityForm #projectName').click(function() {
            chooseProjectPanel.init(function(data) {
                $('#entityForm #partaUnitLeaderId').val("");
                //选择项目名后回调函数
                $('#entityForm #projectId').val(data.id);
                $('#entityForm #projectName').val(data.projectName);
                //$('#entityForm #superviseLeaderId').val(data.superviseDeptIdLeaderId);
                projectQuestionJsForm.loadContract(data.id, null);
                projectQuestionJsForm.loadProblemDept(data.id, null);
            });
        });
    }
    var questionType  =$('#entityForm #questionType').val();
    if (questionType == 'quality') {
        $('#safeFailureTypeContrainer').val(1);
        $('#safeFailureTypeTitle').html("");
        $('#sectionTitleOne').html("质量问题管理 > ");
        $('#sectionTitleTwo').html("质量问题登记");
    } else if (questionType == "scene") {
        $('#sectionTitleOne').html("现场问题管理 > ");
        $('#sectionTitleTwo').html("现场问题登记");
    } else if (questionType == "safe") {
        $('#safeFailureTypeContrainer').val(1);
        $('#safeFailureTypeTitle').html("");
        $('#sectionTitleOne').html("安全问题管理 > ");
        $('#sectionTitleTwo').html("安全问题登记");
    }
    formPriv.doForm();
});

/**
 * jquery.validate
 */
$("#entityForm").validate({
    ignore:'ignore',
    rules: {
        code: { required: true, maxlength:255 },
        projectId: { required: true, maxlength:50 },
        questionMeta: { required: true, maxlength: 60000},
        checkResult: { required: true, maxlength: 60000},
        contractId: { required: true, maxlength:50 },
        title: { required: true, maxlength:500 },
        rectificationAsk: { required: false, maxlength:60000 },
        rectificationResult: { required: false, maxlength:60000 },
        remark: { required: false, maxlength:16383 },
        safeFailureTypeContrainer: { required: true }
    }
});
