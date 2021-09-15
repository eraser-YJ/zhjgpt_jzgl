/***
 * @Description 查询工具类 (单例模式，SearchTools.getInstance())
 * @Author YuanJianHua
 * @CreateTime 2015年12月15日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var SearchTools=(function(){

	var _instance;

	var _init=function(){

		   /**
		    * 分发单位
		    */
		   var distor={
				   "广西壮族自治区基础地理信息中心":{
					   name:"450000"
				   },
				   "山东省地理信息中心":{
					   name:"370000"
				   },
				   "青岛市测绘局测试站点":{
					   name:"370000"
				   },
				   "四川省基础地理信息中心":{
					   name:"510000"
				   },
				   "湖北省测绘成果档案馆":{
					   name:"420000"
				   },
				   "重庆市地理信息中心":{
					   name:"500000"
				   },
				   "北京市测绘设计研究院":{
					   name:"110000"
				   },
				   "江西省测绘成果资料档案馆":{
					   name:"360000"
				   },
				   "福建省基础地理信息中心":{
					   name:"350000"
				   },
				   "浙江省测绘资料档案馆":{
					   name:"330000"
				   },
				   "安徽省测绘档案资料馆":{
					   name:"340000"
				   },
				   "江苏省测绘资料档案馆":{
					   name:"320000"
				   },
				   "湖南省基础地理信息中心":{
					   name:"430000"
				   },
				   "黑龙江基础地理信息中心":{
					   name:"230000"
				   },
				   "陕西省基础地理信息中心":{
					   name:"610000"
				   },
				   "广东省国土资源信息中心":{
					   name:"440000"
				   },
				   "贵州省测绘资料档案馆":{
					   name:"520000"
				   },
				   "河北省测绘局":{
					   name:"130000"
				   },
				   "吉林省基础地理信息中心":{
					   name:"220000"
				   },
				   "辽宁省基础地理信息中心":{
					   name:"210000"
				   },
				   "内蒙古自治区测绘科技档案资料馆":{
					   name:"150000"
				   },
				   "宁夏基础地理信息中心":{
					   name:"640000"
				   },
				   "青海省地理信息中心":{
					   name:"630000"
				   },
				   "山西省综合地理信息中心":{
					   name:"140000"
				   },
				   "上海市测绘院":{
					   name:"310000"
				   },
				   "天津市测绘院":{
					   name:"120000"
				   },
				   "西藏自治区测绘局测绘资料档案馆":{
					   name:"540000"
				   },
				   "新疆维吾尔自治区测绘档案资料馆":{
					   name:"650000"
				   },
				   "云南省测绘资料档案馆":{
					   name:"530000"
				   },
				   "甘肃省基础地理信息中心":{
					   name:"620000"
				   },
				   "河南省基础地理信息中心":{
					   name:"410000"
				   }
		   };

		   /**
			 * 根据PAC，获取省市县名称
			 */
		   var getRegionNameByPAC = function (list,pac) {
		        for(var i in list){
		            var obj = list[i];
		            if(obj.pac === pac){
		                return obj.name;
		            }else if(obj.children){
		                var tmp = getRegionNameByPAC(obj.children,pac);
		                if(tmp){
		                    return tmp;
		                }
		            }else{
		                continue;
		            }
		        }
		        return null;
		    };

		   /**
		    * 临时关键字
		    */
		   var tempKeyWord =null;

		   /**
		    * 所有类型
		    */
		   var allType     =null;

		//--------------------------------私有属性、方法------------------------------------------

		   //--------------工具栏查询条件 查询属性  ------------------

        	/**
        	 * 绘制图形  记录工具条矩形绘制、多边形绘制、线绘制的图形结果
        	 */
        	var drawGeom  =null;

        	/**
        	 * 地域图形  记录工具条道路、水系搜索产生的图形结果
        	 */
        	var regionGeom=null;

        	/**
        	 * 上传图形 记录工具条SHP上传产生的图形结果
        	 */
        	var shpGeom   =null;

        	/**
        	 * 行政区域名  记录产生的行政区域名
        	 */
        	var regionName=null;

        	/**
        	 * 线缓冲距离  记录多折线缓冲宽度
        	 */
        	var buffer    =0;

        	/**
        	 * 是否包含查询  false 相交查询  true 包含查询
        	 */
        	var isWithin  =false;

			//---------搜索栏查询条件 查询属性  ------------------

		    /**
        	 * 查询关键字
        	 */
		    var keyWord   =null;

		    /**
		     * 是否关键字过滤
		     */
		    var keyFilter =true;

		    //---------属性栏查询条件 查询属性  ------------------
		    /**
        	 * 2017-12-06 非涉密成果标签
        	 */
        	var isPublic=null;

		    /**
		     * 站点ID
		     */
		    var storeID   =null;

		    /**
		     * 父级站点ID
		     */
		    var fahterStoreID =0;

		    /**
		     * 类型ID
		     */
		    var resType   =0;

		    /**
		     * 文件批次ID
		     */
		    var fileid    =null;

		    /**
		     * 站点名称
		     */
		    var storeName =null;

		    /**
		     * 密级
		     */
		    var secClass  =null;

		    /**
		     * 属性条件集
		     */
		    var attrQuery ={};

		  //---------查看条件 相关属性----------------------------

		    /**
		     * 原查询条件封装对象
		     */
		    var searchData =null;

		    /**
		     * 当前查看比例尺条件
		     */
		    var lookScale=null;

		    /**
		     * 当前查看区域条件
		     */
		    var lookRegionName=null;

		    /**
		     * 当前查看聚合类型
		     */
		    var lookGroupType =null;

		  //---------分页值----------------------------
		    /**
		     * 第几页
		     */
		    var pageNum  =1;

		    /**
		     * 每页条数
		     */
		    var pageSize =10;


		return{

			//-----------------共有属性、方法--------------------------

			/**
			 * 获取原始查询条件
			 */
			getQueryData:function(){

				//属性原始条件
				if(storeID!=null){
					attrQuery["storeId"]=[storeID];
				}
				//文件批次
				if(fileid!=null){
					attrQuery["fileid"]=[fileid];
				}
				//密级
				if(secClass!=null){
					if(secClass == "公开"){
						attrQuery["secClass"]=["公开"];
					}else{
						attrQuery["secClass"]=["秘密","机密","绝密"];
					}
				}


				var attrInfo={
					resType:resType,
					storeID:storeID,
					fahterStoreID:fahterStoreID,
					attrQuery  :attrQuery,
					pageNum:pageNum,
					pageSize:pageSize,
					isPublic:isPublic
				};

				//空间原始条件
				var geomInfo={
					geomType  :"",
					regionName:regionName,
					drawGeom  :drawGeom,
					regionGeom:regionGeom,
					shpGeom   :shpGeom,
					buffer    :buffer,
					isWithin  :isWithin,
					zoomLevel :MapUtils.getInstance().getZoomLevel()
				};

				//关键字原始条件
				var data = {
		            keyWord : keyWord,
		            keyFilter:keyFilter
		        };

				if(attrQuery.idMapScale!=null){
					lookScale=null;
				};
				if(lookScale!=null){
					data.thScale=lookScale;
				};

		        data = Utils.ObejctCopy(data, geomInfo, true);
		        data = Utils.ObejctCopy(data, attrInfo, true);

		        return data;
			},

			/**
			 * 获取原始查看条件
			 */
			getLookData:function(){

				searchData.thScale=lookScale;
				searchData.regionName=lookRegionName;
				searchData.groupType=lookGroupType;
				return searchData;
			},

			/**
			 * 获取数据类型
			 */
			getResType:function(){

				var temp=resType;
				return temp;
			},

			/**
			 * 获取查询条件显示信息
			 */
			getSearchText:function(flag){
				var text=[];
				var resType  =searchData["resType"];
				var typeName=window.mapPage.attrManager.types[resType]["typeName"];
				text.push(typeName);

				if(resType=="5"||resType=="6"||resType=="7"||resType=="10"||resType=="11"){
						var thScale=searchData["thScale"];
						if(thScale!=null&&thScale!=""){
							var scaleName=this.thScaleMap[thScale]["name"];
							text.push(scaleName);
						}else{
							var attrQuery=searchData["attrQuery"];
							var thScale  =attrQuery["thScale"];
							if(thScale!=null&&thScale!=""){
								var scale=thScale[0];
								var scaleName=this.thScaleMap[scale]["name"];
								text.push(scaleName);
							}
						}
				}

				if(flag){
					var regionName=searchData["regionName"];
					if(lookRegionName!=null&&lookRegionName!=""){
						if(lookRegionName.indexOf(";")>0){
							lookRegionName=lookRegionName.replace(";","");
						}
						var showName=getRegionNameByPAC(adminDataStore,lookRegionName);
						text.push(showName);
					}else{
						if(regionName!=null&&regionName!=""){
							var showName=getRegionNameByPAC(adminDataStore,regionName);
							text.push(showName);
						}else{
							var touchRegion=searchData["attrQuery"]["touchRegion"];
							if(touchRegion!=null&&touchRegion!=""){
								var showName=getRegionNameByPAC(adminDataStore,touchRegion[0]);
								text.push(showName);
							}
						}
					}
				}

				return text;
			},

			/**
			 * 获取过滤后关键字
			 */
			getKeyWord:function(){
				return keyWord;
			},

			getTempKeyWord:function(){
				return tempKeyWord;
			},

			/**
			 * 获取关键字过滤开关
			 */
			getKeyFilter:function(){
				return keyFilter;
			},

			/**
			 * 设置绘制图形值（图形条件）
			 * @param {Object} value
			 */
			setDrawGeom:function(value){
				drawGeom=value;
			},
			/**
			 * 获取绘制图形值（图形条件）
			 * @param {Object} value
			 */
			getDrawGeom:function(){
				return drawGeom;
			},
			/**
			 * 设置地域图形值（图形条件）
			 * @param {Object} value
			 */
			setRegionGeom:function(value){
				regionGeom=value;
			},
			/**
			 * 设置上传图形值（图形条件）
			 * @param {Object} value
			 */
			setShpGeom:function(value){
				shpGeom=value;
			},
            /**
			 * 设置行政区域名（图形条件）
			 * @param {Object} value
			 */
			setRegionName:function(value){
				regionName=value;
			},
			/**
			 * 设置是否涉密数据标签
			 * @param {Object} value
			 */
			setIsPublic:function(value){
				isPublic=value;
			},
			/**
			 * 设置线缓冲距离（图形条件）
			 * @param {Object} value
			 */
			setBuffer:function(value){
				buffer=value;
			},

			/**
			 * 设置是否包含查询
			 */
			setIsWithin:function(value){
				isWithin=value;
			},


			/**
			 * 设置查询关键字（关键字条件）
			 * @param {Object} value
			 */
			setKeyWord:function(value){
				keyWord=value;
			},

			/**
			 * 设置关键字过滤开关（关键字条件）
			 * @param {Object} value
			 */
			setKeyFilter:function(value){
				keyFilter=value;
			},

			/**
			 * 设置文件批次ID（属性条件）
			 * @param {Object} value
			 */
			setFileID:function(value){
				fileid=value;
			},

			/**
			 * 设置密级
			 * @param {Object} value
			 */
			setSecClass:function(value){
				secClass=value;
			},

			/**
			 * 设置站点ID（属性条件）
			 * @param {Object} value
			 */
			setStoreID:function(value){
				storeID=value;
			},
			/**
			 * 设置站点ID（属性条件）
			 * @param {Object} value
			 */
			setFahterStoreID:function(value){
				fahterStoreID=value;
			},

			/**
			 * 设置站点名称
			 * @param {Object} value
			 */
			setStoreName:function(value){
				storeName=null;
				var obj=distor[value];
				if(obj!=null){
					regionName=obj.name;
				}
			},

			/**
			 * 设置类型ID（属性条件）
			 * @param {Object} value
			 */
			setResType:function(value){
				resType=value;
			},

			/**
			 * 设置属性条件集（属性条件）
			 * @param {Object} value
			 */
			setAttrQuery:function(value){
				attrQuery=value;
			},

			/**
			 * 记录临时关键字 命中多区域情况
			 */
			setTempKeyWord:function(value){
				tempKeyWord=value;
			},

			/**
			 * 设置分页值
			 */
			setPageNum:function(value){
				pageNum=value;
			},

//---------------------------查看条件设置-------------------------------------
			/**
			 * 设置原查询条件封装对象（查看条件）
			 */
			setSearchData:function(value){
				searchData=value;
			},

			/**
			 * 设置当前查看比例尺（查看条件）
			 */
			setLookScale:function(value){
				lookScale=value;
			},

			/**
			 * 设置当前查看区域名（查看条件）
			 */
		    setLookRegionName:function(value){
		    	lookRegionName=value;
		    },

		    /**
			 * 设置当前查看聚合类型（查看条件）
			 */
		    setLookGroupType:function(value){
		    	lookGroupType=value;
		    },

//----------------------------设置所有类型 展示所用---------------------------------------------

		    setAllTypes:function(value){
		    	allType=value;
		    },

		    /**
		     * 根据类型ID获取类型名称
		     */
		    getTypeName:function(typeID){

		    	var typeName="";
		    	var obj     =allType[typeID];
		    	if(obj!=null){
		    		typeName=obj.typeName;
		    	}
		    	return typeName;
		    },

		    /**
		     * 根据类型ID,获取类型对象（含核心属性名）
		     */
		    getDataType:function(typeID){

		    	var obj=allType[typeID];
		    	return obj;
		    },

			/**
			 * 图幅比例尺Map,key:比例尺字母缩写,value:比例尺名、及对应地图层级
			 */
			thScaleMap:{
				"A" : {
		            name : "1:100万",
		            zoom : 3
		        },
		        "B" : {
		            name : "1:50万",
		            zoom : 4
		        },
		        "C" : {
		            name : "1:25万",
		            zoom : 5
		        },
		        "D" : {
		            name : "1:10万",
		            zoom : 6
		        },
		        "E" : {
		            name : "1:5万",
		            zoom : 7
		        },
		        "F" : {
		            name : "1:2.5万",
		            zoom : 8
		        },
		        "G" : {
		            name : "1:1万",
		            zoom : 9
		        },
		        "H" : {
		            name : "1:5千",
		            zoom : 10
		        },
		        "I" : {
		            name : "1:2千",
		            zoom : 11
		        },
		        "J" : {
		            name : "1:1千",
		            zoom : 12
		        },
		        "K" : {
		        	name : "1:5百",
		        	zoom : 13
		        },
		        "Z" : {
		        	name : "1:20万",
		        	zoom : 13
		        }

			},


			/**
			 * 根据比例尺，获取需要显示的地图层级
			 * @param {Object} scale 比例尺字母缩写
			 */
			getMapZoomByScale:function(scale){

				var zoomLevel=0;
				switch (scale) {
				case "A":
					zoomLevel=0;
					break;
				case "B":
					zoomLevel=4;
					break;
				case "C":
					zoomLevel=5;
					break;
				case "D":
					zoomLevel=6;
					break;
				case "E":
					zoomLevel=7;
					break;
				case "F":
					zoomLevel=8;
					break;
				case "G":
					zoomLevel=9;
					break;
				case "H":
					zoomLevel=10;
					break;
				case "I":
					zoomLevel=11;
					break;
				case "J":
					zoomLevel=12;
					break;
				case "K":
					zoomLevel=13;
					break;
				case "Z":
					zoomLevel=5;
					break;
				}
				return zoomLevel;
			},

			/**
		     * 根据当前地图层级获取比例尺
		     */
		    getScaleByMapZoom:function(curZoom){
		    	var scale="A";
		    	switch(curZoom){
		    	case 0:
		    	case 1:
		    	case 2:
		    	case 3:
		    	    scale="A";// 1:100万
		    		break;
		    	case 4:
		    		scale="B";// 1:50万
		    		break;
		    	case 5:
		    		scale="C";// 1:25万
		    		break;
		    	case 6:
		    		scale="D";// 1:10万
		    		break;
		    	case 7:
		    		scale="E";// 1:5万
		    		break;
		    	case 8:
		    		scale="F";// 1:2.5万
		    		break;
		    	case 9:
		    		scale="G";// 1:1万
		    		break;
		    	case 10:
		    		scale="H";// 5000
		    		break;
		    	case 11:
		    		scale="I";//2000
		    		break;
		    	case 12:
		    		scale="J";//1000
		    		break;
		    	case 13:
		    		scale="K";//500
		    		break;
		    	}
		    	return scale;
		    },

		    /**
		     * 根据比例尺分母，获取地图比例尺
		     */
		    getThScale:function(mapScale){
		    	var scale=null;
		    	switch(mapScale) {
		    	  case "1000000"://1:100万
		    		  scale="A";
		    		  break;
		    	  case "500000"://1:50万
		    		  scale="B";
		    		  break;
		    	  case "250000"://1:25万
		    		  scale="C";
		    		  break;
		    	  case "200000"://1:20万
		    		  scale="Z";
		    		  break;
		    	  case "100000"://1:10万
		    		  scale="D";
		    		  break;
		    	  case "50000"://1:5万
		    		  scale="E";
		    		  break;
		    	  case "25000"://1:2.5万
		    		  scale="F";
		    		  break;
		    	  case "10000"://1:1万
		    		  scale="G";
		    		  break;
	    		  case "5000"://1:5000
		    		  scale="H";
		    		  break;
		    	  case "2000"://1:2000
		    		  scale="I";
		    		  break;
		    	  case "1000"://1:1000
		    		  scale="J";
		    		  break;
		    	  case "500"://1:500
		    		  scale="K";
		    		  break;
		    	}
		    	return scale;
		    },

		    /**
		     * 根据图幅号获取比例尺
		     */
		    getScaleByTH:function(mapNum){
		    	if(mapNum.length<4){
					return "A";
				}else{
					return mapNum.substring(3,4);
				}
		    },

		    /**
			 * 获取分页值
			 */
			getPageNum:function(){
				return pageNum;
			},

			/**
			 * 获取密级
			 * @param {Object} value
			 */
			getSecClass:function(){
				return secClass;
			}

		};
	};


	return{
		/**
		 * 获取查询工具类实例
		 */
		getInstance:function(){
			if(!_instance){
			  _instance=_init();
		    }
		    return _instance;
		}
	};


})()
