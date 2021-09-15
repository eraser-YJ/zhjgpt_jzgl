var shortcutList = {};

//分页处理 start
//分页对象
shortcutList.oTable = null;

shortcutList.oTableAoColumns = [
	{mData: function(source) {
			return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";
		}
	},
	{'mData':'name' },
	{'mData':function(source){
		return source.subsystemName;
	} },
	{'mData':function(source){
		return source.menuName;
	} },
	{'mData':'queue' },
	{mData: function(source) {
		return oTableSetButtones(source);
	}}
];

shortcutList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(shortcutList.oTable, aoData);
	
	var name = $('#name').val();
if(name.length > 0){
	aoData.push({ 'name': 'name', 'value': name});
}
/*	var subsystemid = $('#subsystemid').val();
if(subsystemid.length > 0){
	aoData.push({ 'name': 'subsystemid', 'value': subsystemid});
}*/
};

//分页查询
shortcutList.shortcutList = function () {
	if (shortcutList.oTable == null) {
		shortcutList.oTable = $('#shortcutTable').dataTable( {
			"iDisplayLength": shortcutList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/shortcut/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": shortcutList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				shortcutList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,5]}
	        ]
		} );
	} else {
		shortcutList.oTable.fnDraw();
	}
};

//删除对象
shortcutList.deleteShortcut = function (id) {
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
			shortcutList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
shortcutList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/shortcut/deleteByIds.action",
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
			shortcutList.shortcutList();
		}
	});
};

shortcutList.queryReset = function(){
	$('#shortcutQueryForm')[0].reset();
};
	
 //加载添加DIV
shortcutList.loadModule = function (){
	if($.trim($("#shortcutModuleDiv").html()).length == 0){
		$("#shortcutModuleDiv").load(getRootPath()+"/sys/shortcut/loadForm.action",null,function(){
			shortcutModule.show();
		});
	}else{
		shortcutModule.show();
	}
};
		
//加载修改div
shortcutList.loadModuleForUpdate = function (id){
	if($.trim($("#shortcutModuleDiv").html()).length == 0){
		$("#shortcutModuleDiv").load(getRootPath()+"/sys/shortcut/loadForm.action",null,function(){
			shortcutModule.get(id);
		});
	}else{
		shortcutModule.get(id);
	}
};

$(document).ready(function(){
	//计算分页显示条数
	shortcutList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	shortcutList.shortcutList();
	$("#addShortcutButton").click(shortcutList.loadModule);
	$("#showAddDiv_t").click(shortcutList.loadModule);
	$("#queryShortcut").click(shortcutList.shortcutList);
	$("#queryReset").click(shortcutList.queryReset);
	$("#deleteShortcuts").click("click", function(){shortcutList.deleteShortcut(0);});
});