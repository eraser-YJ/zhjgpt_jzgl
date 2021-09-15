
/***
 *
 * @Description 地图管理类，提供地图查询条件的封装和查询结果的地图展示。
 * @Author yuanJianHua
 * @CreateTime 2015年12月7日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var MapManager = function() {
    
//-------------------------------------------相关属性-----------------------------------------------------------------
    
    //点类展示对象
    this.PointTypeObj=null;
    
    //图幅类展示对象
    this.THTypeObj=null;
    
    //卫星、影像类展示对象
    this.WXTypeObj=null;
    
    //其它类展示对象
    this.OtherTypeObj=null;
    
    //基础地图类对象
    this.oplayerMap=null;
    
    //地图对象
    this.map=null;
    
    //地图工具类对象
    this.mapUtils=null;
    
    /**
     * 存放地图图形对象
     */
    this.resultVectors = [];
    
    /**
     * 版本选择结果集
     */
    this.listVersion=null;
    
//-------------------------------------------清理子类结果集------------------------------------------------------------------
    
    this.cleanMapResult=function(){
    	
    	this.searchGeom=null;
    	this.resultVectors=[];
    	this.listVersion=null;
    	this.PointTypeObj.resultMarkers=[];
    	this.THTypeObj.resultVectors=[];
    	this.THTypeObj.touchVectors=[];
    	this.WXTypeObj.resultVectors=[];
    	this.WXTypeObj.touchVectors=[];
    }
    
//-------------------------------------------列表选择刷新Vector对象-----------------------------------------------------------    
    
    /**
     * 信息框关闭，修改地图图形颜色
     */
    this.unClickRefushGeom=function(){
    	var resType=this.father.attrManager.resType;
    	if(resType=="1"||resType=="2"||resType=="3"||resType=="4"){
    		this.PointTypeObj.unClickMarker(); 
    	}else if(resType=="5"||resType=="6"||resType=="7"||resType=="10"||resType=="11"){
    		this.THTypeObj.unClickTHGeom(this.THTypeObj.touchVectors); 
    	}else{
    		this.WXTypeObj.unClickWXGeom(this.WXTypeObj.touchVectors); 
    	}
    }

    /**
     * 右侧列表选择，地图面图形添加选择状态
     */
    this.selectVector=function(item,zoom){
    	
    	var wkt=item.geom;
    	this.mapUtils.setCenterByWKT(wkt,zoom);
    	var vector=this.oplayerMap.addPolygon(
         		wkt,null,{
			            strokeWidth : 2,
			            strokeOpacity : 1,
			            strokeColor : "#2A81FF",
			            fillColor : "#f57d28",
			            fillOpacity : 0
         });
    	return vector;
    }
    
    /**
     * 右侧列表图形类取消选择
     */
    this.unSelectVector=function(vector){
    	
    	this.oplayerMap.removeResultLayer(vector);
    }
    
    /**
     * 右侧列表面类单击，地图面图形居中显示
     */
    this.centerVector=function(item,zoom){
    	this.oplayerMap.clearAreaLayer();
    	var wkt=item.geom;
    	var vector=this.oplayerMap.addPolygon(
         		wkt,null,{
			            strokeWidth : 3,
			            strokeOpacity : 1,
			            strokeColor : "#FF0000",
			            fillColor : "#f57d28",
			            fillOpacity : 0.2
         },"areaLayer");
    	this.mapUtils.setCenterByWKT(wkt,zoom);
    }
    
//-------------------------点类选择----------------------------------------------    
    /**
     * 右侧列表点类选择 
     */
    this.selectMarker=function(item){
    	
    	var id    =item.id;
    	var wkt   =item.geom;
    	var list  =this.resultVectors;
    	var marker=null;
    	if(list!=null&&list.length>0){
    		for(var i=0;i<list.length;i++){
    			marker =list[i];
    			var tempID = marker.id;
    			if(tempID==id){
                    break;
    			}
    		}
    		if(marker!=null){
    			marker.setUrl(MapConstant.point_select_url);
    			this.mapUtils.setCenterByWKT(wkt,10);
    		}
    		return marker;
    	}
    }
    
    /**
     * 右侧列表点类取消选择
     */
    this.unSelectMarker=function(marker){
    	if(marker!=null){
    		marker.setUrl(MapConstant.point_show_url);
    	}
    }
    
    /**
     * 右侧列表点类单击
     */
    this.oldCenterMarker=null;
    this.centerMarker=function(item){
    	if(this.oldCenterMarker!=null){
    		this.oldCenterMarker.setUrl(MapConstant.point_show_url);
    	}
    	var id    =item.id;
    	var wkt=item.geom;
    	var list  =this.resultVectors;
    	var marker=null;
    	if(list!=null&&list.length>0){
    		for(var i=0;i<list.length;i++){
    			marker =list[i];
    			var tempID = marker.id;
    			if(tempID==id){
    				this.oldCenterMarker=marker;
                    break;
    			}
    		}
    		if(marker!=null){
    			this.mapUtils.setCenterByWKT(wkt,14);
    			marker.setUrl(MapConstant.point_click_url);
    		}
    	}
    }
    
//-------------------------------------------点类产品结果展示----------------------------------------------------------
    
    /**
     * 点类产品查询结果地图展示
     */
    this.PointTypeMapShowFunction=function(result){
    	
    	this.PointTypeObj.pointTypeMapShow(result);
    }
    
//-------------------------------------------图幅类产品结果展示--------------------------------------------------------
    
    /**
     * 图幅类产品查询结果地图展示
     */
    this.THTypeMapShowFunction=function(result){
    	
    	this.THTypeObj.thTypeMapShow(result);
    }
    
    /**
     * 工具栏图幅号查询地图展示
     */
    this.mapNumShowFunction=function(result){
    	
    	this.THTypeObj.mapNumShow(result);
    }
    
    
    
//-------------------------------------------卫星、影像类产品结果展示---------------------------------------------------
     
    /**
     * 卫星、影像类产品查询结果地图展示
     */
    this.WXTypeMapShowFunction=function(result){
    	
    	this.WXTypeObj.wxTypeMapShow(result);
    }

    
//-------------------------------------------其它类结果展示---------------------------------------------------
    
    /**
     * 其它类查询结果地图展示
     */
    this.OtherTypeMapShowFunction=function(result){
    	
    	this.OtherTypeObj.OtherTypeMapShow(result);
    }
    
    /**
     * 无结果显示
     */
    this.NoResultMapShowFunction=function(result){
    	
    	this.oplayerMap.clearSearchLayer();
    	var listGeom  =result.showGeomList;
    	if(listGeom!=null&&listGeom.length>0){
    		for(var i=0;i<listGeom.length;i++){
        		var geom=listGeom[i];
        		if(geom!=null){
        			this.oplayerMap.addPolygon(geom,null,{
                        strokeWidth : 2,
                        strokeOpacity : 1,
                        strokeColor : "#519dde",
                        fillColor : "#4f88c7",
                        fillOpacity : 0.5
                    },"searchLayer");
        		}
        	}
    	}
    }
    
//-------------------------------------------地图查询区域绘制---------------------------------------------------
    
    this.searchGeom=null;
    /**
     * 地图查询区域绘制（查询）
     */
    this.drawResultGeom=function(searchGeom){
    	if(this.searchGeom==null){
    		this.searchGeom=searchGeom;
    		this.oplayerMap.clearSearchLayer();
	    	if(searchGeom!=null&&searchGeom!="null"){
				this.oplayerMap.addPolygon(searchGeom,null,{
	                strokeWidth : 2,
	                strokeOpacity : 1,
	                strokeColor : "#EE0000",
	                fillColor : "#919191",
	                fillOpacity : 0
	            },"searchLayer");
				this.mapUtils.setViewByWKT(searchGeom);	
			}	
    	}else{
    		if(this.searchGeom!=searchGeom){
    			this.searchGeom=searchGeom;
    			this.oplayerMap.clearSearchLayer();
    	    	if(searchGeom!=null&&searchGeom!="null"){
    				this.oplayerMap.addPolygon(searchGeom,null,{
    	                strokeWidth : 2,
    	                strokeOpacity : 1,
    	                strokeColor : "#EE0000",
    	                fillColor : "#919191",
    	                fillOpacity : 0
    	            },"searchLayer");
    				this.mapUtils.setViewByWKT(searchGeom);	
    			}	
    		}
    	}
    }
    
    /**
     * 地图查询区域绘制（查看）
     */
    this.drawLookGeom=function(lookGeom){
    	if(this.searchGeom!=lookGeom){
    		this.oplayerMap.clearLookLayer();
        	if(lookGeom!=null&&lookGeom!="null"){
    			this.oplayerMap.addPolygon(lookGeom,null,{
                    strokeWidth : 1,
                    strokeOpacity : 1,
                    strokeColor : "#D3DFE5",
                    fillColor : "#919191",
                    fillOpacity : 0.5
                },"lookLayer");
    			this.mapUtils.setViewByWKT(lookGeom);	
    		}	
    	}
    }
    
//-------------------------------------------地图事件------------------------------------------------------------------
    
    /**
     * 取消地图MOVED事件
     */
    this.cancelMapMovedEvent =function(){
        
    	this.THTypeObj.cancelMapMovedEvent();
    }
    
    /**
     * 取消地图层级改变事件
     */
    this.cancelMapZoomEvent =function(){
        
    	this.THTypeObj.cancelMapZoomEvent();
    }
    
    /**
     * 取消地图相关CLICK事件
     */
    this.cancelMapClickEvent = function(){
    	
    	this.mapTools.cancelMapClickEvent();
    	this.PointTypeObj.registerMapZoomEvent(false);
    	this.THTypeObj.cancelMapClickEvent();
    	this.THTypeObj.registerMapZoomEvent(false);
    	this.WXTypeObj.cancelMapClickEvent();
    	this.WXTypeObj.registerMapZoomEvent(false);
    }
    
//-------------------------------------------类初始化------------------------------------------------------------------

    this.init = function(def) {
    
		this.father = def.father;
	    
	    //初始化基础地图类
	    this.oplayerMap = new OplayerMap({
	    	father : this,
	    	mapName: def.mapName
	    });
	    
	    //获取地图对象
	    this.map = this.oplayerMap.getMap();
	    
	    //初始化地图工具类
	    this.mapUtils=MapUtils.getInstance(this.map);
	    
	    //初始化地图工具条类
	    this.mapTools=new MapTools({
	    	father : this,
	    	oplayerMap:this.oplayerMap,
	    	map       :this.map
	    });
	    

	    //初始化点类展示对象
	    this.PointTypeObj=new PointResultMapShow({
	    	father : this,
	    	oplayerMap: this.oplayerMap,
	    	mapUtils:this.mapUtils,
	    	map     :this.map
	    });
	    
	    //初始化图幅类展示对象
	    this.THTypeObj=new THResultMapShow({
	    	father : this,
	    	oplayerMap: this.oplayerMap,
	    	mapUtils:this.mapUtils,
	    	map     :this.map
	    });
	    
	    //初始化卫星、影像类展示对象
	    this.WXTypeObj=new WXResultMapShow({
	    	father : this,
	    	oplayerMap: this.oplayerMap,
	    	mapUtils:this.mapUtils,
	    	map     :this.map
	    });
	    
	    //初始化其它类展示对象
	    this.OtherTypeObj=new OtherResultMapShow({
	    	father : this,
	    	oplayerMap: this.oplayerMap,
	    	mapUtils:this.mapUtils,
	    	map     :this.map
	    });
	    
	    //地图工具栏，行政区域弹出框
	    this.adminArea = new AdminArea({
	        father : this
	    });
	    

	    
    };

	this.init.apply(this, arguments);
};