<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%-- <%@ include file="/WEB-INF/web/include/treeTable.jsp"%> --%>
<script src="${sysPath}/js/com/archive/recycle.js"></script>
<script type="text/javascript">
//显示没有行数据后面的操作按钮
	function oTableSetButtons(bean) {
		var bat = "<td>";
		bat += "<shiro:hasPermission name='recycle:update'><a class=\"a-icon i-new\" href=\"#\" onclick='recycle.updateRecycle("+bean.id+","+bean.type+")' role=\"button\" data-toggle=\"modal\"><i   data-toggle=\"tooltip\" data-placement=\"top\" title=\"\" data-container=\"body\" ></i>&nbsp;还原&nbsp;  </a></shiro:hasPermission>";
	//	bat += "<shiro:hasPermission name='recycle:update'><a class='a-icon i-new' href='#' onclick='recycle.updateRecycle("+bean.id+","+bean.type+")' role='button' data-toggle='modal'><i   data-toggle='tooltip' data-placement='top'data-container='body' ></i>还原</a></shiro:hasPermission>";
		bat += "<shiro:hasPermission name='recycle:delect'><a class='a-icon i-remove m-r-xs' href='#' onclick='recycle.batchDelete("+bean.id+","+bean.type+")' role='button' data-toggle='modal'><i class='fa fa-remove' data-toggle='tooltip' data-placement='top' title='删除' data-container='body' data-original-title='删除'></i></a></shiro:hasPermission>";
		bat += "</td>";
		return bat;
};
</script>

<section class="scrollable padder jcGOA-section" id="scrollable">
<header class="con-header pull-in" id="header_1">
    <div class="con-heading fl" id="navigationMenu">
        <h1></h1>
        <div class="crumbs"></div>
    </div>
</header>
<section class="scrollable padder padder-body">
  <div class="panel email m-t-md">
		<section class="email-btn form-btn document-btn">
			<shiro:hasPermission name='recycle:delect'><button class="btn" type="button" id="clearBtn">清空回收站</button></shiro:hasPermission>
		<div role="search" class="email-search fr">
				<div class="input-append m-b-none">
					<input type="text" class="form-control aside-md w200"
						placeholder="请输入名称搜索当前页" id="search">
						<!-- 
					<button type="submit" class="btn">
						<i class="fa fa-search"></i>
					</button> -->
				</div>
		</div>
		</section>
      <table id="recycleList" class="document-table m-b-md list-table">
          <thead>
              <tr>
                  <th class="w100" style="width:50%;">文件名</th>
                 <!--  <th style="width:15%;">文档类型</th> -->
                  <th style="width:10%;">类型</th>
                  <th style="width:10%;">文档大小</th>
                  <th style="width:18%;">时间</th>
                  <th style="width:12%;">操作</th>
              </tr>
          </thead>
          <tbody>
          
          </tbody>
      </table>
  </div>
  </section>
</section>
<!-- IE8水印 -->
 <%@ include file="/WEB-INF/web/include/foot.jsp"%>
