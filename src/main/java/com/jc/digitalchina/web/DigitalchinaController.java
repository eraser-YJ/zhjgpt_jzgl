package com.jc.digitalchina.web;

import com.jc.csmp.message.domain.CmMessageInfo;
import com.jc.csmp.message.service.ICmMessageInfoService;
import com.jc.csmp.workflow.domain.TodoExtendBean;
import com.jc.csmp.workflow.enums.WorkflowContentEnum;
import com.jc.csmp.workflow.enums.service.IWorkflowTodoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 神州数码数据对接接口
 * @Author 常鹏
 * @Date 2020/8/28 9:15
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/digital/api")
public class DigitalchinaController extends BaseController {
    @Autowired
    private IUserService userService;
    @Autowired
    private ICmMessageInfoService cmMessageInfoService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取菜单
     * @param loginName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "menu/{loginName}", method = RequestMethod.GET)
    @ResponseBody
    public Object selectMenu(@PathVariable String loginName) throws Exception{
        User user = new User();
        user.setLoginName(loginName);
        user = this.userService.getUser(user);
        Map<String, Object> result = new HashMap<>(2);
        if (user == null) {
            result.put("menus", Collections.EMPTY_LIST);
            result.put("authed_menus", Collections.EMPTY_LIST);
            return result;
        }
        String ruleMenuSql = "select distinct menu.id menuId from tty_sys_menu menu join tty_sys_role_menu roleMenu on roleMenu.menu_id = menu.id join tty_sys_user_role roleUser on roleUser.ROLE_ID = roleMenu.ROLE_ID join tty_sys_user user on user.id = roleUser.USER_ID where user.id = ? and menu.PARENT_ID = ? and menu.DELETE_FLAG = 0 order by menu.QUEUE";
        String oneLevelSql = "select id, NAME menuName, EXT_STR4 menuUrl, EXT_STR5 menuIcon from tty_sys_menu where parent_id = '1' and delete_flag = 0 and id not in ('9','329','f9fc46fa747d11eaa73a0050569e19c2','917935ce0eb1416c9724f992bf5540ef') order by QUEUE";
        List<Map<String, Object>> authMenuList = this.jdbcTemplate.queryForList(ruleMenuSql, new Object[]{user.getId(), "1"});
        Map<String, Integer> authMenu = new HashMap<>(1);
        if (authMenuList != null) {
            for (Map<String, Object> map : authMenuList) {
                authMenu.put((String) map.get("menuId"), 1);
            }
        }
        List<Map<String, Object>> allMenuList = this.jdbcTemplate.queryForList(oneLevelSql);
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Integer> authedMenus = new ArrayList<>();
        int index = 0;
        for (Map<String, Object> map : allMenuList) {
            index++;
            if (authMenu.containsKey((String) map.get("id"))) {
                authedMenus.add(index);
            }
            Map<String, Object> menuMap = new HashMap<>(4);
            menuMap.put("id", index);
            menuMap.put("name", map.get("menuName"));
            menuMap.put("icon", "/images/menu/" + map.get("menuIcon"));
            menuMap.put("url", map.get("menuUrl"));
            resultList.add(menuMap);
        }
        result.put("menus", resultList);
        result.put("authed_menus", authedMenus);
        return result;
    }

    /**
     * 我的待办
     * @return
     */
    @RequestMapping(value="todoList", method = RequestMethod.GET)
    @ResponseBody
    public Object todoList(@RequestParam("ln") String loginName, @RequestParam(value = "p",required = false) Integer page, @RequestParam(value = "pr",required = false) Integer pageRows) throws Exception{
        User user = new User();
        user.setLoginName(loginName);
        user = this.userService.getUser(user);
        if (user == null) {
            return null;
        }
        PageManager pageManager = new PageManager();
        page = page == null ? 0 : page;
        pageRows = pageRows == null ? 10 : pageRows;
        pageManager.setPage(page);
        pageManager.setPageRows(pageRows);
        CmMessageInfo entity = new CmMessageInfo();
        entity.setCurUserId(user.getId());
        PageManager page_ = this.cmMessageInfoService.workflowTodoQuery(entity, pageManager);
        GlobalUtil.setExtendInfomation(page_);
        Map<String, Object> resultMap = new HashMap<>(5);
        resultMap.put("page", page_.getPage());
        resultMap.put("pageRows", page_.getPageRows());
        resultMap.put("totalCount", page_.getTotalCount());
        resultMap.put("totalPages", page_.getTotalPages());
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<CmMessageInfo> messageList = (List<CmMessageInfo>) page_.getData();
        int no = page_.getPage() * pageRows + 1;
        if (page_.getPage() != 0) {
            no = (page_.getPage() - 1) * pageRows + 1;
        }
        if (messageList != null) {
            for (CmMessageInfo obj : messageList) {
                Map<String, Object> map = new HashMap<>(5);
                map.put("no", no++);
                map.put("type", obj.getWorkflowTitleValue());
                map.put("time", obj.getWorkflowCreateTime());
                map.put("handleUrl", "/third/login.action?to=todoListHandle," + obj.getWorkflowMenuId() + "," + obj.getWorkflowBean().getBusiness_Key_());
                String title = "";
                IWorkflowTodoService service = WorkflowContentEnum.getByCode(obj.getWorkflowTitle()).getService();
                if (service != null) {
                    TodoExtendBean extend = service.getByPid(obj.getWorkflowBean().getBusiness_Key_());
                    if (extend != null) {
                        title = extend.getName();
                    }
                }
                map.put("title", title);
                dataList.add(map);
            }
        }
        resultMap.put("data", dataList);
        return resultMap;
    }
}
