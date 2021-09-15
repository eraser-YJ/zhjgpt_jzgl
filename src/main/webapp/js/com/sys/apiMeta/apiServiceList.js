var apiServiceList = {};

//分页处理 start
//分页对象
apiServiceList.oTable = null;

apiServiceList.oTableAoColumns = [
	{mData: function(source) {
			return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";
		}
	},
	{'mData':'subsystem' },
	{'mData':'appName' },
	{'mData':'apiName' },
	{'mData':'uri' },
	{'mData':'params' },
	{'mData':'createDate' },
	{mData: function(source) {
		return oTableSetButtones(source);
	}}
];

apiServiceList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(apiServiceList.oTable, aoData);
	
	var subsystem = $('#subsystem').val();
if(subsystem.length > 0){
	aoData.push({ 'name': 'subsystem', 'value': subsystem});
}
	var appName = $('#appName').val();
if(appName.length > 0){
	aoData.push({ 'name': 'appName', 'value': appName});
}
	var apiName = $('#apiName').val();
if(apiName.length > 0){
	aoData.push({ 'name': 'apiName', 'value': apiName});
}
};

//分页查询
apiServiceList.apiServiceList = function () {
	if (apiServiceList.oTable == null) {
		apiServiceList.oTable = $('#apiServiceTable').dataTable( {
			"iDisplayLength": apiServiceList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/apiService/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": apiServiceList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				apiServiceList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,7]}
	        ]
		} );
	} else {
		apiServiceList.oTable.fnDraw();
	}
};

//删除对象
apiServiceList.deleteApiService = function (id) {
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
			apiServiceList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
apiServiceList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/apiService/deleteByIds.action",
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
			apiServiceList.apiServiceList();
		}
	});
};

apiServiceList.queryReset = function(){
	$('#apiServiceQueryForm')[0].reset();
};
	
 //加载添加DIV
apiServiceList.loadModule = function (){
	if($.trim($("#apiServiceModuleDiv").html()).length == 0){
		$("#apiServiceModuleDiv").load(getRootPath()+"/sys/apiService/loadForm.action",null,function(){
			apiServiceModule.show();
		});
	}else{
		apiServiceModule.show();
	}
};
		
//加载修改div
apiServiceList.loadModuleForUpdate = function (id){
	if($.trim($("#apiServiceModuleDiv").html()).length == 0){
		$("#apiServiceModuleDiv").load(getRootPath()+"/sys/apiService/loadForm.action",null,function(){
			apiServiceModule.get(id);
		});
	}else{
		apiServiceModule.get(id);
	}
};

$(document).ready(function(){
	//计算分页显示条数
	apiServiceList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	apiServiceList.apiServiceList();
	$("#addApiServiceButton").click(apiServiceList.loadModule);
	$("#showAddDiv_t").click(apiServiceList.loadModule);
	$("#queryApiService").click(apiServiceList.apiServiceList);
	$("#queryReset").click(apiServiceList.queryReset);
	$("#deleteApiServices").click("click", function(){apiServiceList.deleteApiService(0);});
});