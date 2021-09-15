var companyProjectsZtbJsForm = {};


//初始化
companyProjectsZtbJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        companyProjectsZtbJsForm.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", 
	        data : {id: config.id},
	        dataType : "json",
            url : getRootPath() + "/csmp/ptProjectZtb/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    companyProjectsZtbJsForm.operatorModal('show');
                }
            }
        });
    }
};

//验证表单
companyProjectsZtbJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

//保存或修改方法
companyProjectsZtbJsForm.saveOrModify = function () {
	if (companyProjectsZtbJsForm.subState) {
	    return;
    }
	companyProjectsZtbJsForm.subState = true;
	if (!companyProjectsZtbJsForm.formValidate()) {
	    companyProjectsZtbJsForm.subState = false;
	    return;
    }
	var url = getRootPath() + "/csmp/ptProjectZtb/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action'; 
    jQuery.ajax({
        url : url, 
        type : 'POST',
        cache: false,
        data :  $("#entityForm").serializeArray(),
        success : function(data) {
            companyProjectsZtbJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                companyProjectsZtbJsForm.operatorModal('hide');
                companyProjectsZtbJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            companyProjectsZtbJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

//操作窗口开关
companyProjectsZtbJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
     $('#saveBtn') .click(function () { companyProjectsZtbJsForm.saveOrModify(); });
     $('#closeBtn') .click(function () { companyProjectsZtbJsForm.operatorModal('hide'); });
});

//jquery.validate
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		pcpZblx: { required: false, maxlength:200 },
		pcpZbfs: { required: false, maxlength:200 },
		pcpZbdwmc: { required: false, maxlength:200 },
		pcpZbrq: { required: false, maxlength:19 },
		pcpZbje: { required: false, maxlength:16 },
		pcpZbtzs: { required: false, maxlength:200 },
		pcpXq: { required: false, maxlength:1000 },
		pcpProjectNum: { required: false, maxlength:200 },
		dlhDataId: { required: false, maxlength:100 },
		dlhDataModifyDate: { required: false, maxlength:19 },
		dlhDataSrc: { required: false, maxlength:64 },
		dlhDataUser: { required: false, maxlength:100 },
		pcpArea: { required: false, maxlength:64 },
    }
});