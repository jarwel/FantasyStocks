package com.fantasystocks.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantasystocks.R;
import com.fantasystocks.model.Portfolio;
import com.fantasystocks.model.Pool;

public class HomeAdapter extends ArrayAdapter<Portfolio> {
	Context context;

	public HomeAdapter(Context context, List<Portfolio> portfolios) {
		super(context, 0, portfolios);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Portfolio portfolio = getItem(position);
		Pool pool = portfolio.getPool();
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_custom, parent, false);
		}

		ImageView ivPoolImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
		TextView tvPoolTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
		TextView tvPortfolioRank = (TextView) convertView.findViewById(R.id.tvSubTitleTop);
		TextView tvPortfolioNetGain = (TextView) convertView.findViewById(R.id.tvSubTitleBottom);
		ImageView ivGainArrow = (ImageView) convertView.findViewById(R.id.ivGainArrow);

		tvPoolTitle.setText(pool.getName());

		tvPortfolioRank.setText(pool.getRank(null));
		tvPortfolioRank.setTypeface(null, Typeface.BOLD);

		int photoMediaUrl = context.getResources().getIdentifier(pool.getPoolImageUrl(), "drawable", context.getPackageName());
		ivPoolImage.setImageResource(photoMediaUrl);

		String netGain = pool.getGain(null);
		tvPortfolioNetGain.setText(netGain);
		if (netGain.substring(0, 1).equals("+")) {
			ivGainArrow.setImageResource(R.drawable.icon_arrow_green);
			tvPortfolioNetGain.setTextColor(Color.parseColor("#009900"));
		} else if (netGain.substring(0, 1).equals("-")) {
			ivGainArrow.setImageResource(R.drawable.icon_arrow_red);
			tvPortfolioNetGain.setTextColor(Color.parseColor("#990000"));
		} else {
			tvPortfolioNetGain.setTextColor(Color.parseColor("#0e5878"));
		}

		return convertView;
	}
}
