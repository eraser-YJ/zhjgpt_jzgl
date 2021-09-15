function setGoods(){
	var goods={
		'count': 2,
		'keyStr': [
           
        ]
	};
	var goodsAllCount=0;
	if(GloblExportJSON.length==0)return;
	for (var i=0;i<GloblExportJSON.length;i++) {
		goods.keyStr.push(GloblExportJSON.layerId);
		var fl=[];
		for(var j=0;j<GloblExportJSON.results.length;j++){
			fl.push(GloblExportJSON.results[j].attributes);
		}
		goods[GloblExportJSON.layerId] = fl;
	}
}
var MapCarManager = function() {
    var thi$ = this;
    this.selectGoods = {}
    this.shopCar = window.shopCar;
    this.maxFlag = "0"; //最大最小化标志  最大化时为"1" 最小化时为"0"
    this.nowNum = 1;
    this.list = []; //保存重构后的数据
    this.proTypeName = { //显示名称
        "1": "重力点",
        "2": "水准点",
        "3": "三角点",
        "4": "GNSS成果",
        "5": "矢量地图数据",
        "6": "数字高程模型",
        "7": "正射影像",
        "8": "航空影像",
        "9": "卫星影像",
        "10": "数字栅格地图",
        "11": "模拟地图数据",
        "pro": "成果数据"
    };
    this.proTypeHtml = {};

    this.proNum = 0;
    this.resType = "-1"; //代表没有选择数据类型
    this.countList = [];

    /**
     * 打开购物车
     */
    this.open = function(e) {
        if ($(".shoppingCar").height() != 0) {
            $('.shoppingCar').css({ 'height': '0px', 'border': '0px solid #aaaaaa' });
            return;
        }

        $('.shoppingCar').css({ 'height': '273px', 'border': '1px solid #aaaaaa' });

        e.data.refresh(1);
    }

    this.goods = {
        'count': 2,
        '2': [{
                "buyNum": "1",
                "crsDatum": "2000国家大地坐标系",
                "crsVertCRS": "1985国家高程基准",
                "data_type": "4",
                "dtDistorCont": "国家基础地理信息中心",
                "dtLinkage": "http://www.ngcc.cn/",
                "dtMapMemo": "喷绘",
                "fileid": "C94CDE9618E24C1087909F8245331023",
                "geom": "MULTIPOLYGON (((125.00000000000011 43.979167000000075, 125.00000000000011 44, 125.03125 44, 125.0625 44, 125.09375 44, 125.125 44, 125.15625000000011 44, 125.18750000000011 44, 125.21875000000023 44, 125.25 44, 125.25 43.979167000000075, 125.25 43.95833300000004, 125.25 43.93750000000006, 125.25 43.91666700000013, 125.25 43.895833000000096, 125.25 43.875000000000114, 125.25 43.85416700000002, 125.25 43.83333299999998, 125.21875000000023 43.83333299999998, 125.18750000000011 43.83333299999998, 125.15625000000011 43.83333299999998, 125.125 43.83333299999998, 125.09375 43.83333299999998, 125.0625 43.83333299999998, 125.03125 43.83333299999998, 125.00000000000011 43.83333299999998, 125.00000000000011 43.85416700000002, 125.00000000000011 43.875000000000114, 125.00000000000011 43.895833000000096, 125.00000000000011 43.91666700000013, 125.00000000000011 43.93750000000006, 125.00000000000011 43.95833300000004, 125.00000000000011 43.979167000000075)))",
                "id": "33d3ffe6ac724ff494473a07ccbad48e",
                "idEd": "2015版",
                "idMapCurrency": "2013",
                "idMapScale": "50000",
                "idMapTitle": "合心镇",
                "idNewMapNum": "K51E001021",
                "idOldMapNum": "K-51-011-A",
                "idProject": "1:5万基础地理信息数据库动态更新项目（2013年）",
                "idResType": "模拟地形图",
                "idVersionType": "2014",
                "mdDateSt": "",
                "mdRpOrgName": "国家基础地理信息中心",
                "mode": "0",
                "orgId": "12aff3f141664643b69b08cab7b738d3",
                "resType": "11",
                "secClass": "机密",
                "sourceCode": "0",
                "std": "0",
                "storeId": "2",
                "sysidVersionType": "2014-01-01",
                "time": "2016-08-17 15:43",
                "_id": "33d3ffe6ac724ff494473a07ccbad48e"
            },
            {
                "buyNum": "1",
                "crsDatum": "2000国家大地坐标系",
                "crsVertCRS": "1985国家高程基准",
                "data_type": "4",
                "dtDistorCont": "国家基础地理信息中心",
                "dtLinkage": "http://www.ngcc.cn/",
                "fileid": "DCEECC4F635F42A3A97B723B8DBF8B19",
                "geom": "MULTIPOLYGON (((124.75000000000011 43.979167000000075, 124.75000000000011 44, 124.78125000000011 44, 124.81250000000011 44, 124.84375 44, 124.875 44, 124.90625 44, 124.9375 44, 124.96875000000011 44, 125.00000000000011 44, 125.00000000000011 43.979167000000075, 125.00000000000011 43.95833300000004, 125.00000000000011 43.93750000000006, 125.00000000000011 43.91666700000013, 125.00000000000011 43.895833000000096, 125.00000000000011 43.875000000000114, 125.00000000000011 43.85416700000002, 125.00000000000011 43.83333299999998, 124.96875000000011 43.83333299999998, 124.9375 43.83333299999998, 124.90625 43.83333299999998, 124.875 43.83333299999998, 124.84375 43.83333299999998, 124.81250000000011 43.83333299999998, 124.78125000000011 43.83333299999998, 124.75000000000011 43.83333299999998, 124.75000000000011 43.85416700000002, 124.75000000000011 43.875000000000114, 124.75000000000011 43.895833000000096, 124.75000000000011 43.91666700000013, 124.75000000000011 43.93750000000006, 124.75000000000011 43.95833300000004, 124.75000000000011 43.979167000000075)))",
                "id": "3ff3b838b77a458d9d105a29ef508601",
                "idEd": "2012版",
                "idMapCurrency": "2006/2011",
                "idMapScale": "50000",
                "idMapTitle": "怀德镇",
                "idNewMapNum": "K51E001020",
                "idOldMapNum": "K-51-010-B",
                "idProject": "1:5万基础地理信息数据库更新项目",
                "idResType": "模拟地形图",
                "idVersionType": "2012",
                "mdDateSt": "",
                "mdRpOrgName": "国家基础地理信息中心",
                "mode": "0",
                "orgId": "1266f2473e5f4d6d90b5e3c82234f13a",
                "resType": "11",
                "secClass": "机密",
                "sourceCode": "0",
                "std": "0",
                "storeId": "2",
                "sysidVersionType": "2012-01-01",
                "time": "2016-08-17 15:38",
                "_id": "3ff3b838b77a458d9d105a29ef508601"
            }
        ],
        'keyStr': [
            "2"
        ]
    }

    /**
     * 将购物车的数据取出，按类别类重构数据格式
     */
    this.resetData = function() {
        this.proNum = 0;

        var goods = this.goods; //购物车内容
        $(".version-list1 p").remove();
        var list = {};
        for (key in goods) {
            if (key === "keyStr") continue;
            var data = goods[key];
            for (var i = 0; i < data.length; i++) {
                var type = data[i]["resType"];
                if (!list[type]) {
                    list[type] = [];
                }
                list[type].push(data[i]);
                if (type == "pro") {
                    this.proNum++;
                }
            }
        }

        var totalNum = 0;
        var keyCount = 0;
        for (var key in list) {
            var name = this.proTypeName[key];
            var num = list[key].length || 0;
            totalNum += num;
            if (key == this.resType) {
                keyCount++;
            }
            if (name != null) {
                this.proTypeHtml[key] = name + "<span style='color:red;'>(" + num + "条)</span>";
                $(".version-list1").append("<p resType='" + key + "'>" + this.proTypeHtml[key] + "</p>");
            } else {
                name = SearchTools.getInstance().getTypeName(key);
                this.proTypeHtml[key] = name + "<span style='color:red;'>(" + num + "条)</span>";
                $(".version-list1").append("<p resType='" + key + "'>" + this.proTypeHtml[key] + "</p>");
            }
        }

        $(".version-list1").append("<p resType='-1'>清除</p>");
        if (this.resType == "-1") {
            $(".version-box1").html("所有<span style='color:red;'>(" + totalNum + "条)</span>");
        } else {
            if (keyCount == 0) {
                var showName = thi$.proTypeName[thi$.resType] + "<span style='color:red;'>(0条)</span>";
                $(".version-box1").html(showName);
            } else {
                var showName = thi$.proTypeHtml[thi$.resType];
                $(".version-box1").html(showName);
            }
        }
        //绑定下拉框选中事件
        $(".version-list1 p").bind('click', this, this.resTypeClick);
        this.list = list;
    }


    /**
     * 数据类型点击时响应事件
     */
    this.resTypeClick = function(e) {
        var $this = $(e.currentTarget);
        var resType = $this.attr("resType");
        if (resType == "-1") {
            $(".version-box1").html("所有");
        } else {
            var showName = e.data.proTypeHtml[resType];
            if (showName != null) {
                $(".version-box1").html(showName);
            } else {
                showName = SearchTools.getInstance().getTypeName(resType);
                $(".version-box1").html(showName);
            }
        }
        e.data.resType = resType;
        e.data.refresh(1);
    }

    /**
     * 关闭购物车
     */
    this.close = function(e) {
        //div隐藏
        $('.shoppingCar').css({ 'height': '0px', 'border': '0' });
        //删除购物车里的内容
        $('.carBot div').remove();
    }

    /**
     * 刷新购物车
     */
    this.refresh = function(num) {
            if (typeof(num) === "undefined") {
                num = 1;
            }
            $("#carWithSpan").attr("w", "0");
            thi$.nowNum = num;
            thi$.resetData();

            var resetList = thi$.list;
            thi$.countList = [];
            var count = [];
            if (thi$.resType == "-1") {
                for (key in resetList) {
                    if (key == "pro") {
                        continue;
                    }
                    var tmp = resetList[key];
                    count = count.concat(tmp);
                    thi$.countList = thi$.countList.concat(tmp);
                }
            } else {
                count = count.concat(resetList[thi$.resType]);
                thi$.countList = thi$.countList.concat(resetList[thi$.resType]);
            }

            $(".carPro span").html(thi$.proNum);
            var page = new pageTools(count, 6);
            var list = page.go(num);
            thi$.getMapRelation(list);
            thi$.cancel();
            var pageInfo = page.getPageInfo();
            thi$.updatePageInfo(pageInfo);

            if (thi$.maxFlag === "1") { //是最大化
                var w = parseInt($("#carWithSpan").attr("w")) + 16;
                // 163 67.5  240
                if (thi$.countList.length > 0 && w > 163) {
                    var totalWidth = (w - 163) + 240;
                    var percent = ((w / totalWidth) * 100) + "%";
                } else {
                    var totalWidth = 240;
                    var percent = "67.5%";
                }
                $("#mapShoppingCar").width(totalWidth).find(".ctrontent0").css("width", percent);
            }
        }
        /**
         * 更新购物车脚标
         */
    this.updatePageInfo = function(obj) {
            $('.pageInfo').text(obj.currentPage + '/' + obj.totalPage);
            $('.pageNo').text(obj.currentPage);
        }
        /**
         * 全选chooseAll
         */
    this.chooseAll = function(e) {
            var carCommon = e.data;
            var isAll = $('#mapCarChooseAllID').attr('checked');
            var list = carCommon.countList;
            for (var i = 0; i < list.length; i++) {
                var uuid = list[i].id;
                var item = list[i]; //获取选中物品
                var resType = item.resType;
                if (isAll == "checked") {
                    if (carCommon.isSelectGoods(uuid)) {
                        continue;
                    }
                    carCommon.setCheck(uuid, true);
                    switch (resType) {
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                            //点的形式进行展示
                            carCommon.shopCarPointShow(item.geom, item.id);
                            break;
                        case "5":
                        case "6":
                        case "7":
                        case "10":
                            //图幅的形式进行展示
                            carCommon.shopCarPolygnShow(item.geom, item.id);
                            break;
                        case "8":
                            //航空影像
                            carCommon.shopCarPolygnShow(item.geom, item.id);
                            break;
                        case "9":
                            //卫星影像
                            carCommon.shopCarPolygnShow(item.geom, item.id);
                            break;
                        case "11":
                            //纸制地形图
                            carCommon.shopCarPolygnShow(item.geom, item.id);
                            break;
                        default:
                            carCommon.shopCarPolygnShow(item.geom, item.id);
                            break;
                    }
                } else {
                    carCommon.setCheck(uuid, false);
                    carCommon.$delete(uuid);
                }
            }
        }
        /**
         * 购物车自定义类图形显示
         */
    this.shopCarDefinedShow = function(wkt, id) {
        if (wkt.indexOf("POINT") >= 0) {
            this.shopCarPointShow(wkt, id);
        } else {
            this.shopCarPolygnShow(wkt, id);
        }
    }

    /**
     * 购物车点类图形显示
     */
    this.shopCarPointShow = function(wkt, id) {

        var point = MapUtils.getInstance(null).getGeomFromWKT(wkt);
        if (point.getCentroid != undefined) {
            point = point.getCentroid();
        }
        var size = new OpenLayers.Size(14, 25);
        var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
        var icon = new OpenLayers.Icon(MapConstant.point_shopcar_url, size, offset);
        var marker = this.father.mapManager.oplayerMap.addMarker(point.x, point.y, icon, {
            id: id,
            type: "point"
        }, "shopMarkerLayer");
        this.selectGoods[id] = marker;
        MapUtils.getInstance(null).setCenterByWKT(wkt, null);
    };

    /**
     * 购物车面类图形显示
     */
    this.shopCarPolygnShow = function(wkt, id) {

        var vector = MapUtils.getInstance(null).getVectorFromWKT(wkt);
        vector.style = {
            strokeWidth: 2,
            strokeOpacity: 1,
            strokeColor: "#C7007D",
            fillColor: "#ffffff",
            fillOpacity: 0
        };
        // this.vectorLayer.addFeatures([vector]);
        this.selectGoods[id] = vector;
        // MapUtils.getInstance(null).setCenterByWKT(wkt, null);
    }

    /**
     * 取消全选
     */
    this.cancelChooseAll = function() {

        var isAll = $('#mapCarChooseAllID').attr('checked');
        $("#mapCarChooseAllID").attr("checked", false);
        var list = this.countList;
        for (var i = 0; i < list.length; i++) {
            var uuid = list[i].id;
            var item = list[i]; //获取选中物品
            var resType = item.resType;
            this.setCheck(uuid, false);
            this.$delete(uuid);
        }
    }

    this.setCheck = function(uuid, mark) {
        var $g = $('.choose');
        for (var i = 0; i < $g.length; i++) {
            if ($g.eq(i).attr('uuid') == uuid) {
                $g.eq(i).attr('checked', mark);
            }
        }
    }


    /**
     * 判断地图购物车是否有选中对象
     */
    this.isSelectGoods = function(id) {
        var vector = this.selectGoods[id];
        if (vector)
            return true;
        return false;
    }



    /**
     * chooseSingal
     */
    this.chooseSingal = function(e) {
        var id = e.attributes.uuid.value;
        var item = window.shopCar.getGoodByID(id); //获取选中物品
        this.shopCarPolygnShow(item.geom, item.id);
    }

    /**
     * 删除地图购物车指定数据
     */
    this._delGoods = function(e) {
        var $this = this;
        var def = {
            info: "确认删除此条数据吗？",
            btnName: "确认",
            btnClick: function() {
                var id = e.attributes.uuid.value;
                var page = Number($('.pageNo').text());
                $this.delById(id, $this.refresh);
                $this.$delete(id);
                $this.totleNum--;
            },
            hasCancelBtn: true,
        };
        var popWindow = new PopWindow(def);
    }


    /**
     * delete
     * 删除图形
     */
    this.$delete = function(id) {
        var obj = this.selectGoods[id];
        if (!obj) return;
        var type = obj.type;
        if (type == "point") {
            this.markerLayer.removeMarker(obj);
        } else {
            //地图移除该图形
            // this.vectorLayer.removeFeatures(obj);
        }
        //删除此条数据
        delete this.selectGoods[id];
    }

    /**
     * 统计已经选中多少条数据
     */
    this.countNum = function() {
            var num = 0;
            var list = this.countList;

            for (var i = 0; i < list.length; i++) {
                var uuid = list[i].id;
                if (this.isSelectGoods(uuid)) {
                    num++;
                }
            }
            return num;
        }
        /**
         * 购物车一键删除所选功能
         */
    this.delCarChoosed = function(e) {
        var $this = e.data;
        var selNum = $this.countNum();
        if (selNum === 0) {
            return;
        }

        var def = {
            info: "确认删除这" + selNum + "条数据吗？",
            btnName: "确认",
            btnClick: function() {
                var list = $this.countList;
                // if (list.length > 100) {
                //     SearchDownload && SearchDownload.create();
                // }
                var idArr = [];
                for (var i = 0; i < list.length; i++) {
                    var uuid = list[i].id;
                    var item = list[i]; //获取选中物品
                    if ($this.isSelectGoods(uuid)) {
                        //如果选中则删除图幅
                        $this.$delete(uuid);
                        idArr.push(uuid);
                    }
                }
                //删除购物车里
                $this.delById(idArr, $this.refresh);
                $this.cancel();

                // SearchDownload && SearchDownload.destroy();
            },
            hasCancelBtn: true,
        };
        var popWindow = new PopWindow(def);
    }


    /**
     * 返回相应类型的html
     */
    this.getMapRelation = function(list) {
        var html = '';
        for (var i = 0; i < list.length; i++) {
            var resType = (list[i] && list[i].resType) || "pro";
            //排除图集
            if (resType != "pro") {
                var uuid = list[i].id;
                var mark = this.isSelectGoods(uuid);
                html += this.getHtml(resType, list[i], mark);
            }
        }
        $("#carTitle").html('<li class="btf1">选择</li><li class="ctrontent0">内容概要</li><li class="doan">操作</li>');

        $('.carBot').html(html);
    }


    /**
     * 上一页
     */
    this.prev = function(e) {
            var carCommon = e.data;
            var pageNo = Number($('.pageNo').text()) - 1;
            carCommon.refresh(pageNo);
        }
        /**
         * 下一页
         */
    this.next = function(e) {
        var carCommon = e.data;
        var pageNo = Number($('.pageNo').text()) + 1;
        carCommon.refresh(pageNo);
    }


    /**
     * 根据类型判断填充内容
     */
    this.getHtml = function(resType, obj, mark) {
        switch (resType) {
            case "1":
                return MapCarText.getInstance().getZLD(obj, mark, this.maxFlag);
                break;
            case "2":
                return MapCarText.getInstance().getSZD(obj, mark, this.maxFlag);
                break;
            case "3":
                return MapCarText.getInstance().getSJD(obj, mark, this.maxFlag);
                break;
            case "4":
                return MapCarText.getInstance().getGPS(obj, mark, this.maxFlag);
                break;
            case "5":
                return MapCarText.getInstance().getDLG(obj, mark, this.maxFlag);
                break;
            case "6":
                return MapCarText.getInstance().getDEM(obj, mark, this.maxFlag);
                break;
            case "7":
                return MapCarText.getInstance().getDOM(obj, mark, this.maxFlag);
                break;
            case "8":
                return MapCarText.getInstance().getHKYX(obj, mark, this.maxFlag);
                break;
            case "9":
                return MapCarText.getInstance().getWeix(obj, mark, this.maxFlag);
                break;
            case "10":
                return MapCarText.getInstance().getDRG(obj, mark, this.maxFlag);
                break;
            case "11":
                return MapCarText.getInstance().getMLDT(obj, mark, this.maxFlag);
                break;
                //自定义类别
            default:
                return MapCarText.getInstance().getDefined(obj, mark, this.maxFlag);
                break;
        }
    }

    // 通过id获取商品
    this.getGoodByID = function(id) {
        for (var i in this.goods) {
            if (i !== "keyStr") {
                var list = this.goods[i];
                for (var j in list) {
                    if (list[j].id == id) {
                        return list[j];
                    }
                }
            }
        }
        return null;
    }

    /**
     * 地图购物车点击事件:点击选中，地图显示已选物品图形
     */
    this._chooseGoods = function(e) {
        var id = e.attributes.uuid.value;
        var item = this.getGoodByID(id); //获取选中物品
        if (e.checked) {
            //选中状态
            var resType = item.resType;
            switch (resType) {
                case "1":
                case "2":
                case "3":
                case "4":
                    //点的形式进行展示
                    this.shopCarPointShow(item.geom, item.id);
                    break;
                case "5":
                case "6":
                case "7":
                case "10":
                    //图幅的形式进行展示
                    this.shopCarPolygnShow(item.geom, item.id);
                    break;
                case "8":
                    //航空影像
                    this.shopCarPolygnShow(item.geom, item.id);
                    break;
                case "9":
                    //卫星影像
                    this.shopCarPolygnShow(item.geom, item.id);
                    break;
                case "11":
                    //纸制地形图
                    this.shopCarPolygnShow(item.geom, item.id);
                    break;
                default:
                    this.shopCarDefinedShow(item.geom, item.id);
                    break;
            }
        } else {
            //取消选中状态
            this.$delete(id);
        }
    };


    this.delById = function(idArr) {
            if (typeof(idArr) === "string") {
                idArr = [idArr];
            }
            var len = idArr.length;
            for (var k in idArr) {
                var delFlag = false;
                for (var i in this.goods) {
                    if (i !== "keyStr") {
                        var list = this.goods[i];
                        for (var j in list) {
                            if (list[j].id == idArr[k]) {
                                list.splice(j, 1);
                                if (list.length == 0) {
                                    var keyArr = this.goods["keyStr"];
                                    for (var l in keyArr) {
                                        if (keyArr[l] === i) {
                                            keyArr.splice(l, 1);
                                            this.goods["keyStr"] = keyArr;
                                            break;
                                        }
                                    }
                                    delete this.goods[i];
                                    break;
                                }
                                delFlag = true;
                                break;
                            }
                        }
                    }
                    if (delFlag) {
                        break;
                    }
                }
            }
            this.reSize(thi$.getCount() - len);
            this.goods.count = thi$.getCount() - len;
            this.refresh(1)

        }
        // 获取数量
    thi$.getCount = function() {
        var count = this.goods.count;
        return count;
    }

    /**
     * 购物车标记数量
     */
    this.reSize = function(size) {
        $('.sizeNum').text(size);
    }

    /**
     * 取消全选状态
     */
    this.cancel = function() {
        $('.carChooseAll').attr('checked', false);
    }

    /**
     * 跳转至购物车详情页面
     */
    this.goShopCar = function(e) {
        location.href = "";
    }

    this.maxChange = function() {
        $("#mapShoppingCar").width(320);
        this.maxFlag = "1";
        this.refresh(this.nowNum);
    }
    this.minChange = function() {
        if (this.maxFlag === "0") {
            return;
        }
        $("#mapShoppingCar").width(240);
        this.maxFlag = "0";
        this.refresh(this.nowNum);
    }
    this.boxChange = function(e) {
            var className = $(this).attr("class");
            switch (className) {
                case "cancle": //关闭
                    thi$.close();
                    break;
                case "min": //最小化
                    thi$.minChange();
                    $(this).next().attr("class", "max");
                    break;
                case "max": //最大化
                    thi$.maxChange();
                    $(this).attr("class", "max-c");
                    break;
                case "max-c": //已经最大化后点击 实现最小化功能
                    thi$.minChange();
                    $(this).attr("class", "max");
                    break;
            }
        }
        //----------------------------------初始化方法------------------------------


    this.init = function() {
        $('.sizeNum').text(this.goods.count);
        //大小改变事件
        $('#box-change').on("click", "i", this.boxChange);
        //绑定打开购物车点击事件
        $('.shoppingCarIcon').bind('click', this, this.open);
        //绑定购物车全选点击事件
        $('#mapCarChooseAllID').bind('click', this, this.chooseAll);
        //绑定购物车删除全选点击事件
        $('#mapCarDelAllID').bind('click', this, this.delCarChoosed);
        //绑定地图购物车上页点击事件
        $('#carMapPrevBtn').bind('click', this, this.prev);
        //绑定地图购物车下页点击事件
        $('#carMapNextBtn').bind('click', this, this.next);
    }
    this.init.apply(this, arguments);
}