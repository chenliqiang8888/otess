package com.otess.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.otess.common.bean.BaseResponse;
import com.otess.common.bean.Code;
import com.otess.common.utils.StringUtils;
import com.otess.model.SettingModel;
import com.otess.model.SysLogModel;
import com.otess.model.UserModel;

/**
 * IndexController
 */
public class BaseController extends Controller {
	/**
	 * 响应请求参数有误*
	 * 
	 * @param message
	 *            错误信息
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
	 * 
	 * @param message
	 *            响应信息
	 */
	public void renderSuccess(String message) {
		renderJson(new BaseResponse().setMessage(message));

	}

	/**
	 * 响应操作成功*
	 * 
	 * @param message
	 *            响应信息
	 */
	public void renderExists(String message) {
		renderJson(new BaseResponse(Code.ACCOUNT_EXISTS).setMessage(message));

	}

	public String getStyle() {
		// 前景色|背景色|字体大小|字体|字型|滚动类型|速度|水平对齐|竖直对齐
		return "";
	}

	public int getPageSize() {
		return 12;
	}
	/**
	 * 判断是否系统用户
	 * @return
	 */
	public boolean isSystemRole() {
		UserModel user = this.getSessionAttr("user");
		return user.getInt("r_type") == 0 ? false : true;
	}
    /**
     * 获取当前用户名称
     * @return
     */
	public String getCurrentUserName() {
		UserModel user = this.getSessionAttr("user");
		return user.getStr("username");
	}
	/**
	 * 获取当前用户ID
	 * @return
	 */
	public Integer getCurrentUserId() {
		UserModel user = this.getSessionAttr("user");
		return user.getInt("uid");
	}
	/**
	 * 获取当前用户角色
	 * @return
	 */
	public Integer getCurrentRoleType(){
		UserModel user = this.getSessionAttr("user");
		return user.getInt("r_type");
	}
	/**
	 * 根据角色获取 用户集合
	 * @return
	 */
	public String getCurrentUserIdByRoleType() {
		UserModel user = this.getSessionAttr("user");
		if (user.getInt("r_type") == 0) {// 普通用户
			return user.getInt("uid").toString();
		} else if (user.getInt("r_type") == 1) {// 审核员
			List<UserModel> list = UserModel.me.findByPid(user.getInt("uid"));
			Object[] userid = new Object[list.size()+1];
			Integer i = 0;
			for (UserModel x : list) {
				userid[i++] = x.getInt("uid");
				System.out.println(x.getInt("uid"));
			}
			userid[i++]=user.getInt("uid");
			System.out.println(JsonKit.toJson(userid));
			return StringUtils.join(userid);
		} else {// 管理员
			return "";
		}
	}

	public void setSysLog(String sl_log, Integer sl_type) {
		SettingModel setting=SettingModel.me.findById("k_log");
		if(StrKit.notNull(setting)){
			System.out.print(setting.getStr("s_value"));
			if(setting.getStr("s_value").equals("true")){
				SysLogModel me = new SysLogModel().set("sl_type", sl_type).set("sl_log", sl_log).set("sl_mac", "")
						.set("sl_ip", this.getIpAddr()).set("sl_userid", this.getCurrentUserId());
				me.save();
			}
		}
	}

	private String getIpAddr() {
		String ipAddress = null;
		// ipAddress = this.getRequest().getRemoteAddr();
		ipAddress = this.getRequest().getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = this.getRequest().getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = this.getRequest().getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = this.getRequest().getRemoteAddr();
			System.out.println(ipAddress);
			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}

		}
		/*
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}*/
		return ipAddress;
	}
	private String getMyIPLocal() throws IOException {  
        InetAddress ia = InetAddress.getLocalHost();  
        return ia.getHostAddress();  
    }  
}
