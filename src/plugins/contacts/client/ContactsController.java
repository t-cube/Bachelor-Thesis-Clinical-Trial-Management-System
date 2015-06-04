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

package plugins.contacts.client;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import plugins.contacts.CONTACTS_COMMANDS;
import plugins.contacts.CONTACT_TYPE;
import plugins.contacts.client.listener.ContactsActionListener;
import plugins.contacts.client.view.ContactView;
import plugins.contacts.client.view.ContactsOverView;
import shared.json.JSONObject;
import shared.network.MESSAGE_TYPES;
import shared.util.Dialog;
import shared.util.ClinSysException;
import shared.util.RecordSet;
import client.controller.Controller;

/**
 * This ContactsController class is the client side controller object for the 
 * contact plug-in and delivers the functionality and deciding mechanisms for
 * the contact plug-in. It is more or less the brain of the plug-in.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class ContactsController extends Controller 
{
	
	/* Constants ----------------------------------------------------------------*/
	
	
	/* Variables ----------------------------------------------------------------*/	
	
	// The cache, containing the data, so if it hasn't changed it must not be
	// downloaded everytime the data is viewed.
	private ContactsCache cache;
	
	// The InnerView object showing an overview of the contacts.
	private ContactsOverView refreshableCOV = null;
	
	
	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * The standard constructor, initializing the super class, and creating the 
	 * protocol, the cache and the views, as well as the menus.
	 */
	public ContactsController()
	{
		super("ContactsController");
		this.protocol = new ContactsProtocol();
		this.cache = new ContactsCache();
		this.views.put("ContactsOverView", new ContactsOverView(this));
		this.views.put("ContactView", new ContactView(this));
		createMenus();
	}

	
	/* Getter -------------------------------------------------------------------*/
	
	/**
	 * @return The cache object containing the cached contact data.
	 */
	public ContactsCache getCache()
	{
		return this.cache;
	}
	

	/* Setter -------------------------------------------------------------------*/
		
	
	/* Private ------------------------------------------------------------------*/
	
	/**
	 * Function, that creates the menus for the contact plug-in.
	 */
	private void createMenus()
	{
		JMenu contacts = new JMenu("Contacts");
		JMenuItem showContactOverview = new JMenuItem("Show Contacts");
		showContactOverview.setActionCommand(CONTACTS_COMMANDS.SHOW_CONTACTS.
																		get());
		showContactOverview.addActionListener(new ContactsActionListener(this));
		contacts.add(showContactOverview);
		this.menus.put("Contacts",contacts);
	}
	
	
	/**
	 * Function to request the contact data, if the data was not cached before or
	 * the cache is not up to date.
	 */
	private void requestContactsData()
	{
		JSONObject request = new JSONObject();
		request.put("UserName", this.clientController.getUserName());
		request.put("Sender", this.getPluginName());
		request.put("Receiver", "ContactsController");
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("Command", CONTACTS_COMMANDS.SHOW_CONTACTS);
		
		try 
		{
			this.client.sendMsg(request);
		} 
		catch (ClinSysException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/* Public -------------------------------------------------------------------*/
	
	/** 
	 * If the contacts data request was answered it sets the cached data to the 
	 * answered data.
	 * @param answer The requested contacts data send by the server.
	 */
	public void refreshContactsCache(JSONObject answer)
	{
		// variables to store the requested data in
		Object id = null;
		RecordSet contacts = null;
		RecordSet customer = null;
		RecordSet employee = null;
		RecordSet patient = null;
		RecordSet site = null;
		RecordSet sponsor = null;
		RecordSet supplier = null;
		
		// empty the cache
		this.cache.setContacts(null);
		this.cache.setCustomer(null);
		this.cache.setEmployee(null);
		this.cache.setPatient(null);
		this.cache.setSite(null);
		this.cache.setSponsor(null);
		this.cache.setSupplier(null);

		
		try 
		{
			// cache the suppliers if there are any
			if (answer.has("Supplier"))
			{
				this.cache.setSupplier(new RecordSet(answer.
													getJSONObject("Supplier")));
			}
			
			// cache the employees if there are any
			if (answer.has("Employee"))
			{
				this.cache.setEmployee(new RecordSet(answer.
													getJSONObject("Employee")));
			}
			
			// cache the patients if there are any
			if (answer.has("Patient"))
			{
				this.cache.setPatient(new RecordSet(answer.
													getJSONObject("Patient")));
			}
			
			// cache the customer if there are any
			if (answer.has("Customer"))
			{
				this.cache.setCustomer(new RecordSet(answer.
													getJSONObject("Customer")));
			}
			
			// cache the sites if there are any
			if (answer.has("Site"))
			{
				this.cache.setSite(new RecordSet(answer.
													getJSONObject("Site")));
			}
			
			// cache the sponsors if there are any
			if (answer.has("Sponsor"))
			{
				this.cache.setSponsor(new RecordSet(answer.
													getJSONObject("Sponsor")));
			}
			
			// cache the contacts if there are any
			if (answer.has("Contacts"))
			{
				this.cache.setContacts(new RecordSet(answer.
													getJSONObject("Contacts")));
				
				// begin with identifying the contactType of each contact
				supplier = this.cache.getSupplier();
				employee = this.cache.getEmployee();
				patient = this.cache.getPatient();
				customer = this.cache.getCustomer();
				site = this.cache.getSite();
				sponsor = this.cache.getSponsor();
				contacts = this.cache.getContacts();				
				
				// add a field for the contact type
				contacts.addField("contactType", Types.VARCHAR, 
														new ArrayList<String>());
				
				// go through each contact and set the contact type, 
				// so it will be represented in the table
				for (int i=0; i<contacts.getRecordCount(); i++)
				{
					id = contacts.getValues("id").get(i);
					if (supplier!=null)
					{
						if (supplier.getValues("contact_id").contains(id))
						{
							contacts.editCell("contactType", i, 
													CONTACT_TYPE.SUPPLIER.get());
						}
					}
					
					if (employee!=null)
					{
						if (employee.getValues("contact_id").contains(id))
						{
							contacts.editCell("contactType", i, 
													CONTACT_TYPE.EMPLOYEE.get());
						}
					}
					
					if (patient!=null)
					{
						if (patient.getValues("contact_id").contains(id))
						{
							contacts.editCell("contactType", i, 
													CONTACT_TYPE.PATIENT .get());
						}
					}
					
					if (customer!=null)
					{
						if (customer.getValues("contact_id").contains(id))
						{
							contacts.editCell("contactType", i, 
													CONTACT_TYPE.CUSTOMER.get());
						}
					}
					
					if (site!=null)
					{
						if (site.getValues("supplier_contact_id").contains(id))
						{
							contacts.editCell("contactType", i, 
													CONTACT_TYPE.SITE.get());
						}
					}
					if (sponsor!=null)
					{
						if (sponsor.getValues("customer_contact_id").contains(id))
						{
							contacts.editCell("contactType", i, 
													CONTACT_TYPE.SPONSOR.get());
						}
					}
				}
				
			}
		} 
		catch (ClinSysException e) 
		{
			e.printStackTrace();
		}		
		
		// refresh the table of the contact over view to show the new cached data
		this.loadContactOverViewData(refreshableCOV);
	}
	
	
	/**
	 * Loads/Refreshes the table showing an overview of the contact data.
	 * @param cov An ContactsOverView object, containing the table with which the
	 * 			  overview is shown.
	 */
	public void loadContactOverViewData(ContactsOverView cov)
	{
		// the table which should be refreshed
		JTable tbl = cov.getTable();		
		
		// the table model (contains the data)
		DefaultTableModel dtm = null;
		
		// array for the data
		Object[][] data = null;
		Object[] fieldNames = null;
		
		// objects to handle the data as given by the cache
		ArrayList<?> values = null;
		RecordSet contacts = null;	
			
		// if the cache should be refreshed -> it wasn't load before
		if (this.cache.isRefresh())
		{
			// get the overview view
			this.refreshableCOV = cov;
			// and request the current data
			this.requestContactsData();
		}
		// if it was just refreshed
		else
		{	
			// get the contact data from the cache
			contacts = this.cache.getContacts();

			// receive the field names 
			fieldNames = contacts.getFields().toArray();
			
			// create the array to store the data
			data = new Object[contacts.getRecordCount()][fieldNames.length];
			
			// go through each field and store the data into the array
			for (int j = 0; j<fieldNames.length; j++)
			{
				values = contacts.getValues((String) fieldNames[j]);
			
				for (int i = 0; i<contacts.getRecordCount(); i++)
				{
					data[i][j] = values.get(i);
				}
			}
			
			// add the data array to the table model
			dtm = new DefaultTableModel(data, fieldNames);
			
			// and set the model to the table
			tbl.setModel(dtm);
		}
	}
	
	
	/**
	 * Shows the contact overview view. Loads the data and pushes the view on the 
	 * top of the view hierarchy.
	 */
	public void showContactOverView()
	{		
		loadContactOverViewData((ContactsOverView) this.getViews().
														get("ContactsOverView"));
		
		this.clientController.getMainView().pushView(this.views.
														get("ContactsOverView"));
	}
	
	
	/**
	 * Shows the view to edit/create a contact, by pushing it on the top of the 
	 * view hierarchy. 
	 */
	public void showEditContactView()
	{
		this.clientController.getMainView().pushView(this.views.
															get("ContactView"));
	}
	
	
	/**
	 * Saves a new or edited contact.
	 */
	public void saveContact()
	{
		// get a reference to the view containing the user-entered data.
		ContactView cv = (ContactView) this.views.get("ContactView");

		// JSONObject for the request message 
		JSONObject request = new JSONObject();
		JSONObject data = new JSONObject();
		
		// objects to iterate over the entered data
		Iterator<Entry<String, String>> it = null;
		Entry<String, String> pair = null;
		HashMap<String,String> inputs = new HashMap<String,String>();
		
		
		// put all entered data into a map
		inputs.put("firstname", cv.getTxtFirstname().getText());
		inputs.put("lastname", cv.getTxtLastname().getText());
		inputs.put("company", cv.getTxtCompany().getText());		
		if (cv.getCbxPosition().isVisible())
		{
			inputs.put("position", (String) cv.getCbxPosition().getSelectedItem());
		}
		else
		{
			inputs.put("position", cv.getTxtPosition().getText());
		}		
		inputs.put("address", cv.getTxtAddress().getText());
		inputs.put("zip_code", cv.getTxtZIP_code().getText());
		inputs.put("place", cv.getTxtPlace().getText());
		inputs.put("state", cv.getTxtState().getText());
		inputs.put("country", (String) cv.getCbxCountry().getSelectedItem());
		inputs.put("email", cv.getTxtEmail().getText());
		inputs.put("phone", cv.getTxtPhone().getText());
		inputs.put("mobile", cv.getTxtMobile().getText());
		inputs.put("website", cv.getTxtWebsite().getText());
		inputs.put("notes", cv.getTxtNotes().getText());
		
		// add the data to the JSON Object
		it = inputs.entrySet().iterator();
		while (it.hasNext())
		{
			pair = (Entry<String, String>) it.next();
			if (!pair.getValue().equals(""))
			{
				data.put(pair.getKey(), pair.getValue());
			}
		}
		
		data.put("user_id", 1); // has to be changed to the combobox!
		
		// add the metadata for the request message
		request.put("ContactType", (String) cv.getCbxContactType().
																getSelectedItem());
		request.put("UserName", this.clientController.getUserName());
		request.put("Sender", this.getPluginName());
		request.put("Receiver", "ContactsController");
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("Command", CONTACTS_COMMANDS.SAVE_CONTACT);
		request.put("data", data);
		
		// try to send the save request to the server
		try 
		{
			this.client.sendMsg(request);
		} 
		catch (ClinSysException e) 
		{
			Dialog.inputBox("Could not send the save request. Please try " +
			 				"again later or ask a developer", "Sending request", 
			 				Dialog.WARNING_MESSAGE);
		}
		
		// and close the view
		this.clientController.getMainView().popView();
	}

	
	@Override
	public void afterCreated() 
	{
		// TODO Auto-generated method stub
		
	}
}
