package com.otess.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class ClientImgModel extends Model<ClientImgModel> {
	public static final ClientImgModel me = new ClientImgModel();
	/**
	 * 查询最后一张截图
	 * @param deviceid
	 * @return
	 */
	public ClientImgModel findOneByDeviceId(String deviceid){
		return me.findFirst("select * from mp_client_monitor_img where i_deviceid=? order by createdat desc limit 1",deviceid);
	}
	/**
	 * 退出清空设备监控图
	 * @param deviceid
	 * @return
	 */
	public int deleteAllByDeviceId(String deviceid){
		return Db.update("delete from mp_client_monitor_img where i_deviceid=?",deviceid);
	}
}
