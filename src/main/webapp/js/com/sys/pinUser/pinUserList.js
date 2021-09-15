var pinUserList = {};
pinUserList.oTable = null;

pinUserList.oTableFnServerParams = function(aoData){
	getTableParameters(pinUserList.oTable, aoData);
	var userName = $("#userName").val();
	if(userName) { aoData.push({ "name": "userName", "value": userName}); }
};

pinUserList.pinUserList = function () {
	if (pinUserList.oTable == null) {
		pinUserList.oTable = $('#pinUserTable').dataTable( {
			"iDisplayLength": pinUserList.pageCount,
			"sAjaxSource": getRootPath() + "/sys/pinUser/manageList.action",
			"fnServerData": oTableRetrieveData,
			"aoColumns": [
				{'mData':'userName', sTitle: '用户名称', bSortable: false},
				{'mData':'userInitials', sTitle: '拼音首字母', bSortable: false},
				{'mData':'userAbbreviate', sTitle: '首字母缩写', bSortable: false},
				{'mData':'userFull', sTitle: '拼音全拼', bSortable: false},
				{mData: function(source) {
					return "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"pinUserList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">" + finalParamEditText + "</a>";
				}, sTitle: '操作', bSortable: false, sWidth: 200}
			],
			"fnServerParams": function ( aoData ) {
				pinUserList.oTableFnServerParams(aoData);
			},
			aaSorting:[],
			aoColumnDefs: []
		} );
	} else {
		pinUserList.oTable.fnDraw();
	}
};

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
		msgBox.info({ content: $.i18n.prop("JC_SYS_061") });
		return;
	}
	msgBox.confirm({
		content: $.i18n.prop("JC_SYS_034"),
		success: function(){
			pinUserList.deleteCallBack(ids);
		}
	});
};

pinUserList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST", data : {"ids": ids}, dataType : "json",
		url : getRootPath() + "/sys/pinUser/deleteByIds.action",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({ type:"success", content: data.successMessage });
			} else {
				msgBox.info({ content: data.errorMessage });
			}
			pinUserList.pinUserList();
		}
	});
};

pinUserList.infoLoad = function() {
	$.ajax({
		type : "POST", dataType : "json",
		url : getRootPath() + "/sys/pinUser/infoLoading.action",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({ type:"success", content: data.successMessage });
			} else {
				msgBox.info({ content: data.errorMessage });
			}
			pinUserList.pinUserList();
		}
	});
}

pinUserList.queryReset = function(){
	$('#pinUserQueryForm')[0].reset();
};

pinUserList.loadModule = function (){
	if ($.trim($("#pinUserModuleDiv").html()).length == 0) {
		$("#pinUserModuleDiv").load(getRootPath() + "/sys/pinUser/loadForm.action", null, function() {
			pinUserModule.show();
		});
	} else {
		pinUserModule.show();
	}
};

pinUserList.loadModuleForUpdate = function (id){
	if ($.trim($("#pinUserModuleDiv").html()).length == 0) {
		$("#pinUserModuleDiv").load(getRootPath() + "/sys/pinUser/loadForm.action", null, function() {
			pinUserModule.get(id);
		});
	} else {
		pinUserModule.get(id);
	}
};

$(document).ready(function(){
	pinUserList.pageCount = TabNub>0 ? TabNub : 10;
	pinUserList.pinUserList();
	$("#showAddDiv_t").click(pinUserList.loadModule);
	$("#queryPinUser").click(pinUserList.pinUserList);
	$("#queryReset").click(pinUserList.queryReset);
	$("#info_loading").click(pinUserList.infoLoad);
});