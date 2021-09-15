var superviseResourceList = {};
superviseResourceList.oTable = null;

superviseResourceList.oTableAoColumns = superviseResourceList_oTableAoColumns;

superviseResourceList.oTableFnServerParams = function(aoData){
    //排序条件
    getTableParameters(superviseResourceList.oTable, aoData);
    aoData.push({name: 'sign', value: $('#searchForm #sign').val()});
    var cond = [];
    $("input[name^='query_']").each(function(objIndex,object){
        var value = $(object).val();
        if(value != ''){
            var operationAction = $(object).attr("operationAction");
            var operationKey = $(object).attr("operationKey");
            var operationType = $(object).attr("operationType");
            cond.push({"operationAction":operationAction,"operationKey":operationKey,"operationType":operationType,"value":value})
        }
    });
    $("select[name^='query_']").each(function(objIndex,object){
        var value = $(object).val();
        if (value != '') {
            cond.push({"operationAction": 'like', "operationKey": this.id.replace('query_', ''),"operationType": 'dic', "value":value})
        }
    });
    aoData.push({ 'name': 'condJson', 'value': JSON.stringify(cond)});
};

superviseResourceList.superviseResourceList = function () {
    if (superviseResourceList.oTable == null) {
        superviseResourceList.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": superviseResourceList.pageCount,
            "sAjaxSource": getRootPath() + "/supervise/resource/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": superviseResourceList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                superviseResourceList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        } );
    } else {
        superviseResourceList.oTable.fnDraw();
    }
};

superviseResourceList.queryReset = function(){
    $('#searchForm')[0].reset();
};

superviseResourceList.look = function(id) {
    resourceDetailModal.detail({dataId: id, sign: $('#searchForm #sign').val()});
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    //计算分页显示条数
    superviseResourceList.pageCount = TabNub>0 ? TabNub : 10;
    //初始化列表方法
    superviseResourceList.superviseResourceList();
});