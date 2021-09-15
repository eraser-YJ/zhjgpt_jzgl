var safetySupervisionJsWorkFlowList = {};
safetySupervisionJsWorkFlowList.pageRows = null;
//重复提交标识
safetySupervisionJsWorkFlowList.subState = false;
//分页对象
safetySupervisionJsWorkFlowList.oTable = null;

safetySupervisionJsWorkFlowList.oTableFnServerParams = function(aoData){
    getTableParameters(safetySupervisionJsWorkFlowList.oTable, aoData);
    var projectNameCondObj  = $('#searchForm #query_projectName').val();
    if (projectNameCondObj.length > 0) { aoData.push({"name": "projectName", "value": projectNameCondObj}); }
    var projectTypeCondObj  = $('#searchForm #query_projectType').val();
    if (projectTypeCondObj.length > 0) { aoData.push({"name": "projectType", "value": projectTypeCondObj}); }
    var structureTypeCondObj  = $('#searchForm #query_structureType').val();
    if (structureTypeCondObj.length > 0) { aoData.push({"name": "structureType", "value": structureTypeCondObj}); }
};
safetySupervisionJsWorkFlowList.oTableAoColumns = [
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
    {mData: 'flowStatus', sTitle: '审核状态', bSortable: false ,mRender : function(mData, type, full) {
        if(full.workflowBean.flowStatus_=="END"){
            return "审核完结";
        }else{
            return "审核中";
        }
    }},
	{mData: function(source) {
        var buttonStr = "<a class='a-icon i-new m-r-xs' onclick=safetySupervisionJsWorkFlowList.openForm('/instance/toOpenForm.action?definitionId_="+source.workflowBean.definitionId_+"&business_Key_="+source.workflowBean.business_Key_+"&curNodeId_="+source.workflowBean.curNodeId_+"&taskId_="+source.workflowBean.taskId_+"&instanceId_="+source.workflowBean.instanceId_+"&openType_=VIEW') href='javascript:void(0)' > 查 看 </a>";
        if(null!=source.isAdvice&&source.isAdvice==1){
            buttonStr += "<a class='a-icon i-new m-r-xs' href='"+getRootPath()+"/safe/supervision/downAdviceNote.action?id="+source.id+"'>通知书</a>"
        }
        return buttonStr;
	}, sTitle: '操作', bSortable: false, sWidth: 150}
];

safetySupervisionJsWorkFlowList.openForm = function(url){
    JCFF.loadPage({url:url});
}

safetySupervisionJsWorkFlowList.renderTable = function () {
    $.ajaxSetup ({
        cache: false //设置成false将不会从浏览器缓存读取信息
    });
    if (safetySupervisionJsWorkFlowList.oTable == null) {
        safetySupervisionJsWorkFlowList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": safetySupervisionJsWorkFlowList.pageCount,
            "sAjaxSource": getRootPath() + "/safe/supervision/manageMyList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": safetySupervisionJsWorkFlowList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                safetySupervisionJsWorkFlowList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        safetySupervisionJsWorkFlowList.oTable.fnDraw();
    }
};


safetySupervisionJsWorkFlowList.queryReset = function(){
    $('#searchForm')[0].reset();
};
safetySupervisionJsWorkFlowList.downAdviceNote=function(projectId){
    window.location.href=getRootPath()+"/safe/supervision/downAdviceNote.action?id="+projectId;

}
safetySupervisionJsWorkFlowList.exportExcelAdviceNote = function () {
    var url = getRootPath() + "/safe/supervision/exportExcelAdviceNote.action?itemCode=safetySupervision";

    var projectNameCondObj = $('#searchForm #query_projectName').val();
    if (projectNameCondObj.length > 0) {
        url += "&projectName=" + projectNameCondObj
    }
    var projectTypeCondObj = $('#searchForm #query_projectType').val();
    if (projectTypeCondObj.length > 0) {
        url += "&projectType=" + projectTypeCondObj
    }


    var structureTypeCondObj = $('#searchForm #query_structureType').val();
    if (structureTypeCondObj.length > 0) {
        url += "&structureType=" + structureTypeCondObj
    }

    window.location.href = url;

}
$(document).ready(function(){
    safetySupervisionJsWorkFlowList.pageCount = TabNub > 0 ? TabNub : 10;
    safetySupervisionJsWorkFlowList.renderTable();
    $('#queryBtn').click(safetySupervisionJsWorkFlowList.renderTable);
    $('#resetBtn').click(safetySupervisionJsWorkFlowList.queryReset);
    $('#excelAdviceNote').click(safetySupervisionJsWorkFlowList.exportExcelAdviceNote);
    $('#addBtn').click(function () {
        JCFF.loadPage({url: "/instance/toStartProcess.action?definitionKey_=projectSubmit"});
    });
});