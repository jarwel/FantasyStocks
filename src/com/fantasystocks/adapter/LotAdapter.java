package com.fantasystocks.adapter;

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

public class LotAdapter extends ArrayAdapter<Lot> {

	public LotAdapter(Context context) {
		super(context, R.layout.item_lot, Lists.<Lot> newArrayList());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lot, parent, false);
		}

		TextView tvLotSymbol = (TextView) convertView.findViewById(R.id.tvLotSymbol);
		TextView tvLotPercentChange = (TextView) convertView.findViewById(R.id.tvLotPercentChange);
		TextView tvLotValueChange = (TextView) convertView.findViewById(R.id.tvLotValueChange);
		TextView tvLotValue = (TextView) convertView.findViewById(R.id.tvLotValue);

		Lot lot = getItem(position);
		tvLotSymbol.setText(lot.getSymbol());
		tvLotPercentChange.setText("--");
		tvLotValueChange.setText("--");
		tvLotValue.setText("--");

		populateWithQuote(lot, tvLotPercentChange, tvLotValueChange, tvLotValue);

		return convertView;
	}

	private void populateWithQuote(final Lot lot, final TextView tvLotPercentChange, final TextView tvLotValueChange, final TextView tvLotValue) {
		RestApplication.getFinanceClient().fetchQuote(lot.getSymbol(), new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Quote quote = Quote.fromJSONObject(response);
				if (quote != null) {
					double currentValue = quote.getPrice() * lot.getShares();
					double priceChange = currentValue - lot.getCostBasis();
					double percentChange = priceChange / lot.getCostBasis();
					tvLotPercentChange.setText(RestApplication.getFormatter().formatPercent(percentChange));
					tvLotValueChange.setText(RestApplication.getFormatter().formatChange(priceChange));
					tvLotValue.setText(RestApplication.getFormatter().formatCurrency(currentValue));

					tvLotPercentChange.setTextColor(RestApplication.getFormatter().getColorResource(priceChange));
					tvLotValueChange.setTextColor(RestApplication.getFormatter().getColorResource(priceChange));
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
