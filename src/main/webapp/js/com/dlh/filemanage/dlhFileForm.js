var dlhFileJsForm = {};


//初始化
dlhFileJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        dlhFileJsForm.operatorModal('show');
        liuAttachPool.initPageAttach(1, null,"busi_dlh_file");
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", 
	        data : {id: config.id},
	        dataType : "json",
            url : getRootPath() + "/dlh/filemanage/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    dlhFileJsForm.operatorModal('show');
                    liuAttachPool.initPageAttach(1, data.id ,"busi_dlh_file");
                }
            }
        });
    }
};

//验证表单
dlhFileJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

//保存或修改方法
dlhFileJsForm.saveOrModify = function () {
	if (dlhFileJsForm.subState) {
	    return;
    }
	dlhFileJsForm.subState = true;
	if (!dlhFileJsForm.formValidate()) {
	    dlhFileJsForm.subState = false;
	    return;
    }
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getAllFiles()});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
    var url = getRootPath() + "/dlh/filemanage/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action';
    jQuery.ajax({
        url : url, 
        type : 'POST',
        cache: false,
        data :  formData,
        success : function(data) {
            dlhFileJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                dlhFileJsForm.operatorModal('hide');
                dlhFileJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            dlhFileJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

//操作窗口开关
dlhFileJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
     $('#saveBtn') .click(function () { dlhFileJsForm.saveOrModify(); });
     $('#closeBtn') .click(function () { dlhFileJsForm.operatorModal('hide'); });

});

//jquery.validate
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		filename: { required: false, maxlength:200 },
		objurl: { required: false, maxlength:200 },
		yewuid: { required: false, maxlength:200 },
		yewucolname: { required: false, maxlength:200 },
		remark: { required: false, maxlength:2000 },
		tableName: { required: false, maxlength:200 },
		userId: { required: false, maxlength:200 },
    }
});