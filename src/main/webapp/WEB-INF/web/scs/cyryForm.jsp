<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-Cyry" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormCyryTitle">详细信息</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityCyryForm">
					<input type="hidden" id="nowId" name="nowId" value="${id}"/>
					<input type="hidden" id="nowDisPath" name="nowDisPath" value="${disPath}"/>
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td>姓名</td>
							<td>
								<input type="text" readonly id="person_name" name="person_name"/>
							</td>
							<td>身份证号</td>
							<td>
								<input type="text" readonly id="person_cert_num" name="person_cert_num"/>
							</td>
						</tr>
						<tr>
							<td>性别</td>
							<td>
								<input type="text" readonly id="person_sex_value" name="person_sex_value"/>
							</td>
							<td>民族</td>
							<td>
								<input type="text" readonly id="person_nation_value" name="person_nation_value"/>
							</td>
						</tr>
						<tr>
							<td>注册类型</td>
							<td>
								<input type="text" readonly id="reg_type" name="reg_type"/>
							</td>
							<td>注册证书号</td>
							<td>
								<input type="text" readonly id="reg_num" name="reg_num"/>
							</td>
						</tr>
						<tr>
							<td>联系电话</td>
							<td >
								<input type="text" readonly id="person_tel" name="person_tel"/>
							</td>
							<td>岗位类型</td>
							<td>
								<input type="text" readonly id="gwlx" name="gwlx"/>
							</td>
						</tr>
						<tr>
							<td>联系地址</td>
							<td colspan="3">
								<input type="text" readonly id="person_address" name="person_address"/>
							</td>
						</tr>
						<tr>
							<td>班组名称</td>
							<td >
								<input type="text" readonly id="class_bzmc" name="class_bzmc"/>
							</td>
							<td>班组工种</td>
							<td >
								<input type="text" readonly id="class_bzgz" name="class_bzgz"/>
							</td>
						</tr>
						<tr>
							<td>进场日期</td>
							<td >
								<input type="text" readonly id="class_jcrq" name="class_jcrq"/>
							</td>
							<td>出场日期</td>
							<td >
								<input type="text" readonly id="class_ccrq" name="class_ccrq"/>
							</td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn" type="button" id="closeCyryBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/scs/cyryForm.js'></script>