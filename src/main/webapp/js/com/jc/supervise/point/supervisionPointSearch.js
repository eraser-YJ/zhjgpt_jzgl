var supervisionPointSearchPanel = {};
supervisionPointSearchPanel.oTable = null;

supervisionPointSearchPanel.oTableFnServerParams = function(aoData){
    getTableParameters(supervisionPointSearchPanel.oTable, aoData);
    var pointName  = $('#searchForm #pointName').val();
    if (pointName.length > 0) { aoData.push({"name": "pointName", "value": pointName}); }
};

supervisionPointSearchPanel.oTableAoColumns = [
    {mData: 'pointName', sTitle: '监测项', bSortable: false},
    {mData: 'pointDesc', sTitle: '描述', bSortable: false},
    {mData: function(source) {
        return "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"supervisionPointSearchPanel.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">查看</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 100}
];

supervisionPointSearchPanel.renderTable = function () {
    if (supervisionPointSearchPanel.oTable == null) {
        supervisionPointSearchPanel.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": supervisionPointSearchPanel.pageCount,
            "sAjaxSource": getRootPath() + "/supervise/point/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": supervisionPointSearchPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                supervisionPointSearchPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        supervisionPointSearchPanel.oTable.fnDraw();
    }
};

supervisionPointSearchPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

supervisionPointSearchPanel.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/supervise/point/loadForm.action", null, function() {
        supervisionPointFormPanel.init({title: '修改监测项', operator: 'look', id: id});
    });
};

$(document).ready(function(){
    supervisionPointSearchPanel.pageCount = TabNub > 0 ? TabNub : 10;
    supervisionPointSearchPanel.renderTable();
    $('#queryBtn').click(supervisionPointSearchPanel.renderTable);
    $('#resetBtn').click(supervisionPointSearchPanel.queryReset);
});