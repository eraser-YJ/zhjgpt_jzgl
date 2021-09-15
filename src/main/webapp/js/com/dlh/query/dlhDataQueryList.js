var dlhDataobjectList = {};

//分页处理 start
//分页对象
dlhDataobjectList.oTable = null;

dlhDataobjectList.oTableAoColumns = [	
	{'mData':'objUrl' },
	{'mData':'objName' }, 
	{mData: function(source) {
		return dlhDataobjectList.oTableSetButtones(source);
	}}
];

//设置每行按钮
dlhDataobjectList.oTableSetButtones = function(source){ 
	var detail  = "<a  href=\"#\" onclick=\"dlhDataobjectList.loadModuleForDetail('"+ source.id+ "','"+ source.modelId+ "')\">查看数据</a>"; 
	return detail; 
};
dlhDataobjectList.loadModuleForDetail = function(id){
	window.location.href=getRootPath()+"/dlh/dlhQuery/manageDetail.action?id="+id+"&n_"+(new Date().getTime());
}
dlhDataobjectList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(dlhDataobjectList.oTable, aoData);	
	
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
dlhDataobjectList.dlhDataobjectList = function () {
	if (dlhDataobjectList.oTable == null) {
		dlhDataobjectList.oTable = $('#dlhDataobjectTable').dataTable( {
			"iDisplayLength": dlhDataobjectList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/dlh/dlhQuery/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": dlhDataobjectList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				dlhDataobjectList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2]}
	        ]
		} );
	} else {
		dlhDataobjectList.oTable.fnDraw();
	}
};

dlhDataobjectList.queryReset = function(){
	$('#dlhDataobjectQueryForm')[0].reset();
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
});