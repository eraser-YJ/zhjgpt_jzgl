var projectRelationOrderJsForm = {};

projectRelationOrderJsForm.deptTree = null;

projectRelationOrderJsForm.receiveDeptTree = JCTree.init({
    container: "receiverDeptFormDiv",
    controlId: "receiverDeptFormDiv-receiverDept",
    isCheckOrRadio: true,
    isPersonOrOrg: false,
    urls: {
        org: getRootPath() + "/common/api/deptTree.action"
    }
});

projectRelationOrderJsForm.attach = new AttachControl.AttachListControl({
    renderElement: 'attachTable',
    businessTable: 'cm_project_relation_order',
    titleWidth: 100
});

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
projectRelationOrderJsForm.init = function(config) {
    projectRelationOrderJsForm.currentFormData = null;
    $('#entityFormTitle').html(config.title);
    $("#entityForm #id").val('');
    $('#saveBtn').show();
    if (config.operator == 'add') {
        projectRelationOrderJsForm.registerProjectNameClick();
        projectRelationOrderJsForm.attach.initAttach(0);
        projectRelationOrderJsForm.operatorModal('show');
    } else if (config.operator == 'modify' || config.operator == 'look') {
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/project/relation/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityForm").fill(data);
                    projectRelationOrderJsForm.receiveDeptTree.setData(data.receiverDeptValue);
                    if (config.operator == 'modify') {
                        if (data.signStatus != '1') {
                            projectRelationOrderJsForm.registerProjectNameClick();
                            projectRelationOrderJsForm.attach.initAttach(data.id);
                        } else {
                            $('#saveBtn').hide();
                            projectRelationOrderJsForm.attach.initAttachOnView(data.id);
                        }
                    } else if (config.operator == 'look') {
                        $('#saveBtn').hide();
                        projectRelationOrderJsForm.attach.initAttachOnView(data.id);
                        if (config.callback != undefined && config.callback != null && data.signStatus != '1') {
                            config.callback(data.id);
                        }
                        relationPage.init({data: data, btnId: 'printBtn'});
                    }
                    projectRelationOrderJsForm.loadTree(data.projectId);
                    setTimeout(function() {
                        if (data.signerDept && data.signerDeptValue) {
                            projectRelationOrderJsForm.deptTree.setData({id: data.signerDept, text: data.signerDeptValue });
                        }
                    }, 1000);
                    projectRelationOrderJsForm.operatorModal('show');
                }
            }
        });
    }
};

projectRelationOrderJsForm.registerProjectNameClick = function() {
    $('#entityForm #projectName').click(function() {
        chooseProjectPanel.init(function(data) {
            $('#entityForm #projectId').val(data.id);
            $('#entityForm #projectName').val(data.projectName);
            projectRelationOrderJsForm.loadTree(data.id);
        });
    });
};

projectRelationOrderJsForm.loadTree = function(projectId) {
    $('#signerDeptFormDiv').html('');
    projectRelationOrderJsForm.deptTree = null;
    projectRelationOrderJsForm.deptTree = JCTree.init({
        container: "signerDeptFormDiv",
        controlId: "signerDeptFormDiv-signerDept",
        isCheckOrRadio:false,
        isPersonOrOrg:false,
        urls: {
            org: getRootPath() + "/project/person/personTreeByProjectId.action?projectId=" + projectId
        }
    });
};

/**
 * 验证表单
 * @returns {boolean}
 */
projectRelationOrderJsForm.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    } 
    return true;
};

/**
 * 保存或修改方法
 */
projectRelationOrderJsForm.saveOrModify = function () {
    console.log($("#entityForm").serializeArray());
	if (projectRelationOrderJsForm.subState) {
	    return;
    }
	projectRelationOrderJsForm.subState = true;
	if (!projectRelationOrderJsForm.formValidate()) {
	    projectRelationOrderJsForm.subState = false;
	    return;
    }
	var url = getRootPath() + "/project/relation/" +  ($("#id").val() != '' ? 'update' : 'save') + '.action';
	var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getAllFiles()});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: formData,
        success : function(data) {
            projectRelationOrderJsForm.subState = false;
             $("#token").val(data.token); 
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                projectRelationOrderJsForm.operatorModal('hide');
                projectRelationOrderJsList.renderTable();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            projectRelationOrderJsForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
projectRelationOrderJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn').click(function () { projectRelationOrderJsForm.saveOrModify(); });
    $('#closeBtn').click(function () { projectRelationOrderJsForm.operatorModal('hide'); });
});

/**
 * jquery.validate
 */
 $("#entityForm").validate({ 
	ignore:'ignore',
	rules: {
		projectId: { required: true, maxlength:50 },
		signerDept: { validSelect2: 'signerDeptFormDiv', maxlength:255 },
		title: { required: true, maxlength:255 },
		content: { required: true, maxlength:16383 },
    }
});