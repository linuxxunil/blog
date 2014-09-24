
package edu.http;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;

import edu.sql.serializable.Convert;
import edu.sql.serializable.DeliverResultSet;
import edu.sql.serializable.SerializableResultSet;


public class Client {
	public static void main(String[] args) throws Exception {
		
		URL url = new URL("http://localhost:8080/SerialiableServer/Serialiable");
		URLConnection conn = url.openConnection();
		HttpURLConnection httpConn  = (HttpURLConnection)conn;
		InputStream im = null;
		int status;
		
		/* Setting HTTP Header */
		httpConn.setRequestMethod("GET");  //POST 的重要參數
		httpConn.setRequestProperty("Connection", "Keep-Alive" ) ;
		httpConn.setRequestProperty("Cache-Control", "no-cache" ) ;   
		httpConn.setRequestProperty("Content-Type", "application/octet-stream; charset=utf-8");

		httpConn.setDoOutput(true); 
		httpConn.setDoInput(true); 
		
		/* Connect */
		httpConn.connect();
		
		status = httpConn.getResponseCode();
		
		if ( status == 200 ) {
			/* Get Object from Server */
			
			im = httpConn.getInputStream();
			
			ObjectInputStream ois = new ObjectInputStream(im);
			
			DeliverResultSet drs  = (DeliverResultSet) ois.readObject();
			
			/* Convert to ResultSet*/
			ResultSet rs = Convert.toSerializableResultSet(drs);
			
			/* Printer*/
			while (rs.next() ) {
				System.out.println(rs.getString(5));
			}
		}

		httpConn.disconnect();		
	}
}
