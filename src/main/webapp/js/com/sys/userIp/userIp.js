var userIp = {}, pageCount=10;
userIp.subState = false;
userIp.oTable = null;
userIp.userTree = null;
userIp.oTableAoColumns = [
	{mData: function(source) {
			return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";
		}
	},
	{ "mData": "userId", mRender : function(mData, type, full) {
		return full.userName;
	}},
	{ "mData": "deptName" },
	{ "mData": "userStartIp", mRender : function(mData, type, full){
		if(full.userEndIp != null && full.userEndIp != '' && full.userEndIp != 'null')
			return full.userStartIp+"-"+full.userEndIp;
		else 
			return full.userStartIp;
	}},
	{ "mData": "modifyDate" },
	//设置权限按钮
	{mData: function(source) {
		return oTableSetButtonsUserIp(source);
	}}
];

userIp.oTableFnServerParams = function(aoData){
	getTableParameters(userIp.oTable, aoData);
	var userId = $("#userIpListForm #userSel").val();
	if (userId !=null && userId != "") {
		aoData.push({ "name": "userId", "value": userId});
	}
};

userIp.userIpList = function () {
	if (userIp.oTable == null) {
		userIp.oTable = $('#userIpTable').dataTable( {
			"iDisplayLength": userIp.pageCount,
			"sAjaxSource": getRootPath() + "/sys/userIp/manageList.action",
			"fnServerData": oTableRetrieveData,
			"aoColumns": [
				{mData: function(source) { return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">"; }, sTitle: '<input type="checkbox" />', sWidth: 46, bSortable: false},
				{mData: "userId", sTitle: '绑定用户', bSortable: false, mRender : function(mData, type, full) { return full.userName; }},
				{mData: "deptName", sTitle: '用户组织', bSortable: false},
				{mData: "userStartIp", sTitle: '绑定IP', bSortable: false, mRender : function(mData, type, full) {
					if (full.userEndIp != null && full.userEndIp != '' && full.userEndIp != 'null') {
						return full.userStartIp + "-" + full.userEndIp;
					} else {
						return full.userStartIp;
					}
				}},
				{mData: "modifyDate", sTitle: '修改时间', bSortable: false},
				{mData: function(source) {
					var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"userIp.loadUpdateHtml('"+ source.id+ "')\" role=\"button\" data-toggle=\"modal\">" + finalParamEditText + "</a>";
					var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"userIp.deleteuserIp('"+ source.id+ "')\">" + finalParamDeleteText + "</a>";
					return edit + del ;
				}, sTitle: '操作', bSortable: false, sWidth: 130}
			],
			"fnServerParams": function ( aoData ) {
				userIp.oTableFnServerParams(aoData);
			},
			aaSorting: [],
			aoColumnDefs: []
		} );
	} else {
		userIp.oTable.fnDraw();
	}
};
	
userIp.clearForm = function(form){
	$(':input', form).each(function() {
		var type = this.type;
		var tag = this.tagName.toLowerCase(); // normalize case
		if (type == 'text' || type == 'password' || tag == 'textarea') {
			this.value = "";
		}
	});
};

userIp.changetype = function(){
	var val=$('input:radio[name="bindType"]:checked').val();
	if (val == 2) {
		$("#ipz").hide();
		$("#userIpDiv").show();
	} else {
		$("#ipz").show();
		$("#userIpDiv").hide();
	}
};

userIp.loadAddHtml = function (){
	if ($.trim($("#userIpEdit").html()) == "") {
		$("#userIpEdit").load(getRootPath() + "/sys/userIp/userIpEdit.action", null, function() {
			userIp.createuserIp();
		});
	} else {
		userIp.createuserIp();
	}
};

userIp.createuserIp = function(){
	hideErrorMessage();
	$("#id").attr("value","");
	userIp.clearForm(userIpForm);
	initEditFomrTree();
	$("input[name=bindType]").get(0).checked = true;
	$("#titleuserIp").html("新增");
	userIp.changetype();
	$('#myModal-edit').modal('show');
	ie8StylePatch();
};

userIp.loadUpdateHtml = function (id) {
	if ($.trim($("#userIpEdit").html()) == "") {
		$("#userIpEdit").load(getRootPath() + "/sys/userIp/userIpEdit.action", null, function() {
			userIp.get(id);
		});
	} else {
		userIp.get(id);
	}
};

function initEditFomrTree(data){
	userIpEdit.userEditTree.clearValue();
	if(data) userIpEdit.userEditTree.setData(data);
}

userIp.get = function(id){
	hideErrorMessage();
	jQuery.ajax({
		url: getRootPath() + "/sys/userIp/get.action?id=" + id + "&time=" + (+new Date().getTime()),
		type: 'post', dataType: 'json',
		success: function(data, textStatus, xhr) {
			userIp.clearForm(userIpForm);
			$("#userIpForm #userId").val(data.userId);
			$("#userIpForm").fill(data);
			if (data.userId && data.userName) initEditFomrTree({id:data.userId,text:data.userName});
			if (data.bindType == 1) {
				$("input[name=bindType]").get(0).checked = true;
			} else {
				$("input[name=bindType]").get(1).checked = true;
			}
			$("#titleuserIp").html("编辑");
			userIp.changetype();
			$('#myModal-edit').modal('show');
			ie8StylePatch();
		},
		error:function() {
			msgBox.tip({content: "加载数据错误", type:'fail'});
		}
	});
};

userIp.deleteuserIp = function (id) {
	var ids = String(id);
	var idsArr = [];
	if (id == 0) {
		$("[name='ids']:checked").each(function() {
			idsArr.push($(this).val());
		});
		ids = idsArr.join(",");
	}
	if (ids.length == 0) {
		msgBox.info({content: "请选择要删除的绑定IP", type:'fail'});
		return;
	}
	confirmx($.i18n.prop("JC_SYS_034"),function(){userIp.deleteCallBack(ids,idsArr);});
};

userIp.deleteCallBack = function(ids,idsArr) {
	$.ajax({
		type : "POST", data : {"ids": ids}, dataType : "json",
		url : getRootPath() + "/sys/userIp/deleteByIds.action?time=" + (+new Date().getTime()),
		success : function(data) {
			if (data > 0) {
				msgBox.tip({
					content: "删除成功",
					type:'success',
					callback:function(){
						userIp.userIpList();
						pagingDataDeleteAllForGoBack("userIpTable",idsArr);
					}
				});
			}
		}
	});
};

userIp.queryReset = function(){
	$('#userIpListForm')[0].reset();
	if(userIp.userTree) userIp.userTree.clearValue();
};

jQuery(function($) {
	$.ajaxSetup ({
		cache: false //设置成false将不会从浏览器缓存读取信息
	});
	userIp.pageCount = TabNub>0 ? TabNub : 10;
	$("#queryuserIp").click(userIp.userIpList);
	$("#queryReset").click(userIp.queryReset);
	$("#userIpBottom").click(userIp.loadAddHtml);
	$("#deleteuserIps").click("click", function(){userIp.deleteuserIp(0);});
	userIp.userIpList();
	userIp.userTree = new JCFF.JCTree.Lazy({
		container: 'userTree',
		controlId: 'userSel-userId',
		single: true,
        funFormData: function(){
            return {
                weight : '0'
            }
        }
	});
	$("#userIpEdit").load(getRootPath() + "/sys/userIp/userIpEdit.action");
});	


