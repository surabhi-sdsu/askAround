package com.askaround.mobile.android.app;

import java.util.ArrayList;
import java.util.List;

import com.askaround.mobile.android.data.Post;

import android.content.Context;
import android.content.SharedPreferences;

public final class DataManager {

	public static final String TAG = "DataManager";

	private static DataManager sInstance;
	private SharedPreferences sharedPreferences; 
	public static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
	private ArrayList<Post> myPosts;
	private ArrayList<Post> posts;
	//Singleton management
		public static DataManager from(Context context) {
			if (sInstance == null) {
				sInstance = new DataManager(context);
			}

			return sInstance;
		}
		
		private DataManager(Context context) {
			sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
			myPosts = new ArrayList<Post>();
		}
		
		public boolean isFirstRun() {
			if (sharedPreferences.getBoolean("firstrun", true)) {
				sharedPreferences.edit().putBoolean("firstrun", false).commit();
				return true;
			}
			return false;
		}
		
		public void setMyPosts (List<Post> list, boolean isPage) {
			if (list != null) {
				if (isPage) {
					myPosts.addAll(list);
				} else {
					myPosts = (ArrayList<Post>) list;
				}
			}
		}
		
		public ArrayList<Post> getMyPosts() {
			return myPosts;
		}

		public void setPosts (List<Post> list, boolean isPage) {
			if (list != null) {
				if (isPage) {
					posts.addAll(list);
				} else {
					posts = (ArrayList<Post>) list;
				}
			}
		}
		
		public ArrayList<Post> getPosts() {
			return posts;
		}
}
