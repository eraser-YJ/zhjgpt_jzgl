/**
 * @Description 通用查询列表展示类 包含点类产品、4D图幅类产品、卫星影像类产品
 * @Author yuanJianHua
 * @CreateTime 2015年12月7日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var NormalResultListShow=function(){
	
//---------------------------------------相关属性--------------------------------------------------	
	
	 /**
	  * 基础地图类对象
	  */
	this.oplayerMap=null;
	 
	 /**
	  * 地图工具类对象
	  */
	this.mapUtils=null;
	
	/**
     * 列表信息LIST
     */
    this.listResult=null;
	
	/**
	 * 当前选择比例尺
	 */
    this.curScale=null;
    
    /**
	 * 当前聚合类型
	 */
    this.curGroupType=null;
    
    /**
     * 当前数据类型
     */
    this.curResType=0;
    
    /**
     * 记录各层级聚合类型 (provName 省级   cityName 市级  cntyName 县级)
     */
    this.groupObject=new Object();
    
    /**
     * 比例尺 详情信息是否显示开关
     */
    this.scale_A_onOff=false;
    
    /**
     * 几级元素点击事件
     */
    this.clickLevel=1;
    
    /**
     * 各类型显示名称配置参数
     */
    this.showNameArr={
    		"1":["点名","点号","等级","重力基准"],
    		"2":["点名","点号","等级","高程基准"],
    		"3":["点名","点号","等级","大地基准"],
			"4":["点名","点号","等级","网名"],
			"5":["新图号","生产时间","大地基准","数据格式"],
			"6":["新图号","格网间距","大地基准","数据格式"],
			"7":["新图号","分辨率","影像时间","大地基准"],
			"8":["摄区名称","航摄比例尺","分辨率","航摄起止时间"],
			"9":["卫星","缩略图","分辨率","影像时间"],
			"10":["新图号","出版时间","大地基准","数据格式"],
			"11":["新图号","出版时间","大地基准","高程基准"],
			"123":["成果单元名称","生产时间","大地基准","数据格式"]//地理实体
    };
    
    /**
     * 各类型显示属性配置参数
     */
    this.showCodeArr={
    		"1":["idName","idCode","idClass","crsVertCRS"],
			"2":["idName","idCode","idClass","crsVertCRS"],
			"3":["idName","idCode","idClass","crsDatum"],
			"4":["idName","idCode","idClass","idNet"],
			"5":["idNewMapNum","idFormData","crsDatum","dtFormatName"],
			"6":["idNewMapNum","idScaleDist","crsDatum","dtFormatName"],
			"7":["idNewMapNum","idScaleDist","TePosition","crsDatum"],
			"8":["idGranuleName","idEquScale","idScaleDist","teBeginEnd"],
			"9":["idSatellite","smallimage","idScaleDist","tePosition"],
			"10":["idNewMapNum","idFormData","crsDatum","dtFormatName"],
			"11":["idNewMapNum","idVersionType","crsDatum","crsVertCRS"],
			"123":["idMapTitle","idVersionType","crsDatum","crsVertCRS"]//地理实体
    };
    
    /**
     * 各类型显示宽度配置参数
     */
    this.showWidthArr={
    		"1":[86,76,77,77],
    		"2":[86,76,77,77],
    		"3":[86,76,77,77],
    		"4":[86,76,77,77],
    		"5":[86,76,77,77],
    		"6":[86,76,77,77],
    		"7":[86,76,77,77],
    		"8":[86,76,77,77],
    		"9":[86,76,77,77],
    		"10":[86,76,77,77],
    		"11":[86,76,77,77],
    		"123":[86,76,77,77]//地理实体
    };
    
    /**
     * 复选框已选数据
     */
    var locationVector=[];
	 
//---------------------------------------相关方法------------------------------------------
	
	   /**
	     * 清除列表内容(4D类)
	     */
	    this.clearListGroup = function() {
	        $("#scaleGroupList div").remove();
	    };

	    /***
	     * 清除某比例尺省级聚合结果(4D类)
	     */
	    this.clearProlList = function() {
	        $(".scaleProItem").remove();
	        $(".scaleProList").hide();
	    };

	    /***
	     * 清除某比例尺市级聚合结果(4D类)
	     */
	    this.clearCitylList = function() {
	        $(".scaleCityItem").remove();
	        $(".scaleCityList").hide();
	    };
	    
	    /***
	     * 清除某比例尺县级聚合结果(4D类)
	     */
	    this.clearCntylList = function() {
	        $(".scaleCntyItem").remove();
	        $(".scaleCntyList").hide();
	    };
	    
	    /**
	     * 清除卫星影像、点类聚合显示列表(卫星影像、点类)
	     */
	    this.cleanPointListGroup=function(){
	    
	    	$(".scaleItemGroup").remove();
//	    	$("#scaleGroupList").css('background','');
//			$("#scaleGroupList").next().css('display','none');
	    }
	    
	    /**
	     * 聚合条件参数重置
	     */
	    this.clearScaleListValue=function(){
	    	this.clickLevel=1;
	    	this.scale_A_onOff=false;
	    	this.curGroupType=null;
	    	this.curScale=null;
	    	this.groupObject=new Object();
	    	locationVector=[];
//	    	SearchTools.getInstance().setLookScale(null);
	    	SearchTools.getInstance().setLookGroupType(null);
	    }
	    
	    /**
	     * 获取聚合类型(provName 省级   cityName 市级  cntyName 县级)
	     */
	    this.getGroupType=function(level){
	    	var groupName=this.groupObject[level];
	    	var result=null;
	    	switch(groupName){
	    	case "provName":
	    		result="cityName";
	    		break;
	    	case "cityName":
	    		result="cntyName";
	    		break;
	    	case "cntyName":
	    		result="cntyName";
	    		break;
	    	}
	    	return result;
	    }
	    
	    /**
	     * 获取聚合类型(provName 省级   cityName 市级  cntyName 县级)
	     */
	    this.getGroupTypeByName=function(groupName){
	    	var result=null;
	    	switch(groupName){
	    	case "provName":
	    		result="cityName";
	    		break;
	    	case "cityName":
	    		result="cntyName";
	    		break;
	    	case "cntyName":
	    		result="cntyNames";
	    		break;
	    	}
	    	return result;
	    }
	    
//---------------------------------------卫星影像、点类聚合展示------------------------------------------
        
	    /**
	     * 显示点类聚合列表内容
	     */
	    this.addPointListGroup=function(data){
	    	$('#scaleGroupBoxTitle').text('行政区域聚合');
	    	//区域聚合结果
	    	var regionGroupList=data.groupRegionList;
	    	//当前聚合类型
	    	var typeGroup      =data.curGroupType;
	    	this.curGroupType  =typeGroup;
	    	//当前区域名
	    	var curRegionName  =data.curRegionName;
	    	
	    	switch(typeGroup) {
	    		
	    		case "provName":
	    			this.addProvList({groupsList:regionGroupList});
	    			break;
	    		case "cityName":
	    			this.addCityList({groupsList:regionGroupList,curRegionName:curRegionName});
	    			break;
	    		case "cntyName":
	    			this.addCntyList({groupsList:regionGroupList,curRegionName:curRegionName});
	    			break;
    	    }
	    }
	    
	    /**
	     * @TODO 添加省级
	     */
	    this.addProvList = function(obj){
	    	//list
	    	var array = obj.groupsList;
	    	var box = $("#scaleGroupList");
	    	var curGroupType=this.curGroupType;
	    	var str = '';
	    	//拼接
	        for (var i = 0; i < array.length; i++) {
	            var item = array[i];
	            str += '<div class="scaleItemGroup" id="' + item.pac + 'ItemGroup">';
	            str += '<div class="scaleTitleGroup" is="no" res='+curGroupType+' id="' + item.pac + '">';
	            str += '<a href="javascript:void(0)" id="' + item.pac + '">' + item.name + '</a>';
//	            str += '<span>(' + item.count + ')</span>';
	            str += '</div>';
	            str += '<div class="scaleProList hidden" id="' + item.pac + 'List"></div></div>';
	           
	        }
	        box.html(str);
	        //省级元素点击事件
	        $(".scaleTitleGroup").bind("click", this, this.regionClick);
	    }
	    
	    /**
	     * @TODO 添加市级
	     */
	    this.tempGroup=null;
	    this.addCityList = function(obj){
	    	//list
	    	var array = obj.groupsList;
	    	if(document.getElementById(obj.curRegionName + "List")==undefined){
	    		this.tempGroup="cityName";
	    		this.addProvList(obj);
	    	}else{
	    		this.tempGroup=null;
	    		$("#" + obj.curRegionName + "List div").remove();
	    		var box = $("#" + obj.curRegionName + "List");
	    		var curGroupType=this.curGroupType;
		    	box.show();
		    	var str = '';
		    	for (var i = 0; i < array.length; i++) {
		            var item = array[i];
		        	str+='<div class="scaleProItem" id="' + item.pac + 'Item" >';
		            str+='<div class="scaleTitlePro" is="no" res='+curGroupType+' id="' + item.pac + '">';
		            str+='<a href="javascript:void(0)" id="' + item.pac + '">' + item.name + '</a>';
//		            str+='<span>('+item.count+ ')</span>';
		            str+='</div>';
		            str+='<div class="scaleCityList hidden" id="' +item.pac+'List"></div></div>';
		        }
		    	box.html(str);
		    	$('.scaleTitlePro').bind("click", this, this.regionClick);
	    	}
	    }
	    
	    /**
	     * @TODO 添加县级
	     */
	    this.addCntyList = function(obj){
	    	//list
	    	var array = obj.groupsList;
	    	if(document.getElementById(obj.curRegionName + "List")==undefined){
	    		this.tempGroup="cntyName";
	    		this.addCityList(obj);
	    	}else{
	    		this.tempGroup=null;
	    		$("#" + obj.curRegionName + "List div").remove();
				var box = $("#" + obj.curRegionName + "List");
				var curGroupType=this.curGroupType;
		    	var str = '';
		    	for (var i = 0; i < array.length; i++) {
		            var item = array[i];
		            str	+='<div class="scaleCityItem" id="' + item.pac + 'SItem" >';
		            str    +='<div class="scaleTitleCity" is="no" res='+curGroupType+' id="' + item.pac + '">';
		            str    +='<a href="javascript:void(0)">' + item.name + '</a>';
//		            str    +='<span>('+item.count+ ')</span>';
		            str    +='</div>';
		            str    +='<div class="scaleCntyList hidden" id="' +item.pac+'List"></div></div>';
		           
		        }
		    	box.show();
		    	box.html(str);
		    	$('.scaleTitleCity').bind("click", this, this.regionClick);
			}
	    }
	    
	    /**
	     * @TODO 区域点击
	     */
	    this.regionClick = function(e){
	    	
	    	var scaleList = e.data;
	    	//点击的区域名称
	    	var curRegionName = $(this).attr('id');
	    	//当前点击的级别
	    	var curGroupName =  $(this).attr('res');
	    	//判断是否点击
	    	var is = $(this).attr('is');
	    	
	        if (is=='yes') {
	           $(this).css('background','');
	           $(this).next().css('display','none');
	           $(this).attr('is','no');
	        } else {
	        	$(this).attr('is','yes');
	        	if(curGroupName=="provName"){
	        		$(".selectProItem").css('background','');
					$(".selectProItem").next().css('display','none');
					$(this).addClass("selectProItem");
	        		$(this).css('background','#519dde');
	        	}else if(curGroupName=="cityName"){
	        		$(".selectCityItem").css('background','');
					$(".selectCityItem").next().css('display','none');
					$(this).addClass("selectCityItem");
	        		$(this).css('background','#909090');
	        	}else{
	        		$(".selectCntyItem").css('background','');
					$(".selectCntyItem").next().css('display','none');
					$(this).addClass("selectCntyItem");
	        		$(this).css('background','#F0F0F0');
	        	}
		    	var groupName=scaleList.getGroupTypeByName(curGroupName);
		    	if(groupName==null){
		    		return;
		    	}
	        	SearchTools.getInstance().setLookGroupType(groupName);
	        	SearchTools.getInstance().setLookRegionName(curRegionName);
	        	scaleList.father.father.look();
	        }
	    }
	    
	    
	    
//---------------------------------------4D类聚合展示------------------------------------------
	 
	    /***
	     *显示4D类聚合列表内容
	     */
	    this.add4DListGroup = function(data) {
	    	$('#scaleGroupBoxTitle').text('比例尺聚合');
	    	//比例尺聚合结果
	    	var scaleGroupList =data.groupScaleList;
	    	//区域聚合结果
	    	var regionGroupList=data.groupRegionList;
	    	//当前聚合类型
	    	var typeGroup      =data.curGroupType;
	    	this.curGroupType  =typeGroup;
	    	//当前比例尺
	    	var curScale       =data.curScale;
	    	//当前区域名
	    	var curRegionName  =data.curRegionName;
	    	
	    	switch(this.clickLevel){
	    	   case 1://比例尺
	    		   this.groupObject[1]=typeGroup;
	    		   this.addOneList(scaleGroupList,regionGroupList,curScale,curRegionName);
	    		   break;
	    	   case 2://省
	    		   this.groupObject[2]=typeGroup;
	    		   this.addTwoList(regionGroupList,curRegionName,typeGroup);
	    		   break;
	    	   case 3://市
	    		   this.groupObject[3]=typeGroup;
	    		   this.addThreeList(regionGroupList,curRegionName,typeGroup);
	    		   break;   
	    	   case 4://县
	    		   this.groupObject[4]=typeGroup;
	    		   this.addFourList(regionGroupList,curRegionName,typeGroup);
	    		   break;  
	    	}
	    };
	    
	    /**
	     * 增加比例尺聚合信息
	     */
	    this.addOneList=function(scaleGroupList,regionGroupList,curScale,curRegionName){
	    	
	    	this.clearListGroup();
	        var box = $("#scaleGroupList");
	        for (var i = 0; i < scaleGroupList.length; i++) {
	            var item = scaleGroupList[i];
	            var obj = SearchTools.getInstance().thScaleMap[item.name];
	            if (!obj)
	                continue;
	            var name = obj.name;
	            var str = '<div class="scaleItemGroup" id="' + item.name + 'ItemGroup">';
	            str += '<div class="scaleTitleGroup" id="' + item.name + '">';
	            str += '<a href="javascript:void(0)" id="' + item.name + '">' + name + '</a>';
	            str += '<span>(' + item.count + ')</span>';
	            str += '</div>';
	            str += '<div class="scaleProList hidden" id="' + item.name + 'ListGroup"></div></div>';

	            box.append($(str));
	        }
	        this.addProResultList(regionGroupList,curScale,curRegionName);
	        //一级元素点击事件
	        $(".scaleTitleGroup").bind("click", this, this.OneClickFunction);
	    }

	    /***
	     * 一级元素点击事件
	     */
	    this.OneClickFunction = function(e) {
	    	var scaleList = e.data;
	    	scaleList.clickLevel=1;
	        var target = e.currentTarget;
	        var scale = target.id;
	        //当前选择比例尺
	        scaleList.curScale=scale;
	        //清除之前显示内容
	        scaleList.clearProlList();

	        var $scaleItem = $(e.currentTarget.parentElement);
	        var flag = $scaleItem.hasClass("selectItem");
	        if (flag) {
	        	//比例尺取消选中
	            $scaleItem.removeClass("selectItem");
	            $scaleItem.css("height", "");
            	scaleList.curGroupType=null;
            	SearchTools.getInstance().setLookGroupType(null);
            	SearchTools.getInstance().setLookRegionName(null);
            	SearchTools.getInstance().setLookScale(scale);
//            	scaleList.father.father.look();
	        } else {
	        	scaleList.curGroupType=null;
	        	SearchTools.getInstance().setLookGroupType(null);
	        	SearchTools.getInstance().setLookRegionName(null);
	        	SearchTools.getInstance().setLookScale(scale);
	        	scaleList.father.father.look();
	        }
	    };
	    
	    /***
	     * 一级元素点击事件回调函数
	     */
	    this.addProResultList = function(data,curScale,curRegionName) {
	        this.clearProlList();
	        this.curScale=curScale;
	        var box = $("#" + curScale + "ListGroup");
	        box.show();
	        var list=data;
	        this.resultList=list;
	        $(".selectItem").css("height", "");
	        $(".selectItem").removeClass("selectItem");
	        //高亮显示选择比例尺
	        $("#" + curScale + "ItemGroup").addClass("selectItem");   
	        if(curScale!="A"){
	        	for (var i = 0; i < list.length; i++) {
	                var item = list[i];
	                var str ='<div class="scaleProItem"    id="' + item.pac + 'Item" >';
	                    str+='<div class="scaleTitlePro"   id="' + item.pac + '">';
	                    str+='<a href="javascript:void(0)" id="' + item.name + '">' + item.name + '</a>';
//	                    str+='<span>('+item.count+ ')</span>';
	                    str+='</div>';
	                    str+='<div class="scaleCityList hidden" id="' +item.pac+'List"></div></div>';
	                box.append($(str));
	            }	
	        }
	        //二级元素点击事件
	        $(".scaleTitlePro").bind("click", this, this.TwoClickFunction);
	    };
	    
	    /**
	     * 二级元素点击事件
	     */
	    this.TwoClickFunction = function(e){
	    	var scaleList = e.data;
	    	scaleList.clickLevel=2;
	        var target    = e.currentTarget;
	        var regionPac = target.id;
	        
	        scaleList.clearCitylList();
	    	
	        var $scaleItem = $(e.currentTarget.parentElement);
	        var flag = $scaleItem.hasClass("selectProItem");
	        if (flag) {
	            $scaleItem.removeClass("selectProItem");
	            $scaleItem.css("height", "");
	        } else {
	        	//聚合类型
	        	var groupType=scaleList.getGroupType(1);
	        	//设置聚合类型
	        	SearchTools.getInstance().setLookGroupType(groupType);
	        	//设置区域名
	        	SearchTools.getInstance().setLookRegionName(regionPac);
	        	//设置比例尺
	        	SearchTools.getInstance().setLookScale(scaleList.curScale);
	        	//4D产品查看
	        	scaleList.father.father.look();
	        }
	    };
	    
	    /***
	     * 二级元素点击事件回调函数
	     */
	    this.addTwoList = function(regionGroupList,curRegionName,typeGroup) {
	        
	    	this.clearCitylList();
	    	//高亮显示选择比例尺
	    	var box = $("#" + curRegionName + "List");
	        var list = regionGroupList;
	        this.resultList = list;
	        box.show();
	        $(".selectProItem").css("height", "");
	        $(".selectProItem").removeClass("selectProItem");
	        $("#" + curRegionName + "Item").addClass("selectProItem");
	        if(typeGroup=="cntyName"&&list.length==1){
	        	var temp=list[0].pac;
	        	if(temp==curRegionName){
	        		return;
	        	}
	        }
	    	for (var i = 0; i < list.length; i++) {
	            var item = list[i];
	            var str ='<div class="scaleCityItem"  id="' + item.pac + 'SItem" >';
	            str    +='<div class="scaleTitleCity" id="' + item.pac + '">';
	            str    +='<a href="javascript:void(0)">' + item.name + '</a>';
//	            str    +='<span>('+item.count+ ')</span>';
	            str    +='</div>';
	            str    +='<div class="scaleCntyList hidden" id="' +item.pac+'List"></div></div>';
	            box.append($(str));
	        }
	        //三级元素点击事件
	        $(".scaleTitleCity").bind("click", this, this.ThreeClickFunction);
	    };
	    
	    /**
	     * 三级元素点击事件(市)
	     */
	    this.ThreeClickFunction = function(e){
	    	var scaleList = e.data;
	    	scaleList.clickLevel=3;
	        var target  = e.currentTarget;
	        var cityPac = target.id;
	        scaleList.clearCntylList();
	        var $scaleItem = $(e.currentTarget.parentElement);
	        var flag = $scaleItem.hasClass("selectCityItem");
	        if (flag) {
	            $scaleItem.removeClass("selectCityItem");
	            $scaleItem.css("height", "");
	        }else{
	        	
	        	//聚合类型
	        	var groupType=scaleList.getGroupType(2);
	        	//设置聚合类型
	        	SearchTools.getInstance().setLookGroupType(groupType);
	        	//设置区域名
	        	SearchTools.getInstance().setLookRegionName(cityPac);
	        	//设置比例尺
	        	SearchTools.getInstance().setLookScale(scaleList.curScale);
	        	//4D产品查看
	        	scaleList.father.father.look();
	        }    
	    };
	    
	    /***
	     * 三级元素点击事件回调函数(市)
	     */
	    this.addThreeList = function(regionGroupList,curRegionName,typeGroup) {
	    	
	    	this.clearCntylList();       
	        //高亮显示选择比例尺
	    	var box = $("#" + curRegionName + "List");
	    	//去除省市重名问题
	    	var count=box.length;
			if(count>1){
				box=box.eq(0);
			}
	        var list = regionGroupList;
	        this.resultList = list;
	        box.show();
	        $(".selectCityItem").css("height", "");
	        $(".selectCityItem").removeClass("selectCityItem");
	        $("#" + curRegionName + "SItem").addClass("selectCityItem");
	        if(typeGroup=="cntyName"&&list.length==1){
	        	var temp=list[0].pac;
	        	if(temp==curRegionName){
	        		return;
	        	}
	        }
	    	for (var i = 0; i < list.length; i++) {
	            var item = list[i];
	            var str ='<div class="scaleCntyItem"  id="' + item.pac + 'SSItem" >';
	            str    +='<div class="scaleTitleCnty" id="' + item.pac + '">';
	            str    +='<a href="javascript:void(0)">' + item.name + '</a>';
//	            str    +='<span>(' + item.count + ')</span>';
	            str    +='</div></div>';
	            box.append($(str));
	        }
	        //四级元素点击事件
	        $(".scaleTitleCnty").bind("click", this, this.FourClickFunction);
	    };
	    
	    /**
	     * 四级元素点击事件(县)
	     */
	    this.FourClickFunction = function(e){
	    	var scaleList = e.data;
	    	scaleList.clickLevel=4;
	        var target  = e.currentTarget;
	        var cntyPac = target.id;
	        var $scaleItem = $(e.currentTarget.parentElement);
	        
	        var flag = $scaleItem.hasClass("selectCntyItem");
	        if (flag) {
	            $scaleItem.removeClass("selectCntyItem");
	            $scaleItem.css("height", "");
	        }else{
	        	//聚合类型
	        	var groupType=scaleList.getGroupType(3);
	        	//设置聚合类型
	        	SearchTools.getInstance().setLookGroupType(groupType);
	        	//设置区域名
	        	SearchTools.getInstance().setLookRegionName(cntyPac);
	        	//设置比例尺
	        	SearchTools.getInstance().setLookScale(scaleList.curScale);
	        	//4D产品查看
	        	scaleList.father.father.look();
	        }    
	    };
	    
	    /***
	     * 四级元素点击事件回调函数
	     */
	    this.addFourList = function(regionGroupList,curRegionName,typeGroup) {
	    	
	        var list = regionGroupList;
	        this.resultList = list;
	        $(".selectCntyItem").css("height", "");
	        $(".selectCntyItem").removeClass("selectCntyItem");
	        $("#" + curRegionName + "SSItem").addClass("selectCntyItem");
	    };

//---------------------------------------详情列表展示------------------------------------
	    
	    
	  //其他操作信息配置
	    this.otherDo = {
	    		imgSrc:"./public/style/images/",
				imgData:{magnifierBlue:"iconBrc.png",shopCarIcon:"shopCarIcon.png"},
				imgTitle:{magnifierBlue:"详细信息",shopCarIcon:"加入成果车"}
		};
	    
	    /**
	     * 具体信息显示
	     */
	    this.detialListShow = function(list,restype,curScale){
	    	this.curResType=restype;
//	    	if($(".searchInput").val() != ""){
//	    		var num = parseInt(($(".mapInfo").height() -80 -31 -41 -41)/31);//根据高度 动态计算每页显示条数
//	    	}else{
//	    		var num = parseInt(($(".mapInfo").height() -80 -31 -41)/31);//根据高度 动态计算每页显示条数
//	    	}
	    	var num=list.length;
	    	$(".bbRightText>span").text("共"+num+"条");
	    	//上下页信息配置
	        this.pageData = {
	        		nowPage:0,//当前页 0开始
	        		pageNum:num,//每页显示数目
	        		pageFlag:false,//不提供分页
	        		pageClass:{
	    				back:'<div class="z-back">返回</div>',//返回html
	    				first:'<div class="z-first page" num="0">首页</div>',//首页html
	    				last:'<div class="z-last page" num="-1" style="">上一页</div>',//上一页html
	    				next:'<div class="z-next page" num="-1" style="">下一页</div>',//下一页html
	    				zuihou:'<div class="z-zuihou page" num="0">末页</div>',//末页html
	    				go:'<div class="z-go"><input type="text" value="1"><span>GO</span></div>',//跳转页html
	    				paging:'<div class="z-paging"></div>'//分页信息 html
	    			}
	        };
	    	this.totleWidth = 410;
	    	this.notSortList = list;//未排序的list,每次排序时调用这个list,然后赋值给listResult
	    	
	    	this.listResult = sot(list,this.showCodeArr[restype][0],"up");
	    	this.showName=this.showNameArr[restype];//显示表名
	    	this.showCode=this.showCodeArr[restype];//显示表字段
	    	this.showWidth=this.showWidthArr[restype];//显示字段宽度
	    	this.sortInfo = {sortCode:this.showCodeArr[restype][0],sortDirection:"↓"};//排序信息 sortCode排序字段 sortDirection排序方向
	    	this.showDiv = "scaleListBox";
	    	this.postion =null;
	    	this.blueId = null;//记录标蓝的id
	    	this.addList();
	    }
	    
	    /**
	     * 添加列表
	     */
	    this.addList=function(){
	    	$("#"+this.showDiv).html("");
	    	locationVector = [];
	    	for(var j=0;j<locationVector.length;j++){
	    		var item=locationVector[j];
	    		this.unShowSlectGeom(item.id);
	    	}
	    	var list = this.listResult;
	    	var SNArr = this.showName;//显示的表头数组
			var SCArr = this.showCode;//显示的字段数组
			var SWArr = this.showWidth;//显示的字段宽度数组
			var nowPage = this.pageData["nowPage"];//当前页  从0开始
			var pageNum = this.pageData["pageNum"];//每页显示条数
			var hidNum = nowPage*pageNum;//跳过条数
			var sortCode = this.sortInfo["sortCode"];
			var sortDirection = this.sortInfo["sortDirection"];
			var lastNum = (nowPage+1)*pageNum;//显示条数位置
			var totleNum = list.length;//总数据条数
			//创建html
			var $zList = $('<div class="z-list" style="width:'+this.totleWidth+'px;"></div>');
			
			/*******处理其他信息********/
			var text="版本选择";
			if(this.curResType=="8"){
				text="航摄年份";
			}
			var $idVersion = $('<div class="idVersion"></div>');
			$idVersion.append('<div class="box-bound"><div class="version-box">'+text+'</div>'
					 +'<div class="version-list"></div></div>');
			
			//版本选择是否可见
			if(!(this.curResType == "5" || this.curResType == "6" || this.curResType == "7"||this.curResType == "8")){
				$idVersion.find(".box-bound").hide();
			}
			$idVersion.append('<div class="z-list-tRight">'
					+'<img src="'+this.otherDo["imgSrc"]+'shopCarIcon.png">'
					+'<div class="text">加入成果车</div>');
			$idVersion.append('<div class="newBack">'
					+'<img src="'+this.otherDo["imgSrc"]+'back.png">'
					+'<div class="text">返回上一级</div>');
			$zList.append($idVersion);
			
			//处理列表头部
			var doWidth = this.totleWidth -30;
			var $title = $('<div class="z-list-show z-list-title"></div>');
			$title.append('<div class="wSel r-border all"><div class="z-list-checkbox"></div></div>');
			for(var i = 0 ; i < SNArr.length; i ++){
				var code = SCArr[i];
				if(code == sortCode){//list是此排序 显示方向  由样式map1.css 中hasSort控制
					var str = '<div class="r-border hasSort" style="width:'+(SWArr[i]-1)+'px;height:30px;" code="'+code+'" isSort="1">'+SNArr[i]+'<span>'+sortDirection+'</span></div>';
				}else{//list不是此排序 不显示方向 由样式map1.css 中notSort控制
					var str = '<div class="r-border notSort" style="width:'+(SWArr[i]-1)+'px;height:30px;" code="'+code+'" isSort="0">'+SNArr[i]+'<span>↓</span></div>';		
				}
				$title.append(str);
				doWidth -= SWArr[i];
			}
			var str = '<div class="wdo" style="width:'+doWidth+'px;">其他操作</div>';
			$title.append(str);
			$zList.append($title);

			/*表格内容*/
			var $gridTable = $('<div class="gridTable"></div>');	
			var imgData = this.otherDo["imgData"];
			var imgTitle = this.otherDo["imgTitle"];
			var imgHtml = '<div class="wdo" style="width:'+doWidth+'px;">';
			for(key in imgData){
				imgHtml += '<a class="'+key+'" style="background-image:url(\''+this.otherDo["imgSrc"]+imgData[key]+'\')" title="'+imgTitle[key]+'"></a>'
			}
			imgHtml += '</div>';
			for(var i = hidNum; i < totleNum && i < lastNum;i++){
				var that = list[i];
				var $thatLi = $('<div id="list'+i+'" class="z-list-show" id="'+that["id"]+'"></div>');
				if(_isSelect(that["id"])){
					$thatLi.append('<div class="wSel r-border one" id="'+that["id"]+'"><div class="z-list-checkbox">√</div></div>');
				}else{
					$thatLi.append('<div class="wSel r-border one" id="'+that["id"]+'"><div class="z-list-checkbox"></div></div>');
				}
				
				for(var j = 0 ; j < SCArr.length ;j++){
					var txt = that[SCArr[j]];
					txt = typeof(txt) == "undefined" ? "" : txt;
					
					//格网间距、分辨率加上单位
					if(SCArr[j]=="idScaleDist"){
						if(txt!=null&&txt!=""){
							txt=txt+"m";
						}
					}
					
					if(j == 0){
						var str = '<div class="r-border listCel" style="width:'+(SWArr[j]-1)+'px;height:30px;">'+txt+'</div>';
					}else{
						//添加缩略图
						if(SCArr[j]=="smallimage"){
							var storeId=that.storeId;
							if(that.smallimageFlag){
								var url=that.smallimage;
								var str = '<div class="r-border smallimage" style="width:'+(SWArr[j]-1)+'px;height:30px;" title="'+txt+'"  urls="'+url+'"><img title="卫星影像缩略图" onerror="mapPage.listManager.normalListObj.onerror(this)" src="'+url+'" style="cursor:pointer;"/></div>';
								
							}else if(storeId=="3"){//卫星测绘应用中心
								var str = '<div class="r-border smallimage" style="width:'+(SWArr[j]-1)+'px;height:30px;" title="'+txt+'"  urls="http://sjfw.sasmac.cn/product/meta.imager?flag=1&metaId='+that.idGranuleID+'"><img title="卫星影像缩略图" onerror="mapPage.listManager.normalListObj.onerror(this)" src="http://sjfw.sasmac.cn/product/meta.imager?flag=1&metaId='+that.idGranuleID+'" style="cursor:pointer;"/></div>';
							}else if(storeId=="92"){
								var url ="./weixipicture/"+that.idGranuleID+".jpg";
								var str = '<div class="r-border smallimage" style="width:'+(SWArr[j]-1)+'px;height:30px;" title="'+txt+'"  urls="'+url+'"><img title="卫星影像缩略图" onerror="mapPage.listManager.normalListObj.onerror(this)" src="'+url+'" style="cursor:pointer;"/></div>';
							}else{
								var url="./weixipicture/"+that.idBgFileName;
								var str = '<div class="r-border smallimage" style="width:'+(SWArr[j]-1)+'px;height:30px;" title="'+txt+'"  urls="'+url+'"><img title="卫星影像缩略图" onerror="mapPage.listManager.normalListObj.onerror(this)" src="'+url+'" style="cursor:pointer;"/></div>';
							}
//							var str = '<div class="r-border smallimage" style="width:'+(SWArr[j]-1)+'px;height:30px;" title="'+txt+'"  urls="http://sjfw.sasmac.cn/product/meta.imager?flag=1&metaId='+that.idGranuleID+'"><img title="卫星影像缩略图" onerror="mapPage.listManager.normalListObj.onerror(this)" src="http://sjfw.sasmac.cn/product/meta.imager?flag=1&metaId='+that.idGranuleID+'"/></div>';
						}else{
							var str = '<div class="r-border listCel" style="width:'+(SWArr[j]-1)+'px;height:30px;" title="'+txt+'">'+txt+'</div>';
						}
					}
					$thatLi.append(str);
				}
				
				var elem = $(imgHtml);
				elem.find("a").attr("id",that["id"]);
				$thatLi.append(elem);
				$gridTable.append($thatLi);	
			}
			var otherHeight = 61;
			if($(".searchInput").val() != ""){//如果要显示图册图集的查询 
				otherHeight += 30;
			}
			if(pageNum *31 + otherHeight > $(".mapInfo").height()){//处理超过一定高度是,显示滚动条
				$gridTable.css("overflow-y","scroll");
				$gridTable.css("overflow-x","hidden");
				
				var height = $(".mapInfo").height() - otherHeight;
				$gridTable.height(height);
				var totleWidth = this.totleWidth;
				$gridTable.find(".z-list-show").each(function(){
					$(this).width(totleWidth-8);
				});
				$gridTable.find(".wdo").each(function(){
					$(this).width(56);
				});
			}
			$zList.append($gridTable);
			
			//处理分页信息
			if(this.pageData["pageFlag"]){
				var $zcounter = $('<div class="z-counter"><div>');
				var $btnLi = $('<div class="z-list-btn"></div>');
				var pageClass = this.pageData["pageClass"];
				for(var pKey in pageClass){
					$btnLi.append(pageClass[pKey]);//载入分页html
				}
				$zcounter.append($btnLi);
				var totlePage = parseInt(totleNum/pageNum);
				if(totlePage*pageNum == totleNum){
					totlePage--;
				}
				if(nowPage != 0){//存在上一页 则设置上一页数字
					$btnLi.find(".z-last").attr("num",nowPage-1);
				}
				if(totlePage != nowPage){//存在下一页 则设置下一页数字
					$btnLi.find(".z-next").attr("num",nowPage+1);
				}
				$btnLi.find(".z-zuihou").attr("num",totlePage);//设置末页数字
				$btnLi.find(".z-go").find("input").val(nowPage+1);//设置末页数字
				$btnLi.find(".z-paging").text("共"+(totlePage+1)+"页");//设置总页数	
				this.totlePage = totlePage+1;
				$zList.append($zcounter);		
			}

			$("#"+this.showDiv).html($zList);
			
			//处理进入图册的链接
			if($(".searchInput").val() != ""){//输入了字段查询
				var $zcounter1 = $('<div class="z-counter1"></div>');
				var $btnli1 = $('<div class="z-list-btn"></div>');
				$btnli1.append('<span class="in-text">当前有 <span id="proSearchNum" style="color:red;">0</span> 无空间分布,查看详情请点击</span><span class="in-btn">进入</span>');
				$zcounter1.append($btnli1);
				$zList.append($zcounter1);
				this.showProNum($(".searchInput").val());
			}
			
			//缩略图点击事件
			$(".smallimage").bind("click", this, this.showImageClick);
	    	//详情信息点击事件
		    $(".magnifierBlue").bind("click", this, this.show4DDetailClick);
		    //加入购物车点击事件
		    $(".shopCarIcon").bind("click", this, this.addShopCarClick);
		    //上下页点击事件
		    $(".page").bind("click", this, this.page);
		    //跳转点击事件
		    $(".z-go span").bind("click", this, this.toThePage);
		    //返回按钮点击
		    $(".newBack").bind("click",this, this.returnClick);
		    //批量加入购物车事件
		    $(".z-list-tRight").bind("click", this, this.addAllShopCarClick);
		    //全选绑定
		    $(".all").bind("click",this,this.selectAll);
		    //单个复选框绑定
		    $(".one").bind("click",this,this.showSelectClick);
		    //双选
		    $(".listCel").bind("click",this,this.listCelDbClick);
		    //表头点击 排序  并显示
		    $(".hasSort,.notSort").bind("click",this,this.sortAndShow);
		    //鼠标移动 显示排序方向
		    $(".hasSort,.notSort").bind("hover",function(){
		    	return;
		    	var isSort = $(this).attr("isSort");
		    	if(isSort == 0){
		    		var $span = $(this).find("span").eq(0);
		    		var visi = $span.css("visibility");
		    		if(visi == "hidden"){
		    			$span.css("visibility","visible");
		    		}else{
		    			$span.css("visibility","hidden");
		    		}
		    	}
		    });
		  //进入点击事件
		    //$(".in-btn").bind("click",this,this.storeProduct);
		    this.initEd();
		    this.checkIsAll();
	    }
	    
	    //ajax获取产品的数量
	    this.showProNum = function(keyWord){
	    	return;
	    	$.post("./store.do?method=ajaxCountProduct",{keyWord:keyWord},function(data){
	    		var obj = JSON.parse(data);
	    		if(obj.num > 0){
	    			$("#proSearchNum").html(obj.num);
		    		$(".z-counter1").show();
	    		}
	    	},"text");
	    }
	    /**
	     * 进入按钮点击事件
	     */
	    this.storeProduct=function(e){
	    	return;
	    	if(storeID==null){
	    		var url = "./store.do?method=storeProduct&key="+$(".searchInput").val();
	    	}else{
	    		var url = "./store.do?method=storeProduct&storeId="+storeID+"&key="+$(".searchInput").val();
	    	}
	    	
	    	window.open(url);
	    }
	    
	    /**
	     * 缩略图片加载失败事件
	     */
	    this.onerror = function(e){
	    	var clazz = $(e).attr('class');
	    	var imgs = './public/style/images/nopic.gif';
	    	$(e).attr('src',imgs).parent().attr("urls","");
	    }
	    
	    /**
	     * 缩略图点击事件
	     */
	    this.showImageClick=function(e){
	    	
	    	var thi$ = $(e.currentTarget);
	    	var urls = thi$.attr("urls");
	    	if(urls.length !== 0){
	    		window.open(urls);
	    	}
	    }
	    
	    //初始化版本
	    this.initEd = function(){
	    	var obj = this.father.father.filterResCont;
	    	if(obj){
	    		html = '';
	    		for(key in obj) {
	    			html += '<p>'+key+'</p>'
	    		}
	    		html += '<p>清除</p>'
	    		$('.version-list').append(html);
	    		if(this.father.father.filterClick&&this.father.father.filterClick!='清除'){
	    			$('.version-box').text(this.father.father.filterClick);
	    		}else{
	    			var text="版本选择";
	    			if(this.curResType=="8"){
	    				text="航摄年份";
	    			}
	    			$('.version-box').text(text);
	    		}	
	    	}
	    }
	    
	    /**
	     * 表头排序点击事件
	     */
	    this.sortAndShow = function(e){
	    	$("#"+this.showDiv).html("");
	    	$(".hasSort,.notSort").unbind("hover");
	    	var scaleList = e.data;
	    	var thi$ = $(e.target);
	    	var isSort = thi$.attr("isSort");
	    	var code = thi$.attr("code");
	    	var dire = thi$.find("span").eq(0).text();
	    	var list = scaleList.notSortList;
	    	if(dire == "↑"){
    			dire = "↓";
    			list = sot(list,code,"up");
    		}else{
    			dire = "↑";
    			list = sot(list,code,"down");
    		}
//	    	if(isSort == "1"){
//	    		if(dire == "↑"){
//	    			dire = "↓";
//	    			list = sot(list,code,"down");
//	    		}else{
//	    			dire = "↑";
//	    			list = sot(list,code,"up");
//	    		}
//	    	}else{
//	    		list = sot(list,code,"down");
//	    	}
	    	scaleList.pageData["nowPage"] = 0;
	    	scaleList.listResult = list;
	    	scaleList.sortInfo["sortCode"] = code ;
	    	scaleList.sortInfo["sortDirection"] = dire ;
	    	scaleList.addList();
	    }
	    
	    /**
	     * list排序
	     */
	    function sot(data,parm,b){
			var down = function (x, y) {
				return (eval("x." + parm) > eval("y." + parm)) ? -1 : 1;
			}
			var up = function (x, y) {
				return (eval("x." + parm) < eval("y." + parm)) ? -1 : 1;
			}
			if(b == "down") {
				data.sort(down);
			}
			else{
				data.sort(up);
			}
			return data;
		}
	    
	  
	    
	    /**
	     * 全选点击事件
	     */
	    this.selectAll=function(e){
	    	var $all = $(".all").find(".z-list-checkbox");
	    	var scaleList = e.data;
	    	if($all.text().length == 0){
	    		$all.text("√");
	    		$(".one").each(function(){
	    			var $thisOne = $(this).find(".z-list-checkbox");
	    			if($thisOne.text().length == 0){
	    				$thisOne.text("√");
	    				var id=$(this).attr("id");
	    				scaleList.showSlectGeom(id);
	    			}
	    		});
	    	}else{
	    		$all.text("");
	    		$(".one").each(function(){
	    			var $thisOne = $(this).find(".z-list-checkbox");
	    			if($thisOne.text().length != 0){
	    				$thisOne.text("");
	    				var id=$(this).attr("id");
	    				scaleList.unShowSlectGeom(id);
	    			}
	    		});
	    	}
	    }    
	    
//-------------------------------行单击事件---------------------------------------	    
	    
	    /**
	     * 根据ID获取数据
	     */
	    this.getItemByID=function(id){
	    	var list = this.listResult;
	        var item = null;
	        for (var i = 0; i < list.length; i++) {
	            var val = list[i];
	            if (val.id == id) {
	            	item=val;
	                break;
	            }
	        }
	        return item;
	    }
	    
	    /**
	     * 行单击事件地图居中显示
	     */
	    this.listCelDbClick = function(e){
	    	//关闭详情框
	    	window.mapPage.detailManager.closeFunction();
	    	return;
	    	var thi$ = e.data;
	    	var $thisOne = $(e.currentTarget);
	    	var id = $thisOne.parent().find(".one").eq(0).attr("id");
	    	var item=thi$.getItemByID(id);
	    	if (item != null) {
                var zoom         = null;
                var type         =thi$.curResType;
                
                //计算图幅显示层级
                if(type=="5"||type=="6"||type=="7"||type=="10"||type=="11"){
                	var idMapScale = item.idMapScale;
                	var thScale     = SearchTools.getInstance().getThScale(idMapScale);
                	zoom            = SearchTools.getInstance().thScaleMap[thScale].zoom;
                }
                
                //地图图形标记为已选择
                if(type=="1"||type=="2"||type=="3"||type=="4"){
                	 window.mapPage.mapManager.centerMarker(item);
                }else{
                	 window.mapPage.mapManager.centerVector(item,zoom);
                }
            }
	    }
	    
//---------------------------复选框选择事件---------------------------------------
	    
	    /**
	     * 复选框点击事件
	     * 地图图形标记为选择状态
	     */
	    this.showSelectClick=function(e){	
	    	//关闭详情框
	    	window.mapPage.detailManager.closeFunction();
	    	 var scaleList = e.data;
	    	 var $thisOne = $(e.currentTarget).find(".z-list-checkbox");
	    	 if($thisOne.text().length == 0){
	    		 $thisOne.text("√");
	    		 //地图显示选择图形
	    		 scaleList.showSlectGeom($(e.currentTarget).attr("id"));
	    		 scaleList.checkIsAll();
	    	 }else{
	    		 $thisOne.text("");
	    		 $(".all").find(".z-list-checkbox").text("");
	    		 //地图取消选择图形
	    		 scaleList.unShowSlectGeom($(e.currentTarget).attr("id"));
	    	 }
	    }
	    
	    /**
	     * 复选框取消选择事件
	     */
	    this.unShowSlectGeom=function(id){

	    	if(locationVector.length>0){
	    		var type         =this.curResType;
	    		for(var i=0;i<locationVector.length;i++){
	    			var obj=locationVector[i];
	    			if(obj.id==id){
	    				var vector=obj.vector;
	    				if(type=="1"||type=="2"||type=="3"||type=="4"){
	    					window.mapPage.mapManager.unSelectMarker(vector);
	    				}else{
	    					window.mapPage.mapManager.unSelectVector(vector);
	    				}
	    				locationVector.splice(i,1);
	    				break;
	    			}
	    		}
	    	}
	    }
	    
	    /**
	     * 地图图形标记为选择状态
	     */
	    this.showSlectGeom=function(id){
	    	var item=this.getItemByID(id);
	        if (item != null) {
                var wkt          = item.geom;
                var zoom         = null;
                var vector       = null;
                var type         =this.curResType;
                var vectorObj    =new Object();
                
                //计算图幅显示层级
                if(type=="5"||type=="6"||type=="7"||type=="10"||type=="11"){
                	var idMapScale = item.idMapScale;
                	var thScale     = SearchTools.getInstance().getThScale(idMapScale);
                	zoom            = SearchTools.getInstance().thScaleMap[thScale].zoom;
                }
                
                //地图图形标记为已选择
                if(type=="1"||type=="2"||type=="3"||type=="4"){
                	 vector=window.mapPage.mapManager.selectMarker(item);
                }else{
                	 vector=window.mapPage.mapManager.selectVector(item,zoom);
                }
            }
	        //判断是否已选择
	        if(!_isSelect(id)){
	            vectorObj.id=id;
	            vectorObj.vector=vector;
	            this.addVectorToList(vectorObj);
	        }
	    }
	    
	    
	    //校验是否全选，
	    this.checkIsAll = function(){
	    	var countNum = 0;
			 $(".one").each(function(){
				 $thisOne=$(this).find(".z-list-checkbox");
				 if($thisOne.text().length != 0){
					 countNum ++;
				 }
			 });
			 if(countNum ==this.pageData["pageNum"]){
				 $(".all").find(".z-list-checkbox").text("√");
			 }
	    }
	    
	    /**
	     * 复选框点击事件 地图显示选择数据
	     * 通过id改变一个为选中或者未选中 ture选中 false未选中
	     */
	    this.changeOneById = function(id,flag){
	    	
	    	var $this = this;
	    	$(".one").each(function(){
	    		var $thisOne = $(this).find(".z-list-checkbox");
	    		if($(this).attr("id") == id){
	    			if($this.blueId != null){
		    			$("#"+$this.blueId).parent().children().css("color","#333333");
		    		}
		    		$(this).parent().children().css("color","#519dde");
		    		$this.blueId = id;
		    		var offsetTop = $(this).parent().offset().top - $(this) .parent().parent().offset().top ;
		    		$(".gridTable").animate({scrollTop:$(".gridTable").scrollTop()+offsetTop},500);
	    			return false;
	    		}
	    	});
	    }
	    
	    //校验是否已选择
	    var _isSelect=function(id){
	    	var flag=false;
	    	for(var j=0;j<locationVector.length;j++){
	    		var item=locationVector[j];
	    		if(item.id==id){
	    			window.mapPage.detailManager.cleanSingalButton(true,id);
	    			flag=true;
	    			break;
	    		}
	    	}
	    	return flag;
	    }
	    
	    /**
	     * 添加图形到List中，判断是否已添加过
	     */
	    this.addVectorToList=function(vectorObj){
	    	
	    	if(locationVector.length>0){
	    		var id=vectorObj.id;
	    		var flag=true;
	    		for(var i=0;i<locationVector.length;i++){
	    			var item=locationVector[i];
	    			if(item.id==id){
	    				flag=false;
	    				break;
	    			}
	    		}
	    		if(flag){
	    			locationVector.push(vectorObj);
	    		}
	    	}else{
	    		locationVector.push(vectorObj);
	    	}
	    }

	    
	    /**
	     * 地图层级改变添加之前选择图层
	     */
	    this.addSelcetVector=function(){
	    	if(locationVector.length>0){
	    		for(var i=0;i<locationVector.length;i++){
	    			var item=locationVector[i];
	    			var vector=item.vector;
	    			this.oplayerMap.resultLayer.addFeatures(vector);
	    		}
	    	}
	    }

//-----------------------------详情信息点击事件-----------------------------------	    
	    	    
	    /**
	     * 详情信息点击事件
	     */
	    this.show4DDetailClick=function(e){
	    	 var scaleList = e.data, id = e.target.id;
	    	 var item=scaleList.getItemByID(id);
	    	 var list=[];
	    	 list.push(item);
	    	 window.mapPage.detailManager.content(list,window.mapPage.attrManager.resType);
	    }
	    
//-------------------------------加入购物车点击事件---------------------------------	    
	    
	    /**
	     * 加入购物车点击事件
	     */
	    this.addShopCarClick =function(e){
	    	 var scaleList = e.data, id = e.target.id;
	    	 scaleList.father.father.addShopCar(id);
	    	 //scaleList.father.father.mapCarManager.refresh(1);
	    }
	    
	    /**
	     * 批量加入购物车点击事件
	     */
	    this.addAllShopCarClick =function(e){
	    	
	    	 var scaleList = e.data, id = e.target.id;
	    	 if(locationVector.length>0){
	    		 scaleList.father.father.addShopCars(locationVector);
	    		 //scaleList.father.father.mapCarManager.refresh(1);
	    	 }else{
	    		 info = "请选择数据";
	    		 var pop = new PopWindow({
	    	            info : info
	    	        });
	    	 }
	    }
	    
	    /**
	     * 返回点击事件:选择图形清空
	     */
	    this.returnClick=function(e){
	    	var scaleList = e.data;
	    	if(locationVector.length>0){
	    		for(var i=0;i<locationVector.length;i++){
	    			var item=locationVector[i];
	    			var vector=item.vector;
	    			window.mapPage.mapManager.unSelectVector(vector);
	    		}
	    	}
	    	locationVector=[];
	    	scaleList.father.returnClickFun();
	    };
	    
	    
	    

	    /**
	     * 分页
	     */
	    this.page=function(e){
	    	var scaleList = e.data;
	    	var num = $(e.target).attr("num");
	    	if(num != "-1"){
	    		scaleList.pageData["nowPage"] = parseInt(num);
	        	scaleList.addList();
	    	}
	    }
	    /**
	     * 跳转某页
	     */
	    this.toThePage=function(e){
	    	var scaleList = e.data;
	    	var numInt = parseInt($(e.target).prev().val());
	    	if(!isNaN(parseInt(numInt))){
	    		if(numInt>0 && numInt <= scaleList.totlePage){
	    			scaleList.pageData["nowPage"] = numInt - 1;
	        		scaleList.addList();
	    		}
	    	}
	    }
		    
	 
//---------------------------------------初始化方法------------------------------------------------

    this.init = function(def) {
    	
        this.father     = def.father;
        this.oplayerMap = def.oplayerMap;
        this.mapUtils   = def.mapUtils;
    };

	this.init.apply(this, arguments);	
	
}