/***
 * @Description 地图购物车列表显示类 （单例模式）
 * @Author YuanJianHua
 * @CreateTime 2015年12月15日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark: 
 */
var MapCarText = (function(){

    var _instance;
	var showObj = {
		1:["buyNum","idName","idNet","idClass"],
		2:["buyNum","idLnName","idName","idNet"],
		3:["buyNum","idName","idNet","idClass"],
		4:["buyNum","idName","idNet","idClass"],
		5:["buyNum","idNewMapNum", "idMapScale", "idResType"],
		6:["buyNum","idNewMapNum", "idMapScale", "idResType"],
		7:["buyNum","idNewMapNum", "idMapScale", "idResType"],
		8:["buyNum","idGranuleName", "idEquScale", "idScaleDist"],
		9:["buyNum","idSatellite","idSensor","idScaleDist","tePosition"],
		10:["buyNum","idNewMapNum", "idMapScale", "idResType"],
		11:["buyNum","idNewMapNum", "idMapScale", "idResType"]
	}
    
	var _getCommonHtml = function(obj,key,mark){
		var html = "";
		var txt = "";
		var arr = showObj[key];
		for(var i in arr){
			if(obj[arr[i]]){
				if(arr[i] === "buyNum"){
					txt = obj[arr[i]] + "(条)";
				}else{
					if(txt.length != 0){
						txt += "/" + obj[arr[i]];
					}else{
						txt = obj[arr[i]];
					}
				}
			}
		}
		
		var w = $("#carWithSpan").html(txt).attr("w");
		if($("#carWithSpan").width() > parseInt(w)){
			$("#carWithSpan").attr("w",$("#carWithSpan").width());
		}
		var tmp = "";
		if(mark) {
			tmp = 'checked="checked"';
		}
		return '<div><li class="btf1"><input type="checkbox" ' + tmp + ' class="choose" uuid="'+obj.id+'" onclick="mapPage.mapCarManager._chooseGoods(this)"/>'
		+'</li><li class="ctrontent0">'+txt+'</li>'
		+'<li class="doan"><span uuid="'+obj.id+'" class="del" onclick="mapPage.mapCarManager._delGoods(this)">删除</span></li></div>';
	}
	
	/**
	 * 初始化方法
	 */
	var _init=function(){
		
		return{
			
			/**
			 * 自定义类别
			 */
			getDefined:function(Obj,mark,flag){
				var html = '';
				var text = '';
				for(key in Obj){
					if(Obj.hasOwnProperty(key)){
						var value=Obj[key];
						if(key=="geom"||key=="id"||key=="resType"||key=="sourceCode"||key=="storeId"||key=="fileid"){
							continue;
						}
						if(value!=null&&value!=""){
							text=text+"/"+value;
						}
					}
				}
				if(mark) {
					html='<div><li class="btf1"><input type="checkbox" checked="checked" class="choose" uuid="'+Obj.id+'" onclick="mapPage.mapCarManager._chooseGoods(this)"/>'
					+'</li><li class="ctrontent0">'+text+'</li>'
					+'<li class="doan"><span uuid="'+Obj.id+'" class="del" onclick="mapPage.mapCarManager._delGoods(this)">删除</span></li></div>';
				} else {
					html='<div><li class="btf1"><input type="checkbox" class="choose" uuid="'+Obj.id+'" onclick="mapPage.mapCarManager._chooseGoods(this)"/>'
					+'</li><li class="ctrontent0">'+text+'</li>'
					+'<li class="doan"><span uuid="'+Obj.id+'" class="del" onclick="mapPage.mapCarManager._delGoods(this)">删除</span></li></div>';
				}
				return html;
			},
			
			/**
			 *  1 重力点 点名idName + 网名idNet +等级类型 idClass
			 */
			getZLD:function(Obj,mark,flag){
				return _getCommonHtml(Obj,1,mark,flag);
			},
			/**
			 *  2 水准点 路线名称idLnName + 点名idName +网名 idNet
			 */
			getSZD:function(Obj,mark,flag){
				return _getCommonHtml(Obj,2,mark,flag);
			},
			/**
			 *  3 三角点 点名idName + 网名idNet +等级 idClass
			 */
			getSJD:function(Obj,mark,flag){
				return _getCommonHtml(Obj,3,mark,flag);
			},
			/**
			 *  4 GNSS成果 点名idName + 网名idNet +等级 idClass
			 */
			getGPS:function(Obj,mark,flag){
				return _getCommonHtml(Obj,4,mark,flag);
			},
			/**
			 *  5 DLG 矢量地图 新图号idNewMapNum + 比例尺idMapScale +成果类型 idResType
			 */
			getDLG:function(Obj,mark,flag){
				return _getCommonHtml(Obj,5,mark,flag);
			},
			/**
			 * 6 DEM 数字高程模型 新图号idNewMapNum + 比例尺idMapScale +成果类型 idResType
			 */
			getDEM:function(Obj,mark,flag){
				return _getCommonHtml(Obj,6,mark,flag);
			},

			/**
			 * 7 DOM 分幅正射 新图号idNewMapNum + 比例尺idMapScale +成果类型 idResType
			 */
			getDOM:function(Obj,mark,flag){
				return _getCommonHtml(Obj,7,mark,flag);
			},
			
			/**
			 * 8 航空影像 摄区名称idGranuleName + 航摄比例尺idEquScale +分辨率 idScaleDist
			 */
			getHKYX:function(Obj,mark,flag){
				return _getCommonHtml(Obj,8,mark,flag);
			},
			
			
			/**
			 * 9 卫星影像 
			 */
			getWeix:function(Obj,mark,flag){
				return _getCommonHtml(Obj,9,mark,flag);
			},
			
			/**
			 * 10 DRG 分幅正射 新图号idNewMapNum + 比例尺idMapScale +成果类型 idResType
			 */
			getDRG:function(Obj,mark,flag){
				return _getCommonHtml(Obj,10,mark,flag);
			},
			
			/**
			 * 11 模拟地图 分幅正射 新图号idNewMapNum + 比例尺idMapScale +成果类型 idResType
			 */
			getMLDT:function(Obj,mark,flag){
				return _getCommonHtml(Obj,11,mark,flag);
			}
		};
	};
		
	return{
		/**
		 * 获取地图购物车列表显示类实例
		 * @param {Object} def
		 */
		getInstance:function(){
			if(!_instance){
			  _instance=_init();
		    }
		    return _instance;
		}
	};
	
})();