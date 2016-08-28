package com.otess.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.kit.JsonKit;
import com.otess.model.RoleModel;

/**
 * office 操作工具
 * 
 * @author jinweida
 */
public final class TransCodeUtils {

	public static final TransCodeUtils me = new TransCodeUtils();

	class PrintStream extends Thread {
		java.io.InputStream __is = null;

		public PrintStream(java.io.InputStream is) {
			__is = is;
		}

		public void run() {
			try {
				while (this != null) {
					int _ch = __is.read();
					if (_ch != -1)
						System.out.print((char) _ch);
					else
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean convert(String ffmpegPath, String upFilePath, String codcFilePath, String mediaPicPath) {
		String type = upFilePath.substring(upFilePath.lastIndexOf(".") + 1, upFilePath.length()).toLowerCase();
		try {
			boolean flag=false;
			if (type.equals("avi") || type.equals("mpg") || type.equals("wmv") ||
					type.equals("3gp") || type.equals("mov") || type.equals("mp4") ||
					type.equals("asf") || type.equals("asx") || type.equals("flv") ||
					type.equals("mpg") || type.equals("f4v") || type.equals("vob") || 
					type.equals("rmvb") || type.equals("rm") || type.equals("mkv")) {
				return this.executeCodecs(ffmpegPath, upFilePath, codcFilePath, mediaPicPath);
			}
			// 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
			// 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
			else if (type.equals("wmv9")) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	/**
	 * 视频转码
	 * 
	 * @param ffmpegPath
	 *            转码工具的存放路径
	 * @param upFilePath
	 *            用于指定要转换格式的文件,要截图的视频源文件
	 * @param codcFilePath
	 *            格式转换后的的文件保存路径
	 * @param mediaPicPath
	 *            截图保存路径
	 * @return
	 * @throws Exception
	 */
	public boolean executeCodecs(String ffmpegPath, String upFilePath, String codcFilePath, String mediaPicPath)
			throws Exception {
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> convert = new ArrayList<String>();
		convert.add(ffmpegPath); // 添加转换工具路径
		convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
		convert.add(upFilePath); // 添加要转换格式的视频文件的路径
		convert.add("-qscale"); // 指定转换的质量
		convert.add("6");
		convert.add("-ab"); // 设置音频码率
		convert.add("64");
		convert.add("-ac"); // 设置声道数
		convert.add("2");
		convert.add("-ar"); // 设置声音的采样频率
		convert.add("22050");
		convert.add("-r"); // 设置帧频
		convert.add("24");
		convert.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
		convert.add(codcFilePath);

		// 创建一个List集合来保存从视频中截取图片的命令
		List<String> cutpic = new ArrayList<String>();
		cutpic.add(ffmpegPath);
		cutpic.add("-i");
		cutpic.add(upFilePath); // 同上（指定的文件即可以是转换为flv格式之前的文件，也可以是转换的flv文件）
		cutpic.add("-y");
		cutpic.add("-f");
		cutpic.add("image2");
		cutpic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
		cutpic.add("1"); // 添加起始时间为第17秒
		// cutpic.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
		// cutpic.add("0.001"); // 添加持续时间为1毫秒
		// cutpic.add("-s"); // 添加参数＂-s＂，该参数指定截取的图片大小
		// cutpic.add("800*280"); // 添加截取的图片大小为350*240
		cutpic.add(mediaPicPath); // 添加截取的图片的保存路径
		ProcessBuilder builder = new ProcessBuilder();
		try {
			// http://www.cnblogs.com/zhwl/p/4670478.html
			Process processVideo = new ProcessBuilder(convert).start();

			System.out.println("视频转码--start:" + DateUtils.getNowTime());
			new PrintStream(processVideo.getErrorStream()).start();
			new PrintStream(processVideo.getInputStream()).start();
			processVideo.waitFor();
			System.out.println("视频转码-Finished:" + DateUtils.getNowTime());

			Process processImage = new ProcessBuilder(cutpic).start();

			System.out.println("抽取图片--start:" + DateUtils.getNowTime());
			new PrintStream(processImage.getErrorStream()).start();
			new PrintStream(processImage.getInputStream()).start();
			processImage.waitFor();
			System.out.println("抽取图片-Finished:" + DateUtils.getNowTime());
			return true;

			/*
			 * builder.command(convert); builder.redirectErrorStream(true);
			 * builder.start();
			 * 
			 * builder.command(cutpic); builder.redirectErrorStream(true); //
			 * 如果此属性为 true，则任何由通过此对象的 start() 方法启动的后续子进程生成的错误输出都将与标准输出合并， //
			 * 因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
			 * builder.start();
			 */
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return false;
		}
	}
	  
	
	/**
	 * 获取时长 格式:"00:00:10.68"
	 * @param timelen
	 * @return
	 */
    public static int getTimelen(String timelen){
        int min=0;
        String strs[] = timelen.split(":");
        if (strs[0].compareTo("0") > 0) {
            min+=Integer.valueOf(strs[0])*60*60;//秒
        }
        if(strs[1].compareTo("0")>0){
            min+=Integer.valueOf(strs[1])*60;
        }
        if(strs[2].compareTo("0")>0){
            min+=Math.round(Float.valueOf(strs[2]));
        }
        return min;
    }
	    
	public static int duration(String ffmpegPath,String inputPath){
		//zhuanma:ffmpeg version N-79209-gb3eda69 Copyright (c) 2000-2016 the FFmpeg developers  built with gcc 5.3.0 (GCC)  configuration: --enable-gpl --enable-version3 --disable-w32threads --enable-avisynth --enable-bzlib --enable-fontconfig --enable-frei0r --enable-gnutls --enable-iconv --enable-libass --enable-libbluray --enable-libbs2b --enable-libcaca --enable-libfreetype --enable-libgme --enable-libgsm --enable-libilbc --enable-libmodplug --enable-libmfx --enable-libmp3lame --enable-libopencore-amrnb --enable-libopencore-amrwb --enable-libopenjpeg --enable-libopus --enable-librtmp --enable-libschroedinger --enable-libsnappy --enable-libsoxr --enable-libspeex --enable-libtheora --enable-libtwolame --enable-libvidstab --enable-libvo-amrwbenc --enable-libvorbis --enable-libvpx --enable-libwavpack --enable-libwebp --enable-libx264 --enable-libx265 --enable-libxavs --enable-libxvid --enable-libzimg --enable-lzma --enable-decklink --enable-zlib  libavutil      55. 19.100 / 55. 19.100  libavcodec     57. 33.100 / 57. 33.100  libavformat    57. 29.101 / 57. 29.101  libavdevice    57.  0.101 / 57.  0.101  libavfilter     6. 40.102 /  6. 40.102  libswscale      4.  1.100 /  4.  1.100  libswresample   2.  0.101 /  2.  0.101  libpostproc    54.  0.100 / 54.  0.100Input #0, flv, from 'D:\spring\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\otess/public/upload/flv1.flv':  Metadata:    metadatacreator : Yet Another Metadata Injector for FLV - Version 1.4    hasKeyframes    : true    hasVideo        : true    hasAudio        : true    hasMetadata     : true    canSeekToEnd    : true    datasize        : 12585661    videosize       : 11567684    audiosize       : 985093    lasttimestamp   : 114    lastkeyframetimestamp: 114    lastkeyframelocation: 12588509  Duration: 00:01:54.28, start: 0.047000, bitrate: 881 kb/s    Stream #0:0: Video: h264 (Main), yuv420p, 720x576 [SAR 16:15 DAR 4:3], 807 kb/s, 25 fps, 25 tbr, 1k tbn, 50 tbc    Stream #0:1: Audio: aac (LC), 48000 Hz, stereo, fltp, 64 kb/s[NULL @ 000000000255b840] Unable to find a suitable output format for 'ffmpeg'ffmpeg: Invalid argument
		String result= process(ffmpegPath,inputPath);
		System.out.println(result);
        
      //从视频信息中解析时长  
        String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";  
        Pattern pattern = Pattern.compile(regexDuration); 
        Matcher m = pattern.matcher(result); 
        System.out.println(JsonKit.toJson(m));
        if (m.find()) {  
            return getTimelen(m.group(1)); 
        }  
		return 0;
	}
	//  ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）  
    private static String process(String ffmpegPath,String inputPath) {  

        List<String> commend=new java.util.ArrayList<String>();  
         
        commend.add(ffmpegPath);//可以设置环境变量从而省去这行  
        commend.add("ffmpeg");  
        commend.add("-i");  
        commend.add(inputPath);  
        
        try {  
  
            ProcessBuilder builder = new ProcessBuilder();  
            builder.command(commend);  
            builder.redirectErrorStream(true);  
            Process p= builder.start();  
  
           //1. start  
            BufferedReader buf = null; // 保存ffmpeg的输出结果流  
            String line = null;  
            buf = new BufferedReader(new InputStreamReader(p.getInputStream()));  
             
            StringBuffer sb= new StringBuffer();  
            while ((line = buf.readLine()) != null) {  
            	System.out.println(line);  
            	sb.append(line);  
            	continue;  
            }  
            int ret = p.waitFor();//这里线程阻塞，将等待外部转换进程运行成功运行结束后，才往下执行  
            //1. end  
            return sb.toString();  
        } catch (Exception e) {  
            System.out.println(e);  
            return null;  
        }  
    }  
}
