var portalview = {};
portalview.fullObj = null;
//门户布局获取
portalview.layoutPortal = function(portalId,portalType) {
	/*portalview.clearportlet();
	portalview.clearportlet1();
	portalview.clearportlet2(); */
	jQuery.ajax({
        url: getRootPath()+"/sys/portlet/getLayoutPortals.action?portalId="+portalId+"&portalType="+portalType+"&time="+(+new Date().getTime()),
        type: 'get',
        success: function(data, textStatus, xhr) {
        	portalview.initiallayout(data.layoutType);
        	var dataObj=data.portletList; 
        	var relistsize = data.reListSize;
        	var relist = data.reList; 
        	//var layoutid = data.layoutId;
        	if(relistsize > 0){
        		for(var i=0;i<relist.length;i++){
        			if(relist[i].viewType != 'shortcut'){
        				portalview.layoutPortals(relist[i].id,relist[i].letTitle,relist[i].siteY,relist[i].siteX,relist[i].layoutPackId,relist[i].viewType);
        			} else {
        				portalview.setShortcut(relist[i].id,relist[i].functionId);
        			}
        			if(relist[i].viewType != 'textareaEdit' && relist[i].viewType != 'printEdit'){
        				portalview.setDivPortlets(relist[i].id,relist[i].functionId,relist[i].viewType);
        			}
        		}
        	} else {
        		var coordinateX1=0;
        		var coordinateX2=0;
        		var coordinateX3=0;
        		var coordinateY1=0;
        		var coordinateY2=0;
        		var coordinateY3=0;
        		for(var i=0;i<dataObj.length;i++){ 
        			if(dataObj[i].viewType != 'shortcut'){
        				if((i+1)%3 == 1){
        					if(coordinateX1 == 3){
        						coordinateY1++;
        						coordinateX1=0;
        					}
        					portalview.layoutPortals(dataObj[i].id,dataObj[i].letTitle,coordinateY1,coordinateX1,1,dataObj[i].viewType);
        					coordinateX1++;
        				}else if((i+1)%3 == 2){
        					if(coordinateX2 == 3){
        						coordinateY2++;
        						coordinateX2=0;
        					}
        					portalview.layoutPortals(dataObj[i].id,dataObj[i].letTitle,coordinateY2,coordinateX2,2,dataObj[i].viewType);
        					coordinateX2++;
        				}else if((i+1)%3 == 0){
        					if(coordinateX3 == 3){
        						coordinateY3++;
        						coordinateX3=0;
        					}
        					portalview.layoutPortals(dataObj[i].id,dataObj[i].letTitle,coordinateY3,coordinateX3,3,dataObj[i].viewType);
        					coordinateX3++;
        				}
        			}
        			if(dataObj[i].viewType != 'textareaEdit' && dataObj[i].viewType != 'printEdit'){
        				portalview.setDivPortlets(dataObj[i].id,dataObj[i].functionId,dataObj[i].viewType);
        			}
                }
        	}
        	
        	var t = $("#controlshortcut").html();
			if(!$.trim(t)){
				$("#shortcutShow").css("display","none");
			}

            portalview.clearPortlet(1);
            portalview.clearPortlet(2);
            portalview.clearPortlet(3);
			//全屏展开
			if(JCFF.version3()) portalview.fullObj = new JCFF.LaunchFull();
        },error:function(e){
        	// alert("加载数据错误。");
        	msgBox.tip({content: "加载数据错误", type:'fail'});
        }
    });
};

//加载数据替换门户模块信息
portalview.setDivPortlets = function(divid,functionId,viewType){
	if($("#fun_"+divid).length>0){
		jQuery.ajax({
	        url: getRootPath()+"/sys/portalFunction/getFunctionForPortlet.action?layoutStatus=view&tabsize=0.3&functionId="+functionId+"&viewType="+viewType+"&portletId="+divid+"&time="+(+new Date().getTime()),
	        type: 'get',
	        //async:false,
	        success: function(data, textStatus, xhr) {
	        	$("#fun_"+divid).html(data);
	        },error:function(){
	      	 // alert("加载数据错误。");
	      	  msgBox.tip({content: "加载数据错误", type:'fail'});
	        }
	    });
	} else {
		if(viewType == 'shortcut' && $("#div_"+divid).length > 0){
			$("#liout_"+divid).html("<div class=\"fl panel-con-wrap inform-con\"><span>无访问权限</span></div>");
		}
	}
};
//设置快捷方式
portalview.setShortcut = function(portalvoId,functionId){
	$("#controlshortcut").prepend("<li id='liout_"+portalvoId+"'></li>");
};
//触发更多事件
portalview.loadMore = function(pId,fId){
	var flag = (pId && fId);
	var more = flag ? (pId +'_'+ fId) : pId;

	var moreUrl = $('#morelink_'+more).val(),
		menubarUrl = $('#menubarlink_'+more).val();

	//loadrightmenu(moreurl,'',window.parent.loadmenubarurl.value);

	JCFF.loadPage({
		url  : moreUrl
	});
};

//设置布局
portalview.layoutPortals = function(portletid,letTitle,x,y,layoutPackId,viewType) {
	$('#portalsLayout'+layoutPackId).portlet('option', 'filterRepeat',true);
	$('#portalsLayout'+layoutPackId).portlet('option', 'add',{
			position: {
				x: x,
				y: y
		    },
		    portlet: {
		        attrs: {
		            id: portletid
		        },
		        beforeRemove: function() {
                    return true;
                },
		        content: {
		            type: 'text',
		            text: function() {
		            	$(".loadfunctionview").each(function() {
		            		$(this).empty();
		            	});
		            	if(viewType == 'textareaEdit' && $("#letTextarea_"+portletid).val() != undefined){
		            		var co=$("#letTextarea_"+portletid).html();
		            		$("#letTextarea_"+portletid).html(co.replace(/(_p;)/g,"&nbsp;"));
		            	}
		            	var htmlvalue = $('#div_'+portletid).html();
		            	$('#div_'+portletid).html("");
                        return htmlvalue;
                    }
		        }
		    }
    });
};

//清除临时门户组件
portalview.clearPortlet = function(layoutPackId){
	$('#portalsLayout'+layoutPackId).portlet('option', 'remove', '#news1');
	$('#portalsLayout'+layoutPackId).portlet('option', 'remove', '#news2');
	$('#portalsLayout'+layoutPackId).portlet('option', 'remove', '#news3');
	$('#portalsLayout'+layoutPackId).portlet('option', 'remove', '#-99');
};

//根据类型初始化门户布局
portalview.initiallayout = function(layouttype){
	$('#loadLayout').html($('#loadLayout'+layouttype).html());
	if(layouttype == 1){
		portalview.clearportlet(1);
		portalview.clearportlet(2);
		portalview.clearportlet(3);
	}
	if(layouttype == 2){
		portalview.clearportlet1(1);
		portalview.clearportlet(2);
		portalview.clearportlet(3);
	}
	if(layouttype == 3){
		portalview.clearportlet(3);
		portalview.clearportlet1(1);
		portalview.clearportlet2(2); 
	}
	if(layouttype == 4){
		portalview.clearportlet1(3);
		portalview.clearportlet1(1);
		portalview.clearportlet1(2); 
	}
	if(layouttype == 5){
		portalview.clearportlet(3);
		portalview.clearportlet(1);
		portalview.clearportlet(2); 
	}
	if(layouttype == 6){
		portalview.clearportlet2(3);
		portalview.clearportlet2(1);
		portalview.clearportlet2(2); 
	}
	if(layouttype == 7){
		portalview.clearportlet3(3);
		portalview.clearportlet(1);
		portalview.clearportlet3(2); 
	}
	if(layouttype == 8){
		portalview.clearportlet2(3);
		portalview.clearportlet2(1);
		portalview.clearportlet2(2); 
	}
	if(layouttype == 9){
		portalview.clearportlet3(3);
		portalview.clearportlet3(1);
		portalview.clearportlet3(2); 
	}
};

//清空门户布局数据
portalview.clearportlet = function(layouttype){//3:3
	$('#portalsLayout'+layouttype).portlet({
        sortable: true,
        singleView: false,
        columns: [
            { 
            	portlets: [{
            		attrs: {
            			id: 'news1'
            		},
            		content: {
            			type: 'text',
            			text: function() {
                            return "";
                        }
            		}
            	}]
            },{ 
            	portlets: [{
            		attrs: {
            			id: 'news2'
            		},
            		content: {
            			type: 'text',
            			text: function() {
            				return "";
                        }
            		}
            	}]
            },{ 
            	portlets: [{
            		attrs: {
            			id: 'news3'
            		},
            		content: {
            			type: 'text',
            			text: function() {
            				return "";
                        }
            		}
            	}]
            }
        ]
	});
};

//清空门户布局数据
portalview.clearportlet1 = function(layouttype){//1:1 初始化
	$('#portalsLayout'+layouttype).portlet({
        sortable: true,
        singleView: false,
        columns: [
            { 
            	portlets: [{
            		attrs: {
            			id: 'news1'
            		},
            		content: {
            			type: 'text',
            			text: function() {
                            return "";
                        }
            		}
            	}]
            },{ 
            	portlets: [{
            		attrs: {
            			id: 'news2'
            		},
            		content: {
            			type: 'text',
            			text: function() {
            				return "";
                        }
            		}
            	}]
            }
        ]
	});
};

//清空门户布局数据
portalview.clearportlet2 = function(layouttype){//2:1 初始化
	$('#portalsLayout'+layouttype).portlet({
        sortable: true,
        singleView: false,
        columns: [
            { 
            	portlets: [{
            		attrs: {
            			id: 'news1'
            		},
            		content: {
            			type: 'text',
            			text: function() {
                            return "";
                        }
            		}
            	}]
            },{ 
            	portlets: [{
            		attrs: {
            			id: 'news2'
            		},
            		content: {
            			type: 'text',
            			text: function() {
            				return "";
                        }
            		}
            	}]
            }
        ]
	});
};
//清空门户布局数据
portalview.clearportlet3 = function(layouttype){//1 初始化
	$('#portalsLayout'+layouttype).portlet({
        sortable: true,
        singleView: false,
        columns: [
            { 
            	portlets: [{
            		attrs: {
            			id: 'news1'
            		},
            		content: {
            			type: 'text',
            			text: function() {
                            return "";
                        }
            		}
            	}]
            }
        ]
	});
};

if(!JCFF){
	var JCFF = {};
}


//top5功能 实现类
JCFF.TopFive = Clazz.extend({
	construct : function(){
		this.pWin    = JCFF.getTop();
		this.histObj = this.pWin.JCF.histObj;
		this.$ele    = $('#topfive');
		this.cacheData = [];
		this._draw();
	},
	_draw : function() {
		var data = this.histObj.getTopFive();
		this.$ele.find('>ul').html(this._getTemplate(data));
		this._addEvent();
	},
	_addEvent : function(){
		var self = this;
		self.$ele.on('click' , '.topfive', function(e){
			if(this.id){
				self.goToMenu(self.cacheData[this.id]);
			}
			e.preventDefault();
		});
	},
	_getTemplate : function(datas){
		var template = [];
		for(var i = 0;i < datas.length;i++){
			var obj = datas[i];
			this.cacheData[obj.menuId] = obj;
			//obj.menuAction
			//obj.menuId
			template.push('<li>' +
						'<i class="fa fa-bookmark2 icon fl">' +
							'<b class="bg-yellow-dark">&nbsp;</b>' +
						'</i>' +
						'<div class="fl panel-con-wrap inform-con rounded">' +
							'<a href="javascript:;" class="topfive" id="'+obj.menuId+'">' +
								/*'<strong>'+obj.clicks+'</strong>' +*/
								'<span>'+obj.menuName+'</span>' +
							'</a>' +
						'</div>' +
					'</li>');
		}
		return template.join('');
	},
	goToMenu : function(obj){
		JCFF.loadPage({
			id : 'jcleftmenu_'+obj.menuId,
			name : obj.menuName,
			url  : obj.menuAction,
		} ,true);
	}
});
/**
 * 全屏展开收起方法类
 */
JCFF.LaunchFull = Clazz.extend({
	construct : function(){
		this.$eles = $('.index-inform');
		this.oldScreen = null;
		this.oldContrier = null;
		this.listeners = [];
		this._addEvent();
	},
	_addEvent : function(){
		var self = this;
		self.$eles.each(function(index , item){
			var $item = $(item);
			self.oldContrier = $item;

			$item.on('click', '.full-screen' ,function(e){
				self.oldScreen = e.target;
				self._launchFullScreen($item ,e.target);
				e.preventDefault();
			});

			$item.on('click' , '.exit-full' ,function(e){
				self.oldScreen = null;
				self._exitFullscreen($item ,e.target);
				e.preventDefault();
			});
		});

		JCFF.$document.on("keydown",function(e){
			if(e.which==27) {
				self._exitFullscreen(self.oldContrier ,self.oldScreen);
			}
		});
	},
	//全屏
	_launchFullScreen : function(contriner ,ele){
		var $this = $(ele);
		contriner.addClass('full-box').css({
			'height':'100%',
			'overflow-y' : 'auto'
		});
		$this.hide();
		$this.next('i').show();
		this.screenCallback();
	},
	//关闭全屏
	_exitFullscreen : function(contriner ,ele){
		if(ele){
			var $this = $(ele);
			contriner.removeClass('full-box').css({
				'height':'',
				'overflow-y' : 'hidden'
			});
			$this.hide();
			$this.prev('i').show();
			this.screenCallback();
		}
	},
	addListener : function (event){
		this.listeners.push(event);
	},
	screenCallback : function(){
		var listeners = this.listeners;
		if(listeners.length){
			for(var i = 0;i < listeners.length;i++){
				var listener = listeners[i];
				if(typeof listener === 'function'){
					listener.call(null);
				}
			}
		}
	}
});
function setTimeline(){
	$('#suspend-list').css('height',$(document).height() - 205+'px');
}
//初始化
$(document).ready(function(){
	portalview.layoutPortal($('#portalId').val(),$('#portalType').val());
	//top5初始化
	if(JCFF.version3()){
		new JCFF.TopFive();
		setTimeline();
		$(window).resize(function(){
			setTimeline();
			if(portalview.fullObj){
				portalview.fullObj.screenCallback();
			}
		});
	}
});
