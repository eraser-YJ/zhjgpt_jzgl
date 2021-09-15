package com.jc.csmp.video.snapshot.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.csmp.video.snapshot.dao.IProjectVideoSnapshotDao;
import com.jc.csmp.video.snapshot.domain.ProjectVideoSnapshot;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title   
 * @version
 */
@Repository
public class ProjectVideoSnapshotDaoImpl extends BaseClientDaoImpl<ProjectVideoSnapshot> implements IProjectVideoSnapshotDao{

	public ProjectVideoSnapshotDaoImpl(){}

}