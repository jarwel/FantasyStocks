package com.fantasystocks.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantasystocks.R;
import com.fantasystocks.model.Player;
import com.fantasystocks.model.Pool;

public class HomeAdapter extends ArrayAdapter<Player> {
	Context context;
	public HomeAdapter(Context context, List<Player> players) {
		super(context, 0, players);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Player player = getItem(position);
		Pool pool = player.getPool();
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_home, parent, false);
		}

		ImageView ivPoolImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
		TextView tvPoolTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
		TextView tvPlayerRank = (TextView) convertView.findViewById(R.id.tvSubTitleTop);
		TextView tvPlayerNetGain = (TextView) convertView.findViewById(R.id.tvSubTitleBottom);

		int photoMediaUrl = context.getResources().getIdentifier(pool.getPoolImageUrl(), "drawable", context.getPackageName());
		ivPoolImage.setImageResource(photoMediaUrl);

		tvPoolTitle.setText(pool.getName());
		tvPlayerRank.setText(pool.getRank(null));
		tvPlayerNetGain.setText(pool.getGain(null));
		return convertView;
	}
}
