var loginlog = {}, pageCount=10;
  	
//重复提交标识
loginlog.subState = false;
//分页处理 start
//分页对象
loginlog.oTable = null;

//显示列信息
//常用词
loginlog.oTableAoColumns = [
	{ "mData": "loginName" },
	{ "mData": "displayName" },
	{ "mData": "ip" },
	{ "mData": "loginTime" , mRender : function(mData, type, full){
		if(full.loginTime != null && full.loginTime != "")
			return "登录";
		else
			return "退出";
		}
	},
	{ "mData": "id", mRender : function(mData, type, full){
			if(full.loginTime != null && full.loginTime != "")
				return full.loginTime;
			else
				return full.logoutTime;
		}
	}
];

loginlog.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(loginlog.oTable, aoData);
	
	var loginName = $("#loginlogListForm #loginName").val();
	var displayName = $("#loginlogListForm #displayName").val();
	var loginstatus = $("#loginlogListForm #loginstatus").val();
	var logoperTimeBegin = $("#loginlogListForm #logoperTimeBegin").val();
	var logoperTimeEnd = $("#loginlogListForm #logoperTimeEnd").val();
	
	if(loginName != ""){
		aoData.push({ "name": "loginName", "value": $.trim(loginName)});
	}
	
	if(displayName != ""){
		aoData.push({ "name": "displayName", "value": $.trim(displayName)});
	}
	
	if(loginstatus == 0 || loginstatus == 1 || loginstatus == 2){
		aoData.push({ "name": "loginstatus", "value": loginstatus});
	} else {
		aoData.push({ "name": "createDate", "value": loginlog.dateReckon(6)});
	}
	
	if(loginstatus == 0 || loginstatus == 2){
		if(logoperTimeBegin != ""){
			aoData.push({ "name": "loginTimeBegin", "value": logoperTimeBegin});
		} else {
			aoData.push({ "name": "loginTimeBegin", "value": loginlog.dateReckon(6)});
		}
		
		if(logoperTimeEnd != ""){
			aoData.push({ "name": "loginTimeEnd", "value": logoperTimeEnd});
		} else {
			aoData.push({ "name": "loginTimeEnd", "value": loginlog.dateReckon(-1)});
		}
	}
	
	if(loginstatus == 1 || loginstatus == 2){
		if(logoperTimeBegin != ""){
			aoData.push({ "name": "logoutTimeBegin", "value": logoperTimeBegin});
		} else {
			aoData.push({ "name": "logoutTimeBegin", "value": loginlog.dateReckon(6)});
		}
		
		if(logoperTimeEnd != ""){
			aoData.push({ "name": "logoutTimeEnd", "value": logoperTimeEnd});
		} else {
			aoData.push({ "name": "logoutTimeEnd", "value": loginlog.dateReckon(-1)});
		}
	}
	
};

loginlog.dateReckon = function(dayadd){
	var nowdate = new Date();
	nowdate = nowdate.valueOf();
	nowdate = nowdate - dayadd * 24 * 60 * 60 * 1000;
	nowdate = new Date(nowdate);
	
	var dict = {   
	    "yyyy": nowdate.getFullYear(),   
	    "M": nowdate.getMonth() + 1,   
	    "d": nowdate.getDate(),   
	    "H": nowdate.getHours(),   
	    "m": nowdate.getMinutes(),   
	    "s": nowdate.getSeconds(),   
	    "MM": ("" + (nowdate.getMonth() + 101)).substr(1),   
	    "dd": ("" + (nowdate.getDate() + 100)).substr(1),   
	    "HH": ("" + (nowdate.getHours() + 100)).substr(1),   
	    "mm": ("" + (nowdate.getMinutes() + 100)).substr(1),   
	    "ss": ("" + (nowdate.getSeconds() + 100)).substr(1)   
	}; 
	var format = "yyyy-MM-dd HH:mm:ss";
	return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {   
        return dict[arguments[0]];   
    });
};

//分页查询
loginlog.loginlogList = function () {
	if (loginlog.oTable == null) {
		loginlog.oTable = $('#loginlogTable').dataTable( {
			"iDisplayLength": loginlog.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/loginlog/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": loginlog.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				loginlog.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3,4]}
	        ]
		} );
	} else {
		loginlog.oTable.fnDraw();
	}
};

//处理缓存问题 清空form表单中值
loginlog.queryReset = function(){
	$('#loginlogListForm')[0].reset();
	$("#loginlogListForm #logoperTimeBegin").val(loginlog.dateReckon(6));
	$("#loginlogListForm #logoperTimeEnd").val(loginlog.dateReckon(-1));
};

//初始化
jQuery(function($) 
{
	//计算分页显示条数
	loginlog.pageCount = TabNub>0 ? TabNub : 10;
	$("#queryloginlog").click(loginlog.loginlogList);
	$("#queryReset").click(loginlog.queryReset);
	//日历控件重新初始化
	$(".datepicker-input").datepicker();
	$("#loginlogListForm #logoperTimeBegin").val(loginlog.dateReckon(6));
	$("#loginlogListForm #logoperTimeEnd").val(loginlog.dateReckon(-1));
	//初始化列表方法
	loginlog.loginlogList();
});	


