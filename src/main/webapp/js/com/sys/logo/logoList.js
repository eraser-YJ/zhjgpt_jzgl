var logoList = {};

//分页处理 start
//分页对象
logoList.oTable = null;

logoList.oTableAoColumns = [
	{'mData':'logoName' },
	{'mData':'logoSign' },
	{'mData': 'logoFlag', mRender : function(mData, type, full){
		if(mData == '1')
			return "使用中";
		else
			return "未使用";
	}},
	{mData: function(source) {
		return oTableSetButtones(source);
	}}
];

logoList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(logoList.oTable, aoData);

};

//分页查询
logoList.logoList = function () {
	if (logoList.oTable == null) {
		logoList.oTable = $('#logoTable').dataTable( {
			"iDisplayLength": logoList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/logo/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": logoList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				logoList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3]}
	        ]
		} );
	} else {
		logoList.oTable.fnDraw();
	}
};

//删除对象
logoList.deleteLogo = function (id) {
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
			logoList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
logoList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/logo/deleteByIds.action",
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
			logoList.logoList();
		}
	});
};

logoList.queryReset = function(){
	$('#logoQueryForm')[0].reset();
};
	
 //加载添加DIV
logoList.loadModule = function (){
	if($.trim($("#logoModuleDiv").html()).length == 0){
		$("#logoModuleDiv").load(getRootPath()+"/sys/logo/loadForm.action",null,function(){
			logoModule.show();
		});
	}else{
		logoModule.show();
	}
};
		
//加载修改div
logoList.loadModuleForUpdate = function (id){
	if($.trim($("#logoModuleDiv").html()).length == 0){
		$("#logoModuleDiv").load(getRootPath()+"/sys/logo/loadForm.action",null,function(){
			logoModule.get(id);
		});
	}else{
		logoModule.get(id);
	}
};

$(document).ready(function(){
	//计算分页显示条数
	logoList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	logoList.logoList();
	$("#addLogoButton").click(logoList.loadModule);
	/*$("#showAddDiv_t").click(logoList.loadModule);*/
	$("#deleteLogos").click("click", function(){logoList.deleteLogo(0);});
});