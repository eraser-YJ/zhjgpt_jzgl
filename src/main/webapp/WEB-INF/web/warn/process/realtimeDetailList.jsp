<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<script src='${sysPath}/process-editor/editor-app/libs/layer/layer.js'></script>
<script src='${sysPath}/js/com/jc/csmp/equipment/mechtype/${equipmentType}/detailType.js?n=123'></script>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">

        <c:choose>
            <c:when test="${mechType eq 'tower_crane' }">
                <div class="crumbs">
                    <span>塔机监控预警</span>
                    <span>塔机监控预警 > </span><span>塔吊实时监控</span>
                </div>
            </c:when>
            <c:when test="${mechType eq 'building_hoist' }">
                <div class="crumbs">
                    <span>塔机监控预警</span>
                    <span>塔机监控预警 > </span><span>升级实时监控</span>
                </div>
            </c:when>
            <c:otherwise>
                <div class="crumbs">
                    <span>塔机监控预警</span>
                    <span>塔机监控预警 > </span><span>视频实时监控</span>
                </div>
            </c:otherwise>
        </c:choose>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <input type="hidden" id="queryEquipmentType" name="queryEquipmentType" value="${equipmentType}">
            <form class="table-wrap form-table" id="searchForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width:7%">设备编码</td>
                        <td style="width:38%">
                            <input type="text" id="query_equipmentCode" name="query_equipmentCode"/>
                        </td>
                        <td style="width:7%">设备名称</td>
                        <td style="width:38%">
                            <input type="text" id="query_equipmentName" name="query_equipmentName"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:7%">项目编码</td>
                        <td style="width:38%">
                            <input type="text" id="query_projectCode" name="query_projectCode"/>
                        </td>
                        <td style="width:7%">项目名称</td>
                        <td style="width:38%">
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
            <table class="table table-striped tab_height first-td-tc" id="gridTable"></table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/warn/process/realtimeDetailList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>