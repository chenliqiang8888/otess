package com.otess.controller;

import java.util.List;

import com.otess.common.bean.Code;
import com.otess.common.bean.DataResponse;
import com.otess.interceptor.AdminInterceptor;
import com.otess.model.FileTypeModel;
import com.otess.model.TaskFileModel;
import com.otess.model.TaskItemModel;
import com.otess.model.TaskModel;
import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;

@Before(AdminInterceptor.class)
public class TaskItemController extends BaseController {
	public void list() {
		Integer t_id=getParaToInt(0);
		TaskModel taskModel=TaskModel.me.findByIdAndMrId(t_id);
		setAttr("task",JsonKit.toJson(taskModel));//任务
		setAttr("t_id",t_id);
		setAttr("taskItemPage", TaskItemModel.me.paginate(getParaToInt(1, 1), this.getPageSize(),t_id));
		render("/views/taskitem/list.html");
	}
	public void layout(){
		Integer t_id=getParaToInt(0);//任务
		Integer ti_id=getParaToInt(1);//任务项
		TaskModel taskModel=TaskModel.me.findByIdAndMrId(t_id);
		TaskItemModel taskItemModel=null;
		List<TaskFileModel> taskFileModel=null;
		if(ti_id>0){//编辑的时候获取相关任务资源
			taskItemModel=TaskItemModel.me.findById(ti_id);//获取任务项
			taskFileModel=TaskFileModel.me.findByTaskItemId(ti_id);
		}
		setAttr("t_id",t_id);
		setAttr("task",JsonKit.toJson(taskModel));//任务
		setAttr("taskitem",JsonKit.toJson(taskItemModel));//任务项
		setAttr("taskfile",JsonKit.toJson(taskFileModel));//任务资源
		render("/views/taskitem/layout.html");
	}
	public void detail(){
		Integer ti_id=getParaToInt("ti_id");
		renderJson(new DataResponse(Code.SUCCESS,"ok").setData(TaskItemModel.me.findById(ti_id)));
	}
	/**
	 * 创建布局
	 */
	public void add(){
		TaskItemModel me=new TaskItemModel()
				.set("ti_task_id",getPara("ti_task_id"))
				.set("ti_name",getPara("ti_name"))
				.set("ti_duration",getPara("ti_duration"))
				.set("ti_screen_type",getPara("ti_screen_type"))
				.set("ti_screen_type_affix",getPara("ti_screen_type_affix"));
		System.out.println(getParaToInt("ti_id"));
		if(getParaToInt("ti_id")!=null){
			me.set("ti_id",getParaToInt("ti_id"));
			me.update();
		}
		else{
			me.save();
		}		
		
		Integer ti_id=me.getInt("ti_id");
		if(ti_id>0){
			this.setSysLog("创建任务项："+getPara("ti_task_id"), 1002);
			//renderSuccess("操作成功!");
			renderJson(new DataResponse(Code.SUCCESS,"ok").setData(ti_id));
		}else{
			renderArgumentError("参数错误");
		}
	}
	/**
	 * 保存布局
	 */
	public void eidtscreen(){
		TaskItemModel me=new TaskItemModel()
				.set("ti_screen_type",getPara("ti_screen_type"))
				.set("ti_screen_type_affix",getPara("ti_screen_type_affix"))
				.set("ti_id",getParaToInt("ti_id"));
		me.update();
		if(!me.update()){
			renderSuccess("操作成功!");
		}else{
			renderArgumentError("参数错误");
		}
	}
	public void edit(){
		TaskItemModel me=new TaskItemModel()
				.set("ti_name",getPara("ti_name"))
				.set("ti_duration",getParaToInt("ti_duration"))
				.set("ti_id",getParaToInt("ti_id"));
		me.update();
		if(!me.update()){
			renderSuccess("操作成功!");
		}else{
			renderArgumentError("参数错误");
		}
	}
	public void averagetime(){
		TaskItemModel taskItemModel=TaskItemModel.me.findById(getParaToInt("ti_id"));
		if(taskItemModel.getInt("ti_id")>0){
			String[] attr=((String) taskItemModel.get("ti_screen_type")).split(",");
            int count = Integer.parseInt(attr[0]);
            for (int i = 0; i < count; i++ )
            {
            	List<TaskFileModel> taskFile=TaskFileModel.me.findByTaskItemId(taskItemModel.getInt("ti_id"),i);
            	if(taskFile.size()>0){
            		int avgtime=taskItemModel.getInt("ti_duration")/taskFile.size();
            		for(TaskFileModel file:taskFile){
            			file.set("tf_duration", avgtime);  
            			file.update();
            		}
            	}
            }

    		this.setSysLog("平分时间："+getParaToInt("ti_id"), 1002);
		}
		renderSuccess("操作成功!");
	}

	public void delete(){
		TaskItemModel.me.deleteById(getParaToInt("ti_id"));//删除任务项
		TaskFileModel.me.deleteByTaskItemId(getParaToInt("t_id"));//删除任务资源

		this.setSysLog("删除任务项："+getParaToInt("ti_id"), 1002);
		redirect("/taskitem/list/"+getParaToInt("t_id"));
	}
	
	public void dosave(){
		TaskItemModel taskitem=getModel(TaskItemModel.class,"taskitem");
		taskitem.save();
		redirect("/taskitem/list?t_id="+getPara("ti_task_id"));
	}
	/**
	 * 导入布局
	 */
	public void importscreen(){
		TaskItemModel model=new TaskItemModel()
				.set("ti_screen_type",getPara("ti_screen_type"))
				.set("ti_screen_type_affix",getPara("ti_screen_type"))
				.set("ti_id",getParaToInt("ti_id"));
		if(TaskItemModel.me.update(model)>0){
			renderSuccess("操作成功!");
		}else{
			renderArgumentError("参数错误");
		}
	}
	public void simple(){
		TaskItemModel taskItemModel=TaskItemModel.me.findSimpleByTid(getParaToInt("t_id"));//获取任务项
		if(taskItemModel!=null){
			renderJson(new DataResponse(Code.SUCCESS).setData(taskItemModel));
		}else{
			renderArgumentError("参数错误");
		}
	}
}