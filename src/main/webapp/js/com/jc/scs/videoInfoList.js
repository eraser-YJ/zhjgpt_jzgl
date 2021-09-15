var projectVideoInfoJsList = {};
projectVideoInfoJsList.oTable = null;

projectVideoInfoJsList.initStatis = function () {
    var projectCode = $("#nowProjectCode").val();
    var url = getRootPath() + "/monit/realtime/queryList.action?projectCode=" + projectCode + "&equipmentType=monitors&n=" + new Date().getTime();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: url,
        success: function (data) {
            if (data&&data.length>0) {
                $('#VideoNormalNum').html(data[0].normarlNum);
                $('#VideoWarnNum').html(data[0].warnNum);
                $('#VideoOutLineNum').html(data[0].outLineNum);
            }
        }
    });
};


projectVideoInfoJsList.oTableFnServerParams = function(aoData){
    getTableParameters(projectCraneInfoJsList.oTable, aoData);
    var nowProjectCode  = $('#nowProjectCode').val();
    aoData.push({"name": "targetProjectCode", "value": nowProjectCode});
    aoData.push({"name": "targetType", "value": 'monitors'});
};


projectVideoInfoJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
    {mData: 'targetProjectName', sTitle: '项目', bSortable: false},
    {mData: 'targetCode', sTitle: '视频设备编码', bSortable: false},
    {mData: 'warnReason', sTitle: '报警', bSortable: false},
    {mData: 'warnTime', sTitle: '报警发生时间', bSortable: false},
    { mData: function (source) {
            if (source.warnStatus == 'processed') {
                return "是"
            }
            return "否"
        }, sTitle: '处置状态', bSortable: false},
    {mData: function(source) {
            var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"projectVideoInfoJsList.loadModuleView('"+ source.id+ "')\" role=\"button\">查看</a>";
            return edit;
        }, sTitle: '操作', bSortable: false, sWidth: 70}
];

projectVideoInfoJsList.renderTable = function () {
    if (projectVideoInfoJsList.oTable == null) {
        projectVideoInfoJsList.oTable =  $('#gridVideoTable').dataTable( {
            "iDisplayLength": projectVideoInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/warn/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectVideoInfoJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectVideoInfoJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectVideoInfoJsList.oTable.fnDraw();
    }
};


projectVideoInfoJsList.loadModuleView = function (id){
    $("#formDialogDisDiv").load(getRootPath() + "/warn/info/loadView.action?id=" + id + "&type=monitors&n_=" + (new Date().getTime()), null, function () {
    });
};

$(document).ready(function(){
    projectVideoInfoJsList.pageCount = 5;
    projectVideoInfoJsList.renderTable();
    projectVideoInfoJsList.initStatis();
});