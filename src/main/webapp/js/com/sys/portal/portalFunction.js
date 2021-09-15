var pFunction = {}, pageCount=10;
  	
//分页处理 start
//分页对象
pFunction.oTable = null;

//显示列信息
//门户组件
pFunction.oTableAoColumns = [
	//不需要排序的列直接用mData function方式
	{mData: function(source) {
			return "<input type=\"checkbox\" name=\"ids\" value="+ source.id + ">";
		}
	},
	{ "mData": "funName"},
	{ "mData": "funCode" },
	{ "mData": "funUrl"},
	{mData: "viewType", mRender : function(mData, type, full){
			return full.viewTypeValue;
		}
	},
	//设置权限按钮
	{mData: function(source) {
		return oTableSetButtonspFunction(source);
	}}
];

pFunction.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(pFunction.oTable, aoData);
	
	var funName = $("#pFunctionListForm #funName").val();
	var funCode = $("#pFunctionListForm #funCode").val();
	var funUrl = $("#pFunctionListForm #funUrl").val();
	var pviewType = $("#pFunctionListForm #viewType").val();
	
	if(funName != ""){
		aoData.push({ "name": "funName", "value": $.trim(funName)});
	}
	if(funCode != ""){
		aoData.push({ "name": "funCode", "value": $.trim(funCode)});
	}
	if(funUrl != ""){
		aoData.push({ "name": "funUrl", "value": $.trim(funUrl)});
	}
	if(pviewType != ""){
		aoData.push({ "name": "viewType", "value": $.trim(pviewType)});
	}
};

//分页查询
pFunction.pFunctionList = function () {
	if (pFunction.oTable == null) {
		pFunction.oTable = $('#pFunctionTable').dataTable( {
			"iDisplayLength": pFunction.pageCount,//每页显示多少条记录
			"sAjaxSource": getRootPath()+"/sys/portalFunction/manageList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": pFunction.oTableAoColumns,//table显示列
			//传参
			"fnServerParams": function ( aoData ) {
				pFunction.oTableFnServerParams(aoData);
			},
			"aaSorting":[]//设置表格默认排序列
			//默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3,4,5]}
	        ]
		} );
	} else {
		pFunction.oTable.fnDraw();
	}
};
	
//处理缓存问题 清空form表单中值
pFunction.clearForm = function(form){	    	  
	   $(':input', form).each(function() { 
	     var type = this.type;
	     var tag = this.tagName.toLowerCase(); // normalize case 
	     if (type == 'text' || type == 'password' || tag == 'textarea') 
	       this.value = "";
	     else if (tag == 'select')
	    	 this.value = "";
	   }); 
};

//加载添加DIV
pFunction.loadAddHtml = function (){
	if($.trim($("#functionEdit").html()) == ""){
		$("#functionEdit").load(getRootPath()+"/sys/portalFunction/portalFunctionEdit.action",null,pFunction.createpFunction);
	}
	else{
		pFunction.createpFunction();
	}
};

//新增弹出对话框方法
pFunction.createpFunction = function(){
	hideErrorMessage();
	$("#id").attr("value","");
	pFunction.clearForm(pFunctionForm);
	$("#titlepFunction").html("新增");
	$("#funParametertype option[value='1']").attr("selected", true);
	$("#loadUrlparameter").html("<input type=\"text\" id=\"funUrlparameter\" name = \"funUrlparameter\" ><input type=\"hidden\" id=\"funUrlParametername\" name = \"funUrlParametername\" >");
	$('#myModal-edit').modal('show');
	ie8StylePatch();
};

//加载修改div
pFunction.loadUpdateHtml = function (id){
	if($.trim($("#functionEdit").html()) == ""){
		$("#functionEdit").load(getRootPath()+"/sys/portalFunction/portalFunctionEdit.action",null,function(){pFunction.get(id);});
	}
	else{
		pFunction.get(id);
	}
};

//修改弹出对话框方法
pFunction.get = function(id){
	hideErrorMessage();
  	  jQuery.ajax({
            url: getRootPath()+"/sys/portalFunction/get.action?id="+id+"&time="+(+new Date().getTime()),
            type: 'post',
            dataType: 'json',
            success: function(data, textStatus, xhr) {
              pFunction.clearForm(pFunctionForm);
          	  $("#pFunctionForm").fill(data);
          	  $("#titlepFunction").html("编辑");
          	  $('#myModal-edit').modal('show');
          	  ie8StylePatch();
          	 // pFunction.changeparameter(data.funParametertype,data.funUrlparameter,data.funUrlParametername);
            },error:function(){
          	 // alert("加载数据错误。");
          	  msgBox.tip({content: "加载数据错误", type:'fail'});
            }
       });
};

//删除门户组件
pFunction.deletepFunction = function (id) {
	var ids = String(id);
	if (id == 0) {
		var idsArr = [];
		$("[name='ids']:checked").each(function() {
			idsArr.push($(this).val());
		});
		ids = idsArr.join(",");
	}
	if (ids.length == 0) {
		//alertx("请选择要删除的常用词");
		msgBox.info({content: "请选择要删除的门户组件", type:'fail'});
		return;
	}
	
	if(pFunction.valByUse(ids))
		confirmx("您确定要删除选中的数据吗?",function(){pFunction.deleteCallBack(ids,idsArr);});
};

pFunction.valByUse = function(ids) {
	var returnValByUse = false;
	jQuery.ajax({
		url: getRootPath()+"/sys/portlet/valByUse.action?funtionids="+ids,
		type: "get",
		async:false,
		dataType: 'json',
		success: function(data, textStatus, xhr) {
			if(data.success == "false"){
				msgBox.info({content: "请先删除门户业务组件权限", type:'fail'});
				returnValByUse = false;
			}else {
				returnValByUse = true;
			}
		}
	});
	return returnValByUse;
};

//删除门户组件回调方法
pFunction.deleteCallBack = function(ids,idsArr) {
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/portalFunction/deleteByIds.action?time=" + (+new Date().getTime()),
		data : {"ids": ids},
		dataType : "json",
		success : function(data) {
			if (data > 0) {
				msgBox.tip({content: "删除成功",
					type:'success',
					callback:function(){
						pFunction.pFunctionList();
						pagingDataDeleteAllForGoBack("pFunctionTable",idsArr);
					}
				});
			}
		}
	});
};

pFunction.queryReset = function(){
	$('#pFunctionListForm')[0].reset();
};

//初始化
{
	//计算分页显示条数
	pFunction.pageCount = TabNub>0 ? TabNub : 10;
	$("#querypFunction").click(pFunction.pFunctionList);
	$("#queryReset").click(pFunction.queryReset);
	$("#pFunctionTop").click(pFunction.loadAddHtml);
	$("#pFunctionBottom").click(pFunction.loadAddHtml);
	$("#deletepFunctions").click("click", function(){pFunction.deletepFunction(0);});
	//初始化列表方法 
	pFunction.pFunctionList();    	
};	


