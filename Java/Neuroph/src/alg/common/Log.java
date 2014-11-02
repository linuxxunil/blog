package alg.common;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import alg.database.DatabaseTable;

public class Log {	
	private static float[] X;
	private static float T;
	private static String w ="";
	private static String dbPath;
	
	public static void setOutput(String path) {
		dbPath = path;
	}
	
	public static void insX(float[] _X) {
		if ( X == null || X.length != _X.length)
			X = new float[_X.length];
		
		for (int i=0; i<_X.length; i++) {
			X[i] = _X[i];
		}
	}
	
	public static void insY(float _T) {
		T = _T;
	}
	
	public static void insWQ(float[][] wXH, float[][] wHY, float[] thetaH,float[] thetaY) {
		
		for (int i = 0; i < wXH.length; i++) 
			for (int j = 0; j < wXH[i].length; j++)
				w += "wXH[" + i + "][" + j + "]=" + wXH[i][j] + ";";
		for (int i = 0; i < wHY.length; i++) 
			for (int j = 0; j < wHY[i].length; j++)
				w += "wHY[" + i + "][" + j + "]=" + wHY[i][j] + ";";
		for (int i = 0; i < thetaH.length; i++)
				w += "thetaH["+i+"]="+thetaH[i]+ ";";
		for (int i = 0; i < thetaY.length; i++)
				w += "thetaY["+i+"]="+thetaY[i]+ ";";

	}
	
	public static void insMSE(float MSE) {
		String sql = "INSERT INTO " + DatabaseTable.Training.name +"(";
				for (int i=0; i<X.length; i++) {
					switch(i){
					case 0: sql +=  "\"" + DatabaseTable.Training.colX1	+ "\","; 
					break;
					case 1: sql +=  "\"" + DatabaseTable.Training.colX2	+ "\","; 
					break;
					case 2: sql +=  "\"" + DatabaseTable.Training.colX3	+ "\","; 
					break;
					case 3: sql +=  "\"" + DatabaseTable.Training.colX4	+ "\","; 
					break;
					}
					
				}
				sql += " \"" + DatabaseTable.Training.colT0 	+ "\","
					+ " \"" + DatabaseTable.Training.colMSE		+ "\","
					+ " \"" + DatabaseTable.Training.colParm	+ "\")"
					+ " VALUES (";
				for (int i=0; i<X.length; i++) {
					sql +=  "\"" + X[i]	+ "\",";
				}
				sql += " \"" + T					+ "\","
					+  " \"" + MSE					+ "\","
					+  " \"" + w					+ "\")";
		
		Connection conn = null;
		Statement  stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn =  DriverManager
					.getConnection("jdbc:sqlite:"+dbPath);
			
			stmt = conn.createStatement();
			stmt.executeUpdate(DatabaseTable.Training.create());
			System.out.println(sql);
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
			w="";
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// for test 
	public static void println(String str) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter("mse.log", true);
			bw = new BufferedWriter (fw);
			
			bw.newLine();
			bw.write(str);
		} catch (FileNotFoundException e) {
		} catch (IOException e1) {
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// nothing
			}
		}
	}
	
	public static void printf(String format, Object... args)  {
		System.out.printf(format, args);
	}
}



