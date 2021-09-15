<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="navigationMenu">
        <div class="crumbs">
            <span>项目管理</span>
            <span>进度及周报管理 > </span><span>项目计划模板管理</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <form class="table-wrap form-table" id="searchForm">
                <table class="table table-td-striped" style="width:100% !important; margin-bottom:10px;">
                    <tbody>
                    <tr>
                        <td style="width: 80px">模板名称</td>
                        <td><input type="text" id="templateName" name="templateName" /></td>
                    </tr>
                    </tbody>
                </table>
                <div  class="btn-tiwc">
                    <button class="btn" type="button" id="queryBtn">查 询</button>
                    <button class="btn" type="button" id="resetBtn">重 置</button>
                </div>
            </form>
        </div>
    </section>
    <section class="panel ">
        <div class="table-wrap">
            <section class="form-btn fl m-l"><a class="btn dark" role="button" id="addBtn" style="margin: 0 !important;">新 增</a></section>
            <table class="table table-striped tab_height" id="gridTable"></table>
        </div>
        <section class="clearfix" id="footer_height"></section>
    </section>
    <div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/project/plan/template/cmProjectPlanTempateList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
