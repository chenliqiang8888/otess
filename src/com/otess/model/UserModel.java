package com.otess.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class UserModel extends Model<UserModel> {
	public static final UserModel me = new UserModel();

	public UserModel findByName(String name) {
		return me.findFirst("select * from mp_user u left join" + " mp_role r on u.roleid=r.r_id where u.username=?",
				name);
	}

	public Page<UserModel> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select u.*,r.*,ur.username as auditor,f.cf_name",
				"from mp_user u left join mp_role r on u.roleid=r.r_id left join mp_user ur on u.pid=ur.uid left join mp_fold f on u.groupid=f.cf_id");
	}

	public List<UserModel> findByPid(Integer uid) {
		return me.find("select * from mp_user where pid=?", uid);
	}

	public List<UserModel> findAll() {
		return me.find("select * from mp_user");
	}

}
