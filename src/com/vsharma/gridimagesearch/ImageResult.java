package com.vsharma.gridimagesearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable{
	
	private static final long serialVersionUID = 7183885650037524933L;
	
	private String fullUrl;
	private String thumbUrl;
	private String title;
	
	public ImageResult(JSONObject jsonObject) {
		try {
			this.fullUrl = jsonObject.getString("url");
			this.thumbUrl = jsonObject.getString("tbUrl");
			this.title = jsonObject.getString("titleNoFormatting");
		} catch(JSONException ex) {
			ex.printStackTrace();
		}
	}
	
	public static List<ImageResult> fromJsonArray(JSONArray jsonArray) {
		List<ImageResult> imageResults = new ArrayList<ImageResult>();
		if(jsonArray != null && jsonArray.length()>0)
		for(int count=0; count<jsonArray.length(); count++) {
			try {
				JSONObject jsonObject = jsonArray.getJSONObject(count);
				ImageResult imageResult = new ImageResult(jsonObject);
				imageResults.add(imageResult);
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
		
		return imageResults;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFullUrl() {
		return fullUrl;
	}
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
}
