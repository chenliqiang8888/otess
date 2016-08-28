package com.otess.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.JsonKit;
import com.otess.common.Result;
import com.otess.common.bean.BaseResponse;
import com.otess.common.bean.Code;

public class ComInterceptor implements Interceptor {
	
	public void intercept(Invocation inv) {
		Controller controller=inv.getController();
		if("/api/com/reg".equals(inv.getActionKey())){
			if(StrKit.isBlank(controller.getPara("deviceid"))){
				controller.renderJson(new BaseResponse(Code.FAIL, "deviceid is not empty"));
				return;
			}
			if(StrKit.isBlank(controller.getPara("name"))){
				controller.renderJson(new BaseResponse(Code.FAIL, "name is not empty"));
				return;
			}
			if(StrKit.isBlank(controller.getPara("ip"))){
				controller.renderJson(new BaseResponse(Code.FAIL, "ip is not empty"));
				return;
			}
		}else{
			if(StrKit.isBlank(controller.getPara(0))){
				controller.renderJson(new BaseResponse(Code.FAIL, "deviceid is not empty"));
				return;
			}
		}
		inv.invoke();
	}
}
