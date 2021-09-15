<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>进度及周报管理</span>
            <span>进度及周报管理 > </span><span>项目甘特图</span>
        </div>
    </header>
    <section class="tree-fluid m-t-md">
        <aside class="tree-wrap" id="LeftHeight_box">
            <section class="panel" id="LeftHeight" style="overflow-y: auto;width: 250px !important;" style="position: initial !important;">
                <div id="treeDemo" class="ztree"></div>
            </section>
        </aside>
        <section class="panel" style="height: 809px;margin-top: 16px;float: left;width: 1385px;margin-left: 20px;">
            <div id="ganttChart" style="height: 100%;width: 100%;">
            </div>
        </section>
    </section>
</section>
<script src='${sysPath}/js/com/jc/common/echarts.js'></script>
<script src='${sysPath}/js/com/jc/csmp/project/info/ganttChart.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
