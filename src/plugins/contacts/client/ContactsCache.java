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

import shared.util.RecordSet;

/**
 * This class stores the data used for the contact plug-in, to cache it and to 
 * reduce the traffic between client and server.
 * The data should only be refreshed if the server notified the client that the 
 * data had changed.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class ContactsCache 
{
	
	/* Constants ----------------------------------------------------------------*/
	
	
	/* Variables ----------------------------------------------------------------*/	

	// Recordsets to store the data of the contacts plug-in.
	private RecordSet contacts = null;
	private RecordSet supplier = null;
	private RecordSet site = null;
	private RecordSet employee = null;
	private RecordSet patient = null;
	private RecordSet customer = null;
	private RecordSet sponsor = null;

	// Flag indicating if the data is up to date... maybe rename it in up2date.
	private boolean refresh = true;
	
	
	/* Constructor --------------------------------------------------------------*/
	
	
	/* Getter -------------------------------------------------------------------*/
	
	/**
	 * @return All contact data of all contacts. It's the biggest bunch of data
	 */
	public RecordSet getContacts() 
	{
		return contacts;
	}
	
	
	/**
	 * @return All IDs of contacts, that are tagged as supplier.
	 */
	public RecordSet getSupplier() 
	{
		return supplier;
	}
	
	
	/**
	 * @return All IDs of contacts, that are tagged as sites.
	 */
	public RecordSet getSite() 
	{
		return site;
	}
	
	
	/**
	 * @return All IDs of contacts, that are tagged as employees.
	 */
	public RecordSet getEmployee() 
	{
		return employee;
	}
	
	
	/**
	 * @return All IDs of contacts, that are tagged as patients.
	 */
	public RecordSet getPatient() 
	{
		return patient;
	}
	
	
	/**
	 * @return All IDs of contacts, that are tagged as customers.
	 */
	public RecordSet getCustomer() 
	{
		return customer;
	}
	
	
	/**
	 * @return All IDs of contacts, that are tagged as sponsors.
	 */
	public RecordSet getSponsor() 
	{
		return sponsor;
	}	
	
	
	/**
	 * @return Returns if the data was just refreshed and is up to date.
	 */
	public boolean isRefresh() 
	{
		return refresh;
	}

	
	/* Setter -------------------------------------------------------------------*/
		
	/**
	 * @param contacts RecordSet object containing all contact data to all 
	 * 				   contacts.
	 */
	public void setContacts(RecordSet contacts) 
	{
		this.contacts = contacts;
		this.refresh = false;
	}
	
	
	/**
	 * @param supplier All IDs of contacts tagged as supplier.
	 */
	public void setSupplier(RecordSet supplier) 
	{
		this.supplier = supplier;
	}
	
	
	/**
	 * @param site All IDs of contacts tagged as sites.
	 */
	public void setSite(RecordSet site) 
	{
		this.site = site;
	}
	
	
	/**
	 * @param employee All IDs of contacts tagged as employees.
	 */
	public void setEmployee(RecordSet employee) 
	{
		this.employee = employee;
	}
	
	
	/**
	 * @param patient All IDs of contacts tagged as patients.
	 */
	public void setPatient(RecordSet patient) 
	{
		this.patient = patient;
	}
	
	
	/**
	 * @param customer All IDs of contacts tagged as customer.
	 */
	public void setCustomer(RecordSet customer) 
	{
		this.customer = customer;
	}
	
	
	/**
	 * @param sponsor All IDs of contacts tagged as sponsor.
	 */
	public void setSponsor(RecordSet sponsor) 
	{
		this.sponsor = sponsor;
	}
	

	
	/* Private ------------------------------------------------------------------*/
	
	
	/* Public -------------------------------------------------------------------*/
	
	
}
