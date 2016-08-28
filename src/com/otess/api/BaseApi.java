package com.otess.api;

import com.jfinal.core.Controller;
import com.otess.common.bean.BaseResponse;
import com.otess.common.bean.Code;

public class BaseApi extends Controller{
	/**
     * 响应请求参数有误*
     * @param message 错误信息
     */
    public void renderArgumentError(String message) {
        renderJson(new BaseResponse(Code.FAIL, message));

    }
    /**
     * 响应接口不存在*
     */
    public void render404(String message) {
        renderJson(new BaseResponse(Code.NOT_FOUND).setMessage(message));        
    }
    /**
     * 响应操作成功*
     * @param message 响应信息
     */
    public void renderSuccess(String message) {
        renderJson(new BaseResponse().setMessage(message));
        
    }
    /**
     * 响应操作成功*
     * @param message 响应信息
     */
    public void renderExists(String message) {
        renderJson(new BaseResponse(Code.ACCOUNT_EXISTS).setMessage(message));
        
    }
}
