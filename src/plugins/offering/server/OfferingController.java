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

package plugins.offering.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Types;

import javax.xml.transform.TransformerConfigurationException;

import org.apache.fop.apps.FOPException;

import plugins.offering.OFFERING_FUNCTIONS;
import server.controller.Controller;
import server.model.SQLCreator;
import server.model.enumerations.CONCAT;
import server.model.enumerations.RIGHTS;
import server.model.enumerations.SQLITEMTYPE;
import server.model.enumerations.SQLWORD;
import server.model.enumerations.STATEMENTTYPE;
import shared.json.JSONArray;
import shared.json.JSONException;
import shared.json.JSONObject;
import shared.util.ClinSysException;
import shared.util.PDFWriter;
import shared.util.RecordSet;

/**
 * @author Torsten
 *
 */
public class OfferingController extends Controller {
	public OfferingController(){
		super("OfferingController");
		this.setProtocol(new OfferingProtocol());
	}
	
	public JSONObject getActivities(JSONObject request) throws ClinSysException{
		SQLCreator sqlAct = new SQLCreator();
		SQLCreator sqlDesc = new SQLCreator();
		SQLCreator sql = new SQLCreator();
		String executingUser = request.getString("ExecutingUser");
		JSONArray fields = null;
		RecordSet result = null;		
		
		sqlAct.setStatementType(STATEMENTTYPE.SELECT);
		sqlAct.addItem(new String[]{"id", "name"}, SQLITEMTYPE.FIELD);
		sqlAct.addItem(SQLWORD.FROM);
		sqlAct.addItem("worktime_activities", SQLITEMTYPE.TABLE);		
		sqlAct.addItem(SQLWORD.WHERE);
		sqlAct.addItem("group_id", SQLITEMTYPE.FIELD);
		sqlAct.addItem(SQLWORD.IS);
		sqlAct.addItem("NULL", SQLITEMTYPE.FIELD);

		sqlDesc.setStatementType(STATEMENTTYPE.SELECT);
		sqlDesc.addItem(new String[]{"id", "description"}, SQLITEMTYPE.FIELD);
		sqlDesc.addItem(SQLWORD.FROM);
		sqlDesc.addItem("offering_descriptions", SQLITEMTYPE.TABLE);
		
		sql.setLeftPart(sqlAct);
		sql.setRightPart(sqlDesc);
		sql.setConcat(CONCAT.LEFT, "Act", "Desc");
		sql.setOnFields(new String[]{"Act.id","Desc.id"});			
	
			
		result = this.model.executeSQL(sql, executingUser, OFFERING_FUNCTIONS.GET_ACTIVITIES.get(), this.pluginName, RIGHTS.READ.get());
		
		
		result.changeFieldName("Act.id", "id");
		result.changeFieldName("Act.name", "name");
		result.changeFieldName("Desc.description", "description");
		
		fields = new JSONArray();
		fields.put("id");
		fields.put("name");
		fields.put("description");
						
		return result.toJSON(fields);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getCost(JSONObject request) throws ClinSysException{
		SQLCreator sqlHours = new SQLCreator();
		SQLCreator sqlPrice = new SQLCreator();
		SQLCreator sqlDiscount = new SQLCreator();
		String executingUser = request.getString("ExecutingUser");
		RecordSet hours = null;	
		RecordSet price = null;	
		RecordSet discount = null;	
		RecordSet result = null;
		int study_id = request.getInt("StudyId");
		int activity_id = request.getInt("ActivityId");
		int employee_id;
		ArrayList<Integer> employees; 
		double cost;
		double hour;
		HashMap<String, Object> rowValues;
		
		sqlHours.setStatementType(STATEMENTTYPE.SELECT);
		sqlHours.addItem(new String[]{"employee_id", "SUM(hours) AS cost"}, SQLITEMTYPE.FIELD);
		sqlHours.addItem(SQLWORD.FROM);
		sqlHours.addItem("worktime_hours", SQLITEMTYPE.TABLE);		
		sqlHours.addItem(SQLWORD.WHERE);
		sqlHours.addFieldItemPair("study_id", "=", study_id, SQLITEMTYPE.INT);		
		sqlHours.addItem(SQLWORD.AND);
		sqlHours.addFieldItemPair("activity_id", "=", activity_id, SQLITEMTYPE.INT);		
		sqlHours.addItem(SQLWORD.GROUP);
		sqlHours.addItem(new String[]{"employee_id"}, SQLITEMTYPE.FIELD);
		
		sqlPrice.setStatementType(STATEMENTTYPE.SELECT);
		sqlPrice.addItem(new String[]{"employee_id", "price"}, SQLITEMTYPE.FIELD);
		sqlPrice.addItem(SQLWORD.FROM);
		sqlPrice.addItem("worktime_prices", SQLITEMTYPE.TABLE);
		
		sqlDiscount.setStatementType(STATEMENTTYPE.SELECT);
		sqlDiscount.addItem(new String[]{"employee_id", "study_id", "discount_price"}, SQLITEMTYPE.FIELD);
		sqlDiscount.addItem(SQLWORD.FROM);
		sqlDiscount.addItem("worktime_discounts", SQLITEMTYPE.TABLE);
		sqlDiscount.addItem(SQLWORD.WHERE);
		sqlDiscount.addFieldItemPair("study_id", "=", study_id, SQLITEMTYPE.INT);

		hours = this.model.executeSQL(sqlHours, executingUser, OFFERING_FUNCTIONS.GET_COST.get(), this.pluginName, RIGHTS.READ.get());
		price = this.model.executeSQL(sqlPrice, executingUser, OFFERING_FUNCTIONS.GET_COST.get(), this.pluginName, RIGHTS.READ.get());
		discount = this.model.executeSQL(sqlDiscount, executingUser, OFFERING_FUNCTIONS.GET_COST.get(), this.pluginName, RIGHTS.READ.get());

		result = new RecordSet();
		result.addField("activity_id", Types.INTEGER, new ArrayList<Integer>());
		result.addField("cost", Types.DOUBLE, new ArrayList<Double>());
		
		cost = 0;
		
		for (int i=0; i<hours.getRecordCount(); i++){
			hour = (Double) hours.getValues("cost").get(i);
			employee_id = (Integer) hours.getValues("employee_id").get(i);
			employees = (ArrayList<Integer>) discount.getValues("employee_id");
			if (employees.contains(employee_id)){
				hour *= (Double) discount.getValues("discount_price").get(employees.indexOf(employee_id));
			}else{
				employees = (ArrayList<Integer>) price.getValues("employee_id");
				if (employees.contains(employee_id)){
					hour *= (Double) price.getValues("price").get(employees.indexOf(employee_id));
				}else{
					hour = 0;
				}
			}
			
			cost += hour;
		}
		
		rowValues = new HashMap<String, Object>();
		rowValues.put("activity_id", activity_id);
		rowValues.put("cost", cost);
		
		result.addRow(result.getFields(), rowValues);
		
		return result.toJSON();
	}

	public boolean createOffering(JSONObject request) throws ClinSysException {
		RecordSet data = new RecordSet(request.getJSONObject("Data"));
		PDFWriter pdf;
		try {
			pdf = new PDFWriter();
			
			pdf.writeXSLTStringToPDF(data.toString(), "testAngebot.pdf");
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FOPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public void afterCreated() {
		// TODO Auto-generated method stub
		
	}
}
