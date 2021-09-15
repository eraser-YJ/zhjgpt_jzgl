<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>人员实名制监控</span>
            <span>人员实名制监控 > </span><span>班组监管</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <input type="hidden" id="moduleDisPath" name="moduleDisPath" value="${disPath}"/>
            <form class="table-wrap form-table" id="searchBzxxForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width:10%;">班组编码</td>
                        <td style="width:40%;">
                            <input type="text" id="query_class_num" name="query_class_num"/>
                        </td>
                        <td  style="width:10%;">数据来源</td>
                        <td  style="width:40%;">
                            <select id="query_dataSrc" name="query_dataSrc"></select>
                        </td>
                    </tr>
                    <tr>
                        <td>班组名称</td>
                        <td>
                            <input type="text" id="query_class_bzmc" name="query_class_bzmc"/>
                        </td>
                        <td>项目名称</td>
                        <td>
                            <input type="text" id="query_projectName" name="query_projectName"/>
                        </td>
                    </tr>

                    </tbody>
                </table>
                <div  class="btn-tiwc">
                    <button class="btn" type="button" id="queryBzxxBtn">查 询</button>
                    <button class="btn" type="button" id="resetBzxxBtn">重 置</button>
                </div>
            </form>
        </div>
    </section>
    <section class="panel ">
        <div class="table-wrap">
            <table class="table table-striped tab_height first-td-tc" id="gridBzxxTable"></table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <div id="formBzxxModule"></div>
</section>
<script>
    var myBzxxJsList = {};
    myBzxxJsList.oTable = null;

    myBzxxJsList.oTableFnServerParams = function (aoData) {
        getTableParameters(myBzxxJsList.oTable, aoData);

        var query_class_num = $('#searchBzxxForm #query_class_num').val();
        if (query_class_num && query_class_num.length > 0) {
            aoData.push({"name": "classNum", "value": query_class_num});
        }
        var query_dataSrc = $('#searchBzxxForm #query_dataSrc').val();
        if (query_dataSrc && query_dataSrc.length > 0) {
            aoData.push({"name": "dataSrc", "value": query_dataSrc});
        }

        var class_bzmc = $('#searchBzxxForm #query_class_bzmc').val();
        if (class_bzmc && class_bzmc.length > 0) {
            aoData.push({"name": "classBzmc", "value": class_bzmc});
        }
        var projectName = $('#searchBzxxForm #query_projectName').val();
        if (projectName && projectName.length > 0) {
            aoData.push({"name": "projectName", "value": projectName});
        }
    };

    myBzxxJsList.oTableAoColumns = [
        {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
        {mData: 'projectName', sTitle: '项目名称', bSortable: false},
        {
            mData: function (source) {
                if (source.dataSrc) {
                    return keyFun.dataSrcValue(source.dataSrc);
                } else {
                    return "";
                }
            }, sTitle: '数据来源', bSortable: false
        },
        {mData: 'class_num', sTitle: '班组编码', bSortable: false},
        {mData: 'class_bzmc', sTitle: '班组名称', bSortable: false},
        {mData: 'class_bzgz', sTitle: '班组工种', bSortable: false},
        {
            mData: function (source) {
                if (source.class_jcrq) {
                    var crtTime = new Date(source.class_jcrq);
                    return dateFtt("yyyy-MM-dd", crtTime);
                } else {
                    return "";
                }
            }, sTitle: '进场日期', bSortable: false
        },
        {
            mData: function (source) {
                if (source.class_ccrq) {
                    var crtTime = new Date(source.class_ccrq);
                    return dateFtt("yyyy-MM-dd", crtTime);
                } else {
                    return "";
                }
            }, sTitle: '离场日期', bSortable: false
        },
        {
            mData: function (source) {
                var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#form-modal-Bzxx\"  onclick=\"myBzxxJsList.loadView('" + source.id + "')\" role=\"button\">查看</a>";
                return edit;
            }, sTitle: '操作', bSortable: false, sWidth: 70
        }
    ];

    myBzxxJsList.renderTable = function () {
        var moduleDisPath = $("#moduleDisPath").val();
        if (myBzxxJsList.oTable == null) {
            myBzxxJsList.oTable = $('#gridBzxxTable').dataTable({
                "iDisplayLength": myBzxxJsList.pageCount,
                "sAjaxSource": getRootPath() + "/mock/view/getList.action?disPath=" + moduleDisPath,
                "fnServerData": oTableRetrieveData,
                "aoColumns": myBzxxJsList.oTableAoColumns,
                "fnServerParams": function (aoData) {
                    myBzxxJsList.oTableFnServerParams(aoData);
                },
                aaSorting: [],
                aoColumnDefs: []
            });
        } else {
            myBzxxJsList.oTable.fnDraw();
        }
    };


    myBzxxJsList.queryReset = function () {
        $('#searchBzxxForm')[0].reset();
    };

    myBzxxJsList.loadView = function (id) {
        var moduleDisPath = $("#moduleDisPath").val();
        var page = "view." + moduleDisPath + ".dataForm";
        $("#formBzxxModule").load(getRootPath() + "/mock/view/showPage.action?disPath=" + moduleDisPath + "&id=" + id+"&page="+page, null, function () {
            myBzxxJsForm.init({title: '详细信息', id: id});
        });
    };

    $(document).ready(function () {
        myBzxxJsList.pageCount = TabNub > 0 ? TabNub : 10;
        myBzxxJsList.renderTable();
        keyFun.dataSrcSearchInit("query_dataSrc");
        $('#queryBzxxBtn').click(myBzxxJsList.renderTable);
        $('#resetBzxxBtn').click(myBzxxJsList.queryReset);
    });
</script>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>