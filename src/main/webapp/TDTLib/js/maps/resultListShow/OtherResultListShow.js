/**
 * @Description 其它查询列表展示类 包含工具栏道路、水系、行政区域、无类别查询等
 * @Author yuanJianHua
 * @CreateTime 2015年12月7日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var OtherResultListShow=function(){
	
//-------------------------------相关属性---------------------------------
	
	/**
	  * 基础地图类对象
	  */
	 this.oplayerMap=null;
	 /**
	  * 地图工具类对象
	  */
	 this.mapUtils=null;
	 
	this.showSearchName =  {

		"0":{resTypeName:"地址名称",resShow:["名称","别名"]},
		"1":{resTypeName:"重力点",resShow:["点名","网名","等级类型","生产时间","大地基准"]},
		"2":{resTypeName:"水准点",resShow:["路线名称","点名","网名","等级","高程基准"]},
		"3":{resTypeName:"三角点",resShow:["点名","网名","等级","生产时间","大地基准"]},
		"4":{resTypeName:"GNSS成果",resShow:["点名","网名","等级","生产时间","大地基准"]},
		"5":{resTypeName:"矢量地图数据",resShow:["新图号","比例尺","生产时间","大地基准","数据格式"]},
		"6":{resTypeName:"数字高程模型",resShow:["新图号","格网间距","生产时间","大地基准","数据格式"]},
		"7":{resTypeName:"正射影像",resShow:["新图号","比例尺","分辨率","影像时间","大地基准"]},
		"8":{resTypeName:"航空影像",resShow:["摄区名称","航摄比例尺","分辨率","航摄仪","航摄年份"]},
		"9":{resTypeName:"卫星影像",resShow:["卫星","传感器","分辨率","云量","接收时间"]},
		"10":{resTypeName:"数字栅格地图",resShow:["新图号","比例尺","出版时间","大地基准","数据格式"]},
		"11":{resTypeName:"模拟地图数据",resShow:["新图号","比例尺","出版时间","大地基准","高程基准"]}
				
	};
	this.showSearchCode = {
		"0":["bname","name"],
		"1":["idName","idNet","idClass","idFormData","crsDatum"],
		"2":["idLnName","idName","idNet","idClass","crsVertCRS"],
		"3":["idName","idNet","idClass","idFormData","crsDatum"],
		"4":["idName","idNet","idClass","idFormData","crsDatum"],
		"5":["idNewMapNum","idMapScale","idFormData","crsDatum","dtFormatName"],
		"6":["idNewMapNum","idScaleDist","idFormData","crsDatum","dtFormatName"],
		"7":["idNewMapNum","idMapScale","idScaleDist","TePosition","crsDatum"],
		"8":["idGranuleName","idEquScale","idScaleDist","idSensor","tePosition"],
		"9":["idSatellite","idSensor","idScaleDist","imgdCloudCovPer","tePosition"],
		"10":["idNewMapNum","idMapScale","idFormData","crsDatum","dtFormatName"],
		"11":["idNewMapNum","idMapScale","idVersionType","crsDatum","crsVertCRS"]
	};
	
	/**
	 * 判断是否系统类型
	 */
	this.isSysType=function(typeID){
		if(this.showSearchName[typeID]!=null){
			return true;
		}else{
			return false;
		}
	}
	
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
	
//-------------------------------相关方法---------------------------------
	
	/**
	 * 添加列表信息
	 */
	this.OtherTypeListShow = function(list,divId,totalNum){
		this.searchPageData = {nowPage:SearchTools.getInstance().getPageNum() - 1,pageNum:10};
		this.showSearchList = list;
		this.totleSearchWidth = "400px;"
		this.imgSrc = "./public/style/images/query/noselect/";
		this.searchDivId = divId;
		console.log(totalNum);
		this.addSearchByList(list,totalNum);
	}
	
	this.addSearchByList = function(list,totalNum){
		$("#"+this.searchDivId).html("");
		//解析数据
		var nowPage = this.searchPageData["nowPage"];//当前页 0开始
		var pageNum = this.searchPageData["pageNum"];//每页条数
		var startNum = nowPage*pageNum;//开始位置
		var endNum = (nowPage+1)*pageNum;//结束位置
		var totleNum = totalNum;//总记录数
		var indexNum = 1;

		//显示列表
		var $search = $('<div class="z-search" style="width:'+this.totleSearchWidth+';"></div>');
		for(var i = 0 ; i < list.length ; i++,indexNum++){
			//item.gtype       图形类型 0 面  1 线  2 点
            //item.regiontype  省市县类型 0 省  1 市  2 县
            //item.areatype    数据类型 0 省市县  1 道路  2 铁路 3 河流 4 湖泊
			var item=list[i];
			var id   =item.id;
			var point=item.geom+";"+(indexNum)+";"+item.name+";"+item.areatype+";"+item.regiontype+";"+item.resType;
			var $thisLi = $('<div class="z-search-li"></div>');
			var $thisElem = $('<div class="z-search-elem"></div>');
			
			var restype = list[i]["resType"];
			var flag    = this.isSysType(restype);
			if(flag){
				//---------------系统定义类型-----------------------------------
				var restypeName = this.showSearchName[restype]["resTypeName"];
				var resShow = this.showSearchName[restype]["resShow"];
				var showCode = this.showSearchCode[restype];
//				$thisElem.append($('<div class="z-search-img"><img src="'+this.imgSrc+indexNum+'.png"/></div>'));
				$txt = $('<div class="z-search-txt" gtype="'+item["gtype"]+'" id="' + point + '"></div>');
				if(restype == "0"){
					//道路、河流、省市县区域数据
					$txt.append('<div class="z-search-info">'+resShow[0]+':'+item[showCode[0]]+'</div>');
					if(item["areatype"] == "1"){
						for(var j = 1; j < resShow.length; j ++){
							$txt.append('<div class="z-search-biref">'+resShow[j]+':'+item[showCode[j]]+'</div>');
						}
					}
					
					//0 省市县\4 湖泊有成果查询
					if(item["areatype"] == "0"||item["areatype"] == "4"){
						$txt.append('<div class="z-search-lineSearch">成果查询</div>');
					}
					$thisElem.append($txt);
					$thisLi.append($thisElem);
					$search.append($thisLi);
				}else{
					//系统类型数据
					$txt.append('<div class="z-search-info">数据类型:'+restypeName+'</div>');
					for(var j = 0; j < resShow.length; j ++){
						$txt.append('<div class="z-search-biref">'+resShow[j]+':'+item[showCode[j]]+'</div>');
					}
					
					$thisElem.append($txt);
					//详情按钮
					$thisElem.append($('<div class="z-search-detail"  attr='+id+'><img src="./public/style/images/magnifierBlue.png" title="详细信息"/></div>'));
					//购物车按钮
					$thisElem.append($('<div class="z-search-shopCar" attr='+id+'><img src="./public/style/images/shopCarIcon.png"   title="加入成果车"/></div>'));
					$thisLi.append($thisElem);
					$search.append($thisLi);
				}
			}else{
				//----------------------------自定义类型---------------------------
//				$thisElem.append($('<div class="z-search-img"><img src="'+this.imgSrc+indexNum+'.png"/></div>'));
				$txt = $('<div class="z-search-txt" gtype="'+item["gtype"]+'" id="' + point + '"></div>');
				var name=SearchTools.getInstance().getTypeName(restype);
				$txt.append('<div class="z-search-info">数据类型:'+name+'</div>');
				var count=_getPropertyCount(item);
				var num  =0;
				for(var key in item){
					if(num>4){
						break;
					}
					if(item.hasOwnProperty(key)){
						if(key=="geom"||key=="id"||key=="resType"||key=="sourceCode"||key=="storeId"||key=="idResType"
							||key=="fileid"||key=="cityName"||key=="cntyName"||key=="provName"||key=="touchRegion"){
							continue;
						}
						var value=item[key];
						if(value!=null&&value!=""){
							num++;
							$txt.append('<div class="z-search-biref">'+value+'</div>');
						}
					}
				}
				$thisElem.append($txt);
				//详情按钮
				$thisElem.append($('<div class="z-search-detail"  attr='+id+'><img src="./public/style/images/magnifierBlue.png" title="详细信息"/></div>'));
				//购物车按钮
				$thisElem.append($('<div class="z-search-shopCar" attr='+id+'><img src="./public/style/images/shopCarIcon.png"   title="加入成果车"/></div>'));
				$thisLi.append($thisElem);
				$search.append($thisLi);
			}
		}
		
		//获取上下页
		if(totleNum/pageNum >1){
			$search.append(getPageElem(nowPage,pageNum,totleNum));
		}
		$("#"+this.searchDivId).append($search);
		
		//----------------------------绑定鼠标事件------------------------------
		//页面跳转事件
		$(".z-search-btnli div").bind("click", this, this.showSearchPage);
		
		//元素点击事件，地图居中显示
		$(".z-search-txt").bind("click", this, this.itemLocation);
		//加入成果车事件 
		$(".z-search-shopCar").bind("click", this, this.addShopCar);
		//详情查询事件
		$(".z-search-detail").bind("click", this, this.detailsLook);
	}
	
	/**
	 * 处理上页是否显示
	 */
	function getPageElem(nowPage,pageNum,totleNum){
		var totlePage = parseInt(totleNum/pageNum);
		if(totlePage*pageNum == totleNum){
			totlePage--;
		}
		var dis = ["visibility: hidden;","visibility: hidden;"];//上一页 下一页的样式是否需要隐藏
		$btn = $('<div class="z-search-btnli"></div>');
		
		if(nowPage != 0){
			dis[0] = "";//不是第一页，显示上一页样式
		}
		$btn.append('<div class="z-search-btn" style="'+dis[0]+'" num="'+(nowPage-1)+'"><上一页</div>');
		var btnSel = nowPage + 3 - totlePage;
		if(btnSel<0){
			for(var i = 0; i < 4 && i < totlePage;i++){//页数多于四页并且不是最后四页时候显示
				if(i == 0){
					var str = '<div class="z-search-num-btn btn-sel">'+(nowPage+i+1)+'</div>';
				}else{
					var str = '<div class="z-search-num-btn" num="'+(nowPage+i)+'">'+(nowPage+i+1)+'</div>';
				}
				$btn.append($(str));
			}
		}else{
			for(var i = 0; i < 4 && i < totlePage+1;i++){
				if(totlePage < 4){//页数少于四页使用
					if(i == nowPage){
						var str = '<div class="z-search-num-btn btn-sel">'+(i+1)+'</div>';
					}else{
						var str = '<div class="z-search-num-btn" num="'+i+'">'+(i+1)+'</div>';
					}
					}else{//页数多于四页并且是最后四页时候显示
						if(i == btnSel){
						var str = '<div class="z-search-num-btn btn-sel">'+(nowPage-btnSel+i+1)+'</div>';
					}else{
						var str = '<div class="z-search-num-btn" num="'+(nowPage+i-btnSel)+'">'+(nowPage-btnSel+i+1)+'</div>';
				  	}
				}
				$btn.append($(str));
			}
		}
		
		var totlePage = parseInt(totleNum/pageNum);
		if(totlePage*pageNum == totleNum){
			totlePage--;
		}
		if(nowPage != totlePage && totlePage*pageNum != totleNum){
			dis[1] = "";
		}
		$btn.append('<div class="z-search-btn" style="'+dis[1]+'" num="'+(nowPage+1)+'">下一页></div>');
		return $btn;
	}
	
	/**
	 * 点击跳转页码
	 */
	this.showSearchPage = function(e){
    	var serch = e.data;
        var num = $(e.currentTarget).attr("num");
        if(num){
        	serch.searchPageData["nowPage"] = parseInt(num);
        	SearchTools.getInstance().setPageNum(serch.searchPageData["nowPage"]+1);
        	window.mapPage.pageSearch();
        	//serch.addSearchByList(serch.showSearchList);
        }
    }
	
	/**
	 * 元素点击事件，地图居中显示
	 */
    this.itemLocation=function(e){
    	var thisObj = e.data;
    	var id=$(e.currentTarget).attr("id");
    	var gtype = $(e.currentTarget).attr("gtype");
    	
		var temp=id.split(";");
    	var geom       =temp[0];
        var index      =temp[1];
        var regionName =temp[2];
        var areatype   =temp[3];
        var regiontype =temp[4];
        var resType    =temp[5];
        
        if(!thisObj.isSysType(resType)){
        	//自定义类型
        	if(geom.indexOf("POINT")>=0){
        		//点图形
        		thisObj.addSelectPOI(geom);
        	}else{
        		//面图形
        		thisObj.addSelectPOI(geom);
        	}
        }else{
        	//系统定义类型
        	if(geom!=null){
        		var point=thisObj.mapUtils.getCenterFromWKT(geom);
                thisObj.addSelectPOI(point); 
        	}
            thisObj.mapUtils.setViewByWKT(geom);
            var mapPage = window.mapPage;
            if(resType==0){
            	//地图区域地址成果查询(wkt,areatype,regiontype,regionName)
                //gtype       图形类型 0 面  1 线  2 点
                //regiontype  省市县类型 0 省  1 市  2 县
                //areatype    数据类型 0 省市县  1 道路  2 铁路 3 河流 4 湖泊
            	switch(areatype){
            	case "0":
            		SearchTools.getInstance().setRegionName(regionName);
            		mapPage.isSetKeyWord=false;
            		mapPage.search();
            		break;
            	case "4":
            		SearchTools.getInstance().setRegionGeom(geom);
            		mapPage.isSetKeyWord=false;
            		mapPage.search();
            		break;
            	}
            }
        }
    }
    
    //地图高亮显示选中点
    this.addSelectPOI=function(wkt){
    	
        if(wkt!=null){
        	var size = new OpenLayers.Size(14, 25);
            var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
            var icon = new OpenLayers.Icon(MapConstant.point_location_url,size,offset);
            var point =this.mapUtils.getGeomFromWKT(wkt);
            this.oplayerMap.clearTempMarkerLayer();
            this.oplayerMap.addMarker(point.x, point.y, icon,null,"tempMarkerLayer");
            if(wkt.indexOf("POINT")>=0){
            	this.mapUtils.setCenterByWKT(wkt,12);
            }else{
            	this.mapUtils.setViewByWKT(wkt);
            }
        }
    };
    
    
    /**
     * 加入成果车点击响应函数
     */
    this.addShopCar=function(e){
    	
    	var obj = e.data;
    	var id = $(e.currentTarget).attr("attr");
   	 	obj.father.father.addShopCar(id);
   	    //obj.father.father.mapCarManager.refresh(1);
    }
    
    /**
     * 详情查看点击响应函数
     */
    this.detailsLook=function(e){
    	
    	 var obj = e.data;
    	 var id = $(e.currentTarget).attr("attr");
    	 var item=obj.getItemByID(id);
    	 var type=item.resType;
    	 var list=[];
    	 list.push(item);
    	 window.mapPage.detailManager.content(list,type);
    }
    
    
    /**
     * 根据ID获取元素信息
     */
    this.getItemByID=function(id){
    	
    	var list=this.showSearchList;
    	var item=null;
    	for(var i=0;i<list.length;i++){
    		var temp=list[i];
    		var tempID=temp.id;
    		if(id==tempID){
    			item=temp;
    			break;
    		}
    	}
    	return item;
    }

//---------------------------------------初始化方法--------------------------------------------------

    this.init = function(def) {
        this.father     = def.father;
        this.oplayerMap = def.oplayerMap;
        this.mapUtils   = def.mapUtils;
    };
	
	
	this.init.apply(this, arguments);	
	
}