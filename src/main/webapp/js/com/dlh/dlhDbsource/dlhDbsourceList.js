var dlhDbsourceList = {};

//分页处理 start
//分页对象
dlhDbsourceList.oTable = null;

dlhDbsourceList.oTableAoColumns = [
	{'mData':'dbCode' },
	{'mData':'dbType' },
	{'mData':'dbAddress' },
	{mData: function(source) {
		return dlhDbsourceList.oTableSetButtones(source);
	}}
];

//设置每行按钮
dlhDbsourceList.oTableSetButtones = function(source){
	var buttonStr = "";
	var edit = "<a class=\"a-icon i-edit m-r-xs\" href=\"#myModal-edit\" onclick=\"dlhDbsourceList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
	var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"dlhDbsourceList.deleteDlhDbsource('"+ source.id+ "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
	buttonStr = edit + del;
	return buttonStr;
};

dlhDbsourceList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(dlhDbsourceList.oTable, aoData);
	
	var dbCode = $('#query_dbCode').val();
	if(dbCode.length > 0){
		aoData.push({ 'name': 'dbCode', 'value': dbCode});
	}
};

//分页查询
dlhDbsourceList.dlhDbsourceList = function () {
	if (dlhDbsourceList.oTable == null) {
		dlhDbsourceList.oTable = $('#dlhDbsourceTable').dataTable( {
			"iDisplayLength": dlhDbsourceList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/dlh/dlhDbsource/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": dlhDbsourceList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				dlhDbsourceList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3]}
	        ]
		} );
	} else {
		dlhDbsourceList.oTable.fnDraw();
	}
};

//删除对象
dlhDbsourceList.deleteDlhDbsource = function (id) {
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
			dlhDbsourceList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
dlhDbsourceList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/dlh/dlhDbsource/deleteByIds.action",
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
			dlhDbsourceList.dlhDbsourceList();
		}
	});
};

dlhDbsourceList.queryReset = function(){
	$('#dlhDbsourceQueryForm')[0].reset();
};
	
 //加载添加DIV
dlhDbsourceList.loadModule = function (){
	if($.trim($("#dlhDbsourceModuleDiv").html()).length == 0){
		$("#dlhDbsourceModuleDiv").load(getRootPath()+"/dlh/dlhDbsource/loadForm.action",null,function(){
			dlhDbsourceModule.show();
		});
	}else{
		dlhDbsourceModule.show();
	}
};
		
//加载修改div
dlhDbsourceList.loadModuleForUpdate = function (id){
	if($.trim($("#dlhDbsourceModuleDiv").html()).length == 0){
		$("#dlhDbsourceModuleDiv").load(getRootPath()+"/dlh/dlhDbsource/loadForm.action",null,function(){
			dlhDbsourceModule.get(id);
		});
	}else{
		dlhDbsourceModule.get(id);
	}
};

$(document).ready(function(){

  ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	
	//计算分页显示条数
	dlhDbsourceList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	dlhDbsourceList.dlhDbsourceList();
	$("#addDlhDbsourceButton").click(dlhDbsourceList.loadModule);
	$("#showAddDiv_t").click(dlhDbsourceList.loadModule);
	$("#queryDlhDbsource").click(dlhDbsourceList.dlhDbsourceList);
	$("#queryReset").click(dlhDbsourceList.queryReset);
	$("#deleteDlhDbsources").click("click", function(){dlhDbsourceList.deleteDlhDbsource(0);});
});