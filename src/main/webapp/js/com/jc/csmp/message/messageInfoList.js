var messageInfoListPanel = {};
messageInfoListPanel.oTable = null;

messageInfoListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(messageInfoListPanel.oTable, aoData);
};

messageInfoListPanel.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'createDate', sTitle: '发送时间', bSortable: false},
    {mData: 'title', sTitle: '标题', bSortable: false},
    {mData: 'content', sTitle: '内容', bSortable: false},
    {mData: 'readStatus', sTitle: '阅读状态', bSortable: false, mRender: function(mData, type, full) { return full.readStatusValue; }},
    {mData: 'readDate', sTitle: '阅读时间', bSortable: false},
    {mData: function(source) {
        var look = "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"messageInfoListPanel.look('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
        return look;
    }, sTitle: '操作', bSortable: false, sWidth: 60}
];

messageInfoListPanel.renderTable = function () {
    if (messageInfoListPanel.oTable == null) {
        messageInfoListPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": messageInfoListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/message/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": messageInfoListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                messageInfoListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        messageInfoListPanel.oTable.fnDraw();
    }
};

messageInfoListPanel.look = function (id){
    $("#formModule").load(getRootPath() + "/message/info/loadForm.action", null, function() {
        messageInfoFormPanel.init({id: id});
    });
};

$(document).ready(function(){
    messageInfoListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    messageInfoListPanel.renderTable();
});