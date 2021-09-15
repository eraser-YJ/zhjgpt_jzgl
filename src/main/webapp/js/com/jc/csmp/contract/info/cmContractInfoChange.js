var cmContractInfoChangePanel = {};
cmContractInfoChangePanel.oTable = null;

cmContractInfoChangePanel.callback = null;

cmContractInfoChangePanel.oTableFnServerParams = function(aoData){
    getTableParameters(cmContractInfoChangePanel.oTable, aoData);
    var contractCode = $('#contractChangeSearchForm #contractChange_contractCode').val();
    if (contractCode.length > 0) { aoData.push({name: "contractCode", value: contractCode}); }
    var contractName = $('#contractChangeSearchForm #contractChange_contractName').val();
    if (contractName.length > 0) { aoData.push({name: "contractName", value: contractName}); }
    aoData.push({name: "auditStatus", value: 'finish'});
};

cmContractInfoChangePanel.oTableAoColumns = [
	{mData: 'projectId', sTitle: '项目名称', bSortable: false, mRender : function(mData, type, full) { return full.projectName; }},
    {mData: 'contractCode', sTitle: '合同编号', bSortable: false},
	{mData: 'contractName', sTitle: '合同名称', bSortable: false},
	{mData: 'signDate', sTitle: '合同签订时间', bSortable: false},
	{mData: function(source) {
	    return "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"cmContractInfoChangePanel.change('"+ source.id+ "')\" role=\"button\">&nbsp;选择&nbsp;</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 80}
];

cmContractInfoChangePanel.renderTable = function () {
    if (cmContractInfoChangePanel.oTable == null) {
        cmContractInfoChangePanel.oTable =  $('#contractChageGridTable').dataTable( {
            "iDisplayLength": cmContractInfoChangePanel.pageCount,
            "sAjaxSource": getRootPath() + "/contract/info/searchList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmContractInfoChangePanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmContractInfoChangePanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmContractInfoChangePanel.oTable.fnDraw();
    }
};

cmContractInfoChangePanel.queryReset = function(){
    $('#contractChangeSearchForm')[0].reset();
};

cmContractInfoChangePanel.change = function (id){
    if (cmContractInfoChangePanel.callback != null) {
        $.ajax({
            type: "GET", data: {id: id}, dataType: "json",
            url: getRootPath() + "/contract/info/get.action",
            success: function(data) {
                if (data) {
                    cmContractInfoChangePanel.callback(data);
                    $('#contract-change-modal').modal('hide');
                }
            }
        });
    }
};

cmContractInfoChangePanel.init = function(config) {
    cmContractInfoChangePanel.callback = config.callback;
    cmContractInfoChangePanel.pageCount = TabNub > 0 ? TabNub : 10;
    cmContractInfoChangePanel.renderTable();
    $('#contract-change-modal').modal('show');
};