var warnInfoJsList = {};

warnInfoJsList.queryRealtimeDetailInfo = function () {
    var cond = {};

    var warnTimeBegin = $('#searchForm #query_warnTimeBegin').val();
    if (warnTimeBegin.length > 0) {
        cond['warnTimeBegin'] = warnTimeBegin;
    }
    var warnTimeEnd = $('#searchForm #query_warnTimeEnd').val();
    if (warnTimeEnd.length > 0) {
        cond['warnTimeEnd'] = warnTimeEnd;
    }
    $.ajax({
        type: "POST",
        data: cond,
        dataType: "json",
        url: getRootPath() + "/warn/statis/manageList.action",
        success: function (data) {
            warnInfoJsList.buildMechList(data);
        }
    });
};
warnInfoJsList.queryReset = function () {
    $('#searchForm')[0].reset();
};
//构建页面
warnInfoJsList.buildMechList = function (dataList) {
    var item;
    var h = "";
    if(dataList&&dataList.length>0){
        for (index = 0; index < dataList.length; index++) {
            item = dataList[index];
            h += "<tr>"
            h += "<td>" + (index+1) + "</td>";
            h += "<td>" + item.projectName + "</td>";
            h += "<td>" + item.equipmentTypeValue + "</td>";
            h += "<td>" + warnInfoJsList.parseInt(item.initNum) + "</td>";
            h += "<td>" + warnInfoJsList.parseInt(item.processedNum) + "</td>";
            h += "</tr>";
        }
    } else {
        h = "<tr class=\"odd\"><td valign=\"top\" colspan=\"6\" class=\"dataTables_empty\">没有匹配的记录</td></tr>";
    }
    $("#gridTableBody").html(h);
};
//判空
warnInfoJsList.nvl = function (item) {
    if(item){
        return item;
    }
    return "";
}
warnInfoJsList.parseInt = function (item) {
    if(item){
        return item;
    }
    return "0";
}
$(document).ready(function () {
    warnInfoJsList.queryRealtimeDetailInfo();
    $('#queryBtn').click(warnInfoJsList.queryRealtimeDetailInfo);
    $('#resetBtn').click(warnInfoJsList.queryReset);
});