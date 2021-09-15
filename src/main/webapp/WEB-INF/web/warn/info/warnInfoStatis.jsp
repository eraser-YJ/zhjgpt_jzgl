<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="navigationMenu">
        <h1></h1>
        <div class="crumbs"></div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <input type="hidden" id="query_mechType" name="query_mechType" value="${mechType}"/>
            <form class="table-wrap form-table" id="searchForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width:7%">报警时间</td>
                        <td  >
                            <div class="input-group w-p100">
                                <input class="datepicker-input" type="text" id="query_warnTimeBegin" name="query_warnTimeBegin" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_warnTimeEnd lt">
                                <div class="input-group-btn w30">-</div>
                                <input class="datepicker-input" type="text" id="query_warnTimeEnd" name="query_warnTimeEnd" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_warnTimeBegin gt">
                            </div>
                        </td>
                        <td style="width:10%">
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
        <input type="hidden" id="queryProjectCode" name="queryProjectCode" value="${projectCode}">
        <div class="table-wrap">
            <table class="table table-striped tab_height first-td-tc" id="gridTable">
                <thead>
                <tr>
                    <th style="width:100px;">序号</th>
                    <th>项目</th>
                    <th>类型</th>
                    <th>未处理数</th>
                    <th>处置数</th>
                </tr>
                </thead>
                <tbody id="gridTableBody"></tbody>
            </table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/warn/info/warnInfoStatis.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>