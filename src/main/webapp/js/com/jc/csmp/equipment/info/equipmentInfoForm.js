var equipmentInfoJsForm = {};


equipmentInfoJsForm.init = function () {
    var nowid = $("#entityForm #id").val();
    $.ajax({
        type: "GET",
        data: {id: nowid},
        dataType: "json",
        url: getRootPath() + "/equipment/info/getInfo.action",
        success: function (resData) {
            if (resData.data) {
                hideErrorMessage();
                equipmentInfoJsForm.showHtml(resData.data.equipmentType, function () {

                    $("#entityForm").fill(resData.data);
                    equipmentInfoJsForm.fillWarnInfo(resData.data.warnInfo);
                    if ($("#entityForm #modifyDate").val() != '') {
                        $("#equipmentType").attr("disabled", true)
                    } else {
                        $("#equipmentType").removeAttr("disabled")
                    }
                });
            } else {
                equipmentInfoJsForm.showHtml(null);
            }
        }
    });
};

/**
 * 验证表单
 * @returns {boolean}
 */
equipmentInfoJsForm.formValidate = function () {
    if (!$("#entityForm").valid()) {
        return false;
    }
    if (!$("#entityFormWarn").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
equipmentInfoJsForm.saveOrModify = function () {
    if (equipmentInfoJsForm.subState) {
        return;
    }
    equipmentInfoJsForm.subState = true;
    if (!equipmentInfoJsForm.formValidate()) {
        equipmentInfoJsForm.subState = false;
        return;
    }

    $("#warnInfo").val(equipmentInfoJsForm.getWarnInfo());
    var formData = $("#entityForm").serializeArray();
    var url = "";
    if ($("#entityForm #modifyDate").val() != '') {
        url = getRootPath() + "/equipment/info/update.action";
        formData.push({"name":"equipmentType","value":$("#equipmentType").val()})
    } else {
        url = getRootPath() + "/equipment/info/save.action";
    }


    jQuery.ajax({
        url: url,
        type: 'POST',
        cache: false,
        data: formData,
        success: function (data) {
            equipmentInfoJsForm.subState = false;
            $("#token").val(data.token);
            if (data.success == "true") {
                msgBox.tip({type: "success", content: data.successMessage});
                equipmentInfoJsForm.operatorModal('hide');
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else {
                    msgBox.info({type: "fail", content: data.errorMessage});
                }
            }
        },
        error: function () {
            equipmentInfoJsForm.subState = false;
            msgBox.tip({type: "fail", content: $.i18n.prop("JC_SYS_002")});
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
equipmentInfoJsForm.operatorModal = function (operator) {
    if (operator == 'hide') {
        window.location.href = getRootPath() + "/equipment/info/manage.action";
    }
};
//警告区取得数据
equipmentInfoJsForm.getWarnInfo = function () {
    var warnInfoData = {};
    var itemList = $("#entityFormWarn").serializeArray();
    var item;
    for (var itemIndex = 0; itemIndex < itemList.length; itemIndex++) {
        item = itemList[itemIndex];
        warnInfoData[item.name] = item.value;
    }
    return JSON.stringify(warnInfoData);
};
//警告区赋值数据
equipmentInfoJsForm.fillWarnInfo = function (warnInfoStr) {
    if (warnInfoStr) {
        var warnInfoObj = JSON.parse(warnInfoStr);
        $("#entityFormWarn").fill(warnInfoObj);
    }
}
equipmentInfoJsForm.projectBtnFun = function () {
    var url = getRootPath() + "/doc/project/loadDlgForm.action?n_=" + (new Date()).getTime();
    $("#projectDiv").load(url, null, function () {
        ProjectJsList.showProjectList(equipmentInfoJsForm.projectBtnFunCallback);
    });
};

equipmentInfoJsForm.projectBtnFunCallback = function (code, name) {
    $("#projectCode").val(code);
    $("#projectName").val(name);
};
equipmentInfoJsForm.deptBtnFun = function () {
    var url = getRootPath() + "/doc/dept/loadDlgForm.action?n_=" + (new Date()).getTime();
    $("#deptDiv").load(url, null, function () {
        deptJsList.showDeptList(equipmentInfoJsForm.deptBtnFunCallback);
    });
};

equipmentInfoJsForm.deptBtnFunCallback = function (id, name) {
    $("#useCompany").val(id);
    $("#useCompanyName").val(name);
};
//类型变化
equipmentInfoJsForm.typeChange = function () {
    var type = $("#equipmentType").val();
    equipmentInfoJsForm.showHtml(type);
}
//显示指定类型的预警区域
equipmentInfoJsForm.nowType = "";
equipmentInfoJsForm.showHtml = function (type, disCallback) {
    if (type && equipmentInfoJsForm.nowType != type) {
        equipmentInfoJsForm.nowType = type;
        var url = getRootPath() + "/js/com/jc/csmp/equipment/mechtype/" + type + "/warnStd.js?n_=" + (new Date()).getTime();
        $.getScript(url, function () {
            warnStdFun.initWarnStd(disCallback)
        });
    }
};

$(document).ready(function () {
    ie8StylePatch();
    $(".datepicker-input").each(function () {
        $(this).datepicker();
    });
    $('#projectListBtn').click(function () {
        equipmentInfoJsForm.projectBtnFun();
    });
    $('#useCompanyBtn').click(function () {
        equipmentInfoJsForm.deptBtnFun();
    });
    $('#saveBtn').click(function () {
        equipmentInfoJsForm.saveOrModify();
    });
    $('#closeBtn').click(function () {
        equipmentInfoJsForm.operatorModal('hide');
    });
    equipmentInfoJsForm.init();
});


