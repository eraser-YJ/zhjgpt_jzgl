var supervisionPointColumnListPanel = {};
supervisionPointColumnListPanel.oTable = null;
supervisionPointColumnListPanel.supervisionId = null;

supervisionPointColumnListPanel.oTableAoColumns = [];

supervisionPointColumnListPanel.init = function(config) {
    if (config.operator == 'modify') {
        supervisionPointColumnListPanel.oTableAoColumns = [
            {mData: 'queue', sTitle: '参数顺序', bSortable: false},
            {mData: 'dataMeta', sTitle: '数据描述', bSortable: false},
            {mData: 'dataSource', sTitle: '参数数据来源', bSortable: false, mRender : function(mData, type, full) { return full.dataSourceValue; }},
            {mData: function(source) {
                var edit = "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"supervisionPointColumnListPanel.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('修改', 'fa-edit2') + "</a>";
                var del = "<a class=\"a-icon i-remove\" href=\"javascript:void(0);\" onclick=\"supervisionPointColumnListPanel.delete('"+ source.id+ "')\">" + finalTableBtnText('删除', 'fa-remove') + "</a>";
                return edit + del;
            }, sTitle: '<a href="javascript:void(0);" onclick="supervisionPointColumnListPanel.loadModuleForAdd();">添加</a>', bSortable: false, sWidth: 145}
        ];
    } else {
        supervisionPointColumnListPanel.oTableAoColumns = [
            {mData: 'queue', sTitle: '参数顺序', bSortable: false},
            {mData: 'dataMeta', sTitle: '数据描述', bSortable: false},
            {mData: 'dataSource', sTitle: '参数数据来源', bSortable: false, mRender : function(mData, type, full) { return full.dataSourceValue; }}
        ];
    }
    supervisionPointColumnListPanel.supervisionId = config.supervisionId;
    supervisionPointColumnListPanel.renderTable();
};

supervisionPointColumnListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(supervisionPointColumnListPanel.oTable, aoData);
    aoData.push({"name": "supervisionId", "value": supervisionPointColumnListPanel.supervisionId});
};

supervisionPointColumnListPanel.renderTable = function () {
    if (supervisionPointColumnListPanel.oTable == null) {
        supervisionPointColumnListPanel.oTable =  $('#columnGridTable').dataTable( {
            "iDisplayLength": supervisionPointColumnListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/supervise/pointcolumn/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": supervisionPointColumnListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                supervisionPointColumnListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        supervisionPointColumnListPanel.oTable.fnDraw();
    }
};

supervisionPointColumnListPanel.delete = function (id) {
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
            supervisionPointColumnListPanel.deleteCallBack(ids);
        }
    });
};

supervisionPointColumnListPanel.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/supervise/pointcolumn/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            supervisionPointColumnListPanel.renderTable();
        }
    });
};

supervisionPointColumnListPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

supervisionPointColumnListPanel.loadModuleForAdd = function (){
    $("#formColumnModule").load(getRootPath() + "/supervise/pointcolumn/loadForm.action", null, function() {
        supervisionPointColumnFormPanel.init({title: '新增监测数据来源', operator: 'add',
            supervisionId: supervisionPointColumnListPanel.supervisionId,
            callback: function () {
                supervisionPointColumnListPanel.renderTable();
            }
        });
    });
};

supervisionPointColumnListPanel.loadModuleForUpdate = function (id){
    $("#formColumnModule").load(getRootPath() + "/supervise/pointcolumn/loadForm.action", null, function() {
        supervisionPointColumnFormPanel.init({title: '新增监测数据来源', operator: 'modify', id: id,
            callback: function () {
                supervisionPointColumnListPanel.renderTable();
            }
        });
    });
};

$(document).ready(function(){
    supervisionPointColumnListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    $('#addColumnBtn').click(supervisionPointColumnListPanel.loadModuleForAdd);
});