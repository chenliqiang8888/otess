package com.otess.common.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * CSV操作工具
 */
public final class CSVUtils {
	//CSV文件分隔符
    private static final String NEW_LINE_SEPARATOR = "\n";
    /**
     * 写CSV文件
     * 
     * @param fileName
     */
    public static void writeCsvFile(String fileName,Object[] FILE_HEADER,List<String> dataList) {
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        //创建 CSVFormat
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        try {
            //初始化FileWriter
            fileWriter = new FileWriter(fileName);
            //初始化 CSVPrinter
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            //创建CSV文件头
            csvFilePrinter.printRecord(FILE_HEADER); 

            csvFilePrinter.printRecord(dataList);
             
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
