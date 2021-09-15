<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%--<%@ include file="/WEB-INF/web/include/jctree.jsp"%>--%>
<%@ include file="/WEB-INF/web/include/webupload.jsp" %>
<%@ taglib prefix="dicex" uri="/WEB-INF/tlds/dicex-tags.tld" %>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable">
	<section class="panel search-shrinkage clearfix">
		<input type="hidden" id="query_modelId" name = "query_modelId" value="${modelId}">
	    <%--<div class="search-line hide">
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
	    </div>--%>

	</section>
	<section class="panel m-t-md" id="body">
		<form class="table-wrap form-table" action="${sysPath}/api/dlh/push/save.action" method="post"  id="dlhDatamodelItemSaveForm">
			<h2 class="panel-heading clearfix">查询结果</h2>
			<div class="table-wrap">
				<table class="table table-striped tab_height first-td-tc" Content-Type="" id="dlhDatamodelItemTable">
					<thead>
						<tr>
							<%--<th class="w46"><input type="checkbox" /></th>--%>
							<th style="width:300px;">列描述</th>
							<th>值</th>
							<th style="width:300px;">列名</th>
							<th style="width:300px;">列类型</th>


						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
					<a class="btn dark" role="button" id="addDlhDatamodelItemButton">新 增</a>
					<%--<button class="btn " id="deleteDlhDatamodelItems" type="button">批量删除</button>
					<a class="btn" type="button" href="${sysPath}/resource/template/数据模型模板.xlsx">下载模板</a>
					<input id="excel" type="file" size="45" name="excelFile">
					<a class="btn dark" role="button" id="buttonImport">导 入</a>--%>
					<%--<buttno class="btn " type="submit">提交</buttno>--%>
				</section>
			</section>
		</form>
	</section>
	<div id="dlhDatamodelItemModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/dlh/dlhDatamodelItem/mysql/dlhDatamodelFormList.js'></script>
<script src='${sysPath}/js/lib/jquery/ajaxfileupload.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>