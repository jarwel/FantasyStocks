package com.fantasystocks.adapter;

import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fantasystocks.R;
import com.fantasystocks.RestApplication;
import com.fantasystocks.model.Lot;
import com.fantasystocks.model.Pool;
import com.fantasystocks.model.Portfolio;
import com.fantasystocks.model.Quote;
import com.google.common.collect.Lists;

public class HomeAdapter extends ArrayAdapter<Portfolio> {

	public HomeAdapter(Context context) {
		super(context, R.layout.item_custom, Lists.<Portfolio> newArrayList());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_custom, parent, false);
		}

		ImageView ivPoolImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
		ImageView ivGainArrow = (ImageView) convertView.findViewById(R.id.ivGainArrow);
		TextView tvPoolTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
		TextView tvPortfolioRank = (TextView) convertView.findViewById(R.id.tvSubTitleTop);
		TextView tvPortfolioNetGain = (TextView) convertView.findViewById(R.id.tvSubTitleBottom);

		Portfolio portfolio = getItem(position);
		Pool pool = portfolio.getPool();

		tvPoolTitle.setText(pool.getName());
		tvPortfolioRank.setText(pool.getRank(null));
		tvPortfolioRank.setTypeface(null, Typeface.BOLD);

		int photoMediaUrl = getContext().getResources().getIdentifier(pool.getPoolImageUrl(), "drawable", getContext().getPackageName());
		ivPoolImage.setImageResource(photoMediaUrl);

		tvPortfolioNetGain.setText("--");
		tvPortfolioNetGain.setTextColor(getContext().getResources().getColor(android.R.color.black));
		ivGainArrow.setImageResource(android.R.color.transparent);

		populateWithQuote(portfolio, tvPortfolioNetGain, ivGainArrow);

		return convertView;
	}

	private void populateWithQuote(final Portfolio portfolio, final TextView tvPortfolioNetGain, final ImageView ivGainArrow) {
		RestApplication.getFinanceClient().fetchQuotes(portfolio.getSymbols(), new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				double currentValue = portfolio.getCash();
				Map<String, Quote> quotes = Quote.fromJSONArray(response);
				for (Lot lot : portfolio.getLots()) {
					Quote quote = quotes.get(lot.getSymbol());
					if (quote == null) {
						return;
					}
					currentValue += lot.getShares() * quote.getPrice();
				}
				double priceChange = currentValue - portfolio.getStartingFunds();
				double percentChange = priceChange / portfolio.getStartingFunds();
				tvPortfolioNetGain.setText(RestApplication.getFormatter().formatPercent(percentChange));
				tvPortfolioNetGain.setTextColor(RestApplication.getFormatter().getColorResource(priceChange));
				ivGainArrow.setImageResource(RestApplication.getFormatter().getImageResource(priceChange));
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(getClass().getName(), String.format("error fetching quote for symbols"));
			}
		});
	}

}
