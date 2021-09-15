<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="tower-modal-view" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">

				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">塔吊实时数据查看</h4>
			</div>
			<div class="modal-body">
				<div id="towerDataDiv" style="height: 600px;width: 100%; border: 1px solid #DDDDDD; display: flex;">
					<ul id="dtul" class="gis-nav-tabs">
					</ul>
					<div id="towerViewDiv" class="gis-tab-box" style="display: none;">
							<table class="table table-td-striped" style="margin: 15px;">
								
								<tr>
									<td style="width:150px;">属性</td>
									<td style="">实时值</td>
								</tr>
								
								<tr>
									<td>起重量</td>
									<td><span id="crane_elevating_capacity"></span></td>
								</tr>
								<tr>
									<td>高度</td>
									<td><span id="crane_height"></span></td>
								</tr>
								<tr>
									<td>幅度</td>
									<td><span id="crane_range"></span></td>
								</tr>
								<tr>
									<td>回转</td>
									<td><span id="crane_rotation"></span></td>
								</tr>
								<tr>
									<td>倾角</td>
									<td><span id="crane_tilt_angle"></span></td>
								</tr>
								<tr>
									<td>倾斜百分比</td>
									<td><span id="crane_tilt_percentage"></span></td>
								</tr>
								<tr>
									<td>力距百分比</td>
									<td><span id="crane_torque_percentage"></span></td>
								</tr>
								<tr>
									<td>重量百分比</td>
									<td><span id="crane_weight_percentage"></span></td>
								</tr>
								<tr>
									<td>风速</td>
									<td><span id="crane_wind_speed"></span></td>
								</tr>
								<tr>
									<td>风级</td>
									<td><span id="beaufort_scale"></span></td>
								</tr>
							</table>

						</table>
					</div>
				</div>
				<div id="towerEmptyDiv" style="height: 350px;width: 100%; border: 1px solid #DDDDDD; display: none;text-align: center;">
					<h3 style="margin:0 !important; padding:13px 0 13px 20px; background-color:#f1f1f1; border:1px solid #ececec; text-align: center !important; font-size: 17px !important;">没有匹配的记录</h3>
				</div>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn" type="button" id="towerDivClose">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script>

	var towerView = {};
	towerView.show = function (){
		$('#tower-modal-view').modal('show');
	};

	//塔式起重机设备信息
	towerView.equipmentFun = function(projectCode){
		var dthtml = "";
		$.ajax({
			type : "GET",
			url : getScsAddress() + "/api/system/equipmentList.action",
			data : {
				"projectCode": projectCode,
				"equipmentType" : "tower_crane"
			},
			dataType : "json",
			async : false,
			success : function(data) {
				if(data.length==0){
					$("#towerEmptyDiv").show();
					$("#towerDataDiv").hide();
				}else{
					$("#towerEmptyDiv").hide();
					$("#towerDataDiv").show();
					for(var i=0;i<data.length;i++){
						var item = data[i];
						dthtml +='<li onclick="towerView.towerRealInfo(\'' + item.id + '\')" style="cursor:pointer;"> '+item.equipmentName+'</li>';
					}
				}
			}
		});
		$("#dtul").html(dthtml);
	};

	//塔式起重机设备详细信息
	towerView.towerRealInfo = function(id){

		// var dthtml = "";
		$.ajax({
			type : "GET",
			url : getScsAddress() + "/api/system/getTowerRealInfo.action",
			data : {
				"id": id
			},
			dataType : "json",
			async : false,
			success : function(data) {
				$("#towerViewDiv").show();
				if(data.code == 200){
					var data = data.data;
					$("#crane_elevating_capacity").html(data.crane_elevating_capacity);
					$("#crane_height").html(data.crane_height);
					$("#crane_range").html(data.crane_range);
					$("#crane_rotation").html(data.crane_rotation);
					$("#crane_tilt_angle").html(data.crane_tilt_angle);
					$("#crane_tilt_percentage").html(data.crane_tilt_percentage);
					$("#crane_torque_percentage").html(data.crane_torque_percentage);
					$("#crane_weight_percentage").html(data.crane_weight_percentage);
					$("#crane_wind_speed").html(data.crane_wind_speed);
					$("#beaufort_scale").html(data.beaufort_scale);
				}
			}
		});
		// $("#dtdiv").html(dthtml);
	};

	$(document).ready(function(){
		ie8StylePatch();

		$("#towerDivClose").click(function(){$('#tower-modal-view').modal('hide');});

		$('.gis-tab-box>div').hide();
		$('.gis-tab-box>div').eq(0).show();
		$('.gis-nav-tabs li').click(function(){
			$('.gis-nav-tabs li').removeClass('gis-tab-active');
			$(this).addClass('gis-tab-active');
			var index=$(this).index();
			$('.gis-tab-box>div').hide().eq(index).show();
		})
	});
</script>
