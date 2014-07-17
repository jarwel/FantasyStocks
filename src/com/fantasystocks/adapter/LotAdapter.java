package com.fantasystocks.adapter;

import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fantasystocks.R;
import com.fantasystocks.RestApplication;
import com.fantasystocks.model.Lot;
import com.fantasystocks.model.Quote;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class LotAdapter extends ArrayAdapter<Lot> {

	private Map<String, Quote> quotes;

	public LotAdapter(Context context) {
		super(context, R.layout.item_lot, Lists.<Lot> newArrayList());
		quotes = Maps.newHashMap();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lot, parent, false);
		}

		TextView tvLotSymbol = (TextView) convertView.findViewById(R.id.tvLotSymbol);
		TextView tvLotShares = (TextView) convertView.findViewById(R.id.tvLotShares);
		TextView tvLotValue = (TextView) convertView.findViewById(R.id.tvLotValue);
		TextView tvLotChange = (TextView) convertView.findViewById(R.id.tvLotChange);

		Lot lot = getItem(position);
		tvLotSymbol.setText(lot.getSymbol());
		tvLotShares.setText(String.format("%d Shares", lot.getShares()));

		Quote quote = quotes.get(lot.getSymbol());
		if (quote == null) {
			tvLotValue.setText("--");
			tvLotChange.setText("--");
			tvLotChange.setTextColor(getContext().getResources().getColor(android.R.color.black));
			fetchQuote(lot.getSymbol());
		} else {
			double currentValue = quote.getPrice() * lot.getShares();
			double priceChange = currentValue - lot.getCostBasis();
			double percentChange = priceChange / lot.getCostBasis();

			String priceChangeFormat = RestApplication.getFormatter().formatChange(priceChange);
			String percentChangeFormat = RestApplication.getFormatter().formatPercent(percentChange);

			tvLotValue.setText(RestApplication.getFormatter().formatCurrency(currentValue));
			tvLotChange.setText(String.format("%s (%s)", priceChangeFormat, percentChangeFormat));
			tvLotChange.setTextColor(RestApplication.getFormatter().getColorResource(priceChange));
		}

		return convertView;
	}

	private void fetchQuote(final String symbol) {
		RestApplication.getFinanceClient().fetchQuote(symbol, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Quote quote = Quote.fromJSONObject(response);
				if (quote != null) {
					quotes.put(symbol, quote);
					notifyDataSetChanged();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(getClass().getName(), String.format("error fetching quote for symbols"));
			}
		});
	}
}
