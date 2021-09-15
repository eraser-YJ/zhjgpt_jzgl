var warnStdFun = {};
warnStdFun.initWarnStd = function (callback) {
    $("#entityFormWarnDisplayDiv").html("");
    if (callback) {
        callback();
    }
};