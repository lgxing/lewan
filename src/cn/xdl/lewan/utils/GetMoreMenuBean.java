package cn.xdl.lewan.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import cn.xdl.lewan.R;
import cn.xdl.lewan.bean.MoreBean;

/**得到更多界面的选项
 * @author lgx
 *
 */
public class GetMoreMenuBean {
	
	public static List<MoreBean> getMenuIcon(Context context){
		
		//网络访问获取选项与文字的操作
		List<MoreBean> beans = new ArrayList<>();
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_zhoubian), "周边"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_shaoer), "少儿"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_diy), "DIY"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_jianshen), "健身"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_jishi), "集市"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_yanchu), "演出"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_zhanlan), "展览"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_shalong), "沙龙"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_pincha), "品茶"));
		beans.add(new MoreBean(BitmapFactory.decodeResource(context.getResources(), R.drawable.more_juhui), "聚会"));
		
		return beans;
		
	}
	
	public static List<String> getMenuText(){
		
		List<String> beans = new ArrayList<>();
		
		beans.add("周边");
		beans.add("少儿");
		beans.add("DIY");
		beans.add("健身");
		beans.add("集市");
		beans.add("演出");
		beans.add("展览");
		beans.add("沙龙");
		beans.add("品茶");
		beans.add("聚会");
		
		return beans;
		
	}
}
