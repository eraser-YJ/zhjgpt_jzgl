var footerToolsModule = {};

/**
 * 查看工作流历史
 */
footerToolsModule.workflowHistory = function(pid) {
    $.ajax({
        type: "GET", url: getRootPath() + "/common/api/workflowHistory.action", data: {"pid": pid}, dataType: "json",
        success : function(data) {
            if (data.code == 0) {
                window.open(getRootPath() + "/workflowHistory/showHistory.action?instanceId=" + data.data.instanceId + "&definitionId=" + data.data.definitionId);
            }
        }
    });
};

footerToolsModule.returnHandleUrl = function(pid, callback) {
    $.ajax({
        type: "GET", url: getRootPath() + "/common/api/workflowTodo.action", data: {"pid": pid}, dataType: "json",
        success : function(data) {
            if (data.code == 0) {
                var source = data.data;
                var url = "/instance/toOpenForm.action?definitionId_=" + source.definitionId
                        + "&business_Key_=" + source.businessKey
                        + "&curNodeId_="+source.curNodeId
                        + "&taskId_="+source.taskId
                        + "&instanceId_="+source.instanceId
                        + "&openType_=TODO";
                if (callback != undefined) {
                    callback(url);
                    return "";
                } else {
                    return url;
                }
            }
            return "";
        }
    });
};