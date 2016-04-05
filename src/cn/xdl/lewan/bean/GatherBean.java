package cn.xdl.lewan.bean;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.search.core.PoiInfo;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

@SuppressWarnings("serial")
public class GatherBean extends BmobObject {
	
	private String name;//活动名称
	private String info;//活动详情
	private BmobFile gatherPNG;//活动图片
	private String time;//活动时间
	private PoiInfo poi;//活动地点
	private Integer rmb;//活动金额
	private String type;//活动类型
	private String u_id;//活动发起者
	private BmobGeoPoint point;//bmob提供地理位置信息查询
	
	private Integer loveCount;//活动点赞次数
	private List<String> loveUserIds;//点赞所有用户的ID集合
	private List<String> yifukuanIds;//已付款所有用户的ID集合，用于发送推送消息
	
	
	public BmobGeoPoint getPoint() {
		return point;
	}
	public void setPoint(BmobGeoPoint point) {
		this.point = point;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public BmobFile getGatherPNG() {
		return gatherPNG;
	}
	public void setGatherPNG(BmobFile gatherPNG) {
		this.gatherPNG = gatherPNG;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public PoiInfo getPoi() {
		return poi;
	}
	public void setPoi(PoiInfo poi) {
		this.poi = poi;
	}
	public Integer getRmb() {
		return rmb;
	}
	public void setRmb(Integer rmb) {
		this.rmb = rmb;
	}
	public String getU_id() {
		return u_id;
	}
	public void setU_id(String u_id) {
		this.u_id = u_id;
	}
	
	public Integer getLoveCount() {
		if (loveCount==null) {
			loveCount = 0;
		}
		return loveCount;
	}
	public void setLoveCount(Integer loveCount) {
		this.loveCount = loveCount;
	}
	public List<String> getLoveUserIds() {
		if (loveUserIds==null) {
			loveUserIds = new ArrayList<>();
		}
		return loveUserIds;
	}
	public void setLoveUserIds(List<String> loveUserIds) {
		this.loveUserIds = loveUserIds;
	}
	public List<String> getYifukuanIds() {
		if(yifukuanIds==null){
			yifukuanIds=new ArrayList<>();
		}
		return yifukuanIds;
	}
	public void setYifukuanIds(List<String> yifukuanIds) {
		this.yifukuanIds = yifukuanIds;
	}
	
	
}
