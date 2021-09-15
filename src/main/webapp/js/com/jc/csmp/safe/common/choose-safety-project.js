var chooseSafeytProjectPanel = {};
chooseSafeytProjectPanel.init = function (callbackFunction, deptId) {
    var url = getRootPath() + "/project/info/changeSafety.action";
    if (deptId != undefined && deptId != null && deptId != "") {
        url += "?deptId=" + deptId;
    }
    $("#projectChangeModalModule").load(url, null, function() {
        cmProjectInfoChangeSafeytPanel.init({callback: callbackFunction});
    });
};