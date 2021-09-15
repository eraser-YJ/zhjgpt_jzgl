<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>视频监控预警</span>
			<span>视频监控预警 > </span><span>抓图记录</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm">
				<table class="table table-td-striped">
					<tbody>
						<tr>
							<td style="width:10%;">设备编码</td>
							<td style="width:40%;">
								<input type="text" id="query_equiCode" name="query_equiCode"/>
							</td>
							<td style="width:10%;">上传时间</td>
							<td style="width:40%;">
								<div class="input-group w-p100">
									<input class="datepicker-input" type="text" id="query_createDateBegin" name="query_createDateBegin" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_createDateEnd lt">
									<div class="input-group-btn w30">-</div>
									<input class="datepicker-input" type="text" id="query_createDateEnd" name="query_createDateEnd" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#query_createDateBegin gt">
								</div>
							</td>
							<td style="width:150px;" >
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
		<div class="table-wrap"><table class="table table-striped tab_height first-td-tc" id="gridTable"></table></div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l">

			</section>
		</section>
	</section>
	<div id="formModule">
		<div class="modal fade panel" id="form-modal" aria-hidden="false">
			<div class="modal-dialog w900">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">×</button>
						<h4 class="modal-title" id="entityFormTitle">图片</h4>
					</div>
					<div class="modal-body">
						<img src="${sysPath}/images/portal/ui-bg_gloss-wave_45_e14f1c_500x100.png">
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<script src='${sysPath}/js/com/jc/csmp/video/snapshot/projectVideoSnapshotList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>