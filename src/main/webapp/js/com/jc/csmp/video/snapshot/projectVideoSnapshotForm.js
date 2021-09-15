var projectVideoSnapshotJsForm = {};


//初始化
projectVideoSnapshotJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        projectVideoSnapshotJsForm.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", 
	        data : {id: config.id},
	        dataType : "json",
            url : getRootPath() + "/video/snapshot/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    projectVideoSnapshotJsForm.operatorModal('show');
                }
            }
        });
    }
};

//验证表单
projectVideoSnapshotJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

//保存或修改方法
projectVideoSnapshotJsForm.saveOrModify = function () {
	if (projectVideoSnapshotJsForm.subState) {
	    return;
    }
	projectVideoSnapshotJsForm.subState = true;
	if (!projectVideoSnapshotJsForm.formValidate()) {
	    projectVideoSnapshotJsForm.subState = false;
	    return;
    }
	var url = getRootPath() + "/video/snapshot/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action'; 
    jQuery.ajax({
        url : url, 
        type : 'POST',
        cache: false,
        data :  $("#entityForm").serializeArray(),
        success : function(data) {
            projectVideoSnapshotJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                projectVideoSnapshotJsForm.operatorModal('hide');
                projectVideoSnapshotJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            projectVideoSnapshotJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

//操作窗口开关
projectVideoSnapshotJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
     $('#saveBtn') .click(function () { projectVideoSnapshotJsForm.saveOrModify(); });
     $('#closeBtn') .click(function () { projectVideoSnapshotJsForm.operatorModal('hide'); });
});

//jquery.validate
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		equiCode: { required: false, maxlength:64 },
		imgPath: { required: false, maxlength:2000 },
		remark: { required: false, maxlength:16383 },
    }
});