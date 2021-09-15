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
							<td><span class='required'>*</span>部门编号</td>
							<td>
								<input type="text" id="code" name="code"/>
							</td>
							<td><span class='required'>*</span>部门名称</td>
							<td>
								<input type="text" id="name" name="name"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>组织全称</td>
							<td>
								<input type="text" id="fullName" name="fullName"/>
							</td>
							<td><span class='required'>*</span>部门描述</td>
							<td>
								<input type="text" id="deptDesc" name="deptDesc"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>leaderId</td>
							<td>
								<input type="text" id="leaderId" name="leaderId"/>
							</td>
							<td><span class='required'>*</span>chargeLeaderId</td>
							<td>
								<input type="text" id="chargeLeaderId" name="chargeLeaderId"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>leaderId2</td>
							<td>
								<input type="text" id="leaderId2" name="leaderId2"/>
							</td>
							<td><span class='required'>*</span>parentId</td>
							<td>
								<input type="text" id="parentId" name="parentId"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>上级主管部门id</td>
							<td>
								<input type="text" id="managerDept" name="managerDept"/>
							</td>
							<td><span class='required'>*</span>organizationId</td>
							<td>
								<input type="text" id="organizationId" name="organizationId"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>标记是部门or机构节点
            ’0‘--部门
            ’1‘--机构</td>
							<td>
								<input type="text" id="deptType" name="deptType"/>
							</td>
							<td><span class='required'>*</span>部门排序</td>
							<td>
								<input type="text" id="queue" name="queue"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>租户简称</td>
							<td>
								<input type="text" id="shortName" name="shortName"/>
							</td>
							<td><span class='required'>*</span>租户用户名</td>
							<td>
								<input type="text" id="userName" name="userName"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>密码</td>
							<td>
								<input type="text" id="password" name="password"/>
							</td>
							<td><span class='required'>*</span>0-试用 1-正式</td>
							<td>
								<input type="text" id="type" name="type"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>组织状态
            ’0‘--启用 
            ’1‘--禁用 
            ’2’--锁定 
            ‘3’--删除</td>
							<td>
								<input type="text" id="status" name="status"/>
							</td>
							<td><span class='required'>*</span>启用时间</td>
							<td>
								<input class="datepicker-input" type="text" id="openDay" name="openDay" data-pick-time="false" data-date-format="yyyy-MM-dd">
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>到期时间</td>
							<td>
								<input class="datepicker-input" type="text" id="endDay" name="endDay" data-pick-time="false" data-date-format="yyyy-MM-dd">
							</td>
							<td><span class='required'>*</span>文件空间(m)</td>
							<td>
								<input type="text" id="fileSpace" name="fileSpace"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>已用空间</td>
							<td>
								<input type="text" id="usedSpace" name="usedSpace"/>
							</td>
							<td><span class='required'>*</span>账户余额</td>
							<td>
								<input type="text" id="balance" name="balance"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>短信费用</td>
							<td>
								<input type="text" id="smsBalance" name="smsBalance"/>
							</td>
							<td><span class='required'>*</span>租户LOGO</td>
							<td>
								<input type="text" id="logo" name="logo"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>租户联系人</td>
							<td>
								<input type="text" id="cont" name="cont"/>
							</td>
							<td><span class='required'>*</span>联系人电话</td>
							<td>
								<input type="text" id="telp" name="telp"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>用户邮箱</td>
							<td>
								<input type="text" id="email" name="email"/>
							</td>
							<td><span class='required'>*</span>租户备注</td>
							<td>
								<input type="text" id="memo" name="memo"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>weight</td>
							<td>
								<input type="text" id="weight" name="weight"/>
							</td>
							<td><span class='required'>*</span>secret</td>
							<td>
								<input type="text" id="secret" name="secret"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>资源库id</td>
							<td>
								<input type="text" id="resourceId" name="resourceId"/>
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
<script src='${sysPath}/js/com/jc/csmp/doc/dept/sysDepartmentForm.js'></script>