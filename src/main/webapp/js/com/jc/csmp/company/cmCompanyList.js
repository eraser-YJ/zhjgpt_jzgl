var cmCompanyListPanel = {};
cmCompanyListPanel.oTable = null;

cmCompanyListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(cmCompanyListPanel.oTable, aoData);
    var companyName = $('#searchForm #companyName').val();
    if (companyName.length > 0) { aoData.push({"name": "companyName", "value": companyName}); }
    var creditCode = $('#searchForm #creditCode').val();
    if (creditCode.length > 0) { aoData.push({"name": "creditCode", "value": creditCode}); }
    var companyType = $('#searchForm #companyType').val();
    if (companyType.length > 0) { aoData.push({"name": "companyTypeLike", "value": companyType}); }
};

cmCompanyListPanel.oTableAoColumns = [
    {mData: 'companyName', sTitle: '单位名称', bSortable: false},
    {mData: 'creditCode', sTitle: '统一社会信用代码', bSortable: false},
    {mData: 'legalPerson', sTitle: '法定代表人', bSortable: false},
    {mData: 'legalPhone', sTitle: '法人联系方式', bSortable: false},
    {mData: 'liaisonMan', sTitle: '项目联系人', bSortable: false},
    {mData: 'liaisonPhone', sTitle: '联系人电话', bSortable: false},
    {mData: 'companyType', sTitle: '企业类型', bSortable: false, mRender : function(mData, type, full) { return full.companyTypeValue; }},
    {mData: function(source) {
        var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"cmCompanyListPanel.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalParamEditText + "</a>";
        var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"cmCompanyListPanel.delete('"+ source.id+ "')\">" + finalParamDeleteText + "</a>";
        return edit + del;
    }, sTitle: '操作', bSortable: false, sWidth: 170}
];

cmCompanyListPanel.renderTable = function () {
    if (cmCompanyListPanel.oTable == null) {
        cmCompanyListPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": cmCompanyListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/company/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmCompanyListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmCompanyListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmCompanyListPanel.oTable.fnDraw();
    }
};

cmCompanyListPanel.delete = function (id) {
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
            cmCompanyListPanel.deleteCallBack(ids);
        }
    });
};

cmCompanyListPanel.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/company/info/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            cmCompanyListPanel.renderTable();
        }
    });
};

cmCompanyListPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

cmCompanyListPanel.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/company/info/loadForm.action", null, function() {
        cmCompanyFormPanel.init({title: '新增单位', operator: 'add'});
    });
};

cmCompanyListPanel.loadModuleForUpdate = function (id){
    $("#formModule").load(getRootPath() + "/company/info/loadForm.action", null, function() {
        cmCompanyFormPanel.init({title: '修改单位', operator: 'modify', id: id});
    });
};

$(document).ready(function(){
    cmCompanyListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    cmCompanyListPanel.renderTable();
    $('#queryBtn').click(cmCompanyListPanel.renderTable);
    $('#resetBtn').click(cmCompanyListPanel.queryReset);
    $("#addBtn").click(cmCompanyListPanel.loadModuleForAdd);
});