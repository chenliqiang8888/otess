package com.otess.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.otess.common.bean.Code;
import com.otess.common.bean.DataResponse;
import com.otess.common.utils.DateUtils;
import com.otess.common.utils.HttpUtils;
import com.otess.interceptor.AdminInterceptor;
import com.otess.model.ClientModel;
import com.otess.model.LogSendModel;
import com.otess.model.MediaFileModel;
import com.otess.model.SysLogModel;
import com.otess.model.TaskModel;
import com.otess.model.UserModel;

@Before(AdminInterceptor.class)
public class SysLogController extends BaseController {
	public void list() {
		String username = getPara("username");
		Integer sl_type = getParaToInt("sl_type") == null ? 0 : getParaToInt("sl_type");
		String sl_ip = getPara("sl_ip");
		String m_s_addtime = getPara("m_s_addtime");
		String m_e_addtime = getPara("m_e_addtime");
		System.out.println(sl_type);
		setAttr("sysLogPage", SysLogModel.me.page(getParaToInt("p", 1), this.getPageSize(), username, sl_type, sl_ip,
				m_s_addtime, m_e_addtime));
		setAttr("username",username);
		setAttr("sl_type",sl_type);
		setAttr("sl_ip",sl_ip);
		setAttr("m_s_addtime",m_s_addtime);
		setAttr("m_e_addtime",m_e_addtime);
		render("/views/syslog/list.html");
	}

	public void empty() {
		SysLogModel.me.empty();
		renderSuccess("操作成功!");
	}

	public void export() {
		
		/*String username=getPara("username"); 
		Integer sl_type=getParaToInt("sl_type")==null?0:getParaToInt("sl_type");
		String sl_ip=getPara("sl_ip"); 
		String m_s_addtime=getPara("m_s_addtime"); 
		String m_e_addtime=getPara("m_e_addtime");
		SysLogModel.me.findByNameAndIp(username, sl_type, sl_ip, m_s_addtime,m_e_addtime);
		String filename=PathKit.getWebRootPath() + "\\public\\upload\\csv\\"+DateUtils.getNowTime()+".csv";
		try {
			FileWriter fw = new FileWriter(filename);
			String header = "日期,类型,内容,IP,用户\r\n";
			fw.write(header);
			for(SysLogModel x:SysLogModel.me.findByNameAndIp(username, sl_type, sl_ip,m_s_addtime, m_e_addtime)){
				StringBuffer str = new StringBuffer();
				String date=x.getStr("sl_time");
				String lt_name=x.getStr("lt_name");
				String sl_log=x.getStr("sl_log");
				String ip=x.getStr("sl_ip");
				String sl_username=x.getStr("username");
				str.append(date).append(",").append(lt_name).append(",")
					.append(sl_log).append(",")
					.append(ip).append(",")
					.append(sl_username).append("\r\n");
				fw.write(str.toString());
				fw.flush();
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		renderFile(new File(filename));*/
		
		String username=getPara("username"); 
		Integer sl_type=getParaToInt("sl_type")==null?0:getParaToInt("sl_type");
		String sl_ip=getPara("sl_ip"); 
		String m_s_addtime=getPara("m_s_addtime"); 
		String m_e_addtime=getPara("m_e_addtime");
		SysLogModel.me.findByNameAndIp(username, sl_type, sl_ip, m_s_addtime,m_e_addtime);

		BufferedWriter fw = null;
		String filename = DateUtils.getNowTime() + ".csv";
		try {
			File file = new File(PathKit.getWebRootPath() + "/public/upload/csv/" + filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			String header = "日期,类型,内容,IP,用户\r\n";
			StringBuffer str = new StringBuffer();
			for(SysLogModel x:SysLogModel.me.findByNameAndIp(username, sl_type, sl_ip,m_s_addtime, m_e_addtime)){
				String date=x.getDate("sl_time").toString();
				String lt_name=x.getStr("lt_name");
				String sl_log=x.getStr("sl_log");
				String ip=x.getStr("sl_ip");
				String sl_username=x.getStr("username");
				str.append(date).append(",").append(lt_name).append(",")
					.append(sl_log).append(",")
					.append(ip).append(",")
					.append(sl_username).append("\r\n");
			}
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "GBK")); // 指定编码格式，以免读取时中文字符异常
			fw.append(header);
			fw.append(str);
			fw.flush(); // 全部写入缓存中的内容
			renderFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
