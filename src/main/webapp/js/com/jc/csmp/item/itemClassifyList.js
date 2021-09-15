var itemClassifyJsList = {};
itemClassifyJsList.oTable = null;

itemClassifyJsList.oTableFnServerParams = function(aoData){
    getTableParameters(itemClassifyJsList.oTable, aoData);
    var itemClassifyCondObj  = $('#searchForm #query_itemClassify').val();
    if (itemClassifyCondObj.length > 0) { aoData.push({"name": "itemClassify", "value": itemClassifyCondObj}); }
};

itemClassifyJsList.oTableAoColumns = [
    {mData: 'itemClassify', sTitle: '项目分类', bSortable: false},
    {mData: 'itemCode', sTitle: '项目分类编码', bSortable: false},
	{mData: function(source) {
		var edit = "<a class=\"a-icon i-new\"  href=\"#myModal-edit\"  onclick=\"itemClassifyJsList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalParamEditText + "</a>";
		var del = "<a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"itemClassifyJsList.delete('"+ source.id+ "')\">" + finalParamDeleteText + "</a>";
		return edit + del;
	}, sTitle: '操作', bSortable: false, sWidth: 170}
];

itemClassifyJsList.renderTable = function () {
    if (itemClassifyJsList.oTable == null) {
        itemClassifyJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": itemClassifyJsList.pageCount,
            "sAjaxSource": getRootPath() + "/csmp/item/classify/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": itemClassifyJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                itemClassifyJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        itemClassifyJsList.oTable.fnDraw();
    }
};

itemClassifyJsList.delete = function (id) {
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
            itemClassifyJsList.deleteCallBack(ids);
        }
    });
};

itemClassifyJsList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/csmp/item/classify/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            itemClassifyJsList.renderTable();
        }
    });
};

itemClassifyJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

itemClassifyJsList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/csmp/item/classify/loadForm.action", null, function() {
        itemClassifyJsForm.init({title: '新增', operator: 'add'});
    });
};

itemClassifyJsList.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/csmp/item/classify/loadForm.action", null, function() {
        itemClassifyJsForm.init({title: '修改', operator: 'modify', id: id});
    });
};

$(document).ready(function(){
    itemClassifyJsList.pageCount = TabNub > 0 ? TabNub : 10;
    itemClassifyJsList.renderTable();
    $('#queryBtn').click(itemClassifyJsList.renderTable);
    $('#resetBtn').click(itemClassifyJsList.queryReset);
    $("#addBtn").click(itemClassifyJsList.loadModuleForAdd);
});