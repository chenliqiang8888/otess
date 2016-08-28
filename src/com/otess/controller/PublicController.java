package com.otess.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.otess.common.bean.BaseResponse;
import com.otess.common.utils.OfficeUtils;
import com.otess.model.SysLogModel;
import com.otess.model.UserModel;

import java.io.File;

public class PublicController extends BaseController {
	public void login(){
		System.out.print(HttpKit.post("https://buy.itunes.apple.com/verifyReceipt", "sdf"));
		render("/views/login.html");
	}
	
	@Before(POST.class)
	public void dologin(){
		String username=getPara("username");
		String password=getPara("password");
		UserModel user=UserModel.me.findByName(username);
		System.out.println(JsonKit.toJson(user));
		if(StrKit.notNull(user)){
			if(user.getStr("password").equals(password)){
				setSessionAttr("user",user);
				this.setSysLog(username+"登陆系统", 1000);
		        renderJson(new BaseResponse().setMessage("登录成功！"));
			}else{
		        renderJson(new BaseResponse().setCode(500).setMessage("密码错误"));
			}
		}else{
	        renderJson(new BaseResponse().setCode(500).setMessage("用户名不存在"));
		}
	}
	public void logout(){
		UserModel user=getSessionAttr("user");
		this.setSysLog(user.getStr("username")+"退出系统", 1000);
		removeSessionAttr("user");		
		render("/views/login.html");
	}
}
