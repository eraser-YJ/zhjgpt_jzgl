var superviseMessageFormPanel = {};

superviseMessageFormPanel.subState = false;

superviseMessageFormPanel.callback = null;

superviseMessageFormPanel.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    superviseMessageFormPanel.callback = config.callback;
    $('#saveBtn').hide();
    $.ajax({
        type: "GET", data: {id: config.id}, dataType : "json", url: getRootPath() + "/supervision/message/get.action",
        success : function(data) {
            if (data) {
                hideErrorMessage();
                $("#entityForm").fill(data);
                if (config.operator == 'dispose') {
                    $('#saveBtn').show();
                    $('#handleResultTr').html($('#handleResultTr').html() + "<span class='required'>*</span>");
                } else {
                    $('#entityForm #handleResult').attr('readonly', 'readonly');
                }
                superviseMessageFormPanel.operatorModal('show');
            }
        }
    });
};

/**
 * 验证表单
 * @returns {boolean}
 */
superviseMessageFormPanel.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
superviseMessageFormPanel.saveOrModify = function () {
    if (superviseMessageFormPanel.subState) {
        return;
    }
    superviseMessageFormPanel.subState = true;
    if (!superviseMessageFormPanel.formValidate()) {
        superviseMessageFormPanel.subState = false;
        return;
    }
    var url = getRootPath() + "/supervision/message/dispose.action";
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: $("#entityForm").serializeArray(),
        success : function(data) {
            superviseMessageFormPanel.subState = false;
            $("#token").val(data.token);
            msgBox.tip({ type: data.type, content: data.msg });
            if (data.success) {
                if (superviseMessageFormPanel.callback != undefined && superviseMessageFormPanel.callback != null) {
                    superviseMessageFormPanel.callback();
                }
                superviseMessageFormPanel.operatorModal('hide');
            }
        },
        error : function() {
            superviseMessageFormPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
superviseMessageFormPanel.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn') .click(function () { superviseMessageFormPanel.saveOrModify(); });
    $('#closeBtn') .click(function () { superviseMessageFormPanel.operatorModal('hide'); });
});

$("#entityForm").validate({
    ignore:'ignore',
    rules: {
        handleResult: { required: true, maxlength:2000 }
    }
});