<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>设备信息</span>
			<span>设备信息 > </span><span>规则管理</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm">
				<table class="table table-td-striped">
					<tbody>
						<tr>
							<td style="width:10%;">规则编码</td>
							<td style="width:40%;">
								<input type="text" id="query_ruleCode" name="query_ruleCode"/>
							</td>
							<td style="width:10%;">规则名称</td>
							<td style="width:40%;">
								<input type="text" id="query_ruleName" name="query_ruleName"/>
							</td>

						</tr>
					</tbody>
				</table>
				<div  class="btn-tiwc">
					<button class="btn" type="button" id="queryBtn">查 询</button>
					<button class="btn" type="button" id="resetBtn">重 置</button>
				</div>
			</form>
		</div>
	</section>
	<section class="panel ">
		<div class="table-wrap"><table class="table table-striped tab_height first-td-tc" id="gridTable"></table></div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l">
				<a class="btn dark" role="button" id="addBtn">新 增</a>
			</section>
		</section>
	</section>
	<div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/warn/rule/warnRuleList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>