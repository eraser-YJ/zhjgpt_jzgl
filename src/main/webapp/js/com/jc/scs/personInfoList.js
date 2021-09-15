var projectPersonInfoJsList = {};
projectPersonInfoJsList.oTable = null;

projectPersonInfoJsList.oTableFnServerParams = function(aoData){
    getTableParameters(projectPersonInfoJsList.oTable, aoData);

    var nowProjectCode  = $('#nowProjectCode').val();
    if (nowProjectCode.length > 0) { aoData.push({"name": "projectCode", "value": nowProjectCode}); }
};
projectPersonInfoJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
    {mData: 'person_name', sTitle: '姓名', bSortable: false},
    {mData: 'person_cert_num', sTitle: '身份证号', bSortable: false},
    {mData: 'person_sex_value', sTitle: '性别', bSortable: false},
    {mData: 'person_nation_value', sTitle: '民族', bSortable: false},
    {mData: 'reg_type', sTitle: '注册类型', bSortable: false},
    {mData: 'reg_num', sTitle: '注册证书号', bSortable: false},
    {mData: 'person_tel', sTitle: '联系电话', bSortable: false},
    {mData: function(source) {
            var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"projectPersonInfoJsList.loadModuleView('"+ source.id+ "')\" role=\"button\">查看</a>";
            return edit;
        }, sTitle: '操作', bSortable: false, sWidth: 70}
];

projectPersonInfoJsList.renderTable = function () {
    if (projectPersonInfoJsList.oTable == null) {
        projectPersonInfoJsList.oTable =  $('#gridPersonTable').dataTable( {
            "iDisplayLength": projectPersonInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/mock/view/getList.action?disPath=cyry",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectPersonInfoJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectPersonInfoJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectPersonInfoJsList.oTable.fnDraw();
    }
};


projectPersonInfoJsList.loadModuleView = function (id){
    $("#formDialogDisDiv").load(getRootPath() + "/mock/view/showPage.action?page=scs.cyryForm&disPath=cyry&id=" + id + "&n_=" + (new Date().getTime()), null, function () {

    });
};

$(document).ready(function(){
    projectPersonInfoJsList.pageCount = 5;
    projectPersonInfoJsList.renderTable();
});