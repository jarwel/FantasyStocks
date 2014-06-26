package com.fantasystocks.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantasystocks.R;
import com.fantasystocks.model.Pool;

public class OpenPoolArrayAdapter extends ArrayAdapter<Pool> {

	public OpenPoolArrayAdapter(Context context, List<Pool> pools) {
		super(context, R.layout.item_open_pool, pools);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Pool pool = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_enrolled_pool, parent, false);
		}
		
		ImageView ivPoolImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
		TextView tvPoolTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
		TextView tvAvailableSlots = (TextView) convertView.findViewById(R.id.tvSubTitleTop);
		TextView tvTotalPlayerLimit = (TextView) convertView.findViewById(R.id.tvSubTitleBottom);
		tvAvailableSlots.setTextColor(Color.parseColor("#FF0000"));
		
		int photoMediaUrlResource = pool.getPoolImageUrlResource();
		ivPoolImage.setImageResource(photoMediaUrlResource);
		tvPoolTitle.setText(pool.getName());
		tvTotalPlayerLimit.setText("Total: " + pool.getPlayerLimit());
		tvAvailableSlots.setText("Open: " + new Random().nextInt(8) + "");
		return convertView;
	}
}
