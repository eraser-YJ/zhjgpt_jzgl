<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-Question" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormQuestionTitle">详细信息</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityQuestionForm">
					<input type="hidden" id="nowId" name="nowId" value="${id}"/>
					<input type="hidden" id="nowDisPath" name="nowDisPath" value="${disPath}"/>
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td>问题编号</td>
							<td>
								<input type="text" readonly id="code" name="code"/>
							</td>
							<td>登记时间</td>
							<td>
								<input type="text" readonly id="create_date" name="create_date"/>
							</td>
						</tr>
						<tr>
							<td>项目编号</td>
							<td>
								<input type="text" readonly id="project_number" name="project_number"/>
							</td>
							<td>项目名称</td>
							<td>
								<input type="text" readonly id="project_name" name="project_name"/>
							</td>
						</tr>
						<tr>
							<td>合同名称</td>
							<td>
								<input type="text" readonly id="contract_name" name="contract_name"/>
							</td>
							<td>整改单位</td>
							<td>
								<input type="text" readonly id="dept_ame" name="dept_ame"/>
							</td>
						</tr>
						<tr>
							<td>标题</td>
							<td colspan="3">
								<input type="text" readonly id="title" name="title"/>
							</td>
						</tr>

						<tr>
							<td>问题描述</td>
							<td colspan="3">
								<textarea readonly="readonly" id="question_meta" name="question_meta" rows="5"></textarea>
							</td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn" type="button" id="closeQuestionBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/scs/questionForm.js'></script>