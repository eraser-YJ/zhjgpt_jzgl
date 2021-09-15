<%@ page language="java" pageEncoding="UTF-8"%>
<section class="scrollable jcGOA-section" id="scrollable">
	<h2 class="panel-heading clearfix" style="padding-left:5px;padding-top:3px;padding-bottom:3px;">塔机设备：正常：<span id="CraneNormalNum">0</span>，报警：<span id="CraneWarnNum">0</span>，离线：<span id="CraneOutLineNum">0</span></h2>
	<section >
		<div class="table-wrap"><table class="table table-striped tab_height first-td-tc" id="gridCraneTable"></table></div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l">
			</section>
		</section>
	</section>
	<div id="formCraneModule"></div>
</section>
<script src='${sysPath}/js/com/jc/scs/craneInfoList.js'></script>