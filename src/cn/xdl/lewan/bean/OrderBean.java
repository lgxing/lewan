package cn.xdl.lewan.bean;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class OrderBean extends BmobObject{
	
	private String orderNum;//订单号
	private String userId;//付款用户ID
	private String GatherId;//参加的活动ID
	private Integer orderRmb;//订单金额
	
	
	public Integer getOrderRmb() {
		return orderRmb;
	}
	public void setOrderRmb(Integer orderRmb) {
		this.orderRmb = orderRmb;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGatherId() {
		return GatherId;
	}
	public void setGatherId(String gatherId) {
		GatherId = gatherId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	
}
