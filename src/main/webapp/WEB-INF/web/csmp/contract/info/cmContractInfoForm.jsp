<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityForm">
					<input type="hidden" id="id" name="id"/>
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate" />
					<table class="table table-td-striped">
						<tbody>
							<tr>
								<td style="width: 8%"><span class='required'>*</span>所属项目</td>
								<td style="width: 25%">
									<input type="text" id="projectName" name="projectName" readonly="readonly" />
									<input type="hidden" id="projectId" name="projectId" />
								</td>
								<td style="width: 8%"><span class='required'>*</span>合同编号</td>
								<td style="width: 25%"><input type="text" id="contractCode" name="contractCode"/></td>
								<td style="width: 8%"><span class='required'>*</span>合同名称</td>
								<td style="width: 26%"><input type="text" id="contractName" name="contractName"/></td>
							</tr>
							<tr>
								<td><span class='required'>*</span>合同类型</td>
								<td><dic:select name="contractType" id="contractType" dictName="contract_type" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
								<td><span class='required'>*</span>合同金额(万元)</td>
								<td><input type="text" id="contractMoney" name="contractMoney"/></td>
								<td><span class='required'>*</span>工期(天)</td>
								<td><input type="text" id="constructionPeriod" name="constructionPeriod"/></td>
							</tr>
							<tr>
								<td><span class='required'>*</span>发包单位</td>
								<td>
									<input type="hidden" id="partyaUnit" name="partyaUnit" />
									<input type="text" id="partyaUnitValue" name="partyaUnitValue" readonly="readonly" />
								</td>
								<td><span class='required'>*</span>中标单位</td>
								<td>
									<input type="hidden" id="partybUnit" name="partybUnit" />
									<input type="text" id="partybUnitValue" name="partybUnitValue" readonly="readonly" />
								</td>
								<td>文明用地</td>
								<td><dic:select name="civilizedLand" id="civilizedLand" dictName="civilized_land" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
							</tr>
							<tr>
								<td>创优目标</td>
								<td><dic:select name="goalOfExcellence" id="goalOfExcellence" dictName="goal_of_excellence" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
								<td>付款方式</td>
								<td><dic:select name="paymentMethod" id="paymentMethod" dictName="payment_method" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
								<td>结算是否要审计</td>
								<td><dic:select name="needAudit" id="needAudit" dictName="yes_or_no" parentCode="csmp" defaultValue="0" headType="1" headValue="" /></td>
							</tr>
							<tr>
								<td>合同内容</td>
								<td colspan="5"><textarea id="contractContent" name="contractContent" style="height: 60px"></textarea></td>
							</tr>
							<tr>
								<td>合同开始时间</td>
								<td><input class="datepicker-input" type="text" id="startDate" name="startDate" data-pick-time="false" data-date-format="yyyy-MM-dd" /></td>
								<td>合同结束时间</td>
								<td><input class="datepicker-input" type="text" id="endDate" name="endDate" data-pick-time="false" data-date-format="yyyy-MM-dd"/></td>
								<td>合同签订时间</td>
								<td><input class="datepicker-input" type="text" id="signDate" name="signDate" data-pick-time="false" data-date-format="yyyy-MM-dd"/></td>
							</tr>
							<tr>
								<td><span class='required'>*</span>经办人</td>
								<td><input type="text" id="handleUser" name="handleUser"/></td> 
								<td>支付总额(万元)</td>
								<td><input type="text" id="totalPayment" name="totalPayment" readonly="readonly" /></td>
								<td>支付比例</td>
								<td><input type="text" id="paymentRatio" name="paymentRatio" readonly="readonly" /></td>
							</tr>
							<tr>
								<td>备注</td>
								<td colspan="5"><textarea id="remark" name="remark" style="height: 60px"></textarea></td>
							</tr>
							<tr>
								<td class="w100">附件信息</td>
								<td colspan="5">
									<span workFlowForm='attach' itemName="uploadVoice">
										<div class="uploadButt">
											<a class="btn dark" type="button" data-target="#file-edit1"  id="file-edit_a_1"  role="button" data-toggle="modal">上传</a>
										</div>
										<div class="fjList">
											<ul class="fjListTop nobt">
												<li>
													<span class="enclo">已上传附件</span>
													<span class="enclooper">操作</span>
												</li>
											</ul>
											<ul class="fjListTop" id="attachList1"></ul>
										</div>
									</span>
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
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<script src="${sysPath}/js/webupload/liuUpload.js?n=1" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src='${sysPath}/js/com/jc/csmp/contract/info/cmContractInfoForm.js?n=1'></script>