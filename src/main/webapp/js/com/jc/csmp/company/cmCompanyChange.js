var cmCompanyChangePanel = {};
cmCompanyChangePanel.oTable = null;

cmCompanyChangePanel.callback = null;

cmCompanyChangePanel.init = function(callback) {
    cmCompanyChangePanel.pageCount = TabNub > 0 ? TabNub : 10;
    cmCompanyChangePanel.renderTable();
    $('#queryBtn').click(cmCompanyChangePanel.renderTable);
    $('#resetBtn').click(cmCompanyChangePanel.queryReset);
    $('#closeBtn').click(function () { $('#form-modal').modal("hide"); });
    cmCompanyChangePanel.callback = callback;
    $('#form-modal').modal("show");
};

cmCompanyChangePanel.again = function() {
    $('#form-modal').modal("show");
};

cmCompanyChangePanel.oTableFnServerParams = function(aoData){
    getTableParameters(cmCompanyChangePanel.oTable, aoData);
    var companyName = $('#searchForm #companyName').val();
    if (companyName.length > 0) { aoData.push({"name": "companyName", "value": companyName}); }
    var creditCode = $('#searchForm #creditCode').val();
    if (creditCode.length > 0) { aoData.push({"name": "creditCode", "value": creditCode}); }
    var companyType = $('#searchForm #companyType').val();
    if (companyType.length > 0) { aoData.push({"name": "companyTypeLike", "value": companyType}); }
};

cmCompanyChangePanel.oTableAoColumns = [
    {mData: 'companyName', sTitle: '单位名称', bSortable: false},
    {mData: 'creditCode', sTitle: '统一社会信用代码', bSortable: false},
    {mData: 'companyType', sTitle: '企业类型', bSortable: false, mRender : function(mData, type, full) { return full.companyTypeValue; }},
    {mData: function(source) {
        //return "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"cmCompanyChangePanel.change('"+ source.id+ "', '" + source.companyName + "')\" role=\"button\">&nbsp;选择&nbsp;</a>";
        return "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"cmCompanyChangePanel.change(" + JSON.stringify(source).replace(/"/g, '&quot;') +  ")\" role=\"button\">&nbsp;选择&nbsp;</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 80}
];

cmCompanyChangePanel.renderTable = function () {
    if (cmCompanyChangePanel.oTable == null) {
        cmCompanyChangePanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": cmCompanyChangePanel.pageCount,
            "sAjaxSource": getRootPath() + "/company/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmCompanyChangePanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmCompanyChangePanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmCompanyChangePanel.oTable.fnDraw();
    }
};

cmCompanyChangePanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

/*
cmCompanyChangePanel.change = function (id, companyName) {
    cmCompanyChangePanel.callback({id: id, text: companyName});
    $('#form-modal').modal("hide");
};*/

cmCompanyChangePanel.change = function (source) {
    cmCompanyChangePanel.callback({id: source.id, text: source.companyName});
    $('#form-modal').modal("hide");
};

