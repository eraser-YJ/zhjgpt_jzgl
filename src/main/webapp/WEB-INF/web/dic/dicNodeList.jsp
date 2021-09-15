<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="c"%>
<c:forEach items="${dicNodeInfo}" var="dicinfoVo" varStatus="status">
	<tr id="${status.count }">
		<td>${status.count }</td>
		<td><span class='input-style'><input type="text" maxlength="20" name="value" id="value-{0}" class="valueValider" onblur="dics.clearTrim(this)" value="${dicinfoVo.value }"></span></td>
		<td><span class='input-style'><input type="text" maxlength="20" name="code" id="code-{0}" class="codeValider" onblur="dics.clearTrim(this)" value="${dicinfoVo.code }"></span></td>
		<td><span class='input-style'><input type="text" maxlength="20" name="dicType" id="dicType-{0}" disabled onblur="dics.clearTrim(this)" value="${dicinfoVo.parentId }"></span></td>
		<td><input type="checkbox" name="useFlag" id="useFlag-{0}" value="1" <c:if test="${dicinfoVo.useFlag == 1}">checked</c:if> /></td>
		<td><input type="checkbox" name="defaultValue" id="defaultValue-{0}" onclick="dics.defaultCheck(this)" value="1" <c:if test="${dicinfoVo.defaultValue == 1}">checked</c:if> /></td>
		<td>
		<input type='hidden' id="id-{0}" name="id" value="${dicinfoVo.id }"/>
		<input type='hidden' id="typeFlag-{0}" name="typeFlag" value="${dicinfoVo.typeFlag }"/><input type='hidden' id="dicFlag-{0}" name="dicFlag" value="${dicinfoVo.dicFlag }"/></td>
	</tr>
</c:forEach>