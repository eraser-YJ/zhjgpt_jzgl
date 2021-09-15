var projectYearPlanItemViewJsForm = {};
projectYearPlanItemViewJsForm.callbackFun;
/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
projectYearPlanItemViewJsForm.showNew = function (dicCode, dicName, callback) {
    projectYearPlanItemViewJsForm.callbackFun = callback;
    $('#entityFormTitle').html('新增项目');
    $("#entityForm #id").val('');
    $("#entityForm #itemSelectType").val(dicCode);
    $("#entityForm #itemSelectTypeName").val(dicName);
    var reg = new RegExp(",", "g")
    $("#entityForm #itemSelectTypeDisName").val(dicName.replace(reg, " =》 "));
    projectYearPlanItemViewJsForm.operatorModal('show');
};

projectYearPlanItemViewJsForm.showEdit = function (data, callback) {
    projectYearPlanItemViewJsForm.operatorModal('show');
    projectYearPlanItemViewJsForm.callbackFun = callback;
    projectYearPlanItemViewJsForm.fillData(data);
    $('#entityFormTitle').html('编辑项目');
};


projectYearPlanItemViewJsForm.getRowData = function () {
    var rowData = {
        "itemId": nvl($("#entityForm #itemId").val()),
        "projectType": nvl($("#entityForm #itemSelectType").val()),
        "projectTypeName": nvl($("#entityForm #itemSelectTypeName").val()),
        "projectName": nvl($("#entityForm #projectName").val()),
        "dutyCompany": nvl($("#entityForm #dutyCompany").val()),
        "dutyPerson": nvl($("#entityForm #dutyPerson").val()),
        "projectTotalDay": nvl($("#entityForm #projectTotalDay").val()),
        "projectTotalInvest": nvl($("#entityForm #projectTotalInvest").val()),
        "projectUsedInvest": nvl($("#entityForm #projectUsedInvest").val()),
        "projectNowInvest": nvl($("#entityForm #projectNowInvest").val()),
        "projectAfterInvest": nvl($("#entityForm #projectAfterInvest").val()),
        "projectNowDesc": nvl($("#entityForm #projectNowDesc").val()),
        "projectDesc": nvl($("#entityForm #projectDesc").val()),
        "remark": nvl($("#entityForm #remark").val())
    };
    return rowData;

};
projectYearPlanItemViewJsForm.fillData = function (rowData) {
    $("#entityForm #itemId").val(nvl(rowData.itemId));
    $("#entityForm #itemSelectType").val(rowData.projectType);
    $("#entityForm #itemSelectTypeName").val(rowData.projectTypeName);
    var reg = new RegExp(",", "g")
    $("#entityForm #itemSelectTypeDisName").val(rowData.projectTypeName.replace(reg, " =》 "));
    $("#entityForm #projectName").val(nvl(rowData.projectName));
    $("#entityForm #dutyCompany").val(nvl(rowData.dutyCompany));
    $("#entityForm #dutyPerson").val(nvl(rowData.dutyPerson));
    $("#entityForm #projectTotalDay").val(nvl(rowData.projectTotalDay));
    $("#entityForm #projectTotalInvest").val(nvl(rowData.projectTotalInvest));
    $("#entityForm #projectUsedInvest").val(nvl(rowData.projectUsedInvest));
    $("#entityForm #projectNowInvest").val(nvl(rowData.projectNowInvest));
    $("#entityForm #projectAfterInvest").val(nvl(rowData.projectAfterInvest));
    $("#entityForm #projectNowDesc").val(nvl(rowData.projectNowDesc));
    $("#entityForm #projectDesc").val(nvl(rowData.projectDesc));
    $("#entityForm #remark").val(nvl(rowData.remark));
};


/**
 * 验证表单
 * @returns {boolean}
 */
projectYearPlanItemViewJsForm.formValidate = function () {
    if (!$("#entityForm").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
projectYearPlanItemViewJsForm.saveOrModify = function () {
    if (projectYearPlanItemViewJsForm.formValidate()) {
        projectYearPlanItemViewJsForm.operatorModal('hide');
        projectYearPlanItemViewJsForm.callbackFun(projectYearPlanItemViewJsForm.getRowData());
        return;
    }
};
projectYearPlanItemViewJsForm.typeChangeSize = 4;

//类型是否选择
projectYearPlanItemViewJsForm.checkType = function () {
    for (var nowIndex = 1; nowIndex < projectYearPlanItemViewJsForm.typeChangeSize; nowIndex++) {
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
projectYearPlanItemViewJsForm.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function () {
    ie8StylePatch();
    $(".datepicker-input").each(function () {
        $(this).datepicker();
    });
    $('#saveBtn').click(function () {
        projectYearPlanItemViewJsForm.saveOrModify();
    });
    $('#closeBtn').click(function () {
        projectYearPlanItemViewJsForm.operatorModal('hide');
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
        projectName: {required: true, maxlength: 40},
        dutyCompany: {required: true, maxlength: 64},
        dutyPerson: {required: true, maxlength: 64},
        projectTotalDay: {required: true,number:true},
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
        projectAfterInvest: {
            required: true,
            customXyNumber: true
        },
        projectNowInvestSrc1: {
            required: true,
            customXyNumber: true
        },
        projectNowInvestSrc2: {
            required: true,
            customXyNumber: true
        },
        projectNowInvestSrc3: {
            required: true,
            customXyNumber: true
        },
        projectNowInvestSrc4: {
            required: true,
            customXyNumber: true
        },
        projectNowDesc: {required: true, maxlength: 100},
        projectDesc: {required: false, maxlength: 100},
        remark: {required: false, maxlength: 100}
    }
});