var workflowTodoListPanel = {};
workflowTodoListPanel.pageRows = null;
workflowTodoListPanel.oTable = null;

workflowTodoListPanel.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'workflowCreateTime', sTitle: '时间', bSortable: false, sWidth: 150},
    {mData: 'workflowTitleValue', sTitle: '审批类型', bSortable: false, sWidth: 180},
    {mData: 'businessCode', sTitle: '编码', bSortable: false},
    {mData: 'businessDesc', sTitle: '业务描述', bSortable: false},
    {mData: function(source) {
        var buttonStr = "<a class='a-icon i-new' onclick=\"workflowTodoListPanel.openForm('/instance/toOpenForm.action?definitionId_="+source.workflowBean.definitionId_+"&business_Key_="+source.workflowBean.business_Key_+"&curNodeId_="+source.workflowBean.curNodeId_+"&taskId_="+source.workflowBean.taskId_+"&instanceId_="+source.workflowBean.instanceId_+"&openType_=TODO')\" href='javascript:void(0)' >办理</a>";
        buttonStr += "<a class='a-icon i-new' target='_blank' href='"+getRootPath()+"/workflowHistory/showHistory.action?instanceId="+source.workflowBean.instanceId_+"&definitionId="+source.workflowBean.definitionId_+"' >流程历史</a>";
        return buttonStr;
    }, sTitle: '操作', bSortable: false, sWidth: 150}
];

workflowTodoListPanel.openForm = function(url) {
    JCFF.loadPage({url:url});
};

workflowTodoListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(workflowTodoListPanel.oTable, aoData);
    var workflowTitle = $('#searchForm #workflowTitle').val();
    if (workflowTitle.length > 0) { aoData.push({name: "workflowTitle", value: workflowTitle}); }
};

workflowTodoListPanel.workflowTodoList = function () {
    $.ajaxSetup ({ cache: false });
    $('#sendPassTransact-list').fadeIn();
    if (workflowTodoListPanel.oTable == null) {
        var pagingInfo = pagingDataForGoBack();
        var queryData = pagingInfo.queryData;
        if (queryData != null) {
            $("#searchForm").fill(queryData);
        }
        workflowTodoListPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": workflowTodoListPanel.pageRows,
            "iDisplayStart" : pagingInfo.curPage * workflowTodoListPanel.pageRows,
            "sAjaxSource": getRootPath() + "/workflow/manageTodoList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": workflowTodoListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                workflowTodoListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        } );
    } else {
        workflowTodoListPanel.oTable.fnDraw();
    }
};

workflowTodoListPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

jQuery(function($) {
    workflowTodoListPanel.pageRows = TabNub>0 ? TabNub : 10;
    $("#queryBtn").click(workflowTodoListPanel.workflowTodoList);
    $("#resetBtn").click(workflowTodoListPanel.queryReset);
    workflowTodoListPanel.workflowTodoList();
});

