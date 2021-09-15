<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ taglib prefix="dicex" uri="/WEB-INF/tlds/dicex-tags.tld" %>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog " style="width:1200px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityForm">
					<input type="hidden" id="id" name="id" />
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate" />
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td style="width:120px;"><span class='required'>*</span>规则编码</td>
							<td style="width:40%;">
								<dicex:typeTag name="ruleCode" id="ruleCode"  headName="-请选择-" headValue=""/>
							</td>
							<td style="width:120px;"><span class='required'>*</span>规则名称</td>
							<td style="width:40%;">
								<input type="text" id="ruleName" name="ruleName"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>规则内容</td>
							<td colspan="3">
								<textarea id="ruleTxt" name="ruleTxt" rows="20"></textarea>
							</td>
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
<script src='${sysPath}/js/com/jc/csmp/warn/rule/warnRuleForm.js'></script>