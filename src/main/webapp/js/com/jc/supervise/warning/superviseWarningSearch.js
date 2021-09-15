var superviseWarningSearchPanel = {};
superviseWarningSearchPanel.oTable = null;

superviseWarningSearchPanel.oTableFnServerParams = function(aoData){
    getTableParameters(superviseWarningSearchPanel.oTable, aoData);
    var projectNumber = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({"name": "projectNumber", "value": projectNumber}); }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({"name": "projectName", "value": projectName}); }
    var status = $('#searchForm #status').val();
    if (status.length > 0) { aoData.push({name: 'status', value: status}); }
};

superviseWarningSearchPanel.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'supervisionDate', sTitle: '监测时间', bSortable: false},
    {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'supervisionPointName', sTitle: '预警事项', bSortable: false},
    {mData: 'status', sTitle: '状态', bSortable: false, mRender : function(mData, type, full) { return full.statusValue; }},
    {mData: function(source) {
        var look = "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"superviseWarningSearchPanel.look('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
        return look;
    }, sTitle: '操作', bSortable: false, sWidth: 100}
];

superviseWarningSearchPanel.renderTable = function () {
    if (superviseWarningSearchPanel.oTable == null) {
        superviseWarningSearchPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": superviseWarningSearchPanel.pageCount,
            "sAjaxSource": getRootPath() + "/supervision/warning/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": superviseWarningSearchPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                superviseWarningSearchPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        superviseWarningSearchPanel.oTable.fnDraw();
    }
};

superviseWarningSearchPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

superviseWarningSearchPanel.look = function (id){
    $("#formModule").load(getRootPath() + "/supervision/warning/loadForm.action", null, function() {
        superviseWarningFormPanel.init({title: '查看预警纠错信息', operator: 'look', id: id});
    });
};

$(document).ready(function(){
    superviseWarningSearchPanel.pageCount = TabNub > 0 ? TabNub : 10;
    superviseWarningSearchPanel.renderTable();
    $('#queryBtn').click(superviseWarningSearchPanel.renderTable);
    $('#resetBtn').click(superviseWarningSearchPanel.queryReset);
});