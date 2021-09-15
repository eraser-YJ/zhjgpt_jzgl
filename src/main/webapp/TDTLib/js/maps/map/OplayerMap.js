/***
 * @Description 基础地图类，该类用于初始化基础底图、图层、控制器，提供相关图层设置，点、线、面的添加
 * @Author YuanJianHua
 * @CreateTime 2015年12月15日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var OplayerMap=function(){
	
//------------------------------------私有属性-------------------------------------------------------------------------	
	/**
     * 正式点结果集
     */
    var markers = [];
    
    /**
     * 临时点结果集
     */
    var tempMarkers=[];
    
    /**
     * 购物车点结果集
     */
    var shopMarkers=[];
//------------------------------------公有属性-------------------------------------------------------------------------
   
    /**
     * 地图对象
     */
    this.map=null;
    
    /**
     * 控制器
     */
    this.controls=null;
    
    /**
     * 地图工具类对象
     */
    this.mapUtils=null;
    
//------------------------------------特权方法，相关set、get方法------------------------------------------------------------------
	
	/**
	 * 获取地图对象
	 */
	this.getMap=function(){
		return this.map;
	};
	
	/**
	 * 获取地图控制器
	 */
	this.getControls = function() {
        return this.controls;
    };
    
    /**
     * 获取购物车Vector图层对象
     */
    this.getShopLayer = function(){
    	return this.shopLayer;
    };
    
    /**
     * 获取购物车Marker图层对象
     */
    this.getShopMarkerLayer = function(){
    	return this.shopMarkerLayer;
    };
    
    /**
     *  激活控制器
     *  @param ctrlName <String>: 控制器名称
     */
    this.toggleControl = function(ctrlName) {
        for (var key in this.controls) {
            var ctrl = this.controls[key];
            if (key == ctrlName) {
                ctrl.activate();
            } else {
                ctrl.deactivate();
            }
        }
        this.measureLayer.removeAllFeatures();
    };
    
    /***
     *  将div加入到地图控件上
     */
    this.addDiv = function(element) {
        var viewportDiv = this.map.getViewport();
        var $container = $(viewportDiv);
        $container.append(element);
    };
    
    /**
     * 设置Markers数组值
     */
    this.setMarkers=function(arry){
    	markers=arry;
    }

//------------------------------------特权方法，地图添加点、线、面------------------------------------------------------------------
    
    /**
     * 图层添加点，如果指定layerName，在指定图层上添加点，否则默认在markerLayer上添加
     * @param x <number>: 坐标x
     * @param y <number>: 坐标y
     * @param icon <OpenLayers.Icon>: 图标对象
     * @param option <Obejct>: marker的参数
     * @param layerName <String>: 图层名称（选填）
     * @return marker <OpenLayers.Marker>
     */
    this.addMarker = function(x, y, icon, option,layerName) {
        var lnglat = new OpenLayers.LonLat(x, y);
        var marker = new OpenLayers.Marker(lnglat, icon);
        for (var key in option) {
            var value = option[key];
            marker[key] = value;
        }
        var layer = this[layerName];
        layer = layer?layer:this.markerLayer;
        layer.addMarker(marker);
        if(layerName=="markerLayer"){
        	markers.push(marker);
        }else if(layerName=="tempMarkerLayer"){
        	tempMarkers.push(marker);
        }else if(layerName=="shopMarkerLayer"){
        	shopMarkers.push(marker);
        }else{
        	markers.push(marker);
        }
        return marker;
    };

    /**
     * 图层添加面，如果指定layerName，在指定图层上添加面，否则默认在resultLayer上添加
     * @param wkt <String>: 图标wkt字符串（必填）
     * @param option <Obejct>: polygon的参数（选填）
     * @param style <Object>: 样式对象（选填）
     * @param layerName <String>: 图层名称（选填）
     * @return vector <OpenLayers.Feature.Vector>
     */
    this.addPolygon = function(wkt, option, style,layerName) {
        if(wkt==null||wkt=="null"||wkt==""){
        	return null;
        }
    	var vector = this.mapUtils.getVectorFromWKT(wkt);
        vector.attributes = {};
        for (var key in option) {
            var value = option[key];
            vector.attributes[key] = value;
        }
        vector.style = style;
        var layer = this[layerName];
        layer = layer?layer:this.resultLayer;
        layer.addFeatures([vector], {
            silent : true
        });

        return vector;
    };
    
    this.addPoint = function(wkt,style,layerName) {
        if(wkt==null||wkt=="null"||wkt==""){
        	return null;
        }
    	var geometry  = this.mapUtils.getVectorFromWKT(wkt);
    	geometry.styleMap = new OpenLayers.StyleMap(style);
        var layer = this[layerName];
        layer = layer?layer:this.resultLayer;
        layer.addFeatures(geometry);
        return geometry;
    };
    
    /**
     * 图层添加线，如果指定layerName，在指定图层上添加线，否则默认在resultLayer上添加
     * @param pointList: OpenLayers.Geometry.Point list
     * @param layerName <String>: 图层名称
     */
    this.addLineByList = function(pointList,layerName){
    	var style={
			strokeColor: "red", 
			strokeWidth: 3, 
			strokeOpacity:1,
			strokeStyle:"dashed"
    	};
    	var vector = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(pointList));
    	vector.style = style;
    	var layer = this[layerName];
        layer = layer?layer:this.resultLayer;
        layer.addFeatures([vector], {
            silent : true
        });
        return vector;
    };
    
    /**
     * 图层添加线，如果指定layerName，在指定图层上添加线，否则默认在resultLayer上添加
     * @param pointList: OpenLayers.Geometry.Point list
     * @param layerName <String>: 图层名称
     */
    this.addLineByWKT = function(wkt,layerName){
    	var style={
			strokeColor: "red", 
			strokeWidth: 3, 
			strokeOpacity:1,
			strokeStyle:"dashed"
    	};
    	var vector = this.mapUtils.getVectorFromWKT(wkt);
    	vector.style = style;
    	var layer = this[layerName];
        layer = layer?layer:this.resultLayer;
        layer.addFeatures([vector], {
            silent : true
        });
        return vector;
    };

//------------------------------------特权方法，图层清除、可见设置------------------------------------------------------------------
	
	/**
     *  图层清除（道路图层保留）
     */
    this.clearLayers = function(all) {
        this.clearMeasureLayer();
        this.clearDrawLayer();
        this.clearAreaLayer();
        this.clearResultLayer();
        this.clearLookLayer();
        this.clearMarkerLayer();
        this.clearTempMarkerLayer();
    };
    
	/**
     *  清除所有图层清除
     */
    this.clearAllLayers = function(all) {
        this.clearMeasureLayer();
        this.clearDrawLayer();
        this.clearAreaLayer();
        this.clearResultLayer();
        this.clearSearchLayer();
        this.clearLookLayer();
        this.clearRoadLayer();
        this.clearMarkerLayer();
        this.clearTempMarkerLayer();
    };
    
    /**
     *  展示某个图层可见，其它图层可见设置为false
     *  @param name {String} 图层名称
     */
    this.showLayer = function(name) {
    	this.visiableAreaLayer(false);
    	this.visiableDrawLayer(false);
    	this.visiableMarkerLayer(false);
    	this.visiableResultLayer(false);
    	this.visiableSearchLayer(false);
    	this.visiableLookLayer(false);
    	this.visiableShopLayer(false);
    	this.visiableTempMarkerLayer(false);
        this[name].setVisibility(true);
    };
    
    
	
	/**
     *  清除测量图层上的图形 
     */
    this.clearMeasureLayer=function(){
    	this.measureLayer.removeAllFeatures();
    }
    
    
    /**
     *  清除绘画图层上的图形 
     */
    this.clearDrawLayer = function() {
        this.drawLayer.removeAllFeatures();
    };
    
    /**
     *  清除绘画图层上指定图形 
     */
    this.removeDrawLayer = function(features) {
        this.drawLayer.removeFeatures(features);
    };
    
    /**
     * 设置绘画图层是否可见
     */
    this.visiableDrawLayer=function(value){
    	this.drawLayer.setVisibility(value);
    }
    
    
    /**
     *  清除定位图层上的图形 
     */
    this.clearAreaLayer = function() {
        this.areaLayer.removeAllFeatures();
    };
    
    /**
     *  清除定位图层上指定图形 
     */
    this.removeAreaLayer = function(features) {
        this.areaLayer.removeFeatures(features);
    };
    
    /**
     * 设置定位图层是否可见
     */
    this.visiableAreaLayer=function(value){
    	this.areaLayer.setVisibility(value);
    }
    
    
    /**
     *  清除查询图层上的图形 
     */
    this.clearSearchLayer = function() {
        this.searchLayer.removeAllFeatures();
    };
    
    /**
     *  清除查询图层上指定图形 
     */
    this.removeSearchLayer = function(features) {
        this.searchLayer.removeFeatures(features);
    };
    
    /**
     * 设置查询图层是否可见
     */
    this.visiableSearchLayer=function(value){
    	this.searchLayer.setVisibility(value);
    }
    
    /**
     *  清除查看图层上的图形 
     */
    this.clearLookLayer = function() {
        this.lookLayer.removeAllFeatures();
    };
    
    /**
     *  清除查看图层上指定图形 
     */
    this.removeLookLayer = function(features) {
        this.lookLayer.removeFeatures(features);
    };
    
    /**
     * 设置查看图层是否可见
     */
    this.visiableLookLayer=function(value){
    	this.lookLayer.setVisibility(value);
    }
    
    
    /**
     *  清除购物车图层上的图形 
     */
    this.clearShopLayer = function() {
        this.shopLayer.removeAllFeatures();
    };
    
    /**
     *  清除购物车图层上指定图形 
     */
    this.removeShopLayer = function(features) {
        this.shopLayer.removeFeatures(features);
    };
    
    /**
     * 设置购物车图层是否可见
     */
    this.visiableShopLayer=function(value){
    	this.shopLayer.setVisibility(value);
    }
    
    
    /**
     *  清除结果图层上的图形 
     */
    this.clearResultLayer = function() {
        this.resultLayer.removeAllFeatures();
    };
    
    /**
     *  清除结果图层上指定图形 
     */
    this.removeResultLayer = function(features) {
        this.resultLayer.removeFeatures(features);
    };
    
    /**
     * 设置结果图层是否可见
     */
    this.visiableRoadLayer=function(value){
    	this.roadLayer.setVisibility(value);
    }
    
    /**
     *  清除道路图层上的图形 
     */
    this.clearRoadLayer = function() {
        this.roadLayer.removeAllFeatures();
    };
    
    /**
     *  清除道路图层上指定图形 
     */
    this.removeRoadLayer = function(features) {
        this.roadLayer.removeFeatures(features);
    };
    
    /**
     * 设置道路图层是否可见
     */
    this.visiableResultLayer=function(value){
    	this.resultLayer.setVisibility(value);
    }
    
    
    /**
     *  清除临时点图层上的图形 
     */
    this.clearTempMarkerLayer = function() {
        for (var i = 0; i < tempMarkers.length; i++) {
            this.tempMarkerLayer.removeMarker(tempMarkers[i]);
        }
        tempMarkers = [];
    };
    
    /**
     *  清除临时点图层上指定图形 
     */
    this.removeTempMarkerLayer = function(marker) {
        this.tempMarkerLayer.removeMarker(marker);
    };
    
    /**
     * 设置临时点图层是否可见
     */
    this.visiableTempMarkerLayer=function(value){
    	this.tempMarkerLayer.setVisibility(value);
    }
    
    /**
     *  清除购物车Marker图层上指定图形 
     */
    this.removeShopMarkerLayer = function(marker) {
        this.shopMarkerLayer.removeMarker(marker);
    };
    
    /**
     *  清除购物车Marker图层上的图形 
     */
    this.clearShopMarkerLayer = function() {
        for (var i = 0; i < shopMarkers.length; i++) {
            this.shopMarkerLayer.removeMarker(shopMarkers[i]);
        }
        shopMarkers = [];
    };
    
    /**
     * 设置购物车Marker图层是否可见
     */
    this.visiableShopMarkerLayer=function(value){
    	this.shopMarkerLayer.setVisibility(value);
    }
    
    
    /**
     *  清除聚合点图层上的图形 
     */
    this.clearMarkerLayer = function() {
        for (var i = 0; i < markers.length; i++) {
            this.markerLayer.removeMarker(markers[i]);
        }
        markers = [];
    };
    
    /**
     *  清除聚合点图层上指定图形 
     */
    this.removeMarkerLayer = function(marker) {
        this.markerLayer.removeMarker(marker);
    };
    
    /**
     * 设置聚合点图层是否可见
     */
    this.visiableMarkerLayer=function(value){
    	this.markerLayer.setVisibility(value);
    }
    
//------------------------------------私有方法，获取相关样式及参数-------------------------------------------------------

    /**
     *  获取Style 
     */
    var _getStyleMap = function() {
        var styleConfig = {
        	pointRadius:6,
            strokeWidth : 1,
            strokeColor : "#4f88c7",
            strokeOpacity : 0.45,
            fillColor : "#4f88c7",
            fillOpacity : 0.45
        };
        
        var style = new OpenLayers.Style(styleConfig);
        var styleMap = new OpenLayers.StyleMap({
            "default" : style
        });
        return styleMap;
    };
    
    /***
     * 获取测量图层的样式
     */
    var _getMeasureStyleMap = function(){
         var sketchSymbolizers = {
            "Point": {
                pointRadius: 4,
                graphicName: "square",
                fillColor: "white",
                fillOpacity: 1,
                strokeWidth: 1,
                strokeOpacity: 1,
                strokeColor: "#333333"
            },
            "Line": {
                strokeWidth: 3,
                strokeOpacity: 1,
                strokeStyle: "dash",
                strokeColor: "#4f88c7",
                fillColor: "#a6cffc",
                fillOpacity: 0.5,
                strokeDashstyle: "dash"
            },
            "Polygon": {
                strokeWidth: 3,
                strokeOpacity: 1,
                strokeStyle: "dash",
                strokeColor: "#4f88c7",
                fillColor: "#a6cffc",
                fillOpacity: 0.5,
                strokeDashstyle: "dash"
            }
        };
        var style = new OpenLayers.Style();
        style.addRules([
            new OpenLayers.Rule({symbolizer: sketchSymbolizers})
        ]);
        var styleMap = new OpenLayers.StyleMap({"default": style});
        
        return styleMap;
    };
    
	/***
     *  获取底图的定义参数
     */
    var _getBaseLayDef = function(mapType) {
        var def = {
            mapType : mapType,
            isBaseLayer : true,
            topLevel : 2,
            bottomLevel : 18,
            transitionEffect:"map-resize",
            wrapDateLine : true
        };
        if (mapType == "Terrain") {
            def.bottomLevel = 14;
        }
        return def;
    };
    
    /**
     *  生成控件定义 
     */
    var _getCtrlDef = function(id) {
        var def = {
            id : id,
            persist : true,
            handlerOptions : {
                layerOptions : {
                    styleMap : _getStyleMap()
                }
            }
        };
        if (id == "rectCtrl") {
            def.handlerOptions.sides = 4;
            def.handlerOptions.irregular = true;
        }
        return def;
    };
    
//------------------------------------私有方法，测量监听事件------------------------------------------------------------
	
	/***
     *  测量面积事件 
     */
    var _areaMeasureEvt = function(e){
        var units = e.units;
        var measure = e.measure.toFixed(3);
        var num = parseFloat(measure).toFixed(3);
        
        var text = num + units;
        if(units == "m"){
            text = num + "平方米";
        }else if(units == "km"){
            text = num + "平方千米";
        }        
         var vector = new OpenLayers.Feature.Vector(e.geometry,null,{
            strokeWidth: 3,
            strokeOpacity: 1,
            strokeDashstyle: "dash",
            strokeColor: "#4f88c7",
            fillColor: "#a6cffc",
            fillOpacity: 0.5,
            label : text,
            fontColor : "#df3d3e",
            fontSize : "16px",
            fontFamily : "microsoft yahei",
            labelAlign : 'c'
        });
        
        e.object.drawLayer.addFeatures([vector]);
        
        e.object.deactivate();
    };
    
    
    /***
     *  测量长度事件 
     */
    var _lineMeasurEvt = function(e){
        var units = e.units;
        var measure = e.measure.toFixed(3);
        var num = parseFloat(measure).toFixed(3);
        var text;
        if(units == "m"){
            text = num + "米";
        }else if(units == "km"){
            text = num + "千米";
        }
        
        var vector = new OpenLayers.Feature.Vector(e.geometry,null,{
            strokeWidth: 3,
            strokeOpacity: 1,
            strokeDashstyle: "dash",
            strokeColor: "#4f88c7",
            fillColor: "#a6cffc",
            fillOpacity: 0.5,
            label : text,
            fontColor : "#df3d3e",
            fontSize : "16px",
            fontFamily : "microsoft yahei",
            labelAlign : 'c'
        });
        
        e.object.drawLayer.addFeatures([vector]);
        
        e.object.deactivate();
    };
    
//------------------------------------私有方法，初始化图层及控制器-------------------------------------------------------
	
    /**
     *  初始化控制器。其中控制器的事件由外部传入
     */
    var _initControllers = function(def) {
        var controls = {}, ctrls = [];

        var lineCtrl = new OpenLayers.Control.DrawFeature(this.drawLayer, OpenLayers.Handler.Path, _getCtrlDef("lineCtrl"));
        controls["lineCtrl"] = lineCtrl;
        ctrls.push(lineCtrl);

        var rectCtrl = new OpenLayers.Control.DrawFeature(this.drawLayer, OpenLayers.Handler.RegularPolygon, _getCtrlDef("rectCtrl"));
        controls["rectCtrl"] = rectCtrl;
        ctrls.push(rectCtrl);

        var polygonCtrl = new OpenLayers.Control.DrawFeature(this.drawLayer, OpenLayers.Handler.Polygon, _getCtrlDef("polygonCtrl"));
        controls["polygonCtrl"] = polygonCtrl;
        ctrls.push(polygonCtrl);
        
        var lineMeasureCtrl = new OpenLayers.Control.Measure(OpenLayers.Handler.Path, {
            persist: true,
            eventListeners: {
               'measure':_lineMeasurEvt
            },
            handlerOptions: {
                layerOptions: {
                    styleMap: _getMeasureStyleMap()
                }
            }
       });
       lineMeasureCtrl.drawLayer = this.measureLayer;
       controls["lineMeasureCtrl"] = lineMeasureCtrl;
       ctrls.push(lineMeasureCtrl);
       
       var areaMeasureCtrl = new OpenLayers.Control.Measure(OpenLayers.Handler.Polygon, {
            persist: true,
            eventListeners: {
                 'measure':_areaMeasureEvt
            },
            handlerOptions: {
                layerOptions: {
                    styleMap: _getMeasureStyleMap()
                }
            }
        });
        areaMeasureCtrl.drawLayer = this.measureLayer;
        controls["areaMeasureCtrl"] = areaMeasureCtrl;
        ctrls.push(areaMeasureCtrl);
        
        this.map.addControls(ctrls);
        return controls;
    };
	
	/***
     *  初试化基础底图
     */
    var _initBaseLayers = function() {
        var map = this.map;
        var layers = [];
        //添加天地图
        
        var def = _getBaseLayDef("EMap");
        var tdtlayer = new OpenLayers.Layer.TiandituLayer("vector", "http://t0.tianditu.com/DataServer", def);
        layers.push(tdtlayer);
        
        var tdtanno = new OpenLayers.Layer.TiandituLayer("vector_anno", "http://t0.tianditu.com/DataServer", {
            mapType : "CMapANNO",
            transitionEffect:"map-resize",
            wrapDateLine : false
        });
        layers.push(tdtanno);
        
        var tdtImglayer = new OpenLayers.Layer.TiandituLayer("img", "http://t0.tianditu.com/DataServer", {
            mapType : "Img",
            visibility : false,
            transitionEffect:"map-resize",
            wrapDateLine : true
        });
        layers.push(tdtImglayer);

        var tdtimganno = new OpenLayers.Layer.TiandituLayer("img_anno", "http://t0.tianditu.com/DataServer", {
            mapType : "ImgANNO",
            visibility : false,
            transitionEffect:"map-resize",
            wrapDateLine : true
        });
        layers.push(tdtimganno);

        var tdtTerrain = new OpenLayers.Layer.TiandituLayer("terrain", "http://t0.tianditu.com/DataServer", {
            mapType : "Terrain",
            visibility : false,
            transitionEffect:"map-resize",
            wrapDateLine : true
        });
        layers.push(tdtTerrain);
        
        var tdtTerrainAnno = new OpenLayers.Layer.TiandituLayer("terrain_anno", "http://t0.tianditu.com/DataServer", {
            mapType : "TerrainANNO",
            visibility : false,
            transitionEffect:"map-resize",
            wrapDateLine : true
        });
        layers.push(tdtTerrainAnno);
  
        return layers;
    };
	
	/***
     * 初试化图层
     * @param {Object} def
     */
    var _initLayers = function(def) {
        var measureLabelrenderer;
        measureLabelrenderer = OpenLayers.Util.getParameters(window.location.href).measureLabelrenderer;
        measureLabelrenderer = (measureLabelrenderer) ? [measureLabelrenderer] : OpenLayers.Layer.Vector.prototype.renderers;

        var layers   = _initBaseLayers.call(this);
        var styleMap = _getStyleMap.call(this);
        
        //测量图层，用于显示测量图形
        var measureLayer = this.measureLayer = new OpenLayers.Layer.Vector("measureLayer", {
            styleMap : _getMeasureStyleMap(),
            renderers : measureLabelrenderer
        });
        layers.push(measureLayer);
        
        //绘画图层，用于显示绘制图形
        var drawLayer = this.drawLayer = new OpenLayers.Layer.Vector("drawLayer", {
            styleMap : styleMap,
            renderers : measureLabelrenderer
        });
        layers.push(drawLayer);
        
        //定位图层，用于显示区域定位图形
        var areaLayer = this.areaLayer = new OpenLayers.Layer.Vector("areaLayer");
        layers.push(areaLayer);
        
        //查询图层，用于显示查询范围图形
        var searchLayer = this.searchLayer = new OpenLayers.Layer.Vector("searchLayer");
        layers.push(searchLayer);
        
        //查看图层，用于显示查看范围图形
        var lookLayer = this.lookLayer = new OpenLayers.Layer.Vector("lookLayer");
        layers.push(lookLayer);

        //结果图层，用于显示查询结果图形
        var resultLayer = this.resultLayer = new OpenLayers.Layer.Vector("resultLayer");
        layers.push(resultLayer);
        
        //道路河流图层，用于显示查询道路河流
        var roadLayer = this.roadLayer = new OpenLayers.Layer.Vector("roadLayer");
        layers.push(roadLayer);
        
        //购物车Vector对比图层
        var shopLayer = this.shopLayer = new OpenLayers.Layer.Vector("shopLayer");
        layers.push(shopLayer);
       
        //购物车Marker对比图层
        var shopMarkerLayer = this.shopMarkerLayer = new OpenLayers.Layer.Markers("shopMarkerLayer");
        layers.push(shopMarkerLayer);
        
        //聚合点图层，用于显示聚合点结果
        var markerLayer = this.markerLayer = new OpenLayers.Layer.Markers("markerLayer");
        layers.push(markerLayer);
        
        //临时点图层，用于显示临时点
        var tempMarkerLayer = this.tempMarkerLayer = new OpenLayers.Layer.Markers("tempMarkerLayer");
        layers.push(tempMarkerLayer);

        this.map.addLayers(layers);
    };
	
	/**
	 * 初始化方法（特权方法）
	 */
	this.init=function(def){
		
		this.father=def.father;
		/**
		 * 加载地图鼠标位置控件
		 */
		var mp = this.mp = new mouse();
		var mousePosition = new OpenLayers.Control.MousePosition({formatOutput:function(lnglat){
        	mp.controldiv(lnglat);
        }});

		//地图对象
		this.map = new OpenLayers.Map(def.mapName, {
            controls : [
                new OpenLayers.Control.Navigation(),
                new OpenLayers.Control.SCGISPanZoomBar(),
                new OpenLayers.Control.ScaleLine(),
                mousePosition,
            ]
		});
		//清除undefined
		this.map.removeControl(mousePosition);
		//初始化相关图层
		_initLayers.call(this, def);
		//初始化控制器
        this.controls = _initControllers.call(this, def);
        //初始化地图工具类对象
        this.mapUtils=MapUtils.getInstance(this.map);
        //初始化地图全图
        this.mapUtils.fullExtent(null,true);
	};
	
	//构造函数 可带参 调用初始化方法
	this.init.apply(this, arguments);
}
