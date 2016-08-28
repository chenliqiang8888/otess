package com.otess.model;

import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class MediaFileModel extends Model<MediaFileModel> {
	public static final MediaFileModel me = new MediaFileModel();
	public Page<MediaFileModel> paginate(int pageNumber, int pageSize,String m_name,
			String m_s_addtime,String m_e_addtime,Integer r_type,String m_type,String users) {
		String sql="from mp_media_file mf "
				+ "left join (select tf_media_no from mp_task_file group by tf_media_no) tf on mf.m_no=tf.tf_media_no "
				+ "left join (select right(tf_bgsound,32) as tf_bgsound_no from mp_task_file group by tf_bgsound_no) tb on mf.m_no=tb.tf_bgsound_no "
				+ "left join mp_filetype f on f.f_id=mf.m_type "
				+ "left join mp_user u on mf.m_userid=u.uid where 1=1";
		if(StrKit.notBlank(m_name)){
			sql+=" and m_name like '%"+m_name+"%'";
		}
		if(StrKit.notBlank(m_s_addtime)){
			sql+=" and m_add_time>='"+m_s_addtime+"'";
		}

		if(StrKit.notBlank(m_e_addtime)){
			sql+=" and m_add_time<='"+m_e_addtime+"'";
		}
		if(StrKit.notBlank(m_type)){

			sql+=" and m_type="+m_type;
		}
		if(StrKit.notBlank(users)){
			sql+=" and (m_userid in ("+users+")";

			if(r_type!=2){
				sql+=" or m_state=1";
			}
			sql+=")";
		}
		sql+=" order by m_add_time desc";		
		System.out.println(users);
		return paginate(pageNumber, pageSize, "select mf.*,IFNULL(tf.tf_media_no,0) as tf_media_no,"
				+ "IFNULL(tb.tf_bgsound_no,0) as tf_bgsound_no,"
				+ "IFNULL(f.f_name,'未知类型') as f_name,"
				+ "IFNULL(f.f_id,-1) as f_id,u.username", sql);
	}
	public List<MediaFileModel> findAll(Integer m_type,String m_name,String users,Integer r_type){
		String sql="select mf.*,f.f_name from mp_media_file mf left join mp_filetype f on f.f_id=mf.m_type where 1=1";
		if(!m_type.equals(0)){
			sql+=" and m_type="+m_type;
		}
		if(StrKit.notBlank(m_name)){
			sql+=" and m_name like '%"+m_name+"%'";
		}

		if(StrKit.notBlank(users)){
			sql+=" and (m_userid in ("+users+")";

			if(r_type!=2){
				sql+=" or m_state=1";
			}
			sql+=")";
		}
		sql+=" order by m_add_time desc";
		return me.find(sql);
	}
	public Integer deleteByValues(String idValues){
		return Db.update("delete from mp_media_file id=?",idValues);
	}
	public MediaFileModel findByNo(String no){
		return me.findFirst("select m_id, m_name,m_file,m_size_high,m_type from mp_media_file where m_no=?",no);
	}
}
