<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<script >
$.ajaxSetup ({
	 cache: false //设置成false将不会从浏览器缓存读取信息
	});
</script>
<script src="${sysPath}/js/com/sys/dic/dic.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/dic/dics.validate.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/dic/Data.dictionary.js" type="text/javascript"></script>
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
			<h2 class="panel-heading clearfix" id="dicRelation">录入数据类型配置</h2>
			 <form class="table-wrap form-table-h" id="dicsname"><!-- id="zdsz" -->
			 	<input type="hidden" id="token" name="token" value="${data.token}">
				<table class="table table-striped first-td-tc" id="dictable">
					<thead>
						<tr>
							<th class="w46">编号</th>
							<th><span class="required">*</span>内容</th>
							<th><span class="required">*</span>编码</th>
							<th>父字典类型</th>
							<th class="w-p1">启用标志</th>
							<th class="w-p1">默认值</th>
							<th class="w-p1">操作</th>
						</tr>
					</thead>
					<tbody id="dicinfolist">
						
					</tbody>
				</table>
				<section class="form-btn m-b-lg">
					<button class="btn dark" type="button" id="dicbtn">保 存</button>
					<button class="btn" type="button" id="operAddRow">增 行</button>
				</section>    
			 </form>
		</section>
	</section>
</section>
</section>
<script src="${sysPath}/js/common-tree.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>