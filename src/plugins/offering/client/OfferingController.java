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

package plugins.offering.client;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.table.DefaultTableModel;

import plugins.offering.OFFERING_FUNCTIONS;
import plugins.offering.client.listener.OfferingMenuListener;
import plugins.offering.client.view.CreateOfferingView;
import plugins.worktimetracking.WORKTIME_FUNCTIONS;
import shared.json.JSONArray;
import shared.json.JSONObject;
import shared.network.MESSAGE_TYPES;
import shared.util.ClinSysException;
import shared.util.RecordSet;
import client.controller.Controller;

/**
 * @author Torsten
 *
 */
public class OfferingController extends Controller {
	private RecordSet activities;
	private RecordSet studies;
	private RecordSet cost;
	
	public OfferingController(){
		super("OfferingController");
		this.setProtocol(new OfferingProtocol());
		this.views.put("CreateOfferingView", new CreateOfferingView(this));
		createMenus();
	}
	
	private void setActivities(JSONObject activities) throws ClinSysException{
		this.activities = new RecordSet(activities);
	}
	
	private void setStudies(JSONObject studies) throws ClinSysException{
		this.studies = new RecordSet(studies);
	}
	
	private void setCost(JSONObject cost) throws ClinSysException{
		this.cost = new RecordSet(cost);
	}
	
	private void createMenus(){
		OfferingMenuListener ofml = new OfferingMenuListener(this);
		JMenu menu = new JMenu("Offering");
		menu.setName("Offering");
		JMenuItem miAddWorktime = new JMenuItem("Create an offer");
		miAddWorktime.setActionCommand(OFFERING_FUNCTIONS.CREATE_OFFERING.get());
		miAddWorktime.addActionListener(ofml);
		
		menu.add(miAddWorktime);
		
		this.menus.put(menu.getName(), menu);
	}
	
	public void showCreateOfferingView() throws ClinSysException{
		JSONObject request = new JSONObject();	
		JSONObject requestData = new JSONObject();
		
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("View", "CreateOfferingView");
		request.put("Receiver", "WorktimeTrackingController");
		request.put("Sender", this.getPluginName());
		request.put("Command", WORKTIME_FUNCTIONS.GET_STUDIES);
		requestData.put("ExecutingUser", this.getClientController().getUserName());
		request.put("RequestData", requestData);
		this.client.sendMsg(request);		

		request.put("Receiver", this.pluginName);
		request.put("Command", OFFERING_FUNCTIONS.GET_ACTIVITIES);
		this.client.sendMsg(request);
		
		this.getClientController().getMainView().pushView(this.views.get("CreateOfferingView"));
	}
	
	public void addStudiesToComponent(JSONObject answer) throws ClinSysException{
		CreateOfferingView cov = null;
		JSONObject data = answer.getJSONObject("Data");
		JSONObject field = data.getJSONObject("name");
		JSONArray values = field.getJSONArray("Values");
		
		this.setStudies(data);
				
		cov = (CreateOfferingView) this.views.get("CreateOfferingView");	
		cov.getCbTemplate().removeAllItems();		
		for (int i=0; i<values.length(); i++){
			cov.getCbTemplate().addItem(values.get(i).toString());
		}		
	}
	
	public void addActivitiesToComponent(JSONObject answer) throws ClinSysException{
		CreateOfferingView cov = null;
		JSONObject data = answer.getJSONObject("Data");
		JSONObject field = data.getJSONObject("name");
		JSONArray values = field.getJSONArray("Values");
		
		this.setActivities(data);
				
		cov = (CreateOfferingView) this.views.get("CreateOfferingView");	
		cov.getCbActivities().removeAllItems();		
		for (int i=0; i<values.length(); i++){
			cov.getCbActivities().addItem(values.get(i).toString());
		}	
	}
		
	public void addCostToComponent(JSONObject answer) throws ClinSysException{
		JSONObject data = answer.getJSONObject("Data");
		CreateOfferingView cov = (CreateOfferingView) this.views.get("CreateOfferingView");
		DefaultTableModel model = (DefaultTableModel) cov.getTblCosts().getModel();
		Object[] row = new Object[2];
		ArrayList<?> actIdValues = this.activities.getValues("id");
		ArrayList<?> actNameValues = this.activities.getValues("name");
		
		this.setCost(data);
		
		row[0] = actNameValues.get(actIdValues.indexOf(this.cost.getValues("activity_id").get(0)));
		row[1] = this.cost.getValues("cost").get(0);
				
		model.addRow(row);
	}
	
	public void addSelectedActivity() throws ClinSysException{
		JSONObject request = new JSONObject();	
		JSONObject requestData = new JSONObject();
		CreateOfferingView cov = (CreateOfferingView) this.views.get("CreateOfferingView");
		DefaultTableModel model = (DefaultTableModel) cov.getTblActivities().getModel();
		Object[] row = new Object[3];
		row[0] = this.activities.getValues("id").get(cov.getCbActivities().getSelectedIndex());
		row[1] = this.activities.getValues("name").get(cov.getCbActivities().getSelectedIndex());
		row[2] = this.activities.getValues("description").get(cov.getCbActivities().getSelectedIndex());
		
		model.addRow(row);
		
		
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("View", "CreateOfferingView");
		request.put("Receiver", this.getPluginName());
		request.put("Sender", this.getPluginName());
		request.put("Command", OFFERING_FUNCTIONS.GET_COST);
		requestData.put("ExecutingUser", this.getClientController().getUserName());
		requestData.put("StudyId", this.studies.getValues("id").get(cov.getCbTemplate().getSelectedIndex()));
		requestData.put("ActivityId", this.activities.getValues("id").get(cov.getCbActivities().getSelectedIndex()));
		request.put("RequestData", requestData);
		this.client.sendMsg(request);		
	}
	
	public void createOffering() throws ClinSysException{
		JSONObject request = new JSONObject();	
		JSONObject requestData = new JSONObject();
		RecordSet data = new RecordSet();
		CreateOfferingView cov = (CreateOfferingView) this.views.get("CreateOfferingView");
		DefaultTableModel model = (DefaultTableModel) cov.getTblCosts().getModel();
		HashMap<String, Object> rowValues;
		
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("View", "CreateOfferingView");
		request.put("Receiver", this.getPluginName());
		request.put("Sender", this.getPluginName());
		request.put("Command", OFFERING_FUNCTIONS.CREATE_OFFERING);
		requestData.put("ExecutingUser", this.getClientController().getUserName());
		
		data.addField("activity", Types.VARCHAR, new ArrayList<String>());
		data.addField("description", Types.VARCHAR, new ArrayList<String>());
		data.addField("cost", Types.DOUBLE, new ArrayList<Double>());
		
		for (int i=0; i<model.getRowCount(); i++){
			rowValues = new HashMap<String, Object>();
			
			rowValues.put("activity", model.getValueAt(i, 0));
			rowValues.put("description", this.activities.getValues("description").get(this.activities.getValues("name").indexOf(model.getValueAt(i, 0))));
			rowValues.put("cost", model.getValueAt(i, 1));
					
			data.addRow(data.getFields(), rowValues);
			
		}
		requestData.put("Data", data.toJSON());
		
		request.put("RequestData", requestData);
		this.client.sendMsg(request);			
	}
	
	public void createOfferingResult(JSONObject answer){
		
	}

	@Override
	public void afterCreated() {
		// TODO Auto-generated method stub
		
	}
}
