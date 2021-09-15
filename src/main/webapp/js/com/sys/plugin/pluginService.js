var pluginService = {};
pluginService.subState = false;


pluginService.savePluginService = function () {
	var services = new Array();
	$("select[name='backValue']").find("option").each(function(i, val){
		services.push({"pluginId":$('#pluginId').val(),"serviceId":this.value}); //获取选中节点的值
	});
	var url = getRootPath()+"/sys/plugin/savePluginService.action?time=" + (+new Date().getTime());
	//添加其他表单信息
	jQuery.ajax({
		url : url,
		type : 'POST',
		data : JSON.stringify({"pluginId":$('#pluginId').val(),"pluginService":services}),
		contentType: "application/json;charset=UTF-8",
		success : function(data) {
			//getToken();
			if(data.success == "true"){
				msgBox.tip({
					type:"success",
					content:"保存成功"
				});
				$('#myModal-pluginService').modal('hide');
			} else {
				if(data.labelErrorMessage){
					showErrors("pluginForm", data.labelErrorMessage);
				} else{
					msgBox.info({
						type:"fail",
						content:data.errorMessage
					});
				}
			}
		},
		error : function() {
			alertx("保存失败");
		}
	});
};


jQuery(function($){
	$("#savePluginService").click(function(){pluginService.savePluginService();});

	$("#closePluginService").click(function(){
		$('#myModal-pluginService').modal('hide');
	});

});


pluginService.init = function() {
	var openPluginServiceDiv = "openPluginServiceDiv";
	var selectBackValueId = "backValue";

	var pluginServiceHtml =
	'<div id="'+openPluginServiceDiv+'" aria-hidden="false" style="padding-top: 0px;">'+
		'<div class="w1100 modal-tree">'+
			'<div class="modal-content">'+
				'<div class="modal-body clearfix"></div>'+	//显示人员列表
			'</div>'+
		'</div>'+
	'</div>';
	$("#pluginServiceDiv").empty();
	$("#pluginServiceDiv").append(pluginServiceHtml);


	$.ajax({
		async : false,
		url : getRootPath()+"/sys/apiService/getAllSubsystemAndApi.action",
		type : 'post',
		data : {"pluginId": $('#pluginId').val()},
		success : function(data) {
			$("#"+openPluginServiceDiv).find(".modal-body").html(pluginService.showPluginServicePage(eval(data)[0], selectBackValueId, openPluginServiceDiv));
			//人员界面收缩事件
			$(".tree-list .tree-btn").on('click',function (e) {
		        e.preventDefault()
		        e.stopPropagation()
		        $(this).find("i").toggleClass("fa-chevron-down").end().closest(".tree-list").next().slideToggle();
		    });
			var services = eval(data)[1].services;
			for(var x=0; x<services.length; x++){
				var serviceId = services[x].serviceId;
				$("input[id='api"+serviceId+"']").prop("checked", true);
				var subsystemId = $("#"+$("#api"+serviceId).attr("name")).attr("name");
				pluginService.selectServiceSubsystem(document.getElementById("api"+serviceId), subsystemId, selectBackValueId, openPluginServiceDiv)
			}
		},
		error: function(){

		}
	});
};

/**
 * 选择部门级联人员操作
 * @param obj				本控件对象
 * @param selectId			select的ID
 * @param openPluginServiceDiv	弹出层DIVID
 */
pluginService.selectSubsystemService = function(obj, selectId, openPluginServiceDiv,level){
	if($("#"+openPluginServiceDiv+" #"+obj.id).prop("checked")){
		$("input[name='"+obj.id+"']").prop("checked", true);
		$("input[name='"+obj.id+"']").each(function(){
			if(level == 1){
				$("input[name='"+this.id+"']").prop("checked", true);
				$("input[name='"+this.id+"']").each(function(){
					$("#"+selectId+" option[value='"+this.value.split(",")[0]+"']").remove();
					$("#"+selectId).append("<option value='"+this.value.split(",")[0]+"'>"+this.value.split(",")[1]+"</option>");
				});
			}else{
				$("#"+selectId+" option[value='"+this.value.split(",")[0]+"']").remove();
				$("#"+selectId).append("<option value='"+this.value.split(",")[0]+"'>"+this.value.split(",")[1]+"</option>");
			}
		});
	}else{
		$("input[name='"+obj.id+"']").prop("checked", false);
		$("input[name='"+obj.id+"']").each(function(){
			if(level == 1){
				$("input[name='"+this.id+"']").prop("checked", false);
				$("input[name='"+this.id+"']").each(function(){
					$("#"+selectId+" option[value='"+this.value.split(",")[0]+"']").remove();
				});
			}else{
				$("#"+selectId+" option[value='"+this.value.split(",")[0]+"']").remove();
			}
		});
	}
	var sTmp = 0;
	if(level != 1){
		$("#"+openPluginServiceDiv+" input[name='"+obj.name+"']").each(function(){
			if(this.checked)
				sTmp++;
		});
		if(sTmp == $("#"+openPluginServiceDiv+" input[name='"+obj.name+"']").length){
			$("input[id='"+obj.name+"']").prop("checked", true);
		}else{
			$("input[id='"+obj.name+"']").removeAttr("checked");
		}
	}
}

/**
 * 选择人员级联部门操作
 * @param obj				本控件对象
 * @param serviceId			部门ID对象
 * @param selectId			select的ID
 * @param openPluginServiceDiv	弹出层DIVID
 */
pluginService.selectServiceSubsystem = function(obj, subsystemId, selectId, openPluginServiceDiv){
	var serviceId = obj.name;
	if($("#"+openPluginServiceDiv+" #"+obj.id).prop("checked")){
		$("#"+selectId).append("<option value='"+$(obj).val().split(",")[0]+"'>"+$(obj).val().split(",")[1]+"</option>");
	}else{
		$("#"+selectId+" option[value='"+$(obj).val().split(",")[0]+"']").remove();
	}
	var sTmp = 0;
	var sTmp1 = 0;
	var IE8 = isIE8();
	if(IE8){
		$("#"+openPluginServiceDiv+" input[name='"+serviceId+"']").each(function(){
			if(this.checked)
				sTmp1++;
		});
		if(sTmp1 == $("#"+openPluginServiceDiv+" input[name='"+serviceId+"']").length){
			$("input[id='"+serviceId+"']").prop("checked", true);
		}else{
			$("input[id='"+serviceId+"']").removeAttr("checked");
		}
		$("#"+openPluginServiceDiv+" input[name='"+subsystemId+"']").each(function(){
			if(this.checked)
				sTmp++;
		});
		if(sTmp == $("#"+openPluginServiceDiv+" input[name='"+subsystemId+"']").length){
			$("input[id='"+subsystemId+"']").prop("checked", true);
		}else{
			$("input[id='"+subsystemId+"']").removeAttr("checked");
		}
	}else{
		$("#"+openPluginServiceDiv+" input[name='"+serviceId+"']").each(function(){
			if(this.checked)
				sTmp1++;
		});
		if(sTmp1 == $("#"+openPluginServiceDiv+" input[name='"+serviceId+"']").length){
			$("input[id='"+serviceId+"']").prop("checked", true);
		}else{
			$("input[id='"+serviceId+"']").removeAttr("checked");
		}
		$("#"+openPluginServiceDiv+" input[name='"+subsystemId+"']").each(function(){
			if(this.checked)
				sTmp++;
		});
		if(sTmp == $("#"+openPluginServiceDiv+" input[name='"+subsystemId+"']").length){
			$("input[id='"+subsystemId+"']").prop("checked", true);
		}else{
			$("input[id='"+subsystemId+"']").removeAttr("checked");
		}
	}
}

/**
 * 选择框排序上
 * @param selectId
 */
function up(selectId){
	if($("#"+selectId).val() == null){
		msgBox.tip({content: "请选择api接口", type:'fail'});
	}else{
		var optionIndex = $("#"+selectId).get(0).selectedIndex;
		if(optionIndex > 0){
			$('#'+selectId+' option:selected').insertBefore($('#'+selectId+' option:selected').prev('option'));
		}
	}
}

/**
 * 选择框排序下
 * @param selectId
 */
function down(selectId){
	if($("#"+selectId).val() == null){
		msgBox.tip({content: "请选择api接口", type:'fail'});
	}else{
		var optionLength = $("#"+selectId)[0].options.length;
		var optionIndex = $("#"+selectId).get(0).selectedIndex;
		if(optionIndex < (optionLength-1)){
			$('#'+selectId+' option:selected').insertAfter($('#'+selectId+' option:selected').next('option'));
		}
	}
}

/**
 * 显示人员控件
 * @param d 				部门人员数据
 * @param selectId 			select的ID
 * @param openPluginServiceDiv	弹出层DIVID
 * @returns
 */
pluginService.showPluginServicePage = function(d, selectId, openPluginServiceDiv){
	var lv1_subsystem = '<div>';
	var lv1_api = '<div>';
	lv1_subsystem += '<label for="" class="checkbox fl">'+d.name+'</label> ';
	lv1_api += '<label for="" class="checkbox inline"></label>';
	lv1_api += '</div>';
	lv1_subsystem += '<a href="#" class="fr m-r tree-btn"><i class="fa fa-chevron-up"></i></a></div>';
	var showlist = $(
		'<section class="w820 fl tree-ul tree-scroll">'+
			'<ul class="tree-horizontal clearfix">'+
				'<li>'+
				'<div>'+
				'</div>'+
				'<ul id="lv"></ul>'+
				'</li>'+
			'</ul>'+
		'</section>'+
		'<section class="fl m-l pos-rlt">'+
			'<select id="'+selectId+'" name="'+selectId+'" multiple="false" class="w170 tree-scroll-right tree-select">'+
            '</select>'+
            '<div class="tree-move"> '+
            	'<a href="#" class="tree-move-up" onClick="up(\''+selectId+'\');"><i class="fa fa-caret-up"></i></a> '+
            	'<a href="#" class="tree-move-down" onClick="down(\''+selectId+'\');"><i class="fa fa-caret-down"></i></a> '+
            '</div>'+
        '</section>'
	);
	if(d.subsystem.length > 0)
		pluginService.recur(d.subsystem, showlist.find("#lv"), 1, selectId, openPluginServiceDiv);
	return showlist;
}

/**
 * 递归查询下级菜单
 * @param appList			部门集合
 * @param parentHtml		上级html
 * @param level				level-css样式
 * @param selectId			select的ID
 * @param openPluginServiceDiv	弹出层DIVID
 */
pluginService.recur = function(appList, parentHtml, level, selectId, openPluginServiceDiv, parentId){
	for(var i=0; i<appList.length; i++){
		if(appList[i].subApp) {
			var main_subsystem = '<div>';
			var main_app = '<div>';
			if(appList[i].deleteFlag != 1 && appList[i].subApp.length > 0){
				main_subsystem += '<label for="" class="checkbox fl"><input type="checkbox" id="subsystem'+appList[i].id+'" name="services" onClick="pluginService.selectSubsystemService(this,\''+selectId+'\',\''+openPluginServiceDiv+'\','+ level+')"> '+appList[i].name+'</label> ';

			} else {
				main_subsystem += '<label for="" class="checkbox fl">'+appList[i].name+'</label> ';
				main_app += '<label for="" class="checkbox inline"></label>';
			}
			main_subsystem += '<a href="#" class="fr m-r tree-btn"><i class="fa fa-chevron-up"></i></a></div>';
			main_app += '</div>';
			var main_sub = $(
				'<li>'+
					'<div class="level'+level+' clearfix tree-list">'+
						main_subsystem +
						main_app +
					'</div>'+
					'<ul id="lv'+i+'"></ul>'+
				'</li>'
			);
			$(main_sub).appendTo(parentHtml);
			pluginService.recur(appList[i].subApp, $(main_sub).children().last(), level+1, selectId, openPluginServiceDiv,appList[i].id);
		} else {
			var sub_app = '<div>';
			var sub_api = '<div>';
			if(appList[i].api != undefined){
				sub_app += '<label for="" class="checkbox fl"><input type="checkbox" id="service'+parentId+"_"+appList[i].id+'" name="subsystem'+parentId+'" onClick="pluginService.selectSubsystemService(this,\''+selectId+'\',\''+openPluginServiceDiv+'\')"> '+(appList[i].description?appList[i].description:appList[i].name)+'</label> ';
				if(appList[i].api.length){
					for(var m=0; m<appList[i].api.length; m++){
						sub_api +=
							'<label for="" class="checkbox inline"> '+
								'<input type="checkbox" id="api'+appList[i].api[m].id+'" name="service'+parentId+"_"+appList[i].id+'" value="'+appList[i].api[m].id+','+(appList[i].api[m].extStr1?appList[i].api[m].extStr1:appList[i].api[m].apiName)+'" onClick="pluginService.selectServiceSubsystem(this,\'subsystem'+parentId+'\',\''+selectId+'\',\''+openPluginServiceDiv+'\')"> '
								+'<a href="#" onclick="pluginService.showApiServiceInfo('+appList[i].api[m].id+');"><font color="#60AAE9">'+(appList[i].api[m].extStr1?appList[i].api[m].extStr1:appList[i].api[m].apiName)+'</font></a>'+
							'</label>';
					}
				}
			}else{
				sub_app += '<label for="" class="checkbox fl">'+appList[i].name+'</label> ';
				sub_api += '<label for="" class="checkbox inline"></label>';
			}
			sub_api += '</div>';
			sub_app += '</div>';
			var sub = $(
				'<li>'+
					'<div class="level'+level+' clearfix tree-list">'+
						sub_app +
						sub_api +
					'</div>'+
				'</li>'
			);
			$(sub).appendTo(parentHtml);
		}
	}
}

/**
 * 显示人员详细信息
 */
pluginService.showApiServiceInfo = function(id){
	var url = getRootPath()+"/sys/apiService/get.action";
	$.ajax({
		async: false,
		url : url,
		type : 'post',
		data: {
			id: id
		},
		success : function(data) {
			var dateTime = new Date().getTime(),
			showApiServiceInfo = "showApiServiceInfo" + dateTime,
			x = 1000,
		    y = 10,
		    info = ['<div class="modal fade panel" id="'+showApiServiceInfo+'" aria-hidden="false">'];
			info.push('<div class="modal-dialog"><div class="modal-content"><div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h4 class="modal-title">服务接口信息</h4></div><div class="modal-body dis-table" style="max-height: 661.5999999999999px;">');
			info.push('<section class="panel-tab-con dis-table-cell" style="padding-bottom:20px;"><table class="basicMsg" id="pInfo"><tbody><tr><td class="w115">服务编号</td>');
			if(data.uuid != null && data.uuid != "")
				info.push('<td>'+data.uuid+'</td>');
			else
				info.push('<td></td>');
			info.push('</tr><tr><td>子系统标识</td>');
		    if(data.subsystem != null && data.subsystem != "")
		    	info.push('<td>'+data.subsystem+'</td>');
		    else
		    	info.push('<td></td>');
		    info.push('</tr><tr><td>服务名字</td>');
		    if(data.appName != null && data.appName != "")
		    	info.push('<td>'+(data.extStr2?data.extStr2:data.appName)+'</td>');
		    else
		    	info.push('<td></td>');
		    info.push('</tr><tr><td>API接口名称</td>');
		    if(data.apiName != null && data.apiName != "")
		    	info.push('<td>'+(data.extStr1?data.extStr1:data.apiName)+'</td>');
		    else
		    	info.push('<td></td>');
		    info.push('</tr><tr><td>接口地址</td>');
		    if(data.uri != null && data.uri != "")
		    	info.push('<td>'+data.uri+'</td>');
		    else
		    	info.push('<td></td>');
		    info.push('</tr><tr><td>参数列表</td>');
		    if(data.params != null && data.params != "")
		    	info.push('<td>'+data.params+'</td>');
		    else
		    	info.push('<td></td>');
		    info.push('</tr></tbody></table><br/><br/></section></div></div></div></div>');
			$(info.join('')).appendTo("body");
			$("#"+showApiServiceInfo).modal("show");
		},
		error: function(){
			msgBox.tip({content: "获取API服务信息失败", type:'fail'});
		}
	});
}