package com.jc.csmp.contract.info.event;

import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.csmp.contract.info.service.ICmContractInfoService;
import com.jc.csmp.project.service.ICmProjectDataAuthService;
import com.jc.csmp.project.service.ICmProjectPersonService;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.resource.enums.ResourceEnums;
import com.jc.system.content.service.IAttachService;
import com.jc.workflow.external.EventInterface;
import com.jc.workflow.external.WorkflowBean;

import java.util.List;

/**
 * 合同审批通过线
 * @Author 常鹏
 * @Date 2020/8/20 10:15
 * @Version 1.0
 */
public class CmContractAuditSuccessEvent implements EventInterface {
    private ICmContractInfoService cmContractInfoService = SpringContextHolder.getBean(ICmContractInfoService.class);
    private ICmProjectPersonService cmProjectPersonService = SpringContextHolder.getBean(ICmProjectPersonService.class);
    private ICmProjectDataAuthService cmProjectDataAuthService = SpringContextHolder.getBean(ICmProjectDataAuthService.class);
    private IAttachService attachService = SpringContextHolder.getBean(IAttachService.class);
    @Override
    public boolean doAction(WorkflowBean workflowBean) {
        CmContractInfo entity = new CmContractInfo();
        entity.setPiId(workflowBean.getBusiness_Key_());
        try {
            entity = cmContractInfoService.get(entity);
            if (entity != null) {
                entity.setAuditStatus(WorkflowAuditStatusEnum.finish.toString());
                cmContractInfoService.update(entity);
                //将合同的甲乙方存入数据权限表
                cmProjectDataAuthService.saveOrUpdate(entity.getId(), new String[]{ entity.getPartyaUnit(), entity.getPartybUnit() });
                cmProjectPersonService.saveOtherData(entity.getProjectId(), entity.getPartybUnit());
                //获取要同步的附件信息
                entity.setAttachFile(attachService.getAttachIds(entity.getId(), "cm_contract_info"));
                //同步资源库
                ResourceEnums.pt_company_projects_htba.getResourceService().rsyncData(entity);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
