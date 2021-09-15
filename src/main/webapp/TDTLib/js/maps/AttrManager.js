/***
 * @Description 该类用于管理成果属性的查询条件，包括页面上的条件选择框，和成果类别及展示类型。
 * @Author MeiXinLin
 * @CreateTime 2015年7月15日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
var AttrManager = function() {

//-------------------------------------公有属性------------------------------------------------------
    
    /**
     * 属性标签结果集
     */
    this.attrInfo = {};
    
    /**
     * 当前数据类型
     */
    this.resType = 0;
    
    /**
     * 站点ID
     */
    this.storeID = 0;
    
    /**
     * 站点名称
     */
    this.storeName = null;
    
    /**
     * 选择时间
     */
    this.selectDate=[null,null];
    
    /**
     * 查询获取的标签属性值（来源：后台设置）
     */
    this.tempAttrsList=null;
    
    
//-------------------------------------检索、重置点击事件------------------------------------------------------    
    
    this.resetFun=function(){
    	
    	//重置记录的比例尺
    	SearchTools.getInstance().setLookScale(null);
    	//重置记录的文件批次
    	SearchTools.getInstance().setFileID(null);
    	//重置记录的临时关键字
    	SearchTools.getInstance().setTempKeyWord(null);
    	SearchTools.getInstance().setIsWithin(false);
    	//空间条件重置
    	window.mapPage.mapManager.mapTools.clearMapValueClickFn(null);
    	//属性条件重置
    	window.mapPage.attrManager.clearAttrs();
    	//关键字条件重置
    	window.mapPage.attrManager.clearKeyWord();
    	window.mapPage.listManager.showStatList("searchBox");
    }
    
    
    /**
     * 重置点击事件 包含属性条件重置、关键字条件重置、地图空间查询条件重置
     */
    var _resetClickEvent=function(e){
    	
    	var attrManger=e.data;
    	
    	//重置右侧显示mapInfo高度 并隐藏new-counter
    	var h = document.documentElement.clientHeight;
		var headerHeight = mapPage.pageObject.header.height;
		$(".mapInfo").height(h - headerHeight);
		$(".new-counter").hide();
		$('.listBox_out').css({'height':h-headerHeight+'px'});
    	//重置函数
		attrManger.resetFun();
    	
    }
    
    /**
     * 关键字重置
     */
    this.clearKeyWord = function(){
    	
    	$(".searchInput").val("");
    	SearchTools.getInstance().setTempKeyWord(null);
    	SearchTools.getInstance().setKeyWord(null);
    	SearchTools.getInstance().setKeyFilter(true);
    }
    
    /***
     * 属性重置
     */
    this.clearAttrs = function() {

        this.attrInfo = {};
        this.selectDate=[null,null];
        //this.clearAttr();因为清除了两次会有dom的闪动
        if(this.resType!=null&&this.resType!=""){
        	this.setResType(this.resType);
        }
        $(".z-map-dialog").hide();
    };
    
    /***
     * 清除所有属性
     */
    this.clearAttr = function() {
        var $box = $(".attrItem");

        for (var i = 1; i < $box.length; i++) {
            $(".attrItem:last").remove();
        }
        
        $('.cloud').remove();
	    $('.startdate').remove();
		$('#spliceID').remove();
		$('.enddate').remove();
    };
    
//-------------------------------------类别统计回调函数 增加属性标签------------------------------------------------------
    
    /***
     * 增加一系列属性选择的combobox
     * @param list {Array} 属性列表
     */
    this.curAttrList=null;
    this.addAttrItems = function(list) {
        var max = 6;
        this.curAttrList=list;
        for (var i = 0; i < list.length && i < max; i++) {
            var item = list[i];
            var flag = this.addAttrItem(list[i]);
        }
        this.setAttrBoxValues();
        //如果站点隐藏分发单位
        if(this.storeID!=0){
        	$("#dtDistorCont").hide();
        }else{
        	$("#dtDistorCont").show();
        }
    };
    
    /***
     * 增加单个属性选择的combobox
     * @param obj {Object} 单个属性信息
     * @return {JqueryObject} 属性的Jquery对象
     */
    this.addAttrItem = function(obj) {
        var $container = $(".attrsBox");
        if(obj.format=="date"){
        	var startdateStr = '<div class="startdate"><div>';
			startdateStr    += obj.dName+'</div>';
			startdateStr    += '<input type="text" class="laydate-icon" tempID="'+obj.name+'"  id="start-date" placeholder="起始日期" readonly="readonly"/></div>';
			var $sds = $(startdateStr);
	        $container.append($sds);
	        $("#start-date").bind("click",this, _startdate);
	        
	        var attStr = '<div id="spliceID" style="margin-left:2px;margin-right:2px;color:#AAAAAA;float:left;">─</div>';
			var $att = $(attStr);
	        $container.append($att);
	        
	        var enddateStr = '<div class="enddate">';
			enddateStr    += '<input type="text"  class="laydate-icon"  tempID="'+obj.name+'"  id="end-date" placeholder="结束日期" readonly="readonly"/></div>';
			var $eds = $(enddateStr);
	        $container.append($eds);
	         $("#end-date").bind("click",this, _enddate);
	         
        }else if(obj.format=="slider"){
        	var id=obj.name;
        	var sliderStr='<div class="cloud">';
			sliderStr   +='<div class="firstchild-cloud">云量</div>';
			sliderStr   +='<div id="ratio-container"><input type="text" class="js-check-change" /></div>';
			sliderStr   +='<div isFirst="yes" id="ratio-number">0</div></div>';
			var $slider = $(sliderStr);
	        $container.append($slider);
	        //初始化滑块
	        var changeInput=document.querySelector('.js-check-change');
	        var initChangeInput = new Powerange(changeInput, { start:0,hideRange: true });
	        $('.range-handle').css({'width':'20px','height':'20px','top':'-8.5px','cursor':'move'});
	        changeInput.onchange = function() {
			    $('#ratio-number').html(changeInput.value);
			    var mapPage     = window.mapPage;
		        var attrManager = mapPage.attrManager;
	            attrManager.attrInfo[id] =[changeInput.value];
	            SearchTools.getInstance().setAttrQuery(attrManager.attrInfo);
			};
		}else{
        	var attrStr = '<div class="attrItem" id="' + obj.name + '"><div class="itemValue" id="' + obj.name + 'Value">';
	        attrStr += obj.dName + '</div>';
	        attrStr += '<div class="valList" id="' + obj.name + 'List"></div></div>';
	        var $attr = $(attrStr);
	        $container.append($attr);
	        $attr.mouseover(function(e) {
	            var name = "#" + e.target.id + "List";
	            $(name).show();
	        });
	        $attr.mouseout(function(e) {
	            var name = "#" + e.target.id + "List";
	            $(name).removeAttr("style");
	        });
	        var values = obj.values, name = "#" + obj.name + "List", $list = $(name);
	        var count = 0;
	        for (var i = 0; i < values.length; i++) {
	            var value = values[i].name;
	            if (value == "" || value == "空" || value == "null") {
	                count++;
	                continue;
	            }
	            //转换图幅类比例尺展现形式
	            if(this.isTHScale(obj.name)){
	            	value=this.changeShowScale(value);
	            }
	            var str = '<a class="valBox" title="'+value+'" id="' + obj.name + '">' + value + '</a>';
	            $child = $(str);
	            $list.append($child);
	            $child.bind("click", this, _changeAttrBoxValue);
	        }
	
	        var childNum = $list.children().length;
	        if (childNum == 0) {
	            $attr.remove();
	            return false;
	        }
	
	        $("#" + obj.name + "Value").html(obj.dname);
	        var strTmp = '<div class="clearAttrBtn" >清除选择<div>';
	        var clearBtn = $(strTmp);
	        $list.append(clearBtn);
	        clearBtn.bind("click", this, _delAttr);
	        return $attr;
        }
    };
    
    /**
     * 私有方法，判断是否图幅类比例尺属性
     */
    this.isTHScale=function(name){
    	var bol=false;
    	var resType=this.resType;
    	if(resType=="5"||resType=="6"||resType=="7"||resType=="10"||resType=="11"){
    		if(name=="idMapScale"){
    			bol=true;
    		}
    	}
    	return bol;
    }
    /**
     * 转换真实比例尺
     */
    this.changeRealScale=function(value){
    	var scale="";
    	switch(value){
			case "1:100万":
				scale="1000000";
				break;
			case "1:50万":
				scale="500000";
				break;
			case "1:25万":
				scale="250000";
				break;
			case "1:20万":
				scale="200000";
				break;
			case "1:10万":
				scale="100000";
				break;
			case "1:5万":
				scale="50000";
				break;
			case "1:2.5万":
				scale="25000";
				break;
			case "1:1万":
				scale="10000";
				break;
			case "1:5千":
				scale="5000";
				break;
			case "1:2千":
				scale="2000";
				break;
			case "1:1千":
				scale="1000";
				break;
			case "1:5百":
				scale="500";
				break;
		}
		return scale;	
    }
    /**
     * 转换显示比例尺
     */
    this.changeShowScale=function(value){
    	var scale="";
    	switch(value){
			case "1000000":
				scale="1:100万";
				break;
			case "500000":
				scale="1:50万";
				break;
			case "250000":
				scale="1:25万";
				break;
			case "200000":
				scale="1:20万";
				break;
			case "100000":
				scale="1:10万";
				break;
			case "50000":
				scale="1:5万";
				break;
			case "25000":
				scale="1:2.5万";
				break;
			case "10000":
				scale="1:1万";
				break;
			case "5000":
				scale="1:5千";
				break;
			case "2000":
				scale="1:2千";
				break;
			case "1000":
				scale="1:1千";
				break;
			case "500":
				scale="1:5百";
				break;
    	}
    	return scale;
    }
    
    
    /***
     * 移除某个属性事件
     */
    var _delAttr = function(e,def) {
        var attrManager = e.data;
        var target = e.currentTarget;
        var id = $(target).parents(".attrItem").attr("id");
        var list=attrManager.curAttrList;
        var cn="";
        if(list!=null){
        	for(var i=0;i<list.length;i++){
        		var item=list[i];
        		var dname=item.dName;
        		var name =item.name;
        		if(id==name){
        			cn=dname;
        			break;
        		}
        	}
        }
        $('#'+id+'Value').text(cn);
        delete attrManager.attrInfo[id];
        SearchTools.getInstance().setAttrQuery(attrManager.attrInfo);
    };
    
    /***
     * 选择某属性值
     */
    var _changeAttrBoxValue = function(e) {
        var attrManager = e.data, id = e.target.id, value = e.target.innerHTML, attrItem = e.target.parentElement.parentElement;
        var divName = "#" + attrItem.id + "Value";
        $(divName).html(value);
        $(".valList").hide();
        $(".valList").removeAttr("style");
        
        //转换图幅类真实比例尺
        if(attrManager.isTHScale(id)){
        	value=attrManager.changeRealScale(value);
        }
        attrManager.attrInfo[id] = [value];
        SearchTools.getInstance().setAttrQuery(attrManager.attrInfo);
    };
    
    /**
     * 设置某属性值
     */
    this.setAttrBoxValue=function(obj,flag){
    				
    	var dName    =obj.dName;
        var format   =obj.format;
        var name     =obj.name;
        var value    =obj.value;
        
        //-----------设置标签显示值-------------------------
        var divName  = "#" + name + "Value";
        var tempValue="";
        //转换图幅类显示比例尺
        if(this.isTHScale(name)){
        	tempValue=this.changeShowScale(value);
        }
        if(format=="combox"){
        	if(tempValue!=""){
            	$(divName).html(tempValue);
            }else{
            	$(divName).html(value);
            }
        }else if(format=="date"){
        	var arry=value.split(";");
        	var startDate=arry[0];
        	var endDate  =arry[1];
        	if(startDate!=""){
        		$("#start-date").val(startDate);
        	}
        	if(endDate!=""){
        		$("#end-date").val(endDate);
        	}
        }
        
        //----------设置标签查询条件------------------------
        if(flag){
        	if(format=="combox"){
        		this.attrInfo[name] = [value];
        	}else if(format=="date"){
        		var arry=value.split(";");
            	var startDate=arry[0];
            	var endDate  =arry[1];
            	if(startDate!=""){
            		this.selectDate[0]=startDate;
            	}
            	if(endDate!=""){
            		this.selectDate[1]=endDate;
            	}
            	this.attrInfo[name] = this.selectDate;
        	}
            SearchTools.getInstance().setAttrQuery(this.attrInfo);
        }
    }
    
    /**
     * 标签聚合查询后设置属性值（解决异步问题）
     */
    this.setAttrBoxValues=function(){
    	
    	if(this.tempAttrsList!=null){
    		for(var i=0;i<this.tempAttrsList.length;i++){
    			var obj=this.tempAttrsList[i];
    			 this.setAttrBoxValue(obj,false);
    		}
    		this.tempAttrsList=null;
    	}
    }
    
    
//------------------------------------时间点击函数--------------------------------------------------------------------


	var _startdate = function(e){
	    var attrManager = e.data;
	    var id=e.target.getAttribute("tempID");
	    window.laydateClear = function(){
	    	 attrManager.selectDate[0]=null;
	         attrManager.attrInfo[id] = attrManager.selectDate;
	         SearchTools.getInstance().setAttrQuery(attrManager.attrInfo);
	    }
		laydate({
		    elem: '#start-date', //需显示日期的元素选择器
			isclear: true,
			choose: function(datas){ //选择日期完毕的回调
					console.log(datas);
					   //航空影像时间只到年
				       if(attrManager.resType=="8"){
//						   datas=datas.substr(0,4);
					   }
			           attrManager.selectDate[0]=datas;
			           attrManager.attrInfo[id] = attrManager.selectDate;
			           SearchTools.getInstance().setAttrQuery(attrManager.attrInfo);
			        }
		});
	};
	
	var _enddate = function(e){
		var attrManager = e.data;
	    var id=e.target.getAttribute("tempID");
	    window.laydateClear = function(){
	    	 attrManager.selectDate[0]=null;
	         attrManager.attrInfo[id] = attrManager.selectDate;
	         SearchTools.getInstance().setAttrQuery(attrManager.attrInfo);
	    }
		laydate({
		    elem: '#end-date', //需显示日期的元素选择器
		    isclear: true,
		    choose: function(datas){ //选择日期完毕的回调
			    	   //航空影像时间只到年
				       if(attrManager.resType=="8"){
//						   datas=datas.substr(0,4);
					   }
			           attrManager.selectDate[1]=datas;
			           attrManager.attrInfo[id] = attrManager.selectDate;
			           SearchTools.getInstance().setAttrQuery(attrManager.attrInfo);
			        }
		});
	};
    
//------------------------------------改变、设置类别事件--------------------------------------------------------------------

	/***
     * 改变类别事件
     */
    var _changeResType = function(e) {
        var attrManager = e.data, id = e.target.id, value = e.target.innerHTML;
        attrManager.father.listManager.showStatList("searchBox");
        $("#resTypeValue").html(value);
        //动态添加标签
        _TagCut(dirTypes,value,attrManager);
        attrManager.resType = id;
        $(".valList").hide();
        $(".valList").removeAttr("style");
        $("#resTypeList").hide();
        $("#typeBox").hide();
        $("#attrBox").show();
        
        attrManager.attrInfo = {};
        attrManager.selectDate=[null,null];
        //重置记录的比例尺
    	SearchTools.getInstance().setLookScale(null);
    	//重置记录的文件批次
    	SearchTools.getInstance().setFileID(null);
    	SearchTools.getInstance().setTempKeyWord(null);
        SearchTools.getInstance().setAttrQuery(new Object());
        SearchTools.getInstance().setResType(id);
       
        //空间条件重置
    	window.mapPage.mapManager.mapTools.clearMapValueClickFn(null);
        //初始化类别标签
        attrManager.father.initTag();
        //如果站点隐藏分发单位
        if(attrManager.storeID!=0){
        	$("#dtDistorCont").hide();
        }else{
        	$("#dtDistorCont").show();
        }
    };
    
    
    /***
     * 设置类别事件
     * @param resType {String} 类别id
     */
    this.setResType = function(resType) {
        var typeObj = this.types[resType], value = typeObj.typeName;
        $("#resTypeValue").html(value);
        this.resType = resType;
        //动态添加标签
        //_TagCut(dirTypes,value,this);因为生成了两次搜索条件的dom会有闪烁，MainSearch  _resultParse中会进行重置，这里重复了
        $(".valList").hide();
        $(".valList").removeAttr("style");
        $("#resTypeList").hide();
        $("#typeBox").hide();
        $("#attrBox").show();
        this.attrInfo = {};
        this.selectDate=[null,null];
        SearchTools.getInstance().setAttrQuery(new Object());
        SearchTools.getInstance().setResType(resType);
        //初始化类别标签
        this.father.initTag();
    };
    
    /**
	 * 添加标签
	 * _$obj --> 类型对象
	 * _valBox --> 类型选择的值
	 * e --> 当前类对象
	 */
	var _TagCut = function(_$obj,_valBox,e){
		var $container = $(".attrsBox");
		var $box = $(".attrItem");
	    for (var i = 1; i < $box.length; i++) {
	        $(".attrItem:last").remove();
	    }
	    $('.cloud').remove();
	    $('.startdate').remove();
		$('#spliceID').remove();
		$('.enddate').remove();
		$('.storeAttrItem').remove();
	    
		//当前选择Tag对象
		var _Tag; 
		//遍历所有类型
		for(key in _$obj){
			if(_$obj[key].typeName==_valBox){
				_Tag = _$obj[key];
				break;
			}
		}
		//如果没赋值成功
		if(!_Tag) return;
		//取出attr集合
		var _$attr = _Tag.att;
		//遍历属性数组寻找tag标签
		for(var i=0;i<_$attr.length;i++) {
			var $att = _$attr[i];	//属性对应点
			if($att.isTag==1){
				//时间格式
				if($att.format=="date"){
					$('.startdate').remove();
					$('#spliceID').remove();
					$('.enddate').remove();
					var startdateStr = '<div class="startdate"><div>';
					startdateStr    += $att.cnName+'</div>';
					startdateStr    += '<input type="text" class="laydate-icon" tempID="'+$att.name+'" id="start-date" placeholder="起始日期" readonly="readonly"/></div>';
					var $sds = $(startdateStr);
			        $container.append($sds);
			        $("#start-date").bind("click",e, _startdate);
			        
			        var attStr = '<div id="spliceID" style="margin-left:2px;margin-right:2px;color:#AAAAAA;float:left;">─</div>';
					var $attstr = $(attStr);
			        $container.append($attstr);
			        
			        var enddateStr = '<div class="enddate">';
					enddateStr    += '<input type="text"  class="laydate-icon"  tempID="'+$att.name+'"  id="end-date" placeholder="结束日期" readonly="readonly"/></div>';
					var $eds = $(enddateStr);
			        $container.append($eds);
			        $("#end-date").bind("click",e, _enddate);

				//下拉框格式
				}else if($att.format=="combox"){
					var attrStr = '<div class="attrItem" cn="'+$att.cnName+'" id="'+$att.name+'"><div class="itemValue" id="'+$att.name+'Value">';
			        attrStr += $att.cnName + '</div>';
			        attrStr += '<div class="valList" id="'+$att.name+'List"></div></div>';
			        var $attr = $(attrStr);
			        $container.append($attr);
			        var $a = $('#'+$att.name+'List');	//tag
			        var strTmp = '<div class="clearAttrBtn" >清除选择<div>';
		            var clearBtn = $(strTmp);
		            $a.append(clearBtn);
		            clearBtn.bind("click",e, _delAttr);
		            
		        //滑动条    
				}else if($att.format=="slider"){
					$('.cloud').remove();
					var id=$att.name;
					var sliderStr='<div class="cloud">';
					sliderStr   +='<div class="firstchild-cloud">云量</div>';
					sliderStr   +='<div id="ratio-container"><input type="text" class="js-check-change" /></div>';
					sliderStr   +='<div isFirst="yes" id="ratio-number">0</div></div>';
					var $slider = $(sliderStr);
			        $container.append($slider);
			        //初始化滑块
			        var changeInput=document.querySelector('.js-check-change');
			        var initChangeInput = new Powerange(changeInput, { start:0,hideRange: true });
			        $('.range-handle').css({'width':'20px','height':'20px','top':'-8.5px','cursor':'move'});
			        changeInput.onchange = function() {
					    $('#ratio-number').html(changeInput.value);
					    var mapPage     = window.mapPage;
				        var attrManager = mapPage.attrManager;
			            attrManager.attrInfo[id] =[changeInput.value];
			            SearchTools.getInstance().setAttrQuery(attrManager.attrInfo);
					};
				}
			}
		}
	}

//------------------------------------初始化相关方法--------------------------------------------------------------------

    /***
     * 初始化类别选项
     * @param types {Array} 类别数组
     */
    var _initTypes = function(types) {
        var $list = $("#resTypeList"), $list2 = $("#typeBox");
        for (var key in types) {
        	
            var item = types[key];
            var id=item.id;
        	var str = '<a class="valBox" id="' + item.id + '">' + item.typeName + '</a>';
            $child = $(str);
            $list.append($child);
            $child.bind("click", this, _changeResType);

            var str2 = '<div class="typeBtn" id="' + item.id + '">' + item.typeName + '</div>';
            var $child2 = $(str2);
            $list2.append($child2);
            $child2.bind("click", this, _changeResType);
        }
        $("#resType").mouseover(function(e) {
            $("#resTypeList").show();
        });
        $("#resType").mouseout(function(e) {
            $("#resTypeList").hide();
        });
    };
    
    /**
     * 初始化站点名称
     */
    this.initStoreName=function(){
		
		var storeName=this.storeName;
		if(this.resType==0&&storeName!=null){
			var $container = $(".attrsBox");
			var attrStr = '<div class="storeAttrItem"><div class="itemValue" id="stoteNameValue">';
			attrStr += storeName + '</div></div>';
			var $attr = $(attrStr);
			$container.append($attr);
		}
	};
    
    /**
     * 初始化方法
     */
	this.init = function(def) {
        
        this.father = def.father;
        this.types = def.dirTypes;
        //初始化类别
        _initTypes.call(this, this.types);
        
        //重置按钮点击事件
        $("#attrClearBtn").bind( "click", this, _resetClickEvent);
    };

    this.init.apply(this, arguments);
};




