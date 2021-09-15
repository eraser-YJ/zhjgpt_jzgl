<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<script>
	var oTableAoColumnsList = [];
	<c:forEach items="${disItemList}"  var="entry" varStatus="status">
	oTableAoColumnsList[oTableAoColumnsList.length] = {'itemName':'${entry.itemCode}','disName':'${entry.itemName}'}
	</c:forEach>
</script>
<script src='${sysPath}/js/com/jc/csmp/equipment/mechtype/${mechType}/detailType.js?n=123'></script>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">

<c:choose>
	<c:when test="${mechType eq 'tower_crane' }">
		<div class="crumbs">
			<span>塔机监控预警</span>
			<span>塔机监控预警 > </span><span>塔吊预警信息</span>
		</div>
	</c:when>
	<c:when test="${mechType eq 'building_hoist' }">
		<div class="crumbs">
			<span>塔机监控预警</span>
			<span>塔机监控预警 > </span><span>升级机预警信息</span>
		</div>
	</c:when>
	<c:otherwise>
		<div class="crumbs">
			<span>视频监控预警</span>
			<span>视频监控预警 > </span><span>视频预警</span>
		</div>
	</c:otherwise>
</c:choose>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<input type="hidden" id="query_mechType" name="query_mechType" value="${mechType}"/>
			<form class="table-wrap form-table" id="searchForm">
				<table class="table table-td-striped">
					<tbody>
						<tr>
							<td style="width:7%">项目</td>
							<td >
								<input type="text" id="query_targetProjectName" name="query_targetProjectName"/>
							</td>
							<td style="width:7%">报警类型</td>
							<td >
								<select id="query_warnReasonCode" name="query_warnReasonCode"></select>
							</td>

							<td style="width:7%">设备编码</td>
							<td >
								<input type="text" id="query_targetCode" name="query_targetCode"/>
							</td>
							<td style="width:7%">报警时间</td>
							<td >
								<div class="input-group w-p100">
									<input class="datepicker-input" type="text" id="query_warnTimeBegin" name="query_warnTimeBegin" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_warnTimeEnd lt">
									<div class="input-group-btn w30">-</div>
									<input class="datepicker-input" type="text" id="query_warnTimeEnd" name="query_warnTimeEnd" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_warnTimeBegin gt">
								</div>
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
			</section>
		</section>
	</section>
	<div id="formModule"></div>
	<div id="detailDataModule"></div>
</section>


<script src='${sysPath}/js/com/jc/csmp/warn/info/warnInfoList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>