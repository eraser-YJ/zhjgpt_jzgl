/*******************************************************************************
 * @Description 该类用于管理地图查询页面，包括管理页面上的所有元素，以及管理与后台通信。
 * @Author YuanJianHua
 * @CreateTime 2015年12月15日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var MainSearch = function() {

	// ------------------------------------公有属性-------------------------------------------------------------------------
	// 地图管理类
	this.mapManager = null;
	// 属性管理类
	this.attrManager = null;
	// 列表管理类
	this.listManager = null;
	// 天地图查询类
	this.tdtSearcher = null;
	// 地图购物车管理类
	this.mapCarManager = null;
	// 成果详情显示类
	this.detailManager = null;

	// 页面内部元素的宽高
	this.pageObject = {
		header : {
			height : 95
		},
		mapInfo : {
			width : 406
		}
	};

	// 查询结果
	this.list = [];

	// 特定条件点击内容
	this.filterClick = null;

	// 按特定条件分类结果
	this.filterResCont = null;

	// 是否获取查询关键字（右侧列表成果查询时，设置为false）
	this.isSetKeyWord  = true;

	// 是否道路河流查询结果
	this.isAreaResult  = false;

	// ------------------------------------UI引导框条件设置-------------------------------------------------------------------

	  /**
	   * 根据UI引导框设置对象，设置查询条件
	   */
	  this.setSearchCondition=function(obj){

		  if(obj){
			    var tkeyWord   =obj.keyWord;
			    var tregionName=obj.regionName;
				var tresType   =obj.resType;
				var attrList   =obj.attrList;

				//设置关键字
				if (tkeyWord !=null&&tkeyWord!="") {
					$(".searchInput").val(tkeyWord);
				}
				//设置行政区域
				if (tregionName !=null&&tregionName!="") {
					SearchTools.getInstance().setRegionName(tregionName);
				}else{
					SearchTools.getInstance().setRegionName(null);
				}
				//设置类型ID
				if (tresType !=null&&tresType!="") {
					tresType = parseInt(tresType);
					mapPage.attrManager.setResType(tresType);
				}
				//设置属性
				if(attrList!=null&&attrList.length>0){
					arry=attrList;
					this.setAttrSearch(arry);
				}else{
					mapPage.attrManager.clearAttrs();
				}
		  }else{
			  SearchTools.getInstance().setRegionName(null);
			  mapPage.attrManager.clearAttrs();
		  }
		  this.search();
	  }

	// ------------------------------------首页跳转含查询条件查询--------------------------------------------------------------

	/**
	 * 首页查询 先从后台数据库获取查询设置值，然后进行查询
	 */
	this.searchNewID = function(newID) {

		var obj = new Object();
		obj["newsId"] = newID;
		$.post('./store.do?method=ajaxGetSearch', obj, _searchNewIDResult,
				'json');
	}

	/**
	 * 首页查询回调函数
	 */
	var _searchNewIDResult = function(result) {

		var arry = result.search;
		if (arry != null && arry.length > 0) {
			window.mapPage.attrManager.tempAttrsList = arry;
			for (var i = 0; i < arry.length; i++) {
				var obj = arry[i];
				window.mapPage.attrManager.setAttrBoxValue(obj, true);
			}
		}
		window.mapPage.search();
	}

	/**
	 * 分站点成果类型查询 先从后台数据库获取查询设置值，然后进行查询
	 */
	this.searchTabID = function(tabID) {

		var obj = new Object();
		obj["tabID"] = tabID;
		$.post('./tempStore.do?method=ajaxGetSearch', obj, _searchTabIDResult,
				'json');
	}

	/**
	 * 分站点成果类型查询回调函数
	 */
	var _searchTabIDResult = function(result) {

		var arry = result;
		if (arry != null && arry.length > 0) {
			window.mapPage.attrManager.tempAttrsList = arry;
			for (var i = 0; i < arry.length; i++) {
				var obj = arry[i];
				window.mapPage.attrManager.setAttrBoxValue(obj, true);
			}
		}
		window.mapPage.search();
	}

	/**
	 * 首页分类成果跳转
	 */
	this.setAttrSearch = function(arry) {

		if (arry != null && arry.length > 0) {
			window.mapPage.attrManager.tempAttrsList = arry;
			for (var i = 0; i < arry.length; i++) {
				var obj = arry[i];
				window.mapPage.attrManager.setAttrBoxValue(obj, true);
			}
		}
		window.mapPage.search();
	}

	// ------------------------------------图幅号查询--------------------------------------------------------------

	/**
	 * 图幅号成果查询
	 */
	this.searchMapNum = function(mapNum) {

		this.searchReady();
		SearchTools.getInstance().setIsWithin(true);
		var queryData = SearchTools.getInstance().getQueryData();
		var str = "";
		for (var i = 0; i < mapNum.length; i++) {
			if (i == (mapNum.length - 1)) {
				str = str + mapNum[i];
			} else {
				str = str + mapNum[i] + ",";
			}
		}
		var data = {
			"data" : JSON.stringify(queryData),
			"mapNum" : str
		};
		$.post('./mapDataAction.do?method=SearchMapNum', data, _resultParse,
				'json');
	}

	/**
	 * 图幅号定位查询
	 */
	this.positionMapNum = function(mapNum) {

		var obj = new Object();
		obj["mapNum"] = mapNum;
		$.post('./mapDataAction.do?method=positionMapNum', obj,
				_positionMapNumResult, 'json');
	}

	/**
	 * 图幅号定位回调函数
	 */
	var _positionMapNumResult = function(obj) {

		var geom = obj.searchGeom;
		if (geom != "") {
			var mapPage = window.mapPage;
			var mapManager = mapPage.mapManager;
			var listManager = mapPage.listManager;
			// 取消地图相关事件及参数
			mapManager.cancelMapMovedEvent();
			mapManager.cancelMapZoomEvent();
			mapManager.cancelMapClickEvent();
			mapManager.oplayerMap.clearMarkerLayer();
			mapManager.oplayerMap.clearResultLayer();
			// 清理地图、列表相关设置参数
			mapManager.cleanMapResult();
			mapManager.drawResultGeom(geom);

			listManager.showStatList("searchBox");
			SearchTools.getInstance().setDrawGeom(geom);
		}
	}

	// ------------------------------------成果查询---------------------------------------------------------------------------

	/**
	 * 获取查询结果
	 */
	this.getResultList = function() {
		return this.list;
	};

	/**
	 * 从页面获取查询关键词
	 */
	this.getKeyword = function() {
		var key = $(".searchInput").val();
		key = $.trim(key);
		return key;
	};

	/**
	 * 查询准备方法
	 */
	this.searchReady = function() {

		// 调用查询等待页面
		SearchDownload.create();
		// 列表聚合参数重置
		this.listManager.restNormalResultList();
		// 取消地图相关事件及参数
		this.mapManager.cancelMapMovedEvent();
		this.mapManager.cancelMapZoomEvent();
		this.mapManager.cancelMapClickEvent();
		SearchTools.getInstance().setIsWithin(false);
		// 清理地图、列表相关设置参数
		this.mapManager.cleanMapResult();
		this.listManager.cleanListResult();
		// 关闭详情窗口
		window.mapPage.detailManager.closeFunction();
		// 设置查询关键字
		var keyWord = this.getKeyword();
		SearchTools.getInstance().setKeyWord(keyWord);
		if (!this.isAreaResult) {
			SearchTools.getInstance().setRegionGeom(null);
		}
	}

	/**
	 * 分页查询
	 */
	this.pageSearch = function() {
		this.searchReady();
		// 获取查询条件
		var queryData = SearchTools.getInstance().getQueryData();
		var data = {
			"data" : JSON.stringify(queryData)
		};
		this.searchAjax(data);
	}

	/**
	 * 通用查询方法
	 */
	this.search = function() {
		this.searchReady();
		// 设置分页值
		SearchTools.getInstance().setPageNum(1);
		// 获取查询条件
		var queryData = SearchTools.getInstance().getQueryData();
		var data = {
			"data" : JSON.stringify(queryData)
		};
		this.searchAjax(data);

		// 查询图集图册数量
		// this.ajaxSearchProduct(keyWord);
		// this.inbtnClick(keyWord);
	}

	/**
	 * 通过ajax获取关于keyWord图集图册的查询数量
	 */
	this.ajaxSearchProduct = function(keyWord) {
		$.ajax({
			type : "post",
			url : "./store.do?method=ajaxCountProduct",
			data : {
				"keyWord" : keyWord
			},
			dataType:"text",
			async : false,
			success : function(data) {
				var obj = JSON.parse(data);
				var h = document.documentElement.clientHeight;
				var headerHeight = mapPage.pageObject.header.height;
				if (obj.num > 0) {
					$(".mapInfo").height(h - headerHeight - 40);
					$('.listBox_out').css({
						'height' : h - headerHeight - 40 + 'px'
					});
					$(".new-counter").show();
					$("#proSearchNum").html(obj.num);
				} else {
					$(".mapInfo").height(h - headerHeight);
					$('.listBox_out').css({
						'height' : h - headerHeight + 'px'
					});
					$(".new-counter").hide();
				}
			},
			error : function(e) {
				console.log("MainSearch.js中 ajaxSearchProduct,查询出错...");
			}
		});
	}

	/**
	 * 绑定.in-btn点击
	 */
	this.inbtnClick = function(keyWord) {
		$(".in-btn").unbind("click");
		$(".in-btn").click(
				function() {
					if (storeID == null) {
						var url = "./store.do?method=storeProduct&key="
								+ keyWord;
					} else {
						var url = "./store.do?method=storeProduct&storeId="
								+ storeID + "&key=" + keyWord;
					}
					window.open(url);
				});
	}

	/**
	 * 通用查看方法
	 */
	this.look = function() {

		// 调用查询等待页面
		SearchDownload.create();
		// this.listManager.showStatList("loadingBox");
		// 取消地图相关事件
		this.mapManager.cleanMapResult();
		this.mapManager.cancelMapMovedEvent();
		this.mapManager.cancelMapZoomEvent();
		this.mapManager.cancelMapClickEvent();

		this.listManager.detailOnOff = false;
		// 关闭详情窗口
		window.mapPage.detailManager.closeFunction();

		// 获取查看条件
		var lookData = SearchTools.getInstance().getLookData();

		var data = {
			"data" : JSON.stringify(lookData)
		};

		this.lookAjax(data);
	}

	/**
	 * 通用详情查询
	 */
	var _curUUID = null;
	this.queryDetail = function(e) {
		_curUUID = e.id;
		var uuid = e.id;
		var data = {
			"id" : uuid
		};
		this.queryDetailAjax(data);
	}

	/***************************************************************************
	 * ajax请求 成果查询
	 */
	this.searchAjax = function(data) {
		$.post('./mapDataAction.do?method=Search', data, _resultParse, 'json');
	};

	/***************************************************************************
	 * ajax请求 成果查看
	 */
	this.lookAjax = function(data) {
		$.post('./mapDataAction.do?method=Look', data, _resultParse, 'json');
	};

	/***************************************************************************
	 * ajax请求 详情查询
	 */
	this.queryDetailAjax = function(data) {
		$.post('./dataEnty.do?method=getMapDetail', data, _detailResultParse,
				'json');
	}

	// ------------------------------------初始化标签----------------------------------------------------------------

	/**
	 * 根据类型初始化标签
	 */
	this.initTag = function() {

		var resType = this.attrManager.resType;
		if (resType != 0) {
			var queryData = SearchTools.getInstance().getQueryData();
			delete queryData.keyWord;
			var data = {
				"data" : JSON.stringify(queryData),
				"storeId" : queryData.storeID,
				"fahterStoreID" : queryData.fahterStoreID
			};

			this.searchTags(data);
		}
	}

	/***************************************************************************
	 * ajax请求标签
	 */
	this.searchTags = function(data) {
		$.post('./mapDataAction.do?method=getTag', data, _resultParse, 'json');
	};

	// ------------------------------------通用查询回调函数------------------------------------------------------------------

	/**************下面都是处理知识服务为淘图搜索出现返回的处理 ps:yd 2017/11/4****************/
	var btnArr_mapKnow = [{key:"dataType"},{key:"regionProv"},{key:"regionCity"}];//生成删除btn对象
	var mapPac_mapKnow = null;
	//插入数据到searchData ps:yd 2017/11/3
	var setData_mapKnow = function(){
		var regionName = null;
		var resType = 0;
		if(btnArr_mapKnow[0].data){
			resType = btnArr_mapKnow[0].data.typeId;
		}
		var regionName = mapPac_mapKnow;
		var rsObj = unescape(getStorage().getItem("keySearch"));
		rsObj = JSON.parse(rsObj);
		//循环插入
		rsObj["regionName"] = regionName;
		rsObj["resType"] = resType;
		rsObj["keyWord"] = "";//必须删除原来的关键字
		var b = getStorage();
		var value = escape(JSON.stringify(rsObj));
		b.setItem("keySearch",value);
		window.location.reload();//不能模拟点击，要直接刷新页面(点击搜索会重写本地储存)
	}
	//点击添加属性(通用) ps:yd 2017/11/4
	var addData_mapKnow = function(clickFlag,$elem){
		$elem.attr('active',true);
		$elem.siblings().removeAttr("active");
		var obj = $elem.data();
		for (var i = 0; i < btnArr_mapKnow.length; i++) {
			if(clickFlag == "regionProv" && btnArr_mapKnow[i].key == "regionCity"){
				delete btnArr_mapKnow[i].data;
			}
			if(btnArr_mapKnow[i].key == clickFlag){
				btnArr_mapKnow[i]["data"] = obj;
			}
		}
		creatDelBox_mapKnow(btnArr_mapKnow);
	}
	//点击删除属性(通用) ps:yd 2017/11/4
	var delData_mapKnow = function(clickFlag,$elem){
		var id = "#"+clickFlag;
		$(id).find("span[active]").removeAttr("active");
		if(clickFlag == "regionProv"){//删除省份，则要删除城市
			$("#regionCity").find("span:[active=true]").removeAttr("active");
		}
		var obj = $elem.parent().data();
		for (var i = 0; i < btnArr_mapKnow.length; i++) {
			if(clickFlag == "regionProv" && btnArr_mapKnow[i].key == "regionCity"){
				delete btnArr_mapKnow[i].data;
			}
			if(btnArr_mapKnow[i].key == clickFlag){
				delete btnArr_mapKnow[i].data;
			}
		}
		creatDelBox_mapKnow(btnArr_mapKnow);
	}
	//生成删除框中的内容 ps:yd 2017/11/3
	var creatDelBox_mapKnow = function(btnArr){
		var mapPage = window.mapPage;
		var mapManager = mapPage.mapManager;
		var thisPac = null;
		var $thisBox = $("#noResultBox").find(".mapKnow_btnBox_left").empty();
		for (var i = 0; i < btnArr.length; i++) {
			var data = btnArr[i].data;
			var key = btnArr[i].key;
			if(data){
				var $delDiv = $('<div class="mapKnow_btnBox_btn"><div class="mapKnow_btn_text">'+data.name+'</div><div clickFlag='+key+' class="mapKnow_btn_del del_elemClick">×</div></div>').data(data);
				$thisBox.append($delDiv);
				//给地图绘制对象赋值
				if(data.pac){
					thisPac = data.pac;
				}
			}
		}
		if(thisPac){
			if(thisPac != mapPac_mapKnow){
				mapPac_mapKnow = thisPac;
				$.get("mapDataAction.do?method=mapAreaSearch",{type:0,key:mapPac_mapKnow},function(rs){
					var geom = rs.itemList[0].geom;
					mapManager.drawResultGeom(geom);//绘制图形
				},'json')
			}
		}else{
			if(mapPac_mapKnow){
				mapPac_mapKnow = null;
				mapManager.drawResultGeom(null);//定位到全国
			}
		}
	}
	//绑定点击事件 ps:yd 2017/11/3
	var bindEvent_mapKnow = function(){
		$(".noResultBox").on('click','.elemClick',function(){
			var clickFlag = $(this).attr("clickFlag");
			var $elem = $(this);
			if(!$elem.attr('active')){
				if(clickFlag == "regionProv"){//如果点击省份的话需要创建市级dom
					$("#regionCity").remove();
					var $li = $('<li id="regionCity"><div class="rsBox_li_title"><span>热点地区:</span></div></li>');
					var child = $elem.data().cityList;
					var $div = $('<div class="rsBox_li_content"></div>');
					for (var j = 0; j < child.length; j++) {
						var $span = $('<span clickFlag="regionCity" class="elemClick">'+child[j].name+'</span>').data(child[j]);
						$div.append($span);
					}
					$li.append($div);
					$("#noResultBox").find("ul").append($li);
				}
				addData_mapKnow(clickFlag,$elem);
			}
		})
		$(".noResultBox").on('click','.del_elemClick',function(){
			var clickFlag = $(this).attr("clickFlag");
			var $elem = $(this);
			if(clickFlag == "regionProv"){//如果是点击删除省份的话需要删除市
				$("#regionCity").remove();
			}
			delData_mapKnow(clickFlag,$elem);
		})
		$(".noResultBox").on('click','.mapKnow_btnBox_right',function(){
			setData_mapKnow();
		})
	}
	//知识服务的查询后dom构造 ps:yd 2017/11/1
	var resultFor_mapKnow = function(data){
		//通过数据构建dom
		var dataArr = [{eName:"dataType",cName:"推荐查询数据类型"},{eName:"regionProv",cName:"推荐查询省份"}]
		var keyWord = $("#searchInput").val();
		var noResultBox = $("#noResultBox");
		noResultBox.find(".box").css({"margin-top":"5px","display":"block"});
		noResultBox.find(".mapKnow_rsBox").remove();
		var $box = $('<div class="mapKnow_rsBox"></div>');
		var $ul = $('<ul></ul>');
		for (var i = 0; i < dataArr.length; i++) {
			var key = dataArr[i].eName;
			var name = dataArr[i].cName;
			var $li = $('<li id='+key+'><div class="rsBox_li_title"><span>'+name+':</span></div></li>');
			var child = data[key];
			var $div = $('<div class="rsBox_li_content"></div>');
			for (var j = 0; j < child.length; j++) {
				var $span = $('<span clickFlag='+key+' class="elemClick">'+child[j].name+'</span>').data(child[j]);
				$div.append($span);
			}
			$li.append($div);
			$ul.append($li);
		}
		var $btnBox = $('<div class="mapKnow_btnBox"><div class="mapKnow_btnBox_left"></div><div class="mapKnow_btnBox_right">重新搜索</div></div>');
		var href = "http://kmap.ckcest.cn/newSearch/newIndex?keyWord="+keyWord+"&platformType=2&attrTag=contents&areaWKT=null&areaName=%E5%85%A8%E5%9B%BD&areaPac=0&isStudyArea=false"
		var $link = $('<div class="mapKnow_link">智能推荐由<a href='+href+'>地理信息专业知识服务系统</a>提供<div></div></div>');
		$box.append($ul,$btnBox,$link);
		noResultBox.append($box);
		bindEvent_mapKnow();//绑定事件
	};


	/**
	 * 通用查询请求结果返回处理
	 *
	 * @param {Object}
	 *            result
	 */
	var _resultParse = function(result) {
		//此处打一个断点，判断如果查询无数据的话，返回知识服务的查询数据
		if(result.isZsfw == "true"){
			resultFor_mapKnow(result.zsfwData);
		}

		var mapPage = window.mapPage;
		var mapManager = mapPage.mapManager;
		var attrManager = mapPage.attrManager;
		var listManager = mapPage.listManager;

		if (!result || result.length) {
			console.error("查询未返回结果");
			listManager.NoResultListShowFunction();
		} else if (result.error != "null") {
			console.error(result.error);
			listManager.NoResultListShowFunction();
		} else {
			var dType = result.type;
			if (dType != "search_tag") {
				mapManager.oplayerMap.clearLayers();
			}
			if (result.unionList != null && result.unionList.length > 0) {
				// 设置道路河流区域查询开关为true
				mapPage.isAreaResult = true;
				// 关键字过滤结果处理
				var searchData = result.searchData;
				mapPage.doKeyWordResult(searchData);
				// 设置页面原始关键字和过滤后关键字
				var tempKeyWord = mapPage.getKeyword();
				SearchTools.getInstance().setTempKeyWord(tempKeyWord);
				var keyWord = searchData.keyWord;
				SearchTools.getInstance().setKeyWord(keyWord);
				if (keyWord) {
					$(".searchInput").val(keyWord);
				} else {
					$(".searchInput").val("");
				}
			} else {
				// 设置道路河流区域查询开关为false
				mapPage.isAreaResult = false;
				SearchTools.getInstance().setTempKeyWord(null);
			}

			switch (dType) {
			// 命中道路河流区域
			case "key_area_result":
				var unionList = result.unionList;
				listManager.OtherTypeListShowFunction(unionList);
				mapManager.OtherTypeObj.OtherTypeMapShow(unionList);
				mapManager.oplayerMap.clearSearchLayer();
				// 设置设置绘制图形值为空
				SearchTools.getInstance().setDrawGeom(null);
				break;

			// 图形不相交提示
			case "error_no_touch":
				AlarmTools.getInstance().popUpAlarmByWKTNULL();
				listManager.NoResultListShowFunction();
				mapManager.NoResultMapShowFunction(result);
				break;
			// 图形自相交提示
			case "error_self_touch":
				AlarmTools.getInstance().popUpAlarmByWKTSelf();
				listManager.NoResultListShowFunction();
				mapManager.NoResultMapShowFunction(result);
				break;
			// 标签查询结果集
			case "search_tag":
				attrManager.clearAttr();
				attrManager.addAttrItems(result.resultList);
				break;

			// 无类别查询结果集
			case "search_notype_result":
				// 当前查询条件封装对象
				var searchData = result.searchData;
				SearchTools.getInstance().setSearchData(searchData);
				// 查询详情赋值
				var list = result.itemList;
				mapPage.list = list;
				listManager.NoTypeListShowFunction(list, result.count);
				mapManager.OtherTypeMapShowFunction(list);
				// 关键字过滤结果处理
				mapPage.doKeyWordResult(searchData);
				// 绘图
				var searchGeom = result.searchGeom;
				mapManager.drawResultGeom(searchGeom);
				var count = result.count;
				mapPage.setBigBound([], count);
				break;

			// 自定义产品查询结果集
			case "search_defined_result":
				// 查询区域，用于地图显示
				var searchGeom = result.searchGeom;
				mapManager.drawResultGeom(searchGeom);
				// 查询详情赋值
				mapPage.list = result.itemList;
				// 版本控制
				mapPage.edition();
				// 当前查询条件封装对象
				var searchData = result.searchData;
				SearchTools.getInstance().setSearchData(searchData);
				// 关键字过滤结果处理
				mapPage.doKeyWordResult(searchData);
				// 列表、地图展示
				mapManager.OtherTypeMapShowFunction(result.itemList);
				listManager.OtherTypeListShowFunction(result.itemList,result.count);
				var textList = SearchTools.getInstance().getSearchText(true);
				var count = result.count;
				mapPage.setBigBound(textList, count);
				break;

			// 图幅号查询结果集
			case "search_mapnum_result":
				// 查询详情赋值
				mapPage.list = result.itemList;
				// 版本控制
				mapPage.edition();
				// 列表、地图展示
				mapManager.mapNumShowFunction(result.itemList);
				listManager.OtherTypeListShowFunction(result.itemList);
				var count = result.count;
				mapPage.setBigBound("", count);
				break;

			// 4D产品查询结果集
			case "search_4D_result":
				// 查询区域，用于地图显示
				var searchGeom = result.searchGeom;
				mapManager.drawResultGeom(searchGeom);
				// 查询详情赋值
				mapPage.list = result.itemList;
				// 版本控制
				mapPage.edition();
				mapPage.listManager.normalListObj.scale_A_onOff = false;
				// 当前查询条件封装对象
				var searchData = result.searchData;
				SearchTools.getInstance().setSearchData(searchData);
				// 关键字过滤结果处理
				mapPage.doKeyWordResult(searchData);
				// 列表、地图展示,并绘制查询区域
				listManager.THTypeListShowFunction(result);
				mapManager.THTypeMapShowFunction(result);
				var textList = SearchTools.getInstance().getSearchText(true);
				var count = result.count;
				mapPage.setBigBound(textList, count);
				break;

			// 4D产品查看结果集
			case "look_4D_result":
				// 查询区域，用于地图显示
				var searchGeom = result.searchGeom;
				mapManager.drawLookGeom(searchGeom);
				mapPage.list = result.itemList;
				// 版本控制
				mapPage.edition();
				mapPage.listManager.normalListObj.scale_A_onOff = true;
				listManager.THTypeListShowFunction(result);
				mapManager.THTypeMapShowFunction(result);
				var textList = SearchTools.getInstance().getSearchText(true);
				var count = result.count;
				mapPage.setBigBound(textList, count);
				break;

			// 点类产品查询结果集
			case "search_point_result":
				// 查询区域，用于地图显示
				var searchGeom = result.searchGeom;
				mapManager.drawResultGeom(searchGeom);
				// 查询详情赋值
				mapPage.list = result.itemList;
				// 版本控制
				mapPage.edition();
				// 当前查询条件封装对象
				var searchData = result.searchData;
				SearchTools.getInstance().setSearchData(searchData);
				// 关键字过滤结果处理
				mapPage.doKeyWordResult(searchData);
				// 列表、地图展示,并绘制查询区域
				listManager.PointTypeListShowFunction(result);
				mapManager.PointTypeMapShowFunction(result);
				var textList = SearchTools.getInstance().getSearchText(true);
				var count = result.count;
				mapPage.setBigBound(textList, count);
				break;

			// 点类产品查看结果集
			case "look_point_result":
				// 查询区域，用于地图显示
				var searchGeom = result.searchGeom;
				mapManager.drawLookGeom(searchGeom);
				mapPage.list = result.itemList;
				// 版本控制
				mapPage.edition();
				listManager.PointTypeListShowFunction(result);
				mapManager.PointTypeMapShowFunction(result);
				var textList = SearchTools.getInstance().getSearchText(true);
				var count = result.count;
				mapPage.setBigBound(textList, count);
				break;

			// 卫星影像类产品查询结果集
			case "search_wxyx_result":
				// 查询区域，用于地图显示
				var searchGeom = result.searchGeom;
				mapManager.drawResultGeom(searchGeom);
				// 查询详情赋值
				mapPage.list = result.itemList;
				// 版本控制
				mapPage.edition();
				// 当前查询条件封装对象
				var searchData = result.searchData;
				SearchTools.getInstance().setSearchData(searchData);
				// 关键字过滤结果处理
				mapPage.doKeyWordResult(searchData);
				// 列表、地图展示,并绘制查询区域
				listManager.normalListObj.cleanPointListGroup();
				listManager.WXTypeListShowFunction(result);
				mapManager.WXTypeMapShowFunction(result);
				var textList = SearchTools.getInstance().getSearchText(true);
				var count = result.count;
				mapPage.setBigBound(textList, count);
//				console.log(searchGeom);
//				if (count > 0 && count < 1000 && searchGeom
//						&& attrManager.resType == 8) {
//					var obj = {
//						info : "是否以此区域生成区域订单？",
//						hasCancelBtn : true,
//						btnClick : function() {
//							var rs = shopCar.addAreaGoods(result.itemList,
//									searchGeom);
//							if (rs.fail == 0) {
//								new PopWindow({
//									info : "生成区域订单成功!"
//								});
//							} else {
//								new PopWindow({
//									info : "生成区域订单失败！其中有" + rs.fail + "条数据已下架!"
//								});
//							}
//						}
//					};
//					new PopWindow(obj);
//				}
				break;

			// 卫星影像类产品查看结果集
			case "look_wxyx_result":
				// 查询区域，用于地图显示
				var searchGeom = result.searchGeom;
				mapManager.drawLookGeom(searchGeom);
				mapPage.list = result.itemList;
				// 版本控制
				mapPage.edition();
				listManager.WXTypeListShowFunction(result);
				mapManager.WXTypeMapShowFunction(result);
				var textList = SearchTools.getInstance().getSearchText(true);
				var count = result.count;
				mapPage.setBigBound(textList, count);
				break;

			// 工具栏定位结果集
			case "tool_area_result":
				var list = result.itemList;
				listManager.OtherTypeListShowFunction(list);
				mapManager.OtherTypeMapShowFunction(list);
				break;
			}
		}
		// 销毁查询等待页面
		SearchDownload.destroy();
	}

	/**
	 * 详情查询回调函数
	 */
	var _detailResultParse = function(result) {

		var mapPage = window.mapPage;
		var attrManager = mapPage.attrManager;
		var resType = Number(attrManager.resType);
		mapPage.detailManager.content(result, resType);
	}

	/**
	 * 关键字过滤结果处理 1 类别处理
	 */
	this.doKeyWordResult = function(searchData) {
		var now_resType = searchData.resType;
		var resType = SearchTools.getInstance().getResType();
		if (now_resType != resType) {
			this.attrManager.setResType(now_resType);
		}
	}

	// -------------------------版本选择----------------------------------------
	/**
	 * 版本控制方法
	 */
	this.edition = function() {
		// 清空一次
		mapPage.filterClick = null;

		var key = "idEd";
		if (this.attrManager.resType == "8") {
			key = "tePosition";
		}
		// 版本分类
		var tmer = new teme(mapPage.list, key);
		mapPage.filterResCont = tmer.getResCont();
	}

	/**
	 * 版本选择点击事件 2016-03-08
	 */
	this.idEdClick = function(e) {
		var clickValue = $(this).text();
		if (clickValue == '清除') {
			e.data.edition();
			$('.version-box').text('所有');
			e.data.listManager.normalListObj.detialListShow(e.data.list,
					e.data.attrManager.resType);
			e.data.cancelVersion();
		} else {
			$('.version-box').text(clickValue);
			e.data.filterClick = clickValue;
			var list = e.data.filterResCont[clickValue];
			if (!list)
				return;
			e.data.listManager.normalListObj.detialListShow(list,
					e.data.attrManager.resType);
			e.data.selectVersion(list);
		}
	}

	/**
	 * 版本选择清除 2016-03-08
	 */
	this.cancelVersion = function() {
		var type = this.attrManager.resType;
		if (type == "5" || type == "6" || type == "7") {
			// 图幅类
			this.mapManager.listVersion = null;
			this.mapManager.THTypeObj.resultVectors = [];
			// 刷新图层
			this.mapManager.THTypeObj
					.mapShow4DResultEvent(this.mapManager.THTypeObj.Result4DObj);
		} else if (type == "8") {
			// 航空影像类
			this.mapManager.listVersion = null;
			this.mapManager.WXTypeObj.resultVectors = [];
			this.mapManager.oplayerMap.clearMarkerLayer();
			this.mapManager.oplayerMap.clearResultLayer();
			this.mapManager.WXTypeObj
					.addPolygon(this.mapManager.WXTypeObj.ResultObj.itemList);
		}
	}

	/**
	 * 版本选择选择 2016-03-08
	 */
	this.selectVersion = function(list) {
		var type = this.attrManager.resType;
		if (type == "5" || type == "6" || type == "7") {
			// 图幅类
			this.mapManager.listVersion = list;
			this.mapManager.THTypeObj.resultVectors = [];
			// 刷新图层
			this.mapManager.THTypeObj
					.mapShow4DResultEvent(this.mapManager.THTypeObj.Result4DObj);
		} else if (type == "8") {
			// 航空影像类
			this.mapManager.listVersion = list;
			this.mapManager.WXTypeObj.resultVectors = [];
			this.mapManager.oplayerMap.clearMarkerLayer();
			this.mapManager.oplayerMap.clearResultLayer();
			this.mapManager.WXTypeObj.addPolygon(list);

		}
	}

	// ------------------------------------地图工具栏区域搜索定位------------------------------------------------------------

	/**
	 * 地图工具栏定位搜索 type 查询类型 1 道路 2 铁路 3 水系 4 地名地址
	 */
	this.mapAreaSearch = function(type, keyWord) {

		// 取消地图相关ZOOM事件
		this.mapManager.cancelMapZoomEvent();
		// 取消地图相关CLICK事件
		this.mapManager.cancelMapClickEvent();
		// 取消地图相关MOVED事件
		this.mapManager.cancelMapMovedEvent();

		if (keyWord == "") {
			AlarmTools.getInstance().popUpAlarmBySearchKey();
			return;
		}

		switch (type) {

		case "0":// 行政区域
		case "1":// 道路
		case "2":// 铁路
		case "3":// 水系
		case "4":// 湖泊

			var data = {
				"type" : type,
				"key" : keyWord
			};
			this.listManager.showStatList("loadingBox");
			this.mapAreaSearchAjax(data);
			break;
		case "5":// 地名地址点 天地图查询
			this.listManager.showStatList("loadingBox");
			this.tdtSearcher.tianDiTuSearch(keyWord);
			break;
		case "6":// 图幅号定位
			this.positionMapNum(keyWord);
			break;
		}
	}

	/***************************************************************************
	 * ajax请求 地图工具栏区域搜索
	 */
	this.mapAreaSearchAjax = function(data) {
		$.post('./mapDataAction.do?method=mapAreaSearch', data, _resultParse,
				'json');
	};

	// ------------------------------------购物车相关方法--------------------------------------------------------------------

	/***************************************************************************
	 * 列表选择数据加入购物车（单个数据）
	 */
	this.addShopCar = function(id) {
		var list = this.getResultList();
		for (var i = 0; i < list.length; i++) {
			var item = list[i];
			if (item.id == id) {
				SearchTools.getInstance().getResType();
				var type = SearchTools.getInstance().getResType();
				var wkt = SearchTools.getInstance().getDrawGeom();
				var rs = window.shopCar.addGoods(item,wkt,type,this.mapCarManager.refresh);
				break;
			}
		}
		var info = "";
		if (rs.code !== 200) {
			info = "保存服务器失败";
		} else {
			if (rs && rs.success.num !== 0) {
				info = "成功加入成果车";
			} else if (rs && rs.isExist.num !== 0) {
				info = "此数据成果车已存在";
			} else if (rs && rs.notExist.num !== 0) {
				info = "该成果数据暂不能订购";
			} else {
				info = "返回错误";
			}
		}

		var pop = new PopWindow({
			info : info
		});

	};

	/***************************************************************************
	 * 列表选择数据加入购物车（批量数据）
	 */
	this.addShopCars = function(values) {
		var list = this.getResultList();
		var goodsList = [];
		for (var j = 0; j < values.length; j++) {
			var id = values[j].id;
			for (var i = 0; i < list.length; i++) {
				var item = list[i];
				if (item.id === id) {
					goodsList.push(item);
					break;
				}
			}
		}
		var rs = window.shopCar.addGoods(goodsList,SearchTools.getInstance().getDrawGeom(),SearchTools.getInstance().getResType(),this.mapCarManager.refresh);

		var info = "";
		if (rs.code !== 200) {
			info = "保存服务器失败";
		} else {
			info += rs.success.txt;
			if (rs.isExist.num !== 0) {
				info += "<br>" + rs.isExist.txt;
			}
			if (rs.notExist.num !== 0) {
				info += "<br>" + rs.notExist.txt;
			}
		}
		var pop = new PopWindow({
			info : info
		});
	};

	this.setBigBound = function(list, totle) {
		var str = "";
		for (var i = 0; i < list.length; i++) {
			if (i == 0) {
				str += list[i];
			} else {
				str += ">" + list[i];
			}
		}
		if (totle != null) {
			// if(totle > 9999){
			// var tmp = (totle/10000).toString();
			// totle = tmp.substr(0,tmp.indexOf(".")+3)+"万";
			// }
			$(".bigBound .bbText").text(str);
			$(".bigBound .bbRightText span").text("共" + totle + "条");
		} else {
			$(".bigBound .bbText").text(str);
			$(".bigBound .bbRightText span").text("");
		}
	}
	// ------------------------------------初始化相关方法--------------------------------------------------------------------

	/**
	 * 绑定键盘回车事件
	 */
	var _keyDown = function() {

		$("#key").keydown(function(e) {
			if (e.keyCode == 13) {
				alert(SearchTools.getInstance().getQueryData());
			}
		});
	}

	/**
	 * 初始化方法
	 */
	this.init = function(def) {

		// 地图管理类
		this.mapManager = new MapManager({
			father : this,
			mapName : "map"
		});

		// 属性管理类
		this.attrManager = new AttrManager({
			father : this,
			dirTypes : def.dirTypes
		});

		// 列表管理类
		this.listManager = new ListManager({
			father : this
		});

		// 天地图查询类
		this.tdtSearcher = new TiandituSearch({
			father : this,
			listManager : this.listManager,
			mapManager : this.mapManager
		});

		// 地图购物车管理类
		this.mapCarManager = new MapCarManager({
			father : this,
			vectorLayer : this.mapManager.oplayerMap.getShopLayer(),
			markerLayer : this.mapManager.oplayerMap.getShopMarkerLayer()
		});

		// 成果详情管理类
		this.detailManager = new DetailManager({
			father : this
		});

		// 绑定键盘回车事件
		_keyDown.call(this);

		// 绑定列表版本点击事件
		$('.version-list p').live('click', this, this.idEdClick);

	};

	this.init.apply(this, arguments);
}