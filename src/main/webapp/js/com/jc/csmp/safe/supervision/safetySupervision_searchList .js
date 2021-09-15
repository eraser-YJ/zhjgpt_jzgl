var safetySupervisionJsTodoList = {};
safetySupervisionJsTodoList.pageRows = null;
//重复提交标识
safetySupervisionJsTodoList.subState = false;
//分页对象
safetySupervisionJsTodoList.oTable = null;
safetySupervisionJsTodoList.oTableFnServerParams = function(aoData){
    getTableParameters(safetySupervisionJsTodoList.oTable, aoData);

    var projectNameCondObj  = $('#searchForm #query_projectName').val();
    if (projectNameCondObj.length > 0) { aoData.push({"name": "projectName", "value": projectNameCondObj}); }
    var buildPropertiesCondObj  = $('#searchForm #query_buildProperties').val();
    if (buildPropertiesCondObj.length > 0) { aoData.push({"name": "buildProperties", "value": buildPropertiesCondObj}); }
    var investmentCategoryCondObj  = $('#searchForm #query_investmentCategory').val();
    if (investmentCategoryCondObj.length > 0) { aoData.push({"name": "investmentCategory", "value": investmentCategoryCondObj}); }
    var projectTypeCondObj  = $('#searchForm #query_projectType').val();
    if (projectTypeCondObj.length > 0) { aoData.push({"name": "projectType", "value": projectTypeCondObj}); }
    var structureTypeCondObj  = $('#searchForm #query_structureType').val();
    if (structureTypeCondObj.length > 0) { aoData.push({"name": "structureType", "value": structureTypeCondObj}); }
    var createDateBegin = $("#searchForm #createDateBegin").val();
    if(createDateBegin.length > 0){ aoData.push({ "name": "createDateBegin", "value": createDateBegin}); }
    var createDateEnd = $("#searchForm #createDateEnd").val();
    if(createDateEnd.length > 0){ aoData.push({ "name": "createDateEnd", "value": createDateEnd}); }

};
debugger;
safetySupervisionJsTodoList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'projectName', sTitle: '工程名称', bSortable: false},
    {mData: 'projectAddress', sTitle: '工程地址', bSortable: false},
    {mData: 'buildProperties', sTitle: '建设性质', bSortable: false, mRender : function(mData, type, full) { return full.buildPropertiesValue; }},
    {mData: 'investmentCategory', sTitle: '投资类别', bSortable: false, mRender : function(mData, type, full) { return full.investmentCategoryValue; }},
    {mData: 'buildArea', sTitle: '建筑面积', bSortable: false},
    {mData: 'investmentAmount', sTitle: '合同造价', bSortable: false},
    {mData: 'projectType', sTitle: '工程类型', bSortable: false ,mRender : function(mData, type, full) { return full.projectTypeValue; }},
    {mData: 'structureType', sTitle: '结构类型', bSortable: false,mRender : function(mData, type, full) { return full.structureTypeValue; }},
    {mData: 'planStartDate', sTitle: '开工日期', bSortable: false},
    {mData: 'planEndDate', sTitle: '竣工日期', bSortable: false},

	{mData: function(source) {

        var buttonStr = "<a class='a-icon i-new' onclick=safetySupervisionJsTodoList.openForm('/instance/toOpenForm.action?definitionId_="+source.workflowBean.definitionId_+"&business_Key_="+source.workflowBean.business_Key_+"&curNodeId_="+source.workflowBean.curNodeId_+"&taskId_="+source.workflowBean.taskId_+"&instanceId_="+source.workflowBean.instanceId_+"&openType_=TODO') href='javascript:void(0)' >办理</a>";
         buttonStr += "<a class='a-icon i-new' target='_blank' href='"+getRootPath()+"/workflowHistory/showHistory.action?instanceId="+source.workflowBean.instanceId_+"&definitionId="+source.workflowBean.definitionId_+"' >流程历史</a>";
        return buttonStr;
	}, sTitle: '操作', bSortable: false, sWidth: 170}
];

safetySupervisionJsTodoList.openForm = function(url){
    JCFF.loadPage({url:url});
}

safetySupervisionJsTodoList.renderTable = function () {
    $.ajaxSetup ({
        cache: false //设置成false将不会从浏览器缓存读取信息
    });
    if (safetySupervisionJsTodoList.oTable == null) {
        safetySupervisionJsTodoList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": safetySupervisionJsTodoList.pageCount,
            "sAjaxSource": getRootPath() + "/safe/supervision/manageWorkflowList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": safetySupervisionJsTodoList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                safetySupervisionJsTodoList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        safetySupervisionJsTodoList.oTable.fnDraw();
    }
};


safetySupervisionJsTodoList.queryReset = function(){
    $('#searchForm')[0].reset();
};


$(document).ready(function(){
    safetySupervisionJsTodoList.pageCount = TabNub > 0 ? TabNub : 10;
    safetySupervisionJsTodoList.renderTable();
    $('#queryBtn').click(safetySupervisionJsTodoList.renderTable);
    $('#resetBtn').click(safetySupervisionJsTodoList.queryReset);
});