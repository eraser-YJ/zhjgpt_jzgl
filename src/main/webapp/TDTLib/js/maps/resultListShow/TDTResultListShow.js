/**
 * @Description 天地图地址查询列表展示类
 * @Author yuanJianHua
 * @CreateTime 2015年12月7日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var TDTResultListShow=function(){
	
//------------------------------------------相关属性-----------------------------------------------------------------	
	
	 /**
	  * 当前点数据结果集
	  */
	 this.poiList=null;
	 
	 /**
	  * 当前选择点
	  */
	 this.poiMarker=null;
	 
	 /**
	  * 基础地图类对象
	  */
	 this.oplayerMap=null;
	 
	 /**
	  * 地图工具类对象
	  */
	 this.mapUtils=null;
	
	
    /**
     * 清除天地图查询列表内容
     */
    this.clearList = function() {
        $("#newTianDiTuList div").remove();
        $("#moreList div").remove();
        $("#pageDiv div").remove();
    };
    
//------------------------------------------行政区域展示-----------------------------------------------------------------    
    
    /**
     * 增加天地图查询结果展示内容（行政区划）
     */
    this.addArea = function(data){
    	 this.clearList();
         var box = $("#newTianDiTuList");
         var str = '<div class="areaItemGroup" id="' + data.name + 'Item">';
         str += '<div class="areaTitleGroup" id="' + data.name + '">';
         str += '<a href="javascript:void(0)" id="' + data.name + '">' + data.name + '</a>';
         str += '</div></div>';
         box.append($(str));
    }
    
//------------------------------------------点数据展示-----------------------------------------------------------------

    /**
     * 增加天地图查询结果展示内容（点数据）
     */
    this.addPoiList = function(list){
    	this.poiList=list;
    	//清除天地图列表内容
    	this.clearList(); 
    	//分页内容
    	var box = $("#newTianDiTuList");
        var pageNum='共'+this.father.father.tdtSearcher.TDTSearch.getCountNumber()+'条记录，分'
        +this.father.father.tdtSearcher.TDTSearch.getCountPage()+'页,当前第'
        +this.father.father.tdtSearcher.TDTSearch.getPageIndex()+'页'; 
        var pageNumDiv = '<div class="areaItemGroup">';
        pageNumDiv += '<div class="areaTitleGroup" >';
        pageNumDiv += '<a href="javascript:void(0)">' + pageNum+ '</a>';
        pageNumDiv += '</div>';
        box.append($(pageNumDiv));
        
        //坐标数组，设置最佳比例尺时会用到 
		var pointsArr = []; 
		this.oplayerMap.clearMarkerLayer();
    	for (var i = 0; i < list.length; i++) {
    		var imgUrl = "./public/style/images/query/noselect/"+(i+1)+".png";
            var item = list[i];
            //名称地址
            var index=i+1;
            var innelHTML=item.name+" "+item.address;
            //经纬度
        	var lnglatArr = item.lonlat.split(" "); 
        	pointsArr.push(item.lonlat);
        	var size = new OpenLayers.Size(29,30);
            var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
            var icon = new OpenLayers.Icon(imgUrl,size,offset);
            //地图添加点
            var marker = this.oplayerMap.addMarker(lnglatArr[0], lnglatArr[1], icon, {
                name : item.name
            },"markerLayer");
   
            var str = '<div class="areaItemGroup" id="' + item.hotPointID + 'Item">';
            str += '<div class="areaTitleGroup" id="' + item.hotPointID + '">';
            str += '<img src="'+imgUrl+'" />';
            str += '<a href="javascript:void(0)" title="'+innelHTML+'" id="' + item.hotPointID + '">' +innelHTML+ '</a>';
            str += '</div></div>';
            box.append($(str));
        }
    	//设置鼠标hover事件
    	$(".areaTitleGroup").bind("hover", this, this.hoverFunction);
    	//设置鼠标click事件
    	$(".areaTitleGroup").bind("click", this, this.clickFunction);
    	//设置地图最佳视野显示
    	this.mapUtils.setViewByPointListLonLat(pointsArr);
        //分页按钮
    	var strs='<div id="pageDiv">';
        strs +='<input type="button" id="firstPage" style="width:40px;margin-left:20px;" class="unquis" value="首页"   />';
        strs +='<input type="button" id="previousPage" class="unquis" value="上一页"   />';
        strs +='<input type="button" id="nextPage" class="unquis" value="下一页"   />';
        strs +='<input type="button" id="lastPage" style="width:40px;" class="unquis" value="尾页" />';
        strs +='</div>'
        box.append($(strs));
        //分页绑定点击事件
        $("#firstPage").bind("click", this, this.firstPageClick);
        $("#previousPage").bind("click", this, this.previousPageClick);
        $("#nextPage").bind("click", this, this.nextPageClick);
        $("#lastPage").bind("click", this, this.lastPageClick);
    };
    
    //地图高亮显示选中点
    this.addSelectPOI=function(id){
    	
    	var lnglatArr=null;
        var imgUrl=null;
        if(this.poiMarker!=null){
       	 	this.oplayerMap.removeMarkerLayer(this.poiMarker);
        }
        
        for (var i = 0; i < this.poiList.length; i++) {
	       	 var item=this.poiList[i];
	       	 var hotPointID = item.hotPointID;
	       	 if(id==hotPointID){
	       		 imgUrl = "./public/style/images/query/select/"+(i+1)+".png";
	       		 lnglatArr = item.lonlat.split(" "); 
	       		 break;
	       	 } 
        }
        
        if(lnglatArr!=null){
       	 var size = new OpenLayers.Size(34,38);
            var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
            var icon = new OpenLayers.Icon(imgUrl,size,offset);
            //地图添加点
            this.poiMarker = this.oplayerMap.addMarker(lnglatArr[0], lnglatArr[1], icon, {
                name : item.name
            });
        }
    };
    
    //鼠标hover事件，列表item高亮选中，地图高亮显示选中点
    this.hoverFunction=function(e){
    	 var thisObj = e.data;
    	 var target = e.currentTarget;
         var id = target.id;
         //列表item高亮选中
         $(".selectItem").removeClass("selectItem");
         $("#" + id+ "Item").addClass("selectItem");
         //地图高亮显示选中点
         thisObj.addSelectPOI(id); 
    };
    
   //鼠标click事件，地图居中显示
    this.clickFunction=function(e){
   	    var thisObj = e.data;
   	    var target = e.currentTarget;
        var id = target.id;
        //坐标数组，设置最佳比例尺时会用到 
		var pointsArr = []; 
        for (var i = 0; i < thisObj.poiList.length; i++) {
	       	 var item=thisObj.poiList[i];
	       	 var hotPointID = item.hotPointID;
	       	 if(id==hotPointID){
	       		 pointsArr.push(item.lonlat.split(" "));
	       		 break;
	       	 } 
        }
        var lonlat=new OpenLayers.LonLat(pointsArr[0],pointsArr[1]);
        var zoom  =thisObj.mapUtils.getZoomLevel();
        thisObj.mapUtils.setCenter(lonlat,zoom);
        //地图高亮显示选中点
        thisObj.addSelectPOI(id);
   };
    
    
    
    //第一页
    this.firstPageClick=function(e){
    	var thisObj = e.data;
    	thisObj.father.father.tdtSearcher.TDTSearch.firstPage();
    };
    
    //上一页
    this.previousPageClick=function(e){
    	var thisObj = e.data;
    	thisObj.father.father.tdtSearcher.TDTSearch.previousPage();
    };
    
    //下一页
    this.nextPageClick=function(e){
    	var thisObj = e.data;
    	thisObj.father.father.tdtSearcher.TDTSearch.nextPage();
    };
    
    //最后一页
    this.lastPageClick=function(e){
    	var thisObj = e.data;
    	thisObj.father.father.tdtSearcher.TDTSearch.lastPage();
    };
    
//-----------------------------------推荐城市显示----------------------------------------------------------------------    
    
    /***
     * 增加天地图查询结果展示内容（推荐城市）
     */
    var allCtiyList;
    this.addCityList = function(list,all) {
        this.clearList();
        allCtiyList=all;
        var box = $("#newTianDiTuList");
        if(list!=undefined&&list.length>0){
        	for (var i = 0; i < list.length; i++) {
                var item = list[i];
                var str = '<div class="areaItemGroup" id="' + item.adminCode + 'Item">';
                str += '<div class="areaTitleGroup" id="' + item.adminCode + '">';
                str += '<a href="javascript:void(0)" id="' + item.adminCode + '">' + item.name + '</a>';
                str += '<span>(' + item.count + ')</span></div></div>';
                box.append($(str));
            }
            $(".areaTitleGroup").bind("click", this, this.searchCity);
        }
        
        if(all!=undefined&&all.length>0){
        	var strs= '<div class="title" id="moreCity">';
            strs   += '<a href="javascript:void(0)" class="atitle">+ 更多城市</a>';
            strs   += '</div>';
            strs   += '<div class="scaleBox" id="moreList"></div>';
            box.append($(strs));
            $("#moreCity").bind("click", this, this.moreCityClick);
        }
    };
    
    /**
     * 更多城市点击事件
     */
    this.moreCityClick=function(){
    	var str= $(".atitle").html();
    	if(str=="+ 更多城市"){
    		$(".atitle").text("- 更多城市");
    		var box = $("#moreList");
    		for (var i = 0; i < allCtiyList.length; i++) {
                var item = allCtiyList[i];
                var str = '<div class="areaItemGroup" id="' + item.adminCode + 'Item">';
                str += '<div class="areaTitleGroup" id="' + item.adminCode + '">';
                str += '<a href="javascript:void(0)" id="' + item.adminCode + '">' + item.name + '</a>';
                str += '<span>(' + item.count + ')</span></div></div>';
                box.append($(str));
            }
    	}else{
    		$(".atitle").text("+ 更多城市");
    		 $("#moreList div").remove();
    	}
    }
    
    /***
     * 某城市点击事件
     */
    this.searchCity = function(e) {
        var thisObj = e.data;
        var target  = e.currentTarget;
        var id      = target.id;
        thisObj.father.father.tdtSearcher.tianDiTuCitySearch(id);
    };
	
//---------------------------------------初始化方法--------------------------------------------------

    this.init = function(def) {
        this.father     = def.father;
        this.oplayerMap = def.oplayerMap;
        this.mapUtils   = def.mapUtils;
    };
	
	
	this.init.apply(this, arguments);
}
