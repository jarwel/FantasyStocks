package com.fantasystocks.adapter;

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
import com.google.common.collect.Lists;

public class PoolAdapter extends ArrayAdapter<Pool> {

	public PoolAdapter(Context context) {
		super(context, R.layout.item_pool, Lists.<Pool> newArrayList());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_custom, parent, false);
		}

		ImageView ivPoolImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
		TextView tvPoolTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
		TextView tvAvailableSlots = (TextView) convertView.findViewById(R.id.tvSubTitleTop);
		TextView tvTotalSlots = (TextView) convertView.findViewById(R.id.tvSubTitleBottom);
		tvAvailableSlots.setTextColor(Color.parseColor("#FF0000"));

		Pool pool = getItem(position);
		int photoMediaUrl = getContext().getResources().getIdentifier(pool.getPoolImageUrl(), "drawable", getContext().getPackageName());
		ivPoolImage.setImageResource(photoMediaUrl);

		tvPoolTitle.setText(pool.getName());
		tvTotalSlots.setText("Total: " + pool.getPlayerLimit());
		tvAvailableSlots.setText("Open: " + new Random().nextInt(8) + "");
		return convertView;
	}
}
