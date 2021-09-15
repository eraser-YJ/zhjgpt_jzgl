var deptJsList = {};
deptJsList.oTable = null;

deptJsList.oTableFnServerParams = function(aoData){
    getTableParameters(deptJsList.oTable, aoData);
    var query_deptName  = $('#searchDeptForm #query_deptName').val();
    if (query_deptName.length > 0) { aoData.push({"name": "name", "value": query_deptName}); }
    aoData.push({"name": "parentId", "value": "100031"});
};

deptJsList.oTableAoColumns = [
    {mData: 'code', sTitle: '部门编号', bSortable: false},
    {mData: 'name', sTitle: '部门名称', bSortable: false},
    {mData: function(source) {
            var select = "<a class=\"a-icon i-edit\"  href=\"#\"  onclick=\"deptJsList.selectItem('"+ source.id+ "','"+ source.name+ "')\">&nbsp;&nbsp;选择&nbsp;&nbsp;</a>";
            return "<div style='width:100%;text-align:center'>"+select+"</div>";
        }, sTitle: '操作', bSortable: false, sWidth: 80}
];

deptJsList.renderTable = function () {
    if (deptJsList.oTable == null) {
        deptJsList.oTable =  $('#gridDeptTable').dataTable( {
            "iDisplayLength": deptJsList.pageCount,
            "sAjaxSource": getRootPath() + "/doc/dept/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": deptJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                deptJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        deptJsList.oTable.fnDraw();
    }
};
deptJsList.selectItem = function(id,code){
    deptJsList.callback(id,code);
    $('#form-modal-Dept').modal('hide');
}
deptJsList.callback;
deptJsList.showDeptList = function(callback){
    deptJsList.callback = callback;
    $('#form-modal-Dept').modal('show');
}
deptJsList.closeBtnFun = function(){
    $('#form-modal-Dept').modal('hide');
}
$(document).ready(function(){
    deptJsList.pageCount = TabNub > 0 ? TabNub : 10;
    deptJsList.renderTable();
    $('#queryDeptBtn').click(deptJsList.renderTable);
    $('#closeDeptBtn').click(deptJsList.closeBtnFun);
});