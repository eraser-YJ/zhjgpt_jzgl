var cmProjectWeeklyJsList = {};

cmProjectWeeklyJsList.oTable = null;

cmProjectWeeklyJsList.treeObj = null;

cmProjectWeeklyJsList.tree = function () {
    function onBeforeClick(id, node){
        if(node.isChecked == 0 && node.pId != null){
            return false;
        }else{
            return true;
        }
    }

    function onClick(event, treeId, treeNode) {
        $("#searchForm #searchFormQuery_projectId").val(treeNode.id == '0' ? '' : treeNode.id);
        $("#searchForm #projectName").val(treeNode.name);
        cmProjectWeeklyJsList.renderTable();
    }

    cmProjectWeeklyJsList.treeObj = JCTree.ztree({
        container : 'treeDemo',
        expand : true,
        rootNode : true,
        isSearch: true,
        url: getRootPath() + "/project/weekly/treeList.action",
        onClick: onClick,
        onBeforeClick : onBeforeClick,
        ready: function (data) {
            operatorTreeModule.searchTreeInit({treeObj: cmProjectWeeklyJsList.treeObj, data: data});
        }
    });
};

cmProjectWeeklyJsList.oTableFnServerParams = function(aoData){
    getTableParameters(cmProjectWeeklyJsList.oTable, aoData);
    $("input[name^='searchFormQuery_']").each(function(objIndex, object){
        var value = $(object).val();
        if(value != ''){
            var column = object.id.replace('searchFormQuery_', '');
            aoData.push({"name": column, "value": value});
        }
    });
};
cmProjectWeeklyJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'projectNumber', sTitle: '项目统一编码', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'reportName', sTitle: '周报名称', bSortable: false},
    {mData: 'userName', sTitle: '提交人', bSortable: false},
    {mData: 'ifRelease', sTitle: '是否上报', bSortable: false, mRender:function (mData, type, full) { return full.ifReleaseValue }},
    {mData: 'releaseDate', sTitle: '上报日期', bSortable: false},
    //{mData: 'status', sTitle: '是否反馈', bSortable: false, mRender:function (mData, type, full) { return full.statusValue }},
	{mData: function(source) {
        var edit = "<a class=\"a-icon i-cemt\" href=\"javascript:void(0);\" onclick=\"cmProjectWeeklyJsList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('修改', 'fa-edit2') + "</a>";
	    if (source.ifRelease == 1) {
            edit = "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"cmProjectWeeklyJsList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
        }
        var del = "<a class=\"a-icon i-remove\" href=\"javascript:void(0);\" onclick=\"cmProjectWeeklyJsList.delete('"+ source.id+ "')\">" + finalTableBtnText('删除', 'fa-remove') + "</a>";
		return edit + del;
	}, sTitle: '操作', bSortable: false, sWidth: 135}
];

cmProjectWeeklyJsList.renderTable = function () {
    if (cmProjectWeeklyJsList.oTable == null) {
        cmProjectWeeklyJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": cmProjectWeeklyJsList.pageCount,
            "sAjaxSource": getRootPath() + "/project/weekly/myList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmProjectWeeklyJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmProjectWeeklyJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmProjectWeeklyJsList.oTable.fnDraw();
    }
};

cmProjectWeeklyJsList.delete = function (id) {
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
            cmProjectWeeklyJsList.deleteCallBack(ids);
        }
    });
};

cmProjectWeeklyJsList.deleteCallBack = function(ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/project/weekly/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data.success == "true") {
                msgBox.tip({ type:"success", content: data.successMessage });
            } else {
                msgBox.info({ content: data.errorMessage });
            }
            cmProjectWeeklyJsList.renderTable();
        }
    });
};

cmProjectWeeklyJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

cmProjectWeeklyJsList.loadModuleForAdd = function (){
    if($('#searchForm #searchFormQuery_projectId').val() === ''){
        msgBox.info({content: "请选择项目", type:'fail'});
        return false
    }
    //$('#titleForm #reportName').val($('#searchForm #projectName').val() + $('#searchForm #currentPaperTitle').val());
    $('#title-modal').modal('show');
};

cmProjectWeeklyJsList.saveReportName = function() {
    if ($('#titleForm #reportName').val() == '') {
        msgBox.tip({ type:"fail", content: '请输入周报名称' });
        return;
    }
    $.ajax({
        url: getRootPath() + "/project/weekly/saveReportName.action",
        type: "POST", data: {reportName: $('#titleForm #reportName').val(), projectId: $('#searchForm #searchFormQuery_projectId').val()}, dataType: "json",
        success : function(data) {
            if (data.code == 0) {
                $('#title-modal').modal('hide');
                parent.JCF.LoadPage({url: '/project/weekly/loadForm.action?id='+ data.data.id });
            }
        }
    });
};

cmProjectWeeklyJsList.loadModuleForUpdate = function (id){
    parent.JCF.LoadPage({url: '/project/weekly/loadForm.action?id='+ id });
};

$(document).ready(function(){
    cmProjectWeeklyJsList.pageCount = TabNub > 0 ? TabNub : 10;
    $('#queryBtn').click(cmProjectWeeklyJsList.renderTable);
    $('#resetBtn').click(cmProjectWeeklyJsList.queryReset);
    $("#addBtn").click(cmProjectWeeklyJsList.loadModuleForAdd);
    cmProjectWeeklyJsList.tree();
});