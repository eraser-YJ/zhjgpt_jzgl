var tabTreeList = {};

//分页处理 start
//分页对象
tabTreeList.oTable = null;

tabTreeList.oTableAoColumns = [
	{mData: function(source) {
			return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";
		}
	},
	{'mData':'sysPermission' },
	{'mData':'tabTitle' },
	{'mData':'tabUrl' },
	{'mData':'tabFlag' },
	{mData: function(source) {
		return oTableSetButtones(source);
	}}
];

tabTreeList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(tabTreeList.oTable, aoData);
};

//分页查询
tabTreeList.tabTreeList = function () {
	if (tabTreeList.oTable == null) {
		tabTreeList.oTable = $('#tabTreeTable').dataTable( {
			"iDisplayLength": tabTreeList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/tabTree/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": tabTreeList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				tabTreeList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3,4,5]}
	        ]
		} );
	} else {
		tabTreeList.oTable.fnDraw();
	}
};

//删除对象
tabTreeList.deleteTabTree = function (id) {
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
			tabTreeList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
tabTreeList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/tabTree/deleteByIds.action",
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
			tabTreeList.tabTreeList();
		}
	});
};

tabTreeList.queryReset = function(){
	$('#tabTreeQueryForm')[0].reset();
};
	
 //加载添加DIV
tabTreeList.loadModule = function (){
	if($.trim($("#tabTreeModuleDiv").html()).length == 0){
		$("#tabTreeModuleDiv").load(getRootPath()+"/sys/tabTree/loadForm.action",null,function(){
			tabTreeModule.show();
		});
	}else{
		tabTreeModule.show();
	}
};
		
//加载修改div
tabTreeList.loadModuleForUpdate = function (id){
	if($.trim($("#tabTreeModuleDiv").html()).length == 0){
		$("#tabTreeModuleDiv").load(getRootPath()+"/sys/tabTree/loadForm.action",null,function(){
			tabTreeModule.get(id);
		});
	}else{
		tabTreeModule.get(id);
	}
};

$(document).ready(function(){
	//计算分页显示条数
	tabTreeList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	tabTreeList.tabTreeList();
	$("#addTabTreeButton").click(tabTreeList.loadModule);
	/*$("#showAddDiv_t").click(tabTreeList.loadModule);*/
	$("#queryTabTree").click(tabTreeList.tabTreeList);
	$("#queryReset").click(tabTreeList.queryReset);
	$("#deleteTabTrees").click("click", function(){tabTreeList.deleteTabTree(0);});
});