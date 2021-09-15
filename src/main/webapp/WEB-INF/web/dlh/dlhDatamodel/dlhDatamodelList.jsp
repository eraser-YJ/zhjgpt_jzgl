<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable"> 
	<section class="panel search-shrinkage clearfix">
	    <div class="search-line hide">
	    	<form class="table-wrap form-table" id="dlhDatamodelQueryForm">
		        <table class="table table-td-striped">
		            <tbody>
						<tr>
							<td style="width:150px;">表名</td>
							<td>
						 		<input type="text" id="query_tableCode" name = "query_tableCode" value=""> 
							</td>
							<td style="width:150px;">说明</td>
							<td>
						 		<input type="text" id="query_tableName" name = "query_tableName" value=""> 
							</td>
						</tr>
		            </tbody>
		        </table>
		        <section class="form-btn m-b-lg">
		            <button class="btn dark" type="button" id="queryDlhDatamodel">查 询</button>
		            <button class="btn" type="button" id="queryReset">重 置</button>
		        </section>
		    </form>
	    </div>
	    <%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%>
	</section>
	<section class="panel m-t-md" id="body">
			<h2 class="panel-heading clearfix">查询结果</h2>
			<div class="table-wrap">
				<table class="table table-striped tab_height" id="dlhDatamodelTable">
					<thead>
						<tr>  
							<th>模型编码</th>
							<th>表名</th>
							<th>说明</th>
							<th style="width:300px;">操作</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
					<a class="btn dark" role="button" id="addDlhDatamodelButton">新 增</a> 
				</section>
			</section>
	</section>
	<div id="dlhDatamodelModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/dlh/dlhDatamodel/dlhDatamodelList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>