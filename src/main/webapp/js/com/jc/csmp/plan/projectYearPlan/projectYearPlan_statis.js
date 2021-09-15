var projectPlanStatisFun = {};
projectPlanStatisFun.selectYearOnExport = function () {
    var year = $("#selectYear").val();
    var url = getRootPath() + "/plan/projectYearPlan/statis/export.action?selectYear=" + year + "&n_" + (new Date().getTime());
    window.location.href = url;
}

projectPlanStatisFun.search = function () {
    var year = $("#selectYear").val();
    var url = getRootPath() + "/plan/projectYearPlan/statis/manageList.action?selectYear=" + year + "&n_" + (new Date().getTime());
    jQuery.ajax({
        url: url,
        async: false,
        data: {"selectYear": year},
        type: 'POST',
        success: function (resData) {
            if (resData.data) {
                $("#statisDisplayDiv").html("");
                projectPlanStatisFun.initHtml(resData.data);
            }
        }
    });
}
projectPlanStatisFun.initHtml = function (data) {
    var h = "";
    h += "<table style='width:100%'>";
    h += "<tr>";
    h += "	<td colspan='2' style='width:100%;text-align:center'>";
    h += "	<h2>" + data.main.planYear + "年基础设施及功能类项目建设汇总表</h2>";
    h += "	</td>";
    h += "</tr>";
    h += "<tr>";
    h += "	<td colspan='2' style='width:100%;text-align:right'>";
    h += "	单位：万元";
    h += "	</td>";
    h += "</tr>";
    h += "</table>";
    //列表
    h += "<table style='width:100%' border='1px;' class='table-year'>";
    h += "<tr>";
    h += "	<td rowspan='2' colspan='3' align='center' style='width:200px;'>";
    h += "	项目类别";
    h += "	</td>";
    h += "	<td rowspan='2'  align='center' style='width:120px;'>";
    h += "	累计完成投资";
    h += "	</td>";
    h += "	<td rowspan='2'  align='center'  style='width:120px;'>";
    h += "	计划投资";
    h += "	</td>";
    for (var index = 0; index < data.areaList.length; index++) {
        h += "	<td colspan='2'  align='center'>";
        if (data.areaList[index] && data.areaList[index].value) {
            h += "	" + data.areaList[index].value;
        }
        h += "	</td>";
    }
    h += "</tr>";
    h += "<tr>";
    for (var index = 0; index < data.areaList.length; index++) {
        h += "	<td  align='center'  style='width:120px;'>";
        h += "	累计完成投资";
        h += "	</td>";
        h += "	<td  align='center'  style='width:120px;'>";
        h += "	计划投资";
        h += "	</td>";
    }
    h += "</tr>";
    //求和

    var rowData;
    for (var typeIndex = 0; typeIndex < data.rowList.length; typeIndex++) {
        rowData = data.rowList[typeIndex];
        //求和
        var sumUsedInvest = 0;
        var sumNowInvest = 0;
        var dataList = rowData.subInfo;
        for (var dataIndex = 0; dataIndex < dataList.length; dataIndex++) {
            sumUsedInvest += xyParseToFloat(dataList[dataIndex].projectUsedInvest);
            sumNowInvest += xyParseToFloat(dataList[dataIndex].projectNowInvest);
        }
        h += "<tr>";
        h += "	<td  align='center'  colspan='3'  style='width:120px;'>";
        h += "	" + rowData.value;
        h += "	</td>";
        h += "	<td  align='center'  style='width:120px;'>";
        h += "	" + xyParseToFloatStr(sumUsedInvest);
        h += "	</td>";
        h += "	<td  align='center'  style='width:120px;'>";
        h += "	" + xyParseToFloatStr(sumNowInvest);
        h += "	</td>";
        var rowSubData;
        for (var dIndex = 0; dIndex < dataList.length; dIndex++) {
            rowSubData = dataList[dIndex];
            h += "	<td  align='center'  style='width:120px;'>";
            h += "	" + xyParseToFloatStr(rowSubData.projectUsedInvest);
            h += "	</td>";
            h += "	<td  align='center'  style='width:120px;'>";
            h += "	" + xyParseToFloatStr(rowSubData.projectNowInvest);
            h += "	</td>";

        }
        h += "</tr>";
        var rowNextData;
        for (var nextIndex = 0; nextIndex < rowData.nextDic.length; nextIndex++) {
            rowNextData = rowData.nextDic[nextIndex];
            //求和
            var sumNextUsedInvest = 0;
            var sumNextNowInvest = 0;
            var dataNextList = rowNextData.subInfo;
            for (var dataNextIndex = 0; dataNextIndex < dataNextList.length; dataNextIndex++) {
                sumNextUsedInvest += xyParseToFloat(dataNextList[dataNextIndex].projectUsedInvest);
                sumNextNowInvest += xyParseToFloat(dataNextList[dataNextIndex].projectNowInvest);
            }
            h += "<tr>";
            if (nextIndex == 0) {
                h += "	<td  align='center' rowspan='" + rowData.nextDic.length + "' style='width:80px;'>";
                h += "	按项目<br>类别<br>统计";
                h += "	</td>";
            }
            h += "	<td  align='center'  style='width:60px;'>";
            h += "	" + (nextIndex + 1);
            h += "	</td>";
            h += "	<td  align='center'  style='width:120px;'>";
            h += "	" + rowNextData.value;
            h += "	</td>";
            h += "	<td  align='center'  style='width:120px;'>";
            h += "	" + xyParseToFloatStr(sumNextUsedInvest);
            h += "	</td>";
            h += "	<td  align='center'  style='width:120px;'>";
            h += "	" + xyParseToFloatStr(sumNextNowInvest);
            h += "	</td>";
            var rowNextSubData;
            for (var dNextIndex = 0; dNextIndex < dataNextList.length; dNextIndex++) {
                rowNextSubData = dataNextList[dNextIndex];
                h += "	<td  align='center'  style='width:120px;'>";
                h += "	" + xyParseToFloatStr(rowNextSubData.projectUsedInvest);
                h += "	</td>";
                h += "	<td  align='center'  style='width:120px;'>";
                h += "	" + xyParseToFloatStr(rowNextSubData.projectNowInvest);
                h += "	</td>";
            }
            h += "</tr>";
        }
    }

    h += "</table>";
    $("#statisDisplayDiv").html(h);
}