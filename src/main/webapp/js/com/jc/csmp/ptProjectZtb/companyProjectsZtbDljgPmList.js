var companyProjectsZtbDljgPmList = {};
companyProjectsZtbDljgPmList.oTable = null;


companyProjectsZtbDljgPmList.oTableFnServerParams = function(aoData){
    getTableParameters(companyProjectsZtbDljgPmList.oTable, aoData);
    //组装查询条件
    // var biddingDljgmc = $('#biddingDljgmc').val();
    // if (biddingDljgmc.length > 0) {
    //     aoData.push({'name': 'biddingDljgmc', 'value': biddingDljgmc});
    // }
};

companyProjectsZtbDljgPmList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'biddingDljgmc', sTitle: '招标代理机构名称', bSortable: false},
    {mData: 'kcCc', sTitle: '勘察中标通知书', bSortable: false},
    {mData: 'sjCc', sTitle: '设计中标通知书', bSortable: false},
    {mData: 'sgCc', sTitle: '施工中标通知书', bSortable: false},
    {mData: 'jlCc', sTitle: '监理中标通知书', bSortable: false},
    {mData: 'kcsjCc', sTitle: '勘察设计中标通知书', bSortable: false},
    {mData: 'hwclCc', sTitle: '货物材料中标通知书', bSortable: false},
    {mData: 'total', sTitle: '总中标金额（万元）', bSortable: false}

];

companyProjectsZtbDljgPmList.renderTable = function () {
    if (companyProjectsZtbDljgPmList.oTable == null) {
        companyProjectsZtbDljgPmList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": companyProjectsZtbDljgPmList.pageCount,
            "sAjaxSource": getRootPath() + "/csmp/ptProjectZtb/manageDljgPmList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": companyProjectsZtbDljgPmList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                companyProjectsZtbDljgPmList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        companyProjectsZtbDljgPmList.oTable.fnDraw();
    }
};
companyProjectsZtbDljgPmList.excel = function(){
    var modelTable = $('#gridTable').DataTable();
    var info = modelTable.page.info();
    var dataRows = info.recordsTotal;

    if(dataRows>0){
        window.location.href = getRootPath() + "/csmp/ptProjectZtb/excelDljgPm.action";
    }else{
        msgBox.tip({ type:"fail", content: "暂无需要导出的数据" });
    }

};
companyProjectsZtbDljgPmList.queryReset = function(){
    $('#searchForm')[0].reset();
};

$(document).ready(function(){
    companyProjectsZtbDljgPmList.pageCount = TabNub > 0 ? TabNub : 10;
    companyProjectsZtbDljgPmList.renderTable();
    $('#excel').click(companyProjectsZtbDljgPmList.excel);
    $('#queryBtn').click(companyProjectsZtbDljgPmList.renderTable);
    $('#resetBtn').click(companyProjectsZtbDljgPmList.queryReset);
});