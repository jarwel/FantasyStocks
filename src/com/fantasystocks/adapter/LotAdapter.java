package com.fantasystocks.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fantasystocks.R;
import com.fantasystocks.model.Lot;

public class LotAdapter extends ArrayAdapter<Lot> {

	public LotAdapter(Context context, List<Lot> lots) {
		super(context, R.layout.item_lot, lots);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lot, parent, false);
		}

		TextView tvLotSymbol = (TextView) convertView.findViewById(R.id.tvLotSymbol);
		TextView tvLotShares = (TextView) convertView.findViewById(R.id.tvLotShares);
		TextView tvLotGain = (TextView) convertView.findViewById(R.id.tvLotGain);

		Lot lot = getItem(position);
		tvLotSymbol.setText(lot.getSymbol());
		tvLotShares.setText(String.valueOf(lot.getShares()));
		// tvLotGain.setText("");

		return convertView;
	}

}
