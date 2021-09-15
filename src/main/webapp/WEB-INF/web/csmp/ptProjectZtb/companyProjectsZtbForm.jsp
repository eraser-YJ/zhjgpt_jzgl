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
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate" />
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td><span class='required'>*</span>招标类型</td>
							<td>
								<input type="text" id="pcpZblx" name="pcpZblx"/>
							</td>
							<td><span class='required'>*</span>招标方式</td>
							<td>
								<input type="text" id="pcpZbfs" name="pcpZbfs"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>中标单位名称</td>
							<td>
								<input type="text" id="pcpZbdwmc" name="pcpZbdwmc"/>
							</td>
							<td><span class='required'>*</span>中标日期</td>
							<td>
								<input class="datepicker-input" type="text" id="pcpZbrq" name="pcpZbrq" data-pick-time="false" data-date-format="yyyy-MM-dd">
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>中标金额（万元）</td>
							<td>
								<input type="text" id="pcpZbje" name="pcpZbje"/>
							</td>
							<td><span class='required'>*</span>中标通知书编号</td>
							<td>
								<input type="text" id="pcpZbtzs" name="pcpZbtzs"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>详情</td>
							<td>
								<input type="text" id="pcpXq" name="pcpXq"/>
							</td>
							<td><span class='required'>*</span>项目编号</td>
							<td>
								<input type="text" id="pcpProjectNum" name="pcpProjectNum"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>导入主键</td>
							<td>
								<input type="text" id="dlhDataId" name="dlhDataId"/>
							</td>
							<td><span class='required'>*</span>最后的更新时间</td>
							<td>
								<input class="datepicker-input" type="text" id="dlhDataModifyDate" name="dlhDataModifyDate" data-pick-time="false" data-date-format="yyyy-MM-dd">
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>数据来源接口</td>
							<td>
								<input type="text" id="dlhDataSrc" name="dlhDataSrc"/>
							</td>
							<td><span class='required'>*</span>数据来源</td>
							<td>
								<input type="text" id="dlhDataUser" name="dlhDataUser"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>pcpArea</td>
							<td>
								<input type="text" id="pcpArea" name="pcpArea"/>
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
<script src='${sysPath}/js/com/jc/csmp/ptProjectZtb/companyProjectsZtbForm.js'></script>