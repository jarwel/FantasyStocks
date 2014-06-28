package com.fantasystocks.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Player")
public class Player extends ParseObject {

	public ParseUser getUser() {
		return getParseUser("user");
	}

	public void setUser(ParseUser user) {
		put("user", user);
	}

	public Pool getPool() {
		return (Pool) getParseObject("pool");
	}

	public void setPool(Pool pool) {
		put("pool", pool);
	}

	public double getCash() {
		return getDouble("cash");
	}

	public void setCash(double cash) {
		put("cash", cash);
	}

	public void addLot(Lot lot) {
		getRelation("lots").add(lot);
	}

}
