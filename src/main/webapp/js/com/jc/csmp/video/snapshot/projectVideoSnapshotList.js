var projectVideoSnapshotJsList = {};
projectVideoSnapshotJsList.oTable = null;


projectVideoSnapshotJsList.oTableFnServerParams = function (aoData) {
    getTableParameters(projectVideoSnapshotJsList.oTable, aoData);
    var equiCodeCondObj = $('#searchForm #query_equiCode').val();
    if (equiCodeCondObj && equiCodeCondObj.length > 0) {
        aoData.push({"name": "equiCode", "value": equiCodeCondObj});
    }
    var createDateCondObjBegin = $('#searchForm #query_createDateBegin').val();
    if (createDateCondObjBegin && createDateCondObjBegin.length > 0) {
        aoData.push({"name": "createDateBegin", "value": createDateCondObjBegin});
    }
    var createDateCondObjEnd = $('#searchForm #query_createDateEnd').val();
    if (createDateCondObjEnd && createDateCondObjEnd.length > 0) {
        aoData.push({"name": "createDateEnd", "value": createDateCondObjEnd});
    }
};

projectVideoSnapshotJsList.oTableAoColumns = [
    {mData: 'equiCode', sTitle: '设备编码', bSortable: false},
    {mData: 'createDate', sTitle: '上传时间', bSortable: false},
    {mData: 'imgPath', sTitle: '存放路径', bSortable: false},
    {
        mData: function (source) {
            var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"projectVideoSnapshotJsList.loadModuleView('" + source.id + "')\" role=\"button\">&nbsp;&nbsp;查看&nbsp;&nbsp;</a>";
            return edit;
        }, sTitle: '操作', bSortable: false, sWidth: 170
    }
];

projectVideoSnapshotJsList.renderTable = function () {
    if (projectVideoSnapshotJsList.oTable == null) {
        projectVideoSnapshotJsList.oTable = $('#gridTable').dataTable({
            "iDisplayLength": projectVideoSnapshotJsList.pageCount,
            "sAjaxSource": getRootPath() + "/video/snapshot/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectVideoSnapshotJsList.oTableAoColumns,
            "fnServerParams": function (aoData) {
                projectVideoSnapshotJsList.oTableFnServerParams(aoData);
            },
            aaSorting: [],
            aoColumnDefs: []
        });
    } else {
        projectVideoSnapshotJsList.oTable.fnDraw();
    }
};

projectVideoSnapshotJsList.delete = function (id) {
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
            projectVideoSnapshotJsList.deleteCallBack(ids);
        }
    });
};

projectVideoSnapshotJsList.deleteCallBack = function (ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/video/snapshot/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success: function (data) {
            if (data.success == "true") {
                msgBox.tip({type: "success", content: data.successMessage});
            } else {
                msgBox.info({content: data.errorMessage});
            }
            projectVideoSnapshotJsList.renderTable();
        }
    });
};

projectVideoSnapshotJsList.queryReset = function () {
    $('#searchForm')[0].reset();
};

projectVideoSnapshotJsList.loadModuleView = function (id) {
    $('#form-modal').modal('show');
};

$(document).ready(function () {
    projectVideoSnapshotJsList.pageCount = TabNub > 0 ? TabNub : 10;
    projectVideoSnapshotJsList.renderTable();
    $('#queryBtn').click(projectVideoSnapshotJsList.renderTable);
    $('#resetBtn').click(projectVideoSnapshotJsList.queryReset);
});