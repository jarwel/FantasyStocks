package com.fantasystocks.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fantasystocks.R;
import com.fantasystocks.RestApplication;
import com.fantasystocks.model.Lot;
import com.fantasystocks.model.Quote;
import com.google.common.collect.Maps;

public class LotAdapter extends ArrayAdapter<Lot> {

	private Map<String, Quote> quotes;

	public LotAdapter(Context context, List<Lot> lots) {
		super(context, R.layout.item_lot, lots);
		quotes = Maps.newHashMap();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lot, parent, false);
		}

		TextView tvLotSymbol = (TextView) convertView.findViewById(R.id.tvLotSymbol);
		TextView tvLotPrice = (TextView) convertView.findViewById(R.id.tvLotPrice);
		TextView tvLotShares = (TextView) convertView.findViewById(R.id.tvLotShares);
		TextView tvLotValue = (TextView) convertView.findViewById(R.id.tvLotValue);

		Lot lot = getItem(position);
		tvLotSymbol.setText(lot.getSymbol());
		tvLotPrice.setText("--");
		tvLotShares.setText(String.valueOf(lot.getShares()));
		tvLotValue.setText("--");

		Quote quote = quotes.get(lot.getSymbol());
		if (quote != null) {
			tvLotPrice.setText(RestApplication.dollarFormat.format(quote.getPrice()));
			tvLotValue.setText(RestApplication.dollarFormat.format(quote.getPrice() * lot.getShares()));
		}

		return convertView;
	}

	public void setQuotes(Map<String, Quote> quotes) {
		this.quotes = quotes;
	}

}
