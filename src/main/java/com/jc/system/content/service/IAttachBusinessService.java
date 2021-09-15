package com.jc.system.content.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.content.domain.AttachBusiness;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface IAttachBusinessService extends IBaseService<AttachBusiness>{
    /**
     * 根据附件attachid及业务businessId物理删除数据
     * @param attachBusiness
     * @return
     */
    Integer deleteForAttachAndBusiness(AttachBusiness attachBusiness);
    List<AttachBusiness> queryForAttachAndBusiness(AttachBusiness attachBusiness);
    Boolean deleteUserPhoto(String userId);
	Integer saveUserPhoto(AttachBusiness attachBusiness) throws CustomException;
}