var tableMonthAssFun = {};
tableMonthAssFun.mainProjectType = "0";
tableMonthAssFun.eidt = false;
tableMonthAssFun.dicInfo;
tableMonthAssFun.dicSet = {};
tableMonthAssFun.dicTree;
tableMonthAssFun.xcDicList;
tableMonthAssFun.allItemAttribute = [
    'itemId',
    'projectType',
    'projectTypeName',
    'projectId',
    'projectName',
    'dutyCompany',
    'dutyPerson',
    'projectTotalInvest',
    'projectUsedInvest',
    'projectNowInvest',
    'projectMonthPlanInvest',
    'projectMonthActInvest',
    'xxjd',
    'xxjdAttchList',
    'solveProblem',
    'tudiCard',
    'ydghxkCard',
    'gcghxkCard',
    'kgxkCard',
    'xmxzyjs',
    "remark",
    "extStr5"
];

tableMonthAssFun.numItemAttribute = [
    'projectTotalInvest',
    'projectUsedInvest',
    'projectNowInvest',
    'projectMonthPlanInvest',
    'projectMonthActInvest'
]
//查找下级字典
tableMonthAssFun.findNextDic = function (nowDicCode) {
    return tableMonthAssFun.dicSet[nowDicCode].nextDic;
}
//树形结构转Map
tableMonthAssFun.treeToMap = function (tree) {
    if (tree) {
        var nowDic;
        for (var i = 0; i < tree.length; i++) {
            nowDic = tree[i];
            tableMonthAssFun.dicSet[nowDic.code] = nowDic;
            tableMonthAssFun.treeToMap(nowDic.nextDic);
        }
    }
}
//初始化
tableMonthAssFun.initData = function (data, editFlag) {
    tableMonthAssFun.dicInfo = data.dicInfo;
    tableMonthAssFun.dicTree = data.dicInfo.dicTree;
    tableMonthAssFun.treeToMap(tableMonthAssFun.dicTree);
    tableMonthAssFun.xsDicList = data.dicInfo.xsDicList;
    tableMonthAssFun.eidt = editFlag;
    $("#playBody00").append(tableMonthAssFun.initHtmlTypeTop(tableMonthAssFun.mainProjectType));

}

//取得行数据
tableMonthAssFun.getRowData = function (selectId) {
    var rowData = {};
    var attKey;
    for (var rowIndex = 0; rowIndex < tableMonthAssFun.allItemAttribute.length; rowIndex++) {
        attKey = tableMonthAssFun.allItemAttribute[rowIndex];
        rowData[attKey] = nvl($("#" + attKey + "_" + selectId).val());
    }
    return rowData;

};

//取得行数据
tableMonthAssFun.getAllData = function () {
    var dataAgg = [];
    $("[id^='sequence_']").each(function (itemIndex, itemObject) {
        var idStr = $(itemObject).attr("id");
        var ids = idStr.split("_")
        var id = ids[1];
        dataAgg.push(tableMonthAssFun.getRowData(id));
    })
    return dataAgg;
};

//取得行数据
tableMonthAssFun.fillRowData = function (rowData) {
    var code = rowData.itemId;
    var attKey;
    var attKey1;
    for (var rowIndex = 0; rowIndex < tableMonthAssFun.allItemAttribute.length; rowIndex++) {
        attKey = tableMonthAssFun.allItemAttribute[rowIndex];
        $("#" + attKey + "_" + code).val(rowData[attKey]);
        if (attKey == 'tudiCard' || attKey == 'ydghxkCard' || attKey == 'gcghxkCard' || attKey == 'kgxkCard' || attKey == 'xmxzyjs') {
            $("#dis_" + attKey + "_" + code).html(isDic(rowData[attKey]));
        } else if (attKey == 'solveProblem') {
            $("#dis_" + attKey + "_" + code).html(tableMonthAssFun.buildSolveProblem(rowData[attKey]));
        } else if (attKey == 'xxjdAttchList') {
            $("#dis_" + attKey + "_" + code).html(tableMonthAssFun.buildXxjdAttachList(rowData['xxjdAttchList']));
        } else {
            $("#dis_" + attKey + "_" + code).html(rowData[attKey]);
        }
        for (var rowIndex1 = 0; rowIndex1 < tableMonthAssFun.numItemAttribute.length; rowIndex1++) {

            attKey1 = tableMonthAssFun.numItemAttribute[rowIndex1];
            if(attKey1 == attKey){
                $("#dis_" + attKey + "_" + code).html(xyParseToFloatStr(rowData[attKey]));

            }
        }
    }
};
//添加到类型最后
tableMonthAssFun.addItemToTypeLast = function (rowData) {
    var maxTrOnThisType;
    $("[id^='tr_" + rowData.projectType + "_']").each(function (itemIndex, itemObject) {
        maxTrOnThisType = $(itemObject);
    })
    var trId = "tr_" + rowData.projectType + "_" + rowData.itemId;
    if (maxTrOnThisType) {
        maxTrOnThisType.after(tableMonthAssFun.buildOne(trId, rowData))
    } else {
        $("#tr_" + rowData.projectType).after(tableMonthAssFun.buildOne(trId, rowData));
    }
}

//初始化所有的字典
tableMonthAssFun.initHtmlTypeTop = function (code) {
    var h = "";
    h += tableMonthAssFun.initNextHtmlType(code);
    return h;
}

//初始化所有的字典
tableMonthAssFun.initNextHtmlType = function (code, pre) {
    if (pre) {
        if (pre.length > 0) {
            pre = pre + "."
        }
    } else {
        pre = "";
    }
    var h = "";
    var nextDicList;
    if (code == '0') {
        nextDicList = tableMonthAssFun.dicTree;
    } else {
        nextDicList = tableMonthAssFun.findNextDic(code);
    }
    var item;
    for (var index = 0; index < nextDicList.length; index++) {
        item = nextDicList[index];
        if (item.leaf == 'Y') {
            h += "<tr id='tr_" + item.code + "' leaf='Y' leafcode='" + item.code + "' >";
        } else {
            h += "<tr id='tr_" + item.code + "'>";
        }

        h += " <td colspan='18' style='text-align:left'>&nbsp;（" + pre + (index + 1) + "）&nbsp;" + item.value;
        h += " </td>";
        h += "</tr>";
        h += tableMonthAssFun.initNextHtmlType(item.code, pre + (index + 1));
    }
    return h;
}

//初始化项目行数据
tableMonthAssFun.buildOne = function (trId, rowData) {
    var h = "";
    var item = rowData;
    h += "<tr id='" + trId + "'>";
    h += " <td style='text-align:center' colspan='2'>";
    h += " <span id='sequence_" + item.itemId + "'></span>";

    var attKey;
    for (var rowIndex = 0; rowIndex < tableMonthAssFun.allItemAttribute.length; rowIndex++) {
        attKey = tableMonthAssFun.allItemAttribute[rowIndex];
        h += " <input type='hidden' id='" + attKey + "_" + item.itemId + "' value='" + nvl(item[attKey]) + "'/>";
    }
    h += " </td>";
    h += " <td style='text-align: left'>";
    h += "<span id='dis_projectName_" + item.itemId + "' >" + item.projectName + "</span>";
    if (tableMonthAssFun.eidt) {
        h += '&nbsp;&nbsp;<input type="button" value="编辑" onclick="projectPlanMonthModule.eidtItemBtnFun(\'' + item.itemId + '\')">';
    }
    h += " </td>";
    h += " <td><span id='dis_projectTotalInvest_" + item.itemId + "' >" + xyParseToFloatStr(item.projectTotalInvest) + "</span></td>";
    h += " <td><span id='dis_projectUsedInvest_" + item.itemId + "' >" + xyParseToFloatStr(item.projectUsedInvest) + "</span></td>";
    h += " <td><span id='dis_projectNowInvest_" + item.itemId + "' >" + xyParseToFloatStr(item.projectNowInvest) + "</span></td>";
    h += " <td style='text-align: left'><span id='dis_xxjd_" + item.itemId + "' >" + nvl(item.xxjd)+ "</span><br>";
    h += " <div id='dis_xxjdAttchList_" + item.itemId + "'>"+tableMonthAssFun.buildXxjdAttachList(nvl(item.xxjdAttchList))+"</div>";
    h += " </td>";
    h += " <td><span id='dis_projectMonthPlanInvest_" + item.itemId + "' >" + xyParseToFloatStr(item.projectMonthPlanInvest) + "</span></td>";
    h += " <td><span id='dis_projectMonthActInvest_" + item.itemId + "' >" + xyParseToFloatStr(item.projectMonthActInvest) + "</span></td>";
    h += " <td style='text-align: left'><span id='dis_solveProblem_" + item.itemId + "' >" + tableMonthAssFun.buildSolveProblem(nvl(item.solveProblem)) + "</span></td>";
    h += " <td><span id='dis_tudiCard_" + item.itemId + "' >" + isDic(item.tudiCard) + "</span></td>";
    h += " <td><span id='dis_ydghxkCard_" + item.itemId + "' >" + isDic(item.ydghxkCard) + "</span></td>";
    h += " <td><span id='dis_gcghxkCard_" + item.itemId + "' >" + isDic(item.gcghxkCard) + "</span></td>";
    h += " <td><span id='dis_kgxkCard_" + item.itemId + "' >" + isDic(item.kgxkCard) + "</span></td>";
    h += " <td><span id='dis_xmxzyjs_" + item.itemId + "' >" + isDic(item.xmxzyjs) + "</span></td>";
    h += " <td style='text-align: left'><span id='dis_dutyCompany_" + item.itemId + "' >" + nvl(item.dutyCompany) + "</span></td>";
    h += " <td style='text-align: left'><span id='dis_dutyPerson_" + item.itemId + "' >" + nvl(item.dutyPerson) + "</span></td>";
    h += " <td style='text-align: left'><span id='dis_remark_" + item.itemId + "' >" + nvl(item.remark) + "</span></td>";
    h += "</tr>";
    return h;
}
//问题
tableMonthAssFun.buildSolveProblem = function (strContent) {
    if (strContent) {
        strContent = strContent.replace(/\r\n/g, '<br/>'); //IE9、FF、chrome
        strContent = strContent.replace(/\n/g, '<br/>'); //IE7-8
        strContent = strContent.replace(/\s/g, ' '); //空格处理

    }
    return strContent;
}
//形象进度附件
tableMonthAssFun.buildXxjdAttachList = function (xxjdAttchList) {
    var images = xxjdAttchList.split("#");
    if (images) {
        var imageInfo;
        var h = "";
        for (var index = 0; index < images.length; index++) {
            imageInfo = images[index].split(",");
            if (imageInfo[0]) {
                h += '<a href="' + getRootPath() + '/content/attach/download.action?attachId=' + imageInfo[0] + '">';
                h += '<img src="' + getRootPath() + '/content/attach/originalRead/' + imageInfo[0] + '" style="width:200px;height: 200px;" onclick="projectMonthPlanItemJsForm.removeImage(\'' + imageInfo[0] + '\')"/>';
                h += '</>';
            }
        }
        return h;
    }
    return "";
}
