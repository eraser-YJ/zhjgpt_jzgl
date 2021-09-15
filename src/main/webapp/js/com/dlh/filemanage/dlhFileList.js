var dlhFileJsList = {};
dlhFileJsList.oTable = null;


dlhFileJsList.oTableFnServerParams = function(aoData){
    getTableParameters(dlhFileJsList.oTable, aoData);
};

dlhFileJsList.oTableAoColumns = [
    {mData: 'filename', sTitle: '文件名称', bSortable: false},
    {mData: 'objurl', sTitle: '数据对象路径', bSortable: false},
    {mData: 'yewucolname', sTitle: '列名', bSortable: false},
    {mData: 'remark', sTitle: '备注', bSortable: false},
    {mData: 'tableName', sTitle: '表名', bSortable: false},
    {mData: 'userId', sTitle: 'userId', bSortable: false},
	{mData: function(source) {
		var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"dlhFileJsList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalParamEditText + "</a>";
		var del = "<a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"dlhFileJsList.delete('"+ source.id+ "')\">" + finalParamDeleteText + "</a>";
		return edit + del;
	}, sTitle: '操作', bSortable: false, sWidth: 170}
];

dlhFileJsList.renderTable = function () {
    if (dlhFileJsList.oTable == null) {
        dlhFileJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": dlhFileJsList.pageCount,
            "sAjaxSource": getRootPath() + "/dlh/filemanage/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": dlhFileJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                dlhFileJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        dlhFileJsList.oTable.fnDraw();
    }
};

dlhFileJsList.delete = function (id) {
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
            dlhFileJsList.deleteCallBack(ids);
        }
    });
};

dlhFileJsList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/dlh/filemanage/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            dlhFileJsList.renderTable();
        }
    });
};

dlhFileJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

dlhFileJsList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/dlh/filemanage/loadForm.action", null, function() {
        dlhFileJsForm.init({title: '新增', operator: 'add'});
    });
};

dlhFileJsList.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/dlh/filemanage/loadForm.action", null, function() {
        dlhFileJsForm.init({title: '修改', operator: 'modify', id: id});
    });
};

$(document).ready(function(){
    dlhFileJsList.pageCount = TabNub > 0 ? TabNub : 10;
    dlhFileJsList.renderTable();
    $('#queryBtn').click(dlhFileJsList.renderTable);
    $('#resetBtn').click(dlhFileJsList.queryReset);
    $("#addBtn").click(dlhFileJsList.loadModuleForAdd);

});