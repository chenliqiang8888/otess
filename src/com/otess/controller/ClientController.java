package com.otess.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.otess.common.bean.Code;
import com.otess.common.bean.ListResponse;
import com.otess.common.utils.StringUtils;
import com.otess.interceptor.AdminInterceptor;
import com.otess.model.ClientImgModel;
import com.otess.model.ClientModel;
import com.otess.model.FoldModel;
import com.otess.model.MediaFileModel;
import com.otess.model.OperatorModel;
import com.otess.model.UserModel;

@Before(AdminInterceptor.class)
public class ClientController extends BaseController {
	public void list(){
		UserModel user=this.getSessionAttr("user");
		List<FoldModel> fold=FoldModel.me.findAllByPId(user.getInt("groupid"));
		String cf_ids=getPara("cf_ids");
		String cl_name=getPara("cl_name");
		String cl_ip=getPara("cl_ip");
		if(StrKit.isBlank(cf_ids)){
			cf_ids="";
			for(FoldModel x:fold){
				if(StrKit.notBlank(cf_ids))cf_ids+=",";
				cf_ids+=x.get("cf_id").toString();
			}
		}
		setAttr("fold",JsonKit.toJson(fold));
		setAttr("clientPage", ClientModel.me.paginate(getParaToInt("p", 1), this.getPageSize(),
				cf_ids,cl_name,cl_ip));
		render("/views/client/list.html");
	}
	public void getClientAll(){
		renderJson(ClientModel.me.findAll());
	}
	public void getClientByGroupId(){
		String cf_ids=getPara("cf_ids");
		List<ClientModel> list=ClientModel.me.findAllByGroupId(cf_ids);		
		renderJson(list);	
	}
	//获取终端及组
	public void getClientAndFoldByGroupId(){
		//获取组列表
		UserModel user=this.getSessionAttr("user");
		ClientModel client=new ClientModel()
				.set("cl_name", getPara("cl_name"))
				.set("cl_ip", getPara("cl_ip"));
				
		List<FoldModel> fold=FoldModel.me.findAllByPId(user.getInt("groupid"));
		System.out.println(JsonKit.toJson(fold));
		//获取所有的终端
		List<ClientModel> list=ClientModel.me.findByNameAndIpAndGroup(client,getPara("cl_fold"));
		/*fold.add(new FoldModel()
				.set("cf_id", 10000)
				.set("cf_name","未分组")
				.set("cf_pid",0)
				.set("cf_info",""));*/
		//按分组绑定终端
		Map m=new HashMap();
		m.put("fold", fold);
		m.put("client",list);
		renderJson(m);	
	}
	/**
	 * 更新分组
	 */
	public void updategroup(){
		String[] clientids=getPara("clientids").split(",");
		Integer foldid=getParaToInt("foldid");
		int success=0;
		for (String cl_id : clientids) {
			ClientModel model=new ClientModel()
					.set("cl_fold_id",foldid)
					.set("cl_id",Integer.parseInt(cl_id));
			System.out.println(JsonKit.toJson(model));
			success+=ClientModel.me.updateGroupId(model);
			this.setSysLog("更新分组："+getPara("ids"), 1004);
		}
		renderSuccess("操作成功");
	}
	public void remoteopt(){
		Integer order=getParaToInt("order");
		/**
		 * 1=开机 2=重启 3=关机 
		 * 4=定时关机 11=取消定时关机 5=音量 6=同步时钟 7=清除（改为离线）
		 * 8=清空（客户端删除媒体） 13=升级 15=监控墙体
		 */
		String clientids=getPara("clientids");
		render("/views/client/list.html");
	}
	/**
	 * 终端操作指令
	 */
	public void command(){
		String clientids=getPara("clientids");//终端id,逗号","
		String ids=getPara("ids");//终端id,逗号","
		String type=getPara("type");//指令
		String param=getPara("param");//指令
		int count=0;
		if(StrKit.notBlank(clientids) && StrKit.notBlank(type)){
			String[] deviceids = clientids.split(",");
			
			for (String deviceid : deviceids) {
				OperatorModel me=new OperatorModel()
						.set("o_type",type)
						.set("o_deviceid",deviceid)
						.set("o_param",param);
				/*OperatorModel model=OperatorModel.me.findByDeviceIdAndType(deviceid,type);
				if(StrKit.notNull(model)){
					me.set("c_id", model.getInt("c_id"));
					me.update();
				}else{
					
				}*/
				me.save();
				if(me.getInt("o_id")>0){
					count++;
				}
			}
			if(type.equals("11") || type.equals("4")){
				if(type.equals("11")){
					this.setSysLog("取消定时关机："+getPara("ids"), 1004);					
				}else{
					this.setSysLog("设置定时关机："+getPara("ids"), 1004);					
				}
				setField(ids,"cl_shut_time",param);
			}
			if(type.equals("5")){
				this.setSysLog("设置定时关机："+getPara("ids"), 1004);
				setField(ids,"cl_shut_time",param);
			}
			if(type.equals("2")){
				this.setSysLog("重启终端："+getPara("ids"), 1004);
			}else if(type.equals("3")){
				this.setSysLog("关机："+getPara("ids"), 1004);
			}else if(type.equals("7")){
				this.setSysLog("清除："+getPara("ids"), 1004);
			}else if(type.equals("8")){
				this.setSysLog("清空："+getPara("ids"), 1004);
			}else if(type.equals("3")){
				this.setSysLog("关机："+getPara("ids"), 1004);
			}
			renderSuccess("操作成功");
		}else{
			renderArgumentError("参数错误");
		}		
	}
	/**
	 * 设置定时关机
	 */
	public void shuttime(){
		this.setSysLog("取消定时关机："+getPara("ids"), 1004);
		renderSuccess("["+setField(getPara("ids"),"cl_shut_time",getPara("param"))+"]条 操作成功!");
	}
	public int setField(String clientids,String field,String value){
		String[] ids = clientids.split(",");
		int count=0;
		for (String id : ids) {
			ClientModel clientModel=new ClientModel()
					.set(field,value)
					.set("cl_id",id);			
			
			if(clientModel.update()){
				count++;
			}
		}
		return count;
	}
	/**
	 * 清除
	 */
	public void out(){
		String[] ids = getPara("ids").split(",");
		int count=0;
		for (String id : ids) {
			ClientModel clientModel=new ClientModel()
					.set("cl_is_del",1)
					.set("cl_id",id);			
			
			if(clientModel.update()){
				ClientModel client=ClientModel.me.findById(id);
				this.setSysLog("清除终端："+client.getStr("cl_name"), 1004);
				count++;
			}
		}
		renderSuccess("["+count+"]条 操作成功!");
	}
	/**
	 * 远程监控
	 */
	public void monitor(){
		String deviceid=getPara("deviceid");
		String name=getPara("name");
		String ip=getPara("ip");
		//发送监控指令
		OperatorModel me=new OperatorModel()
				.set("o_type",9)
				.set("o_deviceid",deviceid);
			me.save();
		setAttr("name",name);
		setAttr("ip",ip);
		setAttr("deviceid",deviceid);
		render("/views/client/monitor.html");	
	}
	/**
	 * 定时读取app上报的监控截图
	 */
	public void monitorimg(){
		String deviceid=getPara("deviceid");
		//按时间倒序取出第一条
		renderJson(JsonKit.toJson(ClientImgModel.me.findOneByDeviceId(deviceid)));
	}
}
