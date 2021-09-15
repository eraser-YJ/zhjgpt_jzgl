package com.jc.mobile.web;

import com.jc.csmp.message.domain.CmMessageInfo;
import com.jc.csmp.message.service.ICmMessageInfoService;
import com.jc.csmp.project.domain.CmProjectPerson;
import com.jc.csmp.project.service.ICmProjectPersonService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.mobile.basic.web.MobileController;
import com.jc.mobile.util.MobileApiResponse;
import com.jc.supervise.warning.domain.CmSupervisionMessage;
import com.jc.supervise.warning.domain.CmSupervisionWarning;
import com.jc.supervise.warning.enums.SupervisionWarningStatusEnums;
import com.jc.supervise.warning.service.ICmSupervisionMessageService;
import com.jc.supervise.warning.service.ICmSupervisionWarningService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;
import net.bytebuddy.asm.Advice;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.collections.list.AbstractLinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 移动端预警库接口
 * @Author 常鹏
 * @Date 2020/8/3 16:13
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/mobile/supervision")
public class MobileSupervisionController extends MobileController {

    @Autowired
    private ICmSupervisionWarningService cmSupervisionWarningService;
    @Autowired
    private ICmSupervisionMessageService cmSupervisionMessageService;
    @Autowired
    private ICmMessageInfoService cmMessageInfoService;
    @Autowired
    private ICmProjectPersonService cmProjectPersonService;

    /**
     * 预警列表(监管)
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value="supervisionList.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse supervisionList(PageManager page, HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        String status = request.getParameter("status");
        //是否分页表示
        String isPage = request.getParameter("isPage");
        CmSupervisionWarning entity = new CmSupervisionWarning();
        entity.setDeptCondition(DeptCacheUtil.getCodeById(UserUtils.getUser(apiResponse.getUserId()).getDeptId()));
        entity.setStatus(status);
        entity.addOrderByFieldDesc(" t.supervision_date ");
        if (isPage != null && GlobalContext.TRUE.equals(isPage)) {
            //分页
            PageManager page_ = this.cmSupervisionWarningService.query(entity, page);
            GlobalUtil.setTableRowNo(page_, page.getPageRows());
            return MobileApiResponse.ok(page_);
        } else {
            //不分页直接查询列表
            try {
                List<CmSupervisionWarning> dataList = this.cmSupervisionWarningService.queryAll(entity);
                return MobileApiResponse.ok(dataList);
            } catch (CustomException e) {
                e.printStackTrace();
                return MobileApiResponse.error(e.getMessage());
            }
        }
    }

    /**
     * 监管详情
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value="detailById.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse detailById(@RequestParam("id") String id, HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        return MobileApiResponse.ok(this.cmSupervisionWarningService.getById(id));
    }

    /**
     * 监管处理
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value="supervisionHandle.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse supervisionHandle(@RequestBody CmSupervisionWarning entity, HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        if (StringUtil.isEmpty(entity.getId()) || StringUtil.isEmpty(entity.getDisploseResult())) {
            return MobileApiResponse.fromResult(Result.failure(ResultCode.PARAM_IS_BLANK));
        }
        CmSupervisionWarning db = this.cmSupervisionWarningService.getById(entity.getId());
        if (db == null) {
            return MobileApiResponse.fromResult(Result.failure(ResultCode.RESULE_DATA_NONE));
        }
        db.setDisploseResult(entity.getDisploseResult());
        db.setDisploseDate(new Date(System.currentTimeMillis()));
        db.setDisploseUserId(apiResponse.getUserId());
        db.setStatus(SupervisionWarningStatusEnums.finish.toString());
        return MobileApiResponse.fromResult(this.cmSupervisionWarningService.updateEntity(db));
    }

    /**
     * 根据项目id获取项目的参与者
     * @param projectId
     * @param request
     * @return
     */
    @RequestMapping(value="personList.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse personList(@RequestParam("projectId") String projectId, HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        List<Map<String, String>> resultList = new ArrayList<>();
        List<CmProjectPerson> personList = this.cmProjectPersonService.getByProjectId(projectId);
        if (personList != null) {
            for (CmProjectPerson person : personList) {
                Map<String, String> map = new HashMap<>(4);
                map.put("id", person.getCompanyId());
                map.put("name", person.getCompanyName());
                String leaderId = "";
                String leaderName = "";
                try {
                    Department department = DeptCacheUtil.getDeptById(person.getCompanyId());
                    if (department != null && !StringUtil.isEmpty(department.getLeaderId())) {
                        User user = UserUtils.getUser(department.getLeaderId());
                        if (user != null) {
                            leaderId = user.getId();
                            leaderName = user.getDisplayName();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                map.put("leaderId", leaderId);
                map.put("leaderName", leaderName);
                resultList.add(map);
            }
        }
        return MobileApiResponse.ok(resultList);
    }

    /**
     * 发送督办信息
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value="supervisionSendMessage.action", method = RequestMethod.POST)
    @ResponseBody
    public MobileApiResponse supervisionSendMessage(@RequestBody CmSupervisionMessage entity, HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        Department department = DeptCacheUtil.getDeptById(entity.getReceiveDeptId());
        if (department != null) {
            entity.setReceiveDeptCode(department.getCode());
        }
        entity.setHandleStatus("0");
        User sender = UserUtils.getUser(apiResponse.getUserId());
        entity.setSenderId(sender.getId());
        entity.setSenderDeptId(sender.getDeptId());
        Department senderDept = DeptCacheUtil.getDeptById(entity.getSenderDeptId());
        if (senderDept != null) {
            entity.setSenderDeptCode(senderDept.getCode());
        }
        return MobileApiResponse.fromResult(cmSupervisionMessageService.saveEntity(entity));
    }

    /**
     * 我的督办
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value="supervisionMessageList.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse supervisionMessageList(PageManager page, HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        String handleStatus = request.getParameter("handleStatus");
        //是否分页表示
        String isPage = request.getParameter("isPage");
        CmSupervisionMessage entity = new CmSupervisionMessage();
        entity.setReceiveId(apiResponse.getUserId());
        entity.setHandleStatus(handleStatus);
        entity.addOrderByFieldDesc(" t.create_date ");
        if (isPage != null && GlobalContext.TRUE.equals(isPage)) {
            //分页
            PageManager page_ = this.cmSupervisionMessageService.query(entity, page);
            GlobalUtil.setTableRowNo(page_, page.getPageRows());
            return MobileApiResponse.ok(page_);
        } else {
            //不分页直接查询列表
            try {
                return MobileApiResponse.ok(this.cmSupervisionMessageService.queryAll(entity));
            } catch (CustomException e) {
                e.printStackTrace();
                return MobileApiResponse.error(e.getMessage());
            }
        }
    }

    /**
     * 监管详情
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value="supervisionMessageDetail.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse supervisionMessageDetail(@RequestParam("id") String id, HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        return MobileApiResponse.ok(this.cmSupervisionMessageService.getById(id));
    }

    /**
     * 督办办理
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value="supervisionMessageHandle.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse supervisionMessageHandle(@RequestBody CmSupervisionMessage entity, HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        if (StringUtil.isEmpty(entity.getId()) || StringUtil.isEmpty(entity.getHandleResult())) {
            return MobileApiResponse.fromResult(Result.failure(ResultCode.PARAM_IS_BLANK));
        }
        CmSupervisionMessage db = this.cmSupervisionMessageService.getById(entity.getId());
        if (db == null) {
            return MobileApiResponse.fromResult(Result.failure(ResultCode.RESULE_DATA_NONE));
        }
        db.setHandleResult(entity.getHandleResult());
        db.setHandleStatus("1");
        db.setHandleDate(new Date(System.currentTimeMillis()));
        Result result = this.cmSupervisionMessageService.updateEntity(db);
        if (result.isSuccess()) {
            //根据预警id同步预警的处理状态和结果
            if (!StringUtil.isEmpty(db.getWarningId())) {
                CmSupervisionWarning warning = this.cmSupervisionWarningService.getById(db.getWarningId());
                if (warning != null) {
                    warning.setDisploseResult(entity.getHandleResult());
                    warning.setDisploseDate(new Date(System.currentTimeMillis()));
                    warning.setDisploseUserId(SystemSecurityUtils.getUser().getId());
                    warning.setStatus(SupervisionWarningStatusEnums.finish.toString());
                    this.cmSupervisionWarningService.updateEntity(warning);
                }
            }
        }
        return MobileApiResponse.ok();
    }

    /**
     * 消息列表
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value="messageList.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse action(PageManager page, HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        String readStatus = request.getParameter("readStatus");
        //是否分页表示
        String isPage = request.getParameter("isPage");
        CmMessageInfo entity = new CmMessageInfo();
        entity.setReceiveId(apiResponse.getUserId());
        entity.setReadStatus(readStatus);
        entity.addOrderByFieldDesc(" t.create_date ");
        if (isPage != null && GlobalContext.TRUE.equals(isPage)) {
            //分页
            PageManager page_ = this.cmMessageInfoService.query(entity, page);
            GlobalUtil.setTableRowNo(page_, page.getPageRows());
            return MobileApiResponse.ok(page_);
        } else {
            //不分页直接查询列表
            try {
                return MobileApiResponse.ok(this.cmMessageInfoService.queryAll(entity));
            } catch (CustomException e) {
                e.printStackTrace();
                return MobileApiResponse.error(e.getMessage());
            }
        }
    }

    /**
     * 消息详情
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value="messageDetail.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse messageDetail(@RequestParam("id") String id, HttpServletRequest request) throws Exception {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmMessageInfo messageInfo = this.cmMessageInfoService.getById(id);
        if (messageInfo != null) {
            if (messageInfo.getReadStatus() == null || messageInfo.getReadStatus().equals("0")) {
                messageInfo.setReadStatus("1");
                messageInfo.setReadDate(new Date(System.currentTimeMillis()));
                this.cmMessageInfoService.updateEntity(messageInfo);
            }
        }
        return MobileApiResponse.ok(messageInfo);
    }
}
