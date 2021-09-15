var pinDepartmentList = {};
pinDepartmentList.oTable = null;

pinDepartmentList.oTableFnServerParams = function(aoData){
	getTableParameters(pinDepartmentList.oTable, aoData);
	var deptName = $("#deptName").val();
	if(deptName){ aoData.push({ "name": "deptName", "value": deptName}); }
};

pinDepartmentList.pinDepartmentList = function () {
	if (pinDepartmentList.oTable == null) {
		pinDepartmentList.oTable = $('#pinDepartmentTable').dataTable( {
			"iDisplayLength": pinDepartmentList.pageCount,
			"sAjaxSource": getRootPath() + "/sys/pinDepartment/manageList.action",
			"fnServerData": oTableRetrieveData,
			"aoColumns": [
				{mData:'deptName', sTitle: '部门名称', bSortable: false},
				{mData:'deptInitials', sTitle: '拼音首字母', bSortable: false},
				{mData:'deptAbbreviate', sTitle: '首字母缩写', bSortable: false},
				{mData:'deptFull', sTitle: '拼音全拼', bSortable: false},
				{mData: function(source) {
					return "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"pinDepartmentList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalParamEditText + "</a>";
				}, sTitle: '操作', bSortable: false, sWidth: 100}
			],
			"fnServerParams": function ( aoData ) {
				pinDepartmentList.oTableFnServerParams(aoData);
			},
			aaSorting: [],
			aoColumnDefs: []
		} );
	} else {
		pinDepartmentList.oTable.fnDraw();
	}
};

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
		msgBox.info({ content: $.i18n.prop("JC_SYS_061") });
		return;
	}
	msgBox.confirm({
		content: $.i18n.prop("JC_SYS_034"),
		success: function(){
			pinDepartmentList.deleteCallBack(ids);
		}
	});
};

pinDepartmentList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST", data : {"ids": ids}, dataType : "json",
		url : getRootPath() + "/sys/pinDepartment/deleteByIds.action",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({ type:"success", content: data.successMessage });
			} else {
				msgBox.info({ content: data.errorMessage });
			}
			pinDepartmentList.pinDepartmentList();
		}
	});
};

pinDepartmentList.infoLoad = function() {
	$.ajax({
		type : "POST", url : getRootPath() + "/sys/pinDepartment/infoLoading.action", dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({ type:"success", content: data.successMessage });
			} else {
				msgBox.info({ content: data.errorMessage });
			}
			pinDepartmentList.pinDepartmentList();
		}
	});
};

pinDepartmentList.queryReset = function(){
	$('#pinDepartmentQueryForm')[0].reset();
};

pinDepartmentList.loadModule = function (){
	if ($.trim($("#pinDepartmentModuleDiv").html()).length == 0) {
		$("#pinDepartmentModuleDiv").load(getRootPath() + "/sys/pinDepartment/loadForm.action", null, function() {
			pinDepartmentModule.show();
		});
	} else {
		pinDepartmentModule.show();
	}
};

pinDepartmentList.loadModuleForUpdate = function (id){
	if ($.trim($("#pinDepartmentModuleDiv").html()).length == 0) {
		$("#pinDepartmentModuleDiv").load(getRootPath() + "/sys/pinDepartment/loadForm.action", null, function() {
			pinDepartmentModule.get(id);
		});
	} else {
		pinDepartmentModule.get(id);
	}
};

$(document).ready(function(){
	pinDepartmentList.pageCount = TabNub>0 ? TabNub : 10;
	pinDepartmentList.pinDepartmentList();
	$("#showAddDiv_t").click(pinDepartmentList.loadModule);
	$("#queryPinDepartment").click(pinDepartmentList.pinDepartmentList);
	$("#queryReset").click(pinDepartmentList.queryReset);
	$("#info_loading").click(pinDepartmentList.infoLoad);
});