var projectBiddingJsList = {};
projectBiddingJsList.oTable = null;

projectBiddingJsList.oTableFnServerParams = function(aoData){
    getTableParameters(projectBiddingJsList.oTable, aoData);
    var projectNumber  = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({"name": "projectNumber", "value": projectNumber}); }
    var projectName  = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({"name": "projectName", "value": projectName}); }
    var biddingMethod  = $('#searchForm #biddingMethod').val();
    if (biddingMethod.length > 0) { aoData.push({"name": "biddingMethod", "value": biddingMethod}); }
};

projectBiddingJsList.oTableAoColumns = [
    {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'biddingMethod', sTitle: '招投标方式', bSortable: false, mRender : function(mData, type, full) { return full.biddingMethodValue; }},
    {mData: 'maxPrice', sTitle: '最高限价(万元)', bSortable: false},
    {mData: 'contractPeriod', sTitle: '合同期限(天)', bSortable: false},
	{mData: function(source) { return oTableSetButtons(source); }, sTitle: '操作', bSortable: false, sWidth: 161}
];

projectBiddingJsList.renderTable = function () {
    if (projectBiddingJsList.oTable == null) {
        projectBiddingJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": projectBiddingJsList.pageCount,
            "sAjaxSource": getRootPath() + "/project/bidding/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectBiddingJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectBiddingJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectBiddingJsList.oTable.fnDraw();
    }
};

projectBiddingJsList.delete = function (id) {
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
            projectBiddingJsList.deleteCallBack(ids);
        }
    });
};

projectBiddingJsList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/project/bidding/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            projectBiddingJsList.renderTable();
        }
    });
};

projectBiddingJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

projectBiddingJsList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/project/bidding/loadForm.action", null, function() {
        projectBiddingJsForm.init({title: '新增', operator: 'add'});
    });
};

projectBiddingJsList.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/project/bidding/loadForm.action", null, function() {
        projectBiddingJsForm.init({title: '修改', operator: 'modify', id: id});
    });
};

projectBiddingJsList.loadModuleForLook = function (id){
    $("#formModule").load(getRootPath() + "/project/bidding/loadForm.action", null, function() {
        projectBiddingJsForm.init({title: '查看', operator: 'look', id: id});
    });
};

$(document).ready(function(){
    projectBiddingJsList.pageCount = TabNub > 0 ? TabNub : 10;
    projectBiddingJsList.renderTable();
    $('#queryBtn').click(projectBiddingJsList.renderTable);
    $('#resetBtn').click(projectBiddingJsList.queryReset);
    $("#addBtn").click(projectBiddingJsList.loadModuleForAdd);
});