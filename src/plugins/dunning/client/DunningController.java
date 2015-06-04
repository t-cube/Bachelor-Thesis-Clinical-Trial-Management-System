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

package plugins.dunning.client;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SpringLayout;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.fop.apps.FOPException;

import plugins.dunning.DUNNING_COMMANDS;
import plugins.dunning.client.listener.DunningActionListener;
import plugins.dunning.client.listener.DunningMenuListener;
import plugins.dunning.client.view.DunningTextView;
import plugins.invoices.client.InvoicesController;
import shared.json.JSONArray;
import shared.json.JSONObject;
import shared.network.MESSAGE_TYPES;
import shared.util.Debugger;
import shared.util.Dialog;
import shared.util.ClinSysException;
import shared.util.PDFWriter;
import client.controller.Controller;
import client.view.InnerView;

/**
 * @author torstend
 *
 */
public class DunningController extends Controller {
	private InnerView invoicesView = null;
	private Controller invoicesController = null;

	public DunningController() {
		super("DunningController");
		this.protocol = new DunningProtocol();
		this.views.put("DunningTextView", new DunningTextView());
		
		createMenus();
	}
	
	private void addButtonsToInvoiceView(){
		Controller c = null;
		
		if ((c = this.clientController.getController("InvoicesController"))!=null){
			this.invoicesController = c; 
			this.invoicesView = c.getViews().get("InvoiceDataView");
			
			if (this.invoicesView!=null){
				Container content = this.invoicesView.getContentPane();
				SpringLayout l = (SpringLayout) content.getLayout();
				JButton btnPaymentReminder = new JButton("Create Payment Reminder");
				btnPaymentReminder.addActionListener(new DunningActionListener(this));
				btnPaymentReminder.setActionCommand(DUNNING_COMMANDS.GENERATE_PAYMENT_REMINDER.get());
				
				l.putConstraint(SpringLayout.NORTH, btnPaymentReminder, 5, SpringLayout.NORTH, content);
				l.putConstraint(SpringLayout.EAST, btnPaymentReminder, -5, SpringLayout.EAST, content);
				
				content.add(btnPaymentReminder);
			}
		}
	}
	
	private void createMenus(){
		DunningMenuListener dml = new DunningMenuListener(this);
		JMenu menu = new JMenu("Dunning");
		
		JMenuItem dunningTexts = new JMenuItem("Dunning Texts");
		dunningTexts.setActionCommand(DUNNING_COMMANDS.SHOW_DUNNING_TEXTS.get());
		dunningTexts.addActionListener(dml);
				
		menu.add(dunningTexts);
		
		this.menus.put("Dunning", menu);
	}
	
	
	
	public void showInvoiceContactView(){
		this.getClientController().getMainView().pushView(this.views.get("InvoiceContactView"));
	}
	
	public void showDunningTextView(){
		this.getClientController().getMainView().pushView(this.views.get("DunningTextView"));
	}

	@Override
	public void afterCreated() {
		addButtonsToInvoiceView();		
	}
	
	public void requestPaymentReminder(){
		JTable tbl = null;
		
		if (this.invoicesView != null){			
			for(Component c : this.invoicesView.getContentPane().getComponents()){
				if (c instanceof JScrollPane){
					for (Component cViewport : ((JScrollPane) c).getComponents()){
						if (cViewport instanceof JViewport){
							for (Component cTbl : ((JViewport) cViewport).getComponents()){
								if (cTbl instanceof JTable){
									tbl = (JTable) cTbl;		
									break;
								}													
							}
						}
						if (tbl!=null){
							break;
						}
					}
				}
				if (tbl!=null){
					break;
				}
			}
		}
		
		if (tbl != null && this.invoicesController != null){
			// durch allgemeinen Cache, der bereits im Abstract Controller/ clientseitigen Controller bekannt ist ersetzen!!
			InvoicesController ic = (InvoicesController) this.invoicesController;
			JSONArray ids = new JSONArray();
			JSONObject request = new JSONObject();
						
			for(int i : tbl.getSelectedRows()){
				ids.put(ic.getCache().getInvoices().getValues("id").get(i));
			}
			
			request.put("UserName", this.clientController.getUserName());
			request.put("Receiver", "DunningController");
			request.put("Sender", this.getPluginName());
			request.put("Type", MESSAGE_TYPES.REQUEST);
			request.put("Command", DUNNING_COMMANDS.GENERATE_PAYMENT_REMINDER);
			request.put("id", ids);
			request.put("incrementDunningLevel", Dialog.confirmationBox("Increment the dunning level?", 
													"Increment Dunning Level", Dialog.YES_NO_OPTION, 
													Dialog.QUESTION_MESSAGE)==Dialog.YES_OPTION);
			
			try {
				this.client.sendMsg(request);
			} catch (ClinSysException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void renderPaymentReminder(JSONObject answer){
		JSONArray data = answer.getJSONArray("data");
		try {
			PDFWriter pdf = new PDFWriter();
			// noch ein filechooser einbauen! und pdf doch als eigenes Plugin!
			for (int i = 0; i<data.length(); i++){
				pdf.writeXSLTStringToPDF(data.getString(i), "test" + i + ".pdf");
			}			
		} catch (TransformerConfigurationException | FOPException e) {
			e.printStackTrace();
		}
	}
}
