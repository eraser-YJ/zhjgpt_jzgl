/**
 * Created by sunpeng on 16-4-27.
 */

var demoForm = {};

demoForm.save = function(submitType){
    var id = $("#demoForm #id").val();
    var url = getRootPath()+"/demo/save.action";
    if(id!=null&&id.length>0){
        url = getRootPath()+"/demo/update.action";
    }
    var formData = $("#demoForm").serializeArray();
    formData.push({"name": "workflowBean.submitType_", "value": submitType});

    if(submitType == "SUBMIT"){
        demoForm.addSubmitVar(formData);
    }else if(submitType == "GETBACK"){
        demoForm.addGetBackVar(formData);
    }else if(submitType == "REJECT"){
        demoForm.addRejectVar(formData);
    }else if(submitType == "GOTO"){
        demoForm.addGotoVar(formData);
    }else if(submitType == "MOVE"){
        demoForm.addMoveVar(formData);
    }else if(submitType == "STOP"){
    }
    $.ajax({
        type : "POST",
        url : url,
        data : formData,
        dataType : "json",
        success : function(data) {
           alert("提交成功");
           $('#demoSubmitModal').modal('hide');
            window.location.href = getRootPath() + "/demo/toDoneList.action";
        }
    });
}

demoForm.initFun = function(){
    var demoJson = $("#demoJson").val();
    if(demoJson.length>0){
        var demoJsonObj = eval("("+demoJson+")");
        $("#demoForm").fill(demoJsonObj);
    }
}

demoForm.showSubmitModel = function(){
    $('#demoSubmitModal').modal('show');
}

demoForm.showGetBackModel = function(){
    $('#demoGetBackModal').modal('show');
}

demoForm.showRejectModel = function(){
    $('#demoRejectModal').modal('show');
}

demoForm.showGotoModel = function(){
    $('#demoGotoModal').modal('show');
}

demoForm.showMoveModel = function(){
    $('#demoMoveModal').modal('show');
}

demoForm.showStopModel = function(){
    $('#demoStopModal').modal('show');
}

demoForm.viewFlow = function(){
    var instanceId = $("#instanceId_").val();
    if(instanceId.length>0){
        window.open(getRootPath()+"/instance/getImg.action?instanceId="+instanceId);
    }else{
        alert("无法查看");
    }
}

demoForm.viewFlowHistory = function(){
    var instanceId = $("#instanceId_").val();
    if(instanceId.length>0){
        window.open(getRootPath()+"/workflowHistory/showHistoryDemo.action?instanceId="+instanceId);
    }else{
        alert("无法查看");
    }
}

demoForm.addSubmitVar = function(formData){
    formData.push({"name": "workflowBean.confirmNodeId_", "value": $("#demoSubmitModal #confirmNodeId_").val()});
    formData.push({"name": "workflowBean.confirmUserId_", "value": $("#demoSubmitModal #confirmUserId_").val()});
    formData.push({"name": "workflowBean.confirmRouteId_", "value": $("#demoSubmitModal #confirmRouteId_").val()});
}

demoForm.addGetBackVar = function(formData){
    formData.push({"name": "workflowBean.confirmNodeId_", "value": $("#demoGetBackModal #confirmNodeId_").val()});
}

demoForm.addRejectVar = function(formData){
    formData.push({"name": "workflowBean.confirmNodeId_", "value": $("#demoRejectModal #confirmNodeId_").val()});
}

demoForm.addGotoVar = function(formData){
    formData.push({"name": "workflowBean.confirmNodeId_", "value": $("#demoGotoModal #confirmNodeId_").val()});
}

demoForm.addMoveVar = function(formData){
    formData.push({"name": "workflowBean.confirmUserId_", "value": $("#demoMoveModal #confirmUserId_").val()});
}

$(document).ready(function(){
    $('#submitBtn').click(demoForm.showSubmitModel);
    $('#saveBtn').click(demoForm.showMoveModel);
    $('#rejectBtn').click(demoForm.showRejectModel);
    $('#getBackBtn').click(demoForm.showGetBackModel);
    $('#gotoBtn').click(demoForm.showGotoModel);
    $('#moveBtn').click(demoForm.showMoveModel);
    $('#stopBtn').click(demoForm.showStopModel);


    $("#viewFlowImg").click(demoForm.viewFlow);
    $("#viewFlowHistory").click(demoForm.viewFlowHistory);

    demoForm.initFun();
});
