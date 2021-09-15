<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">

        <c:choose>
            <c:when test="${mechType eq 'tower_crane' }">
                <div class="crumbs">
                    <span>塔机监控预警</span>
                    <span>塔机监控预警 > </span><span>塔吊实时统计</span>
                </div>
            </c:when>
            <c:when test="${mechType eq 'building_hoist' }">
                <div class="crumbs">
                    <span>塔机监控预警</span>
                    <span>塔机监控预警 > </span><span>升级实时统计</span>
                </div>
            </c:when>
            <c:otherwise>
                <div class="crumbs">
                    <span>塔机监控预警</span>
                    <span>塔机监控预警 > </span><span>视频实时统计</span>
                </div>
            </c:otherwise>
        </c:choose>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <input type="hidden" id="query_mechType" name="query_mechType" value="${mechType}"/>
            <form class="table-wrap form-table" id="searchForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width:10%;">项目编码</td>
                        <td style="width:40%;">
                            <input type="text" id="query_projectCode" name="query_projectCode"/>
                        </td>
                        <td style="width:10%;">项目名称</td>
                        <td style="width:40%;">
                            <input type="text" id="query_projectName" name="query_projectName"/>
                        </td>
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
            <table class="table table-striped tab_height first-td-tc" id="gridTable">
                <thead>
                <tr>
                    <th style="text-align:center;width:70px;">序号</th>
                    <th style="text-align:center">项目编码</th>
                    <th style="text-align:center">项目名称</th>
                    <th style="text-align:center">设备类型</th>
                    <th style="text-align:center">报警(个数)</th>
                    <th style="text-align:center">正常(个数)</th>
                    <th style="text-align:center">离线(个数)</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/warn/process/realtimeProcessList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>