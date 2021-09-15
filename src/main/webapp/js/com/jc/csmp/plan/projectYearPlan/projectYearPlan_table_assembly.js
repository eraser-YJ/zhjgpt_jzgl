var tableAssFun = {};
tableAssFun.mainProjectType = "0";
tableAssFun.eidt = false;
tableAssFun.dicInfo;
tableAssFun.dicSet = {};
tableAssFun.dicTree;
tableAssFun.xsDicList;
tableAssFun.allItemAttribute = [
    'itemId',
    'projectType',
    'projectTypeName',
    'projectName',
    'dutyCompany',
    'dutyPerson',
    'projectTotalDay',
    'projectTotalInvest',
    'projectUsedInvest',
    'projectNowInvest',
    'projectAfterInvest',
    'projectNowDesc',
    'projectDesc',
    "remark"
];

tableAssFun.numItemAttribute = [
    'projectTotalInvest',
    'projectUsedInvest',
    'projectNowInvest',
    'projectAfterInvest'
]
//查找下级字典
tableAssFun.findNextDic = function (nowDicCode) {
    return tableAssFun.dicSet[nowDicCode].nextDic;
}
//树形结构转Map
tableAssFun.treeToMap = function (tree) {
    if(tree){
        var nowDic;
        for (var i = 0; i < tree.length; i++) {
            nowDic = tree[i];
            tableAssFun.dicSet[nowDic.code] = nowDic;
            tableAssFun.treeToMap(nowDic.nextDic);
        }
    }
}
//初始化
tableAssFun.initData = function (data) {
    tableAssFun.dicInfo = data.dicInfo;
    tableAssFun.dicTree = data.dicInfo.dicTree;
    tableAssFun.treeToMap(tableAssFun.dicTree);
    tableAssFun.xsDicList = data.dicInfo.xsDicList;
    var configKeyValue = $("#addEditCtrl_wfvar").val();
    if (configKeyValue === "must" || configKeyValue === "edit") {
        tableAssFun.eidt = true;
    }
    $("#playBody00").append(tableAssFun.initHtmlTypeTop(tableAssFun.mainProjectType));

}

//取得行数据
tableAssFun.getRowData = function (selectId) {
    var rowData = {};
    var attKey;
    for (var rowIndex = 0; rowIndex < tableAssFun.allItemAttribute.length; rowIndex++) {
        attKey = tableAssFun.allItemAttribute[rowIndex];
        rowData[attKey] = $("#" + attKey + "_" + selectId).val();
    }
    return rowData;

};

//取得行数据
tableAssFun.getAllData = function () {
    var dataAgg = [];
    $("[id^='sequence_']").each(function (itemIndex, itemObject) {
        var idStr = $(itemObject).attr("id");
        var ids = idStr.split("_")
        var id = ids[1];
        dataAgg.push(tableAssFun.getRowData(id));
    })
    return dataAgg;
};

//取得行数据
tableAssFun.fillRowData = function (rowData) {
    var code = rowData.itemId;
    var attKey;
    var attKey1;
    for (var rowIndex = 0; rowIndex < tableAssFun.allItemAttribute.length; rowIndex++) {
        attKey = tableAssFun.allItemAttribute[rowIndex];
        $("#" + attKey + "_" + code).val(rowData[attKey]);
        $("#dis_" + attKey + "_" + code).html(rowData[attKey]);
        for (var rowIndex1 = 0; rowIndex1 < tableAssFun.numItemAttribute.length; rowIndex1++) {
            attKey1 = tableAssFun.numItemAttribute[rowIndex1];
            if(attKey1 == attKey){
                $("#dis_" + attKey + "_" + code).html(xyParseToFloatStr(rowData[attKey]));

            }
        }
    }
};
//添加到类型最后
tableAssFun.addItemToTypeLast = function (rowData) {
    var maxTrOnThisType;
    $("[id^='tr_" + rowData.projectType + "_']").each(function (itemIndex, itemObject) {
        maxTrOnThisType = $(itemObject);
    })
    var trId = "tr_" + rowData.projectType + "_" + rowData.itemId;
    if (maxTrOnThisType) {
        maxTrOnThisType.after(tableAssFun.buildOne(trId, rowData))
    } else {
        $("#tr_" + rowData.projectType).after(tableAssFun.buildOne(trId, rowData));
    }
}

//初始化所有的字典
tableAssFun.initHtmlTypeTop = function (code) {
    var h = "";
    h += tableAssFun.initNextHtmlStatis(code);
    h += tableAssFun.initNextHtmlType(code);
    return h;
}

//初始化所有的字典
tableAssFun.initNextHtmlStatis = function (code) {
    var h = "";
    h += "<tr>";
    h += " <td colspan='3' style='text-align:left'>&nbsp;<span style='font-size:20px;'>投资总计</span></td>";
    h += " <td>";
    h += " <input type='hidden' id='projectTotalInvest_" + code + "' name='projectTotalInvest_" + code + "'/>";
    h += " <span id='dis_projectTotalInvest_" + code + "'>0.0000</span>";
    h += " </td>";
    h += " <td>";
    h += " <input type='hidden' id='projectUsedInvest_" + code + "' name='projectUsedInvest_" + code + "'/>";
    h += " <span id='dis_projectUsedInvest_" + code + "'>0.0000</span>";
    h += " </td>";
    h += " <td>";
    h += " <input type='hidden' id='projectNowInvest_" + code + "' name='projectNowInvest_" + code + "'/>";
    h += " <span id='dis_projectNowInvest_" + code + "'>0.0000</span>";
    h += " </td>";
    h += " <td>";
    h += " <input type='hidden' id='projectAfterInvest_" + code + "' name='projectAfterInvest_" + code + "'/>";
    h += " <span id='dis_projectAfterInvest_" + code + "'>0.0000</span>";
    h += " </td>";
    h += " <td></td>";
    h += " <td></td>";
    h += " <td></td>";
    h += " <td></td>";
    h += " <td></td>";
    h += " <td></td>";
    h += "</tr>";
    var topItem;
    for (var index1 = 0; index1 < tableAssFun.dicTree.length; index1++) {
        topItem = tableAssFun.dicTree[index1];
        h += "<tr>";
        h += " <td colspan='3' style='text-align:left'>&nbsp;" + numIndexChinaSet['index' + index1] + topItem.value + "</td>";
        h += " <td>";
        h += " <input type='hidden' id='projectTotalInvest_" + topItem.code + "' name='projectTotalInvest_" + topItem.code + "'/>";
        h += " <span id='dis_projectTotalInvest_" + topItem.code + "'>0.0000</span>";
        h += " </td>";
        h += " <td>";
        h += " <input type='hidden' id='projectUsedInvest_" + topItem.code + "' name='projectUsedInvest_" + topItem.code + "'/>";
        h += " <span id='dis_projectUsedInvest_" + topItem.code + "'>0.0000</span>";
        h += " </td>";
        h += " <td>";
        h += " <input type='hidden' id='projectNowInvest_" + topItem.code + "' name='projectNowInvest_" + topItem.code + "'/>";
        h += " <span id='dis_projectNowInvest_" + topItem.code + "'>0.0000</span>";
        h += " </td>";
        h += " <td>";
        h += " <input type='hidden' id='projectAfterInvest_" + topItem.code + "' name='projectAfterInvest_" + topItem.code + "'/>";
        h += " <span id='dis_projectAfterInvest_" + topItem.code + "'>0.0000</span>";
        h += " </td>";
        h += " <td></td>";
        h += " <td></td>";
        h += " <td></td>";
        h += " <td></td>";
        h += " <td></td>";
        h += " <td></td>";
        h += "</tr>";
        var subItem;
        var newCode;
        for (var index2 = 0; index2 < tableAssFun.xsDicList.length; index2++) {
            subItem = tableAssFun.xsDicList[index2];
            newCode = topItem.code+"_"+subItem.code;
            h += "<tr>";
            if (index2 == 0) {
                h += " <td rowspan='" +tableAssFun.xsDicList.length + "'>按建<BR>设类<BR>别统<BR>计</td>";
            }
            h += " <td style='text-align:center;width:40px;'>" + (index2 + 1) + "</td>";
            h += " <td style='text-align:left'>&nbsp;" + subItem.value + "</td>";
            h += " <td>";
            h += " <input type='hidden' id='projectTotalInvest_" + newCode + "' name='projectTotalInvest_" + newCode + "'/>";
            h += " <span id='dis_projectTotalInvest_" + newCode + "'>0.0000</span>";
            h += " </td>";
            h += " <td>";
            h += " <input type='hidden' id='projectUsedInvest_" + newCode + "' name='projectUsedInvest_" + newCode + "'/>";
            h += " <span id='dis_projectUsedInvest_" + newCode + "'>0.0000</span>";
            h += " </td>";
            h += " <td>";
            h += " <input type='hidden' id='projectNowInvest_" + newCode + "' name='projectNowInvest_" + newCode + "'/>";
            h += " <span id='dis_projectNowInvest_" + newCode + "'>0.0000</span>";
            h += " </td>";
            h += " <td>";
            h += " <input type='hidden' id='projectAfterInvest_" + newCode + "' name='projectAfterInvest_" + newCode + "'/>";
            h += " <span id='dis_projectAfterInvest_" + newCode + "'>0.0000</span>";
            h += " </td>";
            h += " <td></td>";
            h += " <td></td>";
            h += " <td></td>";
            h += " <td></td>";
            h += " <td></td>";
            h += " <td></td>";
            h += "</tr>";
        }

        for (var index3 = 0; index3 < topItem.nextDic.length; index3++) {
            subItem = topItem.nextDic[index3];
            newCode = subItem.code;
            h += "<tr>";
            if (index3 == 0) {
                h += " <td rowspan='" + topItem.nextDic.length + "'>按项<BR>目类<BR>别统<BR>计</td>";
            }
            h += " <td style='text-align:center;width:40px;'>" + (index3 + 1) + "</td>";
            h += " <td style='text-align:left'>&nbsp;" + subItem.value + "</td>";
            h += " <td>";
            h += " <input type='hidden' id='projectTotalInvest_" + newCode + "' name='projectTotalInvest_" + newCode + "'/>";
            h += " <span id='dis_projectTotalInvest_" + newCode + "'>0.0000</span>";
            h += " </td>";
            h += " <td>";
            h += " <input type='hidden' id='projectUsedInvest_" + newCode + "' name='projectUsedInvest_" + newCode + "'/>";
            h += " <span id='dis_projectUsedInvest_" + newCode + "'>0.0000</span>";
            h += " </td>";
            h += " <td>";
            h += " <input type='hidden' id='projectNowInvest_" + newCode + "' name='projectNowInvest_" + newCode + "'/>";
            h += " <span id='dis_projectNowInvest_" + newCode + "'>0.0000</span>";
            h += " </td>";
            h += " <td>";
            h += " <input type='hidden' id='projectAfterInvest_" + newCode + "' name='projectAfterInvest_" + newCode + "'/>";
            h += " <span id='dis_projectAfterInvest_" + newCode + "'>0.0000</span>";
            h += " </td>";
            h += " <td></td>";
            h += " <td></td>";
            h += " <td></td>";
            h += " <td></td>";
            h += " <td></td>";
            h += " <td></td>";
            h += "</tr>";
        }
    }
    h += "<tr>";
    h += " <td colspan='13' style='text-align:left'><span style='font-size:20px;'>详细数据</span></td>";
    h += "</tr>";
    return h;
}
//初始化所有的字典
tableAssFun.initNextHtmlType = function (code, pre) {
    if (pre) {
        if (pre.length > 0) {
            pre = pre + "."
        }
    } else {
        pre = "";
    }
    var h = "";
    var nextDicList;
    if(code == '0'){
        nextDicList = tableAssFun.dicTree;
    } else {
        nextDicList = tableAssFun.findNextDic(code);
    }
    var item;
    for (var index = 0; index < nextDicList.length; index++) {
        item = nextDicList[index];
        if (item.leaf == 'Y') {
            h += "<tr id='tr_" + item.code + "' leaf='Y' leafcode='" + item.code + "' >";
        } else {
            h += "<tr id='tr_" + item.code + "'>";
        }

        h += " <td colspan='13' style='text-align:left'>&nbsp;（" + pre + (index + 1) + "）&nbsp;<b>" + item.value+"</b>";
        if (item.leaf == 'Y'&&tableAssFun.eidt) {
            h += '&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="新增" onclick="projectPlanModule.addItemBtnFun(\'' + item.code + '\',\'' + item.treePathName + '\')">';
        }
        h += " </td>";
        h += "</tr>";
        h += tableAssFun.initNextHtmlType(item.code, pre + (index + 1));
    }
    return h;
}

//初始化项目行数据
tableAssFun.buildOne = function (trId, rowData) {
    var h = "";
    var item = rowData;
    h += "<tr id='" + trId + "' datatype='data'>";
    h += " <td style='text-align:center' colspan='2'>";
    h += " <span id='sequence_" + item.itemId + "'></span>";

    var attKey;
    for (var rowIndex = 0; rowIndex < tableAssFun.allItemAttribute.length; rowIndex++) {
        attKey = tableAssFun.allItemAttribute[rowIndex];
        h += " <input type='hidden' id='" + attKey + "_" + item.itemId + "' value='" + nvl(item[attKey]) + "'/>";
    }
    h += " </td>";
    h += " <td style='text-align:left'>";
    h += " <span id='dis_projectName_" + item.itemId + "' >" + nvl(item.projectName) + "</span>";
    if (tableAssFun.eidt) {
        h += "&nbsp;&nbsp;";
        h += ' <input type="button" value="编辑" onclick="projectPlanModule.eidtItemBtnFun(\'' + item.itemId + '\')">';
        h += "&nbsp;&nbsp;";
        h += ' <input type="button" value="删除" onclick="projectPlanModule.deleteItemBtnFun(\'' + item.itemId + '\')">';
    }
    h += " </td>";
    h += " <td><span id='dis_projectTotalInvest_" + item.itemId + "' >" + xyParseToFloatStr(item.projectTotalInvest) + "</span></td>";
    h += " <td><span id='dis_projectUsedInvest_" + item.itemId + "' >" + xyParseToFloatStr(item.projectUsedInvest) + "</span></td>";
    h += " <td><span id='dis_projectNowInvest_" + item.itemId + "' >" + xyParseToFloatStr(item.projectNowInvest) + "</span></td>";
    h += " <td><span id='dis_projectAfterInvest_" + item.itemId + "' >" + xyParseToFloatStr(item.projectAfterInvest) + "</span></td>";
    h += " <td><span id='dis_projectTotalDay_" + item.itemId + "' >" + nvl(item.projectTotalDay) + "</span></td>";
    h += " <td style='text-align:left'><span id='dis_projectNowDesc_" + item.itemId + "' >" + nvl(item.projectNowDesc) + "</span></td>";
    h += " <td style='text-align:left'><span id='dis_dutyCompany_" + item.itemId + "' >" + nvl(item.dutyCompany) + "</span></td>";
    h += " <td style='text-align:left'><span id='dis_dutyPerson_" + item.itemId + "' >" + nvl(item.dutyPerson) + "</span></td>";
    h += " <td style='text-align:left'><span id='dis_projectDesc_" + item.itemId + "' >" + nvl(item.projectDesc) + "</span></td>";
    h += " <td style='text-align:left'><span id='dis_remark_" + item.itemId + "' >" + nvl(item.remark) + "</span></td>";
    h += "</tr>";
    return h;
}
