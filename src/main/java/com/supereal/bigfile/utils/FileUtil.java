package com.supereal.bigfile.utils;


import java.io.File;
import java.io.FileFilter;

/**
 * Create by tianci
 * 2019/1/10 14:59
 * @author bitmain
 */
public class FileUtil {

    /**
     * 根据文件路径，以及文件名查找出该目录下有多少文件
     * @param path
     * @param fileName
     * @return
     */
    public static int getPathFileCount(String path,String fileName) {
        File allFile = new File(path);
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().startsWith(fileName + "_");
            }
        };
        File[] fileArray = allFile.listFiles(filter);
        int fileCount = fileArray == null ? 0 : fileArray.length;
        return fileCount;
    }



}
