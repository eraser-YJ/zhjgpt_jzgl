<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable"> 
	<section class="panel search-shrinkage clearfix">
	    <div class="search-line hide">
	    	<form class="table-wrap form-table" id="dlhDataobjectQueryForm">
		        <table class="table table-td-striped">
		            <tbody>
						<tr>
							<td style="width:150px;">模型编码</td>
							<td>
						 		<input type="text" id="query_modelId" name = "query_modelId" value="">
 
							</td>
							<td style="width:150px;">地址</td>
							<td>
						 		<input type="text" id="query_objUrl" name = "query_objUrl" value="">
 
							</td>
						</tr>
						<tr>
							<td>说明</td>
							<td colspan="3">
						 		<input type="text" id="query_objName" name = "query_objName" value="">
 
							</td>
						</tr>
		            </tbody>
		        </table>
		        <section class="form-btn m-b-lg">
		            <button class="btn dark" type="button" id="queryDlhDataobject">查 询</button>
		            <button class="btn" type="button" id="queryReset">重 置</button>
		        </section>
		    </form>
	    </div>
	    <%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%>
	</section>
	<section class="panel m-t-md" id="body"> 
			<h2 class="panel-heading clearfix">查询结果</h2>
			<div class="table-wrap">
				<table class="table table-striped tab_height " id="dlhDataobjectTable">
					<thead>
						<tr>							
							<th>地址</th>
							<th>说明</th>
							<th>模型编码</th>
							<th style="width:210px;">操作</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
					<a class="btn dark" role="button" id="addDlhDataobjectButton">新 增</a> 
				</section>
			</section>
	</section>
	<div id="dlhDataobjectModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/dlh/dlhDataobject/dlhDataobjectList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>