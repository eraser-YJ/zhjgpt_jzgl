var projectChangeOrderTodoList = {};
projectChangeOrderTodoList.pageRows = null;
projectChangeOrderTodoList.subState = false;
projectChangeOrderTodoList.oTable = null;
projectChangeOrderTodoList.oTableAoColumns = [
	{mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
	{mData: 'changeDate', sTitle: '变更日期', bSortable: false},
	{mData: 'changeType', sTitle: '变更类别', bSortable: false, mRender : function(mData, type, full) { return full.changeTypeValue; }},
	{mData: 'code', sTitle: '变更单编号', bSortable: false},
	{mData: 'modifyType', sTitle: '变更类型', bSortable: false, mRender : function(mData, type, full) { return full.modifyTypeValue; }},
	{mData: 'deptName', sTitle: '申请单位', bSortable: false},
	{mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
	{mData: 'projectName', sTitle: '项目名称', bSortable: false},
	{mData: 'contractCode', sTitle: '合同编号', bSortable: false},
	{mData: 'contractName', sTitle: '合同名称', bSortable: false},
	{mData: 'handleUser', sTitle: '经办人', bSortable: false},
	{mData: function(source) {
		var buttonStr = "<a class='a-icon i-new' onclick=\"projectChangeOrderTodoList.openForm('/instance/toOpenForm.action?definitionId_="+source.workflowBean.definitionId_+"&business_Key_="+source.workflowBean.business_Key_+"&curNodeId_="+source.workflowBean.curNodeId_+"&taskId_="+source.workflowBean.taskId_+"&instanceId_="+source.workflowBean.instanceId_+"&openType_=TODO')\" href='javascript:void(0)' >办理</a>";
		buttonStr += "<a class='a-icon i-new' target='_blank' href='"+getRootPath()+"/workflowHistory/showHistory.action?instanceId="+source.workflowBean.instanceId_+"&definitionId="+source.workflowBean.definitionId_+"' >流程历史</a>";
		return buttonStr;
	}, sTitle: '操作', bSortable: false, sWidth: 150}
];
                  
projectChangeOrderTodoList.openForm = function(url) {
	JCFF.loadPage({url:url});
};

projectChangeOrderTodoList.oTableFnServerParams = function(aoData){
	getTableParameters(projectChangeOrderTodoList.oTable, aoData);
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
	var changeType = $('#searchForm #changeType').val();
	if (changeType.length > 0)  { aoData.push({name: "changeType", value: changeType}); }
};

projectChangeOrderTodoList.workflowTodoList = function () {
	$.ajaxSetup ({ cache: false });
	$('#sendPassTransact-list').fadeIn();
	if (projectChangeOrderTodoList.oTable == null) {
		var pagingInfo = pagingDataForGoBack();
		var queryData = pagingInfo.queryData;
		if (queryData != null) {
			//需要针对自己的查询搜索框进行填入值
			//如果有人员选择框的特殊内容需要特殊实现
			$("#searchForm").fill(queryData);
		}
		projectChangeOrderTodoList.oTable = $('#gridTable').dataTable( {
			"iDisplayLength": projectChangeOrderTodoList.pageRows,
			"iDisplayStart" : pagingInfo.curPage * projectChangeOrderTodoList.pageRows,
			"sAjaxSource": getRootPath() + "/project/change/manageTodoList.action",
			"fnServerData": oTableRetrieveData,
			"aoColumns": projectChangeOrderTodoList.oTableAoColumns,
			"fnServerParams": function ( aoData ) {
				projectChangeOrderTodoList.oTableFnServerParams(aoData);
			},
			aaSorting:[],
			aoColumnDefs: []
		} );
	} else {
		projectChangeOrderTodoList.oTable.fnDraw();
	}
};

projectChangeOrderTodoList.queryReset = function(){
	$('#searchForm')[0].reset();
};

jQuery(function($) {
	projectChangeOrderTodoList.pageRows = TabNub>0 ? TabNub : 10;
	$("#queryBtn").click(projectChangeOrderTodoList.workflowTodoList);
	$("#resetBtn").click(projectChangeOrderTodoList.queryReset);
	$(".datepicker-input").datepicker();
	projectChangeOrderTodoList.workflowTodoList();
});

