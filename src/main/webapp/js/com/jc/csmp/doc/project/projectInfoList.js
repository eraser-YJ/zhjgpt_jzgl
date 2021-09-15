var projectInfoJsList = {};
projectInfoJsList.oTable = null;

projectInfoJsList.oTableFnServerParams = function(aoData){
    getTableParameters(projectInfoJsList.oTable, aoData);

    var projectNumberCondObj  = $('#searchForm #query_projectNumber').val();
    if (projectNumberCondObj.length > 0) { aoData.push({"name": "projectNumber", "value": projectNumberCondObj}); }
    var projectNameCondObj  = $('#searchForm #query_projectName').val();
    if (projectNameCondObj.length > 0) { aoData.push({"name": "projectName", "value": projectNameCondObj}); }
};

projectInfoJsList.oTableAoColumns = [
    {mData: 'projectNumber', sTitle: '项目编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'region', sTitle: '行政区', bSortable: false},
    {mData: 'superviseDeptId', sTitle: '监管单位', bSortable: false},
    {mData: 'buildDeptId', sTitle: '建设单位', bSortable: false},
	{mData: function(source) {
		var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"projectInfoJsList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">查看</a>";
		return edit;
	}, sTitle: '操作', bSortable: false, sWidth: 170}
];

projectInfoJsList.renderTable = function () {
    if (projectInfoJsList.oTable == null) {
        projectInfoJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": projectInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/doc/project/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectInfoJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectInfoJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectInfoJsList.oTable.fnDraw();
    }
};

projectInfoJsList.delete = function (id) {
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
            projectInfoJsList.deleteCallBack(ids);
        }
    });
};

projectInfoJsList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/doc/project/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            projectInfoJsList.renderTable();
        }
    });
};

projectInfoJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

projectInfoJsList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/doc/project/loadForm.action", null, function() {
        projectInfoJsForm.init({title: '新增', operator: 'add'});
    });
};

projectInfoJsList.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/doc/project/loadForm.action", null, function() {
        projectInfoJsForm.init({title: '查看', operator: 'modify', id: id});
    });
};

$(document).ready(function(){
    projectInfoJsList.pageCount = TabNub > 0 ? TabNub : 10;
    projectInfoJsList.renderTable();
    $('#queryBtn').click(projectInfoJsList.renderTable);
    $('#resetBtn').click(projectInfoJsList.queryReset);
    $("#addBtn").click(projectInfoJsList.loadModuleForAdd);
});