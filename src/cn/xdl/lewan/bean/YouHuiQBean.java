package cn.xdl.lewan.bean;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class YouHuiQBean extends BmobObject {
	
	private String uid;//�Ż�ȯ��ӵ����Id
	private Integer jine;//���
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Integer getJine() {
		return jine;
	}
	public void setJine(Integer jine) {
		this.jine = jine;
	}
	
	
}
