var cmProjectPersonJsList = {};
cmProjectPersonJsList.oTable = null;

cmProjectPersonJsList.oTableFnServerParams = function(aoData){
    getTableParameters(cmProjectPersonJsList.oTable, aoData);
    var paramProjectId = $('#searchForm #paramProjectId').val();
    if (paramProjectId.length > 0) { aoData.push({name: "projectId", value: paramProjectId}); }
    aoData.push({name: "canShow", value: '1'});
};

cmProjectPersonJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'partakeType', sTitle: '责任主体', bSortable: false, mRender : function(mData, type, full) { return full.partakeTypeValue; }},
	{mData: 'companyId', sTitle: '单位名称', bSortable: false, mRender : function(mData, type, full) { return full.companyName; }},
	{mData: 'leader', sTitle: '负责人', bSortable: false},
	{mData: 'phone', sTitle: '负责人电话', bSortable: false},
	{mData: function(source) { return oTableSetButtons(source); }, sTitle: '操作', bSortable: false, sWidth: 118}
];

cmProjectPersonJsList.renderTable = function () {
    if (cmProjectPersonJsList.oTable == null) {
        cmProjectPersonJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": cmProjectPersonJsList.pageCount,
            "sAjaxSource": getRootPath() + "/project/person/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmProjectPersonJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmProjectPersonJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmProjectPersonJsList.oTable.fnDraw();
    }
};

cmProjectPersonJsList.delete = function (id) {
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
            cmProjectPersonJsList.deleteCallBack(ids);
        }
    });
};

cmProjectPersonJsList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/project/person/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            cmProjectPersonJsList.renderTable();
        }
    });
};

cmProjectPersonJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

cmProjectPersonJsList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/project/person/loadForm.action", null, function() {
        cmProjectPersonJsForm.init({title: '新增', operator: 'add', projectId: $('#searchForm #paramProjectId').val()});
    });
};

cmProjectPersonJsList.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/project/person/loadForm.action", null, function() {
        cmProjectPersonJsForm.init({title: '修改', operator: 'modify', id: id});
    });
};

$(document).ready(function(){
    cmProjectPersonJsList.pageCount = TabNub > 0 ? TabNub : 10;
    cmProjectPersonJsList.renderTable();
    $('#backBtn').click(function () { parent.JCF.LoadPage({url: '/project/info/manage.action' }); });
    $("#addBtn").click(cmProjectPersonJsList.loadModuleForAdd);
});