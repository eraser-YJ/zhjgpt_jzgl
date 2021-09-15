var cmProjectPlanTemplateListPanel = {};
cmProjectPlanTemplateListPanel.oTable = null;

cmProjectPlanTemplateListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(cmProjectPlanTemplateListPanel.oTable, aoData);
    var templateName = $('#searchForm #templateName').val();
    if (templateName.length > 0) { aoData.push({"name": "templateName", "value": templateName}); }
};

cmProjectPlanTemplateListPanel.oTableAoColumns = [
    {mData: 'templateName', sTitle: '模板名称', bSortable: false},
    {mData: function(source) {
        var edit = "<shiro:hasPermission name='projectUpdate'><a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"cmProjectPlanTemplateListPanel.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('修改', 'fa-edit2') + "</a></shiro:hasPermission>";
        var del = "<shiro:hasPermission name='projectDelete'><a class=\"a-icon i-remove\" href=\"javascript:void(0);\" onclick=\"cmProjectPlanTemplateListPanel.delete('"+ source.id+ "')\">" + finalTableBtnText('删除', 'fa-remove') + "</a></shiro:hasPermission>";
        return edit + del;
    }, sTitle: '操作', bSortable: false, sWidth: 131}
];

cmProjectPlanTemplateListPanel.renderTable = function () {
    if (cmProjectPlanTemplateListPanel.oTable == null) {
        cmProjectPlanTemplateListPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": cmProjectPlanTemplateListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/project/plan/template/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmProjectPlanTemplateListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmProjectPlanTemplateListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmProjectPlanTemplateListPanel.oTable.fnDraw();
    }
};

cmProjectPlanTemplateListPanel.delete = function (id) {
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
            cmProjectPlanTemplateListPanel.deleteCallBack(ids);
        }
    });
};

cmProjectPlanTemplateListPanel.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/project/plan/template/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            cmProjectPlanTemplateListPanel.renderTable();
        }
    });
};

cmProjectPlanTemplateListPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

/**
 * 保存成功后回调
 * @param id
 */
cmProjectPlanTemplateListPanel.saveSuccess = function(id) {
    parent.JCF.LoadPage({url: '/project/plan/template/templateContent.action?templateId='+ id });
};

cmProjectPlanTemplateListPanel.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/project/plan/template/loadForm.action", null, function() {
        cmProjectPlanTemplateFormPanel.init({title: '新增模板', operator: 'add'});
    });
};

cmProjectPlanTemplateListPanel.loadModuleForUpdate = function (id){
    parent.JCF.LoadPage({url: '/project/plan/template/templateContent.action?templateId='+ id });
};

$(document).ready(function(){
    cmProjectPlanTemplateListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    cmProjectPlanTemplateListPanel.renderTable();
    $('#queryBtn').click(cmProjectPlanTemplateListPanel.renderTable);
    $('#resetBtn').click(cmProjectPlanTemplateListPanel.queryReset);
    $("#addBtn").click(cmProjectPlanTemplateListPanel.loadModuleForAdd);
});