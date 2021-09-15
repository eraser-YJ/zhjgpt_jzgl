<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<%@ taglib prefix="dicex" uri="/WEB-INF/tlds/dicex-tags.tld" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>设备信息</span>
            <span>设备信息 > </span><span>设备登记</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
        <form class="table-wrap form-table" id="entityForm">
            <div>
                <h3>基本信息</h3>
                <input type="hidden" id="id" name="id" value="${data.id}"/>
                <input type="hidden" id="modifyDate" name="modifyDate"/>
                <input type="hidden" id="warnInfo" name="warnInfo"/>
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width:120px;"><span class='required'>*</span>设备类型</td>
                        <td><dicex:typeTag name="equipmentType" id="equipmentType"  headName="-请选择-" headValue="" onchange="equipmentInfoJsForm.typeChange()"/></td>
                        <td style="width:120px;"><span class='required'>*</span>设备编码</td>
                        <td><input type="text" id="equipmentCode" name="equipmentCode"/></td>
                    </tr>
                    <tr>
                        <td><span class='required'>*</span>设备名称</td>
                        <td colspan="3"><input type="text" id="equipmentName" name="equipmentName"/></td>
                    </tr>
                    <tr>
                        <td><span class='required'>*</span>所在工程</td>
                        <td>
                            <input type="hidden" id="projectCode" name="projectCode">
                            <input type="text" id="projectName" name="projectName" style="width:80%;" readonly>
                            <input type="button" class="btn" value="选择" id="projectListBtn" name="projectListBtn">
                        </td>
                        <td>施工单位</td>
                        <td>
                            <input type="hidden" id="useCompany" name="useCompany"/>
                            <input type="text" id="useCompanyName" name="useCompanyName" style="width:80%;" readonly/>
                            <input type="button" class="btn" value="选择" id="useCompanyBtn" name="useCompanyBtn">
                        </td>
                    </tr>
                    <tr>
                        <td>区域</td>
                        <td colspan="3"><input type="text" id="workArea" name="workArea"/></td>
                    </tr>
                    <tr>
                        <td>经度</td>
                        <td><input type="text" id="longitude" name="longitude" readonly/></td>
                        <td>纬度</td>
                        <td><input type="text" id="latitude" name="latitude" readonly/></td>
                    </tr>
                    <tr>
                        <td>司机</td>
                        <td><input type="text" id="driver1" name="driver1"/></td>
                        <td>司机手机</td>
                        <td><input type="text" id="driver1Mobile" name="driver1Mobile"/></td>
                    </tr>
                    <tr>
                        <td>司索信号工</td>
                        <td><input type="text" id="signalman" name="signalman"/></td>
                        <td>司索信号工手机</td>
                        <td><input type="text" id="signalmanMobile" name="signalmanMobile"/></td>
                    </tr>
                    <tr>
                        <td>备注</td>
                        <td colspan="3"><input type="text" id="remark" name="remark"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </form>
        <form class="table-wrap form-table" id="entityFormWarn" >
            <div id="entityFormWarnDisplayDiv">

            </div>
        </form>
        </div>
        <div id="formFoorDiv" class="m-l-md" style="text-align: center; height: 100px;">
            <button class="btn dark" id="saveBtn" type="button">保 存</button>
            <button class="btn" id="closeBtn" type="button">返 回</button>
        </div>
    </section>
</section>
<div id="projectDiv"></div>
<div id="deptDiv"></div>
<script src='${sysPath}/js/com/jc/csmp/equipment/info/equipmentInfoForm.js'></script>
<script src='${sysPath}/js/com/jc/csmp/equipment/info/equipmentInfoForm.validate.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>