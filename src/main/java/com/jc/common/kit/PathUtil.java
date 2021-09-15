package com.jc.common.kit;

public class PathUtil {

    /**
     * 去除多余路径
     *
     * @param path
     * @return
     */
    public static String trim(String path){
        if(path == null||path.trim().length()<=0){
            return null;
        }
        String tempPath = path.replace(".","/");
        while(tempPath.startsWith("/")||tempPath.startsWith("\\")){
            tempPath = tempPath.substring(1);
        }
        while(tempPath.endsWith("/")||tempPath.endsWith("\\")){
            tempPath = tempPath.substring(0,tempPath.length()-1);
        }
        return tempPath;
    }

}
