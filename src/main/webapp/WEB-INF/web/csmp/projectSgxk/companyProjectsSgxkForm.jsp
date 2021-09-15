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
							<td><span class='required'>*</span>标段名称</td>
							<td>
								<input type="text" id="pcpHtlb" name="pcpHtlb"/>
							</td>
							<td><span class='required'>*</span>施工许可编号</td>
							<td>
								<input type="text" id="pcpBdmc" name="pcpBdmc"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>面积（平方米)</td>
							<td>
								<input type="text" id="pcpHtbh" name="pcpHtbh"/>
							</td>
							<td><span class='required'>*</span>合同金额（万元）</td>
							<td>
								<input type="text" id="pcpHtje" name="pcpHtje"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>发证日期</td>
							<td>
								<input class="datepicker-input" type="text" id="pcpQdrq" name="pcpQdrq" data-pick-time="false" data-date-format="yyyy-MM-dd">
							</td>
							<td><span class='required'>*</span>详情</td>
							<td>
								<input type="text" id="pcpXq" name="pcpXq"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>项目编号</td>
							<td>
								<input type="text" id="pcpProjectNum" name="pcpProjectNum"/>
							</td>
							<td><span class='required'>*</span>导入主键</td>
							<td>
								<input type="text" id="dlhDataId" name="dlhDataId"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>最后的更新时间</td>
							<td>
								<input class="datepicker-input" type="text" id="dlhDataModifyDate" name="dlhDataModifyDate" data-pick-time="false" data-date-format="yyyy-MM-dd">
							</td>
							<td><span class='required'>*</span>数据来源接口</td>
							<td>
								<input type="text" id="dlhDataSrc" name="dlhDataSrc"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>数据来源</td>
							<td>
								<input type="text" id="dlhDataUser" name="dlhDataUser"/>
							</td>
							<td><span class='required'>*</span>pcpBzry</td>
							<td>
								<input type="text" id="pcpBzry" name="pcpBzry"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>pcpDh</td>
							<td>
								<input type="text" id="pcpDh" name="pcpDh"/>
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
<script src='${sysPath}/js/com/jc/csmp/projectSgxk/companyProjectsSgxkForm.js'></script>