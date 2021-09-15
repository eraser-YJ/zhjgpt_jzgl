var superviseMessageSendListPanel = {};
superviseMessageSendListPanel.oTable = null;

superviseMessageSendListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(superviseMessageSendListPanel.oTable, aoData);
    var handleStatus = $('#searchForm #handleStatus').val();
    if (handleStatus.length > 0) { aoData.push({"name": "handleStatus", "value": handleStatus}); }
};

superviseMessageSendListPanel.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'title', sTitle: '标题', bSortable: false},
    {mData: 'senderId', sTitle: '发送人', bSortable: false, mRender: function(mData, type, full) { return full.senderName; }},
    {mData: 'senderDeptId', sTitle: '发送人所在组织', bSortable: false, mRender: function(mData, type, full) { return full.senderDeptName; }},
    {mData: 'receiveId', sTitle: '接收人', bSortable: false, mRender: function(mData, type, full) { return full.receiveName; }},
    {mData: 'receiveDeptId', sTitle: '接收人所在组织', bSortable: false, mRender: function(mData, type, full) { return full.receiveDeptName; }},
    {mData: 'handleStatus', sTitle: '办理状态', bSortable: false, mRender : function(mData, type, full) { return full.handleStatusValue; }},
    {mData: 'handleDate', sTitle: '办理时间', bSortable: false},
    {mData: function(source) {
        return "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"superviseMessageSendListPanel.look('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 60}
];

superviseMessageSendListPanel.renderTable = function () {
    if (superviseMessageSendListPanel.oTable == null) {
        superviseMessageSendListPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": superviseMessageSendListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/supervision/message/manageSendList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": superviseMessageSendListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                superviseMessageSendListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        superviseMessageSendListPanel.oTable.fnDraw();
    }
};

superviseMessageSendListPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

superviseMessageSendListPanel.look = function (id){
    $("#formModule").load(getRootPath() + "/supervision/message/loadForm.action", null, function() {
        superviseMessageFormPanel.init({title: '查看督办信息', operator: 'look', id: id});
    });
};

$(document).ready(function(){
    superviseMessageSendListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    superviseMessageSendListPanel.renderTable();
    $('#queryBtn').click(superviseMessageSendListPanel.renderTable);
    $('#resetBtn').click(superviseMessageSendListPanel.queryReset);
});