var companyProjectsSgxkJsForm = {};


//初始化
companyProjectsSgxkJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        companyProjectsSgxkJsForm.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", 
	        data : {id: config.id},
	        dataType : "json",
            url : getRootPath() + "/csmp/projectSgxk/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    companyProjectsSgxkJsForm.operatorModal('show');
                }
            }
        });
    }
};

//验证表单
companyProjectsSgxkJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

//保存或修改方法
companyProjectsSgxkJsForm.saveOrModify = function () {
	if (companyProjectsSgxkJsForm.subState) {
	    return;
    }
	companyProjectsSgxkJsForm.subState = true;
	if (!companyProjectsSgxkJsForm.formValidate()) {
	    companyProjectsSgxkJsForm.subState = false;
	    return;
    }
	var url = getRootPath() + "/csmp/projectSgxk/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action'; 
    jQuery.ajax({
        url : url, 
        type : 'POST',
        cache: false,
        data :  $("#entityForm").serializeArray(),
        success : function(data) {
            companyProjectsSgxkJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                companyProjectsSgxkJsForm.operatorModal('hide');
                companyProjectsSgxkJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            companyProjectsSgxkJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

//操作窗口开关
companyProjectsSgxkJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
     $('#saveBtn') .click(function () { companyProjectsSgxkJsForm.saveOrModify(); });
     $('#closeBtn') .click(function () { companyProjectsSgxkJsForm.operatorModal('hide'); });
});

//jquery.validate
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		pcpHtlb: { required: false, maxlength:200 },
		pcpBdmc: { required: false, maxlength:200 },
		pcpHtbh: { required: false, maxlength:200 },
		pcpHtje: { required: false, maxlength:16 },
		pcpQdrq: { required: false, maxlength:19 },
		pcpXq: { required: false, maxlength:1000 },
		pcpProjectNum: { required: false, maxlength:200 },
		dlhDataId: { required: false, maxlength:100 },
		dlhDataModifyDate: { required: false, maxlength:19 },
		dlhDataSrc: { required: false, maxlength:64 },
		dlhDataUser: { required: false, maxlength:100 },
		pcpBzry: { required: false, maxlength:64 },
		pcpDh: { required: false, maxlength:64 },
    }
});