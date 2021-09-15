<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<style>
	/*CSS*/
	ul{ position: relative; width: 100%; }
	ul li{ list-style: none; margin: 2px auto;}
	ul li input{ display: none;}
	ul li label{ float: left; width: 100px; text-align: center; line-height: 30px; border: 1px solid #000; border-right: 0; box-sizing: border-box; cursor: pointer; transition: all .3s;}
	ul li input:checked+label{ color: #fff; background-color: #1d9cd5;}
	ul li:last-child label{ border-right: 1px solid #000;}
	.modal-body iframe{height:500px;}
</style>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog " style="width:1400px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle">工程信息</h4>
				<input type="hidden" id="nowProjectCode" name="nowProjectCode" value="${projectCode}">
			</div>
			<div class="modal-body">
				<div>
				<ul>
					<li>
						<input id="tab1" type="radio" name="tab" checked  onclick="tabChange(1)">
						<label for="tab1">工程概况</label>
					</li>
					<li>
						<input id="tab2" type="radio" name="tab" onclick="tabChange(2)">
						<label for="tab2" >从业人员</label>
					</li>
					<li>
						<input id="tab3" type="radio" name="tab" onclick="tabChange(3)">
						<label for="tab3">工地安全</label>
					</li>
					<li>
						<input id="tab4" type="radio" name="tab" onclick="tabChange(4)">
						<label for="tab4">质量管理</label>
					</li>
					<li>
						<input id="tab5" type="radio" name="tab" onclick="tabChange(5)">
						<label for="tab5">塔吊报警</label>
					</li>
					<li>
						<input id="tab6" type="radio" name="tab" onclick="tabChange(6)">
						<label for="tab6">升降机报警</label>
					</li>
					<li>
						<input id="tab7" type="radio" name="tab" onclick="tabChange(7)">
						<label for="tab7">视频报警</label>
					</li>
				</ul>
				</div>
				<br>
				<div id="tabDisplayDiv1" style="margin-top: 20px;">
					<%@ include file="/WEB-INF/web/scs/projectInfoForm.jsp" %>
				</div>
				<div id="tabDisplayDiv2" style="margin-top: 20px;display: none">
					<%@ include file="/WEB-INF/web/scs/personInfoList.jsp" %>
				</div>
				<div id="tabDisplayDiv3" style="margin-top: 20px;display: none">
					<%@ include file="/WEB-INF/web/scs/gdaqInfoList.jsp" %>
				</div>
				<div id="tabDisplayDiv4" style="margin-top: 20px;display: none">
					<%@ include file="/WEB-INF/web/scs/zlglInfoList.jsp" %>
				</div>
				<div id="tabDisplayDiv5" style="margin-top: 20px;display: none">
					<%@ include file="/WEB-INF/web/scs/craneInfoList.jsp" %>
				</div>
				<div id="tabDisplayDiv6" style="margin-top: 20px;display: none">
					<%@ include file="/WEB-INF/web/scs/hoistInfoList.jsp" %>
				</div>
				<div id="tabDisplayDiv7" style="margin-top: 20px;display: none">
					<%@ include file="/WEB-INF/web/scs/videoInfoList.jsp" %>
				</div>
			</div>

			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" id="videoBtn">视频监控</button>
				<button class="btn" type="button" id="closeBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script>
	function tabChange(selectIndex) {
		for(var index=1;index<=7;index++){
			$("#tabDisplayDiv"+index).hide();
		}
		$("#tabDisplayDiv"+selectIndex).show();
	}

</script>
<script src='${sysPath}/js/com/jc/scs/projectDesktopInfoForm.js'></script>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>