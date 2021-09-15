var dlhUserList = {};

//分页处理 start
//分页对象
dlhUserList.oTable = null;

dlhUserList.oTableAoColumns = [
	{'mData':'dlhUsercode' },
	{'mData':'dlhUsername' },
	{'mData':'batchNum' },
	{'mData':'createDate' },
	{mData: function(source) {
		return oTableSetButtones(source);
	}}
];

//分页查询
dlhUserList.loadModuleForHelp = function (id) {
	var url = getRootPath()+"/api/dlh/doc/index.action?id="+id+"&n_="+(new Date()).getTime();
	window.open( url);
}
dlhUserList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(dlhUserList.oTable, aoData);
	
	var dlhUsercode = $('#dlhUsercode').val();
	if(dlhUsercode.length > 0){
		aoData.push({ 'name': 'dlhUsercode', 'value': dlhUsercode});
	}
		var dlhUsername = $('#dlhUsername').val();
	if(dlhUsername.length > 0){
		aoData.push({ 'name': 'dlhUsername', 'value': dlhUsername});
	}
};

//分页查询
dlhUserList.dlhUserList = function () {
	if (dlhUserList.oTable == null) {
		dlhUserList.oTable = $('#dlhUserTable').dataTable( {
			"iDisplayLength": dlhUserList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/dlh/dlhUser/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": dlhUserList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				dlhUserList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,4]}
	        ]
		} );
	} else {
		dlhUserList.oTable.fnDraw();
	}
};

//删除对象
dlhUserList.deleteDlhUser = function (id) {
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
			dlhUserList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
dlhUserList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/dlh/dlhUser/deleteByIds.action",
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
			dlhUserList.dlhUserList();
		}
	});
};

dlhUserList.queryReset = function(){
	$('#dlhUserQueryForm')[0].reset();
};
	
 //加载添加DIV
dlhUserList.loadModule = function (){
	if($.trim($("#dlhUserModuleDiv").html()).length == 0){
		$("#dlhUserModuleDiv").load(getRootPath()+"/dlh/dlhUser/loadForm.action",null,function(){
			dlhUserModule.show();
		});
	}else{
		dlhUserModule.show();
	}
};
		
//加载修改div
dlhUserList.loadModuleForUpdate = function (id){
	if($.trim($("#dlhUserModuleDiv").html()).length == 0){
		$("#dlhUserModuleDiv").load(getRootPath()+"/dlh/dlhUser/loadForm.action",null,function(){
			dlhUserModule.get(id);
		});
	}else{
		dlhUserModule.get(id);
	}
};

$(document).ready(function(){

  ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	
	//计算分页显示条数
	dlhUserList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	dlhUserList.dlhUserList();
	$("#addDlhUserButton").click(dlhUserList.loadModule);
	$("#showAddDiv_t").click(dlhUserList.loadModule);
	$("#queryDlhUser").click(dlhUserList.dlhUserList);
	$("#queryReset").click(dlhUserList.queryReset);
	$("#deleteDlhUsers").click("click", function(){dlhUserList.deleteDlhUser(0);});
});