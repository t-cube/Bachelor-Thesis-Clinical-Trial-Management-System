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

package plugins.invoices.client;

import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import plugins.contacts.client.ContactsController;
import plugins.contacts.client.view.ContactsOverView;
import plugins.excel.client.ExcelController;
import plugins.invoices.INVOICES_COMMANDS;
import plugins.invoices.client.listener.InvoicesMenuListener;
import plugins.invoices.client.view.AssignContactView;
import plugins.invoices.client.view.InvoiceDataView;
import shared.json.JSONArray;
import shared.json.JSONObject;
import shared.network.MESSAGE_TYPES;
import shared.util.Debugger;
import shared.util.ClinSysException;
import shared.util.RecordSet;
import client.controller.Controller;

/**
 * @author torstend
 *
 */
public class InvoicesController extends Controller {
	private InvoicesCache cache = null;
	
	public InvoicesController(){
		super("InvoicesController");
		this.protocol = new InvoicesProtocol();
		this.cache = new InvoicesCache();
		this.views.put("InvoiceDataView", new InvoiceDataView(this));		
		this.createMenus();
	}
	
	public InvoicesCache getCache(){
		return this.cache;
	}
	
	private void createMenus(){
		JMenu invoiceMenu = new JMenu("Invoices");
		JMenuItem invoiceItem = new JMenuItem("Show Invoices-Overview");
		invoiceItem.setActionCommand(INVOICES_COMMANDS.SHOW_INVOICES.get());
		invoiceItem.addActionListener(new InvoicesMenuListener(this));
		invoiceMenu.add(invoiceItem);
		this.menus.put("Invoices", invoiceMenu);
	}
	
	private void requestInvoiceData(){
		JSONObject request = new JSONObject();
		request.put("UserName", this.clientController.getUserName());
		request.put("Sender", this.getPluginName());
		request.put("Receiver", "InvoicesController");
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("Command", INVOICES_COMMANDS.SHOW_INVOICES);
		
		try {
			this.client.sendMsg(request);
		} catch (ClinSysException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadInvoiceData(){
		Object[][] data = null;
		Object[] fields = null;
		DefaultTableModel dtm = null;
		JTable tbl = null;	
		RecordSet rs = null;		
		ArrayList<?> vals = null;		
		
		tbl = ((InvoiceDataView) this.views.get("InvoiceDataView")).getTblInvoices();
		rs = this.cache.getInvoices();
		fields = rs.getFields().toArray();
		data = new Object[rs.getRecordCount()][rs.getFields().size()];
		
		for (int i=0; i<rs.getFields().size(); i++){
			vals = rs.getValues(rs.getFieldName(i));
			for (int j=0; j<rs.getRecordCount(); j++){
				data[j][i] = vals.get(j);
			}			
		}
		
		dtm = new DefaultTableModel(data, fields);
		tbl.setModel(dtm);
	}
	
	public void refreshInvoiceData(JSONObject answer){
		JSONObject data = answer.getJSONObject("data");	
		
		try {
			this.cache.setInvoices(new RecordSet(data));
		} catch (ClinSysException e) {
			e.printStackTrace();
		}
		
		loadInvoiceData();
	}
	
	public void showInvoiceDataView(){
		if (cache.isRefresh()){
			this.requestInvoiceData();
		}
		
		loadInvoiceData();		
		this.getClientController().getMainView().pushView(this.views.get("InvoiceDataView"));
	}
	
	public void showAssignContactView(){
		if (!this.views.containsKey("AssignContactView")){
			if (this.clientController.getController().containsKey("ContactsController")){
				this.views.put("AssignContactView", new AssignContactView((ContactsController) this.clientController.getController("ContactsController"), 
																			this));
			}
		}
		if (this.views.containsKey("AssignContactView")){
			ContactsController cc = (ContactsController) this.clientController.getController("ContactsController");
			AssignContactView acv = (AssignContactView) this.views.get("AssignContactView");
			cc.loadContactOverViewData(acv);
			this.getClientController().getMainView().pushView(this.views.get("AssignContactView"));
		}
	}
	
	public void assignContact(){
		ContactsController cc = (ContactsController) this.clientController.getController("ContactsController");
		AssignContactView acv = (AssignContactView) this.views.get("AssignContactView");
		InvoiceDataView idv = (InvoiceDataView) this.views.get("InvoiceDataView");		
		int contactRowId = acv.getTable().getSelectedRow();
		int[] invoicesRowIds = idv.getTblInvoices().getSelectedRows(); 
		JSONObject request = new JSONObject();
		JSONObject data = new JSONObject();
		JSONArray ids = new JSONArray();
		
		for (int id: invoicesRowIds){
			ids.put(this.cache.getInvoices().getValues("id").get(id));
		}
		data.put("contact_id", cc.getCache().getContacts().getValues("id").get(contactRowId));
		data.put("id", ids);
		
		request.put("data", data);
		request.put("UserName", this.clientController.getUserName());
		request.put("Receiver", "InvoicesController");
		request.put("Sender", this.getPluginName());
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("Command", INVOICES_COMMANDS.ASSIGN_CONTACT);
		
		try {
			this.client.sendMsg(request);
		} catch (ClinSysException e) {
			e.printStackTrace();
		}
		
		this.clientController.getMainView().popView();
	}
	
	public void importInvoiceData(){
		RecordSet rs = null;
		JSONObject request = new JSONObject();
		JSONObject data = null;
		JSONArray fields = new JSONArray();
		ExcelController ec = (ExcelController) this.getClientController().getController("ExcelController");
		int rc = 0;
		
		fields.put("nr");
		fields.put("create_date");
		fields.put("due_date");
		fields.put("amount");
		fields.put("paid_amount");
		fields.put("currency");
		
		rs = ec.importExcelFile();
		
		rs.changeFieldName("OP-Nr.", "nr");
		rs.changeFieldName("Datum", "create_date");
		rs.changeFieldName("Fälligkeit", "due_date");
		rs.changeFieldName("EW Rechnung", "amount");
		rs.changeFieldName("EW Zahlung", "paid_amount");
		rs.changeFieldName("EW Wkz", "currency");
		
		rc = rs.getRecordCount();
		for (int i=0; i<rc; i++){
			if (rs.getValues("amount").get(i) == null){
				rs.remove(i);
				i--;
				rc = rs.getRecordCount();
			}else if((Double) rs.getValues("amount").get(i)<=0){
				rs.remove(i);
				i--;
				rc = rs.getRecordCount();
			}
		}
		
		data = rs.toJSON(fields);
		
		request.put("UserName", this.clientController.getUserName());
		request.put("Sender", this.getPluginName());
		request.put("Receiver", "InvoicesController");
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("Command", INVOICES_COMMANDS.IMPORT_INVOICES);
		request.put("data", data);
		
		try {
			this.client.sendMsg(request);
		} catch (ClinSysException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterCreated() {
		// TODO Auto-generated method stub
		
	}
}
