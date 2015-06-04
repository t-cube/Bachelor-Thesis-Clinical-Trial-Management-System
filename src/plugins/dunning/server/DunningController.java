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

package plugins.dunning.server;


import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Date;

import javax.print.SimpleDoc;

import org.apache.xmlgraphics.util.DoubleFormatUtil;

import plugins.contacts.CONTACTS_TABLES;
import plugins.dunning.DUNNING_COMMANDS;
import plugins.dunning.DUNNING_TABLES;
import plugins.invoices.INVOICES_TABLES;
import server.controller.Controller;
import server.model.SQLCreator;
import server.model.enumerations.RIGHTS;
import server.model.enumerations.SQLITEMTYPE;
import server.model.enumerations.SQLWORD;
import server.model.enumerations.STATEMENTTYPE;
import shared.json.JSONArray;
import shared.json.JSONObject;
import shared.network.MESSAGE_TYPES;
import shared.network.RECIPIENT;
import shared.util.ClinSysException;
import shared.util.RecordSet;

/**
 * @author torstend
 *
 */
public class DunningController extends Controller {

	public DunningController() {
		super("DunningController");
		this.protocol = new DunningProtocol();
	}
	
	private int getDunningLevel(String userName, int id, boolean increment) throws ClinSysException{
		SQLCreator sql = new SQLCreator();
		RecordSet rsDunning = null;
		int dunningLevel = 0;
		
		sql.reset();
		sql.setStatementType(STATEMENTTYPE.SELECT);
		sql.addItem("dunning_level",SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.FROM);
		sql.addItem(DUNNING_TABLES.TBL_DUNNING.get(), SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.WHERE);
		sql.addFieldItemPair("invoice_id", "=", id, SQLITEMTYPE.INT);
		
		rsDunning = this.model.executeSQL(sql, userName, DUNNING_COMMANDS.GENERATE_PAYMENT_REMINDER.get(), this.getPluginName(), RIGHTS.READ.get());
		
		if (rsDunning!=null) {
			if (rsDunning.getRecordCount()==1){
				dunningLevel = (Integer) rsDunning.getValues("dunning_level").get(0) + (increment?1:0);
			}
		}
		
		if (dunningLevel==0){
			dunningLevel=1;
			sql.reset();
			sql.setStatementType(STATEMENTTYPE.INSERT);
			sql.addItem(DUNNING_TABLES.TBL_DUNNING.get(), SQLITEMTYPE.TABLE);
			sql.addItem(SQLWORD.OPENINGBRACKET);
				sql.addItem(new String[]{"dunning_level", "invoice_id"}, SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.CLOSINGBRACKET);
			sql.addItem(SQLWORD.VALUES);
			sql.addItem(SQLWORD.OPENINGBRACKET);
				sql.addItem(new Integer[]{dunningLevel, id}, SQLITEMTYPE.INT);
			sql.addItem(SQLWORD.CLOSINGBRACKET);
			
			this.model.executeSQL(sql, userName, DUNNING_COMMANDS.GENERATE_PAYMENT_REMINDER.get(), this.getPluginName(), RIGHTS.CREATE.get());
		}else if(increment){
			sql.reset();
			sql.setStatementType(STATEMENTTYPE.UPDATE);
			sql.addItem(DUNNING_TABLES.TBL_DUNNING.get(), SQLITEMTYPE.TABLE);
			sql.addItem(SQLWORD.SET);
			sql.addFieldItemPair("dunning_level", "=", dunningLevel, SQLITEMTYPE.INT);
			sql.addItem(SQLWORD.WHERE);
			sql.addFieldItemPair("invoice_id", "=", id, SQLITEMTYPE.INT);
			
			this.model.executeSQL(sql, userName, DUNNING_COMMANDS.GENERATE_PAYMENT_REMINDER.get(), this.getPluginName(), RIGHTS.EDIT.get());
		}
		
		return dunningLevel;
	}

	public void saveInvoiceContact(JSONObject data) throws ClinSysException{
		SQLCreator sql = new SQLCreator();
		String userName = data.getString("userName");
		int id = -1;
		String sponsor = null;
		String firstname = null;
		String lastname = null;
		String address = null;
		int zip_code = -1;
		String place = null;
		String state = null;
		String country = null;
		int preferred_language = -1;
		String[] fields;
				
		if (data.has("id")){
			id = data.getInt("id");
		}
		
		if (data.has("state")){
			state = data.getString("state");
			fields = new String[9];
			fields[6] = "state";
		}else{
			fields = new String[8];
		}
		sponsor = data.getString("sponsor");
		firstname = data.getString("firstname");
		lastname = data.getString("lastname");
		address = data.getString("address");
		zip_code = data.getInt("zip_code");
		place = data.getString("place");
		country = data.getString("country");
		preferred_language = data.getInt("preferred_language");
		
		fields[0] = "sponsor";
		fields[1] = "firstname";
		fields[2] = "lastname";
		fields[3] = "address";
		fields[4] = "zip_code";
		fields[5] = "place";
		fields[7] = "country";
		fields[8] = "preferred_language";
		
		
		sql.setStatementType(STATEMENTTYPE.INSERT);
		sql.addItem("pi_dunning_tbl_invoice_contacts", SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.OPENINGBRACKET);
			sql.addItem(fields, SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.CLOSINGBRACKET);
		sql.addItem(SQLWORD.VALUES);
		sql.addItem(SQLWORD.OPENINGBRACKET);
			sql.addItem(sponsor, SQLITEMTYPE.TEXT);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(firstname, SQLITEMTYPE.TEXT);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(lastname, SQLITEMTYPE.TEXT);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(address, SQLITEMTYPE.TEXT);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(zip_code, SQLITEMTYPE.TEXT);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(place, SQLITEMTYPE.TEXT);
			sql.addItem(SQLWORD.COMMA);
			if (state!=null){
				sql.addItem(state, SQLITEMTYPE.TEXT);
				sql.addItem(SQLWORD.COMMA);
			}
			sql.addItem(country, SQLITEMTYPE.TEXT);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(preferred_language, SQLITEMTYPE.TEXT);
		sql.addItem(SQLWORD.CLOSINGBRACKET);
		
		if (id!=-1){
			sql.addItem(SQLWORD.WHERE);
			sql.addFieldItemPair("id", "=", id, SQLITEMTYPE.INT);
		}
		
		this.model.executeSQL(sql, userName, DUNNING_COMMANDS.SAVE_INVOICE_CONTACT.get(), this.getPluginName(), RIGHTS.CREATE.get()+RIGHTS.EDIT.get());
	}
	
	public void saveDunningText(JSONObject data) throws ClinSysException{
		SQLCreator sql = new SQLCreator();
		String userName = data.getString("userName");
		int id = -1;
		String dunning_text = null;
		String language_code = null;
		
		if (data.has("id")){
			id = data.getInt("id");
		}
		
		dunning_text = data.getString("dunning_text");
		language_code = data.getString("language_code");
		
		sql.setStatementType(STATEMENTTYPE.INSERT);
		sql.addItem("pi_dunning_tbl_dunning_texts", SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.OPENINGBRACKET);
			sql.addItem(new String[]{"dunning_text", "language_code"}, SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.CLOSINGBRACKET);
		sql.addItem(SQLWORD.VALUES);
		sql.addItem(SQLWORD.OPENINGBRACKET);
			sql.addItem(dunning_text, SQLITEMTYPE.TEXT);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(language_code, SQLITEMTYPE.TEXT);
	sql.addItem(SQLWORD.CLOSINGBRACKET);
	}
	
	public void generatePaymentReminder(JSONObject request) throws ClinSysException{
		SQLCreator sql = new SQLCreator();
		boolean incrementDunningLevel = request.getBoolean("incrementDunningLevel");
		JSONArray ids = request.getJSONArray("id");
		Object[] idArray = new Object[ids.length()];
		String userName = request.getString("UserName");
		int id;
		int cId;
		int iInvoice;
		int iContact;
		String dunningText;
		RecordSet rsInvoice = null;
		RecordSet rsContact = null;
		ArrayList<Integer> contact_ids = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Double amountToPay = null;
		JSONObject answer= new JSONObject();
		DecimalFormat df = new DecimalFormat("###,###,###,###,###.00");
		JSONArray data = new JSONArray();
		int dunningLevel=0;
		
		for (int k=0; k<ids.length(); k++){
			idArray[k] = ids.getInt(k);
		}
		
		// get the needed data
		// invoice
		sql.setStatementType(STATEMENTTYPE.SELECT);
		sql.addItem(new String[]{"id", "contact_id", "project_id", "nr", "create_date", "due_date", "amount", "paid_amount", "currency"}, SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.FROM);
		sql.addItem(INVOICES_TABLES.TBL_INVOICE.get(), SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.WHERE);
		sql.addItem("id",SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.IN);
		sql.addItem(SQLWORD.OPENINGBRACKET);
			sql.addItem(idArray, SQLITEMTYPE.INT);
		sql.addItem(SQLWORD.CLOSINGBRACKET);
		
		rsInvoice = this.model.executeSQL(sql, userName, DUNNING_COMMANDS.GENERATE_PAYMENT_REMINDER.get(), this.getPluginName(), RIGHTS.READ.get());
		
		// contact
		contact_ids = new ArrayList<Integer>();
		for (Object contact_id : rsInvoice.getValues("contact_id")){
			if (contact_id != null){
				contact_ids.add((Integer) contact_id);
			}
		}

		if (contact_ids.size()>0){
			sql.reset();
			sql.setStatementType(STATEMENTTYPE.SELECT);
			sql.addItem(new String[]{"id", "company", "firstname", "lastname", "address", "zip_code", "place"}, SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem(CONTACTS_TABLES.TBL_CONTACT.get(), SQLITEMTYPE.TABLE);
			sql.addItem(SQLWORD.WHERE);
			sql.addItem("id", SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.IN);
			sql.addItem(SQLWORD.OPENINGBRACKET);
			sql.addItem(contact_ids.toArray(), SQLITEMTYPE.INT);
			sql.addItem(SQLWORD.CLOSINGBRACKET);
			
			rsContact = this.model.executeSQL(sql, userName, DUNNING_COMMANDS.GENERATE_PAYMENT_REMINDER.get(), this.getPluginName(), RIGHTS.READ.get());
			
			
		}
			
		///////////////////////////////////////////
		// do here the stuff with creating the pdf etc.
		// group is impossible cause not unique with only the contact id
		////////////////////////////////////////////////
		
		File f = new File("template_PaymentReminder.fo");
		Scanner scan = null;
		
		
		for (int j=0; j<ids.length(); j++){
			id = ids.getInt(j);
			iInvoice = rsInvoice.getValues("id").indexOf(id);
			cId = (Integer) rsInvoice.getValues("contact_id").get(iInvoice);
			iContact = rsContact.getValues("id").indexOf(cId);

			// get dunning level for this invoice
			dunningLevel = getDunningLevel(userName, id, incrementDunningLevel);
			
			//dunningText = "Get it from the database!";
			dunningText = "";
			try {
				scan = new Scanner(f);
				scan.useDelimiter("\\Z");
				dunningText = scan.next();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			
			amountToPay = ((Double) rsInvoice.getValues("amount").get(iInvoice)) - ((Double) rsInvoice.getValues("paid_amount").get(iInvoice));
			
			dunningText = dunningText.replace("$company$", rsContact.getValues("company").get(iContact).toString());
			dunningText = dunningText.replace("$firstname$", rsContact.getValues("firstname").get(iContact).toString());
			dunningText = dunningText.replace("$lastname$", rsContact.getValues("lastname").get(iContact).toString());
			dunningText = dunningText.replace("$address$", rsContact.getValues("address").get(iContact).toString());
			dunningText = dunningText.replace("$zip_code$", rsContact.getValues("zip_code").get(iContact).toString());
			dunningText = dunningText.replace("$place$", rsContact.getValues("place").get(iContact).toString());

			dunningText = dunningText.replace("$date$", sdf.format(new java.util.Date()));
			dunningText = dunningText.replace("$invoice_nr$", rsInvoice.getValues("nr").get(iInvoice).toString());
//			dunningText = dunningText.replace("$debitor_nr$", fieldVals.getString("sponsor"));

			dunningText = dunningText.replace("$dunningLevel$", "" + dunningLevel); // change to incrementing one
			dunningText = dunningText.replace("$title$", "Sehr geehrte Damen und Herren"); // change to one stored in db

			dunningText = dunningText.replace("$invoice_date$", sdf.format((Date) rsInvoice.getValues("create_date").get(iInvoice)));
			dunningText = dunningText.replace("$invoice_nr$", rsInvoice.getValues("nr").get(iInvoice).toString());
			dunningText = dunningText.replace("$due_date$", sdf.format((Date) rsInvoice.getValues("due_date").get(iInvoice)));
			dunningText = dunningText.replace("$invoice_amount$", df.format(amountToPay) + " " + rsInvoice.getValues("currency").get(iInvoice).toString());
						
			dunningText = dunningText.replace("$employeeName$", userName); // must be changed to employee contact name
//			dunningText = dunningText.replace("$employeeFunction$", fieldVals.getString("sponsor")); // must be changed to employee contact function/ position
			
			
			if (incrementDunningLevel){
//				this.incrementDunningLevel(userName, id);
			}
			
			data.put(dunningText);
		}
		
		answer.put("Receiver", request.getString("Sender"));
		answer.put("Command", request.getString("Command"));
		answer.put("Type", MESSAGE_TYPES.ANSWER);
		answer.put("Sender", this.getPluginName());
		answer.put ("data", data);
		
		this.server.sendTo(RECIPIENT.CLIENT, userName, answer);
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws ClinSysException
	 */
	public JSONObject serveData(JSONObject request) throws ClinSysException{
		RecordSet result = null;
		String userName = request.getString("userName");
		SQLCreator sql = new SQLCreator();
		JSONObject answer = new JSONObject();
		int requestedData = request.getInt("requestedData");
		
		sql.setStatementType(STATEMENTTYPE.SELECT);
		switch(requestedData){
		case 1:
			sql.addItem(new String[]{"ID","invoice_nr", "invoice_date","sponsor", "firstname", "lastname", "due_date", "invoice_amount", "invoice_currency", "dunning_level"}, SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem("pi_dunning_view_invoice_data", SQLITEMTYPE.TABLE);
			break;
		case 2:
			sql.addItem(new String[]{"ID", "sponsor", "contact_lastname", "contact_firstname", "address", "zip_code", "place", "state", "country", "language_code"}, SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem("pi_dunning_view_invoice_contacts", SQLITEMTYPE.TABLE);
			break;
		case 3:
			sql.addItem(new String[]{"ID","dunning_text", "language_code"}, SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem("pi_dunning_tbl_dunning_texts", SQLITEMTYPE.TABLE);
			break;
		}
		
		result = this.model.executeSQL(sql, userName, DUNNING_COMMANDS.SERVE_DATA.get(), this.getPluginName(), RIGHTS.READ.get());
		
		if (result!=null){
			answer.put("result", true);
			answer.put("data", result.toJSON());
		}else{
		}
		
		return answer;
	}

	@Override
	public void afterCreated() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	


}
