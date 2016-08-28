package com.otess.service;

import java.util.List;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.otess.model.TaskFileModel;
import com.otess.model.TaskItemModel;

public class TaskFileService {
	public static final TaskFileService me = new TaskFileService();
	/**
	 * 判断是否能添加任务
	 * @param taskFile
	 * @param isAdd
	 * @return
	 */
	public int isUpdateFile(TaskFileModel taskFile, int isAdd) {
		int tf_task_item_id = taskFile.getInt("tf_task_item_id");
		TaskItemModel taskItem = TaskItemModel.me.findById(tf_task_item_id);
		int allDuration = 0, maxIndex = 0;
		List<TaskFileModel> taskFileList = TaskFileModel.me.findByTaskItemId(tf_task_item_id, taskFile.get("tf_position"));

		//累加position下所有资源时长
		for(TaskFileModel model:taskFileList){
			Integer tf_id=StrKit.notNull(taskFile.getInt("tf_id"))?taskFile.getInt("tf_id"):0;
			System.out.println(tf_id);
			if(!model.getInt("tf_id").equals(tf_id)){
				allDuration+=model.getInt("tf_duration");
			}
		}
		int result = 1;
		int ti_duration=taskItem.getInt("ti_duration");
		int ti_duration_new=allDuration+taskFile.getInt("tf_duration");
		//判断资源时长是否大于任务项时长
		if(ti_duration<ti_duration_new){
			if(isAdd==0){//提示用户是否延长时长
				result=0;
			}else{
				taskItem.set("ti_duration",ti_duration_new);
				taskItem.update();
				result=1;
			}
		}		
		return result;
	}
}
