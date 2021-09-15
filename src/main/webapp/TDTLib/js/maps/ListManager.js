/***
 *
 * @Description 列表管理类
 * @Author yuanJianHua
 * @CreateTime 2015年12月7日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var ListManager = function() {
	
//-------------------------------相关属性---------------------------------------------------------------
   
   /**
    * 天地图地址查询列表展示类
    */
   this.tdtListObj=null;
   
   /**
    * 成果查询列表展示类
    */
   this.normalListObj=null;
   
   /**
    * 其它查询列表展示类
    */
   this.otherListObj=null;
   
   /**
    * 图幅类结果集
    */
   this.ResultData=null;
   
   /**
    * 详情是否显示开关
    */
   this.detailOnOff=false;

//-------------------------------------------清理子类结果集------------------------------------------------------------------
   
   this.cleanListResult=function(){
   	
		this.detailOnOff=false;
		this.ResultData =null;
		$("#scaleGroupList div").remove();
		SearchTools.getInstance().setLookRegionName(null);
		SearchTools.getInstance().setLookGroupType(null);
//		SearchTools.getInstance().setLookScale(null);
		SearchTools.getInstance().setSearchData(null);
   }   
   
   
//-------------------------------相关方法--------------------------------------   
    
   /***
    * 展示指定的列表页面控件
    * @param name {String} 控件名称
    */
   this.showStatList = function(name) {
      
       $("#noResultBox").hide();
       $("#searchBox").hide();
       $("#scaleGroupBox").hide();
       $("#scaleListBox").hide();
       $("#tianDiTuBox").hide();
       $("#newTianDiTuBox").hide();
       $("#loadingBox").hide();
       $("#biggerBox").hide();
       if(name!="scaleGroupBox"&&name!="scaleListBox"){
    	   this.father.setBigBound("",null);
       }
       $("#" + name).show();
   };
   
//-------------------------------天地图相关查询结果--------------------------

    /**
     * 增加天地图查询结果展示内容（推荐城市）
     */
    this.addTianDiTuCityList=function(priorityCitys,allAdmins){
    	
    	if(priorityCitys==undefined&&allAdmins==undefined){
    		 this.showStatList("noResultBox");
    		 return false;
    	}
    	
    	if(priorityCitys!=undefined&&allAdmins!=undefined){
    		if (priorityCitys.length < 1&&allAdmins.length<1) {
                this.showStatList("noResultBox");
                return false;
            }
    	}
    	//显示天地图查询列表
    	this.showStatList("newTianDiTuBox");
    	//列表赋值
        this.tdtListObj.addCityList(priorityCitys,allAdmins);
    };
    
    /**
     * 增加天地图查询结果展示内容（点数据）
     */
    this.addTianDiTuPointList=function(data){
    	 
        if (data==undefined||data.length < 1) {
            this.showStatList("noResultBox");
            return false;
        }
        //显示天地图查询列表
        this.showStatList("newTianDiTuBox");
        //列表赋值
        this.tdtListObj.addPoiList(data);
    };
    
    /**
     * 增加天地图查询结果展示内容（行政区划）
     */
    this.addTianDiTuAreaResult=function(data){
    	
    	if (data==undefined||data.length < 1) {
            this.showStatList("noResultBox");
            return false;
        }
    	//显示天地图查询列表
        this.showStatList("newTianDiTuBox");
        //列表赋值
        this.tdtListObj.addArea(data);
    }
    
//------------------------------无类别、自定义类别列表显示-------------------------
   
    this.titleFlag = false;
    /**
     * 无类别列表展示
     */
    this.NoTypeListShowFunction=function(data,count){
    	
    	if (data==undefined||data.length < 1) {
            this.showStatList("noResultBox");
            return false;
        }
    	//显示区域查询列表并赋值
        this.showStatList("tianDiTuBox");
        this.otherListObj.OtherTypeListShow(data,"tianDiTuList",count);
    }
    
    /**
     * 自定义类别列表展示
     */
    this.OtherTypeListShowFunction=function(data,count){
    	
    	if (data==undefined||data.length < 1) {
            this.showStatList("noResultBox");
            return false;
        }
    	//显示区域查询列表并赋值
        this.showStatList("tianDiTuBox");
        this.otherListObj.OtherTypeListShow(data,"tianDiTuList",count);
    }
    
//-----------------------------道路、河流、省市县区域列表显示--------------------------
    
    /**
     * 命中道路、河流、省市县多区域数据列表显示
     */
    this.addAreaSearchResult=function(data){
    	
    	if (data==undefined||data.length < 1) {
            this.showStatList("noResultBox");
            return false;
        }
    	//显示区域查询列表并赋值
        this.showStatList("tianDiTuBox");
        this.scaleList.addAreaList(data,"tianDiTuBox");
    }
    
//------------------------------点产品列表展示----------------------------------------
    
    /**
     * 点类产品类别展示
     */
    this.PointTypeListShowFunction = function(data){
    	
    	this.ResultData=data;
    	//区域聚合结果
    	var regionGroupList=data.groupRegionList;
    	//详情结果
    	var detailList     =data.itemList;
    	//当前区域名
    	var curRegionName  =data.curRegionName;
    	
    	var bbTextStr = "";
    	
    	//根据详情判断显示方式
    	if(detailList.length > 0){
    		//展示详情列表
    		this.detailOnOff=true;
    		this.showStatList("scaleListBox");
    		this.normalListObj.detialListShow(detailList,this.father.attrManager.resType);
    		if(curRegionName!=undefined){
				bbTextStr +=curRegionName;
			}
    	}else if(regionGroupList.length>0){
    		//展示聚合列表
    		this.detailOnOff=false;
    		this.showStatList("scaleGroupBox");
    		this.normalListObj.addPointListGroup(data);
    	}else{
    		this.detailOnOff=false;
    		 this.showStatList("noResultBox");
    	}
    	
    	if(bbTextStr.length != 0 ){
    		$(".bbText").text(bbTextStr);
    	}
    	//信息提示
    	if(detailList.length==0 && regionGroupList.length>0){
    		this.tipsOnline();
    	}
    }
    
//------------------------------卫星影像产品列表展示---------------------------------
    
      /**
       * 卫星影像类产品列表展示
       */
      this.WXTypeListShowFunction = function(data){
      	
      	this.ResultData=data;
      	//区域聚合结果
      	var regionGroupList=data.groupRegionList;
      	//详情结果
      	var detailList     =data.itemList;
      	//当前区域名
      	var curRegionName  =data.curRegionName;
      	
      	var bbTextStr = "";
      	
      	//根据详情判断显示方式
      	if(detailList.length > 0){
      		//展示详情列表
      		this.detailOnOff=true;
      		this.showStatList("scaleListBox");
      		this.normalListObj.detialListShow(detailList,this.father.attrManager.resType);
      		if(curRegionName!=undefined){
  				bbTextStr +=curRegionName;
  			}
      	}else if(regionGroupList.length>0){
      		//展示聚合列表
      		this.detailOnOff=false;
      		this.showStatList("scaleGroupBox");
      		this.normalListObj.addPointListGroup(data);
      	}else{
      		if(data.count>0){
      			this.tipsOnline();
      		}else{
      			this.detailOnOff=false;
          		this.showStatList("noResultBox");
      		}
      	}
      	
      	if(bbTextStr.length != 0){
      		$(".bbText").text(bbTextStr);
      	}
      //信息提示
    	if(detailList.length==0 && regionGroupList.length>0){
    		this.tipsOnline();
    	}
      }

//------------------------------4D产品列表展示------------------------------------
   
    /***
     *4D产品列表展示
     * @param data {Object} 后台返回数据
     */
    this.THTypeListShowFunction = function(data) {
   
    	this.ResultData=data;
    	
        //比例尺聚合结果
    	var scaleGroupList =data.groupScaleList;
    	//区域聚合结果
    	var regionGroupList=data.groupRegionList;
    	//详情结果
    	var detailList     =data.itemList;
    	//当前比例尺
    	var curScale       =data.curScale;
    	//当前区域名
    	var curRegionName  =data.curRegionName;
    	
        if (scaleGroupList.length < 1) {
        	this.detailOnOff=false;
            this.showStatList("noResultBox");
            return false;
        }
        var bbTextStr = "";
    	if(detailList.length>0){
    		this.normalListObj.listResult=detailList;
    		//展示详情结果列表
    		if(this.normalListObj.scale_A_onOff){
				//仅用于100万比例尺展示
    			this.detailOnOff=true;
				bbTextStr +=SearchTools.getInstance().thScaleMap[curScale]["name"];
            	this.showStatList("scaleListBox");
       		    this.normalListObj.detialListShow(detailList,this.father.attrManager.resType);
       		    this.normalListObj.scale_A_onOff=false;
			}else{
				//展示聚合结果列表
				this.detailOnOff=false;
				this.showStatList("scaleGroupBox");
				this.normalListObj.add4DListGroup(data);
			}
    	}else{
    		bbTextStr += "1:"+SearchTools.getInstance().thScaleMap[curScale]["name"];
    		//展示聚合结果列表
    		this.detailOnOff=false;
    		this.showStatList("scaleGroupBox");
    		this.normalListObj.add4DListGroup(data);
    	}
    	if(bbTextStr.length != 0){
    		$(".bbText").text(bbTextStr);
    	}
    	//信息提示
    	if(detailList.length==0 && regionGroupList.length>0){
    		this.tipsOnline();
    	}
    };
   
    /**
     * 信息提示数据量大
     */
    this.tipsOnline = function(){
    	$('.tes-infoPrj').css('bottom','0');
    	setTimeout(function(){
    		$('.tes-infoPrj').css('bottom','-50px');
    	},5000);
    }
 
//----------------------------其它方法-------------------------------------
    
    /**
     * 无结果显示
     */
    this.NoResultListShowFunction=function(){
    	
    	  this.showStatList("noResultBox");
    }
    
    
    /**
     * 详情返回事件
     */
    this.returnClickFun=function(){
    	this.showStatList("scaleGroupBox");
    	this.detailOnOff=false;
    	var resType=SearchTools.getInstance().getResType();
    	var textList=SearchTools.getInstance().getSearchText(false);
        var count   =this.ResultData.count;
        this.father.setBigBound(textList,count);
    	if(resType=="5"||resType=="6"||resType=="7"||resType=="10"||resType=="11"){
    		this.normalListObj.add4DListGroup(this.ResultData);
    	}else{
    		this.normalListObj.addPointListGroup(this.ResultData);
    	}
    	
    	//返回  版本选择清除 2016-03-08
    	$('.version-box').text('所有');
    	this.father.edition();
    	this.father.cancelVersion();
    }

    /**
     * 图幅穿刺信息展示
     */
    this.showMapNumList=function(data){
    	 //数据量小于等于1000展示列表详情信息
    	 var list=data.itemList;
    	 this.showStatList("scaleListBox");
		 this.scaleList.add4DListInfo(list,this.father.attrManager.resType);
    }
    
    /**
     * 列表参数重置
     */
    this.restNormalResultList=function(){
    	this.normalListObj.clearScaleListValue();
    }
 
//--------------------------------------初始化方法-----------------------------------------------------

    this.init = function(def) {
        
        this.father = def.father;
        
        this.normalListObj=new NormalResultListShow({
        	father    : this,
        	oplayerMap:this.father.mapManager.oplayerMap,
        	mapUtils  :this.father.mapManager.mapUtils
        });
        
        this.otherListObj=new OtherResultListShow({
        	father    : this,
        	oplayerMap:this.father.mapManager.oplayerMap,
        	mapUtils  :this.father.mapManager.mapUtils
        });

        this.tdtListObj = new TDTResultListShow({
        	father    : this,
        	oplayerMap:this.father.mapManager.oplayerMap,
        	mapUtils  :this.father.mapManager.mapUtils
        });
        
    };

    this.init.apply(this, arguments);

};