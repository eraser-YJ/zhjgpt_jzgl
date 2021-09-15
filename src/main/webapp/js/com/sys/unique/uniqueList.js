var uniqueList = {};

//分页处理 start
//分页对象
uniqueList.oTable = null;

uniqueList.oTableAoColumns = [
	{'mData':'uuid' },
	{'mData':'state' ,"mRender" : function(mData, type, full){
		var result = "";
		if("0" == full.state){
			result+= "<span class=\"a-icon i-new\" style=\"padding:0 6px;\" href=\"#\" >&nbsp;&nbsp;未启用&nbsp;&nbsp;</span>";

		}else{
			result+= "<span class=\"a-icon i-remove m-r-xs\" style=\"padding:0 6px;\" >&nbsp;&nbsp;已启用&nbsp;&nbsp;</span>";

		}
		return result;
	}},
	{'mData':'createDate' },
	{mData: function(source) {
		return oTableSetButtones(source);
	}}
];

uniqueList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(uniqueList.oTable, aoData);
	var state = $("#state").val();
	if(state.length > 0){
		aoData.push({ "name": "state", "value": state});
	}
	var uuid = $("#uuid").val();
	if(uuid.length > 0){
		aoData.push({ "name": "uuid", "value": uuid});
	}
};

//分页查询
uniqueList.uniqueList = function () {
	if (uniqueList.oTable == null) {
		uniqueList.oTable = $('#uniqueTable').dataTable( {
			"iDisplayLength": uniqueList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/unique/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": uniqueList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				uniqueList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,3]}
	        ]
		} );
	} else {
		uniqueList.oTable.fnDraw();
	}
};

//删除对象
uniqueList.deleteUnique = function (id) {
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
			uniqueList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
uniqueList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/unique/deleteByIds.action",
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
			uniqueList.uniqueList();
		}
	});
};

uniqueList.queryReset = function(){
	$('#uniqueQueryForm')[0].reset();
};
	
 //加载添加DIV
uniqueList.loadModule = function (){
	if($.trim($("#uniqueModuleDiv").html()).length == 0){
		$("#uniqueModuleDiv").load(getRootPath()+"/sys/unique/loadForm.action",null,function(){
			uniqueModule.show();
		});
	}else{
		uniqueModule.show();
	}
};
		
//加载修改div
uniqueList.loadModuleForUpdate = function (id){
	if($.trim($("#uniqueModuleDiv").html()).length == 0){
		$("#uniqueModuleDiv").load(getRootPath()+"/sys/unique/loadForm.action",null,function(){
			uniqueModule.get(id);
		});
	}else{
		uniqueModule.get(id);
	}
};

$(document).ready(function(){

  ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	
	//计算分页显示条数
	uniqueList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	uniqueList.uniqueList();
	$("#addUniqueButton").click(uniqueList.loadModule);
	$("#showAddDiv_t").click(uniqueList.loadModule);
	$("#queryUnique").click(uniqueList.uniqueList);
	$("#queryReset").click(uniqueList.queryReset);
	$("#deleteUniques").click("click", function(){uniqueList.deleteUnique(0);});
});