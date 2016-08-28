package com.otess.model;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class FoldModel extends Model<FoldModel> {
	public static final FoldModel me = new FoldModel();
	public List<FoldModel> findAll(){
		List<FoldModel> list=me.find("select * from mp_fold");
		return list;
	}
	public List<FoldModel> findAllByPId(int cf_id){
		List<FoldModel> result=new ArrayList<FoldModel>();
		List<FoldModel> list=me.findAll();
		for(FoldModel item:list){
			if(item.getInt("cf_id")==cf_id)result.add(item);			
		}
//		if(cf_id==0){
//			result.add(
//					new FoldModel().set("cf_id", 0).set("cf_name","控制中心").set("cf_pid",-1)
//				);
//		}
		result.add(
				new FoldModel().set("cf_id", 0).set("cf_name","控制中心").set("cf_pid",-1)
			);
		return me.findChild(result,list, cf_id);
	}
	private List<FoldModel> findChild(List<FoldModel> result,List<FoldModel> fold,int cf_id){
		for(FoldModel item:fold){
			if(item.getInt("cf_pid")==cf_id){
				result.add(item);
				me.findChild(result,fold,item.getInt("cf_id"));
			}
		}
		return result;
	}
}
