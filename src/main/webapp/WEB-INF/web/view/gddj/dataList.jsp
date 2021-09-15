<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>人员实名制监控</span>
            <span>人员实名制监控 > </span><span>工地对接情况</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <input type="hidden" id="moduleDisPath" name="moduleDisPath" value="${disPath}"/>
            <form class="table-wrap form-table" id="searchGddjForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width:10%;">项目名称</td>
                        <td style="width:40%;">
                            <input type="text" id="name" name="name"/>
                        </td>
                        <td style="width:10%;">建设单位名称</td>
                        <td style="width:40%;">
                            <input type="text" id="bseCompanyBuild" name="bseCompanyBuild"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div  class="btn-tiwc">
                    <button class="btn" type="button" id="queryGddjBtn">查 询</button>
                    <button class="btn" type="button" id="resetGddjBtn">重 置</button>
                </div>
            </form>
        </div>
    </section>
    <section class="panel ">
        <div class="table-wrap">
            <table class="table table-striped tab_height first-td-tc" id="gridGddjTable"></table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <div id="formGddjModule"></div>
</section>
<script>
    var myGddjJsList = {};
    myGddjJsList.oTable = null;

    myGddjJsList.oTableFnServerParams = function (aoData) {
        getTableParameters(myGddjJsList.oTable, aoData);

        var bseCompanyBuild = $('#searchGddjForm #bseCompanyBuild').val();
        if (bseCompanyBuild && bseCompanyBuild.length > 0) {
            aoData.push({"name": "bseCompanyBuild", "value": bseCompanyBuild});
        }
        var projectName = $('#searchGddjForm #name').val();
        if (projectName && projectName.length > 0) {
            aoData.push({"name": "name", "value": projectName});
        }
    };

    myGddjJsList.oTableAoColumns = [
        {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 70},
        {mData: 'name', sTitle: '项目名称', bSortable: false},
        {mData: 'address', sTitle: '项目所在地点', bSortable: false},
        {mData: 'bseCompanyBuild', sTitle: '建设单位名称', bSortable: false},
        {mData: 'bdate', sTitle: '计划开工日期', bSortable: false},
        {mData: 'edate', sTitle: '计划竣工日期', bSortable: false},
        {mData: 'bseCompanySupervise', sTitle: '监理企业名称', bSortable: false},
        {mData: 'bseCompanyRoadWork', sTitle: '施工总包企业名称', bSortable: false},
        {mData: 'buildNature', sTitle: '项目状态', bSortable: false,
            mData: function (source) {
                if (source.buildNature=='0') {
                    return "开工";
                }else if (source.buildNature=='1') {
                    return "完工";
                }else if (source.buildNature=='2') {
                    return "停工";
                }else if (source.buildNature=='3') {
                    return "复工";
                }else if (source.buildNature=='4') {
                    return "创建中";
                }else {
                    return "开工";
                }
            }
        },
        {mData: 'bseContyname', sTitle: '区域', bSortable: false},
        {
            mData: function (source) {
                if (source.src1 > 0) {
                    return "已对接";
                } else {
                    return "";
                }
            }, sTitle: '省平台对接情况', bSortable: false
        }
        // {
        //     mData: function (source) {
        //         if (source.src2 > 0) {
        //             return "已对接";
        //         } else {
        //             return "";
        //         }
        //     }, sTitle: '市平台对接情况', bSortable: false
        // }

    ];

    myGddjJsList.renderTable = function () {
        var moduleDisPath = $("#moduleDisPath").val();
        if (myGddjJsList.oTable == null) {
            myGddjJsList.oTable = $('#gridGddjTable').dataTable({
                "iDisplayLength": myGddjJsList.pageCount,
                "sAjaxSource": getRootPath() + "/mock/view/getList.action?disPath=" + moduleDisPath,
                "fnServerData": oTableRetrieveData,
                "aoColumns": myGddjJsList.oTableAoColumns,
                "fnServerParams": function (aoData) {
                    myGddjJsList.oTableFnServerParams(aoData);
                },
                aaSorting: [],
                aoColumnDefs: []
            });
        } else {
            myGddjJsList.oTable.fnDraw();
        }
    };


    myGddjJsList.queryReset = function () {
        $('#searchGddjForm')[0].reset();
    };

    myGddjJsList.loadView = function (id) {
        var moduleDisPath = $("#moduleDisPath").val();
        var page = "view." + moduleDisPath + ".dataForm";
        $("#formGddjModule").load(getRootPath() + "/mock/view/showPage.action?disPath=" + moduleDisPath + "&id=" + id+"&page="+page, null, function () {
            myGddjJsForm.init({title: '详细信息', id: id});
        });
    };

    $(document).ready(function () {
        myGddjJsList.pageCount = TabNub > 0 ? TabNub : 10;
        myGddjJsList.renderTable();
        $('#queryGddjBtn').click(myGddjJsList.renderTable);
        $('#resetGddjBtn').click(myGddjJsList.queryReset);
    });
</script>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>