<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ taglib prefix="dicex" uri="/WEB-INF/tlds/dicex-tags.tld" %>
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
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate" />
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td>机器类型</td>
							<td>
								<input type="text" id="targetType" name="targetType"/>
							</td>
							<td>机器主键</td>
							<td>
								<input type="text" id="targetId" name="targetId"/>
							</td>
						</tr>
						<tr>
							<td>机器编码</td>
							<td>
								<input type="text" id="targetCode" name="targetCode"/>
							</td>
							<td>报警时间</td>
							<td>
								<input type="text" id="warnTime" name="warnTime"/>
							</td>
						</tr>
						<tr>
							<td>报警原因编码</td>
							<td>
								<input type="text" id="warnReasonCode" name="warnReasonCode"/>
							</td>
							<td>报警原因</td>
							<td>
								<input type="text" id="warnReason" name="warnReason"/>
							</td>
						</tr>
						<tr>
							<td>数量</td>
							<td>
								<input type="text" id="num01" name="num01"/>
							</td>
							<td>数量</td>
							<td>
								<input type="text" id="num02" name="num02"/>
							</td>
						</tr>
						<tr>
							<td>数量</td>
							<td>
								<input type="text" id="num03" name="num03"/>
							</td>
							<td>数量</td>
							<td>
								<input type="text" id="num04" name="num04"/>
							</td>
						</tr>
						<tr>
							<td>数量</td>
							<td>
								<input type="text" id="num05" name="num05"/>
							</td>
							<td>数量</td>
							<td>
								<input type="text" id="num06" name="num06"/>
							</td>
						</tr>
						<tr>
							<td>数量</td>
							<td>
								<input type="text" id="num07" name="num07"/>
							</td>
							<td>数量</td>
							<td>
								<input type="text" id="num08" name="num08"/>
							</td>
						</tr>
						<tr>
							<td>数量</td>
							<td>
								<input type="text" id="num09" name="num09"/>
							</td>
							<td>数量</td>
							<td>
								<input type="text" id="num10" name="num10"/>
							</td>
						</tr>
						<tr>
							<td>数量</td>
							<td>
								<input type="text" id="num11" name="num11"/>
							</td>
							<td>数量</td>
							<td>
								<input type="text" id="num12" name="num12"/>
							</td>
						</tr>
						<tr>
							<td>数量</td>
							<td>
								<input type="text" id="num13" name="num13"/>
							</td>
							<td>数量</td>
							<td>
								<input type="text" id="num14" name="num14"/>
							</td>
						</tr>
						<tr>
							<td>数量</td>
							<td>
								<input type="text" id="num15" name="num15"/>
							</td>
							<td>数量</td>
							<td>
								<input type="text" id="num16" name="num16"/>
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
<script src='${sysPath}/js/com/jc/csmp/warn/info/warnInfoForm.js'></script>