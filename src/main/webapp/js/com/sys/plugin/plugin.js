var plugin = {}, pageCount=10;

//重复提交标识
plugin.subState = false;
//分页处理 start
//分页对象
plugin.oTable = null;

//显示列信息
//插件
plugin.oTableAoColumns = [
	{ "mData": "uuid" },
	{ "mData": "name" },
	{ "mData": "description" },
	{ "mData": "version" },
	{ "mData": "isDBReady" },
	{ "mData": "isServiceReady" },
	{ "mData": "isMenuReady" },
	{ "mData": "enable" }
];

plugin.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(plugin.oTable, aoData);
	
};

//分页查询
plugin.operlogList = function () {
	if (plugin.oTable == null) {
		plugin.oTable = $('#operlogTable').dataTable( {
			"iDisplayLength": plugin.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/plugin/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": plugin.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				plugin.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3,4,5]}
	        ]
		} );
	} else {
		plugin.oTable.fnDraw();
	}
};

//初始化
jQuery(function($) 
{
	//计算分页显示条数
	plugin.pageCount = TabNub>0 ? TabNub : 10;
	$("#queryoperlog").click(plugin.operlogList);

	//初始化列表方法
	plugin.operlogList();
	    		

});	

