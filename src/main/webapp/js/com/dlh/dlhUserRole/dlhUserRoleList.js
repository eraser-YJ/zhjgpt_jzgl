var dlhUserRoleList = {};

//分页处理 start
//分页对象
dlhUserRoleList.oTable = null;

dlhUserRoleList.oTableAoColumns = [
	{'mData':'dlhUsercode' },
	{'mData':'dlhUsername' },
	{'mData':'batchNum' },
	{'mData':'createDate' },
	{mData: function(source) {
			return oTableSetButtones(source);
		}}
];

dlhUserRoleList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(dlhUserRoleList.oTable, aoData);

};

//分页查询
dlhUserRoleList.dlhUserRoleList = function () {
	if (dlhUserRoleList.oTable == null) {
		dlhUserRoleList.oTable = $('#dlhUserRoleTable').dataTable( {
			"iDisplayLength": dlhUserRoleList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/dlh/dlhUser/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": dlhUserRoleList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				dlhUserRoleList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
			,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,4]}
			]
		} );
	} else {
		dlhUserRoleList.oTable.fnDraw();
	}
};


dlhUserRoleList.queryReset = function(){
	$('#dlhUserRoleQueryForm')[0].reset();
};
	
 //加载添加DIV
dlhUserRoleList.loadModule = function (){
	if($.trim($("#dlhUserRoleModuleDiv").html()).length == 0){
		$("#dlhUserRoleModuleDiv").load(getRootPath()+"/dlh/dlhUserRole/loadForm.action",null,function(){
			dlhUserRoleModule.show();
		});
	}else{
		dlhUserRoleModule.show();
	}
};
		
//加载修改div
dlhUserRoleList.loadModuleForUpdate = function (id){
	if($.trim($("#dlhUserRoleModuleDiv").html()).length == 0){
		$("#dlhUserRoleModuleDiv").load(getRootPath()+"/dlh/dlhUserRole/loadForm.action",null,function(){
			dlhUserRoleModule.get(id);
		});
	}else{
		dlhUserRoleModule.get(id);
	}
};

$(document).ready(function(){

  ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	
	//计算分页显示条数
	dlhUserRoleList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	dlhUserRoleList.dlhUserRoleList();
	$("#addDlhUserRoleButton").click(dlhUserRoleList.loadModule);
	$("#showAddDiv_t").click(dlhUserRoleList.loadModule);
	$("#queryDlhUserRole").click(dlhUserRoleList.dlhUserRoleList);
	$("#queryReset").click(dlhUserRoleList.queryReset);
	$("#deleteDlhUserRoles").click("click", function(){dlhUserRoleList.deleteDlhUserRole(0);});
});