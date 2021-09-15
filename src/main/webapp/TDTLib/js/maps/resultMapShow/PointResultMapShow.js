/**
 * @Description 点类产品查询结果地图展示类，包含：重力点、水准点、三角点、GNSS成果
 * @Author yuanJianHua
 * @CreateTime 2015年12月7日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var PointResultMapShow = function() {

//--------------------------------------相关属性--------------------------------------
    /**
     * 地图封装对象
     */
    this.oplayerMap=null;
    
    /**
     * 地图工具对象
     */
    this.mapUtils=null;
    
    /**
     * 地图对象
     */
    this.map    =null;
    
    /**
     * 当前查询结果集
     */
    this.ResultObj=null;
    
    /**
     * 地图添加点 Marker集合
     */
    this.resultMarkers=[];
    
    /**
     * 记录点击Marker
     */
    var _clickMarker=null;
    
    
//--------------------------------------地图添加点类产品结果--------------------------------------    
    
    /**
     * 点类产品展示
     */
    this.pointTypeMapShow=function(result){
    	this.ResultObj    =result;
    	//详情结果
    	var detailList     =result.itemList;
    	//行政区域聚合结果集
    	var groupRegionList=result.groupRegionList;
    	//查询区域
    	var searchGeom=result.searchGeom;
    	//初始化地图层级
    	this.betterZoom = this.mapUtils.getZoomLevel();
    	if(searchGeom!=null&&searchGeom!=""&&searchGeom!="null"){
			this.mapUtils.setCenterByWKT(searchGeom,null);
		}
    	//先关闭
    	this.registerMapZoomEvent(false);
        //注册地图move 
        this.registerMapZoomEvent(true);

    	if(detailList.length>0){
    		this.addPoint(detailList);
    	}else if(groupRegionList.length>0){
    		this.addGroupPoint(groupRegionList);
    		tipsOnline();
    	}

    }
    
    //move
    this._mapZoomToPointFunction = function(){
    	//获取值
    	var obj = this.ResultObj;
    	//获取当前地图层级
    	var curZoom=this.mapUtils.getZoomLevel();
    	//当前比例尺图幅展示对应的地图层级
    	var zoomShow=0;
    	//聚合方式
    	var regionStyle = obj.curGroupType;
    	//详情结果
    	var detailList = obj.itemList;
    	//行政区域聚合结果集
    	var groupRegionList=obj.groupRegionList;
    	//清除聚合点、结果图层
    	this.oplayerMap.clearMarkerLayer();
        this.oplayerMap.clearResultLayer();
    	//判断如果没有详情
    	if(detailList.length==0){
    		//根据聚合方式判断聚点方式  20170619取消
//    		if(regionStyle){
//    			var temp = better(curZoom,this.betterZoom,groupRegionList,this.mapUtils,obj);
//    			if(temp){
//    				if(temp.length==1){
//    					temp[0]["count"]=obj.count;
//    				}
//    				groupRegionList = temp;
//    			}
//    		}
    		//添加
    		this.addGroupPoint(groupRegionList);
    		//tips
    		if(groupRegionList.length > 0 && groupRegionList[0].count!=0){
        		tipsOnline();
    		}
    			
    	}else{
    		//根据聚合方式判断聚点方式
    		if(curZoom >= this.betterZoom){	//显示详情
    			this.addPoint(detailList);
    		}else{
    			//20170619取消
//    			if(curZoom <= (this.betterZoom - 3)){	//独点聚合
//        			var temp = better(curZoom,this.betterZoom,groupRegionList,this.mapUtils,obj);
//        			if(temp){
//        				groupRegionList = temp;
//        			}
//        		}
    			this.addGroupPoint(groupRegionList);
    		} 
    	}
    }
    

    /**
     * 注册地图层级改变事件
     */
    this.registerMapZoomEvent = function(flag) {
        this.map.events.unregister("zoomend", this,this._mapZoomToPointFunction);
        if (flag) {
            this.map.events.register("zoomend", this,this._mapZoomToPointFunction);
        }
    };
    
    /***
     * 行政区域聚合点显示
     * @param list{Array} 聚合信息列表
     */
    this.addGroupPoint = function(list) {
        
        this.oplayerMap.visiableMarkerLayer(true);
        for (var i = 0; i < list.length; i++) {
            var item = list[i];
            var count = item.count = parseInt(item.count);
            var imgUrl, width;
            if (item.name == "总条数") {
                continue;
            }
            //不同数量大小，采用不同的背景
            if (count < 10) {
                width = 19;
                imgUrl = "./public/style/images/bubble1.png";
            } else if (count > 9 && count < 100) {
                width = 25;
                imgUrl = "./public/style/images/bubble2.png";
            } else if (count > 99 && count < 1000) {
                width = 30;
                imgUrl = "./public/style/images/bubble3.png";
            } else if (count > 999 && count < 10000) {
                width = 34;
                imgUrl = "./public/style/images/bubble4.png";
            } else if (count > 9999 && count < 100000) {
                width = 39;
                imgUrl = "./public/style/images/bubble5.png";
            } else if (count > 99999 && count < 1000000) {
                width = 44;
                imgUrl = "./public/style/images/bubble6.png";
            } else {
                width = 49;
                imgUrl = "./public/style/images/bubble7.png";
            }
            var point = this.mapUtils.getGeomFromWKT(item.wkt);
            var size = new OpenLayers.Size(width, 23);
            var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
            var icon = new OpenLayers.Icon(imgUrl, size, offset);
            
            if(point!=null||point!=undefined){
            	var marker = this.oplayerMap.addMarker(point.x, point.y, icon, {
                    name : item.name,
                    pac  : item.pac
                },"markerLayer");
            	//注册marker点击事件 20170619
            	if(this.ResultObj.curGroupType!="cntyName"){
            		
            	}
            	_regionMarkerClickFunction(marker);
                var $marker = $(marker.icon.imageDiv);
                var label_str = "<div class='bubble' style='width:" + width + "px' >" + count + "<div>";
                $marker.append($(label_str));
                var title = item.name + "共有" + item.count + "个结果";
                $marker.attr("title", title);
            }
        }
    };
    
    
    /***
     *  详情点展示
     *  @param list {array} 结果数组
     */
    this.addPoint = function(list) {

        if(this.resultMarkers.length==0){
        	var size = new OpenLayers.Size(14, 25);
            var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
        	for (var i = 0; i < list.length; i++) {
                var item = list[i];
                var wkt = item.point ? item.point : item.geom;
                var point = this.mapUtils.getGeomFromWKT(wkt);
                var icon = new OpenLayers.Icon(MapConstant.point_show_url, size, offset);
                if(point!=null||point!=undefined){
                	var marker = this.oplayerMap.addMarker(point.x, point.y, icon, {
                        id : item.id,
                        cho:false
                    });
                    marker.events.register('mousedown', this, _markerClick);
                    this.resultMarkers.push(marker);
                }
            }
        	this.father.resultVectors=this.resultMarkers;
        }else{
        	for(var j = 0; j < this.resultMarkers.length; j++){
        		var tmarker= this.resultMarkers[j];
        		this.oplayerMap.markerLayer.addMarker(tmarker);
        	}
        	this.oplayerMap.setMarkers(this.resultMarkers);
        }
    }
    
    /**
     * marker点击执行函数 20170619添加
     */
    var _regionMarkerClickFunction = function(marker){
    	
    	marker.events.register('click', marker, function(evt){
    		var thisPac = this.pac;
    		$("#scaleGroupList").find("#"+thisPac).click();
    		var top = $("#"+thisPac).offset().top;
    		var $top = $(".listBox_out").offset().top;
    		$(".listBox_out").scrollTop(top - $top);
    		//alert(this.name+",根据pac:"+this.pac+",模拟点击列表");
    		OpenLayers.Event.stop(evt);
        });
    }
    
    /**
     * 点类产品点击事件
     */
    var _markerClick = function(e) {
        
    	
    	var marker  = e.object;
        var id      = marker.id;
        if(_clickMarker!=null){
        	var url=_clickMarker.icon.url;
        	if(url!=MapConstant.point_select_url){
        		_clickMarker.setUrl(MapConstant.point_show_url);
    		}
        }
        _clickMarker= marker;
        //循环获取详情结果
    	var detailList=this.ResultObj.itemList;
    	var showList  =[];
    	if(detailList!=null&&detailList.length>0){
    		for(var i=0;i<detailList.length;i++){
    			var item=detailList[i];
    			var tempID=item.id;
    			if(id==tempID){
    				showList.push(item);
    				var url=marker.icon.url;
    				if(url!=MapConstant.point_select_url){
    					marker.setUrl(MapConstant.point_click_url);
    	    		}
    				break;
    			}
    		}
    	}
    	//弹出信息展示窗口
    	if(!window.mapPage.listManager.detailOnOff){
    		window.mapPage.listManager.PointTypeListShowFunction(window.mapPage.listManager.ResultData);
    	}
    	window.mapPage.detailManager.content(showList,window.mapPage.attrManager.resType);
    };
    
    /**
     * 取消点击
     */
    this.unClickMarker=function(){
    	if(_clickMarker!=null){
    		var url=_clickMarker.icon.url;
    		if(url!=MapConstant.point_select_url){
    			_clickMarker.setUrl(MapConstant.point_show_url);
    		}
    	}
    }


//---------------------------------------初始化方法--------------------------------------------------

    this.init = function(def) {
        
        this.father = def.father;
        this.oplayerMap = def.oplayerMap;
        this.mapUtils   = def.mapUtils;
        this.map        = def.map;
    };
    
    this.init.apply(this, arguments);
};