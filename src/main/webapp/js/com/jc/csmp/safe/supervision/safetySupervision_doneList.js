var safetySupervisionJsDoneList = {};
safetySupervisionJsDoneList.pageRows = null;
//重复提交标识
safetySupervisionJsDoneList.subState = false;
//分页对象
safetySupervisionJsDoneList.oTable = null;

safetySupervisionJsDoneList.oTableFnServerParams = function(aoData){
    getTableParameters(safetySupervisionJsDoneList.oTable, aoData);
    var projectNameCondObj  = $('#searchForm #query_projectName').val();
    if (projectNameCondObj.length > 0) { aoData.push({"name": "projectName", "value": projectNameCondObj}); }
    var projectTypeCondObj  = $('#searchForm #query_projectType').val();
    if (projectTypeCondObj.length > 0) { aoData.push({"name": "projectType", "value": projectTypeCondObj}); }
    var structureTypeCondObj  = $('#searchForm #query_structureType').val();
    if (structureTypeCondObj.length > 0) { aoData.push({"name": "structureType", "value": structureTypeCondObj}); }
};

safetySupervisionJsDoneList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'projectName', sTitle: '工程名称', bSortable: false},
    {mData: 'projectAddress', sTitle: '工程地点', bSortable: false},
    {mData: 'planStartDate', sTitle: '开工日期', bSortable: false},
    {mData: 'planEndDate', sTitle: '竣工日期', bSortable: false},
    {mData: 'buildArea', sTitle: '建筑面积', bSortable: false},
    {mData: 'investmentAmount', sTitle: '合同造价', bSortable: false},
    {mData: 'projectType', sTitle: '工程类型', bSortable: false ,mRender : function(mData, type, full) { return full.projectTypeValue; }},
    {mData: 'extDate1', sTitle: '报监时间', bSortable: false},
    {mData: 'structureType', sTitle: '结构', bSortable: false,mRender : function(mData, type, full) { return full.structureTypeValue; }},
    {mData: 'createDate', sTitle: '申请时间', bSortable: false},
	{mData: function(source) {
        var buttonStr = "<a class='a-icon i-new' onclick=safetySupervisionJsDoneList.openForm('/instance/toOpenForm.action?definitionId_="+source.workflowBean.definitionId_+"&business_Key_="+source.workflowBean.business_Key_+"&curNodeId_="+source.workflowBean.curNodeId_+"&taskId_="+source.workflowBean.taskId_+"&instanceId_="+source.workflowBean.instanceId_+"&openType_=DONE') href='javascript:void(0)' >查看</a>";
         buttonStr += "<a class='a-icon i-new' target='_blank' href='"+getRootPath()+"/workflowHistory/showHistory.action?instanceId="+source.workflowBean.instanceId_+"&definitionId="+source.workflowBean.definitionId_+"' >流程历史</a>";
        return buttonStr;
	}, sTitle: '操作', bSortable: false, sWidth: 170}
];

safetySupervisionJsDoneList.openForm = function(url){
    JCFF.loadPage({url:url});
}

safetySupervisionJsDoneList.renderTable = function () {
    $.ajaxSetup ({
        cache: false //设置成false将不会从浏览器缓存读取信息
    });
    if (safetySupervisionJsDoneList.oTable == null) {
        safetySupervisionJsDoneList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": safetySupervisionJsDoneList.pageCount,
            "sAjaxSource": getRootPath() + "/safe/supervision/manageDoneList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": safetySupervisionJsDoneList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                safetySupervisionJsDoneList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        safetySupervisionJsDoneList.oTable.fnDraw();
    }
};


safetySupervisionJsDoneList.queryReset = function(){
    $('#searchForm')[0].reset();
};


$(document).ready(function(){
    safetySupervisionJsDoneList.pageCount = TabNub > 0 ? TabNub : 10;
    safetySupervisionJsDoneList.renderTable();
    $('#queryBtn').click(safetySupervisionJsDoneList.renderTable);
    $('#resetBtn').click(safetySupervisionJsDoneList.queryReset);
});