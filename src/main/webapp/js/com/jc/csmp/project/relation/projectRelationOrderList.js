var projectRelationOrderJsList = {};
projectRelationOrderJsList.oTable = null;

projectRelationOrderJsList.oTableFnServerParams = function(aoData){
    getTableParameters(projectRelationOrderJsList.oTable, aoData);
    var projectNumber = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({ name: 'projectNumber', value: projectNumber }); }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({ name: 'projectName', value: projectName }); }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({ name: 'projectName', value: projectName }); }
    var signStatus = $('#searchForm #signStatus').val();
    if (signStatus.length > 0) { aoData.push({ name: 'signStatus', value: signStatus }); }
    var code = $('#searchForm #code').val();
    if (code.length > 0) { aoData.push({ name: 'code', value: code }); }
    var title = $('#searchForm #title').val();
    if (title.length > 0) { aoData.push({ name: 'title', value: title }); }
};

projectRelationOrderJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'code', sTitle: '工程联系单编号', bSortable: false},
    {mData: 'projectNumber', sTitle: '项目统一编码', bSortable: false},
    {mData: 'projectId', sTitle: '项目名称', bSortable: false, mRender : function(mData, type, full) { return full.projectName; }},
    {mData: 'title', sTitle: '工程联系单名称', bSortable: false},
    {mData: 'signerDept', sTitle: '接收单位', bSortable: false, mRender : function(mData, type, full) { return full.signerDeptValue; }},
    {mData: 'createDate', sTitle: '下发时间', bSortable: false, mRender : function(mData, type, full) { return full.createDate.split(' ')[0]; }},
    {mData: 'signStatus', sTitle: '签收状态', bSortable: false, mRender : function(mData, type, full) { return full.signStatusValue; }},
	{mData: function(source) {
        return "<a class=\"a-icon i-new\"  href=\"#myModal-edit\"  onclick=\"projectRelationOrderJsList.loadModuleForLook('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
	}, sTitle: '操作', bSortable: false, sWidth: 90}
];

projectRelationOrderJsList.renderTable = function () {
    if (projectRelationOrderJsList.oTable == null) {
        projectRelationOrderJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": projectRelationOrderJsList.pageCount,
            "sAjaxSource": getRootPath() + "/project/relation/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectRelationOrderJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectRelationOrderJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectRelationOrderJsList.oTable.fnDraw();
    }
};

projectRelationOrderJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

projectRelationOrderJsList.loadModuleForAdd = function (){
    $("#formModule").load(getRootPath() + "/project/relation/loadForm.action", null, function() {
        projectRelationOrderJsForm.init({title: '新增工程联系单', operator: 'add'});
    });
};

projectRelationOrderJsList.loadModuleForLook = function (id){
    $("#formModule").load(getRootPath() + "/project/relation/loadForm.action", null, function() {
        projectRelationOrderJsForm.init({title: '查看工程联系单', operator: 'look', id: id});
    });
};

$(document).ready(function(){
    projectRelationOrderJsList.pageCount = TabNub > 0 ? TabNub : 10;
    projectRelationOrderJsList.renderTable();
    $('#queryBtn').click(projectRelationOrderJsList.renderTable);
    $('#resetBtn').click(projectRelationOrderJsList.queryReset);
    $("#addBtn").click(projectRelationOrderJsList.loadModuleForAdd);
});