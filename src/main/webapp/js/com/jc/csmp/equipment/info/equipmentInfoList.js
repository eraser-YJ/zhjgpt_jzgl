var equipmentInfoJsList = {};
equipmentInfoJsList.oTable = null;

equipmentInfoJsList.oTableFnServerParams = function (aoData) {
    getTableParameters(equipmentInfoJsList.oTable, aoData);
    aoData.push({"name": "dataStatus", "value": "1"});
    var query_equipmentType = $('#searchForm #query_equipmentType').val();
    if (query_equipmentType.length > 0) {
        aoData.push({"name": "equipmentType", "value": query_equipmentType});
    }
    var equipmentCodeCondObj = $('#searchForm #query_equipmentCode').val();
    if (equipmentCodeCondObj.length > 0) {
        aoData.push({"name": "equipmentCode", "value": equipmentCodeCondObj});
    }

    var query_projectCode = $('#searchForm #query_projectCode').val();
    if (query_projectCode.length > 0) {
        aoData.push({"name": "projectCode", "value": query_projectCode});
    }
    var query_projectName = $('#searchForm #query_projectName').val();
    if (query_projectName.length > 0) {
        aoData.push({"name": "projectName", "value": query_projectName});
    }
};

equipmentInfoJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
    {mData: 'projectCode', sTitle: '项目编码', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'equipmentTypeValue', sTitle: '设备类型', bSortable: false},
    {mData: 'equipmentCode', sTitle: '设备编码', bSortable: false},
    {mData: 'equipmentName', sTitle: '设备名称', bSortable: false},
    {mData: 'longitude', sTitle: '经度', bSortable: false},
    {mData: 'latitude', sTitle: '纬度', bSortable: false},
    {
        mData: function (source) {
            var detail = "<a class=\"a-icon i-cemt\"  href=\"#myModal-detail-edit\"  onclick=\"equipmentInfoJsList.loadModuleForDetail('" + source.id + "','" + source.equipmentType + "')\" role=\"button\">详细</a>";
            var edit = "<a class=\"a-icon i-edit\"  href=\"#myModal-edit\"  onclick=\"equipmentInfoJsList.loadModuleForUpdate('" + source.id + "')\" role=\"button\">编辑</a>";
            var del = "<a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"equipmentInfoJsList.delete('" + source.id + "')\">删除</a>";
            return detail + edit + del;
        }, sTitle: '操作', bSortable: false, sWidth: 170
    }
];

equipmentInfoJsList.renderTable = function () {
    if (equipmentInfoJsList.oTable == null) {
        equipmentInfoJsList.oTable = $('#gridTable').dataTable({
            "iDisplayLength": equipmentInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/equipment/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": equipmentInfoJsList.oTableAoColumns,
            "fnServerParams": function (aoData) {
                equipmentInfoJsList.oTableFnServerParams(aoData);
            },
            aaSorting: [],
            aoColumnDefs: []
        });
    } else {
        equipmentInfoJsList.oTable.fnDraw();
    }
};

equipmentInfoJsList.delete = function (id) {
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
            equipmentInfoJsList.deleteCallBack(ids);
        }
    });
};

equipmentInfoJsList.deleteCallBack = function (ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/equipment/info/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success: function (data) {
            if (data.success == "true") {
                msgBox.tip({type: "success", content: data.successMessage});
            } else {
                msgBox.info({content: data.errorMessage});
            }
            equipmentInfoJsList.renderTable();
        }
    });
};

equipmentInfoJsList.queryReset = function () {
    $('#searchForm')[0].reset();
};

equipmentInfoJsList.loadModuleForAdd = function () {
    window.location.href = getRootPath() + "/equipment/info/loadForm.action?n_=" + (new Date()).getTime();
};

equipmentInfoJsList.loadModuleForUpdate = function (id) {
    window.location.href = getRootPath() + "/equipment/info/loadForm.action?id=" + id + "&n_=" + (new Date()).getTime();
};
equipmentInfoJsList.loadModuleForDetail = function (id, type) {
    $("#detailModule").load(getRootPath() + "/equipment/exinfo/loadInfo.action?id=" + id + "&type=" + type + "&n_=" + (new Date()).getTime(), null, function () {
    });
};
$(document).ready(function () {
    equipmentInfoJsList.pageCount = TabNub > 0 ? TabNub : 10;
    equipmentInfoJsList.renderTable();
    $('#queryBtn').click(equipmentInfoJsList.renderTable);
    $('#resetBtn').click(equipmentInfoJsList.queryReset);
    $("#addBtn").click(equipmentInfoJsList.loadModuleForAdd);
});