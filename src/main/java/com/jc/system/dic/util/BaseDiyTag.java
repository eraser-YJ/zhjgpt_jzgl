package com.jc.system.dic.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class BaseDiyTag extends TagSupport{
	
	private static final long serialVersionUID = -9083066581021084737L;
	
	protected String name;
    protected String type;
    protected String onclick;
    protected String onfocus;
    protected String onblur;
    protected String onchange;
    protected String cssStyle;
    protected String cssClass;
    protected String disabled;
    protected String size;
    protected String dictName = "";
    protected Object defaultValue = "";
    protected String filter;
    protected String organCode;
    protected String deptCode;

    public void generateAttribute(StringBuilder sb) {
        if (id != null) {
            sb.append(" id='").append(id).append("'");
        }
        if (onclick != null) {
            sb.append(" onclick='").append(onclick).append("'");
        }
        if (onfocus != null) {
            sb.append(" onfocus='").append(onfocus).append("'");
        }
        if (onblur != null) {
            sb.append(" onblur='").append(onblur).append("'");
        }
        if (onchange != null) {
            sb.append(" onchange='").append(onchange).append("'");
        }
        if (cssStyle != null) {
            sb.append(" style='").append(cssStyle).append("'");
        }
        if (cssClass != null) {
            sb.append(" class='").append(cssClass).append("'");
        }
        if (size != null) {
            sb.append(" size='").append(size).append("'");
        }
        if (disabled != null && "true".equals(disabled)) {
            sb.append(" disabled='").append(disabled).append("'");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public String getOnfocus() {
        return onfocus;
    }

    public void setOnfocus(String onfocus) {
        this.onfocus = onfocus;
    }

    public String getOnblur() {
        return onblur;
    }

    public void setOnblur(String onblur) {
        this.onblur = onblur;
    }

    public String getOnchange() {
        return onchange;
    }

    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }

    public String getCssStyle() {
        return cssStyle;
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
    
    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) throws JspException {
		 if(defaultValue == null) {
             defaultValue = "";
         }
		 this.defaultValue = ExpressionEvaluatorManager.evaluate("defaultValue", defaultValue.toString(),
				 Object.class, this, pageContext);  
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getOrganCode() {
		return organCode;
	}

	public void setOrganCode(String organCode) {
		this.organCode = organCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }
}
