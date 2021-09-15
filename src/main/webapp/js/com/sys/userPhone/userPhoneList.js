$(document).ready(function(){
	
	
	$("#queryType").click(userPhone.userPhoneList);
	//计算分页显示条数
	userPhone.pageCount = TabNub>0 ? TabNub : 10;
	//初始化列表方法
	userPhone.userPhoneList();
	
//	$("#addUserPhoneButton").click(function(){
//		userPhone.openSelectControll("addUserPhoneButton");
//	});
//	$("#showAddDiv_t").click(function(){
//		userPhone.openSelectControll("showAddDiv_t");
//	});
	
	$("#deleteUserPhones").click("click", function(){userPhone.deleteUserPhone(0);});
    

});
  	

	
var userPhone = {};

//分页处理 start
//分页对象
userPhone.oTable = null;

userPhone.oTableAoColumns = [
	{mData: function(source) {
			return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";
		}
	},
	{ "mData": "displayName" },
	{"mData":"deviceModel"},
	{ "mData": "sex" },
	{ "mData": "deptName" },
	{ "mData": "loginName" },
	{mData: function(source) {
		if(source.imeiNo != null && source.imeiNo.length > 0 ){
			return "********";
		}else{
			return "";
		}
	}},
	{mData: function(source) {
		if(source.status == "1"){
			return "启用";
		}else if(source.status == "0"){
			return "锁定";
		}else if(source.status == "2"){
			return "待审核";
		}
	}},
	{mData: function(source) {
		if(source.deviceModel == null || source.deviceModel.length  == 0){
			return "";
		}else{
			return oTableSetButtones(source);
		}
	}}
];

userPhone.oTableFnServerParams = function(aoData){
	
	var displayName = $("#userPhoneQueryForm #displayName").val();
	aoData.push({ "name": "displayName", "value": displayName});
	var sex = $("#userPhoneQueryForm #sex").find("option:checked").val();
	if(sex.length > 0){
	aoData.push({ "name": "sex", "value": sex});
	}
	var status = $("#userPhoneQueryForm #status").find("option:checked").val();
	if(status.length > 0){
	aoData.push({ "name": "status", "value": status});
	}
	//排序条件
	getTableParameters(userPhone.oTable, aoData);
};

//分页查询
userPhone.userPhoneList = function () {
	if (userPhone.oTable == null) {
		userPhone.oTable = $('#userPhoneTable').dataTable( {
			"iDisplayLength": userPhone.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/userPhone/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": userPhone.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				userPhone.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3,4,5,6,7,8]}
	        ]
		} );
	} else {
		userPhone.oTable.fnDraw();
	}
};

//删除对象
userPhone.deleteUserPhone = function (id) {
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
			userPhone.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
userPhone.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/userPhone/deleteByIds.action",
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
			userPhone.userPhoneList();
		}
	});
};

	


userPhone.changeStatus = function(id,status){
	confirmx("是否更改状态?",function(){
	var formData = new Array();
	formData.push({"name" : "id","value" : id});
	formData.push({"name" : "status","value" : status});
	jQuery.ajax({
		url : getRootPath()+"/sys/userPhone/changeStatus.action?time=" + (+new Date().getTime()),
		type : 'POST',
		async:false,
		data : formData,
		success : function(data) {
			if(data.success == "true"){
				msgBox.tip({
					type:"success",
					content:"保存成功"
				});
				userPhone.userPhoneList();
			} else {
			}
		},
		error : function() {
			alertx("保存失败");
		}
	});
	});
}

userPhone.saveOrModify = function(data){
	var formData = new Array();
	var userArray = new Array();
	$.each(data, function(i, val){
		userArray.push({"id":val.id,"displayName":val.text});
	});
	formData.push({"name": "userList", "value": userArray});
	jQuery.ajax({
		url : getRootPath()+"/sys/userPhone/saveUserPhone.action?time=" + (+new Date().getTime()),
		type : 'POST',
		async:false,
		data : JSON.stringify(serializeJson(formData)),
		contentType: "application/json;charset=UTF-8",
		success : function(data) {
			if(data.success == "true"){
				msgBox.tip({
					type:"success",
					content:"保存成功"
				});
				userPhone.userPhoneList();
			} else {
				
			}
		},
		error : function() {
			alertx("保存失败");
		}
	});
}

userPhone.openSelectControll = function(id){
	var eData = new Array();
	jQuery.ajax({
		url : getRootPath()+"/sys/userPhone/queryAll.action?time=" + (+new Date().getTime()),
		type : 'POST',
		async:false,
		data : '',
		success : function(data) {
			$.each(data, function(i, val){
				eData.push({"id":val.userId,"text":val.displayName});
			});
		},
		error : function() {
			alertx("保存失败");
		}
	});
//	var eData = [{"id":"100143","text":"王楠"},{"id":"1007","text":"管理顾问"},{"id":"10011","text":"陈邦一"},
//	             {"id":"10017","text":"杨慧仁"},{"id":"10015","text":"朱成彬"},
//	             {"id":"100261","text":"张玉英"},{"id":"100304","text":"吕彦伟"}];
	selectControl.singlePerson(id, true, "userPhone.saveOrModify", eData);
}


	
	
		  
