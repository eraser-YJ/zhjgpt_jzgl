<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<script src="${sysPath}/js/com/archive/myCollectionFolder.js"></script>
<script type="text/javascript">
//显示没有行数据后面的操作按钮
	function oTableSetButtons( source) {
		var edit = "<a class=\"a-icon i-new\" href=\"#\" onclick=\"mailbox.get("+ source.id+ ")\" role=\"button\" data-toggle=\"modal\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"\" data-container=\"body\" data-original-title=\"配置\"></i></a>";
		var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"mailbox.deleteMailbox("+ source.id+ ")\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"\" data-container=\"body\" data-original-title=\"删除\"></a>";
		return edit +  del;
};
</script>
<section class="scrollable padder jcGOA-section" id="scrollable">
<header class="con-header pull-in" id="header_1">
    <div class="con-heading fl" id="navigationMenu">
        <h1></h1>
        <div class="crumbs"></div>
    </div>
</header>
<section class="scrollable padder padder-body">
   <!-- <form id="myCollectionListForm" class="table-wrap form-table">
       <table class="table table-td-striped">
           <tbody>
               <tr>
                   <td class="w50">文档名</td>
                   <td class="w50">
						<input id="dmName" name="dmName" class="datepicker-input" type="text">
                   </td>
                   <td class="w50">文档类型</td>
                   <td class="w50">
				    <select name="contentType" id="contentType">
						<option value="">-全部-</option>
						<option value="0">未知</option>
						<option value="1">word</option>
						<option value="2">excel</option>
						<option value="3">ppt</option>
					</select>
					</td>
               </tr>
           </tbody>
       </table>
       <section class="form-btn m-b-lg">
           <button id="myCollectionFolderBtn" name="myCollectionFolderBtn" class="btn dark query-jump" type="button">查 询</button>
           <button id="myCollectionFolderResetBtn" name="myCollectionFolderResetBtn" class="btn" type="button">重 置</button>
       </section>
   </form> -->
   
   <section class="panel email m-t-md">
<!--<h2 class="panel-heading clearfix">查询结果</h2>-->
	   <section class="email-btn form-btn document-btn">
		<button type="button" class="btn query-jump" id="deleteMyCollectionFolder">批量删除</button>
		<div role="search" class="email-search fr">
				<div class="input-append m-b-none">
					<input type="text" class="form-control aside-md w200"
						placeholder="请输入名称搜索当前页" id="search">
				</div>
			</div>
		</section>
	   <div class="table-wrap">
	       <table id="myCollectionList" class="document-table m-b-md list-table">
	           <thead>
	               <tr>
	                   <th><input type="checkbox" id="checkbox" name="checkbox"></th>
	                   <th class="w100" style="width:40%;">文件名</th>
	                   <!-- <th>文档类型</th> -->
	                   <th class="w100" style="width:8%;">类型</th>
	                   <th class="w100" style="width:12%;">文档大小</th>
	                   <th class="w100" style="width:8%;">版本号</th>
	                   <th class="w100" style="width:16%;">创建时间</th>
	                   <th class="w100" style="width:16%;">创建者</th>
	               </tr>
	           </thead>
	           <tbody>
	           </tbody>
	       </table>
	   </div>
	</section>
</section>
</section>
<!-- IE8水印 -->
 <%@ include file="/WEB-INF/web/include/foot.jsp"%>
