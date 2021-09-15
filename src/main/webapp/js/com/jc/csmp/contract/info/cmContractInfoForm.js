var cmContractInfoJsForm = {};

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
cmContractInfoJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    $('#saveBtn').show();
    if (config.operator == 'add') {
        liuAttachPool.initPageAttach(1 , 0 ,"cm_contract_info");
        cmContractInfoJsForm.operatorModal('show');
    } else if (config.operator == 'modify' || config.operator == 'look') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/contract/info/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    if (config.operator == 'modify') {
                        liuAttachPool.initPageAttach(1, data.id ,"cm_contract_info");
                    } else if (config.operator == 'look') {
                        $('#saveBtn').hide();
                        liuAttachPool.initPageAttachOnView(1, data.id ,"cm_contract_info");
                    }
                    cmContractInfoJsForm.operatorModal('show');
                }
            }
        });
    }
    if (config.operator != 'look') {
        $('#entityForm #partyaUnitValue').click(function () {
            new ChooseCompany.CompanyDataTable({
                renderElement: 'chooseCompanyModule',
                callback: function(data){
                    $('#entityForm #partyaUnitValue').val(data.name);
                    $('#entityForm #partyaUnit').val(data.id);
                }
            });
        });
        $('#entityForm #partybUnitValue').click(function () {
            new ChooseCompany.CompanyDataTable({
                renderElement: 'chooseCompanyModule',
                callback: function(data){
                    $('#entityForm #partybUnitValue').val(data.name);
                    $('#entityForm #partybUnit').val(data.id);
                }
            });
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
cmContractInfoJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

/**
 * 保存或修改方法
 */
cmContractInfoJsForm.saveOrModify = function () {
	if (cmContractInfoJsForm.subState) {
	    return;
    }
	cmContractInfoJsForm.subState = true;
	if (!cmContractInfoJsForm.formValidate()) {
	    cmContractInfoJsForm.subState = false;
	    return;
    }
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
	var url = getRootPath() + "/contract/info/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action'; 
    jQuery.ajax({
        url : url, type : 'POST', cache: false, data :  formData,
        success : function(data) {
            cmContractInfoJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmContractInfoJsForm.operatorModal('hide');
                cmContractInfoJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            cmContractInfoJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
cmContractInfoJsForm.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn') .click(function () { cmContractInfoJsForm.saveOrModify(); });
    $('#closeBtn') .click(function () { cmContractInfoJsForm.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
        projectId: { required: true, maxlength:50 },
        contractName: { required: true, maxlength:255 },
        contractType: { required: true, maxlength:50 },
        contractCode: { required: true, maxlength:255 },
        contractMoney: { required: true, maxlength:18 },
        partybUnit: { required: true, maxlength:50 },
        partyaUnit: { required: true, maxlength:50 },
        constructionPeriod: { common_check_positive_double_two: true, required: true, maxlength:255 },
        civilizedLand: { required: false, maxlength:50 },
        goalOfExcellence: { required: false, maxlength:50 },
        paymentMethod: { required: false, maxlength:50 },
        needAudit: { required: false, maxlength:50 },
        contractContent: { required: false, maxlength:2000 },
        startDate: { required: false, maxlength:19 },
        endDate: { required: false, maxlength:19 },
        signDate: { required: false, maxlength:19 },
        handleUser: { required: true, maxlength:50 },
        remark: { required: false, maxlength:16383 },
    }
});