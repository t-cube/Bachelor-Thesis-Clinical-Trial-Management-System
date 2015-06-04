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

package plugins.contacts.server;

import java.util.ArrayList;
import java.util.Collection;

import plugins.contacts.CONTACTS_COMMANDS;
import plugins.contacts.CONTACTS_TABLES;
import plugins.contacts.CONTACT_TYPE;
import server.controller.Controller;
import server.model.SQLCreator;
import server.model.enumerations.RIGHTS;
import server.model.enumerations.SQLITEMTYPE;
import server.model.enumerations.SQLWORD;
import server.model.enumerations.STATEMENTTYPE;
import shared.json.JSONObject;
import shared.network.MESSAGE_TYPES;
import shared.network.RECIPIENT;
import shared.util.ClinSysException;
import shared.util.RecordSet;

/**
 * The server side controller of the contacts plug-in. Providing the server side
 * functionality for the contacts plug-in. 
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class ContactsController extends Controller 
{
	
	/* Constants ----------------------------------------------------------------*/
	
	
	/* Variables ----------------------------------------------------------------*/	
	
	
	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * The constructor of the controller. Initializes the super class and creates
	 * a protocol.
	 */
	public ContactsController()
	{
		super("ContactsController");
		this.protocol = new ContactsProtocol();
	}
	
	
	/* Getter -------------------------------------------------------------------*/
	

	/* Setter -------------------------------------------------------------------*/
		
	
	/* Private ------------------------------------------------------------------*/
	
	
	/* Public -------------------------------------------------------------------*/
	
	/**
	 * Saves a edited or created contact in the databases.
	 * 
	 * @param request The JSONObject message containing the data of the contact 
	 * 				   that should be saved.
	 * @return True if the contact was successfully saved. False if not.
	 */
	public boolean saveContact(JSONObject request)
	{
		// object handling the sql statements to save the contact in the database
		SQLCreator sql = new SQLCreator();
		
		// name of the user, who wants to save a contact
		String userName = request.getString("UserName");
		
		// the contact data
		JSONObject data = request.getJSONObject("data");
		String ct = request.getString("ContactType");
		
		// the field names containing data to be saved
		String[] fields = JSONObject.getNames(data);			
		
		// try to create an insert statement, inserting the contact data into
		// the contact table
		try 
		{
			sql.setStatementType(STATEMENTTYPE.INSERT);
			sql.addItem(CONTACTS_TABLES.TBL_CONTACT.get(), SQLITEMTYPE.TABLE);
			// add the field name of every affected field
			sql.addItem(SQLWORD.OPENINGBRACKET);
				sql.addItem(fields, SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.CLOSINGBRACKET);
			sql.addItem(SQLWORD.VALUES);
			sql.addItem(SQLWORD.OPENINGBRACKET);
				// add every field value, comma separated 
				for (int i = 0; i<fields.length; i++)
				{
					if (i>0)
					{
						sql.addItem(SQLWORD.COMMA);
					}
					sql.addItem(data.get(fields[i]), 
							(fields[i].equals("user_id"))?
									SQLITEMTYPE.INT:SQLITEMTYPE.TEXT);
				}			
			sql.addItem(SQLWORD.CLOSINGBRACKET);
			// define what should be returned
			sql.addItem(SQLWORD.RETURNING);
			sql.addItem("id", SQLITEMTYPE.FIELD);
			
			RecordSet rs = this.model.executeSQL(sql, userName, 
					CONTACTS_COMMANDS.SAVE_CONTACT.get(), this.getPluginName(), 
					RIGHTS.CREATE.get());
					
			sql.reset();
			sql.setStatementType(STATEMENTTYPE.INSERT);
			
			if (ct.equals(CONTACT_TYPE.CUSTOMER.get()) || 
				ct.equals(CONTACT_TYPE.SPONSOR.get()))
			{				
				sql.addItem(CONTACTS_TABLES.TBL_CUSTOMER.get(), SQLITEMTYPE.TABLE);				
			}
			else if (ct.equals(CONTACT_TYPE.SUPPLIER.get()) || 
					 ct.equals(CONTACT_TYPE.SITE.get()))
			{
				sql.addItem(CONTACTS_TABLES.TBL_SUPPLIER.get(), SQLITEMTYPE.TABLE);
			}
			else if (ct.equals(CONTACT_TYPE.EMPLOYEE.get()))
			{
				sql.addItem(CONTACTS_TABLES.TBL_EMPLOYEE.get(), SQLITEMTYPE.TABLE);
			}
			else if (ct.equals(CONTACT_TYPE.PATIENT.get()))
			{
				sql.addItem(CONTACTS_TABLES.TBL_PATIENT.get(), SQLITEMTYPE.TABLE);
			}
			
			sql.addItem(SQLWORD.OPENINGBRACKET);
				sql.addItem("contact_id", SQLITEMTYPE.FIELD);
				if (ct.equals(CONTACT_TYPE.EMPLOYEE))
				{
					sql.addItem(SQLWORD.COMMA);
					sql.addItem("position_id", SQLITEMTYPE.FIELD);
				}
			sql.addItem(SQLWORD.CLOSINGBRACKET);
			sql.addItem(SQLWORD.VALUES);
			sql.addItem(SQLWORD.OPENINGBRACKET);
				sql.addItem(rs.getValues("id").get(0), SQLITEMTYPE.INT);
				if (ct.equals(CONTACT_TYPE.EMPLOYEE))
				{
					sql.addItem(SQLWORD.COMMA);
					sql.addItem(request.getInt("position_id"), SQLITEMTYPE.INT);
				}
			sql.addItem(SQLWORD.CLOSINGBRACKET);
			
			this.model.executeSQL(sql, userName,
								  CONTACTS_COMMANDS.SAVE_CONTACT.get(), 
								  this.getPluginName(), RIGHTS.CREATE.get());
			
			if (ct.equals(CONTACT_TYPE.SPONSOR.get()) || 
				ct.equals(CONTACT_TYPE.SITE.get()))
			{
				sql.reset();
				sql.setStatementType(STATEMENTTYPE.INSERT);
				if (ct.equals(CONTACT_TYPE.SPONSOR.get()))
				{
					sql.addItem(CONTACTS_TABLES.TBL_SPONSOR.get(), 
							    SQLITEMTYPE.TABLE);	
					sql.addItem(SQLWORD.OPENINGBRACKET);
						sql.addItem("customer_contact_id", SQLITEMTYPE.FIELD);
				}
				else if (ct.equals(CONTACT_TYPE.SITE.get()))
				{
					sql.addItem(CONTACTS_TABLES.TBL_SITE.get(), SQLITEMTYPE.TABLE);
					sql.addItem(SQLWORD.OPENINGBRACKET);
						sql.addItem("supplier_contact_id", SQLITEMTYPE.FIELD);
				}
				sql.addItem(SQLWORD.CLOSINGBRACKET);
				sql.addItem(SQLWORD.VALUES);
				sql.addItem(SQLWORD.OPENINGBRACKET);
					sql.addItem(rs.getValues("id").get(0), SQLITEMTYPE.INT);
				sql.addItem(SQLWORD.CLOSINGBRACKET);
	
				this.model.executeSQL(sql, userName, 
									  CONTACTS_COMMANDS.SAVE_CONTACT.get(), 
									  this.getPluginName(), RIGHTS.CREATE.get());
			}
			
		}
		catch (ClinSysException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
	}

	
	/**
	 * Reads out the contact data stored in the database and sends it to the 
	 * client requesting the data.
	 *  
	 * @param request The Request, containing which contact data should be send and
	 * 				  other meta data.
	 */
	@SuppressWarnings("unchecked")
	public void getContactsData(JSONObject request) 
	{
		SQLCreator sql = new SQLCreator();
		RecordSet rsSupplier = null;
		RecordSet rsSite = null;
		RecordSet rsEmployee = null;
		RecordSet rsPatient = null;
		RecordSet rsCustomer = null;
		RecordSet rsSponsor = null;
		RecordSet rsContacts = null;
		JSONObject answer = new JSONObject();
		String userName = request.getString("UserName");
		ArrayList<Integer> idList = new ArrayList<Integer>();
		
		answer.put("Receiver", request.getString("Sender"));
		answer.put("Sender", this.getPluginName());
		answer.put("Command", request.getString("Command"));
		answer.put("Type", MESSAGE_TYPES.ANSWER);
		
		try 
		{
			sql.setStatementType(STATEMENTTYPE.SELECT);
			sql.addItem("contact_id", SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem(CONTACTS_TABLES.TBL_CUSTOMER.get(), SQLITEMTYPE.TABLE);
			rsCustomer = this.model.executeSQL(sql, userName, 
											   CONTACTS_COMMANDS.
										   			GET_CONTACTS_CUSTOMER.get(), 
										   	   this.getPluginName(), 
										   	   RIGHTS.READ.get());			
		} 
		catch (ClinSysException e1) 
		{
			e1.printStackTrace();
		}
		sql.reset();
		try 
		{
			sql.setStatementType(STATEMENTTYPE.SELECT);
			sql.addItem("contact_id", SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem(CONTACTS_TABLES.TBL_PATIENT.get(), SQLITEMTYPE.TABLE);
			rsPatient = this.model.executeSQL(sql, userName, 
										      CONTACTS_COMMANDS.
									      				GET_CONTACTS_PATIENT.get(), 
									      	  this.getPluginName(), 
									      	  RIGHTS.READ.get());			
		} 
		catch (ClinSysException e1) 
		{
			e1.printStackTrace();
		}
		sql.reset();
		try 
		{
			sql.setStatementType(STATEMENTTYPE.SELECT);
			sql.addItem("contact_id", SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem(CONTACTS_TABLES.TBL_EMPLOYEE.get(), SQLITEMTYPE.TABLE);
			rsEmployee = this.model.executeSQL(sql, userName, 
											   CONTACTS_COMMANDS.
											   		GET_CONTACTS_EMPLOYEE.get(), 
											   this.getPluginName(), 
											   RIGHTS.READ.get());			
		} 
		catch (ClinSysException e1) 
		{
			e1.printStackTrace();
		}
		
		sql.reset();
		
		try 
		{
			sql.setStatementType(STATEMENTTYPE.SELECT);
			sql.addItem("contact_id", SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem(CONTACTS_TABLES.TBL_SUPPLIER.get(), SQLITEMTYPE.TABLE);
			rsSupplier = this.model.executeSQL(sql, userName, 
											   CONTACTS_COMMANDS.
										   			GET_CONTACTS_SUPPLIER.get(), 
										   	   this.getPluginName(), 
										   	   RIGHTS.READ.get());			
		} 
		catch (ClinSysException e1) 
		{
			e1.printStackTrace();
		}
		
		sql.reset();
		
		try 
		{
			sql.setStatementType(STATEMENTTYPE.SELECT);
			sql.addItem("customer_contact_id", SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem(CONTACTS_TABLES.TBL_SPONSOR.get(), SQLITEMTYPE.TABLE);
			rsSponsor = this.model.executeSQL(sql, userName, 
										 	  CONTACTS_COMMANDS.
										 	  			GET_CONTACTS_SPONSOR.get(), 
										 	  this.getPluginName(), 
										 	  RIGHTS.READ.get());			
		} 
		catch (ClinSysException e1) 
		{
			e1.printStackTrace();
		}
		
		sql.reset();
		
		try 
		{
			sql.setStatementType(STATEMENTTYPE.SELECT);
			sql.addItem("supplier_contact_id", SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem(CONTACTS_TABLES.TBL_SITE.get(), SQLITEMTYPE.TABLE);
			rsSite = this.model.executeSQL(sql, userName, 
										   CONTACTS_COMMANDS.GET_CONTACTS_SITE.
									   										get(), 
									   	   this.getPluginName(), 
									   	   RIGHTS.READ.get());			
		} 
		catch (ClinSysException e1) 
		{
			e1.printStackTrace();
		}
		
		if (rsCustomer.getRecordCount()>0)
		{
			idList.addAll((Collection<? extends Integer>) rsCustomer.
														getValues("contact_id"));
		}
		
		if (rsEmployee.getRecordCount()>0)
		{
			idList.addAll((Collection<? extends Integer>) rsEmployee.
														getValues("contact_id"));
		}
		
		if (rsPatient.getRecordCount()>0)
		{
			idList.addAll((Collection<? extends Integer>) rsPatient.
														getValues("contact_id"));
		}
		
		if (rsSupplier.getRecordCount()>0)
		{
			idList.addAll((Collection<? extends Integer>) rsSupplier.
														getValues("contact_id"));
		}
		
		if (rsSponsor.getRecordCount()>0)
		{
			for (Object id : rsSponsor.getValues("customer_contact_id"))
			{
				if (!idList.contains(id))
				{
					idList.add((Integer) id);
				}
			}
		}
		
		if (rsSite.getRecordCount()>0)
		{
			for (Object id : rsSite.getValues("supplier_contact_id"))
			{
				if (!idList.contains(id))
				{
					idList.add((Integer) id);
				}
			}
		}

		sql.reset();
		
		try 
		{
			sql.setStatementType(STATEMENTTYPE.SELECT);
			sql.addItem(new String[]{"id","firstname","lastname","company",
							"position","address","zip_code","place","state",
							"country", "email","phone","mobile","website","notes",
							"user_id"}, SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem(CONTACTS_TABLES.TBL_CONTACT.get(),SQLITEMTYPE.TABLE);
			sql.addItem(SQLWORD.WHERE);
			sql.addItem("id", SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.IN);
			sql.addItem(SQLWORD.OPENINGBRACKET);
				sql.addItem(idList.toArray(), SQLITEMTYPE.INT);
			sql.addItem(SQLWORD.CLOSINGBRACKET);
			
			rsContacts = this.model.executeSQL(sql, userName, 
											   CONTACTS_COMMANDS.SHOW_CONTACTS.
											   								get(), 
											   this.getPluginName(), 
											   RIGHTS.READ.get());
		} 
		catch (ClinSysException e1) 
		{
			e1.printStackTrace();
		}
		
		if (rsContacts.getRecordCount()>0)
		{
			answer.put("Contacts", rsContacts.toJSON());
		}
		
		if (rsCustomer.getRecordCount()>0)
		{
			answer.put("Customer", rsCustomer.toJSON());
		}
		
		if (rsEmployee.getRecordCount()>0)
		{
			answer.put("Employee", rsEmployee.toJSON());
		}
		
		if (rsPatient.getRecordCount()>0)
		{
			answer.put("Patients", rsPatient.toJSON());
		}
		
		if (rsSupplier.getRecordCount()>0)
		{
			answer.put("Supplier", rsSupplier.toJSON());
		}
		
		if (rsSponsor.getRecordCount()>0)
		{
			answer.put("Sponsor", rsSponsor.toJSON());
		}
		
		if (rsSite.getRecordCount()>0)
		{
			answer.put("Site", rsSite.toJSON());
		}
				
		try 
		{
			this.server.sendTo(RECIPIENT.CLIENT, userName, answer);
		} 
		catch (ClinSysException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterCreated() 
	{
		// TODO Auto-generated method stub
		
	}
}
