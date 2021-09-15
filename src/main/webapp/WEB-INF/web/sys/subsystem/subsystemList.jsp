<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable"> 
	<header class="con-header pull-in" id="header_1"><div class="con-heading fl"><h1></h1><div class="crumbs"></div></div></header>
	<section class="panel m-t-md" id="body">
		<h2 class="panel-heading clearfix">查询结果</h2>
		<div class="table-wrap"><table class="table table-striped tab_height first-td-tc" id="subsystemTable"></table></div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l">
				<a class="btn dark" role="button" id="addSubsystemButton">新 增</a>
				<button class="btn " id="deleteSubsystems" type="button">批量删除</button>
			</section>
		</section>
	</section>
	<div id="subsystemModuleDiv"></div>
</section>
<script src='${sysPath}/js/com/sys/subsystem/subsystemList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>