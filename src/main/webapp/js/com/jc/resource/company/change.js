var resourceCompanyChangePanel = {};
resourceCompanyChangePanel.oTable = null;
resourceCompanyChangePanel.callback = null;

resourceCompanyChangePanel.init = function(config) {
    resourceCompanyChangePanel.pageCount = TabNub > 0 ? TabNub : 10;
    resourceCompanyChangePanel.renderTable();
    resourceCompanyChangePanel.callback = config.callback;
    $('#company-change-form-modal').modal("show");
};

resourceCompanyChangePanel.again = function() {
    $('#company-change-form-modal').modal("show");
};

resourceCompanyChangePanel.oTableFnServerParams = function(aoData){
    getTableParameters(resourceCompanyChangePanel.oTable, aoData);
    var companyName = $('#companyChangeSearchForm #companyName').val();
    if (companyName.length > 0) { aoData.push({"name": "companyName", "value": companyName}); }
};

resourceCompanyChangePanel.oTableAoColumns = [
    {mData: 'company_name', sTitle: '单位名称', bSortable: false},
    {mData: 'company_zznum', sTitle: '营业执照号', bSortable: false},
    {mData: 'company_zzdm', sTitle: '组织机构代码', bSortable: false},
    {mData: function(source) {
        return "<a class='a-icon i-new' href='javascript:void(0);' onclick=\"resourceCompanyChangePanel.change(" + JSON.stringify(source).replace(/"/g, '&quot;') + ")\" role='button'>" + finalTableBtnText('选择', 'fa-doc-exchange') + "</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 60}
];

resourceCompanyChangePanel.renderTable = function () {
    if (resourceCompanyChangePanel.oTable == null) {
        resourceCompanyChangePanel.oTable = $('#companyChangeGridTable').dataTable( {
            "iDisplayLength": 10,
            "sAjaxSource": getRootPath() + "/resource/system/companyList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": resourceCompanyChangePanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                resourceCompanyChangePanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        resourceCompanyChangePanel.oTable.fnDraw();
    }
};

resourceCompanyChangePanel.queryReset = function(){
    $('#companyChangeSearchForm')[0].reset();
};

resourceCompanyChangePanel.change = function (source) {
    resourceCompanyChangePanel.callback({id: source.id, text: source.company_name});
    $('#company-change-form-modal').modal("hide");
};