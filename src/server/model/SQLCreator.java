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
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import server.model.enumerations.CHANGETYPE;
import server.model.enumerations.CONCAT;
import server.model.enumerations.SQLITEMTYPE;
import server.model.enumerations.SQLOBJECTTYPE;
import server.model.enumerations.SQLWORD;
import server.model.enumerations.STATEMENTTYPE;
import shared.util.Debugger;
import shared.util.ClinSysException;

/**
 * Class, which accepts parts of a sql statement and returns a prepared statement.
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class SQLCreator {
	private ArrayList<Object> values = null; // stores the values, which will be binded to the prepared statement
	private ArrayList<SQLITEMTYPE> valuesTypes = null; // stores the sql item types for the binded values
	private STATEMENTTYPE statementType = STATEMENTTYPE.NONE; // stores the statement type
	private SQLOBJECTTYPE objType = SQLOBJECTTYPE.NONE;
	private CONCAT concat = CONCAT.NONE; // stores the concatenation type
	private String sql=null; // stores the raw sql string to create the prepared statement out of
	private SQLCreator leftPart = null; // stores the left part of a concatenated sql statement
	private SQLCreator rightPart = null; // stores the right part of a concatenated sql statement
	private PreparedStatement stmt=null; // stores the created prepared statement
	private String table = null; // stores the table used in this sql creator
	private String[] onFields = null;
	private String alias = null;
	

	
	/**
	 * Constructor
	 * Creates new lists for the values and the value types
	 */
	public SQLCreator(){
		this.values = new ArrayList<Object>();
		this.valuesTypes = new ArrayList<SQLITEMTYPE>();
	}
		
	
	
	/* Getter ------------------------------------------------------------------------- */
	/**
	 * Returns the table name.
	 * @return <code>String</code>
	 */
	public String getTable(){
		return this.table;
	}
	
	/**
	 * Returns the statement type.
	 * @return STATEMENTTYPE
	 */
	public STATEMENTTYPE getStatementType(){
		return this.statementType;
	}
	
	/**
	 * Returns the concatenation type.
	 * @return CONCAT
	 */
	public CONCAT getConcat(){
		return this.concat;
	}
	
	/**
	 * Returns the SQL object type.
	 * @return SQLOBJECTTYPE
	 */
	public SQLOBJECTTYPE getSQLObjectType(){
		return this.objType;
	}
	
	/**
	 * Public function, which returns the sql string of this class object.
	 * @return String
	 */
	public String getSql(){
		return this.sql;
	}	
	
	/**
	 * Public function, which returns a list of the masked values in the sql string. 
	 * Used in the getStmt function, to bind the values of a child SQLCreator. 
	 * @return ArrayList<Object>
	 */
	public ArrayList<Object> getValues(){
		return this.values;
	}	

	/**
	 * Public function, which returns a list of the field types properly to the value data types.
	 * Used in the getStmt function, to bind the values of a child SQLCreator
	 * @return ArrayList<FIELDTYPE>
	 */
	public ArrayList<SQLITEMTYPE> getValueTypes() {
		return this.valuesTypes;
	}
	
	/**
	 * Returns the left part of a concatenated SQL statement, or null if not existing.
	 * @return SQLCreator
	 */
	public SQLCreator getLeftPart(){
		return this.leftPart;
	}
	
	/**
	 * Returns the right part of a concatenated SQL statement, or null if not existing.
	 * @return SQLCreator
	 */
	public SQLCreator getRightPart(){
		return this.rightPart;
	}
	
	/**
	 * Returns the fields of the ON Part of a JOIN-Statement.
	 * @return String[2]
	 */
	public String[] getOnFields(){
		return this.onFields;
	}
	
	/**
	 * Returns the alias of this Object in a join statement.
	 * @return String
	 */
	public String getAlias(){
		return this.alias;
	}

	/**
	 * Uses the given connection to a sql server to create a prepared statement using the sql string 
	 * and the list of values and value types.
	 * The DBConnector class calls this function to execute the sql query
	 * Throws an ICTMException if the connection cannot create the prepared statement, 
	 * if the value and value type list have not the same size or if the cannot bind a value to the prepared statement.
	 * test <code>con</code> und <code>this</code> und noch <code>private</code>  
	 * @param Connection con
	 * @return PreparedStatement
	 * @throws ClinSysException
	 */
	public PreparedStatement getStmt(Connection con) throws ClinSysException{
		try{
			// create a prepared statement object
			if (this.statementType==STATEMENTTYPE.NONE){
				this.stmt = con.prepareStatement(STATEMENTTYPE.SELECT.get() + " * " + SQLWORD.FROM.get() + " " + this.getSql() + ";", 
									ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
			}else{
				this.stmt = con.prepareStatement(this.getSql() + ";",ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			}			
		}catch (SQLException e){
			throw new ClinSysException("(SQLCreator.getStmt(Connection)) : Creating a prepared statement" +
											" with the given connection was not possible.");
		}
		// check if the value and the value type list have the same size if not throw an exception
		if (!(this.values.size()==this.valuesTypes.size())){
			throw new ClinSysException("(SQLCreator.getStmt(Connection)) : The count of the items and item types are not equal. " +
											"Creating a prepared statement is not possible.");
		}
		// go through all value types (i.e. through all values as well)
		for (int i=0; i<this.valuesTypes.size();i++){
			// switch the value types
			switch (this.valuesTypes.get(i)){ 
			case TEXT: // it's a text 
				try {  // i.e. bind a string
					this.stmt.setString(i+1, (String)this.values.get(i));
				} catch (SQLException e) {
					Debugger.debug(this.sql);
					Debugger.debug((String)this.values.get(i));
					Debugger.debug(e.getMessage());
					throw new ClinSysException("(SQLCreator.getStmt(Connection) : Cannot set TEXT" + 
							" - Value: " + this.values.get(i));
				}
				break;
			case INT: // it's an int
				try { // i.e. bind an int
					this.stmt.setInt(i+1, ((Integer)this.values.get(i)).intValue());
				} catch (SQLException e) {
					throw new ClinSysException("(SQLCreator.getStmt(Connection) : Cannot set INT" +
							" - Value: " + this.values.get(i));
				}
				break;
			case DOUBLE: // it's a float
				try {   // i.e. bind a float
					this.stmt.setDouble(i+1, ((Double)this.values.get(i)).floatValue());
				} catch (SQLException e) {
					throw new ClinSysException("(SQLCreator.getStmt(Connection) : Cannot set FLOAT" +
							" - Value: " + this.values.get(i));
				}
				break;
			case BOOL:  // it's a bool
				try {   // i.e. bind a bool
					this.stmt.setBoolean(i+1, ((Boolean)this.values.get(i)).booleanValue());
				} catch (SQLException e) {
					throw new ClinSysException("(SQLCreator.getStmt(Connection) : Cannot set BOOL" +
							" - Value: " + this.values.get(i));
				}
				break;
			case DATE:  // it's a date object
				try {   // i.e. bind a date object
					this.stmt.setDate(i+1, (Date)this.values.get(i));
				} catch (SQLException e) {
					throw new ClinSysException("(SQLCreator.getStmt(Connection) : Cannot set DATE" +
							" - Value: " + ((Date)this.values.get(i)).toString());
				}
				break;
			case BINARY:  // it's a binary
				try {     // i.e. bind a binary stream
					this.stmt.setBinaryStream(i+1, (InputStream)this.values.get(i));
				} catch (SQLException e) {
					throw new ClinSysException("(SQLCreator.getStmt(Connection) : Cannot set BINARY" +
							" - Value: " + ((InputStream)this.values.get(i)).toString());
				}
				break;
			default: // it's an undefined SQL item type
				throw new ClinSysException("(SQLCreator.getStmt(Connection) : Undefined item type was used. " +
												"Creating a prepared statement is not possible.");		
			}			
		}		
		return this.stmt; // return the statement
	}
	
	
	
	
	/* Setter ------------------------------------------------------------------------- */	
	/**
	 * Sets the statement type, e.g. SELECT, UPDATE, etc.
	 * For a full list see the STATEMENTTYPE Java Doc. 
	 * @param STATEMENTTYPE statementType
	 */
	public void setStatementType(STATEMENTTYPE statementType){
		this.sql = statementType.get();
		this.statementType = statementType;
	}
	
	/**
	 * Sets the create type of a create statement e.g. DATABASE, TABLE, etc.
	 * If the statement type is not create a ICTMException will be thrown.
	 * For a full list see the CREATTYPE Java Doc.
	 * @param SQLOBJECTTYPE createType
	 * @throws ClinSysException
	 */
	public void setSQLObjectType(SQLOBJECTTYPE objType) throws ClinSysException{
		if ((this.statementType == STATEMENTTYPE.CREATE)||(this.statementType == STATEMENTTYPE.DROP)){
			this.sql += " " + objType.get();
			this.objType = objType;
		}else{
			throw new ClinSysException("(SQLCreator.setChangeType(" + objType.get() + 
											") : The given statement type (" + this.statementType.get() + 
											" does not accept a sql object type.)");
		}
	}
	
	/**
	 * Sets the change type of a alter statement e.g. ADD, MODIFY, etc.
	 * Throws an ICTMException if the statement type is not alter.  
	 * @param CHANGETYPE changeType
	 * @throws ClinSysException
	 */
	public void setChangeType(CHANGETYPE changeType) throws ClinSysException{
		if (this.statementType == STATEMENTTYPE.ALTER){ 
		}else{
			throw new ClinSysException("(SQLCreator.setChangeType(" + changeType.get() + 
											") : The given statement type (" + this.statementType.get() + 
											" does not accept a change type.)");
		}
	}

	/**
	 * Sets the concatenation type and sets the sql string of the concatenating object to the concatenated version. 
	 * Throws an ICTMException, if neither a left nor a right part was given to concatenate with.
	 * @param CONCAT concat
	 * @throws ClinSysException
	 */
	public void setConcat(CONCAT concat) throws ClinSysException{
		this.setConcat(concat, "", "");
	}
	
	/**
	 * Sets the concatenation type and sets the sql string of the concatenating object to the concatenated version. 
	 * Allows the use of aliases to rename the left and the right part.
	 * Throws an ICTMException, if neither a left nor a right part was given to concatenate with.
	 * @param CONCAT concat
	 * @param String alias1
	 * @param String alias2
	 * @throws ClinSysException
	 */
	public void setConcat(CONCAT concat, String alias1, String alias2) throws ClinSysException{	
		this.concat = concat; // store the concatenation type
		
		// it needs to have either a left or a right part
		if (this.leftPart==null || this.rightPart==null){
			throw new ClinSysException("(SQLCreator.setConcat(" + concat.get() + 
											") : Either the left or the right part are missing." + 
											"Cannot concatenate without both parts.");
		}else{ // both parts exist
			if ((this.leftPart.getStatementType()!=STATEMENTTYPE.SELECT)&&
					(this.leftPart.getConcat()==CONCAT.NONE)){
				throw new ClinSysException("(SQLCreator.setConcat(" + concat.get() + 
										") : The left part is neither a select statement " +
										"nor a concatenation of select statements." + 
										"Cannot concatenate other statements.");
			}
			
			if ((this.rightPart.getStatementType()!=STATEMENTTYPE.SELECT)&&
					(this.rightPart.getConcat()==CONCAT.NONE)){
				throw new ClinSysException("(SQLCreator.setConcat(" + concat.get() + 
										") : The right part is neither a select statement " +
										"nor a concatenation of select statements." + 
										"Cannot concatenate other statements.");
			}
			
			this.sql = this.leftPart.getSql(); // store the sql string of the left part
			
			// take the values and value types of the left part
			for(Object item : this.leftPart.getValues()){
				this.values.add(item);
			}
			for(SQLITEMTYPE type : this.leftPart.getValueTypes()){
				this.valuesTypes.add(type);
			}
			
			if (!alias1.equals("")){ // an alias for the left part exists
				this.leftPart.setAlias(alias1);
				this.sql += " AS " + alias1;
			}
			
			// add the concatenation type
			this.sql += " " + concat.get() + " ";
			
			// if the right part is a full statement (not just a table) or a concatenated statement as well then
			if ((this.rightPart.getStatementType()!=STATEMENTTYPE.NONE)||
					(this.rightPart.getConcat()!=CONCAT.NONE)){
				this.sql += "(" + this.rightPart.getSql() + ")"; // add brackets before and after the statement
			}else{ // if not 
				this.sql += this.rightPart.getSql(); // just add the table name
			}
			
			if (!alias2.equals("")){ // an alias for the right part exists
				this.rightPart.setAlias(alias2);
				this.sql += " AS " + alias2;
			}
			
			// take the values and the value types of the right part
			for(Object item : this.rightPart.getValues()){
				this.values.add(item);
			}
			for(SQLITEMTYPE type : this.rightPart.getValueTypes()){
				this.valuesTypes.add(type);
			}
		}
	}
	
	/**
	 * Sets the fields for the ON Part of a JOIN-Statement
	 * @param String[2] fields
	 * @throws ClinSysException
	 */
	public void setOnFields(String[] fields) throws ClinSysException{
		if (fields.length==2){
			this.onFields = fields;
			this.addItem(SQLWORD.ON);
			this.addFieldItemPair(fields[0], "=", fields[1], SQLITEMTYPE.FIELD);
		}else{
			throw new ClinSysException("(SQLCreator.setOnFields(String[])) : More or less than 2 Strings are given.");
		}
	}
	
	/**
	 * Sets the left part of a sql statement that should be concatenated.
	 * @param SQLCreator sqlCreator
	 */
	public void setLeftPart(SQLCreator sqlCreator){
		this.leftPart = sqlCreator;
	}
	
	
	/**
	 * Sets the right part of a sql statement that should be concatenated.
	 * @param SQLCreator sqlCreator
	 */
	public void setRightPart(SQLCreator sqlCreator){
		this.rightPart = sqlCreator;
	}
	
	/**
	 * Set the alias.
	 * @param String alias
	 */
	public void setAlias(String alias){
		this.alias = alias;
	}
	
	
	
	/* Private ------------------------------------------------------------------------- */	
	/**
	 * Adds a parameter to the sql string, mostly a field or a table name.
	 * Parameters do not appear in the values list.
	 * @param String param
	 */
	private void addParameter(String param){
		if (this.sql!= null){
			this.sql += " ";
		}
		this.sql +=param;
	}
	
	

	/* Public ------------------------------------------------------------------------- */	
	/**
	 * Adds a sql word e.g. FROM, WHERE, etc. to the sql string.
	 * For a full list see the SQLWORD Java Doc
	 * @param SQLWORD sqlWord
	 */
	public void addItem(SQLWORD sqlWord){
		this.sql += " " + sqlWord.get();
	}

	
	/**
	 * Adds a item. 
	 * Depending on the sql item type it will be a parameter (i.e. a field or a table name) or a value (e.g. INT, TEXT, etc.)
	 * If an undefined sql item type was given then it throws an ICTMException.  
	 * @param Object item
	 * @param SQLITEMTYPE type
	 * @throws ClinSysException
	 */
	public void addItem(Object item, SQLITEMTYPE type) throws ClinSysException{
		if (type==SQLITEMTYPE.FIELD){ // if it is a field
			this.addParameter((String)item); // just add the parameter
		}else if (type==SQLITEMTYPE.TABLE){ // if it's a table
			this.addParameter((String)item); // add the parameter
			this.table = (String)item; // and store the table name
		}else if (type==SQLITEMTYPE.TEXT){ // it's a text value
			//this.sql += "'"; // sql needs a ' for text values
			this.values.add((String)item);	// add the value to the values list
			this.valuesTypes.add(SQLITEMTYPE.TEXT);	 // add the sql item type text to the value types list
			this.sql += "?"; // add the wildcard and the ending ' to the sql string
		}else{
			switch (type){ // switch the remaining types and add the casted object to the values list
			case BINARY: 
				this.values.add((InputStream)item);
				break;
			case BOOL:
				this.values.add(((Boolean)item).booleanValue());
				break;
			case DATE:
				if (item instanceof java.util.Date){
					Date d = new Date(((java.util.Date) item).getTime());
					this.values.add(d);
				}else{
					this.values.add((Date)item);
				}
				
				break;
			case DOUBLE:
				this.values.add(((Double)item).doubleValue());
				break;
			case INT:
				this.values.add(((Integer)item).intValue());
				break;
			default:
				throw new ClinSysException("(SQLCreator.addItem(Object[])) : Undefined field type was given.");
			}
			this.sql += " ?"; // add the wildcard
			this.valuesTypes.add(type); // add the value type to the types list
		}		
	}
	
	
	/**
	 * Adds a list of items.
	 * Depending on the sql item type it will be parameter (i.e. a field or a table name) or values (e.g. INT, TEXT, etc.)
	 * If an undefined sql item type was given then it throws an ICTMException.
	 * @param Object[] items
	 * @param SQLITEMTYPE type
	 * @throws ClinSysException
	 */
	public void addItem(Object[] items, SQLITEMTYPE type) throws ClinSysException{
		boolean flag = false;		
		for(Object item: items){ // go through all items
			if (flag){ //after the first item add a "," to the sql string to seperate the list
				this.sql += ",";
			}
			this.addItem(item, type); // add the item
			flag = true;
		}
	}
	
	
	/**
	 * Adds a field item pair.
	 * Consisting of a field name, an operator and an item. 
	 * The item can be another field name e.g. for the ON part of a JOIN statement
	 * or it can be a value e.g. for the WHERE part.
	 * The added sql substring has the form: <field><operator><item>
	 * If an undefined sql item type was given then it throws an ICTMException.
	 * @param String field
	 * @param String operator
	 * @param Object item
	 * @param SQLITEMTYPE type
	 * @throws ClinSysException
	 */
	public void addFieldItemPair(String field, String operator, Object item, SQLITEMTYPE type) throws ClinSysException{
		this.addFieldItemPair(false, field, operator, item, type);
	}
	
	
	/**
	 * Adds a field item pair, that can be negated.
	 * Consisting of a field name, an operator and an item. 
	 * The item can be another field name e.g. for the ON part of a JOIN statement
	 * or it can be a value e.g. for the WHERE part.
	 * The added sql substring has the form: <(negate?"NOT":"")><field><operator><item>
	 * If an undefined sql item type was given then it throws an ICTMException.
	 * @param boolean negate
	 * @param String field
	 * @param String operator
	 * @param Object item
	 * @param SQLITEMTYPE type
	 * @throws ClinSysException
	 */
	public void addFieldItemPair(boolean negate, String field, String operator, Object item, SQLITEMTYPE type) throws ClinSysException{
		if (negate){
			this.addItem(SQLWORD.NOT); // add a "NOT" if the expression should be negated
		}
		this.addParameter(field); // add the field
		this.sql += " " + operator;	// the operator
		this.addItem(item, type); // and the item
	}
	
	/**
	 * Resets the object, to be used for a new sql statement.
	 */
	public void reset(){
		this.statementType = STATEMENTTYPE.NONE;
		this.concat = CONCAT.NONE;
		this.leftPart = null;
		this.rightPart = null;
		this.alias = null;
		this.objType = SQLOBJECTTYPE.NONE;
		this.onFields = null;
		this.sql = null;
		this.stmt = null;
		this.table = null;
		this.values = new ArrayList<Object>();
		this.valuesTypes = new ArrayList<SQLITEMTYPE>();
	}
}
