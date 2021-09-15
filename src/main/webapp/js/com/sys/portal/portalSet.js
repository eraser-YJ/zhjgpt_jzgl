var portalset = {}, pageCount=10;
var map = {};   	
//重复提交标识
portalset.subState = false;
//分页处理 start
//分页对象
portalset.oTable = null;
//图片文本域数组
var picsAndAreas = {};  
//文本域值数组
var textAreaValue = {};
//布局列数
var portalnum1=0,portalnum2=0,portalnum3=0;
//门户图片组件初始化数组
portalset.isGetPhoto=[];
//显示列信息
//门户
portalset.oTableAoColumns = [
	{ "mData": "portalName"},
	{mData: "portalStatus", mRender : function(mData, type, full){
			return full.portalStatusValue;
		}
	},
	{ "mData": "sequence" },
	//设置权限按钮
	{mData: function(source) {
		return oTableSetButtonsportal(source);
	}}
];

portalset.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(portalset.oTable, aoData);
	
	var portalType = $("#portalType").val(); /*门户类型 ptype_org 机构 ptype_dept 部门  ptype_user 个人*/
	if(portalType != ""){
		aoData.push({ "name": "portalType", "value": $.trim(portalType)});
	}
};

//分页查询
portalset.portalsetList = function () {
	if (portalset.oTable == null) {
		portalset.oTable = $('#portalsetTable').dataTable( {
			"iDisplayLength": portalset.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/portal/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": portalset.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				portalset.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3]}
	        ]
		} );
	} else {
		portalset.oTable.fnDraw();
	}
};


//门户设置
portalset.setportal = function(portalId) {
	jQuery.ajax({
        url: getRootPath()+"/sys/portlet/getPortletList.action?portalId="+portalId+"&time="+(+new Date().getTime()),
        type: 'post',
        success: function(data, textStatus, xhr) {
        	$("#portlets").html(data);//要刷新的div
        	$("#portalId").val(portalId);
        	portalset.layoutPortal(portalId);
        },error:function(e){
        	// alert("加载数据错误。");
        	msgBox.tip({content: "加载数据错误", type:'fail'});
        }
    });
};

//门户布局获取
portalset.layoutPortal = function(portalId) {
	jQuery.ajax({
        url: getRootPath()+"/sys/portlet/getLayoutPortals.action?portalId="+portalId+"&portalType="+$('#portalType').val()+"&time="+(+new Date().getTime()),
        type: 'post',
        success: function(data, textStatus, xhr) {
        	portalset.initiallayout(data.layoutType);
        	$("#controlshortcut").html("");
        	$("#controlshortcut").sortable({
                update: function (event, ui) {}
            });
        	var dataObj=data.portletList; 
        	var relistsize = data.reListSize;
        	var relist = data.reList; 
        	var layoutid = data.layoutId;
        	if(layoutid != '0'){
        		$("#id").val(layoutid);
        	}
        	if(relistsize > 0){
        		for(var i=0;i<relist.length;i++){
        			//document.getElementById("checkbox_"+relist[i].id).checked = true;
        			$("#checkbox_"+relist[i].id).attr("checked",true);
        			/*if(relist[i].viewType != 'textareaEdit' && relist[i].viewType != 'printEdit'){
        				portalset.setDivPortlets(relist[i].id,relist[i].functionId,relist[i].viewType);
        			}*/
        			if(relist[i].viewType != 'shortcut'){
        				portalset.layoutPortals(relist[i].id,relist[i].letTitle,relist[i].siteY,relist[i].siteX,relist[i].layoutPackId,relist[i].viewType,relist[i].functionId);
        				if(relist[i].viewType == 'textareaEdit'){
        					$("#count_"+relist[i].id).html(200-$("#letTextarea_"+relist[i].id).text().length);
        				}
        			} else {
        				$("#controlshortcut").prepend("<li id='liout_"+relist[i].id+"' class='pos-rlt' title='"+relist[i].id+"'><div class='index-disabled'></div></li>"); 
        				portalset.setDivPortlets(relist[i].id,relist[i].functionId,relist[i].viewType);
        			}
        		}
        	} else {
        		var coordinateX1=0;
        		var coordinateX2=0;
        		var coordinateX3=0;
        		var coordinateY1=0;
        		var coordinateY2=0;
        		var coordinateY3=0;
        		for(var i=0;typeof(dataObj) != 'undefined' && i<dataObj.length;i++){ 
        			/*if(dataObj[i].viewType != 'textareaEdit' && dataObj[i].viewType != 'printEdit'){
        				portalset.setDivPortlets(dataObj[i].id,dataObj[i].functionId,dataObj[i].viewType);
        			}*/
        			if(dataObj[i].viewType != 'shortcut'){
        				//document.getElementById("checkbox_"+dataObj[i].id).checked = true;
        				$("#checkbox_"+dataObj[i].id).attr("checked",true);
        				if((i+1)%3 == 1){
        					if(coordinateX1 == 3){
        						coordinateY1++;
        						coordinateX1=0;
        					}
                			portalset.layoutPortals(dataObj[i].id,dataObj[i].letTitle,coordinateY1,coordinateX1,1,dataObj[i].viewType,dataObj[i].functionId);
                			coordinateX1++;
        				}else if((i+1)%3 == 2){
        					if(coordinateX2 == 3){
        						coordinateY2++;
        						coordinateX2=0;
        					}
                			portalset.layoutPortals(dataObj[i].id,dataObj[i].letTitle,coordinateY2,coordinateX2,2,dataObj[i].viewType,dataObj[i].functionId);
                			coordinateX2++;
        				}else if((i+1)%3 == 0){
        					if(coordinateX3 == 3){
        						coordinateY3++;
        						coordinateX3=0;
        					}
                			portalset.layoutPortals(dataObj[i].id,dataObj[i].letTitle,coordinateY3,coordinateX3,3,dataObj[i].viewType,dataObj[i].functionId);
                			coordinateX3++;
        				}
        				if(dataObj[i].viewType == 'textareaEdit'){
        					$("#count_"+dataObj[i].id).html(200-$("#letTextarea_"+dataObj[i].id).text().length);
        				}
        			}
                }
        	}
        	portalset.hidePortlets();
        	portalset.clearPortlets(1);
        	portalset.clearPortlets(2);
        	portalset.clearPortlets(3);
        	setTimeout(function(){
        		$('#relayoutCode').hide();
            	$('#relayoutCode').show();
            	$("#check-meeting").modal('setPaddingTop');
        	},10);
        	$('#check-meeting').modal('show');
        	//$("#portal_l").append("<div></div>");
        },error:function(e){
        	// alert("加载数据错误。");
        	msgBox.tip({content: "加载数据错误", type:'fail'});
        }
    });
};
//动态添加门户组件
portalset.layoutPortals = function(portletid,letTitle,x,y,layoutPackId,viewType,functionId) {
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
		        afterLoadContent: function() {
		        	var _this = this;
                    var itemId = $(_this).attr('id');
                    /*var position = $('#portalsLayout'+layoutPackId).portlet('index')[itemId];
                    if(typeof(position) != "undefined")*/
                    map[itemId] = layoutPackId;
		        },
		        title: function() {
                    var d = new Date();
                    return '(点此处进行拖拽)-'+letTitle;
                },
		        content: {
		            type: 'text',
		            text: function() {
		            	$(".loadfunctionview").each(function() {
		            		$(this).empty();
		            	});
		            	
		            	if(viewType == 'textareaEdit' && $("#letTextarea_"+portletid).val() != undefined){
		            		var co=$("#letTextarea_"+portletid).val().trim();
		            		if(textAreaValue[portletid] != null && textAreaValue[portletid] != 'undefined')
		            			co = textAreaValue[portletid];
		            		else
		            			textAreaValue[portletid]=co;
		            		co=portalset.br2Enter(co);
		            		$("#letTextarea_"+portletid).text(co);
		            		//$("#count_"+portletid).html(200-$("#letTextarea_"+portletid).text().length);
		            		var htmlvalue ="<div class='rePortletArea' relid='"+$.trim(portletid)+"' id='portlethtml_"+$.trim(portletid)+"'>" +$('#div_'+portletid).html()+"</div>";
		            		picsAndAreas[portletid] = htmlvalue;
		            		$('#div_'+portletid).html("");
	                        return htmlvalue;
		            	}else if(viewType == 'printEdit'){
		            		var htmlvalue ="<div class='rePortletArea' relid='"+$.trim(portletid)+"' id='portlethtml_"+$.trim(portletid)+"'>" +$('#div_'+portletid).html()+"</div>";
		            		picsAndAreas[portletid] = htmlvalue;
		            		$('#div_'+portletid).html("");
	                        return htmlvalue;
		            	}else{		            		
		            		//alert($('#div_'+portletid).html());
		            		if(viewType == 'friendlyLink'){
		            			var htmlvalue = $('#div_'+portletid).html();
		            			$('#fun_'+portletid).html("");
		            			return htmlvalue;
		            		} else
		            			return $('#div_'+portletid).html();
		            	}
                    },
                    afterShow: function() {
		            	$('#'+portletid+" .ui-portlet-toggle").bind('click',function() {
		            		if($('#'+portletid+" .ui-icon-plusthick").html() != undefined
		            				&& viewType != 'printEdit' && viewType != 'textareaEdit'){
		            			portalset.setDivPortlets(portletid,functionId,viewType);
		            		}
		            	});
                    }
		        }
		    }
    });
};

//隐藏所有内容方便布局
portalset.hidePortlets = function(){
	$('#portalsLayout1').portlet('toggleAll');
	$('#portalsLayout2').portlet('toggleAll');
	$('#portalsLayout3').portlet('toggleAll');
	//$('#portlethide').
};

//清除临时门户组件
portalset.clearPortlets = function(layoutPackId){
	$('#portalsLayout'+layoutPackId).portlet('option', 'remove', '#news1');
	$('#portalsLayout'+layoutPackId).portlet('option', 'remove', '#news2');
	$('#portalsLayout'+layoutPackId).portlet('option', 'remove', '#news3');
	$('#portalsLayout'+layoutPackId).portlet('option', 'remove', '#-99');
};

//控制门户组件
portalset.setportlet = function(e,letTitle,functionId,viewType) {
	var obj = $('#'+e.value);
	if(!e.checked){
		//$('#checkbox_'+e.value).removeAttr("checked");
		if(viewType == 'textareaEdit' || viewType == 'printEdit')
			$('#div_'+$.trim(e.value)).html($('#portlethtml_'+$.trim(e.value)).html());
		
		$('#portalsLayout1').portlet('option', 'remove', obj);
		$('#portalsLayout2').portlet('option', 'remove', obj);
		$('#portalsLayout3').portlet('option', 'remove', obj);
	} else {
		 var isadd = 0;
		 var conditions=document.getElementsByName("checkboxportal");
		 for(var i=0;i<conditions.length;i++){
			    if(conditions[i].checked)
			    	isadd++;
		}
		
		if(isadd == 1){
			portalset.initiallayout($('#layoutType').val());
		}
			
		if(viewType == 'textareaEdit' || viewType == 'printEdit'){
			portalset.layoutPortals(e.value,letTitle,0,0,1,viewType,functionId);
			if(isadd != 1){
			portalset.layoutPortals(e.value,letTitle,0,0,2,viewType,functionId);
			portalset.layoutPortals(e.value,letTitle,0,0,3,viewType,functionId);
			}
		} else {
			portalset.setDivPortlets(e.value,functionId,viewType);
			portalset.layoutPortals(e.value,letTitle,0,0,1,viewType,functionId);
			if(isadd != 1){
			portalset.layoutPortals(e.value,letTitle,0,0,2,viewType,functionId);
			portalset.layoutPortals(e.value,letTitle,0,0,3,viewType,functionId);
			}
		}
		$('#portalsLayout1').portlet('toggle', e.value);
		if(isadd != 1){
			$('#portalsLayout2').portlet('toggle', e.value);
			$('#portalsLayout3').portlet('toggle', e.value);
		}
		if(isadd == 1){
		portalset.clearPortlets(1);
        portalset.clearPortlets(2);
        portalset.clearPortlets(3);
        //portalset.hidePortlets();
		}
	}
};

//返回文本图片控件
portalset.reTextArea = function(){
	$(".rePortletArea").each(function(){
		$('#div_'+$(this).attr("relid")).html($(this).html());
	});
	if($('#perview').text() == '关闭编辑'){
		$('#perview').text("开启编辑");
	}
	$('#check-meeting').modal('hide');
};

//控制门户快捷方式组件
portalset.setshortcut = function(e,letTitle,functionId,viewType) {
	
	if(e.checked){
		if($("#controlshortcut >li").length > 3){
			var code = $("#controlshortcut >li:eq(3)").attr("title");
			$("#checkbox_"+code).attr("checked",false);
			$("#controlshortcut >li:eq(3)").remove();
		}
		$("#controlshortcut").prepend("<li id='liout_"+e.value+"' class='pos-rlt' title='"+e.value+"'><div class='index-disabled'></div></li>"); 
		portalset.setDivPortlets(e.value,functionId,viewType);
	} else {
		$("#liout_"+e.value).remove();
	}
	
};

//门户共享范围保存
portalset.setSubmit = function(){
	
};

//设置门户布局
portalset.setlayout = function(layouttype){
	portalset.initiallayout(layouttype);
	portalset.relayout();
};

//根据类型初始化门户布局
portalset.initiallayout = function(layouttype){
	$('#layoutType').val(layouttype);
	$('#portaletLayoutForm .active').each(function(){
		$(this).removeClass();
	});
	$('#layoutCode'+layouttype).addClass("active");
	$('#loadLayout').html($('#loadLayout'+layouttype).html());
	if(layouttype == 1){
		portalset.clearportlet(1);
		portalset.clearportlet(2);
		portalset.clearportlet(3);
		portalnum1=3;
		portalnum2=3;
		portalnum3=3;
	} else
	if(layouttype == 2){
		portalset.clearportlet1(1);
		portalset.clearportlet(2);
		portalset.clearportlet(3);
		portalnum1=2;
		portalnum2=3;
		portalnum3=3;
	} else
	if(layouttype == 3){
		portalset.clearportlet(3);
		portalset.clearportlet1(1);
		portalset.clearportlet2(2); 
		portalnum1=3;
		portalnum2=2;
		portalnum3=2;
	} else
	if(layouttype == 4){
		portalset.clearportlet1(3);
		portalset.clearportlet1(1);
		portalset.clearportlet1(2); 
		portalnum1=2;
		portalnum2=2;
		portalnum3=2;
	} else
	if(layouttype == 5){
		portalset.clearportlet(3);
		portalset.clearportlet(1);
		portalset.clearportlet(2); 
		portalnum1=3;
		portalnum2=3;
		portalnum3=3;
	} else
	if(layouttype == 6){
		portalset.clearportlet2(3);
		portalset.clearportlet2(1);
		portalset.clearportlet2(2); 
		portalnum1=2;
		portalnum2=2;
		portalnum3=2;
	} else
	if(layouttype == 7){
		portalset.clearportlet3(3);
		portalset.clearportlet(1);
		portalset.clearportlet3(2); 
		portalnum1=3;
		portalnum2=1;
		portalnum3=1;
	} else
	if(layouttype == 8){
		portalset.clearportlet2(3);
		portalset.clearportlet2(1);
		portalset.clearportlet2(2); 
		portalnum1=2;
		portalnum2=2;
		portalnum3=2;
	} else
	if(layouttype == 9){
		portalset.clearportlet3(3);
		portalset.clearportlet3(1);
		portalset.clearportlet3(2); 
		portalnum1=1;
		portalnum2=1;
		portalnum3=1;
	}
};

//加载数据替换门户模块信息
portalset.setDivPortlets = function(divid,functionId,viewType){
	if($("#fun_"+divid).length>0){
		jQuery.ajax({
	        url: getRootPath()+"/sys/portalFunction/getFunctionForPortlet.action?layoutStatus=edit&tabsize=1.5&functionId="+functionId+"&viewType="+viewType+"&portletId="+divid+"&time="+(+new Date().getTime()),
	        type: 'post',
	        async:false,
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

//门户布局编辑
portalset.viewlayout = function(){
	if($('#perview').text() == '关闭编辑'){
		portalset.hidePortlets();
		$('#perview').text("开启编辑");
	}else {	
		portalset.initiallayout($('#layoutType').val());
		jQuery.ajax({
	        url: getRootPath()+"/sys/portlet/getLayoutPortals.action?portalId="+$("#portalId").val()+"&portalType="+$('#portalType').val()+"&time="+(+new Date().getTime()),
	        type: 'post',
	        success: function(data, textStatus, xhr) {
	        	var dataObj=data.portletList; 
	        	var relistsize = data.reListSize;
	        	var relist = data.reList; 
	        	if(relistsize > 0){
	        		for(var i=0;i<relist.length;i++){
	        			if(isIe8Browser)
	        				document.getElementById("checkbox_"+relist[i].id).checked = true;
	        			else
	        				$("#checkbox_"+relist[i].id).attr("checked",true);
	        			if(relist[i].viewType == 'textareaEdit' || relist[i].viewType == 'printEdit'){
	        				//$('#div_'+relist[i].id).html($('#portlethtml_'+relist[i].id).html());
	        				$('#div_'+relist[i].id).html(picsAndAreas[relist[i].id]);
	        			}
	        				
	        			if(relist[i].viewType != 'textareaEdit' && relist[i].viewType != 'printEdit' && relist[i].viewType != 'shortcut'){
	        				portalset.setDivPortlets(relist[i].id,relist[i].functionId,relist[i].viewType);
	        			}
	        			if(relist[i].viewType != 'shortcut'){
	        				portalset.layoutPortals(relist[i].id,relist[i].letTitle,relist[i].siteY,relist[i].siteX,relist[i].layoutPackId,relist[i].viewType,relist[i].functionId);
	        				if(relist[i].viewType == 'textareaEdit'){
	        					$("#count_"+relist[i].id).html(200-$("#letTextarea_"+relist[i].id).text().length);
	        				}
	        			}
	        			if(relist[i].viewType == 'friendlyLink'){
	        				portalset.reloadevaluate(relist[i].id+"_");
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
	        			if(dataObj[i].viewType == 'textareaEdit' || dataObj[i].viewType == 'printEdit'){
	        				//$('#div_'+dataObj[i].id).html($('#portlethtml_'+dataObj[i].id).html());
	        				$('#div_'+dataObj[i].id).html(picsAndAreas[dataObj[i].id]);
	        			}
	        			if(dataObj[i].viewType != 'textareaEdit' && dataObj[i].viewType != 'printEdit' && dataObj[i].viewType != 'shortcut'){
	        				portalset.setDivPortlets(dataObj[i].id,dataObj[i].functionId,dataObj[i].viewType);
	        			}
	        			if(dataObj[i].viewType != 'shortcut'){
	        				document.getElementById("checkbox_"+dataObj[i].id).checked = true;
		        			if((i+1)%3 == 1){
		        				if(coordinateX1 == 3){
		    						coordinateY1++;
		    						coordinateX1=0;
		    					}
		            			portalset.layoutPortals(dataObj[i].id,dataObj[i].letTitle,coordinateY1,coordinateX1,1,dataObj[i].viewType,dataObj[i].functionId);
		            			coordinateX1++;
		            		}else if((i+1)%3 == 2){
		            			if(coordinateX2 == 3){
		    						coordinateY2++;
		    						coordinateX2=0;
		    					}
		            			portalset.layoutPortals(dataObj[i].id,dataObj[i].letTitle,coordinateY2,coordinateX2,2,dataObj[i].viewType,dataObj[i].functionId);
		            			coordinateX2++;
		            		}else if((i+1)%3 == 0){
		            			if(coordinateX3 == 3){
		    						coordinateY3++;
		    						coordinateX3=0;
		    					}
		            			portalset.layoutPortals(dataObj[i].id,dataObj[i].letTitle,coordinateY3,coordinateX3,3,dataObj[i].viewType,dataObj[i].functionId);
		            			coordinateX3++;
		            		}
		        			if(dataObj[i].viewType == 'textareaEdit'){
	        					$("#count_"+dataObj[i].id).html(200-$("#letTextarea_"+dataObj[i].id).text().length);
	        				}
	        			}
	                }
	        	}
	        	portalset.clearPortlets(1);
	        	portalset.clearPortlets(2);
	        	portalset.clearPortlets(3);
	        },error:function(e){
	        	// alert("加载数据错误。");
	        	msgBox.tip({content: "加载数据错误", type:'fail'});
	        }
	    });
		$('#perview').text("关闭编辑");
	}
};
//门户布局重置
portalset.relayout = function(){
	$("input[name='checkboxportal']").attr("checked",false);
	//----
	jQuery.ajax({
        url: getRootPath()+"/sys/portlet/getLayoutPortals.action?portalId="+$("#portalId").val()+"&portalType="+$('#portalType').val()+"&time="+(+new Date().getTime()),
        type: 'post',
        success: function(data, textStatus, xhr) {
        	var dataObj=data.portletList; 
        	var relistsize = data.reListSize;
        	var relist = data.reList; 
        	if(relistsize > 0){
        		for(var i=0;i<relist.length;i++){
        			//$("#checkbox_"+relist[i].id).attr("checked",true);
        			if(isIe8Browser)
        				document.getElementById("checkbox_"+relist[i].id).checked = true;
        			else
        				$("#checkbox_"+relist[i].id).attr("checked",true);
        			document.getElementById("checkbox_"+relist[i].id).checked = true;
        			if(relist[i].viewType == 'textareaEdit' || relist[i].viewType == 'printEdit'){
        				//$('#div_'+relist[i].id).html($('#portlethtml_'+relist[i].id).html());
        				$('#div_'+relist[i].id).html(picsAndAreas[relist[i].id]);
        			}
        				
        			/*if(relist[i].viewType != 'textareaEdit' && relist[i].viewType != 'printEdit' && relist[i].viewType != 'shortcut'){
        				portalset.setDivPortlets(relist[i].id,relist[i].functionId,relist[i].viewType);
        			}*/
        			if(relist[i].viewType != 'shortcut'){
        				portalset.layoutPortals(relist[i].id,relist[i].letTitle,relist[i].siteY,relist[i].siteX,relist[i].layoutPackId,relist[i].viewType,relist[i].functionId);
        				if(relist[i].viewType == 'textareaEdit'){
        					$("#count_"+relist[i].id).html(200-$("#letTextarea_"+relist[i].id).text().length);
        				}
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
        			if(dataObj[i].viewType == 'textareaEdit' || dataObj[i].viewType == 'printEdit'){
        				//$('#div_'+dataObj[i].id).html($('#portlethtml_'+dataObj[i].id).html());
        				$('#div_'+dataObj[i].id).html(picsAndAreas[dataObj[i].id]);
        			}
        			/*if(dataObj[i].viewType != 'textareaEdit' && dataObj[i].viewType != 'printEdit' && dataObj[i].viewType != 'shortcut'){
        				portalset.setDivPortlets(dataObj[i].id,dataObj[i].functionId,dataObj[i].viewType);
        			}*/
        			if(dataObj[i].viewType != 'shortcut'){
        				document.getElementById("checkbox_"+dataObj[i].id).checked = true;
        				//$("#checkbox_"+dataObj[i].id).attr("checked",true);
	        			if((i+1)%3 == 1){
	        				if(coordinateX1 == 3){
	    						coordinateY1++;
	    						coordinateX1=0;
	    					}
	            			portalset.layoutPortals(dataObj[i].id,dataObj[i].letTitle,coordinateY1,coordinateX1,1,dataObj[i].viewType,dataObj[i].functionId);
	            			coordinateX1++;
	            		}else if((i+1)%3 == 2){
	            			if(coordinateX2 == 3){
	    						coordinateY2++;
	    						coordinateX2=0;
	    					}
	            			portalset.layoutPortals(dataObj[i].id,dataObj[i].letTitle,coordinateY2,coordinateX2,2,dataObj[i].viewType,dataObj[i].functionId);
	            			coordinateX2++;
	            		}else if((i+1)%3 == 0){
	            			if(coordinateX3 == 3){
	    						coordinateY3++;
	    						coordinateX3=0;
	    					}
	            			portalset.layoutPortals(dataObj[i].id,dataObj[i].letTitle,coordinateY3,coordinateX3,3,dataObj[i].viewType,dataObj[i].functionId);
	            			coordinateX3++;
	            		}
	        			if(dataObj[i].viewType == 'textareaEdit'){
        					$("#count_"+dataObj[i].id).html(200-$("#letTextarea_"+dataObj[i].id).text().length);
        				}
        			}
                }
        	}
        	portalset.clearPortlets(1);
        	portalset.clearPortlets(2);
        	portalset.clearPortlets(3);
        	portalset.hidePortlets();
        },error:function(e){
        	// alert("加载数据错误。");
        	msgBox.tip({content: "加载数据错误", type:'fail'});
        }
    });
};

//清空门户布局数据
portalset.clearportlet = function(layouttype){//1:1:1
	$('#portalsLayout'+layouttype).portlet({
        sortable: true,
        singleView: false,
        events: {
        	drag: {
                 stop: function(event, ui) {
                     var _this = this;
                     var itemId = $(_this).attr('id');
                     //var position = $('#portalsLayout'+layouttype).portlet('index')[itemId];
                     //if(typeof(position) != "undefined")
                     //map[itemId] = position.x + "$" + position.y;
                    	 map[itemId] = layouttype;
                 }
             }
         },  
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
portalset.clearportlet1 = function(layouttype){//1:1 初始化
	$('#portalsLayout'+layouttype).portlet({
        sortable: true,
        singleView: false,
        events: {
        	drag: {
        		stop: function(event, ui) {
                    var _this = this;
                    var itemId = $(_this).attr('id');
                    /*var position = $('#portalsLayout'+layouttype).portlet('index')[itemId];
                    if(typeof(position) != "undefined")
                    map[itemId] = position.x + "$" + position.y;*/
                    map[itemId] = layouttype;
                }
             }
         },
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
portalset.clearportlet2 = function(layouttype){//2:1 初始化
	$('#portalsLayout'+layouttype).portlet({
        sortable: true,
        singleView: false,
        events: {
        	drag: {
        		stop: function(event, ui) {
                    var _this = this;
                    var itemId = $(_this).attr('id');
                    /*var position = $('#portalsLayout'+layouttype).portlet('index')[itemId];
                    if(typeof(position) != "undefined")
                    map[itemId] = position.x + "$" + position.y;*/
                    map[itemId] = layouttype;
                }
             }
         },
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
portalset.clearportlet3 = function(layouttype){//1 初始化
	$('#portalsLayout'+layouttype).portlet({
        sortable: true,
        singleView: false,
        events: {
        	drag: {
        		stop: function(event, ui) {
                    var _this = this;
                    var itemId = $(_this).attr('id');
                    /*var position = $('#portalsLayout'+layouttype).portlet('index')[itemId];
                    if(typeof(position) != "undefined")
                    map[itemId] = position.x + "$" + position.y;*/
                    map[itemId] = layouttype;
                }
             }
         },
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
//获得已上传的附件
portalset.getPortletPhoto = function(portalvoid){
	//alert(portalvoid);
	/*console.log($("#letFile_174").val());*/
	
	if(!portalset.isGetPhoto[portalvoid]){
		portalset.isGetPhoto[portalvoid] = true;
		//上传图片实例
		attachPool.add({
			containerId: '#uploader'+portalvoid,
			uploadInitId: '#filePicker'+portalvoid,
			uploadInitLabel: '上传图片',
			//限制只能上传一个文件
			fileNumLimit: 1,
			//限制只可以上传图片
			accept: {
				title: '指定格式',
				extensions: 'gif,jpg,jpeg,bmp,png',
				mimeTypes: 'image/*'
			},
			server:getRootPath()+'/sys/portlet/portletPhotoUpload.action',
			dndArea: '#dndArea'+portalvoid,
			form: {
				businessId: $('#id_'+portalvoid).val(),
				businessTable: 'tty_sys_portlet_relation',
				category: ('portal'+portalvoid+'Img')
			},
			isEcho: true,
			error : function(id,name){
				$.ajax({
	    			url : getRootPath()+'/sys/portlet/deletePortletPhoto.action',
	    			type : 'POST',
	    			data : {id: $("#tempportletid"+portalvoid).val(), fileFolderName: $("#tempportletfile"+portalvoid).val()},
	    			success : function(result) {
	    				if(result.state){
	    					/*data.context.fadeOut(function () {
	                            $(this).remove();
	                        });*/
	    					/*$("#letFilePhoto_"+$("#tempportletportalvoid").val()).attr("src", getRootPath()+"/images/demoimg/iphoto.jpg");
	    					$("#letFile_"+$("#tempportletportalvoid").val()).val("");*/
	    					msgBox.tip({
	    						type:"success",
	    						content: "删除成功"
	    					});
	    				} else {
	    					msgBox.tip({
	    						content: "删除失败"
	    					});
	    				}
	    			}
	        	  });
			}
		});
	}

    $("#tempportletid"+portalvoid).val($("#id_"+portalvoid).val());
    //$("#tempportletfile"+portalvoid).val(portalvoidValue);
    //$("#tempportletportalvoid").val(portalvoid);
    $('#myModal'+portalvoid).modal('show');
};

//折行
portalset.enter2Br = function (str){
	var newString = str.replace(/\n/g, '_@').replace(/\r/g, '_#');
	if(isIe8Browser){
		newString = newString.replace(/_#_@/g, '<br>');//IE7-8
	}else {
		newString = newString.replace(/_@/g, '<br>');//IE9、FF、chrome
	}
    newString = newString.replace(/\s/g, '_p;');//空格处理
    
    //str.replace(/\n/g,'<br>').replace(/\s/g,"&nbsp;");
	return newString;
};
//回车
portalset.br2Enter = function (str){
	//str.replace(/<br>/ig,"\r\n").replace(/(&nbsp;)/g,"")
    return str.replace(/<br>/ig,"\r\n").replace(/(_p;)/g," ");
};

//保存门户文本域组件或单图片路径组件内容方法
portalset.saveTextarea = function(portalvoid,portalId) {
	var updateUrl = getRootPath()+"/sys/portlet/update.action?time="+(+new Date().getTime());
	var co=$("#letTextarea_"+portalvoid).val().trim();
	co=portalset.enter2Br(co);
	//co=portalset.br2Enter(co);
	if($("#id_"+portalvoid).val().length > 0){
		jQuery.ajax({
			  url: updateUrl,
			  type: 'post',
			  data: {id:$("#id_"+portalvoid).val(),letTextarea:co,letFile:$("#letFile_"+portalvoid).val(),portalId:portalId},
			  success: function(data, textStatus, xhr) {
				  if(data.errorMessage!=null){
					  msgBox.tip({content: data.errorMessage, type:'fail'});
				  }else{
					  $("#token").val(data.token);
					  textAreaValue[portalvoid] = co;
					  msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
				  }
			  },error:function(){
				  msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
			  }
		});
	}
};
//保存门户与门户组件关系 门户布局地址
portalset.getLayout = function() {
	// 显示
    var layouts = "";
	/*$("#portalsLayout1 >div >div").each(function(){
       // alert(map[$(this).attr("id")]);  //打印子div的ID
        var xy = map[$(this).attr("id")];
        if(typeof(xy) != "undefined"){
        	if(layouts == "")
        		layouts = $(this).attr("id") + '$1#' + xy;
        	else
        		layouts = layouts + "&"+ $(this).attr("id") + '$1#' + xy ;
        }
	});
	$("#portalsLayout2 >div >div").each(function(){
		var xy = map[$(this).attr("id")];
        if(typeof(xy) != "undefined"){
        	if(layouts == "")
        		layouts = $(this).attr("id") + '$2#' + xy;
        	else
        		layouts = layouts + "&"+ $(this).attr("id") + '$2#' + xy ;
        }
	});
	$("#portalsLayout3 >div >div").each(function(){
		var xy = map[$(this).attr("id")];
        if(typeof(xy) != "undefined"){
        	if(layouts == "")
        		layouts = $(this).attr("id") + '$3#' + xy;
        	else
        		layouts = layouts + "&"+ $(this).attr("id") + '$3#' + xy ;
        }
	});*/
    
    /*portalset.clearPortlets(1);
	portalset.clearPortlets(2);
	portalset.clearPortlets(3);*/
    
    var indexs = $('#portalsLayout1').portlet('index');
    $.each(indexs, function(k, v) {
    	
    	$("#portalsLayout1 >div >div").each(function(){
    		if($(this).attr("id") == k){
    			var vx = v.x%portalnum1;
    			if(layouts == "")
            		layouts = k + '$'+1+'#' + vx + "$" + v.y;
            	else
            		layouts = layouts + "&"+ k + '$'+1+'#' + vx + "$" + v.y ;
    			return true;
    		}
    	});
    	
    	$("#portalsLayout2 >div >div").each(function(){
    		if($(this).attr("id") == k){
    			var vx = (v.x+portalnum2-portalnum1)%portalnum2;
    			if(layouts == "")
            		layouts = k + '$'+2+'#' + vx + "$" + v.y;
            	else
            		layouts = layouts + "&"+ k + '$'+2+'#' + vx + "$" + v.y ;
    			return true;
    		}
    	});
    	
    	$("#portalsLayout3 >div >div").each(function(){
    		if($(this).attr("id") == k){
    			var vx = (v.x+portalnum3-portalnum1)%portalnum3;
    			if(layouts == "")
            		layouts = k + '$'+3+'#' + vx + "$" + v.y;
            	else
            		layouts = layouts + "&"+ k + '$'+3+'#' + vx + "$" + v.y ;
    			return true;
    		}
    	});
    	
    });
   
	var shortcutlay = "";
	$("#controlshortcut >li").each(function(){
		var code = $(this).attr("title");
		if(shortcutlay == "")
			shortcutlay = code;
		else
			shortcutlay =  code + "&" + shortcutlay;
	});
    
    $("#layoutSite").val(layouts+","+shortcutlay);
    
    if($("input[name='checkboxportal']").val() == undefined){
    	msgBox.tip({content: "您无可选择的门户模块，请添加", type:'fail'});
    	return;
    } else if(layouts == ""){
    	msgBox.tip({content: "请选择至少一个门户模块", type:'fail'});
    	return;
    }  
    
    if($("#portaletLayoutForm").valid()){
		  var formData = $("#portaletLayoutForm").serializeArray();
		  var addUrl = getRootPath()+"/sys/portaletLayout/save.action?time="+(+new Date().getTime());
		  var updateUrl = getRootPath()+"/sys/portaletLayout/update.action?time="+(+new Date().getTime());
		  var url;
		  
		  if($("#id").val().length > 0){
			  url = updateUrl;
		  }else{
			  url = addUrl;
		  }
		  jQuery.ajax({
            url: url,
            type: 'post',
            data: formData,
            success: function(data, textStatus, xhr) {
              $("#token").val(data.token);
          	  if(data.errorMessage!=null){
          		  msgBox.tip({content: data.errorMessage, type:'fail'});
          	  }else{
          		  msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
          		  portalset.reTextArea();
          		  //$('#check-meeting').modal('hide');
          		  portalset.portalsetList();
          	  }
            },error:function(){
            	  msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
            }
          });
	  }
};

//改变输入字数显示
portalset.checkLen = function(obj,portalvoid)
{
	var str = 0;
	if(isIe8Browser)
		str = $("#letTextarea_"+portalvoid).text();
	else
		str = $("#letTextarea_"+portalvoid).val();
	var namelength = str.length;
	var maxChars = 200-namelength;//最多字符数
	if(isIe8Browser){
		if(obj.value.length > 200 && str.length > 200){
			obj.value = obj.value.substring(0,obj.value.length-(str.length-200));
			maxChars = 0;
		} 
	}else {
		if(obj.value.length > 200){
			obj.value = obj.value.substring(0,200);
			maxChars = 0;
		}
	}
	
	var curr = maxChars;
	$("#count_"+portalvoid).html(curr.toString());
};

//友情链接动态添加排序js------------------------------------start----
//添加指标
portalset.addevaluate = function(linkid){
    var n = $(".evaluate_name"+linkid).size();
    var html = '';
    html = "<div id='div_"+n+"'><table class='table port-btn'><tr><td style='padding:0px 0px;border-top:2px;'>名称:</td><td style='padding:0px 0px;border-top:2px;'><input class='eName evaluate_name"+linkid+"' size='10' maxlength='20' name='index_name_"+linkid+""+n+"' type='text'/></td>";
    html += "<td style='padding:0px 0px;border-top:2px;'>地址:</td><td style='padding:0px 0px;border-top:2px;'><input class='eLimit evaluate_limit"+linkid+"' name='index_limit_time_"+linkid+""+n+"' type='text' size='10' maxlength='100'/></td>";
    /*html += "<td style='padding:0px 0px;border-top:2px;'>排序:</td><td style='padding:0px 0px;border-top:2px;'><input class='eAlert evaluate_alert"+linkid+"' name='index_alert_time_"+linkid+""+n+"' type='text' value='"+n+"' size='6' maxlength='3'/></td>";*/
    html += "<td style='padding:0px 0px;border-top:2px;'><input class='eAlert evaluate_alert"+linkid+"' name='index_alert_time_"+linkid+""+n+"' type='hidden' value='"+n+"' size='6' maxlength='3'/><button type='button' class='btn' name='evaluatemove"+linkid+"' onclick='portalset.evaluatemove("+n+",\""+linkid+"\")'>移除</button></td></tr></table></div>";
    $("div[name=processdiv"+linkid+"]").append(html);
    //使div可以拖拽
    $("div[name=processdiv"+linkid+"]").sortable({
        update: function (event, ui) {
        	portalset.evaluaterefresh(linkid);
        }
    });
    //指标重新排序
    portalset.evaluaterefresh(linkid);
};

//刷新重载数据排序及绑定拖拽
portalset.reloadevaluate = function(linkid){
	$("div[name=processdiv"+linkid+"]").sortable({
        update: function (event, ui) {
        	portalset.evaluaterefresh(linkid);
        }
    });
};

//移除指标
portalset.evaluatemove = function(n,linkid){//alert(linkid);
	$("#div_"+n+"").remove();
	portalset.evaluaterefresh(linkid);
};

//移除指标后重新编号
portalset.evaluaterefresh = function(linkid){//alert(linkid+"----"+$('.evaluate_name'+linkid).length);
	var count=$('.evaluate_name'+linkid).length;
	for(var i=0;i<count;i++){
	    var j = i+1;
		$('div[name="processdiv'+linkid+'"] div:eq('+i+') input:eq(0)').attr("name","index_name_"+linkid+j);
		$('div[name="processdiv'+linkid+'"] div:eq('+i+') input:eq(1)').attr("name","index_limit_time_"+linkid+j);
		$('div[name="processdiv'+linkid+'"] div:eq('+i+') input:eq(2)').attr("name","index_alert_time_"+linkid+j);
		$('div[name="processdiv'+linkid+'"] div:eq('+i+') input:eq(2)').attr("value",j);
	}
};

//保存任务类型和指标数据到数据库
portalset.evaluatesave = function(linkid){
	portalset.evaluaterefresh(linkid);
    var count = $(".evaluate_name"+linkid).size();
    var urlval = /^(https?|s?ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i;
	var nameval =/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
	var evaluate_name = new Array();
	var evaluate_limit = new Array();
	var evaluate_alert = new Array();
	for(var i=1;i <=count;i++){
	   evaluate_name[i] =  $("input[name='index_name_"+linkid+i+"']").val();
	   if(evaluate_name[i].trim()==""||evaluate_name[i]==null){
	   		msgBox.tip({content: "名称不能为空", type:'fail'});
	   		return ;
	   } else if(nameval.test(evaluate_name[i])){
		   msgBox.tip({content: "名称中存在特殊字符", type:'fail'});
		   return ;
	   }

	   evaluate_limit[i] =  $("input[name='index_limit_time_"+linkid+i+"']").val();
	   if(evaluate_limit[i].trim()==""||evaluate_limit[i]==null){
	   		msgBox.tip({content: "地址不能为空", type:'fail'});
	   		return ;
	   }else if(!urlval.test(evaluate_limit[i])){
		   	msgBox.tip({content: "链接地址不正确", type:'fail'});
	   		return ;
	   }
	   evaluate_alert[i] =  $("input[name='index_alert_time_"+linkid+i+"']").val();
	   if(evaluate_alert[i].trim()==""||evaluate_alert[i]==null){
	   		msgBox.tip({content: "排序不能为空", type:'fail'});
	   		return ;
	   }else if(isNaN(evaluate_alert[i].trim())){
		   	msgBox.tip({content: "排序必须全是数字", type:'fail'});
		   	return ;
	   }
	}
	
	var linkName = evaluate_name.join('@@@');
	var linkUrl = evaluate_limit.join('@@@');
	var sequence = evaluate_alert.join('@@@');
	//alert($("#portalId").val());
	$.ajax({
			url:getRootPath()+"/sys/portalFriendlylink/save.action?time="+(+new Date().getTime()),
			type:"POST",
			data:{
				 linkName:linkName,
				 linkUrl:linkUrl,
				 sequence:sequence,
				 portletid:linkid,
				 portalid:$("#portalId").val()
			},
			dataType: 'json',
			success:function(data, textStatus, xhr){
				msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
			},error:function(){
            	msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
            }
	});
};
//友情链接动态添加排序js------------------------------------end----

//初始化
{
	//计算分页显示条数
	portalset.pageCount = 10;
	$("#portalbtn").click(portalset.getLayout);
	$('.btn-layout').click(function(){
    	$('.layout-content').toggle();
    });
	//初始化列表方法 
	portalset.portalsetList(); 
};	


