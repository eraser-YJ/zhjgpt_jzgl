package com.jc.system.common.util;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class XmlUtil {

	private XmlUtil() {
		throw new IllegalStateException("XmlUtil class");
	}

	private static final Logger logger = Logger.getLogger(XmlUtil.class);
	/**
	 * 将对象序列化成xml格式数据
	 * 
	 * @param object
	 * @return
	 */
	public static String object2Xml(Object object) {
		String xml = "";
		StringWriter sw = new StringWriter();
		sw.write("<?xml version='1.0'?>\n");
		BeanWriter bw = new BeanWriter(sw);
		bw.getXMLIntrospector().getConfiguration()
				.setAttributesForPrimitives(false);
		bw.setWriteEmptyElements(false);
		bw.getBindingConfiguration().setMapIDs(false);
		bw.enablePrettyPrint();
		try {
			bw.write(object);
			xml = sw.toString();
			bw.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (SAXException e) {
			logger.error(e.getMessage());
		} catch (IntrospectionException e) {
			logger.error(e.getMessage());
		}
		return xml;
	}

	/***
	 * 将xml文件反序列化为对象
	 * 
	 * @param xml
	 * @param classType
	 * @return
	 */
	public static Object xml2Object(String xml, Class classType) {
		StringReader xmlReader = new StringReader(xml);
		BeanReader beanReader = new BeanReader();
		Object object = null;
		// 配置reader
		beanReader.getXMLIntrospector().getConfiguration()
				.setAttributesForPrimitives(false);
		beanReader.getBindingConfiguration().setMapIDs(false);
		try {
			// 注册beans，以便betwixt知道XML将要被转化为一个什么Bean
			beanReader.registerBeanClass(classType.getSimpleName(), classType);
			object = beanReader.parse(xmlReader);
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (SAXException e) {
			logger.error(e.getMessage());
		} catch (IntrospectionException e) {
			logger.error(e.getMessage());
		}
		return object;
	}

	public static boolean doc2XmlFile(Document document, String filename) {
		boolean flag = true;
		try {
			/** 将document中的内容写入文件中 */
			TransformerFactory tFactory = TransformerFactory.newInstance();
			tFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			/** 编码 */
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(filename));
			transformer.transform(source, result);
		} catch (Exception ex) {
			flag = false;
			logger.error(ex.getMessage());
		}
		return flag;
	}

	public static Document load(String filename) {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(new File(filename));
			document.normalize();
		} catch (Exception ex) {
			logger.error(ex);
		}
		return document;
	}
}
