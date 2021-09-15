<%@ page language="java"  pageEncoding="UTF-8"%>
<!-- 车辆导入对话框 -->
<div class="modal  fade" id="planYearImpModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document" style="width: 720px;">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel4">年度计划导入</h4>
      </div>
      <div class="modal-body">
      	<form>
      		<table id="planYearImp" style="margin-bottom:40px;!important">
      			<tr>
      				<td>
      				<div style="background-color: #F9F8F5;min-height: 16.428571429px;padding: 15px;border: 1px solid #e5e5e5;margin-bottom: 12px;padding: 15px 15px 0 15px;">
      					<p style="font-size:15px;font-weight: bold;">请选择Excel文档!</p>
						<p style="font-size:14px;color:#999">提示：请先点击下载<a href="${sysPath}/install/年度计划导入模板.xls" style="color: black"><b>《年度计划导入模板》</b></a>，并在分类下填写相应的信息，然后上传修改后的文件。</p>
						<p style="font-size:14px;color:#999;color: red">注意：不能删除修改模板中的分类，不能改变分类显示样式，不能加列。</p>

			        </div>
      				</td>
      			</tr>
      			<tr>
      				<td>
      					<input id="excel" type="file" name="excel" accept="application/vnd.ms-excel" style="border:1px solid #cdcdcd;"/>
      				</td>
      			</tr>
      		</table>
        </form>
      </div>
     <div class="transport_box transport_box1" style="height: 56px;padding: 11px 23px;border-top: solid 1px #eee;">
		 <div style="width:160px;margin: 0 auto;">
			 <button type="button" class="btn btn-default" data-dismiss="modal">关 闭</button>
			 <button type="button" class="btn btn-primary" onclick="importPlanYearExcel();" >保 存</button>
		 </div>
      </div>
    </div>
  </div>
</div>
<script src="${sysPath}/js/lib/jquery/ajaxfileupload.js" type="text/javascript"></script>

<script type="text/javascript">
// 打开
function importPlanYearExcelOpen(){
	 
	$('#planYearImpModal').modal('show');
}
//导入
function importPlanYearExcel(){
	//excel文件
	var excel = document.getElementById("excel");
	//获取文件名
	var fileName = new String(excel.value);
	//获取文件类型
	var fileExt = new String(fileName.substring(fileName.lastIndexOf(".")+1,fileName.length));
	//判断是否为空，为空返回
	if(excel.value==""){
		msgBox.info({
			content : "请选择导入文件"
		}); 
		return ;
	}
	//判断文件类型是否为xls、xlsx，否则返回
	if(fileExt!="xls"&&fileExt!="xlsx"){
		msgBox.info({
			content : "导入文件只支持xlsx和xls类型的文件"
		});
		return ;
	}
	$(".loading").show();
    //ajax提交
    $.ajaxFileUpload({
        url:getRootPath()+"/plan/projectYearPlan/importExcel.action",
        secureuri:false,
        fileElementId:"excel",
        contentType : 'multipart/form-data;charset=UTF-8',
		dataType : 'json', // 返回值类型 一般设置为json
        success:function(data, status){
			$(".loading").hide();
			if (data.success == "true"){
				var url = getRootPath() + "/plan/projectYearPlan/getImportExcelData.action";
				jQuery.ajax({
					url: url,
					async: false,
					type: 'POST',
					success: function (data) {
						msgBox.tip({ type: "success", content: "导入成功" });
						projectPlanModule.addItemToPage(data);
						$('#planYearImpModal').modal('hide');

					}
				});
			}else{

				msgBox.info({
					content : data.message
				});
			}
        }
    });
	
};

</script>