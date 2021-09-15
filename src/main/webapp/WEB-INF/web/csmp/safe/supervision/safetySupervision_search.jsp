<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>

<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="con-heading fl" id="navigationMenu">
            <div class="crumbs"></div>
        </div>
    </header>
    <!--start 查询条件-->
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line hide">
            <form class="table-wrap form-table" id="searchForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td class="w140">工程名称</td>
                        <td>
                            <input type="text" id="query_projectName" name="query_projectName"/>
                        </td>
                        <td class="w140">建设性质</td>
                        <td>
                            <%--<input type="text" id="query_buildProperties" name="query_buildProperties"/>--%>
                            <dic:select name="query_buildProperties" id="query_buildProperties" dictName="build_property" parentCode="csmp" defaultValue="" headType="2" headValue="" />
                        </td>
                    </tr>
                    <tr>
                        <td class="w140">投资类别</td>
                        <td>
                            <%--	<input type="text" id="query_investmentCategory" name="query_investmentCategory"/>--%>
                            <dic:select name="query_investmentCategory" id="query_investmentCategory" dictName="investment_category" parentCode="csmp" defaultValue="" headType="2" headValue="" />
                        </td>
                        <td class="w140">工程类型</td>
                        <td>
                            <%--<input type="text" id="query_projectType" name="query_projectType"/>--%>
                            <dic:select name="query_projectType" id="query_projectType" dictName="project_type" parentCode="csmp" defaultValue="" headType="2" headValue="" />
                        </td>
                    </tr>
                    <tr>
                        <td class="w140">结构类型</td>
                        <td>
                            <%--<input type="text" id="query_structureType" name="query_structureType"/>--%>
                            <dic:select name="query_structureType" id="query_structureType" dictName="structure_type" parentCode="csmp" defaultValue="" headType="2" headValue="" />
                        </td>
                        <td colspan="2"></td>
                    </tr>
                    </tbody>
                </table>
                <section class="form-btn m-b-lg">
                    <button class="btn dark query-jump" type="button" id="queryBtn">查 询</button>
                    <button class="btn" type="reset" id="resetBtn">重 置</button>
                </section>
            </form>
        </div>
        <%@include file= "/WEB-INF/web/include/searchConditionHide.jsp"%>
    </section>
    <section class="panel clearfix" id="sendPassTransact-list">
        <div class="table-wrap">
            <table class="table table-striped tab_height" id="gridTable">
            </table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
</section>

<script src='${sysPath}/js/com/jc/csmp/safe/supervision/safetySupervision_searchList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>