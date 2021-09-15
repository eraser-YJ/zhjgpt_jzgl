var dlhDataobjectFieldList = {};

//分页处理 start
//分页对象
dlhDataobjectFieldList.oTable = null;

dlhDataobjectFieldList.oTableAoColumns = [
	{mData: function(source) {
			return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";
		}
	},
	{'mData':'fieldCode' },
	{'mData':'fieldName' },
	{'mData':'itemName' },
	{'mData':'fieldSeq' },
	{mData: function(source) {
		return dlhDataobjectFieldList.oTableSetButtones(source);
	}}
];

//设置每行按钮
dlhDataobjectFieldList.oTableSetButtones = function(source){
	var buttonStr = "";
	var edit = "<a class=\"a-icon i-edit m-r-xs\" href=\"#myModal-edit\" onclick=\"dlhDataobjectFieldList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
	var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"dlhDataobjectFieldList.deleteDlhDataobjectField('"+ source.id+ "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
	buttonStr = edit + del;
	return buttonStr;
};

dlhDataobjectFieldList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(dlhDataobjectFieldList.oTable, aoData);
	aoData.push({ 'name': 'modelId', 'value': $('#query_modelId').val()});
	aoData.push({ 'name': 'objectId', 'value': $('#query_objectId').val()});
	var fieldName = $('#query_fieldName').val();
	if(fieldName.length > 0){
		aoData.push({ 'name': 'fieldName', 'value': fieldName});
	}
 
};

//分页查询
dlhDataobjectFieldList.dlhDataobjectFieldList = function () {
	if (dlhDataobjectFieldList.oTable == null) {
		dlhDataobjectFieldList.oTable = $('#dlhDataobjectFieldTable').dataTable( {
			"iDisplayLength": dlhDataobjectFieldList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/dlh/dlhDataobjectField/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": dlhDataobjectFieldList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				dlhDataobjectFieldList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3,4,5]}
	        ]
		} );
	} else {
		dlhDataobjectFieldList.oTable.fnDraw();
	}
};

//删除对象
dlhDataobjectFieldList.deleteDlhDataobjectField = function (id) {
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
			dlhDataobjectFieldList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
dlhDataobjectFieldList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/dlh/dlhDataobjectField/deleteByIds.action",
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
			dlhDataobjectFieldList.dlhDataobjectFieldList();
		}
	});
};

dlhDataobjectFieldList.queryReset = function(){
	$('#dlhDataobjectFieldQueryForm')[0].reset();
};
	
 //加载添加DIV
dlhDataobjectFieldList.loadModule = function (){
	if($.trim($("#dlhDataobjectFieldModuleDiv").html()).length == 0){
		$("#dlhDataobjectFieldModuleDiv").load(getRootPath()+"/dlh/dlhDataobjectField/loadForm.action?dbType=mongo",null,function(){
			dlhDataobjectFieldModule.show();
		});
	}else{
		dlhDataobjectFieldModule.show();
	}
};
		
//加载修改div
dlhDataobjectFieldList.loadModuleForUpdate = function (id){
	if($.trim($("#dlhDataobjectFieldModuleDiv").html()).length == 0){
		$("#dlhDataobjectFieldModuleDiv").load(getRootPath()+"/dlh/dlhDataobjectField/loadForm.action?dbType=mongo",null,function(){
			dlhDataobjectFieldModule.get(id);
		});
	}else{
		dlhDataobjectFieldModule.get(id);
	}
};

$(document).ready(function(){

  ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	
	//计算分页显示条数
	dlhDataobjectFieldList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	dlhDataobjectFieldList.dlhDataobjectFieldList();
	$("#addDlhDataobjectFieldButton").click(dlhDataobjectFieldList.loadModule);
	$("#showAddDiv_t").click(dlhDataobjectFieldList.loadModule);
	$("#queryDlhDataobjectField").click(dlhDataobjectFieldList.dlhDataobjectFieldList);
	$("#queryReset").click(dlhDataobjectFieldList.queryReset);
	$("#deleteDlhDataobjectFields").click("click", function(){dlhDataobjectFieldList.deleteDlhDataobjectField(0);});
});