var subRoleGroupMenuList = {};

//分页处理 start
//分页对象
subRoleGroupMenuList.oTable = null;

subRoleGroupMenuList.oTableAoColumns = [
	{mData: function(source) {
			return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";
		}
	},
	{'mData':'roleGroupId' },
	{'mData':'menuId' },
	{'mData':'weight' },
	{'mData':'secret' },
	{mData: function(source) {
		return oTableSetButtones(source);
	}}
];

subRoleGroupMenuList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(subRoleGroupMenuList.oTable, aoData);
	
	var createDateBegin = $("#createDateBegin").val();
if(createDateBegin.length > 0){
	aoData.push({ "name": "createDateBegin", "value": createDateBegin});
}
var createDateEnd = $("#createDateEnd").val();
if(createDateEnd.length > 0){
	aoData.push({ "name": "createDateEnd", "value": createDateEnd});
}

};

//分页查询
subRoleGroupMenuList.subRoleGroupMenuList = function () {
	if (subRoleGroupMenuList.oTable == null) {
		subRoleGroupMenuList.oTable = $('#subRoleGroupMenuTable').dataTable( {
			"iDisplayLength": subRoleGroupMenuList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/subRoleGroupMenu/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": subRoleGroupMenuList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				subRoleGroupMenuList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,5]}
	        ]
		} );
	} else {
		subRoleGroupMenuList.oTable.fnDraw();
	}
};

//删除对象
subRoleGroupMenuList.deleteSubRoleGroupMenu = function (id) {
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
			subRoleGroupMenuList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
subRoleGroupMenuList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/subRoleGroupMenu/deleteByIds.action",
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
			subRoleGroupMenuList.subRoleGroupMenuList();
		}
	});
};

subRoleGroupMenuList.queryReset = function(){
	$('#subRoleGroupMenuQueryForm')[0].reset();
};
	
 //加载添加DIV
subRoleGroupMenuList.loadModule = function (){
	if($.trim($("#subRoleGroupMenuModuleDiv").html()).length == 0){
		$("#subRoleGroupMenuModuleDiv").load(getRootPath()+"/sys/subRoleGroupMenu/loadForm.action",null,function(){
			subRoleGroupMenuModule.show();
		});
	}else{
		subRoleGroupMenuModule.show();
	}
};
		
//加载修改div
subRoleGroupMenuList.loadModuleForUpdate = function (id){
	if($.trim($("#subRoleGroupMenuModuleDiv").html()).length == 0){
		$("#subRoleGroupMenuModuleDiv").load(getRootPath()+"/sys/subRoleGroupMenu/loadForm.action",null,function(){
			subRoleGroupMenuModule.get(id);
		});
	}else{
		subRoleGroupMenuModule.get(id);
	}
};

$(document).ready(function(){

  ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	
	//计算分页显示条数
	subRoleGroupMenuList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	subRoleGroupMenuList.subRoleGroupMenuList();
	$("#addSubRoleGroupMenuButton").click(subRoleGroupMenuList.loadModule);
	$("#showAddDiv_t").click(subRoleGroupMenuList.loadModule);
	$("#querySubRoleGroupMenu").click(subRoleGroupMenuList.subRoleGroupMenuList);
	$("#queryReset").click(subRoleGroupMenuList.queryReset);
	$("#deleteSubRoleGroupMenus").click("click", function(){subRoleGroupMenuList.deleteSubRoleGroupMenu(0);});
});