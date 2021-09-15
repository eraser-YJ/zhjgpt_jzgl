var viewJsList = {};

//分页处理 start
//分页对象
viewJsList.oTable = null;

viewJsList.oTableAoColumns = viewList_oTableAoColumns;

viewJsList.oTableFnServerParams = function (aoData) {
    //排序条件
    getTableParameters(viewJsList.oTable, aoData);
    $("#searchForm").find("[id^='query_']").each(function (objIndex, object) {
        var value = $(object).val();
        if (value && value != '') {
            var fieldName = $(object).attr("name");
            aoData.push({"name": fieldName, "value": value});
        }
    });
};

//分页查询
viewJsList.viewJsList = function () {
    var disPath = $("#query_disPath").val();
    if (viewJsList.oTable == null) {
        viewJsList.oTable = $('#viewDataTable').dataTable({
            "iDisplayLength": viewJsList.pageCount,//每页显示多少条记录
            "sAjaxSource": getRootPath() + "/view/manageList.action?disPath=" + disPath,
            "fnServerData": oTableRetrieveData,//查询数据回调函数
            "aoColumns": viewJsList.oTableAoColumns,//table显示列
            //传参
            "fnServerParams": function (aoData) {
                viewJsList.oTableFnServerParams(aoData);
            },
            "ordering": false
        });
    } else {
        viewJsList.oTable.fnDraw();
    }
};

viewJsList.queryReset = function () {
    $('#searchForm')[0].reset();
};
//字典初始化
viewJsList.initDic = function () {
    var aoData = [];
    $("#searchForm").find("select[id^='query_']").each(function (objIndex, object) {
        var value = $(object).val();
        if (value != '') {
            var id = $(object).attr("id");
            var dictName = $(object).attr("dictName");
            var parentCode = $(object).attr("parentCode");
            var row = {};
            row['id'] = id;
            row['dictName'] = dictName;
            row['parentCode'] = parentCode;
            aoData.push(row);
        }
    });
    var jsonStr = JSON.stringify(aoData);
    var url = getRootPath() + "/view/dic.action";
    jQuery.ajax({
        url: url,
        type: 'POST',
        async:false,
        cache: false,
        data: {"dicContent": jsonStr},
        success: function (data) {
            for (var dicIdx = 0; dicIdx < data.length; dicIdx++) {
                $("#" + data[dicIdx].id).empty();
                var nowItem;
                $("#" + data[dicIdx].id).append("<option value=''>-请选择-</option>");
                for (var dicIdx1 = 0; dicIdx1 < data[dicIdx].dicList.length; dicIdx1++) {
                    nowItem = data[dicIdx].dicList[dicIdx1];
                    $("#" + data[dicIdx].id).append("<option value='" + nowItem.code + "'>" + nowItem.value + "</option>");
                }
            }

        }
    });
};

$(document).ready(function () {
    ie8StylePatch();
    $(".datepicker-input").each(function () {
        $(this).datepicker();
    });
    //字典初始化
    viewJsList.initDic();
    //计算分页显示条数
    viewJsList.pageCount = TabNub > 0 ? TabNub : 10;
    //初始化列表方法
    viewJsList.viewJsList();
    $("#queryBtn").click(viewJsList.viewJsList);
    $("#queryReset").click(viewJsList.queryReset);
});