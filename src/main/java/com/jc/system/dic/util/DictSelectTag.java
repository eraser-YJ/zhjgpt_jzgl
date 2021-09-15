package com.jc.system.dic.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;

import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class DictSelectTag extends BaseDiyTag {
    protected transient final Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 8090362971179236804L;
	
	private IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
	
    private String parentCode;
    private boolean group;
    private String grouplabel;
    private String headName;
    private String headValue;
    /**1全部 2请选择*/
    private String headType;
    /**是否二级联动下拉列表*/
    private boolean isHasLinkage;
    /**二级联动下拉列表选中值*/
    private String selectLinkage;

    @Override
    public int doStartTag() throws JspException {
        StringBuilder sb = new StringBuilder();
        StringBuilder childsb = new StringBuilder();
        List<Dic> dictList = dicManager.getAllDicsByTypeCode(this.getDictName(),this.getParentCode(), true);
        if ("secret_type".equals(this.getDictName())){
            User user = SystemSecurityUtils.getUser();
            String unique = "";
            if (user.getIsSystemManager()){
                unique = "secret_type_2";
            } else {
                unique = user.getExtStr1();
            }
            String secretsStr = "";
            if (unique != null && !"".equals(unique)){
                if ("secret_type_2".equals(unique)){
                    secretsStr = "secret_type_2,secret_type_1,secret_type_0";
                } else if ("secret_type_1".equals(unique)){
                    secretsStr = "secret_type_1,secret_type_0";
                } else if ("secret_type_0".equals(unique)){
                    secretsStr = "secret_type_0";
                }
            }
            List<Dic> tempList = new ArrayList<>();
            for (Dic dcu:dictList){
                if (secretsStr.indexOf(dcu.getCode()) != -1){
                    tempList.add(dcu);
                }
            }
            dictList.clear();
            dictList.addAll(tempList);
        }

        sb.append("<select name='" + this.getName() + "' id='" + this.getId() + "' dictName='" + this.getDictName() + "' parentCode='" + this.getParentCode() + "' dataType='" + this.getType() + "'");
        generateAttribute(sb);
        sb.append(">");
        if(headName != null) {
            sb.append("<option value='").append(headValue).append("' selected>").append(headName).append("</option>");
        }
        if(headType != null) {
        	if("1".equals(headType)) {
                sb.append("<option value='").append(headValue).append("' selected>").append("-全部-").append("</option>");
            }
        	if("2".equals(headType)) {
                sb.append("<option value='").append(headValue).append("' selected>").append("-请选择-").append("</option>");
            }
        }
        
        String filter = this.getFilter();

        if ("secret_type".equals(this.getDictName())){
            if (filter != null && !"".equals(filter)){
                if ("secret_type_2".equals(filter)){
                    filter = "secret_type_2,secret_type_1,secret_type_0";
                } else if ("secret_type_1".equals(filter)){
                    filter = "secret_type_1,secret_type_0";
                } else if ("secret_type_0".equals(filter)){
                    filter = "secret_type_0";
                }
            }
        }

        List<BaseOption> baseOptions = new ArrayList<BaseOption>();
        
        String paramValue = "".equals(this.getDefaultValue().toString()) ? null : this.getDefaultValue().toString();
        
        for(Dic dc:dictList){
        	String parentCode = dc.getParentType().split("=")[0];
        	if(filter != null && filter.indexOf(dc.getCode()) == -1){
        		BaseOption option = new BaseOption();
            	option.setOptionValue(dc.getCode());
            	option.setOptionName(dc.getValue());
            	baseOptions.add(option);
            	if(paramValue == null && dc.getDefaultValue() == 1) {
                    paramValue = dc.getCode();
                }
            	if(isHasLinkage) {
                    getChildList(childsb,dc.getCode(),parentCode);
                }
        	} else if(filter == null){
        		BaseOption option = new BaseOption();
            	option.setOptionValue(dc.getCode());
            	option.setOptionName(dc.getValue());
            	baseOptions.add(option);
            	if(paramValue == null && dc.getDefaultValue() == 1) {
                    paramValue = dc.getCode();
                }
            	if(isHasLinkage) {
                    getChildList(childsb,dc.getCode(),parentCode);
                }
        	}
        }

        int index = 0;
        for(BaseOption option : baseOptions) {
            sb.append("<option value='").append(option.getOptionValue()).append("'");
            if(paramValue != null) {
                 if(paramValue.equals(option.getOptionValue())) {
                     sb.append(" selected ");
                 }
            } else {
                 if(index == 0 && headName == null && headType == null) {
                     sb.append(" selected ");
                 }
            }
            sb.append(">").append(option.getOptionName()).append("</option>");
            index ++;
         }

        sb.append("</select>");
        sb.append(childsb);
        
        try {
            this.pageContext.getOut().print(sb.toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }

    public StringBuilder getChildList(StringBuilder childsb,String dictName,String parentCode) { 
    	List<Dic> dictList = dicManager.getAllDicsByTypeCode(dictName,parentCode, true);
    	List<BaseOption> baseOptions = new ArrayList<BaseOption>();
    	for(Dic dc:dictList){
            getChildList(childsb,dc.getCode(),dc.getParentId());
    		BaseOption option = new BaseOption();
        	option.setOptionValue(dc.getCode());
        	option.setOptionName(dc.getValue());
        	baseOptions.add(option);
    	}
    	int index = 0;
    	childsb.append("<div style='display: none'><select class='noneWorkflow' id='").append(dictName).append("'>");
        for(BaseOption option : baseOptions) {
        	childsb.append("<option value='").append(option.getOptionValue()).append("'");
        	if(selectLinkage != null) {
                if(selectLinkage.equals(option.getOptionValue())) {
                	childsb.append(" selected ");
                }
           } else  {
        	   if(index == 0) {
        		   childsb.append(" selected ");
               }
           }
        		
        	childsb.append(">").append(option.getOptionName()).append("</option>");
            index ++;
        }
        childsb.append("</select>");
        childsb.append("</div>");
    	return childsb;
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

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public String getGrouplabel() {
        return grouplabel;
    }

    public void setGrouplabel(String grouplabel) {
        this.grouplabel = grouplabel;
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

	public String getHeadType() {
		return headType;
	}

	public void setHeadType(String headType) {
		this.headType = headType;
	}

	public boolean isHasLinkage() {
		return isHasLinkage;
	}

	public void setIsHasLinkage(boolean isHasLinkage) {
		this.isHasLinkage = isHasLinkage;
	}

	public String getSelectLinkage() {
		return selectLinkage;
	}

	public void setSelectLinkage(String selectLinkage) {
		this.selectLinkage = selectLinkage;
	}
	
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
	public String getParentCode() {
		return parentCode;
	}

}
