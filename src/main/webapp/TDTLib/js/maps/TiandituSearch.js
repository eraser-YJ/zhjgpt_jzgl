/**
 * 天地图查询工具
 */
var TiandituSearch=function(){

//----------------------------------相关属性----------------------------------------------------------------
	 
	 //天地图查询配置参数
	 this.config = { 
		pageCapacity:6,	                    //每页显示的数量 
		onSearchComplete:localSearchResult	//接收数据的回调函数 
	 }; 
	 
	 /**
	  * 天地图查询对象
	  */
	this.TDTSearch=null;
	
	/**
     * 地图工具对象
     */
    this.mapUtils=null;
	 
	 /**
	  * 天地图Map对象
	  */
	var tianDiTuMap=null;
	 
	 /**
     * 地图封装对象
     */
    var oplayerMap=null;
    
    /**
     * 列表管理类对象
     */
    var listManager=null;
    
    /**
     * 地图管理类对象
     */
    var mapManager =null;
    
	 /**
	  * 当前查询关键字
	  */
	 var curKey="";
	 
//----------------------------------相关方法----------------------------------------------------------------

	 /**
	  * 获取天地图查询对象
	  */
	 this.getLocalSearch=function(){
		var level=this.mapUtils.getZoomLevel();
		var center=this.mapUtils.getMapCenter();
		var lon=center.lon;
		var lat=center.lat;
		tianDiTuMap.centerAndZoom(new TLngLat(lon,lat),(level+2)); 
		var localSearch=new TLocalSearch(tianDiTuMap,this.config); 
		this.TDTSearch=localSearch;
		return localSearch;
	}
	 
	 /**
	  * 天地图基础查询
	  * key：关键字
	  */
	 this.tianDiTuSearch=function(key){
		 
		 curKey=key;
    	 var localsearch=this.getLocalSearch();
    	 listManager.showStatList("loadingBox");
		 localsearch.search(key,1);
	 };
	 
	 /**
	  * 天地图基础查询
	  * id：specifyAdminCode区域ID
	  */
	 this.tianDiTuCitySearch=function(id){
	 	
		 var localsearch=this.getLocalSearch();
    	 localsearch.specifyAdminCode=id;
		 localsearch.search(curKey,1); 
	 }
	 
	 
	/**
	 * 天地图查询回调函数
	 * @param {Object} result
	 */
	 function localSearchResult(result){
        
		 //清空地图相关图层
		 oplayerMap.clearLayers();
		 //取消地图相关事件
		 mapManager.cancelMapZoomEvent();
    	 mapManager.cancelMapClickEvent();
    	 mapManager.cancelMapMovedEvent();
		 //设置mark图层可见
		 oplayerMap.visiableMarkerLayer(true);

		//根据返回类型解析搜索结果 
		switch(parseInt(result.getResultType())) 
		{ 
			case 1: 
				//解析点数据结果 
				_pois(result.getPois()); 
				break; 
			case 2: 
				//解析推荐城市 
				_statistics(result.getStatistics()); 
				break; 
			case 3: 
				//解析行政区划边界 
				_area(result.getArea()); 
				break; 
			case 4: 
				//解析建议词信息 
				suggests(result.getSuggests()); 
				break; 
			case 5: 
				//解析公交信息 
				lineData(result.getLineData()); 
				break; 
		} 
	};
	
	/**
	 * 解析点数据结果 
	 * @param {Object} obj
	 */
	var _pois=function(obj){ 
		
		if(obj) { 
			
			listManager.addTianDiTuPointList(obj);
		} 
	};
	
	/**
	 * 解析推荐城市 
	 * @param {Object} obj
	 */
	var _statistics=function(obj){ 
		
		if(obj){   
			//推荐城市
			var priorityCitys = obj.priorityCitys;
			//所有城市
			var allAdmins = obj.allAdmins; 
			var data=[];
			if(allAdmins) 
			{ 
				for(var i=0;i<allAdmins.length;i++) 
				{ 
					var childAdmins = allAdmins[i].childAdmins; 
					if(childAdmins) 
					{ 
						for(var j=0;j<childAdmins.length;j++) {
							var obj=childAdmins[j];
							data.push(obj);
						}
					} 
				} 
				listManager.addTianDiTuCityList(priorityCitys,data);
			} 
		} 
	};
	
	/**
	 * 解析行政区划边界 
	 * @param {Object} obj
	 */
	var _area=function (obj){ 
		if(obj) { 
			//坐标数组，设置最佳比例尺时会用到 
			var pointsArr = []; 
			var points = obj.points; 
			var level=obj.level;
			var lonlat=obj.lonlat;
			var lonlatArr=lonlat.split(",");
			var center=new OpenLayers.LonLat(lonlatArr[0],lonlatArr[1]);
			var lineRings=[];
			for(var i=0;i<points.length;i++) 
			{ 
				var regionLngLats = [];
				var regionArr = points[i].region.split(","); 
				for(var m=0;m<regionArr.length;m++) 
				{ 
					var lnglatArr = regionArr[m].split(" "); 
					var lnglat = new OpenLayers.Geometry.Point(lnglatArr[0],lnglatArr[1]); 
					regionLngLats.push(lnglat); 
					pointsArr.push(lnglat); 
				}
				//地图添加行政区域边界
				oplayerMap.addLineByList(regionLngLats,"tianDiTuLayer");
				var lineRing=new OpenLayers.Geometry.LinearRing(regionLngLats);
				lineRings.push(lineRing);
			}
			var poly=new OpenLayers.Geometry.Polygon(lineRings);
			var bound=poly.getBounds();

			//地图显示最佳比例尺 
			oplayerMap.getMap().zoomToExtent(bound,false);
			listManager.addTianDiTuAreaResult(obj);
		} 
	};
	
//----------------------------------初始化方法----------------------------------------------------------------	
	
	this.init = function(def) {
		 
		 this.father = def.father;
		 listManager = def.listManager;
		 mapManager  = def.mapManager;
		 oplayerMap  = mapManager.oplayerMap;
		 this.mapUtils=mapManager.mapUtils;
		 
		 //天地图地图对象，用于创建天地图查询TLocalSearch类
		 tianDiTuMap =new TMap("tempMap"); 
		 tianDiTuMap.centerAndZoom(new TLngLat(116.40969,39.89945),6); 
	 	 //允许鼠标滚轮缩放地图 
		 tianDiTuMap.enableHandleMouseScroll(); 
		 //允许双击地图放大 
		 tianDiTuMap.enableDoubleClickZoom(); 
	};
	
	 this.init.apply(this, arguments);
};