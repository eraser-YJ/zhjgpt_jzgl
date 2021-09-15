var cmContractInfoJsList = {};
cmContractInfoJsList.oTable = null;

cmContractInfoJsList.oTableFnServerParams = function(aoData){
    getTableParameters(cmContractInfoJsList.oTable, aoData);
    var projectId = $('#searchForm #projectId').val();
    if (projectId.length > 0) { aoData.push({name: "projectId", value: projectId}); }
    var contractCode = $('#searchForm #contractCode').val();
    if (contractCode.length > 0) { aoData.push({name: "contractCode", value: contractCode}); }
    var contractName = $('#searchForm #contractName').val();
    if (contractName.length > 0) { aoData.push({name: "contractName", value: contractName}); }
    var contractType = $('#searchForm #contractType').val();
    if (contractType.length > 0) { aoData.push({name: "contractType", value: contractType}); }
    var startDate = $('#searchForm #startDate').val();
    if (startDate.length > 0) { aoData.push({name: "startDate", value: startDate}); }
    var endDate = $('#searchForm #endDate').val();
    if (endDate.length > 0) { aoData.push({name: "endDate", value: endDate}); }
    var signDateBegin = $('#searchForm #signDateBegin').val();
    if (signDateBegin.length > 0) { aoData.push({name: "signDateBegin", value: signDateBegin}); }
    var signDateEnd = $('#searchForm #signDateEnd').val();
    if (signDateEnd.length > 0) { aoData.push({name: "signDateEnd", value: signDateEnd}); }
    var auditStatus = $('#searchForm #auditStatus').val();
    if (auditStatus.length > 0) { aoData.push({name: "auditStatus", value: auditStatus}); }
};

cmContractInfoJsList.oTableAoColumns = [
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
    {mData: 'auditStatusValue', sTitle: '审核状态', bSortable: false},
	{mData: function(source) { return oTableSetButtons(source) }, sTitle: '操作', bSortable: false, sWidth: 141}
];

cmContractInfoJsList.renderTable = function () {
    if (cmContractInfoJsList.oTable == null) {
        cmContractInfoJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": cmContractInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/contract/info/searchList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmContractInfoJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmContractInfoJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmContractInfoJsList.oTable.fnDraw();
    }
};

cmContractInfoJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

cmContractInfoJsList.loadModuleForLook = function (id){
    parent.JCF.LoadPage({url: '/contract/info/look.action?id='+ id });
};

$(document).ready(function(){
    cmContractInfoJsList.pageCount = TabNub > 0 ? TabNub : 10;
    cmContractInfoJsList.renderTable();
    $('#queryBtn').click(cmContractInfoJsList.renderTable);
    $('#resetBtn').click(cmContractInfoJsList.queryReset);
});