package com.rilin.lzy.mybase.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import android.os.Environment;
import android.os.StatFs;

public class SDcardUtils {

	private static final long ERROR = -1;
	/**
	 * 判断sdcard是否可用
	 * @return true sdcard 可用，false sdcard不可用
	 */
	public static boolean isSDcardExists(){
		
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 得到sdcard路径
	 * @return
	 */
	public static String getSDcardPath(){
		if(isSDcardExists()){
			return  Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return null;
	}
	/**
	 * 得到SDcard剩余空间大小
	 * @return 大小  单位 byte.-1表示SDcard不可用
	 */
	public static long getAvailableExternalMemorySize() {
        if (isSDcardExists()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }
	
	 /**
	 * 得到SDcard总空间大小
	 * @return 大小  单位 byte. -1表示SDcard不可用
	 */
	public static long getTotalExternalMemorySize() {
	        if (isSDcardExists()) {
	            File path = Environment.getExternalStorageDirectory();
	            StatFs stat = new StatFs(path.getPath());
	            long blockSize = stat.getBlockSize();
	            long totalBlocks = stat.getBlockCount();
	            return totalBlocks * blockSize;
	        } else {
	            return ERROR;
	        }
	    }
	/**
	 * 创建文件
	 * @param path 文件路径
	 * @return true 创建文件成功
	 */
	public static boolean createFile(String path){
		boolean success = false;
		if(isSDcardExists()){
			if(path == null){
				new Throwable("文件路径不能为null");
				return false;
			}
			File file = new File(path);
			try {
				success = file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}
	
	/**
	 * 创建文件
	 * @param dir 父文件夹目录
	 * @param name 文件名
	 * @return true 文件创建成功
	 */
	public static boolean createFile(File dir,String name){
		boolean success = false;
		if(isSDcardExists()){
			if(name == null){
				new Throwable("文件名不能为null");
				return false;
			}
			File file = new File(dir, name);
			try {
				success = file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}
	
	/**
	 * 创建文件
	 * @param dir 父文件夹目录
	 * @param name 文件名
	 * @return true 文件创建成功
	 */
	public static File createFile2(String dir,String name){
		File file = null;
		if(isSDcardExists()){
			if(name == null){
				throw new NullPointerException("文件名不能为null");
			}
			File dirFile = new File(dir);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			file = new File(dirFile, name);
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}
	
	/**
	 * 创建文件
	 * @param fileDir 文件夹路径
	 * @param name 文件名
	 * @return true 创建成功
	 */
	public static boolean createFile(String fileDir,String name){
		boolean success = false;
		if(isSDcardExists()){
			if(name == null){
				new Throwable("文件名不能为null");
				return false;
			}
			File file = new File(fileDir, name);
			try {
				success = file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}
	
	/**
	 * 创建文件夹
	 * @param dir 文件夹路径
	 * @return true 创建文件夹成功
	 */
	public static boolean createDir(String dir){
		boolean success = false;
		if(isSDcardExists()){
			if(dir == null){
				new Throwable("文件夹路径不能为null");
				return false;
			}
			File file = new File(dir);
			success = file.mkdirs();
		}
		return success;
	}
	
	/**
	 * 创建文件夹
	 * @param fileDir 父文件夹路径
	 * @param dir 文件夹名
	 * @return true 创建文件夹成功
	 */
	public static boolean createDir(String fileDir,String dir){
		boolean success = false;
		if(isSDcardExists()){
			if(dir == null){
				new Throwable("文件夹名不能为null");
				return false;
			}
			File file= new File(fileDir,dir);
			success = file.mkdirs();
		}
		return success;
	}
	
	/**
	 * @param file 父文件夹
	 * @param dir 文件夹名
	 * @return true 创建文件夹成功
	 */
	public static boolean createDir(File file,String dir){
		boolean success = false;
		if(isSDcardExists()){
			if(dir == null){
				new Throwable("文件夹名不能为null");
				return false;
			}
			File file2= new File(file,dir);
			success = file2.mkdirs();
		}
		return success;
	}
	
	/**
	 * 得到文件大小，返回 byte 长度
	 * @param file
	 * @return
	 */
	public static long getFileSize(File file){
		if(!isSDcardExists()){
			return 0;
		}
		
		InputStream is = null;
		int size = 0;
		try {
			is = new FileInputStream(file);
			size = is.available();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return size;
	}
	
	/**
	 * 得到文件夹大小 单位（byte）
	 * @param dir
	 * @return
	 */
	public static long getFileDirSize(File dir){
		if(isSDcardExists()){
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for(int i = 0;i<files.length;i++){
			File file = files[i];
			if(file.isDirectory()){
				dirSize += getFileDirSize(file);
			}else{
				dirSize += getFileSize(file);
			}
		}
		return dirSize;
	}
	
	
	/**
	 * 得到格式化数据
	 * @param fileS
	 * @return
	 */
	public static String formatFileSize(long fileS)
	 {// 转换文件大小
	  DecimalFormat df = new DecimalFormat("#.00");
	  String fileSizeString = "";
	  if (fileS < 1024)
	  {
	   fileSizeString = df.format((double) fileS) + "B";
	  }
	  else if (fileS < 1048576)
	   {
	    fileSizeString = df.format((double) fileS / 1024) + "K";
	   }
	   else if (fileS < 1073741824)
	   {
	    fileSizeString = df.format((double) fileS / 1048576) + "M";
	   }
	   else
	   {
	    fileSizeString = df.format((double) fileS / 1073741824) + "G";
	   }
	  
	  if(fileSizeString.equals(".00B")){
		  fileSizeString = "0B";
	  }
	  return fileSizeString;
	 }
}
