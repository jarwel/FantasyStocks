package com.fantasystocks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantasystocks.R;
import com.fantasystocks.model.Portfolio;
import com.google.common.collect.Lists;

public class PortfolioAdapter extends ArrayAdapter<Portfolio> {

	public PortfolioAdapter(Context context) {
		super(context, R.layout.item_portfolio, Lists.<Portfolio> newArrayList());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_custom, parent, false);
		}

		ImageView ivPortfolioImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
		TextView tvPortfolioName = (TextView) convertView.findViewById(R.id.tvItemTitle);
		TextView tvPortfolioRank = (TextView) convertView.findViewById(R.id.tvSubTitleTop);
		TextView tvPortfolioGain = (TextView) convertView.findViewById(R.id.tvSubTitleBottom);

		Portfolio portfolio = getItem(position);
		String imageResourceUrl = portfolio.getUser().getString("imageUrl") == null ? "pool_img_4" : portfolio.getUser().getString("imageUrl");
		int photoMediaUrl = getContext().getResources().getIdentifier(imageResourceUrl, "drawable", getContext().getPackageName());
		ivPortfolioImage.setImageResource(photoMediaUrl);
		// tvportfolioRank.setText(pool.getRank(portfolio));
		tvPortfolioName.setText(portfolio.getUser().getString("name"));
		// tvGain.setText(pool.getGain(portfolio));
		return convertView;
	}
}
