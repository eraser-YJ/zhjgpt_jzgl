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
							<td colspan="3"><input type="text" readonly id="crane_name" name="crane_name"/></td>
							<td>转发位置</td>
							<td><input type="text" readonly id="forward" name="forward"/></td>
							<td>绳索倍率</td>
							<td><input type="text" readonly id="rope_ratio" name="rope_ratio"/></td>
						</tr>
						<tr>
							<td>施工状态</td>
							<td><input type="text" readonly id="construction_state" name="construction_state"/></td>
							<td>出厂编号</td>
							<td><input type="text" readonly id="black_box_id" name="black_box_id"/></td>
							<td>经度</td>
							<td><input type="text" readonly id="longitude" name="longitude"/></td>
							<td>纬度</td>
							<td><input type="text" readonly id="latitude" name="latitude"/></td>
						</tr>
						<tr>
							<td>报警次数</td>
							<td><input type="text" readonly id="alarm_count" name="alarm_count"/></td>
							<td>今日报警次数</td>
							<td><input type="text" readonly id="today_alarm_count" name="today_alarm_count"/></td>
							<td>备案编号</td>
							<td><input type="text" readonly id="record_number" name="record_number"/></td>
							<td>备案状态</td>
							<td><input type="text" readonly id="record_status" name="record_status"/></td>
						</tr>
						<tr>
							<td>拆卸日期</td>
							<td><input type="text" readonly id="remove_date" name="remove_date"/></td>
							<td>塔吊编号</td>
							<td><input type="text" readonly id="tower_crane_id" name="tower_crane_id"/></td>
							<td>坐标X</td>
							<td><input type="text" readonly id="coordinate_x" name="coordinate_x"/></td>
							<td>坐标Y</td>
							<td><input type="text" readonly id="coordinate_y" name="coordinate_y"/></td>
						</tr>
						<tr>
							<td>产权单位</td>
							<td><input type="text" readonly id="property_unit" name="property_unit"/></td>
							<td>租凭到期</td>
							<td><input type="text" readonly id="lease_expiration_time" name="lease_expiration_time"/></td>
							<td>塔机开始工作时间</td>
							<td><input type="text" readonly id="offline_start_time" name="offline_start_time"/></td>
							<td>塔机结束工作时间</td>
							<td><input type="text" readonly id="offline_end_time" name="offline_end_time"/></td>
						</tr>
						<tr>
							<td>起重臂长(前臂长)</td>
							<td><input type="text" readonly id="boom_length" name="boom_length"/></td>
							<td>平衡臂长</td>
							<td><input type="text" readonly id="balance_arm_length" name="balance_arm_length"/></td>
							<td>塔帽高</td>
							<td><input type="text" readonly id="tower_hat_height" name="tower_hat_height"/></td>
							<td>起重臂高</td>
							<td><input type="text" readonly id="boom_height" name="boom_height"/></td>
						</tr>
						<tr>
							<td>最大吊重</td>
							<td><input type="text" readonly id="maximum_lifting_weight" name="maximum_lifting_weight"/></td>
							<td>最大力矩</td>
							<td><input type="text" readonly id="maximum_torque" name="maximum_torque"/></td>
							<td>产权状态</td>
							<td><input type="text" readonly id="property_status" name="property_status"/></td>
							<td>塔机型号</td>
							<td><input type="text" readonly id="crane_type" name="crane_type"/></td>
						</tr>
						<tr>
							<td>生产厂商</td>
							<td><input type="text" readonly id="manufacturer" name="manufacturer"/></td>
							<td>吊钩重量</td>
							<td><input type="text" readonly id="hook_weight" name="hook_weight"/></td>
							<td>铰接长度</td>
							<td><input type="text" readonly id="articulated_length" name="articulated_length"/></td>
							<td>传感器安装状态</td>
							<td><input type="text" readonly id="sensor_installation_status" name="sensor_installation_status"/></td>
						</tr>
						<tr>
							<td>传感器安装状态</td>
							<td><input type="text" readonly id="bit_sensor_installation_status" name="bit_sensor_installation_status"/></td>
							<td>水平距离报警值</td>
							<td><input type="text" readonly id="alarm_horizontal_distance" name="alarm_horizontal_distance"/></td>
							<td>垂直距离报警值</td>
							<td><input type="text" readonly id="alarm_perpendicular_distance" name="alarm_perpendicular_distance"/></td>
							<td>重量报警值</td>
							<td><input type="text" readonly id="alarm_weight_distance" name="alarm_weight_distance"/></td>
						</tr>
						<tr>
							<td>力矩报警值</td>
							<td><input type="text" readonly id="alarm_torque" name="alarm_torque"/></td>
							<td>风速报警值</td>
							<td><input type="text" readonly id="alarm_wind_speed" name="alarm_wind_speed"/></td>
							<td>倾斜报警值</td>
							<td><input type="text" readonly id="alarm_tilt" name="alarm_tilt"/></td>
							<td>水平距离预警值</td>
							<td><input type="text" readonly id="warning_horizontal_distance" name="warning_horizontal_distance"/></td>
						</tr>
						<tr>
							<td>垂直距离预警值</td>
							<td><input type="text" readonly id="warning_perpendicular_distance" name="warning_perpendicular_distance"/></td>
							<td>重量预警值</td>
							<td><input type="text" readonly id="warning_weight_percentage" name="warning_weight_percentage"/></td>
							<td>力矩预警值</td>
							<td><input type="text" readonly id="warning_torque" name="warning_torque"/></td>
							<td>风速预警值</td>
							<td><input type="text" readonly id="warning_wind_speed" name="warning_wind_speed"/></td>
						</tr>
						<tr>
							<td>倾斜预警值</td>
							<td><input type="text" readonly id="warning_tilt" name="warning_tilt"/></td>
							<td>幅度限位起点值</td>
							<td><input type="text" readonly id="range_limit_starting_value" name="range_limit_starting_value"/></td>
							<td>幅度限位终点值</td>
							<td><input type="text" readonly id="range_limit_terminal_value" name="range_limit_terminal_value"/></td>
							<td>高度限位起点值</td>
							<td><input type="text" readonly id="height_limit_starting_value" name="height_limit_starting_value"/></td>
						</tr>
						<tr>
							<td>高度限位终点值</td>
							<td><input type="text" readonly id="height_limit_terminal_value" name="height_limit_terminal_value"/></td>
							<td>回转限位起点值</td>
							<td><input type="text" readonly id="rotation_limit_starting_value" name="rotation_limit_starting_value"/></td>
							<td>回转限位终点值</td>
							<td><input type="text" readonly id="rotation_limit_terminal_value" name="rotation_limit_terminal_value"/></td>
							<td>近端幅度标定AD值</td>
							<td><input type="text" readonly id="proximal_amplitude_demarcate_AD" name="proximal_amplitude_demarcate_AD"/></td>
						</tr>
						<tr>
							<td>近端幅度标定实际值</td>
							<td><input type="text" readonly id="proximal_amplitude_demarcate_actual" name="proximal_amplitude_demarcate_actual"/></td>
							<td>远端幅度标定AD值</td>
							<td><input type="text" readonly id="remote_service_demarcate_AD" name="remote_service_demarcate_AD"/></td>
							<td>远端幅度标定实际值</td>
							<td><input type="text" readonly id="remote_service_demarcate_actual" name="remote_service_demarcate_actual"/></td>
							<td>近端高度标定AD值</td>
							<td><input type="text" readonly id="proximal_height_demarcate_AD" name="proximal_height_demarcate_AD"/></td>
						</tr>
						<tr>
							<td>高度近端标定实际值</td>
							<td><input type="text" readonly id="proximal_height_demarcate_actual" name="proximal_height_demarcate_actual"/></td>
							<td>远端高度标定AD值</td>
							<td><input type="text" readonly id="remote_height_demarcate_AD" name="remote_height_demarcate_AD"/></td>
							<td>高度远端标定实际值</td>
							<td><input type="text" readonly id="remote_height_demarcate_actual" name="remote_height_demarcate_actual"/></td>
							<td>空载重量AD值</td>
							<td><input type="text" readonly id="noLoad_weight_AD" name="noLoad_weight_AD"/></td>
						</tr>
						<tr>
							<td>空载重量实际值</td>
							<td><input type="text" readonly id="noLoad_weight_actual" name="noLoad_weight_actual"/></td>
							<td>负载重量AD值</td>
							<td><input type="text" readonly id="load_weight_AD" name="load_weight_AD"/></td>
							<td>负载重量实际值</td>
							<td><input type="text" readonly id="load_weight_actual" name="load_weight_actual"/></td>
							<td>回转起点AD值</td>
							<td><input type="text" readonly id="rotation_starting_point_AD" name="rotation_starting_point_AD"/></td>
						</tr>
						<tr>
							<td>回转起点实际角度</td>
							<td><input type="text" readonly id="rotation_starting_point_actual" name="rotation_starting_point_actual"/></td>
							<td>回转终点AD值</td>
							<td><input type="text" readonly id="rotation_endinig_point_AD" name="rotation_endinig_point_AD"/></td>
							<td>回转终点实际角度</td>
							<td><input type="text" readonly id="rotation_endinig_point_actual" name="rotation_endinig_point_actual"/></td>
							<td>风速校准系数</td>
							<td><input type="text" readonly id="wind_speed_calibration" name="wind_speed_calibration"/></td>
						</tr>
						<tr>
							<td>预留字段1</td>
							<td><input type="text" readonly id="reserved" name="reserved"/></td>
							<td>预留字段2</td>
							<td><input type="text" readonly id="reserved2" name="reserved2"/></td>
							<td>倾斜校准系数</td>
							<td><input type="text" readonly id="tilt_the_calibration" name="tilt_the_calibration"/></td>
							<td>厂家代码</td>
							<td><input type="text" readonly id="crane_manufacturer_and_device_type" name="crane_manufacturer_and_device_type"/></td>
						</tr>
						<tr>
							<td>线性障碍物1起点x</td>
							<td><input type="text" readonly id="linear_obstacle_1_starts_at_x" name="linear_obstacle_1_starts_at_x"/></td>
							<td>线性障碍物1起点y</td>
							<td><input type="text" readonly id="linear_obstacle_1_starts_at_y" name="linear_obstacle_1_starts_at_y"/></td>
							<td>线性障碍物1起点z</td>
							<td><input type="text" readonly id="linear_obstacle_1_starts_at_z" name="linear_obstacle_1_starts_at_z"/></td>
							<td>线性障碍物1终点x</td>
							<td><input type="text" readonly id="linear_obstacle_1_terminal_x" name="linear_obstacle_1_terminal_x"/></td>
						</tr>
						<tr>
							<td>线性障碍物1终点y</td>
							<td><input type="text" readonly id="linear_obstacle_1_terminal_y" name="linear_obstacle_1_terminal_y"/></td>
							<td>线性障碍物1终点z</td>
							<td><input type="text" readonly id="linear_obstacle_1_terminal_z" name="linear_obstacle_1_terminal_z"/></td>
							<td>线性障碍物2起点x</td>
							<td><input type="text" readonly id="linear_obstacle_2_starts_at_x" name="linear_obstacle_2_starts_at_x"/></td>
							<td>线性障碍物2起点y</td>
							<td><input type="text" readonly id="linear_obstacle_2_starts_at_y" name="linear_obstacle_2_starts_at_y"/></td>
						</tr>
						<tr>
							<td>线性障碍物2起点z</td>
							<td><input type="text" readonly id="linear_obstacle_2_starts_at_z" name="linear_obstacle_2_starts_at_z"/></td>
							<td>线性障碍物2终点x</td>
							<td><input type="text" readonly id="linear_obstacle_2_terminal_x" name="linear_obstacle_2_terminal_x"/></td>
							<td>线性障碍物2终点y</td>
							<td><input type="text" readonly id="linear_obstacle_2_terminal_y" name="linear_obstacle_2_terminal_y"/></td>
							<td>线性障碍物2终点z</td>
							<td><input type="text" readonly id="linear_obstacle_2_terminal_z" name="linear_obstacle_2_terminal_z"/></td>
						</tr>
						<tr>
							<td>线性障碍物3起点x</td>
							<td><input type="text" readonly id="linear_obstacle_3_starts_at_x" name="linear_obstacle_3_starts_at_x"/></td>
							<td>线性障碍物3起点y</td>
							<td><input type="text" readonly id="linear_obstacle_3_starts_at_y" name="linear_obstacle_3_starts_at_y"/></td>
							<td>线性障碍物3起点z</td>
							<td><input type="text" readonly id="linear_obstacle_3_starts_at_z" name="linear_obstacle_3_starts_at_z"/></td>
							<td>线性障碍物3终点x</td>
							<td><input type="text" readonly id="linear_obstacle_3_terminal_x" name="linear_obstacle_3_terminal_x"/></td>
						</tr>
						<tr>
							<td>线性障碍物3终点y</td>
							<td><input type="text" readonly id="linear_obstacle_3_terminal_y" name="linear_obstacle_3_terminal_y"/></td>
							<td>线性障碍物3终点z</td>
							<td><input type="text" readonly id="linear_obstacle_3_terminal_z" name="linear_obstacle_3_terminal_z"/></td>
							<td>线性障碍物4起点x</td>
							<td><input type="text" readonly id="linear_obstacle_4_starts_at_x" name="linear_obstacle_4_starts_at_x"/></td>
							<td>线性障碍物4起点y</td>
							<td><input type="text" readonly id="linear_obstacle_4_starts_at_y" name="linear_obstacle_4_starts_at_y"/></td>
						</tr>
						<tr>
							<td>线性障碍物4起点z</td>
							<td><input type="text" readonly id="linear_obstacle_4_starts_at_z" name="linear_obstacle_4_starts_at_z"/></td>
							<td>线性障碍物4终点x</td>
							<td><input type="text" readonly id="linear_obstacle_4_terminal_x" name="linear_obstacle_4_terminal_x"/></td>
							<td>线性障碍物4终点y</td>
							<td><input type="text" readonly id="linear_obstacle_4_terminal_y" name="linear_obstacle_4_terminal_y"/></td>
							<td>线性障碍物4终点z</td>
							<td><input type="text" readonly id="linear_obstacle_4_terminal_z" name="linear_obstacle_4_terminal_z"/></td>
						</tr>
						<tr>
							<td>圆形障碍物1圆心x</td>
							<td><input type="text" readonly id="circle_obstacle_1_center_x" name="circle_obstacle_1_center_x"/></td>
							<td>圆形障碍物1圆心y</td>
							<td><input type="text" readonly id="circle_obstacle_1_center_y" name="circle_obstacle_1_center_y"/></td>
							<td>圆形障碍物1圆心z</td>
							<td><input type="text" readonly id="circle_obstacle_1_center_z" name="circle_obstacle_1_center_z"/></td>
							<td>圆形障碍物1半径r</td>
							<td><input type="text" readonly id="circle_obstacle_1_radius_r" name="circle_obstacle_1_radius_r"/></td>
						</tr>
						<tr>
							<td>圆形障碍物2圆心x</td>
							<td><input type="text" readonly id="circle_obstacle_2_center_x" name="circle_obstacle_2_center_x"/></td>
							<td>圆形障碍物2圆心y</td>
							<td><input type="text" readonly id="circle_obstacle_2_center_y" name="circle_obstacle_2_center_y"/></td>
							<td>圆形障碍物2圆心z</td>
							<td><input type="text" readonly id="circle_obstacle_2_center_z" name="circle_obstacle_2_center_z"/></td>
							<td>圆形障碍物2半径r</td>
							<td><input type="text" readonly id="circle_obstacle_2_radius_r" name="circle_obstacle_2_radius_r"/></td>
						</tr>
						<tr>
							<td>圆形障碍物3圆心x</td>
							<td><input type="text" readonly id="circle_obstacle_3_center_x" name="circle_obstacle_3_center_x"/></td>
							<td>圆形障碍物3圆心y</td>
							<td><input type="text" readonly id="circle_obstacle_3_center_y" name="circle_obstacle_3_center_y"/></td>
							<td>圆形障碍物3圆心z</td>
							<td><input type="text" readonly id="circle_obstacle_3_center_z" name="circle_obstacle_3_center_z"/></td>
							<td>圆形障碍物3半径r</td>
							<td><input type="text" readonly id="circle_obstacle_3_radius_r" name="circle_obstacle_3_radius_r"/></td>
						</tr>
						<tr>
							<td>圆形障碍物4圆心x</td>
							<td><input type="text" readonly id="circle_obstacle_4_center_x" name="circle_obstacle_4_center_x"/></td>
							<td>圆形障碍物4圆心y</td>
							<td><input type="text" readonly id="circle_obstacle_4_center_y" name="circle_obstacle_4_center_y"/></td>
							<td>圆形障碍物4圆心z</td>
							<td><input type="text" readonly id="circle_obstacle_4_center_z" name="circle_obstacle_4_center_z"/></td>
							<td>圆形障碍物4半径r</td>
							<td><input type="text" readonly id="circle_obstacle_4_radius_r" name="circle_obstacle_4_radius_r"/></td>
						</tr>
						<tr>
							<td>1号四边形障碍物A点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_1_A_point_x" name="quadrilateral_obstacle_1_A_point_x"/></td>
							<td>1号四边形障碍物A点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_1_A_point_y" name="quadrilateral_obstacle_1_A_point_y"/></td>
							<td>1号四边形障碍物A点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_1_A_point_z" name="quadrilateral_obstacle_1_A_point_z"/></td>
							<td>1号四边形障碍物B点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_1_B_point_x" name="quadrilateral_obstacle_1_B_point_x"/></td>
						</tr>
						<tr>
							<td>1号四边形障碍物B点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_1_B_point_y" name="quadrilateral_obstacle_1_B_point_y"/></td>
							<td>1号四边形障碍物B点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_1_B_point_z" name="quadrilateral_obstacle_1_B_point_z"/></td>
							<td>1号四边形障碍物C点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_1_C_point_x" name="quadrilateral_obstacle_1_C_point_x"/></td>
							<td>1号四边形障碍物C点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_1_C_point_y" name="quadrilateral_obstacle_1_C_point_y"/></td>
						</tr>
						<tr>
							<td>1号四边形障碍物C点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_1_C_point_z" name="quadrilateral_obstacle_1_C_point_z"/></td>
							<td>1号四边形障碍物D点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_1_D_point_x" name="quadrilateral_obstacle_1_D_point_x"/></td>
							<td>1号四边形障碍物AD点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_1_AD_point_y" name="quadrilateral_obstacle_1_AD_point_y"/></td>
							<td>1号四边形障碍物D点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_1_D_point_z" name="quadrilateral_obstacle_1_D_point_z"/></td>
						</tr>
						<tr>
							<td>2号四边形障碍物A点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_2_A_point_x" name="quadrilateral_obstacle_2_A_point_x"/></td>
							<td>2号四边形障碍物A点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_2_A_point_y" name="quadrilateral_obstacle_2_A_point_y"/></td>
							<td>2号四边形障碍物A点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_2_A_point_z" name="quadrilateral_obstacle_2_A_point_z"/></td>
							<td>2号四边形障碍物B点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_2_B_point_x" name="quadrilateral_obstacle_2_B_point_x"/></td>
						</tr>
						<tr>
							<td>2号四边形障碍物B点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_2_B_point_y" name="quadrilateral_obstacle_2_B_point_y"/></td>
							<td>2号四边形障碍物B点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_2_B_point_z" name="quadrilateral_obstacle_2_B_point_z"/></td>
							<td>2号四边形障碍物C点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_2_C_point_x" name="quadrilateral_obstacle_2_C_point_x"/></td>
							<td>2号四边形障碍物C点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_2_C_point_y" name="quadrilateral_obstacle_2_C_point_y"/></td>
						</tr>
						<tr>
							<td>2号四边形障碍物C点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_2_C_point_z" name="quadrilateral_obstacle_2_C_point_z"/></td>
							<td>2号四边形障碍物D点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_2_D_point_x" name="quadrilateral_obstacle_2_D_point_x"/></td>
							<td>2号四边形障碍物AD点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_2_AD_point_y" name="quadrilateral_obstacle_2_AD_point_y"/></td>
							<td>2号四边形障碍物D点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_2_D_point_z" name="quadrilateral_obstacle_2_D_point_z"/></td>
						</tr>
						<tr>
							<td>3号四边形障碍物A点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_3_A_point_x" name="quadrilateral_obstacle_3_A_point_x"/></td>
							<td>3号四边形障碍物A点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_3_A_point_y" name="quadrilateral_obstacle_3_A_point_y"/></td>
							<td>3号四边形障碍物A点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_3_A_point_z" name="quadrilateral_obstacle_3_A_point_z"/></td>
							<td>3号四边形障碍物B点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_3_B_point_x" name="quadrilateral_obstacle_3_B_point_x"/></td>
						</tr>
						<tr>
							<td>3号四边形障碍物B点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_3_B_point_y" name="quadrilateral_obstacle_3_B_point_y"/></td>
							<td>3号四边形障碍物B点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_3_B_point_z" name="quadrilateral_obstacle_3_B_point_z"/></td>
							<td>3号四边形障碍物C点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_3_C_point_x" name="quadrilateral_obstacle_3_C_point_x"/></td>
							<td>3号四边形障碍物C点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_3_C_point_y" name="quadrilateral_obstacle_3_C_point_y"/></td>
						</tr>
						<tr>
							<td>3号四边形障碍物C点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_3_C_point_z" name="quadrilateral_obstacle_3_C_point_z"/></td>
							<td>3号四边形障碍物D点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_3_D_point_x" name="quadrilateral_obstacle_3_D_point_x"/></td>
							<td>3号四边形障碍物AD点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_3_AD_point_y" name="quadrilateral_obstacle_3_AD_point_y"/></td>
							<td>3号四边形障碍物D点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_3_D_point_z" name="quadrilateral_obstacle_3_D_point_z"/></td>
						</tr>
						<tr>
							<td>4号四边形障碍物A点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_4_A_point_x" name="quadrilateral_obstacle_4_A_point_x"/></td>
							<td>4号四边形障碍物A点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_4_A_point_y" name="quadrilateral_obstacle_4_A_point_y"/></td>
							<td>4号四边形障碍物A点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_4_A_point_z" name="quadrilateral_obstacle_4_A_point_z"/></td>
							<td>4号四边形障碍物B点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_4_B_point_x" name="quadrilateral_obstacle_4_B_point_x"/></td>
						</tr>
						<tr>
							<td>4号四边形障碍物B点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_4_B_point_y" name="quadrilateral_obstacle_4_B_point_y"/></td>
							<td>4号四边形障碍物B点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_4_B_point_z" name="quadrilateral_obstacle_4_B_point_z"/></td>
							<td>4号四边形障碍物C点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_4_C_point_x" name="quadrilateral_obstacle_4_C_point_x"/></td>
							<td>4号四边形障碍物C点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_4_C_point_y" name="quadrilateral_obstacle_4_C_point_y"/></td>
						</tr>
						<tr>
							<td>4号四边形障碍物C点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_4_C_point_z" name="quadrilateral_obstacle_4_C_point_z"/></td>
							<td>4号四边形障碍物D点x</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_4_D_point_x" name="quadrilateral_obstacle_4_D_point_x"/></td>
							<td>4号四边形障碍物AD点y</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_4_AD_point_y" name="quadrilateral_obstacle_4_AD_point_y"/></td>
							<td>4号四边形障碍物D点z</td>
							<td><input type="text" readonly id="quadrilateral_obstacle_4_D_point_z" name="quadrilateral_obstacle_4_D_point_z"/></td>
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