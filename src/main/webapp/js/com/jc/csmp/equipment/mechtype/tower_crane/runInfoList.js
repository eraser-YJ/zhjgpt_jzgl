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
    {mData: 'equiCode', sTitle: '设备编码', bSortable: false},
    {"mData": function (source) { return diss(source.tower_crane_id)}, sTitle: '塔机编号', bSortable: false},
    {"mData": function (source) { return diss(source.braking_state)}, sTitle: '制动状态', bSortable: false},
    {"mData": function (source) { return diss(source.crane_elevating_capacity)}, sTitle: '起重量数据', bSortable: false},
    {"mData": function (source) { return diss(source.crane_height)}, sTitle: '高度数据', bSortable: false},
    {"mData": function (source) { return diss(source.crane_manufacturer_and_device_type)}, sTitle: '厂家及设备类型', bSortable: false},
    {"mData": function (source) { return diss(source.crane_protocol_version)}, sTitle: '版本号', bSortable: false},
    {"mData": function (source) { return diss(source.crane_range)}, sTitle: '幅度数据', bSortable: false},
    {"mData": function (source) { return diss(source.crane_rotation)}, sTitle: '回转', bSortable: false},
    {"mData": function (source) { return diss(source.crane_tilt_angle)}, sTitle: '倾角数据', bSortable: false},
    {"mData": function (source) { return diss(source.crane_tilt_percentage)}, sTitle: '倾斜百分比', bSortable: false},
    {"mData": function (source) { return diss(source.crane_torque_percentage)}, sTitle: '力距百分比', bSortable: false},
    {"mData": function (source) { return diss(source.crane_weight_percentage)}, sTitle: '重量百分比', bSortable: false},
    {"mData": function (source) { return diss(source.crane_wind_speed)}, sTitle: '风速数据', bSortable: false},
    {"mData": function (source) { return diss(source.beaufort_scale)}, sTitle: '风级', bSortable: false},
    {"mData": function (source) { return diss(source.crane_wind_speed_percentage)}, sTitle: '风速百分比', bSortable: false},
    {"mData": function (source) { return diss(source.real_time_or_alarm)}, sTitle: '数据的类型', bSortable: false},
    {"mData": function (source) { return diss(source.project_id)}, sTitle: '项目id', bSortable: false},
    {"mData": function (source) { return diss(source.project_name)}, sTitle: '项目名', bSortable: false}
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