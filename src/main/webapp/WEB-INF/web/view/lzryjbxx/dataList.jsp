<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>人员实名制监控</span>
            <span>人员实名制监控 > </span><span>劳资人员监管 > </span><span>劳资人员信息</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <input type="hidden" id="moduleDisPath" name="moduleDisPath" value="${disPath}"/>
            <form class="table-wrap form-table" id="searchLzryjbxxForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>

                        <td style="width:10%;">身份证号</td>
                        <td style="width:40%;">
                            <input type="text" id="query_personCertNum" name="query_personCertNum"/>
                        </td>
                        <td style="width:10%;">数据来源</td>
                        <td style="width:40%;">
                            <select id="query_dataSrc" name="query_dataSrc"></select>
                        </td>
                    </tr>
                    <tr>
                        <td >姓名</td>
                        <td>
                            <input type="text" id="query_personName" name="query_personName"/>
                        </td>
                        <td>联系电话</td>
                        <td>
                            <input type="text" id="query_personTel" name="query_personTel"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div  class="btn-tiwc">
                    <button class="btn" type="button" id="queryLzryjbxxBtn">查 询</button>
                    <button class="btn" type="button" id="resetLzryjbxxBtn">重 置</button>
                </div>
            </form>
        </div>
    </section>
    <section class="panel ">
        <div class="table-wrap">
            <table class="table table-striped tab_height first-td-tc" id="gridLzryjbxxTable"></table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <div id="formLzryjbxxModule"></div>
</section>
<script>
    var myLzryjbxxJsList = {};
    myLzryjbxxJsList.oTable = null;

    myLzryjbxxJsList.oTableFnServerParams = function (aoData) {
        getTableParameters(myLzryjbxxJsList.oTable, aoData);
        var query_personTel = $('#searchLzryjbxxForm #query_personTel').val();
        if (query_personTel && query_personTel.length > 0) {
            aoData.push({"name": "personTel", "value": query_personTel});
        }
        var query_dataSrc = $('#searchLzryjbxxForm #query_dataSrc').val();
        if (query_dataSrc && query_dataSrc.length > 0) {
            aoData.push({"name": "dataSrc", "value": query_dataSrc});
        }
        var query_personName = $('#searchLzryjbxxForm #query_personName').val();
        if (query_personName && query_personName.length > 0) {
            aoData.push({"name": "personName", "value": query_personName});
        }
        var query_personCertNum = $('#searchLzryjbxxForm #query_personCertNum').val();
        if (query_personCertNum && query_personCertNum.length > 0) {
            aoData.push({"name": "personCertNum", "value": query_personCertNum});
        }
    };

    myLzryjbxxJsList.oTableAoColumns = [
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
        {mData: 'lzry_tel', sTitle: '联系电话', bSortable: false},
        {mData: 'projectCount', sTitle: '参与项目数', bSortable: false},
        {
            mData: function (source) {
                var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#form-modal-Lzryjbxx\"  onclick=\"myLzryjbxxJsList.loadView('" + source.id + "')\" role=\"button\">查看</a>";
                return edit;
            }, sTitle: '操作', bSortable: false, sWidth: 70
        }
    ];

    myLzryjbxxJsList.renderTable = function () {
        var moduleDisPath = $("#moduleDisPath").val();
        if (myLzryjbxxJsList.oTable == null) {
            myLzryjbxxJsList.oTable = $('#gridLzryjbxxTable').dataTable({
                "iDisplayLength": myLzryjbxxJsList.pageCount,
                "sAjaxSource": getRootPath() + "/mock/view/getList.action?disPath=" + moduleDisPath,
                "fnServerData": oTableRetrieveData,
                "aoColumns": myLzryjbxxJsList.oTableAoColumns,
                "fnServerParams": function (aoData) {
                    myLzryjbxxJsList.oTableFnServerParams(aoData);
                },
                aaSorting: [],
                aoColumnDefs: []
            });
        } else {
            myLzryjbxxJsList.oTable.fnDraw();
        }
    };


    myLzryjbxxJsList.queryReset = function () {
        $('#searchLzryjbxxForm')[0].reset();
    };

    myLzryjbxxJsList.loadView = function (id) {
        var moduleDisPath = $("#moduleDisPath").val();
        var page = "view." + moduleDisPath + ".dataForm";
        $("#formLzryjbxxModule").load(getRootPath() + "/mock/view/showPage.action?disPath=" + moduleDisPath + "&id=" + id+"&page="+page, null, function () {
            myLzryjbxxJsForm.init({title: '详细信息', id: id});
        });
    };

    $(document).ready(function () {
        myLzryjbxxJsList.pageCount = TabNub > 0 ? TabNub : 10;
        myLzryjbxxJsList.renderTable();
        keyFun.dataSrcSearchInit("query_dataSrc");
        $('#queryLzryjbxxBtn').click(myLzryjbxxJsList.renderTable);
        $('#resetLzryjbxxBtn').click(myLzryjbxxJsList.queryReset);
    });
</script>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>