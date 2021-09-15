<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
<section class="panel search-shrinkage clearfix">
<div class="search-line" style="padding-left:50px;text-align:center;height:600px;" > 
	<table style="width:900px;margin-top: 80px;"  class="table">
		<tr>
			<td style="width:160px;text-align:center;">功能</td>
			<td style="text-align:center;">操作</td>			
		</tr>
		<tr>
<!-- 			<td  style="text-align:right;">缓存清理</td> -->
<!-- 			<td > -->
<!-- 				<input type="button" class="btn" value="执 行" id="clearCacheBtn" name="clearCacheBtn" onclick="mySysFun.clearCache()"/> -->
<!-- 			</td> -->
			<td colspan="2"> TODO </td>
		</tr> 
	</table>
</div>
</section>
</section> 
<script type="text/javascript">
   var mySysFun = {};
	//显示
	mySysFun.clearCache = function(){
		$.ajax({
			type : "POST",
			url : getRootPath()+"/dlh/sys/clear.action",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					msgBox.tip({
						type:"success",
						content: "执行成功"
					});
				} else {
					msgBox.tip({
						content: data.message
					});
				}
			}
		});
	}	
	
	mySysFun.testJson = function(){
		$.ajax({
                type: 'POST',
                contentType: "application/json;charset=utf-8",
                url: getRootPath()+ '/api/dlh/mock/getData.action',
                dataType: 'json',
                data: JSON.stringify({
                        page: '1'
                }),
                success: function (data) {
                        console.log(data);
                }
        });
	}	
	
</script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>