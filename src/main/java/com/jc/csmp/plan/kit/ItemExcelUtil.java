package com.jc.csmp.plan.kit;

import com.jc.csmp.plan.domain.ProjectYearPlanItem;
import com.jc.csmp.plan.domain.XyDic;
import com.jc.csmp.plan.domain.XyDicAgg;
import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemExcelUtil {
    /**
     * 取得分类
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static List<Integer> getTypePath(String str) throws Exception {
        try {
            String typeStr = str.trim();
            if (typeStr.startsWith("（") && typeStr.indexOf("）") > 0) {
                typeStr = typeStr.substring(1, typeStr.indexOf("）"));
            }
            if (typeStr.startsWith("(") && typeStr.indexOf(")") > 0) {
                typeStr = typeStr.substring(1, typeStr.indexOf(")"));
            }
            String[] typeList = typeStr.split("\\.");
            List<Integer> resList = new ArrayList<>();
            for (String type : typeList) {
                resList.add(Integer.valueOf(type));
            }
            return resList;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查并构建
     *
     * @param excelData
     * @return
     * @throws Exception
     */
    public static List<ProjectYearPlanItem> build(String[][] excelData, XyDicAgg agg) throws Exception {
        ProjectYearPlanItem data = null;
        List<ProjectYearPlanItem> dataList = new ArrayList<ProjectYearPlanItem>();
        for (String[] row : excelData) {
            data = new ProjectYearPlanItem();
            boolean hasData = false;
            if (row.length > 0 && trim(row[0]) != null) {
                hasData = true;
                String tempInfo = trim(row[0]);
                if (tempInfo == null || tempInfo.length() == 0) {
                    throw new Exception("序号不能为空！");
                }
                data.setTempInfo(tempInfo);
            } else {
                throw new Exception("序号不能为空！");
            }

            String projectName = trim(row[1]);
            if (projectName != null && projectName.length() > 0) {
                if (projectName.length() > 200) {
                    throw new Exception("项目名称超出长度限制！");
                }
                data.setProjectName(projectName);
            } else {
                //项目为空，默认就是分类
                //取得分类编码
                List<Integer> typePath = getTypePath(data.getTempInfo());
                if (typePath == null) {
                    throw new Exception("项目名称不能为空！");
                }
                XyDic dic = DicUtil.getType(agg, typePath);
                if (dic == null) {
                    throw new Exception("项目名称不能为空");
                }
                data.setProjectType(dic.getCode());
                data.setProjectTypeName(dic.getTreePathName());
                dataList.add(data);
                continue;
            }


            if (row.length > 2 && trim(row[2]) != null) {
                String projectTotalInvest = trim(row[2]);
                if (projectTotalInvest == null || projectTotalInvest.length() == 0) {
                    throw new Exception("总投资不能为空！");
                } else {
                    Double value = 0d;
                    try {
                        value = Double.valueOf(projectTotalInvest);
                    } catch (Exception ex) {
                    }
                    if (value < 0.01d) {
                        throw new Exception("总投资必须大于0.01的数字！");
                    } else if (value > 999999d) {
                        throw new Exception("总投资输入值过大！");
                    }
                    data.setProjectTotalInvest(BigDecimal.valueOf(value));
                }
            } else {
                throw new Exception("总投资不能为空！");
            }

            if (row.length > 3 && trim(row[3]) != null) {
                String projectNowInvest = trim(row[3]);
                if (projectNowInvest == null || projectNowInvest.length() == 0) {
                    throw new Exception("计划投资不能为空！");
                } else {
                    Double value = 0d;
                    try {
                        value = Double.valueOf(projectNowInvest);
                    } catch (Exception ex) {
                    }
                    if (value < 0.01d) {
                        throw new Exception("计划投资必须大于0.01的数字！");
                    } else if (value > 999999d) {
                        throw new Exception("计划投资输入值过大！");
                    }
                    data.setProjectNowInvest(BigDecimal.valueOf(value));
                }
            } else {
                throw new Exception("计划投资不能为空！");
            }

            if (row.length > 4 && trim(row[4]) != null) {
                String projectUsedInvest = trim(row[4]);
                if (projectUsedInvest == null || projectUsedInvest.length() == 0) {
                    throw new Exception("已投资不能为空！");
                } else {
                    Double value = 0d;
                    try {
                        value = Double.valueOf(projectUsedInvest);
                    } catch (Exception ex) {
                    }
                    if (value < 0.01d) {
                        throw new Exception("已投资必须大于0.01的数字！");
                    } else if (value > 999999d) {
                        throw new Exception("已投资输入值过大！");
                    }
                    data.setProjectUsedInvest(BigDecimal.valueOf(value));
                }
            } else {
                throw new Exception("已投资不能为空！");
            }

            if (row.length > 5 && trim(row[5]) != null) {
                String projectAfterInvest = trim(row[5]);
                if (projectAfterInvest == null || projectAfterInvest.length() == 0) {
                    throw new Exception("以后投资不能为空！");
                } else {
                    Double value = 0d;
                    try {
                        value = Double.valueOf(projectAfterInvest);
                    } catch (Exception ex) {
                    }
                    if (value < 0.01d) {
                        throw new Exception("以后投资必须大于0.01的数字！");
                    } else if (value > 999999d) {
                        throw new Exception("以后投资输入值过大！");
                    }
                    data.setProjectAfterInvest(BigDecimal.valueOf(value));
                }
            } else {
                throw new Exception("以后投资不能为空！");
            }

            if (row.length > 6 && trim(row[6]) != null) {
                String totalDay = trim(row[6]);
                if (totalDay == null || totalDay.length() == 0) {
                    throw new Exception("工期不能为空！");
                } else {
                    Integer value = null;
                    try {
                        value = Integer.valueOf(totalDay);
                    } catch (Exception ex) {
                    }
                    if (value == null) {
                        throw new Exception("工期输入值非数字");
                    }
                    data.setProjectTotalDay(value);
                }
            } else {
                throw new Exception("工期不能为空！");
            }

            if (row.length > 7 && trim(row[7]) != null) {
                String projectNowDesc = trim(row[7]);
                if (projectNowDesc == null || projectNowDesc.length() == 0) {
                    throw new Exception("主要工作内容不能为空！");
                } else if (projectNowDesc.length() > 200) {
                    throw new Exception("主要工作内容过长！");

                }
                data.setProjectNowDesc(projectNowDesc);
            }

            if (row.length > 8 && trim(row[8]) != null) {
                String dutyCompany = trim(row[8]);
                if (dutyCompany == null || dutyCompany.length() == 0) {
                    throw new Exception("责任部门不能为空！");
                } else if (dutyCompany.length() > 100) {
                    throw new Exception("责任部门内容过长！");

                }
                data.setDutyCompany(dutyCompany);
            }

            if (row.length > 9 && trim(row[9]) != null) {
                String dutyPerson = trim(row[9]);
                if (dutyPerson == null || dutyPerson.length() == 0) {
                    throw new Exception("责任人不能为空！");
                } else if (dutyPerson.length() > 100) {
                    throw new Exception("责任人内容过长！");

                }
                data.setDutyPerson(dutyPerson);
            }

            if (row.length > 10 && trim(row[10]) != null) {
                String projectDesc = trim(row[10]);
                if (projectDesc == null || projectDesc.length() == 0) {
                    throw new Exception("实施必要性不能为空！");
                } else if (projectDesc.length() > 200) {
                    throw new Exception("实施必要性内容过长！");

                }
                data.setProjectDesc(projectDesc);
            }
            if (row.length > 11 && trim(row[11]) != null) {
                String remark = trim(row[11]);
                if (remark != null && remark.length() > 200) {
                    throw new Exception("备注内容过长！");
                }
                data.setRemark(remark);
            }
            if (hasData) {
                dataList.add(data);
            }
        }
        return dataList;
    }

    private static Date toDate(String dateStr) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.parse(dateStr);
    }

    private static String trim(String data) {
        if (data == null || data.trim().length() == 0) {
            return null;
        }
        return data.trim();
    }

    private static String trimDate(String cellValue) {
        String date = cellValue;
        if (date != null && date.length() > 0) {
            if (date.endsWith("00:00:00")) {
                date = date.substring(0, date.length() - 9);
                date = date.trim();
            }
        }
        return date;
    }
}
