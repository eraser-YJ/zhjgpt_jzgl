package com.jc.csmp.workflow.enums;

import com.jc.csmp.workflow.enums.service.IWorkflowTodoService;
import com.jc.csmp.workflow.enums.service.impl.*;
import com.jc.foundation.util.StringUtil;

/**
 * 工作流内容枚举
 * @author 常鵬
 * @version 2020/7/11
 */
public enum WorkflowContentEnum {
    /***/
    empty("- 全部 -", "", null),
    htba("合同备案审批", "784128602c5c4516abef6cbd8ce46dfa-jcleftmenu_183b2c63261a402ca2fbe8716770bd01-jcleftmenu_b9a4ca675db64fafb848d838423404b6", new HtbaTodoServiceImpl()),
    htzf("合同支付审批", "784128602c5c4516abef6cbd8ce46dfa-jcleftmenu_183b2c63261a402ca2fbe8716770bd01-jcleftmenu_86fa49ae001c4e5ab36fbb394bf6e67f", new HtzfTodoServiceImpl()),
    gcbgd("工程变更单审批", "784128602c5c4516abef6cbd8ce46dfa-jcleftmenu_72f4ac9f11b04847abab698e592a07e9-jcleftmenu_758e2b484b014ae2b6a14e9f8df9f0c7-jcleftmenu_8443537ca82d49c193817a7d0f307c4d", new BgdTodoServiceImpl()),
    htbgd("合同变更单审批", "784128602c5c4516abef6cbd8ce46dfa-jcleftmenu_72f4ac9f11b04847abab698e592a07e9-jcleftmenu_758e2b484b014ae2b6a14e9f8df9f0c7-jcleftmenu_8443537ca82d49c193817a7d0f307c4d", new BgdTodoServiceImpl()),
    qzd("工程签证单审批", "784128602c5c4516abef6cbd8ce46dfa-jcleftmenu_72f4ac9f11b04847abab698e592a07e9-jcleftmenu_8d2e5aaa73f34bb6b04b8494750a2b51-jcleftmenu_1a3978eb6dcd4e9492a8ca426532e368", new QzdTodoServiceImpl()),
    zlwt("质量问题审批", "784128602c5c4516abef6cbd8ce46dfa-jcleftmenu_17407b6294b148888dab3ef1ef11e442-jcleftmenu_57ad3870e79b44fc91a42a7695ed4436-jcleftmenu_be7f8fc041964d6f9baf5c56f8abb823", new QuestionTodoServiceImpl()),
    aqwt("安全问题审批", "784128602c5c4516abef6cbd8ce46dfa-jcleftmenu_17407b6294b148888dab3ef1ef11e442-jcleftmenu_81892404a5304d4f91fe6fafe87daf4a-jcleftmenu_879cc37a62a14e79b80055393a2bdfb0", new QuestionTodoServiceImpl()),
    xcwt("现场问题审批", "784128602c5c4516abef6cbd8ce46dfa-jcleftmenu_17407b6294b148888dab3ef1ef11e442-jcleftmenu_7428f00d61b04b448e94612ea16eb111-jcleftmenu_ed30e6868c2545c1bd74ff4ddfe934fd", new QuestionTodoServiceImpl()),
    ndjh("年度计划审批", "0e19a474fb8d49728ec0e7b577560917-jcleftmenu_68b50d6080d844709443a3ef808e6ab4", new NdjhTodoServiceImpl()),
    gcbj("工程报监审批", "f08c7faf831e4b9d8b4b534cf3baba2f-jcleftmenu_f5ea281524d54b4c8b6daac276a3cdb0", new GcbjTodoServiceImpl());

    private String value;
    private String menuId;
    private IWorkflowTodoService service;
    WorkflowContentEnum(String value, String menuId, IWorkflowTodoService service) {
        this.value = value;
        this.menuId = menuId;
        this.service = service;
    }

    public static WorkflowContentEnum getByCode(String code) {
        WorkflowContentEnum result = WorkflowContentEnum.empty;
        if (!StringUtil.isEmpty(code)) {
            WorkflowContentEnum[] array = values();
            for (WorkflowContentEnum workflowContentEnum : array) {
                if (code.equals(workflowContentEnum.toString())) {
                    result = workflowContentEnum;
                    break;
                }
            }
        }
        return result;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public IWorkflowTodoService getService() {
        return service;
    }

    public void setService(IWorkflowTodoService service) {
        this.service = service;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }}
