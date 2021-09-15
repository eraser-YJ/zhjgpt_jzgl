package com.jc.system.content.service.impl;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.GlobalContext;
import com.jc.system.content.service.IOfficeChangeService;
import org.apache.log4j.Logger;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class OfficeChangeServiceImpl extends BaseServiceImpl<BaseBean> implements IOfficeChangeService {

	private static final Logger logger = Logger.getLogger(OfficeChangeServiceImpl.class);

	@Override
	public synchronized void office2html(String inputFilePath, String outputFilePath) {
		DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
		String officeHome = getOfficeHome();
		config.setOfficeHome(officeHome);
		OfficeManager officeManager = config.buildOfficeManager();
		officeManager.start();
		try {
			OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
			File inputFile = new File(inputFilePath);
			if (inputFile.exists()) {
				File outputFile = new File(outputFilePath);
				if (!outputFile.getParentFile().exists()) {
					outputFile.getParentFile().mkdirs();
				}
				converter.convert(inputFile, outputFile);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (officeManager != null) {
				officeManager.stop();
			}
		}
	}

	@Override
	public synchronized void office2Pdf(String inputFilePath, String outputFilePath) {
		DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
		String officeHome = getOfficeHome();
		config.setOfficeHome(officeHome);
		OfficeManager officeManager = config.buildOfficeManager();
		officeManager.start();
		try {
			OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
			File inputFile = new File(inputFilePath);
			if (inputFile.exists()) {
				File outputFile = new File(outputFilePath);
				if (!outputFile.getParentFile().exists()) {
					outputFile.getParentFile().mkdirs();
				}
				converter.convert(inputFile, outputFile);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (officeManager != null) {
				officeManager.stop();
			}
		}
	}

	public String getOutputFilePath(String inputFilePath, String officeType, String toOfficeType) {
		String outputFilePath = inputFilePath.replaceAll(officeType, toOfficeType);
		return outputFilePath;
	}

	public String getOfficeHome() {
		String osName = System.getProperty("os.name");
		if (Pattern.matches("Linux.*", osName)) {
			return GlobalContext.getProperty("OFFICE_PATH_LINUX");
		} else if (Pattern.matches("Windows.*", osName)) {
			return GlobalContext.getProperty("OFFICE_PATH_WINDOWS");
		} else if (Pattern.matches("Mac.*", osName)) {
			return GlobalContext.getProperty("OFFICE_PATH_MAX");
		}
		return null;
	}
}
