<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="con-heading fl" >
			<h1></h1>
			<div class="crumbs"></div>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
	 		<form class="table-wrap form-table" id="userIpListForm">
				<table class="table table-td-striped"><tbody>
					<tr>
						<td class="w140">绑定用户</td><td><div id="userTree"></div></td>
						<td style="width: 200px; text-align: center;">
							<button class="btn dark" type="button" id="queryuserIp">查 询</button>
							<button class="btn" id="queryReset" type="reset">重 置</button>
						</td>
					</tr>
				</tbody></table>
			</form>
		</div>
	</section>
	<section class="panel">
		<h2 class="panel-heading clearfix">查询结果</h2>
		<div class="table-wrap">
			<table class="table table-striped tab_height first-td-tc" id="userIpTable"></table>
		</div>
		<section class="clearfix">
			<section class="form-btn fl m-l">
				<a class="btn dark" type="button" href="#myModal-edit" id="userIpBottom" role="button" data-toggle="modal">新 增</a>
				<button class="btn" type="button" id="deleteuserIps">批量删除</button>
			</section>
			<section class="pagination m-r fr m-t-none"></section>
		</section>
	</section>
</section>
<div id="userIpEdit"></div>
<script src="${sysPath}/js/com/sys/userIp/userIp.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>