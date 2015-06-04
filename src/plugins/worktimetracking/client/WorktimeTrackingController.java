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

package plugins.worktimetracking.client;

import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import plugins.worktimetracking.WORKTIME_FUNCTIONS;
import plugins.worktimetracking.client.listener.AddWorktimeViewItemListener;
import plugins.worktimetracking.client.listener.EditDiscountViewTableModelListener;
import plugins.worktimetracking.client.listener.EditPriceViewTableModelListener;
import plugins.worktimetracking.client.listener.EditWorktimeViewTableModelListener;
import plugins.worktimetracking.client.listener.WorktimeMenuListener;
import plugins.worktimetracking.client.view.AddPriceView;
import plugins.worktimetracking.client.view.AddWorktimeView;
import plugins.worktimetracking.client.view.EditPriceView;
import plugins.worktimetracking.client.view.EditWorktimeView;
import shared.json.JSONArray;
import shared.json.JSONObject;
import shared.network.MESSAGE_TYPES;
import shared.util.ClinSysException;
import shared.util.RecordSet;
import client.controller.Controller;

/**
 * @author torstend
 *
 */
public class WorktimeTrackingController extends Controller {
	private RecordSet sponsors;
	private RecordSet studies;
	private RecordSet departments;
	private RecordSet activities;
	private RecordSet worktimes;
	private RecordSet employees;
	private RecordSet prices;
	private RecordSet discounts;
	

	public WorktimeTrackingController() {
		super("WorktimeTrackingController");
		this.protocol = new WorktimeTrackingProtocol();
		this.views.put("AddPriceView", new AddPriceView(this));
		this.views.put("AddWorktimeView", new AddWorktimeView(this));
		this.views.put("EditPriceView", new EditPriceView(this));
		this.views.put("EditWorktimeView", new EditWorktimeView(this));
		this.createMenus();
	}
	
	
	
	public RecordSet getSponsors() {
		return sponsors;
	}



	public void setSponsors(JSONObject sponsors) throws ClinSysException {
		this.sponsors = new RecordSet(sponsors);
	}



	public RecordSet getStudies() {
		return studies;
	}



	public void setStudies(JSONObject studies) throws ClinSysException {
		this.studies =  new RecordSet(studies);
	}



	public RecordSet getDepartments() {
		return departments;
	}



	public void setDepartments(JSONObject departments) throws ClinSysException {
		this.departments =  new RecordSet(departments);
	}



	public RecordSet getActivities() {
		return activities;
	}

	
	public RecordSet getEmployees(){
		return this.employees;
	}
	
	public void setEmployees(JSONObject employees) throws ClinSysException{
		this.employees = new RecordSet(employees);
	}


	public void setActivities(JSONObject activities) throws ClinSysException {
		this.activities =  new RecordSet(activities);
	}

	
	public void setWorktimes(JSONObject worktimes) throws ClinSysException{
		this.worktimes = new RecordSet(worktimes);
	}
	
	public RecordSet getPrices(){
		return this.prices;
	}
	
	public RecordSet getDiscounts(){
		return this.discounts;
	}
	
	public void setPrices(JSONObject prices) throws ClinSysException{
		this.prices = new RecordSet(prices);
	}
	
	public void setDiscounts(JSONObject discounts) throws ClinSysException{
		this.discounts = new RecordSet(discounts);
	}
	

	private void createMenus(){
		WorktimeMenuListener wml = new WorktimeMenuListener(this);
		JMenu menu = new JMenu("Worktime Tracking");
		menu.setName("WorktimeTracking");
		JMenuItem miAddWorktime = new JMenuItem("Track worktime");
		miAddWorktime.setActionCommand(WORKTIME_FUNCTIONS.ADD_WORKTIME.get());
		miAddWorktime.addActionListener(wml);
		
		menu.add(miAddWorktime);
		
		JMenuItem miEditWorktime = new JMenuItem("Edit worktime");
		miEditWorktime.setActionCommand(WORKTIME_FUNCTIONS.EDIT_WORKTIME.get());
		miEditWorktime.addActionListener(wml);
		
		menu.add(miEditWorktime);
		
		JMenuItem miAddPrice = new JMenuItem("Add price/discount");
		miAddPrice.setActionCommand(WORKTIME_FUNCTIONS.ADD_PRICE.get());
		miAddPrice.addActionListener(wml);
		
		menu.add(miAddPrice);
		
		JMenuItem miEditPrice = new JMenuItem("Edit price/discount");
		miEditPrice.setActionCommand(WORKTIME_FUNCTIONS.EDIT_PRICE.get());
		miEditPrice.addActionListener(wml);
		
		menu.add(miEditPrice);
		
		this.menus.put(menu.getName(), menu);
	}
	
	public void showAddWorktimeView() throws ClinSysException{
		JSONObject request = new JSONObject();	
		JSONObject requestData = new JSONObject();
		
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("View", "AddWorktimeView");
		request.put("Receiver", this.getPluginName());
		request.put("Sender", this.getPluginName());
		request.put("Command", WORKTIME_FUNCTIONS.GET_SPONSORS);
		requestData.put("ExecutingUser", this.getClientController().getUserName());
		request.put("RequestData", requestData);
		this.client.sendMsg(request);		

		request.put("Command", WORKTIME_FUNCTIONS.GET_STUDIES);
		this.client.sendMsg(request);
		
		request.put("Command", WORKTIME_FUNCTIONS.GET_DEPARTMENTS);
		this.client.sendMsg(request);
		
		this.getClientController().getMainView().pushView(this.views.get("AddWorktimeView"));
	}
	
	public void showEditWorktimeView() throws ClinSysException{
		JSONObject request = new JSONObject();
		JSONObject requestData = new JSONObject();
		
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("View", "EditWorktimeView");
		request.put("Receiver", this.getPluginName());
		request.put("Sender", this.getPluginName());
		
		request.put("Command", WORKTIME_FUNCTIONS.GET_USER_WORKTIME);
		
		requestData.put("ExecutingUser", this.getClientController().getUserName());
		requestData.put("UserName", this.getClientController().getUserName());
		
		request.put("RequestData", requestData);
		
		this.client.sendMsg(request);
		
		this.getClientController().getMainView().pushView(this.views.get("EditWorktimeView"));
	}
	
	public void showAddPriceView() throws ClinSysException{
		JSONObject request = new JSONObject();
		JSONObject requestData = new JSONObject();
		
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("View", "AddPriceView");
		request.put("Receiver", this.getPluginName());
		request.put("Sender", this.getPluginName());
		request.put("Command", WORKTIME_FUNCTIONS.GET_All_EMPLOYEES);
		
		requestData.put("ExecutingUser", this.getClientController().getUserName());
		
		request.put("RequestData", requestData);		
		this.client.sendMsg(request);
		
		request.put("Command", WORKTIME_FUNCTIONS.GET_STUDIES);
		this.client.sendMsg(request);
		
		this.getClientController().getMainView().pushView(this.views.get("AddPriceView"));
	}
	
	public void showEditPriceView() throws ClinSysException{
		JSONObject request = new JSONObject();
		JSONObject requestData = new JSONObject();
		JSONArray fields;
		
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("View", "EditPriceView");
		request.put("Receiver", this.getPluginName());
		request.put("Sender", this.getPluginName());
		
		requestData.put("ExecutingUser", this.getClientController().getUserName());
		requestData.put("UserName", this.getClientController().getUserName());
		
		
		request.put("Command", WORKTIME_FUNCTIONS.GET_ALL_PRICE);	
		fields = new JSONArray();
		fields.put("employee_id");
		fields.put("firstname");
		fields.put("lastname");
		fields.put("employment_type");
		fields.put("worktime");
		fields.put("price");
		requestData.put("Fields", fields);
		request.put("RequestData", requestData);
		this.client.sendMsg(request);
		
		request.put("Command", WORKTIME_FUNCTIONS.GET_ALL_DISCOUNT);	
		fields = new JSONArray();
		fields.put("employee_id");
		fields.put("firstname");
		fields.put("lastname");
		fields.put("employment_type");
		fields.put("worktime");
		fields.put("sponsor");
		fields.put("study_id");
		fields.put("study");
		fields.put("discount_price");
		requestData.put("Fields", fields);
		request.put("RequestData", requestData);
		this.client.sendMsg(request);
		
		this.getClientController().getMainView().pushView(this.views.get("EditPriceView"));
	}
	
	public void addSponsorsToComponent(JSONObject answer) throws ClinSysException{
		AddWorktimeView awt = null;
		JSONObject data = answer.getJSONObject("Data");
		JSONObject field = data.getJSONObject("name");;
		JSONArray values = field.getJSONArray("Values");
		
		this.setSponsors(data);
		
		if (answer.getString("View").equals("AddWorktimeView")){
			awt = (AddWorktimeView) this.views.get("AddWorktimeView");	
			awt.getSponsor().removeAllItems();		
			for (int i=0; i<values.length(); i++){
				awt.getSponsor().addItem(values.get(i).toString());
			}			
		}
	}
	
	public void addStudiesToComponent(JSONObject answer) throws ClinSysException{
		AddWorktimeView awt = null;
		AddPriceView apv = null;
		JSONObject data = answer.getJSONObject("Data");
		JSONObject field = data.getJSONObject("name");
		JSONArray values = field.getJSONArray("Values");
		
		this.setStudies(data);
		
		if (answer.getString("View").equals("AddWorktimeView")){
			awt = (AddWorktimeView) this.views.get("AddWorktimeView");	
			awt.getStudy().removeAllItems();		
			for (int i=0; i<values.length(); i++){
				awt.getStudy().addItem(values.get(i).toString());
			}			
		}else if(answer.getString("View").equals("AddPriceView")){
			apv = (AddPriceView) this.views.get("AddPriceView");
			apv.getStudy().removeAllItems();
			for (int i=0; i<values.length(); i++){
				apv.getStudy().addItem(values.get(i).toString());
			}
		}
	}
	
	public void addDepartmentsToComponent(JSONObject answer) throws ClinSysException{
		AddWorktimeView awt = null;
		JSONObject data = answer.getJSONObject("Data");
		JSONObject field = data.getJSONObject("name");
		JSONArray values = field.getJSONArray("Values");
		
		this.setDepartments(data);
		
		if (answer.getString("View").equals("AddWorktimeView")){
			awt = (AddWorktimeView) this.views.get("AddWorktimeView");	
			awt.getDepartment().removeAllItems();		
			for (int i=0; i<values.length(); i++){
				awt.getDepartment().addItem(values.get(i).toString());
			}			
		}
		
		awt.getDepartment().addItemListener(new AddWorktimeViewItemListener(this));
	}	
	
	public void addActivitiesToComponent(JSONObject answer) throws ClinSysException{
		AddWorktimeView awt = null;
		JSONObject data = answer.getJSONObject("Data");
		JSONObject field = data.getJSONObject("name");
		JSONArray values = field.getJSONArray("Values");
		
		this.setActivities(data);
		
		if (answer.getString("View").equals("AddWorktimeView")){
			awt = (AddWorktimeView) this.views.get("AddWorktimeView");
			awt.getActivity().removeAllItems();
			for (int i=0; i<values.length(); i++){
				awt.getActivity().addItem(values.get(i).toString());
			}			
		}
	}
	
	public void addEmployeesToComponent(JSONObject answer) throws ClinSysException{
		AddPriceView apv = null;
		JSONObject data = answer.getJSONObject("Data");
		JSONObject fieldFirstName = data.getJSONObject("firstname");
		JSONObject fieldLastName = data.getJSONObject("lastname");
		JSONArray valuesFirstName = fieldFirstName.getJSONArray("Values");
		JSONArray valuesLastName = fieldLastName.getJSONArray("Values");
		
		this.setEmployees(data);
		
		if (answer.getString("View").equals("AddPriceView")){
			apv = (AddPriceView) this.views.get("AddPriceView");
			apv.getEmployee().removeAllItems();
			for (int i=0; i<valuesFirstName.length(); i++){
				apv.getEmployee().addItem(valuesFirstName.get(i) + " " + valuesLastName.get(i));
			}			
		}
	}
	
	public void addWorktimeToComponent(JSONObject answer) throws ClinSysException{
		EditWorktimeView ewv = null;
		JSONObject data = answer.getJSONObject("Data");
		Object[][] table = null;
		ArrayList<String> fields = null;
		ArrayList<?> values = null;
		DefaultTableModel model;
		
		this.setWorktimes(data);
		
		fields = new ArrayList<String>();
		fields.add("id");
		fields.add("workday");
		fields.add("sponsor");
		fields.add("study");
		fields.add("department");
		fields.add("activity");
		fields.add("hours");
		table = new Object[this.worktimes.getRecordCount()][fields.size()];
		
		
		for (int i=0; i<fields.size(); i++){
			values = this.worktimes.getValues(fields.get(i));
			for (int j=0; j<this.worktimes.getRecordCount(); j++){
				table[j][i] = values.get(j);
			}
		}
		
		if (answer.getString("View").equals("EditWorktimeView")){
			ewv = (EditWorktimeView) this.views.get("EditWorktimeView");
			ewv.getTable().removeAll();

			model = new DefaultTableModel(table, fields.toArray());
			model.addTableModelListener(new EditWorktimeViewTableModelListener(this));
			ewv.getTable().setModel(model);
		}
	}
	
	
	public void addPricesToComponent(JSONObject answer) throws ClinSysException{
		EditPriceView epv = null;
		JSONObject data = answer.getJSONObject("Data");
		Object[][] table = null;
		ArrayList<String> fields = null;
		ArrayList<?> values = null;
		DefaultTableModel model;

		this.setPrices(data);
		
		fields = this.prices.getFields();
		table = new Object[this.prices.getRecordCount()][fields.size()];
		for (int i=0; i<fields.size(); i++){
			values = this.prices.getValues(fields.get(i));
			for (int j=0; j<this.prices.getRecordCount(); j++){
				table[j][i] = values.get(j);
			}
		}
		
		if (answer.getString("View").equals("EditPriceView")){
			epv = (EditPriceView) this.views.get("EditPriceView");
			epv.getPriceTable().removeAll();

			model = new DefaultTableModel(table, fields.toArray());
			model.addTableModelListener(new EditPriceViewTableModelListener(this));
			epv.getPriceTable().setModel(model);
		}
	}
	
	public void addDiscountsToComponent(JSONObject answer) throws ClinSysException{
		EditPriceView epv = null;
		JSONObject data = answer.getJSONObject("Data");
		Object[][] table = null;
		ArrayList<String> fields = null;
		ArrayList<?> values = null;
		DefaultTableModel model;

		this.setDiscounts(data);
		
		fields = this.discounts.getFields();
		table = new Object[this.discounts.getRecordCount()][fields.size()];
		for (int i=0; i<fields.size(); i++){
			values = this.discounts.getValues(fields.get(i));
			for (int j=0; j<this.discounts.getRecordCount(); j++){
				table[j][i] = values.get(j);
			}
		}
		
		if (answer.getString("View").equals("EditPriceView")){
			epv = (EditPriceView) this.views.get("EditPriceView");
			epv.getDiscountTable().removeAll();

			model = new DefaultTableModel(table, fields.toArray());
			model.addTableModelListener(new EditDiscountViewTableModelListener(this));
			epv.getDiscountTable().setModel(model);
		}
	}
	
	public void updateWorktime(int row) throws ClinSysException{
		JSONObject request = new JSONObject();
		JSONObject requestData = new JSONObject();
		EditWorktimeView ewv = (EditWorktimeView) this.getViews().get("EditWorktimeView");
		JTable table = ewv.getTable();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("View", "EditWorktimeView");
		request.put("Receiver", this.getPluginName());
		request.put("Sender", this.getPluginName());
		request.put("Command", WORKTIME_FUNCTIONS.EDIT_WORKTIME);
		
		requestData.put("ExecutingUser", this.getClientController().getUserName());
		requestData.put("WorktimeId", (Integer) this.worktimes.getValues("id").get(row));
		requestData.put("Hours", model.getValueAt(row, table.getColumn("hours").getModelIndex()));	
		
		request.put("RequestData", requestData);

		this.client.sendMsg(request);
	}
	
	public void deleteWorktime(int row) throws ClinSysException{
		JSONObject request = new JSONObject();
		JSONObject requestData = new JSONObject();
		int id = (Integer) this.worktimes.getValues("id").get(row);
		
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("View", "EditWorktimeView");
		request.put("Receiver", this.getPluginName());
		request.put("Sender", this.getPluginName());
		request.put("Command", WORKTIME_FUNCTIONS.DELETE_WORKTIME);
			
		requestData.put("ExecutingUser", this.getClientController().getUserName());
		requestData.put("WorktimeId", id);
		
		request.put("RequestData", requestData);
		
		this.client.sendMsg(request);
	}
	
	public void deleteWorktimeResult(JSONObject answer){
		JSONObject data = answer.getJSONObject("Data");
		int id = data.getInt("WorktimeId");
		int row = this.worktimes.getValues("id").indexOf(id);
		
		if (data.getBoolean("Result")&&row>-1){
			for (String fieldName: this.worktimes.getFields()){
				this.worktimes.getValues(fieldName).remove(row);
			}
		}
	}
	
	public void updatePrice(int row) throws ClinSysException{
		JSONObject request = new JSONObject();
		JSONObject requestData = new JSONObject();
		EditPriceView ewv = (EditPriceView) this.getViews().get("EditPriceView");
		JTable table = ewv.getPriceTable();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("View", "EditPriceView");
		request.put("Receiver", this.getPluginName());
		request.put("Sender", this.getPluginName());
		request.put("Command", WORKTIME_FUNCTIONS.EDIT_PRICE);
		
		requestData.put("ExecutingUser", this.getClientController().getUserName());
		requestData.put("EmployeeId", this.prices.getValues("employee_id").get(row));
		requestData.put("Price", model.getValueAt(row, table.getColumn("price").getModelIndex()));	
		
		request.put("RequestData", requestData);

		this.client.sendMsg(request);
	}
	
	public void deletePrice(int row) throws ClinSysException{
		JSONObject request = new JSONObject();
		JSONObject requestData = new JSONObject();
		int employee_id = (Integer) this.prices.getValues("employee_id").get(row);
		
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("View", "EditPriceView");
		request.put("Receiver", this.getPluginName());
		request.put("Sender", this.getPluginName());
		request.put("Command", WORKTIME_FUNCTIONS.DELETE_PRICE);
			
		requestData.put("ExecutingUser", this.getClientController().getUserName());
		requestData.put("EmployeeId", employee_id);
		
		request.put("RequestData", requestData);
		
		this.client.sendMsg(request);
	}
	
	public void updateDiscount(int row) throws ClinSysException{
		JSONObject request = new JSONObject();
		JSONObject requestData = new JSONObject();
		EditPriceView ewv = (EditPriceView) this.getViews().get("EditPriceView");
		JTable table = ewv.getDiscountTable();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("View", "EditPriceView");
		request.put("Receiver", this.getPluginName());
		request.put("Sender", this.getPluginName());
		request.put("Command", WORKTIME_FUNCTIONS.EDIT_DISCOUNT);
		
		requestData.put("ExecutingUser", this.getClientController().getUserName());
		requestData.put("EmployeeId", this.discounts.getValues("employee_id").get(row));
		requestData.put("StudyId", this.discounts.getValues("study_id").get(row));
		requestData.put("DiscountPrice", model.getValueAt(row, table.getColumn("discount_price").getModelIndex()));	
		
		request.put("RequestData", requestData);

		this.client.sendMsg(request);
	}
	
	public void deleteDiscount(int row) throws ClinSysException{
		JSONObject request = new JSONObject();
		JSONObject requestData = new JSONObject();
		int employee_id = (Integer) this.discounts.getValues("employee_id").get(row);
		int study_id = (Integer) this.discounts.getValues("study_id").get(row);
		
		request.put("Type", MESSAGE_TYPES.REQUEST);
		request.put("View", "EditPriceView");
		request.put("Receiver", this.getPluginName());
		request.put("Sender", this.getPluginName());
		request.put("Command", WORKTIME_FUNCTIONS.DELETE_DISCOUNT);
			
		requestData.put("ExecutingUser", this.getClientController().getUserName());
		requestData.put("EmployeeId", employee_id);
		requestData.put("StudyId", study_id);
		
		request.put("RequestData", requestData);
		
		this.client.sendMsg(request);
	}



	@Override
	public void afterCreated() {
		// TODO Auto-generated method stub
		
	}
}
