<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="c"%>
	<input type="hidden" id="id" name = "id" value="${dicInfo.id }" >
	<input type="hidden" id="token" name="token" value="${token}">
	<input type="hidden" id="code"  name = "code" value="${dicInfo.code }" >
	<input type="hidden" id="modifyDate" name="modifyDate">
	<table class="table table-td-striped">
		<tbody>
			<tr>
				<td><span class="required">*</span>字典值</td>
				<td>
		       		<input type="text" id="value" maxlength="20" name = "value" value="${dicInfo.value }" >
				</td>
			</tr>
		</tbody>
	</table>
	<div class="modal-footer form-btn">
        <button class="btn dark" type="button" id="saveOrModify" onclick="dicinfo.dicSubmit()">保存</button>

    </div>