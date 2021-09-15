<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<h1></h1><div class="crumbs"></div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm">
				<table class="table table-td-striped">
					<tbody>
						<tr>
							<td class="w140">单位名称</td>
							<td><input type="text" id="companyName" name="companyName" /></td>
							<td class="w140">统一社会信用代码</td>
							<td><input type="text" id="creditCode" name="creditCode" /></td>
						</tr>
						<tr>
							<td class="w140">企业类型</td>
							<td><dic:select name="companyType" id="companyType" dictName="company_type" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
							<td style="text-align: center; background: #FFFFFF;" colspan="2">
								<button class="btn" type="button" id="queryBtn">查 询</button>
								<button class="btn" type="button" id="resetBtn">重 置</button>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</section>
	<section class="panel ">
		<h2 class="panel-heading clearfix">查询结果</h2>
		<div class="table-wrap"><table class="table table-striped tab_height" id="gridTable"></table></div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l">
				<a class="btn dark" role="button" id="addBtn">新 增</a>
			</section>
		</section>
	</section>
	<div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/company/cmCompanyList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
