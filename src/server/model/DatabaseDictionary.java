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

import java.io.InputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import plugins.base.BASE_TABLES;


import server.controller.Controller;
import server.model.enumerations.CONCAT;
import server.model.enumerations.RIGHTS;
import server.model.enumerations.SQLITEMTYPE;
import server.model.enumerations.SQLWORD;
import server.model.enumerations.STATEMENTTYPE;
import server.network.Server;
import shared.util.Debugger;
import shared.util.ClinSysException;
import shared.util.RecordSet;


/**
 * Class that stores the DBConnector objects and executes the sql statements on 
 * every fitting database. 
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
//TODO Exceptions müssen noch geschrieben werden!
public class DatabaseDictionary 
{
	
	/* Constants ----------------------------------------------------------------*/
	
	
	
	
	private HashMap<String, ArrayList<DBConnector>> tableMapping = null; // the DBConnector objects mapped to the containing tables
	private ArrayList<DBConnector> dbConnectorList = null; // list of all exisiting DBConnector objects
	
	/**
	 * Constructor, creating the table mapping and the DBConnector list
	 */
	public DatabaseDictionary()
	{
		this.tableMapping = new HashMap<String,ArrayList<DBConnector>>();
		this.dbConnectorList = new ArrayList<DBConnector>();
	}

	
	
	/* Getter ------------------------------------------------------------------------- */
	/**
	 * Returns the DBConnector Map.
	 * @return HashMap<String, ArrayList<DBConnector>>
	 */
	//TODO if not used: delete!
	public HashMap<String, ArrayList<DBConnector>> getTableMapping() 
	{
		return tableMapping;
	}
	
	/**
	 * Returns a list of DBConnector, containing the given table.
	 * @return ArrayList<DBConnector
	 */
	//TODO if not used: delete!
	public ArrayList<DBConnector> getTableMapping(String table) 
	{
		return tableMapping.get(table);
	}
	
	/**
	 * Returns the DBConnector list, with all DBConnectors that are mapped.
	 * @return ArrayList<DBConnector>
	 */
	//TODO if not used: delete!
	public ArrayList<DBConnector> getDbConnectorList()
	{
		return this.dbConnectorList;
	}
	
	
	/* Setter ------------------------------------------------------------------------- */
	/**
	 * Overwrites the existing Map with the given map of DBConnectors
	 * @param HashMap<String, ArrayList<DBConnector>> dbConnector 
	 */
	public void setTableMapping(HashMap<String, ArrayList<DBConnector>> dbConnector) 
	{
		this.tableMapping = dbConnector;
	}
	
	/**
	 * Overwrites the existing list of DBConnectors with a given one.
	 * @param ArrayList<DBConnector> dbConnectorList
	 */
	public void setDbConnectorList(ArrayList<DBConnector> dbConnectorList)
	{
		this.dbConnectorList = dbConnectorList;
	}
	
	
	/* Private ------------------------------------------------------------------------- */
	/**
	 * Creates a table for all databases.
	 * @param SQLCreator sqlCreator
	 * @param String userName
	 * @throws ClinSysException 
	 */
	private void createTable(SQLCreator sqlCreator, String userName) 
							throws ClinSysException
	{
		String tableName;
		ArrayList<DBConnector> tempConnectorList;
		
		if (this.hasRights(userName, "Server_Create_Table", "Server", RIGHTS.CREATE.get()))
		{	// if the user has the rights to create tables	
			tableName = sqlCreator.getTable(); // get the table name
			
			if (this.tableMapping.containsKey(tableName))
			{ // if some dbs have the table already
				tempConnectorList = this.tableMapping.get(tableName); // get a list of the DBConnector objects containing the table
			}
			else
			{	
				tempConnectorList = new ArrayList<DBConnector>(); // create a new list
			}
	
			for (DBConnector db: this.dbConnectorList)
			{ // go through all databases
				if (!tempConnectorList.contains(db))
				{ // if the db is a db not containing the table 
					try 
					{
						db.executeStatement(sqlCreator); // execute the sql statement
						tempConnectorList.add(db); // and add the database to the DBConnector list 
					}
					catch (ClinSysException e) 
					{
						throw new ClinSysException("(DatabaseDictionary.createTable(SQLCreator sqlCreator, String userName)) : Can't execute the sql statement given by the SQLCreator object");
					}
				}			
			}
			
			this.tableMapping.put(sqlCreator.getTable(), tempConnectorList); // add the DBConnector list to the table mapping	
		}
		else
		{
			throw new ClinSysException("(DatabaseDictionary.createTable(SQLCreator sqlCreator, String userName)) : The user don't own the rights to create a table");
		}
	}
	
	/**
	 * Creates a table for a given database
	 * @param SQLCreator sqlCreator
	 * @param DBConnector dbConnector
	 * @param String userName
	 * @throws ClinSysException 
	 */
	private void createTable(SQLCreator sqlCreator, DBConnector db, String userName) 
							throws ClinSysException
	{
		String tableName;
		ArrayList<DBConnector> tempConnectorList;		

		if (this.hasRights(userName, "Server_Create_Table", "Server", RIGHTS.CREATE.get()))
		{ // if the user has the rights
			tableName = sqlCreator.getTable(); // get the table name
			
			// check if already some databases containing this table and get a list or create a new list
			if (this.tableMapping.containsKey(tableName))
			{
				tempConnectorList = this.tableMapping.get(tableName);
			}
			else
			{
				tempConnectorList = new ArrayList<DBConnector>();
			}
			
			if (!tempConnectorList.contains(db))
			{ // if our db don't has already this table
				try 
				{
					db.executeStatement(sqlCreator); // execute the create statement
					tempConnectorList.add(db); // add the database to the database list
				} 
				catch (ClinSysException e) 
				{
					throw new ClinSysException("(DatabaseDictionary.createTable(SQLCreator sqlCreator, DBConnector db, String userName)) : Can't execute the sql statement given by the SQLCreator object");
				}
				// add the database list to the mapping
				this.tableMapping.put(sqlCreator.getTable(), tempConnectorList);
			}		
		}
		else
		{
			throw new ClinSysException("(DatabaseDictionary.createTable(SQLCreator sqlCreator, DBConnector db, String userName)) : The user don't own the rights to create a table");
		}
	}
	
		
	/**
	 * Drops a table on all databeses
	 * @param SQLCreator sqlCreator
	 * @param String userName
	 * @throws ClinSysException 
	 */
	private void dropTable(SQLCreator sqlCreator, String userName) 
							throws ClinSysException 
	{
		String tableName;
		ArrayList<DBConnector> tempConnectorList;
		
		// if the user has the rights
		if (this.hasRights(userName, "Server_Drop_Table", "Server", RIGHTS.DELETE.get()))
		{
			tableName = sqlCreator.getTable(); // get the table name
			
			// get a list of databases containing the table
			if(this.tableMapping.containsKey(tableName))
			{
				tempConnectorList = this.tableMapping.get(tableName);
				// go through all DBConnectors of the list
				for (DBConnector db : tempConnectorList)
				{
					try 
					{
						db.executeStatement(sqlCreator); // execute the drop statement
					} 
					catch (ClinSysException e) 
					{
						throw new ClinSysException("(DatabaseDictionary.dropTable(SQLCreator sqlCreator, String userName)) : Can't execute the sql statement given by the SQLCreator object");
					}
				}
				this.tableMapping.remove(tableName); // remove the list from the mapping
			}
		}
		else
		{
			throw new ClinSysException("(DatabaseDictionary.dropTable(SQLCreator sqlCreator, String userName)) : The user don't own the rights to drop a table");
		}
	}
		
	/**
	 * Drops a table in a given database.
	 * @param SQLCreator sqlCreator
	 * @param DBConnector db
	 * @param String userName
	 * @throws ClinSysException 
	 */
	private void dropTable(SQLCreator sqlCreator, DBConnector db, String userName) 
							throws ClinSysException
	{
		String tableName;
		int index;
		ArrayList<DBConnector> tempConnectorList;
		
		// if the user has the rights
		if (this.hasRights(userName, "Server_Drop_Table", "Server", RIGHTS.DELETE.get()))
		{
			tableName = sqlCreator.getTable(); // get the table name
			
			if (this.tableMapping.containsKey(tableName))
			{ 
				// get a list containing the databases with the table
				tempConnectorList = this.tableMapping.get(tableName);
				// get the index of the given database in the list
				index = tempConnectorList.indexOf(db);
				if (index!=-1)
				{ // if we found the database
					try 
					{
						// execute the drop statement
						db.executeStatement(sqlCreator);
					} 
					catch (ClinSysException e) 
					{
						throw new ClinSysException("(DatabaseDictionary.dropTable(SQLCreator sqlCreator, DBConnector db, String userName)) : Can't execute the sql statement given by the SQLCreator object");
					}  
					tempConnectorList.remove(index); // and remove the database from the list
				}
			}
		}
		else
		{
			throw new ClinSysException("(DatabaseDictionary.dropTable(SQLCreator sqlCreator, DBConnector db, String userName)) : The user don't own the rights to drop a table");
		}
	}
	
	/**
	 * Executes the given sql Statement, for the given user, for all databases.
	 * @param SQLCreator sqlCreator
	 * @param String userName
	 * @return RecordSet
	 * @throws ClinSysException
	 */
	private RecordSet doSQL(SQLCreator sqlCreator, String userName) 
							throws ClinSysException
	{
		ArrayList<DBConnector> dbConnectors = null;
		RecordSet result = null;

		// switch over the statement type 
		switch (sqlCreator.getStatementType())
		{
		case CREATE: // if it's a create statment
			switch (sqlCreator.getSQLObjectType())
			{
			case TABLE: // and we should create a table
				this.createTable(sqlCreator, userName); // call the createTable function
				break;
			default:
				Debugger.debug(sqlCreator.getSql());
				throw new ClinSysException("(DatabaseDictionary.doSQL(SQLCreator sqlCreator, String userName) : " + 
												"Undefined SQL object type was given. Can't execute the statement.");
			}				
			break;
		case DROP: // if it's a drop statement 
			switch (sqlCreator.getSQLObjectType())
			{
			case TABLE: // and we should drop a table 
				this.dropTable(sqlCreator, userName); // call the dropTable function
				break;
			default:
				Debugger.debug(sqlCreator.getSql());
				throw new ClinSysException("(DatabaseDictionary.doSQL(SQLCreator sqlCreator, String userName) : " + 
												"Undefined SQL object type was given. Can't execute the statement.");
			}
			break;
		default: // if it's another statement type
			switch (sqlCreator.getConcat())
			{ // switch if it's a concatenation or not
			case NONE: // it's no concatenation
				switch (sqlCreator.getStatementType())
				{
				case SELECT:
					dbConnectors = this.getTableMapping(sqlCreator.getTable()); // get a list of DBConnector objects containing the table
					Debugger.debug(sqlCreator.getTable());
					Debugger.debug(sqlCreator.getSql());
					if(dbConnectors.size()>0)
					{ // if the list is not empty
						// convert the first ResultSet to a RecordSet
						result = new RecordSet(dbConnectors.get(0).executeStatement(sqlCreator), sqlCreator.getTable(), sqlCreator.getAlias());
						// convert all other ResultSets to a RecordSet and append them to the result
						for (int i=1;i<dbConnectors.size(); i++)
						{
							try 
							{
								result = union(result,dbConnectors.get(i).executeStatement(sqlCreator), sqlCreator.getTable(), sqlCreator.getAlias());
							} 
							catch (ClinSysException e) 
							{
								Debugger.debug(e.getMessage());
								Debugger.debug(sqlCreator.getSql());
								throw new ClinSysException("(DatabaseDictionary.doSQL(SQLCreator sqlCreator, String userName) : " +
																"Can't merge the recordsets.");
							}
						}
					}
					break;
				default:
					dbConnectors = this.getTableMapping(sqlCreator.getTable());
					Debugger.debug(sqlCreator.getTable());
					Debugger.debug(sqlCreator.getSql());
					for (int i=0; i<dbConnectors.size(); i++)
					{
						try 
						{
							result = RecordSet.convertToRecordset(dbConnectors.get(i).executeStatement(sqlCreator), sqlCreator.getTable(), sqlCreator.getAlias());
						} 
						catch (ClinSysException e) 
						{
							Debugger.debug(e.getMessage());
							Debugger.debug(sqlCreator.getSql());
							throw new ClinSysException("(DatabaseDictionary.doSQL(SQLCreator sqlCreator, String userName) : " +
															"Can't merge the recordsets.");
						}
					}
					break;
				}
				break;
			default: // it's a concatenation i.e. concatenate the results 
				result = this.concat(sqlCreator);				
			}			
		}		
		return result;
	}
	
	/**
	 * Executes the given sql statement for the given database and the given user.
	 * @param DBConnector db
	 * @param SQLCreator sqlCreator
	 * @param String userName
	 * @return RecordSet
	 * @throws ClinSysException
	 */
	private RecordSet doSQL(DBConnector db, SQLCreator sqlCreator, String userName) 
							throws ClinSysException
	{
		RecordSet result = null;
		
		// switch over the statement type
		switch (sqlCreator.getStatementType())
		{
		case CREATE: // it's a create statement
			switch (sqlCreator.getSQLObjectType())
			{
			case TABLE: // and we should create a table
				this.createTable(sqlCreator, db, userName); // call the createTable function for the given database
				break;
			default:
				Debugger.debug(sqlCreator.getSql());
				throw new ClinSysException("(DatabaseDictionary.doSQL(DBConnector db, SQLCreator sqlCreator, String userName) : " + 
												"Undefined SQL object type was given. Can't execute the statement.");
			}				
			break;
		case DROP: // it's a drop statement
			switch (sqlCreator.getSQLObjectType())
			{
			case TABLE: // and we should drop a table 
				this.dropTable(sqlCreator, db, userName); // call the dropTable function for the given database
				break;
			default:
				Debugger.debug(sqlCreator.getSql());
				throw new ClinSysException("(DatabaseDictionary.doSQL(DBConnector db, SQLCreator sqlCreator, String userName) : " + 
												"Undefined SQL object type was given. Can't execute the statement.");
			}
			break;
		case SELECT:
			result = new RecordSet(db.executeStatement(sqlCreator), sqlCreator.getTable(), sqlCreator.getAlias());
			break;
		default: // it's something else, just execute it
			db.executeStatement(sqlCreator);							
			break;
		}		
		return result;
	}
	
	/**
	 * Checks if the rights for the user for the given function of the given plugin with the given level exist
	 * @param String userName
	 * @param String functionName
	 * @param String pluginName
	 * @param int level
	 * @return boolean
	 * @throws ClinSysException 
	 */
	@SuppressWarnings("unchecked")
	private boolean hasRights(String userName, String functionName, String pluginName, int rights) 
								throws ClinSysException
	{
		SQLCreator user = new SQLCreator(); // sqlcreator object to get the id of the user table
		SQLCreator plugin = new SQLCreator(); // sqlCreator object to get the plugin_id of the plugins table
		SQLCreator function = new SQLCreator(); // sqlCreator object to get the id of the plugin_functions table
		SQLCreator function_join = new SQLCreator(); // sqlcreator object to join the plugin- and function-sqlcreator-object to get the function id with one sql statement
		SQLCreator user_usergroups = new SQLCreator(); // sqlcreator object to get the usergroup_id of the user_usergroups table where the user_id is the given user
		SQLCreator usergroup_rights = new SQLCreator(); // sqlCreator object to get the rights for a usergroup
		SQLCreator usergroup_join = new SQLCreator(); // sqlCreator object to join the user_usergroups- and the usergroup-rights-object to get the rights for the usergroups in which the user is
		SQLCreator user_rights = new SQLCreator(); // sqlCreator object to get the rights of the user out of the user_rights table 
		RecordSet resultGroup; // RecordSet for the result of the usergroup_join-object
		RecordSet resultUser; // RecordSet for the result of the user_rights-object
		RecordSet result; // RecordSet for the results of the user- and the function_join-object
		int userId; // the found user_id
		int functionId; // the found function id
		boolean hasRight = false;
		
		try 
		{
			// Set up the user-object: SELECT id, name FROM user WHERE name = userName
			user.setStatementType(STATEMENTTYPE.SELECT);
			user.addItem(new String[]{"id", "name"}, SQLITEMTYPE.FIELD);
			user.addItem(SQLWORD.FROM);
			user.addItem(BASE_TABLES.TBL_USER.get(), SQLITEMTYPE.TABLE);
			user.addItem(SQLWORD.WHERE);
			user.addFieldItemPair("name", "=", userName, SQLITEMTYPE.TEXT);
			
			result = this.doSQL(user, userName); // get the result
			
			if (result.getRecordCount()!=1)
			{ // we didn't found a user -> the given user has no rights
				return false;
			}
			else
			{ // we found a user -> get the userId
				userId = (Integer) result.getValues("id").get(0);
			}
			
			// Set up the plugin-object: SELECT id, name FROM plugins WHERE name = pluginName
			plugin.setStatementType(STATEMENTTYPE.SELECT);
			plugin.addItem(new String[]{"id", "name"}, SQLITEMTYPE.FIELD);
			plugin.addItem(SQLWORD.FROM);
			plugin.addItem(BASE_TABLES.TBL_PLUGIN.get(), SQLITEMTYPE.TABLE);
			plugin.addItem(SQLWORD.WHERE);
			plugin.addFieldItemPair("name", "=", pluginName, SQLITEMTYPE.TEXT);			
			
			// Set up the function-object: SELECT id, name, plugin_id FROM plugin_functions WHERE name = functionName
			function.setStatementType(STATEMENTTYPE.SELECT);
			function.addItem(new String[]{"id", "name", "plugin_id"}, SQLITEMTYPE.FIELD);
			function.addItem(SQLWORD.FROM);
			function.addItem(BASE_TABLES.TBL_PLUGIN_FUNCTION.get(), SQLITEMTYPE.TABLE);
			function.addItem(SQLWORD.WHERE);
			function.addFieldItemPair("name", "=", functionName, SQLITEMTYPE.TEXT);
			
			// set up the function_join-object: SELECT id, name, plugin_id FROM plugin_functions WHERE name = functionName AS Function 
			//								    INNER JOIN (SELECT id, name FROM plugins WHERE name = pluginName) AS Plugin ON Function.plugin_id = Plugin.id
			function_join.setLeftPart(function);
			function_join.setRightPart(plugin);
			function_join.setConcat(CONCAT.INNER, "Function", "Plugin");
			function_join.setOnFields(new String[]{"Function.plugin_id", "Plugin.id"});
			result = this.doSQL(function_join, userName); // get the result
						
			if (result.getRecordCount()!=1)
			{ // we didn't found the given function -> no rights for this function granted
				return false;
			}
			else
			{ // we found the function -> get the functionId
				functionId = (Integer) result.getValues("Function.id").get(0);
			}
			
			// Set up the user_usergroups-object: SELECT user_id, usergroup_id FROM user_usergroups WHERE user_id = userId
			user_usergroups.setStatementType(STATEMENTTYPE.SELECT);
			user_usergroups.addItem(new String[]{"user_id", "usergroup_id"}, SQLITEMTYPE.FIELD);
			user_usergroups.addItem(SQLWORD.FROM);
			user_usergroups.addItem(BASE_TABLES.TBL_USER_USERGROUP.get(), SQLITEMTYPE.TABLE);
			user_usergroups.addItem(SQLWORD.WHERE);
			user_usergroups.addFieldItemPair("user_id", "=", userId, SQLITEMTYPE.INT);
			
			// Set up the usergroup_rights-object: SELECT usergroup_id, plugin_function_id, level FROM usergroups_rights 
			//										WHERE plugin_fnction_id = functionId AND level = level
			usergroup_rights.setStatementType(STATEMENTTYPE.SELECT);
			usergroup_rights.addItem(new String[]{"usergroup_id", "plugin_function_id", "rights"}, SQLITEMTYPE.FIELD);
			usergroup_rights.addItem(SQLWORD.FROM);
			usergroup_rights.addItem(BASE_TABLES.TBL_USERGROUP_RIGHTS.get(), SQLITEMTYPE.TABLE);
			usergroup_rights.addItem(SQLWORD.WHERE);
			usergroup_rights.addFieldItemPair("plugin_function_id", "=", functionId, SQLITEMTYPE.INT);
			usergroup_rights.addItem(SQLWORD.AND);
			usergroup_rights.addFieldItemPair("rights", ">=", rights, SQLITEMTYPE.INT);
			
			// set up the usergroup_join-object: SELECT usergroup_id, plugin_function_id, level FROM usergroups_rights 
			//										WHERE plugin_fnction_id = functionId AND level = level AS Rights 
			//										INNER JOIN (SELECT user_id, usergroup_id FROM user_usergroups WHERE user_id = userId) AS Groups
			//										ON Rights.usergroup_id = Groups.usergroup_id
			usergroup_join.setLeftPart(usergroup_rights);
			usergroup_join.setRightPart(user_usergroups);
			usergroup_join.setConcat(CONCAT.INNER, "Rights", "Groups");
			usergroup_join.setOnFields(new String[]{"Rights.usergroup_id", "Groups.usergroup_id"});
			
			// Set up the user_rights-object: SELECT user_id, plugin_function_id, level FROM user_rights
			//									WHERE user_id = userId AND plugin_function_id = functionId AND level = level
			user_rights.setStatementType(STATEMENTTYPE.SELECT);
			user_rights.addItem(new String[]{"user_id", "plugin_function_id", "rights"}, SQLITEMTYPE.FIELD);
			user_rights.addItem(SQLWORD.FROM);
			user_rights.addItem(BASE_TABLES.TBL_USER_RIGHTS.get(), SQLITEMTYPE.TABLE);
			user_rights.addItem(SQLWORD.WHERE);
			user_rights.addFieldItemPair("user_id", "=", userId, SQLITEMTYPE.INT);
			user_rights.addItem(SQLWORD.AND);
			user_rights.addFieldItemPair("plugin_function_id", "=", functionId, SQLITEMTYPE.INT);
			user_rights.addItem(SQLWORD.AND);
			user_rights.addFieldItemPair("rights", ">=", rights, SQLITEMTYPE.INT);
		} 
		catch (ClinSysException e) 
		{
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(DatabaseDictionary.hasRights(String userName, String functionName, String pluginName, int rights)) : Could not create the needed SQLCreator objects");
		}
		
		// get the results
		resultGroup = this.doSQL(usergroup_join, userName);
		resultUser = this.doSQL(user_rights, userName);
			
		// if one of the results has at least one record with the correct rights then the user has the required rights
		for (int right : (ArrayList<Integer>)resultGroup.getValues("Rights.rights"))
		{
			if (RIGHTS.hasRight(right, rights))
			{
				hasRight = true;
				break;
			}
		}
		
		if (!hasRight)
		{
			for (int right : (ArrayList<Integer>)resultUser.getValues("rights"))
			{
				if (RIGHTS.hasRight(right, rights))
				{
					hasRight = true;
					break;
				}
			}
		}
				
		return hasRight;
	}
	
	
	/**
	 * Returns a Recordset containing the records of the given Recordset and the given Resultset.
	 * @param Recordset recordset
	 * @param ResultSet rs2
	 * @param String tableName2
	 * @param String alias2
	 * @return RecordSet
	 * @throws ClinSysException
	 */
	private RecordSet union(RecordSet recordset, ResultSet rs2, String tableName2, String alias2) throws ClinSysException
	{
		return this.union(recordset, new RecordSet(rs2, tableName2, alias2));
	}
	
	/**
	 * Returns a Recordset containing the records of both given RecordSets
	 * @param RecordSet rs1
	 * @param RecordSet rs2
	 * @return RecordSet
	 * @throws ClinSysException
	 */
	@SuppressWarnings("unchecked")
	private RecordSet union(RecordSet rs1, RecordSet rs2) throws ClinSysException
	{
		RecordSet result = new RecordSet(rs1);
		ArrayList<String> columnLabels = result.getFields(); // get all fields of the first Recordset (now equal to the result)
		int columnCount = columnLabels.size(); // get the columnCount
		int columnType; 
		ArrayList<?> values1;
		ArrayList<?> values2;
		
		if (columnCount!=rs2.getFields().size())
		{ // if the columnCount is not equal we can't merge the two recordsets
			throw new ClinSysException("(DatabaseDictionary.union(RecordSet rs1, RecordSet rs2)) : The two recordsets don't have the same column count. They can't be merged");
		}
		
		// go through all columns
		for(String columnLabel: columnLabels)
		{
			values1 = result.getValues(columnLabel); // get the values of the column of the first recordset (for this column equal to the result)
			
			if (!rs2.getFields().contains(columnLabel))
			{ // if the second recordset don't knows this table
				throw new ClinSysException("(DatabaseDictionary.union(RecordSet rs1, RecordSet rs2)) : The second recordset don't know the table" + columnLabel); // throw an error
			}
			
			values2 = rs2.getValues(columnLabel); // get the values of the column of the second recordset
				
			columnType = result.getType(columnLabel); // get the columntype
			if (columnType == rs2.getType(columnLabel))
			{ // if it's the same columnType in rs1 and rs2
				switch(columnType)
				{ //switch over the columnType, and add the values of rs2 to the result
				case Types.BINARY:
					ArrayList<InputStream> column1Binary = (ArrayList<InputStream>)values1;
					ArrayList<InputStream> column2Binary = (ArrayList<InputStream>)values2;
					
					for(int i=0; i<column2Binary.size(); i++)
					{
						column1Binary.add(column2Binary.get(i));
					}
					break;
				case Types.BOOLEAN:
					ArrayList<Boolean> column1Boolean = (ArrayList<Boolean>)values1;
					ArrayList<Boolean> column2Boolean = (ArrayList<Boolean>)values2;
					
					for(int i=0; i<column2Boolean.size(); i++)
					{
						column1Boolean.add(column2Boolean.get(i));
					}
					break;
				case Types.DOUBLE:
					ArrayList<Double> column1Double = (ArrayList<Double>)values1;
					ArrayList<Double> column2Double = (ArrayList<Double>)values2;
					
					for(int i=0; i<column2Double.size(); i++)
					{
						column1Double.add(column2Double.get(i));
					}
					break;
				case Types.INTEGER:
					ArrayList<Integer> column1Int = (ArrayList<Integer>)values1;
					ArrayList<Integer> column2Int = (ArrayList<Integer>)values2;
					
					for(int i=0; i<column2Int.size(); i++)
					{
						column1Int.add(column2Int.get(i));
					}
					break;
				case Types.VARCHAR:
					ArrayList<String> column1Text = (ArrayList<String>)values1;
					ArrayList<String> column2Text = (ArrayList<String>)values2;
					
					for(int i=0; i<column2Text.size(); i++)
					{
						column1Text.add(column2Text.get(i));
					}
					break;
				case Types.DATE:
					ArrayList<Date> column1Date = (ArrayList<Date>)values1;
					ArrayList<Date> column2Date = (ArrayList<Date>)values2;
					
					for(int i=0; i<column2Date.size(); i++)
					{
						column1Date.add(column2Date.get(i));
					}
					break;
				default:
					throw new ClinSysException("(DatabaseDictionary.union(RecordSet rs1, RecordSet rs2)) : Given column type" + columnType + " is not implemented!");
				}
			}
			else
			{
				throw new ClinSysException("(DatabaseDictionary.union(RecordSet rs1, RecordSet rs2)) : The two RecordSets don't have the same column type");
			}
		}		
		return rs1;
	}
	
		
	/**
	 * Concatenates the result of the sql-statement-parts of a given sql statement 
	 * @param SQLCreator sqlCreator
	 * @return Recordset
	 * @throws ClinSysException
	 */
	private RecordSet concat(SQLCreator sqlCreator) throws ClinSysException
	{
		RecordSet result = null; // recordset for the result
		RecordSet left = null; // recordset for the left part
		RecordSet right = null; // recordset for the right part
		ArrayList<DBConnector> dbConnector; 
		
		if (sqlCreator.getConcat()==CONCAT.NONE)
		{ // it's the lowest part of a concatenation i.e. the part which will be concatenated
			dbConnector = this.getTableMapping(sqlCreator.getTable()); // get the list of DBConnector-objects containing the table
			Debugger.debug(sqlCreator.getTable());
			if (dbConnector.size()>0)
			{ // if we have at least one result merge the results to one RecordSet
				try 
				{
					result = new RecordSet(dbConnector.get(0).executeStatement(sqlCreator), sqlCreator.getTable(), sqlCreator.getAlias());
				} 
				catch (ClinSysException e) 
				{
					throw new ClinSysException("(DatabaseDictionary.concat(SQLCreator sqlCreator)) : Can't execute sql statement given by the SQLCreator object");
				}
			}
			for (int i=1; i<dbConnector.size(); i++)
			{
				try 
				{
					result = this.union(result, dbConnector.get(i).executeStatement(sqlCreator), sqlCreator.getTable(), sqlCreator.getAlias());
				} 
				catch (ClinSysException e) 
				{
					throw new ClinSysException("(DatabaseDictionary.concat(SQLCreator sqlCreator)) : Can't execute sql statement given by the SQLCreator object");
				}
			}
		}
		else
		{ // it's a concatenation 
			left = this.concat(sqlCreator.getLeftPart()); // concatenate what is in the left part
			right = this.concat(sqlCreator.getRightPart()); // concatenate what is in the right part
			 // concatenate the left and the right part, by switching over the concat type and calling the specified function
			switch(sqlCreator.getConcat())
			{
			case INNER:
				result = innerJoin(left, right, sqlCreator.getOnFields());
				break;
			case LEFT:
				result = leftJoin(left, right, sqlCreator.getOnFields());
				break;
			case OUTER:
				result = outerJoin(left, right, sqlCreator.getOnFields());
				break;
			case RIGHT:
				result = leftJoin(right, left, sqlCreator.getOnFields());
				break;
			case UNION:
				try 
				{
					result = this.union(left, right);
				} 
				catch (ClinSysException e) 
				{
					throw new ClinSysException("(DatabaseDictionary.concat(SQLCreator sqlCreator)) : Can't union the left and right part of the sql statement");
				}
			default:
				throw new ClinSysException("(DatabaseDictionary.concat(SQLCreator sqlCreator)) : Unimplemented concatenation type");
			}
		}
		result.setAlias(sqlCreator.getAlias());
		return result;
	}
	

	
	/**
	 * Returns an empty Recordset, containing all fields of both given recordsets with the used fieldMapping as column label
	 * @param Recordset rs1
	 * @param Recordset rs2
	 * @return Recordset
	 * @throws ClinSysException
	 */
	private RecordSet createJoinedStructure(RecordSet rs1, RecordSet rs2) throws ClinSysException
	{
		ArrayList<String> fieldNames1;
		ArrayList<String> fieldNames2;
		int columnType;		
		RecordSet result = new RecordSet();
		
		fieldNames1 = rs1.getFields(); // get all field names of the first recordset
		fieldNames2 = rs2.getFields(); // get all field names of the second recordset
				
		// go through all records of the first recordset
		for (String columnLabel : fieldNames1)
		{			
			columnType = rs1.getType(columnLabel); // get the columntype of the first recordset
			
			// switch over the columntype and create the 
			switch(columnType)
			{
			case Types.BINARY:
				result.addField(columnLabel, columnType, new ArrayList<InputStream>());
				break;
			case Types.BOOLEAN:
				result.addField(columnLabel, columnType, new ArrayList<Boolean>());
				break;
			case Types.DOUBLE:
				result.addField(columnLabel, columnType, new ArrayList<Double>());
				break;
			case Types.INTEGER:
				result.addField(columnLabel, columnType, new ArrayList<Integer>());
				break;
			case Types.VARCHAR:
				result.addField(columnLabel, columnType, new ArrayList<String>());
				break;
			case Types.DATE:
				result.addField(columnLabel, columnType, new ArrayList<Date>());
				break;
			default:
				throw new ClinSysException("(DatabaseDictionary.createJoinedStructure(RecordSet rs1, RecordSet rs2)) : Unimplemented column type was given!");				
			}
		}
		
		// go through all records of the second recordset
		for (String columnLabel : fieldNames2)
		{			
			columnType = rs2.getType(columnLabel); // get the columntype of the first recordset
			
			// switch over the columntype and create the 
			switch(columnType){
			case Types.BINARY:
				result.addField(columnLabel, columnType, new ArrayList<InputStream>());
				break;
			case Types.BOOLEAN:
				result.addField(columnLabel, columnType, new ArrayList<Boolean>());
				break;
			case Types.DOUBLE:
				result.addField(columnLabel, columnType, new ArrayList<Double>());
				break;
			case Types.INTEGER:
				result.addField(columnLabel, columnType, new ArrayList<Integer>());
				break;
			case Types.VARCHAR:
				result.addField(columnLabel, columnType, new ArrayList<String>());
				break;
			case Types.DATE:
				result.addField(columnLabel, columnType, new ArrayList<Date>());
				break;
			default:
				throw new ClinSysException("(DatabaseDictionary.createJoinedStructure(RecordSet rs1, RecordSet rs2)) : Unimplemented column type was given!");						
			}
		}
		return result;
	}
	
	
	private RecordSet changeFieldNameToMapping(RecordSet rs)
	{
		RecordSet result = new RecordSet(rs);
		ArrayList<String> fields = new ArrayList<String>(result.getFields());
		String fieldMapping;		
		for (String columnLabel : fields)
		{
			fieldMapping = rs.createFieldMapping(columnLabel);
			result.changeFieldName(columnLabel, fieldMapping);
		}
		return result;
	}
	
	/**
	 * Returns a RecordSet containing the Inner Join of the two given RecordSets on the given fields
	 * @param Recordset rs1
	 * @param Recordset rs2
	 * @param String[2] onFields
	 * @return RecordSet
	 * @throws ClinSysException
	 */
	private RecordSet innerJoin(RecordSet rs1, RecordSet rs2, String[] onFields) throws ClinSysException
	{
		RecordSet tempRs1;
		RecordSet tempRs2;
		ArrayList<?> onValues1;
		ArrayList<?> onValues2;
		ArrayList<?> values;
		ArrayList<String> fieldNames1;
		ArrayList<String> fieldNames2;
		ArrayList<String> tempFieldNames;
		HashMap<String, Object> tempValues;	
		RecordSet result = null;
		
		if (onFields.length!=2)
		{ // there must be exact 2 given field names to join on
			throw new ClinSysException("(DatabseDictionary.innerJoin(RecordSet rs1, RecordSet rs2, String[] onFields)) : There are more or less than two fields given for the 'ON' part of the sql statement.");
		}
		else
		{
			// map the field names
			tempRs1 = this.changeFieldNameToMapping(rs1);
			tempRs2 = this.changeFieldNameToMapping(rs2);
			
			result = createJoinedStructure(tempRs1, tempRs2); // create a new RecordSet for the result			
			
			onValues1 = tempRs1.getValues(onFields[0]); // get the values of the first on field
			onValues2 = tempRs2.getValues(onFields[1]); // get the values of the second on field
			fieldNames1 = tempRs1.getFields(); // get all field names of the first recordset
			fieldNames2 = tempRs2.getFields(); // get all field names of the second recordset
			
			
			// go through all records of the first recordset
			for (int i = 0; i<onValues1.size(); i++)
			{
				// take only records, that have equal onValues
				for (int j=0; j<onValues2.size(); j++)
				{
					if (onValues2.get(j)==onValues1.get(i))
					{
						// initialize the temporary lists
						tempValues = new HashMap<String, Object>();
						tempFieldNames = new ArrayList<String>();
						
						// go through all columns of the first recordset
						for (String columnLabel : fieldNames1)
						{					
							values = tempRs1.getValues(columnLabel); // get the values of the first recordset of this column
							tempValues.put(columnLabel, values.get(i)); // add the value of the column of this record to the temporary list
							tempFieldNames.add(columnLabel); // add the fieldMapping name for this column to the temporary list
						}
						
						// go through all columns of the second recordset
						for (String columnLabel : fieldNames2)
						{
							values = tempRs2.getValues(columnLabel); // get the values of the second recordset of this column
							tempValues.put(columnLabel, values.get(j)); // add the value of the column of this record to the temporary list
							tempFieldNames.add(columnLabel); // add the fieldMapping name for this column to the temporary list
						}
						
						// add the row
						result.addRow(tempFieldNames, tempValues);
					}
				}
			}
		}		
		return result;
	}
	
	/**
	 * Returns a Recordset containing the left join of both given recordsets
	 * @param Recordset rs1
	 * @param Recordset rs2
	 * @param String[2] onFields
	 * @return Recordset
	 * @throws ClinSysException
	 */
	private RecordSet leftJoin(RecordSet rs1, RecordSet rs2, String[] onFields) throws ClinSysException
	{
		RecordSet tempRs1;
		RecordSet tempRs2;
		ArrayList<?> onValues1;
		ArrayList<?> onValues2;
		ArrayList<?> values;
		ArrayList<String> fieldNames1;
		ArrayList<String> fieldNames2;
		ArrayList<String> tempFieldNames;
		HashMap<String, Object> tempValues;
		RecordSet result = null;
		
		if (onFields.length!=2)
		{ // there must be exact 2 given field names to join on
			throw new ClinSysException("(DatabseDictionary.leftJoin(RecordSet rs1, RecordSet rs2, String[] onFields)) : There are more or less than two fields given for the 'ON' part of the sql statement.");
		}
		else
		{
			// map the field names
			tempRs1 = this.changeFieldNameToMapping(rs1);
			tempRs2 = this.changeFieldNameToMapping(rs2);
			
			result = createJoinedStructure(tempRs1, tempRs2); // create a new RecordSet for the result	
			
			onValues1 = tempRs1.getValues(onFields[0]); // get the values of the first on field
			onValues2 = tempRs2.getValues(onFields[1]); // get the values of the second on field
			fieldNames1 = tempRs1.getFields(); // get all field names of the first recordset
			fieldNames2 = tempRs2.getFields(); // get all field names of the second recordset
				
			// go through all records of the first recordset
			for (int i = 0; i<onValues1.size(); i++)
			{
				// take all values of the first recordset
				// initialize the temporary lists
				tempValues = new HashMap<String, Object>();
				tempFieldNames = new ArrayList<String>();
				
				// go through all columns of the first recordset
				for (String columnLabel : fieldNames1)
				{				
					values = tempRs1.getValues(columnLabel); // get the values of the first recordset of this column
					tempValues.put(columnLabel, values.get(i)); // add the value of the column of this record to the temporary list
					tempFieldNames.add(columnLabel); // add the fieldMapping name for this column to the temporary list
				}
				
				if (onValues2.contains(onValues1.get(i)))
				{	 // if the second recordset has the same value as the on field then add the other values			
					// go through all columns of the second recordset
					for (String columnLabel : fieldNames2)
					{
						values = tempRs2.getValues(columnLabel); // get the values of the second recordset of this column
						tempValues.put(columnLabel, values.get(onValues2.indexOf(onValues1.get(i)))); // add the value of the column of this record to the temporary list
						tempFieldNames.add(columnLabel); // add the fieldMapping name for this column to the temporary list
					}					
				}
				else
				{ // if the second recordset has not the same value as the on field then just add null
					// go through all columns of the second recordset
					for (String columnLabel : fieldNames2)
					{						
						tempValues.put(columnLabel, null); // add the value of the column of this record to the temporary list
						tempFieldNames.add(columnLabel); // add the fieldMapping name for this column to the temporary list
					}			
				}
				// add the row
				result.addRow(tempFieldNames, tempValues);
			}
		}		
		return result;
	}
	
	/**
	 * Returns a recordset containing the outer join of both given recordsets, i.e. all values of the first and second recordset 
	 * @param Recordset rs1
	 * @param Recordset rs2
	 * @param String[2] onFields
	 * @return Recordset
	 * @throws ClinSysException
	 */
	private RecordSet outerJoin(RecordSet rs1, RecordSet rs2, String[] onFields) throws ClinSysException
	{
		RecordSet tempRs1;
		RecordSet tempRs2;
		ArrayList<?> onValues1;
		ArrayList<?> onValues2;
		ArrayList<?> values;
		ArrayList<String> fieldNames1;
		ArrayList<String> fieldNames2;
		ArrayList<String> tempFieldNames;
		HashMap<String, Object> tempValues;
		RecordSet result = null;
		
		if (onFields.length!=2)
		{ // there must be exact 2 given field names to join on
			throw new ClinSysException("(DatabseDictionary.outerJoin(RecordSet rs1, RecordSet rs2, String[] onFields)) : There are more or less than two fields given for the 'ON' part of the sql statement.");
		}
		else
		{
			// map the field names
			tempRs1 = this.changeFieldNameToMapping(rs1);
			tempRs2 = this.changeFieldNameToMapping(rs2);
			
			result = createJoinedStructure(tempRs1, tempRs2); // create a new RecordSet for the result	
			
			onValues1 = tempRs1.getValues(onFields[0]); // get the values of the first on field
			onValues2 = tempRs2.getValues(onFields[1]); // get the values of the second on field
			fieldNames1 = tempRs1.getFields(); // get all field names of the first recordset
			fieldNames2 = tempRs2.getFields(); // get all field names of the second recordset
				
			// go through all records of the first recordset
			for (int i = 0; i<onValues1.size(); i++)
			{
				// take all values of the first recordset
				// initialize the temporary lists
				tempValues = new HashMap<String, Object>();
				tempFieldNames = new ArrayList<String>();
				
				// go through all columns of the first recordset
				for (String columnLabel : fieldNames1)
				{				
					values = tempRs1.getValues(columnLabel); // get the values of the first recordset of this column
					tempValues.put(columnLabel, values.get(i)); // add the value of the column of this record to the temporary list
					tempFieldNames.add(columnLabel); // add the fieldMapping name for this column to the temporary list
				}
				
				if (onValues2.contains(onValues1.get(i)))
				{	 // if the second recordset has the same value as the on field then add the other values			
					// go through all columns of the second recordset
					for (String columnLabel : fieldNames2)
					{
						values = tempRs2.getValues(columnLabel); // get the values of the second recordset of this column
						tempValues.put(columnLabel, values.get(onValues2.indexOf(onValues1.get(i)))); // add the value of the column of this record to the temporary list
						tempFieldNames.add(columnLabel); // add the fieldMapping name for this column to the temporary list
					}					
				}
				else
				{ // if the second recordset has not the same value as the on field then just add null
					// go through all columns of the second recordset
					for (String columnLabel : fieldNames2)
					{						
						tempValues.put(columnLabel, null); // add the value of the column of this record to the temporary list
						tempFieldNames.add(columnLabel); // add the fieldMapping name for this column to the temporary list
					}			
				}
				
				// add the row
				result.addRow(tempFieldNames, tempValues);
			}
			
			// go through all records of the second recordset
			for (int i = 0; i<onValues2.size(); i++)
			{
				// if the onValues of the second recordset is not in the first then
				if (!onValues1.contains(onValues2.get(i)))
				{
					// initialize the temporary lists
					tempValues = new HashMap<String, Object>();
					tempFieldNames = new ArrayList<String>();
					
					// go through all columns of the first recordset
					for (String columnLabel : fieldNames1)
					{											
						tempValues.put(columnLabel, null); // add null to the temporary list cause it has no value
						tempFieldNames.add(columnLabel); // add the fieldMapping name for this column to the temporary list
					}
					
					// go through all columns of the second recordset
					for (String columnLabel : fieldNames2)
					{
						values = tempRs2.getValues(columnLabel); // get the values of the second recordset of this column
						tempValues.put(columnLabel, values.get(i)); // add the value of the column of this record to the temporary list
						tempFieldNames.add(columnLabel); // add the fieldMapping name for this column to the temporary list
					}
					
					// add the row
					result.addRow(tempFieldNames, tempValues);
				}
			}
		}
		return result;
	}
	
	/* Public ------------------------------------------------------------------------- */
	/**
	 * Adding a DBConnector to the list and the map.
	 * @param dbConnector
	 * @throws ClinSysException 
	 */
	public void addDbConnector(DBConnector dbConnector) throws ClinSysException
	{
		ArrayList<String> tables = null;
		try 
		{
			tables = dbConnector.getTableList(); // get the list of tables contained by the DBConnector
		} 
		catch (ClinSysException e) 
		{
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(DatabaseDictionary.addDbConnector(DBConnector)) : Cannot get the table list." + 
											" Adding the DBConnector is impossible.");
		}	
		for(String table: tables)
		{ // go through all tables
			ArrayList<DBConnector> tempList = this.tableMapping.get(table); // get the list of DBConnector-objects containing this table
			if (tempList!=null)
			{ // if the list already exist
				if (!tempList.contains(dbConnector))
				{ // and the DBConnector is not aready in
					tempList.add(dbConnector);  // add it
					this.tableMapping.put(table, tempList); // replace the list
				}
			}
			else
			{ // the list does not exist
				tempList = new ArrayList<DBConnector>(); // create a new one
				tempList.add(dbConnector); // add the dbconnector
				this.tableMapping.put(table, tempList); // add it to the mapping
			}				
		}
		if (!this.dbConnectorList.contains(dbConnector))
		{
			this.dbConnectorList.add(dbConnector);	// add the dbconnector to the general dbconnector list
		}
	}
	
	/**
	 * Adding a list of DBConnectors to the map and the list.
	 * @param dbConnector
	 * @throws ClinSysException 
	 */
	public void addDbConnector(DBConnector[] dbConnectors) throws ClinSysException
	{
		for(DBConnector dbConnector: dbConnectors)
		{ 
			this.addDbConnector(dbConnector);
		}
	}
	
	/**
	 * Executes a given sql statement if the user has the rights to
	 * @param SQLCreator sqlCreator
	 * @param String userName
	 * @param String functionName
	 * @param String pluginName
	 * @param int securityLevel (using the RIGHTS enumeration)
	 * @return RecordSet
	 * @throws ClinSysException
	 */
	public RecordSet executeSQL(SQLCreator sqlCreator, String userName, String functionName, String pluginName, int securityLevel) throws ClinSysException
	{
		RecordSet result = null;	
		
		if (this.hasRights(userName, functionName, pluginName, securityLevel))
		{ // the user has the rights
			result = this.doSQL(sqlCreator, userName); // do the sql
		}
		else
		{
			Debugger.debug(userName);
			Debugger.debug(functionName);
			Debugger.debug(pluginName);
			Debugger.debug("" + securityLevel);
			throw new ClinSysException("((DatabaseDictionary.executeSQL(SQLCreator sqlCreator, String userName, String functionName, String pluginName, int securityLevel)) : " +
					"The user " + userName + " doesn't have the required rights to execute the sql statement of the function " + functionName + " of the plugin " + pluginName);
		}		
		return result;
	}
	
	/**
	 * Executes a given sql statement for a given database if the user has the rights to
	 * @param DBConnector db
	 * @param SQLCreator sqlCreator
	 * @param String userName
	 * @param String functionName
	 * @param String pluginName
	 * @param int securityLevel (using the RIGHTS enumeration)
	 * @return RecordSet
	 * @throws ClinSysException
	 */
	public RecordSet executeSQL(DBConnector db, SQLCreator sqlCreator, String userName, String functionName, String pluginName, int securityLevel) throws ClinSysException
	{
		RecordSet result = null;	
		
		if (this.hasRights(userName, functionName, pluginName, securityLevel))
		{ // the user has the rights
			result = this.doSQL(db, sqlCreator, userName);  // do the sql for the given database
		}
		else
		{
			throw new ClinSysException("((DatabaseDictionary.executeSQL(DBConnector db, SQLCreator sqlCreator, String userName, String functionName, String pluginName, int securityLevel)) : " +
					"The user " + userName + " doesn't have the required rights to execute the sql statement of the function " + functionName + " of the plugin " + pluginName);
		}
		
		return result;
	}
	
	
	public void close() throws ClinSysException
	{
		for (DBConnector db : this.dbConnectorList)
		{
			db.close();
		}
		
		this.dbConnectorList = null;
		this.tableMapping = null;
	}



	/**
	 * Returns the used network Server object.
	 * @param controller TODO
	 * @return Server
	 */
	public Server getServer(Controller controller) 
	{
		return controller.getServer();
	}
}
