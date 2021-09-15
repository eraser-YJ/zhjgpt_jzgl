package com.jc.common.kit.vo;

import java.io.File;
import java.util.List;

public class ExcelImpVO {
	private File tempFile;
	private String name;
	private String fileExt;
	private String[][] dataArr;

	public ExcelImpVO(File inTempFile, List<String[]> dataList, String name, String fileExt) {
		super();
		this.tempFile = inTempFile;
		if (dataList != null) {
			this.dataArr = dataList.toArray(new String[dataList.size()][]);
		} else {
			this.dataArr = new String[0][0];
		}
		this.name = name;
		this.fileExt = fileExt;
	}

	public ExcelImpVO(File inTempFile, String[][] dataArr, String name, String fileExt) {
		super();
		this.tempFile = inTempFile;
		this.dataArr = dataArr;
		this.name = name;
		this.fileExt = fileExt;
	}

	public File getTempFile() {
		return tempFile;
	}

	public String getName() {
		return name;
	}

	public String getFileExt() {
		return fileExt;
	}

	public String[][] getDataArr() {
		return dataArr;
	}

}
