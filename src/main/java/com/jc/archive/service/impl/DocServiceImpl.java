package com.jc.archive.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.foundation.domain.Attach;
import com.jc.foundation.util.PropertiesUtil;
import com.jc.archive.service.IDocService;
import com.jc.system.content.service.IAttachService;

@Service
public class DocServiceImpl implements IDocService {

	@Autowired
	private IAttachService attachService;
	private static Map<String, String> jddpPath = PropertiesUtil.getProperties("/goa.properties");

	@Override
	public void strChangeXML(String str, HttpServletRequest request) throws IOException {
		SAXReader saxReader = new SAXReader();
		Document document;
		String docPath = request.getRealPath(jddpPath.get("JDDP_PATH") + File.separator);
		try {
			document = saxReader.read(new ByteArrayInputStream(str
					.getBytes("UTF-8")));
			
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			/** 将document中的内容写入文件中 */
			XMLWriter writer = new XMLWriter(new FileOutputStream(new File(docPath
					+ File.separator + "archive.xml")), format);
			writer.write(document);
			writer.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Attach> getAttach(Long id, String table) throws Exception {
		Attach attach  = new Attach();
		attach.setBusinessId(id+"");
		attach.setBusinessTable(table);
		attach.setCategory("picList1");
		attach.setIsPaged("1");
		List<Attach> list = attachService.queryAll(attach);
		return list;
	}

	@Override
	public boolean deleteAllFilesOfDir(File path) {
		boolean success = false;
		if (!path.exists())  
	        return success;  
	    File[] files = path.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	    	success = files[i].delete();
	    	 if (!success) {
                 return false;
             }
	    }  
	   return success;
		
	}
	
}


