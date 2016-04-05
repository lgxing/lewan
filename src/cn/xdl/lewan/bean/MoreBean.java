package cn.xdl.lewan.bean;

import android.graphics.Bitmap;

public class MoreBean {
	
	private Bitmap icon;
	private String text;
	public MoreBean(Bitmap icon, String text) {
		super();
		this.icon = icon;
		this.text = text;
	}
	public Bitmap getIcon() {
		return icon;
	}
	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
