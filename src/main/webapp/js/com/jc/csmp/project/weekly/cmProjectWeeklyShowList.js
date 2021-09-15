var cmProjectWeeklyShowListPanel = {};
cmProjectWeeklyShowListPanel.subState = false;

cmProjectWeeklyShowListPanel.attach = new AttachControl.AttachListControl({
    renderElement: 'attachTable',
    businessTable: 'cm_project_weekly'
});

cmProjectWeeklyShowListPanel.oTable = null;
cmProjectWeeklyShowListPanel.treeObj = null;
cmProjectWeeklyShowListPanel.tree = function () {
    function onBeforeClick(id, node){
        if(node.isChecked == 0 && node.pId != null){
            return false;
        }else{
            return true;
        }
    }

    function onClick(event, treeId, treeNode) {
        if (treeNode.id.indexOf('weekly_') > -1) {
            $('#weeklyDiv').show();
            $('#emptyDiv').hide();
            $('#weeklyId').val(treeNode.id.replace('weekly_', ''));
            cmProjectWeeklyShowListPanel.loadWeekly();
        } else {
            $.ajax({
                type: "GET", data: {projectId: treeNode.id}, dataType : "json",
                url : getRootPath() + "/project/weekly/getLastWeekly.action",
                success : function(data) {
                    if (data.code == 0) {
                        $('#weeklyDiv').show();
                        $('#emptyDiv').hide();
                        $('#weeklyId').val(data.data.id);
                        cmProjectWeeklyShowListPanel.loadWeekly(data.data.id);
                    } else {
                        $('#weeklyDiv').hide();
                        $('#emptyDiv').show();
                    }
                }
            });
        }
    }

    cmProjectWeeklyShowListPanel.treeObj = JCTree.ztree({
        container : 'treeDemo',
        expand : true,
        rootNode : true,
        url: getRootPath() + "/project/weekly/weeklyTree.action",
        isSearch: true,
        onClick: onClick,
        onBeforeClick : onBeforeClick,
        ready: function (data) {
            operatorTreeModule.searchTreeInit({treeObj: cmProjectWeeklyShowListPanel.treeObj, data: data});
        }
    });
};

cmProjectWeeklyShowListPanel.loadWeekly = function() {
    if ($('#weeklyId').val() == '') {
        $('#feedbackBtn').hide();
    } else {
        $('#feedbackBtn').show();
    }
    $('#entityForm #feedback').val("");
    $.ajax({
        type : "GET", data : {id: $('#weeklyId').val()}, dataType : "json",
        url : getRootPath() + "/project/weekly/get.action",
        success : function(data) {
            if (data) {
                hideErrorMessage();
                $("#entityForm").fill(data);
                $('#reportName').html(data.reportName);
                $('#attachList1').html("");
                cmProjectWeeklyShowListPanel.attach.initAttachOnView(data.id);
                cmProjectWeeklyShowListPanel.renderTable();
            }
        }
    });
};

cmProjectWeeklyShowListPanel.renderTable = function() {
    if (cmProjectWeeklyShowListPanel.oTable == null) {
        cmProjectWeeklyShowListPanel.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": 10,
            "sAjaxSource": getRootPath() + "/project/weekly/weeklyItemList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": [
                {mData: 'tableRowNo', bSortable: false},
                {mData: 'stageName', bSortable: false},
                {mData: 'planName', bSortable: false},
                {mData: 'finishRatio', bSortable: false}
            ],
            "fnServerParams": function ( aoData ) {
                getTableParameters(cmProjectWeeklyShowListPanel.oTable, aoData);
                aoData.push({"name": 'weeklyId', "value": $('#weeklyId').val()});
            },
            bPaginate: false,
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmProjectWeeklyShowListPanel.oTable.fnDraw();
    }
};

cmProjectWeeklyShowListPanel.saveFeedback = function() {
    if ($('#entityForm #feedback').val() == "") {
        msgBox.info({ type:"fail", content: '请输入反馈内容' });
        return;
    }
    if (cmProjectWeeklyShowListPanel.subState == true) {
        return;
    }
    cmProjectWeeklyShowListPanel.subState = true;
    jQuery.ajax({
        url: getRootPath() + "/project/weekly/feedback.action", type: 'POST', cache: false,
        data: {id: $('#entityForm #id').val(), feedback: $('#entityForm #feedback').val()},
        success : function(data) {
            cmProjectWeeklyShowListPanel.subState = false;
            $("#token").val(data.token);
            if(data.success == "true"){
                msgBox.tip({ type: "success", content: "反馈成功" });
            } else {
                msgBox.info({ type:"fail", content: data.errorMessage });
            }
        },
        error : function() {
            cmProjectWeeklyShowListPanel.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

$(document).ready(function(){
    cmProjectWeeklyShowListPanel.tree();
});