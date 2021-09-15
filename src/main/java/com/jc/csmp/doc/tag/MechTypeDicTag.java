package com.jc.csmp.doc.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;

import com.jc.csmp.doc.common.MechType;
import com.jc.system.dic.util.BaseDiyTag;
import com.jc.system.dic.util.BaseOption;

public class MechTypeDicTag extends BaseDiyTag {

	private static final long serialVersionUID = 8090362971179236834L;

	private String name;
	private String headName;
	private String headValue;

	public int doStartTag() throws JspException {
		StringBuilder sb = new StringBuilder();
		StringBuilder childsb = new StringBuilder();


		sb.append("<select name='" + this.getName() + "' id='" + this.getId() + "' dataType='" + this.getType() + "'");
		generateAttribute(sb);
		sb.append(">");

		if (headName != null) {
			if (headValue != null && headValue.trim().length() > 0) {
				sb.append("<option value='").append(headValue).append("' selected>").append(headName)
						.append("</option>");
			} else {
				sb.append("<option value='' selected>").append(headName).append("</option>");
			}
		}

		String filter = this.getFilter();
		List<BaseOption> baseOptions = new ArrayList<BaseOption>();
		for (MechType dc : MechType.values()) {
			if (filter != null && filter.indexOf(dc.toString()) == -1) {
				BaseOption option = new BaseOption();
				option.setOptionValue(dc.toString());
				option.setOptionName(dc.getDisName());
				baseOptions.add(option);
			} else if (filter == null) {
				BaseOption option = new BaseOption();
				option.setOptionValue(dc.toString());
				option.setOptionName(dc.getDisName());
				baseOptions.add(option);
			}
		}

		for (BaseOption option : baseOptions) {
			sb.append("<option value='").append(option.getOptionValue()).append("'");
			sb.append(">").append(option.getOptionName()).append("</option>");
		}
		sb.append("</select>");
		sb.append(childsb);
		try {
			this.pageContext.getOut().print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadName() {
		return headName;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}

	public String getHeadValue() {
		return headValue;
	}

	public void setHeadValue(String headValue) {
		this.headValue = headValue;
	}

}
