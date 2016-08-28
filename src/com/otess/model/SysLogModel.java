package com.otess.model;

import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.otess.common.utils.StringUtils;

@SuppressWarnings("serial")
public class SysLogModel extends Model<SysLogModel> {
	public static final SysLogModel me = new SysLogModel();
	public Page<SysLogModel> page(int pageNumber, int pageSize,
			String username,Integer sl_type,String sl_ip,String m_s_addtime,String m_e_addtime) {
		String sql=" from mp_syslog sl left join mp_user u on sl.sl_userid=u.uid left join mp_syslogtype lt on lt.lt_id=sl.sl_type where 1=1";
		if(StringUtils.isNotEmpty(username)){
			sql+=" and username like '%"+username+"%'";
		}
		if(StringUtils.isNotEmpty(sl_ip)){
			sql+=" and sl_ip like '%"+sl_ip+"%'";
		}
		if(!sl_type.equals(0)){
			sql+=" and sl_type="+sl_type;
		}
		if(StrKit.notBlank(m_s_addtime)){
			sql+=" and sl_time>='"+m_s_addtime+"'";
		}

		if(StrKit.notBlank(m_e_addtime)){
			sql+=" and sl_time<='"+m_e_addtime+"'";
		}
		sql+=" order by sl_time desc";
		return paginate(pageNumber, pageSize, "select sl.*,u.username,lt.lt_name", sql);
	}
	
	public int empty(){
		return Db.update("delete from  mp_syslog");
	}
	
	public List<SysLogModel> findByNameAndIp(String username,Integer sl_type,String sl_ip,String m_s_addtime,String m_e_addtime){
		String sql="select sl.*,u.username,lt.lt_name from mp_syslog sl left join mp_user u on sl.sl_userid=u.uid left join mp_syslogtype lt on lt.lt_id=sl.sl_type where 1=1";
		if(StringUtils.isNotEmpty(username)){
			sql+=" and username like '%"+username+"%'";
		}
		if(StringUtils.isNotEmpty(sl_ip)){
			sql+=" and sl_ip like '%"+sl_ip+"%'";
		}
		if(!sl_type.equals(0)){
			sql+=" and sl_type="+sl_type;
		}
		if(StrKit.notBlank(m_s_addtime)){
			sql+=" and sl_time>='"+m_s_addtime+"'";
		}

		if(StrKit.notBlank(m_e_addtime)){
			sql+=" and sl_time<='"+m_e_addtime+"'";
		}
		sql+=" order by sl_time desc";
		return me.find(sql);
	}
}
