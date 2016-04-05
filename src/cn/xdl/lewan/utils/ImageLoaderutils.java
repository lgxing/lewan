package cn.xdl.lewan.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import cn.xdl.lewan.R;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageLoaderutils {
	private static ImageLoader loader;
	private static ImageLoaderConfiguration.Builder confbuilder;
	private static ImageLoaderConfiguration conf;
	private static DisplayImageOptions.Builder optbuilder;
	private static DisplayImageOptions opt;

	// ����ע��ŵ�imageloader����
	@SuppressWarnings("deprecation")
	public static ImageLoader getInstance(Context context) {

		loader = ImageLoader.getInstance();
		confbuilder = new ImageLoaderConfiguration.Builder(context);
		File file = new File(Environment.getExternalStorageDirectory(), "lewan/imageloader");
		// �ƶ�sdcard�����·��
		confbuilder.discCache(new UnlimitedDiscCache(file));
		// �����ͼƬ����
		confbuilder.discCacheFileCount(100);
		// ������������
		confbuilder.discCacheSize(1024 * 1024 * 10 * 10);
		conf = confbuilder.build();
		loader.init(conf);
		return loader;
	}

	
	/**����һ�����ڼ����û�ͷ���Ĭ����ʧ��ͼƬ
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getOpt() {
		optbuilder = new DisplayImageOptions.Builder();
		// uri ����Ĭ��ͼƬ
		optbuilder.showImageForEmptyUri(R.drawable.mruicon);
		// ͼƬ��ȡʧ�� ���ص�Ĭ��ͼƬ
		optbuilder.showImageOnFail(R.drawable.mruicon);
		optbuilder.cacheInMemory(true);// ���ڴ滺��
		optbuilder.cacheOnDisc(true);// ��Ӳ�̻���
		opt = optbuilder.build();
		return opt;
	}
	/**���ػͼƬ��Ĭ�������ʧ�ܵ�ͼƬ
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getOpt2() {
		optbuilder = new DisplayImageOptions.Builder();
		// uri ����Ĭ��ͼƬ
		optbuilder.showImageForEmptyUri(R.drawable.jiazaipng);
		// ͼƬ��ȡʧ�� ���ص�Ĭ��ͼƬ
		optbuilder.showImageOnFail(R.drawable.errorpng);
		optbuilder.cacheInMemory(true);// ���ڴ滺��
		optbuilder.cacheOnDisc(true);// ��Ӳ�̻���
		opt = optbuilder.build();
		return opt;
	}
}
