package com.jc.workflow.suggest.tag;

import com.jc.foundation.util.GlobalUtil;
import com.jc.workflow.suggest.bean.Suggest;
import com.jc.workflow.suggest.service.IWorkflowSuggestService;
import com.jc.workflow.suggest.service.impl.WorkflowSuggestServiceImpl;
import com.jc.workflow.util.SpringContextHolder;
import com.jc.workflow.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SuggestTagLook extends TagSupport {
    private static final long serialVersionUID = 1611081715952972294L;
    protected final transient Logger log = Logger.getLogger(this.getClass());
    private String pid;
    private String itemId;
    private String showLast;
    private String order;
    private String classStr;
    private String style;
    private String maxLength;
    private String showWritePannel = "true";
    IWorkflowSuggestService suggestService = SpringContextHolder.getBean(WorkflowSuggestServiceImpl.class);
    JdbcTemplate jdbcTemplate = SpringContextHolder.getBean(JdbcTemplate.class);

    public SuggestTagLook() {
    }

    @Override
    public int doStartTag() throws JspException {
        ServletRequest request = this.pageContext.getRequest();
        StringBuilder content = new StringBuilder();
        List<Map<String, Object>> dbList = jdbcTemplate.queryForList("select proc_inst_id from workflow_instance where business_key_ = ?", new Object[]{this.getPid()});
        if (dbList != null && dbList.size() > 0) {
            String instanceId = (String) dbList.get(0).get("proc_inst_id");
            if (!StringUtil.isEmpty(instanceId)) {
                List<Suggest> suggestList = getSuggest(instanceId);
                if (suggestList != null) {
                    content.append("<ul>");
                    for (Suggest suggest : suggestList) {
                        content.append("<li style='list-style-type:none;'>");
                        content.append("<div>" + suggest.getMessage() + "</div");
                        content.append("<div>审批时间：" + GlobalUtil.dateUtil2String(suggest.getCreateDate(), "yyyy-MM-dd HH:mm") + "&nbsp;&nbsp;&nbsp;&nbsp;审批人: " + suggest.getUserName() + "</div");
                        content.append("</li>");
                    }
                    content.append("</ul>");
                }
            }
        }
        try {
            this.pageContext.getOut().print(content.toString());
        } catch (IOException var13) {
            this.log.error("流程意见便签错误", var13);
        }
        return 1;
    }

    private List<Suggest> getSuggest(String instanceId) {
        List<Suggest> list = this.suggestService.querySuggestList(instanceId, this.itemId, this.showLast, this.order);
        return list;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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
