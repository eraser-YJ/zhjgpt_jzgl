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
							<td colspan="5"><input type="text" readonly id="hoist_name" name="hoist_name"/></td>
							<td>转发位置</td>
							<td><input type="text" readonly id="forward" name="forward"/></td>
						</tr>
						<tr>
							<td>备案编号</td>
							<td><input type="text" readonly id="record_number" name="record_number"/></td>
							<td>产权单位</td>
							<td><input type="text" readonly id="property_unit" name="property_unit"/></td>
							<td>租凭到期时间</td>
							<td><input type="text" readonly id="lease_expiration_time" name="lease_expiration_time"/></td>
							<td>备案状态</td>
							<td><input type="text" readonly id="record_status" name="record_status"/></td>
						</tr>
						<tr>
							<td>施工状态</td>
							<td><input type="text" readonly id="construction_state" name="construction_state"/></td>
							<td>拆卸日期</td>
							<td><input type="text" readonly id="remove_date" name="remove_date"/></td>
							<td>经度</td>
							<td><input type="text" readonly id="longitude" name="longitude"/></td>
							<td>纬度</td>
							<td><input type="text" readonly id="latitude" name="latitude"/></td>
						</tr>
						<tr>
							<td>sim卡号</td>
							<td><input type="text" readonly id="sim_card_number" name="sim_card_number"/></td>
							<td>项目名称</td>
							<td><input type="text" readonly id="project_name" name="project_name"/></td>
							<td>数据更新时间</td>
							<td><input type="text" readonly id="update_time" name="update_time"/></td>
							<td>最近一次上线时间</td>
							<td><input type="text" readonly id="offline_start_time" name="offline_start_time"/></td>
						</tr>
						<tr>
							<td>最近一次下线时间</td>
							<td><input type="text" readonly id="offline_end_time" name="offline_end_time"/></td>
							<td>报警次数</td>
							<td><input type="text" readonly id="alarm_count" name="alarm_count"/></td>
							<td>今日报警次数</td>
							<td><input type="text" readonly id="today_alarm_count" name="today_alarm_count"/></td>
							<td>在线状态</td>
							<td><input type="text" readonly id="online_status" name="online_status"/></td>
						</tr>
						<tr>
							<td>报警状态</td>
							<td><input type="text" readonly id="alarm_state" name="alarm_state"/></td>
							<td>总在线时长</td>
							<td><input type="text" readonly id="totalOnlineTime" name="totalOnlineTime"/></td>
							<td>设备编号</td>
							<td><input type="text" readonly id="cage_id" name="cage_id"/></td>
							<td>出厂编号(必填)</td>
							<td><input type="text" readonly id="hoist_box_id" name="hoist_box_id"/></td>
						</tr>
						<tr>
							<td>最大起重量预警</td>
							<td><input type="text" readonly id="maximum_elevating_capacity_warning" name="maximum_elevating_capacity_warning"/></td>
							<td>最大起重量报警</td>
							<td><input type="text" readonly id="maximum_elevating_capacity_alarm" name="maximum_elevating_capacity_alarm"/></td>
							<td>最大起升高度</td>
							<td><input type="text" readonly id="maximum_rise_height" name="maximum_rise_height"/></td>
							<td>最大速度预警</td>
							<td><input type="text" readonly id="maximum_speed_warning" name="maximum_speed_warning"/></td>
						</tr>
						<tr>
							<td>最大速度报警</td>
							<td><input type="text" readonly id="maximum_speed_alarm" name="maximum_speed_alarm"/></td>
							<td>最大承载人数</td>
							<td><input type="text" readonly id="maximum_carrying_number" name="maximum_carrying_number"/></td>
							<td>倾斜预警值</td>
							<td><input type="text" readonly id="tilt_warning" name="tilt_warning"/></td>
							<td>倾斜报警值</td>
							<td><input type="text" readonly id="tilt_alarm" name="tilt_alarm"/></td>
						</tr>
						<tr>
							<td>重量空载AD值</td>
							<td><input type="text" readonly id="weight_noload_AD" name="weight_noload_AD"/></td>
							<td>重量空载实际值</td>
							<td><input type="text" readonly id="weight_noload_actual" name="weight_noload_actual"/></td>
							<td>重量负载AD值</td>
							<td><input type="text" readonly id="weight_load_AD" name="weight_load_AD"/></td>
							<td>重量负载实际值</td>
							<td><input type="text" readonly id="weight_load_actual" name="weight_load_actual"/></td>
						</tr>
						<tr>
							<td>高度底端AD值</td>
							<td><input type="text" readonly id="height_bottom_AD" name="height_bottom_AD"/></td>
							<td>高度底端实际值</td>
							<td><input type="text" readonly id="height_bottom_actual" name="height_bottom_actual"/></td>
							<td>高度顶端AD值</td>
							<td><input type="text" readonly id="height_top_AD" name="height_top_AD"/></td>
							<td>高度顶端实际值</td>
							<td><input type="text" readonly id="height_top_actual" name="height_top_actual"/></td>
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