var superviseSendMessageFormPanel = {};

superviseSendMessageFormPanel.subState = false;

superviseSendMessageFormPanel.init = function(config) {
    $.ajax({
        type: "GET", data: {id: config.warningId}, dataType : "json", url: getRootPath() + "/supervision/warning/loadWarningInfo.action",
        success : function(data) {
            var warning = data.warning;
            $("#entityForm #warningId").val(warning.id);
            $("#entityForm #title").val(warning.projectName + "产生预警");
            $("#entityForm #content").val(warning.warningContent);
            var personList = data.personList;
            var receiveDeptIdSelect = $('#entityForm #receiveDeptId');
            receiveDeptIdSelect.append("<option value=''>- 请选择 -</option>");
            if (personList != null) {
                for (var i = 0; i < personList.length; i++) {
                    receiveDeptIdSelect.append("<option value='" + personList[i].companyId + "'>" + personList[i].companyName + "</option>");
                }
            }
            superviseSendMessageFormPanel.operatorModal('show');
        }
    });
};

/**
 * 验证表单
 * @returns {boolean}
 */
superviseSendMessageFormPanel.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
superviseSendMessageFormPanel.saveOrModify = function () {
    if (superviseSendMessageFormPanel.subState) {
        return;
    }
    superviseSendMessageFormPanel.subState = true;
    if (!superviseSendMessageFormPanel.formValidate()) {
        superviseSendMessageFormPanel.subState = false;
        return;
    }
    var url = getRootPath() + "/supervision/message/sendMessage.action";
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: $("#entityForm").serializeArray(),
        success : function(data) {
            superviseSendMessageFormPanel.subState = false;
            $("#token").val(data.token);
            msgBox.tip({ type: data.type, content: data.msg });
            if (data.success) {
                superviseSendMessageFormPanel.operatorModal('hide');
            }
        },
        error : function() {
            superviseSendMessageFormPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

superviseSendMessageFormPanel.receiveChange = function() {
    var deptId = $('#entityForm #receiveDeptId').val();
    if (deptId == "") {
        return;
    }
    jQuery.ajax({
        url: getRootPath() + "/supervision/message/getLeaderIdByDeptId.action", type: 'GET', cache: false, data: {id: deptId},
        success : function(data) {
            if (data.leaderId == "") {
                msgBox.tip({ type:"fail", content: "该企业未配置负责人，无法提交督办信息" });
                return;
            }
            $('#entityForm #receiveId').val(data.leaderId);
            $('#entityForm #receiveName').val(data.leaderName);
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
superviseSendMessageFormPanel.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn') .click(function () { superviseSendMessageFormPanel.saveOrModify(); });
    $('#closeBtn') .click(function () { superviseSendMessageFormPanel.operatorModal('hide'); });
    $('#entityForm #receiveDeptId').change(superviseSendMessageFormPanel.receiveChange);
});

$("#entityForm").validate({
    ignore:'ignore',
    rules: {
        title: {required: true, maxlength: 255 },
        content: {required: true, maxlength: 2000 },
        receiveDeptId: {required: true},
        receiveId: { required: true }
    }
});