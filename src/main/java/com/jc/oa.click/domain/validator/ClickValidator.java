package com.jc.oa.click.domain.validator;

/**
 * Created by yangj on 2016/6/27.
 */
import com.jc.oa.click.domain.Click;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
public class ClickValidator implements Validator{
    @Override
    public boolean supports(Class<?> arg0) {
        return Click.class.equals(arg0);
    }

    /**
     * @description 检验具体实现方法
     * @param arg0  当前的实体类
     * @param arg1  错误的信息
     * @author
     * @version  2016-06-27
     */
    public void validate(Object arg0, Errors arg1) {
        Click v =  (Click)arg0;
        if(v.getUserId()!=null){
            arg1.reject("UserId", "UserId is null");
        }
        if(v.getMenuId()!=null){
            arg1.reject("MenuId", "MenuId is null");
        }
        if(v.getMenuName()!=null){
            arg1.reject("tMenuName", "tMenuName is null");
        }
        if(v.getMenuAction()!=null){
            arg1.reject("MenuAction", "MenuAction is null");
        }
        if(v.getClicks()!=null){
            arg1.reject("Clicks", "Clicks is null");
        }

    }
}
