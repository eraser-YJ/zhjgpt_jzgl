<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-authroize" aria-hidden="false">
	<div class="modal-dialog" style="width:1180px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">授权</h4>
			</div>
			<div class="modal-body">
				<section class="dis-table" style="table-layout: fixed;">
					<div class="dis-table-cell" style="width: 300px;">
						<h4 class="m-t-none">访问权限</h4>
						<form class="cmxform" id="roleMenusForm">
							<input type="hidden" id="id" name="id" value="0">
							<input type="hidden" id="roleId" name="roleId" value="0">
							<input type="hidden" id="modifyDate" name="modifyDate">
							<div class="m-t m-b" id="powerDiv">
								<div id="menuTreeDiv"  class="ztree scrollable inline" style="height:300px;"></div>
							</div>
						</form>
					</div>
					<div class="dis-table-cell">
						<form class="cmxform" id="roleBlocksForm">
							<div class="input-group input-default w-p100">
								<div class="form-control distab" style="display: none;">
									<h4 style="margin-top: 6px;">数据范围设定</h4>
									<select class="select-md" name="permissionType"  id="permissionType" style="width: 200px">
										<option value="1">本人</option>
										<option value="2">所在部门</option>
										<option value="3">所在机构</option>
										<%--<option value="4" id="customDiv">自定义</option>--%>
									</select>
								</div>
							</div>
						</form>
						<h4>角色成员</h4>
						<div id="userSelectDiv"></div>
						<div id="customContainer">
							<h4>自定义</h4>
							<div id="deptTreeDiv"></div>
						</div>
					</div>
				</section>
			</div>
			<div class="modal-footer no-all form-btn">
				<button class="btn dark" type="button" id="savaRoleMenu">保  存</button>
				<button class="btn" type="button" id="closeRoleMenu">关  闭</button>
			</div>
		</div>
	</div>
</div>

<%--<script type="text/javascript" src="${sysPath}/js/com/sys/role/roleUserSelect.js"></script>--%>
<script type="text/javascript" src="${sysPath}/js/com/sys/role/roleAuthorize.js"></script>
<script type="text/javascript" src="${sysPath}/js/com/sys/role/role.validate.js"></script>