var warnInfoJsList = {};
warnInfoJsList.oTable = null;

warnInfoJsList.oTableFnServerParams = function (aoData) {
    getTableParameters(warnInfoJsList.oTable, aoData);
    var equipmentType = $("#query_mechType").val();
    aoData.push({"name": "equipmentType", "value": equipmentType});
    var query_projectName = $('#searchForm #query_projectName').val();
    if (query_projectName.length > 0) {
        aoData.push({"name": "projectName", "value": query_projectName});
    }
    var query_projectCode = $('#searchForm #query_projectCode').val();
    if (query_projectCode.length > 0) {
        aoData.push({"name": "projectCode", "value": query_projectCode});
    }
};

warnInfoJsList.oTableAoColumns = [
    {mData: 'tableRowNo',  bSortable: false},
    {mData: 'projectCode', bSortable: false},
    {mData: 'projectName', bSortable: false},
    {mData: 'equipmentTypeValue', bSortable: false},
    {mData: 'warnNum', bSortable: false},
    {mData: 'normarlNum', bSortable: false},
    {mData: 'outLineNum', bSortable: false}
];

warnInfoJsList.renderTable = function () {
    if (warnInfoJsList.oTable == null) {
        warnInfoJsList.oTable = $('#gridTable').dataTable({
            "iDisplayLength": warnInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/monit/realtime/manageList.action",
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

warnInfoJsList.queryReset = function () {
    $('#searchForm')[0].reset();
};


warnInfoJsList.loadDetail = function (projectCode, projectName, equipmentType) {
    var url = getRootPath() + "/monit/realtime/detail.action?projectCode=" + projectCode + "&projectName=" + projectName + "&equipmentType=" + equipmentType + "&n_=" + (new Date().getTime());
    window.open(encodeURI(url))
};

$(document).ready(function () {
    warnInfoJsList.pageCount = TabNub > 0 ? TabNub : 10;
    warnInfoJsList.renderTable();
    $('#queryBtn').click(warnInfoJsList.renderTable);
    $('#resetBtn').click(warnInfoJsList.queryReset);
});