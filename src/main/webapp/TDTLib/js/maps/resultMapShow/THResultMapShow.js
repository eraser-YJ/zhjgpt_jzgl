/**
 * @Description 图幅类产品查询结果地图展示类，包含：矢量地图数据、数字高程模型、分幅正射影像数据、数字栅格地图数据、模拟地形图
 * @Author yuanJianHua
 * @CreateTime 2015年12月7日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var THResultMapShow = function() {
  
//--------------------------------相关属性--------------------------------------
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
     * 4D产品图幅、聚合点显示地图层级改变事件开关
     */
    this.OnOff_Zoom_4DMap=false;
    
    /**
     * 4D产品图幅当前视野查询事件开关
     */
    this.OnOff_Move_4DMap=false;
    
    /**
     * 当前查询结果集
     */
    this.Result4DObj=null;
    
    /**
     * 地图添加图幅 Vector集合
     */
    this.resultVectors = [];
    
    /**
     *存放点击相交的Vector对象 
     */
    this.touchVectors=[];

//---------------------------------------地图添加图幅类产品结果--------------------------------------
    
    /**
     * 工具栏图幅号查询地图展示
     */
    this.mapNumShow=function(result){
        var list=[];
        var count=0;
        if(result!=null&&result.length>0){
        	list.push(result[0]);
        	count=result.length;
        }
        var zoom = this.mapUtils.getZoomLevel();
    	for (var i = 0; i < list.length; i++) {
            var item = list[i];
            var text = item.idNewMapNum + "\n\r" + "数量："+count;
                if (zoom == 1 || zoom == 0) {
                    var text = item.idNewMapNum;
                }
 
                var vector = this.oplayerMap.addPolygon(item.geom, {
                    mapNum : item.idNewMapNum,
                    id     : "",
                dType  : "mapNum",
                cho    : false
            }, {
                strokeWidth : 1,
                strokeOpacity : 0.5,
                strokeColor : "#FFFFFF",
                fillColor : "#f57d28",
                fillOpacity : 0.5,
                label : text,
                fontColor : "#FFFFFF",
                fontSize : "14px",
                fontFamily : "microsoft yahei",
                labelAlign : 'c'
            },"resultLayer");
            this.resultVectors.push(vector);
            
            var mapNum=item.idNewMapNum;
            if(mapNum!=null&&mapNum!=""){
            	var scale=SearchTools.getInstance().getScaleByTH(mapNum);
            	var zoom =SearchTools.getInstance().getMapZoomByScale(scale);
            	this.mapUtils.setCenterByWKT(item.geom,(zoom+2));
            }
            
            
        }
    }

    /**
     * 图幅类产品展示
     */
    this.thTypeMapShow=function(result){
    	this.Result4DObj    =result;
    	//图幅号聚合结果集
    	var mapNum4DResult  =result.groupTHMap;
    	//行政区域聚合结果集
    	var mapGroup4DResult=result.groupRegionList;
    	//查询区域
    	var searchGeom=result.searchGeom;
    	//初始化地图层级
    	this.betterZoom = this.mapUtils.getZoomLevel();
    	//取消地图相关事件
    	this.OnOff_Zoom_4DMap=false;
	    this.registerMapZoomEvent(false);
	    this.OnOff_Move_4DMap=false;
	    this.registerMapMovedEvent(false);
	    this.resultVectors = [];
	    this.registerMapZoomEvent(true);
    	if(_getPropertyCount(mapNum4DResult)>0){
    		//如果当前图幅号聚合结果有值（查询数据量小于等于阀值1000），地图居中显示图幅层级
    		 this.mapShow4DResultEvent(result);
    		 //注册地图zoom事件,聚合点、图幅切换显示
    		 this.OnOff_Zoom_4DMap=true;
    		
    	}else{
    		//地图添加聚合点
    		this.addGroupPoint(mapGroup4DResult);
    		//如果框选查询，注册地图MOVED事件，查询当前视野范围内数据
    		if(this.geomType=="normal"){
    			this.OnOff_Move_4DMap=true;
        		//this.registerMapMovedEvent(true);
    		}
    	}
    }
    
    /**
     * 图层锁定
     */
    this.lockShowLayerFunction = function(){
    	this.registerMapZoomEvent(false);
    	var mapNum4DResult=this.Result4DObj.groupTHMap;
        var result=null;
        if(mapNum4DResult){
        	for(var key in mapNum4DResult){
        		result=mapNum4DResult[key];
        	}
        }
        if(result){
        	//清除聚合点、结果图层
        	this.oplayerMap.clearMarkerLayer();
            this.oplayerMap.clearResultLayer();
            //取消地图层级点击事件
            this.cancelMapClickEvent();
        	this.addLockGroupMapNum(result);
        }
    }
    
    /**
     * 解除锁定
     */
    this.unLockShowLayerFunction = function(){
    	this.thTypeMapShow(this.Result4DObj);
    }
    
    //4D产品图幅+聚合点切换显示
    this.mapShow4DResultEvent = function(result){
    	//获取当前地图层级
    	var curZoom=this.mapUtils.getZoomLevel();
    	var baseLayer = this.map.baseLayer.layer;
		 if(baseLayer=="tbo"||baseLayer=="ibo"){
			 curZoom=curZoom-2;
		 }
    	//图幅号聚合结果集
    	var mapNum4DResult=result.groupTHMap;
    	//区域聚合结果集
    	var mapGroup4DResult=result.groupRegionList;
    	//当前比例尺
    	var curScale        =result.curScale;
    	//聚合方式
    	var regionStyle = result.curGroupType;
    	//当前比例尺图幅展示对应的地图层级
    	var zoomShow=0;
    	var cresult=null;
    	
    	for(var key in mapNum4DResult){
    		zoomShow=key;
    		cresult=mapNum4DResult[key];
    	}
    	//清除聚合点、结果图层
    	this.oplayerMap.clearMarkerLayer();
        this.oplayerMap.clearResultLayer();
        //取消地图层级点击事件
        this.cancelMapClickEvent();
		//根据聚合方式判断聚点方式 20170619取消
//		if(regionStyle){
//			var temp = better(curZoom,this.betterZoom,mapGroup4DResult,this.mapUtils,result);
//			if(temp){
//				if(temp.length==1){
//					temp[0]["count"]=result.count;
//				}
//				mapGroup4DResult = temp;
//			}
//				
//		}
    	if(cresult!=null&&cresult.length>0){
    	//图幅聚合结果不为空（数据量小于1000），判断图幅或聚合点显示
    		if(curZoom>=zoomShow){
    			//如果当前地图层级大于等于图幅展示层级，显示图幅
    			//如果版本结果集不为空，则根据版本过滤的结果显示图幅
    			if(this.father.listVersion!=null){
    				this.addGroupMapNumByVesrion(this.father.listVersion);
    			}else{
    				this.addGroupMapNum(cresult);
    			}
        		this.father.father.listManager.normalListObj.addSelcetVector();
    		}else{
    			
    			if("FHIJK".indexOf(curScale)!=-1){
    				if(curZoom >= (zoomShow-2)){
    					this.addPoint(cresult);
        			}else {
        				this.addGroupPoint(mapGroup4DResult);
        			}
				}else{
					
					this.addGroupPoint(mapGroup4DResult);
				}
    		}
    	}else{
    		//显示聚合点
    		this.addGroupPoint(mapGroup4DResult);
    		//tips
    		if(mapGroup4DResult.length>0 && mapGroup4DResult[0].count!=0){
        		tipsOnline();
    		}	
    	}
    }
    
    /**
     * 地图居中显示图幅展示层级
     */
    this.zoomTHShow=function(mapNum4DResult,searchGeom){
    	var zoomShow=0;
		for(var key in mapNum4DResult){
    		zoomShow=key;
    	}
		var zoom=(Number(zoomShow)+2);
		if(searchGeom!=null&&searchGeom!=""&&searchGeom!="null"){
			this.mapUtils.setCenterByWKT(searchGeom,zoom);
		}
    }
    
    /***
     *  展示点型数据
     *  @param list {array} 结果数组
     */
    this.addPoint = function(list) {
        var size = new OpenLayers.Size(14, 25);
        var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);

        for (var i = 0; i < list.length; i++) {

            var item = list[i];
            var wkt = item.point ? item.point : item.geom;
            var point = this.mapUtils.getGeomFromWKT(wkt);
            if (point.getCentroid != undefined) {
                point = point.getCentroid();
            }
            var icon = new OpenLayers.Icon('./public/style/images/marker1.png', size, offset);

            var marker = this.oplayerMap.addMarker(point.x, point.y, icon, {
                id : item.id
            });
        }
    };
    
    /**
     * 获取图幅显示名称 2016-07-06 新增
     */
    this.getMapNumText=function(name,count){
    	 var curScale = this.Result4DObj.curScale;
    	 var tmp = this.mapTextObj[curScale];
    	 if(tmp){//先通过curScale获取值，如果获取到，则分层级返回
    		 var zoom   = this.mapUtils.getZoomLevel();
    		 var baseLayer = this.map.baseLayer.layer;
    		 if(baseLayer=="tbo"||baseLayer=="ibo"){
    			 zoom=zoom-2;
    		 }
    		 var t1 = tmp[zoom];
    		 if(t1){
    			 return name.length <= t1 ? name + "\n\r" + "数量：" +count : "数量：" +count;
    		 }else{
    			 return name + "\n\r" + "数量：" +count;
    		 }
    	 }else{
    		 return name + "\n\r" + "数量：" +count;
    	 }
    };
    
    /**
     * 分比例尺显示图幅 getMapNumText使用
     */
    this.mapTextObj = {
		    		"G":{
		    			"9":8
		    		},
		    		"H":{
		    			"10":8
		    		},
		    		"I":{
		    			"11":8,
		    			"12":12
		    		},
		    		"J":{
		    			"12":8,
		    			"13":12,
		    			"14":16
		    		},
		    		"K":{
		    			"13":8,
		    			"14":12,
		    			"15":16
		    		}
		    	};
    
    
    /***
     *  图幅聚合显示
     *  @param list {Array}:
     */
    this.addGroupMapNum = function(list) {
    	//注册地图点击事件
    	this.registerMapClickEvent(true);
    	this.resultVectors=[];
    	for (var i = 0; i < list.length; i++) {
            var item   = list[i];
            var name   = item.name;
            var count  = item.count;
            var text   =this.getMapNumText(name,count);
            var vector = this.oplayerMap.addPolygon(item.geom, {
                mapNum : item.name,
                id     : item.id,
                dType  : "mapNum",
                cho    : false
            }, {
                strokeWidth : 1,
                strokeOpacity : 0.5,
                strokeColor : "#FFFFFF",
                fillColor : "#f57d28",
                fillOpacity : 0.5,
                label : text,
                fontColor : "#FFFFFF",
                fontSize : "14px",
                fontFamily : "microsoft yahei",
                labelAlign : 'c'
            },"resultLayer");
            this.resultVectors.push(vector);
        }
        this.father.resultVectors=this.resultVectors;
    };
    
    /***
     *  图层锁定图幅聚合显示
     *  @param list {Array}:
     */
    this.addLockGroupMapNum = function(list) {
    	//注册地图点击事件
    	this.registerMapClickEvent(true);
    	this.resultVectors=[];
    	for (var i = 0; i < list.length; i++) {
            var item   = list[i];
            var name   = item.name;
            var count  = item.count;
            var text   = "";
            var vector = this.oplayerMap.addPolygon(item.geom, {
                mapNum : item.name,
                id     : item.id,
                dType  : "mapNum",
                cho    : false
            }, {
                strokeWidth : 1,
                strokeOpacity : 0.5,
                strokeColor : "#FFFFFF",
                fillColor : "#f57d28",
                fillOpacity : 0.5,
                label : text,
                fontColor : "#FFFFFF",
                fontSize : "14px",
                fontFamily : "microsoft yahei",
                labelAlign : 'c'
            },"resultLayer");
            this.resultVectors.push(vector);
        }
        this.father.resultVectors=this.resultVectors;
    };
    
    /***
     *  根据版本图幅聚合显示(2016-03-07)
     *  @param list {Array}:
     */
    this.addGroupMapNumByVesrion = function(list) {
    	//注册地图点击事件
    	this.registerMapClickEvent(true);
    	this.resultVectors=[];
    	list=this.getCount(list);
        for (var i = 0; i < list.length; i++) {
            var item = list[i];
            var name   = item.idNewMapNum;
            var count  = item.count;
            var text   =this.getMapNumText(name,count);
	 
            var vector = this.oplayerMap.addPolygon(item.geom, {
                mapNum : item.idNewMapNum,
                id     : "",
                dType  : "mapNum",
                cho    : false
              }, {
	            strokeWidth : 1,
	            strokeOpacity : 0.5,
	            strokeColor : "#FFFFFF",
	            fillColor : "#f57d28",
	            fillOpacity : 0.5,
	            label : text,
	            fontColor : "#FFFFFF",
	            fontSize : "14px",
	            fontFamily : "microsoft yahei",
	            labelAlign : 'c'
             },"resultLayer");
	        this.resultVectors.push(vector);
	    }
        this.father.resultVectors=this.resultVectors;
    };
    
    /**
     * 根据图幅号计算个数
     */
    this.getCount=function(list){
    	
    	var obj=new Object();
    	for(var i=0;i<list.length;i++){
    		var item=list[i];
    		var name=item.idNewMapNum;
    		var value=obj[name];
    		if(value!=null){
    			value=value+1;
    			obj[name]=value;
    		}else{
    			obj[name]=1;
    		}
    	}
    	for(var j=0;j<list.length;j++){
    		var bean=list[j];
    		var tname=bean.idNewMapNum;
    		var count=obj[tname];
    		bean.count=count;
    	}
    	return list;
    }
    
    /***
     * 行政区域聚合点显示
     * @param list{Array} 聚合信息列表
     */
    this.addGroupPoint = function(list) {
        
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
            	if(this.Result4DObj.curGroupType!="cntyName"){
            		
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
    
    
    
     //获取Object长度
    var _getPropertyCount=function(obj){
       
       var n, count = 0;  
	   for(n in obj){  
	      if(obj.hasOwnProperty(n)){  
	         count++;  
	      }  
	   }  
       return count; 
    }
    
//---------------------------------------注册地图事件------------------------------------------------

    /**
     * 注册地图层级改变事件--4D产品图幅、聚合点切换显示
     */
    this.registerMapZoomEvent = function(flag) {
        this.map.events.unregister("zoomend", this,   _mapZoomTo4DFunction);
        if (flag) {
            this.map.events.register("zoomend", this, _mapZoomTo4DFunction);
        }
    };
    
    /**
     * 取消地图层级改变事件
     */
    this.cancelMapZoomEvent =function(){
        
    	this.map.events.unregister("zoomend", this,   _mapZoomTo4DFunction);
    }
    
    /**
     * 注册地图MOVED事件--4D产品当前视野查询
     */
    this.registerMapMovedEvent = function(flag){
    	
    	this.map.events.unregister("moveend", this,   _view4DSearchFunction);
        if (flag) {
            this.map.events.register("moveend", this, _view4DSearchFunction);
        }
    }
    
    /**
     * 取消地图MOVED事件
     */
    this.cancelMapMovedEvent =function(){
        
    	this.map.events.unregister("moveend", this,   _view4DSearchFunction);
    }
    
    
    /***
     * 注册地图层级点击事件
     * @param flag {boolean}
     */
    this.registerMapClickEvent = function(flag) {
        this.map.events.unregister("click",   this, _thMapClickFunction);
        if (flag) {
            this.map.events.register("click", this, _thMapClickFunction);
        }
    };
    
    /**
     * 取消地图层级点击事件
     */
    this.cancelMapClickEvent =function(){
        
    	this.map.events.unregister("click", this, _thMapClickFunction);
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
    
    /***
     * 图形点击事件
     */
    var _thMapClickFunction = function(e) {
        
    	if(this.touchVectors.length>0){
    		this.unClickTHGeom(this.touchVectors);
    		this.touchVectors=[];
    	}
    	
        //根据当前鼠标坐标获取点对象，循环图形对象，获得与此点相交的图形对象
        var px = e.xy;
        var lonlat  = this.map.getLonLatFromPixel(px);
        var point   = new OpenLayers.Geometry.Point(lonlat.lon, lonlat.lat);
        var vectors =this.resultVectors;
        //存放点击点相交图形信息 this.Result4DObj
        var touchList  =[];
        var touchIDS   =[];
        for (var i = 0; i < vectors.length; i++) {
            var vector = vectors[i];
            var flag = vectors[i].geometry.intersects(point);
            if (flag) {
            	this.touchVectors.push(vector);
            	var mapNum = vector.attributes.mapNum;
            	var id     = vector.attributes.id;
            	if(mapNum!=null&&mapNum!=""){
            		//大于5000比例尺，根据图幅号获取元素
            		var item=this.getDetail(mapNum);
                	if(item!=null&&item.length>0){
                		for(var j=0;j<item.length;j++){
                			var temp=item[j];
                			touchList=this.delSameItem(touchList,temp);
                		}
                	}
            	}else{
            		//小于5000比例尺，根据ID获取元素
            		var item=this.getDetailByID(id);
                	if(item!=null&&item.length>0){
                		for(var j=0;j<item.length;j++){
                			var temp=item[j];
                			touchList=this.delSameItem(touchList,temp);
                		}
                	}
            	}
            }
        }
        this.clickTHGeom(this.touchVectors);
        if(touchList!=null&&touchList.length>0){
        	if(!window.mapPage.listManager.detailOnOff){
        		window.mapPage.listManager.normalListObj.scale_A_onOff=true;
        		window.mapPage.listManager.THTypeListShowFunction(window.mapPage.listManager.ResultData);
        	}
        	 window.mapPage.detailManager.content(touchList,window.mapPage.attrManager.resType);
        }
    }
    
    /**
     * 去除重复元素
     */
    this.delSameItem = function(touchList,item){
    	var id=item.id;
    	var flag=true;
    	for(var i=0;i<touchList.length;i++){
    		var temp  =touchList[i];
    		var tempID=temp.id;
    		if(id==temp.id){
    			flag=false;
    		}
    	}
    	if(flag){
    		touchList.push(item);
    	}
    	return touchList;
    }
    
    /**
     * 取消点击，图形刷新
     */
    this.unClickTHGeom = function(list){
    	
    	for(var i=0;i<list.length;i++){
    		var vector=list[i];
    		vector.style.strokeWidth=1;
    		vector.style.strokeOpacity=0.5;
    		vector.style.strokeColor="#FFFFFF";
    		if(vector.layer!=null){
    			vector.layer.drawFeature(vector);
    		}
    	}
    }
    
    /**
     * 点击，图形刷新
     */
    this.clickTHGeom = function(list){
    	
    	for(var i=0;i<list.length;i++){
    		var vector=list[i];
    		vector.style.strokeWidth=2;
    		vector.style.strokeOpacity=1;
    		vector.style.strokeColor="#FF0000";
    		vector.layer.drawFeature(vector);
    	}
    }
    
    /**
     * 根据图幅号获取详情
     */
    this.getDetail = function(id){
       var list=[];
       if(this.father.listVersion!=null){
    	   list=this.father.listVersion;
       }else{
    	   list=this.Result4DObj.itemList;
       }
    	var item=[];
    	if(list!=null&&list.length>0){
    		for(var i=0;i<list.length;i++){
    			var temp=list[i];
				var idNewMapNum=temp.idNewMapNum;
				if(id==idNewMapNum){
					item.push(temp);
				}
			}
    	}
    	return item;
    }
    
    /**
     * 根据ID获取详情
     */
    this.getDetailByID = function(id){
    	var list=this.Result4DObj.itemList;
    	var item=[];
    	if(list!=null&&list.length>0){
    		for(var i=0;i<list.length;i++){
    			var temp=list[i];
				var tempID=temp.id;
				if(id==tempID){
					item.push(temp);
				}
			}
    	}
    	return item;
    }
    

    /**
     * 地图层级改变注册函数--4D产品图幅、聚合点切换显示
     */
    var _mapZoomTo4DFunction = function(e){
    	
    	this.mapShow4DResultEvent(this.Result4DObj);
    }
    
    /**
     * 地图视野变化注册函数--4D产品当前视野图+框选范围图幅查询显示
     */
    var _view4DSearchFunction = function(e){
    	
    	//获取当前地图层级
    	var curZoom=this.mapUtils.getZoomLevel();
    	//获取当前选择比例尺
    	var scale=this.father.listManager.scaleList.curScale;
    	//获取当前比例尺需显示地图层级
    	var showZoom=_getShowZoom(scale);
    	if(curZoom>=showZoom){
    		//地图当前视野查询
    		this.father.MapViewSearch();
    	}else{
    		this.mapShow4DResultEvent(this.Result4DObj);
    	}
    }

//---------------------------------------初始化方法--------------------------------------------------

    this.init = function(def) {
        this.father     = def.father;
        this.oplayerMap = def.oplayerMap;
        this.mapUtils   = def.mapUtils;
        this.map        = def.map;
    };
    
    this.init.apply(this, arguments);
};