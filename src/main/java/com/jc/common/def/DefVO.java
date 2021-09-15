package com.jc.common.def;

import javax.xml.bind.annotation.*;
import java.util.List;
@XmlAccessorType(XmlAccessType.FIELD)
// XML文件中的根标识
@XmlRootElement(name = "Def")
public class DefVO {

    private String disTemplate;

    private String querySql;

    @XmlElementWrapper(name="itemList")
    @XmlElement(name="item")
    private List<DefItemVO> itemList;

    public String getDisTemplate() {
        return disTemplate;
    }

    public void setDisTemplate(String disTemplate) {
        this.disTemplate = disTemplate;
    }

    public List<DefItemVO> getItemList() {
        return itemList;
    }

    public void setItemList(List<DefItemVO> itemList) {
        this.itemList = itemList;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }
}
