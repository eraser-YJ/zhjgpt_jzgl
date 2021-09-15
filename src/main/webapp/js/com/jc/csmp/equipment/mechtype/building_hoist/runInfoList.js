var warnInfoJsList = {};
warnInfoJsList.oTable = null;

warnInfoJsList.oTableFnServerParams = function (aoData) {
    getTableParameters(warnInfoJsList.oTable, aoData);
    var equiId = $('#query_equiId').val();
    aoData.push({"name": "equiId", "value": equiId});
    var runTimeBegin = $('#searchForm #query_runTimeBegin').val();
    if (runTimeBegin.length > 0) {
        aoData.push({"name": "runTimeBegin", "value": runTimeBegin});
    }
    var runTimeEnd = $('#searchForm #query_runTimeEnd').val();
    if (runTimeEnd.length > 0) {
        aoData.push({"name": "runTimeEnd", "value": runTimeEnd});
    }
};
function diss(value){
    if(value){
        return value;
    }
    return "";
}

warnInfoJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 100},
    {mData: 'runTime', sTitle: '运行时间', bSortable: false, sWidth: 170},
    {mData: 'equiCode', sTitle: '设备编号', bSortable: false},
    {"mData": function (source) { return diss(source.system_state)}, sTitle: '系统状态', bSortable: false},
    {"mData": function (source) { return diss(source.record_number)}, sTitle: '备案号', bSortable: false},
    {"mData": function (source) { return diss(source.real_time_height)}, sTitle: '高度', bSortable: false},
    {"mData": function (source) { return diss(source.height_percentage)}, sTitle: '高度百分比', bSortable: false},
    {"mData": function (source) { return diss(source.real_time_lifting_weight)}, sTitle: '重量', bSortable: false},
    {"mData": function (source) { return diss(source.weight_percentage)}, sTitle: '重量百分比', bSortable: false},
    {"mData": function (source) { return diss(source.real_time_gradient1)}, sTitle: '倾斜度1', bSortable: false},
    {"mData": function (source) { return diss(source.real_time_gradient2)}, sTitle: '倾斜度2', bSortable: false},
    {"mData": function (source) { return diss(source.tilt_percentage1)}, sTitle: '倾斜百分比1', bSortable: false},
    {"mData": function (source) { return diss(source.tilt_percentage2)}, sTitle: '倾斜百分比2', bSortable: false},
    {"mData": function (source) { return diss(source.real_time_number_of_people)}, sTitle: '人数', bSortable: false},
    {"mData": function (source) { return diss(source.real_time_speed_direction)}, sTitle: '速度方向', bSortable: false},
    {"mData": function (source) { return diss(source.wind_speed)}, sTitle: '速度', bSortable: false},
    {"mData": function (source) { return diss(source.lock_state)}, sTitle: '门锁状态', bSortable: false},
    {"mData": function (source) { return diss(source.level_and_reason)}, sTitle: '级别', bSortable: false},
    {"mData": function (source) { return diss(source.real_time_or_alarm)}, sTitle: '数据的类型', bSortable: false},
    {"mData": function (source) { return diss(source.driver_identification_state)}, sTitle: '驾驶员身份认证结果', bSortable: false}
];
warnInfoJsList.renderTable = function () {
    if (warnInfoJsList.oTable == null) {
        warnInfoJsList.oTable = $('#gridTable').dataTable({
            "iDisplayLength": warnInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/run/info/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": warnInfoJsList.oTableAoColumns,
            "fnServerParams": function (aoData) {
                warnInfoJsList.oTableFnServerParams(aoData);
            },
            aaSorting: [],
            aoColumnDefs: []
        });
    } else {
        warnInfoJsList.oTable.fnDraw();
    }
};

warnInfoJsList.delete = function (id) {
    var ids = String(id);
    if (id == 0) {
        var idsArr = [];
        $("[name='ids']:checked").each(function () {
            idsArr.push($(this).val());
        });
        ids = idsArr.join(",");
    }
    if (ids.length == 0) {
        msgBox.info({content: $.i18n.prop("JC_SYS_061")});
        return;
    }
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function () {
            warnInfoJsList.deleteCallBack(ids);
        }
    });
};

warnInfoJsList.deleteCallBack = function (ids) {
    $.ajax({
        type: "POST", url: getRootPath() + "/warn/info/deleteByIds.action", data: {"ids": ids}, dataType: "json",
        success: function (data) {
            if (data.success == "true") {
                msgBox.tip({type: "success", content: data.successMessage});
            } else {
                msgBox.info({content: data.errorMessage});
            }
            warnInfoJsList.renderTable();
        }
    });
};

warnInfoJsList.queryReset = function () {
    $('#searchForm')[0].reset();
};

warnInfoJsList.loadModuleForUpdate = function (id) {
    $("#formModule").load(getRootPath() + "/warn/info/loadResult.action", null, function () {
        warnResultJsForm.init({title: '处理报告', operator: 'modify', id: id});
    });
};
warnInfoJsList.loadModuleForView = function (id) {
    $("#formModule").load(getRootPath() + "/warn/info/loadResult.action", null, function () {
        warnResultJsForm.view({title: '处理报告', operator: 'modify', id: id});
    });
};

$(document).ready(function () {
    warnInfoJsList.pageCount = TabNub > 0 ? TabNub : 10;
    warnInfoJsList.renderTable();
    $('#queryBtn').click(warnInfoJsList.renderTable);
    $('#resetBtn').click(warnInfoJsList.queryReset);
    $("#addBtn").click(warnInfoJsList.loadModuleForAdd);
});