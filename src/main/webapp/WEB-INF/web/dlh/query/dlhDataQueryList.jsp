<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<div class="crumbs">
			<span>资源中心</span>
			<span>资源中心 > </span><span>数据列表</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
	    <div class="search-line">
	    	<form class="table-wrap form-table" id="dlhDataobjectQueryForm">
		        <table class="table table-td-striped">
		            <tbody>
						<tr>
							<td style="width:150px;">地址</td>
							<td>
						 		<input type="text" id="query_objUrl" name = "query_objUrl" value="">
 
							</td>						 
							<td style="width:150px;">说明</td>
							<td >
						 		<input type="text" id="query_objName" name = "query_objName" value=""> 
							</td>
							<td style="width: 120px; text-align: center; background: #FFFFFF;" rowspan="2">
								<button class="btn" type="button" id="queryBtn">查 询</button>
								<button class="btn" type="button" id="resetBtn">重 置</button>
							</td>
						</tr>
		            </tbody>
		        </table>
		       <%-- <section class="form-btn m-b-lg">
		            <button class="btn dark" type="button" id="queryDlhDataobject">查 询</button>
		            <button class="btn" type="button" id="queryReset">重 置</button>
		        </section>--%>
		    </form>
	    </div>

	</section>
	<section class="panel m-t-md" id="body">  
			<div class="table-wrap">
				<table class="table table-striped tab_height " id="dlhDataobjectTable">
					<thead>
						<tr>							
							<th>地址</th>
							<th>说明</th> 
							<th style="width:120px;">操作</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
				</section>
			</section>
	</section>
	<div id="dlhDataobjectModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/dlh/query/dlhDataQueryList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>