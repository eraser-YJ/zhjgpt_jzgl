<%@ page language="java" pageEncoding="UTF-8"%>
<section class="scrollable jcGOA-section" id="scrollable">
	<h2 class="panel-heading clearfix" style="padding-left:5px;padding-top:5px;padding-bottom:3px;">视频设备：正常：<span id="VideoNormalNum">0</span>，报警：<span id="VideoWarnNum">0</span>，离线：<span id="VideoOutLineNum">0</span></h2>
	<section >
		<div class="table-wrap"><table class="table table-striped tab_height first-td-tc" id="gridVideoTable"></table></div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l">
			</section>
		</section>
	</section>
	<div id="formVideoModule"></div>
</section>
<script src='${sysPath}/js/com/jc/scs/videoInfoList.js'></script>