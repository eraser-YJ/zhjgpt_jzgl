var superviseWarningFormPanel = {};

superviseWarningFormPanel.subState = false;

superviseWarningFormPanel.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    $('#saveBtn').hide();
    $.ajax({
        type: "GET", data: {id: config.id}, dataType : "json", url: getRootPath() + "/supervision/warning/get.action",
        success : function(data) {
            if (data) {
                hideErrorMessage();
                $("#entityForm").fill(data);
                if (config.operator == 'dispose') {
                    $('#saveBtn').show();
                    $('#disploseResultTitle').html($('#disploseResultTitle').html() + "<span class='required'>*</span>");
                } else {
                    $('#entityForm #disploseResult').attr('readonly', 'readonly');
                }
                superviseWarningFormPanel.operatorModal('show');
            }
        }
    });
};

/**
 * 验证表单
 * @returns {boolean}
 */
superviseWarningFormPanel.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
superviseWarningFormPanel.saveOrModify = function () {
    if (superviseWarningFormPanel.subState) {
        return;
    }
    superviseWarningFormPanel.subState = true;
    if (!superviseWarningFormPanel.formValidate()) {
        superviseWarningFormPanel.subState = false;
        return;
    }
    var url = getRootPath() + "/supervision/warning/dispose.action";
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: $("#entityForm").serializeArray(),
        success : function(data) {
            superviseWarningFormPanel.subState = false;
            $("#token").val(data.token);
            msgBox.tip({ type: data.type, content: data.msg });
            if (data.success) {
                superviseWarningListPanel.renderTable();
                superviseWarningFormPanel.operatorModal('hide');
            }
        },
        error : function() {
            superviseWarningFormPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
superviseWarningFormPanel.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn') .click(function () { superviseWarningFormPanel.saveOrModify(); });
    $('#closeBtn') .click(function () { superviseWarningFormPanel.operatorModal('hide'); });
});

$("#entityForm").validate({
    ignore:'ignore',
    rules: {
        disploseResult: { required: true, maxlength:30000 }
    }
});