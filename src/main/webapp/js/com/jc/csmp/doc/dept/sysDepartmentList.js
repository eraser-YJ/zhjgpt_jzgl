var sysDepartmentJsList = {};
sysDepartmentJsList.oTable = null;

sysDepartmentJsList.oTableFnServerParams = function(aoData){
    getTableParameters(sysDepartmentJsList.oTable, aoData);

};

sysDepartmentJsList.oTableAoColumns = [
    {mData: 'code', sTitle: '部门编号', bSortable: false},
    {mData: 'name', sTitle: '部门名称', bSortable: false},
    {mData: 'fullName', sTitle: '组织全称', bSortable: false},
    {mData: 'deptDesc', sTitle: '部门描述', bSortable: false},
    {mData: 'leaderId', sTitle: 'leaderId', bSortable: false},
    {mData: 'chargeLeaderId', sTitle: 'chargeLeaderId', bSortable: false},
    {mData: 'leaderId2', sTitle: 'leaderId2', bSortable: false},
    {mData: 'parentId', sTitle: 'parentId', bSortable: false},
    {mData: 'managerDept', sTitle: '上级主管部门id', bSortable: false},
    {mData: 'organizationId', sTitle: 'organizationId', bSortable: false},
    {mData: 'deptType', sTitle: '标记是部门or机构节点
            ’0‘--部门
            ’1‘--机构', bSortable: false},
    {mData: 'queue', sTitle: '部门排序', bSortable: false},
    {mData: 'shortName', sTitle: '租户简称', bSortable: false},
    {mData: 'userName', sTitle: '租户用户名', bSortable: false},
    {mData: 'password', sTitle: '密码', bSortable: false},
    {mData: 'type', sTitle: '0-试用 1-正式', bSortable: false},
    {mData: 'status', sTitle: '组织状态
            ’0‘--启用 
            ’1‘--禁用 
            ’2’--锁定 
            ‘3’--删除', bSortable: false},
    {mData: 'openDay', sTitle: '启用时间', bSortable: false},
    {mData: 'endDay', sTitle: '到期时间', bSortable: false},
    {mData: 'fileSpace', sTitle: '文件空间(m)', bSortable: false},
    {mData: 'usedSpace', sTitle: '已用空间', bSortable: false},
    {mData: 'balance', sTitle: '账户余额', bSortable: false},
    {mData: 'smsBalance', sTitle: '短信费用', bSortable: false},
    {mData: 'logo', sTitle: '租户LOGO', bSortable: false},
    {mData: 'cont', sTitle: '租户联系人', bSortable: false},
    {mData: 'telp', sTitle: '联系人电话', bSortable: false},
    {mData: 'email', sTitle: '用户邮箱', bSortable: false},
    {mData: 'memo', sTitle: '租户备注', bSortable: false},
    {mData: 'weight', sTitle: 'weight', bSortable: false},
    {mData: 'secret', sTitle: 'secret', bSortable: false},
    {mData: 'resourceId', sTitle: '资源库id', bSortable: false},
	{mData: function(source) {
		var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"sysDepartmentJsList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalParamEditText + "</a>";
		var del = "<a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"sysDepartmentJsList.delete('"+ source.id+ "')\">" + finalParamDeleteText + "</a>";
		return edit + del;
	}, sTitle: '操作', bSortable: false, sWidth: 170}
];

sysDepartmentJsList.renderTable = function () {
    if (sysDepartmentJsList.oTable == null) {
        sysDepartmentJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": sysDepartmentJsList.pageCount,
            "sAjaxSource": getRootPath() + "/doc/dept/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": sysDepartmentJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                sysDepartmentJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        sysDepartmentJsList.oTable.fnDraw();
    }
};

sysDepartmentJsList.delete = function (id) {
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
            sysDepartmentJsList.deleteCallBack(ids);
        }
    });
};

sysDepartmentJsList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/doc/dept/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            sysDepartmentJsList.renderTable();
        }
    });
};

sysDepartmentJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

sysDepartmentJsList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/doc/dept/loadForm.action", null, function() {
        sysDepartmentJsForm.init({title: '新增', operator: 'add'});
    });
};

sysDepartmentJsList.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/doc/dept/loadForm.action", null, function() {
        sysDepartmentJsForm.init({title: '修改', operator: 'modify', id: id});
    });
};

$(document).ready(function(){
    sysDepartmentJsList.pageCount = TabNub > 0 ? TabNub : 10;
    sysDepartmentJsList.renderTable();
    $('#queryBtn').click(sysDepartmentJsList.renderTable);
    $('#resetBtn').click(sysDepartmentJsList.queryReset);
    $("#addBtn").click(sysDepartmentJsList.loadModuleForAdd);
});