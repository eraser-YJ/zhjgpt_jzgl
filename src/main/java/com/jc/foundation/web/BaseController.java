package com.jc.foundation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.domain.ErrorMessage;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.DataNotFoundException;
import com.jc.foundation.util.GlobalContext;
import com.jc.workflow.core.WorkflowException;
import java.beans.PropertyEditorSupport;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 基础控制类
 * @author Administrator
 * @date 2020-07-01
 */
public class BaseController {
    protected final transient Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private HttpServletRequest request;

    public BaseController() {
    }

    @InitBinder
    public void BinderInit(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                this.setValue(text == null ? text : text.trim());
            }
        });
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(GlobalContext.DATE_FORMAT);
                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat(GlobalContext.DATE_TIME_FORMAT);
                    String patterTimeStr = "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}\\:\\d{1,2}\\:\\d{1,2}";
                    String patterDateStr = "\\d{4}-\\d{1,2}-\\d{1,2}";
                    Pattern patternTime = Pattern.compile(patterTimeStr);
                    Pattern patternDate = Pattern.compile(patterDateStr);
                    Matcher matcher = patternTime.matcher(text);
                    Matcher dateMatcher = patternDate.matcher(text);
                    if (matcher.find()) {
                        this.setValue(dateTimeFormat.parse(text));
                    } else if (dateMatcher.find()) {
                        this.setValue(dateFormat.parse(text));
                    }
                } catch (ParseException var10) {
                    BaseController.this.log.error(var10);
                }
            }
        });
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Object baseException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap();
        CustomException ce;
        if (e.getCause() != null && e.getCause().toString().startsWith(ConcurrentException.class.getName())) {
            ce = (CustomException)e;
            this.log.error(ce.getLogMsg(), ce);
            if (this.isAjaxRequest(request)) {
                resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "数据已被修改，请刷新后重新操作");
            }
        } else if (e instanceof DataNotFoundException) {
            DataNotFoundException ce1 = (DataNotFoundException)e;
            this.log.error(ce1.getLogMsg(), ce1);
            if (this.isAjaxRequest(request)) {
                resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "数据已被删除");
            }
        } else {
            if (e instanceof MaxUploadSizeExceededException) {
                try {
                    List<Attach> attachList = new ArrayList();
                    Attach attach = new Attach();
                    attach.setError("附件大小不能超过500M");
                    attachList.add(attach);
                    resultMap.put("files", attachList);
                    ObjectMapper mapper = new ObjectMapper();
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(mapper.writeValueAsString(resultMap));
                } catch (Exception var8) {
                    var8.printStackTrace();
                }

                return null;
            }

            Object t;
            if (e instanceof WorkflowException) {
                WorkflowException ce2 = (WorkflowException)e;
                resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, ((WorkflowException)e).getMessageStr());
                this.log.error(ce2.getLogMsg(), ce2);

                try {
                    if (this.isAjaxRequest(request)) {
                        resultMap.put(GlobalContext.RESULT_SUCCESS, false);
                        return resultMap;
                    }

                    t = resultMap.get(GlobalContext.RESULT_ERRORMESSAGE);
                    if (t != null) {
                        request.getSession().setAttribute("errorContent", t.toString());
                    }

                    response.sendRedirect(request.getContextPath() + "/workFlow/processDefinition/exception.action");
                } catch (IOException var9) {
                    this.log.error("出现异常，跳转页面错误", var9);
                }

                return "";
            }

            if (e.getCause() != null && e.getCause().toString().startsWith(AuthorizationException.class.getName())) {
                throw new UnauthorizedException();
            }

            if (e instanceof CustomException) {
                ce = (CustomException)e;
                resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, ((CustomException)e).getMessageStr());
                resultMap.put(GlobalContext.SESSION_TOKEN, this.getToken(request));
                t = ce;

                while(((Throwable)t).getCause() != null) {
                    t = ((Throwable)t).getCause();
                    if (t instanceof ConcurrentException) {
                        resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, ((CustomException)t).getMessageStr());
                    }
                }

                this.log.error(ce.getLogMsg(), ce);
            } else {
                this.log.error(e.getMessage(), e);
            }
        }

        if (this.isAjaxRequest(request)) {
            resultMap.put(GlobalContext.RESULT_SUCCESS, false);
            return resultMap;
        } else {
            try {
                response.sendRedirect(request.getContextPath() + "/system/500.action");
            } catch (IOException var10) {
                this.log.error("出现异常，跳转页面错误", var10);
            }

            return "";
        }
    }

    public void sendFile(String path, HttpServletResponse response) {
        this.sendFile(path, response, "application/octet-stream");
    }

    public void sendFile(String path, HttpServletResponse response, String contentType) {
        BufferedInputStream fis = null;

        try {
            File file = new File(path);
            String filename = file.getName();
            fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            int read = fis.read(buffer);
            response.reset();
            if ("application/octet-stream".equals(contentType)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            }

            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType(contentType);
            toClient.write(buffer, 0, read);
            toClient.flush();
            toClient.close();
        } catch (IOException var18) {
            var18.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException var17) {
            }

        }

    }

    public void sendFileByStr(String content, String fileName, HttpServletResponse response) {
        try {
            byte[] buffer = content.getBytes();
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }

    protected boolean isAjaxRequest(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestType);
    }

    protected Map<String, Object> validateBean(BindingResult result) {
        Map<String, Object> resultMap = new HashMap();
        if (!result.hasErrors()) {
            return resultMap;
        } else {
            List<ErrorMessage> errorMessageList = new ArrayList();
            List<ObjectError> errorList = result.getAllErrors();
            Iterator var5 = errorList.iterator();

            while(var5.hasNext()) {
                ObjectError error = (ObjectError)var5.next();
                ErrorMessage em = new ErrorMessage();
                em.setCode(error.getCode());
                em.setMessage(error.getDefaultMessage());
                errorMessageList.add(em);
            }

            resultMap.put(GlobalContext.RESULT_LABELERRORMESSAGE, errorMessageList);
            resultMap.put(GlobalContext.RESULT_SUCCESS, false);
            resultMap.put(GlobalContext.SESSION_TOKEN, this.getToken(this.request));
            return resultMap;
        }
    }

    public boolean isDupSubmit(HttpServletRequest request, String clientToken) {
        String serverToken = (String)request.getSession(false).getAttribute(GlobalContext.SESSION_TOKEN);
        if (serverToken == null) {
            return true;
        } else {
            if (clientToken == null) {
                clientToken = request.getParameter(GlobalContext.SESSION_TOKEN);
            }

            if (clientToken == null) {
                return true;
            } else {
                return !serverToken.equals(clientToken);
            }
        }
    }

    protected Map<String, Object> validateToken(HttpServletRequest request, String clientToken) {
        Map<String, Object> resultMap = new HashMap();
        if (this.isDupSubmit(request, clientToken)) {
            resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "请不要重复提交");
            resultMap.put(GlobalContext.RESULT_SUCCESS, false);
            return resultMap;
        } else {
            request.getSession().removeAttribute(GlobalContext.SESSION_TOKEN);
            return resultMap;
        }
    }

    protected Map<String, Object> validateToken(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap();
        if (this.isDupSubmit(request, (String)null)) {
            resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "请不要重复提交");
            resultMap.put(GlobalContext.RESULT_SUCCESS, false);
            return resultMap;
        } else {
            request.getSession().removeAttribute("token");
            return resultMap;
        }
    }

    public String getToken(HttpServletRequest request) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        request.getSession().setAttribute(GlobalContext.SESSION_TOKEN, token);
        return token;
    }

    protected Object getMapValue(Map<String, Object> map, String key) {
        Iterator var3 = map.entrySet().iterator();

        Entry entry;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            entry = (Entry)var3.next();
        } while(!((String)entry.getKey()).equals(key));

        return entry.getValue();
    }

    protected void fillPageInfo(Model model, HttpServletRequest request) {
        model.addAttribute("iPage", request.getParameter("iPage"));
        model.addAttribute("sortSetting", request.getParameter("sortSetting"));
        model.addAttribute("queryData", request.getParameter("queryData"));
        model.addAttribute("otherWorkflowData", request.getParameter("otherWorkflowData"));
    }
}
