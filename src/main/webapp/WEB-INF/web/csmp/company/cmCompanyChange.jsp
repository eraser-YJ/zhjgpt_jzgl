<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" id="closeBtn">×</button>
				<h4 class="modal-title">选择单位</h4>
			</div>
			<div class="modal-body">
				<section class="panel clearfix search-box search-shrinkage">
					<div class="search-line">
						<form class="table-wrap form-table" id="searchForm">
							<table class="table table-td-striped">
								<tbody>
								<tr>
									<td style="width: 10%">单位名称</td>
									<td><input type="text" id="companyName" name="companyName" /></td>
									<td style="width: 10%">统一社会信用代码</td>
									<td><input type="text" id="creditCode" name="creditCode" /></td>
									<td style="width: 10%">企业类型</td>
									<td><dic:select name="companyType" id="companyType" dictName="company_type" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
									<td style="width: 10%" style="text-align: center; background: #FFFFFF;" colspan="2">
										<button class="btn" type="button" id="queryBtn">查 询</button>
										<button class="btn" type="button" id="resetBtn">重 置</button>
									</td>
								</tr>
								</tbody>
							</table>
						</form>
					</div>
				</section>
				<section class="panel">
					<h2 class="panel-heading clearfix" style="margin-left: -10px;">查询结果</h2>
					<div class="table-wrap" style="margin-top: 15px;"><table class="table table-striped tab_height" id="gridTable"></table></div>
					<section class="clearfix" id="footer_height">
						<section class="form-btn fl m-l">
						</section>
					</section>
				</section>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/company/cmCompanyChange.js'></script>