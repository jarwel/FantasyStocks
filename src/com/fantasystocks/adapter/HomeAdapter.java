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
import butterknife.ButterKnife;

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

		ImageView ivPoolImage = ButterKnife.findById(convertView, R.id.ivItemImage);
		ImageView ivGainArrow = ButterKnife.findById(convertView, R.id.ivGainArrow);
		TextView tvTitle = ButterKnife.findById(convertView, R.id.tvItemTitle);
		TextView tvValue = ButterKnife.findById(convertView, R.id.tvSubTitleTop);
		TextView tvChange = ButterKnife.findById(convertView, R.id.tvSubTitleBottom);

		Portfolio portfolio = getItem(position);
		Pool pool = portfolio.getPool();

		tvTitle.setText(pool.getName());

		int photoMediaUrl = getContext().getResources().getIdentifier(pool.getPoolImageUrl(), "drawable", getContext().getPackageName());
		ivPoolImage.setImageResource(photoMediaUrl);

		if (!quotes.keySet().containsAll(portfolio.getSymbols())) {
			tvValue.setText("--");
			tvChange.setText("--");
			tvChange.setTextColor(getContext().getResources().getColor(android.R.color.black));
			ivGainArrow.setImageResource(android.R.color.transparent);
			fetchQuotes(portfolio.getSymbols());
		} else {
			double currentValue = portfolio.getCurrentValue(quotes);
			double valueChange = currentValue - portfolio.getStartingFunds();
			double percentChange = valueChange / portfolio.getStartingFunds();

			tvValue.setText(RestApplication.getFormatter().formatCurrency(currentValue));

			String valueChangeFormat = RestApplication.getFormatter().formatChange(valueChange);
			String percentChangeFormat = RestApplication.getFormatter().formatPercent(percentChange);
			tvChange.setText(String.format("%s (%s)", valueChangeFormat, percentChangeFormat));
			tvChange.setTextColor(RestApplication.getFormatter().getColorResource(valueChange));
			ivGainArrow.setImageResource(RestApplication.getFormatter().getImageResource(valueChange));
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
