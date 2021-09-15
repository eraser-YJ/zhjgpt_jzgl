<%@ page language="java" pageEncoding="UTF-8" %>
<div class="modal fade panel" id="resource-detail-modal" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" onclick="$('#resource-detail-modal').modal('hide')">×</button>
				<h4 class="modal-title">详细信息</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" style=" margin-top: -20px;">
					<table class="table table-td-striped table-year" id="resourceDetailTable"></table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn" type="button" onclick="$('#resource-detail-modal').modal('hide')">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script>
	var resourceDetailModal = {};

	resourceDetailModal.detail = function(option) {
		$.ajax({
			url: getRootPath() + "/supervise/resource/detail.action",
			type : "GET", data : {dataId: option.dataId, sign: option.sign}, dataType : "json",
			success : function(data) {
				var content = "";
				if (data != null) {
					for (var i = 0 ; i < data.length; i++) {
						var row = data[i];
						if (row.formShow == null || row.formShow != '1') continue;
						content += "<tr>";
						content += "<td style='width: 150px; text-align: right;'>" + row.title + "</td>";
						content += "<td style='text-align: left;'>" + (row.value == null ? "" : row.value) + "</td>";
						content += "</tr>";
					}
				}
				if (option.callback == undefined) {
					$('#resourceDetailTable').html(content);
					$('#resource-detail-modal').modal('show');
				} else {
					option.callback(content);
				}
			}
		});
	};
</script>