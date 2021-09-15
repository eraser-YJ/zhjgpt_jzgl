var projectQuestionListPanel = {};
projectQuestionListPanel.oTable = null;

projectQuestionListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(projectQuestionListPanel.oTable, aoData);
    aoData.push({name: "projectId", value: getUrlParam("id")});
    var createDateBegin = $('#searchForm #createDateBegin').val();
    if (createDateBegin.length > 0) { aoData.push({name: "createDateBegin", value: createDateBegin}); }
    var createDateEnd = $('#searchForm #createDateEnd').val();
    if (createDateEnd.length > 0) { aoData.push({name: "createDateEnd", value: createDateEnd}); }
    var auditStatus = $('#searchForm #auditStatus').val();
    if (auditStatus.length > 0) { aoData.push({name: "auditStatus", value: auditStatus}); }
    var code = $('#searchForm #code').val();
    if (code.length > 0) { aoData.push({name: "code", value: code}); }
};

projectQuestionListPanel.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'code', sTitle: '问题编号', bSortable: false, sWidth: 190},
    {mData: 'createDateFormat', sTitle: '登记时间', bSortable: false, sWidth: 110},
    {mData: 'projectNumber', sTitle: '项目统一编码', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'questionType', sTitle: '问题类型', bSortable: false, mRender : function(mData, type, full) {
        var value = '';
        if (full.questionType == 'scene') { value = '现场问题'; }
        else if (full.questionType == 'safe') { value = '安全问题'; }
        else if (full.questionType == 'quality') { value = '质量问题'; }
        return value;
    }},
    {mData: 'title', sTitle: '标题', bSortable: false, sWidth: 300},
    //{mData: 'rectificationCompany', sTitle: '整改单位', bSortable: false, mRender : function(mData, type, full) { return full.rectificationCompanyValue; }},
    {mData: 'auditStatus', sTitle: '审核状态', bSortable: false, mRender : function(mData, type, full) { return full.auditStatusValue; }, sWidth: 110},
	{mData: function(source) { return oTableSetButtons(source); }, sTitle: '操作', bSortable: false, sWidth: 200}
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
});