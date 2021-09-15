package com.jc.system.dic.util;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.jsp.JspException;

import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class DicRadioTag extends BaseDiyTag{
    private static final long serialVersionUID = 8248756476934340948L;
    protected transient final Logger logger = Logger.getLogger(this.getClass());
    private IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
    
    private String parentCode;

    @Override
    public int doStartTag() throws JspException {
        StringBuilder sb = new StringBuilder();
        
        List<Dic> dictList = dicManager.getAllDicsByTypeCode(this.getDictName(),this.getParentCode(), true);

        String filter = this.getFilter();
        List<BaseOption> baseOptions = new ArrayList<BaseOption>();
        
        String paramValue = this.getDefaultValue().toString();
        
        for(Dic dc:dictList){
        	if(filter != null && filter.indexOf(dc.getCode()) == -1){
        		BaseOption option = new BaseOption();
            	option.setOptionValue(dc.getCode());
            	option.setOptionName(dc.getValue());
            	baseOptions.add(option);
            	if(paramValue == null && dc.getDefaultValue() == 1) {
                    paramValue = dc.getCode();
                }
        	} else if(filter == null){
        		BaseOption option = new BaseOption();
            	option.setOptionValue(dc.getCode());
            	option.setOptionName(dc.getValue());
            	baseOptions.add(option);
            	if(paramValue == null && dc.getDefaultValue() == 1) {
                    paramValue = dc.getCode();
                }
        	}
        }

        int index = 0;
        for(BaseOption option : baseOptions) {
            String id = type + "_" + option.getOptionValue();
            sb.append("<label class='radio inline' for='").append(id).append("'>").append(" <input type='radio' name='").append(name).append("' id='").append(id).append("'")
                    .append(" value='").append(option.getOptionValue()).append("'");
            if(index == 0) {
                sb.append(" checked ");
            }
            if(paramValue!=null&&paramValue.equals(option.getOptionValue())) {
                sb.append(" checked ");
            }

            generateAttribute(sb);
            sb.append(" /> ").append(option.getOptionName()).append("</label>");
            index ++;
        }
        try {
            pageContext.getOut().print(sb.toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return EVAL_BODY_INCLUDE;
}

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
    
    public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
    
    public String getParentCode() {
		return parentCode;
	}
}
