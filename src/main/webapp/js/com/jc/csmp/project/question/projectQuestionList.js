var projectQuestionListPanel = {};
projectQuestionListPanel.oTable = null;

projectQuestionListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(projectQuestionListPanel.oTable, aoData);
    var projectNumber = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) {
        if ($('#searchForm #questionType').val() == 'scene') {
            $('#printBtn').show();
        }
        aoData.push({name: "projectNumber", value: projectNumber});
    } else {
        $('#printBtn').hide();
    }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({name: "projectName", value: projectName}); }
    var createDateBegin = $('#searchForm #createDateBegin').val();
    if (createDateBegin.length > 0) { aoData.push({name: "createDateBegin", value: createDateBegin}); }
    var createDateEnd = $('#searchForm #createDateEnd').val();
    if (createDateEnd.length > 0) { aoData.push({name: "createDateEnd", value: createDateEnd}); }
    var auditStatus = $('#searchForm #auditStatus').val();
    if (auditStatus.length > 0) { aoData.push({name: "auditStatus", value: auditStatus}); }
    aoData.push({name: "questionType", value: $('#searchForm #questionType').val()});
    var code = $('#searchForm #code').val();
    if (code.length > 0) { aoData.push({name: "code", value: code}); }
    var createUser = $('#searchForm #createUser').val();
    if (createUser.length > 0) { aoData.push({name: "createUser", value: createUser}); }
};

projectQuestionListPanel.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'code', sTitle: '问题编号', bSortable: false, sWidth: 190},
    {mData: 'createDateFormat', sTitle: '登记时间', bSortable: false, sWidth: 110},
    {mData: 'projectNumber', sTitle: '项目统一编码', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'title', sTitle: '标题', bSortable: false, sWidth: 300},
    //{mData: 'rectificationCompany', sTitle: '整改单位', bSortable: false, mRender : function(mData, type, full) { return full.rectificationCompanyValue; }},
    {mData: 'auditStatus', sTitle: '审核状态', bSortable: false, mRender : function(mData, type, full) { return full.auditStatusValue; }, sWidth: 110},
	{mData: function(source) { return oTableSetButtons(source); }, sTitle: '操作', bSortable: false, sWidth: caozuoWidth}
];

projectQuestionListPanel.renderTable = function () {
    if (projectQuestionListPanel.oTable == null) {
        projectQuestionListPanel.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": projectQuestionListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/project/question/searchList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectQuestionListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectQuestionListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectQuestionListPanel.oTable.fnDraw();
    }
};

projectQuestionListPanel.loadModuleForLook = function (id, questionType){
    parent.JCF.LoadPage({url: '/project/question/look.action?id=' + id + '&questionType=' + questionType });
};

projectQuestionListPanel.loadModuleForUpdate = function (id, questionType){
    $("#formModule").load(getRootPath() + "/project/question/loadModify.action?questionType=" + questionType, null, function() {
        projectQuestionModifyPanel.init({title: '查看问题单', id: id});
    });
};

projectQuestionListPanel.delete = function(id) {
    if (id == undefined || id == null || id == "") {
        msgBox.info({ content: $.i18n.prop("JC_SYS_061") });
        return;
    }
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function(){
            $.ajax({
                type: "POST", url: getRootPath() + "/project/question/deleteByIds.action", data: {"id": id}, dataType: "json",
                success : function(data) {
                    if (data.success == "true") {
                        msgBox.tip({ type:"success", content: data.successMessage });
                    } else {
                        msgBox.info({ content: data.errorMessage });
                    }
                    projectQuestionListPanel.renderTable();
                }
            });
        }
    });
};

projectQuestionListPanel.queryReset = function(){
    $('#searchForm')[0].reset();
    $('#printBtn').hide();
};

$(document).ready(function(){
    projectQuestionListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    projectQuestionListPanel.renderTable();
    $('#queryBtn').click(projectQuestionListPanel.renderTable);
    $('#resetBtn').click(projectQuestionListPanel.queryReset);
    var questionType  =$('#searchForm #questionType').val();
    if (questionType == 'quality') {
        $('#sectionTitleOne').html("质量问题管理 > ");
        $('#sectionTitleTwo').html("质量问题查询");
        if ($('#searchForm #createUser').val() != "") {
            $('#sectionTitleTwo').html("质量问题登记");
            $('#addBtn').show();
            $('#addBtn').click(function () {
                JCFF.loadPage({url: "/instance/toStartProcess.action?definitionKey_=qualityQuestion"});
            });
        }
    } else if (questionType == "scene") {
        $('#sectionTitleOne').html("现场问题管理 > ");
        $('#sectionTitleTwo').html("现场问题查询");
        if ($('#searchForm #createUser').val() != "") {
            $('#sectionTitleTwo').html("现场问题登记");
            $('#addBtn').show();
            $('#addBtn').click(function () {
                JCFF.loadPage({url: "/instance/toStartProcess.action?definitionKey_=sceneQuestion"});
            });
        }
    } else if (questionType == "safe") {
        $('#sectionTitleOne').html("安全问题管理 > ");
        $('#sectionTitleTwo').html("安全问题查询");
        if ($('#searchForm #createUser').val() != "") {
            $('#sectionTitleTwo').html("安全问题登记");
            $('#addBtn').show();
            $('#addBtn').click(function () {
                JCFF.loadPage({url: "/instance/toStartProcess.action?definitionKey_=safeQuestion"});
            });
        }
    }
    $('#printBtn').click(function () { projectQuestion.print($('#searchForm #projectNumber').val(), $('#searchForm #questionType').val()); });
});