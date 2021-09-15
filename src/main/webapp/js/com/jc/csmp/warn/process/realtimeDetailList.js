var warnInfoJsList = {};

var warnInfoJsList = {};
warnInfoJsList.oTable = null;

warnInfoJsList.oTableFnServerParams = function (aoData) {
    getTableParameters(warnInfoJsList.oTable, aoData);
    aoData.push({"name": "dataStatus", "value": "1"});

    var query_equipmentType = $('#queryEquipmentType').val();
    if (query_equipmentType.length > 0) {
        aoData.push({"name": "equipmentType", "value": query_equipmentType});
    }
    var equipmentCodeCondObj = $('#searchForm #query_equipmentCode').val();
    if (equipmentCodeCondObj.length > 0) {
        aoData.push({"name": "equipmentCode", "value": equipmentCodeCondObj});
    }
    var query_equipmentName = $('#searchForm #query_equipmentName').val();
    if (query_equipmentName.length > 0) {
        aoData.push({"name": "equipmentName", "value": query_equipmentName});
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

warnInfoJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
    {mData: 'projectCode', sTitle: '项目编码', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'equipmentTypeValue', sTitle: '设备类型', bSortable: false},
    {mData: 'equipmentCode', sTitle: '设备编码', bSortable: false},
    {mData: 'equipmentName', sTitle: '设备名称', bSortable: false},
    {mData: 'workStatusValue', sTitle: '状态', bSortable: false},
    {
        mData: function (source) {
           return detailTypeFun.initBut(source);
        }, sTitle: '操作', bSortable: false, sWidth: 200
    }
];

warnInfoJsList.renderTable = function () {
    if (warnInfoJsList.oTable == null) {
        warnInfoJsList.oTable = $('#gridTable').dataTable({
            "iDisplayLength": warnInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/equipment/info/manageList.action",
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

//查询重置
warnInfoJsList.queryReset = function () {
    $('#searchForm')[0].reset();
};

//模拟监控
warnInfoJsList.mockInfoFun = function (equiId) {
    alert("开发中")
};
//运行数据
warnInfoJsList.runInfoFun = function (equiId) {
    window.open(getRootPath() + "/run/info/manage.action?equiId=" + equiId + "&n_" + (new Date().getTime()));
};
//判空
warnInfoJsList.nvl = function (item) {
    if (item) {
        return item;
    }
    return "";
};

$(document).ready(function () {
    warnInfoJsList.pageCount = TabNub > 0 ? TabNub : 10;
    warnInfoJsList.renderTable();
    $('#queryBtn').click(warnInfoJsList.renderTable);
    $('#resetBtn').click(warnInfoJsList.queryReset);
});