package com.otess.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class ResolutionModel extends Model<ResolutionModel> {
	public static final ResolutionModel me = new ResolutionModel();
	public List<ResolutionModel> findAll(){
		String sql="select * from mp_resolution";
		return me.find(sql);
	}
	public ResolutionModel findOneByWH(Integer w,Integer h){
		return me.findFirst("select * from mp_resolution where mr_w=? and mr_h=?",w,h);
	}
}
