<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="navigationMenu">
        <div class="crumbs">
            <span>行业监管</span>
            <span>招标中标统计> </span><span>行政监督汇总统计</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <div id="xzjd" style="height: 240px;width: 100%;">
            </div>
        </div>
    </section>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <form class="table-wrap form-table" id="searchForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width: 10%;">项目名称</td>
                        <td style="width: 40%;"><input type="text" id="projectName" name="projectName" /></td>
                        <td style="width: 10%;">监督类型</td>
                        <td style="width: 40%;"><dic:select name="jdType" id="jdType" dictName="jd_type" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
                    </tr>
                    <tr>
                        <td style="text-align: right; background: #FFFFFF;" colspan="4">
                            <button class="btn" type="button" id="queryBtn">查 询</button>
                            <button class="btn" type="button" id="resetBtn">重 置</button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </section>
    <section class="panel ">
        <button class="btn daochu" style="float: right;margin-right: 30px;margin-top: 10px;background-color: #1572e8;color: white;" type="button" id="excel">导 出</button>
        <div class="table-wrap"><table class="table table-striped tab_height" id="gridTable"></table></div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
</section>
<script src='${sysPath}/js/com/jc/common/echarts.js'></script>
<script src='${sysPath}/js/com/jc/csmp/ptProjectZtb/companyProjectsZtbXzjdList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>