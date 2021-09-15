var sysDepartmentJsForm = {};

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
sysDepartmentJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        sysDepartmentJsForm.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", 
	    data : {id: config.id}, 
	    dataType : "json",
            url : getRootPath() + "/doc/dept/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    sysDepartmentJsForm.operatorModal('show');
                }
            }
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
sysDepartmentJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

/**
 * 保存或修改方法
 */
sysDepartmentJsForm.saveOrModify = function () {
	if (sysDepartmentJsForm.subState) {
	    return;
    }
	sysDepartmentJsForm.subState = true;
	if (!sysDepartmentJsForm.formValidate()) {
	    sysDepartmentJsForm.subState = false;
	    return;
    }
	var url = getRootPath() + "/doc/dept/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action'; 
    jQuery.ajax({
        url : url, 
	type : 'POST', 
	cache: false, 
	data :  $("#entityForm").serializeArray(), 
        success : function(data) {
            sysDepartmentJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                sysDepartmentJsForm.operatorModal('hide');
                sysDepartmentJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            sysDepartmentJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
sysDepartmentJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
     $('#saveBtn') .click(function () { sysDepartmentJsForm.saveOrModify(); });
     $('#closeBtn') .click(function () { sysDepartmentJsForm.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		code: { required: false, maxlength:64 },
		name: { required: false, maxlength:255 },
		fullName: { required: false, maxlength:100 },
		deptDesc: { required: false, maxlength:255 },
		leaderId: { required: false, maxlength:36 },
		chargeLeaderId: { required: false, maxlength:36 },
		leaderId2: { required: false, maxlength:36 },
		parentId: { required: false, maxlength:36 },
		managerDept: { required: false, maxlength:10 },
		organizationId: { required: false, maxlength:36 },
		deptType: { required: false, maxlength:1 },
		queue: { required: false, maxlength:10 },
		shortName: { required: false, maxlength:10 },
		userName: { required: false, maxlength:50 },
		password: { required: false, maxlength:50 },
		type: { required: false, maxlength:2 },
		status: { required: false, maxlength:1 },
		openDay: { required: false, maxlength:10 },
		endDay: { required: false, maxlength:10 },
		fileSpace: { required: false, maxlength:9 },
		usedSpace: { required: false, maxlength:9 },
		balance: { required: false, maxlength:10 },
		smsBalance: { required: false, maxlength:10 },
		logo: { required: false, maxlength:200 },
		cont: { required: false, maxlength:20 },
		telp: { required: false, maxlength:20 },
		email: { required: false, maxlength:64 },
		memo: { required: false, maxlength:500 },
		deleteUser: { required: false, maxlength:36 },
		deleteDate: { required: false, maxlength:19 },
		createUserDep: { required: false, maxlength:36 },
		weight: { required: false, maxlength:36 },
		secret: { required: false, maxlength:32 },
		resourceId: { required: false, maxlength:255 },
    }
});