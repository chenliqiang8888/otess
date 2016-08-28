package com.otess.controller;

import com.otess.interceptor.AdminInterceptor;
import com.otess.model.ClientModel;
import com.otess.model.ResolutionModel;
import com.otess.model.TaskFileModel;
import com.otess.model.TaskItemModel;
import com.otess.model.TaskModel;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.otess.common.bean.Code;
import com.otess.common.bean.DataResponse;
import com.otess.common.bean.ListResponse;
import com.otess.common.utils.*;

@Before(AdminInterceptor.class)
public class TaskController extends BaseController {
	public void list() {
		String t_status=getPara("t_status");
		setAttr("t_status",t_status);
		setAttr("taskPage", TaskModel.me.page(getParaToInt("p", 1), this.getPageSize(), this.getCurrentUserIdByRoleType(),
				t_status));
		render("/views/task/list.html");
	}

	public void getTaskByUid() {
		renderJson(new ListResponse().setList(TaskModel.me.findByStatusAndUid(this.getCurrentUserIdByRoleType(), getPara("t_status"),"",this.getCurrentRoleType())));
	}
	//获取在线的任务，终端发送任务用的
	public void getTaskByOnline(){
		String t_name=getPara("t_name");
		//getCurrentRoleType
		List<TaskModel> a=TaskModel.me.findByStatusAndUid(this.getCurrentUserIdByRoleType(), "2",t_name,this.getCurrentRoleType());
		List<TaskModel> b=TaskModel.me.findByStatusAndUid(this.getCurrentUserIdByRoleType(), "3",t_name,this.getCurrentRoleType());
		a.addAll(b);
		renderJson(new ListResponse().setList(a));
	}

	public void add() {
		/**
		 * -8=时间 -9=天气预报 -10=数据库 -5=链接页面 -11=汇率 -12=字幕 -3||-4=直播 1=文本 2=网页 3=图片
		 * 4=音频 5=视频
		 */
		setAttr("t_begin_time", DateUtils.getTime(0));
		setAttr("t_end_time", DateUtils.getTime(60));			
		setAttr("t_name", DateUtils.getNowTime());
		setAttr("t_no", HashKit.md5(DateUtils.getNowTime()));
		setAttr("resList", ResolutionModel.me.findAll());
		render("/views/task/add.html");
	}

	public void edit() {
		setAttr("resList", ResolutionModel.me.findAll());
		TaskModel taskModel=TaskModel.me.findById(getParaToInt(0));
		setAttr("task", taskModel);
		setAttr("taskJson",JsonKit.toJson(taskModel));
		render("/views/task/edit.html");
	}

	@Before(POST.class)
	public void dosave() throws ParseException {
		TaskModel exist=TaskModel.me.findByName(getPara("task.t_name"));
		System.out.println(JsonKit.toJson(exist));
		Boolean flag=false;
		if(StrKit.notNull(exist)){
			if(!exist.getInt("t_id").equals(getParaToInt("t_id"))){
				flag=true;
			}
		}
		if(flag){
			renderArgumentError("任务名称已经占用了");
		}else{
			TaskModel task = getModel(TaskModel.class, "task");
			if (this.isSystemRole()) {// 判断当前用户角色类型
				task.set("o_status", 1);
			}
			task.set("t_userid", this.getCurrentUserId());
			task.save();
			Integer t_id = task.get("t_id");
			if (t_id > 0) {
				this.setSysLog("创建任务："+task.getStr("t_name"), 1002);
				//renderSuccess("/taskitem/list/" + task.getInt("t_id"));
				TaskModel taskModel=TaskModel.me.findByIdAndMrId(t_id);
				renderJson(new DataResponse(Code.SUCCESS).setData(taskModel));
			} else {
				renderArgumentError("参数错误");
			}
		}
	}

	public void doupdate() {
		TaskModel task = getModel(TaskModel.class, "task");
		
		task.update();
		Integer t_id = task.get("t_id");
		if (t_id > 0) {
			this.setSysLog("更新任务："+t_id, 1002);
			renderSuccess("/task/list/");
		} else {
			renderArgumentError("参数错误");
		}
	}
	/**
	 * 修改任务状态
	 */
	public void doupdatestatus(){
		TaskModel task=new TaskModel()
				.set("t_status",2)
				.set("t_id",getParaToInt("t_id"));
		
		if(task.update()){
			renderSuccess("/task/list/");
		}else{
			renderArgumentError("参数错误");
		}
	}

	public void delete() {
		String[] ids = getPara("ids").split(",");
		Integer count=0;
		for (String id : ids) {
			TaskModel task=TaskModel.me.findById(id);
			TaskModel.me.deleteById(id);//删除任务
			TaskModel.me.deleteTaskAndItemAndFile(id);//删除任务，任务项目，任务资源
			this.setSysLog("删除任务："+task.getStr("t_name"), 1002);
			count++;
		}
		renderSuccess("["+count+"]条 操作成功!");
	}

	// 添加分辨率
	public void createres() {
		Integer w = getParaToInt("mr_w");
		Integer h = getParaToInt("mr_h");
		ResolutionModel me = new ResolutionModel()
				.set("mr_w", (w))
				.set("mr_h", (h))
				.set("istype", 0);
		if(ResolutionModel.me.findOneByWH(w, h)==null){
			me.save();
			if (me.getInt("mr_id") > 0) {
				this.setSysLog("添加分辨率："+w+"*"+h, 1002);
				renderSuccess("操作成功!");
			} else {
				renderArgumentError("参数错误");
			}
		}else{
			renderArgumentError("已存在相同分辨率");
		}		
	}

	/**
	 * 发送任务
	 */
	public void send() {
		int t_id = getParaToInt("t_id");
		int c_id = getParaToInt("c_id");
	}
	/**
	 * 审核任务
	 */
	public void audit(){
		String[] t_ids = getPara("t_id").split(",");
		Integer o_status=getParaToInt("o_status");
		int count=0;
		for (String id : t_ids) {
			TaskModel task=new TaskModel()
					.set("o_status",o_status)
					.set("t_id",id)
					.set("o_userid", this.getCurrentUserId());
			if(o_status==0){
				task.set("o_mark", getPara("o_mark"));
			}
			
			if(task.update()){
				this.setSysLog("审核任务："+id, 1002);
				count++;
			}
		}
		if (count > 0) {
			renderSuccess("["+count+"]条 操作成功!");
		} else {
			renderArgumentError("参数错误");
		}
	}
	/**
	 * 预览
	 */
	public void preview(){
		Integer t_id=getParaToInt("t_id");//任务
		TaskModel taskModel=TaskModel.me.findByIdAndMrId(t_id);
		List<TaskItemModel> taskItemModel=TaskItemModel.me.findByTaskId(t_id);
		List<TaskFileModel> taskFileModel=TaskFileModel.me.findByTaskId(t_id);
		setAttr("task",JsonKit.toJson(taskModel));//任务
		setAttr("taskitem",JsonKit.toJson(taskItemModel));//任务项
		setAttr("taskfile",JsonKit.toJson(taskFileModel));//任务资源
		render("/views/task/preview.html");
	}
	
}