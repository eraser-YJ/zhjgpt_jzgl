var userRecycleList = {};
userRecycleList.oTable = null;

userRecycleList.oTableFnServerParams = function(aoData){
	getTableParameters(userRecycleList.oTable, aoData);
	var createDateBegin = $("#createDateBegin").val();
	if(createDateBegin.length > 0){ aoData.push({ "name": "createDateBegin", "value": createDateBegin}); }
	var createDateEnd = $("#createDateEnd").val();
	if(createDateEnd.length > 0){ aoData.push({ "name": "createDateEnd", "value": createDateEnd}); }
};

userRecycleList.userRecycleList = function () {
	if (userRecycleList.oTable == null) {
		userRecycleList.oTable = $('#userRecycleTable').dataTable( {
			"iDisplayLength": userRecycleList.pageCount,
			"sAjaxSource": getRootPath() + "/sys/userRecycle/manageList.action",
			"fnServerData": oTableRetrieveData,
			"aoColumns": [
				{mData: function(source) { return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">"; }, sTitle: '<input type="checkbox" />', sWidth: 45, bSortable: false},
				{mData:'code', sTitle: '用户编号', bSortable: false},
				{mData:'displayName', sTitle: '姓名', bSortable: false},
				{mData: "deptId", sTitle: '用户部门', bSortable: false, mRender : function(mData, type, full) { return full.deptName; } },
				{mData:'createDate', sTitle: '被删除日期', bSortable: false},
				{mData: function(source) {
					var deleteBack = '<a class="a-icon i-new" href="#" onclick="userRecycleList.deleteBackUsers(\''+ source.id +'\')">恢复数据</a>'+"<input type=\"hidden\" name=\"status\" value="+ source.id + " >";
					var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"userRecycleList.deleteUserRecycle('"+ source.id+ "')\">" + finalParamDeleteText + "</a>";
					return deleteBack + del;
				}, sTitle: '操作', bSortable: false}
			],
			"fnServerParams": function ( aoData ) {
				userRecycleList.oTableFnServerParams(aoData);
			},
			aaSorting: [],
			aoColumnDefs: []
		} );
	} else {
		userRecycleList.oTable.fnDraw();
	}
};

userRecycleList.deleteUserRecycle = function (id) {
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
			userRecycleList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
userRecycleList.deleteCallBack = function(ids) {
	$.ajax({
		type: "POST", data: {"ids": ids}, dataType: "json",
		url: getRootPath() + "/sys/userRecycle/deleteByIds.action",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({ type:"success", content: data.successMessage });
			} else {
				msgBox.info({ content: data.errorMessage });
			}
			userRecycleList.userRecycleList();
		}
	});
};

userRecycleList.queryReset = function(){
	$('#userRecycleQueryForm')[0].reset();
};

userRecycleList.deleteBackUsers = function (id) {
	var ids = String(id);
	if (id == 0) {
		var idsArr = [];
		$("[name='ids']:checked").each(function() {
			idsArr.push($(this).val());
		});
		ids = idsArr.join(",");
		var statusArr = [];
		$("[name='status']:hidden").each(function() {
			if(idsArr.indexOf($(this).val())>-1){
				statusArr.push($(this).val());
			}
		});
		if (ids.length == 0) {
			msgBox.info({ content: '请选择需要恢复的数据' });
			return;
		}else if(statusArr.length==0){
			msgBox.info({ content: '没有需要恢复的数据' });
			return;
		}else if(idsArr.length==statusArr.length){
			msgBox.confirm({
				content: '是否恢复数据？',
				success: function(){
					userRecycleList.deleteBackCallBack(ids);
				}
			});
		}else if(idsArr.length>statusArr.length){
			msgBox.confirm({
				content: '有'+(idsArr.length-statusArr.length)+'条记录被忽略，是否继续恢复数据？',
				success: function(){
					userRecycleList.deleteBackCallBack(statusArr.join(","));
				}
			});
		}
	}else{
		msgBox.confirm({
			content: '是否恢复数据？',
			success: function(){
				userRecycleList.deleteBackCallBack(ids);
			}
		});
	}

};

userRecycleList.deleteBackCallBack = function(ids) {
	$.ajax({
		type: "POST", data: {"ids": ids}, dataType : "json",
		url: getRootPath() + "/sys/userRecycle/deleteBackByIds.action",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({ type:"success", content: data.successMessage });
			} else {
				msgBox.info({ content: data.errorMessage });
			}
			userRecycleList.userRecycleList();
		}
	});
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	userRecycleList.pageCount = TabNub>0 ? TabNub : 10;
	userRecycleList.userRecycleList();
	$("#queryUserRecycle").click(userRecycleList.userRecycleList);
	$("#queryReset").click(userRecycleList.queryReset);
	$("#deleteUserRecycles").click("click", function(){userRecycleList.deleteUserRecycle(0);});
});