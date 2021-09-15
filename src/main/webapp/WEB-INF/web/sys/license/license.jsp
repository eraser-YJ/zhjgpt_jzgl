<%@ page language="java" pageEncoding="UTF-8"%>

   <div class="modal fade panel" id="dLinceseModal" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">授权提醒</h4>
              	</div>
                <div class="modal-body">
                    	<table class="table table-td-striped">
                        	<tbody>
                                <tr>
                                    <td style="text-align: center;">
                                        <span class="required">${licenseMes}</span>
                                    </td>
                                </tr>
                            </tbody>
                    	</table>
                </div>
                <div class="modal-footer no-all form-btn">
                    <button class="btn dark" type="button" onclick="closeDLinceseModal()">确 定</button>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript">
function closeDLinceseModal() {
	//隐藏对话框
	$('#dLinceseModal').modal('hide');
}
</script>