var safetyUnitJsList = {};
safetyUnitJsList.oTable = null;

safetyUnitJsList.oTableFnServerParams = function(aoData){
    getTableParameters(safetyUnitJsList.oTable, aoData);

};

safetyUnitJsList.oTableAoColumns = [
    {mData: 'unitName', sTitle: '单位名称', bSortable: false},
    {mData: 'projectLeader', sTitle: '项目负责人', bSortable: false},
    {mData: 'phone', sTitle: '联系电话', bSortable: false},
    {mData: 'partakeType', sTitle: '单位类别(字典)', bSortable: false},
	{mData: function(source) {
		var edit = "<a class=\"a-icon i-new\"  href=\"#myModal-edit\"  onclick=\"safetyUnitJsList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalParamEditText + "</a>";
		var del = "<a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"safetyUnitJsList.delete('"+ source.id+ "')\">" + finalParamDeleteText + "</a>";
		return edit + del;
	}, sTitle: '操作', bSortable: false, sWidth: 170}
];

safetyUnitJsList.renderTable = function () {
    if (safetyUnitJsList.oTable == null) {
        safetyUnitJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": safetyUnitJsList.pageCount,
            "sAjaxSource": getRootPath() + "/csmp/safe/supervision/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": safetyUnitJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                safetyUnitJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        safetyUnitJsList.oTable.fnDraw();
    }
};

safetyUnitJsList.delete = function (id) {
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
            safetyUnitJsList.deleteCallBack(ids);
        }
    });
};

safetyUnitJsList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/csmp/safe/supervision/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            safetyUnitJsList.renderTable();
        }
    });
};

safetyUnitJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

safetyUnitJsList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/csmp/safe/supervision/loadForm.action", null, function() {
        safetyUnitJsForm.init({title: '新增', operator: 'add'});
    });
};

safetyUnitJsList.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/csmp/safe/supervision/loadForm.action", null, function() {
        safetyUnitJsForm.init({title: '修改', operator: 'modify', id: id});
    });
};

$(document).ready(function(){
    safetyUnitJsList.pageCount = TabNub > 0 ? TabNub : 10;
    safetyUnitJsList.renderTable();
    $('#queryBtn').click(safetyUnitJsList.renderTable);
    $('#resetBtn').click(safetyUnitJsList.queryReset);
    $("#addBtn").click(safetyUnitJsList.loadModuleForAdd);
});