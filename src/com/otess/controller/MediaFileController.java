package com.otess.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.util.security.Credential.MD5;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.FileKit;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.otess.interceptor.AdminInterceptor;
import com.otess.model.FileTypeModel;
import com.otess.model.LogSendModel;
import com.otess.model.MediaFileModel;
import com.otess.common.bean.Code;
import com.otess.common.bean.DataResponse;
import com.otess.common.bean.ListResponse;
import com.otess.common.bean.PageListResponse;
import com.otess.common.utils.*;

/**
 * 2.忽略null值 假设前提：user.name为null ${user.name}，异常 ${user.name!},显示空白
 * ${user.name!'vakin'}，若user.name不为空则显示本身的值，否则显示vakin
 * ${user.name?default('vakin')}，同上 ${user.name???string(user.name,'vakin')},同上
 * 
 * @author j
 *
 */
@Before(AdminInterceptor.class)
public class MediaFileController extends BaseController {
	public void list() {
		setAttr("mediaFilePage", MediaFileModel.me.paginate(getParaToInt("p", 1), this.getPageSize(), getPara("m_name"),
				getPara("m_s_addtime"), getPara("m_e_addtime"),this.getCurrentRoleType(),getPara("m_type"),this.getCurrentUserIdByRoleType()));
		setAttr("fileTypePage", FileTypeModel.me.findAll());
		render("/views/mediafile/list.html");
	}
	public void upload() {
		
		String dir=PathKit.getWebRootPath() + "/public/upload/";
		UploadFile file = getFile("file", dir, 5000 * 1024 * 1024);// 上传文件
																			// 并且获取
		
		String md5="";		
		String fileName=file.getFileName();
		String outFile=dir + fileName;
		
		if(OfficeUtils.convert2PDF(dir + file.getFileName(),outFile+".pdf")){
			fileName+=".pdf";
		}else{
			//if(fileType.getInt("f_id").equals(5)){
				//fileName+=".mp4";
			//}
		}
		FileTypeModel fileType = FileTypeModel.me.findByType(FileUtils.getExtension(fileName.toLowerCase()));
		Integer f_id = 0;
		if (StrKit.notNull(fileType))f_id = fileType.getInt("f_id");

		//md5=DigestUtils.md5Hex(new FileInputStream(new File(outFile)));
		md5=DigestUtils.md5Hex(fileName);
		System.out.println(md5);
		
		MediaFileModel existModel=MediaFileModel.me.findByNo(md5);
		MediaFileModel mediaFile=new MediaFileModel().set("m_name", fileName).set("m_file", fileName)
				.set("m_size_high", getParaToInt("size")).set("m_no",md5)
				.set("m_type", f_id).set("m_userid", this.getCurrentUserId());
		if(StrKit.notNull(existModel)){
			mediaFile.set("m_id", existModel.getInt("m_id"));
			mediaFile.update();
			//已经存在同样的资源了
		}else{
			if(f_id==5){
				String inFile=PathKit.getWebRootPath() + "/public/upload/"+fileName;
				String ffmpeg=PathKit.getWebRootPath() + "/public/tools/ffmpeg.exe";
				Integer duration=TransCodeUtils.me.duration(ffmpeg, inFile);
				
				mediaFile.set("m_duration", duration);
			}
			mediaFile.save();
		}

		this.setSysLog("上传："+fileName, 1003);
		System.out.println(dir);
		renderJson(new DataResponse(Code.SUCCESS).setData(mediaFile).setMessage(fileType.getStr("f_name")));		
	}
	public void transVideo(){
		String m_id=getPara("m_id");
		String inFile=PathKit.getWebRootPath() + "/public/upload/"+getPara("filename").replace(".mp4", "");
		String ffmpeg=PathKit.getWebRootPath() + "/public/tools/ffmpeg.exe";
		String outFile=inFile+".mp4";
		String outImage=inFile+".jpg";
		if(TransCodeUtils.me.convert(ffmpeg, inFile,outFile, outImage)){
			MediaFileModel model=new MediaFileModel()
					.set("m_id",m_id)
					.set("m_from_top",1);
					model.update();
			renderSuccess("成功了");
		}else{
			render404("资源不存在！");
		}
	}

	public void transcode() {
		render("/views/mediafile/transcode.html");
	}

	public void delete() {
		String[] ids = getPara("ids").split(",");
		for (String id : ids) {
			MediaFileModel file=MediaFileModel.me.findById(id);
			String filename=file.getStr("m_name");
			MediaFileModel.me.deleteById(id);
			this.setSysLog("删除："+filename, 1003);
		}
		redirect("/mediafile/list");
	}

	public void getMediaAll() {
		Integer m_type=getParaToInt("m_type")==null?0:getParaToInt("m_type");
		System.out.println(m_type);
		List<MediaFileModel> list = MediaFileModel.me.findAll(m_type,getPara("m_name"),this.getCurrentUserIdByRoleType(),this.getCurrentRoleType());
		ListResponse response = new ListResponse().setList(list);
		response.setCode(Code.SUCCESS);
		response.setMessage("操作成功！");
		renderJson(response);
	}
	public void setState(){
		String[] ids = getPara("ids").split(",");
		Integer m_state=getParaToInt("m_state");
		int count=0;
		for (String id : ids) {
			MediaFileModel media=new MediaFileModel()
					.set("m_state", m_state)
					.set("m_id", id);
			
			if(media.update()){
				count++;

				MediaFileModel file=MediaFileModel.me.findById(id);
				String filename=file.getStr("m_name");
				if(m_state.equals(0)){
					this.setSysLog("私有资源："+filename, 1003);
				}else{
					this.setSysLog("共享资源："+filename, 1003);
				}
			}
		}
		renderSuccess("["+count+"]条 操作成功!");
	}
	public void preview(){
		setAttr("mediafile", JsonKit.toJson(MediaFileModel.me.findById(getParaToInt("m_id"))));
		render("/views/mediafile/preview.html");
		
	}
	public void txt(){
		String m_file=MediaFileModel.me.findByNo(getPara("m_no")).getStr("m_file");
		String filePath=PathKit.getWebRootPath()+"/public/upload/"+m_file;
		System.out.println(filePath);
		String txt=FileUtils.txt2String(filePath);
		System.out.println(txt);
		setAttr("txt", txt);
		render("/views/mediafile/txtpreview.html");
	}
}
