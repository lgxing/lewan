package cn.xdl.lewan.bean;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class MsgBean extends BmobObject {

	private String reciveId;//消息接收者ID
	private String content;//消息内容
	private String sendName;//发送用户的用户名
	
	
	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	public String getReciveId() {
		return reciveId;
	}
	public void setReciveId(String reciveId) {
		this.reciveId = reciveId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
