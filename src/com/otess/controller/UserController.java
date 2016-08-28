package com.otess.controller;

import java.util.List;

import com.otess.common.bean.Code;
import com.otess.common.bean.DataResponse;
import com.otess.interceptor.AdminInterceptor;
import com.otess.model.FoldModel;
import com.otess.model.MediaFileModel;
import com.otess.model.RoleModel;
import com.otess.model.TaskModel;
import com.otess.model.UserModel;
import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;

@Before(AdminInterceptor.class)
public class UserController extends BaseController {
	public void list(){
		setAttr("userPage", UserModel.me.paginate(getParaToInt(0, 1), 10));
		render("/views/user/list.html");
	}
	public void add(){
		setAttr("role",RoleModel.me.findAll());
		setAttr("fold",JsonKit.toJson(FoldModel.me.findAll()));
		setAttr("userlist",UserModel.me.findAll());
		render("/views/user/add.html");
	}
	public void edit(){
		Integer uid=getParaToInt(0);
		setAttr("role",RoleModel.me.findAll());
		setAttr("fold",JsonKit.toJson(FoldModel.me.findAll()));
		setAttr("userlist",UserModel.me.findAll());
		setAttr("model",UserModel.me.findById(uid));
		render("/views/user/edit.html");
	}
	public void dosave(){
		UserModel userModel = getModel(UserModel.class, "user");
		if(UserModel.me.findByName(userModel.getStr("username"))==null){
			userModel.set("name", userModel.get("username"));
			userModel.save();
			Integer uid = userModel.get("uid");
			if (uid > 0) {
				this.setSysLog("创建用户："+ userModel.get("username"), 1001);
				renderSuccess("操作成功!");
			} else {
				renderArgumentError("参数错误!");
			}
		}else{
			renderArgumentError("用户已经存在了!");
		}
	}
	public void doupdate(){
		UserModel userModel = getModel(UserModel.class, "user");
		UserModel u=UserModel.me.findById(userModel.getInt("uid"));
		if(u!=null){
			if (userModel.update()) {
				this.setSysLog("编辑用户："+ u.get("username"), 1001);
				renderSuccess("操作成功!");
			} else {
				renderArgumentError("参数错误!");
			}
		}else{
			renderArgumentError("用户不存在!");
		}
	}
	public void delete(){
		String[] ids = getPara("ids").split(",");
		for (String id : ids) {
			UserModel u=UserModel.me.findById(id);
			UserModel.me.deleteById(id);
			this.setSysLog("删除用户："+ u.getStr("username"), 1001);
		}
		redirect("/user/list");
	}
	public void changepwd(){
		String user_pass=getPara("user_pass");
		String user_pass_new=getPara("user_pass_new");
		String user_pass_new_confim=getPara("user_pass_new_confim");
		UserModel user=getSessionAttr("user");
		System.out.println(user_pass_new);
		System.out.println(user_pass_new_confim);
		System.out.println(user_pass_new.equals(user_pass_new_confim));
		if(user_pass_new.equals(user_pass_new_confim)){
			UserModel userModel=UserModel.me.findByName(user.getStr("username"));
			System.out.println(JsonKit.toJson(userModel));
			if(user_pass.equals(userModel.getStr("password"))){
				userModel.set("password", user_pass_new).update();
				this.setSysLog("修改密码："+ user.getStr("username"), 1001);
				renderSuccess("操作成功!");
			}else{
				renderArgumentError("旧密码错误");
			}
		}else{
			renderArgumentError("两次密码输入不一致");
		}		
	}
}