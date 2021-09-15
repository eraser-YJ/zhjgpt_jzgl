var projectVisaOrderJsForm = {};
projectVisaOrderJsForm.subState = false;

projectVisaOrderJsForm.saveOrUpdate = "save";

function insert(type, jumpFun) {
    if ($('#entityForm #partaUnitLeaderId').val() == "") {
        msgBox.info({ type:"fail", content: "该项目建设单位未设置负责人，无法提交申请单" });
        return;
    }
    if(projectVisaOrderJsForm.subState)  return;
        projectVisaOrderJsForm.subState = true;
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
    jQuery.ajax({
        url: getRootPath() + "/project/visa/saveWorkflow.action", type: 'POST', cache: false, data: formData,
        success : function(data) {
            projectVisaOrderJsForm.subState = false;
            if(data.success == "true"){
                setTimeout(function() {msgBox.tip({ type: "success", content: data.successMessage, callback: projectVisaOrderJsForm.gotoTodo });}, 500);
                $("#token").val(data.token);
            } else {
                if(data.labelErrorMessage){
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({
                        type:"fail",
                        content: data.errorMessage
                    });
                }
                $("#token").val(data.token);
            }
        },
        error : function() {
            projectVisaOrderJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
}

function update(type, jumpFun) {
    if(projectVisaOrderJsForm.subState)  return;
        projectVisaOrderJsForm.subState = true;
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
    jQuery.ajax({
        url: getRootPath() + "/project/visa/updateWorkflow.action", async: false, type: 'POST', data: formData,
        success : function(data) {
            projectVisaOrderJsForm.saveOrUpdate = "update";
            projectVisaOrderJsForm.subState = false;
            if(data.success == "true"){
                setTimeout(function() {msgBox.tip({ type: "success", content: data.successMessage, callback: projectVisaOrderJsForm.gotoTodo });}, 500);
                $("#token").val(data.token);
            } else {
                if(data.labelErrorMessage){
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type: "fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            projectVisaOrderJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
}

function validateForm(){
    return $("#entityForm").valid();
}

//校验失败调用的方法
function validateFormFail(){
    projectVisaOrderJsForm.subState = false;
    msgBox.info({ content : $.i18n.prop("JC_SYS_067"), });
}

projectVisaOrderJsForm.gotoTodo = function(){
    if (projectVisaOrderJsForm.saveOrUpdate = "save") {
        JCFF.loadPage({url: "/project/visa/search.action?fn=myApply"});
    } else {
        JCFF.loadPage({url: "/project/visa/todoList.action"});
    }
};

/**
 * 加载合同
 * @param projectId: 项目
 * @param contractId: 合同
 */
projectVisaOrderJsForm.loadContract = function(projectId, contractId) {
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

projectVisaOrderJsForm.contractChange = function() {
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

projectVisaOrderJsForm.attach = new AttachControl.AttachListControl({
    renderElement: 'attachTable',
    businessTable: 'cm_project_visa_order',
    itemName: 'uploadAttachContainer',
    titleWidth: 150
});

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#entityForm #contractId').change(projectVisaOrderJsForm.contractChange);
    var businessJson = $("#businessJson").val();
    if (businessJson.length > 0) {
        var businessObj = eval("("+businessJson+")");
        $("#entityForm").fill(businessObj);
        if (businessObj.modifyType == '0') {
            document.getElementById("modifyType0").checked = true;
        } else if (businessObj.modifyType == '1') {
            document.getElementById("modifyType1").checked = true;
        }
        projectVisaOrderJsForm.loadContract(businessObj.projectId, businessObj.contractId);
        if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1') {
            projectVisaOrderJsForm.attach.initAttach($('#entityForm #id').val());
        } else {
            projectVisaOrderJsForm.attach.initAttachOnView($('#entityForm #id').val());
            $('#checkedAmountMust').html('*');
            if ($('#entityForm #checkedAmount').val() == '') {
                $('#entityForm #checkedAmount').val($('#entityForm #visaAmount').val());
            }
        }
    } else {
        projectVisaOrderJsForm.attach.initAttach(0);
        liuAttachPool.initPageAttach(1 , 0 ,"");
        $('#entityForm #projectName').click(function() {
            chooseProjectPanel.init(function(data) {
                $('#entityForm #partaUnitLeaderId').val("");
                //选择项目名后回调函数
                $('#entityForm #projectId').val(data.id);
                $('#entityForm #projectName').val(data.projectName);
                projectVisaOrderJsForm.loadContract(data.id, null);
            });
        });
    }
    formPriv.doForm();
});

$("#entityForm").validate({
    ignore:'ignore',
    rules: {
        code: { required: true, maxlength:255 },
        projectId: { required: true, maxlength:50 },
        contractId: { required: true, maxlength:50 },
        visaReason: { required: true, maxlength:2000 },
        visaDate: { required: true, maxlength:19 },
        visaScope: { required: false, maxlength:2000 },
        visaChange: { required: false, maxlength:16383 },
        projectAmount: { required: true, maxlength:16383 },
        visaAmount: { required: true, maxlength:18, number:true },
        modifyType: { required: true },
        checkedAmount: { required: false, maxlength:18, number:true },
    },
    messages:{
        visaAmount: { number: "请输入有效的数字" },
        checkedAmount: { number: "请输入有效的数字" }
    }
});
