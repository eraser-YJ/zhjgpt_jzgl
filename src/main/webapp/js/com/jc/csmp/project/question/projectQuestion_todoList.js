var projectQuestionJsTodoList = {};
projectQuestionJsTodoList.pageRows = null;
projectQuestionJsTodoList.subState = false;
projectQuestionJsTodoList.oTable = null;
projectQuestionJsTodoList.oTableFnServerParams = function(aoData){
    getTableParameters(projectQuestionJsTodoList.oTable, aoData);
    var projectNumber = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({name: "projectNumber", value: projectNumber}); }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({name: "projectName", value: projectName}); }
    var createDateBegin = $('#searchForm #createDateBegin').val();
    if (createDateBegin.length > 0) { aoData.push({name: "createDateBegin", value: createDateBegin}); }
    var createDateEnd = $('#searchForm #createDateEnd').val();
    if (createDateEnd.length > 0) { aoData.push({name: "createDateEnd", value: createDateEnd}); }
    aoData.push({name: "questionType", value: $('#searchForm #questionType').val()});
    var code = $('#searchForm #code').val();
    if (code.length > 0) { aoData.push({name: "code", value: code}); }
};

projectQuestionJsTodoList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'code', sTitle: '问题编号', bSortable: false},
    {mData: 'createDateFormat', sTitle: '登记时间', bSortable: false},
    {mData: 'projectNumber', sTitle: '项目统一编码', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'contractName', sTitle: '合同名称', bSortable: false},
    {mData: 'title', sTitle: '标题', bSortable: false},
    {mData: 'rectificationCompany', sTitle: '整改单位', bSortable: false, mRender : function(mData, type, full) { return full.rectificationCompanyValue; }},
	{mData: function(source) {
        var buttonStr = "<a class='a-icon i-new' onclick=projectQuestionJsTodoList.openForm('/instance/toOpenForm.action?definitionId_="+source.workflowBean.definitionId_+"&business_Key_="+source.workflowBean.business_Key_+"&curNodeId_="+source.workflowBean.curNodeId_+"&taskId_="+source.workflowBean.taskId_+"&instanceId_="+source.workflowBean.instanceId_+"&openType_=TODO') href='javascript:void(0)'>办理</a>";
         buttonStr += "<a class='a-icon i-new' target='_blank' href='"+getRootPath()+"/workflowHistory/showHistory.action?instanceId="+source.workflowBean.instanceId_+"&definitionId="+source.workflowBean.definitionId_+"' >流程历史</a>";
        return buttonStr;
	}, sTitle: '操作', bSortable: false, sWidth: 150}
];

projectQuestionJsTodoList.openForm = function(url){
    JCFF.loadPage({url:url});
};

projectQuestionJsTodoList.renderTable = function () {
    $.ajaxSetup ({cache: false});
    if (projectQuestionJsTodoList.oTable == null) {
        projectQuestionJsTodoList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": projectQuestionJsTodoList.pageCount,
            "sAjaxSource": getRootPath() + "/project/question/manageTodoList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectQuestionJsTodoList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectQuestionJsTodoList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectQuestionJsTodoList.oTable.fnDraw();
    }
};

projectQuestionJsTodoList.queryReset = function(){
    $('#searchForm')[0].reset();
};

$(document).ready(function(){
    projectQuestionJsTodoList.pageCount = TabNub > 0 ? TabNub : 10;
    projectQuestionJsTodoList.renderTable();
    $(".datepicker-input").datepicker();
    $('#queryBtn').click(projectQuestionJsTodoList.renderTable);
    $('#resetBtn').click(projectQuestionJsTodoList.queryReset);
    var questionType  =$('#searchForm #questionType').val();
    if (questionType == 'quality') {
        $('#sectionTitleOne').html("质量问题管理 > ");
        $('#sectionTitleTwo').html("备案审核");
    } else if (questionType == "scene") {
        $('#sectionTitleOne').html("现场问题管理 > ");
        $('#sectionTitleTwo').html("备案审核");
    } else if (questionType == "safe") {
        $('#sectionTitleOne').html("安全问题管理 > ");
        $('#sectionTitleTwo').html("备案审核");
    }
});