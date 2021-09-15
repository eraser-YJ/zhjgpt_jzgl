var warnRuleJsList = {};
warnRuleJsList.oTable = null;

warnRuleJsList.oTableFnServerParams = function(aoData){
    getTableParameters(warnRuleJsList.oTable, aoData);

    var ruleCodeCondObj  = $('#searchForm #query_ruleCode').val();
    if (ruleCodeCondObj.length > 0) { aoData.push({"name": "ruleCode", "value": ruleCodeCondObj}); }
    var ruleNameCondObj  = $('#searchForm #query_ruleName').val();
    if (ruleNameCondObj.length > 0) { aoData.push({"name": "ruleName", "value": ruleNameCondObj}); }
};

warnRuleJsList.oTableAoColumns = [
    {mData: 'ruleCode', sTitle: '规则编码', bSortable: false},
    {mData: 'ruleName', sTitle: '规则名称', bSortable: false},
	{mData: function(source) {
		var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"warnRuleJsList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalParamEditText + "</a>";
		var del = "<a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"warnRuleJsList.delete('"+ source.id+ "')\">" + finalParamDeleteText + "</a>";
		return edit + del;
	}, sTitle: '操作', bSortable: false, sWidth: 170}
];

warnRuleJsList.renderTable = function () {
    if (warnRuleJsList.oTable == null) {
        warnRuleJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": warnRuleJsList.pageCount,
            "sAjaxSource": getRootPath() + "/warn/rule/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": warnRuleJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                warnRuleJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        warnRuleJsList.oTable.fnDraw();
    }
};

warnRuleJsList.delete = function (id) {
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
            warnRuleJsList.deleteCallBack(ids);
        }
    });
};

warnRuleJsList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/warn/rule/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            warnRuleJsList.renderTable();
        }
    });
};

warnRuleJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

warnRuleJsList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/warn/rule/loadForm.action", null, function() {
        warnRuleJsForm.init({title: '新增', operator: 'add'});
    });
};

warnRuleJsList.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/warn/rule/loadForm.action", null, function() {
        warnRuleJsForm.init({title: '修改', operator: 'modify', id: id});
    });
};

$(document).ready(function(){
    warnRuleJsList.pageCount = TabNub > 0 ? TabNub : 10;
    warnRuleJsList.renderTable();
    $('#queryBtn').click(warnRuleJsList.renderTable);
    $('#resetBtn').click(warnRuleJsList.queryReset);
    $("#addBtn").click(warnRuleJsList.loadModuleForAdd);
});