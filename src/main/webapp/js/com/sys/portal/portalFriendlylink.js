$(document).ready(function(){
	
	//计算分页显示条数
	portalFriendlylink.pageCount = TabNub>0 ? TabNub : 10;
	//初始化列表方法
	portalFriendlylink.portalFriendlylinkList();
	
	$("#addPortalFriendlylinkButton").click(portalFriendlylink.loadModule);
	$("#showAddDiv_t").click(portalFriendlylink.loadModule);
	
	$("#queryPortalFriendlylink").click(portalFriendlylink.portalFriendlylinkList);
	$("#queryReset").click(portalFriendlylink.queryReset);
	$("#deletePortalFriendlylinks").click("click", function(){portalFriendlylink.deletePortalFriendlylink(0);});
    

});
  	

	
var portalFriendlylink = {};

//分页处理 start
//分页对象
portalFriendlylink.oTable = null;

portalFriendlylink.oTableAoColumns = [
	{mData: function(source) {
			return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";
		}
	},
	{ "mData": "linkName" },
	{ "mData": "linkUrl" },
	{ "mData": "portalId", mRender : function(mData, type, full) {
		return full.portalName;
	}},
	{ "mData": "portletId", mRender : function(mData, type, full) {
		return full.portletName;
	}},
	{ "mData": "sequence" },
	{mData: function(source) {
		return oTableSetButtones(source);
	}}
];

portalFriendlylink.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(portalFriendlylink.oTable, aoData);
	
	var portalId = $("#portalFriendlylinkQueryForm #portalId").val();
	var portletId = $("#portalFriendlylinkQueryForm #portletId").val();
	
	if(portalId > 0){
		aoData.push({ "name": "portalId", "value": portalId});
	}
	
	if(portletId > 0){
		aoData.push({ "name": "portletId", "value": portletId});
	}
};

//分页查询
portalFriendlylink.portalFriendlylinkList = function () {
	if (portalFriendlylink.oTable == null) {
		portalFriendlylink.oTable = $('#portalFriendlylinkTable').dataTable( {
			"iDisplayLength": portalFriendlylink.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/portalFriendlylink/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": portalFriendlylink.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				portalFriendlylink.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3,4,5,6]}
	        ]
		} );
	} else {
		portalFriendlylink.oTable.fnDraw();
	}
};

//删除对象
portalFriendlylink.deletePortalFriendlylink = function (id) {
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
			portalFriendlylink.deleteCallBack(ids,idsArr);
		}
	});
};

//删除用户回调方法
portalFriendlylink.deleteCallBack = function(ids,idsArr) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/portalFriendlylink/deleteByIds.action",
		data : {"ids": ids},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					type:"success",
					content: data.successMessage,
					callback:function(){
						portalFriendlylink.portalFriendlylinkList();
						pagingDataDeleteAllForGoBack("portalFriendlylinkTable",idsArr);
					}
				});
			} else {
				msgBox.info({
					content: data.errorMessage
				});
			}
		}
	});
};

	
	
 //加载添加DIV
portalFriendlylink.loadModule = function (){
	if($.trim($("#formModuleDiv").html()).length == 0){
		$("#formModuleDiv").load(getRootPath()+"/sys/portalFriendlylink/loadForm.action",null,function(){
			portalFriendlylinkModule.show();
		});
	}
	else{
		portalFriendlylinkModule.show();
	}
};
		
//加载修改div
portalFriendlylink.loadModuleForUpdate = function (id){
	if($.trim($("#formModuleDiv").html()).length == 0){
		$("#formModuleDiv").load(getRootPath()+"/sys/portalFriendlylink/loadForm.action",null,function(){
			portalFriendlylinkModule.get(id);
		});
	}
	else{
		portalFriendlylinkModule.get(id);
	}
};

portalFriendlylink.queryReset = function(){
	$('#portalFriendlylinkQueryForm')[0].reset();
};
	
portalFriendlylink.selportlets = function(portalid){
	$("#portalFriendlylinkQueryForm #portletId").find("option").remove();
	
	$("#portalFriendlylinkQueryForm #portletId").append("<option value='0'>-全部-</option>");
	
	$("#portalFriendlylinkQueryForm #tempportlet").find("option").each(function(){
		if($(this).attr("name").indexOf(portalid) != -1){
			$("#portalFriendlylinkQueryForm #portletId").append($(this).clone());
		}
	});
};	
		  
