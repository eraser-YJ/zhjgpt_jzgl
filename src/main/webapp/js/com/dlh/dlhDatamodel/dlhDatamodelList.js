var dlhDatamodelList = {};

//分页处理 start
//分页对象
dlhDatamodelList.oTable = null;

dlhDatamodelList.oTableAoColumns = [
	{'mData':'id' },
	{'mData':'tableCode' },
	{'mData':'tableName' },
	{mData: function(source) {
		return dlhDatamodelList.oTableSetButtones(source);
	}}
];
//设置每行按钮
dlhDatamodelList.oTableSetButtones = function(source){
	var buttonStr = "";
	var edit = "<a  href=\"#\" onclick=\"dlhDatamodelList.loadModuleForUpdate('"+ source.id+ "')\">编辑</a>";
	var detail  = "<a  href=\"#\" onclick=\"dlhDatamodelList.loadModuleForDetail('"+ source.id+ "','"+ source.dbType+ "')\">详细</a>";
	var publish  = "<a  href=\"#\" onclick=\"dlhDatamodelList.publish('"+ source.id+ "')\">发布</a>";
	var adddata  = "<a  href=\"#\" onclick=\"dlhDatamodelList.loadModuleForm('"+ source.id+ "','"+source.dbType+"')\">添加数据</a>";
	var del  = "<a  href=\"#\" onclick=\"dlhDatamodelList.deleteDlhDatamodel('"+ source.id+ "')\">删除</a>";

	buttonStr = edit +"&nbsp;&nbsp;&nbsp;&nbsp;"+ detail+"&nbsp;&nbsp;&nbsp;&nbsp;"+publish+"&nbsp;&nbsp;&nbsp;&nbsp;"+ adddata+"&nbsp;&nbsp;&nbsp;&nbsp;"+ del;
	return buttonStr;
};

//删除用户回调方法
dlhDatamodelList.publish = function(id) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/dlh/dlhDatamodel/publish.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data.success) {
				msgBox.tip({
					type:"success",
					content: "发布成功"
				});
			} else {
				msgBox.info({
					content: data.message
				});
			}
			dlhDatamodelList.dlhDatamodelList();
		}
	});
};

dlhDatamodelList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(dlhDatamodelList.oTable, aoData);
	
	var tableCode = $('#query_tableCode').val();
	if(tableCode.length > 0){
		aoData.push({ 'name': 'tableCode', 'value': tableCode});
	}
	var tableName = $('#query_tableName').val();
	if(tableName.length > 0){
		aoData.push({ 'name': 'tableName', 'value': tableName});
	}
};

//分页查询
dlhDatamodelList.dlhDatamodelList = function () {
	if (dlhDatamodelList.oTable == null) {
		dlhDatamodelList.oTable = $('#dlhDatamodelTable').dataTable( {
			"iDisplayLength": dlhDatamodelList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/dlh/dlhDatamodel/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": dlhDatamodelList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				dlhDatamodelList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3]}
	        ]
		} );
	} else {
		dlhDatamodelList.oTable.fnDraw();
	}
};

//删除对象
dlhDatamodelList.deleteDlhDatamodel = function (id) {
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
			dlhDatamodelList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
dlhDatamodelList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/dlh/dlhDatamodel/deleteByIds.action",
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
			dlhDatamodelList.dlhDatamodelList();
		}
	});
};

dlhDatamodelList.queryReset = function(){
	$('#dlhDatamodelQueryForm')[0].reset();
};
	
 //加载添加DIV
dlhDatamodelList.loadModule = function (){
	if($.trim($("#dlhDatamodelModuleDiv").html()).length == 0){
		$("#dlhDatamodelModuleDiv").load(getRootPath()+"/dlh/dlhDatamodel/loadForm.action",null,function(){
			dlhDatamodelModule.show();
		});
	}else{
		dlhDatamodelModule.show();
	}
};
		
//加载修改div
dlhDatamodelList.loadModuleForUpdate = function (id){
	if($.trim($("#dlhDatamodelModuleDiv").html()).length == 0){
		$("#dlhDatamodelModuleDiv").load(getRootPath()+"/dlh/dlhDatamodel/loadForm.action",null,function(){
			dlhDatamodelModule.get(id);
		});
	}else{
		dlhDatamodelModule.get(id);
	}
};

dlhDatamodelList.loadModuleForm = function (id,dbType){
	window.location.href=getRootPath()+"/dlh/dlhDatamodelItem/manageForm.action?dbType="+dbType+"&modelId="+id+"&n_"+(new Date().getTime());
};


//加载修改div
dlhDatamodelList.loadModuleForDetail = function (id,dbType){
	window.location.href=getRootPath()+"/dlh/dlhDatamodelItem/manage.action?dbType="+dbType+"&modelId="+id+"&n_"+(new Date().getTime());
};

$(document).ready(function(){

  ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	
	//计算分页显示条数
	dlhDatamodelList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	dlhDatamodelList.dlhDatamodelList();
	$("#addDlhDatamodelButton").click(dlhDatamodelList.loadModule);
	$("#showAddDiv_t").click(dlhDatamodelList.loadModule);
	$("#queryDlhDatamodel").click(dlhDatamodelList.dlhDatamodelList);
	$("#queryReset").click(dlhDatamodelList.queryReset);
	$("#deleteDlhDatamodels").click("click", function(){dlhDatamodelList.deleteDlhDatamodel(0);});
});