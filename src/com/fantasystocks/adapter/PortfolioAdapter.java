package com.fantasystocks.adapter;

import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import android.content.Context;
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
import com.fantasystocks.model.Portfolio;
import com.fantasystocks.model.Quote;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PortfolioAdapter extends ArrayAdapter<Portfolio> {

	private Map<String, Quote> quotes;

	public PortfolioAdapter(Context context) {
		super(context, R.layout.item_portfolio, Lists.<Portfolio> newArrayList());
		quotes = Maps.newHashMap();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_custom, parent, false);
		}

		ImageView ivPortfolioImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
		TextView tvPortfolioName = (TextView) convertView.findViewById(R.id.tvItemTitle);
		TextView tvPortfolioRank = (TextView) convertView.findViewById(R.id.tvSubTitleTop);
		TextView tvPortfolioChange = (TextView) convertView.findViewById(R.id.tvSubTitleBottom);

		Portfolio portfolio = getItem(position);
		tvPortfolioName.setText(portfolio.getUser().getString("name"));
		tvPortfolioRank.setText(RestApplication.getFormatter().formatRank(position + 1));

		String imageResourceUrl = portfolio.getUser().getString("imageUrl") == null ? "pool_img_4" : portfolio.getUser().getString("imageUrl");
		int photoMediaUrl = getContext().getResources().getIdentifier(imageResourceUrl, "drawable", getContext().getPackageName());
		ivPortfolioImage.setImageResource(photoMediaUrl);

		if (!quotes.keySet().containsAll(portfolio.getSymbols())) {
			tvPortfolioChange.setText("--");
			tvPortfolioChange.setTextColor(getContext().getResources().getColor(android.R.color.black));
			fetchQuotes(portfolio.getSymbols());
		} else {
			Double currentValue = portfolio.getCurrentValue(quotes);
			if (currentValue != null) {
				double priceChange = currentValue - portfolio.getStartingFunds();
				double percentChange = priceChange / portfolio.getStartingFunds();
				tvPortfolioChange.setText(RestApplication.getFormatter().formatPercent(percentChange));
				tvPortfolioChange.setTextColor(RestApplication.getFormatter().getColorResource(priceChange));
			}
		}

		return convertView;
	}

	private void fetchQuotes(final Set<String> symbols) {
		RestApplication.getFinanceClient().fetchQuotes(symbols, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				quotes.putAll(Quote.fromJSONArray(response));
				notifyDataSetChanged();
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(getClass().getName(), String.format("error fetching quote for symbols"));
			}
		});
	}
}
