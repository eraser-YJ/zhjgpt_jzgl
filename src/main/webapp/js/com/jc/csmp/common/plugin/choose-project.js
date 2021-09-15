var chooseProjectPanel = {};
chooseProjectPanel.init = function (callbackFunction, deptId) {
    var url = getRootPath() + "/project/info/change.action";
    if (deptId != undefined && deptId != null && deptId != "") {
        url += "?deptId=" + deptId;
    }
    $("#projectChangeModalModule").load(url, null, function() {
        cmProjectInfoChangePanel.init({callback: callbackFunction});
    });
};