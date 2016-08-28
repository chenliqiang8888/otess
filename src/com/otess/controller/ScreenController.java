package com.otess.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.otess.common.bean.ListResponse;
import com.otess.interceptor.AdminInterceptor;
import com.otess.model.MediaFileModel;
import com.otess.model.ScreenModel;

@Before(AdminInterceptor.class)
public class ScreenController extends BaseController {
	public void list(){
		setAttr("screen",ScreenModel.me.findAll());
		render("/views/screen/list.html");
	}
	public void saveas(){
		ScreenModel screenModel=new ScreenModel()
				.set("sc_name",getPara("sc_name"))
				.set("sc_info",getPara("sc_info"))
				.set("sc_target",getPara("sc_target"))
				.set("sc_default",0);
		screenModel.save();
		if(screenModel.getInt("sc_id")>0){
			this.setSysLog("另存为屏幕布局："+getPara("sc_name"), 1002);
			renderSuccess("操作成功!");
		}else{
			renderArgumentError("参数错误");
		}
	}
	public void delete() {
		String[] ids = getPara("ids").split(",");
		for (String id : ids) {

			ScreenModel screen=ScreenModel.me.findById(id);
			ScreenModel.me.deleteById(id);
			this.setSysLog("删除屏幕布局："+screen.getStr("sc_name"), 1002);
		}
		redirect("/screen/list");
	}
	/**
	 * 异步获取全部数据
	 */
	public void asynall(){
		renderJson(new ListResponse().setList(ScreenModel.me.findAll()));
	}
}
