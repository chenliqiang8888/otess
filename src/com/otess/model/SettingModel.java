package com.otess.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class SettingModel extends Model<SettingModel> {
	public static final SettingModel me = new SettingModel();
	public List<SettingModel> findAll(){
		return me.find("select * from mp_setting");
	}
	
}
