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
import com.fantasystocks.model.Pool;
import com.fantasystocks.model.Portfolio;
import com.fantasystocks.model.Quote;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class HomeAdapter extends ArrayAdapter<Portfolio> {

	private Map<String, Quote> quotes;

	public HomeAdapter(Context context) {
		super(context, R.layout.item_custom, Lists.<Portfolio> newArrayList());
		quotes = Maps.newHashMap();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_custom, parent, false);
		}

		ImageView ivPoolImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
		ImageView ivGainArrow = (ImageView) convertView.findViewById(R.id.ivGainArrow);
		TextView tvTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
		TextView tvValueChange = (TextView) convertView.findViewById(R.id.tvSubTitleTop);
		TextView tvPercentChange = (TextView) convertView.findViewById(R.id.tvSubTitleBottom);

		Portfolio portfolio = getItem(position);
		Pool pool = portfolio.getPool();

		tvTitle.setText(pool.getName());

		int photoMediaUrl = getContext().getResources().getIdentifier(pool.getPoolImageUrl(), "drawable", getContext().getPackageName());
		ivPoolImage.setImageResource(photoMediaUrl);

		if (!quotes.keySet().containsAll(portfolio.getSymbols())) {
			tvValueChange.setText("--");
			tvValueChange.setTextColor(getContext().getResources().getColor(android.R.color.black));
			tvPercentChange.setText("--");
			tvPercentChange.setTextColor(getContext().getResources().getColor(android.R.color.black));
			ivGainArrow.setImageResource(android.R.color.transparent);
			fetchQuotes(portfolio.getSymbols());
		} else {
			double currentValue = portfolio.getCurrentValue(quotes);
			double priceChange = currentValue - portfolio.getStartingFunds();
			double percentChange = priceChange / portfolio.getStartingFunds();

			tvValueChange.setText(RestApplication.getFormatter().formatChange(priceChange));
			tvValueChange.setTextColor(RestApplication.getFormatter().getColorResource(priceChange));
			tvPercentChange.setText(RestApplication.getFormatter().formatPercent(percentChange));
			tvPercentChange.setTextColor(RestApplication.getFormatter().getColorResource(priceChange));
			ivGainArrow.setImageResource(RestApplication.getFormatter().getImageResource(priceChange));
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
