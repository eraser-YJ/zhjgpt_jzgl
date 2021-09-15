var superviseWarningListPanel = {};
superviseWarningListPanel.oTable = null;

superviseWarningListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(superviseWarningListPanel.oTable, aoData);
    var projectNumber = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({"name": "projectNumber", "value": projectNumber}); }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({"name": "projectName", "value": projectName}); }
    var status = $('#searchForm #status').val();
    if (status.length > 0) { aoData.push({name: 'status', value: status}); }
};

superviseWarningListPanel.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'supervisionDate', sTitle: '监测时间', bSortable: false},
    {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'supervisionPointName', sTitle: '预警原因', bSortable: false},
    {mData: 'status', sTitle: '状态', bSortable: false, mRender : function(mData, type, full) { return full.statusValue; }},
    {mData: function(source) {
        var message = "<a class=\"a-icon i-remove\" href=\"javascript:void(0);\" onclick=\"superviseWarningListPanel.message('"+ source.id+ "')\">" + finalTableBtnText('发起督办', 'fa-chat') + "</a>";
        var look = "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"superviseWarningListPanel.look('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
        var edit = "<a class=\"a-icon i-cemt\" href=\"javascript:void(0);\" onclick=\"superviseWarningListPanel.dispose('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('处理', 'fa-edit2') + "</a>";
        if (source.status == 'finish') {
            edit = ""; message = "";
        }
        return message + look + edit;
    }, sTitle: '操作', bSortable: false, sWidth: 125}
];

superviseWarningListPanel.renderTable = function () {
    if (superviseWarningListPanel.oTable == null) {
        superviseWarningListPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": superviseWarningListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/supervision/warning/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": superviseWarningListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                superviseWarningListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        superviseWarningListPanel.oTable.fnDraw();
    }
};

superviseWarningListPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

superviseWarningListPanel.message = function(id) {
    $("#formModule").load(getRootPath() + "/supervision/message/sendForm.action", null, function() {
        superviseSendMessageFormPanel.init({title: '发起督办', warningId: id});
    });
};

superviseWarningListPanel.look = function (id){
    $("#formModule").load(getRootPath() + "/supervision/warning/loadForm.action", null, function() {
        superviseWarningFormPanel.init({title: '查看预警纠错信息', operator: 'look', id: id});
    });
};

superviseWarningListPanel.dispose = function (id) {
    $("#formModule").load(getRootPath() + "/supervision/warning/loadForm.action", null, function() {
        superviseWarningFormPanel.init({title: '处理预警纠错信息', operator: 'dispose', id: id});
    });
};

$(document).ready(function(){
    superviseWarningListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    superviseWarningListPanel.renderTable();
    $('#queryBtn').click(superviseWarningListPanel.renderTable);
    $('#resetBtn').click(superviseWarningListPanel.queryReset);
});