package com.otess.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.otess.common.bean.Code;
import com.otess.common.bean.DataResponse;
import com.otess.interceptor.AdminInterceptor;
import com.otess.model.ActionModel;
import com.otess.model.ClientImgModel;
import com.otess.model.RoleModel;
import com.otess.model.SettingModel;
import com.otess.model.UserModel;
import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

@Before(AdminInterceptor.class)
public class SettingController extends BaseController {
	public void list(){
		setAttr("setting", SettingModel.me.findAll());
		render("/views/setting/list.html");
	}
	
	public void update(){
		
		Map<String,String[]> paraMap=getParaMap();
		Set<String> nameSet = paraMap.keySet();
		for(String name:nameSet){
			if (paraMap.get(name) != null && !paraMap.get(name)[0].equals("")) {
        		SettingModel model=new SettingModel()
        				.set("s_key",name)
        				.set("s_value",paraMap.get(name)[0]);
        		if(StrKit.notNull(SettingModel.me.findById(name))){
            		model.update();
        		}else{
        			model.save();
        		}
        	}
		}
		renderSuccess("ok");
	}
	/**
	 * 升级页面
	 */
	public void upgrade(){
		SettingModel setting=SettingModel.me.findById("k_upgrade");
		
		if(StrKit.notNull(setting)){
			setAttr("upgrade",JsonKit.toJson(setting.getStr("s_value")));
		}else{
			setAttr("upgrade",null);
		}
		render("/views/setting/upgrade.html");
	}
	public void doupgrade(){
		String key="k_upgrade";
        HashMap<String, String> hashMap = new HashMap<String, String>();  
        hashMap.put("version", getPara("u.version"));   
        hashMap.put("desc", getPara("u.desc"));    
        hashMap.put("downloadurl", getPara("u.downloadurl"));    
        hashMap.put("force", getPara("u.force"));
		System.out.println(JsonKit.toJson(hashMap)); 
		SettingModel setting=new SettingModel()
				.set("s_value",JsonKit.toJson(hashMap));
		setting.set("s_key",key);
		if(StrKit.notNull(SettingModel.me.findById(key))){
			setting.update();
		}else{
			setting.save();
		}
		renderSuccess("ok");
	}
}