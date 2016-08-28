package com.otess.interceptor;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.JsonKit;
import com.otess.common.Result;
import com.otess.common.bean.BaseResponse;
import com.otess.common.bean.Code;
import com.otess.common.utils.StringUtils;
import com.otess.model.ActionModel;
import com.otess.model.SettingModel;
import com.otess.model.UserModel;

public class AdminInterceptor implements Interceptor {
	
	public void intercept(Invocation inv) {
		Controller controller=inv.getController();		
		UserModel user=controller.getSessionAttr("user");
		if(!StrKit.notNull(user)){
			controller.redirect("/public/login");
			return;
		}else{
			/*
			String permission=user.getStr("r_permission");
			List<ActionModel> actionModel=ActionModel.me.findAll();
			if(StrKit.isBlank(permission)){
				controller.setAttr("actionList", actionModel);
			}else{
				List<ActionModel> list=new ArrayList<ActionModel>();
				for (ActionModel value : actionModel) {
					if(StringUtils.isContain(permission, value.getStr("a_code"))){
						list.add(value);
					}
		        } 
				controller.setAttr("actionList", list);
			}*/
		}
		controller.setAttr("user", user);
		controller.setAttr("searchCon", StringUtils.makePara(controller.getParaMap()));
		controller.setAttr("k_version", SettingModel.me.findById("k_version").getStr("s_value"));
		inv.invoke();
	}
}
