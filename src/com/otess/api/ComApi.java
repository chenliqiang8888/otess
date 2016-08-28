package com.otess.api;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.otess.common.bean.DataResponse;
import com.otess.common.utils.DateUtils;
import com.otess.common.utils.HttpUtils;
import com.otess.interceptor.ComInterceptor;
import com.otess.model.ClientModel;
import com.otess.model.MediaFileModel;
import com.otess.model.SettingModel;

@Before(ComInterceptor.class)
public class ComApi extends BaseApi{
	private static final Logger log = Logger.getLogger(ComApi.class);
	@Before(POST.class)
	public void reg(){
		String deviceid=getPara("deviceid");
		String name=getPara("name");
		String ip=getPara("ip");
		ClientModel clientModel=ClientModel.me.findByDeviceId(deviceid);
		if(StrKit.notNull(clientModel)){
			renderExists("已经注册过了");
		}else{
			ClientModel clientModelName=ClientModel.me.findByName(name);
			if(StrKit.notNull(clientModelName)){
				renderExists("名称已经被占用了");
			}else{
				ClientModel me=new ClientModel().set("cl_name", name)
						.set("cl_ip", ip)
						.set("cl_deviceid", deviceid)
						.set("cl_mac", getPara("mac"))
						.set("cl_version", getPara("version"))
						.set("cl_build_number", getPara("buildnumber"));
					me.save();
					if(me.getInt("cl_id")>0){
						renderSuccess("操作成功!");
					}else{
						render404("操作失败");
					}
			}			
		}
	}
	/**
	 * 升级配置
	 */
	public void upgrade(){
		String deviceid=getPara(0);
		SettingModel setting=SettingModel.me.findById("k_upgrade");
		if(StrKit.notNull(setting)){
			System.out.println(setting.getStr("s_value"));
			renderJson(setting.getStr("s_value"));
		}else{
			render404("操作失败");
		}
	}
	public void time(){
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("time",DateUtils.getNowTimeFormat());
		map.put("utc",DateUtils.getNowDateTime());
		renderJson(new DataResponse().setData(map).setCode(200).setMessage("操作成功！"));
	}
}
