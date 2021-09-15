var companyProjectsZtbTspmList = {};
companyProjectsZtbTspmList.oTable = null;


companyProjectsZtbTspmList.oTableFnServerParams = function(aoData){
    getTableParameters(companyProjectsZtbTspmList.oTable, aoData);
    //组装查询条件
    var biddingDljgbm = $('#biddingDljgbm').val();
    if (biddingDljgbm.length > 0) {
        aoData.push({'name': 'biddingDljgbm', 'value': biddingDljgbm});
    }
    var biddingDljgmc = $('#biddingDljgmc').val();
    if (biddingDljgmc.length > 0) {
        aoData.push({'name': 'biddingDljgmc', 'value': biddingDljgmc});
    }
};

companyProjectsZtbTspmList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'biddingDljgbm', sTitle: '招标代理机构编码', bSortable: false},
    {mData: 'biddingDljgmc', sTitle: '招标代理机构名称', bSortable: false},
    {mData: 'biddingYysl', sTitle: '异议数量', bSortable: false},
    {mData: 'biddingTsss', sTitle: '投诉数量', bSortable: false},
    {mData: 'biddingRemark', sTitle: '备注', bSortable: false}

];

companyProjectsZtbTspmList.renderTable = function () {
    if (companyProjectsZtbTspmList.oTable == null) {
        companyProjectsZtbTspmList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": companyProjectsZtbTspmList.pageCount,
            "sAjaxSource": getRootPath() + "/csmp/ptProjectZtb/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": companyProjectsZtbTspmList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                companyProjectsZtbTspmList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        companyProjectsZtbTspmList.oTable.fnDraw();
    }
};
companyProjectsZtbTspmList.excel = function(){
    var modelTable = $('#gridTable').DataTable();
    var info = modelTable.page.info();
    var dataRows = info.recordsTotal;

    if(dataRows>0){
        window.location.href = getRootPath() + "/csmp/ptProjectZtb/excelTspm.action";
    }else{
        msgBox.tip({ type:"fail", content: "暂无需要导出的数据" });
    }

};
companyProjectsZtbTspmList.queryReset = function(){
    $('#searchForm')[0].reset();
};

$(document).ready(function(){
    companyProjectsZtbTspmList.pageCount = TabNub > 0 ? TabNub : 10;
    companyProjectsZtbTspmList.renderTable();
    $('#excel').click(companyProjectsZtbTspmList.excel);
    $('#queryBtn').click(companyProjectsZtbTspmList.renderTable);
    $('#resetBtn').click(companyProjectsZtbTspmList.queryReset);
});