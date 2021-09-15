var projectZlglInfoJsList = {};
projectZlglInfoJsList.oTable = null;

projectZlglInfoJsList.oTableFnServerParams = function (aoData) {
    getTableParameters(projectZlglInfoJsList.oTable, aoData);

    var nowProjectCode = $('#nowProjectCode').val();
    if (nowProjectCode.length > 0) {
        aoData.push({"name": "projectCode", "value": nowProjectCode});
    }
};

projectZlglInfoJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
    {mData: 'code', sTitle: '问题编号', bSortable: false},
    {
        mData: function (source) {
            if (source.create_date) {
                var crtTime = new Date(source.create_date);
                return dateFtt("yyyy-MM-dd hh:mm:ss", crtTime);
            } else {
                return "";
            }
        }, sTitle: '登记时间', bSortable: false
    },
    {mData: 'title', sTitle: '标题', bSortable: false},
    {mData: 'dept_ame', sTitle: '整改单位', bSortable: false},
    {
        mData: function (source) {
            var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"projectZlglInfoJsList.loadModuleView('" + source.id + "')\" role=\"button\">查看</a>";
            return edit;
        }, sTitle: '操作', bSortable: false, sWidth: 70
    }
];

projectZlglInfoJsList.renderTable = function () {
    if (projectZlglInfoJsList.oTable == null) {
        projectZlglInfoJsList.oTable = $('#gridZlglTable').dataTable({
            "iDisplayLength": projectZlglInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/mock/view/getList.action?disPath=zlgl&n=" + new Date().getTime(),
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectZlglInfoJsList.oTableAoColumns,
            "fnServerParams": function (aoData) {
                projectZlglInfoJsList.oTableFnServerParams(aoData);
            },
            aaSorting: [],
            aoColumnDefs: []
        });
    } else {
        projectZlglInfoJsList.oTable.fnDraw();
    }
};


projectZlglInfoJsList.loadModuleView = function (id) {
    $("#formDialogDisDiv").load(getRootPath() + "/mock/view/showPage.action?page=scs.questionForm&disPath=zlgl&id=" + id + "&n_=" + (new Date().getTime()), null, function () {

    });
};

$(document).ready(function () {
    projectZlglInfoJsList.pageCount = 5;
    projectZlglInfoJsList.renderTable();
});