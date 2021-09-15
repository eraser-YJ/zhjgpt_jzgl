var superviseQualityListPanel = {};
superviseQualityListPanel.oTable = null;

superviseQualityListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(superviseQualityListPanel.oTable, aoData);
    var projectNumber = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({name: "projectNumber", value: projectNumber}); }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({name: "projectName", value: projectName}); }
};

superviseQualityListPanel.oTableAoColumns = [
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
        return "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"superviseQualityListPanel.loadModuleForLook('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 60}
];

superviseQualityListPanel.renderTable = function () {
    if (superviseQualityListPanel.oTable == null) {
        superviseQualityListPanel.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": superviseQualityListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/supervise/basic/qualityList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": superviseQualityListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                superviseQualityListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        superviseQualityListPanel.oTable.fnDraw();
    }
};

superviseQualityListPanel.loadModuleForLook = function (id){
    $("#formModule").load(getRootPath() + "/project/question/loadForm.action", null, function() {
        projectQuestionFormPanel.init({title: '查看问题单', id: id});
    });
};

superviseQualityListPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

$(document).ready(function(){
    superviseQualityListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    superviseQualityListPanel.renderTable();
    $('#queryBtn').click(superviseQualityListPanel.renderTable);
    $('#resetBtn').click(superviseQualityListPanel.queryReset);
});