
/***
 * @Description 地图工具类 （单例模式，MapUtils.getInstance(map) 需传入地图对象）
 * @Author YuanJianHua
 * @CreateTime 2015年12月15日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark: 第一次初始化时需传入map对象，以后调用时可传入null
 */
var MapUtils=(function(){
	
	var _instance;
	
	/**
	 * 初始化方法
	 * @param {Object} map
	 */
	var _init=function(map){
		
		//-----------------私有属性、方法--------------------------
		
		//单位
        var _UNITS = ['km', 'm'];
        //地图对象
        var _map=map;
        
		//-----------------共有属性、方法--------------------------
		
		return{
			
			/**
			 * 获取一个图形的面积，能自动判断单位
			 * @param  geometry  OpenLayers2的图形对象
			 * @return object{
		     *     result: 面积大小
		     *     units:  单位
		     *  }
			 */
			getBestArea:function(geometry){
				var unit, area;
			    for (var i = 0, len = _UNITS.length; i < len; ++i) {
			        var units = _UNITS[i];
			        area = this.getArea(geometry, units);
			        if (area > 1) {
			            break;
			        }
			    }
			    return {
			        result : area,
			        units : units + 2
			    };
			},
			
			/***
			 *  获取一个图形的面积，需要指定单位
			 *  @param geometry: OpenLayers2的图形对象
			 *  @param units: 结果的单位("km"或"m")
			 *  @return object{
			 *     result: 面积大小
			 *     units:  单位
			 *  }
			 */
			getArea:function(geometry, units) {
			    var area, geomUnits;
			    area = geometry.getArea();
			    geomUnits = _map.getUnits();
			
			    var inPerDisplayUnit = OpenLayers.INCHES_PER_UNIT[units];
			    if (inPerDisplayUnit) {
			        var inPerMapUnit = OpenLayers.INCHES_PER_UNIT[geomUnits];
			        area *= Math.pow((inPerMapUnit / inPerDisplayUnit), 2);
			    }
			    return area;
			},
			
			/**
		     *  根据wkt字符串获取一个Geometry
		     *  @param wkt: WKT字符串
		     *  @return <OpenLayers.Geometry>
		     */
		    getGeomFromWKT:function(wkt) {
		        var geom = OpenLayers.Geometry.fromWKT(wkt);
		        return geom;
		    },
		    
		    /**
		     *  获取Geometry的WKT字符串
		     *  @param geom：Geometry对象
		     *  @return WKT字符串
		     */
		    getWKTFromGeometry:function(geom){
		          return geom.toString();
		    },
		    
		    /**
		     *  根据wkt字符串获取一个Geometry的中心点
		     *  @param wkt: WKT字符串
		     *  @return <OpenLayers.Geometry>
		     */
		    getCenterFromWKT:function(wkt) {
		        var geom = OpenLayers.Geometry.fromWKT(wkt);
		        var point=geom.getCentroid();
		        return point.toString();
		    },
		
		    /**
		     * 根据wkt字符串获取一个Vector对象
		     * @param wkt: WKT字符串 
		     * @return <OpenLayers.Feature.Vector> 
		     */
		    getVectorFromWKT:function(wkt) {
		        var wkt_parser = new OpenLayers.Format.WKT();
		        var vector = wkt_parser.read(wkt);
		        return vector;
		    },
		    
		    /**
		     * 获取地图当前层级
		     */
		    getZoomLevel:function() {
		        return _map.getZoom();
		    },
		    
		    /**
		     * 获取地图的中心点
		     */
		    getMapCenter:function(){
		    	return _map.getCenter();
		    },
		    
		    /**
		     * 获取当前可视区域的bounds
		     */
		    getBounds:function(){
		    	 var bounds = _map.getExtent();
		    	 return bounds;
		    },
		    
		    /**
		     * 获取当前可视区域的wkt，可视范围要在-180到180，和-90到90之间
		     */
		    getViewport:function() {
		        var bounds = this.getBounds();
		        if (bounds.top > 90) {
		            bounds.top = 90;
		        }
		        if (bounds.bottom < -90) {
		            bounds.bottom = -90;
		        }
		        if (bounds.left < -180) {
		            bounds.left = -180;
		        }
		        if (bounds.right > 180) {
		            bounds.right = 180;
		        }
		        var geom = bounds.toGeometry();
		        return this.getWKTFromGeometry(geom);
		    },
		    
		    /**
		     * 设置地图最佳视野显示
		     * wkt wkt字符串
		     */
		    setViewByWKT:function(wkt){
		    	if(wkt!=null&&wkt!=""){
		    		var geom=this.getGeomFromWKT(wkt);
		        	var bound=geom.getBounds();
		        	_map.zoomToExtent(bound,false);
		    	}
		    },
		    
		    /**
		     * 设置地图最佳视野显示
		     * list:坐标点集合元素为经纬度，如：{'103.43 21.23','104.21 30.11'}
		     */
		    setViewByPointListLonLat:function(list){
		    	
		    	//坐标数组，设置最佳比例尺时会用到 
				var pointsArr = []; 
		    	for(var i=0;i<list.length;i++){
		    		var lnglatArr=list[i].split(" ");
		    		var lnglat = new OpenLayers.Geometry.Point(lnglatArr[0],lnglatArr[1]); 
					pointsArr.push(lnglat); 
		    	}
		    	var lineRing=new OpenLayers.Geometry.LinearRing(pointsArr);
		    	var poly=new OpenLayers.Geometry.Polygon(lineRing);
				var bound=poly.getBounds();
				_map.zoomToExtent(bound,false);
		    },
		    
		    /**
		     * 设置地图最佳视野显示
		     * list:坐标点集合元素为WKT字符串，如：{'POINT(103.23 21.34)','POINT(103.65 21.67)'}
		     */
		    setViewByPointListWKT:function(list){
		    	
		    	//坐标数组，设置最佳比例尺时会用到 
				var pointsArr = []; 
		    	for(var i=0;i<list.length;i++){
		    		var wkt=list[i];
		    		var geom=this.getGeomFromWKT(wkt);
		        	var point=geom.getCentroid();
		    		var lnglat = new OpenLayers.Geometry.Point(point.x,point.y); 
					pointsArr.push(lnglat); 
		    	}
		    	var lineRing=new OpenLayers.Geometry.LinearRing(pointsArr);
		    	var poly=new OpenLayers.Geometry.Polygon(lineRing);
				var bound=poly.getBounds();
				_map.zoomToExtent(bound,false);
		    },
		    
		    /**
		     * 设置地图层级
		     * zoom 层级
		     */
		    setMapZoom:function(zoom){
		    	_map.zoomTo(zoom);
		    },
		    
		    /**
		     * 设置地图的中心点
		     * lonlat:<OpenLayers.LonLat>
		     * zoom   :层级
		     */
		    setCenter:function(lonlat,zoom){
		    	_map.setCenter(lonlat,zoom);
		    },
		    
		    /**
		     * 设置地图中心点
		     * WKT 图形的WKT字符串
		     * 如果zoom为null，根据当前地图层级设置中心点
		     */
		    setCenterByWKT:function(wkt,zoom){
		    	
		    	if(wkt!=null&&wkt!=""){
		    		var geom=this.getGeomFromWKT(wkt);
		        	var point=geom.getCentroid();
		        	if(zoom==null){
		        		zoom=this.getZoomLevel();
		        	}
		        	_map.setCenter(new OpenLayers.LonLat(point.x, point.y),zoom);
		    	}
		    },
		    
		    /**
		     *  全图
		     *  @param extent {Array} 4个经纬度
		     *  @param firstExtent {Boolean} 是否为初始化，（初始化算法与普通的不一样）
		     */
		    fullExtent:function(extent,firstExtent) {
		    	if (!extent && typeof (extent) != "array") {
		            extent = [66.93828, 12.52102, 150.87383, 56.20266];
		        }
		        _map.zoomToExtent(extent);
		        
		        if(firstExtent){
		            var zoom = _map.getZoom();
		            _map.setCenter(new OpenLayers.LonLat(104.90703, 34.23),(2));
		            _map.updateSize();
		        }
		    },
		    
		    /**
		     * 地图居中刷新
		     */
		    refushMap:function(){
		    	 _map.setCenter(new OpenLayers.LonLat(104.90703, 34.23),2);
		    }
		   
		};
			
	};
	
	
	return{
		/**
		 * 获取地图工具类实例,需要传入OpenLayers2的map对象
		 * @param {Object} def
		 */
		getInstance:function(def){
			if(!_instance){
			  _instance=_init(def);
		    }
		    return _instance;
		}
	};
	
	
})();
