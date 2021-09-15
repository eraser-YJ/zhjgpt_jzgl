var supervisionPointListPanel = {};
supervisionPointListPanel.oTable = null;

supervisionPointListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(supervisionPointListPanel.oTable, aoData);
    var pointName  = $('#searchForm #pointName').val();
    if (pointName.length > 0) { aoData.push({"name": "pointName", "value": pointName}); }
};

supervisionPointListPanel.oTableAoColumns = [
    {mData: 'pointName', sTitle: '监测项', bSortable: false},
    {mData: 'pointDesc', sTitle: '描述', bSortable: false},
	{mData: function(source) {
        var edit = "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"supervisionPointListPanel.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('修改', 'fa-edit2') + "</a>";
        var del = "<a class=\"a-icon i-remove\" href=\"javascript:void(0);\" onclick=\"supervisionPointListPanel.delete('"+ source.id+ "')\">" + finalTableBtnText('删除', 'fa-remove') + "</a>";
        return edit + del;
    }, sTitle: '操作', bSortable: false, sWidth: 145}
];

supervisionPointListPanel.renderTable = function () {
    if (supervisionPointListPanel.oTable == null) {
        supervisionPointListPanel.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": supervisionPointListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/supervise/point/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": supervisionPointListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                supervisionPointListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        supervisionPointListPanel.oTable.fnDraw();
    }
};

supervisionPointListPanel.delete = function (id) {
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
            supervisionPointListPanel.deleteCallBack(ids);
        }
    });
};

supervisionPointListPanel.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/supervise/point/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            supervisionPointListPanel.renderTable();
        }
    });
};

supervisionPointListPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

supervisionPointListPanel.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/supervise/point/loadForm.action", null, function() {
        supervisionPointFormPanel.init({title: '新增监测项', operator: 'add'});
    });
};

supervisionPointListPanel.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/supervise/point/loadForm.action", null, function() {
        supervisionPointFormPanel.init({title: '修改监测项', operator: 'modify', id: id});
    });
};

$(document).ready(function(){
    supervisionPointListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    supervisionPointListPanel.renderTable();
    $('#queryBtn').click(supervisionPointListPanel.renderTable);
    $('#resetBtn').click(supervisionPointListPanel.queryReset);
    $('#addBtn').click(supervisionPointListPanel.loadModuleForAdd);
});