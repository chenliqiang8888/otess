package com.otess.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.otess.common.bean.Code;
import com.otess.common.bean.DataResponse;
import com.otess.common.bean.ListResponse;
import com.otess.interceptor.AdminInterceptor;
import com.otess.model.ClientModel;
import com.otess.model.LogSendModel;
import com.otess.model.MediaFileModel;
import com.otess.model.TaskModel;
import com.otess.model.UserModel;

@Before(AdminInterceptor.class)
public class LogSendController extends BaseController {
	public void list() {
		//这里先删除100%的任务
		//LogSendModel.me.deleteByPercent();
		setAttr("logSendPage", LogSendModel.me.findAll());
		render("/views/logsend/list.html");
	}
	
	public void getfindall() {
		renderJson(new ListResponse().setList(LogSendModel.me.findAll()).setMessage("ok"));
	}
	
	public void add() {
		setAttr("ls_task_id", getParaToInt(0));
		render("/views/logsend/add.html");
	}

	public void dosave() {
		LogSendModel logsend = getModel(LogSendModel.class, "logsend");
		logsend.save();
		redirect("/logsend/list");
	}

	public void sendToClient() {
		int TaskId = getParaToInt("taskid");
		String ClientIds = getPara("clientids");
		System.out.println(ClientIds);
		Integer SendType = getParaToInt("sendtype");// 发送类型
		Integer RadType = getParaToInt("sendmethod");// 发送方式
		String SendTime = getPara("sendtime");// 发送时间
		if (TaskId != 0 && StrKit.notBlank(ClientIds)) {
			UserModel user=this.getSessionAttr("user");
			this.sendTask(TaskId, ClientIds, SendType, RadType, SendTime, "", user.getInt("uid"));
			this.setSysLog("发送任务>"+TaskId, 3);
			//renderJson(new DataResponse(Code.SUCCESS, "ok"));
			renderSuccess("ok");
		} else {
			renderArgumentError("参数错误");
		}
	}

	public void sendTask(int TaskId, String ClientIds, Integer SendType, Integer RadType, String SendTime,
			String MediaNos, Integer UserId) {

		if(SendType!=2){
			SendTime="0000-00-00 00:00:00";
		}
		String[] ids = ClientIds.split(",");
		for(String x:ids){
			if (Integer.parseInt(x) > 0){
                //LogHelper.AddSendLog(int.Parse(arrCid[i]), nTid, nType, strDate, nSendAttribute, strMediaIds,userid);
				LogSendModel logSendModel=new LogSendModel()
						.set("ls_task_id", TaskId)
						.set("ls_client_id", x)
						.set("ls_type", SendType)
						.set("ls_time_send", SendTime)
						.set("ls_attribute",RadType==null?0:RadType)
						.set("ls_send_file_ids", MediaNos)
						.set("userid",UserId);
				logSendModel.save();
            }
		}		
	}	

	public void asyndelete(){
		if(LogSendModel.me.deleteById(getParaToInt("id"))){
			renderSuccess("操作成功!");			
		}else{
			renderArgumentError("参数错误");
		}
	}

	public void delete() {
		String[] ids = getPara("ids").split(",");
		for (String id : ids) {
			LogSendModel.me.deleteById(id);
		}
		redirect("/logsend/list");
	}
}
