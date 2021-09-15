var pinDepartmentList = {};

//分页处理 start
//分页对象
pinDepartmentList.oTable = null;

pinDepartmentList.oTableAoColumns = [
	{'mData':'deptName' },
	{'mData':'deptInitials' },
	{'mData':'deptAbbreviate' },
	{'mData':'deptFull' },
	{mData: function(source) {
		return oTableSetButtones(source);
	}}
];

pinDepartmentList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(pinDepartmentList.oTable, aoData);
	
	var deptName = $("#deptName").val();
	if(deptName){
		aoData.push({ "name": "deptName", "value": deptName});
	}
};

//分页查询
pinDepartmentList.pinDepartmentList = function () {
	if (pinDepartmentList.oTable == null) {
		pinDepartmentList.oTable = $('#pinDepartmentTable').dataTable( {
			"iDisplayLength": pinDepartmentList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/pinSubDepartment/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": pinDepartmentList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				pinDepartmentList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3,4]}
	        ]
		} );
	} else {
		pinDepartmentList.oTable.fnDraw();
	}
};

//删除对象
pinDepartmentList.deletePinDepartment = function (id) {
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
			pinDepartmentList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
pinDepartmentList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/pinSubDepartment/deleteByIds.action",
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
			pinDepartmentList.pinDepartmentList();
		}
	});
};

pinDepartmentList.infoLoad = function() {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/pinSubDepartment/infoLoading.action",
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
			pinDepartmentList.pinDepartmentList();
		}
	});
}

pinDepartmentList.queryReset = function(){
	$('#pinDepartmentQueryForm')[0].reset();
};
	
 //加载添加DIV
pinDepartmentList.loadModule = function (){
	if($.trim($("#pinDepartmentModuleDiv").html()).length == 0){
		$("#pinDepartmentModuleDiv").load(getRootPath()+"/sys/pinSubDepartment/loadForm.action",null,function(){
			pinDepartmentModule.show();
		});
	}else{
		pinDepartmentModule.show();
	}
};
		
//加载修改div
pinDepartmentList.loadModuleForUpdate = function (id){
	if($.trim($("#pinDepartmentModuleDiv").html()).length == 0){
		$("#pinDepartmentModuleDiv").load(getRootPath()+"/sys/pinSubDepartment/loadForm.action",null,function(){
			pinDepartmentModule.get(id);
		});
	}else{
		pinDepartmentModule.get(id);
	}
};

$(document).ready(function(){
	//计算分页显示条数
	pinDepartmentList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	pinDepartmentList.pinDepartmentList();
	//$("#addPinDepartmentButton").click(pinDepartmentList.loadModule);
	$("#showAddDiv_t").click(pinDepartmentList.loadModule);
	$("#queryPinDepartment").click(pinDepartmentList.pinDepartmentList);
	$("#queryReset").click(pinDepartmentList.queryReset);
	$("#info_loading").click(pinDepartmentList.infoLoad);
	//$("#deletePinDepartments").click("click", function(){pinDepartmentList.deletePinDepartment(0);});
});