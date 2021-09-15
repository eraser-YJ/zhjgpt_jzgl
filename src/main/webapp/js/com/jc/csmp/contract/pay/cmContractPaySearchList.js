var cmContractPaySearchList = {};
cmContractPaySearchList.oTable = null;

cmContractPaySearchList.oTableFnServerParams = function(aoData){
    getTableParameters(cmContractPaySearchList.oTable, aoData);
    var contractCode = $('#searchForm #contractCode').val();
    if (contractCode.length > 0) { aoData.push({name: "contractCode", value: contractCode}); }
    var contractName = $('#searchForm #contractName').val();
    if (contractName.length > 0) { aoData.push({name: "contractName", value: contractName}); }
    var auditStatus = $('#searchForm #auditStatus').val();
    if (auditStatus.length > 0) { aoData.push({name: 'auditStatus', value: auditStatus}); }
    var createUser = $('#searchForm #createUser').val();
    if (createUser.length > 0) { aoData.push({name: "createUser", value: createUser}); }
};

cmContractPaySearchList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'createDate', sTitle: '申请时间', bSortable: false},
	{mData: 'payNo', sTitle: '单据编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'contractCode', sTitle: '合同编号', bSortable: false},
	{mData: 'contractName', sTitle: '合同名称', bSortable: false},
	{mData: 'applyMoney', sTitle: '申请金额', bSortable: false},
	{mData: 'replyMoney', sTitle: '批复金额', bSortable: false},
	{mData: 'handleUserValue', sTitle: '经办人', bSortable: false},
    {mData: 'auditStatusValue', sTitle: '审核状态', bSortable: false},
	{mData: function(source) {
	    var look = "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"cmContractPaySearchList.loadModuleForLook('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
        var history = "<a class=\"a-icon i-edit\" href=\"javascript:void(0);\" onclick=\"footerToolsModule.workflowHistory('"+ source.piId+ "')\" role=\"button\">流程历史</a>";
        return look + history;
    }, sTitle: '操作', bSortable: false, sWidth: 150}
];

cmContractPaySearchList.renderTable = function () {
    if (cmContractPaySearchList.oTable == null) {
        cmContractPaySearchList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": cmContractPaySearchList.pageCount,
            "sAjaxSource": getRootPath() + "/contract/pay/paySearchList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmContractPaySearchList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmContractPaySearchList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmContractPaySearchList.oTable.fnDraw();
    }
};

cmContractPaySearchList.loadModuleForLook = function (id){
    $("#formModule").load(getRootPath() + "/contract/pay/loadForm.action", null, function() {
        cmContractPayJsForm.init({title: '查看支付信息', operator: 'look', id: id});
    });
};

cmContractPaySearchList.queryReset = function(){
    $('#searchForm')[0].reset();
};

$(document).ready(function(){
    cmContractPaySearchList.pageCount = TabNub > 0 ? TabNub : 10;
    cmContractPaySearchList.renderTable();
    $('#queryBtn').click(cmContractPaySearchList.renderTable);
    $('#resetBtn').click(cmContractPaySearchList.queryReset);
    if ($('#searchForm #createUser').val() != "") {
        $('#crumbsHeaderTitle').html("<span>项目管理</span><span>合同管理 > </span><span>合同支付申请</span>");
        $('#addBtn').show();
        $('#addBtn').click(function () {
            JCFF.loadPage({url: "/instance/toStartProcess.action?definitionKey_=contractPay"});
        });
    } else {
        $('#crumbsHeaderTitle').html("<span>项目管理</span><span>合同管理 > </span><span>合同支付查询</span>");
    }
});