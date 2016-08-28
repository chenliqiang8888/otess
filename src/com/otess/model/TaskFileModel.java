package com.otess.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class TaskFileModel extends Model<TaskFileModel> {
	public static final TaskFileModel me = new TaskFileModel();
	public Page<TaskFileModel> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from mp_task_file");
	}
	public List<TaskFileModel> findByTaskItemId(Integer id){
		return me.find("select f.*,m.m_id,m.m_name,m.m_file,m.m_size_high,m.m_type from mp_task_file f left join mp_media_file m "
				+ "on f.tf_media_no=m.m_no"
				+ " where f.tf_task_item_id=? order by f.tf_index asc,f.tf_id desc",id);
	}
	public List<TaskFileModel> findByTaskItemId(Integer tf_task_item_id,Integer tf_position){
		return me.find("select f.*,m.m_id,m.m_name,m.m_file,m.m_size_high,m.m_type from mp_task_file f left join mp_media_file m on f.tf_media_no=m.m_no"
				+ " where f.tf_task_item_id=? and tf_position=? order by tf_index asc,f.tf_id desc",tf_task_item_id,tf_position);
	}
	/**
	 * 删除任务项目下的全部资源
	 * @param tf_id
	 * @return
	 */
	public int deleteByTaskItemId(Integer tf_id){
		return Db.update("delete from mp_task_file where tf_task_item_id=?",tf_id);
	}
	/**
	 * 删除布局中的资源
	 * @param tf_id
	 * @param tf_position
	 * @return
	 */
	public int deleteByTAskItemIdAndPosition(Integer ti_id,Integer tf_position){
		return Db.update("delete from mp_task_file where tf_task_item_id=? and tf_position=?",ti_id,tf_position);
	}
	/**
	 * 根据任务id获取任务资源
	 * @param id
	 * @return
	 */
	public List<TaskFileModel> findByTaskId(Integer id){
		return me.find("select *,m.m_id,m.m_name,m.m_file,m.m_size_high,m.m_type from mp_task_file file inner JOIN "
				+ "(select * from mp_task_item  where ti_task_id=?) item "
				+ "on file.tf_task_item_id=item.ti_id left join mp_media_file m "
				+ "on file.tf_media_no=m.m_no order by file.tf_index asc,file.tf_id desc",id);
	}
	
	public int updateIndex(Integer tf_id,Integer tf_index){
		return Db.update("update mp_task_file set tf_index=? where tf_id=?",tf_index,tf_id);
	}
}
