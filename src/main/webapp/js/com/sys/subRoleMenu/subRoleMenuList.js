var subRoleMenuList = {};

//分页处理 start
//分页对象
subRoleMenuList.oTable = null;

subRoleMenuList.oTableAoColumns = [
	{mData: function(source) {
			return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";
		}
	},
	{'mData':'roleId' },
	{'mData':'menuId' },
	{'mData':'menuName' },
	{'mData':'weight' },
	{'mData':'secret' },
	{mData: function(source) {
		return oTableSetButtones(source);
	}}
];

subRoleMenuList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(subRoleMenuList.oTable, aoData);
	
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
subRoleMenuList.subRoleMenuList = function () {
	if (subRoleMenuList.oTable == null) {
		subRoleMenuList.oTable = $('#subRoleMenuTable').dataTable( {
			"iDisplayLength": subRoleMenuList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/subRoleMenu/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": subRoleMenuList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				subRoleMenuList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,6]}
	        ]
		} );
	} else {
		subRoleMenuList.oTable.fnDraw();
	}
};

//删除对象
subRoleMenuList.deleteSubRoleMenu = function (id) {
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
			subRoleMenuList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
subRoleMenuList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/subRoleMenu/deleteByIds.action",
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
			subRoleMenuList.subRoleMenuList();
		}
	});
};

subRoleMenuList.queryReset = function(){
	$('#subRoleMenuQueryForm')[0].reset();
};
	
 //加载添加DIV
subRoleMenuList.loadModule = function (){
	if($.trim($("#subRoleMenuModuleDiv").html()).length == 0){
		$("#subRoleMenuModuleDiv").load(getRootPath()+"/sys/subRoleMenu/loadForm.action",null,function(){
			subRoleMenuModule.show();
		});
	}else{
		subRoleMenuModule.show();
	}
};
		
//加载修改div
subRoleMenuList.loadModuleForUpdate = function (id){
	if($.trim($("#subRoleMenuModuleDiv").html()).length == 0){
		$("#subRoleMenuModuleDiv").load(getRootPath()+"/sys/subRoleMenu/loadForm.action",null,function(){
			subRoleMenuModule.get(id);
		});
	}else{
		subRoleMenuModule.get(id);
	}
};

$(document).ready(function(){

  ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	
	//计算分页显示条数
	subRoleMenuList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	subRoleMenuList.subRoleMenuList();
	$("#addSubRoleMenuButton").click(subRoleMenuList.loadModule);
	$("#showAddDiv_t").click(subRoleMenuList.loadModule);
	$("#querySubRoleMenu").click(subRoleMenuList.subRoleMenuList);
	$("#queryReset").click(subRoleMenuList.queryReset);
	$("#deleteSubRoleMenus").click("click", function(){subRoleMenuList.deleteSubRoleMenu(0);});
});