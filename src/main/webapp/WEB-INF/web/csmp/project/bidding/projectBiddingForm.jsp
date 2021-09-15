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
					<div>
						<h3 class="tc">基本信息</h3>
						<input type="hidden" id="id" name="id" />
						<input type="hidden" id="token" name="token" value="${data.token}">
						<input type="hidden" id="modifyDate" name="modifyDate" />
						<input type="hidden" id="projectApproval" name="projectApproval" value="0" />
						<table class="table table-td-striped">
							<tbody>
							<tr>
								<td style="width: 8%"><span class='required'>*</span>所属项目</td>
								<td style="width: 25%">
									<input type="text" id="projectName" name="projectName" readonly="readonly" />
									<input type="hidden" id="projectId" name="projectId"/>
								</td>
								<td style="width: 8%"><span class='required'>*</span>招标方式</td>
								<td style="width: 25%"><dic:select name="biddingMethod" id="biddingMethod" dictName="bidding_method" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
								<td style="width: 8%">最高限价(万元)</td>
								<td style="width: 25%"><input type="text" id="maxPrice" name="maxPrice"/></td>
							</tr>
							<tr>
								<td>合同期限(天)</td>
								<td><input type="text" id="contractPeriod" name="contractPeriod"/></td>
								<td>建设用地规<br />划许可证号</td>
								<td><input type="text" id="buildLandLicence" name="buildLandLicence"/></td>
								<td>建设工程规<br />划许可证号</td>
								<td><input type="text" id="buildProjectLicence" name="buildProjectLicence"/></td>
							</tr>
							<tr>
								<td>立项批文</td>
								<td><label class="checkbox inline" for="projectApprovalCheck"><input type="checkbox" id="projectApprovalCheck" name="projectApprovalCheck" value="1" />有立项批文</label></td>
								<td colspan="4" style="background: #ffffff; text-align: left; display: none;" id="projectApprovalAttachListTd">
									<div class="uploadButt">
										<a class="btn dark" type="button" data-target="#file-edit2" id="file-edit_a_2"  role="button" data-toggle="modal">上传立项批文</a>
									</div>
									<div class="fjList">
										<ul class="fjListTop nobt">
											<li>
												<span class="enclo">已上传附件</span>
												<span class="enclooper">操作</span>
											</li>
										</ul>
										<ul class="fjListTop" id="attachList2"></ul>
									</div>
								</td>
							</tr>
							<tr>
								<td>采购需求</td>
								<td colspan="5"><textarea type="text" id="purchasingDemand" name="purchasingDemand" style="height: 80px;"></textarea></td>
							</tr>
							<tr>
								<td>质量要求</td>
								<td colspan="5"><textarea id="qualityRequirement" name="qualityRequirement" style="height: 80px;"></textarea></td>
							</tr>
							<tr>
								<td>项目规模及内容</td>
								<td colspan="5"><textarea id="projectContent" name="projectContent" style="height: 80px;"></textarea></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div><h3 class="tc">附件信息</h3><table class="table form-table table-td-striped" id="attachTable"></table></div>
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
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src='${sysPath}/js/com/jc/csmp/project/bidding/projectBiddingForm.js'></script>