package alg.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import alg.common.StatusCode;


public class SqliteDriver extends DatabaseDriver {

	private final String dbPath;
	private Connection conn = null;
	private Statement stat = null;
	
	
	public SqliteDriver(final String dbPath) {
			this.dbPath = dbPath;
	}
		
	@Override
	public int onConnect() {
		if ( conn != null ) return StatusCode.success;
		
		try {
			Class.forName("org.sqlite.JDBC");

			conn =  DriverManager
					.getConnection(
					"jdbc:sqlite:" + this.dbPath	
					);
		}  catch (ClassNotFoundException e) {
			return StatusCode.ERR_JDBC_CLASS_NOT_FOUND();
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		return StatusCode.success;
	}
		
	@Override
	public int createTable(String sql) {
		if (conn == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();

		try {
			stat = conn.createStatement();
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		return StatusCode.success;
	}
		
	@Override
	public int inset(String sql) {
		if (conn == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();

		try {
			stat = conn.createStatement();
			if ( stat.executeUpdate(sql) == 0 )
				return StatusCode.WAR_SQL_STATEMENT_NOTHING();
			
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}

		return StatusCode.success;
	}
		
	@Override
	public int update(String sql) {
		if (conn == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();

		try {
			stat = conn.createStatement();
			if ( stat.executeUpdate(sql)  == 0 )
				return StatusCode.WAR_SQL_STATEMENT_NOTHING();
			
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
			
		return StatusCode.success;
	}
		

		
	@Override
	public ResultSet select(String sql)  {
		if (conn == null) {
			StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
			return null;
		} else if (sql.isEmpty()) {
			StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();
			return null;
		}

		ResultSet result = null;
		try {
			stat = conn.createStatement();
			result =stat.executeQuery(sql);
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
			return null;
		}
		return result;
	}
		
	@Override
	public int delete(String sql) {
		if ( conn == null )
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if ( sql.isEmpty() )
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();
	
		try {
			stat = conn.createStatement();
			if ( stat.executeUpdate(sql) == 0 )
				return StatusCode.WAR_SQL_STATEMENT_NOTHING();

		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		 
		return StatusCode.success;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		conn.setAutoCommit(autoCommit);
	}

	@Override
	public void commit() throws SQLException {
		conn.commit();
	}

	@Override
	public void rollback() throws SQLException {
		conn.rollback();
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return conn.getAutoCommit();
	}

	@Override 
	public String[] getTables() {
		String[] tables = null;
		try {
			stat = conn.createStatement();
			ResultSet rs = null;
			String sql = "SELECT * FROM sqlite_master WHERE type='table'";
			rs = stat.executeQuery(sql);
			
			int len=0;
			while (rs.next() ) len++;
			
			if ( len <= 0 ) {
				rs.close();
				return null;
			}
			
			tables = new String[len];
			rs = stat.executeQuery(sql);
		
			int i = 0;
			while ( rs.next() ) {
				tables[i++] = new String(rs.getString("tbl_name"));
			}
			
			rs.close();
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
			return null;
		} 
		return tables;
	}
	
	@Override
	public void close() {
		if(stat != null) {
			try {
				stat.close();
			} catch(SQLException e) {
				// nothing
			}
		}
		
		if(conn != null) {
			try {
				conn.close();
			} catch(SQLException e) {
				StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());          
			}
		}
	}
}
