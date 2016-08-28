package com.otess.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class ActionModel extends Model<ActionModel> {
	public static final ActionModel me = new ActionModel();
	public List<ActionModel> findAll(){
		return me.find("select * from mp_action where a_status=1 order by a_order");
	}
}
