var projectHoistInfoJsList = {};
projectHoistInfoJsList.oTable = null;

projectHoistInfoJsList.initStatis = function () {
    var projectCode = $("#nowProjectCode").val();
    var url = getRootPath() + "/monit/realtime/queryList.action?projectCode=" + projectCode + "&equipmentType=building_hoist&n=" + new Date().getTime();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: url,
        success: function (data) {
            if (data&&data.length>0) {
                $('#HoistNormalNum').html(data[0].normarlNum);
                $('#HoistWarnNum').html(data[0].warnNum);
                $('#HoistOutLineNum').html(data[0].outLineNum);
            }
        }
    });
};

projectHoistInfoJsList.oTableFnServerParams = function(aoData){
    getTableParameters(projectCraneInfoJsList.oTable, aoData);
    var nowProjectCode  = $('#nowProjectCode').val();
    aoData.push({"name": "targetProjectCode", "value": nowProjectCode});
    aoData.push({"name": "targetType", "value": 'building_hoist'});
};


projectHoistInfoJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
    {mData: 'targetProjectName', sTitle: '项目', bSortable: false},
    {mData: 'targetCode', sTitle: '升降机设备编码', bSortable: false},
    {mData: 'warnReason', sTitle: '报警', bSortable: false},
    {mData: 'warnTime', sTitle: '报警发生时间', bSortable: false},
    { mData: function (source) {
            if (source.warnStatus == 'processed') {
                return "是"
            }
            return "否"
        }, sTitle: '处置状态', bSortable: false},
    {mData: function(source) {
            var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"projectHoistInfoJsList.loadModuleView('"+ source.id+ "')\" role=\"button\">查看</a>";
            return edit;
        }, sTitle: '操作', bSortable: false, sWidth: 70}
];

projectHoistInfoJsList.renderTable = function () {
    if (projectHoistInfoJsList.oTable == null) {
        projectHoistInfoJsList.oTable =  $('#gridHoistTable').dataTable( {
            "iDisplayLength": projectHoistInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/warn/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectHoistInfoJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectHoistInfoJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectHoistInfoJsList.oTable.fnDraw();
    }
};


projectHoistInfoJsList.loadModuleView = function (id){
    $("#formDialogDisDiv").load(getRootPath() + "/warn/info/loadView.action?id=" + id + "&type=building_hoist&n_=" + (new Date().getTime()), null, function () {

    });
};

$(document).ready(function(){
    projectHoistInfoJsList.pageCount = 5;
    projectHoistInfoJsList.renderTable();
    projectHoistInfoJsList.initStatis();
});