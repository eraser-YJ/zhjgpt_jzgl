<%@ page language="java" import="com.jc.foundation.util.GlobalContext" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="/WEB-INF/tlds/shiros.tld"%>
<%@ taglib prefix="c" uri="c"%>
<%@ taglib prefix="fn" uri="fn" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="dic" uri="/dic-tags" %>
<%@ taglib prefix="common" uri="/WEB-INF/tlds/common.tld" %>
<c:set var="systemState" value="${initParam.systemState}"/>
<c:set var="sysPath" value="${pageContext.request.contextPath}"/>
<%
	if ("true".equals(GlobalContext.getProperty("cas.start"))) {
		pageContext.setAttribute("managerHostPath", GlobalContext.getProperty("api.dataServer"));
		pageContext.setAttribute("subsystemId", GlobalContext.getProperty("subsystem.id"));
	} else {
		pageContext.setAttribute("managerHostPath", "");
		pageContext.setAttribute("subsystemId", "");
	}
%>
<c:set var="theme" value="${sessionScope.theme}"/>
<c:if test="${sysPath=='/'}"><c:set var="sysPath" value="" /></c:if>
<c:if test="${manageHostPath==''}"><c:set var="manageHostPath" value="${pageContext.request.contextPath}" /></c:if>
<script>
	var apiServerPath = "${manageHostPath}";
	if (typeof(subsystemId) == "undefined") {
		var subsystemId = "${subsystemId}";
	}
	function getSubsystemId() {
		return subsystemId;
	}
</script>