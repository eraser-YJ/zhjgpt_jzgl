var cmProjectInfoChangePanel = {};
cmProjectInfoChangePanel.oTable = null;

cmProjectInfoChangePanel.callback = null;

cmProjectInfoChangePanel.oTableFnServerParams = function(aoData){
    getTableParameters(cmProjectInfoChangePanel.oTable, aoData);
    var projectNumber = $('#projectChangeSearchForm #projectChange_projectNumber').val();
    if (projectNumber.length > 0) { aoData.push({"name": "projectNumber", "value": projectNumber}); }
    var projectName = $('#projectChangeSearchForm #projectChange_projectName').val();
    if (projectName.length > 0) { aoData.push({"name": "projectName", "value": projectName}); }
    var region = $('#projectChangeSearchForm #projectChange_region').val();
    if (region.length > 0) { aoData.push({name: 'region', value: region}); }
    var deptId = $('#projectChangeSearchForm #projectChange_deptId').val();
    if (deptId.length > 0) { aoData.push({name: 'deptCodeCondition', value: deptId}); }
};

cmProjectInfoChangePanel.oTableAoColumns = [
    {mData: 'projectNumber', sTitle: '项目统一编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'buildDeptId', sTitle: '建设单位', bSortable: false, mRender : function(mData, type, full) { return full.buildDeptIdValue; }},
    {mData: 'superviseDeptId', sTitle: '监管单位', bSortable: false, mRender : function(mData, type, full) { return full.superviseDeptIdValue; }},
    {mData: function(source) {
        return "<a class=\"a-icon i-new\"  href=\"#javascript:void(0);\"  onclick=\"cmProjectInfoChangePanel.change('" + source.id + "')\" role=\"button\">&nbsp;选择&nbsp;</a>";
    }, sTitle: '操作', bSortable: false, sWidth: 80}
];

cmProjectInfoChangePanel.renderTable = function () {
    if (cmProjectInfoChangePanel.oTable == null) {
        cmProjectInfoChangePanel.oTable = $('#projectChangeGridTable').dataTable( {
            "iDisplayLength": cmProjectInfoChangePanel.pageCount,
            "sAjaxSource": getRootPath() + "/project/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmProjectInfoChangePanel.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmProjectInfoChangePanel.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: [],
            fnDrawCallback : function() {
                var height = $('.tree-right').height();
                $('#projectChangeWindow').height((height < 400 ? 400 : height));
            }
        });
    } else {
        cmProjectInfoChangePanel.oTable.fnDraw();
    }
};

cmProjectInfoChangePanel.queryReset = function(){
    $('#projectChangeSearchForm')[0].reset();
};

cmProjectInfoChangePanel.change = function (id) {
    if (cmProjectInfoChangePanel.callback != null) {
        $.ajax({
            type: "GET", data: {id: id}, dataType: "json",
            url: getRootPath() + "/project/info/get.action",
            success: function(data) {
                if (data) {
                    cmProjectInfoChangePanel.callback(data);
                    $('#project-change-modal').modal('hide');
                }
            }
        });
    }
};

cmProjectInfoChangePanel.tree = function(){
    function onBeforeClick(id, node){
        if(node.isChecked == 0 && node.pId != null){
            return false;
        }else{
            return true;
        }
    }

    function onClick(event, treeId, treeNode) {
        $('#projectChangeSearchForm #projectChange_region').val(treeNode.id == 0 ? "" : treeNode.id);
        cmProjectInfoChangePanel.renderTable();
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

cmProjectInfoChangePanel.init = function(config) {
    cmProjectInfoChangePanel.callback = config.callback;
    cmProjectInfoChangePanel.pageCount = TabNub > 0 ? TabNub : 10;
    cmProjectInfoChangePanel.tree();
    $('#project-change-modal').modal('show');
};