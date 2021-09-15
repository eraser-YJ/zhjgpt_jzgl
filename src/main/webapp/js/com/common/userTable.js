/**
 * 暂时停用
 */
var userTableByDept = {};
var count = 5;
var time = new Date().getTime();

userTableByDept.uId = "";
userTableByDept.uName = "";

userTableByDept.oTable = null;

userTableByDept.oTableAoColumns = [
    { 
    	"mData": "id", 
    	"mRender" : function(mData, type, full) {
			return "<input type=\"radio\" value="+full.id+" onclick=\"returnValue('"+full.id+"','"+full.name+"')\"/>";
    	}
    },
  	{ "mData": "name" },
];

function returnValue(id, name){
	//alert(userTableByDept.uId+"::::"+userTableByDept.uName);
	$("#"+userTableByDept.uId).val(id);
	$("#"+userTableByDept.uName).val(name);
}


/**
 * 显示列表信息
 * @param tableId 	表格ID
 * @param url		调用链接
 * @param deptId	部门ID
 * @param userId	回显控件ID名称
 * @param userName	回显控件ID名称
 */
userTableByDept.show = function(tableId, url, deptId, userId, userName){
	userTableByDept.uId = userId;
	userTableByDept.uName = userName;
	url += "?time="+time;
	
	userTableByDept.oTableFnServerParams = function(aoData){
		 aoData.push({ "name": "page", "value": userTableByDept.oTable ? userTableByDept.oTable.fnPag() : 0 });
		 aoData.push({ "name": "deptIds", "value": deptId});	
	};
	
	userTableByDept.userTableByDeptList = function () {
		if (userTableByDept.oTable == null) {
			userTableByDept.oTable = $("#"+tableId).dataTable({
				"iDisplayLength": count,//每页显示多少条记录
				"sAjaxSource": url,
				"fnServerData": oTableRetrieveData,//查询数据回调函数
				"aoColumns": userTableByDept.oTableAoColumns,//table显示列
				"fnServerParams": function (aoData) {//传参
					userTableByDept.oTableFnServerParams(aoData);
				},
				"aoColumnDefs": [
				    {"bSortable": false, "aTargets": [0,0]},
				    {"bSortable": false, "aTargets": [0,1]},
				],
				"sServerMethod": "POST"
			});
			/*userTableByDept.oTable.$('tr').click(function() {
		        var data = userTableByDept.oTable.fnGetData(this);
		        alert(data);
		    });*/
		} else {
			userTableByDept.oTable.fnDraw();
		}
	}();
}
