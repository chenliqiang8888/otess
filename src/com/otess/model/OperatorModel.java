package com.otess.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class OperatorModel extends Model<OperatorModel> {
	public static final OperatorModel me = new OperatorModel();
	public List<OperatorModel> findByDeviceId(String deviceid){
		return me.find("select o_type,o_param from mp_operator where o_deviceid like '%"+deviceid+"%'");
	}
	public OperatorModel findByDeviceIdAndType(String deviceid,String type){
		return me.findFirst("select o_type,o_param,o_deviceid from mp_operator where o_deviceid=? and o_type=?",deviceid,type);
	}
	public int delete(String deviceid){
		return Db.update("delete from mp_operator where o_deviceid=?",deviceid);
	}
}
