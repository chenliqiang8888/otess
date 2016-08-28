package com.otess.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.otess.interceptor.AdminInterceptor;
import com.otess.model.ClientModel;
import com.otess.model.FileTypeModel;
import com.otess.model.LogSendModel;
import com.otess.model.TaskModel;

@Before(AdminInterceptor.class)
public class FileTypeController extends BaseController {
	public void list(){
		setAttr("fileTypePage", FileTypeModel.me.findAll());		
		render("/views/filetype/list.html");
	}
	
	public void update(){
		FileTypeModel me=new FileTypeModel()
				.set("f_type", getPara("f_type"))
				.set("f_id", getParaToInt("f_id"));
		if(me.update()){
			this.setSysLog("设置文件类型："+getPara("f_type"), 1003);
			renderSuccess("操作成功!");
		}else{
			renderArgumentError("参数错误！");			
		}
				
	}
}
