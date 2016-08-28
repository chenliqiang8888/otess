package com.otess.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class LogSendModel extends Model<LogSendModel> {
	public static final LogSendModel me = new LogSendModel();
	public Page<LogSendModel> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select lsv.* ,t.* ",
				" from (select ls.*, cl_name, cl_ip  from  mp_log_send ls, mp_client"
				+ " where ls_client_id=cl_id  and ls_type <> 0) lsv LEFT JOIN mp_task t on lsv.ls_task_id=t.t_id");
	}
	public List<LogSendModel> findAll(){
		return me.find("select lsv.* ,t.* ,u.username"
				+ " from (select ls.*, cl_name, cl_ip  from  mp_log_send ls, mp_client"
				+ " where ls_client_id=cl_id  and ls_type <> 0) lsv LEFT JOIN mp_task t on lsv.ls_task_id=t.t_id left join mp_user u on lsv.userid=u.uid");
	}
	/**
	 * 获取发送任务，只发送小于当前时间的
	 * @param deviceid
	 * @return
	 */
	public List<LogSendModel> findByDeviceId(String deviceid){
		String sql="select mls.* from mp_log_send as mls "
				+ "left join mp_client as mc on mls.ls_client_id=mc.cl_id "
				+ "where mls.ls_total_percent<100 and mc.cl_deviceid = ?";
		//System.out.print(sql);
		return me.find(sql,deviceid);
	}
	/**
	 * 删除进度是100%的任务
	 * @return
	 */
	public int deleteByPercent(){
		return Db.update("delete from mp_log_send where ls_total_percent=100");
	}
}
