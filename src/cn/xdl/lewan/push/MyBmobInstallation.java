package cn.xdl.lewan.push;

import android.content.Context;
import cn.bmob.v3.BmobInstallation;

@SuppressWarnings("serial")
public class MyBmobInstallation extends BmobInstallation {

    /**  
     * �û�id-�������Խ��豸���û�֮����а�
     */  
    private String uid;
	
	public MyBmobInstallation(Context context) {
		super(context);
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	
}
