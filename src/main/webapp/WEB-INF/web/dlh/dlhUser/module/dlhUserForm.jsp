<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">用户</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="dlhUserForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<table class="table table-td-striped">
						<tbody>
						<tr >
							<td class="w100">
								<span class='required'>*</span>系统简述
							</td>
							<td class="w300">
								<input type="text" id="dlhUsercode" name = "dlhUsercode" value="" placeholder="必填">
					 
							</td>
							<td class="w100">
								<span class='required'>*</span>用户
							</td>
							<td class="w300">
								<input type="text" id="dlhUsername" name = "dlhUsername" value="" placeholder="必填">					 
							</td>
						</tr>						
						<tr >
							<td class="w100">
								<span class='required'>*</span>一次最大量
							</td>
							<td class="w300">
								<input type="text" id="batchNum" name = "batchNum" value="" placeholder="必填">					 
							</td>
							<td class="w100">
								对称密码<br> （简单验证）
							</td>
							<td class="w300">
								<input type="text" id="dlhPassword" name = "dlhPassword" value="" placeholder="必填">
							</td>
						</tr>
						<tr >
							<td class="w100">
								<span class='required'>*</span>公共密钥
							</td>
							<td class="w300" colspan=3>
								<textarea id="dlhPasswordPublic" name = "dlhPasswordPublic" rows=3 readonly="readonly"></textarea>
							</td>
						</tr>
						<tr >
							<td class="w100">
								<span class='required'>*</span>私有密钥
							</td>
							<td class="w300" colspan=3>
								<textarea id="dlhPasswordPrivate" name = "dlhPasswordPrivate" rows=4 readonly="readonly"></textarea>
							</td>
						</tr>
						<tr >
							<td class="w100">
								备注
							</td>
							<td class="w300" colspan=3>
								<input type="text" id="remark" name = "remark" value="">
							</td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="saveOrModify">保存继续</button>
				<button class="btn" type="button" id="saveAndClose">保存退出</button>
				<button class="btn" type="button" id="generatorBtn">产生非对称密钥</button>
				<button class="btn" type="button" id="formDivClose">关 闭</button>
			</div>
		</div>
	</div>
</div>

<script src="${sysPath}/js/com/dlh/dlhUser/module/dlhUserForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/dlh/dlhUser/dlhUser.validate.js" type="text/javascript"></script>
