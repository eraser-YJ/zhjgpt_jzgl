var cmContractSearchPayList = {};
cmContractSearchPayList.oTable = null;

cmContractSearchPayList.oTableFnServerParams = function(aoData){
    getTableParameters(cmContractSearchPayList.oTable, aoData);
    var projectNumber = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({name: "projectNumber", value: projectNumber}); }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({name: "projectName", value: projectName}); }
    var contractCode = $('#searchForm #contractCode').val();
    if (contractCode.length > 0) { aoData.push({name: "contractCode", value: contractCode}); }
    var contractName = $('#searchForm #contractName').val();
    if (contractName.length > 0) { aoData.push({name: "contractName", value: contractName}); }
    aoData.push({name: "auditStatus", value: "finish"});
};

cmContractSearchPayList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
	{mData: 'projectId', sTitle: '项目名称', bSortable: false, mRender : function(mData, type, full) { return full.projectName; }},
    {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
    {mData: 'contractCode', sTitle: '合同编号', bSortable: false},
	{mData: 'contractName', sTitle: '合同名称', bSortable: false},
    {mData: 'partybUnit', sTitle: '中标单位', bSortable: false, mRender : function(mData, type, full) { return full.partybUnitValue;}},
	{mData: 'contractMoney', sTitle: '合同金额(万元)', bSortable: false},
	{mData: 'totalPayment', sTitle: '支付总额(万元)', bSortable: false},
	{mData: 'paymentRatio', sTitle: '累计支付比例', bSortable: false, mRender : function(mData, type, full) { return full.paymentRatioValue + "%";}},
	{mData: function(source) {
	    return "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"cmContractSearchPayList.payInfomation('"+ source.id+ "')\" role=\"button\">&nbsp;支付情况&nbsp;</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 80}
];

cmContractSearchPayList.renderTable = function () {
    if (cmContractSearchPayList.oTable == null) {
        cmContractSearchPayList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": cmContractSearchPayList.pageCount,
            "sAjaxSource": getRootPath() + "/contract/info/searchList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmContractSearchPayList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmContractSearchPayList.oTableFnServerParams(aoData);
            },
            "fnCreatedRow": function( nRow, aData, iDataIndex ) {	//创建行事件，用来改变行的文字颜色.
                $(nRow.cells[5]).attr('style', 'text-align: right');
                $(nRow.cells[6]).attr('style', 'text-align: right');
                $(nRow.cells[7]).attr('style', 'text-align: right');
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmContractSearchPayList.oTable.fnDraw();
    }
};

cmContractSearchPayList.delete = function (id) {
    var ids = String(id);
    if (id == 0) {
        var idsArr = [];
        $("[name='ids']:checked").each(function() {
            idsArr.push($(this).val());
        });
        ids = idsArr.join(",");
    }
    if (ids.length == 0) {
        msgBox.info({ content: $.i18n.prop("JC_SYS_061") });
        return;
    }
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function(){
            cmContractSearchPayList.deleteCallBack(ids);
        }
    });
};

cmContractSearchPayList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/contract/info/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            cmContractSearchPayList.renderTable();
        }
    });
};

cmContractSearchPayList.queryReset = function(){
    $('#searchForm')[0].reset();
};

cmContractSearchPayList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/contract/info/loadForm.action", null, function() {
        cmContractInfoJsForm.init({title: '新增合同备案', operator: 'add'});
    });
};

cmContractSearchPayList.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/contract/info/loadForm.action", null, function() {
        cmContractInfoJsForm.init({title: '修改合同备案', operator: 'modify', id: id});
    });
};

cmContractSearchPayList.loadModuleForLook = function (id){
    $("#formModule").load(getRootPath() + "/contract/info/loadForm.action", null, function() {
        cmContractInfoJsForm.init({title: '查看合同备案', operator: 'look', id: id});
    });
};

cmContractSearchPayList.payInfomation = function (id) {
    parent.JCF.LoadPage({url: '/contract/pay/search.action?contractId='+ id });
};

$(document).ready(function(){
    cmContractSearchPayList.pageCount = TabNub > 0 ? TabNub : 10;
    cmContractSearchPayList.renderTable();
    $('#queryBtn').click(cmContractSearchPayList.renderTable);
    $('#resetBtn').click(cmContractSearchPayList.queryReset);
    $("#addBtn").click(cmContractSearchPayList.loadModuleForAdd);
});