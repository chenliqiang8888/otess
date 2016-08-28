package com.otess.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class RoleModel extends Model<RoleModel> {
	public static final RoleModel me = new RoleModel();
	public List<RoleModel> findAll(){
		return me.find("select * from mp_role order by r_id");
	}
	
}
