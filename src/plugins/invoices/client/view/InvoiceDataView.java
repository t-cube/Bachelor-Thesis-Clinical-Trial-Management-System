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

package plugins.invoices.client.view;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import plugins.invoices.INVOICES_COMMANDS;
import plugins.invoices.client.InvoicesController;
import plugins.invoices.client.listener.InvoiceDataViewActionListener;

import client.view.InnerView;

/**
 * @author torstend
 *
 */
public class InvoiceDataView extends InnerView {
	private static final long serialVersionUID = 2050558536182115693L;
	private JButton btnImport;
	private JButton btnSetToPaid;
	private JButton btnAssignContact;
	private JButton btnAssignProject;
	private JScrollPane scrollPane;
	private JTable tblInvoices;
	private Container content;
	private InvoicesController ic;
	
	public InvoiceDataView(InvoicesController ic) {
		super("Invoice data", "InvoiceDataView");
		this.ic = ic;
		setUp();
	}
	
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the btnImport
	 */
	public JButton getBtnImport() {
		return btnImport;
	}

	/**
	 * @return the btnSetToPaid
	 */
	public JButton getBtnSetToPaid() {
		return btnSetToPaid;
	}

	/**
	 * @return the tblInvoices
	 */
	public JTable getTblInvoices() {
		return tblInvoices;
	}

	/**
	 * @return the contentPane
	 */
	public Container getContent() {
		return content;
	}

	private void setUp(){
		SpringLayout l = new SpringLayout();
		InvoiceDataViewActionListener al = new InvoiceDataViewActionListener(this.ic);
		
		this.content = this.getContentPane();
		
		this.btnSetToPaid = new JButton("Set selected invoice to paid");
		this.btnImport = new JButton("Import new invoice data");
		this.btnAssignContact = new JButton("Assign a Contact");
		this.btnAssignProject = new JButton("Assign a Project");
		
		this.btnImport.addActionListener(al);
		this.btnAssignContact.addActionListener(al);
		this.btnAssignProject.addActionListener(al);
		this.btnImport.setActionCommand(INVOICES_COMMANDS.IMPORT_INVOICES.get());
		this.btnAssignContact.setActionCommand(INVOICES_COMMANDS.ASSIGN_CONTACT.get());
		this.btnAssignProject.setActionCommand(INVOICES_COMMANDS.ASSIGN_PROJECT.get());
		
		
		this.tblInvoices = new JTable();
		DefaultTableModel dtm = new DefaultTableModel(new Object[0][7], new String[]{"ID", "Invoice Nr", "Sponsor", "Contact", "Due Date", "Amount", "Dunning Level"});
		this.tblInvoices.setModel(dtm);
		
		this.scrollPane = new JScrollPane(this.tblInvoices);

		l.putConstraint(SpringLayout.NORTH, this.btnImport, 5, SpringLayout.NORTH, this.content);
		l.putConstraint(SpringLayout.NORTH, this.btnSetToPaid, 5, SpringLayout.NORTH, this.content);
		l.putConstraint(SpringLayout.NORTH, this.btnAssignContact, 5, SpringLayout.NORTH, this.content);
		l.putConstraint(SpringLayout.NORTH, this.btnAssignProject, 5, SpringLayout.NORTH, this.content);
		
		l.putConstraint(SpringLayout.WEST, this.btnImport, 5, SpringLayout.WEST, this.content);
		l.putConstraint(SpringLayout.WEST, this.btnSetToPaid, 50, SpringLayout.EAST, this.btnImport);
		l.putConstraint(SpringLayout.WEST, this.btnAssignContact, 50, SpringLayout.EAST, this.btnSetToPaid);
		l.putConstraint(SpringLayout.WEST, this.btnAssignProject, 50, SpringLayout.EAST, this.btnAssignContact);

		l.putConstraint(SpringLayout.NORTH, this.scrollPane, 5, SpringLayout.SOUTH, this.btnImport);
		l.putConstraint(SpringLayout.WEST, this.scrollPane, 5, SpringLayout.WEST, this.content);
		l.putConstraint(SpringLayout.EAST, this.scrollPane, -5, SpringLayout.EAST, this.content);
		l.putConstraint(SpringLayout.SOUTH, this.scrollPane, -5, SpringLayout.SOUTH, this.content);
		
		this.content.setLayout(l);

		this.content.add(btnImport);
		this.content.add(btnSetToPaid);
		this.content.add(btnAssignContact);
		this.content.add(btnAssignProject);
		this.content.add(this.scrollPane);
	}
}
