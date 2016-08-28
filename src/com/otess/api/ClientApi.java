package com.otess.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.otess.common.bean.Code;
import com.otess.common.bean.ListResponse;
import com.otess.common.utils.Base64Utils;
import com.otess.common.utils.DateUtils;
import com.otess.common.utils.HttpUtils;
import com.otess.interceptor.ComInterceptor;
import com.otess.model.ClientImgModel;
import com.otess.model.ClientModel;
import com.otess.model.ClientStatusModel;
import com.otess.model.LogSendModel;
import com.otess.model.OperatorModel;

import sun.misc.BASE64Decoder;
@Before(ComInterceptor.class)
public class ClientApi extends BaseApi{
	private static final Logger log = Logger.getLogger(ClientApi.class);
	public void monitor(){
		String deviceid=getPara(0);
		log.info("ClientApi...init...monitor");
		List<OperatorModel> list=OperatorModel.me.findByDeviceId(deviceid);
		ListResponse response=new ListResponse(list);
		//取出一次就删除
		OperatorModel.me.delete(deviceid);
		response.setCode(Code.SUCCESS);
		response.setMessage("操作成功！");
		renderJson(response);
	}
	/* 修改注册信息  */
	public void update(){
		String deviceid=getPara(0);
		String name=getPara("name");
		String ip=getPara("ip");
		ClientModel me=ClientModel.me.findByName(name);
		
		if(StrKit.notNull(me)){
			renderExists("名称已经被占用了");
		}else{			
			ClientModel clientModel=new ClientModel()
					.set("cl_name", name)
					.set("cl_deviceid", deviceid)
					.set("cl_ip", ip);
			if(ClientModel.me.update(clientModel)>0){
				renderSuccess("操作成功!");
			}else{
				render404("操作失败");
			}	
		}
	}
	/* 上报客户端状态  */
	public void status(){
		String deviceid=getPara(0);
		ClientStatusModel status=new ClientStatusModel()
				.set("createdat", DateUtils.getNowTimeFormat())
				.set("version", getPara("version"))
				.set("deviceid", deviceid);
		boolean flag=true;
		ClientModel clientModel=ClientModel.me.findByDeviceId(deviceid);
		if(StrKit.notNull(clientModel)){
			clientModel.set("cl_is_del",0).update();
			ClientStatusModel statusModel=ClientStatusModel.me.findById(deviceid);
			if(StrKit.notNull(statusModel)){
				flag=status.update();
			}else{
				flag=status.save();
			}
		}else{
			flag=false;
		}
		if(flag){
			renderSuccess("操作成功!");			
		}else{
			render404("操作失败");
		}
	}
	/**
	 * 远程监控图片上报，直接存储base64编码
	 */
	@Before(POST.class)
	public void img(){
		String deviceid=getPara(0);
		String data=getPara("data");
		System.out.println(data);
		//保存base64 为图片
		//decodeBase64ToImage(data,PathKit.getWebRootPath()+folder,filename);
		ClientImgModel me=new ClientImgModel()
				.set("i_deviceid",deviceid)
				.set("i_img",data);
		me.save();
		renderSuccess("操作成功!");
	}
	public void decodeBase64ToImage(String base64, String path,
	      String imgName) {
	    BASE64Decoder decoder = new BASE64Decoder();
	    try {
			if (!(new File(path).isDirectory())) {
				new File(path).mkdir();
			}
			FileOutputStream write = new FileOutputStream(new File(path + imgName));
			
			byte[] decoderBytes = decoder.decodeBuffer(base64);
			//byte[] decoderBytes=Base64Utils.decode(base64);
			write.write(decoderBytes);
			write.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
  	}
}
