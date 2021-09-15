package com.jc.dlh.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jc.common.db.dialect.mysql.MysqlColumnType;
import com.jc.common.kit.ListTool;
import com.jc.dlh.dao.IDlhDatamodelItemDao;
import com.jc.dlh.domain.DlhDatamodelItem;
import com.jc.dlh.service.IDlhDatamodelItemService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;

/**
 * @title 统一数据资源中心
 * @description 业务服务类
 * @author lc 
 * @version 2020-03-10
 */
@Service
public class DlhDatamodelItemServiceImpl extends BaseServiceImpl<DlhDatamodelItem> implements IDlhDatamodelItemService {

	private IDlhDatamodelItemDao dlhDatamodelItemDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public DlhDatamodelItemServiceImpl() {
	}

	@Autowired
	public DlhDatamodelItemServiceImpl(IDlhDatamodelItemDao dlhDatamodelItemDao) {
		super(dlhDatamodelItemDao);
		this.dlhDatamodelItemDao = dlhDatamodelItemDao;
	}

	/**
	 * @description 根据主键删除多条记录方法
	 * @param DlhDatamodelItem dlhDatamodelItem 实体类
	 * @return Integer 处理结果
	 * @author lc 
	 * @version 2020-03-10
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer deleteByIds(DlhDatamodelItem dlhDatamodelItem) throws CustomException {
		Integer result = -1;
		try {
			String[] ids = dlhDatamodelItem.getPrimaryKeys();
			// 判断是否被引用
			String idsStr = ListTool.joinSQLString(Arrays.asList(ids));
			String sqlExists = "select count(*) from busi_dlh_dataobject_field where item_id in (" + idsStr + ")";
			Integer value = jdbcTemplate.queryForObject(sqlExists, Integer.class);
			if (value != null && value > 0) {
				throw new Exception("已经被引用不能删除！");
			}
			result = dlhDatamodelItemDao.delete(dlhDatamodelItem, false);
		} catch (Exception e) {
			e.printStackTrace();
			CustomException ce = new CustomException(e.getMessage());
			ce.setBean(dlhDatamodelItem);
			throw ce;
		}
		return result;
	}

	/**
	 * @param file,request
	 * @return Map<String, Object> 处理结果
	 * @description 导入excel
	 * @author lc 
	 * @version 2020-03-19
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Map<String, Object> importExcel(MultipartFile file, HttpServletRequest request) throws Exception {
		String modelId = request.getParameter("modelId");
//		System.out.println("modelId:"+modelId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Workbook workbook = null;
			if (file.getOriginalFilename().endsWith("xlsx")) {
				// 处理excel2007
				workbook = new XSSFWorkbook(file.getInputStream());
				resultMap = readExcel(workbook, modelId);
			} else {
				// 处理excel2003
				workbook = new HSSFWorkbook(file.getInputStream());
				resultMap = readExcel(workbook, modelId);
			}
			if (resultMap.get("dataList") != null) {
				List<DlhDatamodelItem> dlhDatamodelItemList = (List<DlhDatamodelItem>) resultMap.get("dataList");
				for (DlhDatamodelItem dlhDatamodelItem : dlhDatamodelItemList) {
					this.propertyService.fillProperties(dlhDatamodelItem, false);
					dlhDatamodelItem.setModelId(modelId);
					this.dlhDatamodelItemDao.save(dlhDatamodelItem);
				}
				resultMap.put("success", "true");
				return resultMap;
			} else {
				return resultMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/*
	 * 读取excel
	 */
	public Map<String, Object> readExcel(Workbook workbook, String modelId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 实例化实体list
		List<DlhDatamodelItem> dlhDatamodelItemList = new ArrayList<DlhDatamodelItem>();
		Sheet sheet = workbook.getSheetAt(0);
		Row row;
		Cell cell;
		for (int i = sheet.getFirstRowNum() + 1, rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows() - 1; i++) {
			row = sheet.getRow(i);
			rowCount++;
			// 实例化实体
			DlhDatamodelItem dlhDatamodelItem = new DlhDatamodelItem();
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					continue;
				}
				// 列名(必填项,最大长度64)
				if (j == 0) {
					// 判断是否为空单元格
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列名不得为空");
						resultMap.put("success", "false");
						return resultMap;
					}
					// 正常String格式
					else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						String xm = cell.getStringCellValue();
						xm = xm.trim();
						if ("".equals(xm)) {
							resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列名不得为空");
							resultMap.put("success", "false");
							return resultMap;
						} else if (xm.length() > 64) {
							resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列名最大长度为64");
							resultMap.put("success", "false");
							return resultMap;
						} else {
							// 判断是否已经存在列名
							dlhDatamodelItem.setItemName(xm);
							dlhDatamodelItem.setDeleteFlag(0);
							dlhDatamodelItem.setModelId(modelId);
							List<DlhDatamodelItem> list = dlhDatamodelItemDao.queryAll(dlhDatamodelItem);
							if (list.size() > 0) {
								resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列名已经存在");
								resultMap.put("success", "false");
								return resultMap;
							}
							dlhDatamodelItem.setItemName(xm);
						}
					}
				}
				// 列描述(必填项,最大长度200)
				else if (j == 1) {
					// 判断是否为空单元格
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列描述不得为空");
						resultMap.put("success", "false");
						return resultMap;
					}
					// 正常String格式
					else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						String itemComment = cell.getStringCellValue();
						itemComment = itemComment.trim();
						if (!"".equals(itemComment)) {
							if ("".equals(itemComment)) {
								resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列描述不得为空");
								resultMap.put("success", "false");
								return resultMap;
							} else if (itemComment.length() > 200) {
								resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列描述最大长度为200");
								resultMap.put("success", "false");
								return resultMap;
							} else {
								dlhDatamodelItem.setItemComment(itemComment);
							}
						}
					}
				}
				// 列类型(必填项,最大长度64)
				else if (j == 2) {
					// 判断是否为空单元格
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列类型不得为空");
						resultMap.put("success", "false");
						return resultMap;
					}
					// 正常String格式
					else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						String itemType = cell.getStringCellValue();
						itemType = itemType.trim();
						if (!"".equals(itemType)) {
							if ("".equals(itemType)) {
								resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列类型不得为空");
								resultMap.put("success", "false");
								return resultMap;
							} else if (MysqlColumnType.getType(itemType) == null) {
								resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列类型不合法");
								resultMap.put("success", "false");
								return resultMap;
							} else if (itemType.length() > 64) {
								resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列类型最大长度为64");
								resultMap.put("success", "false");
								return resultMap;
							} else {
								dlhDatamodelItem.setItemType(itemType);
							}
						}
					}
				}
				// 列长度(最大长度64，格式为数字类型)
				else if (j == 3) {
					// 判断是否为数字格式
					if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						double itemLen = cell.getNumericCellValue();
						long longItemLen = (long) itemLen;
						String zyzh = String.valueOf(longItemLen);
						if (zyzh.length() > 64) {
							resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列长度最大长度为64");
							resultMap.put("success", "false");
							return resultMap;
						} else {
							dlhDatamodelItem.setItemLen(zyzh);
						}
					}
					// 正常String格式
					else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						String zyzh = cell.getStringCellValue();
						zyzh = zyzh.trim();
						if (!"".equals(zyzh)) {
							if (!Pattern.matches("/^[1-9]+[0-9]*$/", zyzh)) {
								resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列长度只能输入数字");
								resultMap.put("success", "false");
								return resultMap;
							} else if (zyzh.length() > 64) {
								resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 列长度最大长度为64");
								resultMap.put("success", "false");
								return resultMap;
							} else {
								dlhDatamodelItem.setItemLen(zyzh);
							}
						}
					}
				}
				// 是否主键(最大长度200)
				else if (j == 4) {
					// 数字格式
					if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						String itemKey = cell.getStringCellValue();
						itemKey = itemKey.trim();
						if (!"".equals(itemKey)) {
							if (itemKey.length() > 200) {
								resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 是否主键最大长度为200");
								resultMap.put("success", "false");
								return resultMap;
							} else {
								dlhDatamodelItem.setItemKey(itemKey);
							}
						}
					}
				}
				// 字典编码
				else if (j == 5) {
					// 正常String格式
					if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						String dicCode = cell.getStringCellValue();
						dicCode = dicCode.trim();
						if (!"".equals(dicCode)) {
							if (dicCode.length() > 64) {
								resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 排序最大长度为64");
								resultMap.put("success", "false");
								return resultMap;
							} else {
								dlhDatamodelItem.setDicCode(dicCode);
							}
						}
					}
				}
				// 排序
				else if (j == 6) {
					// 判断是否为空单元格
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 排序不得为空");
						resultMap.put("success", "false");
						return resultMap;
					}
					// 判断是否为数字格式
					if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						double itemSeq = cell.getNumericCellValue();
						long longItemSeq = (long) itemSeq;
						String zyzh = String.valueOf(longItemSeq);
						if (zyzh.length() > 10) {
							resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 排序最大长度为10");
							resultMap.put("success", "false");
							return resultMap;
						} else {
							dlhDatamodelItem.setItemSeq(Integer.parseInt(zyzh));
						}
					}
					// 正常String格式
					else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						String itemSeq = cell.getStringCellValue();
						itemSeq = itemSeq.trim();
						if (!"".equals(itemSeq)) {
							if (!Pattern.matches("/^[1-9]+[0-9]*$/", itemSeq)) {
								resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 排序只能输入数字");
								resultMap.put("success", "false");
								return resultMap;
							} else if (itemSeq.length() > 10) {
								resultMap.put("errorMsg", "第" + (i + 1) + "行" + " 排序最大长度为10");
								resultMap.put("success", "false");
								return resultMap;
							} else {
								dlhDatamodelItem.setItemSeq(Integer.parseInt(itemSeq));
							}
						}
					}
				}
			}
			// 添加当前行实体类到list中
			dlhDatamodelItemList.add(dlhDatamodelItem);
		}
		// 判断列名、排序有重复项
		for (int i = 0; i < dlhDatamodelItemList.size() - 1; i++) {
			DlhDatamodelItem dlhDatamodelItem = (DlhDatamodelItem) dlhDatamodelItemList.get(i);
			String itemName = dlhDatamodelItem.getItemName();
			Integer itemSeq = dlhDatamodelItem.getItemSeq();
			for (int j = i + 1; j < dlhDatamodelItemList.size(); j++) {
				DlhDatamodelItem dlhDatamodelItem1 = (DlhDatamodelItem) dlhDatamodelItemList.get(j);
				if (itemName.equals(dlhDatamodelItem1.getItemName())) {
					resultMap.put("errorMsg", "第" + (i + 2) + "行与第" + (j + 2) + "行 列名称重复");
					resultMap.put("success", "false");
					return resultMap;
				}
				if (itemSeq == dlhDatamodelItem1.getItemSeq()) {
					resultMap.put("errorMsg", "第" + (i + 2) + "行与第" + (j + 2) + "行 排序重复");
					resultMap.put("success", "false");
					return resultMap;
				}
			}
		}
		resultMap.put("dataList", dlhDatamodelItemList);
		return resultMap;
	}
}