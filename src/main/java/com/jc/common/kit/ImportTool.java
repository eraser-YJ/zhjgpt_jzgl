package com.jc.common.kit;

import com.jc.common.kit.vo.ExcelImpVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 导入工具类
 * 
 * @author lc  liubq
 * @since 2018年6月14日
 */
public class ImportTool {
	/**
	 * 取得数据
	 * 
	 * @param excelFile
	 * @param startLine
	 * @return
	 * @throws Exception
	 */
	public static ExcelImpVO assembleList(MultipartFile excelFile, int startRow, int columsLength) throws Exception {
		String fileName = excelFile.getOriginalFilename();
		int pos = fileName.lastIndexOf(".");
		String fileNameExt = fileName.substring(pos + 1);
		long fileSize = excelFile.getSize();
		if (fileNameExt == "xlsx") {
			throw new Exception("请选择后缀名为xlsx的文件");
		} else if (fileSize > 8 * 1024 * 1024) {
			throw new Exception("请上传文件大小不超过8M");
		}
		InputStream inputStream = null;
		try {
			inputStream = excelFile.getInputStream();
			File tempFile = FileCopy.createFile(fileNameExt, inputStream);
			List<String[]> excelData = ExcelParser.parse(tempFile, startRow, columsLength);
			return new ExcelImpVO(tempFile, excelData, fileName, fileNameExt);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception ex) {
				}
			}
		}
	}
}
