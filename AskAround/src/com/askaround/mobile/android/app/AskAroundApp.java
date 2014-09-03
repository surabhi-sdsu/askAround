package com.askaround.mobile.android.app;

import android.app.Application;

import com.askaround.mobile.android.R;
import com.askaround.mobile.android.data.Post;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
public class AskAroundApp extends Application{

	public static final boolean DEBUG = true;

	@Override 
	public void onCreate() {
		super.onCreate();

		ParseObject.registerSubclass(Post.class);
		Parse.initialize(this, "E9NgoK846YbKGPzdnQ8zg2liHOiY7jgfdFOkYWhV", "RonxlxY0dJTpNyFKW2rw7k5d2aOR91ttNpxuk6JA"); 
		ParseFacebookUtils.initialize(getResources().getString(R.string.app_id));
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.memoryCache(new LruMemoryCache(20 * 1024 * 1024))
		.memoryCacheSize(20 *1024 *1024)
		.discCacheSize(50 *1024 *1024)
		.build();

		ImageLoader.getInstance().init(config);


	}

}
