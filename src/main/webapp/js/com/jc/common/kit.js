var keyFun = {};

//添加附件
keyFun.addAttach = function (attachScope, attchKey, displayDivId) {
    if (attachScope && attchKey && displayDivId) {
        $.ajax({
            type: "GET",
            data: {"attachScope": attachScope, "attachKey": attchKey},
            dataType: "json",
            url: getRootPath() + "/mock/view/attachList.action",
            success: function (data) {
                if (data) {
                    keyFun.addAttachCallback(data, displayDivId);
                }
            }
        });
    }
}
//添加附件回调
keyFun.addAttachCallback = function (attachList, displayDivId) {
    $("#" + displayDivId).html("");
    var h = "";
    h += "<ul>";
    var urlPre = getRootPath() + "/mock/view/download.action?n_=" + new Date().getTime();
    var item;
    for (var index = 0; index < attachList.length; index++) {
        item = attachList[index];
        var url = urlPre + "&resourceName=" + item.filePath + "&fileName=" + item.fileName;
        h += '<a href="' + url + '" >';
        h += item.fileName;
        h += '</a>';
    }
    h += "</ul>";
    $("#" + displayDivId).html(h);
}

//数据来源查询条件初始化
keyFun.dataSrcSearchInit = function (displaySelectId) {
    $("#" + displaySelectId).empty();
    $("#" + displaySelectId).append('<option value="">-全部-</option>');
    $("#" + displaySelectId).append('<option value="ptSrc01">省平台</option>');
    $("#" + displaySelectId).append('<option value="ptSrc02">市平台</option>');
}
//显示值
keyFun.dataSrcValue = function (type) {
    if (type == 'ptSrc01') {
        return "省平台";
    } else if (type == 'ptSrc02') {
        return "市平台";
    } else {
        return "";
    }
}