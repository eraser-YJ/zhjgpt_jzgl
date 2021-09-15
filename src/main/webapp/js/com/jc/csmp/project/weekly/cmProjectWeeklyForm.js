var cmcmProjectWeeklyItemForm = {};
cmcmProjectWeeklyItemForm.subState = false;

cmcmProjectWeeklyItemForm.attach = new AttachControl.AttachListControl({
    renderElement: 'attachTable',
    businessTable: 'cm_project_weekly'
});

cmcmProjectWeeklyItemForm.read = true;

cmcmProjectWeeklyItemForm.init = function(){
    $('#saveBtn').hide();
    $('#saveReleaseBtn').hide();
    $.ajax({
        type : "GET", data : {id: $('#entityForm #id').val()}, dataType : "json",
        url : getRootPath() + "/project/weekly/get.action",
        success : function(data) {
            if (data) {
                hideErrorMessage();
                $("#entityForm").fill(data);
                if (data.ifRelease == 0) {
                    //未发布，可以修改
                    $('#saveBtn').show();
                    $('#saveReleaseBtn').show();
                    cmcmProjectWeeklyItemForm.read = false;
                    cmcmProjectWeeklyItemForm.attach.initAttach(data.id);
                    $('#saveBtn').click(function () {cmcmProjectWeeklyItemForm.save(false); });
                    $('#saveReleaseBtn').click(function () {cmcmProjectWeeklyItemForm.save(true); });
                } else {
                    $('#feedbackTr').show();
                    $('input').attr('readonly','readonly');
                    $('textarea').attr('readonly','readonly');
                    cmcmProjectWeeklyItemForm.attach.initAttachOnView(data.id);
                }
                if (cmcmProjectWeeklyItemForm.read) {
                    $('#addItemBen').hide();
                }
                cmProjectWeeklyItemPanel.renderTable();
            }
        }
    });
};

cmcmProjectWeeklyItemForm.back = function () {
    parent.JCF.LoadPage({url: '/project/weekly/manage.action' });
};

cmcmProjectWeeklyItemForm.save = function(release) {
    if (cmcmProjectWeeklyItemForm.subState) {
        return;
    }
    cmcmProjectWeeklyItemForm.subState = true;
    if (!$("#entityForm").valid())  {
        cmcmProjectWeeklyItemForm.subState = false;
        return;
    }
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
    var successMessage = "保存成功";
    if (release == true) {
        //发布
        successMessage = "上报成功";
        formData.push({"name": "clickRelease", "value": 1});
    }
    var url = getRootPath() + "/project/weekly/" +  ($("#entityForm #id").val() != '' ? 'update' : 'save') + '.action';
    jQuery.ajax({
        url : url, type : 'POST', cache: false, data : formData,
        success : function(data) {
            cmcmProjectWeeklyItemForm.subState = false;
            $("#WeeklyToken").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: successMessage });
                cmcmProjectWeeklyItemForm.back();
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({ type:"fail", content: data.errorMessage });
                }
            }
        },
        error : function() {
            cmcmProjectWeeklyItemForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

$(document).ready(function(){
    cmcmProjectWeeklyItemForm.init();
    $('#addItemBen').click(cmProjectWeeklyItemPanel.choosePlan);
});

$("#entityForm").validate({
    ignore:'ignore',
    rules: {
        reportName: { required: true, maxlength: 255},
        reportRemark: { required: true, maxlength: 60000 }
    }
});

/*** 周报事项 **/
var cmProjectWeeklyItemPanel = {};
cmProjectWeeklyItemPanel.oTable = null;

cmProjectWeeklyItemPanel.choosePlan = function() {
    new ChooseCompany.CompanyDataTable({
        renderElement: 'chooseModule',
        treeUrl: getRootPath() + "/common/api/planStage.action?projectId=" + $('#entityForm #projectId').val(),
        tableUrl: getRootPath() + "/common/api/planList.action?projectId=" + $('#entityForm #projectId').val(),
        column: [
            {mData: 'planCode', sTitle: '计划编码', bSortable: false},
            {mData: 'planName', sTitle: '计划名称', bSortable: false},
            {mData: 'completionRatio', sTitle: '完成进度(%)', bSortable: false}
        ],
        callback: function(data){
            cmProjectWeeklyItemForm.itemInit(data.id);
        }
    });
};

cmProjectWeeklyItemPanel.renderTable = function() {
    if (cmProjectWeeklyItemPanel.oTable == null) {
        cmProjectWeeklyItemPanel.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": 10,
            "sAjaxSource": getRootPath() + "/project/weekly/weeklyItemList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": [
                {mData: 'tableRowNo', bSortable: false},
                {mData: 'stageName', bSortable: false},
                {mData: 'planName', bSortable: false},
                {mData: 'finishRatio', bSortable: false},
                {mData: function(source) {
                    var edit = "<a class=\"a-icon i-cemt\" href=\"javascript:void(0);\" onclick=\"cmProjectWeeklyItemPanel.update('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('修改', 'fa-edit2') + "</a>";
                    var del = "<a class=\"a-icon i-remove\" href=\"javascript:void(0);\" onclick=\"cmProjectWeeklyItemPanel.delete('"+ source.id+ "')\">" + finalTableBtnText('删除', 'fa-remove') + "</a>";
                    if (cmcmProjectWeeklyItemForm.read) {
                        return "";
                    }
                    return edit + del;
                }, bSortable: false}
            ],
            "fnServerParams": function ( aoData ) {
                getTableParameters(cmProjectWeeklyItemPanel.oTable, aoData);
                aoData.push({"name": 'weeklyId', "value": $('#entityForm #id').val()});
            },
            bPaginate: false,
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmProjectWeeklyItemPanel.oTable.fnDraw();
    }
};

cmProjectWeeklyItemPanel.update = function (id) {
    $.ajax({
        type : "GET", data : {id: id}, dataType : "json",
        url : getRootPath() + "/project/weekly/getItem.action",
        success : function(data) {
            if (data) {
                cmProjectWeeklyItemForm.clearItemForm();
                $("#weeklyItemForm").fill(data);
                $('#item-form-modal').modal('show');
            }
        }
    });
};

cmProjectWeeklyItemPanel.delete = function (id) {
    if (id == '') {
        msgBox.info({ content: $.i18n.prop("JC_SYS_061") });
        return;
    }
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function(){
            $.ajax({
                type : "POST", data : {ids: id}, dataType : "json",
                url : getRootPath() + "/project/weekly/deleteItem.action",
                success : function(data) {
                    if (data) {
                        cmProjectWeeklyItemPanel.renderTable();
                    }
                }
            });
        }
    });
};

/**周报事项保存***/
var cmProjectWeeklyItemForm = {};
cmProjectWeeklyItemForm.subState = false;

cmProjectWeeklyItemForm.clearItemForm = function() {
    $('#weeklyItemForm #id').val("");
    $('#weeklyItemForm #planId').val("");
    $('#weeklyItemForm #weeklyId').val("");
    $('#weeklyItemForm #finishRatio').val("");
    $('#weeklyItemForm #planCode').val("");
    $('#weeklyItemForm #planName').val("");
    $('#weeklyItemForm #planStartDate').val("");
    $('#weeklyItemForm #planEndDate').val("");
    $('#weeklyItemForm #outputValue').val("");
    $('#weeklyItemForm #remark').val("");
};

cmProjectWeeklyItemForm.itemInit = function(planId) {
    $.ajax({
        type : "GET", data : {id: planId}, dataType : "json",
        url : getRootPath() + "/project/plan/info/get.action",
        success : function(data) {
            if (data) {
                cmProjectWeeklyItemForm.clearItemForm();
                $('#weeklyItemForm #id').val("");
                $('#weeklyItemForm #planId').val(data.id);
                $('#weeklyItemForm #weeklyId').val($('#entityForm #id').val());
                $('#weeklyItemForm #finishRatio').val(data.completionRatio == null ? 0 : data.completionRatio);
                $('#weeklyItemForm #planCode').val(data.planCode);
                $('#weeklyItemForm #planName').val(data.planName);
                $('#weeklyItemForm #planStartDate').val(data.planStartDate);
                $('#weeklyItemForm #planEndDate').val(data.planEndDate);
                $('#weeklyItemForm #outputValue').val(data.outputValue);
                $('#item-form-modal').modal('show');
            }
        }
    });
};

cmProjectWeeklyItemForm.saveOrUpdate = function() {
    if (cmProjectWeeklyItemForm.subState) {
        return;
    }
    cmProjectWeeklyItemForm.subState = true;
    if (!$("#weeklyItemForm").valid()) {
        cmProjectWeeklyItemForm.subState = false;
        return;
    }
    jQuery.ajax({
        url: getRootPath() + "/project/weekly/saveOrUpdateItem.action",
        type: 'POST', cache: false, data : $("#weeklyItemForm").serializeArray(),
        success : function(data) {
            cmProjectWeeklyItemForm.subState = false;
            if (data.code == 0) {
                msgBox.tip({ type: "success", content: data.msg });
                $('#item-form-modal').modal('hide');
                cmProjectWeeklyItemForm.clearItemForm();
                cmProjectWeeklyItemPanel.renderTable();
            } else {
                msgBox.tip({ type:"fail", content: data.msg });
            }
        },
        error : function() {
            cmProjectWeeklyItemForm.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**验证比例*/
jQuery.validator.addMethod("checkCompletionRatio", function (value, element) {
    if (value == "") {
        return true;
    }
    if (parseInt(value) > 100) {
        $(element).data('error-msg', '已完进度不能大于100');
        return false;
    }
    return true;
}, function(params, element) { return $(element).data('error-msg'); });

$("#weeklyItemForm").validate({
    ignore:'ignore',
    rules: {
        completionRatio: { required: true, common_check_positive_integer_can_zore: true, checkCompletionRatio: true },
        remark: { maxlength: 60000 }
    }
});

