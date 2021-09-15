var projectBiddingJsForm = {};

projectBiddingJsForm.attach = new AttachControl.AttachListControl({
    renderElement: 'attachTable',
    titleWidth: 100,
    businessTable: 'cm_project_bidding'
});

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
projectBiddingJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    $('#saveBtn').show();
    if (config.operator == 'add') {
        projectBiddingJsForm.attach.initAttach(0);
        projectBiddingJsForm.operatorModal('show');
    } else if (config.operator == 'modify' || config.operator == 'look') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/project/bidding/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    if (data.projectApproval == "1") {
                        $('#projectApprovalAttachListTd').show();
                        $("[name='projectApprovalCheck']").attr("checked",'true');
                    }
                    if (config.operator == 'modify') {
                        projectBiddingJsForm.attach.initAttach(data.id);
                    } else {
                        $('#saveBtn').hide();
                        projectBiddingJsForm.attach.initAttachOnView(data.id);
                    }
                    projectBiddingJsForm.operatorModal('show');
                }
            }
        });
    }
};

/**
 * 验证表单
 * @returns {boolean}
 */
projectBiddingJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    }
    if ($('#entityForm #projectApproval').val() == "1") {
        if (liuAttachPool.getFiles(2) == "") {
            msgBox.info({ type:"fail", content: "请上传立项批文" });
            return false;
        }
    }
    return true;
};

/**
 * 保存或修改方法
 */
projectBiddingJsForm.saveOrModify = function () {
	if (projectBiddingJsForm.subState) {
	    return;
    }
	projectBiddingJsForm.subState = true;
	if (!projectBiddingJsForm.formValidate()) {
	    projectBiddingJsForm.subState = false;
	    return;
    }
	var url = getRootPath() + "/project/bidding/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action';
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile1", "value": liuAttachPool.getFiles(1)});
    formData.push({"name": "deleteAttachFile1", "value": liuAttachPool.getDeleteFiles(1)});
    formData.push({"name": "attachFile2", "value": liuAttachPool.getFiles(2)});
    formData.push({"name": "deleteAttachFile2", "value": liuAttachPool.getDeleteFiles(2)});
    jQuery.ajax({
        url : url, type : 'POST', cache: false, data : formData,
        success : function(data) {
            projectBiddingJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                projectBiddingJsForm.operatorModal('hide');
                projectBiddingJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            projectBiddingJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
projectBiddingJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

projectBiddingJsForm.projectChangeCallback = function (data) {
    $('#entityForm #projectId').val(data.id);
    $('#entityForm #projectName').val(data.projectName);
    if ($("#entityForm #id").val() == '') {
        $('#entityForm #projectContent').val(data.projectContent);
    }
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	$('#saveBtn').click(function () { projectBiddingJsForm.saveOrModify(); });
    $('#closeBtn').click(function () { projectBiddingJsForm.operatorModal('hide'); });

    $('#entityForm #projectName').click(function() {
        chooseProjectPanel.init(function(data) { projectBiddingJsForm.projectChangeCallback(data); });
    });

    $('#entityForm #projectApprovalCheck').click(function() {
        if (this.checked) {
            $('#entityForm #projectApproval').val("1");
            $('#projectApprovalAttachListTd').show();
        } else {
            $('#entityForm #projectApproval').val("0");
            $('#projectApprovalAttachListTd').hide();
        }
    });
});

/**
 * jquery.validate
 */
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		projectId: { required: true, maxlength:50 },
		biddingMethod: { required: true, maxlength:50 },
		maxPrice: { required: false, maxlength:18 },
		purchasingDemand: { required: false, maxlength:2000 },
		qualityRequirement: { required: false, maxlength:2000 },
		projectContent: { required: false, maxlength:16383 },
		contractPeriod: { required: false, maxlength:255 },
        buildLandLicence: { required: false, maxlength: 255 },
        buildProjectLicence: { required: false, maxlength: 255 },
    }
});