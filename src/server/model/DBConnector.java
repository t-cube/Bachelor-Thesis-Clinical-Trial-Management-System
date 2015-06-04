/* 
 * Copyright (C) 2013 Torsten Dietl <Torsten.Dietl@gmx.de>
 *
 * This source code is released under the Microsoft Reference Source License 
 * (MS-RSL).
 *
 * MICROSOFT REFERENCE SOURCE LICENSE (MS-RSL)
 *
 * This license governs use of the accompanying software. 
 * If you use the software, you accept this license. 
 * If you do not accept the license, do not use the software.
 *
 *
 * 1. Definitions
 *
 * The terms "reproduce," "reproduction," and "distribution" have the same 
 * meaning here as under U.S. copyright law.
 * "You" means the licensee of the software.
 * "Your company" means the company you worked for when you downloaded the 
 * software.
 * "Reference use" means use of the software within your company as a reference,
 * in read only form, for the sole purposes of debugging your products, 
 * maintaining your products, or enhancing the interoperability of your products
 * with the software, and specifically excludes the right to distribute the 
 * software outside of your company.
 * "Licensed patents" means any Licensor patent claims which read directly on 
 * the software as distributed by the Licensor under this license.
 * 
 *
 * 2. Grant of Rights
 * 
 * (A) Copyright Grant- Subject to the terms of this license, the Licensor 
 * grants you a non-transferable, non-exclusive, worldwide, royalty-free 
 * copyright license to reproduce the software for reference use.
 * (B) Patent Grant- Subject to the terms of this license, the Licensor grants 
 * you a non-transferable, non-exclusive, worldwide, royalty-free patent license
 * under licensed patents for reference use.
 * 
 *
 * 3. Limitations
 *
 * (A) No Trademark License- This license does not grant you any rights to use 
 * the Licensor’s name, logo, or trademarks.
 * (B) If you begin patent litigation against the Licensor over patents that you
 * think may apply to the software (including a cross-claim or counterclaim in a
 * lawsuit), your license to the software ends automatically.
 * (C) The software is licensed "as-is." You bear the risk of using it. 
 * The Licensor gives no express warranties, guarantees or conditions. You may 
 * have additional consumer rights under your local laws which this license 
 * cannot change. To the extent permitted under your local laws, the Licensor 
 * excludes the implied warranties of merchantability, fitness for a particular 
 * purpose and non-infringement. 
 *
 */


package server.model;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import server.model.enumerations.DBMS;
import server.model.enumerations.SQLITEMTYPE;
import shared.util.Debugger;
import shared.util.ClinSysException;

/**
 * @author Torsten Dietl
 * @version 1.0.0a
 * Class that creates a connection with a database server and executes SQL statements, given through a SQLCreator Object.
 */
public class DBConnector {
	private String host = "";
	private int port = -1;
	private String dbName = "";
	private String dbUser = "";
	private String dbPassword = "";
	private DBMS dbmsEnum = DBMS.POSTGRESQL;
	private Connection connection = null;
	
	//really private attributes i.e. they have no getter nor a setter 
	private String driverClassName = "";
	private String driverPath = "";
	private String databaseURL = "";
	private String dbms = "";
	private String selectTablesStatement = "";
	
	/**
	 * Constructor
	 * Does nothing atm.
	 */
	public DBConnector(){		
	}
	
	/* Getter ------------------------------------------------------------------------- */
	/**
	 * Returns the host url/ip
	 * @return String host
	 */
	public String getHost() {
		return this.host;
	}
	
	/**
	 * Returns the port number
	 * @return int port
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Returns the database name
	 * @return String dbName
	 */
	public String getDbName() {
		return this.dbName;
	}
	
	/**
	 * Returns the database user
	 * @return String dbUser
	 */
	public String getDbUser() {
		return this.dbUser;
	}

	/**
	 * Returns database Password
	 * @return String dbPassword
	 */
	public String getDbPassword() {
		return this.dbPassword;
	}
	
	/**
	 * Returns the used database management system
	 * @return DBMS dbms
	 */
	public DBMS getDbms() {
		return this.dbmsEnum;
	}

	/* Setter ------------------------------------------------------------------------- */
	/**
	 * Sets the host url/ip
	 * @param String host
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * Sets the port number
	 * @param int port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * Sets the database name
	 * @param String dbName
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	/**
	 * Sets the database user
	 * @param String dbUser
	 */
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	/**
	 * Sets the database password
	 * @param String dbPassword
	 */
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	/**
	 * Sets the used database management system
	 * @param DBMS dbms
	 */
	public void setDbms(DBMS dbms) {
		this.dbmsEnum = dbms;
	}

	
	/* Private ------------------------------------------------------------------------- */	
	/**
	 * Function, which checks if all attributes are set.
	 * @throws ClinSysException
	 */
	private void checkAttributes() throws ClinSysException{
		// check if all needed information are given
		if (this.host.equals("")){
			throw new ClinSysException("(DBConnector.checkAttributs()) : No host was given to connect to");
		}
		if (this.port<1024 || this.port>65535){
			throw new ClinSysException("(DBConnector.checkAttributs()) : No usable port was given. The port must be between 1024 and 65535");
		}
		if (this.dbName.equals("")){
			throw new ClinSysException("(DBConnector.checkAttributs()) : No database name was given to connect to.");
		}
		if (this.dbUser.equals("")){
			throw new ClinSysException("(DBConnector.checkAttributs()) : No database user was given to connect as.");
		}
		if (this.dbPassword.equals("")){
			throw new ClinSysException("(DBConnector.checkAttributs()) : No database password was given to connect with.");
		}
	}
	
	/**
	 * Returns all tables of this database, using a custom database management system. 
	 * For this it needs the sql statement that is used to get the table names.
	 * If no table was found an empty list will be returned.
	 * Care: Only the tables for which the database user has the rights can be found.
	 * @param SQLCreator sqlCreator
	 * @return ArrayList<String>
	 * @throws ClinSysException
	 */
	private ArrayList<String> getTableList(SQLCreator sqlCreator) throws ClinSysException{
		ArrayList<String> tableNames = new ArrayList<String>();
		ResultSet rs = null;
		
		try{
			rs = this.executeStatement(sqlCreator);
			while(rs.next()){
				tableNames.add(rs.getString(1));
			}
		} catch (ClinSysException e) {
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(DBConnector.getTableList(SQLCreator)) : Cannot execute the prepared statement.");
		} catch (SQLException e) {
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(DBConnector.getTableList(SQLCreator)) : Cannot work with the returned resultset.");
		}
		
		return tableNames;
	}
	
	
	/* Public ------------------------------------------------------------------------- */
	/**
	 * Connects to the set database server
	 * @throws ClinSysException
	 */
	public void connect() throws ClinSysException{
		
		// check the attributes
		checkAttributes();
				
		// set up driver class name and database URL
		this.dbms = this.dbmsEnum.get();
		if (this.dbmsEnum == DBMS.POSTGRESQL){
			driverClassName = "org.postgresql.Driver";
			databaseURL = "jdbc:" + this.dbms + "://" + this.host + ":" + this.port + "/" + this.dbName;
		}else if(this.dbmsEnum == DBMS.MYSQL){
			// code will follow here later
		}else if(this.dbmsEnum == DBMS.MSSQL){
			// code will follow here later
		} 
		
		
		// try to load the driver and connecto to the database
		try {
			Class.forName(driverClassName);
			this.connection = DriverManager.getConnection(databaseURL, this.dbUser, this.dbPassword);			
		} catch (ClassNotFoundException e) {
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(DBConnector.connect()) : Could not find the database driver class.");
		} catch (SQLException e) {
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(DBConnector.connect()) : Could not connect to the database. " +
					"						Please check the database name, the user name and the password");
		}		
	}
	
	/**
	 * Connects to the set database server with an individual database driver 
	 * @param String serverPropertiesPath Path to the files containing the database settings
	 * @throws ClinSysException
	 */
	public void connect(String databasePropertiesPath) throws ClinSysException{		
		// check the attributes
		checkAttributes();
		
		try {
			//TODO Read out the database properties file and set up the database settings			
			this.driverPath = ""; //TODO has to be set			
			this.driverClassName = ""; //TODO has to be set
			this.dbms = ""; //TODO has to be set		
			this.selectTablesStatement = ""; //TODO has to be set
			
			
			// create the URL to the driver 
			URL url = new File(driverPath).toURI().toURL();
			// create the ClassLoader
			URLClassLoader cl = new URLClassLoader( new URL[]{ url } );
			// create the Class
			Class<?> c = cl.loadClass(driverClassName);
			// create the driver
			Driver driver = (Driver) c.newInstance();
			// register the driver
			DriverManager.registerDriver(driver);
			
			try {
				cl.close();
			} catch (IOException e) {
				throw new ClinSysException("");
			}
		} catch (MalformedURLException e) {
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(DBConnector.connect(" + driverPath + ", " + driverClassName + ", " + dbms + 
											")) : Driver file not found.");
		} catch (ClassNotFoundException e) {
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(DBConnector.connect(" + driverPath + ", " + driverClassName + ", " + dbms + 
											")) : Could not find the database driver class.");
		} catch (InstantiationException e) {
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(DBConnector.connect(" + driverPath + ", " + driverClassName + ", " + dbms + 
											")) : Could not create a driver instance.");
		} catch (IllegalAccessException e) {
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(DBConnector.connect(" + driverPath + ", " + driverClassName + ", " + dbms + 
											")) : You are not allowed to create a driver instance.");
		} catch (SQLException e) {
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(DBConnector.connect(" + driverPath + ", " + driverClassName + ", " + dbms + 
											")) : Could not register the driver.");
		}		
		
		// set up the database URL
		this.databaseURL = "jdbc:" + dbms + "://" + this.host + ":" + this.port + "/" + this.dbName;
		
		// try to connect to the database
		try {
			this.connection = DriverManager.getConnection(this.databaseURL, this.dbUser, this.dbPassword);
			this.connection.setAutoCommit(true);
		} catch (SQLException e) {
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(DBConnector.connect(" + driverPath + ", " + driverClassName + ", " + dbms + 
											")) : Could not connect to the database. " +
											"Please check the database name, the user name and the password.");
		}		
	}
	
	/**
	 * Returns all tables of this database.
	 * Only can be used for a supported database managements system.
	 * If no table was found an empty list will be returned.
	 * Care: Only the tables for which the database user has the rights can be found.
	 * @return ArrayList<String>
	 * @throws ClinSysException
	 */
	public ArrayList<String> getTableList() throws ClinSysException{
		ArrayList<String> tableNames = new ArrayList<String>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		switch(this.dbmsEnum){
		case POSTGRESQL:
			try {
				stmt = this.connection.prepareStatement("SELECT table_name FROM information_schema.tables " +
															"WHERE table_schema = 'public';");
				rs = stmt.executeQuery();
				while(rs.next()){
					tableNames.add(rs.getString(1));
				}
			} catch (SQLException e) {
				throw new ClinSysException("(DBConnector.getTableList()) : Cannot prepare or execute the table names query.");
			}			
			break;
		case MYSQL:
			// code will follow here later
		case MSSQL:
			// code will follow here later
		case OTHER:
			//TODO Set up the creator by using a serverside database properties file in the connect(file) function
			SQLCreator creator = new SQLCreator();
			creator.addItem(this.selectTablesStatement, SQLITEMTYPE.TABLE);
			return (this.getTableList(creator)); 
		default:
			throw new ClinSysException("(DBConnector.getTableList()) : No defined or external DBMS used.");
		} 
		return tableNames;
	}
	
	/**
	 * Executes a sql statement
	 * @param SQLCreator sqlCreator
	 * @return ResultSet
	 * @throws ClinSysException
	 */
	public ResultSet executeStatement(SQLCreator sqlCreator) throws ClinSysException{
		PreparedStatement stmt = null;
		try {
			stmt = sqlCreator.getStmt(this.connection);
			Debugger.debug(stmt.toString());
			ResultSet rs = stmt.executeQuery();
			return rs; 			
		} catch (SQLException e) {
			if (e.getErrorCode()!=0){
				Debugger.debug(e.getMessage());
				throw new ClinSysException ("(DBConnector.executeStatement(" + sqlCreator.getSql() + ") : Could not execute the query.");
			}else{
				return null;
			}
		}
	}
	
	
	public void close() throws ClinSysException{
		try {
			this.connection.close();
		} catch (SQLException e) {
			throw new ClinSysException("(DBConnector.close()) : Coult not close the database connection.");
		}
	}
}


