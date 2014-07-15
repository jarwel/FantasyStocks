package com.fantasystocks.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

import com.fantasystocks.R;

public class FinanceFormat {

	private final DateFormat dateFullFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
	private final DateFormat dateShortFormat = new SimpleDateFormat("MMM d", Locale.getDefault());
	private final DecimalFormat currencyFormat = new DecimalFormat("$###,###,###,###,##0.00");
	private final DecimalFormat changeFormat = new DecimalFormat("+$###,###,###,###,##0.00;-$#");
	private final DecimalFormat percentFormat = new DecimalFormat("+###,###,###,###,##0.00%;-#%");
	private final int redColorResource;
	private final int greenColorResource;
	private final int blueColorResource;

	private static FinanceFormat instance;

	private FinanceFormat(Context context) {
		redColorResource = context.getResources().getColor(R.color.text_red);
		greenColorResource = context.getResources().getColor(R.color.text_green);
		blueColorResource = context.getResources().getColor(R.color.text_blue);
	}

	public static FinanceFormat getInstance(Context context) {
		if (instance == null) {
			instance = new FinanceFormat(context.getApplicationContext());
		}
		return instance;
	}

	public String formatFullDate(Date date) {
		return dateFullFormat.format(date);
	}

	public String formatShortDate(Date date) {
		return dateShortFormat.format(date);
	}

	public String formatCurrency(double value) {
		return currencyFormat.format(value);
	}

	public String formatChange(double value) {
		return changeFormat.format(value);
	}

	public String formatPercent(double value) {
		return percentFormat.format(value);
	}

	public String formatRank(int value) {
		switch (value) {
		case 1:
			return "1st";
		case 2:
			return "2nd";
		case 3:
			return "3rd";
		default:
			return String.format(Locale.getDefault(), "%dth", value);
		}
	}

	public int getColorResource(double value) {
		if (value > 0) {
			return greenColorResource;
		}
		if (value < 0) {
			return redColorResource;
		}
		return blueColorResource;
	}

	public int getImageResource(double value) {
		if (value > 0) {
			return R.drawable.icon_arrow_green;
		}
		if (value < 0) {
			return R.drawable.icon_arrow_red;
		}
		return android.R.color.transparent;
	}
}
