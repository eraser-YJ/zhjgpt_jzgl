var contractPayTodoList = {};
contractPayTodoList.pageRows = null;
contractPayTodoList.subState = false;
contractPayTodoList.oTable = null;
contractPayTodoList.oTableAoColumns = [
	{mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
	{mData: 'payNo', sTitle: '单据编号', bSortable: false},
	{mData: 'projectName', sTitle: '项目名称', bSortable: false},
	{mData: 'contractCode', sTitle: '合同编号', bSortable: false},
	{mData: 'contractName', sTitle: '合同名称', bSortable: false},
	{mData: 'applyMoney', sTitle: '申请金额', bSortable: false},
	{mData: 'replyMoney', sTitle: '批复金额', bSortable: false},
	{mData: function(source) {
		var buttonStr = "<a class='a-icon i-new' onclick=\"contractPayTodoList.openForm('/instance/toOpenForm.action?definitionId_="+source.workflowBean.definitionId_+"&business_Key_="+source.workflowBean.business_Key_+"&curNodeId_="+source.workflowBean.curNodeId_+"&taskId_="+source.workflowBean.taskId_+"&instanceId_="+source.workflowBean.instanceId_+"&openType_=TODO')\" href='javascript:void(0)' >办理</a>";
		buttonStr += "<a class='a-icon i-new' target='_blank' href='"+getRootPath()+"/workflowHistory/showHistory.action?instanceId="+source.workflowBean.instanceId_+"&definitionId="+source.workflowBean.definitionId_+"' >流程历史</a>";
		return buttonStr;
	}, sTitle: '操作', bSortable: false, sWidth: 150}
];
                  
contractPayTodoList.openForm = function(url) {
	JCFF.loadPage({url:url});
};

contractPayTodoList.oTableFnServerParams = function(aoData){
	getTableParameters(contractPayTodoList.oTable, aoData);
	var contractCode = $('#searchForm #contractCode').val();
	if (contractCode.length > 0) { aoData.push({name: "contractCode", value: contractCode}); }
	var contractName = $('#searchForm #contractName').val();
	if (contractName.length > 0) { aoData.push({name: "contractName", value: contractName}); }
};

contractPayTodoList.workflowTodoList = function () {
	$.ajaxSetup ({ cache: false });
	$('#sendPassTransact-list').fadeIn();
	if (contractPayTodoList.oTable == null) {
		var pagingInfo = pagingDataForGoBack();
		var queryData = pagingInfo.queryData;
		if (queryData != null) {
			//需要针对自己的查询搜索框进行填入值
			//如果有人员选择框的特殊内容需要特殊实现
			$("#searchForm").fill(queryData);
		}
		contractPayTodoList.oTable = $('#gridTable').dataTable( {
			"iDisplayLength": contractPayTodoList.pageRows,
			"iDisplayStart" : pagingInfo.curPage * contractPayTodoList.pageRows,
			"sAjaxSource": getRootPath() + "/contract/pay/manageTodoList.action",
			"fnServerData": oTableRetrieveData,
			"aoColumns": contractPayTodoList.oTableAoColumns,
			"fnServerParams": function ( aoData ) {
				contractPayTodoList.oTableFnServerParams(aoData);
			},
			aaSorting:[],
			aoColumnDefs: []
		} );
	} else {
		contractPayTodoList.oTable.fnDraw();
	}
};

contractPayTodoList.queryReset = function(){
	$('#searchForm')[0].reset();
};

jQuery(function($) {
	contractPayTodoList.pageRows = TabNub>0 ? TabNub : 10;
	$("#queryBtn").click(contractPayTodoList.workflowTodoList);
	$("#resetBtn").click(contractPayTodoList.queryReset);
	$(".datepicker-input").datepicker();
	contractPayTodoList.workflowTodoList();
});

