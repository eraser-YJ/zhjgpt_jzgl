<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityForm">
					<input type="hidden" id="id" name="id" />
					<input type="hidden" id="companyType" name="companyType" />
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate" />
					<table class="table table-td-striped">
						<tbody>
							<tr>
								<td style="width: 15%"><span class='required'>*</span>单位名称</td>
								<td style="width: 35%"><input type="text" id="companyName" name="companyName" /></td>
								<td style="width: 15%"><span class='required'>*</span>统一社会信用代码</td>
								<td style="width: 35%"><input type="text" id="creditCode" name="creditCode" /></td>
							</tr>
							<tr>
								<td><span class='required'>*</span>法定代表人</td>
								<td><input type="text" id="legalPerson" name="legalPerson" /></td>
								<td><span class='required'>*</span>法人联系方式</td>
								<td><input type="text" id="legalPhone" name="legalPhone" /></td>
							</tr>
							<tr>
								<td><span class='required'>*</span>项目联系人</td>
								<td><input type="text" id="liaisonMan" name="liaisonMan" /></td>
								<td><span class='required'>*</span>联系人电话</td>
								<td><input type="text" id="liaisonPhone" name="liaisonPhone" /></td>
							</tr>
							<tr>
								<td>委托代理人</td>
								<td><input type="text" id="proxyMan" name="proxyMan" /></td>
								<td>委托代理人电话</td>
								<td><input type="text" id="proxyPhone" name="proxyPhone" /></td>
							</tr>
							<tr>
								<td><span class='required'>*</span>企业类型</td>
								<td><dic:checkbox name="companyTypeCheckbox" id="companyTypeCheckbox" dictName="company_type" parentCode="csmp" /></td>
								<td>邮政编码</td>
								<td><input type="text" id="zipcode" name="zipcode" /></td>
							</tr>
							<tr>
								<td>单位地址</td>
								<td colspan="3"><input type="text" id="companyAddress" name="companyAddress" /></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" id="saveBtn">保 存</button>
				<button class="btn" type="button" id="closeBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/company/cmCompanyForm.js'></script>