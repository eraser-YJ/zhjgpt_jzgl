var cmProjectPlanProgressFormPanel = {};
cmProjectPlanProgressFormPanel.subState = false;
cmProjectPlanProgressFormPanel.callback = null;

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
cmProjectPlanProgressFormPanel.init = function(config) {
    $('#entityTaskFormTitle').html(config.title);
    cmProjectPlanProgressFormPanel.callback = config.callback;
    $.ajax({
        type : "GET", data : {id: config.id}, dataType : "json",
        url : getRootPath() + "/project/plan/info/get.action",
        success : function(data) {
            if (data) {
                hideErrorMessage();
                $("#entityTaskForm").fill(data);
                if (data.completionRatio == null) {
                    $('#entityTaskForm #completionRatio').val(0);
                }
                if (data.completionMoney == null) {
                    $('#entityTaskForm #completionMoney').val(0);
                }
                cmProjectPlanProgressFormPanel.operatorModal('show');
            }
        }
    });
};

/**
 * 验证表单
 * @returns {boolean}
 */
cmProjectPlanProgressFormPanel.formValidate = function() {
    if (!$("#entityTaskForm").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
cmProjectPlanProgressFormPanel.saveOrModify = function () {
    if (cmProjectPlanProgressFormPanel.subState) {
        return;
    }
    cmProjectPlanProgressFormPanel.subState = true;
    if (!cmProjectPlanProgressFormPanel.formValidate()) {
        cmProjectPlanProgressFormPanel.subState = false;
        return;
    }
    var url = getRootPath() + "/project/plan/info/modifyProgress.action";
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: $("#entityTaskForm").serializeArray(),
        success : function(data) {
            cmProjectPlanProgressFormPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmProjectPlanProgressFormPanel.operatorModal('hide');
                if (cmProjectPlanProgressFormPanel != undefined && cmProjectPlanProgressFormPanel != null) {
                    cmProjectPlanProgressFormPanel.callback();
                }
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityTaskForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            cmProjectPlanProgressFormPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
cmProjectPlanProgressFormPanel.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#modifyBtn').click(function () { cmProjectPlanProgressFormPanel.saveOrModify(); });
});

/**验证比例*/
jQuery.validator.addMethod("checkCompletionRatio", function (value, element) {
    if (value == "") {
        return true;
    }
    if (parseInt(value) > 100) {
        $(element).data('error-msg', '已完进度不能大于100');
        return false;
    }
    return true;
}, function(params, element) { return $(element).data('error-msg'); });

jQuery.validator.addMethod("commonStartEndDate", function (value, element) {
    var planStartDate = $('#entityTaskForm #actualStartDate').val();
    var planEndDate = $('#entityTaskForm #actualEndDate').val();
    if (planStartDate == '' || planEndDate == '') {
        return true;
    }
    var json = new CommonToolsLib.CommonTools({}).dateCompare(planStartDate, planEndDate);
    if(!json){
        $(element).data('error-msg', "实际开始时间不能大于实际结束时间");
        return false;
    }
    return json;
}, function(params, element) { return $(element).data('error-msg'); });

$("#entityTaskForm").validate({
    ignore:'ignore',
    rules: {
        completionRatio: { required: true, common_check_positive_integer_can_zore: true, checkCompletionRatio: true },
        //completionMoney: { required: true, common_check_positive_double_four_can_zore: true },
        actualStartDate: {required: true, commonStartEndDate: true },
        actualEndDate: { commonStartEndDate: true }
    }
});