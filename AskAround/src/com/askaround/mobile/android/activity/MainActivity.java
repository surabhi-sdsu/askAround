package com.askaround.mobile.android.activity;

import java.util.List;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.askaround.mobile.android.R;
import com.askaround.mobile.android.app.AskAroundApp;
import com.askaround.mobile.android.app.DataManager;
import com.askaround.mobile.android.data.Post;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
public class MainActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = "MainActivity";
	boolean isPage;
	ImageView image1, image2;
	TextView title;
	ImageView stamp1ImageView;
	ImageView stamp2ImageView;
	TextView votes1TextView, votes2TextView;
	DisplayImageOptions imageOptions;
	private int currentIndex;
	private static final int SECTION_1 = 1;
	private static final int SECTION_2 = 2;
	RelativeLayout section1, section2;
	final Handler handler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (getIntent().getBooleanExtra("EXIT", false)) {
			finish();
		}
		imageOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.color.imageview_background)
		.cacheOnDisc(false)
		.displayer(new FadeInBitmapDisplayer(700))
		.cacheInMemory(true)
		.build();
		currentIndex = 0;
		ParseAnalytics.trackAppOpened(getIntent());
		setupUI();
		queryData();
	}

	@Override
	public void onResume() {
		super.onResume();

		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser == null) ) {
			showAuthActivity();
		}
	}

	protected void queryData() {
		ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
		query.whereEqualTo("author", ParseUser.getCurrentUser());
		query.findInBackground(new FindCallback<Post>() {
			public void done(List<Post> postList, ParseException e) {
				if (e == null) {
					Log.d(TAG, "Retrieved " + postList.size() );
					onFinishedLoadingData(postList);

				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});
	}

	protected void setupUI() {
		image1 = (ImageView) findViewById(R.id.main_image1);
		image2 = (ImageView) findViewById(R.id.main_image2);
		title = (TextView) findViewById(R.id.main_title);
		section1 = (RelativeLayout) findViewById(R.id.main_section1);
		//section1.setOnClickListener(this);
		section2 = (RelativeLayout) findViewById(R.id.main_section2);
		//section2.setOnClickListener(this);

		votes1TextView = (TextView) findViewById(R.id.main_votes_image1);
		votes2TextView = (TextView) findViewById(R.id.main_votes_image2);
		stamp1ImageView = (ImageView) findViewById(R.id.main_stamp_image1);
		stamp2ImageView = (ImageView) findViewById(R.id.main_stamp_image2);

		section1.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP){
					//Toast.makeText(getApplicationContext(), "Touch coordinates1 : " +
						//	String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()), Toast.LENGTH_SHORT).show();
					onStamped(SECTION_1, event.getX(), event.getY());
				}
				return true;
			}
		});
		section2.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP){
					//Toast.makeText(getApplicationContext(), "Touch coordinates2 : " +
					//		String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()), Toast.LENGTH_SHORT).show();
					onStamped(SECTION_2, event.getX(), event.getY());
				}
				return true;
			}
		});
	}

	protected void onFinishedLoadingData(List<Post> list) {
		DataManager.from(getApplicationContext()).setPosts(list, isPage);
		showNextPost();
	}

	protected void showNextPost() {
		stamp1ImageView.setVisibility(View.GONE);
		stamp2ImageView.setVisibility(View.GONE);
		votes2TextView.setVisibility(View.GONE);
		votes1TextView.setVisibility(View.GONE);
		//check if more data exists
		if ( currentIndex <= DataManager.from(getApplicationContext()).getPosts().size() - 1) {
			if (AskAroundApp.DEBUG) Log.d(TAG, "show next post index ->" + currentIndex);
			String image1Url = DataManager.from(getApplicationContext()).getPosts().get(currentIndex).getPhotoFile1().getUrl(); 
			String image2Url = DataManager.from(getApplicationContext()).getPosts().get(currentIndex).getPhotoFile2().getUrl(); 
			if (AskAroundApp.DEBUG) Log.d(TAG, "show images from url ->" + image1Url);
			ImageLoader.getInstance().displayImage(image1Url, image1, imageOptions);
			ImageLoader.getInstance().displayImage(image2Url, image2, imageOptions);
			title.setText(DataManager.from(getApplicationContext()).getPosts().get(currentIndex).getTitle());
		}
	}

	protected void onStamped(int section, float x, float y) {
		if (AskAroundApp.DEBUG) Log.d(TAG, "stamped ->" + section);
		if (section == SECTION_1) {
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
//			params.leftMargin = (int) x;
//			params.topMargin = (int) y;
//			params.width = LayoutParams.WRAP_CONTENT;
//			params.height = LayoutParams.WRAP_CONTENT;
//			stamp1ImageView.setLayoutParams(params);
			stamp1ImageView.setVisibility(View.VISIBLE);
			stamp1ImageView.bringToFront();
			votes1TextView.setVisibility(View.VISIBLE);
			votes1TextView.bringToFront();
		} else if (section == SECTION_2) {
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
//			params.leftMargin = (int) x;
//			params.topMargin = (int) y;
//			params.width = LayoutParams.WRAP_CONTENT;
//			params.height = LayoutParams.WRAP_CONTENT;
//			stamp2ImageView.setLayoutParams(params);
			stamp2ImageView.setVisibility(View.VISIBLE);
			stamp2ImageView.bringToFront();
			votes2TextView.setVisibility(View.VISIBLE);
			votes2TextView.bringToFront();
		}
		
		//TODO: saveLike(section);
		currentIndex++;
		
		handler.postDelayed(new Runnable() {
			  @Override
			  public void run() {
				  showNextPost();
			  }
			}, 2000);
		
	}

	protected void onLogout() {
		if (AskAroundApp.DEBUG) Log.d(TAG, "logout...");
		ParseUser.logOut();
		Intent i = new Intent(this, AuthActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}

	protected void onCreate() {
		Intent i = new Intent(this, CreateActivity.class);
		startActivity(i);
	}

	protected void showAuthActivity() {
		Intent i = new Intent(this, AuthActivity.class);
		startActivity(i);
	}

	protected void onMyPosts() {
		Intent i = new Intent(this, MyPostsActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_logout:
			onLogout();
			return true;
		case R.id.action_create:
			onCreate();
			return true;
		case R.id.action_my_posts:
			onMyPosts();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		int vid = v.getId();
		switch (vid) {
		//		case R.id.main_section1:
		//			onStamped(SECTION_1);
		//			break;
		//		case R.id.main_section2:
		//			onStamped(SECTION_2);
		//			break;
		}

	}
}
