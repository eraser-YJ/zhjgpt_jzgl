<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%--<%@ include file="/WEB-INF/web/include/jctree.jsp"%>--%>
<%@ include file="/WEB-INF/web/include/webupload.jsp" %>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable"> 
	<section class="panel search-shrinkage clearfix">
	    <div class="search-line hide">
	    	<form class="table-wrap form-table" id="dlhDatamodelItemQueryForm">
		        <table class="table table-td-striped">
		        	<input type="hidden" id="query_modelId" name = "query_modelId" value="${modelId}">
		            <tbody>
						<tr>
							<td style="width:150px;">列名</td>
							<td>
						 		<input type="text" id="query_itemName" name = "query_itemName" value=""> 
							</td>
							<td style="width:150px;">列描述</td>
							<td>
						 		<input type="text" id="query_itemComment" name = "query_itemComment" value=""> 
							</td>
						</tr>
		            </tbody>
		        </table>
		        <section class="form-btn m-b-lg">
		            <button class="btn dark" type="button" id="queryDlhDatamodelItem">查 询</button>
		            <button class="btn" type="button" id="queryReset">重 置</button>
		        </section>
		    </form>
	    </div>
	    <%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%>
	</section>
	<section class="panel m-t-md" id="body">
	    
			<h2 class="panel-heading clearfix">查询结果</h2>
			<div class="table-wrap">
				<table class="table table-striped tab_height first-td-tc" id="dlhDatamodelItemTable">
					<thead>
						<tr>
							<th class="w46"><input type="checkbox" /></th>
							<th>列名</th>
							<th>列描述</th>
							<th>列类型</th>
							<th>列长度</th>
							<th>是否主键</th>
							<th>排序</th>
							<th class="w170">操作</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
					<a class="btn dark" role="button" id="addDlhDatamodelItemButton">新 增</a>
					<button class="btn " id="deleteDlhDatamodelItems" type="button">批量删除</button>
				</section>
			</section>
	</section>
	<div id="dlhDatamodelItemModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/dlh/dlhDatamodelItem/mongo/dlhDatamodelItemList.js'></script>
<script src='${sysPath}/js/lib/jquery/ajaxfileupload.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>