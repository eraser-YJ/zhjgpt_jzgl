var projectPlanDoneList = {};
projectPlanDoneList.pageRows = null;
//重复提交标识
projectPlanDoneList.subState = false;

//分页处理 start
//分页对象
projectPlanDoneList.oTable = null;
//显示列信息

projectPlanDoneList.oTableAoColumns = [
    {'mData': 'tableRowNo'},
    {'mData': 'planYear'},
    {'mData': 'planAreaName'},
    {
        "mData": function (source) {
            if (source.canChange == 'NEW') {
                return "待调整";
            }
            return source.planStatusValue;
        }
    },
    {
        "mData": function (source) {
            if (source.canChange == 'NEW') {
                return "";
            }
            return source.createDate;
        }
    },
    {
        "mData": function (source) {
            return source.chanageDateBegin+" 至 "+ source.chanageDateEnd;
        }
    },
    //设置权限按钮
    {
        "mData": function (source) {
            if (source.canChange == 'NEW') {
                var edit = "<a class=\"a-icon i-new\"  href=\"#myModal-edit\"  onclick=\"projectPlanDoneList.loadModuleForNew('" + source.planYear + "','" + source.planAreaCode + "','" + source.planAreaName + "')\" role=\"button\">调整</a>";
                return edit;
            } else {
                if (source.planStatus > 1) {
                    var buttonStr = "<a class=\"a-icon i-new\"  href=\"#myModal-edit\"  onclick=\"projectPlanDoneList.loadView('" + source.id + "')\" role=\"button\">查看</a>";
                    return buttonStr;
                } else if (source.canChange == 'Y') {
                    var edit = "<a class=\"a-icon i-new\"  href=\"#myModal-edit\"  onclick=\"projectPlanDoneList.loadModuleForUpdate('" + source.id + "')\" role=\"button\">编辑</a>";
                    var del = "<a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"projectPlanDoneList.delete('" + source.id + "')\">删除</a>";
                    return edit + del;
                } else {
                    var buttonStr = "<a class=\"a-icon i-new\"  href=\"#myModal-edit\"  onclick=\"projectPlanDoneList.loadView('" + source.id + "')\" role=\"button\">查看</a>";
                    return buttonStr;
                }

            }
        }
    }
];
projectPlanDoneList.loadView = function (id) {
    var url = getRootPath() + "/plan/projectYearPlan/loadView.action?id=" + id;
    window.location.href = url;
}
projectPlanDoneList.view = function (url) {
    JCFF.loadPage({url: url});
}
projectPlanDoneList.openForm = function (url) {
    JCFF.loadPage({url: url});
}
projectPlanDoneList.loadModuleForNew = function (selectYear, areaCode, areaName) {
    var planSeqno = $("#query_planSeqno").val();
    var condition = "0," + selectYear + "," + areaCode + "," + areaName + "," + planSeqno;
    var chheckUrl = getRootPath() + "/plan/projectYearPlan/checkRepetition.action?selectYear=" + selectYear + "&selectAreaCode=" + areaCode + "&planSeqno=" + planSeqno + "&n_=" + (new Date().getTime());
    jQuery.ajax({
        url: encodeURI(chheckUrl),
        async: false,
        type: 'GET',
        success: function (data) {
            if (data.code == "0") {
                var newUrl = getRootPath() + "/instance/toStartProcess.action?definitionKey_=yearplan001&condition=" + condition;
                window.location.href = encodeURI(newUrl);
            } else {
                msgBox.info({
                    type: "fail",
                    content: data.msg
                });
            }
        }
    });
}

projectPlanDoneList.loadModuleForUpdate = function (id) {
    window.location.href = getRootPath() + "/instance/toStartProcess.action?definitionKey_=yearplan001&condition=" + id;
}
projectPlanDoneList.delete = function (id) {
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
            projectPlanDoneList.deleteCallBack(ids);
        }
    });
}

projectPlanDoneList.deleteCallBack = function (ids) {
    $.ajax({
        type: "POST",
        url: getRootPath() + "/plan/projectYearPlan/deleteByIds.action",
        data: {"ids": ids},
        dataType: "json",
        success: function (data) {
            if (data.success == "true") {
                msgBox.tip({type: "success", content: data.successMessage});
            } else {
                msgBox.info({content: data.errorMessage});
            }
            projectPlanDoneList.workflowDoneList();
        }
    });
};
projectPlanDoneList.openForm = function (url) {
    JCFF.loadPage({url: url});
}

//组装后台参数
projectPlanDoneList.oTableFnServerParams = function (aoData) {
    //排序条件
    getTableParameters(projectPlanDoneList.oTable, aoData);
    var query_planSeqno = $("#query_planSeqno").val();
    if (query_planSeqno.length > 0) {
        aoData.push({'name': 'planSeqno', 'value': query_planSeqno});
    }
    //组装查询条件
    var query_planYear = $('#query_planYear').val();
    if (query_planYear.length > 0) {
        aoData.push({'name': 'planYear', 'value': query_planYear});
    }
    var query_planAreaCode = $('#query_planAreaCode').val();
    if (query_planAreaCode.length > 0) {
        aoData.push({'name': 'planAreaCode', 'value': query_planAreaCode});
    }
};

projectPlanDoneList.workflowDoneList = function () {

    $.ajaxSetup({
        cache: false //设置成false将不会从浏览器缓存读取信息
    });
    $('#sendPassTransact-list').fadeIn();
    if (projectPlanDoneList.oTable == null) {
        var pagingInfo = pagingDataForGoBack();
        var queryData = pagingInfo.queryData;
        if (queryData != null) {
            //需要针对自己的查询搜索框进行填入值
            //如果有人员选择框的特殊内容需要特殊实现
            $("#projectPlanForm").fill(queryData);
        }
        projectPlanDoneList.oTable = $('#projectPlanTable').dataTable({
            "iDisplayLength": projectPlanDoneList.pageRows,//每页显示多少条记录
            "iDisplayStart": pagingInfo.curPage * projectPlanDoneList.pageRows,
            "sAjaxSource": getRootPath() + "/plan/projectYearPlan/manageList2.action",
            "fnServerData": oTableRetrieveData,//查询数据回调函数
            "aoColumns": projectPlanDoneList.oTableAoColumns,//table显示列
            aaSorting: [],//设置表格默认排序列
            //传参
            "fnServerParams": function (aoData) {
                projectPlanDoneList.oTableFnServerParams(aoData);
            },
            //默认不排序列
            "aoColumnDefs": [{"bSortable": false, "aTargets": [0, 1, 2, 3, 4, 5, 6]}]
        });
    } else {
        projectPlanDoneList.oTable.fnDraw();
    }
};

projectPlanDoneList.queryReset = function () {
    $('#projectPlanQueryForm')[0].reset();
};

jQuery(function ($) {
    //计算分页显示条数
    projectPlanDoneList.pageRows = TabNub > 0 ? TabNub : 10;
    $("#queryProjectPlan").click(projectPlanDoneList.workflowDoneList);
    $("#queryReset").click(projectPlanDoneList.queryReset);

    //日历控件重新初始化
    $(".datepicker-input").datepicker();
    projectPlanDoneList.workflowDoneList();
});

