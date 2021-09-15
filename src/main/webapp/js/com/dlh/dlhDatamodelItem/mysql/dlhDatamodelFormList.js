var dlhDatamodelFormList = {};

//分页处理 start
//分页对象
dlhDatamodelFormList.oTable = null;

dlhDatamodelFormList.oTableAoColumns = [
	/*{mData: function(source) {
			return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";
		}
	},*/

	{'mData':'itemComment' },
	{mData: function(source) {
		var input_str = "<input type=\"text\" size='200px' name=\""+ source.itemName +"\" name=\""+ source.itemName +"\" value=\"\">";
		if(source.itemType=='datetime'){
			input_str = "<input  id=\""+ source.itemName +"\" size=\"100px\" name = \""+ source.itemName +"\" class=\"datepicker\" type=\"text\" data-date-format=\"yyyy-MM-dd\">";
		}
		if(source.dicCode!=null&&source.dicCode!='null'&&source.dicCode!=''){

			if(source.dicList){
				input_str = "";
				input_str = input_str + "<select name=\""+ source.itemName +"\" id=\""+ source.itemName +"\">";
				input_str = input_str + "	<option value=\"\" selected=\"\">-请选择-</option>"
				$.each(source.dicList,function (index,element) {
					input_str = input_str + "   <option value=\""+ element.code +"\">"+ element.value +"</option>"
				})
				input_str = input_str + "</select>"
			}
		}

		return input_str;
	}},
	{'mData':'itemName' },
	{'mData':'itemType' }/*,
	{mData: function(source) {
		return dlhDatamodelFormList.oTableSetButtones(source);
	}}*/
];
dlhDatamodelFormList.oTableSetButtones = function(source){
	var buttonStr = "";
	var edit = "<a class=\"a-icon i-edit m-r-xs\" href=\"#myModal-edit\" onclick=\"dlhDatamodelFormList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
	var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"dlhDatamodelFormList.deleteDlhDatamodelItem('"+ source.id+ "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
	buttonStr = edit + del;
	return buttonStr;
};
dlhDatamodelFormList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(dlhDatamodelFormList.oTable, aoData);
	aoData.push({ 'name': 'modelId', 'value': $('#query_modelId').val()});
	/*var itemName = $('#query_itemName').val();
	if(itemName.length > 0){
		aoData.push({ 'name': 'itemName', 'value': itemName});
	}
	var itemComment = $('#query_itemComment').val();
	if(itemComment.length > 0){
		aoData.push({ 'name': 'itemComment', 'value': itemComment});
	}*/
};

//分页查询
dlhDatamodelFormList.dlhDatamodelFormList = function () {
	if (dlhDatamodelFormList.oTable == null) {
		dlhDatamodelFormList.oTable = $('#dlhDatamodelItemTable').dataTable( {
			//"iDisplayLength": dlhDatamodelFormList.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/dlh/dlhDatamodelItem/manageFormList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"paging": false, // 禁止分页
			"bPaginate": false, //开关，是否显示分页器
			"aoColumns": dlhDatamodelFormList.oTableAoColumns,//table显示列
			"initComplete": function( settings, json ) {

				$(".datepicker").datepicker({
					needDay:true,
					changeMonth: true, //显示月份
					changeYear: true, //显示年份
					showButtonPanel: true, //显示按钮
					dateFormat: 'yyyy-MM-dd' //日期格式
				});
			},
			//传参
			"fnServerParams": function ( aoData ) {
				dlhDatamodelFormList.oTableFnServerParams(aoData);
			},
			aaSorting:[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3]}
	        ]
		} );
	} else {
		dlhDatamodelFormList.oTable.fnDraw();
	}
};

//删除对象
dlhDatamodelFormList.deleteDlhDatamodelItem = function (id) {
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
			dlhDatamodelFormList.deleteCallBack(ids);
		}
	});
};

//删除用户回调方法
dlhDatamodelFormList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/dlh/dlhDatamodelItem/deleteByIds.action",
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
			dlhDatamodelFormList.dlhDatamodelFormList();
		}
	});
};

dlhDatamodelFormList.queryReset = function(){
	$('#dlhDatamodelItemQueryForm')[0].reset();
};
	
 //加载添加DIV
dlhDatamodelFormList.loadModule = function (){
	if($.trim($("#dlhDatamodelItemModuleDiv").html()).length == 0){
		$("#dlhDatamodelItemModuleDiv").load(getRootPath()+"/dlh/dlhDatamodelItem/loadForm.action?dbType=mysql",null,function(){
			dlhDatamodelItemModule.show();
		});
	}else{
		dlhDatamodelItemModule.show();
	}
};
		
//加载修改div
dlhDatamodelFormList.loadModuleForUpdate = function (id){
	if($.trim($("#dlhDatamodelItemModuleDiv").html()).length == 0){
		$("#dlhDatamodelItemModuleDiv").load(getRootPath()+"/dlh/dlhDatamodelItem/loadForm.action?dbType=mysql",null,function(){
			dlhDatamodelItemModule.get(id);
		});
	}else{
		dlhDatamodelItemModule.get(id);
	}
};

//重复提交标识
dlhDatamodelFormList.subState = false;
/**
 *  导入Excel
 */
dlhDatamodelFormList.save = function () {
	var query_modelId = $("#query_modelId").val();
	var url = getRootPath()+"/api/dlh/push/save2.action?query_modelId="+query_modelId;

	var formData = $("#dlhDatamodelItemSaveForm").serializeArray();


	var newdata={
		"info":JSON.stringify(formData)
	};

	jQuery.ajax({
		url : url,
		type : 'POST',
		cache: false,
		dataType:"json",
		contentType:"application/json",
		data : JSON.stringify(newdata),
		success : function(data) {
			dlhDatamodelFormList.subState = false;
			if(data.code == "000000"){
				msgBox.tip({
					type:"success",
					content: data.message
				});

				$("#token").val(data.token);
			} else {
				msgBox.info({
					type:"fail",
					content: data.message
				});
				$("#token").val(data.token);
			}
		},
		error : function() {
			dlhDatamodelFormList.subState = false;
			msgBox.tip({
				type:"fail",
				content: 'error'
			});
		}
	});
};

$(document).ready(function(){

  ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	
	//计算分页显示条数
/*	dlhDatamodelFormList.pageCount = TabNub>0 ? TabNub : 10;*/
	
	
	//初始化列表方法
	dlhDatamodelFormList.dlhDatamodelFormList();
/*	$("#addDlhDatamodelItemButton").click(dlhDatamodelFormList.loadModule);*/
/*	$("#showAddDiv_t").click(dlhDatamodelFormList.loadModule);
	$("#queryDlhDatamodelItem").click(dlhDatamodelFormList.dlhDatamodelFormList);
	$("#queryReset").click(dlhDatamodelFormList.queryReset);*/
/*	$("#deleteDlhDatamodelItems").click("click", function(){dlhDatamodelFormList.deleteDlhDatamodelItem(0);});*/
	//绑定导入按钮
	$("#addDlhDatamodelItemButton").click(dlhDatamodelFormList.save);

});

