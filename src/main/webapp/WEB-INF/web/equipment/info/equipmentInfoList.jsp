<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ taglib prefix="dicex" uri="/WEB-INF/tlds/dicex-tags.tld" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>设备信息</span>
			<span>设备信息 > </span><span>设备登记</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm">
				<table class="table table-td-striped">
					<tbody>
						<tr>
							<td style="width:7%">设备类型</td>
							<td style="width:38%">
								<dicex:typeTag name="query_equipmentType" id="query_equipmentType"  headName="-全部-" headValue=""/>
							</td>
							<td style="width:7%">设备编码</td>
							<td style="width:38%">
								<input type="text" id="query_equipmentCode" name="query_equipmentCode"/>
							</td>
						</tr>
						<tr>
							<td style="width:7%">项目编码</td>
							<td style="width:38%">
								<input type="text" id="query_projectCode" name="query_projectCode"/>
							</td>
							<td style="width:7%">项目名称</td>
							<td style="width:38%">
								<input type="text" id="query_projectName" name="query_projectName"/>
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
		<div class="table-wrap">
			<section class="form-btn fl m-l">
				<a class="btn dark" role="button" id="addBtn" style="margin: 0 !important;">新 增</a>
			</section>
			<table class="table table-striped tab_height first-td-tc" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height"></section>
	</section>
	<div id="formModule"></div>
	<div id="detailModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/equipment/info/equipmentInfoList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>