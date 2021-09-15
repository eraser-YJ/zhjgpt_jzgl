<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable"> 
	<header class="con-header pull-in" id="header_1">
		<div class="con-heading fl" id="navigationMenu">
			<h1></h1>
			<div class="crumbs"></div>
		</div>
	</header>
	<section class="panel search-shrinkage clearfix">
	    <div class="search-line">
	    	<form class="table-wrap form-table" id="userRecycleQueryForm">
		        <table class="table table-td-striped"><tbody>
					<tr>
						<td style="width: 150px;">被删除日期</td>
						<td>
							<div class="input-group w-p100">
								<input class="datepicker-input" type="text" id="createDateBegin" name="createDateBegin" data-pick-time="true" data-date-format="yyyy-MM-dd HH:mm:ss" data-ref-obj="#createDateEnd lt">
								<div class="input-group-btn w30">-</div>
	 							<input class="datepicker-input" type="text" id="createDateEnd" name="createDateEnd" data-pick-time="true" data-date-format="yyyy-MM-dd HH:mm:ss" data-ref-obj="#createDateBegin gt">
							</div>
						</td>
						<td style="width: 200px; text-align: center;">
							<button class="btn dark" type="button" id="queryUserRecycle">查 询</button>
							<button class="btn" type="button" id="queryReset">重 置</button>
						</td>
					</tr>
				</tbody></table>
		    </form>
	    </div>
	</section>
	<section class="panel m-t-md" id="body">
		<h2 class="panel-heading clearfix">查询结果</h2>
		<div class="table-wrap">
			<table class="table table-striped tab_height first-td-tc" id="userRecycleTable"></table>
		</div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l">
				<button class="btn " id="deleteUserRecycles" type="button">批量删除</button>
			</section>
		</section>
	</section>
	<div id="userRecycleModuleDiv"></div>
</section>
<script src='${sysPath}/js/com/sys/userRecycle/userRecycleList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>