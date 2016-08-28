package com.otess.common.utils;

import javax.imageio.ImageIO;

import com.jfinal.core.JFinal;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.PathKit;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jinweida
 */
public final class FileUtils {
	/**
	 * 获取不带扩展名的文件名
	 * @param filename
	 * @return
	 */
	public static String getFileNameNoEx(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length()))) {   
                return filename.substring(0, dot);   
            }   
        }   
        return filename;   
    } 

	/**
	 * 获取文件扩展名*
	 * 
	 * @param fileName
	 *            文件名
	 * @return 扩展名
	 */
	public static String getExtension(String fileName) {
		int i = fileName.lastIndexOf(".");
		if (i < 0)
			return null;

		return fileName.substring(i + 1);
	}

	/**
	 * 获取文件扩展名*
	 * 
	 * @param file
	 *            文件对象
	 * @return 扩展名
	 */
	public static String getExtension(File file) {
		if (file == null)
			return null;

		if (file.isDirectory())
			return null;

		String fileName = file.getName();
		return getExtension(fileName);
	}

	/**
	 * 读取文件*
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 文件对象
	 */
	public static File readFile(String filePath) {
		File file = new File(filePath);
		if (file.isDirectory())
			return null;

		if (!file.exists())
			return null;

		return file;
	}

	/**
	 * 复制文件
	 * 
	 * @param oldFilePath
	 *            源文件路径
	 * @param newFilePath
	 *            目标文件毒经
	 * @return 是否成功
	 */
	public static boolean copyFile(String oldFilePath, String newFilePath) {
		try {
			int byteRead = 0;
			File oldFile = new File(oldFilePath);
			if (oldFile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldFilePath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newFilePath);
				byte[] buffer = new byte[1444];
				while ((byteRead = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteRead);
				}
				inStream.close();
				fs.close();
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错 ");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 *            文件地址
	 * @return 是否成功
	 */
	public static boolean delFile(String filePath) {
		return delFile(new File(filePath));
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 *            文件对象
	 * @return 是否成功
	 */
	public static boolean delFile(File file) {
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * png图片转jpg*
	 * 
	 * @param pngImage
	 *            png图片对象
	 * @param jpegFile
	 *            jpg图片对象
	 * @return 转换是否成功
	 */
	public static boolean png2jpeg(File pngImage, File jpegFile) {
		BufferedImage bufferedImage;

		try {
			bufferedImage = ImageIO.read(pngImage);

			BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);

			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

			ImageIO.write(bufferedImage, "jpg", jpegFile);

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断文件是否是图片*
	 * 
	 * @param imgFile
	 *            文件对象
	 * @return
	 */
	public static boolean isImage(File imgFile) {
		try {
			BufferedImage image = ImageIO.read(imgFile);
			return image != null;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据系统当前时间，返回时间层次的文件夹结果，如：upload/2015/01/18/0.jpg
	 * 
	 * @return
	 */
	public static String getTimeFilePath() {
		return new SimpleDateFormat("/yyyy/MM/dd").format(new Date()) + "/";
	}

	/**
	 * 将文件头转换成16进制字符串
	 *
	 * @param src
	 *            原生byte
	 * @return 16进制字符串
	 */
	private static String bytesToHexString(byte[] src) {

		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 得到文件头
	 *
	 * @param file
	 *            文件
	 * @return 文件头
	 * @throws IOException
	 */
	private static String getFileContent(File file) throws IOException {

		byte[] b = new byte[28];

		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(file);
			inputStream.read(b, 0, 28);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
		return bytesToHexString(b);
	}
	
	// 写文件
	public static String WriteTxt(String content) {
		BufferedWriter fw = null;
		String filename = "zm_"+DateUtils.getNowTime() + ".txt";
		try {
			File file = new File(PathKit.getWebRootPath() + "/public/upload/" + filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8")); // 指定编码格式，以免读取时中文字符异常
			fw.append(content.replace("\r\n", System.getProperty("line.separator")));
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return filename;
	}
	/*public static String WriteTxt(String fileContent)   
	{     
    	String filename = DateUtils.getNowTime() + ".txt";
	    try   
	    {   
	        File f = new File(PathKit.getWebRootPath() + "/public/upload/"+filename);      
	        if (!f.exists())   
	        {       
	            f.createNewFile();      
	        }      
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"gbk");      
	        BufferedWriter writer=new BufferedWriter(write);          
	        writer.write(fileContent);      
	        writer.close();     
	    } catch (Exception e)   
	    {      
	        e.printStackTrace();     
	    } 
		return filename; 
	}  */
	
//	public static String txt2String(String filePath) {
//		String result = "";
//		File file = new File(filePath);
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
//			String s = null;
//			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
//				
//				if(!result.equals(""))result+="\r\n";
//				result +=s;
//			}
//			br.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	public static String txt2String(String fileName)  
	{     
	    String fileContent = "";     
	    try   
	    {       
	        File f = new File(fileName);      
	        if(f.isFile()&&f.exists())  
	        {       
	        	String encode="GBK";
	        	if(fileName.indexOf("zm_")>-1){
	        		encode="UTF-8";
	        	}
	            InputStreamReader read = new InputStreamReader(new FileInputStream(f),encode);       
	            BufferedReader reader=new BufferedReader(read);       
	            String line;       
	            while ((line = reader.readLine()) != null)   
	            {      
	            	if(!line.equals(""))line+="\r\n";
	                fileContent += line;       
	            }         
	            read.close();      
	        }     
	    } catch (Exception e)   
	    {         
	        e.printStackTrace();     
	    }     
	    return fileContent;   
	}
	public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
	    try {
	        MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
	        MessageDigest md5 = MessageDigest.getInstance("MD5");
	        md5.update(byteBuffer);
	        BigInteger bi = new BigInteger(1, md5.digest());
	        value = bi.toString(16);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	            if(null != in) {
	                try {
	                in.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    return value;
	    /*
	     * public static void main(String[] args) {
		        String v = MD5Filter.getMd5ByFile(new File("C:\\Users\\Administrator\\Desktop\\index.txt"));
		    	System.out.println(v.toUpperCase());
		   }
	     * */
    }

	public static void main(String[] args) {
		System.out.print(WriteTxt("测试了啊"));
	}

}
