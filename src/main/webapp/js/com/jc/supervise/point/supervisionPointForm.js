var supervisionPointFormPanel = {};
supervisionPointFormPanel.subState = false;

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
supervisionPointFormPanel.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        supervisionPointColumnListPanel.init({supervisionId: $('#entityForm #tempId').val()});
        supervisionPointFormPanel.operatorModal('show');
    } else if (config.operator == 'modify' || config.operator == 'look') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/supervise/point/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    if (config.operator == 'look') {
                        $('#saveBtn').hide();
                        $('#checkBtn').hide();
                    }
                    supervisionPointColumnListPanel.init({supervisionId: data.id, operator: config.operator});
                    supervisionPointFormPanel.operatorModal('show');
                }
            }
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
supervisionPointFormPanel.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
supervisionPointFormPanel.saveOrModify = function () {
    if (supervisionPointFormPanel.subState) {
        return;
    }
    supervisionPointFormPanel.subState = true;
    if (!supervisionPointFormPanel.formValidate()) {
        supervisionPointFormPanel.subState = false;
        return;
    }
    var url = getRootPath() + "/supervise/point/" + ($("#id").val() != '' ? 'update' : 'save') + '.action';
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: $("#entityForm").serializeArray(),
        success : function(data) {
            supervisionPointFormPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                supervisionPointFormPanel.operatorModal('hide');
                supervisionPointListPanel.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            supervisionPointFormPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
supervisionPointFormPanel.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

supervisionPointFormPanel.loadCheck = function (){
    $("#formCheckModule").load(getRootPath() + "/supervise/point/checkPage.action", null, function() {
        supervisionPointCheckPagePanel.init({supervisionId: supervisionPointColumnListPanel.supervisionId});
    });
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn').click(function () { supervisionPointFormPanel.saveOrModify(); });
    $('#closeBtn').click(function () { supervisionPointFormPanel.operatorModal('hide'); });
    $('#checkBtn').click(function(){ supervisionPointFormPanel.loadCheck(); });
});

/**
 * jquery.validate
 */
$("#entityForm").validate({
    ignore:'ignore',
    rules: {
        pointName: { required: true, maxlength:255 },
        functionName: { required: true, maxlength:255 },
        pointDesc: { maxlength: 2000 },
        jsContent: {required: true},
        errorText: { required: true, maxlength: 500 }
    }
});