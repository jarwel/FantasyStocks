package com.fantasystocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fantasystocks.model.Lot;
import com.fantasystocks.model.Portfolio;

public class ViewPortfolioActivity extends Activity {

	private ListView lvLots;
	private ArrayAdapter<Lot> lotsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_portfolio);
		lvLots = (ListView) findViewById(R.id.lvLots);
		Portfolio portfolio = Portfolio.getMockPorfolio();
		lotsAdapter = new ArrayAdapter<Lot>(getBaseContext(), android.R.layout.simple_list_item_1, portfolio.getLots());
		lvLots.setAdapter(lotsAdapter);
	}

	public void onTradeClicked(View view) {
		Intent intent = new Intent(ViewPortfolioActivity.this, TradeActivity.class);
		startActivity(intent);
	}
}
