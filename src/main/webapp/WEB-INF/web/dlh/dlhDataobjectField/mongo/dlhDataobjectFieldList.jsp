<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable">
	<section class="panel search-shrinkage clearfix">
	    <div class="search-line hide">
	    	<form class="table-wrap form-table" id="dlhDataobjectFieldQueryForm">
		        <table class="table table-td-striped">
		        <input type="hidden" id="query_modelId" name = "query_modelId" value="${modelId}">
		        <input type="hidden" id="query_objectId" name = "query_objectId" value="${objectId}">
		            <tbody>
						<tr>
							<td style="width:150px;">属性名</td>
							<td>
						 		<input type="text" id="query_fieldName" name = "query_fieldName" value=""> 
							</td>
						</tr>
		            </tbody>
		        </table>
		        <section class="form-btn m-b-lg">
		            <button class="btn dark" type="button" id="queryDlhDataobjectField">查 询</button>
		            <button class="btn" type="button" id="queryReset">重 置</button>
		        </section>
		    </form>
	    </div>
	    <%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%>
	</section>
	<section class="panel m-t-md" id="body">
	    
			<h2 class="panel-heading clearfix">查询结果</h2>
			<div class="table-wrap">
				<table class="table table-striped tab_height first-td-tc" id="dlhDataobjectFieldTable">
					<thead>
						<tr>
							<th class="w46"><input type="checkbox" /></th>
							<th>属性</th>
							<th>属性名</th>
							<th>列名</th>
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
					<a class="btn dark" role="button" id="addDlhDataobjectFieldButton">新 增</a>
					<button class="btn " id="deleteDlhDataobjectFields" type="button">批量删除</button>
				</section>
			</section>
	</section>
	<div id="dlhDataobjectFieldModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/dlh/dlhDataobjectField/mongo/dlhDataobjectFieldList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>