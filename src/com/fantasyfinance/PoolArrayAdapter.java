package com.fantasyfinance;

import java.util.List;

import com.fantasyfinance.model.Pool;

import android.content.Context;
import android.widget.ArrayAdapter;

public class PoolArrayAdapter extends ArrayAdapter<Pool> {

	public PoolArrayAdapter(Context context, List<Pool> pools) {
		super(context, android.R.layout.simple_list_item_1, pools);
	}
}
