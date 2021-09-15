<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="contract-change-modal" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" onclick="$('#contract-change-modal').modal('hide');">×</button>
				<h4 class="modal-title">选择合同</h4>
			</div>
			<div class="modal-body">
				<section class="panel clearfix search-box search-shrinkage">
					<div class="search-line" style="margin-top: -20px">
						<form class="table-wrap form-table" id="contractChangeSearchForm">
							<table class="table table-td-striped">
								<tbody>
								<tr>
									<td class="w140">合同编号</td>
									<td><input type="text" id="contractChange_contractCode" name="contractChange_contractCode" /></td>
									<td class="w140">合同名称</td>
									<td><input type="text" id="contractChange_contractName" name="contractChange_contractName" /></td>
									<td rowspan="2" style="width: 200px; text-align: center; background: #FFFFFF;">
										<button class="btn" type="button" onclick="cmContractInfoChangePanel.renderTable();">查 询</button>
										<button class="btn" type="button" onclick="cmContractInfoChangePanel.queryReset();">重 置</button>
									</td>
								</tr>
								</tbody>
							</table>
						</form>
					</div>
				</section>
				<section class="panel" style="margin-top: -20px">
					<div class="table-wrap"><table class="table table-striped tab_height" id="contractChageGridTable"></table></div>
					<section class="clearfix" id="footer_height">
						<section class="form-btn fl m-l">
						</section>
					</section>
				</section>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/contract/info/cmContractInfoChange.js'></script>