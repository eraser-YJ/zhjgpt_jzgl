var projectMonthPlanItemJsForm = {};
projectMonthPlanItemJsForm.callbackFun;


projectMonthPlanItemJsForm.showEdit = function (data, callback) {
    projectMonthPlanItemJsForm.operatorModal('show');
    projectMonthPlanItemJsForm.callbackFun = callback;
    projectMonthPlanItemJsForm.fillData(data);
    $('#entityFormTitle').html('编辑项目月度信息');
};

projectMonthPlanItemJsForm.getRowData = function () {
    var rowData = {
        "itemId": $("#entityForm #itemId").val(),
        "projectType": $("#entityForm #itemSelectType").val(),
        "projectTypeName": $("#entityForm #itemSelectTypeName").val(),
        "projectId": $("#entityForm #projectId").val(),
        "projectName": $("#entityForm #projectName").val(),
        "dutyCompany": $("#entityForm #dutyCompany").val(),
        "dutyPerson": $("#entityForm #dutyPerson").val(),
        "projectTotalInvest": $("#entityForm #projectTotalInvest").val(),
        "projectUsedInvest": $("#entityForm #projectUsedInvest").val(),
        "projectNowInvest": $("#entityForm #projectNowInvest").val(),
        "projectMonthPlanInvest": $("#entityForm #projectMonthPlanInvest").val(),
        "projectMonthActInvest": $("#entityForm #projectMonthActInvest").val(),
        "xxjd": $("#entityForm #xxjd").val(),
        "xxjdAttchList": projectMonthPlanItemJsForm.getAttach(),
        "solveProblem": $("#entityForm #solveProblem").val(),
        "tudiCard": $("#entityForm #tudiCard").val(),
        "ydghxkCard": $("#entityForm #ydghxkCard").val(),
        "gcghxkCard": $("#entityForm #gcghxkCard").val(),
        "kgxkCard": $("#entityForm #kgxkCard").val(),
        "xmxzyjs": $("#entityForm #xmxzyjs").val(),
        "extStr5": $("#entityForm #extStr5").val(),
        "remark": $("#entityForm #remark").val()
    };
    return rowData;
};

projectMonthPlanItemJsForm.fillData = function (rowData) {
    $("#entityForm #itemId").val(nvl(rowData.itemId));
    $("#entityForm #itemSelectType").val(rowData.projectType);
    $("#entityForm #itemSelectTypeName").val(rowData.projectTypeName);
    var reg = new RegExp(",", "g")
    $("#entityForm #itemSelectTypeDisName").val(rowData.projectTypeName.replace(reg, " =》 "));
    $("#entityForm #projectName").val(nvl(rowData.projectName));
    $("#entityForm #projectId").val(nvl(rowData.projectId));
    $("#entityForm #dutyCompany").val(nvl(rowData.dutyCompany));
    $("#entityForm #dutyPerson").val(nvl(rowData.dutyPerson));
    $("#entityForm #projectTotalInvest").val(nvl(rowData.projectTotalInvest));
    $("#entityForm #projectUsedInvest").val(nvl(rowData.projectUsedInvest));
    $("#entityForm #projectNowInvest").val(nvl(rowData.projectNowInvest));
    $("#entityForm #projectMonthPlanInvest").val(nvl(rowData.projectMonthPlanInvest));
    $("#entityForm #projectMonthActInvest").val(nvl(rowData.projectMonthActInvest));
    $("#entityForm #solveProblem").val(nvl(rowData.solveProblem));
    $("#entityForm #tudiCard").val(nvl(rowData.tudiCard, "N"));
    $("#entityForm #ydghxkCard").val(nvl(rowData.ydghxkCard, "N"));
    $("#entityForm #gcghxkCard").val(nvl(rowData.gcghxkCard, "N"));
    $("#entityForm #kgxkCard").val(nvl(rowData.kgxkCard, "N"));
    $("#entityForm #xmxzyjs").val(nvl(rowData.xmxzyjs, "N"));
    $("#entityForm #remark").val(nvl(rowData.remark));
    $("#entityForm #extStr5").val(nvl(rowData.extStr5));
    $("#entityForm #xxjd").val(nvl(rowData.xxjd));
    $("#entityForm #xxjdAttchList").val(nvl(rowData.xxjdAttchList));
    //附件
    projectMonthPlanItemJsForm.showAttach(nvl(rowData.xxjdAttchList));
    if (rowData.projectId && rowData.projectId.length > 0) {
        if (rowData.xxjd && rowData.xxjd.length > 0) {
            return;
        }
        projectMonthPlanItemJsForm.selectedCallBack(rowData.projectId);
    }


};
projectMonthPlanItemJsForm.projectBtnFun = function () {
    var url = getRootPath() + "/plan/projectMonthPlan/projectNumDlg.action?n_=" + (new Date()).getTime();
    $("#projectDiv").load(url, null, function () {
        ProjectJsList.showProjectList(projectMonthPlanItemJsForm.projectBtnFunCallback);
    });
};

projectMonthPlanItemJsForm.projectBtnFunCallback = function (code) {
    var itemId = $("#entityForm #itemId").val();
    var msg = "";
    $("input[id^='projectId_']").each(function (index, indeObj) {
        if (indeObj.value == code) {
            var id = $(indeObj).attr("id");
            if (id != 'projectId_' + itemId) {
                msg = "重复";
            }
        }
    })
    if (msg.length > 0) {
        return msg;
    }
    projectMonthPlanItemJsForm.selectedCallBack(code);
    return "";
};

projectMonthPlanItemJsForm.selectedCallBack = function (prjectNum) {
    //2201081905240102
    $("#entityForm #projectId").val(nvl(prjectNum));
    var url1 = getRootPath() + "/plan/projectMonthPlan/getXxjd.action?projectId=" + prjectNum + "&n_" + new Date().getTime();
    jQuery.ajax({
        url: url1,
        type: 'GET',
        success: function (data) {
            $("#entityForm #xxjd").val(nvl(data.xxjd));
            $("#entityForm #xxjdAttchList").val(nvl(data.xxjdAttchList));
            $("#entityForm #projectMonthPlanInvest").val(nvl(data.projectMonthPlanInvest));
            $("#entityForm #projectMonthActInvest").val(nvl(data.projectMonthActInvest));
            projectMonthPlanItemJsForm.showAttach(data.xxjdAttchList);
        }
    });

    var url2 = getRootPath() + "/plan/projectMonthPlan/getQuestion.action?projectId=" + prjectNum + "&n_" + new Date().getTime();
    jQuery.ajax({
        url: url2,
        type: 'GET',
        success: function (data) {
            $("#entityForm #solveProblem").val(nvl(data.solveProblem));
        }
    });
}
projectMonthPlanItemJsForm.showAttach = function (xxjdAttchList) {
    $("#xxjdAttchListDiv").html("");
    if (xxjdAttchList) {
        var images = xxjdAttchList.split("#");
        if (images) {
            var imageInfo;
            var h = "";
            var h2 = "";
            for (var index = 0; index < images.length; index++) {
                imageInfo = images[index].split(",");
                if (imageInfo[0]) {
                    h2 += '<input type="hidden" id="imgInfo_' + imageInfo[0] + '" value="' + images[index] + '#">';
                    h += '<div class="delet-image" id="imgAInfo_' + imageInfo[0] + '">';
                    h += '<a href="' + getRootPath() + '/content/attach/download.action?attachId=' + imageInfo[0] + '">';
                    h += '<img src="' + getRootPath() + '/content/attach/originalRead/' + imageInfo[0] + '" style="width: 100%;height: 100%;"/>';
                    h += '</a>';
                    h += '<div class="delet-image-remove">';
                    h += '<i class="fa fa-remove-sign" onclick="projectMonthPlanItemJsForm.removeImage(\'' + imageInfo[0] + '\')" ></i>';
                    h += '</div>';
                    h += '</div>';
                }
            }
            $("#xxjdAttchListDiv").append(h2);
            $("#xxjdAttchListDiv").append(h);
        }
    }
}

projectMonthPlanItemJsForm.getAttach = function () {
    var info = [];
    $("input[id^='imgInfo_']").each(function (index, indeObj) {
        info.push(indeObj.value);
    })
    if (info.length > 0) {
        return info.join("#");
    }
    return ""
}

/**
 * 验证表单
 * @returns {boolean}
 */
projectMonthPlanItemJsForm.removeImage = function (removeImageId) {
    $("#imgInfo_" + removeImageId).remove()
    $("#imgAInfo_" + removeImageId).remove()
};

/**
 * 验证表单
 * @returns {boolean}
 */
projectMonthPlanItemJsForm.formValidate = function () {
    if (!$("#entityForm").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
projectMonthPlanItemJsForm.saveOrModify = function () {
    if (projectMonthPlanItemJsForm.formValidate()) {
        projectMonthPlanItemJsForm.operatorModal('hide');
        projectMonthPlanItemJsForm.callbackFun(projectMonthPlanItemJsForm.getRowData());
        return;
    } else {
        msgBox.info({type: "fail", content: "表单填写错误"});
    }
};
projectMonthPlanItemJsForm.typeChangeSize = 4;

//类型是否选择
projectMonthPlanItemJsForm.checkType = function () {
    for (var nowIndex = 1; nowIndex < projectMonthPlanItemJsForm.typeChangeSize; nowIndex++) {
        var selectValue = $("#itemSelectType" + nowIndex).val();
        if (selectValue == '' && $("#itemSelectType" + nowIndex + " option").length > 1) {
            msgBox.tip({type: "fail", content: "请选择第" + (nowIndex + 1) + "级的项目类型"});
            return false;
        }
    }
    return true;
}
/**
 * 操作窗口开关
 * @param operator
 */
projectMonthPlanItemJsForm.operatorModal = function (operator) {
    $('#plan-month-form-modal').modal(operator);
};

$(document).ready(function () {
    ie8StylePatch();
    $(".datepicker-input").each(function () {
        $(this).datepicker();
    });
    $('#saveMonthItemBtn').click(function () {
        projectMonthPlanItemJsForm.saveOrModify();
    });
    $('#closeMonthItemBtn').click(function () {
        projectMonthPlanItemJsForm.operatorModal('hide');
    });
});

//参数:param[0] 整数的范围，param[1] 小数的范围，param[2] 是否包含负数
jQuery.validator.addMethod("customXyNumber", function (value, element, param) {
    var reg = "";
    reg = "//^[0-9]{1,13}(\\.[0-9]{0,4})?$//";		//xx.xx
    reg = reg.replace(/\/\//g, "\/");
    reg = eval(reg);
    return this.optional(element) || reg.test(value);
}, $.i18n.prop("JC_SYS_013"));

/**
 * jquery.validate
 */
$("#entityForm").validate({
    ignore: 'ignore',
    rules: {
        projectId: {required: true},
        projectName: {required: true, maxlength: 40},
        dutyCompany: {required: true, maxlength: 64},
        dutyPerson: {required: true, maxlength: 64},
        projectTotalDay: {required: true, number: true},
        projectTotalInvest: {
            required: true,
            customXyNumber: true
        },
        projectUsedInvest: {
            required: true,
            customXyNumber: true
        },
        projectNowInvest: {
            required: true,
            customXyNumber: true
        },
        projectMonthPlanInvest: {
            required: true,
            customXyNumber: true
        },
        projectMonthActInvest: {
            required: true,
            customXyNumber: true
        },
        xxjd: {required: true, maxlength: 500},
        solveProblem: {required: false, maxlength: 1000},
        remark: {required: false, maxlength: 100}
    }
});