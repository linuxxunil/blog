package alg.common;

/**
 * StatusCode Definition 
 * @author jesse
 *
 */

public class StatusCode {
	public static final int success = 0;
	
	/**
	 * 
	 * @param errCode
	 * @param errDescription
	 * @return
	 */
	
	private static int log(String errCode, String errDescription) {
		int value = Integer.valueOf(errCode);
		if ( value > 0 ) System.out.println("WarnCode: " + errCode + ", WarnDescription: " + errDescription);
		else System.out.println("ErrCode: " + errCode + ", ErrDescription: " + errDescription);
		return value;
	}

	private static int syslog(String errCode, String errDescription) {
		int value = Integer.valueOf(errCode);
		if ( value > 0 ) System.out.println("WarnCode: " + errCode + ", WarnDescription: " + errDescription);
		else System.out.println("ErrCode: " + errCode + ", ErrDescription: " + errDescription);
		return value;
	}
	// Common Error	
	public static int ERR_JDBC_CLASS_NOT_FOUND() {
		return log("-001","JDBC class not found");
	}
	
	public static int  ERR_INITIAL_DB_NOT_SUCCESS() {
		return log("-002","Inital DB don't success");
	}
	
	public static int ERR_SQL_SYNTAX_IS_ILLEGAL(String event) {
		return log("-003","SQL syntax is illegal("+event+")");
	}
	
	public static int ERR_UNKOWN_ERROR() {
		return log("-999","unkown error");
	}
	
	public static int WAR_SQL_STATEMENT_NOTHING() {
		return log("001","SQL statement nothing");
	}
	// Parameter Error
	
	public static int ERR_PARM_SQL_SYNTAX_IS_NULL() {
		return log("-101","SQL syntax is null");
	}
	
	public static int ERR_PARM_CSPID_IS_NULL() {
		return log("-102","cspId is null");
	}
	
	public static int ERR_PARM_APPID_IS_NULL() {
		return log("-103","appId is null");
	}
	
	public static int ERR_PARM_SMEID_IS_NULL() {
		return log("-104","smeId is null");
	}
	
	public static int ERR_PARM_DB_DOWNLOAD_LINK_IS_NULL() {
		return log("-105","dbDownloadLink is null");
	}
	
	public static int ERR_PARM_SOURCE_DB_ISNOT_ERROR() {
		return log("-106","source db is only null");
	}
	
	public static int ERR_PARM_SYNC_DB_IS_NULL() {
		return log("-107","source db is only null");
	}

	// SqliteDriver
	public static int ERR_OPEN_DIR(String path) {
		return log("-1001","Open sqlite dir error");
	}
	
	public static int ERR_OPEN_SQLITE_FILE(String path) {
		return log("-1002","Open Sqlite file error");
	}
	

	// MSSQL Driver
	public static int ERR_MSSQL_CONNECT_ERROR() {
		return log("-2001","Cannot connect to MSSQL ");
	}
	
	// Database synchronize
	public static int ERR_GET_DB_PATH_ERROR() {
		return log("-3001","Get sqlite path fail");
	}

	public static int ERR_EXE_USER_RULES_ERROR() {
		return log("-3002","Rules of user execute error");
	}
	
	public static int ERR_EXE_USER_RULES_TO_DB_ERROR() {
		return log("-3002","Rules of user execute error");
	}
	
	public static int ERR_COLS_NUMBER_ERROR() {
		return log("-3003","columns number is less than 0");
	}
	public static int ERR_JSON_PARSER_ERROR(String errMsg) {
		return log("-3004","json parser is error"+errMsg);
	}
	
	public static int ERR_WGET_FILE_ERROR() {
		return log("-3005","wget file error");
	}
	
	public static int ERR_COPY_FILE_IO_ERROR() {
		return log("-3006","copy file IO error");
	}
	
	public static int ERR_COPY_FILE_NOT_FOUND_ERROR() {
		return log("-3007","file not found");
	}
	
	public static int ERR_RM_FILE_CANNOT_WRITE_ERROR() {
		return log("-3008","file can not write");
	}
	public static int ERR_RM_DIR_NOT_EMPTY_ERROR() {
		return log("-3009","Dir of rmmove isn't empty");
	}
	public static int ERR_RM_FILE_ERROR() {
		return log("-3010","remove file error");
	}
	
	public static int ERR_MOVE_FILE_ERROR() {
		return log("-3011","move file error");
	}

}
