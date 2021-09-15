<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>人员实名制监控</span>
            <span>人员实名制监控 > </span><span>人员基本信息监控</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <input type="hidden" id="moduleDisPath" name="moduleDisPath" value="${disPath}"/>
            <form class="table-wrap form-table" id="searchRyjbxxForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width:10%;">项目名称</td>
                        <td style="width:40%;">
                            <input type="text" id="query_projectName" name="query_projectName"/>
                        </td>
                        <td style="width:10%;">数据来源</td>
                        <td style="width:40%;">
                            <select id="query_dataSrc" name="query_dataSrc"></select>
                        </td>
                    </tr>
                    <tr>
                        <td>姓名</td>
                        <td>
                            <input type="text" id="query_personName" name="query_personName"/>
                        </td>
                        <td>身份证号</td>
                        <td>
                            <input type="text" id="query_personCertNum" name="query_personCertNum"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div  class="btn-tiwc">
                    <button class="btn" type="button" id="queryRyjbxxBtn">查 询</button>
                    <button class="btn" type="button" id="resetRyjbxxBtn">重 置</button>
                </div>
            </form>
        </div>
    </section>
    <section class="panel ">
        <div class="table-wrap">
            <table class="table table-striped tab_height first-td-tc" id="gridRyjbxxTable"></table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <div id="formRyjbxxModule"></div>
</section>
<script>
    var myRyjbxxJsList = {};
    myRyjbxxJsList.oTable = null;

    myRyjbxxJsList.oTableFnServerParams = function (aoData) {
        getTableParameters(myRyjbxxJsList.oTable, aoData);

        var projectNameCondObj = $('#searchRyjbxxForm #query_projectName').val();
        if (projectNameCondObj && projectNameCondObj.length > 0) {
            aoData.push({"name": "projectName", "value": projectNameCondObj});
        }
        var query_dataSrc = $('#searchRyjbxxForm #query_dataSrc').val();
        if (query_dataSrc && query_dataSrc.length > 0) {
            aoData.push({"name": "dataSrc", "value": query_dataSrc});
        }

        var query_personName = $('#searchRyjbxxForm #query_personName').val();
        if (query_personName && query_personName.length > 0) {
            aoData.push({"name": "personName", "value": query_personName});
        }
        var query_personCertNum = $('#searchRyjbxxForm #query_personCertNum').val();
        if (query_personCertNum && query_personCertNum.length > 0) {
            aoData.push({"name": "personCertNum", "value": query_personCertNum});
        }
    };

    myRyjbxxJsList.oTableAoColumns = [
        {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
        {mData: 'project_number', sTitle: '项目编码', bSortable: false},
        {mData: 'project_name', sTitle: '项目名称', bSortable: false},
        {
            mData: function (source) {
                if (source.dataSrc) {
                    return keyFun.dataSrcValue(source.dataSrc);
                } else {
                    return "";
                }
            }, sTitle: '数据来源', bSortable: false
        },
        {mData: 'person_name', sTitle: '姓名', bSortable: false},
        {mData: 'person_cert_num', sTitle: '身份证号', bSortable: false},
        {mData: 'person_sex_value', sTitle: '性别', bSortable: false},
        {mData: 'person_nation_value', sTitle: '民族', bSortable: false},
        {mData: 'reg_type', sTitle: '注册类型', bSortable: false},
        {mData: 'reg_num', sTitle: '注册证书号', bSortable: false},
        {mData: 'person_tel', sTitle: '联系电话', bSortable: false},
        {mData: 'gwlx', sTitle: '岗位类型', bSortable: false},
        {
            mData: function (source) {
                var edit = "<a class=\"a-icon i-edit m-r-xs\"  href=\"#form-modal-Ryjbxx\"  onclick=\"myRyjbxxJsList.loadView('" + source.id + "')\" role=\"button\">查看</a>";
                return edit;
            }, sTitle: '操作', bSortable: false, sWidth: 70
        }
    ];

    myRyjbxxJsList.renderTable = function () {
        var moduleDisPath = $("#moduleDisPath").val();
        if (myRyjbxxJsList.oTable == null) {
            myRyjbxxJsList.oTable = $('#gridRyjbxxTable').dataTable({
                "iDisplayLength": myRyjbxxJsList.pageCount,
                "sAjaxSource": getRootPath() + "/mock/view/getList.action?disPath=" + moduleDisPath,
                "fnServerData": oTableRetrieveData,
                "aoColumns": myRyjbxxJsList.oTableAoColumns,
                "fnServerParams": function (aoData) {
                    myRyjbxxJsList.oTableFnServerParams(aoData);
                },
                aaSorting: [],
                aoColumnDefs: []
            });
        } else {
            myRyjbxxJsList.oTable.fnDraw();
        }
    };


    myRyjbxxJsList.queryReset = function () {
        $('#searchRyjbxxForm')[0].reset();
    };

    myRyjbxxJsList.loadView = function (id) {
        var moduleDisPath = $("#moduleDisPath").val();
        var page = "view." + moduleDisPath + ".dataForm";
        $("#formRyjbxxModule").load(getRootPath() + "/mock/view/showPage.action?disPath=" + moduleDisPath + "&id=" + id+"&page="+page, null, function () {
            myRyjbxxJsForm.init({title: '详细信息', id: id});
        });
    };

    $(document).ready(function () {
        myRyjbxxJsList.pageCount = TabNub > 0 ? TabNub : 10;
        myRyjbxxJsList.renderTable();
        keyFun.dataSrcSearchInit("query_dataSrc");
        $('#queryRyjbxxBtn').click(myRyjbxxJsList.renderTable);
        $('#resetRyjbxxBtn').click(myRyjbxxJsList.queryReset);
    });
</script>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>