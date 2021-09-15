/**
 * js
 */
var audithis = {};
var selectControl = null;
audithis.pageRows = null;//分页变量
audithis.subState = false;//重复提交标识
audithis.oTable = null;//分页对象

//显示列信息
audithis.oTableAoColumns = [
    {mData:"userName"},// 操作人员
	{mData: "dataName", mRender:function(mData) {
		return mData;
		} 
	},
 	{mData: "auditType", mRender : function(mData,type,full) {
		return full.auditTypeValue;
	}},//
	{mData: "dataType", mRender : function(mData) {
		return mData == 0 ? "文件夹" : "文档";
	  }
	},// 文档类型
	{mData: "operTime"},//操作时间
	{mData: "ip"},// 操作IP
	{mData: "operDesc"}// 事件描述
 ];

//组装后台参数
audithis.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(audithis.oTable, aoData);
	//组装查询条件
	$.each($("#audihhisForm").serializeArray(), function(i, o) {
		if(o.value != ""){
			aoData.push({ "name": o.name, "value": o.value});
		}
	});
};

//分页查询方法
audithis.initList = function () {
	if (audithis.oTable == null) {
		audithis.oTable = $('#audithisList').dataTable( {
			"iDisplayLength": audithis.pageRows,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/archive/audithis/manageList.action",//后台分页查询地址url
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": audithis.oTableAoColumns,//table显示列
			"fnServerParams": function ( aoData ) {
				audithis.oTableFnServerParams(aoData);
			},
			aaSorting:[],//设置表格默认排序列
			//默认不排序列
	        "aoColumnDefs": [{"bSortable": false, "aTargets": [1,3,6]}]
		});
	} else {
		//table不为null时重绘表格
		audithis.oTable.fnDraw();
	}
};

//清空表单数据
audithis.queryFormReset = function(){
	//清空表单
	$('#audihhisForm')[0].reset();
	//清空人员选择树
	selectControl.clearValue();	
};

/**
 * 初始化方法
 */
jQuery(function($){
	//计算分页显示条数
	audithis.pageRows = TabNub > 0 ? TabNub : 10;
	//初始化汇总人员选择树
	selectControl = JCTree.init({
		container   : 'userId',
		controlId   : 'userId-userId',
		isCheckOrRadio: false,
		isPersonOrOrg: true
	});
	//查询按钮绑定事件
	$("#audithisBtn").click(audithis.initList);
	//重置按钮绑定事件
	$("#audithisResetBtn").click(audithis.queryFormReset);
	//日历控件重新初始化
	$(".datepicker-input").datepicker();
	//分页查询方法
	audithis.initList();
});