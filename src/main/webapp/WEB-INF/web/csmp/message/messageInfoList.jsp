<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="navigationMenu">
        <div class="crumbs">
            <span>消息查看</span>
        </div>
    </header>
    <section class="panel ">
        <div class="table-wrap"><table class="table table-striped tab_height" id="gridTable"></table></div>
        <section class="clearfix" id="footer_height"></section>
    </section>
    <div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/message/messageInfoList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>