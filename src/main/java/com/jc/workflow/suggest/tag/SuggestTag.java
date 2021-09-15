//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jc.workflow.suggest.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.workflow.suggest.bean.Suggest;
import com.jc.workflow.suggest.service.IWorkflowSuggestService;
import com.jc.workflow.suggest.service.impl.WorkflowSuggestServiceImpl;
import com.jc.workflow.util.SpringContextHolder;
import com.jc.workflow.util.StringUtil;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;

public class SuggestTag extends TagSupport {
    private static final long serialVersionUID = 1611081715952972294L;
    protected final transient Logger log = Logger.getLogger(this.getClass());
    private String itemId;
    private String showLast;
    private String order;
    private String classStr;
    private String style;
    private String maxLength;
    private Boolean saveFlag = false;
    private String showWritePannel = "true";
    IWorkflowSuggestService suggestService = (IWorkflowSuggestService)SpringContextHolder.getBean(WorkflowSuggestServiceImpl.class);

    public SuggestTag() {
    }

    public int doStartTag() throws JspException {
        this.saveFlag = false;
        ServletRequest request = this.pageContext.getRequest();
        StringBuilder sb = new StringBuilder();
        Map<String, Object> workflowBeanMap = (Map)request.getAttribute("workflowBean");
        String formItemPrivJsonStr = workflowBeanMap.get("formItemPrivJsonStr") == null ? "" : workflowBeanMap.get("formItemPrivJsonStr").toString();
        List<String> hideFieldsList = new ArrayList();
        List<String> editFieldsList = new ArrayList();
        if (!StringUtil.isEmpty(formItemPrivJsonStr)) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                JsonNode node = mapper.readTree(formItemPrivJsonStr);
                JsonNode fieldList = node.get("todo");
                if (fieldList != null) {
                    for(int i = 0; i < fieldList.size(); ++i) {
                        String privilege = fieldList.get(i).get("privilege").toString();
                        privilege = privilege == null ? "" : privilege.replace("\"", "");
                        if ("edit".equals(privilege)) {
                            editFieldsList.add(fieldList.get(i).get("formName") == null ? "" : fieldList.get(i).get("formName").toString().replace("\"", ""));
                        } else if ("hidden".equals(privilege)) {
                            hideFieldsList.add(fieldList.get(i).get("formName") == null ? "" : fieldList.get(i).get("formName").toString().replace("\"", ""));
                        }
                    }
                }
            } catch (IOException var14) {
                var14.printStackTrace();
            }
        }

        String[] hideFields = new String[0];
        hideFields = (String[])hideFieldsList.toArray(hideFields);
        String[] editFields = new String[0];
        editFields = (String[])editFieldsList.toArray(editFields);
        String openType = workflowBeanMap.get("openType_") == null ? "" : workflowBeanMap.get("openType_").toString();
        String instanceId = workflowBeanMap.get("instanceId_") == null ? "" : workflowBeanMap.get("instanceId_").toString();
        List<Suggest> list = this.getSuggest(instanceId);
        String fillStr;
        if (!Arrays.asList(hideFields).contains(this.itemId)) {
            sb.append("<div workflowSuggest='true' id='" + this.itemId + "' itemId='" + this.itemId + "' class='" + this.classStr + "' style='" + this.style + "'>");
            if (list.size() > 0) {
                fillStr = this.getSuggestContentHtml(list);
                sb.append(fillStr);
            }

            if (!this.saveFlag) {
                editFields = this.getNewEditFields(editFields, request);
                if ("TODO".equals(openType.toUpperCase()) && Arrays.asList(editFields).contains(this.itemId)) {
                    fillStr = this.getFillSuggestHtml();
                    sb.append(fillStr);
                }

                this.saveFlag = false;
            }

            sb.append("</div>");
        } else {
            fillStr = this.getSuggestContentHtml(list, false);
            sb.append(fillStr);
        }

        try {
            this.pageContext.getOut().print(sb.toString());
        } catch (IOException var13) {
            this.log.error("流程意见便签错误", var13);
        }

        return 1;
    }

    private List<Suggest> getSuggest(String instanceId) {
        List<Suggest> list = this.suggestService.querySuggestList(instanceId, this.itemId, this.showLast, this.order);
        return list;
    }

    private String getSuggestContentHtml(List<Suggest> list) {
        return this.getSuggestContentHtml(list, true);
    }

    private String getSuggestContentHtml(List<Suggest> list, boolean isHidden) {
        StringBuffer buffer = new StringBuffer();
        Iterator var4;
        Suggest suggest;
        if (isHidden) {
            var4 = list.iterator();

            while(var4.hasNext()) {
                suggest = (Suggest)var4.next();
                buffer.append(this.getSuggestHtml(suggest));
            }
        } else {
            var4 = list.iterator();

            while(var4.hasNext()) {
                suggest = (Suggest)var4.next();
                buffer.append("<div id='" + suggest.getSignContainerId() + "' style='display:none;'></div>");
            }
        }

        return buffer.toString();
    }

    private String getSuggestHtml(Suggest suggest) {
        String itemId = suggest.getSignContainerId().split("_")[0];
        String flag = suggest.getSignContainerId().split("_")[1];
        String suggestId = "";
        String divId = "";
        if (suggest.getSignContainerId().split("_").length > 2) {
            suggestId = "_" + (suggest.getSignContainerId().split("_")[2] == null ? "" : suggest.getSignContainerId().split("_")[2]);
            divId = itemId + "_" + flag + suggestId;
        } else if (suggest.getSignContainerId().split("_").length == 2) {
            suggestId = "_" + (suggest.getSignContainerId().split("_")[1] == null ? "" : suggest.getSignContainerId().split("_")[1]);
            divId = itemId + "_" + flag;
        }

        String htmlStr = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (suggest.getShowFlag()) {
            if ("SAVE".equals(suggest.getConfirmType().toUpperCase())) {
                this.saveFlag = true;
                htmlStr = "<div style='z-index:1'><div class='m-b-sm'><span id='phrase" + suggestId.substring(1) + "'></span>";
                if ("true".equals(this.showWritePannel)) {
                    htmlStr = htmlStr + "<a href='javascript:void(0);' onclick='jcSignature.showWritePannel(\\\"\"+divId+\"\\\")' class='a-icon i-new m-r-xs'>签批</a>";
                }

                htmlStr = htmlStr + "</div><div signContainer='true' id='" + divId + "'><textarea " + this.getMaxLengthStr() + " id='" + itemId + "_" + flag + "Textarea" + suggestId.substring(1) + "' class='comments' rows=4 cols=27>" + suggest.getMessage() + "</textarea></div></div>";
            } else {
                htmlStr = "<div class='signature-box m-b-sm' id='" + divId + "' ><p style='min-height:50px;'>" + this.formatContent(suggest.getMessage()) + "</p><P>" + dateFormat.format(suggest.getCreateDate()) + "<strong style='padding-left:30px'>" + suggest.getUserName() + "</strong></p></div>";
            }
        } else {
            htmlStr = "<div style='display:none' id='" + divId + "' ></div>";
        }

        return htmlStr;
    }

    private String getFillSuggestHtml() {
        String flag = "" + (new Date()).getTime();
        String divId = this.itemId + "_" + flag;
        String textareaId = this.itemId + "Textarea" + flag;
        String htmlStr = "<div style='z-index:1'><div class='m-b-sm'><span id='phrase" + flag + "'></span>";
        if ("true".equals(this.showWritePannel)) {
            htmlStr = htmlStr + "<a href='javascript:void(0);' onclick='jcSignature.showWritePannel(\"" + divId + "\")' class='a-icon i-new m-r-xs'>签批</a>";
        }

        htmlStr = htmlStr + "</div><div signContainer='true' id='" + divId + "'><textarea " + this.getMaxLengthStr() + " id='" + textareaId + "' class='comments' rows=4 cols=27></textarea></div></div>";
        return htmlStr;
    }

    private String formatContent(String content) {
        String result = content.replaceAll("\\r\\n", "<BR>");
        result = result.replaceAll("\\n", "<BR>");
        return result;
    }

    private String[] getNewEditFields(String[] editFields, ServletRequest request) {
        for(int i = 0; i < editFields.length; ++i) {
            if (editFields[i].lastIndexOf("_${") != -1) {
                String fieldName = editFields[i].split("\\_")[0];
                String columnName = editFields[i].split("\\_")[1];
                columnName = columnName.substring(2, columnName.length() - 1);
                String belongsName = request.getAttribute(columnName) == null ? "" : request.getAttribute(columnName).toString();
                editFields[i] = fieldName + "_" + belongsName;
            }
        }

        return editFields;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setShowLast(String showLast) {
        this.showLast = showLast;
    }

    public String getShowLast() {
        return this.showLast;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getClassStr() {
        return this.classStr;
    }

    public void setClassStr(String classStr) {
        this.classStr = classStr;
    }

    public String getStyle() {
        return this.style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public String getMaxLengthStr() {
        return this.maxLength != null && this.maxLength.length() > 0 && !this.maxLength.equals("0") ? "maxLengths=" + this.maxLength : "";
    }

    public String getShowWritePannel() {
        return this.showWritePannel;
    }

    public void setShowWritePannel(String showWritePannel) {
        this.showWritePannel = showWritePannel;
    }
}
