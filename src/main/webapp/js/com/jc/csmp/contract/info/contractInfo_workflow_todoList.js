var contractInfoTodoList = {};
contractInfoTodoList.pageRows = null;
contractInfoTodoList.subState = false;
contractInfoTodoList.oTable = null;
contractInfoTodoList.oTableAoColumns = [
	{mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
	{mData: 'projectId', sTitle: '项目名称', bSortable: false, mRender : function(mData, type, full) { return full.projectName; }},
	{mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
	{mData: 'contractCode', sTitle: '合同编号', bSortable: false},
	{mData: 'contractName', sTitle: '合同名称', bSortable: false},
	{mData: 'contractType', sTitle: '合同类型', bSortable: false, mRender : function(mData, type, full) { return full.contractTypeValue; }},
	{mData: 'startDate', sTitle: '合同开始时间', bSortable: false},
	{mData: 'endDate', sTitle: '合同结束时间', bSortable: false},
	{mData: 'signDate', sTitle: '合同签订时间', bSortable: false},
	{mData: 'handleUser', sTitle: '经办人', bSortable: false},
	{mData: function(source) {
		var buttonStr = "<a class='a-icon i-new' onclick=\"contractInfoTodoList.openForm('/instance/toOpenForm.action?definitionId_="+source.workflowBean.definitionId_+"&business_Key_="+source.workflowBean.business_Key_+"&curNodeId_="+source.workflowBean.curNodeId_+"&taskId_="+source.workflowBean.taskId_+"&instanceId_="+source.workflowBean.instanceId_+"&openType_=TODO')\" href='javascript:void(0)' >办理</a>";
		buttonStr += "<a class='a-icon i-new' target='_blank' href='"+getRootPath()+"/workflowHistory/showHistory.action?instanceId="+source.workflowBean.instanceId_+"&definitionId="+source.workflowBean.definitionId_+"' >流程历史</a>";
		return buttonStr;
	}, sTitle: '操作', bSortable: false, sWidth: 150}
];
                  
contractInfoTodoList.openForm = function(url) {
	JCFF.loadPage({url:url});
};

contractInfoTodoList.oTableFnServerParams = function(aoData){
	getTableParameters(contractInfoTodoList.oTable, aoData);
	var projectNumber = $('#searchForm #projectNumber').val();
	if (projectNumber.length > 0) { aoData.push({name: "projectNumber", value: projectNumber}); }
	var projectName = $('#searchForm #projectName').val();
	if (projectName.length > 0) { aoData.push({name: "projectName", value: projectName}); }
	var contractCode = $('#searchForm #contractCode').val();
	if (contractCode.length > 0) { aoData.push({name: "contractCode", value: contractCode}); }
	var contractName = $('#searchForm #contractName').val();
	if (contractName.length > 0) { aoData.push({name: "contractName", value: contractName}); }
	var contractType = $('#searchForm #contractType').val();
	if (contractType.length > 0) { aoData.push({name: "contractType", value: contractType}); }
	var needAudit = $('#searchForm #needAudit').val();
	if (needAudit.length > 0) { aoData.push({name: "needAudit", value: needAudit}); }
	var startDate = $('#searchForm #startDate').val();
	if (startDate.length > 0) { aoData.push({name: "startDate", value: startDate}); }
	var endDate = $('#searchForm #endDate').val();
	if (endDate.length > 0) { aoData.push({name: "endDate", value: endDate}); }
	var signDateBegin = $('#searchForm #signDateBegin').val();
	if (signDateBegin.length > 0) { aoData.push({name: "signDateBegin", value: signDateBegin}); }
	var signDateEnd = $('#searchForm #signDateEnd').val();
	if (signDateEnd.length > 0) { aoData.push({name: "signDateEnd", value: signDateEnd}); }
	console.log(aoData);
};

contractInfoTodoList.workflowTodoList = function () {
	$.ajaxSetup ({ cache: false });
	$('#sendPassTransact-list').fadeIn();
	if (contractInfoTodoList.oTable == null) {
		var pagingInfo = pagingDataForGoBack();
		var queryData = pagingInfo.queryData;
		if (queryData != null) {
			//需要针对自己的查询搜索框进行填入值
			//如果有人员选择框的特殊内容需要特殊实现
			$("#searchForm").fill(queryData);
		}
		contractInfoTodoList.oTable = $('#gridTable').dataTable( {
			"iDisplayLength": contractInfoTodoList.pageRows,
			"iDisplayStart" : pagingInfo.curPage * contractInfoTodoList.pageRows,
			"sAjaxSource": getRootPath() + "/contract/info/manageTodoList.action",
			"fnServerData": oTableRetrieveData,
			"aoColumns": contractInfoTodoList.oTableAoColumns,
			"fnServerParams": function ( aoData ) {
				contractInfoTodoList.oTableFnServerParams(aoData);
			},
			aaSorting:[],
			aoColumnDefs: []
		} );
	} else {
		contractInfoTodoList.oTable.fnDraw();
	}
};

contractInfoTodoList.queryReset = function(){
	$('#searchForm')[0].reset();
};

jQuery(function($) {
	contractInfoTodoList.pageRows = TabNub>0 ? TabNub : 10;
	$("#queryBtn").click(contractInfoTodoList.workflowTodoList);
	$("#resetBtn").click(contractInfoTodoList.queryReset);
	$(".datepicker-input").datepicker();
	contractInfoTodoList.workflowTodoList();
});

