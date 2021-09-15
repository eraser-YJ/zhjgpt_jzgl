var cmProjectInfoFormPanel = {};
cmProjectInfoFormPanel.subState = false;

cmProjectInfoFormPanel.attach = new AttachControl.AttachListControl({
    renderElement: 'attachTable',
    businessTable: 'cm_project_info'
});

/**监管单位*/
superviseDeptIdJcTree = JCTree.init({
    container: "superviseDeptIdFormDiv",
    controlId: "superviseDeptIdFormDiv-superviseDeptId",
    isCheckOrRadio:false,
    isPersonOrOrg:false,
    urls: {
        org: getRootPath() + "/common/api/deptTree.action?rootCode=GOVERNMENT"
    }
});

/**国标行业下拉框*/
projectTradeJcTree = JCTree.init({
    container: "projectTradeFormDiv",
    controlId: "projectTradeFormDiv-projectTrade",
    isCheckOrRadio:false,
    isPersonOrOrg:false,
    urls: {
        org: getRootPath() + "/custom/dic/queryTree.action?dataType=projectTrade"
    }
});

/**用地性质下拉框*/
landNatureJcTree = JCTree.init({
    container: "landNatureFormDiv",
    controlId: "landNatureFormDiv-landNature",
    isCheckOrRadio:false,
    isPersonOrOrg:false,
    urls: {
        org: getRootPath() + "/custom/dic/queryTree.action?dataType=landNature"
    }
});

/**
 * 验证表单
 * @returns {boolean}
 */
cmProjectInfoFormPanel.formValidate = function() {
    if (!$("#entityForm").valid()) {
        return false;
    }
    return cmProjectInfoFormPanel.attach.validate();
};

/**
 * 新增项目
 */
cmProjectInfoFormPanel.loadAdd = function() {
    $('#entityTitle').html("项目新增");
    $('#headerTitle').html("项目新增");
    cmProjectInfoFormPanel.attach.initAttach(0);
    if ($('#resourceData').val() != "" && $('#resourceData').val() != 'null') {
        //从初始化带过来的数据
        var resourceData = JSON.parse($('#resourceData').val());
        console.log(resourceData);
        $('#entityForm #projectName').val(resourceData.projectName);
        var approvalDate = resourceData.approvalDate;
        if (approvalDate != "" && approvalDate.indexOf(' ') > -1) {
            approvalDate = approvalDate.split(' ')[0];
        }
        $('#entityForm #projectApprovalDate').val(approvalDate);
        $('#entityForm #projectNumber').val(resourceData.projectNumber);
        $('#entityForm #approvalNumber').val(resourceData.approvalNumber);
        $('#entityForm #planAmount').val(resourceData.projectMoney);
        $('#entityForm #investmentAmount').val(resourceData.projectMoney);
        $('#entityForm #projectType').val(resourceData.projectType);
        $('#entityForm #projectAddress').val(resourceData.projectAddress);
        $('#entityForm #region').val(resourceData.project_area);
        $('#entityForm #buildType').val(resourceData.build_type);
        $('#entityForm #buildArea').val(resourceData.buildArea);
        $('#entityForm #resourceDataId').val($('#paramResourceDataId').val());

    }
    if ($('#canBandBuild').val() == 'true') {
        $('#entityForm #buildDeptIdValue').val($('#loginUserDeptName').val());
        $('#entityForm #buildDeptId').val($('#loginUserDeptId').val());
    }
    $('#buildDeptChangeBtn').click(function () {
        new ChooseCompany.CompanyDataTable({
            renderElement: 'chooseCompanyModule',
            callback: function(data){
                $('#entityForm #buildDeptIdValue').val(data.name);
                $('#entityForm #buildDeptId').val(data.id);
            }
        });
    });
};

/**
 * 修该项目
 */
cmProjectInfoFormPanel.loadModify = function(id) {
    $('#entityTitle').html("项目修改");
    $('#headerTitle').html("项目修改");
    $.ajax({
        type: "GET", data: {id: id}, dataType: "json", url: getRootPath() + "/project/info/get.action",
        success : function(data) {
            if (data) {
                $("#entityForm").fill(data);
                if (data.superviseDeptId && data.superviseDeptIdValue) {
                    superviseDeptIdJcTree.setData({"id": data.superviseDeptId, "text": data.superviseDeptIdValue});
                }
                if (data.projectTrade && data.projectTradeValue) {
                    projectTradeJcTree.setData({"id": data.projectTrade, "text": data.projectTradeValue});
                }
                if (data.landNature && data.landNatureValue) {
                    landNatureJcTree.setData({"id": data.landNature, "text": data.landNatureValue});
                }
                cmProjectInfoFormPanel.attach.initAttach(id);
                $('#buildDeptChangeBtn').click(function () {
                    new ChooseCompany.CompanyDataTable({
                        column: [{mData: 'name', sTitle: '企业名称', bSortable: false}],
                        renderElement: 'chooseCompanyModule',
                        callback: function(data){
                            $('#entityForm #buildDeptIdValue').val(data.name);
                            $('#entityForm #buildDeptId').val(data.id);
                        }
                    });
                });
            }
        }
    });
};

/**
 * 查看项目
 */
cmProjectInfoFormPanel.loadLook = function(id) {
    $('#entityTitle').html("项目查看");
    $('#headerTitle').html("项目查看");
    $('#saveBtn').hide();
    $.ajax({
        type: "GET", data: {id: id}, dataType: "json", url: getRootPath() + "/project/info/get.action",
        success : function(data) {
            if (data) {
                $("#entityForm").fill(data);
                if (data.superviseDeptId && data.superviseDeptIdValue) {
                    superviseDeptIdJcTree.setData({"id": data.superviseDeptId, "text": data.superviseDeptIdValue});
                }
                if (data.projectTrade && data.projectTradeValue) {
                    projectTradeJcTree.setData({"id": data.projectTrade, "text": data.projectTradeValue});
                }
                if (data.landNature && data.landNatureValue) {
                    landNatureJcTree.setData({"id": data.landNature, "text": data.landNatureValue});
                }
                cmProjectInfoFormPanel.attach.initAttachOnView(id);
                cmProjectInfoFormPanel.loadPerson(id);
            }
        }
    });
};

cmProjectInfoFormPanel.personOTable = null;

cmProjectInfoFormPanel.loadPerson = function(projectId) {
    $('#personDiv').show();
    if (cmProjectInfoFormPanel.personOTable == null) {
        cmProjectInfoFormPanel.personOTable =  $('#personTable').dataTable( {
            "iDisplayLength": 100000,
            "sAjaxSource": getRootPath() + "/project/person/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": [
                {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
                {mData: 'partakeTypeValue', sTitle: '责任主体', bSortable: false},
                {mData: 'companyName', sTitle: '单位名称', bSortable: false},
                {mData: 'leader', sTitle: '负责人', bSortable: false},
                {mData: 'phone', sTitle: '负责人电话', bSortable: false}
            ],
            "fnServerParams": function ( aoData ) {
                getTableParameters(cmProjectInfoFormPanel.personOTable, aoData);
                aoData.push({name: "projectId", value: projectId});
                aoData.push({name: "canShow", value: '1'});
            },
            aaSorting:[],
            pagination: false,
            aoColumnDefs: []
        });
    } else {
        cmProjectInfoFormPanel.personOTable.fnDraw();
    }
};

/**
 * 保存或修改方法
 */
cmProjectInfoFormPanel.saveOrModify = function () {
    if ($('#entityForm #projectTradeFormDiv-projectTrade').val() == undefined || $('#entityForm #projectTradeFormDiv-projectTrade').val() == '0') {
        msgBox.tip({ type: "fail", content: '请选择国标行业' });
        return;
    }
    if ($('#entityForm #landNatureFormDiv-landNature').val() == undefined || $('#entityForm #landNatureFormDiv-landNature').val() == '0') {
        msgBox.tip({ type: "fail", content: '请选择用地性质' });
        return;
    }
	if (cmProjectInfoFormPanel.subState) {
	    return;
    }
	cmProjectInfoFormPanel.subState = true;
	if (!cmProjectInfoFormPanel.formValidate()) {
	    cmProjectInfoFormPanel.subState = false;
	    return;
    }

    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getAllFiles()});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
	var url = getRootPath() + "/project/info/" + ($("#entityForm #id").val() != '' ? 'update' : 'save') + '.action';
    jQuery.ajax({
        url: url, type: 'POST', cache: false, data: formData,
        success : function(data) {
            cmProjectInfoFormPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: data.successMessage });
                cmProjectInfoFormPanel.jumpList();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            cmProjectInfoFormPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

cmProjectInfoFormPanel.jumpList = function() {
    parent.JCF.LoadPage({url: '/project/info/manage.action' });
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
    $('#saveBtn').click(function () { cmProjectInfoFormPanel.saveOrModify(); });
    $('#closeBtn').click(function () { cmProjectInfoFormPanel.jumpList(); });
    if ($('#entityForm #operator').val() == 'add') {
        cmProjectInfoFormPanel.loadAdd();
    } else if ($('#entityForm #operator').val() == 'modify') {
        cmProjectInfoFormPanel.loadModify($('#entityForm #id').val());
    } else if ($('#entityForm #operator').val() == 'look') {
        cmProjectInfoFormPanel.loadLook($('#entityForm #id').val());
    }
});

/**
 * jquery.validate
 */
$("#entityForm").validate({
	ignore:'ignore',
	rules: {
        approvalNumber: {required: true},
        projectNumber: {
            required: true,
            maxlength:255,
            remote : {
                url : getRootPath() + "/project/info/checkProjectNumber.action",
                type: "post", dataType: "json",
                data: {
                    projectNumber: function() {
                        return $("#entityForm #projectNumber").val();
                    },
                    id: function () {
                        return $("#entityForm #id").val();
                    }
                }
            }
        },
        projectType: { required: true },
        buildType: { required: true },
        projectName: { required: true, maxlength:255 },
        region: { required: true },
        projectAddress: { maxlength:255 },
        planAmount: { common_check_positive_double_two: true },
        investmentAmount: { common_check_positive_double_two: true },
        buildArea: { common_check_positive_double_two: true },
        landArea: { common_check_positive_double_two: true },
        superviseDeptId: {validSelect2: 'superviseDeptIdFormDiv', maxlength: 50},
        buildDeptId: {required: true, maxlength: 50},
        projectTrade: { validSelect2: 'projectTradeFormDiv', maxlength: 50 },
        landNature: { validSelect2: 'landNatureFormDiv', maxlength: 50 },
        projectContent: { maxlength: 60000 },
        remark: { maxlength: 60000 }
    },
    messages:{
        creditCode: {
            remote: "项目统一编号已存在"
        }
    }
});