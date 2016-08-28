package com.otess.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class FileTypeModel extends Model<FileTypeModel> {
	public static final FileTypeModel me = new FileTypeModel();
	public FileTypeModel findByType(String type){
		String sql="select * from mp_filetype where f_type like '%"+type+"%'";
		return me.findFirst(sql);
	}
	public List<FileTypeModel> findAll(){
		String sql="select * from mp_filetype";
		return me.find(sql);
	}
}
