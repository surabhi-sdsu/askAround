package com.askaround.mobile.android.activity;

import java.io.ByteArrayOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.askaround.mobile.android.R;
import com.askaround.mobile.android.app.AskAroundApp;
import com.askaround.mobile.android.data.Post;
import com.askaround.mobile.android.utils.Config;
import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;
import com.aviary.android.feather.library.filters.FilterLoaderFactory;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreateActivity extends BaseActivity implements OnClickListener {

	private static final int SAMPLE_SIZE = 8;
	private static final int REQ_CODE_PICK_IMAGE1 = 101;
	private static final int REQ_CODE_PICK_IMAGE2 = 102;
	private static final int REQUEST_CODE_AVIARY1 = 103;
	private static final int REQUEST_CODE_AVIARY2 = 104;
	EditText title;
	private Bitmap imageBitmap1, imageBitmap2;
	private Uri imageUri1, imageUri2;
	ImageView image1, image2; 
	Post post;
//	private AmazonS3Client s3Client = new AmazonS3Client(
//			new BasicAWSCredentials(Config.ACCESS_KEY_ID,
//					Config.SECRET_KEY));
	private static final String TAG = "CreateActivity"; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);

		post = new Post();
		image1 = (ImageView) findViewById(R.id.create_image1);
		image2 = (ImageView) findViewById(R.id.create_image2);
		image1.setOnClickListener(this);
		image2.setOnClickListener(this);
		title = (EditText) findViewById(R.id.create_title);
	}

	protected void onImage1() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		photoPickerIntent.setType("image/*");
		photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
		startActivityForResult(photoPickerIntent, REQ_CODE_PICK_IMAGE1);    
	}

	protected void onImage2() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		photoPickerIntent.setType("image/*");
		photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
		startActivityForResult(photoPickerIntent, REQ_CODE_PICK_IMAGE2);  
	}

	protected void onPhotoSelected1(Uri uri) {
		startActivityForResult( createAviaryIntent(uri), REQUEST_CODE_AVIARY1 ); 
	}

	protected void onPhotoSelected2(Uri uri) {
		startActivityForResult( createAviaryIntent(uri), REQUEST_CODE_AVIARY2 ); 
	}

	protected Intent createAviaryIntent (Uri uri) {
		Intent newIntent = new Intent( this, FeatherActivity.class );
		newIntent.setData(uri);
		newIntent.putExtra( Constants.EXTRA_OUTPUT_FORMAT, Bitmap.CompressFormat.JPEG.name() );
		newIntent.putExtra( Constants.EXTRA_OUTPUT_QUALITY, 100 );

		newIntent.putExtra( "tools-list", new String[] { 
				//FilterLoaderFactory.Filters.ENHANCE.name(),
				FilterLoaderFactory.Filters.EFFECTS.name(), 
				//FilterLoaderFactory.Filters.BORDERS.name(), 
				//FilterLoaderFactory.Filters.STICKERS.name(),
				FilterLoaderFactory.Filters.CROP.name(), 
				FilterLoaderFactory.Filters.TILT_SHIFT.name(),
				//FilterLoaderFactory.Filters.ADJUST.name(), 
				//FilterLoaderFactory.Filters.BRIGHTNESS.name(), 
				//FilterLoaderFactory.Filters.CONTRAST.name(), 
				//FilterLoaderFactory.Filters.SATURATION.name(), 
				//FilterLoaderFactory.Filters.COLORTEMP.name(),
				//FilterLoaderFactory.Filters.SHARPNESS.name(), 
				//FilterLoaderFactory.Filters.COLOR_SPLASH.name(),
				FilterLoaderFactory.Filters.DRAWING.name(), 
				FilterLoaderFactory.Filters.TEXT.name(), 
				//FilterLoaderFactory.Filters.RED_EYE.name(), 
				//FilterLoaderFactory.Filters.WHITEN.name(), 
				//FilterLoaderFactory.Filters.BLEMISH.name(),
				FilterLoaderFactory.Filters.MEME.name(),
		} );

		newIntent.putExtra( Constants.EXTRA_STICKERS_ENABLE_EXTERNAL_PACKS, false );
		return newIntent;
	}

	@SuppressLint("NewApi")
	protected void onPublish() {


		System.out.println("imag1 " + imageBitmap1.getByteCount() + " w " + imageBitmap1.getWidth() + " h " + imageBitmap1.getHeight());
		System.out.println("imag2 " + imageBitmap2.getByteCount() + " w " + imageBitmap2.getWidth() + " h " + imageBitmap2.getHeight());
		//save file 1
		ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
		imageBitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
		byte[] byteArray1 = stream1.toByteArray();

		ParseFile file1 = new ParseFile("image1.jpeg", byteArray1);

		//save file 2
		ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
		imageBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
		byte[] byteArray2 = stream2.toByteArray();

		ParseFile file2 = new ParseFile("image2.jpeg", byteArray2);

		//
		post.setAuthor(ParseUser.getCurrentUser());
		post.setTitle(title.getText().toString());
		post.setPhotoFile1(file1);
		post.setPhotoFile2(file2);
		post.setActive(true);
		post.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(
							getApplicationContext(),
							"saved yay!! ",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Error saving: " + e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

		});
	}


	protected void onActivityResult(int requestCode, int resultCode, 
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

		switch(requestCode) { 
		case REQ_CODE_PICK_IMAGE1:
			if(resultCode == RESULT_OK){  
				Uri imageUri = imageReturnedIntent.getData();	
				//imageBitmap1 = decodeSampledBitmapFromFilePath(getPath(imageUri));
				onPhotoSelected1(imageUri);
			}
			break;
		case REQ_CODE_PICK_IMAGE2:
			if(resultCode == RESULT_OK){  
				Uri imageUri = imageReturnedIntent.getData();
				//imageBitmap2 = decodeSampledBitmapFromFilePath(getPath(imageUri));
				onPhotoSelected2(imageUri);
			}
			break;
		case REQUEST_CODE_AVIARY1:
			if(resultCode == RESULT_OK){
				// output image path
				Uri mImageUri = imageReturnedIntent.getData();
				Bundle extra = imageReturnedIntent.getExtras();
				if( null != extra ) {
					// image has been changed by the user?
					boolean changed = extra.getBoolean( Constants.EXTRA_OUT_BITMAP_CHANGED);
				}
				imageUri1 = mImageUri;
				imageBitmap1 = BitmapFactory.decodeFile(getPath(mImageUri));
				//imageBitmap1 = decodeSampledBitmapFromFilePath(getPath(mImageUri));
				image1.setImageBitmap(imageBitmap1);
			}
			break;
		case REQUEST_CODE_AVIARY2:
			if(resultCode == RESULT_OK){
				// output image path
				Uri mImageUri = imageReturnedIntent.getData();
				Bundle extra = imageReturnedIntent.getExtras();
				if( null != extra ) {
					// image has been changed by the user?
					boolean changed = extra.getBoolean( Constants.EXTRA_OUT_BITMAP_CHANGED);
				}
				imageUri2 = mImageUri;
				imageBitmap2 = BitmapFactory.decodeFile(getPath(mImageUri));
				//imageBitmap2 = decodeSampledBitmapFromFilePath(getPath(mImageUri));
				image2.setImageBitmap(imageBitmap2);
			}
			break;
		}
	}

	public String getPath(Uri imageUri) {
		String[] proj = { MediaColumns.DATA };
		if (imageUri.toString().startsWith(
				"content://com.android.gallery3d.provider")) {
			imageUri = Uri.parse(imageUri.toString().replace(
					"com.android.gallery3d", "com.google.android.gallery3d"));
		}
		Cursor cursor = null;
		System.out.println("filcolumn path " + proj);
		cursor = getContentResolver().query(
				imageUri, proj, null, null, null);
		cursor.moveToFirst();

		String filePath = "";
		if (imageUri.toString().startsWith(
				"content://com.google.android.gallery3d")) {
			filePath = imageUri.toString();
		} else if (imageUri.toString().startsWith(
				"content://com.google.android.apps.photos.content")) {
			filePath = imageUri.toString();
		} else {
			filePath = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaColumns.DATA));
		}
		cursor.close();
		if (AskAroundApp.DEBUG) Log.d(TAG, "filepath>> " + filePath);
		return filePath;
	}


	public static Bitmap decodeSampledBitmapFromFilePath(String filePath) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		System.out.println("wtf>>>>" + " w " +options.outWidth + " h " + options.outHeight);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 600, 600);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		if(AskAroundApp.DEBUG) Log.d(TAG, "sample size>> " + inSampleSize);
		return inSampleSize;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_create, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_done:
			onPublish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onClick(View v) {
		int vid = v.getId();

		switch (vid) {
		case R.id.create_image1:
			onImage1();
			break;
		case R.id.create_image2:
			onImage2();
			break;
		}

	}

	/**
	 * Check the external storage status
	 * 
	 * @return
	 */
	private boolean isExternalStorageAvilable() {
		String state = Environment.getExternalStorageState();
		if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
			return true;
		}
		return false;
	}
}

