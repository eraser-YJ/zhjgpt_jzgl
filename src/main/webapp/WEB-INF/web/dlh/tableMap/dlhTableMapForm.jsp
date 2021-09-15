<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityForm">
					<input type="hidden" id="id" name="id" />
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate" />
					<table class="table table-td-striped">
						<tbody>
						<%--<tr>
							<td><span class='required'>*</span>dataObject对象obj_url</td>
							<td>
								<input type="text" id="objUrlK" name="objUrlK"/>
							</td>
							<td><span class='required'>*</span>dataObject对象obj_url</td>
							<td>
								<input type="text" id="objUrlV" name="objUrlV"/>
							</td>
						</tr>--%>
						<tr>
							<td><span class='required'>*</span>主表名</td>
							<td>
								<select name="tableNameK" id="tableNameK" datatype="null" onchange="dlhTableMapJsForm.loadOptions(this,'columnNameK')">
									<option value="0" selected>--请选择--</option>
									<c:forEach items="${modelList}" var="mode"  varStatus="abcCond">
										<option value="${mode.tableCode}">${mode.tableName}</option>
									</c:forEach>

								</select>

							</td>
							<td><span class='required'>*</span>主表列名</td>
							<td>
								<select name="columnNameK" id="columnNameK" datatype="null">
								</select>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>子表表名</td>
							<td>
								<select name="tableNameV" id="tableNameV" onchange="dlhTableMapJsForm.loadOptions(this,'columnNameV')" datatype="null">
									<option value="0" selected>--请选择--</option>
									<c:forEach items="${modelList}" var="mode"  varStatus="abcCond" >
										<option value="${mode.tableCode}">${mode.tableName}</option>
									</c:forEach>

								</select>

							</td>
							<td><span class='required'>*</span>子表列名</td>
							<td>
								<select name="columnNameV" id="columnNameV" datatype="null">
								</select>
							</td>

						</tr>
						<tr>
							<td><span class='required'>*</span>关联类型</td>
							<td>
                                <select name="columnType" id="columnType" >

                                   <option value="one2one" selected>一对一</option>
                                    <option value="one2one" >一对多</option>

                                </select>

							</td>
						</tr>
                        <tr>
                            <td><span class='required'>*</span>连接方式</td>
                            <td>
                                <select name="extStr1" id="extStr1" >

                                    <option value="left join" selected>左连接</option>
                                    <option value="right join" >右连接</option>
                                    <option value="inner join" >内连接</option>
                                </select>
                            </td>
                        </tr>
						<tr>
							<td><span class='required'>*</span>排序</td>
							<td>
								<input type="text" id="extNum1" name="extNum1">

							</td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" id="saveBtn">保 存</button>
				<button class="btn" type="button" id="closeBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/dlh/tableMap/dlhTableMapForm.js'></script>