<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable"> 
	<header class="con-header pull-in" id="header_1">
		<div class="con-heading fl" id="navigationMenu">
			<h1></h1>
			<div class="crumbs"></div>
		</div>
	</header>
	<section class="panel clearfix">
		<h2 class="panel-heading clearfix">当前在线人员</h2>
		<div class="table-wrap">
			<div id="userOnLineTree"></div>
		</div>
	</section>
</section>
<script src='${sysPath}/js/com/sys/sessionService/sessionServiceList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>