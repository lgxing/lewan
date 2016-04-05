package cn.xdl.lewan.utils;

import android.content.Context;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.xdl.lewan.bean.GatherBean;

public class FindDatasUtils {
	
	/**查询活动队象
	 * @param limit 查询条数
	 * @param skip 忽略查询条数
	 * @param context 上下文 
	 * @param findListener 查询成功与失败的回掉接口
	 * @param isReture 是否自动执行查询操作 true 查询 false 不查询
	 * @param skip 忽略多少条
	 * @return query
	 */
	public static BmobQuery<GatherBean> findDatas(int skip,int limit, Context context, FindListener<GatherBean> findListener,boolean isReture){
		BmobQuery<GatherBean> query = findDatas(limit, context, findListener, false);
		query.setSkip(skip);
		//查找活动
		if (isReture) {
			query.findObjects(context, findListener);
		}
		return query;
	}
	/**查询活动队象
	 * @param limit 查询条数
	 * @param context 上下文 
	 * @param findListener 查询成功与失败的回掉接口
	 * @param isReture 是否自动执行查询操作 true 查询 false 不查询
	 * @return query
	 */
	public static BmobQuery<GatherBean> findDatas(int limit, Context context, FindListener<GatherBean> findListener,boolean isReture){
		
		BmobQuery<GatherBean> query = new BmobQuery<>();
		query.setLimit(limit);
		query.order("-createdAt");//按时间倒序查找
		//查找活动
		if (isReture) {
			query.findObjects(context, findListener);
		}
		return query;
	}
}
