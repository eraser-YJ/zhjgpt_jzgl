<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="hoist-modal-view" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">

				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">升降机实时数据查看</h4>
			</div>
			<div class="modal-body">
				<div id="hoistDataDiv" style="height: 750px;width: 100%; border: 1px solid #DDDDDD; display: flex;">
					<ul id="dtul" class="gis-nav-tabs">
					</ul>
					<div id="hoistViewDiv" class="gis-tab-box" style="display: none;">
						<table class="table table-td-striped" style="margin: 15px;">
							<tr>
								<td style="width:150px;">属性</td>
								<td>实时值</td>
							</tr>
							<tr>
								<td>高度</td>
								<td><span id="real_time_height"></span></td>
							</tr>
							<tr>
								<td>高度百分比</td>
								<td><span id="height_percentage"></span></td>
							</tr>
							<tr>
								<td>重量</td>
								<td><span id="real_time_lifting_weight"></span></td>
							</tr>
							<tr>
								<td>重量百分比</td>
								<td><span id="weight_percentage"></span></td>
							</tr>
							<tr>
								<td>倾斜度1</td>
								<td><span id="real_time_gradient1"></span></td>
							</tr>
							<tr>
								<td>倾斜度3</td>
								<td><span id="real_time_gradient2"></span></td>
							</tr>
							<tr>
								<td>倾斜百分比1</td>
								<td><span id="tilt_percentage1"></span></td>
							</tr>
							<tr>
								<td>倾斜百分比2</td>
								<td><span id="tilt_percentage2"></span></td>
							</tr>
							<tr>
								<td>人数</td>
								<td><span id="real_time_number_of_people"></span></td>
							</tr>
							<tr>
								<td>速度原始格式</td>
								<td><span id="real_time_speed"></span></td>
							</tr>
							<tr>
								<td>速度方向</td>
								<td><span id="real_time_speed_direction"></span></td>
							</tr>
							<tr>
								<td>速度</td>
								<td><span id="wind_speed"></span></td>
							</tr>
							<tr>
								<td>门锁状态</td>
								<td><span id="lock_state"></span></td>
							</tr>
						</table>
					</div>
				</div>
				<div id="hoistEmptyDiv" style="height: 350px;width: 100%; border: 1px solid #DDDDDD; display: none;text-align: center;">
					<h3 style="margin:0 !important; padding:13px 0 13px 20px; background-color:#f1f1f1; border:1px solid #ececec; text-align: center !important; font-size: 17px !important;">没有匹配的记录</h3>
				</div>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn" type="button" id="hoistDivClose">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script>

	var hoistView = {};
	hoistView.show = function (){
		$('#hoist-modal-view').modal('show');
	};

	//塔式起重机设备信息
	hoistView.equipmentFun = function(projectCode){
		var dthtml = "";
		$.ajax({
			type : "GET",
			url : getScsAddress() + "/api/system/equipmentList.action",
			data : {
				"projectCode": projectCode,
				"equipmentType" : "building_hoist"
			},
			dataType : "json",
			async : false,
			success : function(data) {
				if(data.length==0){
					$("#hoistEmptyDiv").show();
					$("#hoistDataDiv").hide();
				}else{
					$("#hoistEmptyDiv").hide();
					$("#hoistDataDiv").show();
					for(var i=0;i<data.length;i++){
						var item = data[i];
						dthtml +='<li onclick="hoistView.hoistRealInfo(\'' + item.id + '\')" style="cursor:pointer;"> '+item.equipmentTypeValue+'</li>';
					}
				}
			}
		});
		$("#dtul").html(dthtml);
	};

	//塔式起重机设备详细信息
	hoistView.hoistRealInfo = function(id){

		// var dthtml = "";
		$.ajax({
			type : "GET",
			url : getScsAddress() + "/api/system/getHoistRealInfo.action",
			data : {
				"id": id
			},
			dataType : "json",
			async : false,
			success : function(data) {
				$("#hoistViewDiv").show();
				if(data.code == 200){
					var data = data.data;
					$("#real_time_height").html(data.real_time_height);
					$("#height_percentage").html(data.height_percentage);
					$("#real_time_lifting_weight").html(data.real_time_lifting_weight);
					$("#weight_percentage").html(data.weight_percentage);
					$("#real_time_gradient1").html(data.real_time_gradient1);
					$("#real_time_gradient2").html(data.real_time_gradient2);
					$("#tilt_percentage1").html(data.tilt_percentage1);
					$("#tilt_percentage2").html(data.tilt_percentage2);
					$("#real_time_number_of_people").html(data.real_time_number_of_people);
					$("#real_time_speed").html(data.real_time_speed);
					$("#real_time_speed_direction").html(data.real_time_speed_direction);
					$("#wind_speed").html(data.wind_speed);
					$("#lock_state").html(data.lock_state);
				}

			}
		});
		// $("#dtdiv").html(dthtml);
	};

	$(document).ready(function(){
		ie8StylePatch();

		$("#hoistDivClose").click(function(){$('#hoist-modal-view').modal('hide');});

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
