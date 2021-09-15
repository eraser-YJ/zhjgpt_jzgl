<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>塔机监控预警</span>
            <span>塔机监控预警 > </span><span>升级机预警信息</span>
        </div>
    </header>

    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <input type="hidden" id="query_mechType" name="query_mechType" value="${mechType}"/>
            <form class="table-wrap form-table" id="searchWarnDetailForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width:10%;">项目</td>
                        <td style="width:40%;">
                            <input type="text" id="query_targetProjectName" name="query_targetProjectName"/>
                        </td>
                        <td style="width:10%;">报警</td>
                        <td style="width:40%;">
                            <input type="text" id="query_warnReason" name="query_warnReason"/>
                        </td>
                    </tr>
                    <tr>
                        <td>设备编码</td>
                        <td >
                            <input type="text" id="query_targetCode" name="query_targetCode"/>
                        </td>
                        <td>报警时间</td>
                        <td >
                            <div class="input-group w-p100">
                                <input class="datepicker-input" type="text" id="query_warnTimeBegin" name="query_warnTimeBegin" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_warnTimeEnd lt">
                                <div class="input-group-btn w30">-</div>
                                <input class="datepicker-input" type="text" id="query_warnTimeEnd" name="query_warnTimeEnd" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_warnTimeBegin gt">
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div  class="btn-tiwc">
                    <button class="btn" type="button" id="queryWarnDetailBtn">查 询</button>
                    <button class="btn" type="button" id="resetWarnDetailBtn">重 置</button>
                </div>
            </form>
        </div>
    </section>

    <section class="panel ">
        <div class="table-wrap">
            <table class="table table-striped tab_height first-td-tc" id="gridWarnDetailTable"></table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <div id="formWarnDetailModule"></div>
</section>
<script>
    var warnInfoJsList = {};
    warnInfoJsList.oTable = null;

    warnInfoJsList.oTableFnServerParams = function (aoData) {
        getTableParameters(warnInfoJsList.oTable, aoData);
        var query_mechType = $('#query_mechType').val();
        aoData.push({"name": "targetType", "value": query_mechType});
        var query_targetProjectName = $('#searchWarnDetailForm #query_targetProjectName').val();
        if (query_targetProjectName.length > 0) {
            aoData.push({"name": "targetProjectName", "value": query_targetProjectName});
        }
        var query_warnReason = $('#searchWarnDetailForm #query_warnReason').val();
        if (query_warnReason && query_warnReason.length > 0) {
            aoData.push({"name": "warnReason", "value": query_warnReason});
        }
        var targetCodeCondObj = $('#searchWarnDetailForm #query_targetCode').val();
        if (targetCodeCondObj.length > 0) {
            aoData.push({"name": "targetCode", "value": targetCodeCondObj});
        }
        var warnTimeBegin = $('#searchWarnDetailForm #query_warnTimeBegin').val();
        if (warnTimeBegin.length > 0) {
            aoData.push({"name": "warnTimeBegin", "value": warnTimeBegin});
        }
        var warnTimeEnd = $('#searchWarnDetailForm #query_warnTimeEnd').val();
        if (warnTimeEnd.length > 0) {
            aoData.push({"name": "warnTimeEnd", "value": warnTimeEnd});
        }
    };

    //空处理
    warnInfoJsList.nvl = function (value, defaultValue) {
        if (value) {
            return value;
        }
        if (defaultValue) {
            return defaultValue;
        }
        return "";
    }

    warnInfoJsList.oTableAoColumns = [
        {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
        {mData: 'targetProjectName', sTitle: '项目', bSortable: false},
        {mData: 'targetCode', sTitle: '设备编码', bSortable: false},
        { mData: function (source) {
                var h = '<a href="javascript:warnInfoJsList.showView(\'' + source.id + '\')">' + warnInfoJsList.nvl(source.warnReason) + '</a>';
                return h;
            }, sTitle: '报警', bSortable: false},
        {mData: 'warnTime', sTitle: '报警发生时间', bSortable: false},
        {mData: function (source) {
                if (source.warnStatus == 'processed') {
                    return "是"
                }
                return "否"
            }, sTitle: '处置状态', bSortable: false},
        {mData: 'processTime', sTitle: '处置时间', bSortable: false},
        {mData: 'num01', sTitle: '高度', bSortable: false},
        {mData: 'num02', sTitle: '高度百分比', bSortable: false},
        {mData: 'num03', sTitle: '重量', bSortable: false},
        {mData: 'num04', sTitle: '重量百分比', bSortable: false},
        {mData: 'num05', sTitle: '倾斜度1', bSortable: false},
        {mData: 'num06', sTitle: '倾斜度2', bSortable: false},
        {
            mData: function (source) {
                if (source.warnStatus == 'processed') {
                    var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"warnInfoJsList.loadModuleForView('" + source.id + "')\" role=\"button\">&nbsp;查看&nbsp;</a>";
                    return edit;
                } else {
                    var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#myModal-edit\"  onclick=\"warnInfoJsList.loadModuleForUpdate('" + source.id + "')\" role=\"button\">&nbsp;处置&nbsp;</a>";
                    return edit;
                }
            }, sTitle: '操作', bSortable: false, sWidth: 120
        }
    ];

    warnInfoJsList.renderTable = function () {
        var moduleDisPath = $("#moduleDisPath").val();
        if (warnInfoJsList.oTable == null) {
            warnInfoJsList.oTable = $('#gridWarnDetailTable').dataTable({
                "iDisplayLength": warnInfoJsList.pageCount,
                "sAjaxSource": getRootPath() + "/warn/info/manageList.action",
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


    warnInfoJsList.queryReset = function () {
        $('#searchWarnDetailForm')[0].reset();
    };

    warnInfoJsList.initWarnDic = function () {
        var query_mechType = $('#query_mechType').val();
        $.ajax({
            type: "GET",
            data: {"mechType": query_mechType},
            dataType: "json",
            url: getRootPath() + "/warn/info/dicList.action",
            success: function (data) {
                $("#query_warnReasonCode").empty();
                $("#query_warnReasonCode").append("<option value=''>-请选择-</option>")
                for (var dicIndex = 0; dicIndex < data.length; dicIndex++) {
                    $("#query_warnReasonCode").append("<option value='" + data[dicIndex].name + "'>" + data[dicIndex].value + "</option>")
                }
            }
        });
    };


    warnInfoJsList.loadModuleForUpdate = function (id) {
        $("#formWarnDetailModule").load(getRootPath() + "/warn/info/loadResult.action", null, function () {
            warnResultJsForm.init({title: '处理报告', operator: 'modify', id: id});
        });
    };
    warnInfoJsList.loadModuleForView = function (id) {
        $("#formWarnDetailModule").load(getRootPath() + "/warn/info/loadResult.action", null, function () {
            debugger
            warnResultJsForm.view({title: '处理报告', operator: 'modify', id: id});
        });
    };
    //查看
    warnInfoJsList.showView = function (id) {
        var type = $("#query_mechType").val();
        $("#formWarnDetailModule").load(getRootPath() + "/warn/info/loadView.action?id=" + id + "&type=" + type + "&n_=" + (new Date().getTime()), null, function () {
        });
    }
    $(document).ready(function () {
        warnInfoJsList.pageCount = TabNub > 0 ? TabNub : 10;
        warnInfoJsList.renderTable();
        warnInfoJsList.initWarnDic();
        $('#queryWarnDetailBtn').click(warnInfoJsList.renderTable);
        $('#resetWarnDetailBtn').click(warnInfoJsList.queryReset);
    });
</script>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>