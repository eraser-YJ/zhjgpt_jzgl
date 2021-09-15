<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title"><div>批量生成</div></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="uniqueForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
<table class="table table-td-striped">
	<tbody>
	<tr >
		<td class="w100">
<span class='required'>*</span>批量生成数量
		</td>
		<td class="w300">
	<input type="text" id="createCount" name = "createCount" value="" maxlength="4" placeholder="必填">
 
		</td>
	</tr>
	</tbody>
</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<%--<button class="btn dark" type="button" id="saveOrModify">保存继续</button>--%>
				<button class="btn dark" type="button" id="saveAndClose">保存</button>
				<button class="btn" type="button" id="formDivClose">关 闭</button>
			</div>
		</div>
	</div>
</div>

<script src="${sysPath}/js/com/sys/unique/module/uniqueForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/unique/unique.validate.js" type="text/javascript"></script>
