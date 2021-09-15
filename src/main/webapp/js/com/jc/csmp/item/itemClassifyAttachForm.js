var itemClassifyAttachJsForm = {};

//初始化
itemClassifyAttachJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        itemClassifyAttachJsForm.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", 
	        data : {id: config.id},
	        dataType : "json",
            url : getRootPath() + "/csmp/item/attach/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    itemClassifyAttachJsForm.operatorModal('show');
                }
            }
        });
    }
};

//验证表单
itemClassifyAttachJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

//保存或修改方法
itemClassifyAttachJsForm.saveOrModify = function () {
	if (itemClassifyAttachJsForm.subState) {
	    return;
    }
	itemClassifyAttachJsForm.subState = true;
	if (!itemClassifyAttachJsForm.formValidate()) {
	    itemClassifyAttachJsForm.subState = false;
	    return;
    }
	var url = getRootPath() + "/csmp/item/attach/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action'; 
    jQuery.ajax({
        url : url, 
        type : 'POST',
        cache: false,
        data :  $("#entityForm").serializeArray(),
        success : function(data) {
            itemClassifyAttachJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                itemClassifyAttachJsForm.operatorModal('hide');
                itemClassifyAttachJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            itemClassifyAttachJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

//操作窗口开关
itemClassifyAttachJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
     $('#saveBtn') .click(function () { itemClassifyAttachJsForm.saveOrModify(); });
     $('#closeBtn') .click(function () { itemClassifyAttachJsForm.operatorModal('hide'); });
});

//jquery.validate
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		itemAttach: { required: false, maxlength:255 },
		itemId: { required: false, maxlength:255 },
		isRequired: { required: false, maxlength:10 },
		isCheckbox: { required: false, maxlength:10 },
    }
});