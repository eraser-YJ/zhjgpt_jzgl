package com.jc.csmp.project.dao.impl;

import com.jc.csmp.project.dao.ICmProjectPersonDao;
import com.jc.csmp.project.domain.CmProjectPerson;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-项目人员分配DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmProjectPersonDaoImpl extends BaseClientDaoImpl<CmProjectPerson> implements ICmProjectPersonDao {

	public CmProjectPersonDaoImpl(){}

}