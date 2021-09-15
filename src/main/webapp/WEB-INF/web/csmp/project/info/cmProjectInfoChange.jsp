<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="project-change-modal" aria-hidden="false">
	<div class="modal-dialog" style="width: 1300px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" onclick="$('#project-change-modal').modal('hide');">×</button>
				<h4 class="modal-title">项目选择</h4>
			</div>
			<div class="modal-body">
				<section class="tree-fluid m-t-md" style="margin-top: -10px;" id="projectChangeWindow">
					<aside class="tree-wrap">
						<section class="panel" id="projectChangeLeftHeight" style="overflow-y: auto;">
							<div id="projectChangeTreeDemo" class="ztree"></div>
						</section>
					</aside>
					<section class="tree-right">
						<section class="panel tab-content">
							<form class="table-wrap form-table" id="projectChangeSearchForm">
								<input type="hidden" id="projectChange_region" name="projectChange_region" />
								<input type="hidden" id="projectChange_deptId" name="projectChange_deptId" value="${deptId}" />
								<table class="table table-td-striped">
									<tbody>
									<tr>
										<td style="width: 80px">项目统一编号</td>
										<td><input type="text" id="projectChange_projectNumber" name="projectChange_projectNumber" /></td>
										<td style="width: 80px">项目名称</td>
										<td><input type="text" id="projectChange_projectName" name="projectChange_projectName" /></td>
										<td style="text-align: center; background: #FFFFFF;">
											<button class="btn" type="button" onclick="cmProjectInfoChangePanel.renderTable();">查 询</button>
											<button class="btn" type="button" onclick="cmProjectInfoChangePanel.queryReset();">重 置</button>
										</td>
									</tr>
									</tbody>
								</table>
							</form>
							<div class="tab-pane fade active in" style="margin-top: 10px;">
								<div class="table-wrap">
									<table class="table table-striped tab_height" id="projectChangeGridTable"></table>
								</div>
								<section class="clearfix">
									<section class="pagination m-r fr m-t-none"></section>
								</section>
							</div>
						</section>
					</section>
				</section>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/project/info/cmProjectInfoChange.js?n=2'></script>