var subsystemList = {};
subsystemList.oTable = null;

subsystemList.oTableFnServerParams = function(aoData){
	getTableParameters(subsystemList.oTable, aoData);
};

subsystemList.subsystemList = function () {
	if (subsystemList.oTable == null) {
		subsystemList.oTable = $('#subsystemTable').dataTable( {
			"iDisplayLength": subsystemList.pageCount,
			"sAjaxSource": getRootPath() + "/sys/subsystem/manageList.action",
			"fnServerData": oTableRetrieveData,
			"aoColumns": [
				{mData: function(source) {return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";}, sTitle: '<input type="checkbox" />', bSortable: false, sWidth: 45},
				{mData: 'name', sTitle: '子系统名称', bSortable: false},
				{mData: 'url', sTitle: '子系统地址', bSortable: false},
				{mData: 'queue', sTitle: '排序', bSortable: false},
				{mData: 'openType', sTitle: '打开方式', bSortable: false, mRender : function(mData, type, full){
					var result = "";
					if (full.openType=="1") result = "内部打开";
					else if (full.openType=="2") result = "弹出打开";
					return result;
				}},
				{mData:'createDate', sTitle: '创建时间', bSortable: false},
				{mData: function(source) {
					var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"subsystemList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
					var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"subsystemList.deleteSubsystem('"+ source.id+ "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
					return edit + del;
				}, sTitle: '操作', bSortable: false, sWidth: 170}
			],
			"fnServerParams": function ( aoData ) {
				subsystemList.oTableFnServerParams(aoData);
			},
			aaSorting:[],
			aoColumnDefs: []
		});
	} else {
		subsystemList.oTable.fnDraw();
	}
};

subsystemList.deleteSubsystem = function (id) {
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
			subsystemList.deleteCallBack(ids);
		}
	});
};

subsystemList.deleteCallBack = function(ids) {
	$.ajax({
		type: "POST", url: getRootPath() + "/sys/subsystem/deleteByIds.action", data: {"ids": ids}, dataType: "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({ type:"success", content: data.successMessage });
			} else {
				msgBox.info({ content: data.errorMessage });
			}
			subsystemList.subsystemList();
		}
	});
};

subsystemList.queryReset = function(){
	$('#subsystemQueryForm')[0].reset();
};

subsystemList.loadModule = function (){
	if ($.trim($("#subsystemModuleDiv").html()).length == 0) {
		$("#subsystemModuleDiv").load(getRootPath() + "/sys/subsystem/loadForm.action", null, function() {
			subsystemModule.show();
		});
	} else {
		subsystemModule.show();
	}
};

subsystemList.loadModuleForUpdate = function (id){
	if ($.trim($("#subsystemModuleDiv").html()).length == 0) {
		$("#subsystemModuleDiv").load(getRootPath() + "/sys/subsystem/loadForm.action", null, function() {
			subsystemModule.get(id);
		});
	} else {
		subsystemModule.get(id);
	}
};

$(document).ready(function(){
	subsystemList.pageCount = TabNub > 0 ? TabNub : 10;
	subsystemList.subsystemList();
	$("#addSubsystemButton").click(subsystemList.loadModule);
	$("#deleteSubsystems").click("click", function(){subsystemList.deleteSubsystem(0);});
});