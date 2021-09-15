var pinUserList = {};

//分页处理 start
//分页对象
pinUserList.oTable = null;

pinUserList.oTableAoColumns = [
	{'mData':'userName' },
	{'mData':'userInitials' },
	{'mData':'userAbbreviate' },
	{'mData':'userFull' },
	{mData: function(source) {
		return oTableSetButtones(source);
	}}
];

pinUserList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(pinUserList.oTable, aoData);
	
	var userName = $("#userName").val();
	if(userName){
		aoData.push({ "name": "userName", "value": userName});
	}

};

//分页查询
pinUserList.pinUserList = function () {
	if (pinUserList.oTable == null) {
		pinUserList.oTable = $('#pinUserTable').dataTable( {
			"iDisplayLength": pinUserList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/pinSubUser/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": pinUserList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				pinUserList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3,4]}
	        ]
		} );
	} else {
		pinUserList.oTable.fnDraw();
	}
};

//删除对象
pinUserList.deletePinUser = function (id) {
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
			pinUserList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
pinUserList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/pinSubUser/deleteByIds.action",
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
			pinUserList.pinUserList();
		}
	});
};

pinUserList.infoLoad = function() {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/pinSubUser/infoLoading.action",
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					type:"success",
					content: data.successMessage
				});
			} else {
				msgBox.info({
					content: "没有可以导入的数据!"
				});
			}
			pinUserList.pinUserList();
		}
	});
}

pinUserList.queryReset = function(){
	$('#pinUserQueryForm')[0].reset();
};
	
 //加载添加DIV
pinUserList.loadModule = function (){
	if($.trim($("#pinUserModuleDiv").html()).length == 0){
		$("#pinUserModuleDiv").load(getRootPath()+"/sys/pinSubUser/loadForm.action",null,function(){
			pinUserModule.show();
		});
	}else{
		pinUserModule.show();
	}
};
		
//加载修改div
pinUserList.loadModuleForUpdate = function (id){
	if($.trim($("#pinUserModuleDiv").html()).length == 0){
		$("#pinUserModuleDiv").load(getRootPath()+"/sys/pinSubUser/loadForm.action",null,function(){
			pinUserModule.get(id);
		});
	}else{
		pinUserModule.get(id);
	}
};

$(document).ready(function(){
	//计算分页显示条数
	pinUserList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	pinUserList.pinUserList();
	//$("#addPinUserButton").click(pinUserList.loadModule);
	$("#showAddDiv_t").click(pinUserList.loadModule);
	$("#queryPinUser").click(pinUserList.pinUserList);
	$("#queryReset").click(pinUserList.queryReset);
	$("#info_loading").click(pinUserList.infoLoad);
	//$("#deletePinUsers").click("click", function(){pinUserList.deletePinUser(0);});
});