<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/headEx.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="navigationMenu">
        <h1></h1>
        <div class="crumbs"></div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <h2 class="panel-heading clearfix">设备类型：${data.equipmentTypeValue}，设备编号：${data.equipmentCode} 的运行数据</h2>
        <div class="search-line">
            <input type="hidden" id="query_equiId" name="query_equiId" value="${data.id}"/>
            <form class="table-wrap form-table" id="searchForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width:7%">运行时间</td>
                        <td>
                            <div class="input-group w-p100">
                                <input class="datepicker-input" type="text" id="query_runTimeBegin"
                                       name="query_runTimeBegin" data-pick-time="true"
                                       data-date-format="yyyy-MM-dd HH:mm:ss" data-ref-obj="#query_runTimeEnd lt">
                                <div class="input-group-btn w30">-</div>
                                <input class="datepicker-input" type="text" id="query_runTimeEnd"
                                       name="query_runTimeEnd" data-pick-time="true"
                                       data-date-format="yyyy-MM-dd HH:mm:ss" data-ref-obj="#query_runTimeBegin gt">
                            </div>
                        </td>
                        <td style="width:8%">
                            <button class="btn" type="button" id="queryBtn">查 询</button>
                            <button class="btn" type="button" id="resetBtn">重 置</button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </section>

    <section class="panel " style="overflow-x:auto;">

        <div class="table-wrap" style="width:4000px;">
            <table class="table table-striped tab_height first-td-tc" id="gridTable" style="width:100%;"></table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/equipment/mechtype/${data.equipmentType}/runInfoList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>