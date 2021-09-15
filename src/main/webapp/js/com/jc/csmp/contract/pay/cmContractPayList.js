var cmContractPayJsList = {};
cmContractPayJsList.oTable = null;

cmContractPayJsList.oTableFnServerParams = function(aoData){
    getTableParameters(cmContractPayJsList.oTable, aoData);
    var contractId = $('#searchForm #contractId').val();
    aoData.push({name: "contractId", value: (contractId == '' ? '-1' : contractId)});
    aoData.push({name: 'auditStatus', value: 'finish'});
};

cmContractPayJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'createDate', sTitle: '申请时间', bSortable: false},
	{mData: 'payNo', sTitle: '单据编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'contractCode', sTitle: '合同编号', bSortable: false},
	{mData: 'contractName', sTitle: '合同名称', bSortable: false},
	{mData: 'applyMoney', sTitle: '申请金额', bSortable: false},
	{mData: 'replyMoney', sTitle: '批复金额', bSortable: false},
	{mData: 'payMoney', sTitle: '累计支付金额', bSortable: false},
	{mData: 'handleUserValue', sTitle: '经办人', bSortable: false},
	{mData: function(source) {
        return "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"cmContractPayJsList.loadModuleForLook('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 60}
];

cmContractPayJsList.renderTable = function () {
    if (cmContractPayJsList.oTable == null) {
        cmContractPayJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": cmContractPayJsList.pageCount,
            "sAjaxSource": getRootPath() + "/contract/pay/searchList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmContractPayJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmContractPayJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmContractPayJsList.oTable.fnDraw();
    }
};

cmContractPayJsList.loadModuleForLook = function (id){
    $("#formModule").load(getRootPath() + "/contract/pay/loadForm.action", null, function() {
        cmContractPayJsForm.init({title: '查看支付信息', operator: 'look', id: id});
    });
};

$(document).ready(function(){
    cmContractPayJsList.pageCount = TabNub > 0 ? TabNub : 10;
    cmContractPayJsList.renderTable();
    $('#backBtn').click(function () { window.history.go(-1); });
});