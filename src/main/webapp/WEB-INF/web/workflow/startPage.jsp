<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>

<section class="scrollable padder" id="scrollable">
     <header class="con-header pull-in" id="navigationMenu">
              <h1>流程管理</h1>
              <div class="crumbs">
                  <a href="#">首页</a><i></i>流程管理
              </div>
      </header>
      <section class="m-t-md panel personal-con">
      	<div class="personal-con-left">
          	<div class="personal-icon">
          		<b class="rounded">
                  	<i class="fa fa-flow"></i>
                      	流程管理
                  </b>
             	</div>
          </div>
      	<div class="personal-con-right">
          	<div class="m-b-lg personal-img"><img src="${sysPath }/images/demoimg/flow.jpg" width="1920" height="215"  alt=""/></div>
              <div class="m-l-lg personal-right-con">
              	<p><i class="fa fa-stop fl"></i>流程管理为组织内部协同业务提供底层的引擎支持，帮助组织建立标准化的工作流程体系、明确审批权限、内部业务流程办理优化、提供业务流程办理的数据报表决策支持。</p>
              </div>
        </div>
      </section>
</section>

<script>
  $(function(){
	ie8InRounded();
	var content_height=$("#content").height();
	$(".personal-con").height(content_height-$("#navigationMenu").height()-75)
   })
</script>