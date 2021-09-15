<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-Exinfo" aria-hidden="false">
	<div class="modal-dialog" style="width:1500px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityExinfoFormTitle">详细信息</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityExinfoForm">
					<input type="hidden" id="nowId" name="nowId" value="${id}"/>
					<input type="hidden" id="modifyDate" name="modifyDate" />
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td>设备名称</td>
							<td colspan="3"><input type="text" readonly id="cameraName" name="cameraName"/></td>
							<td>设备编码</td>
							<td  colspan="3"><input type="text" readonly id="cameraIndexCode" name="cameraIndexCode"/></td>
						</tr>
						<tr>
							<td>能力集说明</td>
							<td colspan="7"><input type="text" readonly id="capabilitySetName" name="capabilitySetName"/></td>
						</tr>
						<tr>
							<td>能力集编码</td>
							<td colspan="7"><input type="text" readonly id="capabilitySet" name="capabilitySet"/></td>
						</tr>
						<tr>
							<td>所属设备标识</td>
							<td ><input type="text" readonly id="encodeDevIndexCode" name="encodeDevIndexCode"/></td>

							<td>区域标识</td>
							<td ><input type="text" readonly id="regionIndexCode" name="regionIndexCode"/></td>
							<td>监控点类型</td>
							<td><input type="text" readonly id="cameraTypeName" name="cameraTypeName"/></td>
							<td>录像存储位置</td>
							<td><input type="text" readonly id="recordLocationName" name="recordLocationName"/></td>
						</tr>
						<tr>
							<td>经度</td>
							<td><input type="text" readonly id="longitude" name="longitude"/></td>
							<td>纬度</td>
							<td><input type="text" readonly id="latitude" name="latitude"/></td>
							<td>通道</td>
							<td><input type="text" readonly id="channelTypeName" name="channelTypeName"/></td>
							<td>传输协议</td>
							<td><input type="text" readonly id="transTypeName" name="transTypeName"/></td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn" type="button" id="closeExinfoBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script>
	var myExinfoJsForm = {};
	myExinfoJsForm.init = function() {
		var nowId = $("#nowId").val();
		$.ajax({
			type : "GET",
			data : {id: nowId},
			dataType : "json",
			url : getRootPath() + "/equipment/exinfo/getInfo.action",
			success : function(data) {
				if (data) {
					hideErrorMessage();
					$("#entityExinfoForm").fill(data);

				}
				myExinfoJsForm.operatorModal('show');
			}
		});
	};

	myExinfoJsForm.operatorModal = function (operator) {
		$('#form-modal-Exinfo').modal(operator);
	};

	$(document).ready(function(){
		ie8StylePatch();
		myExinfoJsForm.init();
		$(".datepicker-input").each(function(){$(this).datepicker();});
		$('#closeExinfoBtn') .click(function () { myExinfoJsForm.operatorModal('hide'); });
	});
</script>