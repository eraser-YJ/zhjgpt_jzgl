var itemClassifyAttachJsList = {};
itemClassifyAttachJsList.oTable = null;
itemClassifyAttachJsList.oTableFnServerParams = function(aoData){
    getTableParameters(itemClassifyAttachJsList.oTable, aoData);
    var itemAttachCondObj  = $('#searchForm #query_itemAttach').val();
    if (itemAttachCondObj.length > 0) { aoData.push({"name": "itemAttach", "value": itemAttachCondObj}); }
    var itemIdCondObj  = $('#searchForm #query_itemId').val();
    if (itemIdCondObj.length > 0) { aoData.push({"name": "itemId", "value": itemIdCondObj}); }
};
itemClassifyAttachJsList.oTableAoColumns = [
    {mData: 'itemAttach', sTitle: '附件名称', bSortable: false},
    {mData: 'itemName', sTitle: '项目分类', bSortable: false},
    {mData: "isRequired", sTitle: '是否必填',mRender : function(mData, type, full){
            if(full.isRequired==0){
                return "是";
            }else{
                return "否";
            }
        }
    },
    {mData: "isCheckbox", sTitle: '是否多个',mRender : function(mData, type, full){
            if(full.isCheckbox==0){
                return "是";
            }else{
                return "否";
            }
        }
    },
    {mData: 'extNum1', sTitle: '排序', bSortable: false},
	{mData: function(source) {
		var edit = "<a class=\"a-icon i-new\"  href=\"#myModal-edit\"  onclick=\"itemClassifyAttachJsList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalParamEditText + "</a>";
		var del = "<a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"itemClassifyAttachJsList.delete('"+ source.id+ "')\">" + finalParamDeleteText + "</a>";
		return edit + del;
	}, sTitle: '操作', bSortable: false, sWidth: 170}
];

itemClassifyAttachJsList.renderTable = function () {
    if (itemClassifyAttachJsList.oTable == null) {
        itemClassifyAttachJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": itemClassifyAttachJsList.pageCount,
            "sAjaxSource": getRootPath() + "/csmp/item/attach/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": itemClassifyAttachJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                itemClassifyAttachJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        itemClassifyAttachJsList.oTable.fnDraw();
    }
};

itemClassifyAttachJsList.delete = function (id) {
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
            itemClassifyAttachJsList.deleteCallBack(ids);
        }
    });
};

itemClassifyAttachJsList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/csmp/item/attach/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            itemClassifyAttachJsList.renderTable();
        }
    });
};

itemClassifyAttachJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

itemClassifyAttachJsList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/csmp/item/attach/loadForm.action", null, function() {
        itemClassifyAttachJsForm.init({title: '新增', operator: 'add'});
    });
};

itemClassifyAttachJsList.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/csmp/item/attach/loadForm.action", null, function() {
        itemClassifyAttachJsForm.init({title: '修改', operator: 'modify', id: id});
    });
};

$(document).ready(function(){
    itemClassifyAttachJsList.pageCount = TabNub > 0 ? TabNub : 10;
    itemClassifyAttachJsList.renderTable();
    $('#queryBtn').click(itemClassifyAttachJsList.renderTable);
    $('#resetBtn').click(itemClassifyAttachJsList.queryReset);
    $("#addBtn").click(itemClassifyAttachJsList.loadModuleForAdd);
});