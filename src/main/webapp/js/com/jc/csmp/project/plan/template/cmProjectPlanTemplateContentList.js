var cmProjectPlanTemplateContentListPanel = {};
cmProjectPlanTemplateContentListPanel.oTable = null;

cmProjectPlanTemplateContentListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(cmProjectPlanTemplateContentListPanel.oTable, aoData);
    var paramTemplateId = $('#searchForm #paramTemplateId').val();
    if (paramTemplateId.length > 0) { aoData.push({"name": "templateId", "value": paramTemplateId}); }
    var paramStageId = $('#searchForm #paramStageId').val();
    if (paramStageId.length > 0) { aoData.push({"name": "stageId", "value": paramStageId}); }
};

cmProjectPlanTemplateContentListPanel.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'stageId', sTitle: '所属阶段', bSortable: false, mRender : function(mData, type, full) { return full.stageName; }},
    {mData: 'taskNumber', sTitle: '任务编码', bSortable: false},
    {mData: 'taskName', sTitle: '任务名称', bSortable: false},
    {mData: 'planDay', sTitle: '计划日历天', bSortable: false},
    {mData: 'planWorkDay', sTitle: '计划工时天', bSortable: false},
    {mData: function(source) {
        var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"cmProjectPlanTemplateContentListPanel.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('修改', 'fa-edit2') + "</a>";
        var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"cmProjectPlanTemplateContentListPanel.delete('"+ source.id+ "')\">" + finalTableBtnText('删除', 'fa-remove') + "</a>";
        return edit + del;
    }, sTitle: '操作', bSortable: false, sWidth: 131}
];

cmProjectPlanTemplateContentListPanel.renderTable = function () {
    if (cmProjectPlanTemplateContentListPanel.oTable == null) {
        cmProjectPlanTemplateContentListPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": cmProjectPlanTemplateContentListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/project/plan/templatetask/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmProjectPlanTemplateContentListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmProjectPlanTemplateContentListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmProjectPlanTemplateContentListPanel.oTable.fnDraw();
    }
};

cmProjectPlanTemplateContentListPanel.delete = function (id) {
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
            cmProjectPlanTemplateContentListPanel.deleteCallBack(ids);
        }
    });
};

cmProjectPlanTemplateContentListPanel.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/project/plan/templatetask/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            cmProjectPlanTemplateContentListPanel.renderTable();
        }
    });
};

cmProjectPlanTemplateContentListPanel.loadModuleForAdd = function (){
    if ($("#searchForm #paramStageId").val() == '' || $("#searchForm #paramStageId").val() == '0') {
        msgBox.tip({ type: "fail", content: "请选择所属阶段" });
        return;
    }
    $("#taskModule").load(getRootPath() + "/project/plan/templatetask/loadForm.action", null, function() {
        cmProjectPlanTemplateStageFormPanel.init({
            title: '新增阶段任务', operator: 'add', templateId: $('#searchForm #paramTemplateId').val(), stageId: $('#searchForm #paramStageId').val(),
            callback: function () {
                cmProjectPlanTemplateContentListPanel.renderTable();
            }
        });
    });
};

cmProjectPlanTemplateContentListPanel.loadModuleForUpdate = function (id){
    $("#taskModule").load(getRootPath() + "/project/plan/templatetask/loadForm.action", null, function() {
        cmProjectPlanTemplateStageFormPanel.init({
            title: '修改标阶段任务', operator: 'modify', id: id,
            callback: function () {
                cmProjectPlanTemplateContentListPanel.renderTable();
            }
        });
    });
};

cmProjectPlanTemplateContentListPanel.leftTreeObj = null;

cmProjectPlanTemplateContentListPanel.tree = function(){
    function onBeforeClick(id, node){
        if(node.isChecked == 0 && node.pId != null){
            return false;
        }else{
            return true;
        }
    }

    function onClick(event, treeId, treeNode) {
        if (cmProjectPlanTemplateContentListPanel.leftTreeObj == null) {
            cmProjectPlanTemplateContentListPanel.leftTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
        }
        $('#stageNameHtml').html("当前阶段: " + treeNode.name);
        $("#searchForm #paramStageId").val(treeNode.id == '0' ? '' : treeNode.id);
        $('#searchForm #paramStageName').val(treeNode.name);
        cmProjectPlanTemplateContentListPanel.renderTable();
    }

    JCTree.ztree({
        container : 'treeDemo',
        expand : true,
        rootNode : true,
        isSearch: false,
        url: getRootPath() + "/project/plan/templatestage/treeList.action?templateId=" + $('#searchForm #paramTemplateId').val(),
        onClick: onClick,
        onBeforeClick : onBeforeClick/*,
        ready: function (data) {
            operatorTreeModule.searchTreeInit({treeObj: cmProjectPlanTemplateContentListPanel.leftTreeObj, data: data});
        }*/
    });
};

/**
 * 加载添加阶段功能
 */
cmProjectPlanTemplateContentListPanel.loadStageAdd = function() {
    $("#stageModule").load(getRootPath() + "/project/plan/templatestage/loadForm.action", null, function() {
        cmProjectPlanTemplateStageFormPanel.init({title: '添加阶段', operator: 'add',
            parentId: $('#searchForm #paramStageId').val(), parentName: $('#searchForm #paramStageName').val(),
            templateId: $('#searchForm #paramTemplateId').val(), callback: function(data) {
                operatorTreeModule.addNode(cmProjectPlanTemplateContentListPanel.leftTreeObj, data.id, data.parentId, data.stageName, data.queue)
            }
        });
    });
};

/**
 * 加载修改阶段功能
 * @param id
 */
cmProjectPlanTemplateContentListPanel.loadStageModify = function() {
    if ($('#searchForm #paramStageId').val() == "" || $('#searchForm #paramStageId').val() == '0') {
        msgBox.tip({ type: "fail", content: "请选择要修改的阶段" });
        return;
    }
    $("#stageModule").load(getRootPath() + "/project/plan/templatestage/loadForm.action", null, function() {
        cmProjectPlanTemplateStageFormPanel.init({title: '修改阶段', operator: 'modify',
            id: $('#searchForm #paramStageId').val(), templateId: $('#searchForm #paramTemplateId').val(), callback: function(data) {
                operatorTreeModule.editNode(cmProjectPlanTemplateContentListPanel.leftTreeObj, data.id, data.stageName, data.queue);
            }
        });
    });
};

/**
 * 删除阶段
 */
cmProjectPlanTemplateContentListPanel.deleteStage = function() {
    if ($('#searchForm #paramStageId').val() == "" || $('#searchForm #paramStageId').val() == '0') {
        msgBox.tip({ type: "fail", content: "请选择要删除的阶段" });
        return;
    }
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function(){
            $.ajax({
                type: "POST", url: getRootPath() + "/project/plan/templatestage/deleteByIds.action", data: {"ids": $('#searchForm #paramStageId').val()}, dataType: "json",
                success : function(data) {
                    if (data.success == "true") {
                        msgBox.tip({ type:"success", content: data.successMessage });
                        operatorTreeModule.deleteNode(cmProjectPlanTemplateContentListPanel.leftTreeObj, $('#searchForm #paramStageId').val());
                    } else {
                        msgBox.info({ content: data.errorMessage });
                    }
                }
            });
        }
    });
};

cmProjectPlanTemplateContentListPanel.updateTemplateName = function() {
    $.ajax({
        type: "POST", url: getRootPath() + "/project/plan/template/update.action",
        data: {id: $('#searchForm #paramTemplateId').val(), templateName: $('#searchForm #templateName').val()}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
        }
    });
};

$(document).ready(function(){
    cmProjectPlanTemplateContentListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    cmProjectPlanTemplateContentListPanel.tree();
    $("#addBtn").click(cmProjectPlanTemplateContentListPanel.loadModuleForAdd);
    $('#updateBtn').click(cmProjectPlanTemplateContentListPanel.updateTemplateName);
    $('#refreshBtn').click(function () { cmProjectPlanTemplateContentListPanel.tree(); });
    $('#addStageBtn').click(function() { cmProjectPlanTemplateContentListPanel.loadStageAdd() });
    $('#editStageBtn').click(function() { cmProjectPlanTemplateContentListPanel.loadStageModify() });
    $('#deleteStageBtn').click(function () { cmProjectPlanTemplateContentListPanel.deleteStage(); });
});