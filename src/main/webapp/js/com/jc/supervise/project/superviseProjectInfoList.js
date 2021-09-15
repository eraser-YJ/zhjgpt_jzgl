var superviseProjectInfoListPanel = {};
superviseProjectInfoListPanel.oTable = null;

superviseProjectInfoListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(superviseProjectInfoListPanel.oTable, aoData);
    var projectNumber = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({"name": "projectNumber", "value": projectNumber}); }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({"name": "projectName", "value": projectName}); }
};

superviseProjectInfoListPanel.oTableAoColumns = [
    {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: function(source) {
        return "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"superviseProjectInfoListPanel.loadDetail('" + source.dlh_data_id_ + "', '" + source.projectNumber + "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 60}
];

superviseProjectInfoListPanel.renderTable = function () {
    if (superviseProjectInfoListPanel.oTable == null) {
        superviseProjectInfoListPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": superviseProjectInfoListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/supervise/project/informationList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": superviseProjectInfoListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                superviseProjectInfoListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        superviseProjectInfoListPanel.oTable.fnDraw();
    }
};

superviseProjectInfoListPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

superviseProjectInfoListPanel.loadDetail = function(dataId, projectNumber) {
    $('#paramProjectNumber').val(projectNumber);
    $('.emptyContainer').hide();
    $('.lc-content').show();
    $('#pt_project_approval').removeClass('active');
    $('#pt_project_design').removeClass('active');
    $('#pt_company_projects_ztb').removeClass('active');
    $('#pt_company_projects_htba').removeClass('active');
    $('#pt_project_finish').removeClass('active');
    $('#pt_project_safe').removeClass('active');
    $('#pt_company_projects_sgxk').removeClass('active');
    $('#pt_project_quality').removeClass('active');
    $('#detailContainer').html('');
    superviseProjectInfoListPanel.loadFlow(projectNumber);
    resourceDetailModal.detail({dataId: dataId, sign: "pt_project_info", callback: function(content) { $('#detailContainer').html(content); }});
};

superviseProjectInfoListPanel.loadFlow = function(projectNumber) {
    $.ajax({
        url: getRootPath() + "/supervise/resource/flowDataCount.action",
        type : "GET", data : {projectNumber: projectNumber}, dataType : "json",
        success : function(data) {
            if (data.code == 0) {
                for (var key in data.data) {
                    if (data.data[key] > 0) {
                        try { $('#' + key).addClass('active'); } catch (e) {}
                    }
                }
            }
        }
    });
};

$(document).ready(function(){
    superviseProjectInfoListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    superviseProjectInfoListPanel.renderTable();
    $('#queryBtn').click(superviseProjectInfoListPanel.renderTable);
    $('#resetBtn').click(superviseProjectInfoListPanel.queryReset);
    $('.tab-box>div').hide();
    $('.tab-box>div').eq(0).show();
    $('.nav-tabs li').click(function(){
        $('.nav-tabs li').removeClass('tab-item-active');
        $(this).addClass('tab-item-active');
        var index=$(this).index();
        $('.tab-box>div').hide().eq(index).show();
    })
});

/********** 列表数据 ***************/
var flowListPanel = {};
flowListPanel.oTable = null;
flowListPanel.oTableAoColumns = [];
flowListPanel.resource = null;

flowListPanel.init = function() {
    flowListPanel.oTable = null;
    flowListPanel.oTableAoColumns = [{mData: 'no', sTitle: '序号', bSortable: false, sWidth: 60}];
    flowListPanel.resource = null;
    $('#listContent').html("");
    $('#listContent').html('<table class="table table-striped tab_height" id="listTable"></table>');
};

flowListPanel.search = function(resource) {
    flowListPanel.init();
    if ($('#' + resource).attr('class').indexOf('active') < 0) return;
    flowListPanel.resource = resource;
    $.ajax({
        url: getRootPath() + "/supervise/resource/resourceHeader.action", type: "GET", data : {sign: resource}, dataType : "json",
        success : function(data) {
            for (var i = 0; i < data.length; i++) {
                var row = data[i];
                if (row.listShow == 0) { continue; }
                flowListPanel.oTableAoColumns.push({mData: row.code, sTitle: row.title, bSortable: false});
            }
            flowListPanel.oTableAoColumns.push({mData: function(source) {
                return "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"flowListPanel.look('" + source.dlh_data_id_ + "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
            }, sTitle: '操作', bSortable: false, sWidth: 60});
            flowListPanel.renderTableResource();
            $('#list-modal').modal('show');
        }
    });
};

flowListPanel.renderTableResource = function() {
    if (flowListPanel.oTable == null) {
        flowListPanel.oTable = $('#listTable').dataTable( {
            "iDisplayLength": flowListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/supervise/resource/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": flowListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                getTableParameters(superviseProjectInfoListPanel.oTable, aoData);
                aoData.push({"name": "sign", "value": flowListPanel.resource});
                aoData.push({"name": "projectNumber", "value": $('#paramProjectNumber').val()});
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        flowListPanel.oTable.fnDraw();
    }
};

flowListPanel.look = function (value) {
    resourceDetailModal.detail({dataId: value, sign: flowListPanel.resource});
};