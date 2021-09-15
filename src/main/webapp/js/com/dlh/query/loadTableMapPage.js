

//分页处理 start
//分页对象
loadTablePapPage.oTable = null;

loadTablePapPage.oTableAoColumns = loadTablePapPage_oTableAoColumns;

//设置每行按钮
loadTablePapPage.oTableSetButtones = function(source){
	var detail  = "<a  href=\"#\" onclick=\"loadTablePapPage.loadModuleForDetail('"+ source.id+ "','"+ source.modelId+ "')\">查看数据</a>";
	return detail;
};
loadTablePapPage.loadModuleForDetail = function(id){
	window.location.href=getRootPath()+"/dlh/dlhQuery/manageDetail.action?id="+id+"&n_"+(new Date().getTime());
}
loadTablePapPage.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(loadTablePapPage.oTable, aoData);
	var cond = [];
	$("#query_"+tabDefaultid).each(function(objIndex,object){
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
loadTablePapPage.dlhDataobjectList = function () {
	if (loadTablePapPage.oTable == null) {
		loadTablePapPage.oTable = $('#loadTablePapPageTable').dataTable( {
			"iDisplayLength": loadTablePapPage.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/dlh/dlhQuery/loadPromeryData.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": loadTablePapPage.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				loadTablePapPage.oTableFnServerParams(aoData);
			},
			"ordering": false
		} );
	} else {
		loadTablePapPage.oTable.fnDraw();
	}
};




$(document).ready(function(){
// alert("1111")

	$(".datepicker-input").each(function(){$(this).datepicker();});
	//计算分页显示条数
	loadTablePapPage.pageCount = TabNub>0 ? TabNub : 10;
	//初始化列表方法
	loadTablePapPage.dlhDataobjectList();

});