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

	// 返回注册号的imageloader对象
	@SuppressWarnings("deprecation")
	public static ImageLoader getInstance(Context context) {

		loader = ImageLoader.getInstance();
		confbuilder = new ImageLoaderConfiguration.Builder(context);
		File file = new File(Environment.getExternalStorageDirectory(), "lewan/imageloader");
		// 制定sdcard缓存的路径
		confbuilder.discCache(new UnlimitedDiscCache(file));
		// 缓存的图片个数
		confbuilder.discCacheFileCount(100);
		// 缓存的最大容量
		confbuilder.discCacheSize(1024 * 1024 * 10 * 10);
		conf = confbuilder.build();
		loader.init(conf);
		return loader;
	}

	
	/**返回一个用于加载用户头像的默认与失败图片
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getOpt() {
		optbuilder = new DisplayImageOptions.Builder();
		// uri 加载默认图片
		optbuilder.showImageForEmptyUri(R.drawable.mruicon);
		// 图片获取失败 加载的默认图片
		optbuilder.showImageOnFail(R.drawable.mruicon);
		optbuilder.cacheInMemory(true);// 做内存缓存
		optbuilder.cacheOnDisc(true);// 做硬盘缓存
		opt = optbuilder.build();
		return opt;
	}
	/**返回活动图片的默认与加载失败的图片
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getOpt2() {
		optbuilder = new DisplayImageOptions.Builder();
		// uri 加载默认图片
		optbuilder.showImageForEmptyUri(R.drawable.jiazaipng);
		// 图片获取失败 加载的默认图片
		optbuilder.showImageOnFail(R.drawable.errorpng);
		optbuilder.cacheInMemory(true);// 做内存缓存
		optbuilder.cacheOnDisc(true);// 做硬盘缓存
		opt = optbuilder.build();
		return opt;
	}
}
