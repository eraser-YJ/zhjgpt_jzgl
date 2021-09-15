var pluginList = {};

//分页处理 start
//分页对象
pluginList.oTable = null;

pluginList.isTrue = function(mData, type, full, column){
	var result = "";
	if(full[column]=="1"){
		result = "是";
	}
	else{
		result = "否";
	}
	return result;
}

pluginList.oTableAoColumns = [
	{'mData':'name' },
	{'mData':'description' },
	{'mData':'version' },
	{'mData':'jarName' },
	{'mData':'isDbReady' ,"mRender" : function(mData, type, full){return pluginList.isTrue(mData, type, full,'isDbReady')}},
	{'mData':'isServiceReady' ,"mRender" : function(mData, type, full){return pluginList.isTrue(mData, type, full,'isServiceReady')}},
	{'mData':'isMenuReady' ,"mRender" : function(mData, type, full){return pluginList.isTrue(mData, type, full,'isMenuReady')}},
	{'mData':'state' ,"mRender" : function(mData, type, full){
		var result = "";
		if(full.name=="Plugin initial"||full.name=="centre"||full.name=="Operlog"||full.name=="Setting"){
			result+= "<a class=\"\" style=\"padding:0 6px;\" href=\"#\" onclick=\"\">&nbsp;&nbsp;启用&nbsp;&nbsp;</a>";

		}else if("0" == full.state){
			result+= "<a class=\"a-icon i-remove m-r-xs\" style=\"padding:0 6px;\" href=\"#\" onclick=\"pluginList.changeStatus('"+
					full.id + "','"+ full.state+ "','"+ full.modifyDate +"')\">&nbsp;&nbsp;停用&nbsp;&nbsp;</a>";
		
		}else{
			result+= "<a class=\"a-icon i-new\" style=\"padding:0 6px;\" href=\"#\" onclick=\"pluginList.changeStatus('"+
					full.id + "','"+ full.state + "','"+ full.modifyDate +"')\" role=\"button\" data-toggle=\"modal\">&nbsp;&nbsp;启用&nbsp;&nbsp;</a>";
		
		}
		return result;
	}},
	{'mData':'createDate' },
	{mData: function(source) {
		return oTableSetButtones(source);
	}}
];

pluginList.changeStatus = function(id, state, modifyDate){
	if(state == "0"){
		state = "1";
		pluginList.doChangeStatus(id, state, modifyDate);
	}else{
		state = "0";
		msgBox.confirm({
			content: "是否确认停用该插件？",
			success: function(){
				pluginList.doChangeStatus(id, state, modifyDate);
			}
		});
	}
	
}

pluginList.doChangeStatus = function(id, state, modifyDate){
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/plugin/changeStatus.action",
		data : {"id": id, "state":eval(state), "modifyDate":modifyDate},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					type:"success",
					content: data.successMessage
				});
			} else {
				msgBox.info({
					content: data.errorMessage
				});
			}
			pluginList.pluginList();
		}
	});
}

pluginList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(pluginList.oTable, aoData);
	
	var name = $('#name').val();
if(name.length > 0){
	aoData.push({ 'name': 'name', 'value': name});
}
	var state = $('input[name="state"]:checked').val();
if(state!=null&&state.length > 0){
	aoData.push({ 'name': 'state', 'value': state});
}

};

//分页查询
pluginList.pluginList = function () {
	if (pluginList.oTable == null) {
		pluginList.oTable = $('#pluginTable').dataTable( {
			"iDisplayLength": pluginList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/plugin/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": pluginList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				pluginList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3,4,5,6,7,8,9]}
	        ]
		} );
	} else {
		pluginList.oTable.fnDraw();
	}
};

//删除对象
pluginList.deletePlugin = function (id) {
	var ids = String(id);
	if (id == 0) {
		var idsArr = [];
		$("[name='ids']:checked").each(function() {
			idsArr.push($(this).val());
		});
		ids = idsArr.join(",");
	}
	if (ids.length == 0) {
		msgBox.info({
			content: $.i18n.prop("JC_SYS_061")
		});
		return;
	}
	msgBox.confirm({
		content: $.i18n.prop("JC_SYS_034"),
		success: function(){
			pluginList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
pluginList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/plugin/deleteByIds.action",
		data : {"ids": ids},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					type:"success",
					content: data.successMessage
				});
			} else {
				msgBox.info({
					content: data.errorMessage
				});
			}
			pluginList.pluginList();
		}
	});
};

pluginList.queryReset = function(){
	$('#pluginQueryForm')[0].reset();
};
	
 //加载添加DIV
pluginList.loadModule = function (){
	if($.trim($("#pluginModuleDiv").html()).length == 0){
		$("#pluginModuleDiv").load(getRootPath()+"/sys/plugin/loadForm.action",null,function(){
			pluginModule.show();
		});
	}else{
		pluginModule.show();
	}
};

//加载授权DIV
pluginList.loadUpdateAuthorizeHtml = function (id){
	if(true){
		$("#roleAuthorize").load(getRootPath()+"/sys/role/roleAuthorize.action",null,function(responseTxt,statusTxt,xhr){
			if(statusTxt=="success"){
				pluginList.showPluginService(id);
			}
		});
	}
	else{
		pluginList.showPluginService(id);
	}
};
		
//加载修改div
pluginList.loadModuleForUpdate = function (id){
	if($.trim($("#pluginModuleDiv").html()).length == 0){
		
		$("#pluginModuleDiv").load(getRootPath()+"/sys/plugin/loadForm.action",null,function(responseTxt,statusTxt,xhr){
			if(statusTxt=="success"){
				pluginList.showPluginService(id);
			}
		});
		
	}else{
		pluginList.showPluginService(id);
	}
};

//显示授权页面
pluginList.showPluginService = function(pluginId){
	$('#pluginId').val(pluginId);
	pluginService.init("","name1-name", true, true);
	$('#myModal-pluginService').modal('show');
	return;
	
};

$(document).ready(function(){
	//计算分页显示条数
	pluginList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	pluginList.pluginList();
	$("#addPluginButton").click(pluginList.loadModule);
	$("#showAddDiv_t").click(pluginList.loadModule);
	$("#queryPlugin").click(pluginList.pluginList);
	$("#queryReset").click(pluginList.queryReset);
	$("#deletePlugins").click("click", function(){pluginList.deletePlugin(0);});
});