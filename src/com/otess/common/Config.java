package com.otess.common;


import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.Const;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.otess.api.*;
import com.otess.controller.*;
import com.otess.model.*;

public class Config extends JFinalConfig{
	
	public void configConstant(Constants me) {
		PropKit.use("jdbc.txt");
		me.setEncoding("UTF-8");		
		me.setDevMode(PropKit.getBoolean("devMode", false));
		//me.setUploadedFileSaveDirectory("/public/");
		//me.setMaxPostSize(2000*Const.DEFAULT_MAX_POST_SIZE);
	}
	/**
	 * 注册路由
	 */
	public void configRoute(Routes me) {
		me.add("/public",PublicController.class,"/login");
		me.add("/api/task",TaskApi.class,"/index");
		me.add("/api/client",ClientApi.class,"/index");
		me.add("/api/com",ComApi.class);		
		me.add("/api/taskfile",TaskFileApi.class);	

		me.add("/main", IndexController.class, "/index");
		me.add("/task",TaskController.class,"/index");
		me.add("/taskitem",TaskItemController.class,"/index");
		me.add("/taskfile",TaskFileController.class,"/index");
		me.add("/client",ClientController.class);
		me.add("/mediafile",MediaFileController.class);
		me.add("/logsend",LogSendController.class);
		me.add("/filetype",FileTypeController.class);
		me.add("/screen",ScreenController.class);
		me.add("/user",UserController.class);
		me.add("/fold",FoldController.class);
		me.add("/syslog",SysLogController.class);
		me.add("/role",RoleController.class);
		me.add("/setting",SettingController.class);
	}
	public void configPlugin(Plugins me) {
		C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
		me.add(c3p0Plugin);
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.setShowSql(true);//这句话就是ShowSql
		arp.addMapping("mp_task", "t_id",TaskModel.class);
		arp.addMapping("mp_task_item","ti_id", TaskItemModel.class);
		arp.addMapping("mp_task_file","tf_id", TaskFileModel.class);
		arp.addMapping("mp_client","cl_id", ClientModel.class);
		arp.addMapping("mp_log_send","ls_id", LogSendModel.class);
		arp.addMapping("mp_user","uid", UserModel.class);
		arp.addMapping("mp_media_file","m_id", MediaFileModel.class);
		arp.addMapping("mp_filetype","f_id",FileTypeModel.class);
		arp.addMapping("mp_operator","o_id",OperatorModel.class);
		arp.addMapping("mp_resolution","mr_id",ResolutionModel.class);
		arp.addMapping("mp_screen","sc_id",ScreenModel.class);
		arp.addMapping("mp_fold","cf_id",FoldModel.class);
		arp.addMapping("mp_syslog","sl_id",SysLogModel.class);
		arp.addMapping("mp_role","r_id",RoleModel.class);
		arp.addMapping("mp_action","a_id",ActionModel.class);
		arp.addMapping("mp_client_status","deviceid",ClientStatusModel.class);
		arp.addMapping("mp_client_monitor_img","i_id",ClientImgModel.class);
		arp.addMapping("mp_setting","s_key",SettingModel.class);
		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
	}
	public void configInterceptor(Interceptors me) {
		me.add(new SessionInViewInterceptor());
	}
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("base"));
	}
	/**
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args){
		JFinal.start("WebRoot", 8090, "/", 5);
	}
}
