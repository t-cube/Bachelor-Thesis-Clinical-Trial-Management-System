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

package plugins.contacts.client.view;

import java.awt.Container;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import plugins.contacts.CONTACTS_COMMANDS;
import plugins.contacts.client.ContactsController;
import plugins.contacts.client.listener.ContactsActionListener;

import client.view.InnerView;

/**
 * The GUI frame showing an overview of all contacts.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class ContactsOverView extends InnerView 
{
	
	/* Constants ----------------------------------------------------------------*/
	
	// just an id for serializing
	private static final long serialVersionUID = 2393558847366615709L;
	
	/* Variables ----------------------------------------------------------------*/	
		
	// Buttons allowing to edit or create a contact
	private JButton btnEdit = null;
	private JButton btnNew = null;
	
	// Other GUI Elements
	private JScrollPane pane = null;
	private Container content = null;
	
	// Table showing the overview data
	private JTable table = null;
	
	// Client side controller of the Contacts plug-in
	private ContactsController cc;
	
	
	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * The constructor for the contacts overview, it initialize the super class,
	 * sets the required controller object and creates the controls of the view.
	 * 
	 * @param cc The client side controller object of the contacts plug-in.
	 */
	public ContactsOverView(ContactsController cc)
	{
		super("Contacts Overview", "ContactsOverView");
		this.cc = cc;
		setUp();
	}
	
	
	/* Getter -------------------------------------------------------------------*/
	
	/**
	 * Returns the button object, which is used to open the edit form for a 
	 * selected contact.
	 * 
	 * @return Button object used to show edit form for a selected contact.
	 */
	public JButton getBtnEdit() 
	{
		return btnEdit;
	}

	
	/**
	 * Returns button object, which is used to create a new contact.
	 * 
	 * @return Button object, which shows a view to create a new contact.
	 */
	public JButton getBtnNew() 
	{
		return btnNew;
	}

	
	/**
	 * Returns the JScrollPane object, used to create scroll bars for the table.
	 * 
	 * @return JScrollPane object handling scroll bars for the table containing
	 * 		   the overview of the contacts.
	 */
	public JScrollPane getPane() 
	{
		return pane;
	}

	
	/**
	 * Returns the JTable object containing/representing an overview of contacts.
	 * 
	 * @return JTable object containing overview of contacts.
	 */
	public JTable getTable() 
	{
		return table;
	}

	
	/**
	 * Returns the container of the InnerView, in which all other Controls and 
	 * visible view elements are stored.
	 * 
	 * @return Container object of the InnerView.
	 */
	public Container getContent() 
	{
		return content;
	}

	
	/**
	 * Returns the used controller object. Should be the client side controller of
	 * the contacts plug-in.
	 * 
	 * @return The ContactsController object, providing the client side 
	 * 		   functionality of the plug-in.
	 */
	public ContactsController getCc()
	{
		return cc;
	}
	
	
	/* Setter -------------------------------------------------------------------*/
		
	/**
	 * Sets a JButton object used for showing the edit view for a selected
	 * contact.
	 * 
	 * @param btnEdit JButton object, which should be used to show the edit view.
	 */
	public void setBtnEdit(JButton btnEdit) 
	{
		this.btnEdit = btnEdit;
	}

	
	/**
	 * Sets a JButton object used for showing the create view for a contact.
	 * 
	 * @param btnNew JButton object, which should be used to show the create view.
	 */
	public void setBtnNew(JButton btnNew) 
	{
		this.btnNew = btnNew;
	}

	
	/**
	 * Sets a JScrollPane object providing scroll bars for the table containing
	 * the contact overview.
	 * 
	 * @param pane JScrollPane object handling scroll bars for the contact table.
	 */
	public void setPane(JScrollPane pane) 
	{
		this.pane = pane;
	}

	
	/**
	 * Sets a JTable object storing/representing an overview of all contacts.
	 * 
	 * @param table JTable object giving an overview of all contacts.
	 */
	public void setTable(JTable table)
	{
		this.table = table;
	}

	
	/**
	 * Sets Container object used for the InnerView and handling the shown 
	 * controls.
	 * 
	 * @param content Container object handling the controls of an InnerView.
	 */
	public void setContent(Container content)
	{
		this.content = content;
	}

	
	/**
	 * Sets a ContactsController object, which should provide the functionality for
	 * the used actions.
	 * 
	 * @param cc A client side Controller object of the contacts plug-in.
	 */
	public void setCc(ContactsController cc) 
	{
		this.cc = cc;
	}
	
	
	/* Private ------------------------------------------------------------------*/	

	/**
	 * Sets up the view. That means creating action listeners, control objects, 
	 * data entry object, etc., setting them up in an layout and adding them to
	 * the Container object of the InnerView.
	 */
	private void setUp()
	{
		ContactsActionListener cal = new ContactsActionListener(this.cc);
		
		this.content = this.getContentPane();
		
		this.btnEdit = new JButton("Edit");
		this.btnEdit.setActionCommand(CONTACTS_COMMANDS.EDIT_CONTACT.get());
		this.btnEdit.addActionListener(cal);
		this.btnNew = new JButton("New");
		this.btnNew.setActionCommand(CONTACTS_COMMANDS.EDIT_CONTACT.get());
		this.btnNew.addActionListener(cal);
		
		this.table = new JTable(new DefaultTableModel(new Object[0][16], 
						new String[]{"Type", "Firstname", "Lastname","Company",
						"Position","Address","ZIP","Place","State","Country",
						"Email","Phone","Mobile","Website","User","Notes"}));
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.pane = new JScrollPane(this.table);
		
		this.content.setLayout(createLayout());
		this.content.add(btnNew);
		this.content.add(btnEdit);
		this.content.add(pane);
	}
	
	
	/**
	 * Sets up the controls in a hard coded layout using SpringLayout LayoutManager
	 * and returning the configured SpringLayout object.
	 * 
	 * @return SpringLayout object, configured to show the controls in the hard 
	 * 		   coded layout.
	 */
	private LayoutManager createLayout()
	{
		SpringLayout sl = new SpringLayout();

		sl.putConstraint(SpringLayout.NORTH, this.btnNew, 10, SpringLayout.NORTH, this.content);
		sl.putConstraint(SpringLayout.NORTH, this.btnEdit, 10, SpringLayout.NORTH, this.content);
		sl.putConstraint(SpringLayout.WEST, this.btnNew, 10, SpringLayout.WEST, this.content);
		sl.putConstraint(SpringLayout.WEST, this.btnEdit, 10, SpringLayout.EAST, this.btnNew);
		sl.putConstraint(SpringLayout.NORTH, this.pane, 10, SpringLayout.SOUTH, this.btnEdit);
		sl.putConstraint(SpringLayout.WEST, this.pane, 10, SpringLayout.WEST, this.content);
		sl.putConstraint(SpringLayout.SOUTH, this.pane, -10, SpringLayout.SOUTH, this.content);
		sl.putConstraint(SpringLayout.EAST, this.pane, -10, SpringLayout.EAST, this.content);
		
		return sl;
	}
	
	/* Public -------------------------------------------------------------------*/
	
	
}
