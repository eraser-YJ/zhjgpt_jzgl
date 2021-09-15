<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>人员实名制监控</span>
            <span>人员实名制监控 > </span><span>合同监控</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <input type="hidden" id="moduleDisPath" name="moduleDisPath" value="${disPath}"/>
            <form class="table-wrap form-table" id="searchHtxxForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width:10%;">合同期限类型</td>
                        <td style="width:40%;">
                            <dic:select name="query_contract_time_type" id="query_contract_time_type" dictName="contract_time_type" parentCode="data_center" defaultValue="" headName="-请选择-" headValue="" />
                        </td>
                        <td style="width:10%;">数据来源</td>
                        <td style="width:40%;">
                            <select id="query_dataSrc" name="query_dataSrc"></select>
                        </td>
                    </tr>
                    <tr>
                        <td>合同编码</td>
                        <td>
                            <input type="text" id="query_contract_num" name="query_contract_num"/>
                        </td>
                        <td>项目名称</td>
                        <td>
                            <input type="text" id="query_projectName" name="query_projectName"/>
                        </td>
                    </tr>

                    </tbody>
                </table>
                <div  class="btn-tiwc">
                    <button class="btn" type="button" id="queryHtxxBtn">查 询</button>
                    <button class="btn" type="button" id="resetHtxxBtn">重 置</button>
                </div>
            </form>
        </div>
    </section>
    <section class="panel ">
        <div class="table-wrap">
            <table class="table table-striped tab_height first-td-tc" id="gridHtxxTable"></table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <div id="formHtxxModule"></div>
</section>
<script>
    var myHtxxJsList = {};
    myHtxxJsList.oTable = null;

    myHtxxJsList.oTableFnServerParams = function (aoData) {
        getTableParameters(myHtxxJsList.oTable, aoData);

        var query_contract_time_type = $('#searchHtxxForm #query_contract_time_type').val();
        if (query_contract_time_type && query_contract_time_type.length > 0) {
            aoData.push({"name": "contractTimeType", "value": query_contract_time_type});
        }
        var query_dataSrc = $('#searchHtxxForm #query_dataSrc').val();
        if (query_dataSrc && query_dataSrc.length > 0) {
            aoData.push({"name": "dataSrc", "value": query_dataSrc});
        }

        var contract_num = $('#searchHtxxForm #query_contract_num').val();
        if (contract_num && contract_num.length > 0) {
            aoData.push({"name": "contractNum", "value": contract_num});
        }
        var projectName = $('#searchHtxxForm #query_projectName').val();
        if (projectName && projectName.length > 0) {
            aoData.push({"name": "projectName", "value": projectName});
        }
    };

    myHtxxJsList.oTableAoColumns = [
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
        {mData: 'contract_num', sTitle: '合同编号', bSortable: false},
        {mData: 'contract_rymc', sTitle: '人员姓名', bSortable: false},
        {mData: 'contract_project_name', sTitle: '项目名称', bSortable: false},
        {mData: 'contract_time_type_value', sTitle: '合同期限类型', bSortable: false},
        {
            mData: function (source) {
                if (source.contract_star) {
                    var crtTime = new Date(source.contract_star);
                    return dateFtt("yyyy-MM-dd", crtTime);
                } else {
                    return "";
                }
            }, sTitle: '开始日期', bSortable: false
        },
        {
            mData: function (source) {
                if (source.contract_end) {
                    var crtTime = new Date(source.contract_end);
                    return dateFtt("yyyy-MM-dd", crtTime);
                } else {
                    return "";
                }
            }, sTitle: '结束日期', bSortable: false
        },
        {
            mData: function (source) {
                var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#form-modal-Htxx\"  onclick=\"myHtxxJsList.loadView('" + source.id + "')\" role=\"button\">查看</a>";
                return edit;
            }, sTitle: '操作', bSortable: false, sWidth: 70
        }
    ];

    myHtxxJsList.renderTable = function () {
        var moduleDisPath = $("#moduleDisPath").val();
        if (myHtxxJsList.oTable == null) {
            myHtxxJsList.oTable = $('#gridHtxxTable').dataTable({
                "iDisplayLength": myHtxxJsList.pageCount,
                "sAjaxSource": getRootPath() + "/mock/view/getList.action?disPath=" + moduleDisPath,
                "fnServerData": oTableRetrieveData,
                "aoColumns": myHtxxJsList.oTableAoColumns,
                "fnServerParams": function (aoData) {
                    myHtxxJsList.oTableFnServerParams(aoData);
                },
                aaSorting: [],
                aoColumnDefs: []
            });
        } else {
            myHtxxJsList.oTable.fnDraw();
        }
    };


    myHtxxJsList.queryReset = function () {
        $('#searchHtxxForm')[0].reset();

    };

    myHtxxJsList.loadView = function (id) {
        var moduleDisPath = $("#moduleDisPath").val();
        var page = "view." + moduleDisPath + ".dataForm";
        $("#formHtxxModule").load(getRootPath() + "/mock/view/showPage.action?disPath=" + moduleDisPath + "&id=" + id+"&page="+page, null, function () {
            myHtxxJsForm.init({title: '详细信息', id: id});
        });
    };

    $(document).ready(function () {
        myHtxxJsList.pageCount = TabNub > 0 ? TabNub : 10;
        myHtxxJsList.renderTable();
        keyFun.dataSrcSearchInit("query_dataSrc");
        $('#queryHtxxBtn').click(myHtxxJsList.renderTable);
        $('#resetHtxxBtn').click(myHtxxJsList.queryReset);
    });
</script>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>