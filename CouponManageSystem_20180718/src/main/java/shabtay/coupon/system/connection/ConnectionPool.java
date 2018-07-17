package shabtay.coupon.system.connection;

import java.util.ArrayList;

import shabtay.coupon.system.common.ConstantList;

/**
 * Connection pool holding 5 connections
 * @author Shabtay Shalem
 *
 */
public class ConnectionPool {

	private static ConnectionPool _INSTANCE;
	private boolean terminate;

	// define maximum 5 connections
	

	// list of connections
	private ArrayList<DBConnection> connections = null;

	/*
	 * Initializing connectionPool with 5 connections
	 */
	private ConnectionPool() {
		connections = new ArrayList<>();
		for (int i = 0; i < ConstantList.NUMBER_OF_CONNECTIONS; i++) {
			DBConnection dbConnection = new DBConnection();
			connections.add(dbConnection);
		}		
		this.terminate = false;
	} 
 
	/**
	 * Returning INSTANCE connection
	 * this is singleton since only one instance of Connection pool is created 
	 * @return ConnectionPool instance
	 */
	public static synchronized ConnectionPool getInstance() {
		if (_INSTANCE == null) 
		{
			_INSTANCE = new ConnectionPool();
		}
		return _INSTANCE;
	}
	
	
	/**
	 * get a connection from the list
	 * if there is no connection available- wait
	 * @return DBConnection
	 * @throws InterruptedException
	 */
    public synchronized DBConnection getConnection() throws InterruptedException {
        while (this.connections.size() == 0) {          
            wait();
        }
        DBConnection connection = this.connections.get(0);
        this.connections.remove(0);        
        return connection;
    }

    /**
     *  return connection into the list- notify (the waiters)
     * @param connection
     */
    public synchronized void returnConenction(DBConnection connection)
    {
        this.connections.add(connection);        
        notify();
    }

    /**
     * This method is been called from CouponSystem , and puts the flag "terminate" to ture 
     * which terminates the connection pool,
     * and stops the daily coupon expired task
     * @return boolean
     */
    public boolean terminateDailyTaskAndCloseConnectionPool() {
    	this.terminate = true;
    	this.connections.clear();
    	System.out.println(" shut down will be executed in 5 seconds");
    	try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
    	return true;
    }
	
    /**
     * this is been called from the DailyCouponExpirationTask Thread 
     * which checks if terminate is true/false      
     * @return boolean  
     */
    public boolean invokeStopTask() {		
		return this.terminate;
	}
	
}
