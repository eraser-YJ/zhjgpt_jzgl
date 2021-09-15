/**
 * @title GOA V2.0
 * @description 
 * Copyright (c) 2014 ccyixun.cn Inc. All Rights Reserved
 * Company 长春奕迅建筑智能化工程有限公司
 * @author 都业广
 * @version  2014-07-22 09:50
 */

var commonGroup = {}, pageCount=10;

//分页对象
commonGroup.oTable = null;

//组别显示列信息
commonGroup.oTableAoColumns = [
                             
	//不需要排序的列直接用mData function方式
	//checkbox框,绑定数据id
	{ "mData": "id", mData: function(source) {
				return "<input type=\"checkbox\" name=\"ids\" value=" + source.id + ">";
			}
	},
	
	//组名称
	{ "mData": "name", mRender : function(mData, type, full) {
		return full.name;
	}},
	
	{ mData : function(full) {

		//成员按钮
		var buttons = "<a class=\"a-icon i-new\" href=\"#\" id=\"groupName"+full.id+"\" role=\"button\" data-toggle=\"modal\" " +
				"onClick=\"commonGroup.rowDetail(this, "+ full.id +")\">成员</a>";
		
		//编辑按钮
		var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"commonGroup.loadUpdateHtml("+
					full.id+ ")\" role=\"button\" data-toggle=\"modal\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
		
		//删除按钮
		var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"commonGroup.deleteGroup("+
					full.id+ ")\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
		
		//三个按钮同时显示在操作栏中
		return edit + buttons + del;
	}}
];

/**
 * rowDetail的展开和关闭的方法
 * 参数id:对应数据唯一的key
 */
commonGroup.rowDetail = function(obj,id){
	//绑定onClick事件.示例是绑定到每行第一个td上面.
	 //取得选中的row  
	var nTr = $(obj).parent().parent("tr")[0];

	//判断如果rowDetail是打开状态,那么则关闭.
    if ( commonGroup.oTable.fnIsOpen(nTr) ){
    	$("#groupName"+id).text("成员");
    	commonGroup.oTable.fnClose( nTr );
    }else{
    	$("#groupName"+id).text("隐藏成员");
    	//否则,未打开rowDetail时,则追加一个新row
    	//数据加载过程中,显示"数据加载中..."message.对应资源文件中的JC_SYS_068
    	var newRow = commonGroup.oTable.fnOpen( nTr, "读取中.....",  "pd");
    	
    	//调用获得rowDetail数据的方法
    	commonGroup.getDetailData(id,newRow,obj);
    }
};

/**
 * 调用获得rowDetail数据的方法
 * 参数1:id对应每行数据的唯一标示
 * 参数2:新创建的row对象
 */
commonGroup.getDetailData = function(id,newRow,obj){

	if(commonGroup.valdel(id)){
		//getToken();
		//ajax提交内容
		jQuery.ajax({
			//请求地址
			url: getRootPath()+"/sys/group/get.action?id="+id+"&time="+(+new Date().getTime()),
			//请求方式
			type: 'post',
			//请求传递参数的数据格式
			dataType: 'json',
			//成功时的回调
			success: function(data, textStatus, xhr) {

				//清空掉form中的内容
				// commonGroupEdit.clearForm(groupForm);

				//向新创建的row中追加detail数据
				//调用拼装html语句格式的方法
				$('td', newRow).html(commonGroup.getDetailHtml(data));

			},error:function(){
				//错误信息:加载数据错误
				msgBox.tip({content: $.i18n.prop("JC_SYS_060"), type:'fail'});
			}
		});
	}
};

//验证名称是否重复
commonGroup.valdel = function(id){
	var returnvalNameEcho = false;
	jQuery.ajax({
		url: getRootPath()+"/sys/group/getVal.action?id="+id+"&time="+(+new Date().getTime()),
		type: "post",
		async:false,
		success: function(data, textStatus, xhr) {
			if(data.success == "false"){
				msgBox.tip({content: "数据已被其他人删除，请刷新页面", type:'fail'});
				returnvalNameEcho = false;
			}else {
				returnvalNameEcho = true;
			}
		}
	});
	return returnvalNameEcho;
};

/**
 * 拼装html的方法
 * 参数1:data对应ajax返回的数据
 */
commonGroup.getDetailHtml = function(data){

	var html = "<div class=\"detail-html\" style=\"text-align:left\">" + data.groupMember + "</div>";
	return html;
};

/** 拼装datatable需要的参数
 * @param aoData:对应搜索条件数据
 * @author 都业广
 * @version  2014-05-22 13:47
 */
commonGroup.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(commonGroup.oTable, aoData);
	
	//查询页面组名称
	var name = $("#groupListForm input[name='name']").val();
	
	//组名称不为空的情况加入查询条件
	if(name != ""){
		aoData.push({ "name": "name", "value": name});
	}
};

/** 分页查询
 * @author 都业广
 * @version  2014-05-22 13:47
 */
commonGroup.groupList = function () {
	if (commonGroup.oTable == null) {
		commonGroup.oTable = $('#groupTable').dataTable( {
			"iDisplayLength": commonGroup.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/group/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": commonGroup.oTableAoColumns,//table显示列
			
			//传参
			"fnServerParams": function ( aoData ) {
				commonGroup.oTableFnServerParams(aoData);
			},
			"aaSorting":[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2]}
	        ]
		} );
	} else {
		commonGroup.oTable.fnDraw();
	}
};


//加载添加DIV
commonGroup.loadAddHtml = function (){
	if($.trim($("#groupEdit").html()).length == 0){
		$("#groupEdit").load(getRootPath()+"/sys/group/groupEdit.action",null,function(){commonGroupEdit.createGroup();});
	}
	else{
		commonGroupEdit.createGroup();
	}
};


//加载修改DIV
commonGroup.loadUpdateHtml = function (id){
	if(commonGroup.valdel(id)) {
		if ($.trim($("#groupEdit").html()).length == 0) {
			$("#groupEdit").load(getRootPath() + "/sys/group/groupEdit.action", null, function () {
				commonGroupEdit.get(id);
			});
		}
		else {
			commonGroupEdit.get(id);
		}
	}
};

/** 删除组别
 * @param id:数据与数据库中对应的id
 * @author 都业广
 * @version  2014-07-26 13:47
 */
commonGroup.deleteGroup = function (id) {
	var ids = String(id);

	//id为0时:批量删除
	if (id == 0) {
		var idsArr = [];

		//获得选中的数据
		$("[name='ids']:checked").each(function() {
			idsArr.push($(this).val());
		});

		//将取得的id以逗号分隔
		ids = idsArr.join(",");
	}

	//没有选中任何数据情况的判断
	if (ids.length == 0) {
		//提示信息:请选择需要删除的数据
		msgBox.info({content: $.i18n.prop("JC_SYS_061"), type:'fail'});
		return;
	}

	//提交时弹出确认信息:是否删除该数据？
	confirmx($.i18n.prop("JC_SYS_034"),function(){commonGroup.deleteCallBack(ids,idsArr);});
};

/** 删除组别回调方法
 * @param ids:数据与数据库中对应的id串(格式:1,2,3)
 * @author 都业广
 * @version  2014-07-26 13:47
 */
commonGroup.deleteCallBack = function(ids,idsArr) {
	//ajax提交
	$.ajax({
		//提交类型
		type : "POST",
		//请求地址
		url : getRootPath()+"/sys/group/deleteByIds.action?time=" + (+new Date().getTime()),
		//向后台传递的id参数(格式:1,2,3)
		data : {"ids": ids},
		//传递数据的格式
		dataType : "json",
		//成功后的处理
		success : function(data) {
			//删除数据大于0条时的处理
			if (data > 0) {
				//提示信息:删除成功
				msgBox.tip({content: $.i18n.prop("JC_SYS_005"),
					type:'success',
					callback:function(){
						//重新刷新查询列表
						commonGroup.groupList();
						pagingDataDeleteAllForGoBack("groupTable",idsArr);
					}
				});

			}
		}
	});
};

/** 重置查询页面查询条件的方法
 * @author 都业广
 * @version  2014-07-24 13:47
 */
commonGroup.queryReset = function(){
	$('#groupListForm')[0].reset();
	$('#groupListForm')[1].reset();
};


/** 页面初始化
 * @author 都业广
 * @version  2014-07-24 13:47
 */
jQuery(function($) 
{
	//计算分页显示条数
	commonGroup.pageCount = TabNub>0 ? TabNub : 10;
	
	//绑定页面上方的新增按钮
	$("#groupTop").click(commonGroup.loadAddHtml);
	
	//绑定页面下方的新增按钮
	$("#groupBottom").click(commonGroup.loadAddHtml);
	
	//绑定查询按钮
	$("#queryGroup").click(commonGroup.groupList);
	
	//绑定查询页面的重置按钮
	$("#queryReset").click(commonGroup.queryReset);
	
	//绑定批量删除按钮
	$("#deleteGroups").click("click", function(){
		commonGroup.deleteGroup(0);
	});
	
	$("#groupListForm input[name='name']").keypress(function(e){
		if(e.which==13) return false;
	});
	
	//初始化列表方法
	commonGroup.groupList();
});	


