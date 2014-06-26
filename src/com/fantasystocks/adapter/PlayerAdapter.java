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

public class PlayerAdapter extends ArrayAdapter<Player> {

	public PlayerAdapter(Context context, List<Player> players) {
		super(context, R.layout.item_player, players);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Player player = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_home, parent, false);
		}

		ImageView ivPlayerImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
		TextView tvPlayerName = (TextView) convertView.findViewById(R.id.tvItemTitle);
		TextView tvPlayerRank = (TextView) convertView.findViewById(R.id.tvSubTitleTop);
		TextView tvGain = (TextView) convertView.findViewById(R.id.tvSubTitleBottom);

		// tvPlayerRank.setText(pool.getRank(player));
		tvPlayerName.setText(player.getUser() != null ? player.getUser().getUsername() : "Saumitra");
		// tvGain.setText(pool.getGain(player));
		return convertView;
	}
}
