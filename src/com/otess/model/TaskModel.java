package com.otess.model;

import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class TaskModel extends Model<TaskModel> {
	public static final TaskModel me = new TaskModel();
	public Page<TaskModel> page(int pageNumber, int pageSize,String t_userid,String t_status) {
		System.out.print("pp:"+pageNumber);
		String sql="from mp_task mt left join mp_user mu on mt.t_userid=mu.uid left join mp_user mo on mo.uid=mt.o_userid where 1=1";
		if(StrKit.notBlank(t_status)){
			sql+=" and t_status="+t_status;
		}
		if(StrKit.notBlank(t_userid)){
			sql+=" and t_userid in ("+t_userid+")";
		}
		sql+=" order by t_add_time desc";
		return paginate(pageNumber, pageSize, "select mt.*,mu.*,mo.username as ousername", sql);
	}
	public List<TaskModel> findByStatusAndUid(String t_userid,String t_status,String t_name,Integer roleid){
		System.out.println(roleid);
		String sql="select mt.*,mu.*,mo.username as ousername from mp_task mt left join mp_user mu on mt.t_userid=mu.uid left join mp_user mo on mo.uid=mt.o_userid where 1=1";
		if(StrKit.notBlank(t_status)){
			sql+=" and t_status="+t_status;
		}
		if(StrKit.notBlank(t_userid)){
			sql+=" and t_userid in ("+t_userid+")";
		}
		if(StrKit.notBlank(t_name)){
			sql+=" and t_name like '%"+t_name+"%'";
		}
		//普通用户
//		if(roleid.equals(0)){
//			sql+=" and o_status=1";
//		}
		sql+=" and o_status=1";
		sql+=" order by t_add_time desc";
		return me.find(sql);
	}
	
	public TaskModel findByIdAndMrId(Integer t_id){
		String sql="select * from mp_task t inner join mp_resolution r on t.mr_id=r.mr_id where t.t_id=?";
		return me.findFirst(sql,t_id);
	}
	public List<TaskModel> getAll(){
		List<TaskModel> tasks=me.find("select * from mp_task");
		return tasks;
	}
	/**
	 * 删除任务，任务项目，任务资源
	 * @param t_id
	 * @return
	 */
	public int deleteTaskAndItemAndFile(Object t_id){
		return Db.update("delete item,file from mp_task_item item left join mp_task_file file on item.ti_id=file.tf_task_item_id where item.ti_task_id=?",t_id);
	}
	/**
	 * 判断是否存在同名任务
	 * @param name
	 * @return
	 */
	public TaskModel findByName(String name){
		String sql="select * from mp_task where t_name=?";
		return me.findFirst(sql,name);
	}
	
	/**
	 * 修改任务状态
	 * @param model
	 * @return
	 */
	public int updateStatus(TaskModel model){
		return Db.update("update mp_task set t_status=? where t_id=?",model.getInt("t_status"),model.getInt("t_status"));
	}
}
