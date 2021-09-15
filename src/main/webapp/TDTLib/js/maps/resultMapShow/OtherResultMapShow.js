/**
 * @Description 其它查询地图展示类 包含工具栏道路、水系、行政区域、无类别查询等
 * @Author yuanJianHua
 * @CreateTime 2015年12月7日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var OtherResultMapShow = function() {

//----------------------------相关属性-------------------------------------

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
    
//----------------------------相关方法-------------------------------------	
	 
     /**
      * 无类别、自定义类别查询结果地图展示
      */
	 this.OtherTypeMapShow=function(list){
	    	
	    	var listWKT=[];
	    	this.oplayerMap.clearRoadLayer();
	    	for(var i=0;i<list.length;i++){
	    		var item=list[i];
	    		var resType=item.resType;
	    		var geom=item.geom;
	    		listWKT.push(geom);
	    		

	    		if(resType==0){
	    			//命中道路、河流、湖泊、省市县等区域图形数据
	    			var gtype=item.gtype;
	    			if(gtype==0){
	        			//面展示
	        			this.oplayerMap.addPolygon(geom, null, {
	        	            strokeWidth : 1,
	        	            strokeOpacity : 1,
	        	            strokeColor : "#FF0000",
	        	            fillColor : "#FFFFFF",
	        	            fillOpacity : 0
	        	        }, "roadLayer");
	        			
	        		}else if(gtype==1){
	        			//道路河流线展示
	        			this.oplayerMap.addLineByWKT(geom,"roadLayer");
	        			this.mapUtils.setViewByWKT(geom);
	        		}else if(gtype==2){
	        		}	
	    		}else{
	    			//命中成果类型数据
	    			if(resType==1||resType==2||resType==3||resType==4){
		    			//命中点类产品
	    				var size = new OpenLayers.Size(14, 25);
						var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
						var point = this.mapUtils.getGeomFromWKT(item.geom);
						var icon = new OpenLayers.Icon(MapConstant.point_show_url, size, offset);
						if(point!=null||point!=undefined){
							this.oplayerMap.addMarker(point.x, point.y, icon, {
								id : item.id,
								cho:false
							});
						}
		    		}else if(resType==5||resType==6||resType==7||resType==10||resType==11){
		    			//命中图幅类产品
		    			var text=item.idNewMapNum;
		    			this.oplayerMap.addPolygon(item.geom, {
		                    id : item.name,
		                    dType : "mapNum"
		                }, {
		                    strokeWidth : 1,
		                    strokeOpacity : 0.5,
		                    strokeColor : "#FFFFFF",
		                    fillColor : "#f57d28",
		                    fillOpacity : 0.5,
		                    fontColor : "#FFFFFF",
		                    fontSize : "14px",
		                    fontFamily : "microsoft yahei",
		                    labelAlign : 'c'
		                },"resultLayer");
		    			
		    		}else if(resType==8||resType==9){
		    			//命中卫星影像类产品
		    			this.oplayerMap.addPolygon(item.geom, {
		                    id : item.id,
		                    dType : "polygon"
		                }, {
		                    strokeWidth : 1,
		                    strokeOpacity : 1,
		                    strokeColor : "#79c74f",
		                    fillColor : "#FFFFFF",
		                    fillOpacity : 0
		                },"resultLayer");
		    			
		    		}else{
		    			//命中自定义类产品
		    			var wkt=item.geom;
		    			if(wkt.indexOf("POINT")>=0){
		    				//点展示
		    				var size = new OpenLayers.Size(14, 25);
		    			    var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
		    			    var point = this.mapUtils.getGeomFromWKT(wkt);
		    	            var icon = new OpenLayers.Icon(MapConstant.point_show_url, size, offset);
		    	            if(point!=null||point!=undefined){
		    	                this.oplayerMap.addMarker(point.x, point.y, icon, {
		    	                    id : item.id,
		    	                    cho:false
		    	                });
		    	            }
		    			}else{
		    				//面展示
		    				this.oplayerMap.addPolygon(item.geom, {
			                    id : item.id,
			                    dType : "polygon"
			                }, {
			                    strokeWidth : 1,
			                    strokeOpacity : 1,
			                    strokeColor : "#79c74f",
			                    fillColor : "#FFFFFF",
			                    fillOpacity : 0
			                },"resultLayer");
		    			}
		    		}
	    		}  
	    	}
	    	if(listWKT.length>0){
	    		this.mapUtils.setViewByPointListWKT(listWKT);
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