var cmCustomDicListPanel = {};
cmCustomDicListPanel.oTable = null;

cmCustomDicListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(cmCustomDicListPanel.oTable, aoData);
    var paramDataType = $('#searchForm #paramDataType').val();
    if (paramDataType.length > 0) { aoData.push({"name": "dataType", "value": paramDataType}); }
    var paramParentId = $('#searchForm #paramParentId').val();
    if (paramParentId.length > 0) { aoData.push({"name": "parentId", "value": paramParentId}); }
};

cmCustomDicListPanel.oTableAoColumns = [
    {mData: 'code', sTitle: '类别代码', bSortable: false, sWidth: 90},
    {mData: 'name', sTitle: '类别名称', bSortable: false, sWidth: 200},
    {mData: 'dataType', sTitle: '数据类型', sWidth: 120, bSortable: false, mRender : function(mData, type, full) { return full.dataTypeValue; }},
    {mData: 'scope', sTitle: '说明', bSortable: false},
    {mData: function(source) {
        var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"cmCustomDicListPanel.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalParamEditText + "</a>";
        var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"cmCustomDicListPanel.delete('"+ source.id+ "')\">" + finalParamDeleteText + "</a>";
        return edit + del;
    }, sTitle: '操作', bSortable: false, sWidth: 120}
];

cmCustomDicListPanel.renderTable = function () {
    if (cmCustomDicListPanel.oTable == null) {
        cmCustomDicListPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": cmCustomDicListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/custom/dic/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmCustomDicListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmCustomDicListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmCustomDicListPanel.oTable.fnDraw();
    }
};

cmCustomDicListPanel.delete = function (id) {
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
            cmCustomDicListPanel.deleteCallBack(ids);
        }
    });
};

cmCustomDicListPanel.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/custom/dic/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            cmCustomDicListPanel.renderTable();
        }
    });
};

cmCustomDicListPanel.loadModuleForAdd = function (){
    if ($("#searchForm #paramDataType").val() == '') {
        msgBox.tip({ type: "fail", content: "请选择数据类型" });
        return;
    }
    $("#formModule").load(getRootPath() + "/custom/dic/loadForm.action", null, function() {
        cmCustomDicFormPanel.init({
            title: '新增标准代码', operator: 'add',
            dataType: $("#searchForm #paramDataType").val(),
            parentId: $("#searchForm #paramParentId").val(),
            callback: function(data) {
                operatorTreeModule.addNode(cmCustomDicListPanel.treeObj, data.id, data.parentId, data.name, data.queue)
            }
        });
    });
};

cmCustomDicListPanel.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/custom/dic/loadForm.action", null, function() {
        cmCustomDicFormPanel.init({title: '修改标准代码', operator: 'modify', id: id, callback: function(data) {
            operatorTreeModule.editNode(cmCustomDicListPanel.treeObj, data.id, data.name, data.queue);
        }});
    });
};

cmCustomDicListPanel.treeObj = null;

cmCustomDicListPanel.tree = function(){
    function onBeforeClick(id, node){
        if(node.isChecked == 0 && node.pId != null){
            return false;
        }else{
            return true;
        }
    }

    function onClick(event, treeId, treeNode) {
        if (cmCustomDicListPanel.treeObj == null) {
            cmCustomDicListPanel.treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        }
        $("#searchForm #paramParentId").val(treeNode.id);
        cmCustomDicListPanel.renderTable();
    }

    JCTree.ztree({
        container : 'treeDemo',
        expand : true,
        rootNode : true,
        url: getRootPath() + "/custom/dic/queryTree.action?dataType=" + $('#searchForm #paramDataType').val(),
        onClick: onClick,
        onBeforeClick : onBeforeClick,
    });
};

$(document).ready(function(){
    cmCustomDicListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    cmCustomDicListPanel.tree();
    //cmCustomDicListPanel.renderTable();
    $("#addBtn").click(cmCustomDicListPanel.loadModuleForAdd);
    $('#refreshBtn').click(function () { cmCustomDicListPanel.tree(); });
});