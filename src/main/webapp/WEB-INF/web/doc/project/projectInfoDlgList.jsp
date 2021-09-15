<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-Project" aria-hidden="false">
	<div class="modal-dialog " style="width:1200px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle">工程选择</h4>
			</div>
			<div  style="max-height:500px; ">
				<section class="clearfix search-box search-shrinkage">
					<div class="search-line">
						<form class="table-wrap form-table" id="searchProjectForm">
							<table class="table table-td-striped">
								<tbody>
								<tr>
									<td class="w140" align="center" style="text-align:center;width:100px;">项目编号</td>
									<td>
										<input type="text" id="query_projectNumber" name="query_projectNumber"/>
									</td>
									<td class="w140" align="center" style="text-align:center;width:100px;">项目名称</td>
									<td>
										<input type="text" id="query_projectName" name="query_projectName"/>
									</td>
									<td align="center"  style="text-align:center;width:100px;">
										<button class="btn" type="button" id="queryProjectBtn">查 询</button>
									</td>
								</tr>
								</tbody>
							</table>
						</form>
					</div>
				</section>
				<section class="panel " >
					<div class="table-wrap"><table class="table table-striped tab_height first-td-tc" id="gridProjectTable"></table></div>
					<section class="clearfix">
						<section class="form-btn fl m-l">
						</section>
					</section>
				</section>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn" type="button" id="closeProjectBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/doc/project/projectInfoDlgList.js'></script>