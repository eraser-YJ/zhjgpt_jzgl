var dlhDatamodelItemList = {};

//分页处理 start
//分页对象
dlhDatamodelItemList.oTable = null;

dlhDatamodelItemList.oTableAoColumns = [
	{mData: function(source) {
			return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";
		}
	},
	{'mData':'itemName' },
	{'mData':'itemComment' },
	{'mData':'itemType' },
	{'mData':'itemLen' },
	{'mData':'itemKey' },
	{'mData':'itemSeq' },
	{mData: function(source) {
		return dlhDatamodelItemList.oTableSetButtones(source);
	}}
];
dlhDatamodelItemList.oTableSetButtones = function(source){
	var buttonStr = "";
	var edit = "<a class=\"a-icon i-edit m-r-xs\" href=\"#myModal-edit\" onclick=\"dlhDatamodelItemList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
	var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"dlhDatamodelItemList.deleteDlhDatamodelItem('"+ source.id+ "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
	buttonStr = edit + del;
	return buttonStr;
};
dlhDatamodelItemList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(dlhDatamodelItemList.oTable, aoData);
	aoData.push({ 'name': 'modelId', 'value': $('#query_modelId').val()});
	var itemName = $('#query_itemName').val();
	if(itemName.length > 0){
		aoData.push({ 'name': 'itemName', 'value': itemName});
	}
	var itemComment = $('#query_itemComment').val();
	if(itemComment.length > 0){
		aoData.push({ 'name': 'itemComment', 'value': itemComment});
	}
};

//分页查询
dlhDatamodelItemList.dlhDatamodelItemList = function () {
	if (dlhDatamodelItemList.oTable == null) {
		dlhDatamodelItemList.oTable = $('#dlhDatamodelItemTable').dataTable( {
			"iDisplayLength": dlhDatamodelItemList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/dlh/dlhDatamodelItem/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": dlhDatamodelItemList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				dlhDatamodelItemList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,6]}
	        ]
		} );
	} else {
		dlhDatamodelItemList.oTable.fnDraw();
	}
};

//删除对象
dlhDatamodelItemList.deleteDlhDatamodelItem = function (id) {
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
			dlhDatamodelItemList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
dlhDatamodelItemList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/dlh/dlhDatamodelItem/deleteByIds.action",
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
			dlhDatamodelItemList.dlhDatamodelItemList();
		}
	});
};

dlhDatamodelItemList.queryReset = function(){
	$('#dlhDatamodelItemQueryForm')[0].reset();
};
	
 //加载添加DIV
dlhDatamodelItemList.loadModule = function (){
	if($.trim($("#dlhDatamodelItemModuleDiv").html()).length == 0){
		$("#dlhDatamodelItemModuleDiv").load(getRootPath()+"/dlh/dlhDatamodelItem/loadForm.action?dbType=mysql",null,function(){
			dlhDatamodelItemModule.show();
		});
	}else{
		dlhDatamodelItemModule.show();
	}
};
		
//加载修改div
dlhDatamodelItemList.loadModuleForUpdate = function (id){
	if($.trim($("#dlhDatamodelItemModuleDiv").html()).length == 0){
		$("#dlhDatamodelItemModuleDiv").load(getRootPath()+"/dlh/dlhDatamodelItem/loadForm.action?dbType=mysql",null,function(){
			dlhDatamodelItemModule.get(id);
		});
	}else{
		dlhDatamodelItemModule.get(id);
	}
};

/**
 *  导入Excel
 */
dlhDatamodelItemList.importExcel = function () {
	var modelId=$('#query_modelId').val();
	//ajax提交
	$.ajaxFileUpload({
		//处理文件上传操作的服务器端地址(可以传参数,已亲测可用)
		url:getRootPath()+"/dlh/dlhDatamodelItem/importExcel.action",
		data : {"modelId": modelId},
		secureuri:false,
		fileElementId:"excel",
		contentType : 'multipart/form-data;charset=UTF-8',
		dataType : 'json', // 返回值类型 一般设置为json
		success:function(data){
			if (data.success=="true"){
				msgBox.info({
					type:"success",
					content: "导入成功"
				});
			}else{
				msgBox.info({
					content: data.errorMsg
				});
			}
			dlhDatamodelItemList.dlhDatamodelItemList();
		}
	});
};

$(document).ready(function(){

  ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	
	//计算分页显示条数
	dlhDatamodelItemList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	dlhDatamodelItemList.dlhDatamodelItemList();
	$("#addDlhDatamodelItemButton").click(dlhDatamodelItemList.loadModule);
	$("#showAddDiv_t").click(dlhDatamodelItemList.loadModule);
	$("#queryDlhDatamodelItem").click(dlhDatamodelItemList.dlhDatamodelItemList);
	$("#queryReset").click(dlhDatamodelItemList.queryReset);
	$("#deleteDlhDatamodelItems").click("click", function(){dlhDatamodelItemList.deleteDlhDatamodelItem(0);});
	//绑定导入按钮
	$("#buttonImport").click(dlhDatamodelItemList.importExcel);
});

