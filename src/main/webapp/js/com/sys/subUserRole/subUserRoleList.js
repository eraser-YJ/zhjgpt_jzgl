var subUserRoleList = {};

//分页处理 start
//分页对象
subUserRoleList.oTable = null;

subUserRoleList.oTableAoColumns = [
	{mData: function(source) {
			return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";
		}
	},
	{'mData':'userId' },
	{'mData':'roleId' },
	{'mData':'roleName' },
	{'mData':'weight' },
	{'mData':'secret' },
	{mData: function(source) {
		return oTableSetButtones(source);
	}}
];

subUserRoleList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(subUserRoleList.oTable, aoData);
	
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
subUserRoleList.subUserRoleList = function () {
	if (subUserRoleList.oTable == null) {
		subUserRoleList.oTable = $('#subUserRoleTable').dataTable( {
			"iDisplayLength": subUserRoleList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/subUserRole/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": subUserRoleList.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				subUserRoleList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,6]}
	        ]
		} );
	} else {
		subUserRoleList.oTable.fnDraw();
	}
};

//删除对象
subUserRoleList.deleteSubUserRole = function (id) {
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
			subUserRoleList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
subUserRoleList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/subUserRole/deleteByIds.action",
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
			subUserRoleList.subUserRoleList();
		}
	});
};

subUserRoleList.queryReset = function(){
	$('#subUserRoleQueryForm')[0].reset();
};
	
 //加载添加DIV
subUserRoleList.loadModule = function (){
	if($.trim($("#subUserRoleModuleDiv").html()).length == 0){
		$("#subUserRoleModuleDiv").load(getRootPath()+"/sys/subUserRole/loadForm.action",null,function(){
			subUserRoleModule.show();
		});
	}else{
		subUserRoleModule.show();
	}
};
		
//加载修改div
subUserRoleList.loadModuleForUpdate = function (id){
	if($.trim($("#subUserRoleModuleDiv").html()).length == 0){
		$("#subUserRoleModuleDiv").load(getRootPath()+"/sys/subUserRole/loadForm.action",null,function(){
			subUserRoleModule.get(id);
		});
	}else{
		subUserRoleModule.get(id);
	}
};

$(document).ready(function(){

  ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	
	//计算分页显示条数
	subUserRoleList.pageCount = TabNub>0 ? TabNub : 10;
	
	
	//初始化列表方法
	subUserRoleList.subUserRoleList();
	$("#addSubUserRoleButton").click(subUserRoleList.loadModule);
	$("#showAddDiv_t").click(subUserRoleList.loadModule);
	$("#querySubUserRole").click(subUserRoleList.subUserRoleList);
	$("#queryReset").click(subUserRoleList.queryReset);
	$("#deleteSubUserRoles").click("click", function(){subUserRoleList.deleteSubUserRole(0);});
});