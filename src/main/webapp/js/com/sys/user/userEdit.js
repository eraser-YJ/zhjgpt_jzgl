var userEdit = {};

userEdit.otherDeptCount = 1;
//重复提交标识
userEdit.subState = false;
//上传头像的user id标识
userEdit.userPhotoId = 0;

//显示添加用户弹出层
userEdit.showAddDiv = function (){
	userEdit.userPhotoId = 0;
	userEdit.clearForm();
	deptFullTree.init();
	userEdit.initRoleList();
	userEdit.removeOtherDept(true);
	$("#loginNameOld").val("");
	$("#photo").val("");

	var deptId = $('#userListForm #deptId').val();
	var deptName = $('#userListForm #deptName').val();
	var deptWeight = $("#userListForm #deptWeight").val();
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var node = treeObj.getNodeByParam("id", deptId);
	//如果是部门同步到添加页面
	if(node.deptType == 0){
		if(deptId != "" && deptName != ""){
			$('#userForm #deptId').val(deptId);
			$('#userForm #deptName').val(deptName);
            $('#userForm #deptWeight').val(deptWeight);
		}
	}
	$("#saveAndClose").attr("class","btn");
	$("#saveAndClose").html($.i18n.prop("JC_SYS_097"));
	$("#id").val(0);
	$("#userPhoto").attr("src", getRootPath()+"/images/demoimg/iphoto.jpg");
	$("#userForm #loginName").attr("readonly",false);

	$("#otherDiv").hide();
	$("#otherContentDIV").hide();
	$("input[type='radio'][name=isOtherDept][value='0']").prop("checked",true);

	userEdit.initTab();
	//getToken();
	userEdit.showCale();
	$("#actionTitle").html($.i18n.prop("JC_SYS_096"));
	$("#saveOrModify").show();

	$('#myModal-edit').modal('show');
	userEdit.getUserPhoto();
};
//获取修改信息
userEdit.get = function (id) {
	$('#id').val("");
	userEdit.getUserPhoto();
	userEdit.userPhotoId = id;
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/user/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				userEdit.userPhotoId = 0;
				userEdit.clearForm();
				userEdit.initForm();
				$("#userForm").fill(data);
				if(data.photo){
					$("#userPhoto").attr("src", getRootPath()+"/"+data.photo);
				} else {
					$("#userPhoto").attr("src", getRootPath()+"/images/demoimg/iphoto.jpg");
				}
				/*if(data.leaderId){
					userEdit.leader.setData(data.leaderId+'');
				}*/

				/*if(data.deptLeader){
					userEdit.deptLeader.setData(data.deptLeader+'');
				}*/
				/*if(data.chargeLeader){
					userEdit.chargeLeader.setData(data.chargeLeader+'');
					/!*$("#leaderName").html(leaderName == null ? "" : leaderName);//负责人*!/
				}*/

				//填充其他部门数据
				userEdit.removeOtherDept(true);
				if(data.otherDepts && data.otherDepts.length > 0) {
					//重新初始化其他部门ID
					userEdit.otherDeptCount = 1;
					for(var i=0;i<data.otherDepts.length;i++){
						userEdit.addOtherDept();
						var o = data.otherDepts[i];
						$("#otherDeptId-"+(i+1)).val(o.deptId);
						$("#otherDeptNo-"+(i+1)).val(o.queue);
						$("#otherDeptName-"+(i+1)).val(o.deptName);
						$("#otherDeptDuty-"+(i+1)).val(o.dutyId);
					}
					$("#otherDiv").show();
					$("#otherContentDIV").show();
					$("input[type='radio'][name=isOtherDept][value='1']").prop("checked",true);
				} else {
					$("#otherDiv").hide();
					$("#otherContentDIV").hide();
					$("input[type='radio'][name=isOtherDept][value='0']").prop("checked",true);
				}
				//填充权限
				userEdit.initRoleList();
				if(data.sysUserRole && data.sysUserRole.length > 0){
					$.each(data.sysUserRole, function(i, o) {
						$("#chooseList").append($("#roleList option[value='"+o.roleId+"']"));
					});
				}
				//管理员机构树
				deptFullTree.init();
				if(data.adminSide && data.adminSide.length > 0){
					$.each(data.adminSide, function(i, o) {
						deptFullTree.ids.push(o.deptId+","+o.parentId+","+o.isChecked);
					});
					$("#adminSideV").val(deptFullTree.ids.join(","));
				}
				userEdit.initTab();
				$("#actionTitle").html($.i18n.prop("JC_SYS_094"));
				$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
				$("#saveAndClose").html($.i18n.prop("JC_SYS_095"));
				$("#userForm #loginName").attr("readonly",true);
				$("#loginNameOld").val(data.loginName);
				$("#mobileOld").val(data.mobile);
				$("#statusOld").val(data.status);
				userEdit.showCale();
				$('#myModal-edit').modal('show');
				userEdit.getUserPhoto();
			}
		}
	});
};

userEdit.validateDeptName = function(){
	if($("#userForm #deptName").val() == ""){
		userEdit.subState = false;
		$("#deptError").html("此信息不能为空").show();
		return true;
	} else {
		$("#deptError").hide();
		return false;
	}
};

//添加修改公用方法
userEdit.saveOrModify = function (hide) {
	var dWeight = $("#userForm #deptWeight").val();
	var uWeight = $("#userForm #weight").val();
	if(parseInt(dWeight) < parseInt(uWeight)){
		msgBox.info({content:"个人权重不能大于部门权重", type:'fail'});
		return;
	}
	if(userEdit.subState)return;
	userEdit.subState = true;
	if ($("#userForm").valid()) {
		if(userEdit.validateDeptName()){
			return;
		}
		var url = getRootPath()+"/sys/user/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/sys/user/update.action";
		}
		var formData = $("#userForm").serializeArray();
		//添加其他表单信息
		userEdit.addFormParameters(formData);
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			contentType: "application/json;charset=UTF-8",//一对多关系必填否则去掉
			data : JSON.stringify(serializeJson(formData),replaceJsonNull),
			success : function(data) {
				userEdit.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if ($("#id").val() == 0) {
						var deptId = $("#userForm #deptId").val();
						var deptName = $("#userForm #deptName").val();
						userEdit.initForm();
						$("#userForm #deptId").val(deptId);
						$("#userForm #deptName").val(deptName);
					}
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						userEdit.showAddDiv();
//						return;
						//userEdit.userPhotoId = 0;
						//userEdit.clearForm();
						//userEdit.initTab();
					}
					//getToken();
//					console.log("ddd")
					$("#userForm #token").val(data.token);
					user.userList();
				} else {
					if(data.labelErrorMessage){
						showErrors("userForm", data.labelErrorMessage);
					} else{
						msgBox.info({
							content: data.errorMessage
						});
					}
					//getToken();
					$("#userForm #token").val(data.token);
				}
			},
			error : function() {
				userEdit.subState = false;
				msgBox.tip({
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		userEdit.subState = false;
		userEdit.validateDeptName();
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//添加部门、角色、机构表单数据
userEdit.addFormParameters = function (formData){
	//根据实际业务组装json对象
	var otherDepts = new Array(), deptIdArr = new Array(), deptDutyArr = new Array(), deptNoArr = new Array();
	//组织其他部门
	if($("input[name='isOtherDept']:checked").val() == 1){
		$.each($("[id^=otherDeptId-]"), function(i, o) {
			deptIdArr.push(o.value);
		});
		$.each($("[id^=otherDeptDuty-]"), function(i, o) {
			deptDutyArr.push(o.value);
		});
		$.each($("[id^=otherDeptNo-]"), function(i, o) {
			deptNoArr.push(o.value);
		});
		$.each(deptIdArr, function(i, o) {
			otherDepts.push({"deptId":deptIdArr[i],"dutyId":deptDutyArr[i],"queue":deptNoArr[i]});
		});
		formData.push({"name": "otherDepts", "value": otherDepts});
	}

	//组织角色数据
	var sysUserRole = new Array();
	$.each($("#chooseList").find("option"), function(i, o) {
		sysUserRole.push({"roleId":o.value});
	});
	formData.push({"name": "sysUserRole", "value": sysUserRole});

	var adminSide = new Array();
	//组织管理员机构树	先判断管理员按钮是否选中
	if($("input[name='isAdmin']:checked").val() == 1 || $("input[name='isAdmin']:checked").val() == 2
				|| $("input[name='isAdmin']:checked").val() == 3){
		$.each(deptFullTree.ids, function(i, o) {
			adminSide.push({"deptId":o.split(",")[0],"parentId":o.split(",")[1],"isChecked":o.split(",")[2]});
		});
	}
	formData.push({"name": "adminSide", "value": adminSide});
	var flies = attachPool.getFiles();
	if(flies) {
		formData.push({"name": "fileids", "value": flies});
		formData.push({"name": "photo", "value": $("#userPhotoImgFile").attr("path")});
	}
};

//添加成功清空表单数据
userEdit.initForm = function(){
	userEdit.clearForm();
	userEdit.removeOtherDept(true);
	userEdit.removeAllRole();
	deptFullTree.init();
};

//清空表单
userEdit.clearForm = function () {
	var token = $("#userForm #token").val();

	$('#userForm')[0].reset();
	$("#userForm #token").val(token);
	$("#photo").val("");
	$("#userPhoto").attr("src", getRootPath()+"/images/demoimg/iphoto.jpg");
	//userEdit.leader.clearValue();
	//userEdit.deptLeader.clearValue();
	//userEdit.chargeLeader.clearValue();
	$("#deptError").hide();
	hideErrorMessage();
};

//var deptTree2 = null;
//显示部门树
userEdit.showDeptTree = function (){
	$('#myModal').modal('show');
	//deptTree2 = new deptTree();
	//deptTree2.show("deptTreeDiv", "userForm #deptId", "userForm #deptName",null,false);
	userDeptTree.show("deptTreeDiv", "userForm #deptId", "userForm #deptName",null,false,"userForm #deptWeight");
};

//显示管理员机构树
userEdit.showAdminTree = function () {
	$('#myModal1').modal('show');
	deptFullTree.show("deptFullTreeDiv");
};
//显示保密员机构树
userEdit.showSecurityTree = function () {
	$('#myModal1').modal('show');
	deptFullTree.show("deptFullTreeDiv");
};
//显示审计员机构树
userEdit.showAuditTree = function () {
	$('#myModal1').modal('show');
	deptFullTree.show("deptFullTreeDiv");
};

//初始化表单tab
userEdit.initTab = function(){
	$("#m1").attr("class", "active");
	$("#m2").attr("class", "");
	$("#m3").attr("class", "");
	$("#messages1").attr("class", "tab-pane fade active in");
	$("#messages2").attr("class", "tab-pane fade");
	$("#messages3").attr("class", "tab-pane fade");
};

//添加其他部门
userEdit.addOtherDept = function (){
	var template = jQuery.validator.format($.trim($("#template").val()));
	$(template(userEdit.otherDeptCount)).appendTo("#otherDeptTable tbody");
	//dic.fillDics("otherDeptDuty-"+userEdit.otherDeptCount, "dutyId",true,1);
	userEdit.otherDeptCount = userEdit.otherDeptCount + 1;
};

//显示其他部门树
userEdit.showOtherDept = function(id,name){
	$('#myModal').modal('show');
	//var treeOther = new deptTree();
	userDeptTree.show("deptTreeDiv", id, name,null,false);
};

//删除其他部门	(state为true时删除所有其他部门)
userEdit.removeOtherDept = function (state) {
	var obj = $("[name^='otherDeptTrId-']:checked");
	if(state)obj = $("[name^='otherDeptTrId-']");
	if(obj.length == 0 && !state) {
		msgBox.info({
			content: $.i18n.prop("JC_SYS_061")
		});
		return;
	}
	if(state){
		obj.each(function() {
			$(this).parent().parent().remove();
		});
	} else {
		confirmx($.i18n.prop("JC_SYS_034"),function(){
			obj.each(function() {
				$(this).parent().parent().remove();
			});
			msgBox.tip({
				type:"success",
				content: $.i18n.prop("JC_SYS_005")
			});
		});
	}
};

//初始化角色
userEdit.initRoleList = function () {
//	var url = getRootPath()+"/system/getRolesForUser.action";
//	jQuery.ajax({
//		url : url,
//		type : 'GET',
//		cache: false,
//		async : false,
//		success : function(data) {
//			$("#userForm #token").val(data.token);
//			$("#roleList").empty();
//			$("#chooseList").empty();
//			$.each(data.roleList, function(i, o) {
//				var option = $("<option>").val(o.id).text(o.name);
//				$("#roleList").append(option);
//			});
//		},
//	});

	$("#roleList").find("option").remove();
	$("#chooseList").find("option").remove();

	$("#tempRoleList").find("option").each(function(){
		$("#roleList").append($(this).clone());
	});
};

//添加角色
userEdit.addRole = function () {
	var obj = $("#roleList").find("option:selected");
	$("#chooseList").append(obj);
};
//添加所有角色
userEdit.addAllRole = function () {
	var obj = $("#roleList").find("option");
	$("#chooseList").append(obj);
};
//删除角色
userEdit.removeRole = function () {
	var obj = $("#chooseList").find("option:selected");
	$("#roleList").append(obj);
};
//删除所有角色
userEdit.removeAllRole = function () {
	var obj = $("#chooseList").find("option");
	$("#roleList").append(obj);
};

//显示日程
userEdit.showCale = function(){
	var val = $("input[name='isLeader']:checked").val();
	if(val == 1){
		$(".openCaleTd").show();
		$(".openCaleDiv").show();
	} else {
		$(".openCaleTd").hide();
		$(".openCaleDiv").hide();
	}
};

userEdit.showOtherDeptDiv = function(){
	var val = $("input[name='isOtherDept']:checked").val();
	if(val == 1){
		$("#otherDiv").show();
		$("#otherContentDIV").show();
	} else {
		$("#otherDiv").hide();
		$("#otherContentDIV").hide();
	}
};

//获得已上传的附件1001
userEdit.getUserPhoto = function(){
	attachPool.clearFileQueue('#userUploader');
	//$("#userMyModal").modal("show");
	//上传头像实例
	attachPool.add({
		containerId: '#userUploader',
		uploadInitId: '#userFilePicker',
		toButtonView : '#userEditUpload',
		uploadInitLabel: '上传头像',
		//限制只能上传一个文件
		fileNumLimit: 1,
		//限制只可以上传图片
		accept: {
			title: '指定格式',
			extensions: 'gif,jpg,jpeg,bmp,png',
			mimeTypes: 'image/*'
		},
		dndArea: '#dndArea',
		form: {
			businessId: (userEdit.userPhotoId?userEdit.userPhotoId:$('#id').val()),
			businessTable: 'tty_sys_user',
			category: 'userPhotoImg'
		},
		isEcho: true,
		error:function(id,name){
			//删除业务逻辑
			jQuery.ajax({
				url : getRootPath()+'/content/attach/deleteUserPhoto.action',
				type : 'POST',
				data : {id: $('#id').val()},
				success : function(result) {
					if(result.state){
						msgBox.tip({
							type:"success",
							content: "删除头像成功"
						});
					} else {
						msgBox.tip({
							content: "删除失败！"
						});
					}
				},
				error : function() {
					msgBox.tip({
						content: $.i18n.prop("JC_SYS_002")
					});
				}
			});
		}
	});
};
userEdit.leader = null;
userEdit.deptLeader = null;
userEdit.chargeLeader = null;
//初始化
$(document).ready(function(){
	ie8StylePatch();
	$("#saveAndClose").click(function(){userEdit.saveOrModify(true);});
	$("#saveOrModify").click(function(){userEdit.saveOrModify(false);});
	$("#showDeptTree").click(userEdit.showDeptTree);
	$("#dept-append").click(userEdit.addOtherDept);
	$("#dept-remove").click(function(){userEdit.removeOtherDept(false);});
	$("#showAdminTree").click(function(){
		$("input[type='radio'][name=isAdmin][value='1']").prop("checked",true);
		userEdit.showAdminTree();
	});
	$("#showSecurityTree").click(function(){
		$("input[type='radio'][name=isAdmin][value='2']").prop("checked",true);
		userEdit.showSecurityTree();
	});
	$("#showAuditTree").click(function(){
		$("input[type='radio'][name=isAdmin][value='3']").prop("checked",true);
		userEdit.showAuditTree();
	});
	$("#showAdminTree_t").click(userEdit.showAdminTree);
	$("#showSecurityTree_t").click(userEdit.showSecurityTree);
	$("#showAuditTree_t").click(userEdit.showAuditTree);

	$(".datepicker-input").each(function(){$(this).datepicker();});

	$("#addRole").click(userEdit.addRole);
	$("#addAllRole").click(userEdit.addAllRole);
	$("#removeRole").click(userEdit.removeRole);
	$("#removeAllRole").click(userEdit.removeAllRole);
	$("#userDivClose").click(function(){$('#myModal-edit').modal('hide');});

	$(".leaderCla").change(userEdit.showCale);
	$(".isOtherCla").change(userEdit.showOtherDeptDiv);
	//初始化列表
//	userEdit.leader = JCTree.init({
//		container   : 'leaderIdDiv',
//		controlId   : 'leaderId-leaderId',
//		isCheckOrRadio  : false,
//		callback    : function(obj){
//			leaderName = obj ? obj.text : "";
//		}
//	});

//	userEdit.deptLeader = JCTree.init({
//		container   : 'deptLeaderDiv',
//		controlId   : 'deptLeader-deptLeader',
//		isCheckOrRadio  : false
//	});

//	userEdit.chargeLeader =  JCTree.init({
//		container   : 'chargeLeaderDiv',
//		controlId   : 'chargeLeader-chargeLeader',
//		isCheckOrRadio  : false
//	});
});