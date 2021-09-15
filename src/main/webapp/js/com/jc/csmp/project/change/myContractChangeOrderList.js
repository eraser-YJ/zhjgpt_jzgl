var projectChangeOrderJsList = {};
projectChangeOrderJsList.oTable = null;

projectChangeOrderJsList.oTableFnServerParams = function(aoData){
    getTableParameters(projectChangeOrderJsList.oTable, aoData);
    var projectNumber = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({name: "projectNumber", value: projectNumber}); }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({name: "projectName", value: projectName}); }
    var changeDateBegin = $('#searchForm #changeDateBegin').val();
    if (changeDateBegin.length > 0) { aoData.push({name: "changeDateBegin", value: changeDateBegin}); }
    var changeDateEnd = $('#searchForm #changeDateEnd').val();
    if (changeDateEnd.length > 0) { aoData.push({name: "changeDateEnd", value: changeDateEnd}); }
    var code = $('#searchForm #code').val();
    if (code.length > 0) { aoData.push({name: "code", value: code}); }
    var contractCode = $('#searchForm #contractCode').val();
    if (contractCode.length > 0) { aoData.push({name: "contractCode", value: contractCode}); }
    var contractName = $('#searchForm #contractName').val();
    if (contractName.length > 0) { aoData.push({name: "contractName", value: contractName}); }
    var auditStatus = $('#searchForm #auditStatus').val();
    if (auditStatus.length > 0) { aoData.push({name: "auditStatus", value: auditStatus}); }
    var createUser = $('#searchForm #createUser').val();
    if (createUser.length > 0) { aoData.push({name: "createUser", value: createUser}); }
    aoData.push({name: "changeType", value: "1"});
};

projectChangeOrderJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'changeDate', sTitle: '变更日期', bSortable: false},
    {mData: 'code', sTitle: '变更单编号', bSortable: false},
    {mData: 'modifyType', sTitle: '变更类型', bSortable: false, mRender : function(mData, type, full) { return full.modifyTypeValue; }},
    {mData: 'deptName', sTitle: '申请单位', bSortable: false},
    {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'contractCode', sTitle: '合同编号', bSortable: false},
    {mData: 'contractName', sTitle: '合同名称', bSortable: false},
    {mData: 'handleUser', sTitle: '经办人', bSortable: false},
    {mData: 'auditStatus', sTitle: '审核状态', bSortable: false, mRender : function(mData, type, full) { return full.auditStatusValue; }},
	{mData: function(source) {
	    var look = "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"projectChangeOrderJsList.loadModuleForLook('"+ source.id+ "', '" + source.changeType + "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
        var history = "<a class=\"a-icon i-edit\" href=\"javascript:void(0);\" onclick=\"footerToolsModule.workflowHistory('"+ source.piId+ "')\" role=\"button\">流程历史</a>";
        return look + history;
    }, sTitle: '操作', bSortable: false, sWidth: 150}
];

projectChangeOrderJsList.renderTable = function () {
    if (projectChangeOrderJsList.oTable == null) {
        projectChangeOrderJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": projectChangeOrderJsList.pageCount,
            "sAjaxSource": getRootPath() + "/project/change/searchList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectChangeOrderJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectChangeOrderJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectChangeOrderJsList.oTable.fnDraw();
    }
};

projectChangeOrderJsList.loadModuleForLook = function (id, changeType){
    $("#formModule").load(getRootPath() + "/project/change/loadForm.action?changeType=" + changeType, null, function() {
        projectChangeOrderJsForm.init({title: '查看变更单', id: id});
    });
};

projectChangeOrderJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

$(document).ready(function(){
    projectChangeOrderJsList.pageCount = TabNub > 0 ? TabNub : 10;
    projectChangeOrderJsList.renderTable();
    $('#queryBtn').click(projectChangeOrderJsList.renderTable);
    $('#resetBtn').click(projectChangeOrderJsList.queryReset);
    $('#addBtn').click(function () {
        JCFF.loadPage({url: "/instance/toStartProcess.action?definitionKey_=contractChangeOrder"});
    });
});