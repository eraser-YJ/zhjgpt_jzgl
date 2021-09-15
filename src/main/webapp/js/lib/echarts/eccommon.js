/**
 * @title GOA V2.0
 * @description 封装百度图表插件echarts2.0.0
 * Copyright (c) 2014 ccyixun.cn Inc. All Rights Reserved
 * Company 长春奕迅建筑智能化工程有限公司
 * @author 都业广
 * @version  2014-06-24 14:33
 */
//定义命名空间
echartsCommon = {};

$.extend({
	createChart : function(option) {
		
		//动态获得X轴内容的情况
		if(option.xAxisUrl){
			option.xAxisData = echartsCommon.ajaxGetData(option.xAxisUrl,option.xAxisParm);
		}
		
		//遍历series,适用于混合图形的情况
		for(var i=0; i<option.series.length; i++){
			//配置了url的情况
			if(option.series[i].url){
				option.series[i].data = echartsCommon.ajaxGetData(option.series[i].url, option.series[i].data);
				
				//饼状图使用legend的情况
				if(option.series[i].enableLegend){
					
					var arrLegend = new Array();
					for(var j=0; j<option.series[i].data.length; j++){
						
						//有分组需求时,根据指定的数目进行分组
						if(option.legendFormatLineNo){
							if(j % option.legendFormatLineNo == 0){
								arrLegend.push("");
							}
						}
						arrLegend.push(option.series[i].data[j].name);
					}
					
					//图表上方的文字标注
					option.legend = {
						orient: option.legendOrient,
						x: option.legendPosX,
						y: option.legendPosY,
						formatter:function(name){
							return name.split("|")[0];
						},
				        data:arrLegend
				    };
				}
			}
		}
		
		//获得默认配置
		var defaultOption = echartsCommon.getDefaultOption(option);
		
		//柱状图X轴及Y轴的配置
		var barOption = echartsCommon.getBarOption(option);
		
		//启用legend
		if(option.legend){
			defaultOption.legend = option.legend;
		}
		//grid参数，设置图表左右留白宽度
		if(option.grid){
			defaultOption.grid = option.grid;
		}

		//图像类型
		var type = "";
		
		//设置数据
		for(var i=0; i<option.series.length; i++){
			type = type + option.series[i].type;
		}

		//tips 类型
		var triggerType;
		if(type.indexOf('pie') >= 0){
			//带有饼状图的情况适用item方式
			triggerType = 'item';
		}else{
			//混合图形时轴显示方式
			triggerType = option.series.length > 1 ? 'axis':'item';
		}
		
		//混合图形的判断
		var mixOption={
			tooltip : {
				trigger: triggerType
			}
		};
		
		//保存设置
		$.extend(true, defaultOption, mixOption);
		
		//柱状图时合并选项
		if(type.indexOf('bar') >= 0 || type.indexOf('line') >= 0){
			$.extend(true, defaultOption, barOption);
		}
		
		//合并默认配置
		var newOption = $.extend(true, {}, defaultOption, option.options);
		
		//初始化图表
		chart = echarts.init($('#' + option.elementId)[0]);
		
		// 过渡
		chart.showLoading({
		    text: '读取数据中...',    //loading话术
		});

		//设置图标的配置
		chart.setOption(newOption);
		
		//绘制图形
		chart.setSeries(option.series);
		
		// ajax callback
		chart.hideLoading();
		
		return chart;
	}
});

/**
 * 默认的共通配置
 * @param option:用户设置的全局配置参数
 */
echartsCommon.getDefaultOption = function(option){
	
	//设置DIV的宽度
	$('#' + option.elementId).css("width", option.width);

	//设置DIV的高度
	$('#' + option.elementId).css("height", option.height);
	
	//IE浏览器ie8以下版本时不适用
	if(navigator.userAgent.indexOf("MSIE 6.0")>0 || 
			navigator.userAgent.indexOf("MSIE 7.0")>0 || 
			navigator.userAgent.indexOf("MSIE 8.0")>0)  
	{
		option.calculable = false;
	}
	
	var defaultOption = {
			title : {
				text : option.title,
				subtext : option.subTitle,
				x : option.titlePosX,
				y : option.titlePosY
			},
			//鼠标经过数据时的提示
			tooltip : {
				show : option.tooltip,
				formatter : option.tipsFormat
			},
			//拖拽重新计算功能
			calculable : option.calculable,
			//右上角的特殊功能区
			toolbox : {
				show : option.toolboxEnabled,
				feature : {
					//辅助线功能
					mark : {
						show : true
					},
					//数据视图功能
					dataView : {
						show : false,
						readOnly : false
					},
					//视图切换功能
					magicType : {
						show : true,
						type : [ 'line', 'bar' ]
					},
					//还原功能
					restore : {
						show : true
					}
					//,
					//保存图片功能
					//saveAsImage : {
					//	show : true
					//}
				}
			}
		};
	return defaultOption;
};

/**
 * 柱状图设置
 * @param option:用户设置的全局配置参数
 */
echartsCommon.getBarOption = function(option){
	
	var barOption = {
			xAxis : [ {
				type : option.xAxisType,
				data : option.xAxisData,
				boundaryGap : option.xBoundaryGap,
				axisLabel : {
	                formatter: option.xAxisFormatter
	            }
			} ],
			yAxis : [ {
				type : option.yAxisType,
				data : option.yAxisData,
				boundaryGap : option.yBoundaryGap,
				axisLabel : {
	                formatter: option.yAxisFormatter
	            },
				splitArea : {show : true}
			} ]
		};
	return barOption;
};

/**
 * ajax获得轴数据
 * @param url:请求地址
 * @param data:请求参数
 */
echartsCommon.ajaxGetData = function(url, data){
	
	//定义数据数组
	var arrAxis = new Array();
	
	//发送ajax请求
	$.ajax({
		url:url,
		data:data,
		dataType:"text",
		async: false,
		success:function(data){
			//解析返回的数据串
			var objArr = eval(data);
			if(objArr != ""){
				for(var i=0;i<objArr.length; i++){
					arrAxis.push(objArr[i]);
				}
			} else {
				arrAxis.push("");
			}
		}
	});
	
	return arrAxis;
};