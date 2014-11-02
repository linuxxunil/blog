package alg.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DatabaseDriver {
	
	/**
	 * 
	 * @param sql
	 * @return
	 */
	abstract public int createTable(String sql) ;
	
	
	abstract public void setAutoCommit(boolean value) throws SQLException ;
	
	abstract public void commit() throws SQLException ;

	abstract public void rollback() throws SQLException ;
	
	abstract public boolean getAutoCommit() throws SQLException ;
	
	/**
	 * 
	 * @return
	 * @return 
	 */
	abstract public int onConnect();
	
	/**
	 * 
	 * @return
	 */
	abstract public void close();
	
	
	/**
	 * 
	 * @param sql
	 * @return 
	 */
	abstract public int inset(String sql) ;
	
	/**
	 * 
	 * @param sql
	 * @return 
	 */
	abstract public ResultSet select(String sql) ;

	
	/**
	 * 
	 * @param sql
	 * @return 
	 */
	abstract public int update(String sql) ;
	
	/**
	 * 
	 * @param table
	 * @return 
	 */
	abstract public int delete(String sql);

	abstract public String[] getTables();	
	protected void finalize()  {
		close();
	}
		
}
