package com.fantasystocks.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fantasystocks.R;
import com.fantasystocks.model.Player;
import com.fantasystocks.model.Pool;

public class PlayerArrayAdapter extends ArrayAdapter<Player> {

	private Pool pool;

	public PlayerArrayAdapter(Context context, List<Player> players, Pool pool) {
		super(context, R.layout.item_player, players);
		this.pool = pool;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Player player = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_player, parent, false);
		}

		TextView tvPlayerRank = (TextView) convertView.findViewById(R.id.tvPlayerRank);
		TextView tvPlayerName = (TextView) convertView.findViewById(R.id.tvPlayerName);
		TextView tvGain = (TextView) convertView.findViewById(R.id.tvGain);

		tvPlayerRank.setText(player.getPlayer().getUsername());
		tvPlayerName.setText(pool.getRank(player));
		tvGain.setText(pool.getGain(player));
		return convertView;
	}
}
