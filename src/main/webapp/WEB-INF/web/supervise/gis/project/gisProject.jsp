<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<link rel="stylesheet" href="https://js.arcgis.com/3.21/esri/css/esri.css">
<script src="https://js.arcgis.com/3.21/"></script>

<style>
	html,
	body,#map {
		height: 100%;
		width: 100%;
		margin: 0;
		padding: 0;
	}
</style>

<section class="scrollable padder jcGOA-section" id="scrollable">
	<div class="left-search">
		<div class="gis-search-box clearfix">
			<div class="searchbox-container">
				<div  class="searchbox-content">
					<input id="projectName" class="searchbox-content-common" type="text" name="word" autocomplete="off" maxlength="256" placeholder="请输入项目名称" value="" >
				</div>
			</div>
			<button id="searchBtn" class="search-button" data-title="搜索"></button>
		</div>
	</div>
	<div class="tuli">
		<h4>图例</h4>
		<ul>
			<li id="lou1" value="lou1,pt_project_approval,projectNumber"><span class="dian"><img src="${sysPath}/TDTLib/images/lou1.png"/></span>项目立项</li>
			<li id="lou2" value="lou2,pt_project_design,projectNumber"><span class="dian"><img src="${sysPath}/TDTLib/images/lou2.png"/></span>勘察设计</li>
			<li id="lou3" value="lou3,pt_company_projects_ztb,pcp_project_num"><span class="dian"><img src="${sysPath}/TDTLib/images/lou3.png"/></span>招投标</li>
			<li id="lou4" value="lou4,pt_company_projects_htba,pcp_project_num"><span class="dian"><img src="${sysPath}/TDTLib/images/lou4.png"/></span>合同备案</li>
			<li id="lou5" value="lou5,pt_project_finish,projectNumber"><span class="dian"><img src="${sysPath}/TDTLib/images/lou5.png"/></span>施工许可证</li>
			<li id="lou6" value="lou6,pt_project_safe,project_num"><span class="dian"><img src="${sysPath}/TDTLib/images/lou6.png"/></span>质量监督</li>
			<li id="lou7" value="lou7,pt_project_quality,project_num"><span class="dian"><img src="${sysPath}/TDTLib/images/lou7.png"/></span>安全监管</li>
			<li id="lou8" value="lou8,pt_company_projects_sgxk,pcp_project_num"><span class="dian"><img src="${sysPath}/TDTLib/images/lou8.png"/></span>竣工备案</li>
		</ul>
	</div>
	<div class="map-nav">
		<ul class="map-nave-content">
			<li style="position: relative;" id="li1">
				<div class="nav-nav">
					<img src="${sysPath}/images/zjjg.png" style="width: 32px;"/>
					<span style="display: block;">在建竣工</span>
				</div>
				<div class="check-content" >
					<div class="check-content-cn">
						<c:forEach items="${compList}" var="dic" varStatus="ci">
							<input name="completionId" value="${dic.code}" type="hidden">
							<input type="checkbox" id="completionId_${ci.index}" value="${dic.code}" /> ${dic.value}<br/>
						</c:forEach>
					</div>
				</div>
			</li>
			<li id="li2">
				<div class="nav-nav">
					<img src="${sysPath}/images/qyzb.png" style="width: 32px;"/>
					<span style="display: block;">企业占比</span>
				</div>
				<div class="check-content">
					<div class="check-content-cn" style="padding-top: 1px;">
						<div class="gis-div-title">企业占比</div>
						<table class="gis-table-information" style="margin: 15px;width: 500px;">
							<tr>
								<td>区域</td>
								<td>勘察单位</td>
								<td>设计单位</td>
								<td>施工单位</td>
								<td>监管单位</td>
								<td>总数</td>
								<td>对比</td>
							</tr>
							<c:forEach items="${areaMap}" var="area">
								<tr>
									<td width="16%">${area.key}</td>
									<td width="14%">${area.value.compType1}</td>
									<td width="14%">${area.value.compType2}</td>
									<td width="14%">${area.value.compType3}</td>
									<td width="14%">${area.value.compType4}</td>
									<td width="14%">${area.value.areaTotal}</td>
									<td width="14%">${area.value.proportion1}</td>
								</tr>
							</c:forEach>
						</table>

					</div>
				</div>
			</li>
			<li id="li3">
				<div class="nav-nav">
					<img src="${sysPath}/images/gcjz.png" style="width: 32px;"/>
					<span style="display: block;">高层建筑</span>
				</div>
				<div class="check-content">
					<div class="check-content-cn">
						<c:forEach items="${highList}" var="dic" varStatus="hi">
							<input name="highBuildId" value="${dic.code}" type="hidden">
							<input type="checkbox" id="highBuildId_${hi.index}" value="${dic.code}" /> ${dic.value}<br/>
						</c:forEach>
					</div>
				</div>
			</li>
			<li id="li4">
				<div class="nav-nav">
					<img src="${sysPath}/images/tzfl.png" style="width: 32px;"/>
					<span style="display: block;">项目分类</span>
				</div>
				<div class="check-content">
					<div class="check-content-cn">
						<c:forEach items="${projectCateList}" var="dic" varStatus="pi">
							<input name="projectCateId" value="${dic.code}" type="hidden">
							<input type="checkbox" id="projectCateId_${pi.index}" value="${dic.value}" /> ${dic.value}<br/>
						</c:forEach>
					</div>
				</div>
			</li>
			<li id="li5">
				<div class="nav-nav">
					<img src="${sysPath}/images/cz.png" style="width: 32px;"/>
					<span style="display: block;">产值/计划投资完成情况</span>
				</div>
				<div class="check-content">
					<div class="check-content-cn">
						<div class="gis-div-title">产值/计划投资完成情况</div>
						<table class="gis-table-information" style="margin: 15px;width: 500px;">
							<tr>
								<td>区域</td><td>产值</td><td>占比</td><td>投资完成金额</td><td>占比</td>
							</tr>
							<c:forEach items="${areaCzMap}" var="areaCz">
								<tr>
									<td width="20%">${areaCz.key}</td>
									<td width="20%">${areaCz.value.productionTotal}</td>
									<td width="18%">${areaCz.value.proportion1}</td>
									<td width="24%">${areaCz.value.amountCompleted}</td>
									<td width="18%">${areaCz.value.proportion2}</td>
								</tr>
							</c:forEach>
						</table>

					</div>
				</div>
			</li>
		</ul>

	</div>
	<div id="map"></div>
	<div id="info"></div>
	<input type="hidden" id="stage"/>
</section>
<div id="detailDiv"></div>
<div id="childDetailDiv"></div>

<%@ include file="/WEB-INF/web/include/foot.jsp"%>

<script>
	//gis-js
	var package_path = window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/'));
	// console.log(package_path);
	var dojoConfig = {

		async: true,
		locale: 'zh-cn'
	}

	//获取地图服务地址
	function getArcGisAddress() {
		return "${arcGisAddress}";
	}
</script>
<script>
	//gis-js
	var map;//全局map
	require([
				"${sysPath}/TDTLib/lib/TDTMapLayer.js",
				"esri/map",
				"esri/dijit/Scalebar",
				"esri/symbols/SimpleMarkerSymbol", "esri/symbols/SimpleLineSymbol","esri/symbols/PictureMarkerSymbol","esri/geometry/Point","esri/layers/GraphicsLayer",
				"esri/symbols/PictureFillSymbol", "esri/symbols/CartographicLineSymbol", "esri/SpatialReference","esri/InfoTemplate",
				"esri/graphic",
				"esri/layers/ArcGISDynamicMapServiceLayer","esri/symbols/TextSymbol","esri/symbols/Font","esri/Color",
				"dojo/domReady!"
			], function (TDTMapLayer, Map,Scalebar,SimpleMarkerSymbol, SimpleLineSymbol,PictureMarkerSymbol,Point,GraphicsLayer,
						 PictureFillSymbol, CartographicLineSymbol, SpatialReference,InfoTemplate,
						 Graphic,ArcGISDynmicMapServiceLayer,TextSymbol,Font,Color) {

				$.ajax({
					url: getRootPath() + "/supervise/gis/projectListByType.action", type: "GET",
					async: false,
					dataType: "json",
					success: function (resData) {
						graphicLoadCallback(resData);
					}
				});

				//绑定新建竣工事件
				$("input[name='completionId']").each(function(index){
					$("#completionId_"+index).click(function(){
						var data = {};
						//选中传值
						if($("#completionId_"+index).is(":checked")) {
							data["isFinish"]=$(this).val();
							typeClean("completionId_"+index);//取消所有选中并选中点击的复选框
						}
						projectListByType(data);
					});
				});

				//绑定高层建筑事件
				$("input[name='highBuildId']").each(function(index){
					$("#highBuildId_"+index).click(function(){
						var data = {};
						//选中传值
						if($("#highBuildId_"+index).is(":checked")) {
							data["highBuild"]=$(this).val();
							typeClean("highBuildId_"+index);//取消所有选中并选中点击的复选框
						}
						projectListByType(data);
					});
				});

				//绑定项目分类事件
				$("input[name='projectCateId']").each(function(index){
					$("#projectCateId_"+index).click(function(){
						var data = {};
						//选中传值
						if($("#projectCateId_"+index).is(":checked")) {
							data["projectCate"]=$(this).val();
							typeClean("projectCateId_"+index);//取消所有选中并选中点击的复选框
						}
						projectListByType(data);
					});
				});

				//项目名称
				$("#searchBtn").click(function(){
					var data = {};
					data["projectname"] = $("#projectName").val();
					projectListByType(data);
				});

				// //阶段
				// $(".tuli").find('li').each(function(index){
				// 	var i = index + 1;
				// 	$("#lou"+i).click(function(){
				// 		$("#stage").val("lou"+i);//设置阶段因此参数
				// 		var stageVal = $("#lou"+i).attr("value");
				// 		var stages = stageVal.split(",");
				// 		var data = {};
				// 		//选中传值
				// 		data["stage"]=stages[0];
				// 		data["tableName"]=stages[1];
				// 		data["columnName"]=stages[2];
				// 		typeClean('');//清空所有选中的复选框
				// 		projectListByStage(data);
				// 	});
				// });

				//根据字典查询项目坐标
				function projectListByType(data) {
					arcgisClear();//清空坐标
					$.ajax({
						url: getRootPath() + "/supervise/gis/projectListByType.action", type: "GET",
						async: false,
						data : data,
						dataType : "json",
						success : function(data) {
							graphicLoadCallback(data);
						}
					});
				};

				// //根据阶段查询项目坐标
				// function projectListByStage(data) {
				// 	arcgisClear();//清空坐标
				// 	$.ajax({
				// 		url: getRootPath() + "/supervise/gis/projectListByStage.action", type: "GET",
				// 		async: false,
				// 		data : data,
				// 		dataType : "json",
				// 		success : function(data) {
				// 			graphicLoadCallback(data);
				// 		}
				// 	});
				// };


				function graphicLoadCallback(dataList) {
					var position = [125.7160000, 44.0300000];
					//初始化地图取第一个坐标
					// if (dataList && dataList.length > 0) {
					//     for (var i = 0; i < dataList.length; i++) {
					//         item = dataList[i];
					//         if (item.latitude && item.latitude!="" && item.longitude && item.longitude!="" ) {
					//             position[0] = item.longitude;
					//             position[1] = item.latitude;
					//         }
					//     }
					//
					// }
					map = new Map('map', {
						logo: false,
						center: position,
						zoom: 10
					});
					/**
					 * TDTMapLayer 中包含四个参数，分别为
					 * 1.mapType：该值的默认值为vec,代表矢量图层，可供选择的值有vec|img|ter，分别代表矢量、影像、地形
					 * 2.mapCoordinate：该值的默认值为w,代表投影坐标，可供选择的值有c|w分比为：地理坐标，投影坐标
					 * 3.isAnnoLayer：默认值为"",可供选择的值为"ss"|"ano",分别代表非注记图层，注记图层
					 * 4.mapAnnotation：默认值为cva,不同注记类型的默认值分别为cva,cia,cta,代表矢量中文注记图层，不同底图的矢量注记可选项有：
					 *   vec(矢量):cva(中文)、eva(英文)、mva(蒙古文)、wva(维吾尔文)
					 *   img(影像):cia(中文)、eia(英文)、mia(蒙古文)、wia(维吾尔文)
					 *   ter(地形图):cta(中文)、eta(英文)、mta(蒙古文)、wta(维吾尔文)
					 */
							//1.加载矢量图层（默认投影web墨卡托）
					let verLayer = new TDTMapLayer();
					//2.加载矢量注记图层
					let verAnnoLayer = new TDTMapLayer("ano");
					//3.加载影像图层
					let imgLayer = new TDTMapLayer("img", "c");
					//4.加载影像注记图层
					let imgAnnoLayer = new TDTMapLayer("img", "c", "ano");

					var _Dynamiclayer = new ArcGISDynmicMapServiceLayer(getArcGisAddress());
					_Dynamiclayer.setOpacity("60%");
					map.addLayer(verLayer);
					map.addLayer(verAnnoLayer);
					map.addLayer(_Dynamiclayer);

					//点击事件
					map.graphics.on("click",function(e){
						var g = e.graphic;
						var nowProjectId = g.attributes.projectId;
						if(nowProjectId){
							clickProjectTarget(nowProjectId);
						}
						e.stopPropagation();
					});

					var stage = $("#stage").val();//项目阶段
					var zbimgName = "lou1.png";//默认坐标图片
					// if(stage != ''){
					// 	zbimgName = stage + ".png";
					// }

					var item;
					for (var i = 0; i < dataList.length; i++) {
						item = dataList[i];
						if (item.latitude && item.latitude!="" && item.longitude && item.longitude!="" ) {
							var newPoint = new Point(item.longitude, item.latitude, new SpatialReference({wkid: 4326}));
							zbimgName = imgName(item);
							var picSymbol = new PictureMarkerSymbol("${sysPath}/TDTLib/images/"+zbimgName, 22, 35);
							picSymbol.setOffset(0, 30);

							var picGraphic = new Graphic(newPoint, picSymbol);
							picGraphic.setAttributes( {"projectId":item.dlhDataId});
							// var infoTemplate = new InfoTemplate();
							// infoTemplate.setTitle(item.projectnumber);
							// infoTemplate.setContent(item.projectnumber);
							// picGraphic.setInfoTemplate(infoTemplate);
							map.graphics.add(picGraphic);

							var graphicsLayer = new GraphicsLayer();
							var font = new Font("12pt");
							font.setFamily("Arial");
							font.setWeight(Font.WEIGHT_BOLD);
							font.setDecoration("none");
							font.setStyle(Font.STYLE_NORMAL);
							var textSym = new TextSymbol();
							textSym.setAlign(TextSymbol.ALIGN_RIGHT);
							textSym.setColor(new Color("#602EFF"));
							textSym.setFont(font);
							//textSym.setText(item.projectname);
							textSym.setHaloColor(new Color([255, 255, 255, 1]));  //
							textSym.setHaloSize(1);  //

							//添加为每个文字graphic添加位置、文字内容
							var graphicText = new Graphic(newPoint, textSym);
							//将graphic加入图层
							graphicsLayer.add(graphicText);
							//map添加graphiclayer图层
							map.addLayer(graphicsLayer);
						}
					}
					dojo.connect(map, "onExtentChange", showExtent);
					var scalebar = new Scalebar({
						map: map,
						attachTo: "bottom-left",
						scalebarUnit: "dual"
					});


					function showExtent(extent) {
						var s = "";
						s = "XMin: " + extent.xmin + "&nbsp;"
								+ "YMin: " + extent.ymin + "&nbsp;"
								+ "XMax: " + extent.xmax + "&nbsp;"
								+ "YMax: " + extent.ymax;                    //showExtent函数的最后一行显示在页面上添加完成字符串的“info”DIV坐标
						dojo.byId("info").innerHTML = s;
					};


				}
			}
	);

	//查询项目坐标
	function queryProjectJwd(isFinish,highBuild,investment) {
		var projectJwd = "[]";
		$.ajax({
			url: getRootPath() + "/supervise/gis/projectGisList.action", type: "GET",
			async: false,
			data : {
				"isFinish" : isFinish,
				"highBuild" : highBuild,
				"investment" : investment
			},
			dataType : "json",
			success : function(data) {
				console.log(data);
				projectJwd = data;
			}
		});
		return projectJwd;
	};

	//查看项目详细信息
	function clickProjectTarget(id) {
		$("#detailDiv").html("");//先将div置空
		$("#detailDiv").load(getRootPath()+"/supervise/gis/loadDetail.action?viewType=queryView&dlhDataId="+id+"&dlh_data_src_="+projectListPanel.resource+"&time="+new Date().getTime(), null, function () {
			modalShow('项目详细信息');
		});
	}

	//清空坐标点
	function arcgisClear(){
		map.destroy();
	}

	//取消所有复选框选中状态
	function typeClean(checkId){
		$("input:checkbox").each(function(ix){
			$(this).prop("checked",false);
		});
		if(checkId != ''){
			$("#"+checkId).prop("checked",true);//选中
		}
	}

	//清空阶段选中
	function stageClean(){
		$("#stage").val("");//清空阶段
		$(".tuli>ul>li").removeClass('check-change-active');//清空样式
	}

	//根据阶段返回阶段图片名称
	function imgName(item){
		var imgName = "";
		if(item.stageCount8>0){
			imgName = "lou8";
		}else if(item.stageCount7>0){
			imgName = "lou7";
		}else if(item.stageCount6>0){
			imgName = "lou6";
		}else if(item.stageCount5>0){
			imgName = "lou5";
		}else if(item.stageCount4>0){
			imgName = "lou4";
		}else if(item.stageCount3>0){
			imgName = "lou3";
		}else if(item.stageCount2>0){
			imgName = "lou2";
		}else{
			imgName = "lou1";
		}
		return imgName + ".png";
	}
</script>

<script src='${sysPath}/js/com/jc/supervise/gis/project/gisProject.js'></script>

<script>
	$(window).ready(function () {
		$('#list-modal').modal('hide');
		// var $top = $('.map-nave-content').offset().top + $('.map-nave-content').height();
		$(".map-nave-content>li").on('mouseenter',function(e){
			//alert(e.target.id);
			// $('#list-modal').modal('show');
			// var $height = $(this).offset().top + $(this).find('.check-content').outerHeight();
			$(this).css('background','#eeeeee');
			$(this).find('.check-content').css('display','block');
			// if($height - $top >= 0) {
			//     $(this).find('.check-content').css({
			//        top: -($height - $top) + 'px'
			//     });
			// }

		});
		$('.map-nave-content>li').on('mouseleave', function(e) {
			$(this).css('background','#fff');
			$(this).find('.check-content').css('display','none');
		});

		//阶段点击样式
		// $(".tuli>ul>li").click(function () {
		// 	$(".tuli>ul>li").removeClass('check-change-active');
		// 	$(this).addClass("check-change-active");
		// })

	})

</script>