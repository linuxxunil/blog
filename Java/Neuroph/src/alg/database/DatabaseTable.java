package alg.database;

public class DatabaseTable {
	static public class Ibeacon {
		// table name define
		public static String name = "Ibeacon";
		
		public static final String colDeviceID 	= "id";
		public static final String colDeviceMAC = "device";
		public static final String colRSSI 		= "rssi";
		public static final String colDistance 	= "distance";
		public static final String colWinAvg	= "winAvg";
		public static final String colKalman 	= "kalman";

		// sql syntax : create
		public static String create() {
			return "CREATE TABLE IF NOT EXISTS " + name + "("
					+ colDeviceID	+ " nvarchar(3)	NOT NULL,"
					+ colDeviceMAC	+ " nvarchar(6)	NOT NULL,"
					+ colRSSI		+ " nvarchar(6)	NOT NULL,"
					+ colDistance 	+ " nvarchar(64) NULL   ,"
					+ colWinAvg 	+ " nvarchar(1) NULL    ,"
					+ colKalman 	+ " nvarchar(1) NULL    )";
		}
		
		
	}
	
	static public class Training {
		public static String name = "Training";
		
		public static final String colX1 	= "X1";
		public static final String colX2 	= "X2";
		public static final String colX3 	= "X3";
		public static final String colX4 	= "X4";
		public static final String colT0 	= "T0";
		public static final String colMSE	= "mse";
		public static final String colParm	= "parm";

		// sql syntax : create
		public static String create() {
			return "CREATE TABLE IF NOT EXISTS " + name + "("
					+ colX1	+ " nvarchar(12) NOT NULL,"
					+ colX2	+ " nvarchar(12) NULL,"
					+ colX3	+ " nvarchar(12) NULL,"
					+ colX4 + " nvarchar(12) NULL,"
					+ colT0 + " nvarchar(1)  NOT NULL,"
					+ colMSE+ " nvarchar(1)  NOT NULL,"
					+ colParm+" nvarchar(256) NOT NULL)";
		}
	}
	
	static public class Testing {
		// table name define
		public static String name = "Testing";
		
		public static final String colDeviceID 	= "id";
		public static final String colPredict	= "predict";
		public static final String colDeviceMAC = "device";
		public static final String colRSSI 		= "rssi";
		public static final String colWinAvg	= "winAvg";
		public static final String colKalman 	= "kalman";

		// sql syntax : create
		public static String create() {
			return "CREATE TABLE IF NOT EXISTS " + name + "("
					+ colDeviceID	+ " nvarchar(3)	NOT NULL,"
					+ colPredict	+ " nvarchar(3)	NOT NULL,"
					+ colDeviceMAC	+ " nvarchar(64)	NOT NULL,"
					+ colRSSI		+ " nvarchar(32)	NOT NULL,"
					+ colWinAvg 	+ " nvarchar(1) NULL    ,"
					+ colKalman 	+ " nvarchar(1) NULL    )";
		}
	}
}
