package com.jc.oa.click.service;

import java.util.List;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.DBException;
import com.jc.foundation.service.IBaseService;
import com.jc.oa.click.domain.Click;


public interface IClickService extends IBaseService<Click>{
	public Integer save(Click click) throws DBException;
	public List<Click> queryAll(Click click);
	public void clicks(String url) throws CustomException;
}
