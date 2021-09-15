/***
 * @Description 地图工具栏,该类提供地图工具栏相关操作方法
 * @Author YuanJianHua
 * @CreateTime 2015年12月15日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var MapTools=function(){
	
//------------------------------------私有属性-------------------------------------------------------------------------	
	/**
     * 地图对象
     */
    var map=null;
 
    /**
     * 基础地图类对象
     */
    var _oplayerMap=null;
    
//------------------------------------公有属性-------------------------------------------------------------------------
    
    /**
     * 图形类别  normal  polyline  
     */
    this.geomType = null;
    
    /**
     * 多折线参数
     */
    this.polyline = null;
    
    /**
     * 行政区域名称
     */
    this.regionName = null;
    
    /**
     * 绘制图形 记录矩形绘制、多边形绘制、线绘制的图形结果
     */
    this.drawGeom = null;
    
    /**
     * 存放图形穿刺图形
     */
    this.addVectors=[];
    
    
//------------------------------------私有方法--------------------------------------------------------

    /**
     * 设置鼠标样式
     */
    var _toggleMouse = function(mark){
    	if(mark) {
    		$('#map').css('cursor','crosshair');
    	} else {
    		$('#map').css('cursor','Default');
    	}
    };
    
    /**
     * 重置工具栏历史条件
     */
    var _restToolValue=function(){
    	
    	_oplayerMap.clearDrawLayer();
	   	this.drawGeom = null;
	   	this.geomType = null;
	   	this.polyline = null;
	   	
	   	SearchTools.getInstance().setDrawGeom(null);
	   	SearchTools.getInstance().setBuffer(0);
    };
    
    
//------------------------------------地图工具栏事件-------------------------------------------------------    
    
    /**
     *  矩形、多边形、多折线、测距、测面点击事件（激活工具栏控制器）
     *  @param e: 事件对象
     */
    this.toggleControlClickFn = function(e) {
    	var id=$(e).attr("id");
        //var id = e.target.id;
        if(id=='rectCtrl' || id=='polygonCtrl') {
       	 	_toggleMouse(true);
        } else {
       	 	_toggleMouse(false);
        }
        _oplayerMap.toggleControl(id);
        _restToolValue.call(this);
    };
    
    //锁定图层样式改变
    this.toggleLocklayer = function(flag){
    	var className = $("#locklayer").attr("class");
    	if(flag){//锁定
    		if(className.indexOf("locklayer-tools-lock") === -1){
    			$("#locklayer").addClass("locklayer-tools-lock")
    				.find(".tools-text").text("解除锁定");;
    		}
    	}else{//解除锁定
    		if(className.indexOf("locklayer-tools-lock") !== -1){
    			$("#locklayer").removeClass("locklayer-tools-lock")
    				.find(".tools-text").text("锁定图层");
    		}
    	}
    }
    
    //锁定图层事件点击
    this.locklayerValueClickFn = function(e){
    	var className = $("#locklayer").attr("class");
		if(className.indexOf("locklayer-tools-lock") === -1){//锁定
			$("#locklayer").addClass("locklayer-tools-lock")
				.find(".tools-text").text("解除锁定");
			var resType=SearchTools.getInstance().getResType();
			if(resType==5||resType==6||resType==7||resType==10||resType==11){
				window.mapPage.mapManager.THTypeObj.lockShowLayerFunction();
			}else if(resType==1||resType==2||resType==3||resType==4){
				
			}else if(resType==8||resType==9){
				
			}
		}else{//解除锁定
			$("#locklayer").removeClass("locklayer-tools-lock")
				.find(".tools-text").text("锁定图层");
			var resType=SearchTools.getInstance().getResType();
			if(resType==5||resType==6||resType==7||resType==10||resType==11){
				window.mapPage.mapManager.THTypeObj.unLockShowLayerFunction();
			}else if(resType==1||resType==2||resType==3||resType==4){
				
			}else if(resType==8||resType==9){
				
			}
		}
    }
    
    /***
     * 清除空间查询条件点击事件
     */
    this.clearMapValueClickFn = function(e) {
        
        //清除所有图层
        _oplayerMap.clearAllLayers();
        //购物车取消全选
        window.mapPage.mapCarManager.cancelChooseAll();
        window.mapPage.mapManager.oplayerMap.clearShopMarkerLayer();
        window.mapPage.mapManager.oplayerMap.clearShopLayer();
        
        //鼠标样式
        _toggleMouse(false);
        //穿刺查询设置不可用
        $('#checkRePeat').attr('used','no');
        
        this.drawGeom = null;
        this.geomType = null;
        this.polyline = null;
        this.regionName = null;
        
        SearchTools.getInstance().setDrawGeom(null);
        SearchTools.getInstance().setBuffer(0);
        SearchTools.getInstance().setRegionGeom(null);
        SearchTools.getInstance().setShpGeom(null);
        SearchTools.getInstance().setIsWithin(false);
        SearchTools.getInstance().setRegionName(null);
        SearchTools.getInstance().setTempKeyWord(null);
        SearchTools.getInstance().setKeyFilter(true);
       //取消地图相关事件
    	window.mapPage.mapManager.cancelMapMovedEvent();
    	window.mapPage.mapManager.cancelMapZoomEvent();
    	window.mapPage.mapManager.cancelMapClickEvent();
    };
    
    /**
     * 道路、水系、地址查询点击事件（弹出查询输入框）
     */
    this.popAreaDlgClickFn = function(e){
    	var thisTitle = $(e.currentTarget).attr("title");
    	var typeValue = $(e.currentTarget).attr("typeValue");
    	var areaSearchInput = $("#areaSearchDlg").find("input").eq(0);
    	$("#areaSearchDlg").find(".headerTitle").eq(0).text(thisTitle);
    	if(thisTitle == areaSearchInput.attr("placeholder")){
    		$("#areaSearchDlg").show();
    		//areaSearchInput.focus();
    	}else{
    		var placText=_getPlacText(typeValue);
    		areaSearchInput.attr("typeValue",typeValue);
    		areaSearchInput.val("");
    		areaSearchInput.attr("placeholder",placText);
    		$("#areaSearchDlg").show();
    		//areaSearchInput.focus();
    	}
    	$("#adminRegion,#mapNum,#latLong").hide();
    	window.mapPage.mapManager.mapTools.showPlaceholder();
        //取消地图相关事件
      window.mapPage.mapManager.cancelMapMovedEvent();
      window.mapPage.mapManager.cancelMapZoomEvent();
      window.mapPage.mapManager.cancelMapClickEvent();  
    };
    
    //经纬度查询点击事件
    this.latandlongClickFn = function(){
    	$("#latLong").show();
    	$("#areaSearchDlg,#adminRegion,#mapNum").hide();
    	RangeSearch.createLatLog('latLong');//RangeSearch在本文件中
    }
    //图幅号查询点击事件
    this.mapnumClickFn = function(){
    	$("#mapNum").show();
    	$("#areaSearchDlg,#adminRegion,#latLong").hide();
    	RangeSearch.createMapNum('mapNum');//RangeSearch在本文件中
    }
    
    
    /**
     * 道路、水系、地址查询点击后在输入框的提示字(仅IE)
     */
    this.showPlaceholder = function(){
		if(!this._check()){
			var $this = this;
			$("#areaSearchDlg").find("input").each(function(index, element) {
	            var self = $(this), txt = self.attr('placeholder');
	            if($this.placeholderCheck){
	            	$("#areaSearchDlg").find("#spanPlaceholder").eq(0).text(txt);
	            }else{
	            	self.wrap($('<div></div>').css({position:'relative', zoom:'1', border:'none', background:'none', padding:'none', margin:'none'}));
		            var pos = self.position(), h = self.outerHeight(true), paddingleft = self.css('padding-left');
		            var holder = $('<span id="spanPlaceholder"></span>').text(txt).css({position:'absolute', left:pos.left, top:pos.top, height:h, lineHeight:h+"px", paddingLeft:paddingleft, color:'#aaa',overflow:"hidden"}).appendTo(self.parent());
		            self.focusin(function(e) {
		            	self.css({border:'1px solid #4DADFF'});
		                holder.hide();
		            }).focusout(function(e) {
		            	self.css({border:'1px solid #AAAAAA'});
		                if(!self.val()){
		                    holder.show();
		                }
		            });
		            holder.click(function(e) {
		                holder.hide();
		                self.focus();
		            });
		            $this.placeholderCheck = true;
	            }
	        });
        }
	}
    /**
     * 检测是否为IE浏览器
    */
	this._check = function(){
		return "placeholder" in document.createElement('input');
	}

    
    /**
     * 获取提示词
     */
	var _getPlacText=function(typeValue){
		
		var text="";
		switch(typeValue){
			case "1":
				   text="可输入道路编号，如：G107";
				   break;
			case "3":
				   text="可输入河流、湖泊、水库名称";
				   break;
			case "5":
				   text="可输入地名，如：北京";
				   break;
			case "6":
				   text="可输入标准图幅号，如：H48E001018";
				   break;
		}
		return text;
	}
    
    /**
     * 图形穿刺点击事件
     */
    this.polySearchClickFn = function(){
    	var statu = $('#checkRePeat').attr('used');
    	if(statu=='no'){
    		$('#checkRePeat').attr('used','yes');
    		_toggleMouse(true);
    		this.registerMapClickEvent(true);
    	}else{
    		$('#checkRePeat').attr('used','no');
    		_toggleMouse(false);
    		this.registerMapClickEvent(false);
    		this.clearVectors();
    	}
    }

    /**
     * 开关mouse样式
     */
    this.toggleMouse = function(mark){
    	if(mark) {
    		$('#map').css('cursor','crosshair');
    	} else {
    		$('#map').css('cursor','Default');
    	}
    }
    
    /**
     * 清除穿刺存放图形
     */
    this.clearVectors=function(){
    	
    	if(this.addVectors.length>0){
        	for(var i=0;i<this.addVectors.length;i++){
        		var vector=this.addVectors[i];
        		_oplayerMap.removeResultLayer(vector);
        	}
        	this.addVectors=[];
        }
    }
    
    /***
     * 弹出多折线缓冲设置对话框
     */   
    this.popPlBufferDlg = function(){
        $("#plBufferInput").css("border","1px solid #e6e6e6");
        var h = document.documentElement.clientHeight;
        var w = document.documentElement.clientWidth;
        var _scroll = window.pageYOffset;
        var top = h/2 - (126/2);
        var left = w/2 - (190/2);
        $("#plBufferDlg").show();
    };
    
 
//------------------------------------地图事件------------------------------------------------------- 

    /**
     * 注册地图工具栏添加要素成功事件
     */
    this.registerFeatureaddedEvent=function(){
    	
	    var ctrls = _oplayerMap.getControls();
	    for (var key in ctrls) {
	        var ctrl = ctrls[key];
	        ctrl.events.on({
	            featureadded : _ctrlEvent,
	            scope : this
	        });
	    }
    };

    /***
     *  地图工具栏添加要素成功响应事件
     */
    var _ctrlEvent = function(e) {
    	
        var ctrl = e.object, ctrlId = ctrl.id;
        var geom=e.feature.geometry;
        var mapUtils=MapUtils.getInstance(null);
        var wkt = mapUtils.getWKTFromGeometry(geom);
        ctrl.deactivate();
         
        switch(ctrlId) {
            case "polygonCtrl":
            case "rectCtrl":
                this.drawGeom = wkt;
                this.geomType = "normal";
                _toggleMouse(false);
                SearchTools.getInstance().setDrawGeom(wkt);
                break;
            case "lineCtrl":
                this.drawGeom = wkt;
                this.polyline = {
                    wkt : wkt,
                    bufferDist : 10
                };
                this.geomType = "polyline";
                SearchTools.getInstance().setDrawGeom(wkt);
                this.popPlBufferDlg();
                break;
        }
    };
    
    /***
     * 注册地图点击事件
     * @param flag {boolean}
     */
    this.registerMapClickEvent = function(flag) {
        map.events.unregister("click", this, _mapClickFunction);
        if (flag) {
            map.events.register("click", this, _mapClickFunction);
        }
    };
    
    /**
     * 取消地图点击事件
     */
    this.cancelMapClickEvent = function(){
    	
    	map.events.unregister("click", this, _mapClickFunction);
    }
      
    /***
     * 图形点击事件
     */
    var _mapClickFunction = function(e) {
    	
        if (this.father.resultVectors.length <= 0){
        	return null;
        }
        //清除之前存放的图形
        this.clearVectors();
        
        var px = e.xy;
        var lonlat = map.getLonLatFromPixel(px);
        var point = new OpenLayers.Geometry.Point(lonlat.lon, lonlat.lat);
        var detailList = this.father.father.listManager.ResultData.itemList;
        var resultList =[];
        for (var i = 0; i < detailList.length; i++) {
            var item = detailList[i];
            var geom = MapUtils.getInstance().getGeomFromWKT(item.geom);
            var flag = geom.intersects(point);
            if (flag) {
            	resultList.push(item);
            }
        }
        if(resultList.length>0){
        	//详情列表展示
        	this.father.father.listManager.showStatList("scaleListBox");
        	this.father.father.listManager.normalListObj.detialListShow(resultList,this.father.father.attrManager.resType);
        	//
        	window.mapPage.detailManager.content(resultList,'5');
        	//地图展示
        	for(var i=0;i<resultList.length;i++){
        		var item=resultList[i];
        		var geom=item.geom;
        		var vector=_oplayerMap.addPolygon(
        				    geom,null,{
	    			            strokeWidth : 2,
	    			            strokeOpacity : 1,
	    			            strokeColor : "#FF0000",
	    			            fillColor : "#f57d28",
	    			            fillOpacity : 0
	                });
        		this.addVectors.push(vector);
        	}
        }
    };

//------------------------------------查询方法---------------------------------------------------------

    /**
     *  道路、水系、地址查询函数
     */
    this.mapAreaSearchFn=function(){
    	var areaSearchInput = $("#areaSearchDlg").find("input").eq(0);
    	var typeValue = areaSearchInput.attr("typeValue");
    	var keyWord   = areaSearchInput.attr("value");
    	mapPage.mapAreaSearch(typeValue,keyWord);
    	$("#areaSearchDlg").hide();
    };
    
    /***
     * 多折线缓冲设置查询函数
     */
    this.plBufferSearchFn = function(e){
        var dlgManager = e.data;
        var buffer = $("#plBufferInput").val();
        if(isNaN(buffer)){
            $("#plBufferInput").css("border","1px solid red");
            return null;
        }
        SearchTools.getInstance().setBuffer(buffer);
        //----------------查询调用-----------------
        dlgManager.closePlBufferDlg();
        window.mapPage.search();
    };
    
//------------------------------------初始化方法-------------------------------------------------------    
    
    /***
     * 关闭多折线缓冲设置对话框
     */
    this.closePlBufferDlg = function(){
        $("#plBufferDlg").hide();
    };
    
    /***
     * 关闭地图工具栏区域搜索对话框 
     */
    this.closeAreaSearchDlg = function(){
        $("#areaSearchDlg").hide();
    };
    
    /***
     * 初始化多折线缓冲设置对话框
     */
    var _initPlBufferDlg = function(){
        $("#plBufferCloseBtn").click(this.closePlBufferDlg);
        $("#plBufferBtn").bind("click",this,this.plBufferSearchFn);
        $("#plBufferCancelBtn").click(this.closePlBufferDlg);
    };
    
    /**
     * 初始图幅号查询点击事件
     */
    var _initMapNumSearchClick = function(){
    	$("#mapNum").find(".map-num-btn").click(function(){
    		var key = $("#mapNum").find("textarea").val();
    		var reg = /[ ,，；;\n]/;
    		var arr = [];
    		var tmp = reg.exec(key);
    		while(tmp){
    			var tmpStr = key.substring(0,key.indexOf(tmp[0]));
    			if(tmpStr.length > 0){
    				arr.push(tmpStr);
    			}
    			key = key.substring(tmpStr.length+1);
    			tmp = reg.exec(key);
    		}
    		arr.push(key);
    		window.mapPage.attrManager.resetFun();
    		window.mapPage.searchMapNum(arr);
    		$("#mapNumClose").click();
    	});
    	
    	$("#mapNumClose").click(function(){
    		$("#mapNum").hide();
    	});
    }
    
    
    /**
     * 初始经纬度查询点击事件
     */
    var _initLatLongSearchClick = function(){

    	$("#latLong").find(".lat-long-btn").click(function(){
    		var t = $(this).text();
    		if(t === "重置"){
    			$("#latLong").find("input").val("");
    		}else{
    			var key = $("#latLong").find(".com-check").attr("v");
    			if(key === "0"){//矩形
    				var arrVal = [];//存储数字
    				var $inputs = $("#latLong").find(".lat-long-"+key).find("input");
    				$inputs.each(function(i){
    					var $this = $(this);
    					var v = $(this).val();
    					if(v.length === 0){
    						v = 0;
    					}
    					v = parseFloat(v);
    					if(!isNaN(v)){//经纬度是数字
    						if(i%3 !== 0 && (v < 0 || v > 60)){//分和秒不在0-60
    							return _showLatLongError("分和秒必须是0度到60度之间!",$this);
    						}
    						arrVal.push(v);
    					}else{
    						return _showLatLongError("经纬度只能为数字!",$this);
    					}
    				});
    				if(arrVal.length !== 12){//为校验完数字
    					return;
    				}
    				var rectArr = _getRectArr(arrVal);
    				if(rectArr[0] < -180 || rectArr[2] > 180){
    					return _showLatLongError("经度必须是-180度到+180度之间!");
    				}else if(rectArr[1] < - 90 || rectArr[3] > 90){
    					return _showLatLongError("纬度必须是-90度到+90度之间!");
    				}else if(rectArr[0] > rectArr[2]){
    					return _showLatLongError("最小经度必须小于最大经度!");
    				}else if(rectArr[1] > rectArr[3]){
    					return _showLatLongError("最小纬度必须小于最大纬度!");
    				}
    				var text = 'POLYGON((';
    				text += rectArr[0] + " " + rectArr[1] + ",";
    				text += rectArr[0] + " " + rectArr[3] + ",";
    				text += rectArr[2] + " " + rectArr[3] + ",";
    				text += rectArr[2] + " " + rectArr[1] + ",";
    				text += rectArr[0] + " " + rectArr[1];
    				text += '))';
    				SearchTools.getInstance().setDrawGeom(text);
    				window.mapPage.search();
    			}
    			
    			$("#latLongClose").click();
    		}
    	});
    	
    	/*获取到矩形的四个值*/
    	var _getRectArr = function(arr){
    		var rs = [];
    		var len = arr.length/3;
    		for(var i = 0; i <len; i++){
    			var index = i * 3;
    			if(arr[index] < 0){
    				var v = arr[index] - arr[index+1]/60 - arr[index+2]/3600;
    			}else{
    				var v = arr[index] + arr[index+1]/60 + arr[index+2]/3600;
    			}
    			rs.push(v);
    		}
    		return rs;
    	}
    	
    	/*经纬度查询的显示错误信息*/
    	var _showLatLongError = function(txt,$elem){
    		var message = {btnName:'确认',info:txt,btnClick:function(){
    			$elem && $elem.focus();
			}};
			new PopWindow(message);
    		return false;
    	}
    	
    	$("#latLong").find(".lat-long-select>i").click(function(){
    		var className = $(this).attr("class");
    		if(!className){
    			var v = $(this).attr("v");
    			$latLong.find(".lat-long-"+v).show().siblings().hide();
    			$(this).attr("class","com-check").siblings().attr("class","");
    		}
    	});
    	
    	$("#latLongClose").click(function(){
    		$("#latLong").hide();
    	});
    }
    
    /***
     * 初始化工具栏区域搜索对话框 
     */
    var _initareaSearchDlg = function(){
        $("#mapAreaSearchCloseBtn").click(this.closeAreaSearchDlg);
        $("#mapAreaSearchBtn").click(this.mapAreaSearchFn);
        $("#roadSearch").click(this.popAreaDlgClickFn);
        $("#riverSearch").click(this.popAreaDlgClickFn);
        $("#addressSearch").click(this.popAreaDlgClickFn);
        $("#mapNumSearch").click(this.popAreaDlgClickFn);
        $("#latandlong").click(this.latandlongClickFn);
        $("#mapnum").click(this.mapnumClickFn);
    };
    
    /**
     * 设置地图比例尺位置
     */
    var _setPanBar = function(){
    	 $(".olControlSCGISPanZoomBar").css("top", "120px");
    };
    
    /**
     * 绑定列表框收缩点击事件
     */
    var _hideShowListClickFn =function(){
    	$('.hidenBtnt').toggle(function() {
				$('.showData').css('right','-410px');
				//设置地图宽度offsetWidth;
				$('.mapZone .showMap').css({'width':"100%"});
				$(this).find("i").eq(0).addClass("rightImg");
		        map.updateSize();
			},function(){
				$('.showData').css('right','0px');
				var w = $(document).width() - 410;
				$('.mapZone .showMap').css({'width':w+"px"});
				$(this).find("i").eq(0).removeClass("rightImg");
		        map.updateSize();
			});
    };
    
    /**
     * 绑定底图切换点击事件
     */
    var _baseLayerChooseClickFn=function(){
    	
        $('.iconOfLayer').hover(function(){
				$(this).css('height','185px');
	    },function(){
				$(this).css('height','56px');
		});
        $(".iconOfLayer div").click(_baseLayerChangeEvt);
    };
    
    var wmtsLayer=null;
    
    /**
     * 底图切换事件
     */
    var _baseLayerChangeEvt=function(e){
    	var $tg = $(e.currentTarget);
        var layerName = $tg.attr("id");
       
        var vAnno    = map.getLayersByName("vector_anno")[0];
        var imgLayer = map.getLayersByName("img")[0];
        var iAnno    = map.getLayersByName("img_anno")[0];
        var tanLayer = map.getLayersByName("terrain")[0]; 
        var tAnno    = map.getLayersByName("terrain_anno")[0]; 
        var target   =map.getLayersByName(layerName)[0];
        if(layerName=="vector"){
        	 map.setBaseLayer(target);
        }else if(layerName=="img"){
        	 if(wmtsLayer!=null){
        		 map.removeLayer(wmtsLayer);
        		 wmtsLayer=null;
        	 }
        	 wmtsLayer = new OpenLayers.Layer.WMTS({
             	 name: "wmtsLayer",
                 url: "http://t0.tianditu.cn/ibo_c/wmts",
                 layer: "ibo",
                 matrixSet: "c",
                 format: "tiles",
                 style: "default",
                 maxZoomLevel: 18,
                 opacity: 1,
                 isBaseLayer: false
             });
        	 map.addLayer(wmtsLayer);
        	 map.setLayerIndex(wmtsLayer,3);
        	 map.setBaseLayer(wmtsLayer);
        }else{
        	 if(wmtsLayer!=null){
	       		 map.removeLayer(wmtsLayer);
	       		 wmtsLayer=null;
       	     }
	       	 wmtsLayer = new OpenLayers.Layer.WMTS({
            	name: "wmtsLayer",
                url: "http://t0.tianditu.cn/tbo_c/wmts",
                layer: "tbo",
                matrixSet: "c",
                format: "tiles",
                style: "default",
                maxZoomLevel: 18,
                opacity: 1,
                isBaseLayer: false
	         });
	       	 map.addLayer(wmtsLayer);
	       	 map.setLayerIndex(wmtsLayer,5);
	       	 map.setBaseLayer(wmtsLayer);
        }
        
        switch (layerName) {
            case "vector":
            	vAnno.setVisibility(true);
                imgLayer.setVisibility(false);
                iAnno.setVisibility(false);
                tanLayer.setVisibility(false);
                tAnno.setVisibility(false);
                break;
            case "img":
                vAnno.setVisibility(false);
                imgLayer.setVisibility(true);
                iAnno.setVisibility(true);
                tanLayer.setVisibility(false);
                tAnno.setVisibility(false);
                break;
            case "terrain":
            	 vAnno.setVisibility(false);
                 imgLayer.setVisibility(false);
                 iAnno.setVisibility(false);
                 tanLayer.setVisibility(true);
                 tAnno.setVisibility(true);
                break;
        }
         _setPanBar();
    };
    
    
    /**
	 * 初始化方法（特权方法）
	 */
	this.init=function(def){
		this.father = def.father;
		_oplayerMap=def.oplayerMap;
		map       =def.map;
		
		//注册地图工具栏添加要素成功事件
		this.registerFeatureaddedEvent();
		//初始化多折线缓冲设置对话框
		_initPlBufferDlg.call(this);
		//初始化工具栏区域搜索对话框
        _initareaSearchDlg.call(this);
        //设置地图比例尺位置
        _setPanBar.call(this);
        //绑定列表框收缩点击事件
        _hideShowListClickFn.call(this);
        //绑定底图切换点击事件
        _baseLayerChooseClickFn.call(this);
	};
    
    //构造函数 可带参 调用初始化方法
	this.init.apply(this, arguments);
}

/**
 * 此文件用于动态生成图幅号查询和经纬度查询的代码
 */
var RangeSearch = (function(){
	var $latLongBox = null;//经纬度查询框  #latLong
	var $mapNumBox = null;//图幅号查询框 #mapNum
	var latLongData = null;//经纬度查询的数据
	/******* 处理经纬度查询 start*******/
	var rectangleName = "矩形";
	var polygon = "多边形";
	var polygonDefaultNum = 3;
	var $selectTypeElem = null;//保存选中的那一个类型元素
	
	var lCloseFlag = 1;//经纬度的关闭按钮标识
	var lTypeFlag = 2;//坐标类型
	var lAddFlag = 3;//新增按钮
	var lSearchFlag = 4;//查询按钮
	var lRsetFlag = 5;//重置按钮
	
	var mCloseFlag = 1;//图幅号查询关闭标识
	var mSearchFlag = 2;//图幅号查询 查询标识
	
	var latLogFucObj = {};
	var mapNumFucObj = {};
	//创建经纬度的查询框
	var _createLatLog = function(latLongId){
		if(!$latLongBox){
			$latLongBox = $("#"+latLongId);
			latLongData = [];
			latLongData.push({
				name:rectangleName,
				childList:[{name:"最小经度"},{name:"最小纬度"},{name:"最大经度"},{name:"最大纬度"}]
			});
			latLongData.push({
				name:polygon,
				childList:polygonDefaultNum //初始显示4个
			});
			latLogFucObj[lCloseFlag] = _closeClick;
			latLogFucObj[lTypeFlag] = _lTypeClick;
			latLogFucObj[lAddFlag] = _lAddClick;
			latLogFucObj[lSearchFlag] = _lSearchClick;
			latLogFucObj[lRsetFlag] = _lResetClick;
			_bindLayLogMouseEvent();
			_initLatLog();
		}
	}
	//绑定鼠标事件
	var _bindLayLogMouseEvent = function(){
		$latLongBox.on("click",".elemClick",function(){
			var flag = $(this).data("cflag");
			flag && latLogFucObj[flag] && latLogFucObj[flag]($(this));
		});
		
	}
	//初始化经纬度
	var _initLatLog = function(){
		/*右上角关闭按钮*/
		var $close = $('<div class="com-close-btn right elemClick"></div>')
			.data("cflag",lCloseFlag);
		//
		var $text = $('<div class="com-text">经纬度查询</div><div class="com-line"></div>');
		
		//坐标类型和坐标类型的对应显示
		var $select = $('<div class="lat-long-select"><span>坐标类型：</span></div>');
		var $chlidBox = $('<div class="lat-long-box" id="lat-long-box"></div>');
		for(var i in latLongData){
			var obj = latLongData[i];
			var $elem = $('<i class="elemClick">'+obj.name+'</i>')
						.data("cflag",lTypeFlag)
						.data("i",i)
			
			var $chlid = $('<div class="lat-long-0" style="display:none;"></div>');
			var childList = obj.childList;
			if(obj.name === rectangleName){//矩形
				_doRectangle($chlid,childList);
			}else if(obj.name === polygon){//多边形
				_doPolygon($chlid,childList);
			}	
			$chlidBox.append($chlid);
			$select.append($elem);
		}
		var $add = $('<span class="com-btn lat-long-add elemClick">+　新增</span>').data("cflag",lAddFlag);
		var $btns = $('<div class="lat-long-box-btn"></div>');
			$('<span class="com-btn lat-long-btn elemClick" style="margin-left:64px;">查询</span>')
			.data("cflag",lSearchFlag).appendTo($btns);
			$('<span class="com-btn lat-long-btn elemClick" style="margin-left:20px;">重置</span>')
			.data("cflag",lRsetFlag).appendTo($btns);
			
		$latLongBox.append($close,$text,$select,$chlidBox,$add,$btns);
		$select.find("i").eq(0).click();
	}
	//获取到latLong公用的东西
	var _getLatLongCom = function(name){
		return '<div class="lat-long-input"><span>'+name+'：</span>'
		+'<input type="text"><span>度</span>'
		+'<input type="text"><span>分</span>'
		+'<input type="text"><span>秒</span></div>'
	}
	//处理矩形
	var _doRectangle = function($chlid,childList){
		for(var i in childList){
			var obj = childList[i];
			$chlid.append(_getLatLongCom(obj.name));
		}
	}
	//处理多边形
	var _doPolygon = function($chlid,childList){
		for(var i = 0; i < childList; i++){
			var $aLine = $('<div class="lat-long-line"></div>');
			var $left = $('<div class="line-left">'+(i+1)+'</div>');
			var $right = $('<div class="line-right"></div>');
			$right.append(_getLatLongCom('经度'),_getLatLongCom('纬度'));
			
			$chlid.append($aLine.append($left,$right));
		}
	}
	//获取到当前显示
	var _getShowBox = function(){
		var i = $selectTypeElem.data("i");
		return $latLongBox.find(".lat-long-0").eq(i);
	}
	//类型点击
	var _lTypeClick  = function($elem){
		$selectTypeElem && $selectTypeElem.removeClass("com-check");
		$selectTypeElem = $elem.addClass("com-check");
		
		var $nowShow = _getShowBox();
		$nowShow.show().siblings().hide();
		if($elem.text() === polygon){//多边形的时候
			$latLongBox.find(".lat-long-add").show();
		}else{
			$latLongBox.find(".lat-long-add").hide();
		}
	}
	//新增按钮
	var _lAddClick = function(){
		var $nowShow = _getShowBox();
		var len = $nowShow.find(".lat-long-line").length +1;
		
		var $aLine = $('<div class="lat-long-line"></div>');
		var $left = $('<div class="line-left">'+ len +'</div>');
		var $right = $('<div class="line-right"></div>');
		$right.append(_getLatLongCom('经度'),_getLatLongCom('纬度'));
		$nowShow.append($aLine.append($left,$right));
		if($nowShow.height() > 220){//滚动滚动条
			$nowShow.parent().scrollTop($nowShow.height() - 220);
		}
	}
	//查询按钮点击
	var _lSearchClick = function(){
		var $showBox = _getShowBox();
		var $inputs = $showBox.find("input");
		var arrVal = [];//存储数值
		var baseCheck = true;//校验录入数据是否正确
		$inputs.each(function(i){
			var $this = $(this);
			var v = $(this).val();
			if(v.length === 0){
				v = 0;
			}
			v = parseFloat(v);
			if(!isNaN(v)){//经纬度是数字
				if(i%3 !== 0 && (v < 0 || v > 60)){//分和秒不在0-60
					baseCheck = false;
					var t = $this.next().text();
					return _showLatLongError(t + "必须是0到60之间的!",$this);
				}
				if(i%3 === 0 && v === 0){//每个经纬度的第一个值为空
					baseCheck = false;
					var t = $this.prev().text().replace("：","");
					return _showLatLongError(t + "必须输入值!",$this);
				}
				arrVal.push(v);
			}else{
				baseCheck = false;
				if(i%3 === 0){
					var t = $this.prev().text().replace("：","");
				}else{
					var t = $this.next().text();
				}
				return _showLatLongError(t+"必须为数字!",$this);
			}
		});
		if(!baseCheck){//为校验完数字
			return;
		}
		var rectArr = _getAngleArr(arrVal);

		var text = 'POLYGON((';
		if($selectTypeElem.text() === polygon){//是多边形
			var len = rectArr.length/2;
			for(var i = 0 ;i < len; i++){
				var long = rectArr[i*2];
				var lat = rectArr[i*2+1];
				if(long < -180 || long > 180){
					return _showLatLongError("经度必须是-180度到+180度之间!");
				}
				if(lat < - 90 || lat > 90){
					return _showLatLongError("纬度必须是-90度到+90度之间!");
				}
				text += long + " " + lat + ",";
			}
			text += rectArr[0] + " " + rectArr[1];
			
		}else if($selectTypeElem.text() === rectangleName){//矩形
			if(rectArr[0] < -180 || rectArr[2] > 180){
				return _showLatLongError("经度必须是-180度到+180度之间!");
			}else if(rectArr[1] < - 90 || rectArr[3] > 90){
				return _showLatLongError("纬度必须是-90度到+90度之间!");
			}else if(rectArr[0] >= rectArr[2]){
				return _showLatLongError("最小经度必须小于最大经度!");
			}else if(rectArr[1] >= rectArr[3]){
				return _showLatLongError("最小纬度必须小于最大纬度!");
			}
			text += rectArr[0] + " " + rectArr[1] + ",";
			text += rectArr[0] + " " + rectArr[3] + ",";
			text += rectArr[2] + " " + rectArr[3] + ",";
			text += rectArr[2] + " " + rectArr[1] + ",";
			text += rectArr[0] + " " + rectArr[1];
		}
		text += '))';
		SearchTools.getInstance().setDrawGeom(text);
		window.mapPage.search();
		
		//关闭掉
		_closeClick($latLongBox.find(".com-close-btn"));
	}
	/*获取到图形的经纬度值*/
	var _getAngleArr = function(arr){
		var rs = [];
		var len = arr.length/3;
		for(var i = 0; i <len; i++){
			var index = i * 3;
			if(arr[index] < 0){
				var v = arr[index] - arr[index+1]/60 - arr[index+2]/3600;
			}else{
				var v = arr[index] + arr[index+1]/60 + arr[index+2]/3600;
			}
			rs.push(v);
		}
		return rs;
	}
	/*经纬度查询的显示错误信息*/
	var _showLatLongError = function(txt,$elem){
		var message = {btnName:'确认',info:txt,btnClick:function(){
			$elem && $elem.focus();
		}};
		new PopWindow(message);
		return false;
	}
	//重置按钮点击
	var _lResetClick = function(){
		var $showBox = _getShowBox();
		if($selectTypeElem.text() === polygon){//是多边形
			var $chlidList = $showBox.find(".lat-long-line");
			for(var i = polygonDefaultNum; i < $chlidList.length; i++){
				$chlidList.eq(i).remove();
			}
		}
		$showBox.find("input").val("");
	}
	/******* 处理经纬度查询 end*******/
	
	/******* 处理图幅号查询 start*******/
	//创建图幅号的查询框
	var _createMapNum = function(mapNumId){
		if(!$mapNumBox){
			$mapNumBox = $("#"+mapNumId);
			
			mapNumFucObj[mCloseFlag] = _closeClick;
			mapNumFucObj[mSearchFlag] = _mSearchClick;

			_bindMapNumMouseEvent();
			_initMapNum();
		}
	}
	//绑定鼠标事件
	var _bindMapNumMouseEvent = function(){
		$mapNumBox.on("click",".elemClick",function(){
			var flag = $(this).data("cflag");
			flag && mapNumFucObj[flag] && mapNumFucObj[flag]($(this));
		});
	}
	//初始化图幅号
	var _initMapNum = function(){
		/*右上角关闭按钮*/
		var $close = $('<div class="com-close-btn right elemClick"></div>')
			.data("cflag",mCloseFlag);
		var $text = $('<div class="com-text">图幅号查询</div><div class="com-line"></div>');
		var $mapTextarea = $('<textarea></textarea>');
		var $btn = $('<span class="com-btn map-num-btn elemClick">查询</span>')
						.data("cflag",mSearchFlag);
		var $tips = $('<div class="map-num-tips">可输入多个图幅号，图幅号直接用空格或逗号分隔<br>如"G49E005001 G49E005002"或<br>"G49E005001,G49E005002"</div>');
		$mapNumBox.append($close,$text,$mapTextarea,$btn,$tips);
	}
	//查询按钮点击事件
	var _mSearchClick = function($elem){
		var key = $elem.prev().val();
		var reg = /[ ,，；;\n]/;
		var arr = [];
		var tmp = reg.exec(key);
		while(tmp){
			var tmpStr = key.substring(0,key.indexOf(tmp[0]));
			if(tmpStr.length > 0){
				arr.push(tmpStr);
			}
			key = key.substring(tmpStr.length+1);
			tmp = reg.exec(key);
		}
		arr.push(key);
		window.mapPage.attrManager.resetFun();
		window.mapPage.searchMapNum(arr);
		_closeClick($mapNumBox.find(".com-close-btn"));
	}
	/******* 处理图幅号查询 end*******/
	
	/******** 经纬度查询和图幅号查询公用部分 start ****************/
	//关闭按钮点击事件
	var _closeClick = function($elem){
		$elem.parent().hide();
	}
	/******** 经纬度查询和图幅号查询公用部分 end ****************/
	
	return {
		createLatLog:_createLatLog,
		createMapNum:_createMapNum
	}
})();



