var projectCraneInfoJsList = {};
projectCraneInfoJsList.oTable = null;

projectCraneInfoJsList.initStatis = function () {
    var projectCode = $("#nowProjectCode").val();
    var url = getRootPath() + "/monit/realtime/queryList.action?projectCode=" + projectCode + "&equipmentType=tower_crane&n=" + new Date().getTime();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: url,
        success: function (data) {
            if (data) {
                if (data&&data.length>0) {
                    $('#CraneNormalNum').html(data[0].normarlNum);
                    $('#CraneWarnNum').html(data[0].warnNum);
                    $('#CraneOutLineNum').html(data[0].outLineNum);
                }
            }
        }
    });
};

projectCraneInfoJsList.oTableFnServerParams = function (aoData) {
    getTableParameters(projectCraneInfoJsList.oTable, aoData);
    var nowProjectCode = $('#nowProjectCode').val();
    aoData.push({"name": "targetProjectCode", "value": nowProjectCode});
    aoData.push({"name": "targetType", "value": 'tower_crane'});

};


projectCraneInfoJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
    {mData: 'targetProjectName', sTitle: '项目', bSortable: false},
    {mData: 'targetCode', sTitle: '塔吊设备编码', bSortable: false},
    {mData: 'warnReason', sTitle: '报警', bSortable: false},
    {mData: 'warnTime', sTitle: '报警发生时间', bSortable: false},
    {
        mData: function (source) {
            if (source.warnStatus == 'processed') {
                return "是"
            }
            return "否"
        }, sTitle: '处置状态', bSortable: false
    },
    {
        mData: function (source) {
            var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"projectCraneInfoJsList.loadModuleView('" + source.id + "')\" role=\"button\">查看</a>";
            return edit;
        }, sTitle: '操作', bSortable: false, sWidth: 70
    }
];

projectCraneInfoJsList.renderTable = function () {
    if (projectCraneInfoJsList.oTable == null) {
        projectCraneInfoJsList.oTable = $('#gridCraneTable').dataTable({
            "iDisplayLength": projectCraneInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/warn/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectCraneInfoJsList.oTableAoColumns,
            "fnServerParams": function (aoData) {
                projectCraneInfoJsList.oTableFnServerParams(aoData);
            },
            aaSorting: [],
            aoColumnDefs: []
        });
    } else {
        projectCraneInfoJsList.oTable.fnDraw();
    }
};


projectCraneInfoJsList.loadModuleView = function (id) {
    $("#formDialogDisDiv").load(getRootPath() + "/warn/info/loadView.action?id=" + id + "&type=tower_crane&n_=" + (new Date().getTime()), null, function () {

    });
};

$(document).ready(function () {
    projectCraneInfoJsList.pageCount = 5;
    projectCraneInfoJsList.renderTable();
    projectCraneInfoJsList.initStatis();
});