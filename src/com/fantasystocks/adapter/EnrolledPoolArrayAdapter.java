package com.fantasystocks.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantasystocks.R;
import com.fantasystocks.model.Pool;
import com.squareup.picasso.Picasso;

public class EnrolledPoolArrayAdapter extends ArrayAdapter<Pool> {

	public EnrolledPoolArrayAdapter(Context context, List<Pool> pools) {
		super(context, 0, pools);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Pool pool = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_enrolled_pool, parent, false);
		}

		ImageView ivPoolImage = (ImageView) convertView.findViewById(R.id.ivPoolImage);
		TextView tvPoolTitle = (TextView) convertView.findViewById(R.id.tvPoolTitle);
		TextView tvPlayerRank = (TextView) convertView.findViewById(R.id.tvPlayerPosition);
		TextView tvPlayerNetGain = (TextView) convertView.findViewById(R.id.tvPlayerNetGain);
		
		int photoMediaUrlResource = pool.getPoolImageUrlResource();
		ivPoolImage.setImageResource(photoMediaUrlResource);
		

		tvPoolTitle.setText(pool.getName());
		tvPlayerRank.setText(pool.getRank(null));
		tvPlayerNetGain.setText(pool.getGain(null));
		return convertView;
	}
}
