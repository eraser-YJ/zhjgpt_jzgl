var cmContractInfoJsList = {};
cmContractInfoJsList.oTable = null;

cmContractInfoJsList.oTableFnServerParams = function(aoData){
    getTableParameters(cmContractInfoJsList.oTable, aoData);
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
    var auditStatus = $('#searchForm #auditStatus').val();
    if (auditStatus.length > 0) { aoData.push({name: "auditStatus", value: auditStatus}); }
    var createUser = $('#searchForm #createUser').val();
    if (createUser.length > 0) { aoData.push({name: "createUser", value: createUser}); }
};

cmContractInfoJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '??????', bSortable: false, sWidth: 60},
	{mData: 'projectId', sTitle: '????????????', bSortable: false, mRender : function(mData, type, full) { return full.projectName; }},
    {mData: 'projectNumber', sTitle: '??????????????????', bSortable: false},
    {mData: 'contractCode', sTitle: '????????????', bSortable: false},
	{mData: 'contractName', sTitle: '????????????', bSortable: false},
	{mData: 'contractType', sTitle: '????????????', bSortable: false, mRender : function(mData, type, full) { return full.contractTypeValue; }},
	{mData: 'startDate', sTitle: '??????????????????', bSortable: false},
	{mData: 'endDate', sTitle: '??????????????????', bSortable: false},
	{mData: 'signDate', sTitle: '??????????????????', bSortable: false},
	{mData: 'handleUser', sTitle: '?????????', bSortable: false},
    {mData: 'auditStatusValue', sTitle: '????????????', bSortable: false},
	{mData: function(source) { return oTableSetButtons(source) }, sTitle: '??????', bSortable: false, sWidth: 141}
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

cmContractInfoJsList.delete = function (id) {
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
            cmContractInfoJsList.deleteCallBack(ids);
        }
    });
};

cmContractInfoJsList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/contract/info/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            cmContractInfoJsList.renderTable();
        }
    });
};

cmContractInfoJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

cmContractInfoJsList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/contract/info/loadForm.action", null, function() {
        cmContractInfoJsForm.init({title: '??????????????????', operator: 'add'});
    });
};

cmContractInfoJsList.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/contract/info/loadForm.action", null, function() {
        cmContractInfoJsForm.init({title: '??????????????????', operator: 'modify', id: id});
    });
};

cmContractInfoJsList.loadModuleForLook = function (id){
    parent.JCF.LoadPage({url: '/contract/info/look.action?id='+ id });
};

cmContractInfoJsList.payInfomation = function (id) {
    parent.JCF.LoadPage({url: '/contract/pay/search.action?contractId='+ id });
};

$(document).ready(function(){
    cmContractInfoJsList.pageCount = TabNub > 0 ? TabNub : 10;
    cmContractInfoJsList.renderTable();
    if ($('#searchForm #createUser').val() != "") {
        $('#crumbsHeaderTitle').html("<span>????????????</span><span>???????????? > </span><span>??????????????????</span>");
        $('#addBtn').show();
        $('#addBtn').click(function () {
            JCFF.loadPage({url: "/instance/toStartProcess.action?definitionKey_=contractKeepOnRecord"});
        });
    } else {
        $('#crumbsHeaderTitle').html("<span>????????????</span><span>???????????? > </span><span>????????????</span>");
    }
    $('#queryBtn').click(cmContractInfoJsList.renderTable);
    $('#resetBtn').click(cmContractInfoJsList.queryReset);
});