var userAll = {};

//分页处理 start
//分页对象
userAll.oTable = null;

//分页查询用户
userAll.userList = function () {
	$('#IP-edit').fadeIn();
	
	//显示列信息
	userAll.oTableAoColumns = [
	    //不需要排序的列直接用mData function方式
		{mData: "displayName"},
		//需要排序列需要指定属性mData
		{mData: "sex", mRender : function(mData, type, full) {
				return full.sexValue;
			}
		},
		{mData: "deptName"},
		{mData: "dutyId", mRender : function(mData, type, full){
				return full.dutyIdValue;
			}
		},
		{mData: "status", mRender : function(mData, type, full){
				return full.statusValue;
			}
		},
		{mData: function(source) {
			return "<div class=\"input-group w-p100\"><input type=\"text\" id=\"orderNo_"+source.id+"\" value="+ source.orderNo +" maxlength=\"6\" style=\"width:60px;margin-bottom:0;\"> " +
					"<a class=\"a-icon i-new\" href=\"#\" style=\"margin-left:10px;margin-bottom:0;\" onclick=\"userAll.modifyUserOrder("+ source.id +")\">确认</a></div>";
		}
	},
		//设置权限按钮
		{mData: function(source) {
			return userOTableSetButtons(source);
		}}
	 ];
	
	//组装后台参数
	userAll.oTableFnServerParams = function(aoData){
		//排序条件
		getTableParameters(userAll.oTable, aoData);
		//组装查询条件
		var deptIds = $("#userListForm #deptIds").val();
		var sex = $("#userListForm #sex").val();
		var status = $("#userListForm #status").val();
		var displayName = $("#userListForm #displayName").val();
		
		if(deptIds.length > 0){
			aoData.push({ "name": "deptIds", "value": deptIds});
		}
		if(sex !=null && sex != ""){
			aoData.push({ "name": "sex", "value": sex});
		}
		if(status !=null && status != ""){
			aoData.push({ "name": "status", "value": status});
		}
		if(displayName != ""){
			aoData.push({ "name": "displayName", "value": displayName});
		}
	};
	
	userAll.oTableRetrieveData = function ( sSource, aoData, fnCallback ) {
	    $.ajax({
			type : "GET",
			url : sSource,
			data : aoData,
			dataType : "json",
			success : function(resp) {
				fnCallback(resp);
				//重新绑定编辑和删除提交框
				$("i[data-toggle='tooltip']").tooltip();
				treeHightReset("treeDemo");
				ie8TableStyle();
				//判断操作列没有按钮时隐藏操作列 没有操作列不需要添加此方法
				//setColumnVis(user.oTable, 6);
			}
		});
	};
	
	//table对象为null时初始化
	if (userAll.oTable == null) {
		userAll.oTable = $('#userTable').dataTable( {
			bPaginate: false,
			sAjaxSource: getRootPath()+"/sys/user/userAllList.action",
			fnServerData: userAll.oTableRetrieveData,//查询数据回调函数
			aoColumns: userAll.oTableAoColumns,//table显示列
			fnServerParams: function ( aoData ) {//传参
				userAll.oTableFnServerParams(aoData);
			},
			aaSorting:[],//设置表格默认排序列
			//fnDrawCallback: rememberPage,
			//默认不排序列 bSortable: false, aTargets: [0,6] 表格默认第一列和第7列不进行排序
	        aoColumnDefs: [{bSortable: false, aTargets: [0,5,6]}],
		} );
		
	} else {
		//table不为null时重绘表格
		userAll.oTable.fnDraw();
	}
};

userAll.modifyUserOrder = function(userId){
	var orderNo = $("#orderNo_"+userId).val();
	if (!/^[1-9]\d*$/.test(orderNo)) {
		msgBox.tip({
			content: "请输入大于1的数字"
		});
		return;
	}
	if(orderNo){
		var url = getRootPath()+"/sys/user/updateUserOrder.action";
		jQuery.ajax({
			url : url,
			type : 'POST',
			data : {userId:userId,orderNo:orderNo},
			success : function(data) {
				if(data == 1){
					msgBox.tip({
						type:"success",
						content: "修改成功"
					});
					userAll.userList();
				} else {
					msgBox.tip({
						content: "修改失败"
					});
				}
			}
		});
	}
};

userAll.userOrder = function(rowNo, type){
	var userId = $("#rowUserId_"+rowNo).val();
	var orderNo = $("#rowOrderNo_"+rowNo).val();
	var oUserId = null;
	var oOrderNo = null;
	
	if(type == 1){
		if(rowNo == 1){
			msgBox.tip({
				content: "不能向上操作"
			});
			return;
		} else {
			oUserId = $("#rowUserId_"+(rowNo-1)).val();
			oOrderNo = $("#rowOrderNo_"+(rowNo-1)).val();
		}
	} else {
		if($("#rowUserId_"+(rowNo+1)).length == 0){
			msgBox.tip({
				content: "不能向下操作"
			});
			return;
		} else {
			oUserId = $("#rowUserId_"+(rowNo+1)).val();
			oOrderNo = $("#rowOrderNo_"+(rowNo+1)).val();
		}
	}
	
	if(userId && orderNo && oUserId && oOrderNo){
		var url = getRootPath()+"/sys/user/updateUserOrder.action";
		jQuery.ajax({
			url : url,
			type : 'POST',
			data : {userId:userId,orderNo:orderNo,oUserId:oUserId,oOrderNo:oOrderNo},
			success : function(data) {
				if(data == 1){
					msgBox.tip({
						type:"success",
						content: "操作成功"
					});
					userAll.userList();
				} else {
					msgBox.tip({
						content: "操作失败"
					});
				}
			}
		});
	}
};

//分页处理 end
userAll.queryReset = function(){
	$('#userListForm #status').val('status_0');
	$('#userListForm #sex').val('');
	$('#userListForm #displayName').val('');
	if(userAll.usertree){
		var node = userAll.usertree.getRootNode();
		userAll.usertree.getZTreeObj().selectNode(node);
		$("#userListForm #deptIds").val(userAll.usertree.getChildNodesId(node));
		$("#userListForm #deptId").val(node.id);
		$("#userListForm #deptName").val(node.name);
	}
	//2019-04-10修改 测试提出只是重置查询条件，不应触发查询处理
	//userAll.userList();
	//deptTreeToPage.cancelCheck();
};

//初始化
jQuery(function($) 
{
	$("#queryUser").click(userAll.userList);
	$("#queryReset").click(userAll.queryReset);
	//初始化列表
	userAll.usertree = JCTree.ztree({
	    container  : 'treeDemo',
	    expand : true,
	    rootNode : true,
	    onClick: function(jObj,id,tree){
	    	tree.checkState = true;
	    	$("#userListForm #deptIds").val(userAll.usertree.getChildNodesId(tree));
	    	$("#userListForm #deptId").val(tree.id);
			$("#userListForm #deptName").val(tree.name);
			
			userAll.userList();
	    }
	});
});