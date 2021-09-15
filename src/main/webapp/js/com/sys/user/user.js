var user = {};
user.oTable = null;
user.pageRows = null;

/**
 * 初始化密码
 * @param id
 */
user.initPassword = function(id) {
	confirmx($.i18n.prop("JC_SYS_093"), function(){
		jQuery.ajax({
			url: getRootPath() + "/sys/user/initPassword.action", type: 'POST', data: {"id": id},
			success: function(data) {
				if (data.success == "true") {
					msgBox.tip({ type:"success", content: data.successMessage });
				} else {
					msgBox.tip({ content: data.errorMessage });
				}
			},
			error: function() {
				msgBox.tip({ content: $.i18n.prop("JC_SYS_091") });
			}
		});
	});
};

/**
 * 删除用户
 * @param id
 */
user.deleteUser = function (id) {
	var ids = String(id);
	if (id == 0) {
		var idsArr = [];
		$("[name='ids']:checked").each(function() { idsArr.push($(this).val()); });
		ids = idsArr.join(",");
		if (ids.length == 0) {
			msgBox.info({ content: $.i18n.prop("JC_SYS_061") });
			return;
		}
		var statusArr = [];
		$("[name='delete_status']:hidden").each(function() { if (idsArr.indexOf($(this).val()) > -1) { statusArr.push($(this).val()); } });
		if(statusArr.length==0){
			msgBox.info({ content: '没有需要删除的数据' });
			return;
		}
	}
	msgBox.confirm({
		content: $.i18n.prop("JC_SYS_034"),
		success: function(){
			user.deleteCallBack(ids);
		}
	});
};

/**
 * 删除用户回调方法
 * @param ids
 */
user.deleteCallBack = function(ids) {
	$.ajax({
		type: "POST", url: getRootPath() + "/sys/user/deleteByIds.action", data: {"ids": ids}, dataType: "json",
		success: function(data) {
			if (data.success == "true") {
				msgBox.tip({ type:"success", content: data.successMessage });
			} else {
				msgBox.info({ content: data.errorMessage });
			}
			user.userList();
		}
	});
};

/**
 * 分页查询用户
 */
user.userList = function () {
	$('#IP-edit').fadeIn();

	user.oTableAoColumns = [
	    {mData: function(source) { return (!source.isSystemAdmin && !source.isSystemSecurity && !source.isSystemAudit && !source.isSystemManager) ? "<input type='checkbox' name='ids' value='" + source.id + "'>" : null; }, bSortable: false},
		{mData: "displayName", bSortable: false},
		{mData: "sex", bSortable: false, mRender: function(mData, type, full) { return full.sexValue; } },
		{mData: "deptId", bSortable: false, mRender: function(mData, type, full) { return full.deptName; } },
        {mData: "extStr1", bSortable: false, mRender: function(mData, type, full) { return full.extStr1Value; } },
		{mData: "status", bSortable: false, mRender: function(mData, type, full) { return full.statusValue; } },
		{mData: function(source) {
			return userOTableSetButtons(source);
		}}
	];

	user.oTableFnServerParams = function(aoData){
		getTableParameters(user.oTable, aoData);
		var deptIds = $("#userListForm #deptId").val();
		var sex = $("#userListForm #sex").val();
		var status = $("#userListForm #status").val();
		var displayName = $("#userListForm #displayName").val();
		if (deptIds.length > 0) { aoData.push({ "name": "deptIdToCode", "value": deptIds}); }
		if (sex !=null && sex != "") { aoData.push({ "name": "sex", "value": sex}); }
		if (status !=null && status != "") { aoData.push({ "name": "status", "value": status}); }
		if (displayName != "") { aoData.push({ "name": "displayName", "value": displayName}); }
	};

	user.oTableRetrieveData = function ( sSource, aoData, fnCallback ) {
	    $.ajax({
			type: "GET", url: sSource, data: aoData, dataType: "json",
			success: function(resp) {
				fnCallback(resp);
				$("i[data-toggle='tooltip']").tooltip();
				treeHightReset("treeDemo");
				ie8TableStyle();
			}
		});
	};

	if (user.oTable == null) {
		user.oTable = $('#userTable').dataTable({
			iDisplayLength: user.pageRows,
			sAjaxSource: getRootPath() + "/sys/user/manageList.action",
			fnServerData: user.oTableRetrieveData,
			aoColumns: user.oTableAoColumns,
			fnServerParams: function ( aoData ) { user.oTableFnServerParams(aoData); },
			aaSorting: [],
	        aoColumnDefs: [],
		});
	} else {
		user.oTable.fnDraw();
	}
};

user.queryReset = function(){
	$('#status').val('');
	$('#sex').val('');
	$('#status').val('status_0');
	$('#displayName').val('');
	if(user.usertree){
		var node = user.usertree.getRootNode();
		user.usertree.getZTreeObj().selectNode(node);
		$("#userListForm #deptIds").val(user.usertree.getChildNodesId(node));
		$("#userListForm #deptId").val(node.id);
		$("#userListForm #deptName").val(node.name);
	}
};

user.loadAddHtml = function (){
    $('#showAddDiv').attr('disabled', "true");
	if ($.trim($("#userEdit").html()).length == 0) {
		$("#userEdit").load(getRootPath() + "/sys/user/userEdit.action", null, function() {
			userEdit.showAddDiv();
		});
	} else{
		userEdit.showAddDiv();
	}
    setTimeout(function () { $('#showAddDiv').removeAttr("disabled");}, 3000);
};

user.loadUpdateHtml = function (id){
	if ($.trim($("#userEdit").html()).length == 0) {
		$("#userEdit").load(getRootPath() + "/sys/user/userEdit.action", null, function() {
			userEdit.get(id);
		});
	} else {
		userEdit.get(id);
	}
};

/**
 * 恢复删除用户
 * @param id
 */
user.deleteBackUsers = function (id) {
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
				success: function() {
					user.deleteBackCallBack(ids);
				}
			});
		}else if(idsArr.length>statusArr.length){
			msgBox.confirm({
				content: '有' + (idsArr.length - statusArr.length) + '条记录被忽略，是否继续恢复数据？',
				success: function() {
					user.deleteBackCallBack(statusArr.join(","));
				}
			});
		}
	} else {
		msgBox.confirm({
			content: '是否恢复数据？',
			success: function(){
				user.deleteBackCallBack(ids);
			}
		});
	}
};

/**
 * 恢复删除用户回调方法
 * @param ids
 */
user.deleteBackCallBack = function(ids) {
	$.ajax({
		type: "POST", url: getRootPath() + "/sys/user/deleteBackByIds.action", data: {"ids": ids}, dataType: "json",
		success: function(data) {
			if (data.success == "true") {
				msgBox.tip({ type:"success", content: data.successMessage });
			} else {
				msgBox.info({ content: data.errorMessage });
			}
			user.userList();
		}
	});
};

//初始化
jQuery(function($) {
	user.pageRows =  10;
	$("#queryUser").click(user.userList);
	$("#showAddDiv").click(user.loadAddHtml);
	$("#deleteUsers").click("click", function(){user.deleteUser(0);});
	$("#deleteBackUsers").click("click", function(){user.deleteBackUsers(0);});
	$("#queryReset").click(user.queryReset);
	user.usertree = JCTree.ztree({
		container : 'treeDemo',
		expand: true,
		rootNode: true,
		onClick: function(jObj,id,tree){
			tree.checkState = true;
			$("#userListForm #deptIds").val(user.usertree.getChildNodesId(tree));
			$("#userListForm #deptId").val(tree.id);
			$("#userListForm #deptName").val(tree.name);
            $("#userListForm #deptWeight").val(tree.weight);
			user.userList();
	    }
	});
});
