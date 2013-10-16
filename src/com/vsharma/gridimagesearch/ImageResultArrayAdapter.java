package com.vsharma.gridimagesearch;

import java.util.List;

import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
		super(context, R.layout.item_image_result, images);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult imageResult = this.getItem(position);
		SmartImageView smartImageView = null;
		if(convertView == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(getContext());
			convertView = (View) layoutInflater.inflate(R.layout.item_image_result, parent, false);
		   //smartImageView = (SmartImageView) layoutInflater.inflate(R.layout.item_image_result, parent, false);
		} else {
			//smartImageView = (SmartImageView) convertView;
			//smartImageView.setImageResource(android.R.color.transparent);
		}
		TextView tvImage = (TextView) convertView.findViewById(R.id.tvImage);
		smartImageView = (SmartImageView) convertView.findViewById(R.id.svImage);
		smartImageView.setImageResource(android.R.color.transparent);
		smartImageView.setImageUrl(imageResult.getThumbUrl());
		String text = imageResult.getTitle();
		if(text.length() > 10) 
			text = text.substring(0,10)+"...";
		tvImage.setText(text);
		return convertView;
	}

}
