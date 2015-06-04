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

package plugins.invoices.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import plugins.invoices.INVOICES_COMMANDS;
import plugins.invoices.INVOICES_TABLES;
import server.controller.Controller;
import server.model.SQLCreator;
import server.model.enumerations.RIGHTS;
import server.model.enumerations.SQLITEMTYPE;
import server.model.enumerations.SQLWORD;
import server.model.enumerations.STATEMENTTYPE;
import shared.json.JSONArray;
import shared.json.JSONException;
import shared.json.JSONObject;
import shared.network.MESSAGE_TYPES;
import shared.network.RECIPIENT;
import shared.util.ClinSysException;
import shared.util.RecordSet;

/**
 * @author torstend
 *
 */
public class InvoicesController extends Controller {
	public InvoicesController(){
		super("InvoicesController");
		this.protocol = new InvoicesProtocol();
	}
	
	public void setInvoiceToPaid(JSONObject data) throws ClinSysException{
		SQLCreator sql = new SQLCreator();
		JSONArray ids = data.getJSONArray("ids");
		String userName = data.getString("userName");
		
		sql.setStatementType(STATEMENTTYPE.DELETE);
		sql.addItem(INVOICES_TABLES.TBL_INVOICE.get(), SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.WHERE);
		for (int i=0; i<ids.length(); i++){
			if (i>0){
				sql.addItem(SQLWORD.OR);
			}
			sql.addFieldItemPair("id", "=", ids.getInt(i), SQLITEMTYPE.INT);
		}
		
		this.model.executeSQL(sql, userName,  INVOICES_COMMANDS.IMPORT_INVOICES.get(), this.getPluginName(), RIGHTS.DELETE.get());
	}
	
	public void getInvoiceData(JSONObject request){
		SQLCreator sql = new SQLCreator();
		JSONObject answer = new JSONObject();
		RecordSet rs = null;
		String userName = request.getString("UserName");
		
		try {
			sql.setStatementType(STATEMENTTYPE.SELECT);
			sql.addItem(new String[]{"id", "nr", "create_date", "due_date", "amount", "paid_amount", "currency"}, SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem(INVOICES_TABLES.TBL_INVOICE.get(), SQLITEMTYPE.TABLE);
			sql.addItem(SQLWORD.WHERE);
			sql.addItem(SQLWORD.NOT);
			sql.addFieldItemPair("paid_amount", "=", "amount", SQLITEMTYPE.FIELD);
		} catch (ClinSysException e) {
			e.printStackTrace();
		}
		
		answer.put("Sender", this.getPluginName());
		answer.put("Receiver", request.getString("Sender"));
		answer.put("Type", MESSAGE_TYPES.ANSWER);
		answer.put("Command", request.get("Command"));
		
		try {
			rs = this.model.executeSQL(sql, userName, INVOICES_COMMANDS.SHOW_INVOICES.get(), this.getPluginName(), RIGHTS.READ.get());
			answer.put("data", rs.toJSON());
			this.server.sendTo(RECIPIENT.CLIENT, userName, answer);			
		} catch (ClinSysException e) {
			e.printStackTrace();
		}
	}
	
	public void importInvoiceData(JSONObject request) throws ClinSysException{
		SQLCreator sql = new SQLCreator();
		JSONObject data = request.getJSONObject("data");
		JSONArray nr = data.getJSONObject("nr").getJSONArray("Values");
		JSONArray due_date = data.getJSONObject("due_date").getJSONArray("Values");
		JSONArray create_date = data.getJSONObject("create_date").getJSONArray("Values");
		JSONArray amount = data.getJSONObject("amount").getJSONArray("Values");
		JSONArray paid_amount = data.getJSONObject("paid_amount").getJSONArray("Values");
		JSONArray currency = data.getJSONObject("currency").getJSONArray("Values");
		JSONArray updated_indexes = new JSONArray();
		JSONArray new_indexes = new JSONArray();
		JSONArray paid_ids = new JSONArray();
		JSONArray result_ids = null;
		ArrayList<?> result_nrs = null;
		RecordSet result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z y", Locale.US);
		String userName = request.getString("UserName");
		
		sql.setStatementType(STATEMENTTYPE.SELECT);
		sql.addItem(new String[]{"id","nr"}, SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.FROM);
		sql.addItem(INVOICES_TABLES.TBL_INVOICE.get(), SQLITEMTYPE.TABLE);
		
		result = this.model.executeSQL(sql, userName, INVOICES_COMMANDS.IMPORT_INVOICES.get(), this.getPluginName(), RIGHTS.READ.get());
		
		result_ids = result.toJSON().getJSONObject("id").getJSONArray("Values");
		result_nrs = result.getValues("nr");
		
		paid_ids = result_ids;
		
		for (int i = 0; i<nr.length(); i++){			
			if (result_nrs.contains(nr.get(i))){
				updated_indexes.put(i);
				paid_ids.remove(i);
			}else{
				new_indexes.put(i);
			}
		}		
		
		sql.reset();
		sql.setStatementType(STATEMENTTYPE.DELETE);
		sql.addItem(INVOICES_TABLES.TBL_INVOICE.get(), SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.WHERE);
		for (int i=0; i<paid_ids.length(); i++){
			if (i>0){
				sql.addItem(SQLWORD.OR);
			}
			sql.addFieldItemPair("id", "=", paid_ids.getInt(i), SQLITEMTYPE.INT);
		}
		
		this.model.executeSQL(sql, userName,  INVOICES_COMMANDS.IMPORT_INVOICES.get(), this.getPluginName(), RIGHTS.DELETE.get());
		
		for (int i=0; i<updated_indexes.length(); i++){
			sql.reset();
			sql.setStatementType(STATEMENTTYPE.UPDATE);
			sql.addItem(INVOICES_TABLES.TBL_INVOICE.get(), SQLITEMTYPE.TABLE);
			sql.addItem(SQLWORD.SET);
				try {
					sql.addFieldItemPair("create_date", "=", sdf.parse((String) create_date.get((Integer) updated_indexes.get(i))), SQLITEMTYPE.DATE);
					sql.addFieldItemPair("due_date", "=", sdf.parse((String) due_date.get((Integer) updated_indexes.get(i))), SQLITEMTYPE.DATE);
				} catch (JSONException | ParseException e) {
					e.printStackTrace();
				}
				if (amount.isNull((Integer) updated_indexes.get(i))){
					sql.addFieldItemPair("amount", "=", 0.0, SQLITEMTYPE.DOUBLE);
				}else{
					sql.addFieldItemPair("amount", "=", amount.getDouble((Integer) updated_indexes.get(i)), SQLITEMTYPE.DOUBLE);
				}
				if (paid_amount.isNull((Integer) updated_indexes.get(i))){
					sql.addFieldItemPair("paid_amount", "=", 0.0, SQLITEMTYPE.DOUBLE);
				}else{
					sql.addFieldItemPair("paid_amount", "=", paid_amount.getDouble((Integer) updated_indexes.get(i)), SQLITEMTYPE.DOUBLE);
				}				
				sql.addFieldItemPair("currency", "=", currency.getString((Integer) updated_indexes.get(i)), SQLITEMTYPE.TEXT);
			sql.addItem(SQLWORD.WHERE);
				sql.addFieldItemPair("id", "=", result_ids.get((Integer) updated_indexes.get(i)), SQLITEMTYPE.INT);
				
			this.model.executeSQL(sql, userName,  INVOICES_COMMANDS.IMPORT_INVOICES.get(), this.getPluginName(), RIGHTS.EDIT.get());
		}
		
		sql.reset();
		sql.setStatementType(STATEMENTTYPE.INSERT);
		sql.addItem(INVOICES_TABLES.TBL_INVOICE.get(), SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.OPENINGBRACKET);
		/// muss noch currency und paid amount hin wenns den gibt "!!! und das create_date
			sql.addItem(new String[]{"nr","create_date", "due_date", "currency", "amount", "paid_amount"}, SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.CLOSINGBRACKET);
		sql.addItem(SQLWORD.VALUES);
		for (int i=0; i<new_indexes.length(); i++){
			if (i>0){
				sql.addItem(SQLWORD.COMMA);
			}
			sql.addItem(SQLWORD.OPENINGBRACKET);
				sql.addItem(nr.getString((Integer) new_indexes.get(i)), SQLITEMTYPE.TEXT);				
				try {
					sql.addItem(SQLWORD.COMMA);
					sql.addItem(sdf.parse((String) create_date.get((Integer) new_indexes.get(i))), SQLITEMTYPE.DATE);
					sql.addItem(SQLWORD.COMMA);
					sql.addItem(sdf.parse((String) due_date.get((Integer) new_indexes.get(i))), SQLITEMTYPE.DATE);
				} catch (JSONException | ParseException e) {
					e.printStackTrace();
				}
				sql.addItem(SQLWORD.COMMA);
				sql.addItem(currency.getString((Integer) new_indexes.get(i)), SQLITEMTYPE.TEXT);
				sql.addItem(SQLWORD.COMMA);
				if (amount.isNull((Integer) new_indexes.get(i))){
					sql.addItem(0.0, SQLITEMTYPE.DOUBLE);
				}else{
					sql.addItem(amount.getDouble((Integer) new_indexes.get(i)), SQLITEMTYPE.DOUBLE);
				}
				sql.addItem(SQLWORD.COMMA);
				if (paid_amount.isNull((Integer) new_indexes.get(i))){
					sql.addItem(0.0, SQLITEMTYPE.DOUBLE);
				}else{
					sql.addItem(paid_amount.getDouble((Integer) new_indexes.get(i)), SQLITEMTYPE.DOUBLE);
				}				
			sql.addItem(SQLWORD.CLOSINGBRACKET);
		}
		
		this.model.executeSQL(sql, userName,  INVOICES_COMMANDS.IMPORT_INVOICES.get(), this.getPluginName(), RIGHTS.CREATE.get());		
	}
	
	public void assignContact(JSONObject request){
		SQLCreator sql = new SQLCreator();
		String userName = request.getString("UserName");
		JSONObject data = request.getJSONObject("data");
		JSONArray ids = data.getJSONArray("id");
		int contact_id = data.getInt("contact_id");
		
		if (ids.length()>0){
			try {
				sql.setStatementType(STATEMENTTYPE.UPDATE);
				sql.addItem(INVOICES_TABLES.TBL_INVOICE.get(), SQLITEMTYPE.TABLE);
				sql.addItem(SQLWORD.SET);
				sql.addFieldItemPair("contact_id", "=", contact_id, SQLITEMTYPE.INT);
				sql.addItem(SQLWORD.WHERE);
				for (int i=0; i<ids.length(); i++){
					if (i>0){
						sql.addItem(SQLWORD.OR);
					}
					sql.addFieldItemPair("id", "=", ids.get(i), SQLITEMTYPE.INT);
				}
			} catch (JSONException | ClinSysException e) {
				e.printStackTrace();
			}
			
			try {
				this.model.executeSQL(sql, userName, INVOICES_COMMANDS.ASSIGN_CONTACT.get(), this.getPluginName(), RIGHTS.EDIT.get());
			} catch (ClinSysException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void afterCreated() {
		// TODO Auto-generated method stub
		
	}
}
