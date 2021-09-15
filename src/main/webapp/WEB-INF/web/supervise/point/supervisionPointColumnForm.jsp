<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-column-modal" aria-hidden="false">
	<div class="modal-dialog w600">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" onclick="supervisionPointColumnFormPanel.operatorModal('hide');">×</button>
				<h4 class="modal-title" id="entityColumnFormTitle"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityColumnForm">
					<input type="hidden" id="id" name="id" />
					<input type="hidden" id="supervisionId" name="supervisionId" />
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate" />
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td class="w100">说明</td>
							<td>sql语句查询结果为多个时，属性别名用逗号分隔，参数顺序与属性数量有关系</td>
						</tr>
						<tr>
							<td class="w100"><span class='required'>*</span>数据描述</td>
							<td><input type="text" id="dataMeta" name="dataMeta" /></td>
						</tr>
						<tr>
							<td class="w100"><span class='required'>*</span>数据来源</td>
							<td>
								<select id="dataSource" name="dataSource">
									<option value="">-请选择-</option>
									<option value="sql">SQL语句</option>
									<option value="bean">程序数据源</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="w100" id="dataClassTd"><span class='required'>*</span>值</td>
							<td><input type="text" id="dataClass" name="dataClass" /></td>
						</tr>
						<tr>
							<td class="w100" id="dataValueTd"><span class='required'>*</span>值</td>
							<td><input type="text" id="dataValue" name="dataValue" /></td>
						</tr>
						<tr>
							<td class="w100"><span class='required'>*</span>参数顺序</td>
							<td><input type="text" id="queue" name="queue" /></td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" onclick="supervisionPointColumnFormPanel.saveOrModify();">保 存</button>
				<button class="btn" type="button" onclick="supervisionPointColumnFormPanel.operatorModal('hide');">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script>
	var supervisionPointColumnFormPanel = {};
	supervisionPointColumnFormPanel.subState = false;
	supervisionPointColumnFormPanel.callback = null;

	supervisionPointColumnFormPanel.init = function(config) {
		$('#entityColumnFormTitle').html(config.title);
		$("#entityColumnForm #id").val('');
		supervisionPointColumnFormPanel.callback = config.callback;
		if (config.operator == 'add') {
			$('#entityColumnForm #supervisionId').val(config.supervisionId);
			supervisionPointColumnFormPanel.operatorModal('show');
		} else if (config.operator == 'modify' || config.operator == 'look') {
			$.ajax({
				type : "GET", data : {id: config.id}, dataType : "json",
				url : getRootPath() + "/supervise/pointcolumn/get.action",
				success : function(data) {
					if (data) {
						hideErrorMessage();
						$("#entityColumnForm").fill(data);
						if (data.dataValueFile != '') {
							$('#entityColumnForm #dataValue').val(data.dataValueFile);
						}
						supervisionPointColumnFormPanel.dataSourceChange(data.dataSource);
						supervisionPointColumnFormPanel.operatorModal('show');
					}
				}
			});
		}
	};

	supervisionPointColumnFormPanel.formValidate = function() {
		if (!$("#entityColumnForm").valid()) {
			return false;
		}
		return true;
	};

	supervisionPointColumnFormPanel.saveOrModify = function () {
		if (supervisionPointColumnFormPanel.subState) {
			return;
		}
		supervisionPointColumnFormPanel.subState = true;
		if (!supervisionPointColumnFormPanel.formValidate()) {
			supervisionPointColumnFormPanel.subState = false;
			return;
		}
		var url = getRootPath() + "/supervise/pointcolumn/" + ($("#entityColumnForm #id").val() != '' ? 'update' : 'save') + '.action';
		jQuery.ajax({
			url: url, type: 'POST', cache: false, data: $("#entityColumnForm").serializeArray(),
			success : function(data) {
				supervisionPointColumnFormPanel.subState = false;
				$("#token").val(data.token);
				if(data.success == "true"){
					msgBox.tip({ type: "success", content: data.successMessage });
					supervisionPointColumnFormPanel.operatorModal('hide');
					supervisionPointColumnListPanel.renderTable();
				} else {
					if (data.labelErrorMessage) {
						showErrors("entityColumnForm", data.labelErrorMessage);
					} else{
						msgBox.info({ type:"fail", content: data.errorMessage });
					}
				}
			},
			error : function() {
				supervisionPointColumnFormPanel.subState = false;
				msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
			}
		});
	};

	/**
	 * 操作窗口开关
	 * @param operator
	 */
	supervisionPointColumnFormPanel.operatorModal = function (operator) {
		$('#form-column-modal').modal(operator);
	};

	supervisionPointColumnFormPanel.dataSourceChange = function(value) {
		$('#dataClassTd').html('<span class=\'required\'>*</span>值')
		$('#dataValueTd').html('<span class=\'required\'>*</span>值');
		if (value == "bean") {
			$('#dataClassTd').html('<span class=\'required\'>*</span>类路径')
			$('#dataValueTd').html('<span class=\'required\'>*</span>对象属性');
		} else if (value == 'sql') {
			$('#dataClassTd').html('<span class=\'required\'>*</span>属性别名')
			$('#dataValueTd').html('<span class=\'required\'>*</span>sql语句');
		}
	};

	$(document).ready(function(){
		ie8StylePatch();
		$(".datepicker-input").each(function(){$(this).datepicker();});
		$('#entityColumnForm #dataSource').change(function () {
			supervisionPointColumnFormPanel.dataSourceChange($(this).val());
		});
	});

	$("#entityColumnForm").validate({
		ignore:'ignore',
		rules: {
			dataMeta: { required: true, maxlength: 255 },
			dataSource: { required: true },
			dataClass: { required: true, maxlength: 500},
			dataValue: { required: true, maxlength: 500 },
			queue: { required: true, number: true }
		}
	});
</script>