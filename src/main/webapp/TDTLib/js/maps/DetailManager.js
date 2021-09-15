/***
 * @Description 详情信息显示管理类，管理各类产品详情的展示
 * @Author YuanJianHua
 * @CreateTime 2015年12月15日
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:  
 */
var DetailManager=function(){
	//var uu="http://sjfw.sasmac.cn/product/meta.imager?flag=1&metaId="+result.title.影像ID
	
	//数据源+类型 obj{源:{summary:...,details:...},源...}
	this.sorce = {};
	this.dats  = {};
	this.result=null;
	/**
	 * 图幅类产品详情展示
	 */
	this.content = function(result,resType){
		this.result=result;
		//填充表格
		this.tables(result,resType);
		/**显示div**/
		//显示半透明层
		var body_H = $('body').height();
		var h = document.documentElement.clientHeight;
        var w = document.documentElement.clientWidth;
        var _scroll = window.pageYOffset;
        var top = h/2 - (300/2);
        var left = w/2 - (425/2);
		$('#detailes-window').show();
	}
	
	/**
	 * 伪造自定义表格
	 */
	this.autoDefined = function(type){
		//从后台读取数据 obj
		var data = SearchTools.getInstance().getDataType(type);
		//获取属性 array
		var useAtt = data.tagAtt;
		//表头
		var title = new Array();
		//css
		var css = new Array();
		//cnName
		var cnName = new Array();
		//获取长度
		var length = useAtt.length;
		if(length === 0){
			useAtt = data.att.slice(0,4);
		}
		var len = useAtt.length;
		//width
		var width = Math.floor((405-30)/len);
		for(var i = 0 ; i < len; i ++){
			var attr = useAtt[i];
			//push
			title.push(attr.name);
			cnName.push(attr.cnName);
			css.push(width);
		}
		//表头
		this.showNameArr[type] = cnName;
		//key
		this.showCodeArr[type] = title;
		//css
		this.showWidthArr[type] = css;
	}
	
	/**
	 * table
	 */
	this.tables = function(list,type){
		var html = '<li style="width:30px"></li>';
		//
		if(parseInt(type)>11){
			this.autoDefined(type);
		}
		//表头
		var title = this.showNameArr[type];
		//key
		var keys = this.showCodeArr[type];
		//css
		var css = this.showWidthArr[type];
		//初始化表头
		for(var i = 0; i < title.length; i++){
			html += '<li style="width:'+css[i]+'px">'+title[i]+'</li>';
		}
		//添加title
		$('.detailes-top-title').html(html);
		//rows
		var rows = '';
		//添加rows
		for(var i = 0; i < list.length; i++){
			var obj = list[i];
			rows += '<div class="rows" rowsdid="'+obj.id+'"><div ckd="none" rowsID="'+obj.id+'" class="rows-title">';
			rows += '<li style="width:30px"><img class="icon-sharyer" src="./public/style/images/maptoolsicon/show-icon.png"/></li>';
			for(var z = 0; z < keys.length; z++){
				var kes = keys[z];
				var filed = obj[kes];
				rows += '<li title="'+filed+'" style="width:'+css[z]+'px">'+filed+'</li>';
			}
			rows += '</div><div class="rows-content" rowsContentId="'+obj.id+'"></div></div>';
		}
		//添加
		$('.detailes-bottom-rows').html(rows);
		//bind
		$('.rows-title').bind('click',this,this.choosed);
		//
		if(list.length==1){
			$('[rowsID="'+list[0].id+'"]').click();
		}
	}
	
	/**
	 * 初始化
	 */
	this.initClick = function(e){
		//概要·
		$('.summary-btn').live('click',function(){
			$('.details-content').hide();
			$('.summary-content').show();
			//边框
			$(this).css('border-color','#519dde');
			$('.detail-btn').css('border-color','#e8e8e8');
			
			setTimeout(function(){
				var h = formate($('.summary-content .itemrs'));
				$('.summary-content').css('height',(h + 30) +'px');
			}, 50);
			
		});
		//详情
		$('.detail-btn').live('click',function(){
			$('.details-content').show();
			$('.summary-content').hide();
			//边框
			$(this).css('border-color','#519dde');
			$('.summary-btn').css('border-color','#e8e8e8');

			var h = formate($('.details-content .itemrs'));
			$('.details-content').css('height',h+'px');
		});
		
		var thi$ = this;
		//加入成果车
		$('.detail-car').live('click',function(){
			var id = $(this).parent().parent().attr("rowscontentid");
			thi$.father.addShopCar(id);
		});

		//source
		$('.zone').live('click',function(){
			//css
			$(this).css({'background':'url(./public/style/images/maptoolsicon/bulue-icons.jpg) no-repeat 100% 100%',
				'border':'2px solid #519DDE','padding-left':'9px','padding-right':'9px',
				'height':'18px','line-height':'18px'}).siblings().attr('style','');
			//js
			var name = $(this).text();
			//添加
			var id = $(this).parent().parent().parent().attr('rowscontentid');
			e.detailes(name,id);
		});
	}
	
	/**
	 * click
	 */
	this.choosed = function(e){
		//获取必要数据
		var id = $(this).attr('rowsID');
		var checked = $(this).attr('ckd');
		//判断选中状态
		var flag = false;
		if(checked == 'none'){
			flag = true;
			e.data.request(id,e.data.forma);
		}
		//添加样式
		e.data.cleanSingalButton(flag,id);
		
		window.mapPage.listManager.normalListObj.changeOneById(id,flag);
	}
	
	//单选样式
	this.cleanSingalButton = function(flag,id){
		if(flag){
			$('[rowsdid="'+id+'"]').find('.rows-title').attr('ckd','ckd').
				css({'background':'#f0f0f0'}).find('li').css({'color':'#519dde'}).find('.icon-sharyer').attr('src','./public/style/images/maptoolsicon/hide-icon.png');
			$('[rowsdid="'+id+'"]').siblings().find('.rows-title').attr('ckd','none').
			css({'background':'#fff'}).find('li').css({'color':'#333333'}).find('.icon-sharyer').attr('src','./public/style/images/maptoolsicon/show-icon.png');
			$('[rowsdid="'+id+'"]').siblings().find('.rows-content').html('');
		}else{
			$('[rowsdid="'+id+'"]').find('.rows-title').attr('ckd','none').
				css({'background':'#fff'}).find('li').css({'color':'#666666'}).find('.icon-sharyer').attr('src','./public/style/images/maptoolsicon/show-icon.png');
			//清除
			$('[rowsContentId="'+id+'"]').html('');
		}
	}

	/**
	 * 发送请求
	 */
	this.request = function(id,callBack){
		$.ajax({
			url:'./dataEnty.do?method=getMapDetail',
			method:'POST',
			data:{id : id},
			dataType:'json',
			success:function(data){
				callBack(data);
			},
			error:function(){
				cleanDIV();
			}
		});
	}
	/**
	 * 添加详情概要
	 */
	this.detailes = function(key,id){
		/*类型*/
		var dt = this.dt;
		//数据归类
		var sos = this.sorce;
		//内容
		var objs = sos[key];
		//概要
		var content = objs.summary;
		//详情
		var details = objs.detail;
		
		var html = '';
		//添加图片
		var img = dt.imageUrl;
		if(dt.id==9){
			if(this.isWeiXiZX(id)){
				img = 'http://sjfw.sasmac.cn/product/meta.imager?flag=1&metaId='+content[0]["影像ID"]+'';
				html +='<div class="itemrs"><img onerror="window.mapPage.detailManager.onerror(this)" src="'+img+'"/></div>';
			}else{
				img = this.getImageByID(id);
				html +='<div class="itemrs"><img onerror="window.mapPage.detailManager.onerror(this)" src="'+img+'"/></div>';
//				img = 'http://sjfw.sasmac.cn/product/meta.imager?flag=1&metaId='+content[0]["影像ID"]+'';
//				html +='<div class="itemrs"><img onerror="window.mapPage.detailManager.onerror(this)" src="'+img+'"/></div>';
			}
		}
		if(this.dats.pic){
			html +='<div class="itemrs"><a href="'+this.dats.pic+'" target="_blank"><img src="'+this.dats.pic+'"/></a></div>';
		}
		for(var i = 0; i < content.length;i++){
			var obj = content[i];
			for(key in obj){
				html += '<li class="itemrs"><span>'+key+' : </span><span>'+obj[key]+'</span></li>';	
			}
		}
		//添加概要
		$('[rowscontentid="'+id+'"]').find('.summary-content').html(html);
		
		var deHtml = '';
		for(var i = 0; i < details.length;i++){
			var obj = details[i];
			for(key in obj){
				deHtml += '<li class="itemrs"><span>'+key+' : </span><span>'+obj[key]+'</span></li>';
			}
		}
		//添加详情
		$('[rowscontentid="'+id+'"]').find('.details-content').html(deHtml);
		$('[rowsContentId="'+id+'"]').find('.summary-btn').click();

	}
	
	/*
	 * 初始化加载头部
	 */
	this.iniAped = function(id){
		var scor = this.sorce;
		var data = this.dats;
		var obj = null,kes = ''+data.sorce,index=0,mark = true;
		
		//判断是否需要添加数据源
		var html = '';
		if(data.noStandardRange){
			html += '<div class="more-header"><div class="header-discription">图幅所跨片区:</div><div class="land-map">';
			for(key in scor){
				if(mark){
					kes = key;
					mark = false;
				}
				html += '<div class="zone">'+key+'</div>';
			}
			html += '</div></div>';
		}
		//button
		html += '<div class="tail-title"><li class="summary-btn">概要</li><li class="detail-btn">详情</li><li class="detail-car">加入成果车</li></div>';
		//
		html += '<div class="summary-content"></div><div class="details-content" style="display:none;"></div>';

		$('[rowsContentId="'+id+'"]').html(html);
		if(kes=='undefined')
			kes='';
		this.detailes(kes,id);
		$('[rowsContentId="'+id+'"]').find('.zone').eq(0).click();
		//计算高度
		var offsettop = $('[rowsdid="'+id+'"]')[0].offsetTop;
		$('.continer-scrollbar').animate({
			scrollTop:offsettop
		},500);
	}
	
	/**
	 * 格式化数据,按类型分组
	 */
	this.forma = function(data){
		/*概要*/	
		var summary = data.summary;
		window.mapPage.detailManager.dats = data;
		/*详情*/
		var detail = data.detailes;
		/*类型*/
		var dt = window.mapPage.detailManager.dt = data.type;
		//源
		var idMapTitle = data.sorce;
		//判断是否有多个描述
		var moreSource =null;
		if(idMapTitle!=null){
			moreSource = idMapTitle.split('&');
		}
		var sorce = {};
		if(!moreSource){
			//无数据源字段
			sorce[""] = {'summary':summary,'detail':detail}
		}else if(moreSource.length > 1){
			//多种
			
			for(var i = 0; i < moreSource.length; i++){
				//1.切分概要
				//key
				var obj = moreSource[i];
				//创建容器
				var $newSummary = [];
				//获取概要
				for(var z = 0; z < summary.length; z++){
					//类型为obj
					var map = summary[z];
					for(key in map){
						var $newMap = {}
						//分割值
						var $valArray = map[key].split('&');
						if($valArray.length>1){
							$newMap[key] = $valArray[i];
						}else{
							$newMap[key] = $valArray[0];
						}
						$newSummary.push($newMap);
					}					
				}
				//创建对象
				sorce[obj] = {}
				sorce[obj]['summary'] = $newSummary;
				
				//2切分详情
				var $newDetails = [];
				for(var z = 0; z < detail.length; z++){
					//类型为obj
					var map = detail[z];
					for(key in map){
						var $newMap = {}
						//分割值
						var $valArray = map[key].split('&');
						if($valArray.length>1){
							$newMap[key] = $valArray[i];
						}else{
							$newMap[key] = $valArray[0];
						}
						$newDetails.push($newMap);
					}					
				}
				//创建对象
				sorce[obj]['detail'] = $newDetails;
			}
		}else{
			//单个
			sorce[moreSource[0]] = {'summary':summary,'detail':detail}
		}
		window.mapPage.detailManager.sorce = sorce;
		//初始化加载
		window.mapPage.detailManager.iniAped(data.id);
		//判断是否添加
//		if(data.pic){
//			$('.tail-title').append('<li style="border-color: rgb(232, 232, 232);"><a href="'+data.pic+'" target="_blank">缩略图</a></li>');
//		}
	}
	
	/**
	 * 根据ID获取省局卫星影像图片
	 */
	this.getImageByID = function(id){
		
		var image="./public/style/images/store/HK.png";
		if(this.result!=null){
			for(var i=0;i<this.result.length;i++){
				var obj=this.result[i];
				var objID=obj.id;
				if(id==objID){
					var imageName=obj.idBgFileName;
					if(obj.storeId == '92'){//特殊处理黑龙江
						image = "./weixipicture/"+obj.idGranuleID+".jpg";
					}else{
						image="./weixipicture/"+imageName;
					}
					break;
				}
			}
		}
		return image;
	}
	
	/**
	 * 根据ID判断是否为卫星中心数据
	 */
	this.isWeiXiZX = function(id){
		
		var flag=false;
		if(this.result!=null){
			for(var i=0;i<this.result.length;i++){
				var obj=this.result[i];
				var objID=obj.id;
				if(id==objID){
					var storeId=obj.storeId;
					if(storeId=="3"){
						flag=true;
					}
					break;
				}
			}
		}
		return flag;
	}
	
	
	/**
     * 缩略图片加载失败事件
     */
    this.onerror = function(e){
    	var clazz = $(e).attr('class');
    	var imgs = './public/style/images/nopicture.png';
    	$(e).attr('src',imgs);
    }
	
    
	/**
	 * 绑定关闭点击事件
	 */
	this.closeClickFn=function(){
		$(".close-s-btn").click(this.closeFunction);
	}
	
	/**
	 * 关闭执行函数
	 */
	this.closeFunction=function(){
		$('#shadowZone').hide();
		$('#detailes-window').hide();
		window.mapPage.mapManager.unClickRefushGeom();
	}
	
	/**
	 * open
	 */
	this.open = function(){
		$('.iconOfTps').click(function(){
			$('#detailes-window').show();
		});
	}

	 /**
     * 各类型显示名称配置参数
     */
    this.showNameArr={
    		"1":["点名","网名","等级类型","大地基准"],
    		"2":["路线名称","点名","网名","高程基准"],
    		"3":["点名","网名","等级","大地基准"],
			"4":["点名","网名","等级","大地基准"],
			"5":["新图号","生产时间","大地基准","数据格式"],
			"6":["新图号","格网间距","大地基准","数据格式"],
			"7":["新图号","分辨率","影像时间","大地基准"],
			"8":["摄区名称","航摄比例尺","分辨率","航摄仪"],
			"9":["卫星","成果类型","传感器","分辨率"],
			"10":["新图号","出版时间","大地基准","数据格式"],
			"11":["新图号","出版时间","大地基准","高程基准"]
    };
    
    /**
     * 各类型显示属性配置参数
     */
    this.showCodeArr={
    		"1":["idName","idNet","idClass","crsDatum"],
			"2":["idLnName","idName","idNet","crsVertCRS"],
			"3":["idName","idNet","idClass","crsDatum"],
			"4":["idName","idNet","idClass","crsDatum"],
			"5":["idNewMapNum","idFormData","crsDatum","dtFormatName"],
			"6":["idNewMapNum","idScaleDist","crsDatum","dtFormatName"],
			"7":["idNewMapNum","idScaleDist","TePosition","crsDatum"],
			"8":["idGranuleName","idEquScale","idScaleDist","idSensor"],
			"9":["idSatellite","idResType","idSensor","idScaleDist"],
			"10":["idNewMapNum","idFormData","crsDatum","dtFormatName"],
			"11":["idNewMapNum","idVersionType","crsDatum","crsVertCRS"]
    };
    
    /**
     * 各类型显示宽度配置参数
     */
    this.showWidthArr={
    		"1":["90","90","90","105"],
    		"2":["90","90","90","105"],
    		"3":["90","90","90","105"],
    		"4":["90","90","90","105"],
    		"5":["90","90","90","105"],
    		"6":["90","90","90","105"],
    		"7":["90","90","90","105"],
    		"8":["90","90","90","105"],
    		"9":["90","90","90","105"],
    		"10":["90","90","90","105"],
    		"11":["90","90","90","105"],
    };
	
//---------------------------------------初始化方法--------------------------------------------------

    this.init = function(def) {
        this.father     = def.father;
        //绑定关闭事件
        this.closeClickFn();
        //绑定详情点击事件
        this.initClick(this);
        //打开事件
        this.open();
    };
	
	
	this.init.apply(this, arguments);		
	
	
}
/**
 * 清空div
 */
function cleanDIV(){
	$('.summary-description-content').html('');
	$('.details-content-show').html('');
}