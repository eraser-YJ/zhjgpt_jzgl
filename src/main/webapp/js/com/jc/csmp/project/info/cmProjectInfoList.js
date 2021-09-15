var cmProjectInfoListPanel = {};
cmProjectInfoListPanel.oTable = null;
//资源选择窗口对象
cmProjectInfoListPanel.resourceWindowObject = null;

cmProjectInfoListPanel.oTableFnServerParams = function(aoData){
    getTableParameters(cmProjectInfoListPanel.oTable, aoData);
    var projectNumber = $('#searchForm #projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({"name": "projectNumber", "value": projectNumber}); }
    var projectName = $('#searchForm #projectName').val();
    if (projectName.length > 0) { aoData.push({"name": "projectName", "value": projectName}); }
    var region = $('#searchForm #region').val();
    if (region.length > 0) { aoData.push({name: 'region', value: region}); }
    var projectType = $('#searchForm #projectType').val();
    if (projectType.length > 0) { aoData.push({name: 'projectType', value: projectType}); }
};

cmProjectInfoListPanel.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'region', sTitle: '所属区域', bSortable: false, sWidth: 100, mRender : function(mData, type, full) { return full.regionValue; }},
    {mData: 'projectType', sTitle: '项目类型', bSortable: false, sWidth: 120, mRender : function(mData, type, full) { return full.projectTypeValue; }},
    {mData: 'buildDeptId', sTitle: '建设单位', bSortable: false, mRender : function(mData, type, full) { return full.buildDeptIdValue; }},
    {mData: 'landArea', sTitle: '用地面积(㎡)', bSortable: false},
    {mData: 'investmentAmount', sTitle: '投资金额(万元)', bSortable: false},
    /*{mData: 'planStartDate', sTitle: '拟开工时间', bSortable: false},
    {mData: 'planEndDate', sTitle: '拟建成时间', bSortable: false},*/
    {mData: function(source) { return oTableSetButtons(source); }, sTitle: '操作', bSortable: false, sWidth: 210}
];

cmProjectInfoListPanel.renderTable = function () {
    if (cmProjectInfoListPanel.oTable == null) {
        cmProjectInfoListPanel.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": cmProjectInfoListPanel.pageCount,
            "sAjaxSource": getRootPath() + "/project/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmProjectInfoListPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmProjectInfoListPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmProjectInfoListPanel.oTable.fnDraw();
    }
};

cmProjectInfoListPanel.delete = function (id) {
    var ids = String(id);
    if (id == 0) {
        var idsArr = [];
        $("[name='ids']:checked").each(function() {
            idsArr.push($(this).val());
        });
        ids = idsArr.join(",");
    }
    if (ids.length == 0) {
        msgBox.info({ content: $.i18n.prop("JC_SYS_061") });
        return;
    }
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function(){
            cmProjectInfoListPanel.deleteCallBack(ids);
        }
    });
};

cmProjectInfoListPanel.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/project/info/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            cmProjectInfoListPanel.renderTable();
        }
    });
};

cmProjectInfoListPanel.queryReset = function(){
    $('#searchForm')[0].reset();
};

cmProjectInfoListPanel.loadModuleForAdd = function (){
    $('#approval-modal').modal('show');
};
cmProjectInfoListPanel.waitJcLayer = null;
cmProjectInfoListPanel.chooseResource = function () {
    $('#approval-modal').modal('hide');
    cmProjectInfoListPanel.resourceWindowObject = new ResourcePlugin.ResourceData({
        renderElement: 'chooseResourceModule',
        sign: 'pt_project_info',
        autoClose: false,
        title: '选择资源库项目信息',
        search: [
            { text: '项目编号', column: 'project_num' },
            { text: '项目名称', column: 'project_name' }
        ],
        column: [
            {mData: 'no', sTitle: '序号', bSortable: false, sWidth: 60},
            {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
            {mData: 'projectName', sTitle: '项目名称', bSortable: false},
            {mData: 'buildDept', sTitle: '建设单位', bSortable: false}
        ],
        callback: function (data) {
            console.log(data);
            // 验证项目是否已经存在
            cmProjectInfoListPanel.waitJcLayer = layer.msg('加载中，请稍后...', { icon: 16, shade: [0.5, '#f5f5f5'], scrollbar: false, offset: ['200px', '45%'], time: 0 });
            $.ajax({
                type: "GET", data: {projectNumber: data.projectNumber, deptResourceId: data.buildDeptId}, dataType : "json",
                url : getRootPath() + "/project/info/checkProjectNumberAndGetDept.action",
                success : function(result) {
                    if (result.code != 0) {
                        msgBox.tip({ type:"fail", content: result.msg });
                        layer.close(cmProjectInfoListPanel.waitJcLayer);
                        return;
                    }
                    cmProjectInfoListPanel.resourceWindowObject.closeWindow();
                    cmProjectInfoListPanel.addProject(data.dlh_data_id_, result.data == null ? "" : result.data.id);
                }
            });
        }
    });
};

cmProjectInfoListPanel.addProject = function(resourceDataId, deptId) {
    resourceDataId = resourceDataId == undefined ? '' : resourceDataId;
    deptId = deptId == undefined ? '' : deptId;
    parent.JCF.LoadPage({url: '/project/info/loadForm.action?operator=add&resourceDataId=' + resourceDataId + '&deptId=' + deptId });
};

cmProjectInfoListPanel.loadModuleForUpdate = function (id){
    parent.JCF.LoadPage({url: '/project/info/loadForm.action?operator=modify&id=' + id });
};

cmProjectInfoListPanel.loadModuleForLook = function (id) {
    parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=' + id + '&p=detail' });
};

cmProjectInfoListPanel.distribution = function (id) {
    parent.JCF.LoadPage({url: '/project/person/manage.action?projectId='+ id });
};

$(document).ready(function(){
    cmProjectInfoListPanel.pageCount = TabNub > 0 ? TabNub : 10;
    cmProjectInfoListPanel.renderTable();
    $('#queryBtn').click(cmProjectInfoListPanel.renderTable);
    $('#resetBtn').click(cmProjectInfoListPanel.queryReset);
    $("#addBtn").click(cmProjectInfoListPanel.loadModuleForAdd);
});