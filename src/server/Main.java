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



package server;

import server.controller.ServerController;
import server.controller.ServerProtocol;
import server.model.DBConnector;
import server.model.DatabaseDictionary;
import server.model.enumerations.DBMS;
import shared.util.FLAGS;
import shared.util.Debugger;
import shared.util.ClinSysException;
import shared.util.Flag;

/**
 * The main class for the server application. It containts the main method and 
 * here the journey begins. ^^
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class Main 
{
	
	/* Constants ----------------------------------------------------------------*/	

	
	/* Variables ----------------------------------------------------------------*/
	
	// The DatabaseDictionary object, used to execute SQL statements
	private static DatabaseDictionary dd;
	
	// The DBConnector object, used to create a connection to a SQL-Server
	private static DBConnector db;
	
	// Address of the SQL-Server
	private static String dbHost = "localhost";
	
	// Port number of the SQL-Server
	private static int dbPort = 5432;
	
	// Name of the database containing the system tables
	private static String dbName = "ikfe_cro_ClinSys_Application";
	
	// Name of the user to connect to the database
	private static String dbUser = "ikfe_cro_admin";
	
	// Password of the user, who connects to the database
	private static String dbPassword = "cro2013!";
	
	// SQL-Server type
	private static DBMS dbms = DBMS.POSTGRESQL;
	
	
	/* Constructor --------------------------------------------------------------*/
	
	
	/* Getter -------------------------------------------------------------------*/
	

	/* Setter -------------------------------------------------------------------*/
		
	
	/* Private ------------------------------------------------------------------*/
	
	/**
	 * Method to start the application. Sets up and starts all objects needed
	 * to run the server application. 
	 */
	private static void start(){
		// create a new connection to a sql-server containing the system tables
		db = new DBConnector();
		db.setDbms(dbms);
		db.setDbName(dbName);
		db.setDbPassword(dbPassword);
		db.setDbUser(dbUser);
		db.setHost(dbHost);
		db.setPort(dbPort);
		
		// try to connect to the server
		try 
		{
			db.connect();
		} 
		catch (ClinSysException e)
		{
			e.printStackTrace();
		}
		Debugger.debug("Connected");
		
		// create a new DatabaseDictionary and add the just connected sql-server		
		dd = new DatabaseDictionary();
		try 
		{
			dd.addDbConnector(db);
		} 
		catch (ClinSysException e) 
		{
			e.printStackTrace();
		}

		// try to start the ServerController object as it is the "control-center"
		// of the application.
		try 
		{
			new ServerController(dd, new ServerProtocol());
		} 
		catch (ClinSysException e) 
		{
			e.printStackTrace();
		}
	}
	
	/* Public -------------------------------------------------------------------*/	
	
	/**
	 * The main method. It accepts start parameter to set the settings of the 
	 * application and then starts the application. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Flag.set(FLAGS.DEBUG);
		
		start();
	}

}
