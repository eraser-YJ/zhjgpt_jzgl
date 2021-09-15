package com.jc.system.content.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.Result;
import com.jc.system.security.domain.User;
import com.jc.foundation.domain.Attach;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface IUploadService {

	/**
	 * 文件上传接口
	 * @param request
	 * @param category
	 * @return Map<"code",状态值> 200 正常 500 异常 <"id",插入成功后的数据库编号> 成功返回编号 不成功返回空
	 */
	Attach uploadFile(HttpServletRequest request ,String category);

	/**
	 * 文件上传接口
	 * @param request
	 * @param category
	 * @param grade
	 * @return Map<"code",状态值> 200 正常 500 异常 <"id",插入成功后的数据库编号> 成功返回编号 不成功返回空
	 */
	Attach uploadFile(HttpServletRequest request ,String category,String grade);

	/**
	 * 文件上传接口
	 * @param request
	 * @param category
	 * @param user
	 * @return Map<"code",状态值> 200 正常 500 异常 <"id",插入成功后的数据库编号> 成功返回编号 不成功返回空
	 */
	Attach uploadFile(HttpServletRequest request ,String category , User user);

	/**
	 * 文件下载接口
	 * @param uploadtype 下载目标 是 ftp 还是相对项目路径 uploadtype="" 相对项目路径 uploadtype="ftp",为上传到ftp 请注意上传ftp的配置文件是否存在
	 * @param filename 文件名
	 * @param resourcesname 相对文件路径和文件名
	 * @param response 请求响应
	 * @param request 请求对象
	 */
	void downloadFile(String uploadtype, String filename,String resourcesname, HttpServletResponse response, HttpServletRequest request);

	/**
	 * 删除已上传的文件以及数据库实体记录
	 * @param ids
	 * @param request
	 * @return
	 */
	Boolean deleteDocumentAttach(String ids,HttpServletRequest request);

	/**
	 * 删除已上传的文件以及数据库实体记录
	 * @param ids
	 * @return
	 */
	Boolean deleteFileByIds(String ids);

	/**
	 * 删除用户头像
	 * @param userId
	 * @return
	 */
	Boolean deleteUserPhoto(String userId);

	/**
	 * 删除附件
	 * @param id
	 * @throws CustomException
	 */
	void deleteAttach(String id) throws CustomException;

	/**
	 * 获取缩略图
	 * @param id
	 * @param request
	 * @param response
	 * @throws CustomException
	 */
	void getThumbnail(String id,HttpServletRequest request, HttpServletResponse response) throws CustomException;

	/**
	 * 获取图片
	 * @param id
	 * @param request
	 * @param response
	 * @throws CustomException
	 */
	void getOriginalImg(String id,HttpServletRequest request, HttpServletResponse response) throws CustomException;

	/**
	 * 根据路径加载附件
	 * @param path
	 * @param request
	 * @param response
	 */
	void getOriginalImgByPath(String path, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 获取附件
	 * @param id
	 * @param type
	 * @param subFile
	 * @param request
	 * @param response
	 * @throws CustomException
	 */
	void getOriginalFile(String id, String type, String subFile, HttpServletRequest request, HttpServletResponse response) throws CustomException;

	/**
	 * 拷贝
	 * @param request
	 * @param list
	 * @return
	 */
	List<Attach> copyAttachList(HttpServletRequest request, List<Attach> list);

	/**
	 * 拷贝
	 * @param request
	 * @param list
	 * @param businessId
	 * @return
	 */
	List<Attach> copyAttachAndTextList(HttpServletRequest request, List<Attach> list, String businessId);

	/**
	 * 根据附件业务信息管理附件
	 * @param buesinessId
	 * @param buesinessTable
	 * @param addFileids 新增附件ids
	 * @param delFileids 删除附件ids
	 * @param isEdit 附件状态标识 1只读 0可编辑
	 * @return
	 */
	Integer managerForAttach(String buesinessId,String buesinessTable,String addFileids,String delFileids,int isEdit);


	/**
	 * 根据附件业务信息管理附件
	 * @param buesinessId
	 * @param buesinessTable
	 * @param addFileids 新增附件ids
	 * @param delFileids 删除附件ids
	 * @param isEdit 附件状态标识 1只读 0可编辑
	 * @return
	 */
	Integer managerForAttach(Long buesinessId,String buesinessTable,String addFileids,String delFileids,String isEdit);

	/**
	 * 根据附件业务信息管理附件(报表用接口)
	 * @param buesinessId
	 * @param buesinessTable
	 * @param addFileids 新增附件ids
	 * @param delFileids 删除附件ids
	 * @param isEdit 附件状态标识 1只读 0可编辑
	 * @return
	 */
	Integer managerForAttach(String buesinessId,String buesinessTable,String addFileids,String delFileids,String isEdit);

	/**
	 * 根据附件业务信息管理附件
	 * @param buesinessId
	 * @param buesinessTable
	 * @param addFileids 新增附件ids
	 * @param delFileids 删除附件ids
	 * @param isEdit 附件状态标识 1只读 0可编辑
	 * @param secrets
	 * @return
	 */
	Integer managerForAttach(Long buesinessId,String buesinessTable,String addFileids,String delFileids,String isEdit, String secrets);

	/**
	 * 上传
	 * @param mf
	 * @param category
	 * @param request
	 * @return
	 */
	Attach upload(MultipartFile mf, String category, HttpServletRequest request);

	/**
	 * 删除图片
	 * @param busId
	 * @param busTable
	 * @return
	 */
	Result deleteBusinessPhoto(String busId, String busTable);

	/**
	 * 保存业务影响关系
	 * @param busId： 业务id
	 * @param busTable： 业务表名
	 * @param fileId： 影响id
	 * @return
	 * @throws CustomException
	 */
	Result saveBusinessPhoto(String busId, String busTable, String fileId) throws CustomException;
}
