<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-WarnResult" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityWarnResultFormTitle"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityWarnResultForm">
					<input type="hidden" id="id" name="id" value="${data.id}"/>
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td style="width:120px;">处理内容</td>
							<td>
								<textarea rows="5" style="width:100%" id="processResult" name="processResult"/>
							</td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" id="saveWarnResultBtn">保 存</button>
				<button class="btn" type="button" id="closeWarnResultBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/warn/info/warnInfoResult.js'></script>