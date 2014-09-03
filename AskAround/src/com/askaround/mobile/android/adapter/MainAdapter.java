package com.askaround.mobile.android.adapter;

import java.util.Arrays;

import com.askaround.mobile.android.R;
import com.askaround.mobile.android.app.DataManager;
import com.askaround.mobile.android.data.Post;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter {

	LayoutInflater inflater;
	private Context context;
	DisplayImageOptions imageOptions;
	public MainAdapter(Context ctx) {
		context = ctx;
		inflater = LayoutInflater.from(context);
		imageOptions = new DisplayImageOptions.Builder()
		.showStubImage(R.color.white)
		//.showStubImage(new ColorDrawable(R.color.white)) 
		//.showImageForEmptyUri(R.drawable.ic_launcher)
		.cacheOnDisc(true)
		.displayer(new FadeInBitmapDisplayer(700))
		.cacheInMemory(true)
		.build();
	}

	@Override
	public int getCount() {
		return DataManager.from(context.getApplicationContext()).getPosts() == null ? 0 : DataManager.from(context).getPosts().size();
	}

	@Override
	public Object getItem(int position) {
		return DataManager.from(context).getPosts().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View cv = convertView;
		ViewHolder holder;
		if (convertView == null) {
			cv = inflater.inflate(R.layout.item_my_post, null);
			holder = new ViewHolder();
			holder.titleView = (TextView) cv.findViewById(R.id.my_post_title);
			holder.image1 = (ImageView) cv.findViewById(R.id.my_post_image1);
			holder.image2 = (ImageView) cv.findViewById(R.id.my_post_image2);
			cv.setTag(holder);
		} else {
			holder = (ViewHolder) cv.getTag();
		}

//		cv.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				listener.onFavoriteItemClick(v, position, DataManager.from(context).getFavoritedPosts().get(position).id);
//			}
//
//		});
		holder.titleView.setText(DataManager.from(context).getMyPosts().get(position).getTitle());
		if (DataManager.from(context).getMyPosts().get(position).getPhotoFile1() != null) {
			
			String url = DataManager.from(context).getMyPosts().get(position).getPhotoFile1().getUrl();
			ImageLoader.getInstance().displayImage(url, holder.image1, imageOptions);
		}
		if (DataManager.from(context).getMyPosts().get(position).getPhotoFile2() != null) {
			String url = DataManager.from(context).getMyPosts().get(position).getPhotoFile2().getUrl();
			ImageLoader.getInstance().displayImage(url, holder.image2, imageOptions);
		}
		//holder.viewCountView.setText(String .valueOf(DataManager.from(context).getCategoryPosts().get(position).viewCount));
		//ImageLoader.getInstance().displayImage(DataManager.from(context).getFavoritedPosts().get(position).image, holder.imageView, imageOptions);

		return cv;
	}
	
	private class ViewHolder {
		TextView titleView;
		ImageView image1;
		ImageView image2;
	}

}
