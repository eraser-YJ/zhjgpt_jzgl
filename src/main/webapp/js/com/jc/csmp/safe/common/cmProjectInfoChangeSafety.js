var cmProjectInfoChangeSafeytPanel = {};
cmProjectInfoChangeSafeytPanel.oTable = null;

cmProjectInfoChangeSafeytPanel.callback = null;

cmProjectInfoChangeSafeytPanel.oTableFnServerParams = function(aoData){
    getTableParameters(cmProjectInfoChangeSafeytPanel.oTable, aoData);
    var projectNumber = $('#projectChangeSearchForm #projectChange_projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({"name": "projectNumber", "value": projectNumber}); }
    var projectName = $('#projectChangeSearchForm #projectChange_projectName').val();
    if (projectName.length > 0) { aoData.push({"name": "projectName", "value": projectName}); }
    var region = $('#projectChangeSearchForm #projectChange_region').val();
    if (region.length > 0) { aoData.push({name: 'region', value: region}); }
    var deptId = $('#projectChangeSearchForm #projectChange_deptId').val();
    if (deptId.length > 0) { aoData.push({name: 'deptCodeCondition', value: deptId}); }
};

cmProjectInfoChangeSafeytPanel.oTableAoColumns = [
    {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'buildDeptId', sTitle: '建设单位', bSortable: false, mRender : function(mData, type, full) { return full.buildDeptIdValue; }},
    {mData: 'superviseDeptId', sTitle: '监管单位', bSortable: false, mRender : function(mData, type, full) { return full.superviseDeptIdValue; }},
    {mData: function(source) {
        return "<a class=\"a-icon i-new\"  href=\"#javascript:void(0);\"  onclick=\"cmProjectInfoChangeSafeytPanel.change('" + source.id + "')\" role=\"button\">&nbsp;选择&nbsp;</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 80}
];

cmProjectInfoChangeSafeytPanel.renderTable = function () {
    if (cmProjectInfoChangeSafeytPanel.oTable == null) {
        cmProjectInfoChangeSafeytPanel.oTable = $('#projectChangeGridTable').dataTable( {
            "iDisplayLength": cmProjectInfoChangeSafeytPanel.pageCount,
            "sAjaxSource": getRootPath() + "/project/info/manageSafetyList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmProjectInfoChangeSafeytPanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmProjectInfoChangeSafeytPanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: [],
            fnDrawCallback : function() {
                var height = $('.tree-right').height();
                $('#projectChangeWindow').height((height < 400 ? 400 : height));
            }
        });
    } else {
        cmProjectInfoChangeSafeytPanel.oTable.fnDraw();
    }
};

cmProjectInfoChangeSafeytPanel.queryReset = function(){
    $('#projectChangeSearchForm')[0].reset();
};

cmProjectInfoChangeSafeytPanel.change = function (id) {
    if (cmProjectInfoChangeSafeytPanel.callback != null) {
        $.ajax({
            type: "GET", data: {id: id}, dataType: "json",
            url: getRootPath() + "/project/info/get.action",
            success: function(data) {
                if (data) {
                    cmProjectInfoChangeSafeytPanel.callback(data);
                    $('#project-change-modal').modal('hide');
                }
            }
        });
    }
};
cmProjectInfoChangeSafeytPanel.tree = function(){
    function onBeforeClick(id, node){
        if(node.isChecked == 0 && node.pId != null){
            return false;
        }else{
            return true;
        }
    }

    function onClick(event, treeId, treeNode) {
        $('#projectChangeSearchForm #projectChange_region').val(treeNode.id == 0 ? "" : treeNode.id);
        cmProjectInfoChangeSafeytPanel.renderTable();
    }

    JCTree.ztree({
        container : 'projectChangeTreeDemo',
        expand : true,
        rootNode : true,
        url: getRootPath() + "/common/api/dicTree.action?tc=region&pc=csmp",
        onClick: onClick,
        onBeforeClick : onBeforeClick,
    });
};

cmProjectInfoChangeSafeytPanel.init = function(config) {
    cmProjectInfoChangeSafeytPanel.callback = config.callback;
    cmProjectInfoChangeSafeytPanel.pageCount = TabNub > 0 ? TabNub : 10;
    cmProjectInfoChangeSafeytPanel.tree();
    $('#project-change-modal').modal('show');
};