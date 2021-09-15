var dlhTableMapJsList = {};
dlhTableMapJsList.oTable = null;

dlhTableMapJsList.oTableFnServerParams = function(aoData){
    getTableParameters(dlhTableMapJsList.oTable, aoData);
};

dlhTableMapJsList.oTableAoColumns = [
    /*{mData: 'objUrlK', sTitle: 'dataObject对象obj_url', bSortable: false},
    {mData: 'objUrlV', sTitle: 'dataObject对象obj_url', bSortable: false},*/
    {mData: 'tableNameK', sTitle: '表名K', bSortable: false},
    {mData: 'columnNameK', sTitle: '列名K', bSortable: false},
    {mData: 'tableNameV', sTitle: '表名V', bSortable: false},
    {mData: 'columnNameV', sTitle: '列名V', bSortable: false},
    {mData: 'columnType', sTitle: '类型', bSortable: false},
	{mData: function(source) {
		var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"dlhTableMapJsList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\"> 修 改 </a>";
		var del = "<a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"dlhTableMapJsList.delete('"+ source.id+ "')\"> 删 除 </a>";
		return edit + del;
	}, sTitle: '操作', bSortable: false, sWidth: 170}
];

dlhTableMapJsList.renderTable = function () {
    if (dlhTableMapJsList.oTable == null) {
        dlhTableMapJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": dlhTableMapJsList.pageCount,
            "sAjaxSource": getRootPath() + "/dlh/tablemap/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": dlhTableMapJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                dlhTableMapJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        dlhTableMapJsList.oTable.fnDraw();
    }
};

dlhTableMapJsList.delete = function (id) {
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
            dlhTableMapJsList.deleteCallBack(ids);
        }
    });
};

dlhTableMapJsList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/dlh/tablemap/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            dlhTableMapJsList.renderTable();
        }
    });
};

dlhTableMapJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

dlhTableMapJsList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/dlh/tablemap/loadForm.action", null, function() {
        dlhTableMapJsForm.init({title: '新增', operator: 'add'});
    });
};

dlhTableMapJsList.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/dlh/tablemap/loadForm.action", null, function() {
        dlhTableMapJsForm.init({title: '修改', operator: 'modify', id: id});
    });
};

$(document).ready(function(){
    dlhTableMapJsList.pageCount = TabNub > 0 ? TabNub : 10;
    dlhTableMapJsList.renderTable();
    $('#queryBtn').click(dlhTableMapJsList.renderTable);
    $('#resetBtn').click(dlhTableMapJsList.queryReset);
    $("#addBtn").click(dlhTableMapJsList.loadModuleForAdd);
});