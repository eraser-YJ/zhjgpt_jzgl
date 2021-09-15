<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<script >
$.ajaxSetup ({
	 cache: false //设置成false将不会从浏览器缓存读取信息
	});
</script>
<script src="${sysPath}/js/com/sys/dic/dicinfo.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/dic/dicsinfo.validate.js" type="text/javascript"></script>
<section class="scrollable padder jcGOA-section" id="scrollable">
<header class="con-header pull-in" id="header_1">
    <div class="con-heading fl" id="navigationMenu">
        <h1></h1>
        <div class="crumbs"></div>
    </div>
</header>
<section class="tree-fluid m-t-md">
	<aside class="tree-wrap" id="LeftHeight_box">
		<section class="panel" id="LeftHeight" style="overflow-y: auto;">
			<h2 class="panel-heading clearfix" id="header_2">字典类型</h2>
			<ul class="m-l-md dictionary-nav m-t" id="treeDemo"><!-- id="diclefttree" -->
			</ul>
		</section>
	</aside>
	<section class="tree-right">
		<section class="panel">
			<h2 class="panel-heading clearfix" id="dicInfo">字典类型信息管理</h2>
			 <form class="table-wrap form-table-h" id="dicInfoname"><!-- id="zdsz" -->
			 	   
			 </form>
		</section>
	</section>
</section>
</section>
<script src="${sysPath}/js/common-tree.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>