package com.askaround.mobile.android.activity;

import java.util.List;

import com.askaround.mobile.android.R;
import com.askaround.mobile.android.adapter.MyPostsAdapter;
import com.askaround.mobile.android.app.DataManager;
import com.askaround.mobile.android.data.Post;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import com.parse.ParseException;
public class MyPostsActivity extends BaseActivity implements OnClickListener { 

	private static final String TAG = "MyPosts"; 
	private PullToRefreshListView mPullRefreshListView;
	private MyPostsAdapter myPostsAdapter;
	private View footerView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_posts);
		setupListView();
		queryData();
	}

	protected void queryData() {
		ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
		query.whereEqualTo("author", ParseUser.getCurrentUser());
		query.findInBackground(new FindCallback<Post>() {
		    public void done(List<Post> myPostList, ParseException e) {
		        if (e == null) {
		            Log.d(TAG, "Retrieved " + myPostList.size() );
		            onFinishedLoadingData(myPostList);
		           
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }
		});
	}
	
	protected void setupListView() {

		// get the list view
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		// Set a listener to be invoked when the list should be refreshed.
		//mPullRefreshListView
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				//TODO on refresh
				System.out.println("on ptr");
				
			}
		});

		// Add an end-of-list listener
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				//Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
				//mPullRefreshListView.onRefreshComplete();
				System.out.println("on bottom ptr");
				
			}
		});

		ListView myPostsListView = mPullRefreshListView.getRefreshableView();

		footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spinner_footer_layout, null, false);
		myPostsListView.addFooterView(footerView);
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(myPostsListView);
	
		
		//postFeedListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		myPostsAdapter = new MyPostsAdapter(this);
		myPostsListView.setAdapter(myPostsAdapter);
	}
		
	protected void onFinishedLoadingData (List<Post> list) {
		 DataManager.from(getApplicationContext()).setMyPosts(list, false);
         myPostsAdapter.notifyDataSetChanged();
         mPullRefreshListView.getRefreshableView().removeFooterView(footerView);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	
}

