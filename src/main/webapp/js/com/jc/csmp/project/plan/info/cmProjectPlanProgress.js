var cmProjectPlanProgressList = {};
cmProjectPlanProgressList.oTable = null;
cmProjectPlanProgressList.commonTools = new CommonToolsLib.CommonTools({});

cmProjectPlanProgressList.treeObj = null;

cmProjectPlanProgressList.tree = function () {
    function onBeforeClick(id, node){
        if(node.isChecked == 0 && node.pId != null){
            return false;
        }else{
            return true;
        }
    }

    function onClick(event, treeId, treeNode) {
        $('#searchForm #progress_paramProjectId').val('');
        $('#searchForm #progress_paramStageId').val('');
        if (treeNode.id != '0') {
            var array = treeNode.id.split('|');
            $('#searchForm #progress_paramProjectId').val(array[1]);
            if (array[0] == 'stage') {
                $('#searchForm #progress_paramStageId').val(array[2]);
            }
            $('#emptyDiv').hide();
            $('#tableDiv').show();
            cmProjectPlanProgressList.renderTable();
        }
    }

    cmProjectPlanProgressList.treeObj = JCTree.ztree({
        container : 'treeDemo',
        expand : true,
        rootNode : true,
        isSearch: true,
        url: getRootPath() + "/project/plan/info/projectStageTree.action",
        onClick: onClick,
        onBeforeClick : onBeforeClick,
        ready: function (data) {
            operatorTreeModule.searchTreeInit({treeObj: cmProjectPlanProgressList.treeObj, data: data});
        }
    });
};

cmProjectPlanProgressList.oTableFnServerParams = function(aoData){
    getTableParameters(cmProjectPlanProgressList.oTable, aoData);
    $("input[name^='progress_']").each(function(objIndex, object){
        var value = $(object).val();
        if(value != '') {
            var operatorColumn = $(object).attr("data-column");
            aoData.push({"name": operatorColumn, "value": value});
        }
    });
};
cmProjectPlanProgressList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'stageId', sTitle: '所属阶段', bSortable: false, mRender : function(mData, type, full) { return full.stageName; }},
    {mData: 'planCode', sTitle: '计划编码', bSortable: false},
    {mData: 'planName', sTitle: '计划名称', bSortable: false},
    {mData: 'completionRatio', sTitle: '已完进度(%)', bSortable: false},
    {mData: 'planStartDate', sTitle: '计划开始时间', bSortable: false},
    {mData: 'planEndDate', sTitle: '计划结束时间', bSortable: false},
    {mData: 'actualStartDate', sTitle: '实际开始时间', bSortable: false},
    {mData: 'actualEndDate', sTitle: '实际结束时间', bSortable: false},
    {mData: function(source) {
        var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"cmProjectPlanProgressList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalTableBtnText('更新', 'fa-edit2') + "</a>";
        return edit;
    }, sTitle: '操作', bSortable: false, sWidth: 100}
];

cmProjectPlanProgressList.renderTable = function () {
    if (cmProjectPlanProgressList.oTable == null) {
        cmProjectPlanProgressList.oTable =  $('#progressTable').dataTable( {
            "iDisplayLength": cmProjectPlanProgressList.pageCount,
            "sAjaxSource": getRootPath() + "/project/plan/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": cmProjectPlanProgressList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                cmProjectPlanProgressList.oTableFnServerParams(aoData);
            },
            "fnCreatedRow": function( nRow, aData, iDataIndex ) {
                var planStartDate = aData.planStartDate;
                var planEndDate = aData.planEndDate;
                console.log(aData.actualStartDate)
                var actualStartDate = (aData.actualStartDate == null || aData.actualStartDate == '') ? new Date() : new Date(aData.actualStartDate.replace('-', '/'));
                var actualEndDate = (aData.actualEndDate == null || aData.actualEndDate == '') ? new Date() : new Date(aData.actualEndDate.replace('-', '/'));
                if (planStartDate == '' || planEndDate == '') {
                    return;
                }
                planStartDate = new Date(planStartDate.replace('-', '/'));
                planEndDate = new Date(planEndDate.replace('-', '/'));
                if (cmProjectPlanProgressList.commonTools.dateCompare(actualEndDate, planEndDate)) {
                    //计划结束时间大于=实际结束时间的话，
                    return;
                }
                if (cmProjectPlanProgressList.commonTools.dateCompare(actualStartDate, planStartDate)) {
                    //计划开始时间大于=实际开始时间的话，
                    return;
                }
                for(var i = 0 ; nRow.cells && i < nRow.cells.length - 1 ; i++){
                    nRow.cells[i].innerHTML = "<span title='" + nRow.cells[i].innerHTML + "'><font color='red'>" + nRow.cells[i].innerHTML + "</font></span>";
                }
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        cmProjectPlanProgressList.oTable.fnDraw();
    }
};

cmProjectPlanProgressList.queryReset = function(){
    $('#searchForm')[0].reset();
};

cmProjectPlanProgressList.loadModuleForUpdate = function (id){
    if($('#searchForm #query_projectId').val() === ''){
        msgBox.info({content: "请选择项目", type:'fail'});
        return false
    }
    $("#formModule").load(getRootPath() + "/project/plan/info/loadProgressForm.action", null, function() {
        cmProjectPlanProgressFormPanel.init({
            title: '进度更新', id: id,
            callback: function () {
                cmProjectPlanProgressList.renderTable();
            }
        });
    });
};

$(document).ready(function(){
    cmProjectPlanProgressList.pageCount = TabNub > 0 ? TabNub : 10;
    cmProjectPlanProgressList.tree();
});