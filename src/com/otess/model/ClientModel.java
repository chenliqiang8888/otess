package com.otess.model;

import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.kit.StringKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.otess.common.utils.StringUtils;

@SuppressWarnings("serial")
public class ClientModel extends Model<ClientModel> {
	public static final ClientModel me = new ClientModel();
	public Page<ClientModel> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from mp_client where cl_is_del=0");
	}
	/**
	 * 按名字搜索终端
	 * @param name
	 * @return
	 */
	public ClientModel findByName(String name){
		return me.findFirst("select * from mp_client where cl_name=?",name);
	}
	public ClientModel findByDeviceId(String deviceid){
		return me.findFirst("select * from mp_client where cl_deviceid=?",deviceid);
	}
	public List<ClientModel> findAll(){
		String sql="select mc.*,IFNULL(f.cf_name,'') as cf_name,IFNULL(TIMESTAMPDIFF(SECOND, cs.createdat, now()),1000) as status from mp_client mc left join mp_client_status cs on mc.cl_deviceid=cs.deviceid left join mp_fold f on cl_fold_id=f.cf_id where cl_is_del=0 order by cl_id asc";
		return me.find(sql);
	}
	/**
	 * 按分组获取终端
	 * @param cf_ids
	 * @return
	 */
	public List<ClientModel> findAllByGroupId(String cf_ids){
		String sql="select * from mp_client where cl_is_del";
		if(StrKit.notBlank(cf_ids)){
			sql+=" and cl_fold_id in ("+cf_ids+")";
		}
		return me.find(sql);
	}
	/**
	 * 更新终端名字
	 * @param model
	 * @return
	 */
	public int update(ClientModel model){
		return Db.update("update mp_client set cl_name=? where cl_deviceid=?",model.getStr("cl_name"),model.getStr("cl_deviceid"));
	}

	public Page<ClientModel> paginate(int pageNumber, int pageSize,String cf_ids,String cl_name,String cl_ip) {
		String sql="from mp_client mc left join mp_client_status cs on mc.cl_deviceid=cs.deviceid left join mp_fold f on cl_fold_id=f.cf_id where cl_is_del=0 ";
		if(StringUtils.isNotEmpty(cf_ids)){
			sql+=" and cl_fold_id in ("+cf_ids+")";
		}
		if(StringUtils.isNotEmpty(cl_name)){
			sql+=" and cl_name like '%"+cl_name+"%'";
		}
		if(StringUtils.isNotEmpty(cl_ip)){
			sql+=" and cl_ip like '%"+cl_ip+"%'";
		}
		return paginate(pageNumber, pageSize, "select mc.*,cs.version,IFNULL(f.cf_name,'') as cf_name,IFNULL(TIMESTAMPDIFF(SECOND, cs.createdat, now()),1000) as status", sql);
	}
	/**
	 * 根据分组、机器名、ip搜索
	 * @param model
	 * @return
	 */
	public int updateGroupId(ClientModel model){
		return Db.update("update mp_client set cl_fold_id=? where cl_id=?",model.getInt("cl_fold_id"),model.getInt("cl_id"));
	}
	/**
	 * 删除分组，改变终端分组为0
	 * @param cl_fold_id
	 * @return
	 */
	public int updateGroupId(Integer cl_fold_id){
		return Db.update("update mp_client set cl_fold_id=0 where cl_fold_id=?",cl_fold_id);
	}
	/**
	 * 根据分组、机器名、ip搜索
	 * @param model
	 * @return
	 */
	public List<ClientModel> findByNameAndIpAndGroup(ClientModel model,String cl_fold){
		String sql="select mc.*,f.cf_name,IFNULL(f.cf_name,'') as cf_name,IFNULL(TIMESTAMPDIFF(SECOND, cs.createdat, now()),1000) as status "
		+ "from mp_client mc left join mp_client_status cs on mc.cl_deviceid=cs.deviceid left join "
		+ "mp_fold f on cl_fold_id=f.cf_id where cl_is_del=0";
		if(StringUtils.isNotEmpty(model.getStr("cl_name"))){
			sql+=" and mc.cl_name like '%"+model.getStr("cl_name")+"%'";
		}
		if(StringUtils.isNotEmpty(cl_fold)){
			sql+=" and f.cf_name like '%"+cl_fold+"%'";
		}
		if(StringUtils.isNotEmpty(model.getStr("cl_ip"))){
			sql+=" and mc.cl_ip like '%"+model.getStr("cl_ip")+"%'";
		}
		sql+=" order by cl_id asc";
		return me.find(sql); 
	}
}
