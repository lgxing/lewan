package cn.xdl.lewan.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

@SuppressWarnings("serial")
public class UserBean extends BmobUser {
	private BmobFile userIcon; //用户头象
	private List<String> loveGather_id;//用户收藏活动的ID
	private List<String> aplayedONum;//用户支付活动的订单号
	private String address,sex; //用户的地址、性别
	private Integer age;//用户年龄
	

	public List<String> getAplayedONum() {
		if (aplayedONum==null) {
			aplayedONum = new ArrayList<>();
		}
		
		return aplayedONum;
	}

	public void setAplayedONum(List<String> aplayedO_id) {
		this.aplayedONum = aplayedO_id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public List<String> getLoveGather_id() {
		if (loveGather_id==null) {
			loveGather_id = new ArrayList<>();
		}
		return loveGather_id;
	}

	public void setLoveGather_id(List<String> loveGather_id) {
		this.loveGather_id = loveGather_id;
	}

	public BmobFile getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(BmobFile userIcon) {
		this.userIcon = userIcon;
	}
	
	
}
