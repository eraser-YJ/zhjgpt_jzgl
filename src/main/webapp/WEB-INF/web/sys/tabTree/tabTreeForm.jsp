<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">系统页签树表</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="tabTreeForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
<table class="table table-td-striped">
	<tbody>
	<tr >
		<td class="w100"><span class="required">*</span>
子系统标识
		</td>
		<td class="w300">
	<input type="text" id="sysPermission" name = "sysPermission" value="">
 
		</td>
		<td class="w100"><span class="required">*</span>
系统页签树标题
		</td>
		<td class="w300">
	<input type="text" id="tabTitle" name = "tabTitle" value="">
 
		</td>
	</tr>
	<tr >
		<td class="w100"><span class="required">*</span>
系统页签树链接
		</td>
		<td class="w300">
	<input type="text" id="tabUrl" name = "tabUrl" value="">
 
		</td>
		<td class="w100"><span class="required">*</span>
系统页签树位置标记
		</td>
		<td class="w300">
	<input type="text" id="tabFlag" name = "tabFlag" value="">
 
		</td>
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

<script src="${sysPath}/js/com/sys/tabTree/tabTreeForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/tabTree/tabTree.validate.js" type="text/javascript"></script>
