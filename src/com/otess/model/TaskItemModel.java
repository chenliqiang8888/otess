package com.otess.model;

import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class TaskItemModel extends Model<TaskItemModel> {
	public static final TaskItemModel me = new TaskItemModel();
	public Page<TaskItemModel> paginate(int pageNumber, int pageSize,Integer ti_task_id) {
		return paginate(pageNumber, pageSize, "select *", 
				"from mp_task_item where ti_task_id=? order by ti_id asc",ti_task_id);
	}
	public List<TaskItemModel> findByTaskId(Integer id){
		String sql="select * from mp_task_item where ti_task_id=? order by ti_id asc";
		return me.find(sql,id);
	}	
	/**
	 * 修改任务项目
	 * @param model
	 * @return
	 */
	public int update(TaskItemModel model){
		String sql="update mp_task_item set ";
		//导入布局
		if(StrKit.notNull(model.getStr("ti_screen_type"))){
			sql+="ti_screen_type='"+model.getStr("ti_screen_type")+"',";
			sql+="ti_screen_type_affix='"+model.getStr("ti_screen_type_affix")+"'";
		}
		sql+=" where ti_id="+model.getInt("ti_id");
		return Db.update(sql);
	}
	
	public TaskItemModel findSimpleByTid(Integer t_id){
		String sql="select * from mp_task_item where ti_task_id=?";
		return me.findFirst(sql,t_id);
	}
}
