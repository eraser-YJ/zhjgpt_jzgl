var projectYearPlanCtrlJsList = {};
projectYearPlanCtrlJsList.oTable = null;


projectYearPlanCtrlJsList.oTableFnServerParams = function(aoData){
    getTableParameters(projectYearPlanCtrlJsList.oTable, aoData);
    var planYearCondObj  = $('#searchForm #query_planYear').val();
    if (planYearCondObj && planYearCondObj.length > 0) { aoData.push({"name": "planYear", "value": planYearCondObj}); }
    var query_seqno  = $('#searchForm #query_seqno').val();
    if (query_seqno && query_seqno.length > 0) { aoData.push({"name": "seqno", "value": query_seqno}); }
};

projectYearPlanCtrlJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
    {mData: 'extStr4', sTitle: '年度计划名称', bSortable: false},
    {mData: 'planYear', sTitle: '年度', bSortable: false},
    {mData: 'seqnonameValue', sTitle: '类型', bSortable: false},
    {mData: 'requestStartDate', sTitle: '开始日期', bSortable: false},
    {mData: 'requestEndDate', sTitle: '结束日期', bSortable: false},

	{mData: function(source) {
		var edit = "<a class=\"a-icon i-edit\"  href=\"#myModal-edit\"  onclick=\"projectYearPlanCtrlJsList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">编辑</a>";
		var del = "<a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"projectYearPlanCtrlJsList.delete('"+ source.id+ "')\">删除</a>";
		return edit + del;
	}, sTitle: '操作', bSortable: false, sWidth: 170}
];

projectYearPlanCtrlJsList.renderTable = function () {
    if (projectYearPlanCtrlJsList.oTable == null) {
        projectYearPlanCtrlJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": projectYearPlanCtrlJsList.pageCount,
            "sAjaxSource": getRootPath() + "/plan/ctrl/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectYearPlanCtrlJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectYearPlanCtrlJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectYearPlanCtrlJsList.oTable.fnDraw();
    }
};

projectYearPlanCtrlJsList.delete = function (id) {
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
            projectYearPlanCtrlJsList.deleteCallBack(ids);
        }
    });
};

projectYearPlanCtrlJsList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/plan/ctrl/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            projectYearPlanCtrlJsList.renderTable();
        }
    });
};

projectYearPlanCtrlJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

projectYearPlanCtrlJsList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/plan/ctrl/loadForm.action", null, function() {
        projectYearPlanCtrlJsForm.init({title: '新增', operator: 'add'});
    });
};

projectYearPlanCtrlJsList.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/plan/ctrl/loadForm.action", null, function() {
        projectYearPlanCtrlJsForm.init({title: '修改', operator: 'modify', id: id});
    });
};

$(document).ready(function(){
    //年度初始化
    initSelectYearSearch($("#searchForm #query_planYear"));
    projectYearPlanCtrlJsList.pageCount = TabNub > 0 ? TabNub : 10;
    projectYearPlanCtrlJsList.renderTable();
    $('#queryBtn').click(projectYearPlanCtrlJsList.renderTable);
    $('#resetBtn').click(projectYearPlanCtrlJsList.queryReset);
    $("#addBtn").click(projectYearPlanCtrlJsList.loadModuleForAdd);
});