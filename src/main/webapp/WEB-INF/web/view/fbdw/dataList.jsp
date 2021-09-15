<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>人员实名制监控</span>
            <span>人员实名制监控 > </span><span>分包单位监控</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <input type="hidden" id="moduleDisPath" name="moduleDisPath" value="${disPath}"/>
            <form class="table-wrap form-table" id="searchFbdwForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width:10%;">社会信用代码</td>
                        <td style="width:40%;">
                            <input type="text" id="query_contract_time_type" name="query_contract_time_type"/>
                        </td>
                        <td style="width:10%;">数据来源</td>
                        <td style="width:40%;">
                            <select id="query_dataSrc" name="query_dataSrc"></select>
                        </td>
                    </tr>
                    <tr>
                        <td>企业名称</td>
                        <td>
                            <input type="text" id="company_name" name="company_name"/>
                        </td>
                        <td>项目名称</td>
                        <td>
                            <input type="text" id="projectName" name="projectName"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div  class="btn-tiwc">
                    <button class="btn" type="button" id="queryFbdwBtn">查 询</button>
                    <button class="btn" type="button" id="resetFbdwBtn">重 置</button>
                </div>
            </form>
        </div>
    </section>
    <section class="panel ">
        <div class="table-wrap">
            <table class="table table-striped tab_height first-td-tc" id="gridFbdwTable"></table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <div id="formFbdwModule"></div>
</section>
<script>
    var myFbdwJsList = {};
    myFbdwJsList.oTable = null;

    myFbdwJsList.oTableFnServerParams = function (aoData) {
        getTableParameters(myFbdwJsList.oTable, aoData);
        var query_contract_time_type = $('#searchFbdwForm #query_contract_time_type').val();
        if (query_contract_time_type && query_contract_time_type.length > 0) {
            aoData.push({"name": "contractTimeType", "value": query_contract_time_type});
        }
        var query_dataSrc = $('#searchFbdwForm #query_dataSrc').val();
        if (query_dataSrc && query_dataSrc.length > 0) {
            aoData.push({"name": "dataSrc", "value": query_dataSrc});
        }
        var company_name = $('#searchFbdwForm #company_name').val();
        if (company_name && company_name.length > 0) {
            aoData.push({"name": "companyName", "value": company_name});
        }
        var projectName = $('#searchFbdwForm #projectName').val();
        if (projectName && projectName.length > 0) {
            aoData.push({"name": "projectName", "value": projectName});
        }
    };

    myFbdwJsList.oTableAoColumns = [
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
        {mData: 'company_name', sTitle: '参建企业名称', bSortable: false},
        {mData: 'company_type_value', sTitle: '参建企业类型', bSortable: false},
        {mData: 'project_name', sTitle: '项目名称', bSortable: false},
        {mData: 'contract_time_type', sTitle: '参建企业统一社会信用代码', bSortable: false},
        {mData: 'person_num', sTitle: '负责人姓名', bSortable: false},
        {mData: 'person_tel', sTitle: '联系电话', bSortable: false},
        {
            mData: function (source) {
                if (source.supervise_star) {
                    var crtTime = new Date(source.supervise_star);
                    return dateFtt("yyyy-MM-dd", crtTime);
                } else {
                    return "";
                }
            }, sTitle: '进场时间', bSortable: false
        },
        {
            mData: function (source) {
                if (source.supervise_end) {
                    var crtTime = new Date(source.supervise_end);
                    return dateFtt("yyyy-MM-dd", crtTime);
                } else {
                    return "";
                }
            }, sTitle: '进场时间', bSortable: false
        },
        {
            mData: function (source) {
                var edit = "<a class=\"a-icon i-edit\"  href=\"#form-modal-Fbdw\"  onclick=\"myFbdwJsList.loadView('" + source.id + "')\" role=\"button\">查看</a>";
                return edit;
            }, sTitle: '操作', bSortable: false, sWidth: 70
        }
    ];

    myFbdwJsList.renderTable = function () {
        var moduleDisPath = $("#moduleDisPath").val();
        if (myFbdwJsList.oTable == null) {
            myFbdwJsList.oTable = $('#gridFbdwTable').dataTable({
                "iDisplayLength": myFbdwJsList.pageCount,
                "sAjaxSource": getRootPath() + "/mock/view/getList.action?disPath=" + moduleDisPath,
                "fnServerData": oTableRetrieveData,
                "aoColumns": myFbdwJsList.oTableAoColumns,
                "fnServerParams": function (aoData) {
                    myFbdwJsList.oTableFnServerParams(aoData);
                },
                aaSorting: [],
                aoColumnDefs: []
            });
        } else {
            myFbdwJsList.oTable.fnDraw();
        }
    };


    myFbdwJsList.queryReset = function () {
        $('#searchFbdwForm')[0].reset();
    };

    myFbdwJsList.loadView = function (id) {
        var moduleDisPath = $("#moduleDisPath").val();
        var page = "view." + moduleDisPath + ".dataForm";
        $("#formFbdwModule").load(getRootPath() + "/mock/view/showPage.action?disPath=" + moduleDisPath + "&id=" + id+"&page="+page, null, function () {
            myFbdwJsForm.init({title: '详细信息', id: id});
        });
    };

    $(document).ready(function () {
        myFbdwJsList.pageCount = TabNub > 0 ? TabNub : 10;
        myFbdwJsList.renderTable();
        keyFun.dataSrcSearchInit("query_dataSrc");
        $('#queryFbdwBtn').click(myFbdwJsList.renderTable);
        $('#resetFbdwBtn').click(myFbdwJsList.queryReset);
    });
</script>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>