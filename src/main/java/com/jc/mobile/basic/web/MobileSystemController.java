package com.jc.mobile.basic.web;

import com.jc.crypto.utils.SM2Utils;
import com.jc.csmp.item.domain.ItemClassifyAttach;
import com.jc.csmp.item.service.IItemClassifyAttachService;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.mobile.util.MobileApiResponse;
import com.jc.system.content.service.IAttachService;
import com.jc.system.content.service.IUploadService;
import com.jc.system.dic.IDicManager;
import com.jc.system.event.UserSetPasswordEvent;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IUserService;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 如字典等系统的控制器
 * @Author 常鹏
 * @Date 2020/8/3 8:52
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/mobile/system")
public class MobileSystemController extends MobileController {
    @Autowired
    private IDicManager dicManager;
    @Autowired
    private IUploadService uploadService;
    @Autowired
    private IAttachService attachService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IItemClassifyAttachService itemClassifyAttachService;

    /**
     * 获取字典下拉框
     *
     * @param typeCode
     * @param parentCode
     * @return
     */
    @RequestMapping(value = "dicList.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse manageList(@RequestParam("typeCode") String typeCode, @RequestParam("parentCode") String parentCode) {
        if (StringUtil.isEmpty(typeCode) || StringUtil.isEmpty(parentCode)) {
            return MobileApiResponse.ok(Collections.EMPTY_LIST);
        }
        return MobileApiResponse.ok(dicManager.getDicsByTypeCode(typeCode, parentCode));
    }

    /**
     * 上传接口
     *
     * @param file
     * @param category
     * @throws Exception
     */
    @RequestMapping(value = "upload.action", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(@RequestParam("file") MultipartFile file, @RequestParam("category") String category, HttpServletRequest request) throws Exception {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        Attach attach = uploadService.upload(file, category, request);
        Map<String, String> resultMap = new HashMap<>(5);
        resultMap.put("ext", attach.getExt());
        resultMap.put("attachId", attach.getId());
        resultMap.put("name", attach.getName());
        resultMap.put("url", "/mobile/system/download.action?attachId=" + attach.getId());
        resultMap.put("preview", "/mobile/system/preview.action?attachId=" + attach.getId());
        return MobileApiResponse.ok(resultMap);
    }

    /**
     * 批量上传接口
     *
     * @param mutiPart
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "uploadMuti.action", method = RequestMethod.POST)
    @ResponseBody
    public MobileApiResponse uploadMuti(MultipartRequest mutiPart, HttpServletRequest request) throws Exception {
        String category = request.getParameter("category");
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        List<Map<String, String>> resultList = new ArrayList<>();
        Map<String, MultipartFile> fileMap = mutiPart.getFileMap();
        if (fileMap != null && fileMap.size() > 0) {
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                Attach attach = uploadService.upload(entry.getValue(), category, request);
                Map<String, String> resultMap = new HashMap<>(5);
                resultMap.put("ext", attach.getExt());
                resultMap.put("attachId", attach.getId());
                resultMap.put("name", attach.getName());
                resultMap.put("url", "/mobile/system/download.action?attachId=" + attach.getId());
                resultMap.put("preview", "/mobile/system/preview.action?attachId=" + attach.getId());
                resultList.add(resultMap);
            }
        }
        return MobileApiResponse.ok(resultList);
    }

    /**
     * 删除附件方法
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "deleteAttach.action", method = RequestMethod.GET)
    @ResponseBody
    public Object deleteAttach(@RequestParam("ids") String ids, @RequestParam("businessId") String businessId, @RequestParam("businessTable") String businessTable, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        uploadService.managerForAttach(businessId, businessTable, null, ids, "1");
        return MobileApiResponse.ok(ids);
    }

    /**
     * 获取附件列表
     *
     * @param busId
     * @param busTable
     * @return
     */
    @RequestMapping(value = "attachList.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse attachList(@RequestParam("busId") String busId, @RequestParam("busTable") String busTable, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        try {
            Attach param = new Attach();
            param.setBusinessIdArray(new String[]{busId});
            param.setBusinessTable(busTable);
            List<Attach> attachList = this.attachService.queryAttachByBusinessIds(param);
            if (attachList == null) {
                attachList = Collections.EMPTY_LIST;
            }
            List<Map<String, String>> resultList = new ArrayList<>();
            for (Attach attach : attachList) {
                Map<String, String> resultMap = new HashMap<>(4);
                resultMap.put("ext", attach.getExt());
                resultMap.put("attachId", attach.getId());
                resultMap.put("name", attach.getName());
                resultMap.put("category", attach.getCategory());
                resultMap.put("url", "/mobile/system/download.action?attachId=" + attach.getId());
                resultMap.put("preview", "/mobile/system/preview.action?attachId=" + attach.getId());
                resultList.add(resultMap);
            }
            return MobileApiResponse.ok(resultList);
        } catch (Exception ex) {
            ex.printStackTrace();
            return MobileApiResponse.error(ex.getMessage());
        }
    }

    /**
     * 下载附件
     *
     * @param request
     * @param attachId
     * @param response
     */
    @RequestMapping(value = "download.action", method = RequestMethod.GET)
    public void downloadFile(@RequestParam("attachId") String attachId, HttpServletRequest request, HttpServletResponse response) {
        try {
            Attach attach = new Attach();
            attach.setId(attachId);
            attach = attachService.get(attach);
            String fileName = attach.getFileName();
            String resourcesName = attach.getResourcesName();
            uploadService.downloadFile(null, fileName, resourcesName, response, request);
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    /**
     * 附件预览
     *
     * @param attachId
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "preview.action", method = RequestMethod.GET)
    public void preview(@RequestParam("attachId") String attachId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        uploadService.getOriginalImg(attachId, request, response);
    }

    @RequestMapping(value = "/userPwdModify.action", method = RequestMethod.POST)
    @ResponseBody
    public MobileApiResponse userPwdModify(@RequestBody User user, HttpServletRequest request) throws CustomException {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        MobileApiResponse result = new MobileApiResponse();
        String password = user.getPassword();
        String newPassword = user.getNewPassword();
        try {
            User u = new User();
            u.setId(apiResponse.getUserId());
            u = userService.get(u);
            if (SystemSecurityUtils.validatePassword(password, u.getPassword(), u.getKeyCode())) {
                SM2Utils.generateKeyPair();
                User u1 = new User();
                u1.setId(apiResponse.getUserId());
                u1.setPassword(SystemSecurityUtils.entryptPassword(newPassword, SM2Utils.getPubKey()));
                u1.setModifyPwdFlag(1);
                u1.setExtDate1(DateUtils.getSysDate());
                u1.setKeyCode(SM2Utils.getPriKey());
                if (userService.update2(u1) == 1) {
                    SystemSecurityUtils.setFirstLoginState();
                    u.setPassword(newPassword);
                    SpringContextHolder.getApplicationContext().publishEvent(new UserSetPasswordEvent(u));
                    result =  MobileApiResponse.okMsg("修改密码成功");
                }
            } else {
                result =   MobileApiResponse.error("旧密码不正确");
            }
        } catch (CustomException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    /**
     * 获取附件分类
     * @param code
     * @return
     */
    @RequestMapping(value="getAttachListByItemCode.action", method=RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse getAttachListByItemCode(String code) {
        if (StringUtil.isEmpty(code)) {
            return MobileApiResponse.ok(Collections.emptyList());
        }
        ItemClassifyAttach param = new ItemClassifyAttach();
        param.setItemCode(code);
        param.addOrderByField(" t.ext_num1 ");
        try {
            List<ItemClassifyAttach> dbList = this.itemClassifyAttachService.queryAll(param);
            if (dbList == null ||dbList.size() == 0) {
                dbList = new ArrayList<>();
                ItemClassifyAttach entity = new ItemClassifyAttach();
                entity.setIsCheckbox(1);
                entity.setIsRequired(1);
                entity.setItemAttach("附件信息");
                entity.setId("0");
                dbList.add(entity);
            }
            return MobileApiResponse.ok(dbList);
        } catch (Exception ex) {
            ex.printStackTrace();
            return MobileApiResponse.ok(Collections.emptyList());
        }
    }
}