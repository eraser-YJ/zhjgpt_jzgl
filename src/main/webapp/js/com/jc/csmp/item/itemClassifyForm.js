var itemClassifyJsForm = {};

//初始化
itemClassifyJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        itemClassifyJsForm.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", 
	        data : {id: config.id},
	        dataType : "json",
            url : getRootPath() + "/csmp/item/classify/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    itemClassifyJsForm.operatorModal('show');
                }
            }
        });
    }
};

//验证表单
itemClassifyJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

//保存或修改方法
itemClassifyJsForm.saveOrModify = function () {
	if (itemClassifyJsForm.subState) {
	    return;
    }
	itemClassifyJsForm.subState = true;
	if (!itemClassifyJsForm.formValidate()) {
	    itemClassifyJsForm.subState = false;
	    return;
    }
	var url = getRootPath() + "/csmp/item/classify/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action'; 
    jQuery.ajax({
        url : url, 
        type : 'POST',
        cache: false,
        data :  $("#entityForm").serializeArray(),
        success : function(data) {
            itemClassifyJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                itemClassifyJsForm.operatorModal('hide');
                itemClassifyJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            itemClassifyJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

//操作窗口开关
itemClassifyJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
     $('#saveBtn') .click(function () { itemClassifyJsForm.saveOrModify(); });
     $('#closeBtn') .click(function () { itemClassifyJsForm.operatorModal('hide'); });
});

//jquery.validate
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		itemClassify: { required: false, maxlength:255 },
		itemCode: { required: false, maxlength:255 },
    }
});