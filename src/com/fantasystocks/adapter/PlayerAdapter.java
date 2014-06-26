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
import com.fantasystocks.model.Player;

public class PlayerAdapter extends ArrayAdapter<Player> {
	Context context;

	public PlayerAdapter(Context context, List<Player> players) {
		super(context, R.layout.item_player, players);
		this.context = context;
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
		Log.d("d", player.getUser().getString("name") + "");
		Log.d("d", player.getUser().getString("imageUrl") + "");
		String imageResourceUrl = player.getUser().getString("imageUrl") == null ? "pool_img_4" : player.getUser().getString("imageUrl");
		int photoMediaUrl = context.getResources().getIdentifier(imageResourceUrl, "drawable", context.getPackageName());
		ivPlayerImage.setImageResource(photoMediaUrl);
		// tvPlayerRank.setText(pool.getRank(player));
		tvPlayerName.setText(player.getUser() != null ? player.getUser().getUsername() : "Saumitra");
		// tvGain.setText(pool.getGain(player));
		return convertView;
	}
}
