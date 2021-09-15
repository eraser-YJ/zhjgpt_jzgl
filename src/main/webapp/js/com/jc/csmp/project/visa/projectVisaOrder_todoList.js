var projectVisaOrderJsTodoList = {};
projectVisaOrderJsTodoList.pageRows = null;
projectVisaOrderJsTodoList.subState = false;
projectVisaOrderJsTodoList.oTable = null;

projectVisaOrderJsTodoList.oTableFnServerParams = function(aoData){
    getTableParameters(projectVisaOrderJsTodoList.oTable, aoData);
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
};

projectVisaOrderJsTodoList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'code', sTitle: '签证编号', bSortable: false},
    {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
    {mData: 'projectName', sTitle: '所属项目', bSortable: false},
    {mData: 'contractCode', sTitle: '合同编号', bSortable: false},
    {mData: 'contractName', sTitle: '所属合同', bSortable: false},
    {mData: 'modifyTypeValue', sTitle: '变更类型', bSortable: false},
    {mData: 'visaReason', sTitle: '签证原因', bSortable: false},
    {mData: 'visaDate', sTitle: '发生时间', bSortable: false},
    {mData: 'visaAmount', sTitle: '签证费用', bSortable: false},
	{mData: function(source) {
        var buttonStr = "<a class='a-icon i-new' onclick=projectVisaOrderJsTodoList.openForm('/instance/toOpenForm.action?definitionId_="+source.workflowBean.definitionId_+"&business_Key_="+source.workflowBean.business_Key_+"&curNodeId_="+source.workflowBean.curNodeId_+"&taskId_="+source.workflowBean.taskId_+"&instanceId_="+source.workflowBean.instanceId_+"&openType_=TODO') href='javascript:void(0)' >办理</a>";
        buttonStr += "<a class='a-icon i-new' target='_blank' href='"+getRootPath()+"/workflowHistory/showHistory.action?instanceId="+source.workflowBean.instanceId_+"&definitionId="+source.workflowBean.definitionId_+"' >流程历史</a>";
        return buttonStr;
	}, sTitle: '操作', bSortable: false, sWidth: 145}
];

projectVisaOrderJsTodoList.openForm = function(url){
    JCFF.loadPage({url:url});
};

projectVisaOrderJsTodoList.renderTable = function () {
    $.ajaxSetup ({ cache: false });
    if (projectVisaOrderJsTodoList.oTable == null) {
        projectVisaOrderJsTodoList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": projectVisaOrderJsTodoList.pageCount,
            "sAjaxSource": getRootPath() + "/project/visa/manageTodoList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectVisaOrderJsTodoList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectVisaOrderJsTodoList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectVisaOrderJsTodoList.oTable.fnDraw();
    }
};

projectVisaOrderJsTodoList.queryReset = function(){
    $('#searchForm')[0].reset();
};

$(document).ready(function(){
    $(".datepicker-input").datepicker();
    projectVisaOrderJsTodoList.pageCount = TabNub > 0 ? TabNub : 10;
    projectVisaOrderJsTodoList.renderTable();
    $('#queryBtn').click(projectVisaOrderJsTodoList.renderTable);
    $('#resetBtn').click(projectVisaOrderJsTodoList.queryReset);
});