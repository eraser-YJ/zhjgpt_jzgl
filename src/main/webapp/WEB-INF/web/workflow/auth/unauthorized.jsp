<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
<header class="con-header pull-in">
    <div class="con-heading fl" id="navigationMenu">
         <h1>没有权限访问</h1>
     </div>
</header>
<section class="panel m-t-md" id="prompt_sub">
    <div class="h500">
    	<div class="g-z-hint img-rounded">
    		<img src="${sysPath}/images/demoimg/no-staff.png" width="70px;">
            <div class="g-z-div">
                <span class="g-z-span">抱歉！<br>您没有权限访问</span><br>
            </div>
    	</div>
    </div>
</section>
</section>
<script type="text/javascript">
	$(document).ready(function(){
		$('#prompt_sub').css('height',$('#scrollable').height()-80)
	})
</script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>  