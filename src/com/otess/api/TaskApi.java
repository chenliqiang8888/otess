package com.otess.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.otess.common.bean.*;
import com.otess.common.utils.DateUtils;
import com.otess.interceptor.ComInterceptor;
import com.otess.model.LogSendModel;
import com.otess.model.MediaFileModel;
import com.otess.model.TaskFileModel;
import com.otess.model.TaskItemModel;
import com.otess.model.TaskModel;
@Before(ComInterceptor.class)
public class TaskApi extends BaseApi{
	private static final Logger log = Logger.getLogger(TaskApi.class);
	/**
	 * 
	 */
	public void index(){
		System.out.print(getRequest().getMethod());
		String deviceid=getPara(0);
		Integer id=getParaToInt("id");
		TaskModel taskModel=TaskModel.me.findById(id);
		if(taskModel==null){
			render404("resources are not fouond");
		}else{
			HashMap<String , Object> map = new HashMap<String , Object>();
			map.put("task",taskModel);
			List<TaskItemModel> taskItemModel=TaskItemModel.me.findByTaskId(id);
			map.put("task_item",taskItemModel);
			List<TaskFileModel> taskFileModel=new ArrayList<TaskFileModel>();
			Integer duration=0;
			for(TaskItemModel m:taskItemModel){
				duration+=m.getInt("ti_duration");
				taskFileModel.addAll(TaskFileModel.me.findByTaskItemId(m.getInt("ti_id")));
			}
			/*加入后立即播放的任务 替app计算出来 开始and结束时间*/
			if(taskModel.getInt("t_loop").equals(2)){
				taskModel.set("t_begin_time",DateUtils.getNowTimeFormat());
				taskModel.set("t_end_time",DateUtils.addFormatTime(duration));
			}
			map.put("task_file",taskFileModel);
			map.put("code",Code.SUCCESS);
			renderJson(map);
		}
	}
	public void monitor(){
		log.info("TaskApi...init...monitor");
		String deviceid=getPara(0);
		System.out.println(deviceid);
		//获取进度小于100的
		List<LogSendModel> list=LogSendModel.me.findByDeviceId(deviceid);
		List<LogSendModel> send=new ArrayList<LogSendModel>();
		System.out.println(JsonKit.toJson(list));
		for(LogSendModel log:list){
			try {
				if(log.getInt("ls_type")==1){
					send.add(log);
				}else{
					System.out.println(DateUtils.getNowDateTime());
					System.out.println(DateUtils.getTime(log.getDate("ls_time_send")));
					if(DateUtils.getNowDateTime()>DateUtils.getTime(log.getDate("ls_time_send"))){
						send.add(log);
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ListResponse response=new ListResponse(send);
		response.setCode(Code.SUCCESS);
		response.setMessage("操作成功！");
		renderJson(response);
	}
	public void medias(){
		String deviceid=getPara(0);
		String no=getPara("no");
		MediaFileModel mediaFile=MediaFileModel.me.findByNo(no);
		if(StrKit.notNull(mediaFile)){
			mediaFile.set("m_file", "/public/upload/"+mediaFile.get("m_file"));
			renderJson(new DataResponse(mediaFile).setCode(200).setMessage("操作成功！"));
		}else{
			render404("操作失败");
		}
	}
	public void progress(){
		Integer ls_total_percent=getParaToInt("ls_total_percent");//总进度 百分比
		Integer ls_id=getParaToInt("ls_id");//当前发送的id
		String ls_speed=getPara("ls_speed");//速度
		Integer ls_last_second=getParaToInt("ls_last_second");//剩余描述
		//查找任务是否还存在、
		LogSendModel logsend=LogSendModel.me.findById(ls_id);
		boolean result=false;
		if(StrKit.notNull(logsend)){
			if(logsend.getInt("ls_total_percent")<=100){//防止重复上报ls_total_percent
				LogSendModel logSendModel=new LogSendModel()
						.set("ls_total_percent", ls_total_percent)
						.set("ls_speed", ls_speed)
						.set("ls_last_second", ls_last_second)
						.set("ls_id", ls_id);

				result=logSendModel.update();
			}			
		}
		
		if(result){
			renderSuccess("操作成功");
		}else{
			render404(getPara("taskid"));
		}	
	}
}
