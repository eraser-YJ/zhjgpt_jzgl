<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs" style="float: left;">
			<span>项目管理</span>
			<span>项目驾驶舱</span>
		</div>
	</header>
	<section class="tree-fluid m-t-md">
		<aside class="tree-wrap" id="LeftHeight_box" style="height: 497px !important;">
			<section class="panel" id="LeftHeight" style="overflow-y: auto;height: 457px !important;">
				<div id="treeDemo" class="ztree" style="width: 250px;"></div>
			</section>
		</aside>
		<section class="tree-right">
			<form id="searchForm" style="display: none;">
				<input type="hidden" id="projectId" name="projectId" />
				<input type="hidden" id="projectNumber" name="projectNumber" />
				<input type="hidden" id="approvalNumber" name="approvalNumber" />
			</form>
			<div>
				<section class="panel" style="height: 300px; float: left; width: 29%">
					<div class="tab-pane fade active in" style="font-size: 12px;margin: 5px;">
						<table class="table" >
							<tbody>
							<tr style="background: #f3f3f3 !important;">
								<td style="text-align: center; padding: 0 !important;">项目总投资(万元)</td>
								<td style="text-align: center; padding: 0 !important;">企业个数</td>
								<td style="text-align: center; padding: 0 !important;">人员总数</td>
							</tr>

								<tr>
									<td style="text-align: center; padding: 0 !important;" id="investmentAmount">-</td>
									<td style="text-align: center; padding: 0 !important;" id="companyCount">-</td>
									<td style="text-align: center; padding: 0 !important;" id="personCount">-</td>
								</tr>
								<tr>
									<td colspan="3" ><div style=" width:158px !important;height: 200px; line-height: 290px;margin:0 auto; padding-top:10px;  " id="moneyModule"></div></td>
								</tr>

							</tbody>

						</table>

					</div>
				</section>
				<section class="panel" style="margin-left: 1%; height: 300px; float: left; width: 38%">
					<div class="tab-pane fade active in" style="height: 300px; display: none;" id="planModule"></div>
					<div class="tab-pane fade active in" style="width: 100%; height: 300px; text-align:center; line-height: 300px;" id="planModuleEmpty">暂无任务进度情况</div>
				</section>
				<section class="panel" style="margin-left:1%; height: 300px; float: left; width: 30%">
					<div class="tab-pane fade active in">
						<div style="height: 30px; margin-top: 5px; margin-left: 10px; font-weight: bold;">超期计划</div>
						<div style="height: 260px; line-height: 260px;font-size: 12px!important; margin-left: 5px; text-align:center; width: 98%; overflow-y: scroll" id="passTimeModule">选择项目进行查看</div>
					</div>
				</section>
			</div>
			<div>
				<section class="panel" style="height: 400px; margin-top: -5px; float: left; width: 60%">
					<div class="tab-pane fade active in" style="height: 400px; line-height: 400px; text-align:center; width: 100%" id="questionModule">选择项目进行查看</div>
				</section>
				<section class="panel" style="margin-left: 1%; margin-top: -5px; height: 400px; float: left; width: 38%">
					<div class="tab-pane fade active in" >
						<p style="height:30px;margin-top:5px;margin-left:10px;font-weight: bold;">形象进度</p>
						<div id="imageEmptyModule" style="height: 300px; line-height: 300px; width: 100%; text-align: center;">无形象进度</div>
						<div id="imageModule" style="display: none;">
							<div style="display: flex;">
								<i class="fa fa-caret-left" style="width:10%;text-align: center; cursor: pointer;" onclick="cmProjectCockpitPanel.upImage();"></i>
								<p  style="width:80%;text-align: center" id="imageTitle">一层顶板浇筑</p>
								<i class="fa fa-caret-right"  style="width:10%;text-align: center; cursor: pointer;" onclick="cmProjectCockpitPanel.downImage();"></i>
							</div>
							<div style="text-align: center; width: 100%; height: 300px; line-height: 300px;" id="imageListEmptyModule">无图片信息</div>
							<div style="display: flex; margin: 0 10px; flex-wrap: wrap; overflow-y: scroll;height:300px;display:none;" id="imageListModule">
							</div>
						</div>
					</div>
				</section>
			</div>
		</section>
	</section>
	<div id="formModule"></div>
</section>
<script>
 $(window).ready(function(){
	 $("body").scrollTop(0);
 })

</script>
<script src='${sysPath}/js/com/jc/common/echarts.js'></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/csmp/project/info/cmProjectCockpit.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
