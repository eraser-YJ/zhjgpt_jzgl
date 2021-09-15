var chooseContractPanel = {};
chooseContractPanel.init = function (callbackFunction) {
    if ($("#contractChangeModalModule").html() == '') {
        $("#contractChangeModalModule").load(getRootPath() + "/contract/info/change.action", null, function() {
            cmContractInfoChangePanel.init({callback: callbackFunction});
        });
    } else {
        cmContractInfoChangePanel.init({callback: callbackFunction});
    }
};