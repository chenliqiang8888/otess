package com.otess.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class ScreenModel extends Model<ScreenModel> {
	public static final ScreenModel me = new ScreenModel();
	public List<ScreenModel> findAll(){
		return me.find("select * from mp_screen");
	}
	
}