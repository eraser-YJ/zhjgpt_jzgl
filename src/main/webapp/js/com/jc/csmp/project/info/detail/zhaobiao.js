var zhaobiaoModule = {};
zhaobiaoModule.oTable = null;

zhaobiaoModule.oTableFnServerParams = function(aoData){
    getTableParameters(zhaobiaoModule.oTable, aoData);
    alert(getUrlParam("projectNumber"));
    aoData.push({name: "projectNumber", value: getUrlParam("projectNumber")});
};

zhaobiaoModule.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: function(source) {
	    return "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"zhaobiaoModule.loadModuleForLook()\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 150}
];

zhaobiaoModule.renderTable = function () {
    if (zhaobiaoModule.oTable == null) {
        zhaobiaoModule.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": zhaobiaoModule.pageCount,
            "sAjaxSource": getRootPath() + "/project/change/searchList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": zhaobiaoModule.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                zhaobiaoModule.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        zhaobiaoModule.oTable.fnDraw();
    }
};

zhaobiaoModule.loadModuleForLook = function (id){
    $("#formModule").load("", null, function() {});
};

zhaobiaoModule.queryReset = function(){
    $('#searchForm')[0].reset();
};

$(document).ready(function(){
    zhaobiaoModule.pageCount = TabNub > 0 ? TabNub : 10;
    zhaobiaoModule.renderTable();
    $('#queryBtn').click(zhaobiaoModule.renderTable);
    $('#resetBtn').click(zhaobiaoModule.queryReset);
});