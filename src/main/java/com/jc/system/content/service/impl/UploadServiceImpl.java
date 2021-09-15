package com.jc.system.content.service.impl;

import com.jc.foundation.domain.Attach;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.system.common.util.CharConventUtils;
import com.jc.system.common.util.FileUtil;
import com.jc.system.common.util.FtpUploader;
import com.jc.system.common.util.ZipFile;
import com.jc.system.content.domain.AttachBusiness;
import com.jc.system.content.service.IAttachBusinessService;
import com.jc.system.content.service.IAttachService;
import com.jc.system.content.service.IUploadService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class UploadServiceImpl extends BaseServiceImpl<BaseBean> implements IUploadService {
	private static List<String> imgExt = new ArrayList<>();
	static {
		imgExt.add("png"); imgExt.add("jpg"); imgExt.add("bmp"); imgExt.add("jpeg"); imgExt.add("jpe"); imgExt.add("gif");
	}
	private String uploadType = GlobalContext.getProperty("FILE_TRANS_TYPE");
	private boolean absolutePath = Boolean.parseBoolean(GlobalContext.getProperty("ABSOLUTE_PATH"));
	public String getUploadBaseDir() {
		return String.valueOf(GlobalContext.getProperty("FILE_PATH"));
	}
	public String getDeleteLogic(){return String.valueOf(GlobalContext.getProperty("DELETE_ATTACH_LOGIC"));}
	public int getAttachSize() {
		String attachsize = GlobalContext.getProperty("ATTACH_SIZE") == null || "".equals(GlobalContext.getProperty("ATTACH_SIZE")) ? "524288000" : GlobalContext.getProperty("ATTACH_SIZE");
		return Integer.parseInt(attachsize);
	}

	@Autowired
	private IAttachService attachService ;
	@Autowired
	private IAttachBusinessService attachBusinessService;
	private String photoUrl = "/api/user/syncphoto";

	@Override
	public Attach uploadFile(HttpServletRequest request, String category) {
		User user = SystemSecurityUtils.getUser();
		return uploadFile(request, category, user);
	}

	@Override
	public Attach uploadFile(HttpServletRequest request, String category, User user) {
		return uploadFile(request, category, "", user);
	}

	@Override
	public Attach uploadFile(HttpServletRequest request, String category, String grade) {
		User user = SystemSecurityUtils.getUser();
		return uploadFile(request, category, grade, user);
	}

	public Attach uploadFile(HttpServletRequest request, String category, String grade, User user) {
		Attach attach = new Attach();
		Map<String, String> message = new HashMap<>();
		message.put("code", "200");
		message.put("id", "");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e2) {
			log.error("文件上传编码异常" + e2, e2);
		}
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String fileName = null;
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile mf = entity.getValue();
			fileName = mf.getOriginalFilename();
			if (mf.getSize() > getAttachSize()) {
				attach.setError("附件大小不能超过500M");
				attach.setFileName(fileName);
				return attach;
			}
			FileNamePolicy policy = new FileNamePolicy(fileName);
			String newFileName = policy.getNewFileName();
			try {
				if ("ftp".equals(uploadType)) {
					boolean a = FtpUploader.uploadFile(this.getAbsolutePath(), newFileName, mf.getInputStream());
					if (!a) {
						message.put("code", "500");
						log.error("上传失败! 文件名称:" + mf.getOriginalFilename() + "文件大小:" + mf.getSize());
					}
					//如果是图片格式生成缩略图
					if(UploadUtils.isImageExt(policy.getFileExt())){
						BufferedImage thumbnail = Scalr.resize(ImageIO.read(mf.getInputStream()), 80);
		                String thumbnailFilename = policy.getThumbnailFilename();
		                ByteArrayOutputStream baos = new ByteArrayOutputStream();
		                ImageIO.write(thumbnail, "png", baos);
		                boolean b = FtpUploader.uploadFile(this.getAbsolutePath(), thumbnailFilename, new ByteArrayInputStream(baos.toByteArray()));
						if (!b) {
							message.put("code", "500");
							log.error("上传失败! 文件名称:" + mf.getOriginalFilename() + "文件大小:" + mf.getSize());
						}
					}
				} else {
					String root = "";
					if (!absolutePath) {
						root = request.getRealPath("/");
						// cas开启后，同步用户图片到数据中心时使用
						if(request.getRequestURI().endsWith(photoUrl)){
							policy.setNewNameBase(fileName.substring(0, fileName.indexOf(policy.getFileExt())-1));
							newFileName = fileName;
						}
					}
					String absPath = root + this.getAbsolutePath();
					File file = new File(absPath);
					if (!file.exists()||!file.isDirectory()) {
						file.mkdirs();
					}
					File uploadFile = new File(absPath + "/" + newFileName);
					mf.transferTo(uploadFile);
					//如果是图片格式生成缩略图
					if (UploadUtils.isImageExt(policy.getFileExt())) {
						BufferedImage thumbnail = Scalr.resize(ImageIO.read(uploadFile), 80);
		                String thumbnailFilename = policy.getThumbnailFilename();
		                File thumbnailFile = new File(absPath + "/" + thumbnailFilename);
		                ImageIO.write(thumbnail, "png", thumbnailFile);
					}
					if (!absolutePath) {
						// cas开启后，同步用户图片到数据中心时使用
						if( "true".equals(GlobalContext.getProperty("cas.start")) && category!=null && "userPhotoImg".equals(category)){
							if(!GlobalContext.isSysCenter()) {
								Map<String,String> paramMap = new HashMap<>();
								paramMap.put("id", String.valueOf(user.getId()));
								paramMap.put("displayName", String.valueOf(user.getDisplayName()));
								Map<String,File> uploadFileMap = new HashMap<>();
								uploadFileMap.put("file", uploadFile);
								String resultJson = HttpClientUtil.post(GlobalContext.getProperty("api.dataServer") + photoUrl, null, paramMap, uploadFileMap);
								Map resultMap = (Map) JsonUtil.json2Java(resultJson, Map.class);
								if (resultMap.get("data") != null) {
									attach.setId(String.valueOf(resultMap.get("data")));
								}
							}
						}
					}
				}
			} catch (IOException e1) {
				message.put("code", "500");
				log.error("上传失败! 文件名称:" + mf.getOriginalFilename() + "文件大小:" + mf.getSize() + e1);
			}
			// 这里是数据库入库操作.
			attach.setFileSize(mf.getSize());
			if (grade != null && !"".equals(grade)) {
				if(fileName.indexOf("(" + grade + ")") == -1) {
					fileName = "(" + grade + ")" + fileName;
				}
			}
			attach.setFileName(fileName);
			attach.setUploadTime(new Date());
			attach.setUrl(UploadUtils.REQUEST_URI + "/download.action");
			attach.setResourcesName(this.getContextPath() + "/" + newFileName);
			attach.setContent_type(request.getContentType().split(";")[0]);
			attach.setCategory(category);
			attach.setExt(policy.getFileExt());
			attach.setUserid(String.valueOf(user.getId()));
			attach.setUsername(user.getDisplayName());
			try {
				if( "true".equals(GlobalContext.getProperty("cas.start")) && category!=null && "userPhotoImg".equals(category) && !GlobalContext.isSysCenter()){

				} else {
					attachService.save(attach);
				}
				message.put("id", attach.getId());
			} catch (Exception e) {
				log.error("附件插入数据库异常" + e);
				log.debug("开始删除" + getContextPath() + "文件");
				message.put("code", "500");
			}

		}
		return attach;
	}

	private Integer httpUploadUserPhoto(File uploadFile, User user){
		if (GlobalContext.isSysCenter()) {
			return null;
		}
		int socketTimeout = 10000;
		int connectTimeout = 10000;
		Boolean setTimeOut = true;
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        //设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        //构建客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connManager).build();
		HttpPost httpPost = new HttpPost(GlobalContext.getProperty("api.dataServer")+photoUrl);
		if (setTimeOut) {
            //设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(socketTimeout)
                    .setConnectTimeout(connectTimeout).build();
            httpPost.setConfig(requestConfig);
        }
		//对请求的表单域进行填充
		FileBody bin = new FileBody(uploadFile);
		StringBody userId = new StringBody(String.valueOf(user.getId()), ContentType.TEXT_PLAIN);
		StringBody displayName = new StringBody(user.getDisplayName(), ContentType.TEXT_PLAIN);
        //设置浏览器兼容模式，解决文件名乱码问题
		HttpEntity reqEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addPart("file", bin).addPart("userId", userId).addPart("displayName", displayName)
                .setCharset(Charset.forName("utf-8")).build();
		CloseableHttpResponse response = null;
		try {
			httpPost.setEntity(reqEntity);
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
                Map<String,String> map = (Map) JsonUtil.json2Java(EntityUtils.toString(entity), Map.class);
                if (map.get("data") != null) {
                	return Integer.parseInt(map.get("data"));
                }
			}
		} catch (IOException e3) {
			return null;
		}
		return null;
	}

	@Override
	public void downloadFile(String httpUploadType, String fileName, String resourcesName, HttpServletResponse response, HttpServletRequest request) {
		String filename = CharConventUtils.encodingFileName(fileName);
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=\"" + filename + "\"");
		InputStream is = null;
		ServletOutputStream out = null;
		try {
			if ("ftp".equals(uploadType)) {
				boolean success = FtpUploader.downFile(getUploadBaseDir() + "/" + resourcesName, response);
				if (success) {
					out = response.getOutputStream();
					out.flush();
				}
			} else {
				downFile(resourcesName, response, request);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(is);
		}
	}

	@Override
	public Boolean deleteDocumentAttach(String ids, HttpServletRequest request) {
		if ("".equals(ids) || ids == null) {
			return false;
		}
		try {
			String[] attachIds=ids.split(",");
			for (int i = 0 ; i < attachIds.length ; i++) {
				String id = attachIds[i];
				AttachBusiness attachBusiness = new AttachBusiness();
				attachBusiness.setAttachId(id);
				List<AttachBusiness> attbus = attachBusinessService.queryAll(attachBusiness);
				if (attbus.size() > 0) {
					for (int j = 0; j < attbus.size(); j++) {
						attachBusinessService.deleteForAttachAndBusiness(attbus.get(j));
					}
				}
				Attach attach = new Attach();
				attach.setId(id);
				Attach result = attachService.get(attach);
				String resourceName = result.getResourcesName();
				attach.setPrimaryKeys(new String[]{id});
				attachService.delete(attach, false);
				if (resourceName != null && !"".equals(resourceName) && "no".equals(getDeleteLogic())) {
					if ("ftp".equals(uploadType)) {
						FtpUploader.deleteFile(getUploadBaseDir() + "/" + resourceName);
					} else {
						String root = "";
						if (!absolutePath) {
							root = request.getRealPath("/");
						}
						FileUtil.delFile(root + getUploadBaseDir() + "/" + resourceName);
					}
				}
			}
		} catch (Exception e) {
			log.error("数据删除异常编号为:" + ids, e);
			return false;
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteFileByIds(String ids) {
		if ("".equals(ids) || ids == null) {
			return false;
		}
		try {
			String[] attachIds=ids.split(",");
			for (int i = 0; i < attachIds.length; i++) {
				deleteAttach(attachIds[i]);
			}
		} catch (Exception e) {
			log.error("数据删除异常编号为:" + ids, e);
			return false;
		}
		return true;
	}

	private Boolean deleteFileByIdsImpl(String ids) {
		if ("".equals(ids) || ids ==  null) {
			return false;
		}
		try {
			String[] attachIds=ids.split(",");
			for (int i = 0 ; i < attachIds.length ; i++) {
				deleteAttach(attachIds[i]);
			}
		} catch (Exception e) {
			log.error("数据删除异常编号为:" + ids, e);
			return false;
		}
		return true;
	}

	@Override
	public void deleteAttach(String id) throws CustomException {
		try {
			Attach attach = new Attach();
			attach.setId(id);
			Attach result = attachService.get(attach);
			result.getResourcesName();
			attach.setPrimaryKeys(new String[]{});
			attachService.delete(attach, false);
		} catch (Exception e) {
			throw new CustomException("删除附件异常", e);
		}
	}

	private String generateDir(HttpServletRequest request) {
		if (!absolutePath) {
			request.getRealPath("/");
		}
		String pathString = request.getSession().getServletContext().getRealPath(File.separator + "upload");
		File dirPath = new File(pathString);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		return pathString;
	}

	private boolean deleteFile(String uploadType, String path, HttpServletRequest request) {
		boolean success = false;
		if (path != null && !"".equals(path) && "no".equals(getDeleteLogic())) {
			if ("ftp".equals(uploadType)) {
				success = FtpUploader.deleteFile(getUploadBaseDir() + "/" + path);
			} else {
				String root = !absolutePath ? request.getRealPath("/") : "";
				success = FileUtil.delFile(root + getUploadBaseDir() + File.separator + path);
			}
		}
		return success;
	}

	private Boolean downFile(String resourcesName, HttpServletResponse response, HttpServletRequest request) {
		boolean success = true;
		String root = !absolutePath ? request.getRealPath("/") : "";
		InputStream inputStream = null;
		OutputStream os = null;
		try {
			File file = new File(root + getUploadBaseDir() + File.separatorChar+resourcesName);
			inputStream = new FileInputStream(file);
			os = response.getOutputStream();
			byte[] b = new byte[Integer.parseInt(GlobalContext.getProperty("STREAM_SLICE"))];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
			os.flush();
		} catch (FileNotFoundException e) {
			log.error("下载文件不存在 path:" + root + getUploadBaseDir() + File.separatorChar + resourcesName);
			success = false;
		} catch (IOException e) {
			log.error("下载异常" + e);
			success = false;
		} finally {
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(inputStream);
		}
		return success;
	}

	private Boolean downFile(Map<String, String> files, HttpServletResponse response, HttpServletRequest request) {
		boolean success = true;
		try {
			ZipFile.zip(files, response.getOutputStream(), generateDir(request) + "/");
		} catch (FileNotFoundException e) {
			log.error("下载文件不存在 path:" + e, e);
			success = false;
		} catch (IOException e) {
			log.error("下载异常" + e);
			success = false;
		}
		return success;
	}

	private static class FileNamePolicy {
		private String fileExt;
		private String newNameBase;
		public FileNamePolicy(String baseDir, String oriName){
			this.fileExt = UploadUtils.getFileExt(oriName);
		    newNameBase = UUID.randomUUID().toString();
		}
		public FileNamePolicy(String oriName){
			this("", oriName);
		}
		public String getNewFileName(){
			return newNameBase+ "." + fileExt;
		}
		public void setNewNameBase(String newNameBase){
			this.newNameBase = newNameBase;
		}
		public String getThumbnailFilename(){
			return newNameBase + "-thumbnail.png";
		}
		public String getFileExt(){
			return this.fileExt;
		}
	}

	public String getContextPath(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String dateStr = sdf.format(new Date());
		return dateStr;
	}

	public String getAbsolutePath(){
		return (getUploadBaseDir()+"/"+getContextPath()).replace('\\','/');
	}

	@Override
	public void getThumbnail(String id, HttpServletRequest request, HttpServletResponse response) throws CustomException {
		Attach attach = new Attach();
		attach.setId(id);
		InputStream is = null;
		ServletOutputStream out = null;
		try {
			Attach result = attachService.get(attach);
			if ("ftp".equals(uploadType)) {
				if(result.getResourcesName() != null && !"".equals(result.getResourcesName())) {
					boolean success = FtpUploader.downFile(getUploadBaseDir() + "/" + result.getResourcesName(), response);
					if (success) {
						out = response.getOutputStream();
						out.flush();
					}
				} else {
					File imageFile = new File(request.getRealPath("/") + "/images/no.png");
					is = new FileInputStream(imageFile);
					out = response.getOutputStream();
					IOUtils.copy(is, out);
					out.flush();
				}
			} else {
				String root = !absolutePath ? request.getRealPath("/") : "";
				File imageFile = null;
				if (result.getResourcesName() != null && !"".equals(result.getResourcesName())) {
					imageFile = new File(root + getUploadBaseDir() + "/" + FileUtil.getFileName(result.getResourcesName()) + "-thumbnail.png");
				} else {
					imageFile = new File(request.getRealPath("/") + "/images/no.png");
				}
		        is = new FileInputStream(imageFile);
		        out = response.getOutputStream();
	            IOUtils.copy(is, out);
	            out.flush();
			}
		} catch (Exception e) {
			throw new CustomException("获取缩略图异常",e);
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(is);
		}
	}

	@Override
	public void getOriginalImg(String id, HttpServletRequest request, HttpServletResponse response) throws CustomException {
		Attach attach = new Attach();
		attach.setId(id);
		InputStream is = null;
		ServletOutputStream out = null;
		try {
			Attach result = attachService.get(attach);
			if ("ftp".equals(uploadType)) {
				if (result.getResourcesName() != null && !"".equals(result.getResourcesName())) {
					boolean success = FtpUploader.downFile(getUploadBaseDir() + "/" + result.getResourcesName(), response);
					if (success) {
						out = response.getOutputStream();
						out.flush();
					}
				} else {
					File imageFile = new File(request.getRealPath("/") + "/images/no.png");
					is = new FileInputStream(imageFile);
					out = response.getOutputStream();
					IOUtils.copy(is, out);
					out.flush();
				}
			} else {
				String root = !absolutePath ? request.getRealPath("/") : "";
				File imageFile = null;
				if (result.getResourcesName() != null && !"".equals(result.getResourcesName())) {
					imageFile = new File(root + getUploadBaseDir()+"/"+result.getResourcesName());
				} else {
					imageFile = new File(request.getRealPath("/") + "/images/no.png");
				}
				is = new FileInputStream(imageFile);
				out = response.getOutputStream();
				IOUtils.copy(is, out);
				out.flush();
			}
		} catch (Exception e) {
			throw new CustomException("获取缩略图异常",e);
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(is);
		}
	}

	@Override
	public void getOriginalImgByPath(String path, HttpServletRequest request, HttpServletResponse response) {
		InputStream is = null;
		ServletOutputStream out = null;
		try {
			File imageFile = new File((!absolutePath ? request.getRealPath("/") : "") + path);
			is = new FileInputStream(imageFile);
			out = response.getOutputStream();
			IOUtils.copy(is, out);
			out.flush();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(is);
		}
	}

	@Override
	public void getOriginalFile(String id, String type, String subFile, HttpServletRequest request, HttpServletResponse response) throws CustomException {
		Attach attach = new Attach();
		attach.setId(id);
		InputStream is = null;
		ServletOutputStream out = null;
		try {
			Attach result = attachService.get(attach);
			if ("ftp".equals(uploadType)) {
				if (result.getResourcesName() != null && !"".equals(result.getResourcesName())) {
					boolean success = FtpUploader.downFile(getUploadBaseDir() + "/" + result.getResourcesName(), response);
					if (success) {
						out = response.getOutputStream();
						out.flush();
					}
				}
			} else {
				String root = !absolutePath ? request.getRealPath("/") : "";
				if(result.getResourcesName() != null && !"".equals(result.getResourcesName())) {
					String filePath = root + getUploadBaseDir() + "/" + result.getResourcesName();
					if (type != null && !"".equals(type)) {
						filePath = filePath.replaceAll("." + result.getExt(), "." + type);
						String fileName = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.lastIndexOf('.'));
						if (subFile != null && !"".equals(subFile)) {
							filePath = filePath.substring(0, filePath.lastIndexOf('/') + 1) + fileName + "/" + subFile;
						} else {
							filePath = filePath.substring(0, filePath.lastIndexOf('/') + 1) + fileName + "/" + fileName + "." + type;
						}
					}
					File file = new File(filePath);
					is = new FileInputStream(file);
					out = response.getOutputStream();
					IOUtils.copy(is, out);
					out.flush();
				}
			}
		} catch (Exception e) {
			throw new CustomException("获取文件异常",e);
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(is);
		}
	}

	@Override
	public List<Attach> copyAttachList(HttpServletRequest request, List<Attach> list){
		Map<String, String> message = new HashMap<>();
		List<Attach> result = new ArrayList<>();
		String root = !absolutePath ? request.getRealPath("/") : "";
		String absPath = root + this.getAbsolutePath();
		File file = new File(absPath);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		Iterator<Attach> iterator = list.iterator();
		while(iterator.hasNext()){
			Attach attachOld = iterator.next();
			Attach attachNew = new Attach();
			FileNamePolicy policy = new FileNamePolicy(attachOld.getFileName());
			String newFileName = policy.getNewFileName();
			attachNew.setResourcesName(this.getContextPath() + "/" + newFileName);
			attachNew.setFileSize(attachOld.getSize());
			attachNew.setFileName(attachOld.getFileName());
			attachNew.setUploadTime(attachOld.getUploadTime());
			attachNew.setUrl(UploadUtils.REQUEST_URI + "/download.action");
			attachNew.setContent_type(attachOld.getContent_type());
			attachNew.setCategory(attachOld.getCategory());
			attachNew.setExt(policy.getFileExt());
			attachNew.setUserid(attachOld.getUserid());
			attachNew.setUsername(attachOld.getUsername());
			try {
				String oldPathFile = root + getUploadBaseDir()+"/"+attachOld.getResourcesName();
				String newPathFile = root + this.getAbsolutePath()+"/"+newFileName;
				if ("ftp".equals(uploadType)) {
					InputStream inputStream = FtpUploader.downFile(getUploadBaseDir() + "/" + attachOld.getResourcesName());
					if (inputStream != null) {
						FtpUploader.uploadFile(this.getAbsolutePath(), newFileName,	inputStream);
					}
				} else {
					copyFile(oldPathFile, newPathFile);
				}
				oldPathFile = root + getUploadBaseDir()+"/"+ FileUtil.getFileName(attachOld.getResourcesName()) + "-thumbnail.png";
				newPathFile = root + getUploadBaseDir()+"/"+ FileUtil.getFileName(attachNew.getResourcesName()) + "-thumbnail.png";
				if ("ftp".equals(uploadType)) {
					InputStream inputStream = FtpUploader.downFile(getUploadBaseDir() + "/" + FileUtil.getFileName(attachOld.getResourcesName())+"-thumbnail.png");
					if (inputStream != null) {
						FtpUploader.uploadFile(getUploadBaseDir(), FileUtil.getFileName(attachNew.getResourcesName())+"-thumbnail.png", inputStream);
					}
				} else {
					copyFile(oldPathFile, newPathFile);
				}
				attachService.save(attachNew);
				result.add(attachNew);
				message.put("id", attachNew.getId());
			} catch (Exception e) {
				log.error("附件插入数据库异常" + e);
				log.debug("开始删除" + getContextPath() + "文件");
				Boolean delbool = false;
				if ("ftp".equals(uploadType)) {
					delbool = FtpUploader.deleteFile(getUploadBaseDir() + "/" + attachNew.getResourcesName());
				} else {
					delbool = deleteFile(uploadType, attachNew.getResourcesName(), request);
				}
				if (delbool) {
					log.debug(getContextPath() + "删除成功");
				} else {
					log.warn(getContextPath() + "删除失败");
				}
				message.put("code", "500");
			}
		}
		return result;
	}

	@Override
	public Boolean deleteUserPhoto(String userId) {
		try {
			AttachBusiness attachBusiness = new AttachBusiness();
			attachBusiness.setBusinessId(userId);
			attachBusiness.setBusinessTable("tty_sys_user");
			attachBusiness.setBusinessSource("0");
			List<AttachBusiness> attachList = attachBusinessService.queryAll(attachBusiness);
			if(attachList.size() > 0){
				String[] attachIds = new String[attachList.size()];
				for (int i = 0; i < attachList.size(); i++) {
					AttachBusiness business = attachList.get(i);
                    attachIds[i] = business.getId();
				}
				AttachBusiness business = new AttachBusiness();
				business.setPrimaryKeys(attachIds);
				attachBusinessService.delete(business, false);
			}
		} catch (CustomException e) {
			log.error(e);
			return false;
		}
		return true;
	}

	private void copyFile(String oldPathFile, String newPathFile) {
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			int byteread = 0;
			File oldfile = new File(oldPathFile);
			File newfile = new File(newPathFile);
			if (oldfile.exists()) {
				inStream = new FileInputStream(oldPathFile);
				fs = new FileOutputStream(newfile);
				byte[] buffer = new byte[Integer.parseInt(GlobalContext.getProperty("STREAM_SLICE"))];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
			}
		} catch (Exception e) {
			log.error("附件插入数据库异常" + e);
		} finally {
			IOUtils.closeQuietly(fs);
			IOUtils.closeQuietly(inStream);
		}
	}

	@Override
	public List<Attach> copyAttachAndTextList(HttpServletRequest request, List<Attach> list, String businessId){
		Map<String, String> message = new HashMap<>();
		List<Attach> result = new ArrayList<>();
		Iterator<Attach> iterator = list.iterator();
		String root = !absolutePath ? request.getRealPath("/") : "";
		String absPath = root + this.getAbsolutePath();
		File file = new File(absPath);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		while (iterator.hasNext()) {
			Attach attachOld = iterator.next();
			Attach attachNew = new Attach();
			FileNamePolicy policy = new FileNamePolicy(attachOld.getFileName());
			String newFileName = policy.getNewFileName();
			attachNew.setResourcesName(this.getContextPath() + "/" + newFileName);
			attachNew.setFileSize(attachOld.getSize());
			attachNew.setFileName(attachOld.getFileName());
			attachNew.setUploadTime(attachOld.getUploadTime());
			attachNew.setUrl(UploadUtils.REQUEST_URI + "/download.action");
			attachNew.setContent_type(attachOld.getContent_type());
			attachNew.setCategory(attachOld.getCategory());
			attachNew.setExt(policy.getFileExt());
			attachNew.setUserid(attachOld.getUserid());
			attachNew.setUsername(attachOld.getUsername());
			try {
				String oldPathFile = root + getUploadBaseDir() + "/" + attachOld.getResourcesName();
				String newPathFile = root + this.getAbsolutePath() + "/" + newFileName;
				if ("ftp".equals(uploadType)) {
					InputStream inputStream = FtpUploader.downFile(getUploadBaseDir() + "/" + attachOld.getResourcesName());
					if (inputStream != null) {
						FtpUploader.uploadFile(this.getAbsolutePath(), newFileName,	inputStream);
					}
				} else {
					copyFile(oldPathFile, newPathFile);
				}
				oldPathFile = root + getUploadBaseDir() + "/" + FileUtil.getFileName(attachOld.getResourcesName()) + "-thumbnail.png";
				newPathFile = root + getUploadBaseDir() + "/" + FileUtil.getFileName(attachNew.getResourcesName()) + "-thumbnail.png";
				if ("ftp".equals(uploadType)) {
					InputStream inputStream = FtpUploader.downFile(getUploadBaseDir() + "/" + FileUtil.getFileName(attachOld.getResourcesName())+"-thumbnail.png");
					if (inputStream != null) {
						FtpUploader.uploadFile(getUploadBaseDir(), FileUtil.getFileName(attachNew.getResourcesName())+"-thumbnail.png", inputStream);
					}
				} else {
					copyFile(oldPathFile, newPathFile);
				}
				attachService.save(attachNew);
				result.add(attachNew);
				message.put("id", attachNew.getId());
			} catch (Exception e) {
				log.error("附件插入数据库异常" + e);
				log.debug("开始删除" + getContextPath() + "文件");
				Boolean delbool = false;
				if ("ftp".equals(uploadType)) {
					delbool = FtpUploader.deleteFile(getUploadBaseDir() + "/" + attachNew.getResourcesName());
				} else {
					delbool = deleteFile(uploadType, attachNew.getResourcesName(), request);
				}
				if (delbool) {
					log.debug(getContextPath() + "删除成功");
				} else {
					log.warn(getContextPath() + "删除失败");
				}
				message.put("code", "500");
			}
		}
		return result;
	}

	@Override
	public Integer managerForAttach(String buesinessId, String buesinessTable, String addFileids, String delFileids, int isEdit) {
		return managerForAttach(buesinessId, buesinessTable, addFileids, delFileids, String.valueOf(isEdit));
	}

	@Override
	public Integer managerForAttach(Long buesinessId, String buesinessTable, String addFileids, String delFileids, String isEdit) {
		if(isEdit != null && !"".equals(isEdit)) {
			return managerForAttach(buesinessId+"", buesinessTable, addFileids, delFileids, Integer.parseInt(isEdit));
		} else {
			return null;
		}
	}

	@Override
	public Integer managerForAttach(String buesinessId, String buesinessTable, String addFileids, String delFileids, String isEdit) {
		if (isEdit != null && !"".equals(isEdit)) {
			try {
				if (delFileids != null && delFileids.length() > 0) {
					AttachBusiness temp = new AttachBusiness();
					temp.setAttachIds(delFileids);
					temp.setBusinessId(buesinessId);
					temp.setBusinessTable(buesinessTable);
					List<AttachBusiness> attachBusinessList = attachBusinessService.queryForAttachAndBusiness(temp);
					for (AttachBusiness delVo : attachBusinessList) {
						String[] keys = new String[1];
                        keys[0] = String.valueOf(delVo.getId());
						delVo.setPrimaryKeys(keys);
						attachBusinessService.delete(delVo,false);
					}
					deleteFileByIdsImpl(delFileids);
				}
				if (addFileids != null && !"".equals(addFileids)) {
					AttachBusiness temp = new AttachBusiness();
					temp.setAttachIds(addFileids);
					temp.setBusinessId(buesinessId);
					temp.setBusinessTable(buesinessTable);
					List<AttachBusiness> attachBusinessList = attachBusinessService.queryForAttachAndBusiness(temp);
					for (AttachBusiness delVo : attachBusinessList) {
						String[] keys = new String[1];
                        keys[0] = String.valueOf(delVo.getId());
						delVo.setPrimaryKeys(keys);
						attachBusinessService.delete(delVo,false);
					}
					String[] addFileIds = addFileids.split(",");
					for (int i = 0; i < addFileIds.length; i++) {
						AttachBusiness savetemp = new AttachBusiness();
						savetemp.setAttachId(addFileIds[i]);
						savetemp.setBusinessId(buesinessId);
						savetemp.setBusinessTable(buesinessTable);
						savetemp.setIsEdit(Integer.parseInt(isEdit));
						attachBusinessService.save(savetemp);
					}
				}
			} catch (CustomException e) {
				log.error(e);
			}
			return null;
		} else {
			return null;
		}
	}

	@Override
	public Integer managerForAttach(Long buesinessId, String buesinessTable, String addFileids, String delFileids, String isEdit, String secrets) {
		if (isEdit != null && !"".equals(isEdit)) {
			try {
				if (delFileids != null && delFileids.length() > 0) {
					AttachBusiness temp = new AttachBusiness();
					temp.setAttachIds(delFileids);
					temp.setBusinessId(buesinessId.toString());
					temp.setBusinessTable(buesinessTable);
					List<AttachBusiness> attachBusinessList = attachBusinessService.queryForAttachAndBusiness(temp);
					for (AttachBusiness delVo : attachBusinessList) {
						String[] keys = new String[1];
                        keys[0] = String.valueOf(delVo.getId());
						delVo.setPrimaryKeys(keys);
						attachBusinessService.delete(delVo, false);
					}
					deleteFileByIdsImpl(delFileids);
				}
				if (addFileids != null && !"".equals(addFileids)) {
					AttachBusiness temp = new AttachBusiness();
					temp.setAttachIds(addFileids);
					temp.setBusinessId(buesinessId.toString());
					temp.setBusinessTable(buesinessTable);
					List<AttachBusiness> attachBusinessList = attachBusinessService.queryForAttachAndBusiness(temp);
					for(AttachBusiness delVo:attachBusinessList){
						String[] keys = new String[1];
                        keys[0] = String.valueOf(delVo.getId());
						delVo.setPrimaryKeys(keys);
						attachBusinessService.delete(delVo,false);
					}

					String[] addfileIds = addFileids.split(",");
					for (int i = 0; i < addfileIds.length; i++) {
						AttachBusiness saveTemp = new AttachBusiness();
                        saveTemp.setAttachId(addfileIds[i]);
                        saveTemp.setBusinessId(buesinessId.toString());
                        saveTemp.setBusinessTable(buesinessTable);
						//记录附件状态
                        saveTemp.setIsEdit(Integer.parseInt(isEdit));
						//记录附件状态
						attachBusinessService.save(saveTemp);
						Attach attach = new Attach();
						attach.setId(saveTemp.getAttachId());
						attach = attachService.get(attach);
						String fileName = attach.getFileName();
						if (secrets != null && !"".equals(secrets)) {
							if (fileName.indexOf("(" + secrets + ")") == -1) {
								fileName = "(" + secrets + ")" + fileName;
							}
						}
						attach.setFileName(fileName);
						attachService.update(attach);
					}
				}
			} catch (CustomException e) {
				log.error(e);
			}
			return null;
		} else {
			return null;
		}
	}

	@Override
	public Attach upload(MultipartFile mf, String category, HttpServletRequest request) {
		User user = SystemSecurityUtils.getUser();
		Attach attach = new Attach();
		Map<String, String> message = new HashMap<>();
		message.put("code", "200");
		message.put("id", "");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e2) {
			log.error("文件上传编码异常" + e2, e2);
		}
		String fileName = mf.getOriginalFilename();
		if(mf.getSize() > getAttachSize()){
			attach.setError("附件大小不能超过500M");
			attach.setFileName(fileName);
			return attach;
		}
		try {
			int fileType = FileTypeJudge.isFileType(FileTypeJudge.getType(mf.getInputStream()));
			System.out.println(fileType);
			if(fileType != 1 && fileType != 2){
				attach.setError("附件格式异常");
				return attach;
			}
			int fileNameType = FileNameType.isFileName(fileName);
			if(fileNameType != fileType){
				attach.setError("附件格式异常");
				return attach;
			}
		} catch (IOException e) {
			log.error("附件格式异常" + e);
		}
		FileNamePolicy policy = new FileNamePolicy(fileName);
		String newFileName = policy.getNewFileName();
		try {
			if ("ftp".equals(uploadType)) {
				boolean a = FtpUploader.uploadFile(this.getAbsolutePath(), newFileName, mf.getInputStream());
				if (!a) {
					message.put("code", "500");
					log.error("上传失败! 文件名称:" + mf.getOriginalFilename() + "文件大小:" + mf.getSize());
				}
				if(UploadUtils.isImageExt(policy.getFileExt())){
					BufferedImage thumbnail = Scalr.resize(ImageIO.read(mf.getInputStream()), 80);
					String thumbnailFilename = policy.getThumbnailFilename();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(thumbnail, "png", baos);
					boolean b = FtpUploader.uploadFile(this.getAbsolutePath(), thumbnailFilename, new ByteArrayInputStream(baos.toByteArray()));
					if (!b) {
						message.put("code", "500");
						log.error("上传失败! 文件名称:" + mf.getOriginalFilename() + "文件大小:" + mf.getSize());
					}
				}
			} else {
				String root = "";
				if (!absolutePath) {
					root = request.getRealPath("/");
					if(request.getRequestURI().endsWith(photoUrl)){
						policy.setNewNameBase(fileName.substring(0, fileName.indexOf(policy.getFileExt())-1));
						newFileName = fileName;
					}
				}
				String absPath = root + this.getAbsolutePath();
				File file = new File(absPath);
				if (!file.exists()||!file.isDirectory()) {
					file.mkdirs();
				}
				File uploadFile = new File(absPath + "/" + newFileName);
				mf.transferTo(uploadFile);
				if(UploadUtils.isImageExt(policy.getFileExt())){
					BufferedImage thumbnail = Scalr.resize(ImageIO.read(uploadFile), 80);
					String thumbnailFilename = policy.getThumbnailFilename();
					File thumbnailFile = new File(absPath + "/" + thumbnailFilename);
					ImageIO.write(thumbnail, "png", thumbnailFile);
				}
				if (!absolutePath) {
					if( "true".equals(GlobalContext.getProperty("cas.start")) && category!=null && "userPhotoImg".equals(category)){
						if(!GlobalContext.isSysCenter()) {
							Map<String,String> paramMap = new HashMap<>();
							if (user != null) {
								paramMap.put("id", String.valueOf(user.getId()));
								paramMap.put("displayName", String.valueOf(user.getDisplayName()));
							}
							Map<String,File> uploadFileMap = new HashMap<>();
							uploadFileMap.put("file", uploadFile);
							String resultJson = HttpClientUtil.post(GlobalContext.getProperty("api.dataServer")+photoUrl, null, paramMap,uploadFileMap);
							Map resultMap = (Map) JsonUtil.json2Java(resultJson, Map.class);
							if(resultMap.get("data")!=null){
								attach.setId(String.valueOf(resultMap.get("data")));
							}
						}
					}
				}
			}
		} catch (IOException e1) {
			message.put("code", "500");
			log.error("上传失败! 文件名称:" + mf.getOriginalFilename() + "文件大小:" + mf.getSize() + e1);
		}
		attach.setFileSize(mf.getSize());
		attach.setFileName(fileName);
		attach.setUploadTime(new Date());
		attach.setUrl(UploadUtils.REQUEST_URI + "/download.action");
		attach.setResourcesName(this.getContextPath() + "/" + newFileName);
		attach.setContent_type(request.getContentType().split(";")[0]);
		attach.setCategory(category);
		attach.setExt(policy.getFileExt());
		if (user != null) {
			attach.setUserid(String.valueOf(user.getId()));
			attach.setUsername(user.getDisplayName());
		}
		try {
			attachService.save(attach);
			message.put("id", attach.getId());
		} catch (Exception e) {
			log.error("附件插入数据库异常" + e);
			log.debug("开始删除" + getContextPath() + "文件");
			message.put("code", "500");
		}
		return attach;

	}

	@Override
	public Result deleteBusinessPhoto(String busId, String busTable) {
		if (StringUtil.isEmpty(busId) || StringUtil.isEmpty(busTable)) {
			return Result.failure(ResultCode.PARAM_IS_BLANK);
		}
		try {
			AttachBusiness attachBusiness = new AttachBusiness();
			attachBusiness.setBusinessId(busId);
			attachBusiness.setBusinessTable(busTable);
			attachBusiness.setBusinessSource("0");
			List<AttachBusiness> attachList = attachBusinessService.queryAll(attachBusiness);
			if(attachList.size() > 0){
				String[] attachIds = new String[attachList.size()];
				for(int i = 0; i < attachList.size(); i++){
					AttachBusiness business = attachList.get(i);
                    attachIds[i] = business.getId();
				}
				AttachBusiness business = new AttachBusiness();
				business.setPrimaryKeys(attachIds);
				attachBusinessService.delete(business, false);
			}
		} catch (CustomException e) {
			log.error(e);
			return Result.failure(ResultCode.INTERFACE_INNER_INVOKE_ERROR);
		}
		return Result.success();
	}

	@Override
	public Result saveBusinessPhoto(String busId, String busTable, String fileId) throws CustomException {
		if (StringUtil.isEmpty(fileId)) {
			return Result.success();
		}
		String[] array = splitStr(fileId, ',');
		if (array == null || array.length == 0) {
			return Result.success();
		}
		deleteBusinessPhoto(busId, busTable);
		for (String s : array) {
			AttachBusiness param = new AttachBusiness();
			param.setAttachId(s);
			param.setBusinessId(busId);
			param.setBusinessTable(busTable);
			param.setBusinessSource("0");
			attachBusinessService.save(param);
		}
		return Result.success();
	}

	public static String[] splitStr(String str , char c){
		if(str == null){
			return null;
		}
		str += c;
		int n = 0;
		for(int i = 0 ; i < str.length() ; i++)	{
			if(str.charAt(i) == c){
				n++;
			}
		}
		String out[] = new String[n];
		for(int i = 0 ; i < n ; i++) {
			int index = str.indexOf(c);
			out[i] = str.substring(0 , index);
			str = str.substring(index + 1 , str.length());
		}
		return out;
	}
}
