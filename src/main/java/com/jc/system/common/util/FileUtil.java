package com.jc.system.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.jc.foundation.util.GlobalContext;
import org.apache.log4j.Logger;

/**
 * 文件处理
 * @author Administrator
 * @date 2020-06-30
 */
public class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("FileUtil class");
    }

    private static final Logger logger = Logger.getLogger(FileUtil.class);

    private static String message;

    /**
     * 读取文本文件内容
     * @param filePathAndName 带有完整绝对路径的文件名
     * @param encoding        文本文件打开的编码方式
     * @return 返回文本文件的内容
     * @throws Exception
     */
    public static String readTxt(String filePathAndName, String encoding)
            throws IOException {
        FileInputStream fs = new FileInputStream(filePathAndName);
        return readTxt(fs, encoding);
    }

    public static String readTxt(InputStream fs, String encoding) {
        if(fs == null) {
            return "";
        }
        encoding = encoding.trim();
        StringBuffer str = new StringBuffer("");
        String st = "";
        try {
            InputStreamReader isr;
            if ("".equals(encoding)) {
                isr = new InputStreamReader(fs);
            } else {
                isr = new InputStreamReader(fs, encoding);
            }
            BufferedReader br = new BufferedReader(isr);
            try {
                String data = "";
                while ((data = br.readLine()) != null) {
                    str.append(data + " ");
                }
            } catch (Exception e) {
                str.append(e.toString());
            }
            st = str.toString();
        } catch (IOException es) {
            st = "";
        }
        return st;
    }

    /**
     * 新建目录
     * @param folderPath 目录
     * @return 返回目录创建后的路径
     */
    public static String createFolder(String folderPath) {
        String txt = folderPath;
        try {
            java.io.File myFilePath = new java.io.File(txt);
            txt = folderPath;
            if (!myFilePath.exists()) {
                myFilePath.mkdirs();
            }
        } catch (Exception e) {
            message = "创建目录操作出错";
        }
        return txt;
    }

    /**
     * 新建目录
     * @param folderPaths 多个目录
     */
    public static void createFolder(String[] folderPaths) {
        for (int i = 0; i < folderPaths.length; i++) {
            createFolder(folderPaths[i]);
        }
    }

    /**
     * 多级目录创建
     * @param folderPath 准备要在本级目录下创建新目录的目录路径 例如 c:myf
     * @param paths      无限级目录参数，各级目录以单数线区分 例如 a|b|c
     * @return 返回创建文件后的路径 例如 c:myfa/c
     */
    public static String createFolders(String folderPath, String paths) {
        String txts = folderPath;
        try {
            String txt;
            StringTokenizer st = new StringTokenizer(paths, "|");
            while (st.hasMoreTokens()) {
                txt = st.nextToken().trim();
                if (txts.lastIndexOf('/') != -1) {
                    txts = createFolder(txts + File.separator + txt);
                } else {
                    txts = createFolder(txts + txt + File.separator);
                }
            }
        } catch (Exception e) {
            message = "创建目录操作出错！";
        }
        return txts;
    }

    /**
     * 新建文件
     * @param filePathAndName 文本文件完整绝对路径及文件名
     * @param fileContent     文本文件内容
     */
    public static void createFile(String filePathAndName, String fileContent) {
    	FileOutputStream fos = null;
    	Writer out = null;
        try {
            String filePath = filePathAndName;
            int position = filePath.lastIndexOf(File.separator);
            String folderPath = filePath.substring(0, position);
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                if(!myFilePath.createNewFile()){
                    logger.error("创建文件失败");
                }
            }
            fos = new FileOutputStream(filePathAndName);
            out = new OutputStreamWriter(fos, "UTF-8");
            out.write(fileContent);
        } catch (Exception e) {
            message = "创建文件操作出错";
        } finally {
        	try {
        		if(out!=null) {
        			out.close();
        		}
        		if(fos!=null) {
        			fos.close();
        		}
			} catch (Exception e) {
                logger.error(e.getMessage());
			}
        }
    }

    public static void createNewFile(String filePathAndName, String fileContent) {
        FileOutputStream fos = null;
        Writer out = null;
        try {
            String filePath = filePathAndName;
            int position = filePath.lastIndexOf('/');
            String folderPath = filePath.substring(0, position);
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                if(!myFilePath.createNewFile()){
                    logger.error("创建文件失败");
                }
            }
            fos = new FileOutputStream(filePathAndName);
            out = new OutputStreamWriter(fos, "UTF-8");
            out.write(fileContent);
        } catch (Exception e) {
            message = "创建文件操作出错";
        } finally {
            try {
                if(out!=null) {
                    out.close();
                }
                if(fos!=null) {
                    fos.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * 有编码方式的文件创建
     * @param filePathAndName 文本文件完整绝对路径及文件名
     * @param fileContent     文本文件内容
     * @param encoding        编码方式 例如 GBK 或者 UTF-8
     */
    public static void createFile(String filePathAndName, String fileContent, String encoding) {
    	PrintWriter myFile = null;
        try {
            String filePath = filePathAndName;
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                if(!myFilePath.createNewFile()){
                    logger.error("创建文件失败");
                }
            }
            myFile = new PrintWriter(myFilePath, encoding);
            String strContent = fileContent;
            myFile.println(strContent);
            
        } catch (Exception e) {
            message = "创建文件操作出错";
        }finally {
        	if(myFile!=null) {
        		myFile.close();
        	}
        }
    }

    /**
     * 删除文件
     * @param filePathAndName 文本文件完整绝对路径及文件名
     * @return Boolean 成功删除返回true遭遇异常返回false
     */
    public static boolean delFile(String filePathAndName) {
        boolean bea = false;
        try {
            String filePath = filePathAndName;
            int position = filePathAndName.lastIndexOf(File.separator);
            String forderPath = filePathAndName.substring(0, position - 1);
            File forder = new File(forderPath);
            if (!forder.exists()) {
                forder.mkdirs();
            }
            File myDelFile = new File(filePath);
            if (myDelFile.exists()) {
                if(!myDelFile.delete()){
                    logger.error("删除文件失败");
                }
                bea = true;
            } else {
                bea = false;
                message = (filePathAndName + "删除文件操作出错");
            }
        } catch (Exception e) {
            message = e.toString();
        }
        return bea;
    }

    /**
     * 删除文件夹
     * @param folderPath 文件夹完整绝对路径
     */
    public static boolean delFolder(String folderPath) {
        boolean bea = false;
        try {
            // 删除完里面所有内容
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath;
            java.io.File myFilePath = new java.io.File(filePath);
            if(!myFilePath.delete()){
                logger.error("删除空文件夹失败");
            }
            bea = true;
        } catch (Exception e) {
            message = ("删除文件夹操作出错");
            bea = false;
        }
        return bea;
    }

    /**
     * 删除指定文件夹下所有文件
     * @param path 文件夹完整绝对路径
     * @return boolean 成功删除返回true遭遇异常返回false
     */
    public static boolean delAllFile(String path) {
        boolean bea = false;
        File file = new File(path);
        if (!file.exists()) {
            return bea;
        }
        if (!file.isDirectory()) {
            return bea;
        }
        String[] tempList = file.list();
        File temp = null;
        if(tempList != null && tempList.length >0) {
            for (int i = 0; i < tempList.length; i++) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                } else {
                    temp = new File(path + File.separator + tempList[i]);
                }
                if (temp.isFile()) {
                    if(!temp.delete()){
                        logger.error("删除空文件失败");
                    }
                }
                if (temp.isDirectory()) {
                    delAllFile(path + "/" + tempList[i]);
                    delFolder(path + "/" + tempList[i]);
                    bea = true;
                }
            }
        }
        return bea;
    }

    /**
     * 复制单个文件
     * @param oldPathFile 准备复制的文件源
     * @param newPathFile 拷贝到新绝对路径带文件名
     */
    public static void copyFile(String oldPathFile, String newPathFile) {
    	InputStream inStream = null;
    	FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPathFile);
            if (oldfile.exists()) {
            	inStream = new FileInputStream(oldPathFile);
            	fs = new FileOutputStream(newPathFile);
                byte[] buffer = new byte[Integer.parseInt(GlobalContext
                        .getProperty("STREAM_SLICE"))];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
            }
        } catch (Exception e) {
            message = ("复制单个文件操作出错");
        }finally {
            if(inStream!=null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if(fs!=null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 复制整个文件夹的内容
     * @param oldPath 准备拷贝的目录
     * @param newPath 指定绝对路径的新目录
     * @return 所有复制后的新文件
     */
    public static List<File> copyFolder(String oldPath, String newPath) {
        List<File> list = new ArrayList<>();
        try {
            new File(newPath).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            if(file != null && file.length >0) {
                for (int i = 0; i < file.length; i++) {
                	copyFolderForFile(oldPath,newPath,file[i],list);
                }
            }
        } catch (Exception e) {
            message = "复制整个文件夹内容操作出错";
            logger.error(message, e);
        }
        return list;
    }
    
    /**
     * 解决代码规范提示bug
     * @param oldPath
     * @param newPath
     * @param filename
     * @param list
     * @throws Exception
     */
    private static void copyFolderForFile(String oldPath, String newPath,String filename,List<File> list){
    	FileInputStream input = null;
    	FileOutputStream output = null;
    	try {
    		File temp = null;
        	if (oldPath.endsWith(File.separator)) {
                temp = new File(oldPath + filename);
            } else {
                temp = new File(oldPath + File.separator + filename);
            }
            if (temp.isFile()) {
            	input = new FileInputStream(temp);
            	output = new FileOutputStream(newPath
                        + "/" + (temp.getName()));
                byte[] b = new byte[Integer.parseInt(GlobalContext
                        .getProperty("STREAM_SLICE"))];
                int len;
                while ((len = input.read(b)) != -1) {
                    output.write(b, 0, len);
                }
                output.flush();
                list.add( new File(newPath + "/"
                        + (temp.getName())));
            }
            if (temp.isDirectory()) {
                List<File> childList = copyFolder(oldPath + "/" + filename,
                        newPath + "/" + filename);
                for (int j = 0; j < childList.size(); j++) {
                    list.add(childList.get(j));
                }
            }
    	}catch(IOException e) {
            logger.error(e.getMessage());
        }finally {
    		if(output!=null) {
                try {
                    output.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
    		if(input!=null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
    	}
    }

    /**
     * 移动文件
     * @param oldPath 准备移动的文件路径
     * @param newPath 移动之后的文件路径
     */
    public static void moveFile(String oldPath, String newPath) {
        copyFile(oldPath, newPath);
        delFile(oldPath);
    }

    /**
     * 移动目录
     * @param oldPath 准备移动的目录路径
     * @param newPath 移动之后的目录路径
     */
    public static void moveFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delFolder(oldPath);
    }

    /**
     * 得到错误信息
     * @return 错误信息内容
     */
    public static String getMessage() {
        return message;
    }

    /**
     * 打包文件
     * @param filePath 打包文件目录
     * @return 打包后的路径
     */
    public static String zip(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        String outPath = file.getParent() + File.separator + file.getName() + ".zip";
        return zip(filePath, outPath);
    }

    /**
     * 打包文件
     * @param filePath    打包文件目录
     * @param outFilePath 压缩文件输出打包文件目录
     * @return 打包后的路径
     */
    public static String zip(String filePath, String outFilePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        String name = file.getName();
        String outPath = null;
        ZipOutputStream out;
        try {
            out = new ZipOutputStream(new FileOutputStream(outFilePath));
            zip(out, file, name);
            out.close();
        } catch (Exception e) {
            logger.error(e);
        }
        return outPath;
    }

    /**
     * 压缩文件
     * @param out  输出的文件流
     * @param f    打包的文件
     * @param base 根目录
     */
    private static void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            if(fl != null && fl.length > 0) {
                for (int i = 0; i < fl.length; i++) {
                    zip(out, fl[i], base + fl[i].getName());
                }
            }
        } else {
        	FileInputStream in = null;
        	try {
        		out.putNextEntry(new ZipEntry(base));
                in = new FileInputStream(f);
                byte[] content = new byte[Integer.parseInt(GlobalContext
                        .getProperty("STREAM_SLICE"))];
                int len;
                while ((len = in.read(content)) != -1) {
                    out.write(content, 0, len);
                    out.flush();
                }
        	}catch(Exception e) {
        		throw e;
        	}finally {
        		if(in!=null) {
        			in.close();
        		}
        	}
        }
    }

    /**
     * 读取所有目录下的文件
     * @param filepath 读取的目录
     * @return list 文件列表
     */
    public static List<File> readfileList(String filepath) {
        List<File> fileList = new ArrayList<>();
        return readfileList(filepath, fileList);
    }

    /**
     * 读取所有目录下的文件
     * @param filepath 读取的目录
     * @param list     文件列表（包括目录）
     * @return list 文件列表
     */
    private static List<File> readfileList(String filepath, List<File> list) {
        File file = new File(filepath);
        if (!file.isDirectory()) {
            list.add(file);
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + "/" + filelist[i]);
                if (!readfile.isDirectory()) {
                    list.add(readfile);
                } else if (readfile.isDirectory()) {
                    readfileList(filepath + "/" + filelist[i], list);
                }
            }
        }
        return list;
    }

    /**
     * 读取所有目录下的文件
     * @param filepath 读取的目录
     * @param list     文件列表（包括目录）
     * @return list 文件列表
     */
    public static List<String> readfile(String filepath, List<String> list) throws Exception {
        if (list == null) {
            list = new ArrayList<>();
        }
        File file = new File(filepath);
        // 文件
        if (!file.isDirectory()) {
            list.add(file.getPath());

        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            if(filelist != null && filelist.length >0){
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "/" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        list.add(readfile.getPath());
                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "/" + filelist[i], list);
                    }
                }
            }
        }
        return list;
    }


    private static List<String> imgExt = new ArrayList<>();

    static {
        imgExt.add("png");
        imgExt.add("jpg");
        imgExt.add("bmp");
        imgExt.add("jpeg");
        imgExt.add("jpe");
        imgExt.add("gif");
    }


    /**
     * 获取文件名后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileExt(String fileName) {
        String fileExt = null;
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            fileExt = "";
        } else {
            fileExt = fileName.substring(index + 1)
                    .toLowerCase();
        }
        return fileExt;
    }

    public static boolean isImage(String fileName) {
        return imgExt.contains(getFileExt(fileName));
    }

    public static boolean isImageExt(String ext) {
        return imgExt.contains(ext);
    }

    public static String getFileName(String fullFileName) {
        int index = fullFileName.lastIndexOf('.');
        if (index == -1) {
            return fullFileName;
        } else {
            return fullFileName.substring(0, index);
        }
    }

}
