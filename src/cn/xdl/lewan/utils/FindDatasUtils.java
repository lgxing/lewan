package cn.xdl.lewan.utils;

import android.content.Context;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.xdl.lewan.bean.GatherBean;

public class FindDatasUtils {
	
	/**��ѯ�����
	 * @param limit ��ѯ����
	 * @param skip ���Բ�ѯ����
	 * @param context ������ 
	 * @param findListener ��ѯ�ɹ���ʧ�ܵĻص��ӿ�
	 * @param isReture �Ƿ��Զ�ִ�в�ѯ���� true ��ѯ false ����ѯ
	 * @param skip ���Զ�����
	 * @return query
	 */
	public static BmobQuery<GatherBean> findDatas(int skip,int limit, Context context, FindListener<GatherBean> findListener,boolean isReture){
		BmobQuery<GatherBean> query = findDatas(limit, context, findListener, false);
		query.setSkip(skip);
		//���һ
		if (isReture) {
			query.findObjects(context, findListener);
		}
		return query;
	}
	/**��ѯ�����
	 * @param limit ��ѯ����
	 * @param context ������ 
	 * @param findListener ��ѯ�ɹ���ʧ�ܵĻص��ӿ�
	 * @param isReture �Ƿ��Զ�ִ�в�ѯ���� true ��ѯ false ����ѯ
	 * @return query
	 */
	public static BmobQuery<GatherBean> findDatas(int limit, Context context, FindListener<GatherBean> findListener,boolean isReture){
		
		BmobQuery<GatherBean> query = new BmobQuery<>();
		query.setLimit(limit);
		query.order("-createdAt");//��ʱ�䵹�����
		//���һ
		if (isReture) {
			query.findObjects(context, findListener);
		}
		return query;
	}
}
