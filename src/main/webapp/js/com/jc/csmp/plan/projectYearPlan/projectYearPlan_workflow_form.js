//初始化方法
var projectPlanModule = {};


//重复提交标识
projectPlanModule.subState = false;
projectPlanModule.itemIndex = 0;

function insert(type, jumpFun) {
    if (projectPlanModule.subState) return;
    projectPlanModule.subState = true;
    var url = getRootPath() + "/plan/projectYearPlan/saveWorkflow.action";
    var formData = $("#projectPlanForm").serializeArray();

    jQuery.ajax({
        url: url,
        type: 'POST',
        cache: false,
        data: formData,
        success: function (data) {
            projectPlanModule.subState = false;
            if (data.success == "true") {
                msgBox.tip({
                    type: "success",
                    content: data.successMessage,
                    callback: projectPlanModule.gotoTodo
                });
                $("#token").val(data.token);
            } else {
                if (data.labelErrorMessage) {
                    showErrors("projectPlanForm", data.labelErrorMessage);
                } else {
                    msgBox.info({
                        type: "fail",
                        content: data.errorMessage
                    });
                }
                $("#token").val(data.token);
            }
        },
        error: function () {
            projectPlanModule.subState = false;
            msgBox.tip({
                type: "fail",
                content: $.i18n.prop("JC_SYS_002")
            });
        }
    });
}

function update(type, jumpFun) {
    if (projectPlanModule.subState) return;
    projectPlanModule.subState = true;
    var url = getRootPath() + "/plan/projectYearPlan/updateWorkflow.action";
    var formData = $("#projectPlanForm").serializeArray();

    jQuery.ajax({
        url: url,
        async: false,
        type: 'POST',
        data: formData,
        success: function (data) {
            projectPlanModule.subState = false;
            if (data.success == "true") {
                msgBox.tip({
                    type: "success",
                    content: data.successMessage,
                    callback: projectPlanModule.gotoTodo
                });
                $("#token").val(data.token);
            } else {
                if (data.labelErrorMessage) {
                    showErrors("projectPlanForm", data.labelErrorMessage);
                } else {
                    msgBox.info({
                        type: "fail",
                        content: data.errorMessage
                    });
                }
            }
        },
        error: function () {
            projectPlanModule.subState = false;
            msgBox.tip({
                type: "fail",
                content: $.i18n.prop("JC_SYS_002")
            });
        }
    });
}

function validateForm() {
    $("#planType").val(JSON.stringify(tableAssFun.dicInfo));
    $("#remark").val($("#disSemark").val());
    //有编码再传明显数据，减少数据量传输
    if ("Y" == $("#itemChange").val()) {
        $("#itemListContent").val(JSON.stringify(tableAssFun.getAllData()));
    }
    return $("#projectPlanForm").valid();
}

//校验失败调用的方法
function validateFormFail() {
    projectPlanModule.subState = false;
    msgBox.info({
        content: $.i18n.prop("JC_SYS_067"),
    });
}

projectPlanModule.gotoTodo = function () {
    var pageMode = $("#pageMode").val();
    if (pageMode != "VIEW" && pageMode != "NEW") {
        JCFF.loadPage({url: "/plan/projectYearPlan/todoList.action"});
    } else {
        var planSeqno = $("#planSeqno").val();
        if (planSeqno == '1') {
            JCFF.loadPage({url: "/plan/projectYearPlan/manageReq.action"});
        } else if (planSeqno == '2') {
            JCFF.loadPage({url: "/plan/projectYearPlan/manageChange.action"});
        } else {
            JCFF.loadPage({url: "/plan/projectYearPlan/manageView.action"});
        }


    }
}
//根据TR的ID取得行的itemid
projectPlanModule.getIdOnTr = function (jqueryObj) {
    var idStr = jqueryObj.attr("id");
    var ids = idStr.split("_")
    return ids[ids.length - 1];
}


//新增
projectPlanModule.addItemBtnFun = function (dicCode, dicName) {
    $("#itemshowdiv").load(getRootPath() + "/plan/projectYearPlan/showItem.action", null, function () {
        projectYearPlanItemJsForm.showNew(dicCode, dicName, projectPlanModule.addItemBtnFunCallback);
    });
}
//新增的回调
projectPlanModule.addItemBtnFunCallback = function (rowData) {
    for (var ti = 0; ti < 1; ti++) {
        console.log(ti);
        rowData.itemId = xyGuid();
        tableAssFun.addItemToTypeLast(rowData);
        $("#itemChange").val("Y");
    }
    //整个页面重新计算，排号
    projectPlanModule.calcauteAllPage();
}
//回调
projectPlanModule.addItemToPage = function (rowDataList) {
    var rowData
    for (var ti = 0; ti < rowDataList.length; ti++) {
        rowData = rowDataList[ti];
        rowData.itemId = xyGuid();
        tableAssFun.addItemToTypeLast(rowData);
        $("#itemChange").val("Y");
    }
    //整个页面重新计算，排号
    projectPlanModule.calcauteAllPage();
}
//编辑
projectPlanModule.eidtItemBtnFun = function (selectId) {
    $("#itemshowdiv").load(getRootPath() + "/plan/projectYearPlan/showItem.action", null, function () {
        projectYearPlanItemJsForm.showEdit(tableAssFun.getRowData(selectId), projectPlanModule.eidtItemBtnFunCallback);
    });
}
//编辑的回调
projectPlanModule.eidtItemBtnFunCallback = function (rowData) {
    //赋值
    tableAssFun.fillRowData(rowData)
    //整个页面重新计算，排号
    projectPlanModule.calcauteAllPage(rowData.projectType);
    $("#itemChange").val("Y");
}
//查看
projectPlanModule.viewItemBtnFun = function (selectId) {
    $("#itemshowdiv").load(getRootPath() + "/plan/projectYearPlan/showViewItem.action", null, function () {
        projectYearPlanItemViewJsForm.showEdit(tableAssFun.getRowData(selectId), null);
    });
}
//删除按钮
projectPlanModule.deleteItemBtnFun = function (selectId) {
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function () {
            var itemType = $("#projectType_" + selectId).val();
            $("#tr_" + itemType + "_" + selectId).remove();
            projectPlanModule.calcauteAllPage();
            $("#itemChange").val("Y");
        }
    });
}
//取消全选
projectPlanModule.unSelectItemFun = function () {
    $("[name^='selectItemCheckbox_']:checked").each(function (itemIndex, itemObject) {
        $(this).click()
    })
}

//计算整个页
projectPlanModule.calcauteAllPage = function () {
    projectPlanModule.sequenceRebuild();
    projectPlanModule.calcauteStatis();
}

//计算所有末级节点
projectPlanModule.calcauteStatis = function () {
    var topItem;
    var attKey;
    var rowDataRoot = {};
    for (var index1 = 0; index1 < tableAssFun.dicTree.length; index1++) {
        topItem = tableAssFun.dicTree[index1];
        var subxsItem;
        var statisCode;
        var rowDataTop = {};
        for (var index2 = 0; index2 < tableAssFun.xsDicList.length; index2++) {
            var rowData1 = {};
            subxsItem = tableAssFun.xsDicList[index2];
            statisCode = topItem.code + "_" + subxsItem.code;
            $("[id^='tr_" + topItem.code + "_']").each(function (itemIndex, itemObject) {
                var trid = $(itemObject).attr("id");
                if (trid.indexOf("_" + subxsItem.code + "_") > 0) {
                    var id = projectPlanModule.getIdOnTr($(itemObject));
                    for (var rowIndex = 0; rowIndex < tableAssFun.numItemAttribute.length; rowIndex++) {
                        attKey = tableAssFun.numItemAttribute[rowIndex];
                        rowData1[attKey] = xyParseToFloat(rowData1[attKey]) + xyParseToFloat($("#" + attKey + "_" + id).val());
                    }
                }
            })
            for (var rowIndex = 0; rowIndex < tableAssFun.numItemAttribute.length; rowIndex++) {
                attKey = tableAssFun.numItemAttribute[rowIndex];
                $("#" + attKey + "_" + statisCode).val(xyParseToFloat(rowData1[attKey]));
                $("#dis_" + attKey + "_" + statisCode).html(xyParseToFloatStr(rowData1[attKey]));
                rowDataTop[attKey] = xyParseToFloat(rowDataTop[attKey]) + xyParseToFloat(rowData1[attKey]);
            }
        }

        for (var rowIndex = 0; rowIndex < tableAssFun.numItemAttribute.length; rowIndex++) {
            attKey = tableAssFun.numItemAttribute[rowIndex];
            $("#" + attKey + "_" + topItem.code).val(xyParseToFloat(rowDataTop[attKey]));
            $("#dis_" + attKey + "_" + topItem.code).html(xyParseToFloatStr(rowDataTop[attKey]));
            rowDataRoot[attKey] = xyParseToFloat(rowDataRoot[attKey]) + xyParseToFloat(rowDataTop[attKey]);
        }

        var subxcItem;
        for (var index3 = 0; index3 < topItem.nextDic.length; index3++) {
            var rowData3 = {};
            subxcItem = topItem.nextDic[index3];
            $("[id^='tr_" + subxcItem.code + "_'][datatype='data']").each(function (itemIndex, itemObject) {
                var id = projectPlanModule.getIdOnTr($(itemObject));
                for (var rowIndex = 0; rowIndex < tableAssFun.numItemAttribute.length; rowIndex++) {
                    attKey = tableAssFun.numItemAttribute[rowIndex];
                    rowData3[attKey] = xyParseToFloat(rowData3[attKey]) + xyParseToFloat($("#" + attKey + "_" + id).val());
                }
            })
            for (var rowIndex = 0; rowIndex < tableAssFun.numItemAttribute.length; rowIndex++) {
                attKey = tableAssFun.numItemAttribute[rowIndex];
                $("#" + attKey + "_" + subxcItem.code).val(xyParseToFloat(rowData3[attKey]));
                $("#dis_" + attKey + "_" + subxcItem.code).html(xyParseToFloatStr(rowData3[attKey]));
            }
        }
    }
    for (var rowIndex = 0; rowIndex < tableAssFun.numItemAttribute.length; rowIndex++) {
        attKey = tableAssFun.numItemAttribute[rowIndex];
        $("#" + attKey + "_0").val(xyParseToFloat(rowDataRoot[attKey]));
        $("#dis_" + attKey + "_0").html(xyParseToFloatStr(rowDataRoot[attKey]));
    }
}


//序号重排
projectPlanModule.sequenceRebuild = function (code) {
    if (code) {
        var index = 1;
        $("[id^='tr_" + code + "_']").each(function (itemIndex, itemObject) {
            var id = projectPlanModule.getIdOnTr($(itemObject));
            $("#sequence_" + id).html((index++))
        })
    } else {
        $("[leaf='Y']").each(function (leafIndex, leafbject) {
            var index = 1;
            var nowCode = $(leafbject).attr("leafcode");
            $("[id^='tr_" + nowCode + "_']").each(function (itemIndex, itemObject) {
                var id = projectPlanModule.getIdOnTr($(itemObject));
                $("#sequence_" + id).html((index++))
            })
        })
    }


}
//初始化意见域
projectPlanModule.initSuggestArea = function (busiId) {
    $("#suggestAreaDiv").hide();
    //发请求显示
    $.ajax({
        type: 'GET',
        cache: false,
        url: getRootPath() + '/plan/projectYearPlan/loadSuggest.action?id=' + busiId + "&_n=" + (new Date()).getTime(),
        dataType: "json",
        success: function (resData) {
            //系统变化和项目编码重新赋值成最新的
            if (resData) {
                projectPlanModule.initSuggestAreaCallback(resData);
            }

        }
    });
}
projectPlanModule.initSuggestAreaCallback = function (dataList) {
    if (dataList && dataList.length > 0) {
        $("#suggestAreaDiv").show();
    } else {
        $("#suggestAreaDiv").hide();
    }
    var html = "";
    var rowData;
    for (var i = 0; i < dataList.length; i++) {
        rowData = dataList[i];
        html += "<p>";
        html += "	<div>" + rowData.message + "</div>";
        html += "	<br>";
        html += "	<div >" + rowData.createDateDesc + "&nbsp;&nbsp;&nbsp;" + rowData.userName + "</div>";
        html += "</p>";
    }
    $("#suggestListDiv").html(html)
}

//页面赋值
projectPlanModule.initItemData = function (itemList) {
    if (itemList) {
        var rowData;
        for (var iIndex = 0; iIndex < itemList.length; iIndex++) {
            rowData = itemList[iIndex];
            tableAssFun.addItemToTypeLast(rowData);
        }
        //所有页计算
        projectPlanModule.calcauteAllPage();
    }
}
//页面赋值
projectPlanModule.export = function () {
    var id = $("#id").val();
    window.location.href = getRootPath() + "/plan/projectYearPlan/export.action?id=" + id;
}
//初始化
projectPlanModule.initData = function () {
    var id = $("#id").val();
    var url = getRootPath() + "/plan/projectYearPlan/getData.action";
    jQuery.ajax({
        url: url,
        async: false,
        type: 'POST',
        data: {"id": id},
        success: function (data) {
            //页面初始化
            tableAssFun.initData(data);
            //项目列表初始化
            projectPlanModule.initItemData(data.itemList);
            //主表信息出生化
            if (data.head) {
                $("#id").val(data.head.id);
                $("#selectId").val(data.head.id);
                $("#modifyDate").val(data.head.modifyDate);
                $("#selectYear").val(data.head.planYear);
                $("#planYear").val(data.head.planYear);
                $("#areaCode").val(data.head.planAreaCode);
                $("#areaName").val(data.head.planAreaName);
                $("#planAreaCode").val(data.head.planAreaCode);
                $("#planAreaName").val(data.head.planAreaName);
                if(data.head.planSeqno){
                    $("#planSeqno").val(data.head.planSeqno);
                }
                $("#remark").val(data.head.remark);
                $("#disSemark").val(data.head.remark);
                $("#planName").val(data.head.planName);
                $("#itemChange").val("N");
                $("#planNameTitle1").html("<span>" + data.head.planAreaName + data.head.planYear + "年基础设施及功能类项目建设计划表</span>");
            }
            if(data.dataNew == "Y"){
                $("#itemChange").val("Y");
            }
            //调整页面显示，其它页面不显示
            var planSeqnoValueTemp = $("#planSeqno").val();
            if(planSeqnoValueTemp == '2'){
                $("#remarkDiv").show();
            }
            var configKeyValue = $("#addEditCtrl_wfvar").val();
            if (configKeyValue === "must" || configKeyValue === "edit") {
                $("#disSemark").removeAttr("readonly");
            }
        }
    });
}


$(document).ready(function () {
    ie8StylePatch();

    $(".datepicker-input").each(function () {
        $(this).datepicker();
    });


    var pageMode = $("#pageMode").val();
    if (pageMode != "VIEW") {
        var businessJson = $("#businessJson").val();
        if (businessJson.length > 0) {
            var businessObj = eval("(" + businessJson + ")");
            $("#projectPlanForm").fill(businessObj);
        }
        formPriv.doForm();
        if ('EDIT' != pageMode) {
            var planSeqnoValue = $("#planSeqno").val();

            if(planSeqnoValue == '1'){
                $("#workflowFormButton").prepend("<button class=\"btn dark\" id=\"excelDown\" type=\"button\">导入</button>&nbsp;");
                $("#excelDown").click(function () {
                    importPlanYearExcelOpen();
                });
            }
        }
    }

    //初始化
    projectPlanModule.initData();
    //初始化意见域
    if (pageMode == "VIEW") {
        projectPlanModule.initSuggestArea($("#id").val());
        $("#workflowFormButton").prepend("<button class=\"btn dark\" id=\"exportExcel\" type=\"button\">导出</button>&nbsp;");
        $("#exportExcel").click(function () {
            projectPlanModule.export();
        });
    }
    var tiHeight = $(window).height();
    $("#pageDisplayDiv").height(tiHeight - 60);
});
