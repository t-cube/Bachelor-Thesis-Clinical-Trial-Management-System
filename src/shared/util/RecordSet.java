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



package shared.util;

import java.io.InputStream;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import shared.json.JSONArray;
import shared.json.JSONException;
import shared.json.JSONObject;


/**
 * Class, which defines a RecordSet. Used to store the data of a table, which is
 * returned as a ResultSet object. The RecordSet furthermore defines some useful 
 * methods to edit the data, as well as transforming the data into a JSONObject.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class RecordSet 
{

	
	/* Constants ----------------------------------------------------------------*/
	
	
	/* Variables ----------------------------------------------------------------*/	

	// List storing the field names of the represented table
	ArrayList<String> fields = null;
	
	// HashMap storing a list of values indexed by the field name
	// by that it is a matrix with the field names as columns
	HashMap<String, ArrayList<?>> values = null;
	
	// Hash map storing the field data type of the represented table, index by
	// field name
	HashMap<String, Integer> types = null;
	
	// the name of the table
	String tableName = null;
	
	// stores an alias for the table, used in join statements
	String alias = null;
	

	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * The empty constructor, just creating an empty list for the field names and
	 * empty HashMaps for the values and types.
	 */
	public RecordSet()
	{		
		this.fields = new ArrayList<String>();
		this.values = new HashMap<String, ArrayList<?>>();
		this.types = new HashMap<String,Integer>();
	}
	
	
	/**
	 * Constructor, which accepts a JSONObject with data of a table. The data is 
	 * converted into this RecordSet object.
	 * 
	 * @param json The JSONObject containing the data to represent.
	 * @throws ClinSysException Thrown if the JSONObject cannot be converted into
	 * 						    a RecordSet.
	 */
	public RecordSet(JSONObject json) throws ClinSysException
	{
		this.fields = new ArrayList<String>();
		this.values = new HashMap<String, ArrayList<?>>();
		this.types = new HashMap<String,Integer>();
		this.convert(json);		
	}
	
	/**
	 * Constructor that converts a ResultSet, given by a executed SQL-Statement 
	 * into a RecordSet.
	 * 	
	 * @param rs The ResultSet object containing the data to represent. 
	 * @param tableName The name of the table, that the RecordSet object represents
	 * @param alias An alias for the table, that is represented
	 * @throws ClinSysException Thrown if the ResultSet cannot be converted.
	 */
	public RecordSet(ResultSet rs, String tableName, String alias) 
						throws ClinSysException
	{
		this.fields = new ArrayList<String>();
		this.values = new HashMap<String, ArrayList<?>>();
		this.types = new HashMap<String,Integer>();
		this.convert(rs, tableName, alias);
	}
	
	
	/**
	 * Constructor that clones a given RecordSet.
	 * 
	 * @param rs The RecordSet we want to clone the data from.
	 */
	public RecordSet(RecordSet rs)
	{		
		ArrayList<String> tempFields = new ArrayList<String>(rs.getFields());
		HashMap<String, ArrayList<?>> tempValues = new HashMap<String, 
													ArrayList<?>>(rs.getValues());
		HashMap<String, Integer> tempTypes = new HashMap<String, 
													Integer>(rs.getTypes());
		
		this.setAlias(rs.getAlias());
		this.setTableName(rs.getTableName());
		this.setFields(tempFields);
		this.setValues(tempValues);
		this.setTypes(tempTypes);
	}

	
	
	/* Getter -------------------------------------------------------------------*/
	
	/**
	 * Returns the list of field names of the represented table.
	 * 
	 * @return List of the fields.
	 */
	public ArrayList<String> getFields() 
	{
		return this.fields;
	}
	
	
	/**
	 * Returns the i-th field name of the represented table.
	 * 
	 * @param index The index of the field, which name we want.
	 * @return The name of the indexed field.
	 */
	public String getFieldName(int index)
	{
		return this.fields.get(index);
	}

	
	/**
	 * Returns the HashMap representing all values of the table.
	 * 
	 * @return All values of the represented table.
	 */
	public HashMap<String, ArrayList<?>> getValues() 
	{
		return this.values;
	}
	
	/**
	 * Returns a list of all values of a given field.
	 * 
	 * @param fieldName The name of the field of which we want the values.
	 * @return A list containing all values of the field.
	 */
	public ArrayList<?> getValues(String fieldName)
	{
		return this.values.get(fieldName);
	}

	
	/**
	 * Returns a HashMap containing all types indexed by the field name. 
	 * 
	 * @return Map containing the data types of the table indexed by the field
	 * 		   name.
	 */
	public HashMap<String, Integer> getTypes() 
	{
		return this.types;
	}
	
	
	/**
	 * Returns the type of a specified field.
	 * 
	 * @param fieldName The name of the field we want the data type of.
	 * @return The data type of the specified field.
	 */
	public Integer getType(String fieldName)
	{
		return this.types.get(fieldName);
	}

	/**
	 * Returns the name of the table the RecordSet object represents.
	 * 
	 * @return The name of the table this object represents.
	 */
	public String getTableName() 
	{
		return this.tableName;
	}

	
	/**
	 * Returns the alias used in an SQL-Statement for the table, that is
	 * represented by this object.
	 * 
	 * @return The alias for the represented table.
	 */
	public String getAlias() 
	{
		return this.alias;
	}
	
	
	/**
	 * Returns the count of represented rows in this record set.
	 * 
	 * @return The record count.
	 */
	public int getRecordCount()
	{
		ArrayList<?> tempList;
		if (this.values.size()==0)
		{
			return 0;
		}
		else
		{
			tempList = this.values.get(this.fields.get(0));
			return tempList.size();
		}
	}
	

	/* Setter -------------------------------------------------------------------*/
	
	/**
	 * Sets the list containing the field names.
	 * 
	 * @param fields All field names of the represented table in a list.
	 */
	public void setFields(ArrayList<String> fields) 
	{
		this.fields = fields;
	}

	
	/**
	 * Sets the Map containing the values.
	 * 
	 * @param values All values of the represented table in a map, indexed by 
	 * 				 name of the field.
	 */
	public void setValues(HashMap<String, ArrayList<?>> values) 
	{
		this.values = values;
	}

	
	/**
	 * Sets the Map containing the data types.
	 * 
	 * @param types All data types of the represented table in a map, indexed by 
	 *              name of the  field.
	 */
	public void setTypes(HashMap<String, Integer> types) 
	{
		this.types = types;
	}

	/**
	 * Sets the name of the table.
	 * 
	 * @param tableName The name of the represented table.
	 */
	public void setTableName(String tableName) 
	{
		this.tableName = tableName;
	}
	
	/**
	 * Sets the alias of the table.
	 * 
	 * @param alias The alias of the represented table, used in SQL-Statements.
	 */
	public void setAlias(String alias) 
	{
		this.alias = alias;
	}
	
	
	
	/* Private ------------------------------------------------------------------*/

	
	/**
	 * Adds a field name to the list of fields.
	 * 
	 * @param fieldName The name of a field in the represented table.
	 */
	private void addFieldName(String fieldName)
	{
		this.fields.add(fieldName);
	}
	
	
	/**
	 * Adds a data type of a field to the list of types.
	 * 
	 * @param fieldName The name of the represented field.
	 * @param fieldType The data type of the field.
	 */
	private void addFieldType(String fieldName, int fieldType)
	{
		this.types.put(fieldName, fieldType);
	}
	
	
	/**
	 * Adds a list of values for a field to the Map of values.
	 * 
	 * @param fieldName The name of the field to which the values belong. With 
	 * 					which they are indexed in the map.
	 * @param values A list containing the values.
	 */
	private void addValues(String fieldName, ArrayList<?> values)
	{
		this.values.put(fieldName, values);
	}
	
	
	/**
	 * Converts the data of a ResultSet into this RecordSet representation.
	 * 
	 * @param rs The ResultSet object containing the data, which should be
	 * 			 converted into this object.
	 * @param tableName The name of the table, which is represented.
	 * @param alias The alias for the table, which is represented, used in SQL 
	 * 				statements 
	 * @throws ClinSysException Is thrown if the data cannot be converted.
	 */
	private void convert(ResultSet rs, String tableName, String alias) 
							throws ClinSysException
	{
		// Object storing the meta data of a result set.
		ResultSetMetaData rsmd = null;
		
		// Data type of the current field/column.
		int columnType;
		
		// Count of fields/columns.
		int columnCount;
		
		// Label of the field/column.
		String columnLabel;
		
		// if there exists an alias, set it
		if ((alias!=null)&&(!(alias.equals(""))))
		{
			this.setAlias(alias);
		}
		// set the table name
		this.setTableName(tableName);
		
		// try to get the meta data and the count of fields of the table, which
		// should be represented
		try 
		{
			rsmd = rs.getMetaData();
			columnCount = rsmd.getColumnCount();			
		} 
		// throw an error if retrieving the meta data fails
		catch (SQLException e) 
		{
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(RecordSet.convert(ResultSet, " + 
									   tableName + ")) : " + "Can't get the " +
								   	   "MetaData of ResultSet. Can't convert " +
									   "the ResultSet.");
		}
		
		// go through each column/field and read out the data type as well as the 
		// field name/label, then go through each row and add the data of the field
		// to a temporary list. After each row add the complete field by setting
		// the field name and type as well as the value list.
		try 
		{
			for (int i=1; i<=columnCount; i++)
			{
				columnType = rsmd.getColumnType(i); 	
				columnLabel = rsmd.getColumnLabel(i);
				rs.beforeFirst();
				
				switch(columnType)
				{
				case Types.BINARY:
					ArrayList<InputStream> tempListBinary = 
											new ArrayList<InputStream>();
					while (rs.next())
					{
						tempListBinary.add(rs.getBinaryStream(i));
					}
					this.addField(columnLabel, columnType, tempListBinary);
					break;
					
				case Types.BOOLEAN:
					ArrayList<Boolean> tempListBoolean = new ArrayList<Boolean>();
					while (rs.next())
					{
						tempListBoolean.add(rs.getBoolean(i));
					}
					this.addField(columnLabel, columnType, tempListBoolean);
					break;
					
				case Types.NUMERIC:
				case Types.DOUBLE:
					ArrayList<Double> tempListDouble = new ArrayList<Double>();
					while (rs.next())
					{
						tempListDouble.add(rs.getDouble(i));
					}
					this.addField(columnLabel, columnType, tempListDouble);
					break;
					
				case Types.INTEGER:
					ArrayList<Integer> tempListInteger = new ArrayList<Integer>();
					while (rs.next())
					{
						tempListInteger.add(rs.getInt(i));
					}
					this.addField(columnLabel, columnType, tempListInteger);
					break;
					
				case Types.VARCHAR:
					ArrayList<String> tempListString = new ArrayList<String>();
					while (rs.next())
					{
						tempListString.add(rs.getString(i));
					}
					this.addField(columnLabel, columnType, tempListString);
					break;
					
				case Types.DATE:
					ArrayList<Date> tempListDate = new ArrayList<Date>();
					while (rs.next())
					{
						tempListDate.add(rs.getDate(i));
					}
					this.addField(columnLabel, columnType, tempListDate);
					break;
					
				case Types.NULL:
					ArrayList<Object> tempListNull = new ArrayList<Object>();
					while (rs.next())
					{
						tempListNull.add(null);
					}
					this.addField(columnLabel, columnType, tempListNull);
					break;
					
				// throw an error if the data type is not defined
				default:
					throw new ClinSysException("(RecordSet.convert(ResultSet, " + 
											   tableName + ")) : " + "Undefined " +
							                   "column type. Can't convert the " +
											   "ResultSets.");
				}
			}
		} 
		// throw an error if there are problems with the meta data
		catch (SQLException e) 
		{
			Debugger.debug(e.getMessage());
			throw new ClinSysException("(RecordSet.convert(ResultSet, " + 
									   tableName + ")) : " + "Can't read the " +
									   "column type. Can't convert the " +
									   "ResultSets.");
		}
	}
	
	
	/**
	 * Convert a given JSONObject containing the table data into a this RecordSet
	 * object. 
	 * 
	 * @param json The JSONObject containing the data of the table, that is
	 * 			   represented.
	 * @throws ClinSysException Thrown if the JSONOBject cannot be converted.
	 */
	private void convert(JSONObject json) throws ClinSysException
	{
		// A JSONObject storing the field data, which is name, data type and a list
		// of values
		JSONObject field;
		
		// Name of the current field
		String fieldName;
		
		// All values of the current field
		JSONArray values;
		
		// All field names of the table
		JSONArray fieldNames;
		
		// Data type of the field
		int type;
		
		// get all field names of the table, which are the indices of the
		// JSONObject
		fieldNames = json.names();
		
		// go through each field
		for (int j=0; j<fieldNames.length(); j++)
		{
			// get the current field name
			fieldName = (String) fieldNames.get(j);
			
			// get the JSONObject representing the field
			field = json.getJSONObject(fieldName);
			
			// get the values of the field
			values = field.getJSONArray("Values");
			
			// get the data type of the field
			type = field.getInt("Type");
			
			// depending on the data type 
			// it creates a temporary list with the correct data type and adds
			// the casted value into it
			// then it adds the complete field, by adding the field name, type
			// and values to this object.
			switch(type)
			{
			case Types.BINARY:
				ArrayList<InputStream> tempListBinary = 
											new ArrayList<InputStream>();
				for (int i=0; i<values.length(); i++)
				{
					if (values.isNull(i))
					{
						tempListBinary.add(null);
					}
					else
					{
						tempListBinary.add((InputStream)values.get(i));
					}					
				}
				this.addField(fieldName, type, tempListBinary);
				break;
				
			case Types.BOOLEAN:
				ArrayList<Boolean> tempListBoolean = new ArrayList<Boolean>();
				for (int i=0; i<values.length(); i++)
				{
					if (values.isNull(i))
					{
						tempListBoolean.add(null);
					}
					else
					{
						tempListBoolean.add((Boolean)values.getBoolean(i));
					}
				}
				this.addField(fieldName, type, tempListBoolean);
				break;
				
			case Types.NUMERIC:
			case Types.DOUBLE:
				ArrayList<Double> tempListDouble = new ArrayList<Double>();
				for (int i=0; i<values.length(); i++)
				{
					if (values.isNull(i))
					{
						tempListDouble.add(null);
					}
					else
					{
						tempListDouble.add((Double) values.getDouble(i));
					}
				}
				this.addField(fieldName, type, tempListDouble);
				break;
				
			case Types.INTEGER:
				ArrayList<Integer> tempListInteger = new ArrayList<Integer>();
				for (int i=0; i<values.length(); i++)
				{
					if (values.isNull(i))
					{
						tempListInteger.add(null);
					}
					else
					{
						tempListInteger.add((Integer) values.get(i));
					}
				}
				this.addField(fieldName, type, tempListInteger);
				break;
				
			case Types.VARCHAR:
				ArrayList<String> tempListString = new ArrayList<String>();
				for (int i=0; i<values.length(); i++)
				{
					if (values.isNull(i))
					{
						tempListString.add(null);
					}
					else
					{
						tempListString.add(values.getString(i));
					}
				}
				this.addField(fieldName, type, tempListString);
				break;
				
			case Types.DATE:
				ArrayList<Date> tempListDate = new ArrayList<Date>();
				SimpleDateFormat spf = new SimpleDateFormat();
				spf.applyPattern("y-M-d");
				for (int i=0; i<values.length(); i++)
				{
					try 
					{
						if (values.isNull(i))
						{
							tempListDate.add(null);
						}
						else
						{
							tempListDate.add(spf.parse((String) values.get(i)));
						}
					} 
					// Date format error
					catch (JSONException e) 
					{
						throw new ClinSysException("(RecordSet.convert(" + 
										           "JSONObject)) : Could not " + 
										           "read the date value out of " + 
										           "the JSONObject");
					} 
					// Date format error
					catch (ParseException e) 
					{
						throw new ClinSysException("(RecordSet.convert(" + 
												   "JSONObject)) : Could not " + 
											   	   "parse the date value out of " +
												   "the JSONObject");
					}
				}
				this.addField(fieldName, type, tempListDate);
				break;
			// empty field	
			case Types.NULL:
				ArrayList<Object> tempListNull = new ArrayList<Object>();
				for (int i=0; i<values.length(); i++)
				{
					tempListNull.add(null);
				}
				this.addField(fieldName, type, tempListNull);
				break;
			// Not defined data type
			default:
				throw new ClinSysException("(RecordSet.convert(JSONObject)) : " + 
						"Undefined column type. Can't convert the ResultSets.");
			}
		}
	}
	
	
	
	/* Public -------------------------------------------------------------------*/	

	/**
	 * Converts a given ResultSet object into an RecordSet object and returns it.
	 * 
	 * @param rs The ResultSet, which should be converted.
	 * @param tableName The name of the table, the ResultSet object represents.
	 * @param alias The alias of the represented table in a SQL statement.
	 * @return The converted RecordSet of the represented table.
	 * @throws ClinSysException Thrown if the ResultSet cannot be converted.
	 */
	public static RecordSet convertToRecordset(ResultSet rs, String tableName, 
											String alias) throws ClinSysException
	{
		if (rs!=null)
		{
			RecordSet temp = new RecordSet(rs, tableName, alias);		
			return temp;
		}
		
		return null;
	}
	
	
	
	/**
	 * Returns the created mapping name for a field depending on the alias and the 
	 * table name of this RecordSet.
	 * 
	 * @param columnLabel The name of the field a mapping name should be created 
	 * 					  for.
	 * @return The mapping name for the given field.
	 */
	public String createFieldMapping(String columnLabel) 
	{
		String fieldMapping;
		
		// if an alias exists create the mapping like this: alias.field	
		if (this.getAlias()!=null)
		{
			fieldMapping = this.getAlias() + "." + columnLabel;
		}
		// if no alias exists but a table name create the mapping like this:
		// table.field
		else if(this.getTableName()!=null)
		{
			fieldMapping = this.getTableName() + "." + columnLabel;
		}
		// neither an alias nor a table name exist so just return the given field
		// name
		else
		{
			fieldMapping = columnLabel;
		}
		
		return fieldMapping;
	}
	
	
	/**
	 * Changes the name of a field. Used to shorten mapping names in join 
	 * statements or to give them proper names like Field1 instead of T1.M.C2.F1
	 * 
	 * @param oldFieldName The old field name/mapping, we want to change.
	 * @param newFieldName The name in which it should be changed.
	 */
	public void changeFieldName(String oldFieldName, String newFieldName)
	{
		// check if it exists in the fields list
		if (this.fields.contains(oldFieldName))
		{
			// if it exists remove the values and the data type and add them 
			// under the new name
			ArrayList<?> tempValues = this.getValues(oldFieldName);
			int type = this.getType(oldFieldName);
			this.fields.remove(oldFieldName);
			this.fields.add(newFieldName);
			this.values.remove(oldFieldName);
			this.values.put(newFieldName, tempValues);
			this.types.remove(oldFieldName);
			this.types.put(newFieldName, type);
		}
	}
	
	
	/**
	 * Adds a row of data to the RecordSet object. All fields must be addressed and
	 * there must be exact one value for each field. If a field has no value null
	 * must be given.
	 * 
	 * @param fields All field names that are addressed. (Must be all of the table
	 * 				 for the complete row)
	 * @param objects One value for each field. The values must respect the data 
	 * 				  type of the field. If the field is empty in this row null 
	 * 				  must be given.
	 * @throws ClinSysException Thrown if either not all fields are given or if
	 * 							an undefined data type is used.
	 */
	@SuppressWarnings("unchecked")
	public void addRow(ArrayList<String> fields, HashMap<String, Object> objects) 
							throws ClinSysException{
		// If neither all fields are given nor enough values
		if ((this.fields.size()!=fields.size()) && (fields.size()!=objects.size()))
		{	
			// throw an error
			throw new ClinSysException("ERROR");
		}
		// else if everything is complete 
		// go through each field, get the values list of the field and add the 
		// given value
		else
		{
			for (String field : fields)
			{
				switch (this.types.get(field))
				{
				case Types.BINARY:
					ArrayList<InputStream> binaryList = (ArrayList<InputStream>) 
															this.values.get(field);
					binaryList.add((InputStream) objects.get(field));
					break;
					
				case Types.BOOLEAN:
					ArrayList<Boolean> booleanList = (ArrayList<Boolean>) 
														this.values.get(field);
					booleanList.add((Boolean) objects.get(field));
					break;
					
				case Types.DOUBLE:
					ArrayList<Double> doubleList = (ArrayList<Double>) 
														this.values.get(field);
					doubleList.add((Double) objects.get(field));
					break;
					
				case Types.INTEGER:
					ArrayList<Integer> intList = (ArrayList<Integer>) 
														this.values.get(field);
					intList.add((Integer) objects.get(field));
					break;
					
				case Types.VARCHAR:
					ArrayList<String> textList = (ArrayList<String>) 
														this.values.get(field);
					textList.add((String) objects.get(field));
					break;
					
				case Types.DATE:
					ArrayList<Date> dateList = (ArrayList<Date>) 
														this.values.get(field);
					dateList.add((Date) objects.get(field));
					break;
					
				case Types.NULL:
					ArrayList<Object> nullList = (ArrayList<Object>) 
														this.values.get(field);
					nullList.add(null);
					break;
				
				// an undefined data type was used
				default:
					throw new ClinSysException("(RecordSet.addRow(" + 
											   "ArrayList<String>, " +
											   "HashMap<String, Object)) : " + 
											   "Undefined column type. Can't " + 
											   "add the row.");
				}
				
			}
		}
		
	}
	
	
	/**
	 * Adds a field with all its information such as the name, the data type and 
	 * the values list.
	 * 
	 * @param fieldName The name of the field.
	 * @param fieldType The data type of the field.
	 * @param values All values of the field.
	 */
	public void addField(String fieldName, int fieldType, ArrayList<?> values)
	{
		// temporary list of values used to keep the record count in constancy and
		// to fill up the values list of the field, if it has lesser items than
		// the other fields or to fill up the other field values if they have 
		// lesser items
		ArrayList<?> tempValues = null;
		
		Debugger.debug("RC: " + this.getRecordCount() + " - Values: " + 
																values.size());
		
		// if there are records
		if (this.getRecordCount()>0)
		{
			// if there are lesser values of the given field than rows in the
			// existing table
			if (values.size()<this.getRecordCount())
			{
				// as long as there are remaining rows add null at the end
				for(int i=values.size(); i<=this.getRecordCount();i++)
				{
					values.add(null);
				}
			}
			// if the are more values of the given field than rows in the existing
			// table
			else if(values.size()>this.getRecordCount())
			{
				for (String field : this.fields)
				{								
					tempValues = this.getValues(field);
					
					for (int i=this.getRecordCount();i<=values.size(); i++)
					{
						tempValues.add(null);
					}				
				}
			}
		}

		Debugger.debug("RC: " + this.getRecordCount() + " - Values: " + 
																values.size());
		
		// add the field to the talbe
		this.addFieldName(fieldName);
		this.addFieldType(fieldName, fieldType);
		this.addValues(fieldName, values);
	}
	
	
	/**
	 * Add an "empty" field to the table, by just adding the data type and name
	 * with an empty list of values.
	 * 
	 * @param fieldName The name of the field, that should be added.
	 * @param fieldType The data type of the field, that should be added.
	 * @throws ClinSysException Thrown if the data type is not defined.
	 */
	public void addField(String fieldName, int fieldType) throws ClinSysException
	{
		// List that will be created with the generic type of objects given by
		// the data type
		ArrayList<?> list = null;
		
		// check, which data type the field has
		// and then create the array list with the given type 
		switch (fieldType)
		{
		case Types.BINARY:
			list = new ArrayList<InputStream>();
			break;
			
		case Types.BOOLEAN:
			list = new ArrayList<Boolean>();
			break;
			
		case Types.DOUBLE:
			list = new ArrayList<Double>();
			break;
			
		case Types.INTEGER:
			list = new ArrayList<Integer>();
			break;
			
		case Types.VARCHAR:
			list = new ArrayList<String>();
			break;
			
		case Types.DATE:
			list = new ArrayList<Date>();
			break;
			
		case Types.NULL:
			list = new ArrayList<Object>();
			break;
			
		default:
			throw new ClinSysException("(RecordSet.addField(String fieldName, " +
									   "int fieldType)) : " + "Undefined column " +
									   "type. Can't add the row.");
		}
		
		// add the field with the empty list
		this.addField(fieldName, fieldType, list);
	}
	
	
	/**
	 * Adds a list of "empty" fields to the table.
	 * 
	 * @param fieldNames A list of field names.
	 * @param fieldTypes A map of data types index by the field names.
	 */
	public void addFields(ArrayList<String> fieldNames, 
										HashMap<String, Integer> fieldTypes)
	{
		// go through each field
		for (String field : fieldNames)
		{
			// and try to add it
			try 
			{
				this.addField(field, fieldTypes.get(field));
			} 
			catch (ClinSysException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Edits the value of a cell, defined by a row index and a field name.
	 * 
	 * @param fieldName The name of the field.
	 * @param row The number of the row.
	 * @param newVal The new value the cell should have
	 * @throws ClinSysException Thrown if the field has an undefined data type.
	 */
	@SuppressWarnings("unchecked")
	public void editCell(String fieldName, int row, Object newVal) 
								throws ClinSysException
	{
		// check which data type the field has
		// then create a corresponding list and retrieve the values list of the
		// field, and set the by row indexed value to the new value
		switch (this.types.get(fieldName))
		{
		case Types.BINARY:
			ArrayList<InputStream> binaryList = (ArrayList<InputStream>) 
														this.values.get(fieldName);
			binaryList.set(row, (InputStream) newVal);
			break;
			
		case Types.BOOLEAN:
			ArrayList<Boolean> booleanList = (ArrayList<Boolean>) 
													this.values.get(fieldName);
			booleanList.set(row, (Boolean) newVal);
			break;
			
		case Types.DOUBLE:
			ArrayList<Double> doubleList = (ArrayList<Double>) 
												this.values.get(fieldName);
			doubleList.set(row, (Double) newVal);
			break;
			
		case Types.INTEGER:
			ArrayList<Integer> intList = (ArrayList<Integer>) 
												this.values.get(fieldName);
			intList.set(row, (Integer) newVal);
			break;
			
		case Types.VARCHAR:
			ArrayList<String> textList = (ArrayList<String>) 
												this.values.get(fieldName);
			textList.set(row, (String) newVal);
			break;
			
		case Types.DATE:
			ArrayList<Date> dateList = (ArrayList<Date>) 
											this.values.get(fieldName);
			dateList.set(row, (Date) newVal);
			break;
			
		case Types.NULL:
			ArrayList<Object> nullList = (ArrayList<Object>) 
												this.values.get(fieldName);
			nullList.set(row, null);
			break;
			
		default:
			throw new ClinSysException("(RecordSet.addRow(ArrayList<String>, " +
					 				   "HashMap<String, Object)) : " + 
									"Undefined column type. Can't add the row.");
		}
	}

	
	/**
	 * Converts a field with its data into a JSON object.
	 * 
	 * @param fieldName The name of the field that should be converted.
	 * @return A JSONObject containing the data of the selected field.
	 */
	public JSONObject fieldToJSON(String fieldName)
	{
		JSONObject field = new JSONObject();
		field.put("Type", this.getType(fieldName));
		field.put("Values", this.getValues(fieldName));
		return field;
	}
	
	
	/**
	 * Converts the complete RecordSet object into a JSONObject.
	 * 
	 * @return The converted RecordSet object with all its data.
	 */
	public JSONObject toJSON()
	{
		JSONObject result = new JSONObject();
		
		for (String fieldName : this.fields)
		{
			result.put(fieldName, this.fieldToJSON(fieldName));
		}
		
		return result;		
	}
	
	
	
	/**
	 * Converts the only the selected fields of the RecordSet object into a 
	 * JSONObject.
	 * 
	 * @param fields A list with the field names, that should be converted.
	 * @return A JSONObject containing the converted data of the selected fields.
	 */
	public JSONObject toJSON(JSONArray fields)
	{
		JSONObject result = new JSONObject();
		
		for (int i=0; i<fields.length(); i++)
		{
			result.put(fields.getString(i), this.fieldToJSON(fields.getString(i)));
		}
		
		return result;		
	}
	
	
	/**
	 * Removes the i-th row.
	 * 
	 * @param i The index number of the row, that should be removed
	 */
	public void remove(int i)
	{
		if (i<this.getRecordCount())
		{
			for (String field : this.fields)
			{
				this.values.get(field).remove(i);
			}
		}
	}
}
