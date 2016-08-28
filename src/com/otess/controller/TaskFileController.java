package com.otess.controller;

import com.otess.common.bean.BaseResponse;
import com.otess.common.bean.Code;
import com.otess.common.bean.DBType;
import com.otess.common.bean.DataResponse;
import com.otess.common.bean.ListResponse;
import com.otess.common.utils.FileUtils;
import com.otess.common.utils.HttpUtils;
import com.otess.common.utils.JDBCUtils;
import com.otess.common.utils.SqlUtils;
import com.otess.common.utils.StringUtils;
import com.otess.interceptor.AdminInterceptor;
import com.otess.model.FileTypeModel;
import com.otess.model.MediaFileModel;
import com.otess.model.TaskFileModel;
import com.otess.model.TaskItemModel;
import com.otess.model.TaskModel;
import com.otess.service.TaskFileService;

import java.io.File;
import java.math.BigDecimal;

import com.jfinal.aop.Before;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.kit.StringKit;

@Before(AdminInterceptor.class)
public class TaskFileController extends BaseController {
	public void detail(){
		renderJson(new DataResponse().setData(TaskFileModel.me.findById(getParaToInt("tf_id"))).setMessage("ok"));
	}
	public void addfile(){
		Integer tf_type=getParaToInt("tf_type");
		if(tf_type.equals(-9)){
			render("/views/taskitem/components/weather.html");
		}else if(tf_type.equals(-8)){
			render("/views/taskitem/components/time.html");			
		}else if(tf_type.equals(-11)){
			render("/views/taskitem/components/huilv.html");			
		}else if(tf_type.equals(-12)){
			render("/views/taskitem/components/txt.html");			
		}else if(tf_type.equals(-5)){
			render("/views/taskitem/components/link.html");			
		}else if(tf_type.equals(-1)){
			setAttr("fileTypePage", FileTypeModel.me.findAll());
			render("/views/taskitem/components/file.html");			
		}else if(tf_type.equals(-4)){
			render("/views/taskitem/components/live.html");			
		}else if(tf_type.equals(-10)){
			render("/views/taskitem/components/db.html");	
		}
	}
	public void loadfile(){
		Integer tf_task_item_id=getParaToInt("tf_task_item_id"),tf_position=getParaToInt("tf_position");		

		renderJson(new ListResponse(Code.SUCCESS, "ok")
				.setList(TaskFileModel.me.findByTaskItemId(tf_task_item_id,tf_position)));
	}
	/**
	 * 修改
	 */
	public void editfile(){
		TaskFileModel taskfile=TaskFileModel.me.findById(getParaToInt("tf_id"));
		setAttr("taskfile",taskfile);
		setAttr("taskfileJson",JsonKit.toJson(taskfile));
		Integer tf_type=taskfile.getInt("tf_type");
		System.out.println(tf_type);
		if(tf_type.equals(-9)){
			render("/views/taskitem/components/edit/weather_edit.html");
		}else if(tf_type.equals(-8)){
			render("/views/taskitem/components/edit/time_edit.html");			
		}else if(tf_type.equals(-10)){			
			render("/views/taskitem/components/edit/db_edit.html");			
		}else if(tf_type.equals(-11)){
			render("/views/taskitem/components/edit/huilv_edit.html");			
		}else if(tf_type.equals(-12) || tf_type.equals(1)){
			String m_file=MediaFileModel.me.findByNo(taskfile.getStr("tf_media_no")).getStr("m_file");
			String filePath=PathKit.getWebRootPath()+"/public/upload/"+m_file;
			setAttr("txt",FileUtils.txt2String(filePath));
			render("/views/taskitem/components/edit/txt_edit.html");			
		}else if(tf_type.equals(-5)){
			render("/views/taskitem/components/edit/link_edit.html");			
		}else if(tf_type.equals(-4)){
			render("/views/taskitem/components/edit/live_edit.html");			
		}else if(tf_type.equals(-1) || tf_type.equals(2) || 
				tf_type.equals(3) || tf_type.equals(4) || tf_type.equals(5) || tf_type.equals(6)){
			render("/views/taskitem/components/edit/media_edit.html");			
		}
	}
	/**
	 * 保存资源
	 */
	public void savefile(){
		// 前景色|背景色|字体大小|字体|字型|滚动类型|速度|水平对齐|竖直对齐|行高|缩放比例 //缩放比例大于1需要缩放
		BigDecimal scale=new BigDecimal(getPara("tf_scale"));//缩放比例
		BigDecimal fontSize=new BigDecimal(getPara("fontsize"));
		BigDecimal lineHeight=new BigDecimal(getPara("lineheight"));
		BigDecimal   one   =   new   BigDecimal("1");
		String fontSizeScale=scale.multiply(fontSize).divide(one, 0, BigDecimal.ROUND_HALF_UP).toString();
		String lineHeightScale=scale.multiply(lineHeight).divide(one, 0, BigDecimal.ROUND_HALF_UP).toString();
		Object[] style = new Object[] { getPara("color"), getPara("backgroundcolor"), fontSizeScale, getPara("family"),
				getPara("fonttype"), getPara("scrolltype"), getPara("speed"), getPara("align"), getPara("valign"), lineHeightScale,
				scale };
		Object[] style_web = new Object[] { getPara("color"), getPara("backgroundcolor"), fontSize, getPara("family"),
				getPara("fonttype"), getPara("scrolltype"), getPara("speed"), getPara("align"), getPara("valign"), lineHeight,
				scale };
		Integer tf_type=getParaToInt("tf_type");
		String bgsound="";
		if(getPara("tf_bgsound")!=null){
			bgsound=getPara("tf_bgsound");
		};
		//修改的时候 html页面需要把所有属性赋值，否则编辑文件 tf_media_no会为空了
		TaskFileModel taskFile = new TaskFileModel()
				.set("tf_task_item_id", getParaToInt("tf_task_item_id"))
				.set("tf_position", getParaToInt("position"))
				.set("tf_index", getParaToInt("index"))
				.set("tf_type", tf_type)
				.set("tf_media_no", getPara("tf_media_no"))
				.set("tf_duration", getParaToInt("tf_duration"))
				.set("tf_style", StringUtils.join(style, "|"))
				.set("tf_style_web", StringUtils.join(style_web, "|"))
				.set("tf_bgsound", bgsound).set("tf_http_url", "");
				if(tf_type.equals(-9)){
					taskFile.set("tf_affdate_weather", getPara("datatype") + "|" + getPara("linetype") + "|"
							+ getPara("province") + "," + getPara("city") + "," + getPara("area"));
				}
				if(tf_type.equals(-8)){
					taskFile.set("tf_affdate_weather",
							getPara("timetype") + "|" + getPara("linetype") + "|" + getPara("area"));
				}
				if(tf_type.equals(-5) || tf_type.equals(-4)){
					taskFile.set("tf_http_url", getPara("url"));
				}
				if(tf_type.equals(6)){
					taskFile.set("tf_affdate_weather", getPara("filepage"));
				}
				if(tf_type.equals(-10)){
					//2|121.43.114.111|3306|root|jinweida|otess|select a_id,a_code,a_path,a_title,a_pcode,a_order,a_status,a_icon from mp_action|32,55,144,33|1,1,1,1|10|337|270|10|1|EFEFEF|秒|表
					Integer dbtype=getParaToInt("dbtype");
					String dbaddress=getPara("dbaddress");
					String dbport=getPara("dbport");
					String dbusername=getPara("dbusername");
					String dbname=getPara("dbname");
					String dbpassword=getPara("dbpassword");
					String dbsql=getPara("dbsql");
					String dbpagecount=getPara("dbpagecount");//每页行数
					String dbcolumnswidth=getPara("dbcolumnswidth");//列宽
					String dbcolumnstype=getPara("dbcolumnstype");//列类型 1=文字 2=图片
					String dbtablewidth=getPara("db_width");//表宽
					String dbtableheight=getPara("db_height");//表高
					String dbpage=getPara("dbpage");//翻页时间
					String borderwidth=getPara("borderwidth");//线条宽度
					String bordercolor=getPara("bordercolor");//线条颜色
					String dbrefresh=getPara("dbrefresh");//刷新周期
					String path=getPara("path");//图片路径
					String dbtables=getPara("dbtables");//表名
					System.out.println("dbcolumnswidth:"+dbcolumnswidth);
					//2|121.43.114.111|3306|root|jinweida|otess|
					//select a_id,a_code,a_path,a_title,a_order,a_pcode,a_status from mp_action|
					//24,24,24,24,25,25,25|1,1,1,1,1,1,1|5|301|172|10|1|FFE852|3600|http://www.baidu.com|mp_action
					taskFile.set("tf_affdate_weather", dbtype + "|" +dbaddress + "|"
							+ dbport + "|" + dbusername + "|" + dbpassword+"|"+dbname+"|"
							+dbsql+"|"+dbcolumnswidth+"|"+dbcolumnstype+"|"+dbpagecount+"|"+dbtablewidth+"|"
							+dbtableheight+"|"+dbpage+"|"+borderwidth+"|"+bordercolor+"|"+dbrefresh+"|"+path+"|"+dbtables);

					BigDecimal dbtableWidthAffix=new BigDecimal(dbtablewidth);//缩放比例
					BigDecimal dbtableHeightAffix=new BigDecimal(dbtableheight);
					String dbtablewidthScale=scale.multiply(dbtableWidthAffix).divide(one, 0, BigDecimal.ROUND_HALF_UP).toString();
					String dbtableheightScale=scale.multiply(dbtableHeightAffix).divide(one, 0, BigDecimal.ROUND_HALF_UP).toString();

					String[] tdWidth = dbcolumnswidth.split(",");
					String dbcolumnswidthScale="";
					for(String x:tdWidth){
						BigDecimal width=new BigDecimal(x);//缩放比例
						if(dbcolumnswidthScale!="")dbcolumnswidthScale+=",";
						dbcolumnswidthScale+=scale.multiply(width).divide(one, 0, BigDecimal.ROUND_HALF_UP).toString();
					}
					System.out.println("dbcolumnswidthScale:"+dbcolumnswidthScale);
					System.out.println("dbtablewidthScale:"+dbtablewidthScale);
					System.out.println("dbtableheightScale:"+dbtableheightScale);
					taskFile.set("tf_affix_url", dbtype + "|" +dbaddress + "|"
							+ dbport + "|" + dbusername + "|" + dbpassword+"|"+dbname+"|"
							+dbsql+"|"+dbcolumnswidthScale+"|"+dbcolumnstype+"|"+dbpagecount+"|"+dbtablewidthScale+"|"
							+dbtableheightScale+"|"+dbpage+"|"+borderwidth+"|"+bordercolor+"|"+dbrefresh+"|"+path+"|"+dbtables);
					
				}
				if(tf_type.equals(-12) || tf_type.equals(1)){
					String filename=FileUtils.WriteTxt(getPara("content"));
					String m_no=HashKit.md5(filename);
					File file=FileUtils.readFile(PathKit.getWebRootPath()+"/public/upload/"+filename);
					new MediaFileModel()
						.set("m_name",file.getName())
						.set("m_file", file.getName())
						.set("m_size_high",file.length())
						.set("m_no",m_no)
						.set("m_type", 1)
						.save();
					taskFile.set("tf_media_no", m_no);
				}
				/**
				 * 判断是不是修改
				 */
				if(StrKit.notNull(getPara("tf_id"))){
					taskFile.set("tf_id",getParaToInt("tf_id"));
				}

		this.renderTaskFile(taskFile);
	}
	public void delete() {
		if (TaskFileModel.me.deleteById(getPara("tf_id"))) {
			renderJson(new DataResponse(Code.SUCCESS, "ok"));
		} else {
			renderArgumentError("参数错误");
		}
	}
	
	public void addmedia() {
		String[] tf_media_no = getParaValues("chk");
		int isAdd = getParaToInt("tf_isadd");
		if (StrKit.notNull(tf_media_no)) {
			TaskFileModel taskFile = new TaskFileModel().set("tf_position", getParaToInt("position"))
					.set("tf_task_item_id", getParaToInt("tf_task_item_id"))
					.set("tf_duration", tf_media_no.length * getParaToInt("tf_duration"));
			int result = 0;
			if (TaskFileService.me.isUpdateFile(taskFile, isAdd) != 0) {
				for (String no : tf_media_no) {
					String[] media = no.split("_");
					TaskFileModel model = new TaskFileModel().set("tf_type", media[1]).set("tf_media_no", media[0])
							.set("tf_style", "000000||12|微软雅黑|bold|left|0|center|top|20|0")
							.set("tf_style_web", "000000||12|微软雅黑|bold|left|0|center|top|20|0")
							.set("tf_position", getParaToInt("position"))
							.set("tf_task_item_id", getParaToInt("tf_task_item_id"))
							.set("tf_duration", getParaToInt("tf_duration"));

					model.save();
				}
				renderJson(new ListResponse(Code.SUCCESS, "ok")
						.setList(TaskFileModel.me.findByTaskItemId(getParaToInt("tf_task_item_id"))));
			} else {
				renderJson(new DataResponse(Code.FAIL).setData(result));
			}
		} else {
			renderArgumentError("参数错误");
		}
	}
	public void renderTaskFile(TaskFileModel taskFile){
		int isAdd = getParaToInt("tf_isadd");
		int result = 0;
		if (TaskFileService.me.isUpdateFile(taskFile, isAdd) != 0) {
			if(StrKit.notNull(taskFile.getInt("tf_id"))){
				taskFile.update();
			}else{
				taskFile.save();
			}
			Integer tf_id = taskFile.getInt("tf_id");
			if (tf_id > 0) {
				renderJson(new ListResponse(Code.SUCCESS, "ok")
						.setList(TaskFileModel.me.findByTaskItemId(taskFile.getInt("tf_task_item_id"))));
			} else {
				renderArgumentError("参数错误");
			}
		}else{
			renderJson(new DataResponse(Code.FAIL).setData(result));
		}
	}
	/**
	 * 登陆数据库，并返回所有表名
	 */
	public void loadtables(){
		Integer dbtype=getParaToInt("dbtype");
		String dbaddress=getPara("dbaddress");
		String dbport=getPara("dbport");
		String dbusername=getPara("dbusername");
		String dbname=getPara("dbname");
		String dbpassword=getPara("dbpassword");
		System.out.println("dbtype:"+dbtype);
		SqlUtils db = null;
		try {
			db = new SqlUtils(dbtype,dbaddress,dbport,dbusername,dbpassword,dbname);
			if(dbtype==DBType.MSSQL){
				renderJson(new ListResponse(Code.SUCCESS,"ok")
						.setList(db.ResultSetToList(
								db.executeQuery("select [id], [name] as TABLE_NAME "
										+ "from [sysobjects] where [type] = 'u' "
										+ "order by [name]"))));
			}else if(dbtype==DBType.MYSQL){
				renderJson(new ListResponse(Code.SUCCESS,"ok")
						.setList(db.ResultSetToList(db.executeQueryTables())));
			}else if(dbtype==DBType.ORACLE){
				renderJson(new ListResponse(Code.SUCCESS,"ok")
						.setList(db.ResultSetToList(
								db.executeQuery("select a.TABLE_NAME,b.COMMENTS "
										+ "from user_tables a,user_tab_comments b "
										+ "WHERE a.TABLE_NAME=b.TABLE_NAME order by TABLE_NAME"))));
			}else if(dbtype==DBType.SYBASE){
				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
            e.printStackTrace();  
			renderArgumentError(e.getMessage());
		}finally{
			//db.close();
		}
	}
	/**
	 * 获取表的所有字段
	 * @throws Exception 
	 */
	public void loadcolumns(){
		Integer dbtype=getParaToInt("dbtype");
		String dbaddress=getPara("dbaddress");
		String dbport=getPara("dbport");
		String dbusername=getPara("dbusername");
		String dbname=getPara("dbname");
		String dbpassword=getPara("dbpassword");
		String dbtablename=getPara("dbtablename");
		SqlUtils db = null;
		try {
			db = new SqlUtils(dbtype,dbaddress,dbport,dbusername,dbpassword,dbname);
			renderJson(new ListResponse(Code.SUCCESS,"ok")
					.setList(db.ResultSetToList(db.executeQueryColumns(dbtablename))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//db.close();
		}
	}
	public void loadtabledata(){
		Integer dbtype=getParaToInt("dbtype");
		String dbaddress=getPara("dbaddress");
		String dbport=getPara("dbport");
		String dbusername=getPara("dbusername");
		String dbname=getPara("dbname");
		String dbpassword=getPara("dbpassword");
		String dbtablename=getPara("dbtablename");
		String dbsql=getPara("dbsql");
		System.out.println(dbsql);
		SqlUtils db = null;
		try {
			/*
			 * select COLUMN_NAME,DATA_TYPE,DATA_LENGTH 
from dba_tab_columns 
where table_name =upper('Account_user') 
order by COLUMN_NAME

select a.TABLE_NAME,b.COMMENTS 
from user_tables a,user_tab_comments b 
WHERE a.TABLE_NAME=b.TABLE_NAME 
order by TABLE_NAME
			 * */
			db = new SqlUtils(dbtype,dbaddress,dbport,dbusername,dbpassword,dbname);
			renderJson(new ListResponse(Code.SUCCESS,"ok")
					.setList(db.ResultSetToList(db.executeQuery(dbsql))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//db.close();
		}
	}
	/**
	 * 获取天气预报
	 */
	public void getWeather(){
		String area=getPara("tf_weather");
        //1|2|北京,市辖区,海淀区
        renderSuccess(HttpUtils.Weather(area));
	}
	/**
	 * 获取汇率
	 */
	public void getHuilv(){
		renderSuccess(HttpUtils.Huilv());
	}
	public void getTxt(){
		String m_file=MediaFileModel.me.findByNo(getPara("tf_media_no")).getStr("m_file");
		String filePath=PathKit.getWebRootPath()+"/public/upload/"+m_file;
		renderSuccess(FileUtils.txt2String(filePath));
	}
	public void deleteByPosition(){
		Integer ti_id=getParaToInt("ti_id");
		Integer tf_position=getParaToInt("tf_position");
		//删除任务资源布局资源
		if(TaskFileModel.me.deleteByTAskItemIdAndPosition(ti_id,tf_position)>0){
			renderSuccess("操作成功！");
		}else{
			renderArgumentError("参数错误");
		}
	}
	public void sortfile(){
		String[] tf_id_array = getPara("tf_id").split(",");
		int i=0;
		for (String tf_id : tf_id_array) {
			TaskFileModel.me.updateIndex(Integer.parseInt(tf_id), i++);
		}
		renderSuccess("操作成功！");
	}
}