var projectVisaOrderJsList = {};
projectVisaOrderJsList.oTable = null;

projectVisaOrderJsList.oTableFnServerParams = function(aoData){
    getTableParameters(projectVisaOrderJsList.oTable, aoData);
    var projectNumber = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({name: "projectNumber", value: projectNumber}); }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({name: "projectName", value: projectName}); }
    var contractCode = $('#searchForm #contractCode').val();
    if (contractCode.length > 0) { aoData.push({name: "contractCode", value: contractCode}); }
    var contractName = $('#searchForm #contractName').val();
    if (contractName.length > 0) { aoData.push({name: "contractName", value: contractName}); }
    var visaDateBegin = $('#searchForm #visaDateBegin').val();
    if (visaDateBegin.length > 0) { aoData.push({name: "visaDateBegin", value: visaDateBegin}); }
    var visaDateEnd = $('#searchForm #visaDateEnd').val();
    if (visaDateEnd.length > 0) { aoData.push({name: "visaDateEnd", value: visaDateEnd}); }
    var code = $('#searchForm #code').val();
    if (code.length > 0) { aoData.push({name: "code", value: code}); }
    var auditStatus = $('#searchForm #auditStatus').val();
    if (auditStatus.length > 0) { aoData.push({name: "auditStatus", value: auditStatus}); }
    var createUser = $('#searchForm #createUser').val();
    if (createUser.length > 0) { aoData.push({name: "createUser", value: createUser}); }
};

projectVisaOrderJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'code', sTitle: '签证编号', bSortable: false},
    {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
    {mData: 'projectName', sTitle: '所属项目', bSortable: false},
    {mData: 'contractCode', sTitle: '合同编号', bSortable: false},
    {mData: 'contractName', sTitle: '所属合同', bSortable: false},
    {mData: 'modifyTypeValue', sTitle: '变更类型', bSortable: false},
    {mData: 'visaReason', sTitle: '签证原因', bSortable: false},
    {mData: 'visaDate', sTitle: '发生时间', bSortable: false},
    {mData: 'auditStatus', sTitle: '审核状态', bSortable: false, mRender : function(mData, type, full) { return full.auditStatusValue; }},
	{mData: function(source) {
        var look = "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"projectVisaOrderJsList.loadModuleForLook('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
        var history = "<a class=\"a-icon i-edit\" href=\"javascript:void(0);\" onclick=\"footerToolsModule.workflowHistory('"+ source.piId+ "')\" role=\"button\">流程历史</a>";
        return look + history;
    }, sTitle: '操作', bSortable: false, sWidth: 150}
];

projectVisaOrderJsList.renderTable = function () {
    if (projectVisaOrderJsList.oTable == null) {
        projectVisaOrderJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": projectVisaOrderJsList.pageCount,
            "sAjaxSource": getRootPath() + "/project/visa/searchList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectVisaOrderJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectVisaOrderJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectVisaOrderJsList.oTable.fnDraw();
    }
};

projectVisaOrderJsList.loadModuleForLook = function (id){
    $("#formModule").load(getRootPath() + "/project/visa/loadForm.action", null, function() {
        projectVisaOrderJsForm.init({title: '查看签证单', id: id});
    });
};

projectVisaOrderJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

$(document).ready(function(){
    projectVisaOrderJsList.pageCount = TabNub > 0 ? TabNub : 10;
    projectVisaOrderJsList.renderTable();
    $('#queryBtn').click(projectVisaOrderJsList.renderTable);
    $('#resetBtn').click(projectVisaOrderJsList.queryReset);
    if ($('#searchForm #createUser').val() != "") {
        $('#crumbsHeaderTitle').html("<span>项目管理</span><span>变更及签证管理 > </span><span>签证管理 > </span><span>工程签证单申请</span>");
        $('#addBtn').show();
        $('#addBtn').click(function () {
            JCFF.loadPage({url: "/instance/toStartProcess.action?definitionKey_=projectVisaOrder"});
        });
    } else {
        $('#crumbsHeaderTitle').html("<span>项目管理</span><span>变更及签证管理 > </span><span>签证管理 > </span><span>工程签证单查询</span>");
    }
});