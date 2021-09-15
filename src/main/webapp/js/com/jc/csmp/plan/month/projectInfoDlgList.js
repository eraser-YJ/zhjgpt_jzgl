var ProjectJsList = {};
ProjectJsList.oTable = null;

ProjectJsList.oTableFnServerParams = function(aoData){
    getTableParameters(ProjectJsList.oTable, aoData);
    var query_projectNumber  = $('#searchProjectForm #query_projectNumber').val();
    if (query_projectNumber.length > 0) { aoData.push({"name": "projectNumber", "value": query_projectNumber}); }
    var query_projectName  = $('#searchProjectForm #query_projectName').val();
    if (query_projectName.length > 0) { aoData.push({"name": "projectName", "value": query_projectName}); }
};

ProjectJsList.oTableAoColumns = [
    {mData: 'projectNumber', sTitle: '项目编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: function(source) {
            var select = "<a class=\"a-icon i-edit\"  href=\"#\"  onclick=\"ProjectJsList.selectItem('"+ source.projectNumber+ "','"+ source.projectName+ "')\">&nbsp;&nbsp;选择&nbsp;&nbsp;</a>";
            return "<div style='width:100%;text-align:center'>"+select+"</div>";
        }, sTitle: '操作', bSortable: false, sWidth: 80}
];

ProjectJsList.renderTable = function () {
    if (ProjectJsList.oTable == null) {
        ProjectJsList.oTable =  $('#gridProjectTable').dataTable( {
            "iDisplayLength": ProjectJsList.pageCount,
            "sAjaxSource": getRootPath() + "/plan/projectMonthPlan/manageProjectList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": ProjectJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                ProjectJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        ProjectJsList.oTable.fnDraw();
    }
};
ProjectJsList.selectItem = function(code,name){
    var msg = ProjectJsList.callback(code,name);
    if(msg == ''){
        $('#form-modal-Project').modal('hide');
    } else {
        msgBox.info({type: "fail", content: "同一申请内项目编码不能重复,请重新选择"});
    }

}
ProjectJsList.callback;
ProjectJsList.showProjectList = function(callback){
    ProjectJsList.callback = callback;
    $('#form-modal-Project').modal('show');
}
ProjectJsList.closeBtnFun = function(){
    $('#form-modal-Project').modal('hide');
}
$(document).ready(function(){
    ProjectJsList.pageCount = 5;
    ProjectJsList.renderTable();
    $('#queryProjectBtn').click(ProjectJsList.renderTable);
    $('#closeProjectBtn').click(ProjectJsList.closeBtnFun);
});