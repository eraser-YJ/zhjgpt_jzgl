var dlhTableMapJsForm = {};

//初始化
dlhTableMapJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    if (config.operator == 'add') {
        dlhTableMapJsForm.operatorModal('show');
    } else if (config.operator == 'modify') {
        $.ajax({
            type : "GET", 
	        data : {id: config.id},
	        dataType : "json",
            url : getRootPath() + "/dlh/tablemap/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    dlhTableMapJsForm.operatorModal('show');
                }
            }
        });
    }
};

//验证表单
dlhTableMapJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};
dlhTableMapJsForm.loadOptions = function(obj,cid){
    var obj = $(obj);
    var cobj = $('#'+cid);
    if(obj.val()=='0'){
        cobj.empty();
        return;
    }

    cobj.empty();
    jQuery.ajax({
        url :  getRootPath() + "/dlh/tablemap/itemList.action",
        type : 'GET',
        data :  {"modelId":obj.val()},
        success : function(data) {
            for(var i=0;i<data.length;i++) {
                cobj.append($("<option value='"+data[i].itemName+"'>").html(data[i].itemComment));
            }
        },
        error : function() {

            msgBox.tip({ type:"fail", content: "获取列名错误" });
        }
    });


}
//保存或修改方法
dlhTableMapJsForm.saveOrModify = function () {
	if (dlhTableMapJsForm.subState) {
	    return;
    }
	dlhTableMapJsForm.subState = true;
	if (!dlhTableMapJsForm.formValidate()) {
	    dlhTableMapJsForm.subState = false;
	    return;
    }
	var url = getRootPath() + "/dlh/tablemap/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action';
    jQuery.ajax({
        url : url, 
        type : 'POST',
        cache: false,
        data :  $("#entityForm").serializeArray(),
        success : function(data) {
            dlhTableMapJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                dlhTableMapJsForm.operatorModal('hide');
                dlhTableMapJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            dlhTableMapJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

//操作窗口开关
dlhTableMapJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
     $('#saveBtn') .click(function () { dlhTableMapJsForm.saveOrModify(); });
     $('#closeBtn') .click(function () { dlhTableMapJsForm.operatorModal('hide'); });
});

//jquery.validate
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		objUrlK: { required: false, maxlength:64 },
		objUrlV: { required: false, maxlength:64 },
		tableNameK: { required: false, maxlength:100 },
		tableNameV: { required: false, maxlength:100 },
		columnNameK: { required: false, maxlength:100 },
		columnNameV: { required: false, maxlength:100 },
		columnType: { required: false, maxlength:50 },
    }
});