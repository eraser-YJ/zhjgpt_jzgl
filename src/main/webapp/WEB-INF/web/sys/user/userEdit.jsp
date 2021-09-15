<%@ page language="java" pageEncoding="UTF-8"%>
<style>
    #deptName{
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
    }
</style>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
    <div class="modal-dialog w900">
        <form class="table-wrap form-table" id="userForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title" id="actionTitle">编辑</h4>
                </div>
                <div class="modal-body">
                    <section class="dis-table">
                        <section class="dis-table-cell w105" >
                            <img src="${sysPath}/images/demoimg/iphoto.jpg" id="userPhotoImg" height="105" width="105"/>
                            <div class="form-btn">
                                <button class="btn dark w105" type="button" data-target="#userMyModal" data-toggle="modal" id="userEditUpload">上传头像</button>
                            </div>
                        </section>

                        <section class="panel-tab-con dis-table-cell">
                            <!-- tabs -->
                            <section class="tabs-wrap tabs-wrap-in">
                                <!-- 同时添加 tabs-wrap-in 类-->
                                <ul class="nav nav-tabs">
                                    <li class="active" id="m1">
                                        <a href="#messages1" data-toggle="tab">基本信息</a>
                                    </li>
                                    <!-- 根据a标签href属性相对应的id，切换tab-pane -->
                                    <li id="m2">
                                        <a href="#messages2" data-toggle="tab">其他信息</a>
                                    </li>
                                    <%--<li id="m3">
                                        <a href="#messages3" data-toggle="tab">用户角色</a>
                                    </li>--%>
                                </ul>
                            </section>
                            <!-- tabs end -->
                            <!-- tabs模块下紧接panel模块 -->
                            <div class="tab-content" style="overflow:hidden;">
                                <div class="tab-pane for fade active in" id="messages1">
                                    <!-- tab-pane tab切换显示的模块 内部panel的类名替换为panel-in-box -->
                                    <input type="hidden" id="id" name="id" value="0">
                                    <input type="hidden" id="token" name="token" value="${data.token}">
                                    <input type="hidden" id="modifyDate" name="modifyDate">
                                    <input type="hidden" id="dutyId" name="dutyId" value="">
                                    <input type="hidden" id="level" name="level" value="">
                                    <input type="hidden" id="jobTitle" name="jobTitle" value="">
                                    <input type="hidden" id="political" name="political" value="">
                                    <input type="hidden" id="leaderId" name="leaderId" value="">
                                    <input type="hidden" id="deptLeader" name="deptLeader" value="">
                                    <input type="hidden" id="chargeLeader" name="chargeLeader" value="">
                                    <div class="table-wrap form-table">
                                        <table class="table table-td-striped" style="table-layout: fixed;">
                                            <tbody>
                                            <tr>
                                                <td style="width:18%;" class=" b-green-dark b-tc">用户状态</td>
                                                <td style="width:32%;">
                                                    <input type="hidden" id="statusOld" name="statusOld" />
                                                    <select name="status" id="status" class="valid">
                                                        <option value="status_0" selected="">启用</option>
                                                        <option value="status_2">锁定</option>
                                                    </select>
                                                </td>
                                                <td style="width:18%;">用户编号</td>
                                                <td>
                                                    <input type="text" id="code" name="code" disabled/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><span class="required">*</span>登录名称</td>
                                                <td>
                                                    <input type="text" id="loginName" name="loginName"/>
                                                    <input type="hidden" id="loginNameOld" name="loginNameOld"/>
                                                </td>
                                                <td><span class="required">*</span>显示名称</td>
                                                <td>
                                                    <input type="text" id="displayName" name="displayName"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>联系电话</td>
                                                <td><input type="text" name="mobile" id="mobile" /></td>
                                                <td>用户性质</td>
                                                <td>
                                                    <dic:select name="kind" id="kind" dictName="kind" parentCode="user" headName="-请选择-" headValue="99" defaultValue=""/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <span class="required">*</span>部门
                                                </td>
                                                <td>
                                                    <div class="input-group inline-tree">
                                                        <input type="text" id="deptName" name="deptName" readonly /><input
                                                            type="hidden" id="deptId" name="deptId" /><input
                                                            type="hidden" id="deptWeight" name="deptWeight" /><a
                                                            class="btn btn-file input-group-btn" href="#"
                                                            id="showDeptTree" role="button" data-toggle="modal"><i
                                                            class="fa fa-group"></i></a>
                                                    </div>
                                                    <p class="hide" id="deptError" style="color:red;">此信息不能为空</p>
                                                </td>
                                                <!--123<td><input id="password" name="password" type="text" data-trigger="manual" data-placement="top" data-animation="false" /></td>-->
                                                <td>身份证号码</td>
                                                <td>
                                                    <input type="text" id="cardNo" name = "cardNo"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><span class="required">序号</span></td>
                                                <td><input type="text" id="orderNo" name = "orderNo"/></td>
                                                <td>入职时间</td>
                                                <td><input class="datepicker-input" type="text" id="entryDate" name="entryDate" data-date-format="yyyy-MM-dd" ></td>
                                            </tr>
                                            <tr>
                                                <td><span class="required">*</span>权重系数</td>
                                                <td><input type="text" id="weight" name="weight"></td>
                                                <td><span class="required">*</span>密级级别</td>
                                                <td>
                                                    <dic:select name="extStr1" id="extStr1" dictName="secret_type" parentCode="send_manage"  defaultValue="secret_type_0"/>
                                                    <input type="hidden"  name="isAdmin" value="0">
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="tab-pane fade" id="messages2">
                                    <!-- tab-pane tab切换显示的模块 内部panel的类名替换为panel-in-box -->
                                    <div class="table-wrap form-table">
                                        <table class="table table-td-striped">
                                            <tbody>
                                            <tr>
                                                <td style="width:20%;">出生日期</td>
                                                <td style="width:30%;">
                                                    <input id="birthday" name = "birthday" class="datepicker-input" type="text" data-date-format="yyyy-MM-dd">
                                                </td>
                                                <td style="width:20%;">民族</td>
                                                <td>
                                                    <input type="hidden" id="mobileOld" name = "mobileOld"/>
                                                    <input type="hidden" id="groupTel" name = "groupTel"/>
                                                    <input type="hidden" id="email" name = "email"/>
                                                    <input type="hidden" id="officeAddress" name="officeAddress"/>
                                                    <input type="hidden" id="officeTel" name = "officeTel"/>
                                                    <input id="entryPoliticalDate" name="entryPoliticalDate"  type="hidden">
                                                    <dic:select name="ethnic" id="ethnic" dictName="ethnic" parentCode="user" headName="-请选择-" headValue="" defaultValue=""/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>婚姻状况</td>
                                                <td>
                                                    <dic:select name="maritalStatus" id="maritalStatus" dictName="maritalStatus" parentCode="user" headName="-请选择-" headValue="" defaultValue=""/>
                                                </td>
                                                <td>籍贯</td>
                                                <td>
                                                    <input type="text" id="hometown" name="hometown"/>
                                                </td>

                                            </tr>
                                            <tr>
                                                <td>学历</td>
                                                <td>
                                                    <dic:select name="degree" id="degree" dictName="degree" parentCode="user" headName="-请选择-" headValue="" defaultValue=""/>
                                                </td>
                                                <td>用户性别</td>
                                                <td>
                                                    <dic:select name="sex" id="sex" dictName="sex" parentCode="user" headName="-请选择-" headValue="99" defaultValue=""/>
                                                </td>
                                                </td>
                                                <%--<td>开户银行</td>
                                                <td><input type="text" id="cardBank" name = "cardBank"/></td>--%>
                                            </tr>
                                            <%--<tr>
                                                <td>开户姓名</td>
                                                <td><input type="text" id="cardName" name = "cardName"/></td>
                                                 <td>卡号</td>
                                                <td><input type="text" id="payCardNo" name = "payCardNo"/></td>
                                            </tr>--%>
                                            <input type="hidden" id="question" name = "question">
                                            <input type="hidden" id="answer" name = "answer">
                                            <input type="hidden" id="cardBank" name = "cardBank"/>
                                            <input type="hidden" id="cardName" name = "cardName"/>
                                            <input type="hidden" id="payCardNo" name = "payCardNo"/>
                                            <input type="hidden" id="isDriver" name = "isDriver" value="0"/>
                                            <input type="hidden" id="isLeader" name = "isLeader" value="0"/>
                                            <%--<tr>
                                                <td>司机</td>
                                                <td>
                                                    <label class="radio inline">
                                                        <input type="radio" id="isDriver" name = "isDriver" value="1"/>是
                                                    </label>
                                                    <label class="radio inline">
                                                        <input type="radio" id="isDriver" name = "isDriver" checked value="0"/>否
                                                    </label>
                                                </td>
                                                <td>是否是领导</td>
                                                <td>
                                                    <label class="radio inline">
                                                        <input type="radio" id="isLeader" name = "isLeader" value="1" class="leaderCla"/>是
                                                    </label>
                                                    <label class="radio inline">
                                                        <input type="radio" id="isLeader" name = "isLeader" checked value="0" class="leaderCla"/>否
                                                    </label>
                                                </td>
                                            </tr>--%>
                                            <%--<tr>
                                                <td><div class="openCaleTd" style="display:none">是否公开日程</div></td>
                                                <td>
                                                    <div class="openCaleDiv" style="display:none">
                                                    <label class="radio inline">
                                                        <input type="radio" name="openCale" value="1"/>是
                                                    </label>
                                                    <label class="radio inline">
                                                        <input type="radio" name="openCale" checked value="0"/>否
                                                    </label>
                                                    </div>
                                                </td>
                                            </tr>--%>
                                            <input type="hidden" name="isCheck" value="1" />
                                            <%--<tr>
                                                <td>是否考勤</td>
                                                <td>
                                                    <label class="radio inline">
                                                        <input type="radio" name="isCheck" value="1" checked/>是
                                                    </label>
                                                    <label class="radio inline">
                                                        <input type="radio" name="isCheck" value="0"/>否
                                                    </label>
                                                </td>
                                                <td></td>
                                                <td></td>
                                            </tr> --%>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="m-t m-b">
                                        <input type="hidden" name="isOtherDept" value="0"/>
                                        <%--<strong class="m-r-sm">是否添加其他部门</strong>
                                        <label class="radio inline m-l m-t-n-xs">
                                            <input type="radio" name="isOtherDept" value="1" class="isOtherCla"/>
                                            是
                                        </label>
                                        <label class="radio inline m-t-n-xs">
                                            <input type="radio" name="isOtherDept" value="0" class="isOtherCla" checked/>否
                                        </label>--%>
                                    </div>
                                    <div class="form-btn m-b" style="display:none" id="otherDiv">

                                        <button class="btn dark" type="button" id="dept-append" >添 加</button><button class="btn" type="button" id="dept-remove" >删 除</button>
                                    </div>
                                    <div class="table-wrap show us-list form-table-h" id="otherContentDIV">
                                        <table class="table table-striped  m-b-md first-td-tc" id="otherDeptTable">
                                            <tbody>

                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="tab-pane fade" id="messages3">
                                    <div class="ms2side__div">
                                        <div class="ms2side__select">
                                            <div class="ms2side__header">角色选择框</div>
                                            <select title="角色选择框" name="roleList" id="roleList" size="0" multiple="multiple" class="select-list-h">
                                                <c:forEach items="${data.roleList}" var="role">
                                                    <option value="${role.id}">${role.name}</option>
                                                </c:forEach>
                                            </select>
                                            <select id="tempRoleList" name="tempRoleList" style="display: none;">
                                                <c:forEach items="${data.roleList}" var="rolevo">
                                                    <option value="${rolevo.id}">${rolevo.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="ms2side__options" style="padding-top: 6.5px;">
                                            <p class="AddOne ms2side__hide" title="添加" id="addRole"><span></span></p>
                                            <p class="AddAll ms2side__hide" title="添加所有" id="addAllRole"><span></span></p>
                                            <p class="RemoveOne ms2side__hide" title="删除" id="removeRole"><span></span></p>
                                            <p class="RemoveAll ms2side__hide" title="删除所有" id="removeAllRole"><span></span></p>
                                        </div>
                                        <div class="ms2side__select">
                                            <div class="ms2side__header">已选角色框</div>
                                            <select title="已选角色框" name="chooseList" id="chooseList" size="0" multiple="multiple" class="select-list-h"></select>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </section>
                    </section>
                </div>
                <div class="modal-footer form-btn">
                    <button class="btn dark" type="button" id="saveOrModify">保存继续</button><button class="btn" type="button" id="saveAndClose">保存退出</button><button class="btn" type="button" id="userDivClose">关 闭</button>
                </div>
            </div>
        </form>
    </div>
</div>
<div class="modal fade panel" id="myModal" aria-hidden="false">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title">机构选择</h4>
            </div>
            <div class="modal-body">
                <div id="deptTreeDiv" class="ztree"></div>
            </div>
            <div class="modal-footer no-all form-btn">
                <button class="btn dark" type="submit" id="treeSave">确 定</button><button class="btn" type="reset" id="treeClose">取 消</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade panel" id="myModal1" aria-hidden="false">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title">机构选择</h4>
            </div>
            <div class="modal-body">
                <div id="deptFullTreeDiv" class="ztree"></div>
            </div>
            <div class="modal-footer no-all form-btn">
                <button class="btn dark" type="submit" id="fullTreeSave">确 定</button><button class="btn" type="reset" id="fullTreeClose">取 消</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade panel" id="userMyModal" aria-hidden="false">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" id="closebtn" data-dismiss="modal">×</button>
                <h4 class="modal-title">上传头像</h4>
            </div>
            <div class="modal-body">
                <div id="wrapper">
                    <div id="container">
                        <div id="userUploader" class="attach">
                            <div class="queueList">
                                <div id="dndArea" class="placeholder">
                                    <div id="userFilePicker"></div>
                                    <p></p>
                                </div>
                            </div>
                            <div class="statusBar" style="display:none;">
                                <div class="progress">
                                    <span class="text">0%</span>
                                    <span class="percentage"></span>
                                </div><div class="info"></div>
                                <div class="btns">
                                    <div id="filePickerBtn" class="attachBtn"></div><div class="uploadBtn">开始上传</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer form-btn">
                <button class="btn dark" type="button"  data-dismiss="modal">关 闭</button>
            </div>
        </div>
    </div>
</div>
     <textarea style="display:none" id="template">
        <tr>
            <td class="w46"><input type="checkbox" name="otherDeptTrId-{0}" id="otherDeptTrId-{0}"/>
                <input type="hidden" id="otherDeptId-{0}" name="otherDeptId-{0}"/>
            </td>
            <td class="w100"><span class="required">*</span>部门名称</td>
            <td style="w105"><div class="input-group inline-tree"><input type="text" id="otherDeptName-{0}" name="otherDeptName-{0}" class="otherDeptNameCla" readonly/>
                <a class="btn btn-file input-group-btn" href="#" id="showDeptTree-{0}" role="button" data-toggle="modal" onclick='userEdit.showOtherDept("otherDeptId-{0}","otherDeptName-{0}")'><i class="fa fa-group"></i></a></div></td>
            <td class="w100"><span class="required">*</span>部门职务</td>
            <td class="btn-s-md">
                <select id="otherDeptDuty-{0}" name="otherDeptDuty-{0}" class="otherDeptDutyCla">

                </select>
            </td>
            <td class="w100"><span class="required">*</span>部门序号</td>
            <td class="w84">
                <input type="text" id="otherDeptNo-{0}" name="otherDeptNo-{0}" class="otherDeptNoCla"/>
            </td>
        </tr>
     </textarea>
<script src="${sysPath}/js/com/sys/user/userEdit.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/user/user.validate.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/user/userDeptTree.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/user/deptFullTree.js" type="text/javascript"></script>
