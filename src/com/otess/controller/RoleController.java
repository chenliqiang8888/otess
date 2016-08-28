package com.otess.controller;

import java.util.List;

import com.otess.common.bean.Code;
import com.otess.common.bean.DataResponse;
import com.otess.interceptor.AdminInterceptor;
import com.otess.model.ActionModel;
import com.otess.model.RoleModel;
import com.otess.model.UserModel;
import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;

@Before(AdminInterceptor.class)
public class RoleController extends BaseController {
	public void list(){
		setAttr("role", RoleModel.me.findAll());
		render("/views/role/list.html");
	}
	public void edit(){
		Integer r_id=getParaToInt(0);
		setAttr("roleaction",JsonKit.toJson(RoleModel.me.findById(r_id)));
		setAttr("action", JsonKit.toJson(ActionModel.me.findAll()));
		render("/views/role/edit.html");
	}
	public void doupdate(){
		String r_permission=getPara("r_permission");
		Integer r_id=getParaToInt("r_id");
		RoleModel roleModel=new RoleModel()
				.set("r_permission", r_permission)
				.set("r_id", r_id);
		if(roleModel.update()){
			UserModel userModel = getModel(UserModel.class, "user");
			this.setSysLog("修改角色："+ userModel.get("username"), 1001);
			renderSuccess("操作成功!");
		}else{
			renderArgumentError("参数错误");
		}
	}
}