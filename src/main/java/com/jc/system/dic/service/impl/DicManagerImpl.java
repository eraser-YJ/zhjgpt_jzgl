package com.jc.system.dic.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.cache.IDicCacheService;
import com.jc.system.dic.cache.impl.DicCacheServiceImpl;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.domain.DicType;
import com.jc.system.dic.service.IDicService;
import com.jc.system.dic.service.IDicTypeService;

/**
 * 字典接口实现类
 * @author Administrator
 * @date 2020-06-29
 */
@Service
@Lazy(true)
public class DicManagerImpl implements IDicManager {

	IDicCacheService cacheService = DicCacheServiceImpl.getInstance();

	IDicService dicService = SpringContextHolder.getBean(IDicService.class);
	IDicTypeService typeService = SpringContextHolder
			.getBean(IDicTypeService.class);

	private Logger log = Logger.getLogger(DicManagerImpl.class);

	@Override
	public void init() {
		cacheService.init();
	}

	/**
	 * @description 根据字典类型获得标志可以使用的字典
	 * @param typeCode
	 *            字典类型码
	 */
	@Override
    public List<Dic> getDicsByTypeCode(String typeCode) {
		return getDicsByTypeCode(typeCode,"-1");
	}
	
	/**
	 * @description 根据字典类型获得标志可以使用的字典
	 * @param typeCode
	 *            字典类型码
	 * @param parentCode
	 *            父类型码            
	 */
	@Override
    public List<Dic> getDicsByTypeCode(String typeCode, String parentCode) {
		return cacheService.getDicsByTypeCode(typeCode,parentCode);
	}

	/**
	 * @description 根据字典类型获得字典
	 * @param typeCode
	 *            字典类型吗
	 * @param parentCode
	 *            父类型码                        
	 * @param useFlag
	 *            true:返回标志启用的字典 false:返回所有（包括标志不可用）的字典
	 */
	@Override
    public List<Dic> getAllDicsByTypeCode(String typeCode, String parentCode, Boolean useFlag) {
		if (useFlag == false) {
			return cacheService.getDicsByTypeCodeAll(typeCode,parentCode);
		} else {
			return getDicsByTypeCode(typeCode,parentCode);
		}
	}

	/**
	 * @description 获得字典
	 * @param typeCode
	 *            字典类型code
	 * @param dicCode
	 *            字典code
	 */
	@Override
    public Dic getDic(String typeCode, String parentCode, String dicCode) {
		if(StringUtil.isEmpty(typeCode)||StringUtil.isEmpty(parentCode)||StringUtil.isEmpty(dicCode)){
			return null;
		}
		return cacheService.getDic(typeCode,parentCode, dicCode);
	}

	/**
	 * @description 获得字典类型类型
	 */
	@Override
    public DicType getDicType(String code, String parentCode) {
		return cacheService.getType(code,parentCode);
	}

	/**
	 * @description 获得所有类型
	 * @return 树形结构（子节点放入到children中）
	 */
	@Override
    public List<DicType> getDicTypes() {
		return cacheService.getTypes();
	}

	/**
	 * @description 新增字典类型
	 * @param dicType
	 *            字典类型
	 * @throws CustomException 
	 */
	@Override
    public DicType addNewDicType(DicType dicType) throws CustomException {
		Dic dicTemp = new Dic();
		dicTemp.setCode(dicType.getCode());
		dicTemp.setParentId(dicType.getParentId());
		// dicTemp.setDicFlag(1);
		if (dicService.query(dicTemp).size() > 0) {
			log.info("存在相同code的字典类型,字典类型为：" + dicType.getCode());
			return null;
		}
		return typeService.save(dicType);
	}

	/**
	 * @description 新增字典
	 * @param dic
	 *            新字典bean
	 * @throws CustomException 
	 */
	@Override
    public Dic addNewDic(Dic dic) throws CustomException {
		Dic dicTemp = new Dic();
		dicTemp.setCode(dic.getCode());
		dicTemp.setParentId(dic.getParentId());
		dicTemp.setParentType(dic.getParentType());
		// dicTemp.setDicFlag(1);
		if (dicService.query(dicTemp).size() > 0) {
			log.info("不存在传入code的字典,字典code为：" + dic.getCode());
			return null;
		}
		if (dic.getOrderFlag() == null) {
			dic.setOrderFlag(1);
		}
		dic.setDicFlag(1);
		dic.setTypeFlag(0);
		dic.setUseFlag(1);

		return dicService.saveDic(dic);
	}
	
	/**
	 * @description 批量新增字典
	 * @param dicList
	 *            新字典bean集合
	 * @throws CustomException 
	 */
	@Override
    public Integer addNewDicList(List<Dic> dicList) throws CustomException {
		if(dicList != null && dicList.size() > 0){
			Dic dic = new Dic();
			String parentId = "";
			String parentType = "";
			String ids = "";
			for(int i=0;i<dicList.size();i++){
				Dic dicTemp = dicList.get(i);
				parentId = dicTemp.getParentId();
				parentType = dicTemp.getParentType();
				if(dicTemp.getId() != null){
					if("".equals(ids)) {
                        ids = String.valueOf(dicTemp.getId());
                    } else {
                        ids = ids + "," + String.valueOf(dicTemp.getId());
                    }
					dicTemp.setModifyDate(DateUtils.getSysDate());
					dicService.update(dicTemp);
				} else{
					dicTemp.setCreateDate(DateUtils.getSysDate());
					dicTemp.setModifyDate(DateUtils.getSysDate());
					dicService.saveDic(dicTemp);
					if("".equals(ids)) {
                        ids = String.valueOf(dicTemp.getId());
                    } else {
                        ids = ids + "," + dicTemp.getId();
                    }
				}
			}
			dic.setPrimaryKeys(ids.split(","));
			dic.setParentId(parentId);
			dicService.delForDicList(dic);
			
			Dic redic = new Dic();
			redic.setParentId(parentId);
			redic.setParentType(parentType);
			redic.setDeleteFlag(1);
			List<Dic> rediclist = dicService.query(redic);
			if(rediclist != null && rediclist.size() > 0){
				for(int i=0;i<rediclist.size();i++){
					Dic redicTemp = rediclist.get(i);
					cacheService.refreshDicItem(redicTemp,redicTemp);
				}
			}
			
			return 1;
		}else {
			return 0;
		}
	}

	/**
	 * 删除字典
	 * @param dic 字典
	 * @return
	 * @throws CustomException
	 */
	@Override
    public Dic removeDic(Dic dic) throws CustomException {
		Dic dicTemp = new Dic();
		dicTemp.setCode(dic.getCode());
		dicTemp.setParentId(dic.getParentId());
		dicTemp.setParentType(dic.getParentType());
		dicTemp = dicService.get(dicTemp);
		if (dicTemp == null) {
			log.info("不存在传入code的字典,typeId为:" + dic.getParentId() + ",字典code为："
					+ dic.getCode());
			return null;
		}
		dicService.delete(dicTemp);
		return dicTemp;
	}


	/**
	 * @description 删除字典类型
	 * @param dicType
	 *            字典类型
	 * @throws CustomException 
	 */
	@Override
    public DicType removeDicType(DicType dicType) throws CustomException {
		DicType dicTypeTemp = new DicType();
		dicTypeTemp.setCode(dicType.getCode());
		dicTypeTemp.setParentId(dicType.getParentId());
		dicTypeTemp = typeService.get(dicTypeTemp);
		if (dicTypeTemp == null) {
			log.info("不存在传入code的字典类型,字典类型为：" + dicType.getCode());
			return null;
		}
		typeService.delete(dicTypeTemp);
		return dicTypeTemp;
	}

	/**
	 * 更新字典
	 * @param dic
	 * @return
	 * @throws CustomException
	 */
	@Override
    public Dic updateDic(Dic dic) throws CustomException {
		Dic dicTemp = new Dic();
		dicTemp.setId(dic.getId());
		dicTemp.setCode(dic.getCode());
		dicTemp.setParentType(dic.getParentType());
		dicTemp.setDicFlag(1);
		dicTemp = dicService.get(dicTemp);
		if (dicTemp == null) {
			log.info("不存在传入code的字典,typeId为:" + dic.getParentId() + ",字典code为："
					+ dic.getCode());
			return null;
		}
		dicService.update(dic);
		return dicTemp;
	}


	/**
	 * @description 更新字典类型
	 * @param dicType
	 *            字典类型
	 * @throws CustomException 
	 */
	@Override
    public DicType updateType(DicType dicType) throws CustomException {
		DicType dicTypeTemp = new DicType();
		dicTypeTemp.setId(dicType.getId());
		dicTypeTemp.setCode(dicType.getCode());
		dicTypeTemp.setParentId(dicType.getParentId());
		dicTypeTemp = typeService.get(dicTypeTemp);
		if (dicTypeTemp == null) {
			log.info("不存在传入code的字典类型,字典类型为：" + dicType.getCode());
			return null;
		}
		typeService.updateDicType(dicType);
		return dicTypeTemp;
	}

	/**
	 * @description 将字典转换为字典类型，以增加字典
	 * @param dic
	 *            .parentId 类型id
	 * @param dic
	 *            .code 字典code
	 * @throws CustomException 
	 */
	@Override
    public Dic changeDicToType(Dic dic) throws CustomException {
		String parentType = dic.getParentType();
		String typeCode = parentType.split("=")[0];
		String typeForParentCode = parentType.split("=")[1];
		Dic cacheDic = getDic(typeCode,typeForParentCode, dic.getCode());
		// 如果字典更新前不是类型需要更新类型
		if (cacheDic.getTypeFlag().equals(Dic.TYPE_FLAG_FALSE)) {
			if (getDicType(dic.getCode(),typeForParentCode) != null) {
				log.info("存在字典code相同的字典类型:" + cacheDic.toString() + ",转换失败");
				return null;
			} else {
				log.info("开始将字典转换为字典类型:" + cacheDic.toString());
				dic.setTypeFlag(Dic.TYPE_FLAG_TRUE);
				dicService.update(dic);
				log.info("字典转换为字典类型结束:" + cacheDic.toString());
			}
		} else {
			log.info("不需要转换字典类型:" + cacheDic.toString());
		}
		return getDic(parentType,typeForParentCode, dic.getCode());
	}

	@Override
    public Dic getDic(String dicId) {
		Dic dic = new Dic();
		dic.setId(dicId);
		return dicService.get(dic);
	}

}
