var warnInfoJsList = {};
warnInfoJsList.oTable = null;

warnInfoJsList.oTableFnServerParams = function (aoData) {
    getTableParameters(warnInfoJsList.oTable, aoData);
    var equiId = $('#query_equiId').val();
    aoData.push({"name": "equiId", "value": equiId});
    var runTimeBegin = $('#searchForm #query_runTimeBegin').val();
    if (runTimeBegin.length > 0) {
        aoData.push({"name": "runTimeBegin", "value": runTimeBegin});
    }
    var runTimeEnd = $('#searchForm #query_runTimeEnd').val();
    if (runTimeEnd.length > 0) {
        aoData.push({"name": "runTimeEnd", "value": runTimeEnd});
    }
};

warnInfoJsList.oTableAoColumns = [
    {mData: 'runTime', sTitle: '运行时间', bSortable: false, sWidth: 170},
    {
        mData: function (source) {
            delete source._id
            // delete source.runTime
            return JSON.stringify(source);
        }, sTitle: '运行数据', bSortable: false
    }
];
warnInfoJsList.renderTable = function () {
    if (warnInfoJsList.oTable == null) {
        warnInfoJsList.oTable = $('#gridTable').dataTable({
            "iDisplayLength": warnInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/run/info/manageList.action",
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

$(document).ready(function () {
    warnInfoJsList.pageCount = TabNub > 0 ? TabNub : 10;
    warnInfoJsList.renderTable();
    $('#queryBtn').click(warnInfoJsList.renderTable);
    $('#resetBtn').click(warnInfoJsList.queryReset);
    $("#addBtn").click(warnInfoJsList.loadModuleForAdd);
});