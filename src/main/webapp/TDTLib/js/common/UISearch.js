/**
 * 输入框响应
 * @constructor
 * _reset 					重置清空
 * _startBindEvent  		绑定鼠标事件
 * _showDefault 			显示$default
 * _showSearch  			显示$search
 * _goSearch    			关键字查询并填充到$search中
 * _provinceClick 			省点击响应事件
 * _cityClick     			市点击响应事件
 * _countyClick   			县点击响应事件
 * _resultTypeClick 		成果类型点击响应事件
 * _resultTypeTagClick 		成果类型标签点击响应事件
 * _searchFlagClick    		关键字查询结果点击响应事件
 * _getObjInListByKey  		通过key和value获取list中的某个对象
 * _createTag 				创建成果类型标签
 * _createSelect    		创建下拉框（这儿是创建分发单位）
 * _createSelectDo  		创建下拉框具体实现
 * _selectMouseEvent		下拉框鼠标事件
 * _createCombox			创建combox类型
 * _createDate				创建date类型(这儿是创建日期)
 * _showDate				date类型输入框点击事件
 * _showBoxAdd 				$showBox加入元素
 * _showBoxDel				$showBox移除元素
 * _showBoxWidthChange 		$showBox宽度计算
 * _startInitDefaultBox 	初始化显示
 * setType					设置resultTypeList的值
 * _analysisText       		解析文字
 * getUrl					通过$showBox生成网址，外部调用
 * start					开始处理，外部调用
 * _init					初始化函数，设置部分值
 */
var UISearch = function(){
	var thi$ = this;
	var $allBox = null;//#input-box的jquery对象
    var $input = null;//#searchInput的jquery对象
    var $default = null;//#input-default的jquery对象
    var $search = null;//#input-search的jquery对象
    var $showBox = null;//#show-default的下的.show-box的jquery对象
    var sureFlag = false;//参数是否传入正常
    //因为typeList是common.js中ajax获取的,
    //所以在此js中用setInterval函数来获取window中typeList存入resultTypeList
    //typeT用于存储setInterval的返回值
    var typeT = null;

    var province = "0";//省标识
    var city = "1";//市标识
    var county = "2";//县标识
    var resultType = "3";//成果类型标识
    var resultTypeTag = "4";//成果类型标签 标识
    var searchFlag = "5";//搜索关键字 标识
    var moreFlag = "6";//更多 标识

    var cityKey = "name";//省市县的显示字段
    var cityId = "name";//省市县的唯一标识符
    var cityChild = "children";//省市县的子元素字段
    var cityPidId = "cityPidId";//省市县的最高父级id

    var resultTypeKey = "typeName";//成果类型的显示字段
    var resultTypeId = "id";//成果类型的唯一标识符
    var resultTypePid = "resultTypePid";//成果类型的最高父级id

    var provinceList = null;//省的list列表
    var cityList = null;//市的list列表
    var countyList = null;//县的list列表
    var resultTypeList = null;//成果类型的list列表

    var showFlag = 0;//显示标识 0表示$default 1表示$search
    var inputLen = 0;//输入框的输入长度记录
    var showSearchIndex = -1;//记录键盘按上下键时$search下li的下标
    var inputText = "在 0 条成果目录里面查询";
    var tagObj = {};
    /**
     * 重置
     */
    var _reset = function(){
    	$default.find(".input-default-box").html('');
    	$search.html('');
    	$input.html('');
    	$showBox.html('');
    }

    /**
     * 绑定鼠标事件
     */
    var _startBindEvent = function(){
    	//$allBox中class为elemClick的点击事件
    	$allBox.on("click",".elemClick",function(){
    		var $this = $(this);
    		var flag = $this.attr("flag");
    		switch(flag){
    			case province://省点击
    				_provinceClick($this,flag);break;
    			case city://市点击
    				_cityClick($this,flag);break;
    			case county://县点击
    				_countyClick($this,flag);break;
    			case resultType://成果类型点击
    				_resultTypeClick($this,flag);break;
    			case resultTypeTag://成果类型标签点击
    				_resultTypeTagClick($this,flag);break;
    			case searchFlag://搜索关键字点击
    				_searchFlagClick($this,flag);break;
    			case moreFlag://更多点击
    				_moreFlagClick($this,flag);break;

    		}
    	});
    	//$showBox的删除事件
    	$showBox.on("click",".ui-del",function(){
    		_showBoxDel($(this).parent());
    	});
    	//$search下边的lihover事件
    	$search.on("mouseenter","li",function(){
    		showSearchIndex = $(this).index();
    		_showSearchLiHover($(this));
    	}).on("mouseleave","li",function(){
    		showSearchIndex = -1;
    		$(this).removeClass("liHover").index();
    	});

    	var tmpFlag = false;
    	/*此处是处理用户点击其他地方时的响应事件*/
    	$allBox.click(function(e){
    		e = e || window.event;
    		e.stopPropagation();
    	}).mouseover(function(){
    		tmpFlag = true;
    	}).mouseleave(function(){
    		tmpFlag = false;
    		setTimeout(function(){
    			if(!tmpFlag){
    				$default.hide();  $search.hide();
    			}
    		},800);
    	});
    	$(document).click(function(){
    		$default.hide();  $search.hide();
    	});

    	//输入框事件
    	$input.click(function(){
    		if($showBox.width() == 0){
    			$(this).addClass("searchInputFocus");
    		}
    		showFlag == 0 ? _showDefault() : _showSearch();
    	}).focus(function(){
    		if($showBox.width() == 0){
    			$(this).addClass("searchInputFocus");
    		}
    		showFlag == 0 ? _showDefault() : _showSearch();
    	}).blur(function(){
    		if($showBox.width() == 0 && $(this).val().length == 0){
    			$(this).removeClass("searchInputFocus");
    		}
    	}).keyup(function(e){
    		e = e || window.event;
    		switch(e.keyCode){
	    		case 8://backspace键
	    			_inputKeyBackspace();break;
	    		case 13://enter键
	    			$allBox.find(".inputSearch").eq(0).click();break;
	    		case 38://向上箭头
	    			_inputKeyEnter("up");break;
	    		case 39:
	    			break;
	    		case 40://向下箭头
	    			_inputKeyEnter("down");break;
	    		default:
	    			_inputKeyOther();break;
    		}
    	});
    }
    /**
     * 键盘按backspace键
     */
    var _inputKeyBackspace = function(){
    	inputLen--;
		if(inputLen >0){
			_goSearch();
		}else if(inputLen == 0){
			_showDefault();
		}else if(inputLen < 0 &&$showBox.width() != 0){
			$showBox.find(".show-box-elem:last").find(".ui-del").click();
		}
    }
    /**
     * 键盘按其他键
     */
    var _inputKeyOther = function(){
    	inputLen = $input.val().length;
		if($showBox.width() != 0){
			_showDefault();
		}else{
			_showSearch();
			_goSearch();
		}
    }
    /**
     * 按上下键的时候
     */
    var _inputKeyEnter = function(keyFlag){
    	if(showFlag == 1){//是搜索时
    		var $lis = $search.find("li");
    		if(keyFlag == "up"){
    			if(showSearchIndex === 0 || showSearchIndex-- === -1){
    				showSearchIndex = $lis.length - 1;
    			}
    		}else{
    			if($lis.length === ++showSearchIndex){
    				showSearchIndex = 0;
    			}
    		}
    		_showSearchLiHover($lis.eq(showSearchIndex));
    		$input.val($lis.eq(showSearchIndex).attr("value"));
    	}
    }
    /**
     * 显示定位某一行的颜色
     */
    var _showSearchLiHover = function($elem){
    	$elem.addClass("liHover").siblings().removeClass("liHover");
    }

    /**
     * 显示$default
     */
    var _showDefault = function(){
    	showFlag = 0;
    	$default.show();  $search.hide();
    }
    /**
     * 显示$search
     */
    var _showSearch = function(){
    	showFlag = 1;
    	$search.show();  $default.hide();
    }

    /**
     * 生成嵌入知识服务的iframe
     * ps:yd 2017/10/30
     */
    var _createIframeForMapknow = function(){
        if(!$('#mapKnow_iframe').length){
            var oldIframe = $('<iframe style="display:none" id="mapKnow_iframe" src="http://kmap.ckcest.cn/" frameborder="no"   border="no" height="0" width="0" scrolling="auto"></iframe>');
            $("body").prepend(oldIframe);
            var iframe = document.getElementById("mapKnow_iframe");
            iframe.onload = function() {
                $('#searchInput').focus();
            };

        }
    }
    /**
     * 输入框响应模糊查询
     */
    var _goSearch = function(){
    	var text =  $.trim($input.val());
    	if(text.length > 0){
    		$search.html("");
    		showSearchIndex = -1;
    		var obj = {
    			url:"./mapDataAction.do?method=keyFilter",
    			data:{keyWord:text},
    			callBack:function(rs){
    				rs = typeof(rs) == "string" ? eval("("+rs+")") : rs;
                    //ps: yd 2017/10/30 添加知识服务的查询如果keyType == 1的话就生成一个iframe里面嵌入知网
                    if(rs.keyType == 1){
                        _createIframeForMapknow();
                    }
                    var list = rs.result;
    				if(list.length == 0) list = [{name:text}];
                    $search.empty();
    				for(var i in list){
    					var name = list[i].name.replaceAll(text,'<span>'+text+'</span>');
    					var tUpper = text.toUpperCase();
    					if(text !== tUpper){
    						name =  name.replaceAll(tUpper,'<span>'+tUpper+'</span>');
    					}
    					var tLower = text.toLowerCase();
    					if(text !== tLower){
    						name =  name.replaceAll(tLower,'<span>'+tLower+'</span>');
    					}
    					var bname = list[i].bname || "";

    					$('<li class="elemClick" flag="'+searchFlag+'">'+name+'<span class="other">'+bname+'</span></li>')
    					.data("name",list[i].name).appendTo($search);
    				}
    			}
    		}
    		comAjax(obj);
    	}
    }
	//省点击
    var _provinceClick = function($elem,flag){
    	_showBoxAdd(flag,$elem.text(),$elem.attr("tid"),$elem.attr("pid"),false,$elem.attr("pac"));
    	var tid = $elem.attr("tid");
    	var obj = _getObjInListByKey(provinceList,cityId,tid);
    	if(obj){
    		cityList = obj[cityChild];
    		_startInitDefaultBox(cityList,0,cityKey,cityId,city,tid);
    	}

    }
	//市点击
    var _cityClick = function($elem,flag){
    	_showBoxAdd(flag,$elem.text(),$elem.attr("tid"),$elem.attr("pid"),false,$elem.attr("pac"));
    	var tid = $elem.attr("tid");
    	var obj = _getObjInListByKey(cityList,cityId,tid);
    	if(obj){
    		countyList = obj[cityChild];
    		_startInitDefaultBox(countyList,0,cityKey,cityId,county,tid);
    	}
    }
    //县点击
    var _countyClick = function($elem,flag){
    	_showBoxAdd(flag,$elem.text(),$elem.attr("tid"),$elem.attr("pid"),false,$elem.attr("pac"));
    }
    //成果类型点击
    var _resultTypeClick = function($elem,flag){
    	_showBoxAdd(flag,$elem.text(),$elem.attr("tid"),$elem.attr("pid"));
    	$default.find(".input-default-box").eq(1).html("");
    	var typeID = $elem.attr("tid");
    	if(window.IndexJsonData){
    		_createTag(IndexJsonData.tagList[typeID],typeID);
    	}else{
    		if(tagObj[typeID]){
        		_createTag(tagObj[typeID],typeID);
        	}else{
        		var obj = {
    				url:"./mapDataAction.do?method=getTagInterface",
    				data:{typeID:typeID},
    				callBack:_createTag,
    				otherArgs:typeID//回调函数使用
    	    	}
    			comAjax(obj);
        	}
    	}
    }
    //成果类型标签点击事件
    var _resultTypeTagClick = function($elem,flag){
    	_showBoxAdd(flag,$elem.text(),$elem.attr("tid"),$elem.attr("pid"),$elem.attr("format"));
    }
    //搜索关键字结果点击
    var _searchFlagClick = function($elem,flag){
    	$input.val($elem.data("name"));
//    	$allBox.find(".inputSearch").eq(0).click();
    }
    //更多按钮点击
    var _moreFlagClick = function($elem,flag){
    	var $liRight = $elem.toggleClass("li-more-up").prev();
    	var h1 = $liRight.data("heightValue");
    	var openFlag = $liRight.data("openFlag");
    	if(openFlag == 0){
    		$liRight.data("openFlag",1).animate({"height":h1+"px"},500);

    	}else{
    		$liRight.data("openFlag",0).animate({"height":"30px"},500);
    	}
    }

    //通过字段key和值value获取list中的某个元素
    var _getObjInListByKey = function(list,key,value){
    	for(var i in list){
    		if(list[i][key] == value) return list[i];
    	}
    	return null;
    }


    /**
     * 成果类型点击后响应加载标签
     */
    var _createTag = function(rs,typeID){
    	if(typeof(rs) == "string"){
    		if(rs.length == 0){
    			return _startInitDefaultBox(resultTypeList,1,resultTypeKey,resultTypeId,resultType,resultTypePid);
    		}else{
    			rs = eval("("+rs+")")
    		}
    		tagObj[typeID] = rs;
    	}
    	var $tagBox = $default.find(".input-default-box").eq(1);
    	var tagList = rs.resultList;
    	for(var i in tagList){
    		var tmp = tagList[i];
    		var $li = $('<div class="li"></div>');
    		var $lileft = $('<div class="li-left">'+tmp.dName+'：</div>');
    		var $liRight = $('<div class="li-right"></div>');
    		var argsObj = {
    				$box:$liRight,list:tmp.values,
    				dName:tmp.dName,format:tmp.format,
    				tid:tmp.name,valueStr:"name",idStr:"name",
    				pid:typeID,flag:resultTypeTag,
    				checkFuc:function(text){
    					switch(text){
    						case "1000000": return "1:100万";
    						case "500000": return "1:50万";
    						case "250000": return "1:25万";
    						case "200000": return "1:20万";
    						case "100000": return "1:10万";
    						case "50000": return "1:5万";
    						case "25000": return "1:2.5万";
    						case "10000": return "1:1万";
    						case "5000": return "1:5千";
    						case "2000": return "1:2千";
    						case "1000": return "1:1千";
    						case "500": return "1:5百";
    						default:return text;
    					}
    				}
    		};
    		if(tmp.dName == "分发单位"){
    			_createSelect(argsObj);
    			$liRight.css({"overflow-y":"initial"});
    		}else if(tmp.format == "combox"){
    			_createCombox(argsObj);
    		}else if(tmp.format == "date"){
    			_createDate(argsObj);
    		}else{
    			$li.hide();
    		}
    		//如果创建日期的话创建一个空div不创建更多按钮
    		if(tmp.format == "date"){
    			var $limore = $('<div style="width:0px"></div>');
    		}else{
    			var $limore = $('<div class="li-more elemClick" flag="'+moreFlag+'">更多</div>');
    		}
    		$tagBox.append($li.append($lileft).append($liRight).append($limore));
    		$liRight.width($li.width() - $lileft.outerWidth(true) - $limore.outerWidth(true) - 10);
    		(function(){
    			var h1 = $liRight.height();
    			if(h1 <= 30){
    				$limore.hide();
    			}else{
    				$liRight.css({"height":"30px"}).data("heightValue",h1).data("openFlag",0);
    			}
    		})();
    	}
    }
    //创建下拉框
    var _createSelect = function(obj){
    	var $selectInput = $('<input type="text" class="select" idv="none" value="" placeholder="'+obj.dName+'"/>');
    	var $ul = $('<ul></ul>');
    	var $box = obj.$box;
    	obj.text = "";
    	obj.$input = $selectInput;
    	obj.$ul = $ul;
    	var arguObj = obj;
		var rsObj = _createSelectDo(arguObj);
		$selectInput.keyup(function(){
			arguObj.text = $(this).attr("idv","none").val();
			_createSelectDo(arguObj);
		});

		$box.append($selectInput).append($ul);
    	_selectMouseEvent($box,$ul);

    	var $selectLi = null;
    	$ul.on("click","li",function(){//绑定li的点击事件
    		var $this = $(this);
    		$this.parent().hide();
    		var idv = $this.attr("idv");
    		if(idv == "none"){
    			$selectLi = $selectLi ? _showBoxDel($showBox.find("[tid='"+$selectLi.attr("tid")+"']")) : null;
    			$selectInput.attr("idv",idv).val("");
    		}else{
    			$selectLi = $this;
    			$selectInput.attr("idv",idv).val($this.text());
    			_showBoxAdd($this.attr("flag"),$this.text(),$this.attr("tid"),$this.attr("pid"),$this.attr("format"));
    		}
    	});
    }

    /**
	 * 下拉框的鼠标事件
	 */
	var _selectMouseEvent = function($box,$ul){
		$box.mouseenter(function(){
			$ul.show();
		}).mouseleave(function(){
			$ul.hide();
		});
	}

    /**
	 * 填充下拉框并处理事件
	 */
	var _createSelectDo = function(obj){
		try{
			var rsObj = null;//返回选中的某一个元素
			var list = obj.list;//下拉框参数
			var text = obj.text;//搜索值
			var $input = obj.$input;//显示的输入框
			var $ul = obj.$ul;//下路框ul
			var idStr = obj.idStr;//唯一标识符
			var valueStr = obj.valueStr;//显示字段
			var liClick = obj.liClick;//点击事件
			var clearFlag = obj.clearFlag;//清除选择标识
			var select = obj.select;//默认选中的
			var $selectBox = obj.$selectBox;//整个下拉框
			$ul.html('');
			if(clearFlag){
				var $clearLi = $('<li idv="none">清除选择</li>')
				$ul.append($clearLi);
			}
			for(var i in list){
				var tmp = list[i];
				var $li = $('<li idv="'+tmp[idStr]+'" flag="'+obj.flag+'" tid="'+obj.tid+'" pid="'+obj.pid+'" format="'+obj.format+'">'+tmp[valueStr]+'</li>');
				if(text.length != 0 && tmp[valueStr].indexOf(text) != -1){
					$li.html(tmp[valueStr].replaceAll(text,'<span style="color:#528dde">'+text+'</span>'));
					if($clearLi){
						$clearLi.after($li);
					}else{
						$ul.prepend($li);
					}
				}else{
					$ul.append($li);
				}
				if(select && select == tmp[idStr]){
					rsObj = tmp;
				}
			}
			return rsObj;
		}catch(e){
			console.log("请检查_createSelect传入参数是否正确.");
		}
	}

    //创建横线显示
    var _createCombox = function(obj){
    	var $box = obj.$box;
    	var list = obj.list;
    	var checkFuc = obj.checkFuc;
    	for(var i in list){
    		var showName = list[i][obj.valueStr];
    		showName = checkFuc ? checkFuc(showName) : showName;
    		$box.append('<div class="rightElem elemClick" flag="'+obj.flag+'" tid="'+obj.tid+'" pid="'+obj.pid+'" format="'+obj.format+'">'+showName+'</div>');
    	}
    }

    //创建生产日期
    var _createDate = function(obj){
    	var $box = obj.$box;
    	var list = obj.list;
    	var $btn = $('<span class="btn" old="不限至不限">确认</span>');
    	var $start = $('<input type="text" class="laydate-icon choose-date" id="ui-start-date" placeholder="起始日期" readonly="readonly">');
    	var $end = $('<input type="text" class="laydate-icon choose-date" id="ui-end-date" placeholder="结束日期" readonly="readonly">');
    	$box.append($start)
    		.append('<span class="splice">—</span>')
    		.append($end)
    		.append($btn);
    	$start.click(function(){
    		_showDate("ui-start-date");
    	});
    	$end.click(function(){
    		_showDate("ui-end-date");
    	});

    	$btn.click(function(){
    		var $this = $(this);
    		var old = $this.attr("old");
    		var start = $start.val();
    		var end = $end.val();
    		var str = (start == "" ? "不限": start) + "至" + (end == "" ? "不限": end);
    		$(this).attr("old",str);
    		if(start.length == 0 && end.length == 0){
    			old == "不限至不限" ? "" :_showBoxDel($showBox.find("[tid='"+obj.tid+"']"));
    		}else{
    			_showBoxAdd(obj.flag,str,obj.tid,obj.pid,obj.format);
    		}
    	});
    }

    //显示日期
    var _showDate = function(id){
    	laydate({
    	    elem:'#'+id,
    	    isclear: true,
    	    choose:function(date){

    	    }
    	});
    }

    /**
     * 对$showBox进行填充元素
     * flag值是 province|city|county|resultType|resultTypeTag中的某一个
     * name是显示文字
     * tid当前元素的id
     * pid当前元素的父id
     */
    var _showBoxAdd = function(flag,name,tid,pid,format,pac){
    	$showBox.css({"width":"100%"});
    	var $elem = null;
    	if(flag == resultTypeTag){//当类型是成果类型标签的时候,另外处理
    		$elem = $showBox.find(".show-box-elem[tid="+tid+"]");
    	}else{
    		$elem = $showBox.find(".show-box-elem[flag="+flag+"]");
    	}
    	if($elem.length == 0){
    		$elem = $('<div class="show-box-elem" flag="'+flag+'" tid="'+tid+'" pid="'+pid+'">'
    				+'<span>'+name+'</span>'
        			+'<span class="ui-del" style="cursor:pointer;">x</span></div>');
    		$showBox.append($elem);
    	}else{
    		$elem.attr("tid",tid).attr("pid",pid).find("span").eq(0).text(name);
    	}
    	if(format) $elem.attr("format",format);
    	if(pac) $elem.attr("pac",pac);
    	_showBoxWidthChange();
    }
    /**
     * 对$showBox进行进行删除
     */
    var _showBoxDel = function($elem){
		var flag = $elem.attr("flag");
		var tid = $elem.attr("tid");
		var $nexts = $elem.nextAll("[pid="+tid+"]");
		$nexts.find(".ui-del").click();
		$elem.remove();
		switch(flag){
			case province://移除选中的省
				_startInitDefaultBox(provinceList,0,cityKey,cityId,province,tid);break;
			case city://移除选中的市
				_startInitDefaultBox(cityList,0,cityKey,cityId,city,tid);break;
			case county://移除选中的县
				break;
			case resultType://成果类型点击
				_startInitDefaultBox(resultTypeList,1,resultTypeKey,resultTypeId,resultType,resultTypePid);break;
		}
		_showBoxWidthChange();
		return null;
    }
    /**
     * 改变$showBox的宽度
     */
    var _showBoxWidthChange = function(){
    	var maxWidth = parseInt($showBox.attr("mw"));
    	var w = 10;
    	$showBox.find(".show-box-elem").each(function(){
    		w += $(this).outerWidth(true);
    	});
    	if(w>maxWidth){
    		$showBox.css({"width":w+"px","margin-left":(maxWidth-w)+"px"});
    		$input.css({"text-indent":maxWidth+"px"})
    				.focus().addClass("searchInputFocus").attr("placeholder","");
    	}else if(w != 10){
    		$showBox.css({"width":w+"px","margin-left":"0px"});
    		$input.css({"text-indent":w+"px"})
    				.focus().addClass("searchInputFocus").attr("placeholder","");
    	}else{
    		$showBox.css({"width":"0px","margin-left":"0px"});
    		$input.css({"text-indent":"10px"}).focus().attr("placeholder",inputText);
    	}
    }
    /**
     * 处理初始化显示
     * elemClick只是用于绑定点击的标识
     */
    var _startInitDefaultBox = function(list,index,key,idStr,flag,pid){
    	var $box = $default.find(".input-default-box").eq(index).html('');
    	for(var i in list){
    		var tmp = list[i];
    		$box.append('<div class="elem elemClick" title="'+tmp[key]+'" flag="'+flag+'" tid="'+tmp[idStr]+'" pid="'+pid+'" pac="'+tmp["pac"]+'">'+tmp[key]+'</div>');
    	}
    }

    /**
     * 设置resultTypeList的值
     */
    thi$.setType = function(){
    	//typeList在 public/js/common/common.js中setMenu获取
    	if(window.typeList){
    		clearInterval(typeT);
    		resultTypeList = window.typeList;
    		_startInitDefaultBox(resultTypeList,1,resultTypeKey,resultTypeId,resultType,resultTypePid);
    	}

    }

    /**
     * 解析文字
     */
    var _analysisText = function(text){
    	switch(text){
			case "1:100万": return "1000000";
			case "1:50万": return "500000";
			case "1:25万": return "250000";
			case "1:20万": return "200000";
			case "1:10万": return "100000";
			case "1:5万": return "50000";
			case "1:2.5万": return "25000";
			case "1:1万": return "10000";
			case "1:5千": return "5000";
			case "1:2千": return "2000";
			case "1:1千": return "1000";
			case "1:5百": return "500";
			default:return text;
		}
    }
    /**
     * 查询数据总量
     * */
    var _inputCountAll = function(){
    	$.ajax({
    		url:"dataEnty.do?method=getMongoDataCount",
    		data:{},
    		success:function(rs){
    			inputText = "在 "+rs+" 条成果目录里面查询"
    			$("#searchInput").attr("placeholder",inputText);
    		}
    	});
    }

    /**
     * 把参数设置到缓存中
     */
    thi$.setObj = function(){
    	var rsObj = {
    		keyWord:null,resType:null,
    		regionName:null,groupType:null,
    		storeID:null,attrQuery:null,
    		attrList:[]//用于设置标签
    	};
    	var regionName = "";//区域名称
		var groupType = "";//区域聚合类型
		var resType = "";
		var attrQuery = {};
		var attrQueryFlag = false;
		var keyWord = $input.val();
		var urlFlag = false;
		var $list = $showBox.find(".show-box-elem");
    	if($list.length != 0){
    		$showBox.find(".show-box-elem").each(function(){
        		var $this = $(this);
        		var flag = $this.attr("flag");
        		var text = $this.find("span").eq(0).text();
        		switch(flag){
        			case province://省
        				regionName = $this.attr("pac"); groupType = "cityName";
        				break;
        			case city://市
        				regionName = $this.attr("pac");
        			case county://县
        				regionName = $this.attr("pac");
        				groupType = "cnty";
        				break;
        			case resultType://成果类型
        				resType = $this.attr("tid");
        				break;
        			case resultTypeTag://成果类型标签
        				attrQueryFlag = true;
        				var format = $this.attr("format");
        				var tid = $this.attr("tid");

        				if(format == "combox"){
        					attrQuery[tid] = [_analysisText(text)];
        					rsObj["attrList"].push({"format":format,"name":tid,value:_analysisText(text)});
        				}else if(format == "date"){
        					attrQuery[tid] = text.replace("不限","").split("至");
        					rsObj["attrList"].push({"format":format,"name":tid,value:text.replace("不限","").replace("至",";")});
        				}
        				break;
        		}
        	});
    	}
		if(keyWord.length != 0){
			urlFlag = true;
			rsObj["keyWord"] = keyWord;
		}
    	if(resType.length != 0){
    		urlFlag = true;
    		rsObj["resType"] = resType;
    	}
    	if(regionName.length != 0){
    		urlFlag = true;
    		rsObj["regionName"] = regionName;
    		rsObj["groupType"] = groupType;
    	}
    	if(attrQueryFlag){
    		urlFlag = true;
    		rsObj["attrQuery"] = attrQuery;
    	}
    	if(urlFlag){
    		var b = getStorage();
    		var value = escape(JSON.stringify(rsObj));
    		b.setItem("keySearch",value);//keySearch请勿修改
    		return rsObj;
    	}else{
    		return null;
    	}
    }

    /**
     * 取出缓存中的数据
     */
    thi$.getObj = function(){
    	var rsObj = unescape(getStorage().getItem("keySearch"));
    	if (rsObj != null && rsObj!='undefined') {
            return JSON.parse(rsObj);
        }
        return null;
    }
    /**
     * 开始函数 外部调用
     */
    thi$.start = function(){
        if(sureFlag){
        	_reset();
        	provinceList = adminDataStore;//adminDataStore在public/js/maps/map/adminDataStore.js中

        	/**
        	 * 显示所有的省
        	 */
        	_startInitDefaultBox(provinceList,0,cityKey,cityId,province,cityPidId);

        	//显示所有的成果类型
        	typeT = setInterval(thi$.setType,500);

        	//绑定点击事件
        	_startBindEvent();
        	//查询数据总量
        	_inputCountAll();
        }
    }
    thi$.reset = function(){
    	_reset();
    }

    var _init = function(args){
    	try{
    		$allBox = args.$allBox;
    		$input = args.$input;
            $showBox = args.$showBox;
            $default = args.$default;
            $search = args.$search;
            sureFlag = true;
    	}catch(e){
    		sureFlag = false;
    		//alert("参数传入不对,请检查传入参数.");
            console.log("存在参数未传入,请检查创建对象时是否正确.");
    	}
    }
    _init.apply(this,arguments);
}