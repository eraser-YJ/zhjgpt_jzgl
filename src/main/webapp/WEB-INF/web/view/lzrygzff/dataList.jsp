<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>人员实名制监控</span>
            <span>人员实名制监控 > </span><span>劳资人员监管 > </span><span>工资发放</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <input type="hidden" id="moduleDisPath" name="moduleDisPath" value="${disPath}"/>
            <form class="table-wrap form-table" id="searchLzrygzffForm">
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
                        <td>年份</td>
                        <td>
                            <input type="text" id="query_pay_year" name="query_pay_year"/>
                        </td>
                        <td>项目名称</td>
                        <td>
                            <input type="text" id="query_project_name" name="query_project_name"/>
                        </td>
                    </tr>
                    <tr>
                        <td>月份</td>
                        <td>
                            <select  id="query_month" name="query_month">
                                <option value="">-全部-</option>
                                <option value="1">1月</option>
                                <option value="2">2月</option>
                                <option value="3">3月</option>
                                <option value="4">4月</option>
                                <option value="5">5月</option>
                                <option value="6">6月</option>
                                <option value="7">7月</option>
                                <option value="8">8月</option>
                                <option value="9">9月</option>
                                <option value="10">10月</option>
                                <option value="11">11月</option>
                                <option value="12">12月</option>
                            </select>
                        </td>
                        <td>数据来源</td>
                        <td>
                            <select id="query_dataSrc" name="query_dataSrc"></select>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div  class="btn-tiwc">
                    <button class="btn" type="button" id="queryLzrygzffBtn">查 询</button>
                    <button class="btn" type="button" id="resetLzrygzffBtn">重 置</button>
                </div>
            </form>
        </div>
    </section>
    <section class="panel ">
        <div class="table-wrap">
            <table class="table table-striped tab_height first-td-tc" id="gridLzrygzffTable"></table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <div id="formLzrygzffModule"></div>
</section>
<script>
    var myLzrygzffJsList = {};
    myLzrygzffJsList.oTable = null;

    myLzrygzffJsList.oTableFnServerParams = function (aoData) {
        getTableParameters(myLzrygzffJsList.oTable, aoData);
        var query_pay_year = $('#searchLzrygzffForm #query_pay_year').val();
        if (query_pay_year && query_pay_year.length > 0) {
            aoData.push({"name": "payYear", "value": query_pay_year});
        }
        var query_project_name = $('#searchLzrygzffForm #query_project_name').val();
        if (query_project_name && query_project_name.length > 0) {
            aoData.push({"name": "projectName", "value": query_project_name});
        }

        var query_personName = $('#searchLzrygzffForm #query_personName').val();
        if (query_personName && query_personName.length > 0) {
            aoData.push({"name": "personName", "value": query_personName});
        }
        var query_personCertNum = $('#searchLzrygzffForm #query_personCertNum').val();
        if (query_personCertNum && query_personCertNum.length > 0) {
            aoData.push({"name": "personCertNum", "value": query_personCertNum});
        }

        var query_month = $('#searchLzrygzffForm #query_month').val();
        if (query_month && query_month.length > 0) {
            aoData.push({"name": "payMoth", "value": query_month});
        }
        var query_pay_status = $('#searchLzrygzffForm #query_pay_status').val();
        if (query_pay_status && query_pay_status.length > 0) {
            aoData.push({"name": "payStatus", "value": query_pay_status});
        }
        var query_dataSrc = $('#searchLzrygzffForm #query_dataSrc').val();
        if (query_dataSrc && query_dataSrc.length > 0) {
            aoData.push({"name": "dataSrc", "value": query_dataSrc});
        }
    };

    myLzrygzffJsList.oTableAoColumns = [
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
        {mData: 'project_name', sTitle: '项目名称', bSortable: false},
        {mData: 'pay_year', sTitle: '工资年份', bSortable: false},
        {mData: 'pay_moth', sTitle: '工资月份', bSortable: false},
        {mData: 'pay_count', sTitle: '实发金额', bSortable: false},
        {mData: 'pay_status', sTitle: '状态', bSortable: false},
        {
            mData: function (source) {
                var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#form-modal-Lzrygzff\"  onclick=\"myLzrygzffJsList.loadView('" + source.id + "')\" role=\"button\">查看</a>";
                return edit;
            }, sTitle: '操作', bSortable: false, sWidth: 70
        }
    ];

    myLzrygzffJsList.renderTable = function () {
        var moduleDisPath = $("#moduleDisPath").val();
        if (myLzrygzffJsList.oTable == null) {
            myLzrygzffJsList.oTable = $('#gridLzrygzffTable').dataTable({
                "iDisplayLength": myLzrygzffJsList.pageCount,
                "sAjaxSource": getRootPath() + "/mock/view/getList.action?disPath=" + moduleDisPath,
                "fnServerData": oTableRetrieveData,
                "aoColumns": myLzrygzffJsList.oTableAoColumns,
                "fnServerParams": function (aoData) {
                    myLzrygzffJsList.oTableFnServerParams(aoData);
                },
                aaSorting: [],
                aoColumnDefs: []
            });
        } else {
            myLzrygzffJsList.oTable.fnDraw();
        }
    };


    myLzrygzffJsList.queryReset = function () {
        $('#searchLzrygzffForm')[0].reset();
    };

    myLzrygzffJsList.loadView = function (id) {
        var moduleDisPath = $("#moduleDisPath").val();
        var page = "view." + moduleDisPath + ".dataForm";
        $("#formLzrygzffModule").load(getRootPath() + "/mock/view/showPage.action?disPath=" + moduleDisPath + "&id=" + id+"&page="+page, null, function () {
            myLzrygzffJsForm.init({title: '详细信息', id: id});
        });
    };

    $(document).ready(function () {
        myLzrygzffJsList.pageCount = TabNub > 0 ? TabNub : 10;
        myLzrygzffJsList.renderTable();
        keyFun.dataSrcSearchInit("query_dataSrc");
        $('#queryLzrygzffBtn').click(myLzrygzffJsList.renderTable);
        $('#resetLzrygzffBtn').click(myLzrygzffJsList.queryReset);
    });
</script>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>