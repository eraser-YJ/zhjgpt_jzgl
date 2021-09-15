var projectMonthPlanJsList = {};
projectMonthPlanJsList.oTable = null;


projectMonthPlanJsList.oTableFnServerParams = function (aoData) {
    getTableParameters(projectMonthPlanJsList.oTable, aoData);
    var planYearCondObj = $('#searchForm #query_planYear').val();
    if (planYearCondObj && planYearCondObj.length > 0) {
        aoData.push({"name": "planYear", "value": planYearCondObj});
    }
    var query_planMonth = $('#searchForm #query_planMonth').val();
    if (query_planMonth && query_planMonth.length > 0) {
        aoData.push({"name": "planMonth", "value": query_planMonth});
    }
    var planAreaCodeCondObj = $('#searchForm #query_planAreaCode').val();
    if (planAreaCodeCondObj && planAreaCodeCondObj.length > 0) {
        aoData.push({"name": "planAreaCode", "value": planAreaCodeCondObj});
    }
};

projectMonthPlanJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
    {mData: 'planYear', sTitle: '年度', bSortable: false},
    {
        mData: function (source) {
            return source.planMonth + "月";
        }, sTitle: '月度', bSortable: false
    },
    {mData: 'planAreaName', sTitle: '地区', bSortable: false},
    {mData: 'ower', sTitle: '创建人', bSortable: false},
    {mData: 'createDate', sTitle: '创建时间', bSortable: false},
    {
        mData: function (source) {
            if (source.canChange == 'Y') {
                var edit = "<a class=\"a-icon i-new\"  href=\"#myModal-edit\"  onclick=\"projectMonthPlanJsList.loadModuleForUpdate('" + source.id + "')\" role=\"button\">编辑</a>";
                var del = "<a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"projectMonthPlanJsList.delete('" + source.id + "')\">删除</a>";
                var exportExcel = "<a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"projectMonthPlanJsList.loadModuleForExport('" + source.id + "')\"> 导出 </a>";
                return edit + del + exportExcel;
            } else {
                var edit = "<a class=\"a-icon i-new\"  href=\"#myModal-edit\"  onclick=\"projectMonthPlanJsList.loadModuleForView('" + source.id + "')\" role=\"button\">查看</a>";
                var exportExcel = "<a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"projectMonthPlanJsList.loadModuleForExport('" + source.id + "')\">导出</a>";
                return edit + exportExcel;
            }

        }, sTitle: '操作', bSortable: false, sWidth: 170
    }
];

projectMonthPlanJsList.renderTable = function () {
    if (projectMonthPlanJsList.oTable == null) {
        projectMonthPlanJsList.oTable = $('#gridTable').dataTable({
            "iDisplayLength": projectMonthPlanJsList.pageCount,
            "sAjaxSource": getRootPath() + "/plan/projectMonthPlan/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectMonthPlanJsList.oTableAoColumns,
            "fnServerParams": function (aoData) {
                projectMonthPlanJsList.oTableFnServerParams(aoData);
            },
            aaSorting: [],
            aoColumnDefs: []
        });
    } else {
        projectMonthPlanJsList.oTable.fnDraw();
    }
};

projectMonthPlanJsList.delete = function (id) {
    var ids = String(id);
    if (id == 0) {
        var idsArr = [];
        $("[name='ids']:checked").each(function () {
            idsArr.push($(this).val());
        });
        ids = idsArr.join(",");
    }
    if (ids.length == 0) {
        msgBox.info({content: $.i18n.prop("JC_SYS_061")});
        return;
    }
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function () {
            projectMonthPlanJsList.deleteCallBack(ids);
        }
    });
};

projectMonthPlanJsList.deleteCallBack = function (ids) {
    $.ajax({
        type: "POST",
        url: getRootPath() + "/plan/projectMonthPlan/deleteByIds.action",
        data: {"ids": ids},
        dataType: "json",
        success: function (data) {
            if (data.success == "true") {
                msgBox.tip({type: "success", content: data.successMessage});
            } else {
                msgBox.info({content: data.errorMessage});
            }
            projectMonthPlanJsList.renderTable();
        }
    });
};

projectMonthPlanJsList.queryReset = function () {
    $('#searchForm')[0].reset();
};

projectMonthPlanJsList.loadModuleForNew = function () {
    window.location.href = getRootPath() + "/plan/projectMonthPlan/index.action";
}

projectMonthPlanJsList.loadModuleForUpdate = function (id) {
    window.location.href = getRootPath() + "/plan/projectMonthPlan/loadForm.action?id=" + id + "&pageMode=EDIT";
}

projectMonthPlanJsList.loadModuleForView = function (id) {
    window.location.href = getRootPath() + "/plan/projectMonthPlan/loadForm.action?id=" + id + "&pageMode=VIEW";
}

projectMonthPlanJsList.loadModuleForExport = function (id) {
    window.location.href = getRootPath() + "/plan/projectMonthPlan/export.action?id=" + id;
}
$(document).ready(function () {
    projectMonthPlanJsList.pageCount = TabNub > 0 ? TabNub : 10;
    projectMonthPlanJsList.renderTable();
    $('#queryBtn').click(projectMonthPlanJsList.renderTable);
    $('#resetBtn').click(projectMonthPlanJsList.queryReset);
    $("#addBtn").click(projectMonthPlanJsList.loadModuleForNew);
});