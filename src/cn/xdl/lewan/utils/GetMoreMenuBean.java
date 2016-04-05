package cn.xdl.lewan.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import cn.xdl.lewan.R;
import cn.xdl.lewan.bean.MoreBean;

/**�õ���������ѡ��
 * @author lgx
 *
 */
public class GetMoreMenuBean {
	
	public static List<MoreBean> getMenuIcon(Context context){
		
		//������ʻ�ȡѡ�������ֵĲ���
		List<MoreBean> beans = new ArrayList<>();
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_zhoubian), "�ܱ�"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_shaoer), "�ٶ�"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_diy), "DIY"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_jianshen), "����"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_jishi), "����"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_yanchu), "�ݳ�"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_zhanlan), "չ��"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_shalong), "ɳ��"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_pincha), "Ʒ��"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_juhui), "�ۻ�"));
		
		return beans;
		
	}
	
	public static List<String> getMenuText(){
		
		List<String> beans = new ArrayList<>();
		
		beans.add("�ܱ�");
		beans.add("�ٶ�");
		beans.add("DIY");
		beans.add("����");
		beans.add("����");
		beans.add("�ݳ�");
		beans.add("չ��");
		beans.add("ɳ��");
		beans.add("Ʒ��");
		beans.add("�ۻ�");
		
		return beans;
		
	}
}
