/**
 * 购物车类(此类为添加购物车信息)
 */
var ShoppingCart = function() {
	var CLASS = ShoppingCart;
	var thi$ = CLASS.prototype;
	var storage = null;// 缓存对象
	var goods = null;
	var resType = null;
	var oldList = null;
	var oldWkt = null;
	var oldType = null;
	var oldAddCallBack = null;

	var carKey = "newShopCar";// 购物车在缓存中的字段值
	var countKey = "shopCarCount";// 购物车中的数量
	var mergeKey = "mergeFlag";// 购物车在缓存中的同步标识 1代表已同步 0代表未同步
	var loginKey = "loginFlag";// 登录标识
	/*模拟地形图的添加购物车*/
	var dxtAddGoods = function(list, wkt, type,addCallBack){
		oldList = list;
		oldWkt = wkt;
		oldType = type;
		oldAddCallBack = addCallBack;
		var info = buildDxtInfo(list);
		var pop = new PopWindow({
            info : info,
            btnClick:confirmAddGoods
        });
		bindInfo();
	}
	var starAddGoods = function(numList){
		var newList = [];
		for(var i=0;i<numList.length;i++){
			for(var j=0;j<numList[i];j++){
				if(oldList.length){
					newList.push(oldList[i]);
				}else{
					newList.push(oldList);
				}
			}
		}
		var list = newList;
		var rsObj = null;
		rsObj = toAddGoods(newList,oldWkt,oldType,oldAddCallBack);
		return rsObj;
	}
	var toAddGoods = function(list, wkt, type,addCallBack){
		resType = type;
		var loginFlag = storage.getItem(loginKey);
		if (loginFlag !== "1") {
			location.href = "./user.do?method=loginPage";
			return;
		}

		if (typeof (list.length) === "undefined") {
			list = [ list ];
		}
		var idArr = [];
		for ( var i in list) {
			idArr.push(list[i].id);
		}
		if (list.length > 500) {
			// 显示正在加入购物车
			SearchDownload && SearchDownload.create();
		}
		var rsObj = null;

		var tmpUrl = "./shopCar.do?method=webBatchAddShopCar";
		if (isSecret) {
			tmpUrl = "./sysDocking.do?method=webBatchAddShopCar";
		}
		var ajaxObj = {
			url : tmpUrl,
			data : {
				goodIDS : idArr.join(","),
				wkt : wkt,
				typeID : type
			},
			isAsync : false,
			callBack : function(rs) {
				rs = typeof (rs) === "string" ? JSON.parse(rs) : rs;
				rsObj = _saveToLocal(list, rs,addCallBack);
				//隐藏正在加载的操作
				SearchDownload && SearchDownload.destroy();
			}
		};
		comAjax(ajaxObj); //common.js中
		return rsObj;
	}
	//绑定点击弹窗内容的事件
	var bindInfo = function(){
		$(".delNum").click(function(){
			var num = parseInt($(this).parent().find(".number").text());
			if(num != 1){
				var nowNum = num - 1;
				$(this).parent().find(".number").text(nowNum);
				if(nowNum == 1){
					$(this).find("img").attr("src","./public/style/images/store/infoDel-end.png")
				}
			}
		})
		$(".addNum").click(function(){
			var num = parseInt($(this).parent().find(".number").text());
			var nowNum = num + 1;
			$(this).parent().find(".number").text(nowNum);
			if(nowNum != 1){
				$(this).parent().find(".delNum").find("img").attr("src","./public/style/images/store/infoDel-begin.png")
			}
		})
	}
	//构造弹窗里面HTML
	var buildDxtInfo = function(list){
		var content = $('<div class="info-body"><div class="info-title"><div class="mapNum">新图号</div><div class="time">出版时间</div><div class="count">数量</div></div></div></div>');
		var infoContent = $('<div class="info-content"></div>')
		if(list.length){
			for(var i=0;i<list.length;i++){
				var mapNum = list[i].idNewMapNum;
				var time = list[i].idVersionType;
				var idMapScale=list[i].idMapScale;
				var idEd      =list[i].idEd;
				if(idMapScale=="50000"&&idEd=="2012版"){//是否纸质模拟地形图 ps:yd 2017/11/13
					var li = $('<div class="info-content-li"><div class="mapNum">'+mapNum+'</div><div class="time">'+time+'</div><div class="count"> <div class="delNum"><img src="./public/style/images/store/infoDel-end.png"></div><div class="number">1</div><div class="delNum"><img src="./public/style/images/store/infoDel-end.png"></div></div>')
				}else{
					var li = $('<div class="info-content-li"><div class="mapNum">'+mapNum+'</div><div class="time">'+time+'</div><div class="count"> <div class="delNum"><img src="./public/style/images/store/infoDel-end.png"></div><div class="number">1</div><div class="addNum"><img src="./public/style/images/store/infoAdd-begin.png"></div></div></div>')
				}
				infoContent.append(li);
			}
		}else{
			var mapNum = list.idNewMapNum;
			var time = list.idVersionType;
			var idMapScale=list.idMapScale;
			var idEd      =list.idEd;
			if(idMapScale=="50000"&&idEd=="2012版"){//是否纸质模拟地形图 ps:yd 2017/11/13
				var li = $('<div class="info-content-li"><div class="mapNum">'+mapNum+'</div><div class="time">'+time+'</div><div class="count"> <div class="delNum"><img src="./public/style/images/store/infoDel-end.png"></div><div class="number">1</div><div class="delNum"><img src="./public/style/images/store/infoDel-end.png"></div></div>')
			}else{
				var li = $('<div class="info-content-li"><div class="mapNum">'+mapNum+'</div><div class="time">'+time+'</div><div class="count"> <div class="delNum"><img src="./public/style/images/store/infoDel-end.png"></div><div class="number">1</div><div class="addNum"><img src="./public/style/images/store/infoAdd-begin.png"></div></div></div>')
			}
			infoContent.append(li);
		}
		content.append(infoContent);
		content = content.prop("outerHTML");
		return content;
	}
	//ps:yd 2017/11/14 特殊处理如果模拟地形图弹出提示
	var openInfoNews = function(rs){
		var info = "";
		if (rs.code !== 200) {
			info = "保存服务器失败";
		} else {
			if(rs.other){
				info = rs.other;
			}else if(rs.repeat.num !== 0){
				info = rs.repeat.txt;
			}else if (rs.success.num !== 0) {
				info = "成功加入成果车";
			} else if (rs.isExist.num !== 0) {
				info = "此数据成果车已存在";
			} else if (rs.notExist.num !== 0) {
				info = "该成果数据暂不能订购";
			} else {
				info = "返回错误";
			}
		}
		var pop = new PopWindow({
			info : info
		});
		throw new Error("stop next function!");
	}
	//如果是模拟地形图的话，为确定方法
	var confirmAddGoods = function(){
		var List = $(".info-body").find(".number");
		var numList = [];
		for(var i=0;i<List.length;i++){
			numList.push(parseInt($(List[i]).text()));
		}
		var rs = starAddGoods(numList);
		openInfoNews(rs);
	}
	/*如果是航空影像没有wkt的情况*/
	var PopToaddGoods = function(list, wkt, type,addCallBack){
		oldList = list;
		oldWkt = wkt;
		oldType = type;
		oldAddCallBack = addCallBack;
		//弹窗提示框
		var info = "未设置范围，是否以摄区范围生成成果范围";
		var pop = new PopWindow({
            info : info,
            btnClick: NoWktAddGoods,
            hasCancelBtn:true
		});
	}
	//弹窗点击确定的事件
	var NoWktAddGoods = function(){
		toAddGoods(oldList,oldWkt,oldType,oldAddCallBack);
	}
	/*国情普查特定处理添加数据的时候，选择图层*/
	var _gqpcAddGoods = {
		//发送请求加入购物车
		ajaxFun:function(list,ajData,addCallBack){
			if (list.length > 500) {
				// 显示正在加入购物车
				SearchDownload && SearchDownload.create();
			}
			var rsObj = null;
			var tmpUrl = "./shopCar.do?method=webBatchAddShopCar";
			if (isSecret) {
				tmpUrl = "./sysDocking.do?method=webBatchAddShopCar";
			}
			var ajaxObj = {
				url : tmpUrl,
				data : ajData,
				isAsync : false,
				callBack : function(rs) {
					rs = typeof (rs) === "string" ? JSON.parse(rs) : rs;
					if(rs.statusCode == 400){
						comGoLogin();
						return false;
					}
					rsObj = _saveToLocal(list, rs,addCallBack);
					//隐藏正在加载的操作
					SearchDownload && SearchDownload.destroy();
					//显示提示
					_gqpcAddGoods.newsLayer(rsObj);
				}
			};
			comAjax(ajaxObj); //common.js中
		},
		//构建弹窗dom
		createLayerHtml : function(rs){
			var $gqpcBox = $("#gqpcBox").empty();
	    	var $html = $('<ul class="gqpcBox-show"><li class="gqpcBox-title">层级选择</li><li class="gqpcBox-news">*请选择您需要的层级</li><li class="gqpcBox-show-box"></li></ul>');
	    	var $box = $html.find(".gqpcBox-show-box");
	    	var $all = $('<div id="gqpcBox-elem-all" title="全部" class="gqpcBox-elem gqpcBox-elem-select">全部</div>').data({name:'全部',id:'all'});
	    	$box.append($all);
	    	for (var i = 0; i < rs.length; i++) {
	    		var obj = rs[i];
    			var $btn = $('<div class="gqpcBox-elem" title='+obj.layerName+'>'+obj.layerName+'</div>').data(obj);
	    		$box.append($btn);
	    	}
	    	$gqpcBox.append($html);
	    },
	    //因为弹窗的关系所有不同同步加载，自定义提示弹窗
	    newsLayer:function(rs){
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
	    },
		//弹窗显示
		openLayer:function(list, wkt, type,addCallBack,layerList){
			_gqpcAddGoods.createLayerHtml(layerList);
			layer.open({
			  	type: 1,
			  	title: false,
			  	closeBtn: 1,
			  	shadeClose: false,
			  	skin: 'yourclass',
			  	area: ['351px', '256px'], //宽高
	    		content: $("#gqpcBox"),
	    		btn: ['确定', '取消'],
	    		yes: function(index, layero){
	    			//获取数据
	    			var ajData = _gqpcAddGoods.getAjData(list,wkt,type,layerList);
	    			layer.close(index);
	    			_gqpcAddGoods.ajaxFun(list,ajData,addCallBack);
	    		},
	    		btn2: function(index, layero){
	    			layer.close(index);
	    			return false;
				}
			});
		},
		//获取选择的数据
		getAjData:function(list,wkt,type,layerList){
			var layerChoose = [];
			var domList = $("#gqpcBox").find(".gqpcBox-elem-select");
			for (var i = 0; i < domList.length; i++) {
				layerChoose.push($(domList[i]).data());
			}
			//如果是点击的全部，则发送所有的图层数据
			if(layerChoose[0].id == "all"){
				layerChoose = layerList;
			}
			var idArr = [];
			var goodLayerIDs = "";
			for ( var i in list) {
				//传入图层数据
				list[i]['layerList'] = layerChoose;
				var obj = list[i]
				idArr.push(obj.id);
				var $goodLayerID = obj.id;
				for (var j = 0; j < layerChoose.length; j++) {
					var lObj = layerChoose[j];
					$goodLayerID += ','+lObj.id;
				}
				goodLayerIDs += $goodLayerID + ";";
			}
			goodLayerIDs = goodLayerIDs.substring(0,goodLayerIDs.length-1);
			var goodIDS = idArr.join(",");
			var ajData = {'goodIDS':goodIDS,'goodLayerIDs':goodLayerIDs,'typeID':type,'wkt':wkt};
			return ajData;
		},
		//绑定点击事件
		bindEvent:function(){
			$("#gqpcBox").off();
			$("#gqpcBox").on("click",".gqpcBox-elem",function(){
				var obj = $(this).data();
				if($(this).attr("class") == "gqpcBox-elem"){
					$(this).attr("class","gqpcBox-elem gqpcBox-elem-select");
					if(obj.id == "all"){
						$(this).siblings().attr("class","gqpcBox-elem");
					}else{
						$(this).siblings("#gqpcBox-elem-all").attr("class","gqpcBox-elem");
					}
				}else if($(this).siblings(".gqpcBox-elem-select").length){//必须选择一个
					$(this).attr("class","gqpcBox-elem");
				}
			})
		},
		init:function(list, wkt, type,addCallBack){
			//对单条数据进行转化
			if (typeof (list.length) === "undefined") {
				list = [ list ];
			}
			//最先获取图层数据
			var ajaxObj = {
				url : './shopCar.do?method=listDataLayer',
				data : {},
				isAsync : false,
				dataType:'json',
				callBack : function(rs) {
					_gqpcAddGoods.openLayer(list, wkt, type,addCallBack,rs);
				}
			};
			comAjax(ajaxObj); //common.js中
			_gqpcAddGoods.bindEvent();
		}
	};
	// 添加商品
	thi$.addGoods = function(list, wkt, type,addCallBack) {
		resType = type;
		/*此处变动，如果resType=11的话（模拟地形图）,改变增加一个弹窗，可以选择数量*/
		if(resType == 11 && isSecret){
			dxtAddGoods(list, wkt, type,addCallBack);
		}else if(resType == 123){//在此处断点进行国情普查特殊处理 time:2017/9/29  autor:yd
			_gqpcAddGoods.init(list, wkt, type,addCallBack);
		}else if(resType == 8 && !wkt){
			PopToaddGoods(list, wkt, type,addCallBack);
		}else{
			var loginFlag = storage.getItem(loginKey);
			if (loginFlag !== "1") {
				location.href = "./user.do?method=loginPage";
				return;
			}

			if (typeof (list.length) === "undefined") {
				list = [ list ];
			}
			var idArr = [];
			for ( var i in list) {
				idArr.push(list[i].id);
			}
			if (list.length > 500) {
				// 显示正在加入购物车
				SearchDownload && SearchDownload.create();
			}
			var rsObj = null;
			var tmpUrl = "./shopCar.do?method=webBatchAddShopCar";
			if (isSecret) {
				tmpUrl = "./sysDocking.do?method=webBatchAddShopCar";
			}
			var ajaxObj = {
				url : tmpUrl,
				data : {
					goodIDS : idArr.join(","),
					wkt : wkt,
					typeID : type
				},
				isAsync : false,
				callBack : function(rs) {
					rs = typeof (rs) === "string" ? JSON.parse(rs) : rs;
					rsObj = _saveToLocal(list, rs,addCallBack);

					//隐藏正在加载的操作
					SearchDownload && SearchDownload.destroy();
				}
			};
			comAjax(ajaxObj); //common.js中
			return rsObj;
		}

	}

	// 删除商品
	thi$.delGoods = function(list) {

	}

	// 通过id获取商品
	thi$.getGoodByID = function(id) {
		for ( var i in goods) {
			if (i !== "keyStr") {
				var list = goods[i];
				for ( var j in list) {
					if (list[j].id == id) {
						return list[j];
					}
				}
			}
		}
		return null;
	}
	// 通过id删除商品
	thi$.delById = function(idArr,delCallBack) {
		if (typeof (idArr) === "string") {
			idArr = [ idArr ];
		}
		var idStr = idArr.join(",");
		var ajaxObj = {
			url : "./shopCar.do?method=webBatchDelShopCar",
			data : {
				goodIDS : idStr
			},
			callBack : function(rs) {
				thi$.setMergeFlag("0");
				thi$.getToService(delCallBack);
			}
		};
		comAjax(ajaxObj); //common.js中

	}

	//删除本地的内容 此处暂未使用
	var _delLocal = function(idArr){
		var len = idArr.length;
		for(var k in idArr){
			var delFlag = false;
			for (var i in goods) {
				if (i !== "keyStr") {
					var list = goods[i];
					for ( var j in list) {
						if (list[j].id == idArr[k]) {
							list.splice(j, 1);
							if (list.length == 0) {
								var keyArr = goods["keyStr"];
								for ( var l in keyArr) {
									if (keyArr[l] === i) {
										keyArr.splice(l, 1);
										goods["keyStr"] = keyArr;
										break;
									}
								}
								delete goods[i];
								break;
							}
							delFlag = true;
							break;
						}
					}
				}
				if(delFlag){
					break;
				}
			}
		}
		_setGoods(goods);
		_setCount(thi$.getCount() - len);

	}


	// 保存到本地 list是新的列表 rs是保存到服务器的返回结果
	var _saveToLocal = function(list, rs,addCallBack) {
		var rsObj = {
			code : 200,
			success : {
				num : 0,
				txt : ""
			},
			notExist : {
				num : 0,
				txt : ""
			},
			isExist : {
				num : 0,
				txt : ""
			},
			repeat : {
				num : 0,
				txt : ""
			}
		}
		if (rs.statusCode !== 200) {
			rsObj.code = rs.statusCode;
			return rsObj;
		}
		var isExistArr = rs.isExist;
		var notExistArr = rs.notExist;
		var repeat4D = rs.repeat4D;
		var repeatPoint = rs.repeatPoint;
		
		rsObj.message   = rs.message;
		if(rs.crsDatumOnly == 0){
			rsObj.other = "数据重复，成果车已存在！<br>重复数据量：'"+rs.count+"'";
		}

		if(rs.crsDatumOnly == 1){
			rsObj.other = "坐标系不唯一（同一数据类型相同图幅只能选择一套坐标系）";
		}
		if(rs.crsDatumOnly == 2){
			rsObj.other = "坐标系不唯一（同一数据类型相同图幅只能选择一套坐标系）<br>已选坐标系：'"+rs.carCrsDatum+"'";
		}
		if(rs.crsDatumOnly == 3){
			rsObj.other = "坐标系不唯一（同一数据类型相同点名点号只能选择一套坐标系）";
		}
		if(rs.crsDatumOnly == 4){
			rsObj.other = "坐标系不唯一（同一数据类型相同点名点号只能选择一套坐标系）<br>已选坐标系：'"+rs.carCrsDatum+"'";
		}
		if (isExistArr && isExistArr.length !== 0) {
			rsObj.isExist.num = isExistArr.length;
			rsObj.isExist.txt = isExistArr.length + "条成果车已存在!";
		}
		if (notExistArr && notExistArr.length !== 0) {
			rsObj.notExist.num = notExistArr.length;
			rsObj.notExist.txt = notExistArr.length + "条数据不存在!";
		}
		if(repeat4D && repeat4D.length !== 0){
			rsObj.repeat.num = repeat4D.length;
			repeat4D = unique(repeat4D);;
			var t = "图幅号重复（同一数据类型，同一图幅，只能选择一幅）";
			rsObj.repeat.txt = t + "<br>重复图幅号："+repeat4D.join(",");
		}
		if(repeatPoint && repeatPoint.length !== 0){
			rsObj.repeat.num = repeatPoint.length;
			repeatPoint = unique(repeatPoint);;
			var t = "点名点号重复（同一数据类型，同一点名点号，只能选择一个）";
			rsObj.repeat.txt = t + "重复点名点号："+repeatPoint.join(",");
		}

		var successNum = rs.addCount;
		rsObj.success.num = successNum;
		rsObj.success.txt = successNum + "条加入成功！";

		thi$.setMergeFlag("0");
		thi$.getToService(addCallBack);

		return rsObj;
	}

	var unique = function(arr){
		var n = {},r=[]; //n为hash表，r为临时数组
		for(var i = 0; i < arr.length; i++) //遍历当前数组
		{
			if (!n[arr[i]]) //如果hash表中没有当前项
			{
				n[arr[i]] = true; //存入hash表
				r.push(arr[i]); //把当前数组的当前项push到临时数组里面
			}
		}
		return r;
	}


	// 获取到本地的商品内容
	thi$.getGoods = function() {
		var att = unescape(storage.getItem(carKey));
		if (att === null || att === "null" || att === "undefined") {
			att = "{}";
		}
		att = $.parseJSON(att);
		//获取回来的数据通过id进行请求添加wkt数据(为了避免本地储存超限，所以本地不存入wkt数据)
		var attrW = _getAttaddWkt(att);
		return attrW;
	}
	//设置本地缓存
	thi$.setGoods = function(rs){
		_setGoods(rs);
	}
	//后台根据id进行请求获取wkt
	var _getAttaddWkt = function(att){
		for(key in att){
			var ids = "";
			var thiList = [];
			if(key === "keyStr") continue;
    		var data = att[key];
    		for(var i = 0;i<data.length;i++){
				if(data[i]._id){
					ids += data[i]._id + ",";
				}
			}
    		ids = ids.substring(0,ids.length-1);
    		//发送请求获取wkt参数
    		var ajaxObj = {
				url : "./shopCar.do?method=getGeombyIds",
				data : {ids:ids},
				isAsync : false,
				callBack : function(rs) {
					rs = typeof(rs) === "string" ? eval('('+rs+')') : rs;
					thiList = rs;
				},
				errorCallBack:function(){
					console.log("获取geom失败")
				}
			};
			comAjax(ajaxObj);
			var oneData = _conductOneW(data,thiList);//把返回的gemo参数加上去
			att[key] = oneData;
    	}
		return att;
	}
	//传入一种类型的不含wkt的数组，和含有的数组来进行合并
	var _conductOneW = function(nData,hData){
		for(var i=0;i<nData.length;i++){
			nData[i].geom = hData[i].geom;
		}
		return nData;
	}

	// 设置成果到缓存
	var _setGoods = function(g) {
		goods = _getAttaddWkt(g);
		var value = escape(JSON.stringify(g));
		storage.setItem(carKey, value);
	}

	// 获取数量
	thi$.getCount = function() {
		var count = parseInt(storage.getItem(countKey));
		if (count === null || count === "null" || count === "undefined") {
			count = 0;
		}
		return count;
	}
	// 设置数量
	_setCount = function(count) {
		storage.setItem(countKey, count);
		$("#shopCar").html("成果车(" + count + ")");
		$('.sizeNum').text(count);
	}

	// 清空
	thi$.clear = function() {
		storage.setItem(countKey, 0);
		storage.setItem(carKey, escape("{}"));
		storage.setItem(mergeKey, "0");// 设置同步状态
		$("#shopCar").html("成果车(0)");
		$('.sizeNum').text(0);
	}

	// 显示成果车数量
	thi$.showCount = function() {
		var count = thi$.getCount();
		$("#shopCar").html("成果车(" + thi$.getCount() + ")");
		$('.sizeNum').text(count);
	}

	// 从服务器获取数据
	thi$.getToService = function(syncCallBack) {
		var flag = storage.getItem(mergeKey);
		if (flag === null || flag === "null" || flag === "0") {// 未同步
			var ajaxObj = {
				url : "./shopCar.do?method=webGetShopCarContent",
				data : {},
				callBack : function(rs) {
					rs = typeof(rs) === "string" ? eval('('+rs+')') : rs;
					if(rs.statusCode === 200){
						var count = rs.count;
						delete rs["statusCode"];
						delete rs["count"];
						thi$.setMergeFlag("1");
						_setGoods(rs);
						_setCount(count);

						syncCallBack && syncCallBack();
						//在成果车页面有specialShopCar函数,如果存在则调用生成页面
						window.specialShopCar && window.specialShopCar();
					}
				}
			};
			comAjax(ajaxObj);
		}else{
			//在成果车页面有specialShopCar函数,如果存在则调用生成页面
			window.specialShopCar && window.specialShopCar();
		}
	}

	//涉密成果页面 特殊处理
	thi$.secretDo = function(){
		var flag = storage.getItem(mergeKey, flag);
		if(flag == "1"){
			storage.setItem(mergeKey, "0");
			thi$.getToService();
		}
	}

	thi$.setMergeFlag = function(flag) {
		storage.setItem(mergeKey, flag);
	}
	thi$.setLoginFlag = function(flag) {
		storage.setItem(loginKey, flag);
	}

	// 初始化函数
	thi$._init = function() {
		storage = getStorage();
		goods = thi$.getGoods();
		thi$.showCount();
	};
	this._init.apply(this, arguments);
}
var getStorage = function(type) {
	if (typeof (window.localStorage) == "undefined") {
		if (typeof (window.globalStorage) != "undefined") {
			// try/catch for file protocol in Firefox
			try {
				window.localStorage = window.globalStorage;
			} catch (e) {
				console.err("无法创建globalStorage");
			}
		} else {
			var storage = new IeStorage();
			window.localStorage = storage;
		}
	} else if (type === "session") {
		return window.sessionStorage;
	}
	return window.localStorage;
}

// 此类用于创建和管理缓存，yl.tool.IeStorage对象是针对ie7以下不支持window.localStorage而模拟的对象。
// 使用时请调用 yl_sys.getStorage()。
IeStorage = function() {
	var CLASS = IeStorage, thi$ = CLASS.prototype;
	if (CLASS.__defined__) {
		this.init.apply(this, arguments);
		return;
	}
	CLASS.__defined__ = true;

	var attrKey = "localStorage";

	this.cleanKey = function(key) {
		return key
				.replace(
						/[^-._0-9A-Za-z\xb7\xc0-\xd6\xd8-\xf6\xf8-\u037d\u37f-\u1fff\u200c-\u200d\u203f\u2040\u2070-\u218f]/g,
						"-");
	};

	this.setItem = function(key, value) {
		this.userData.load(attrKey);
		key = this.cleanKey(key);

		if (!this.userData.getAttribute(key)) {
			this.length++;
		}
		this.userData.setAttribute(key, value);

		this.userData.save(attrKey);
	};

	this.getItem = function(key) {
		this.userData.load(attrKey);
		key = this.cleanKey(key);
		return this.userData.getAttribute(key);
	};

	this.removeItem = function(key) {
		this.userData.load(attrKey);
		key = this.cleanKey(key);
		this.userData.removeAttribute(key);

		this.userData.save(ttrKey);
		this.length--;
		if (this.length < 0) {
			this.length = 0;
		}
	};

	this.clear = function() {
		this.userData.load(attrKey);
		var i = 0;
		while (attr = this.userData.XMLDocument.documentElement.attributes[i++]) {
			this.userData.removeAttribute(attr.name);
		}
		this.userData.save(attrKey);
		this.length = 0;
	};

	this.key = function(key) {
		this.userData.load(attrKey);
		return this.userData.XMLDocument.documentElement.attributes[key];
	};

	this.init = function() {
		var userData = this.userData = document.createElement("input");

		userData.style.display = "none";
		document.getElementsByTagName("head")[0].appendChild(userData);
		userData.addBehavior("#default#userdata");

		userData.load(attrKey);
		this.length = userData.XMLDocument.documentElement.attributes.length;
	};
	this.init.apply(this, arguments);
};