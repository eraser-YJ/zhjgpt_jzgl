var lngLatPanel = {};
lngLatPanel.oTable = null;

lngLatPanel.oTableFnServerParams = function(aoData){
    getTableParameters(lngLatPanel.oTable, aoData);
    var insert = $('#searchForm #insert').val();
    if (insert != undefined && insert.length > 0) { aoData.push({"name": "userId", "value": insert}); }
    var createDateBegin = $('#searchForm #createDateBegin').val();
    if (createDateBegin.length > 0) { aoData.push({"name": "createDateBegin", "value": createDateBegin}); }
    var createDateEnd = $('#searchForm #createDateEnd').val();
    if (createDateEnd.length > 0) { aoData.push({"name": "createDateEnd", "value": createDateEnd}); }
};

lngLatPanel.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'createDate', sTitle: '上报时间', bSortable: false},
    {mData: 'userName', sTitle: '上报人', bSortable: false},
    {mData: 'projectNumber', sTitle: '项目统一编码', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'lng', sTitle: '经度', bSortable: false},
    {mData: 'lat', sTitle: '纬度', bSortable: false},
    {mData: 'juli', sTitle: '与工地距离(米)', bSortable: false}
];

lngLatPanel.renderTable = function () {
    if (lngLatPanel.oTable == null) {
        lngLatPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": lngLatPanel.pageCount,
            "sAjaxSource": getRootPath() + "/project/personlnglat/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": lngLatPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                lngLatPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        lngLatPanel.oTable.fnDraw();
    }
};

lngLatPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

$(document).ready(function(){
    lngLatPanel.pageCount = TabNub > 0 ? TabNub : 10;
    lngLatPanel.renderTable();
    $('#queryBtn').click(lngLatPanel.renderTable);
    $('#resetBtn').click(lngLatPanel.queryReset);
    lngLatPanel.userIdJcTree = new JCFF.JCTree.Lazy({
        title: '人员选择树',
        container: 'userIdFormDiv',
        controlId: 'insert-userId',
        single: true,
        ready: function(){
            //deptDefer.resolve();
        },
        funFormData: function(){
            return { weight : '0' }
        }
    });
});