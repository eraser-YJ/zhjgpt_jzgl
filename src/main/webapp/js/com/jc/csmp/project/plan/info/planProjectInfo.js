var projectListPanel = {};
projectListPanel.oTable = null;

projectListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(projectListPanel.oTable, aoData);
    var projectNumber = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({"name": "projectNumber", "value": projectNumber}); }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({"name": "projectName", "value": projectName}); }
    var region = $('#searchForm #region').val();
    if (region.length > 0) { aoData.push({name: 'region', value: region}); }
};

projectListPanel.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'region', sTitle: '所属区域', bSortable: false, sWidth: 100, mRender : function(mData, type, full) { return full.regionValue; }},
    {mData: 'projectType', sTitle: '项目类型', bSortable: false, sWidth: 120, mRender : function(mData, type, full) { return full.projectTypeValue; }},
    {mData: 'buildDeptId', sTitle: '建设单位', bSortable: false, mRender : function(mData, type, full) { return full.buildDeptIdValue; }},
    {mData: 'landArea', sTitle: '用地面积(㎡)', bSortable: false},
    {mData: 'investmentAmount', sTitle: '投资金额(万元)', bSortable: false},
    {mData: function(source) {
        return "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"projectListPanel.setPlanFunction('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('编制计划', 'fa-list') + "</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 60}
];

projectListPanel.renderTable = function () {
    if (projectListPanel.oTable == null) {
        projectListPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": projectListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/project/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectListPanel.oTable.fnDraw();
    }
};

projectListPanel.jumpSavePlanPage = function(projectId) {
    parent.JCF.LoadPage({url: '/project/plan/info/manage.action?projectId=' + projectId });
};

projectListPanel.setPlanFunction = function(projectId) {
    $.ajax({
        type: "POST", url: getRootPath() + "/project/plan/stage/checkPlan.action", data: {"projectId": projectId}, dataType: "json",
        success : function(data) {
            if (data.code == 0) {
                projectListPanel.jumpSavePlanPage(projectId);
            } else{
                msgBox.confirm({
                    content: "是否选择模板",
                    success: function(){
                        projectListPanel.chooseTemplate(projectId);
                    },
                    cancel: function() {
                        projectListPanel.jumpSavePlanPage(projectId);
                    }
                });
            }
        }
    });
};

projectListPanel.chooseTemplate = function(projectId) {
    $("#chooseModule").load(getRootPath() + "/project/plan/template/choose.action?projectId=" + projectId, null, function() {
        projectPlanChooseTemplateModule.init({callback: function (pid) {
            projectListPanel.jumpSavePlanPage(pid);
        }});
    });
};

projectListPanel.queryReset = function() {
    $('#searchForm')[0].reset();
};

$(document).ready(function(){
    projectListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    projectListPanel.renderTable();
    $('#queryBtn').click(projectListPanel.renderTable);
    $('#resetBtn').click(projectListPanel.queryReset);
});