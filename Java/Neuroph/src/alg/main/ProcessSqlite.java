package alg.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;

import alg.database.DatabaseDriver;
import alg.database.DatabaseTable;
import alg.database.SqliteDriver;

public class ProcessSqlite {
	private DatabaseDriver srcdb = null;
	private DatabaseDriver dstdb = null;

	public ProcessSqlite() {
	}

	private void insertTraining(int id, String mac, String rssi, String distance,
			int mode1, int mode2) {
		String s = "INSERT INTO " + DatabaseTable.Ibeacon.name + "(\""
				+ DatabaseTable.Ibeacon.colDeviceID + "\"," + "	\""
				+ DatabaseTable.Ibeacon.colDeviceMAC + "\"," + " \""
				+ DatabaseTable.Ibeacon.colRSSI + "\"," + " \""
				+ DatabaseTable.Ibeacon.colDistance + "\"," + " \""
				+ DatabaseTable.Ibeacon.colWinAvg + "\"," + " \""
				+ DatabaseTable.Ibeacon.colKalman + "\")" + " VALUES " + "(\""
				+ id + "\"," + "	\"" + mac + "\"," + " \"" + rssi + "\","
				+ " \"" + 0 + "\"," + " \"" + mode1 + "\"," + " \"" + mode2
				+ "\")";
		System.out.println(s);
		dstdb.inset(s);
	}

	private void insertTesting(int id,int predict, String mac, String rssi,
			int mode1, int mode2) {
		String s = "INSERT INTO " + DatabaseTable.Testing.name + "(\""
				+ DatabaseTable.Testing.colDeviceID + "\"," + "	\""
				+ DatabaseTable.Testing.colPredict + "\"," + " \""
				+ DatabaseTable.Testing.colDeviceMAC + "\"," + " \""
				+ DatabaseTable.Testing.colRSSI + "\"," + " \""
				+ DatabaseTable.Testing.colWinAvg + "\"," + " \""
				+ DatabaseTable.Testing.colKalman + "\")" + " VALUES " + "(\""
				+ id + "\",\"" +predict+ "\",\"" + mac + "\"," + " \"" + rssi + "\",\""
				+ mode1 + "\"," + " \"" + mode2
				+ "\")";
		System.out.println(s);
		dstdb.inset(s);
	}
	
	private void deleteTrainingGarbageRows(int id, String mac, int winAvg,
			int kalman, int limit) {
		String sql = String
				.format("DELETE FROM Ibeacon WHERE rowid in "
						+ "(SELECT rowid FROM Ibeacon WHERE id=\'%d\' AND device=\'%s\' "
						+ "AND winAvg=\'%d\' AND kalman=\'%d\' LIMIT %d)", id,
						mac, winAvg, kalman, limit);
		srcdb.delete(sql);
	}

	private void deleteTestingGarbageRows(int id,int winAvg,
			int kalman, int keepLastCount) {
		int rowCount;
		try {
			String sql = String.format("SELECT COUNT(*) FROM Testing WHERE id=\'%d\' "
					+ "AND winAvg=\'%d\' AND kalman=\'%d\'",
					id, winAvg, kalman);

			ResultSet rs = srcdb.select(sql);
			if (rs == null) {
				rowCount = 0;
			} else {
				rowCount = rs.getInt(1);
			}

			sql = String.format("DELETE FROM Testing WHERE rowid in "
					+ "(SELECT rowid FROM Testing WHERE id=\'%d\' "
					+ "AND winAvg=\'%d\' AND kalman=\'%d\' LIMIT %d)", id,
					winAvg, kalman, rowCount - keepLastCount);
			srcdb.delete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void processTraining(String src, int maxID, int garbageRowsCount,
			int trainingCount, int testingCount, String[] macSet) {
		int[] mode1 = { 0, 0, 1, 1 };
		int[] mode2 = { 0, 1, 0, 1 };

		try {
			Files.copy(new File(src + ".sqlite.orig").toPath(), new File(src
					+ ".sqlite").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		srcdb = new SqliteDriver(src + ".sqlite");
		srcdb.onConnect();
		// 產生Filter後的Database
		ResultSet[][] rs = new ResultSet[maxID][macSet.length];
		for (int k = 0; k < 4; k++) { // handle 4 mode
										// (None/WinAvg/Kalman/Hybrid)
			// Delete Garbage Rows
			for (int id = 0; id < maxID; id++) { //
				for (int m = 0; m < macSet.length; m++) {
					deleteTrainingGarbageRows(id, macSet[m], mode1[k],
							mode2[k], garbageRowsCount);
				}
			}
		}

		// Process Traning Databas
		for (int k = 0; k < 4; k++) { // handle 4 mode
										// (None/WinAvg/Kalman/Hybrid

			dstdb = new SqliteDriver(String.format("training%02d.sqlite", k));
			dstdb.onConnect();
			dstdb.createTable(DatabaseTable.Ibeacon.create());

			for (int id = 0; id < maxID; id++) { //
				for (int m = 0; m < macSet.length; m++) {
					String sql = String.format("SELECT * FROM Ibeacon WHERE "
							+ "id=\'%d\' AND device=\'%s\' AND "
							+ "winAvg=\'%d\' AND Kalman=\'%d\'", id, macSet[m],
							mode1[k], mode2[k]);
					rs[id][m] = srcdb.select(sql);
				}
			}

			try {

				for (int i = 0; i < testingCount; i++) {
					for (int id = 0; id < maxID; id++) {
						for (int m = 0; m < macSet.length; m++) {
							if (rs[id][m].next()) {
								insertTraining(id, macSet[m],
										rs[id][m].getString("rssi"), "0",
										mode1[k], mode2[k]);
							}

						}
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			dstdb.close();
		}

		// Process Testing Database
		for (int k = 0; k < 4; k++) { // handle 4 mode
										// (None/WinAvg/Kalman/Hybrid

			dstdb = new SqliteDriver(String.format("testing%02d.sqlite", k));
			dstdb.onConnect();
			dstdb.createTable(DatabaseTable.Ibeacon.create());

			for (int id = 0; id < maxID; id++) { //
				for (int m = 0; m < macSet.length; m++) {
					String sql = String.format("SELECT * FROM Ibeacon WHERE "
							+ "id=\'%d\' AND device=\'%s\' AND "
							+ "winAvg=\'%d\' AND Kalman=\'%d\'", id, macSet[m],
							mode1[k], mode2[k]);
					rs[id][m] = srcdb.select(sql);
				}
			}

			try {

				for (int i = 0; i < testingCount; i++) {
					for (int id = 0; id < maxID; id++) {
						for (int m = 0; m < macSet.length; m++) {
							if (rs[id][m].next()) {
								insertTraining(id, macSet[m],
										rs[id][m].getString("rssi"), "0",
										mode1[k], mode2[k]);
							}

						}
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			dstdb.close();
		}
		srcdb.close();
		System.out.println("Finish");

	}

	public void processTesting(String src, int maxID, int keepLastCount) {
		int[] mode1 = { 0, 0, 1, 1 };	// winAvg
		int[] mode2 = { 0, 1, 0, 1 };	// kalman

		try {
			Files.copy(new File(src + ".sqlite.orig").toPath(), new File(src
					+ ".sqlite").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		srcdb = new SqliteDriver(src + ".sqlite");
		srcdb.onConnect();
		// 產生Filter後的Database
		ResultSet[] rs = new ResultSet[maxID];
		for (int k = 0; k < 4; k++) { // handle 4 mode
										// (None/WinAvg/Kalman/Hybrid)
			// Delete Garbage Rows
			for (int id = 0; id < maxID; id++) { //
				deleteTestingGarbageRows(id, mode1[k], mode2[k],keepLastCount);
			}
		}

		// Process Result Databas
		for (int k = 0; k < 4; k++) { // handle 4 mode
										// (None/WinAvg/Kalman/Hybrid
			dstdb = new SqliteDriver(String.format("result%02d.sqlite", k));
			dstdb.onConnect();
			dstdb.createTable(DatabaseTable.Testing.create());
			for (int id = 0; id < maxID; id++) { //
					String sql = String.format("SELECT * FROM Testing WHERE "
							+ "id=\'%d\' AND winAvg=\'%d\' AND Kalman=\'%d\' "
							+ "ORDER BY rowid DESC LIMIT %d", 
							id, mode1[k],mode2[k],keepLastCount);
					rs[id] = srcdb.select(sql);
			}
			
			try {
				for ( int id=0; id<maxID; id++) {
					while ( rs[id].next() ) {

						insertTesting(id,
								rs[id].getInt(DatabaseTable.Testing.colPredict),
								rs[id].getString(DatabaseTable.Testing.colDeviceMAC),
								rs[id].getString(DatabaseTable.Testing.colRSSI),
								rs[id].getInt(DatabaseTable.Testing.colWinAvg),
								rs[id].getInt(DatabaseTable.Testing.colKalman));
							
					}
				}
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dstdb.close();
	}

	private void writeToTxt(String file, String line) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);
			bw.write(line);
			bw.newLine();
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

	public double normalize(double y) {

		double dMax = 0.8f;
		double dMin = -0.8f;
		double yMax = 100.0f;
		double yMin = 40.0f;
		y = -y;
		if (y > yMax)
			y = yMax;
		else if (y < yMin)
			y = yMin;
		return (double) ((((y - yMin) * (dMax - dMin) / (yMax - yMin))) + dMin);
	}

	public float normalize(float y) {
		float dMax = 0.8f;
		float dMin = -0.8f;
		float yMax = 100.0f;
		float yMin = 40.0f;
		y = -y;
		if (y > yMax)
			y = yMax;
		else if (y < yMin)
			y = yMin;
		return (float) ((((y - yMin) * (dMax - dMin) / (yMax - yMin))) + dMin);

	}

	public void transferToTxt(String src, int nOutput, String[] macSet) {
		int i = 0;
		double rssi;
		int areaID = 0;
		String line = "";
		ResultSet rs = null;

		srcdb = new SqliteDriver(src + ".sqlite");
		srcdb.onConnect();

		rs = srcdb.select("SELECT * FROM Ibeacon");
		try {
			while (rs.next()) {
				rssi = rs.getDouble("rssi");

				line += String.valueOf(normalize((float) rssi)) + ",";
				if (++i % macSet.length == 0) {
					int id = rs.getInt("id");
					for (int x = 0; x < nOutput; x++) {
						if (x == nOutput - 1) {
							if (x == id)
								line += "1";
							else
								line += "0";
						} else {
							if (x == id)
								line += "1,";
							else
								line += "0,";
						}

					}
					writeToTxt(src + ".txt", line);
					line = "";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		srcdb.close();
	}

	public static void main(String[] args) {
		ProcessSqlite ps = new ProcessSqlite();
		String[] macSet = { "78:A5:04:60:02:26", "D0:39:72:D9:FA:65",
				"D0:39:72:D9:FA:2A" };

		ps.processTraining("location", 11, 20, 30, 20, macSet);
		for (int i = 0; i < 4; i++) {
			ps.transferToTxt(String.format("training%02d", i), 11, macSet);
			ps.transferToTxt(String.format("testing%02d", i), 11, macSet);
		}

		//ps.processTesting("location", 11, 100);
	}
}
