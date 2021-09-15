var cmProjectPlanInfoFormPanel = {};
cmProjectPlanInfoFormPanel.subState = false;
cmProjectPlanInfoFormPanel.callback = null;
cmProjectPlanInfoFormPanel.dutyCompanyJcTree = null;

cmProjectPlanInfoFormPanel.loadCompanyTree = function(projectId) {
    cmProjectPlanInfoFormPanel.dutyCompanyJcTree = JCTree.init({
        container: "dutyCompanyFormDiv",
        controlId: "dutyCompanyFormDiv-dutyCompany",
        isCheckOrRadio:false,
        isPersonOrOrg:false,
        urls: {
            org: getRootPath() + "/project/person/personTreeByProjectId.action?projectId=" + projectId
        }
    });
};

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
cmProjectPlanInfoFormPanel.init = function(config) {
    $('#entityTaskFormTitle').html(config.title);
    $("#entityTaskForm #id").val('');
    cmProjectPlanInfoFormPanel.callback = config.callback;
    if (config.operator == 'add') {
        cmProjectPlanInfoFormPanel.loadCompanyTree(config.projectId);
        $("#entityTaskForm #projectId").val(config.projectId);
        $("#entityTaskForm #stageId").val(config.stageId);
        cmProjectPlanInfoFormPanel.operatorModal('show');
    } else {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/project/plan/info/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityTaskForm").fill(data);
                    cmProjectPlanInfoFormPanel.loadCompanyTree(data.projectId);
                    if (data.dutyCompany && data.dutyCompanyValue) {
                        setTimeout(function(){
                            cmProjectPlanInfoFormPanel.dutyCompanyJcTree.setData({"id": data.dutyCompany, "text": data.dutyCompanyValue});
                        }, 1000);
                    }
                    cmProjectPlanInfoFormPanel.operatorModal('show');
                }
            }
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
cmProjectPlanInfoFormPanel.formValidate = function() {
    if (!$("#entityTaskForm").valid()) {
        return false;
    }
    return true;
};

/**
 * 保存或修改方法
 */
cmProjectPlanInfoFormPanel.saveOrModify = function () {
	if (cmProjectPlanInfoFormPanel.subState) {
	    return;
    }
	cmProjectPlanInfoFormPanel.subState = true;
	if (!cmProjectPlanInfoFormPanel.formValidate()) {
	    cmProjectPlanInfoFormPanel.subState = false;
	    return;
    }
    var formData = $("#entityTaskForm").serializeArray();
    var url = getRootPath() + "/project/plan/info/" + ($("#id").val() != '' ? 'update' : 'save') + ".action";
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: formData,
        success : function(data) {
            cmProjectPlanInfoFormPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmProjectPlanInfoFormPanel.operatorModal('hide');
                if (cmProjectPlanInfoFormPanel != undefined && cmProjectPlanInfoFormPanel != null) {
                    cmProjectPlanInfoFormPanel.callback();
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
            cmProjectPlanInfoFormPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
cmProjectPlanInfoFormPanel.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn').click(function () { cmProjectPlanInfoFormPanel.saveOrModify(); });
    $('#closeBtn').click(function () { cmProjectPlanInfoFormPanel.operatorModal('hide'); });
});

/**check时间*/
jQuery.validator.addMethod("common_start_end_date", function (value, element) {
    var planStartDate = $('#entityTaskForm #planStartDate').val();
    var planEndDate = $('#entityTaskForm #planEndDate').val();
    if (planStartDate == '' || planEndDate == '') {
        return true;
    }
    var json = new CommonToolsLib.CommonTools({}).dateCompare(planStartDate, planEndDate);
    if(!json){
        $(element).data('error-msg', "开始时间不能大于结束时间");
        return false;
    }
    return json;
}, function(params, element) { return $(element).data('error-msg'); });

/**
 * jquery.validate
 */
$("#entityTaskForm").validate({
	ignore:'ignore',
	rules: {
        planCode: { required: true, maxlength:255 },
        planName: { required: true, maxlength:255 },
        outputValue: { common_check_positive_double_four: true },
        dutyCompany: { required: false, maxlength:50 },
        dutyPerson: { required: false, maxlength:255 },
        planStartDate: { required: true, common_start_end_date: true },
        planEndDate: { required: true, common_start_end_date: true },
        queue: { required: true, maxlength:10, number: true},
        remark: { required: false, maxlength:16383 },
        planDay: {maxlength: 16, number: true},
        planWorkDay: {maxlength: 16, number: true},
        selfWeight: {maxlength: 16, number: true}
    },
    messages:{
        queue: { number: "请输入有效的数字" },
        planDay: { number: "请输入有效的数字" },
        planWorkDay: { number: "请输入有效的数字" },
        selfWeight: { number: "请输入有效的数字" }
    }
});