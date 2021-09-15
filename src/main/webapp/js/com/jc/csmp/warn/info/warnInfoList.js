var warnInfoJsList = {};
warnInfoJsList.oTable = null;
//空处理
warnInfoJsList.nvl = function (value, defaultValue) {
    if (value) {
        return value;
    }
    if (defaultValue) {
        return defaultValue;
    }
    return "";
}

warnInfoJsList.oTableFnServerParams = function (aoData) {
    getTableParameters(warnInfoJsList.oTable, aoData);
    var query_mechType = $('#query_mechType').val();
    aoData.push({"name": "targetType", "value": query_mechType});
    var query_targetProjectName = $('#searchForm #query_targetProjectName').val();
    if (query_targetProjectName.length > 0) {
        aoData.push({"name": "targetProjectName", "value": query_targetProjectName});
    }
    var query_warnReasonCode = $('#searchForm #query_warnReasonCode').val();
    if (query_warnReasonCode && query_warnReasonCode.length > 0) {
        aoData.push({"name": "warnReasonCode", "value": query_warnReasonCode});
    }
    var targetCodeCondObj = $('#searchForm #query_targetCode').val();
    if (targetCodeCondObj.length > 0) {
        aoData.push({"name": "targetCode", "value": targetCodeCondObj});
    }
    var warnTimeBegin = $('#searchForm #query_warnTimeBegin').val();
    if (warnTimeBegin.length > 0) {
        aoData.push({"name": "warnTimeBegin", "value": warnTimeBegin});
    }
    var warnTimeEnd = $('#searchForm #query_warnTimeEnd').val();
    if (warnTimeEnd.length > 0) {
        aoData.push({"name": "warnTimeEnd", "value": warnTimeEnd});
    }
};
warnInfoJsList.oTableAoColumns = [];
//初始化
warnInfoJsList.oTableAoColumnsInit = function () {
    var item;
    var colInfo = []
    colInfo[colInfo.length] = {mData: 'targetProjectName', sTitle: '项目', bSortable: false};
    colInfo[colInfo.length] = {mData: 'targetCode', sTitle: '设备编码', bSortable: false};
    colInfo[colInfo.length] = {
        mData: function (source) {
            if (oTableAoColumnsList && oTableAoColumnsList.length > 0) {
                var h = '<a href="javascript:warnInfoJsList.showView(\'' + source.id + '\')">' + warnInfoJsList.nvl(source.warnReason) + '</a>';
                return h;
            } else {
                return warnInfoJsList.nvl(source.warnReason);
            }

        }, sTitle: '报警', bSortable: false
    };
    colInfo[colInfo.length] = {mData: 'warnTime', sTitle: '报警发生时间', bSortable: false};
    for (var index = 0; index < oTableAoColumnsList.length; index++) {
        item = oTableAoColumnsList[index];
        colInfo[colInfo.length] = {mData: item.itemName, sTitle: item.disName, bSortable: false};
    }
    colInfo[colInfo.length] = {
        mData: function (source) {
            if (source.warnStatus == 'processed') {
                return "是"
            }
            return "否"
        }, sTitle: '处置状态', bSortable: false
    };
    colInfo[colInfo.length] = {
        mData: function (source) {
            if (source.warnStatus == 'processed') {
                var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"warnInfoJsList.loadModuleForView('" + source.id + "')\" role=\"button\">&nbsp;查看报告&nbsp;</a>";
                return edit;
            } else {
                var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"warnInfoJsList.loadModuleForUpdate('" + source.id + "')\" role=\"button\">&nbsp;添加报告&nbsp;</a>";
                return edit;
            }
        }, sTitle: '操作', bSortable: false, sWidth: 120
    }
    warnInfoJsList.oTableAoColumns = colInfo;
};
//查看
warnInfoJsList.showView = function (id) {
    var type = $("#query_mechType").val();
    $("#detailDataModule").load(getRootPath() + "/warn/info/loadView.action?id=" + id + "&type=" + type + "&n_=" + (new Date().getTime()), null, function () {
    });
}

warnInfoJsList.renderTable = function () {
    if (warnInfoJsList.oTable == null) {
        //初始化
        warnInfoJsList.oTableAoColumnsInit();
        warnInfoJsList.oTable = $('#gridTable').dataTable({
            "iDisplayLength": warnInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/warn/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": warnInfoJsList.oTableAoColumns,
            "fnServerParams": function (aoData) {
                warnInfoJsList.oTableFnServerParams(aoData);
            },
            aaSorting: [],
            aoColumnDefs: []
        });
    } else {
        warnInfoJsList.oTable.fnDraw();
    }
};

warnInfoJsList.delete = function (id) {
    var ids = String(id);
    if (id == 0) {
        var idsArr = [];
        $("[name='ids']:checked").each(function () {
            idsArr.push($(this).val());
        });
        ids = idsArr.join(",");
    }
    if (ids.length == 0) {
        msgBox.info({content: $.i18n.prop("JC_SYS_061")});
        return;
    }
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function () {
            warnInfoJsList.deleteCallBack(ids);
        }
    });
};

warnInfoJsList.deleteCallBack = function (ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/warn/info/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success: function (data) {
            if (data.success == "true") {
                msgBox.tip({type: "success", content: data.successMessage});
            } else {
                msgBox.info({content: data.errorMessage});
            }
            warnInfoJsList.renderTable();
        }
    });
};

warnInfoJsList.queryReset = function () {
    $('#searchForm')[0].reset();
};

warnInfoJsList.loadModuleForUpdate = function (id) {
    $("#formModule").load(getRootPath() + "/warn/info/loadResult.action", null, function () {
        warnResultJsForm.init({title: '处理报告', operator: 'modify', id: id});
    });
};
warnInfoJsList.loadModuleForView = function (id) {
    $("#formModule").load(getRootPath() + "/warn/info/loadResult.action", null, function () {
        warnResultJsForm.view({title: '处理报告', operator: 'modify', id: id});
    });
};

warnInfoJsList.initWarnDic = function () {
    var query_mechType = $('#query_mechType').val();
    $.ajax({
        type: "GET",
        data: {"mechType": query_mechType},
        dataType: "json",
        url: getRootPath() + "/warn/info/dicList.action",
        success: function (data) {
            $("#query_warnReasonCode").empty();
            $("#query_warnReasonCode").append("<option value=''>-请选择-</option>")
            for (var dicIndex = 0; dicIndex < data.length; dicIndex++) {
                $("#query_warnReasonCode").append("<option value='" + data[dicIndex].name + "'>" + data[dicIndex].value + "</option>")
            }
        }
    });
};


$(document).ready(function () {
    warnInfoJsList.pageCount = TabNub > 0 ? TabNub : 10;
    warnInfoJsList.initWarnDic();
    warnInfoJsList.renderTable();
    $('#queryBtn').click(warnInfoJsList.renderTable);
    $('#resetBtn').click(warnInfoJsList.queryReset);
    $("#addBtn").click(warnInfoJsList.loadModuleForAdd);
});