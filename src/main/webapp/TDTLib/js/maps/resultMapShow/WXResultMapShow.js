/**
 * @Description 多边形类产品查询结果地图展示类，包含：航空影像、卫星影像
 * @Author yuanJianHua
 * @CreateTime 2015年12月7日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var WXResultMapShow = function() {

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
     * 存放地图添加Vector对象
     */
    this.resultVectors = [];
    
    /**
     *存放点击相交的Vector对象 
     */
    this.touchVectors=[];
//--------------------------------------地图添加卫星影像类产品结果--------------------------------------    
    
    /**
     * 卫星影像类产品展示
     */
    this.wxTypeMapShow=function(result){
    	
    	this.ResultObj    =result;
    	//详情结果
    	var detailList     =result.itemList;
    	//行政区域聚合结果集
    	var groupRegionList=result.groupRegionList;
    	//查询区域
    	var searchGeom=result.searchGeom;
    	
    	if(searchGeom!=null&&searchGeom!=""&&searchGeom!="null"){
			this.mapUtils.setCenterByWKT(searchGeom,null);
		}
    	//初始化地图层级
    	this.betterZoom = this.mapUtils.getZoomLevel();
        //注册地图
    	this.registerMapZoomEvent(false);
        this.registerMapZoomEvent(true);
    	if(detailList.length>0){
    		this.registerMapZoomEvent(false);
    		this.addPolygon(detailList);
    	}else if(groupRegionList.length>0){
    		this.addGroupPoint(groupRegionList);
    	}else if(result.count>0){
    		var count     =result.count;
    		var searchGeom=result.searchGeom;
    		var wkt       =this.mapUtils.getCenterFromWKT(searchGeom);
    		var li        =[{name:'',count:count,wkt:wkt}];
    		result.groupRegionList=li;
    		this.addGroupPoint(li);
    	}
    }
    
    /**
     * 地图层级改变执行函数
     */
    this._mapZoomToWexFunction = function(){
    	//清除聚合点、结果图层
    	this.oplayerMap.clearMarkerLayer();
        this.oplayerMap.clearResultLayer();
        //取消地图层级点击事件
        this.cancelMapClickEvent();
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
    	//判断如果没有详情
    	if(detailList.length==0){
    		//根据聚合方式判断聚点方式
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
    			this.addPolygon(detailList);
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

    /***
     *  详情展示
     *  @param list {array} 结果数组
     */
    this.addPolygon = function(list) {
    	//注册地图点击事件
    	this.registerMapClickEvent(true);
    	if(this.resultVectors.length==0){
    		for (var i = 0; i < list.length; i++) {
                var item = list[i];
                var vector = this.oplayerMap.addPolygon(item.geom, {
                    id : item.id,
                    dType : "polygon",
                    cho:false
                }, {
                    strokeWidth : 1,
                    strokeOpacity : 1,
                    strokeColor : "#79c74f",
                    fillColor : "#FFFFFF",
                    fillOpacity : 0
                });
                this.resultVectors.push(vector);
            }
        	this.father.resultVectors=this.resultVectors;
    	}else{
    		this.oplayerMap.resultLayer.addFeatures(this.resultVectors, {
                silent : true
            });
    	}
    }
    
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
            		_regionMarkerClickFunction(marker);
            	}
                var $marker = $(marker.icon.imageDiv);
                var label_str = "<div class='bubble' style='width:" + width + "px' >" + count + "<div>";
                $marker.append($(label_str));
                var title = item.name + "共有" + item.count + "个结果";
                $marker.attr("title", title);
            }
        }
    };
    
//---------------------------------------注册地图事件------------------------------------------------

    /**
     * 注册地图层级改变事件
     */
    this.registerMapZoomEvent = function(flag) {
        this.map.events.unregister("zoomend",   this,this._mapZoomToWexFunction);
        if (flag) {
            this.map.events.register("zoomend", this,this._mapZoomToWexFunction);
        }
    };
    
    /***
     * 注册地图层级点击事件
     * @param flag {boolean}
     */
    this.registerMapClickEvent = function(flag) {
        this.map.events.unregister("click",   this, _wxMapClickFunction);
        if (flag) {
            this.map.events.register("click", this, _wxMapClickFunction);
        }
    };
    
    /**
     * 取消地图层级点击事件
     */
    this.cancelMapClickEvent =function(){
        
    	this.map.events.unregister("click", this, _wxMapClickFunction);
    }
    
    /***
     * 图形点击事件
     */
    var _wxMapClickFunction = function(e) {
        
    	if(this.touchVectors.length>0){
    		this.unClickWXGeom(this.touchVectors);
    		this.touchVectors=[];
    	}
    	
        //根据当前鼠标坐标获取点对象，循环图形对象，获得与此点相交的图形对象
        var px = e.xy;
        var lonlat = this.map.getLonLatFromPixel(px);
        var point = new OpenLayers.Geometry.Point(lonlat.lon, lonlat.lat);
        var vectors = this.resultVectors;
        //存放点击点相交图形信息
        var touchList  =[];
        var touchIDS   =[];
        for (var i = 0; i < vectors.length; i++) {
            var vector = vectors[i];
            var flag = vectors[i].geometry.intersects(point);
            if (flag) {
            	var id = vector.attributes.id;
            	this.touchVectors.push(vector);
            	var item=this.getDetail(id);
            	if(item!=null){
            		touchList.push(item);
            	}
            }
        }
        this.clickWXGeom(this.touchVectors);
        if(touchList!=null&&touchList.length>0){
        	if(!window.mapPage.listManager.detailOnOff){
        		window.mapPage.listManager.WXTypeListShowFunction(window.mapPage.listManager.ResultData);
        	}
        	window.mapPage.detailManager.content(touchList,window.mapPage.attrManager.resType);
        }
    }
    
    /**
     * 取消点击，图形刷新
     */
    this.unClickWXGeom = function(list){
    	
    	for(var i=0;i<list.length;i++){
    		var vector=list[i];
    		vector.style.strokeWidth=1;
    		vector.style.strokeOpacity=1;
    		vector.style.strokeColor="#79c74f";
    		if(vector.layer!=null){
    			vector.layer.drawFeature(vector);
    		}
    	}
    }
    
    /**
     * 点击，图形刷新
     */
    this.clickWXGeom = function(list){
    	
    	for(var i=0;i<list.length;i++){
    		var vector=list[i];
    		vector.style.strokeWidth=2;
    		vector.style.strokeOpacity=1;
    		vector.style.strokeColor="#FF0000";
    		vector.layer.drawFeature(vector);
    	}
    }
    
    /**
     * 根据ID获取详情
     */
    this.getDetail = function(id){
    	var list=this.ResultObj.itemList;
    	var item=null;
    	if(list!=null&&list.length>0){
    		for(var i=0;i<list.length;i++){
    			var temp=list[i];
    			var tempID=temp.id;
    			if(id==tempID){
    				item=temp;
    				break;
    			}
    		}
    	}
    	return item;
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

    
//---------------------------------------初始化方法--------------------------------------------------

    this.init = function(def) {
        
        this.father = def.father;
        this.oplayerMap = def.oplayerMap;
        this.mapUtils   = def.mapUtils;
        this.map        = def.map;  
    };
    
    this.init.apply(this, arguments);
};