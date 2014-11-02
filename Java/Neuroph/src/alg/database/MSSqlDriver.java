package alg.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import alg.common.StatusCode;


public class MSSqlDriver extends DatabaseDriver {

	private String dbUrl = "sqlserver://175.99.86.134:1433;instance=Cscheduling_SQL;DatabaseName=cscheduling;charset=utf-8";
	private String dbUser = "sa";
	private String dbPass = "ptch@RS";
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	
	public MSSqlDriver(String dbUrl, String dbUser, String dbPass) {
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
}
	@Override
	public int onConnect() {
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			
			con = DriverManager
					.getConnection("jdbc:jtds:"+dbUrl, dbUser, dbPass);
		} catch (ClassNotFoundException e1) {
			return StatusCode.ERR_JDBC_CLASS_NOT_FOUND();
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		return StatusCode.success;
	}

	@Override
	public int inset(String sql) {
		if (con == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		return StatusCode.success;
	}


	@Override
	public int createTable(String sql) {
		if (con == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		return StatusCode.success;
	}


	@Override
	public ResultSet select(String sql) {
		if (con == null) {
			StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
			return null;
		} else if (sql.isEmpty()) {
			StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();
			return null;
		}

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
			return null;
		}
		return rs;
	}

	@Override
	public int delete(String sql) {
		if (con == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		return StatusCode.success;
	}

	@Override
	public int update(String sql) {
		if (con == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		return StatusCode.success;
	}

	@Override
	public void setAutoCommit(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getAutoCommit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		if(stmt != null) {
			try {
				stmt.close();
			} catch(SQLException e) {
				StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
			}
		}
		
		if(con != null) {
			try {
				con.close();
			} catch(SQLException e) {
				StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());     
			}
		}
		
	}

	@Override
	public String[] getTables() {
		String[] tables = null;
		try {
			DatabaseMetaData  meta = con.getMetaData();
			
			ResultSet rs = meta.getTables(null, null, "%", null);
			
			int len=0;
			while ( rs.next() ) { 
				if ( rs.getString("TABLE_NAME").equals("CHECK_CONSTRAINTS") )
					break;
				len++;
			}
			
			if ( len <= 0 ) {
				rs.close();
				return null;
			}
			
			tables = new String[len];
			rs = meta.getTables(null, null, "%", null);
			
			int i = 0;
			while ( i<len && rs.next() ) {
				tables[i++] = new String(rs.getString("TABLE_NAME"));
			}
			
			rs.close();
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
			return null;
		}
		return tables;
	}

}
