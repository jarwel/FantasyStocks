package com.fantasystocks.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fantasystocks.R;
import com.fantasystocks.model.Pool;

public class EnrolledPoolArrayAdapter extends ArrayAdapter<Pool> {

	public EnrolledPoolArrayAdapter(Context context, List<Pool> pools) {
		super(context, R.layout.item_enrolled_pool, pools);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Pool pool = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_enrolled_pool, parent, false);
		}

		TextView tvPoolName = (TextView) convertView.findViewById(R.id.tvPlayerRank);
		TextView tvPoolRank = (TextView) convertView.findViewById(R.id.tvPoolRank);
		TextView tvPoolGain = (TextView) convertView.findViewById(R.id.tvPoolGain);

		tvPoolName.setText(pool.getName());
		tvPoolRank.setText(pool.getRank(null));
		tvPoolGain.setText(pool.getGain(null));
		return convertView;
	}
}
