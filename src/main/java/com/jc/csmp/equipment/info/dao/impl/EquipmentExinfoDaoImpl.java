package com.jc.csmp.equipment.info.dao.impl;

import com.jc.csmp.doc.common.IdUtil;
import org.springframework.stereotype.Repository;

import com.jc.csmp.equipment.info.dao.IEquipmentExinfoDao;
import com.jc.csmp.equipment.info.domain.EquipmentExinfo;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title
 */
@Repository
public class EquipmentExinfoDaoImpl extends BaseClientDaoImpl<EquipmentExinfo> implements IEquipmentExinfoDao {

    public EquipmentExinfoDaoImpl() {
    }

    public Integer save(EquipmentExinfo o) throws DBException {
        Integer result = -1;

        try {
            if (o.getId() == null) {
                o.setId(IdUtil.createId());
            }
            result = this.getTemplate().insert(this.getNameSpace(o) + "." + "insert", o);
            return result;
        } catch (Exception var5) {
            this.log.error(var5, var5);
            DBException exception = new DBException(var5);
            exception.setLogMsg("数据库添加数据发生错误");
            throw exception;
        }
    }

}