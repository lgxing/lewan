package cn.xdl.lewan.bean;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class PinglunBean extends BmobObject {
	
	private String uid; //�����û���ID
	private String content;//��������
	private String gatherid;//���ۻ��Id
	
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getGatherid() {
		return gatherid;
	}
	public void setGatherid(String gatherid) {
		this.gatherid = gatherid;
	}
	
	
}
