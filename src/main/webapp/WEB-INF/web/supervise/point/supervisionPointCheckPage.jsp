<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-check-modal" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" onclick="$('#form-check-modal').modal('hide');">×</button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityCheckForm">
					<input type="hidden" id="tempId" name="tempId" value="${data.tempId}" />
					<input type="hidden" id="id" name="id" />
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="createDate" name="createDate" />
					<input type="hidden" id="modifyDate" name="modifyDate" />
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td class="w100"><span class='required'>*</span>项目统一编号</td>
							<td><input type="text" id="projectNumber" name="projectNumber" /></td>
						</tr>
						<tr>
							<td class="w100">验证结果</td>
							<td colspan="5" id="resultTxt"></td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-body">
				<table class="table table-striped tab_height" id="columnGridTable"></table>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" onclick="supervisionPointCheckPagePanel.check();">验证脚本</button>
				<button class="btn" type="button" onclick="$('#form-check-modal').modal('hide');">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script>
	var supervisionPointCheckPagePanel = {};
	supervisionPointCheckPagePanel.supervisionId = null;

	supervisionPointCheckPagePanel.init = function(config) {
		supervisionPointCheckPagePanel.supervisionId = config.supervisionId;
		$('#form-check-modal').modal('show');
	};

	supervisionPointCheckPagePanel.check = function () {
		var formData = $("#entityForm").serializeArray();
		formData.push({name: 'supervisionId', value: supervisionPointCheckPagePanel.supervisionId});
		formData.push({name: 'projectNumber', value: $('#entityCheckForm #projectNumber').val()});
		jQuery.ajax({
			url: getRootPath() + "/supervise/point/checkJs.action", type: 'POST', cache: false, data: formData,
			success : function(data) {
				$('#entityCheckForm #resultTxt').html(data.msg);
			},
			error : function() {
				supervisionPointFormPanel.subState = false;
				msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
			}
		});
	}
</script>