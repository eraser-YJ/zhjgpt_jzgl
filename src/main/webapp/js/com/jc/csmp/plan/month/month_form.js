//初始化方法
var projectPlanMonthModule = {};


//重复提交标识
projectPlanMonthModule.subState = false;
projectPlanMonthModule.itemIndex = 0;


//根据TR的ID取得行的itemid
projectPlanMonthModule.getIdOnTr = function (jqueryObj) {
    var idStr = jqueryObj.attr("id");
    var ids = idStr.split("_")
    return ids[ids.length - 1];
}


//编辑
projectPlanMonthModule.eidtItemBtnFun = function (selectId) {
    $("#itemshowdiv").load(getRootPath() + "/plan/projectMonthPlan/showItem.action", null, function () {
        projectMonthPlanItemJsForm.showEdit(tableMonthAssFun.getRowData(selectId), projectPlanMonthModule.eidtItemBtnFunCallback);
    });
}
//编辑的回调
projectPlanMonthModule.eidtItemBtnFunCallback = function (rowData) {
    //赋值
    tableMonthAssFun.fillRowData(rowData)
    //整个页面重新计算，排号
    projectPlanMonthModule.sequenceRebuild(rowData.projectType);
    $("#itemChange").val("Y");
}


//序号重排
projectPlanMonthModule.sequenceRebuild = function (code) {
    if (code) {
        var index = 1;
        $("[id^='tr_" + code + "_']").each(function (itemIndex, itemObject) {
            var id = projectPlanMonthModule.getIdOnTr($(itemObject));
            $("#sequence_" + id).html((index++))
        })
    } else {
        $("[leaf='Y']").each(function (leafIndex, leafbject) {
            var index = 1;
            var nowCode = $(leafbject).attr("leafcode");
            $("[id^='tr_" + nowCode + "_']").each(function (itemIndex, itemObject) {
                var id = projectPlanMonthModule.getIdOnTr($(itemObject));
                $("#sequence_" + id).html((index++))
            })
        })
    }

}


//页面赋值
projectPlanMonthModule.initItemData = function (itemList) {
    if (itemList) {
        var rowData;
        for (var iIndex = 0; iIndex < itemList.length; iIndex++) {
            rowData = itemList[iIndex];
            tableMonthAssFun.addItemToTypeLast(rowData);
        }
        //所有页计算
        projectPlanMonthModule.sequenceRebuild();
    }
}

//初始化
projectPlanMonthModule.initData = function () {
    var id = $("#id").val();
    var url = getRootPath() + "/plan/projectMonthPlan/startFormData.action";
    if (id && id != '') {
        url = getRootPath() + "/plan/projectMonthPlan/loadFormData.action?id=" + id + "&n_" + new Date().getTime();
    }
    jQuery.ajax({
        url: url,
        async: false,
        type: 'POST',
        success: function (data) {
            //页面初始化
            var pageMode = $("#pageMode").val();
            if (pageMode == 'VIEW') {
                tableMonthAssFun.initData(data, false);
            } else {
                tableMonthAssFun.initData(data, true);
            }
            //项目列表初始化
            projectPlanMonthModule.initItemData(data.itemList);
            //主表信息出生化
            if (data.head) {
                $("#id").val(data.head.id);
                $("#modifyDate").val(data.head.modifyDate);
                $("#planYear").val(data.head.planYear);
                $("#planMonth").val(data.head.planMonth);
                $("#planAreaCode").val(data.head.planAreaCode);
                $("#planAreaName").val(data.head.planAreaName);
                var title = "长春新区"+data.head.planYear+"年"+ data.head.planMonth+"月基础设施及功能类项目建设计划表——"+data.head.planAreaName;
                $("#planName").val(title);
                $("#planNameTitle1").html(title);
                $("#itemChange").val("N");
            }
        }
    });
}
projectPlanMonthModule.subState = false;
projectPlanMonthModule.saveData = function () {
    if (projectPlanMonthModule.subState) {
        return;
    }
    var isEdit = false;
    if ($("#projectPlanForm #modifyDate").val() != '') {
        isEdit = true;
    }
    $("#planType").val(JSON.stringify(tableMonthAssFun.dicInfo));
    //有编码再传明显数据，减少数据量传输
    if ("Y" == $("#itemChange").val()) {
        $("#itemListContent").val(JSON.stringify(tableMonthAssFun.getAllData()));
    } else if (!isEdit) {
        $("#itemChange").val("Y")
        $("#itemListContent").val(JSON.stringify(tableMonthAssFun.getAllData()));
    }
    var url = getRootPath() + "/plan/projectMonthPlan/" + (isEdit ? 'update' : 'save') + '.action';
    jQuery.ajax({
        url: url,
        type: 'POST',
        cache: false,
        data: $("#projectPlanForm").serializeArray(),
        success: function (data) {
            projectPlanMonthModule.subState = false;
            if (data.success == "true") {
                var url = getRootPath() + "/plan/projectMonthPlan/manage.action";
                window.location.href = url;
            } else {
                msgBox.info({type: "fail", content: data.errorMessage});
            }
        }
    });
}
$(document).ready(function () {
    ie8StylePatch();

    $(".datepicker-input").each(function () {
        $(this).datepicker();
    });
    $("#addMonthBtn").click(function () {
        projectPlanMonthModule.saveData(true);
    });
    $("#closeMonthBtn").click(function () {
        var url = getRootPath() + "/plan/projectMonthPlan/manage.action";
        window.location.href = url;
    });
    //初始化
    projectPlanMonthModule.initData();

    var tiHeight = $(window).height();
    $("#pageDisplayDiv").height(tiHeight - 60);
    $("#pageDisplayTable").css("width", "100%");

});
