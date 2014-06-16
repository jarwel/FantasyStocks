package com.fantasystocks.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Pool")
public class Pool extends ParseObject {

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}

	public double getFunds() {
		return getDouble("funds");
	}

	public void setFunds(double funds) {
		put("funds", funds);
	}

	@Override
	public String toString() {
		return getName();
	}

}
