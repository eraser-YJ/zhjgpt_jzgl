<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<h1></h1><div class="crumbs"></div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
	    <div class="search-line">
	    	<form class="table-wrap form-table" id="dlhDbsourceQueryForm">
		        <table class="table table-td-striped">
		            <tbody>
						<tr>
							<td style="width:150px;">数据源编码</td>
							<td>
								<input type="text" id="query_dbCode" name = "query_dbCode" value="">
 
							</td>
							<td style="text-align: center; background: #FFFFFF;" colspan="2">
								<button class="btn" type="button" id="queryDlhDbsource">查 询</button>
								<button class="btn" type="button" id="queryReset">重 置</button>
							</td>
						</tr>
		            </tbody>
		        </table>
		        <%--<section class="form-btn m-b-lg">
		            <button class="btn dark" type="button" id="queryDlhDbsource">查 询</button>
		            <button class="btn" type="button" id="queryReset">重 置</button>
		        </section>--%>
		    </form>
	    </div>

	</section>
	<section class="panel m-t-md" id="body">  
			<div class="table-wrap">
				<table class="table table-striped tab_height" id="dlhDbsourceTable">
					<thead>
						<tr>
							<th class="w170">数据源编码</th>
							<th class="w170">数据源类型</th>
							<th >数据源地址</th>
							<th class="w100">操作</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
					<a class="btn dark" role="button" id="addDlhDbsourceButton">新 增</a>
				</section>
			</section>
	</section>
	<div id="dlhDbsourceModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/dlh/dlhDbsource/dlhDbsourceList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>