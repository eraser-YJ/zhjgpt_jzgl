var cmProjectPlanInfoList = {};
cmProjectPlanInfoList.oTable = null;

cmProjectPlanInfoList.oTableFnServerParams = function(aoData){
    getTableParameters(cmProjectPlanInfoList.oTable, aoData);
    var paramProjectId = $('#searchForm #paramProjectId').val();
    if (paramProjectId.length > 0) { aoData.push({"name": "projectId", "value": paramProjectId}); }
    var paramStageId = $('#searchForm #paramStageId').val();
    if (paramStageId.length > 0) { aoData.push({"name": "stageId", "value": paramStageId}); }
};

cmProjectPlanInfoList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'stageId', sTitle: '所属阶段', bSortable: false, mRender : function(mData, type, full) { return full.stageName; }},
    {mData: 'planCode', sTitle: '计划编码', bSortable: false},
    {mData: 'planName', sTitle: '计划名称', bSortable: false},
    {mData: 'dutyCompany', sTitle: '责任单位', bSortable: false, mRender : function(mData, type, full) { return full.dutyCompanyValue; }},
    {mData: 'dutyPerson', sTitle: '责任人', bSortable: false},
    {mData: 'planStartDate', sTitle: '计划开始时间', bSortable: false},
    {mData: 'planEndDate', sTitle: '计划结束时间', bSortable: false},
    {mData: 'completionRatio', sTitle: '已完进度(%)', bSortable: false},
    {mData: 'statusValue', sTitle: '状态', bSortable: false},
    {mData: function(source) {
        var look = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"cmProjectPlanInfoList.loadModuleForLook('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
        var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"cmProjectPlanInfoList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('修改', 'fa-edit2') + "</a>";
        var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"cmProjectPlanInfoList.delete('"+ source.id+ "')\">" + finalTableBtnText('删除', 'fa-remove') + "</a>";
        return look + edit + del;
    }, sTitle: '操作', bSortable: false, sWidth: 180}
];

cmProjectPlanInfoList.renderTable = function () {
    if (cmProjectPlanInfoList.oTable == null) {
        cmProjectPlanInfoList.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": cmProjectPlanInfoList.pageCount,
            "sAjaxSource": getRootPath() + "/project/plan/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmProjectPlanInfoList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmProjectPlanInfoList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmProjectPlanInfoList.oTable.fnDraw();
    }
};

cmProjectPlanInfoList.delete = function (id) {
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
            cmProjectPlanInfoList.deleteCallBack(ids);
        }
    });
};

cmProjectPlanInfoList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/project/plan/info/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            cmProjectPlanInfoList.renderTable();
        }
    });
};

cmProjectPlanInfoList.loadModuleForAdd = function (){
    if ($("#searchForm #paramStageId").val() == '' || $("#searchForm #paramStageId").val() == '0') {
        msgBox.tip({ type: "fail", content: "请选择所属阶段" });
        return;
    }
    $("#taskModule").load(getRootPath() + "/project/plan/info/loadForm.action", null, function() {
        cmProjectPlanInfoFormPanel.init({
            title: '新增阶段任务', operator: 'add', projectId: $('#searchForm #paramProjectId').val(), stageId: $('#searchForm #paramStageId').val(),
            callback: function () {
                cmProjectPlanInfoList.renderTable();
            }
        });
    });
};

cmProjectPlanInfoList.loadModuleForUpdate = function (id){
    $("#taskModule").load(getRootPath() + "/project/plan/info/loadForm.action", null, function() {
        cmProjectPlanInfoFormPanel.init({
            title: '修改计划信息', operator: 'modify', id: id,
            callback: function () {
                cmProjectPlanInfoList.renderTable();
            }
        });
    });
};

cmProjectPlanInfoList.loadModuleForLook = function (id){
    $("#taskModule").load(getRootPath() + "/project/plan/info/loadLook.action", null, function() {
        cmProjectPlanLookModule.init({title: '查看计划信息', id: id});
    });
};

cmProjectPlanInfoList.treeObj = null;

cmProjectPlanInfoList.tree = function(){
    function onBeforeClick(id, node){
        if(node.isChecked == 0 && node.pId != null){
            return false;
        }else{
            return true;
        }
    }

    function onClick(event, treeId, treeNode) {
        if (cmProjectPlanInfoList.treeObj == null) {
            cmProjectPlanInfoList.treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        }
        $('#stageNameHtml').html((treeNode.id == '0' ? "项目名称：" : "当前阶段: ") + treeNode.name);
        $("#searchForm #paramStageId").val(treeNode.id == '0' ? '' : treeNode.id);
        $("#searchForm #paramStageName").val(treeNode.name);
        cmProjectPlanInfoList.renderTable();
    }

    JCTree.ztree({
        container : 'treeDemo',
        expand : true,
        rootNode : true,
        //isSearch: true,
        url: getRootPath() + "/project/plan/stage/treeList.action?projectId=" + $('#searchForm #paramProjectId').val(),
        onClick: onClick,
        onBeforeClick : onBeforeClick/*,
        ready: function (data) {
            operatorTreeModule.searchTreeInit({treeObj: cmProjectPlanInfoList.treeObj, data: data});
        }*/
    });
};

/**
 * 加载添加阶段功能
 */
cmProjectPlanInfoList.loadStageAdd = function() {
    $("#stageModule").load(getRootPath() + "/project/plan/stage/loadForm.action", null, function() {
        cmProjectPlanStageFormPanel.init({title: '添加阶段', operator: 'add',
            parentId: $('#searchForm #paramStageId').val(), parentName: $('#searchForm #paramStageName').val(),
            projectId: $('#searchForm #paramProjectId').val(), callback: function(data) {
                operatorTreeModule.addNode(cmProjectPlanInfoList.treeObj, data.id, data.parentId, data.stageName, data.queue)
            }
        });
    });
};

/**
 * 加载修改阶段功能
 * @param id
 */
cmProjectPlanInfoList.loadStageModify = function() {
    if ($('#searchForm #paramStageId').val() == "" || $('#searchForm #paramStageId').val() == '0') {
        msgBox.tip({ type: "fail", content: "请选择要修改的阶段" });
        return;
    }
    $("#stageModule").load(getRootPath() + "/project/plan/stage/loadForm.action", null, function() {
        cmProjectPlanStageFormPanel.init({title: '添加阶段', operator: 'modify',
            id: $('#searchForm #paramStageId').val(), projectId: $('#searchForm #paramProjectId').val(), callback: function(data) {
                operatorTreeModule.editNode(cmProjectPlanInfoList.treeObj, data.id, data.stageName, data.queue);
            }
        });
    });
};

/**
 * 删除阶段
 */
cmProjectPlanInfoList.deleteStage = function() {
    if ($('#searchForm #paramStageId').val() == "" || $('#searchForm #paramStageId').val() == '0') {
        msgBox.tip({ type: "fail", content: "请选择要删除的阶段" });
        return;
    }
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function(){
            $.ajax({
                type: "POST", url: getRootPath() + "/project/plan/stage/deleteByIds.action", data: {"ids": $('#searchForm #paramStageId').val()}, dataType: "json",
                success : function(data) {
                    if (data.success == "true") {
                        msgBox.tip({ type:"success", content: data.successMessage });
                        operatorTreeModule.deleteNode(cmProjectPlanInfoList.treeObj, $('#searchForm #paramStageId').val());
                    } else {
                        msgBox.info({ content: data.errorMessage });
                    }
                    cmProjectPlanInfoList.tree();
                }
            });
        }
    });
};

$(document).ready(function(){
    cmProjectPlanInfoList.pageCount = TabNub > 0 ? TabNub : 10;
    cmProjectPlanInfoList.tree();
    $("#addBtn").click(cmProjectPlanInfoList.loadModuleForAdd);
    $('#addStageBtn').click(function() { cmProjectPlanInfoList.loadStageAdd() });
    $('#editStageBtn').click(function() { cmProjectPlanInfoList.loadStageModify() });
    $('#deleteStageBtn').click(function () { cmProjectPlanInfoList.deleteStage(); });
});