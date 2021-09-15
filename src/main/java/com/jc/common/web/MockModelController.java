package com.jc.common.web;

import com.jc.common.def.DefItemVO;
import com.jc.common.def.DefUtil;
import com.jc.common.def.DefVO;
import com.jc.common.domain.DicSearchVO;
import com.jc.common.kit.PathUtil;
import com.jc.common.kit.RequestUtil;
import com.jc.common.kit.vo.PageManagerEx;
import com.jc.common.service.IMockViewService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.web.BaseController;
import com.jc.system.common.util.CharConventUtils;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/mock/view/")
public class MockModelController extends BaseController {

    @Autowired
    private IMockViewService viewService;

    private static String resourcesPath = GlobalContext.getProperty("resource.path");


    //定义文件所在目录
    private String baseFile = "defview";


    /**
     * 列表显示
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "showListPage.action", method = RequestMethod.GET)
    public String showList(HttpServletRequest request) throws Exception {
        request.setAttribute("disPath", request.getParameter("disPath"));
        String page = request.getParameter("page");
        return PathUtil.trim(page);
    }

    /**
     * 卡片显示
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "showPage.action", method = RequestMethod.GET)
    public String show(HttpServletRequest request) throws Exception {
        request.setAttribute("id", request.getParameter("id"));
        request.setAttribute("disPath", request.getParameter("disPath"));
        String page = request.getParameter("page");
        return PathUtil.trim(page);
    }


    /**
     * 分页查询方法
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "getList.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public PageManagerEx<Map<String, Object>> getList(PageManager page, HttpServletRequest request) {
        try {
            //没有传地址
            String disPath = request.getParameter("disPath");
            DefVO def = DefUtil.getSQL(baseFile, disPath);
            if (def == null) {
                return new PageManagerEx<Map<String, Object>>();
            }
            PageManagerEx page_ = viewService.query(RequestUtil.getPara(request), def, page);
            GlobalUtil.setTableRowNoEx(page_, page_.getPageRows());
            return page_;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageManagerEx<Map<String, Object>>();
        }
    }


    /**
     * 单条查询
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "get.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> get(HttpServletRequest request) {
        try {
            //没有传地址
            String disPath = request.getParameter("disPath");
            DefVO def = DefUtil.getSQL(baseFile, disPath);
            if (def == null) {
                return new HashMap<String, Object>();
            }
            Map<String, Object> resMap = viewService.get(RequestUtil.getPara(request), def);
            return resMap;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<String, Object>();
        }
    }


    /**
     * @return String form对话框所在位置
     * @throws Exception
     * @description 弹出对话框方法
     * @author
     * @version 2020-04-10
     */
    @RequestMapping(value = "loadForm.action", method = {RequestMethod.GET, RequestMethod.POST})
    public String loadForm(Model model, HttpServletRequest request) throws Exception {
        try {
            //没有传地址
            String disPath = request.getParameter("disPath");
            DefVO def = DefUtil.getXmlAndSQL(baseFile, disPath);
            if (def == null) {
                return "error/unauthorized";
            }
            Map<String, Object> data = viewService.get(RequestUtil.getPara(request), def);
            List<DefItemVO> inList = def.getItemList();
            for (DefItemVO item : inList) {
                item.setItemValue(DefUtil.getItemShowValue(item, data.get(item.getItemCode())));
            }
            request.setAttribute("pageForm", DefUtil.assemblyFormTable(inList));
            return "view/dataForm";
        } catch (Exception e) {
            e.printStackTrace();
            return "error/unauthorized";
        }
    }


    @RequestMapping(value = "manageList.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public PageManagerEx<Map<String, Object>> manageList(PageManager page, HttpServletRequest request) {
        try {
            //没有传地址
            String disPath = request.getParameter("disPath");
            DefVO def = DefUtil.getXmlAndSQL(baseFile, disPath);
            if (def == null) {
                return new PageManagerEx<Map<String, Object>>();
            }
            PageManagerEx<Map<String, Object>> page_ = viewService.query(RequestUtil.getPara(request), def, page);
            List<Map<String, Object>> dataList = page_.getData();
            if (dataList != null && dataList.size() > 0) {
                List<DefItemVO> defList = DefUtil.assemblyListTable(def.getItemList());
                Map<String, DefItemVO> defMap = defList.stream().collect(Collectors.toMap(item -> item.getItemCode(), item -> item));
                for (Map<String, Object> itemData : dataList) {
                    //转换查询结果
                    for (Map.Entry<String, Object> entry : itemData.entrySet()) {
                        itemData.put(entry.getKey(), DefUtil.getItemShowValue(defMap.get(entry.getKey()), entry.getValue()));
                    }
                    //补充全部值
                    for (String defKey : defMap.keySet()) {
                        if (!itemData.containsKey(defKey)) {
                            itemData.put(defKey, "");
                        }
                    }
                }
            }
            return page_;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageManagerEx<Map<String, Object>>();
        }
    }


    @RequestMapping(value = "manage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public String manage(HttpServletRequest request) throws Exception {
        try {
            //没有传地址
            String disPath = request.getParameter("disPath");
            DefVO def = DefUtil.getXmlAndSQL(baseFile, disPath);
            if (def == null) {
                return "error/unauthorized";
            }
            request.setAttribute("disPath", disPath);
            request.setAttribute("def", def);
            request.setAttribute("pageList", DefUtil.assemblyListTable(def.getItemList()));
            request.setAttribute("pageCond", DefUtil.assemblyCondTable(def.getItemList()));
            return "view/dataList";
        } catch (Exception e) {
            e.printStackTrace();
            return "error/unauthorized";
        }
    }

    @RequestMapping(value = "dic.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<DicSearchVO> dic(HttpServletRequest request) {
        String dicContent = request.getParameter("dicContent");
        dicContent = dicContent.replaceAll("“", "\"");
        List<DicSearchVO> dataList = JsonUtil.json2Array(dicContent, DicSearchVO.class);
        if (dataList != null && dataList.size() > 0) {
            IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
            for (DicSearchVO dicSearch : dataList) {
                List<Dic> dicList = dicManager.getAllDicsByTypeCode(dicSearch.getDictName(), dicSearch.getParentCode(), true);
                if (dicList != null && dicList.size() > 0) {
                    //排序
                    Collections.sort(dicList, new Comparator<Dic>() {

                        @Override
                        public int compare(Dic t0, Dic t1) {
                            Integer seq0 = t0.getOrderFlag() == null ? 0 : t0.getOrderFlag();
                            Integer seq1 = t1.getOrderFlag() == null ? 0 : t1.getOrderFlag();
                            return seq0.compareTo(seq1);
                        }
                    });
                    List<Map<String, String>> dicResList = new ArrayList<>();
                    Map<String, String> map = null;
                    for (Dic nowDic : dicList) {
                        map = new HashMap<>();
                        map.put("code", nowDic.getCode());
                        map.put("value", nowDic.getValue());
                        dicResList.add(map);
                    }
                    dicSearch.setDicList(dicResList);
                }
            }
        }
        return dataList;
    }


    /**
     * 分页查询方法
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "attachList.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<Map<String, Object>> attachList(HttpServletRequest request) {
        List<Map<String, Object>> resMap = new ArrayList<>();
        try {
            String attachKey = request.getParameter("attachKey");
            String attachScope = request.getParameter("attachScope");
            if (attachKey == null || attachKey.trim().length() <= 0) {
                return resMap;
            }
            if (attachScope == null || attachScope.trim().length() <= 0) {
                return resMap;
            }
            DefVO def = DefUtil.getSQL(baseFile, "attach");
            if (def == null) {
                return resMap;
            }
            return viewService.getList(RequestUtil.getPara(request), def);
        } catch (Exception e) {
            e.printStackTrace();
            return resMap;
        }
    }

    @RequestMapping(value = "download.action", method = RequestMethod.GET)
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {

        String fileNameSrc = request.getParameter("fileName");
        String resourcesNameStr = request.getParameter("resourceName");
        String filename = CharConventUtils.encodingFileName(fileNameSrc);
        String resourcesName = resourcesNameStr;
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + filename + "\"");
        InputStream is = null;
        ServletOutputStream out = null;
        try {
            String root = PathUtil.trim(resourcesPath) + File.separatorChar + resourcesName;
            InputStream inputStream = null;
            OutputStream os = null;
            try {
                File file = new File(root);
                inputStream = new FileInputStream(file);
                os = response.getOutputStream();
                byte[] b = new byte[Integer.parseInt(GlobalContext.getProperty("STREAM_SLICE"))];
                int length;
                while ((length = inputStream.read(b)) > 0) {
                    os.write(b, 0, length);
                }
                os.flush();
            } catch (FileNotFoundException e) {
                log.error("下载文件不存在 path:" + root + resourcesPath + File.separatorChar + resourcesName);

            } catch (IOException e) {
                log.error("下载异常" + e);
            } finally {
                IOUtils.closeQuietly(os);
                IOUtils.closeQuietly(inputStream);
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(is);
        }

    }


}

