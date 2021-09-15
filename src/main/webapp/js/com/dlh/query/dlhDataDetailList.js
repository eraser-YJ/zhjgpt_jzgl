var dlhDataobjectList = {};

//分页处理 start
//分页对象
dlhDataobjectList.oTable = null;

dlhDataobjectList.oTableAoColumns = dlhDataobjectList_oTableAoColumns;

dlhDataobjectList.selectDataExport = function () {
	// var aoData = {};
	var cond = [];
	$("[name^='query_']").each(function(objIndex,object){
		//alert($(object).val());
		var value = $(object).val();
		if(value != ''){
			var operationAction = $(object).attr("operationAction");
			var operationKey = $(object).attr("operationKey");
			var operationType = $(object).attr("operationType");
			cond.push({"operationAction":operationAction,"operationKey":operationKey,"operationType":operationType,"value":value})
		}
	});
	// aoData.push({ 'name': 'condJson', 'value': JSON.stringify(cond)});
	// var data_src = $('#data_src_').val();
	$('#condJson').val(JSON.stringify(cond));

	$("#excelForm").attr("action","excelDownload.action");
	$("#excelForm").submit();
	// var url = getRootPath() + "/dlh/dlhQuery/excelDownload.action?data_src=" + data_src + "&condJson=" + JSON.stringify(cond);
	// window.location.href = url;


}

//设置每行按钮
dlhDataobjectList.oTableSetButtones = function(source){ 
	var detail  = "<a  href=\"#\" onclick=\"dlhDataobjectList.loadModuleForDetail('"+ source.id+ "','"+ source.modelId+ "')\">查看数据</a>"; 
	return detail; 
};
dlhDataobjectList.loadModuleForDetail = function(id){alert(id);
	window.location.href=getRootPath()+"/dlh/dlhQuery/manageDetail.action?id="+id+"&n_"+(new Date().getTime());
}
dlhDataobjectList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(dlhDataobjectList.oTable, aoData);
	var cond = [];
	$("[name^='query_']").each(function(objIndex,object){
		//alert($(object).val());
		var value = $(object).val();
		if(value != ''){
			var operationAction = $(object).attr("operationAction");
			var operationKey = $(object).attr("operationKey");
			var operationType = $(object).attr("operationType");
			cond.push({"operationAction":operationAction,"operationKey":operationKey,"operationType":operationType,"value":value})
		}		
	});
	aoData.push({ 'name': 'condJson', 'value': JSON.stringify(cond)});
	//console.log(JSON.stringify(cond));
};

//分页查询
dlhDataobjectList.dlhDataobjectList = function () {
	var data_src_ = $('#data_src_').val();
	if (dlhDataobjectList.oTable == null) {
		dlhDataobjectList.oTable = $('#dlhDataobjectTable').dataTable( {
			"iDisplayLength": dlhDataobjectList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/dlh/dlhQuery/manageDetailList.action?data_src="+data_src_,
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": dlhDataobjectList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				dlhDataobjectList.oTableFnServerParams(aoData);
			},
			"ordering": false                
		} );
	} else {
		dlhDataobjectList.oTable.fnDraw();
	}
};

dlhDataobjectList.queryReset = function(){
	$('#dlhDataDetailQueryForm')[0].reset();
};
	
 

$(document).ready(function(){

  ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});	
	//计算分页显示条数
	dlhDataobjectList.pageCount = TabNub>0 ? TabNub : 10;	
	//初始化列表方法
	dlhDataobjectList.dlhDataobjectList(); 
	$("#queryDlhDataobject").click(dlhDataobjectList.dlhDataobjectList);
	$("#queryReset").click(dlhDataobjectList.queryReset);
	$("#excel").click(dlhDataobjectList.selectDataExport);
});