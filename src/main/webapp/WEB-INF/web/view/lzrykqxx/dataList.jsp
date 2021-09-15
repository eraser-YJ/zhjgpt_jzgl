<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>人员实名制监控</span>
            <span>人员实名制监控 > </span><span>劳资人员监管 > </span><span>考勤信息</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <input type="hidden" id="moduleDisPath" name="moduleDisPath" value="${disPath}"/>
            <form class="table-wrap form-table" id="searchLzrykqxxForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width:10%;">姓名</td>
                        <td style="width:40%;">
                            <input type="text" id="query_personName" name="query_personName"/>
                        </td>
                        <td style="width:10%;">身份证号</td>
                        <td style="width:40%;">
                            <input type="text" id="query_personCertNum" name="query_personCertNum"/>
                        </td>
                    </tr>
                    <tr>
                        <td>考勤时间</td>
                        <td>
                            <div class="input-group w-p100">
                            <input class="datepicker-input" type="text" id="query_kqnyBegin" name="query_kqnyBegin" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_kqnyEnd lt"/>
                            <div class="input-group-btn w30">-</div>
                            <input class="datepicker-input" type="text" id="query_kqnyEnd" name="query_kqnyEnd" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_kqnyBegin gt"/>
                            </div>
                        </td>
                        <td>数据来源</td>
                        <td>
                            <select id="query_dataSrc" name="query_dataSrc"></select>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div  class="btn-tiwc">
                    <button class="btn" type="button" id="queryLzrykqxxBtn">查 询</button>
                    <button class="btn" type="button" id="resetLzrykqxxBtn">重 置</button>
                </div>
            </form>
        </div>
    </section>
    <section class="panel ">
        <div class="table-wrap">
            <table class="table table-striped tab_height first-td-tc" id="gridLzrykqxxTable"></table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <div id="formLzrykqxxModule"></div>
</section>
<script>
    var myLzrykqxxJsList = {};
    myLzrykqxxJsList.oTable = null;

    myLzrykqxxJsList.oTableFnServerParams = function (aoData) {
        getTableParameters(myLzrykqxxJsList.oTable, aoData);


        var query_personName = $('#searchLzrykqxxForm #query_personName').val();
        if (query_personName && query_personName.length > 0) {
            aoData.push({"name": "personName", "value": query_personName});
        }
        var query_personCertNum = $('#searchLzrykqxxForm #query_personCertNum').val();
        if (query_personCertNum && query_personCertNum.length > 0) {
            aoData.push({"name": "personCertNum", "value": query_personCertNum});
        }

        var query_kqnyBegin = $('#searchLzrykqxxForm #query_kqnyBegin').val();
        if (query_kqnyBegin && query_kqnyBegin.length > 0) {
            aoData.push({"name": "kqnyBegin", "value": query_kqnyBegin+" 00:00:00"});
        }

        var query_kqnyEnd = $('#searchLzrykqxxForm #query_kqnyEnd').val();
        if (query_kqnyEnd && query_kqnyEnd.length > 0) {
            aoData.push({"name": "kqnyEnd", "value": query_kqnyEnd+" 23:59:59"});
        }

        var query_lzry_cqts = $('#searchLzrykqxxForm #query_lzry_cqts').val();
        if (query_lzry_cqts && query_lzry_cqts.length > 0) {
            aoData.push({"name": "cqts", "value": query_lzry_cqts});
        }

        var query_dataSrc = $('#searchLzrykqxxForm #query_dataSrc').val();
        if (query_dataSrc && query_dataSrc.length > 0) {
            aoData.push({"name": "dataSrc", "value": query_dataSrc});
        }
    };

    myLzrykqxxJsList.oTableAoColumns = [
        {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
        {
            mData: function (source) {
                if (source.dataSrc) {
                    return keyFun.dataSrcValue(source.dataSrc);
                } else {
                    return "";
                }
            }, sTitle: '数据来源', bSortable: false
        },
        {mData: 'lzry_xm', sTitle: '姓名', bSortable: false},
        {mData: 'lzry_sfz_num', sTitle: '身份证号', bSortable: false},
        {mData: 'lzry_sex_value', sTitle: '性别', bSortable: false},
        {mData: 'project_name', sTitle: '项目', bSortable: false},
        {mData: 'class_bzmc', sTitle: '所属班组', bSortable: false},
        {
            mData: function (source) {
                if (source.lzry_kqny) {
                    var crtTime = new Date(source.lzry_kqny);
                    return dateFtt("yyyy-MM-dd", crtTime);
                } else {
                    return "";
                }
            }, sTitle: '考勤时间', bSortable: false
        },
        {mData: 'lzry_cqts', sTitle: '考勤状态', bSortable: false},
        {
            mData: function (source) {
                var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#form-modal-Lzrykqxx\"  onclick=\"myLzrykqxxJsList.loadView('" + source.id + "')\" role=\"button\">查看</a>";
                return edit;
            }, sTitle: '操作', bSortable: false, sWidth: 70
        }
    ];

    myLzrykqxxJsList.renderTable = function () {
        var moduleDisPath = $("#moduleDisPath").val();
        if (myLzrykqxxJsList.oTable == null) {
            myLzrykqxxJsList.oTable = $('#gridLzrykqxxTable').dataTable({
                "iDisplayLength": myLzrykqxxJsList.pageCount,
                "sAjaxSource": getRootPath() + "/mock/view/getList.action?disPath=" + moduleDisPath,
                "fnServerData": oTableRetrieveData,
                "aoColumns": myLzrykqxxJsList.oTableAoColumns,
                "fnServerParams": function (aoData) {
                    myLzrykqxxJsList.oTableFnServerParams(aoData);
                },
                aaSorting: [],
                aoColumnDefs: []
            });
        } else {
            myLzrykqxxJsList.oTable.fnDraw();
        }
    };


    myLzrykqxxJsList.queryReset = function () {
        $('#searchLzrykqxxForm')[0].reset();
    };

    myLzrykqxxJsList.loadView = function (id) {
        var moduleDisPath = $("#moduleDisPath").val();
        var page = "view." + moduleDisPath + ".dataForm";
        $("#formLzrykqxxModule").load(getRootPath() + "/mock/view/showPage.action?disPath=" + moduleDisPath + "&id=" + id+"&page="+page, null, function () {
            myLzrykqxxJsForm.init({title: '详细信息', id: id});
        });
    };

    $(document).ready(function () {
        myLzrykqxxJsList.pageCount = TabNub > 0 ? TabNub : 10;
        myLzrykqxxJsList.renderTable();
        keyFun.dataSrcSearchInit("query_dataSrc");
        $('#queryLzrykqxxBtn').click(myLzrykqxxJsList.renderTable);
        $('#resetLzrykqxxBtn').click(myLzrykqxxJsList.queryReset);
    });
</script>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>