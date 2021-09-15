package com.jc.archive.util;

import java.util.List;

import com.jc.archive.domain.Folder;
import com.jc.archive.domain.Permission;

public class PermissionUtil {

	public static String permission(String permissionValue, String value, String num){
		if("0".equals(value)){
			value="1";
		} else {
			value="0";
		}
		return permissionValue.substring(0, Integer.valueOf(num)-1)+value+permissionValue.substring(Integer.valueOf(num));
	}
	
	public static Folder permissionValue(List<Permission> mission){
		String a1="";
		String a2="";
		String a3="";
		String a4="";
		String a5="";
		String a6="";
		String a7="";
		String a8="";
		String a9="";
		String a10="";
		Folder folder = new Folder();
		for(int i=0;i<mission.size();i++){
			Permission permission = mission.get(i);
			if(!"1".equals(a1)){
				a1 = permission.getPermissionValue().substring(0, 1);
			}
			if(!"1".equals(a2)){
				a2 = permission.getPermissionValue().substring(1, 2);
			}
			if(!"1".equals(a3)){
				a3 = permission.getPermissionValue().substring(2, 3);
			}
			if(!"1".equals(a4)){
				a4 = permission.getPermissionValue().substring(3, 4);
			}
			if(!"1".equals(a5)){
				a5 = permission.getPermissionValue().substring(4, 5);
			}
			if(!"1".equals(a6)){
				a6 = permission.getPermissionValue().substring(5, 6);
			}
			if(!"1".equals(a7)){
				a7 = permission.getPermissionValue().substring(6, 7);
			}
			if(!"1".equals(a8)){
				a8 = permission.getPermissionValue().substring(7, 8);
			}
			if(!"1".equals(a9)){
				a9 = permission.getPermissionValue().substring(8, 9);
			}
			if(!"1".equals(a10)){
				a10 = permission.getPermissionValue().substring(9, 10);
			}
		}
		folder.setPermView(a1.equals("1")?true:false);
		folder.setPermNewUpDown(a2.equals("1")?true:false);
		folder.setPermEdit(a3.equals("1")?true:false);
		folder.setPermDelete(a4.equals("1")?true:false);
		folder.setPermCopyPaste(a5.equals("1")?true:false);
		folder.setPermRename(a6.equals("1")?true:false);
		folder.setPermCollect(a7.equals("1")?true:false);
		folder.setPermVersion(a8.equals("1")?true:false);
		folder.setPermHistory(a9.equals("1")?true:false);
		folder.setPermRelate(a10.equals("1")?true:false);
		return folder;
	}
	

	public static String permissionType(String num){
		
		String type = "";
		if("1".equals(num)){
			type = "浏览";
		} else if("2".equals(num)){
			type = "新建/上传/下载";
		} else if("3".equals(num)){
			type = "编辑";
		} else if("4".equals(num)){
			type = "彻底删除";
		} else if("5".equals(num)){
			type = "复制/剪切";
		} else if("6".equals(num)){
			type = "重命名";
		} else if("7".equals(num)){
			type = "收藏";
		} else if("8".equals(num)){
			type = "版本管理";
		} else if("9".equals(num)){
			type = "文档审计";
		} else if("10".equals(num)){
			type = "文档关联";
		} else if("11".equals(num)){
			type = "授权";
		} else if("12".equals(num)){
			type = "删除到回收站";
		} else if("13".equals(num)){
			type = "清空回收站";
		}
		return type;
	}
}
