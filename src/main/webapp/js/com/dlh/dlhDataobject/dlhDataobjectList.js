var dlhDataQueryList = {};

//分页处理 start
//分页对象
dlhDataQueryList.oTable = null;

dlhDataQueryList.oTableAoColumns = [	
	{'mData':'objUrl' },
	{'mData':'objName' },
	{'mData':'modelId' },
	{mData: function(source) {
		return dlhDataQueryList.oTableSetButtones(source);
	}}
];

//设置每行按钮
dlhDataQueryList.oTableSetButtones = function(source){ 
	var buttonStr = "";
	var edit = "<a  href=\"#\" onclick=\"dlhDataQueryList.loadModuleForUpdate('"+ source.id+ "')\">编辑</a>";
	var detail  = "<a  href=\"#\" onclick=\"dlhDataQueryList.loadModuleForDetail('"+ source.id+ "','"+ source.modelId+ "','"+ source.dbType+ "')\">详细</a>";
	var init  = "<a  href=\"#\" onclick=\"dlhDataQueryList.init('"+ source.id+ "','"+ source.modelId+ "')\">初始化</a>";
	var del  = "<a  href=\"#\" onclick=\"dlhDataQueryList.deleteDlhDataobject('"+ source.id+ "')\">删除</a>";

	buttonStr = edit +"&nbsp;&nbsp;&nbsp;&nbsp;" + detail +"&nbsp;&nbsp;&nbsp;&nbsp;"+ init +"&nbsp;&nbsp;&nbsp;&nbsp;" + del;
	return buttonStr; 
};

//加载修改div
dlhDataQueryList.loadModuleForDetail = function (id,modelId,dbType){
	window.location.href=getRootPath()+"/dlh/dlhDataobjectField/manage.action?dbType="+dbType+"&objectId="+id+"&modelId="+modelId+"&n_"+(new Date().getTime());
};
//加载修改div
dlhDataQueryList.init = function (id){
	$.ajax({
		type : "POST",
		url : getRootPath()+"/dlh/dlhDataobject/init.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data.success) {
				msgBox.tip({
					type:"success",
					content: "初始化成功"
				});
			} else {
				msgBox.info({
					content: data.message
				});
			}
		}
	});
};
dlhDataQueryList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(dlhDataQueryList.oTable, aoData);
	
	var modelId = $('#query_modelId').val();
	if(modelId.length > 0){
		aoData.push({ 'name': 'modelId', 'value': modelId});
	}
	var objUrl = $('#query_objUrl').val();
	if(objUrl.length > 0){
		aoData.push({ 'name': 'objUrl', 'value': objUrl});
	}
	var objName = $('#query_objName').val();
	if(objName.length > 0){
		aoData.push({ 'name': 'objName', 'value': objName});
	}
};

//分页查询
dlhDataQueryList.dlhDataQueryList = function () {
	if (dlhDataQueryList.oTable == null) {
		dlhDataQueryList.oTable = $('#dlhDataobjectTable').dataTable( {
			"iDisplayLength": dlhDataQueryList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/dlh/dlhDataobject/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": dlhDataQueryList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				dlhDataQueryList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3]}
	        ]
		} );
	} else {
		dlhDataQueryList.oTable.fnDraw();
	}
};

//删除对象
dlhDataQueryList.deleteDlhDataobject = function (id) {
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
			dlhDataQueryList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
dlhDataQueryList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/dlh/dlhDataobject/deleteByIds.action",
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
			dlhDataQueryList.dlhDataQueryList();
		}
	});
};

dlhDataQueryList.queryReset = function(){
	$('#dlhDataobjectQueryForm')[0].reset();
};
	
 //加载添加DIV
dlhDataQueryList.loadModule = function (){
	if($.trim($("#dlhDataobjectModuleDiv").html()).length == 0){
		$("#dlhDataobjectModuleDiv").load(getRootPath()+"/dlh/dlhDataobject/loadForm.action",null,function(){
			dlhDataobjectModule.show();
		});
	}else{
		dlhDataobjectModule.show();
	}
};
		
//加载修改div
dlhDataQueryList.loadModuleForUpdate = function (id){
	if($.trim($("#dlhDataobjectModuleDiv").html()).length == 0){
		$("#dlhDataobjectModuleDiv").load(getRootPath()+"/dlh/dlhDataobject/loadForm.action",null,function(){
			dlhDataobjectModule.get(id);
		});
	}else{
		dlhDataobjectModule.get(id);
	}
};

$(document).ready(function(){

  ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	
	//计算分页显示条数
	dlhDataQueryList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	dlhDataQueryList.dlhDataQueryList();
	$("#addDlhDataobjectButton").click(dlhDataQueryList.loadModule);
	$("#showAddDiv_t").click(dlhDataQueryList.loadModule);
	$("#queryDlhDataobject").click(dlhDataQueryList.dlhDataQueryList);
	$("#queryReset").click(dlhDataQueryList.queryReset);
	$("#deleteDlhDataobjects").click("click", function(){dlhDataQueryList.deleteDlhDataobject(0);});
});