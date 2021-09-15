<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade" id="DataModelItemModal" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">数据模型属性选择</h4>
			</div>
			<div class="modal-body">
					<table class="table table-td-striped">
					<tr>
			        <td style="text-align:center;width:60px;">选择</td>
			        <td style="text-align:center">编码</td>
			        <td style="text-align:center">列名</td>
			        </tr>
				   <c:forEach items="${itemList}" var="itemData">
				        <tr>
				        <td style="text-align:center;">
				        <input name="selectRadioXX" id="selectRadio_${itemData.id}" type="radio" value="${itemData.id}">
				        </td>
				        <td>
				        <input id="selectItemName_${itemData.id}" type="hidden" value="${itemData.itemName}">
				        ${itemData.itemName}
				        </td>
				        <td>				        
				        ${itemData.itemComment}
				        </td>
				        </tr>
				   </c:forEach>
				   </table>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="saveSelect" onclick="myDataModelItemModalFun.save()">选 择</button> 
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
   var myDataModelItemModalFun = {};
	myDataModelItemModalFun.callback; 
	//显示
	myDataModelItemModalFun.show = function(callbackFun){
		myDataModelItemModalFun.callback = callbackFun;
		$('#DataModelItemModal').modal('show');
	}
	
    //保存
    myDataModelItemModalFun.save = function () {   
    	var id = $('input[name="selectRadioXX"]:checked').val();
    	if(id){
    		var name = $("#selectItemName_"+id).val();
	        myDataModelItemModalFun.callback(id,name);
	        $('#DataModelItemModal').modal('hide');
    	} else {
    		alert("请选择属性")
    		return;
    	}
        
    }
</script>
