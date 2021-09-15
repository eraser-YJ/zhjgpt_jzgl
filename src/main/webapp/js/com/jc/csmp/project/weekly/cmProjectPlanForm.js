var cmProjectPlanInfoFormPanel = {};
cmProjectPlanInfoFormPanel.subState = false;
cmProjectPlanInfoFormPanel.callback = null;



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
       // cmProjectPlanInfoFormPanel.loadCompanyTree(config.projectId);
        $("#entityTaskForm #projectId").val(config.projectId);
        $("#entityTaskForm #stageId").val(config.stageId);
        liuAttachPool.initPageAttach(1 , 0 ,"cm_project_plan");
        cmProjectPlanInfoFormPanel.operatorModal('show');
    } else {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/project/plan/info/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityTaskForm").fill(data);
                  //  cmProjectPlanInfoFormPanel.loadCompanyTree(data.projectId);
                    liuAttachPool.initPageAttach(1, data.id ,"cm_project_plan");
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
   // formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
   // formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
    console.log(formData)
    var url = getRootPath() + "/project/plan/info/" + ($("#id").val() != '' ? 'update' : 'save') + ".action";
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: formData,
        success : function(data) {
            console.log(data.success)
            cmProjectPlanInfoFormPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmProjectPlanInfoFormPanel.operatorModal('hide');
                if (cmProjectPlanInfoFormPanel != undefined && cmProjectPlanInfoFormPanel != null) {
                    cmProjectPlanInfoFormPanel.callback();
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
    $('#form-modal-WeeklyPlan').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn').click(function () { cmProjectPlanInfoFormPanel.saveOrModify(); });
    $('#closeBtn').click(function () { cmProjectPlanInfoFormPanel.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
$("#entityTaskForm").validate({
	ignore:'ignore',
	rules: {
        completionRatio:
            {
                required: function(value) {
                    if (value.value == null || value.value == "") return true;
                   // console.log(value.value)
                    var reg = /^-?(100|(([1-9]\d|\d)(\.\d{1,2})?))$/;
                    console.log(reg.test(value.value))
                   // $("#completionRatio").val(value.value+"%")
                    return reg.test(value.value);
                },
                maxlength: 3
            },
        remark: { required: false, maxlength:16383 },
    }
});