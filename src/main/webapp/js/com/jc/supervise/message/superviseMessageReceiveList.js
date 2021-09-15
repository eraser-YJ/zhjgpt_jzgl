var superviseMessageReceiveListPanel = {};
superviseMessageReceiveListPanel.oTable = null;

superviseMessageReceiveListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(superviseMessageReceiveListPanel.oTable, aoData);
    var handleStatus = $('#searchForm #handleStatus').val();
    if (handleStatus.length > 0) { aoData.push({"name": "handleStatus", "value": handleStatus}); }
    var title = $('#searchForm #title').val();
    if (title.length > 0) { aoData.push({"name": "title", "value": title}); }
};

superviseMessageReceiveListPanel.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'title', sTitle: '标题', bSortable: false},
    {mData: 'senderId', sTitle: '发送人', bSortable: false, mRender: function(mData, type, full) { return full.senderName; }},
    {mData: 'senderDeptId', sTitle: '发送人所在组织', bSortable: false, mRender: function(mData, type, full) { return full.senderDeptName; }},
    {mData: 'receiveId', sTitle: '接收人', bSortable: false, mRender: function(mData, type, full) { return full.receiveName; }},
    {mData: 'receiveDeptId', sTitle: '接收人所在组织', bSortable: false, mRender: function(mData, type, full) { return full.receiveDeptName; }},
    {mData: 'handleStatus', sTitle: '办理状态', bSortable: false, mRender : function(mData, type, full) { return full.handleStatusValue; }},
    {mData: 'handleDate', sTitle: '办理时间', bSortable: false},
    {mData: function(source) {
        var button = "<a class=\"a-icon i-cemt\" href=\"javascript:void(0);\" onclick=\"superviseMessageReceiveListPanel.dispose('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('处理', 'fa-edit2') + "</a>";
        if (source.handleStatus == '1') {
            button = "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"superviseMessageReceiveListPanel.look('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
        }
        return button;
    }, sTitle: '操作', bSortable: false, sWidth: 60}
];

superviseMessageReceiveListPanel.renderTable = function () {
    if (superviseMessageReceiveListPanel.oTable == null) {
        superviseMessageReceiveListPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": superviseMessageReceiveListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/supervision/message/manageReceiveList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": superviseMessageReceiveListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                superviseMessageReceiveListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        superviseMessageReceiveListPanel.oTable.fnDraw();
    }
};

superviseMessageReceiveListPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

superviseMessageReceiveListPanel.look = function (id){
    $("#formModule").load(getRootPath() + "/supervision/message/loadForm.action", null, function() {
        superviseMessageFormPanel.init({title: '查看督办信息', operator: 'look', id: id});
    });
};

superviseMessageReceiveListPanel.dispose = function (id) {
    $("#formModule").load(getRootPath() + "/supervision/message/loadForm.action", null, function() {
        superviseMessageFormPanel.init({title: '办理督办信息', operator: 'dispose', id: id, callback: function () {
            superviseMessageReceiveListPanel.renderTable();
        }});
    });
};

$(document).ready(function(){
    superviseMessageReceiveListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    superviseMessageReceiveListPanel.renderTable();
    $('#queryBtn').click(superviseMessageReceiveListPanel.renderTable);
    $('#resetBtn').click(superviseMessageReceiveListPanel.queryReset);
});