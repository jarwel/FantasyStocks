package com.fantasystocks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;

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

		ImageView ivPoolImage = ButterKnife.findById(convertView, R.id.ivItemImage);
		TextView tvPoolTitle = ButterKnife.findById(convertView, R.id.tvItemTitle);
		TextView tvOpenSlots = ButterKnife.findById(convertView, R.id.tvSubTitleTop);
		TextView tvTotalSlots = ButterKnife.findById(convertView, R.id.tvSubTitleBottom);

		Pool pool = getItem(position);
		int photoMediaUrl = getContext().getResources().getIdentifier(pool.getPoolImageUrl(), "drawable", getContext().getPackageName());
		ivPoolImage.setImageResource(photoMediaUrl);

		tvPoolTitle.setText(pool.getName());
		tvTotalSlots.setText("Total: " + pool.getPlayerLimit());

		tvOpenSlots.setText(String.format("Open: %d", pool.getOpenCount()));
		if (pool.getOpenCount() < 2) {
			tvOpenSlots.setTextColor(getContext().getResources().getColor(R.color.text_red));
		} else {
			tvOpenSlots.setTextColor(getContext().getResources().getColor(android.R.color.black));
		}

		return convertView;
	}
}
