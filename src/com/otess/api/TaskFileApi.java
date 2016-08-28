package com.otess.api;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.otess.common.bean.*;
import com.otess.common.utils.FileUtils;
import com.otess.common.utils.HttpUtils;
import com.otess.common.utils.JDBCUtils;
import com.otess.common.utils.SqlUtils;
import com.otess.common.utils.StringUtils;
import com.otess.interceptor.ComInterceptor;
import com.otess.model.LogSendModel;
import com.otess.model.MediaFileModel;
import com.otess.model.TaskFileModel;
import com.otess.model.TaskItemModel;
import com.otess.model.TaskModel;
@Before(ComInterceptor.class)
public class TaskFileApi extends BaseApi{
	private static final Logger log = Logger.getLogger(TaskFileApi.class);
	public void get(){
		Integer tf_id=getParaToInt("tf_id");//资源id
		TaskFileModel tf=TaskFileModel.me.findById(tf_id);
		setAttr("tf",JsonKit.toJson(tf));
		setAttr("tfModel",tf);
		setAttr("from",getPara(0));
		if(tf.getInt("tf_type").equals(-9)){//天气预报
			setAttr("weather",HttpUtils.Weather(tf.getStr("tf_affdate_weather")));
			render("/views/api/weather.html");
		}else if(tf.getInt("tf_type").equals(-11)){//汇率
			setAttr("huilv",HttpUtils.Huilv());
			render("/views/api/huilv.html");
		}else if(tf.getInt("tf_type").equals(-12) || tf.getInt("tf_type").equals(1)){//字幕

			String m_file=MediaFileModel.me.findByNo(tf.getStr("tf_media_no")).getStr("m_file");
 			String filePath=PathKit.getWebRootPath()+"/public/upload/"+m_file;
			String txt=FileUtils.txt2String(filePath);

			System.out.println("txt:"+txt);
			setAttr("txt",txt);
 			render("/views/api/txt.html");
		}else if(tf.getInt("tf_type").equals(-8)){//时间
			render("/views/api/time.html");
		}else if(tf.getInt("tf_type").equals(-10)){//数据库
			render("/views/api/db.html");
		}
	}
	public void loadtabledata() throws Exception{
		Integer dbtype=getParaToInt("dbtype");
		String dbaddress=getPara("dbaddress");
		String dbport=getPara("dbport");
		String dbusername=getPara("dbusername");
		String dbname=getPara("dbname");
		String dbpassword=getPara("dbpassword");
		String dbsql=getPara("dbsql");
		
		SqlUtils db = null;
		try {
			db = new SqlUtils(dbtype,dbaddress,dbport,dbusername,dbpassword,dbname);
			renderJson(new ListResponse(Code.SUCCESS,"ok")
					.setList(db.ResultSetToList(db.executeQuery(dbsql))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			renderArgumentError(e.getMessage());
			e.printStackTrace();
		}finally{
			//db.close();
		}
	}
}
