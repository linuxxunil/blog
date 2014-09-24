package edu.sql.serializable;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

public class DeliverResultSet implements Serializable{
	private List<Hashtable> list = null;
	DeliverResultSet( List<Hashtable> list) {
		this.list= list;
	}
	public List<Hashtable> getList() {
		return list;
	}
}
