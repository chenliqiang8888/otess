package com.otess.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.otess.interceptor.AdminInterceptor;
import com.otess.model.ClientModel;
import com.otess.model.FoldModel;
import com.otess.model.LogSendModel;
import com.otess.model.MediaFileModel;
import com.otess.model.TaskModel;

@Before(AdminInterceptor.class)
public class FoldController extends BaseController {
	public void list(){
		setAttr("fold",JsonKit.toJson(FoldModel.me.findAll()));
		
		render("/views/fold/list.html");
	}
	public void dosave(){
		Integer cf_id=getParaToInt("cf_id");
		FoldModel me=new FoldModel()
				.set("cf_name",getPara("cf_name"))
				.set("cf_info",getPara("cf_info"))
				.set("cf_pid",getParaToInt("cf_pid"));
		System.out.print(me.toJson());
		System.out.print(cf_id);
		boolean result=true;
		if(cf_id!=null){
			me.set("cf_id",cf_id);
			result=me.update();
		}else{
			result=me.save();
		}
		if(result){
			renderSuccess("操作成功!");
		}else{
			renderArgumentError("参数错误！");
		}
	}
	public void delete(){
		String[] ids=getPara("cf_ids").split(",");
		for(String id:ids){
			FoldModel.me.deleteById(id);
			ClientModel.me.updateGroupId(Integer.parseInt(id));
		}
		renderSuccess("删除成功了");
	}
}
