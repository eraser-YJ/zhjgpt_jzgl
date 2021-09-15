package com.jc.csmp.plan.kit;

import com.jc.csmp.plan.domain.XyDic;
import com.jc.csmp.plan.domain.XyDicAgg;
import com.jc.system.dic.domain.Dic;

import java.util.*;
import java.util.stream.Collectors;

public class DicUtil {

    /**
     * 构建出树形结构
     *
     * @param dicList
     * @return
     */
    public static List<XyDic> buildList(List<Dic> dicList) {
        List<XyDic> dataList = new ArrayList<XyDic>();
        for (Dic dic : dicList) {
            XyDic newVo = new XyDic();
            newVo.setCode(dic.getCode());
            newVo.setValue(dic.getValue());
            newVo.setParentId(dic.getParentId());
            dataList.add(newVo);
        }
        return dataList;
    }
    /**
     * 取得数据
     *
     * @param typePath
     * @return
     */
    public static XyDic getType(XyDicAgg agg,List<Integer> typePath) {
        XyDic now = agg.getDicTree().get(typePath.get(0)-1);
        for(int i=1;i<typePath.size();i++){
            now = now.getNextDic().get(typePath.get(i)-1);
        }
        return now;
    }
    /**
     * 构建出树形结构
     *
     * @param dicList
     * @return
     */
    public static XyDicAgg buildTree(List<Dic> dicList) {
        List<XyDic> dataList = new ArrayList<XyDic>();
        for (Dic dic : dicList) {
            XyDic newVo = new XyDic();
            newVo.setCode(dic.getCode());
            newVo.setValue(dic.getValue());
            newVo.setParentId(dic.getParentId());
            dataList.add(newVo);
        }
        List<XyDic> xtList = filter("xt", dataList);
        Collections.sort(xtList, new Comparator<XyDic>() {
            @Override
            public int compare(XyDic t0, XyDic t1) {
                return t0.getCode().compareTo(t1.getCode());
            }
        });
        //构建树
        List<XyDic> xcList = assemblyTree(filter("xc", dataList));
        List<XyDic> xsList = filter("xs", dataList);
        for(XyDic dic:xsList){
            dic.setLeaf("Y");
        }
        Collections.sort(xsList, new Comparator<XyDic>() {
            @Override
            public int compare(XyDic t0, XyDic t1) {
                return t0.getCode().compareTo(t1.getCode());
            }
        });
        //添加到也在节点
        addToLeaf(xcList,xsList);
        //添加到也在节点
        addToLeaf(xtList,xcList);
        //重新构造树编码
        rebuildCode(xtList,"0",null);
        XyDicAgg resMap = new XyDicAgg();
        resMap.setDicTree(xtList);
        resMap.setXsDicList(xsList);
        return resMap;
    }

    /**
     * 添加到叶子节点
     * @param srcList
     * @param leafList
     * @return
     */
    private static void addToLeaf(List<XyDic> srcList, List<XyDic> leafList) {
        for (XyDic src : srcList) {
            if (src.getNextDic() == null || src.getNextDic().size() == 0) {
                src.setNextDic(deepClone(leafList));
            } else {
                addToLeaf(src.getNextDic(),leafList);
            }
        }
    }

    /**
     * 添加到叶子节点
     * @param nowList
     * @param preCode
     * @return
     */
    private static void rebuildCode(List<XyDic> nowList, String preCode, String preCodeName) {
        for (XyDic src : nowList) {
            src.setCode(preCode+"_"+src.getCode());
            src.setParentId(preCode);
            if(preCodeName!=null){
                src.setTreePathName(preCodeName+","+src.getValue());
            } else {
                src.setTreePathName(src.getValue());
            }

            if (src.getNextDic() != null&&src.getNextDic().size()>0) {
                rebuildCode(src.getNextDic(),src.getCode(),src.getTreePathName());
            }
        }
    }

    /**
     * 构建出树形结构
     *
     * @param dicList
     * @return
     */
    private static List<XyDic> assemblyTree(List<XyDic> dicList) {
        //<子，对象>
        Map<String, XyDic> map = dicList.stream().collect(Collectors.toMap(item -> item.getCode(), item -> item));
        //<父，子列表>
        Map<String, List<String>> parentMapList = new HashMap<>();
        //取得根
        LinkedList<String> stack = new LinkedList<String>();
        List<String> rootList = new ArrayList<String>();
        for (XyDic item : dicList) {
            List<String> itemList = parentMapList.get(item.getParentId());
            if (itemList == null) {
                itemList = new ArrayList<>();
            }
            itemList.add(item.getCode());
            parentMapList.put(item.getParentId(), itemList);
            //取得根
            if (!map.containsKey(item.getParentId())) {
                stack.add(item.getCode());
                rootList.add(item.getCode());
            }
        }

        String nowNode;
        XyDic nowDic;
        List<String> subCodes;
        while (stack.peek() != null) {
            nowNode = stack.pop();
            subCodes = parentMapList.get(nowNode);
            if (subCodes != null) {
                List<XyDic> subList = new ArrayList<XyDic>();
                for (String subCode : subCodes) {
                    if (map.containsKey(subCode)) {
                        subList.add(map.get(subCode));
                        stack.push(subCode);
                    }
                }
                //排序
                Collections.sort(subList, new Comparator<XyDic>() {
                    @Override
                    public int compare(XyDic t0, XyDic t1) {
                        return t0.getCode().compareTo(t1.getCode());
                    }
                });
                nowDic = map.get(nowNode);
                if (nowDic != null) {
                    nowDic.setNextDic(subList);
                }
            }
        }
        //排序
        Collections.sort(rootList);
        List<XyDic> resList = new ArrayList<XyDic>();
        for (String root : rootList) {
            resList.add(map.get(root));
        }
        return resList;
    }



    /**
     * 克隆
     *
     * @param dicList
     * @return
     */
    public static List<XyDic> clone(List<XyDic> dicList) {
        List<XyDic> dataList = new ArrayList<XyDic>();
        for (XyDic dic : dicList) {
            dataList.add(dic.clone());
        }
        return dataList;
    }

    /**
     * 克隆
     *
     * @param dicList
     * @return
     */
    public static List<XyDic> deepClone(List<XyDic> dicList) {
        List<XyDic> dataList = new ArrayList<XyDic>();
        for (XyDic dic : dicList) {
            dataList.add(dic.deepClone());
        }
        return dataList;
    }

    /**
     * 过滤
     *
     * @param type
     * @param dicList
     * @return
     */
    public static List<XyDic> filter(String type, List<XyDic> dicList) {
        List<XyDic> dataList = new ArrayList<XyDic>();
        for (XyDic dic : dicList) {
            if (dic.getCode().startsWith(type)) {
                dataList.add(dic);
            }
        }
        return dataList;
    }



}
