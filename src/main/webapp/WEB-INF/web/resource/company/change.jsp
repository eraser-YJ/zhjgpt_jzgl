<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="company-change-form-modal" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" onclick="$('#company-change-form-modal').modal('hide');">×</button>
				<h4 class="modal-title">选择企业</h4>
			</div>
			<div class="modal-body">
				<section class="panel clearfix search-box search-shrinkage">
					<div class="search-line">
						<form class="table-wrap form-table" id="companyChangeSearchForm">
							<table class="table table-td-striped" style="margin-top: -30px;">
								<tbody>
								<tr>
									<td class="w100">企业名称</td>
									<td><input type="text" id="companyName" name="companyName" /></td>
									<td style="width: 200px; text-align: center; background: #FFFFFF;">
										<button class="btn" type="button" onclick="resourceCompanyChangePanel.renderTable();">查 询</button>
										<button class="btn" type="button" onclick="resourceCompanyChangePanel.queryReset();">重 置</button>
									</td>
								</tr>
								</tbody>
							</table>
						</form>
					</div>
				</section>
				<section class="panel">
					<div class="table-wrap" style="margin-top: -30px;"><table class="table table-striped tab_height" id="companyChangeGridTable"></table></div>
					<section class="clearfix" id="footer_height"><section class="form-btn fl m-l"></section></section>
				</section>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/resource/company/change.js?n=1'></script>