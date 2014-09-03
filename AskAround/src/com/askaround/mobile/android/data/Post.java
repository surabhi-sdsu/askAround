package com.askaround.mobile.android.data;


import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/*
 * An extension of ParseObject that makes
 * it more convenient to access information
 * about a given Post 
 */

@ParseClassName("Post")
public class Post extends ParseObject {

	public Post() {
		// A default constructor is required.
	}

	public String getTitle() {
		return getString("title");
	}

	public void setTitle(String title) {
		put("title", title);
	}

	public ParseUser getAuthor() {
		return getParseUser("author");
	}

	public void setAuthor(ParseUser user) {
		put("author", user);
	}
	
	public ParseFile getPhotoFile1() {
		return getParseFile("photo1");
	}

	public void setPhotoFile1(ParseFile file) {
		put("photo1", file);
	}
	
	public ParseFile getPhotoFile2() {
		return getParseFile("photo2");
	}

	public void setPhotoFile2(ParseFile file) {
		put("photo2", file);
	}
	
	public int getLikesOnPhoto1() {
		return getInt("likes1");
	}

	public void setLikesOnPhoto1(Integer count) {
		put("likes1", count);
	}
	
	public int getLikesOnPhoto2() {
		return getInt("likes2");
	}

	public void setLikesOnPhoto2(Integer count) {
		put("likes2", count);
	}

	public boolean isActive() {
		return getBoolean("active");
	}
	
	public void setActive(boolean active) {
		put("active", active);
	}

	public void setFlagCount() {
		put("flagCount", getInt("flagCount") + 1 );
	}
}
