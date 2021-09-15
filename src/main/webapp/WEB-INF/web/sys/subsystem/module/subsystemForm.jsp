<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="titleSubsystem"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="subsystemForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<input type="hidden" id="menuid" name="menuid">
					<table class="table table-td-striped"><tbody>
						<tr>
							<td class="w100"><span class='required'>*</span>子系统名称</td>
							<td class="w300"><input type="text" id="name" name="name" placeholder="必填"></td>
						</tr>
						<tr>
							<td class="w100"><span class='required'>*</span>子系统地址</td>
							<td class="w300"><input type="text" id="url" name="url" placeholder="必填"></td>
						</tr>
						<tr>
							<td class="w100">子系统首页</td>
							<td class="w300"><input type="text" id="firstUrl" name="firstUrl" ></td>
						</tr>
						<tr>
							<td class="w100">图标类型名称</td>
							<td class="w300"><input type="text" id="icon" name="icon"></td>
						</tr>
						<tr>
							<td class="w100"><span class='required'>*</span>排序</td>
							<td class="w300"><input type="text" id="queue" name="queue" placeholder="必填"></td>
						</tr>
						<tr style="display:none;">
							<td class="w100">可见标识</td>
							<td class="w300">
                                <label class="radio inline"><input type="radio" id='isShow1' name='isShow'  checked value='0' label='显示'/>显示</label>
                                <label class="radio inline"><input type="radio" id='isShow2' name='isShow' value='1' label='不显示'/>不显示</label>
							</td>
						</tr>
						<tr>
							<td class="w100">打开方式</td>
							<td class="w300">
                                <label class="radio inline"><input type="radio" id='openType1' name='openType'  checked value='1' label='内部打开'/>内部打开</label>
                                <label class="radio inline"><input type="radio" id='openType2' name='openType' value='2' label='弹出打开'/>弹出打开</label>
							</td>
						</tr>
						<tr>
							<td class="w100">子系统标识</td>
							<td class="w300"><input type="text" id="permission" name="permission"></td>
						</tr>
						<tr>
							<td class="w100">用户同步接口地址</td>
							<td class="w300"><input type="text" id="userSyncUrl" name="userSyncUrl"></td>
						</tr>
						<tr>
							<td class="w100">子系统IP</td>
							<td class="w300"><input type="text" id="extStr1" name="extStr1"></td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="saveOrModify">保存继续</button>
				<button class="btn" type="button" id="saveAndClose">保存退出</button>
				<button class="btn" type="button" id="formDivClose">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src="${sysPath}/js/com/sys/subsystem/module/subsystemForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/subsystem/subsystem.validate.js" type="text/javascript"></script>
