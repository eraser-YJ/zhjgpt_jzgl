var superviseSafeListPanel = {};
superviseSafeListPanel.oTable = null;

superviseSafeListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(superviseSafeListPanel.oTable, aoData);
    var projectNumber = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({name: "projectNumber", value: projectNumber}); }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({name: "projectName", value: projectName}); }
};

superviseSafeListPanel.oTableAoColumns = [
    {mData: 'no', sTitle: '序号', bSortable: false},
    {mData: 'code', sTitle: '编号', bSortable: false},
    {mData: 'createDateFormat', sTitle: '登记时间', bSortable: false},
    {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'contractCode', sTitle: '合同编号', bSortable: false},
    {mData: 'contractName', sTitle: '合同名称', bSortable: false},
    {mData: 'title', sTitle: '标题', bSortable: false, sWidth: 200},
    {mData: 'rectificationCompany', sTitle: '整改单位', bSortable: false, mRender : function(mData, type, full) { return full.rectificationCompanyValue; }},
    {mData: function(source) {
        return "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"superviseSafeListPanel.loadModuleForLook('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 60}
];

superviseSafeListPanel.renderTable = function () {
    if (superviseSafeListPanel.oTable == null) {
        superviseSafeListPanel.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": superviseSafeListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/supervise/basic/safeList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": superviseSafeListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                superviseSafeListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        superviseSafeListPanel.oTable.fnDraw();
    }
};

superviseSafeListPanel.loadModuleForLook = function (id){
    $("#formModule").load(getRootPath() + "/project/question/loadForm.action", null, function() {
        projectQuestionFormPanel.init({title: '查看问题单', id: id});
    });
};

superviseSafeListPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

$(document).ready(function(){
    superviseSafeListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    superviseSafeListPanel.renderTable();
    $('#queryBtn').click(superviseSafeListPanel.renderTable);
    $('#resetBtn').click(superviseSafeListPanel.queryReset);
});