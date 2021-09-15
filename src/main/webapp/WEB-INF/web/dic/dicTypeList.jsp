<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="c"%>
<c:forEach items="${dicTreeTypeList}" var="dictypeVo">
	<li>
		<a href="#demo${dictypeVo.id }"  data-toggle="collapse">
		<i class="fa fa-minus3"></i>${dictypeVo.value }</a>
		<ul id="demo${dictypeVo.id }" class="collapse in">
			<c:forEach items="${dictypeVo.children}" var="dictype">
				<li><a href="#demo${dictype.id }"  data-toggle="collapse">
				<i class="fa fa-minus3"></i>${dictype.value }</a>
				<ul id="demo${dictype.id }" class="collapse in">
					<c:forEach items="${dictype.children}" var="dictypechild">
						<li><a href="#" onClick="dics.loaddicinfo('${dictypechild.code}','${dictypechild.parentId}')">${dictypechild.value }</a></li>
					</c:forEach>
				</ul>
			</c:forEach>
		</ul>
	</li>
</c:forEach>
