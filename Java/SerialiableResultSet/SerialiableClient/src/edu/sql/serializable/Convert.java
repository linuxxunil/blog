package edu.sql.serializable;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


/**
 * Result -- Convert --> DeliverResultSet  
 * DeliverResultSet --> Convert --> SerializableResultSet
 * 
 * @author jesse
 *
 */

public class Convert {
	public static SerializableResultSet 
					toSerializableResultSet(DeliverResultSet deliver) {
		if ( deliver == null ) return null;
		return new SerializableResultSet(deliver.getList());
	}
	
	public static DeliverResultSet
					toDeliverResultSet(ResultSet rs) {
		if ( rs == null ) {
			return null;
		}
		try {
			List<Hashtable> list = new ArrayList<Hashtable>();
			int i = 0;
			ResultSetMetaData meta = rs.getMetaData();
			Hashtable<String,String> tb = new Hashtable<String,String>();
			String rowName,rowVal;
			
			while (rs.next()) {
				for ( int j=1; j<=meta.getColumnCount(); j++ ){
					rowName = meta.getColumnName(j);
					rowVal  = rs.getString(meta.getColumnName(j));
					tb.put(rowName,rowVal==null?"null":rowVal);
				}
				list.add(tb);
			}
			rs.close();
			return new DeliverResultSet(list);
		} catch (Exception e) {
			e.getStackTrace();
		}	
		return null;
	}
}
