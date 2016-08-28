package com.otess.common;

public class Result {
	public Integer code;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String msg;
	
	public Object list;
	public Object getList(){
		return list;
	}
	public void setList(Object list){
		this.list=list;
	}
}
